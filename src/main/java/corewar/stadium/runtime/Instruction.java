package corewar.stadium.runtime;

import corewar.shared.InstructionType;
import corewar.stadium.StadiumShip;
import corewar.stadium.memory.FetchQueue;
import corewar.stadium.memory.InstructionParameters;
import corewar.stadium.runtime.Decoders.Decoder;
import corewar.stadium.runtime.Executors.Executor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum Instruction {

	NOP(InstructionType.NOP, Decoders::decodeNothingToDo, Executors::executeNop),
	CRASH(InstructionType.CRASH, Decoders::decodeNothingToDo, Executors::executeCrash),
	CHECK(InstructionType.CHECK, Decoders::decodeNothingToDo, Executors::executeCheck),
	FORK(InstructionType.FORK, Decoders::decodeNothingToDo, Executors::executeFork),

	MODE(InstructionType.MODE, Decoders::decodeMode, Executors::executeMode),
	LC(InstructionType.LC, Decoders::decodeLc, Executors::executeLoad),
	LL(InstructionType.LL, Decoders::decodeLl, Executors::executeLoad),
	SWP(InstructionType.SWP, Decoders::decodeSwp, Executors::executeSwp),
	LDB(InstructionType.LDB, Decoders::decodeLdbStb, Executors::executeLdb),
	STB(InstructionType.STB, Decoders::decodeLdbStb, Executors::executeStb),
	LDR(InstructionType.LDR, Decoders::decodeLdrStr, Executors::executeLdr),
	STR(InstructionType.STR, Decoders::decodeLdrStr, Executors::executeStr),
	STAT(InstructionType.STAT, Decoders::decodeStat, Executors::executeStat),

	MOV(InstructionType.MOV, Decoders::decodeRegReg, Executors::executeMov),
	ADD(InstructionType.ADD, Decoders::decodeRegReg, Executors::executeAdd),
	OR(InstructionType.OR, Decoders::decodeRegReg, Executors::executeOr),
	AND(InstructionType.AND, Decoders::decodeRegReg, Executors::executeAnd),
	XOR(InstructionType.XOR, Decoders::decodeRegReg, Executors::executeXor),
	SUB(InstructionType.SUB, Decoders::decodeRegReg, Executors::executeSub),
	NEG(InstructionType.NEG, Decoders::decodeRegReg, Executors::executeNeg),
	NOT(InstructionType.NOT, Decoders::decodeRegReg, Executors::executeNot),
	CMP(InstructionType.CMP, Decoders::decodeRegReg, Executors::executeCmp),

	ASR(InstructionType.ASR, Decoders::decodeRegN, Executors::executeAsr),
	ROL(InstructionType.ROL, Decoders::decodeRegN, Executors::executeRol),

	WRITE(InstructionType.WRITE, Decoders::decodeFReg, Executors::executeWrite),
	B(InstructionType.B, Decoders::decodeFReg, Executors::executeB),
	BZ(InstructionType.BZ, Decoders::decodeFReg, Executors::executeBz),
	BS(InstructionType.BS, Decoders::decodeFReg, Executors::executeBs),
	BNZ(InstructionType.BNZ, Decoders::decodeFReg, Executors::executeBnz),

	CMPI(InstructionType.CMPI, Decoders::decodeFRegN, Executors::executeCmpi),
	ADDI(InstructionType.ADDI, Decoders::decodeFRegN, Executors::executeAddi);

	private static final Map<Integer, Map<Byte, Instruction>> map = new HashMap<>();

	static {
		map.put(1, new HashMap<Byte, Instruction>() {{
			put((byte) 0x00, CRASH);
			put((byte) 0x01, NOP);
		}});
		map.put(2, new HashMap<Byte, Instruction>() {{
			put((byte) 0xfc, CHECK);
			put((byte) 0xfe, FORK);
		}});
		map.put(3, new HashMap<Byte, Instruction>() {{
			put((byte) 0xf7, B);
			put((byte) 0xf8, BZ);
			put((byte) 0xf9, BNZ);
			put((byte) 0xfa, BS);
			put((byte) 0xff, WRITE);
			put((byte) 0xfd, MODE);
			put((byte) 0x02, AND);
			put((byte) 0x03, OR);
			put((byte) 0x04, XOR);
			put((byte) 0x05, NOT);
			put((byte) 0x08, ADD);
			put((byte) 0x09, SUB);
			put((byte) 0x0a, CMP);
			put((byte) 0x0b, NEG);
			put((byte) 0x0c, MOV);
			put((byte) 0x06, ROL);
			put((byte) 0x07, ASR);
			put((byte) 0x0d, LDR);
			put((byte) 0x0e, STR);
		}});
		map.put(4, new HashMap<Byte, Instruction>() {{
			put((byte) 0xf4, SWP);
			put((byte) 0xf5, ADDI);
			put((byte) 0xf6, CMPI);
			put((byte) 0xfb, STAT);
		}});
		map.put(5, new HashMap<Byte, Instruction>() {{
			put((byte) 0xf2, LC);
		}});
		map.put(7, new HashMap<Byte, Instruction>() {{
			put((byte) 0xf3, LL);
			put((byte) 0xf0, LDB);
			put((byte) 0xf1, STB);
		}});
	}

	private final InstructionType instructionType;
	private final Decoder decoder;
	private final Executor executor;

	Instruction(InstructionType instructionType, Decoder decoder, Executor executor) {
		this.instructionType = instructionType;
		this.decoder = decoder;
		this.executor = executor;
	}

	public static Optional<Instruction> getInstructionFromFetchQueue(FetchQueue fetchQueue) {
		Map<Byte, Instruction> subMap = map.get(fetchQueue.size());
		if (subMap == null) {
			return Optional.empty();
		}

		Instruction instruction = subMap.get(fetchQueue.get(0));
		if (instruction == null && fetchQueue.size() > 1) {
			byte tmp = (byte) ((fetchQueue.get(0) << 4) | fetchQueue.get(1));
			instruction = subMap.get(tmp);
		}
		return Optional.ofNullable(instruction);
	}

	public InstructionType getInstructionType() {
		return instructionType;
	}

	public InstructionParameters decode(FetchQueue fetchQueue) {
		return decoder.apply(fetchQueue);
	}

	public void execute(StadiumShip ship, InstructionParameters parameter, long currentCycle) {
		executor.accept(ship, parameter, currentCycle);
	}
}
