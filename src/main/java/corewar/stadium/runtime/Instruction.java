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

import static corewar.stadium.runtime.Printers.*;

public enum Instruction {

	NOP(InstructionType.NOP, Decoders::decodeNothingToDo, Executors::executeNop, Printers::nop),
	CRASH(InstructionType.CRASH, Decoders::decodeNothingToDo, Executors::executeCrash,
			Printers::crash),
	CHECK(InstructionType.CHECK, Decoders::decodeNothingToDo, Executors::executeCheck,
			Printers::check),
	FORK(InstructionType.FORK, Decoders::decodeNothingToDo, Executors::executeFork,
			Printers::fork),

	MODE(InstructionType.MODE, Decoders::decodeMode, Executors::executeMode, Printers::mode),
	LC(InstructionType.LC, Decoders::decodeLc, Executors::executeLoad, Printers::lc),
	LL(InstructionType.LL, Decoders::decodeLl, Executors::executeLoad, Printers::ll),
	SWP(InstructionType.SWP, Decoders::decodeSwp, Executors::executeSwp, Printers::swp),
	LDB(InstructionType.LDB, Decoders::decodeLdbStb, Executors::executeLdb, Printers::ldb),
	STB(InstructionType.STB, Decoders::decodeLdbStb, Executors::executeStb, Printers::stb),
	LDR(InstructionType.LDR, Decoders::decodeLdrStr, Executors::executeLdr, Printers::ldr),
	STR(InstructionType.STR, Decoders::decodeLdrStr, Executors::executeStr, Printers::str),
	STAT(InstructionType.STAT, Decoders::decodeStat, Executors::executeStat, Printers::stat),

	MOV(InstructionType.MOV, Decoders::decodeRegReg, Executors::executeMov, Printers::mov),
	ADD(InstructionType.ADD, Decoders::decodeRegReg, Executors::executeAdd, Printers::add),
	OR(InstructionType.OR, Decoders::decodeRegReg, Executors::executeOr, Printers::or),
	AND(InstructionType.AND, Decoders::decodeRegReg, Executors::executeAnd, Printers::and),
	XOR(InstructionType.XOR, Decoders::decodeRegReg, Executors::executeXor, Printers::xor),
	SUB(InstructionType.SUB, Decoders::decodeRegReg, Executors::executeSub, Printers::sub),
	NEG(InstructionType.NEG, Decoders::decodeRegReg, Executors::executeNeg, Printers::neg),
	NOT(InstructionType.NOT, Decoders::decodeRegReg, Executors::executeNot, Printers::not),
	CMP(InstructionType.CMP, Decoders::decodeRegReg, Executors::executeCmp, Printers::cmp),

	ASR(InstructionType.ASR, Decoders::decodeRegN, Executors::executeAsr, Printers::asr),
	ROL(InstructionType.ROL, Decoders::decodeRegN, Executors::executeRol, Printers::rol),

	WRITE(InstructionType.WRITE, Decoders::decodeFReg, Executors::executeWrite, Printers::write),
	B(InstructionType.B, Decoders::decodeFReg, Executors::executeB, Printers::b),
	BZ(InstructionType.BZ, Decoders::decodeFReg, Executors::executeBz, Printers::bz),
	BS(InstructionType.BS, Decoders::decodeFReg, Executors::executeBs, Printers::bs),
	BNZ(InstructionType.BNZ, Decoders::decodeFReg, Executors::executeBnz, Printers::bnz),

	CMPI(InstructionType.CMPI, Decoders::decodeFRegN, Executors::executeCmpi, Printers::cmpi),
	ADDI(InstructionType.ADDI, Decoders::decodeFRegN, Executors::executeAddi, Printers::addi);

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
	private final Printer printer;

	Instruction(InstructionType instructionType, Decoder decoder, Executor executor,
			Printer printer) {
		this.instructionType = instructionType;
		this.decoder = decoder;
		this.executor = executor;
		this.printer = printer;
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
		executor.apply(ship, parameter, currentCycle);
	}

	public String print(StadiumShip ship, InstructionParameters p, int verbosity) {
		return printer.apply(ship, p, verbosity);
	}
}
