package corewar.shared;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Delay {

	AND(InstructionType.AND, toArray(toArray(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), toArray(20, 12, 10, 10, 5, 20, 20, 20, 20, 20, 200, 50, 50))),
	LDB(InstructionType.LDB, toArray(toArray(4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4), toArray(60, 30, 50, 20, 50, 60, 60, 60, 70, 70, 65535, 65535, 65535))),
	ROL(InstructionType.ROL, toArray(toArray(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), toArray(25, 20, 15, 20, 8, 25, 25, 25, 25, 25, 200, 50, 50))),
	LDR(InstructionType.LDR, toArray(toArray(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2), toArray(40, 20, 30, 10, 30, 30, 30, 30, 80, 80, 80, 120, 90))),
	CMPI(InstructionType.CMPI, toArray(toArray(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), toArray(50, 40, 40, 35, 35, 50, 50, 50, 50, 50, 200, 70, 70))),
	CHECK(InstructionType.CHECK, toArray(toArray(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), toArray(151, 142, 160, 150, 160, 90, 110, 120, 65535, 65535, 65535, 65535, 65535))),
	BNZ(InstructionType.BNZ, toArray(toArray(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), toArray(30, 30, 30, 24, 30, 28, 20, 30, 20, 20, 0, 65535, 65535))),
	XOR(InstructionType.XOR, toArray(toArray(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), toArray(20, 15, 15, 12, 0, 20, 20, 20, 20, 20, 200, 50, 50))),
	SUB(InstructionType.SUB, toArray(toArray(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), toArray(30, 30, 30, 24, 16, 32, 35, 30, 50, 50, 200, 70, 70))),
	NEG(InstructionType.NEG, toArray(toArray(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), toArray(24, 24, 24, 20, 8, 24, 24, 24, 30, 30, 200, 50, 50))),
	LL(InstructionType.LL, toArray(toArray(3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3), toArray(50, 40, 50, 20, 60, 60, 65, 60, 120, 120, 500, 200, 200))),
	WRITE(InstructionType.WRITE, toArray(toArray(1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), toArray(5, 5, 5, 5, 5, 5, 5, 5, 5, 10, 0, 10, 0))),
	ADD(InstructionType.ADD, toArray(toArray(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), toArray(30, 30, 30, 24, 16, 32, 35, 30, 50, 50, 200, 70, 70))),
	SWP(InstructionType.SWP, toArray(toArray(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), toArray(25, 20, 15, 25, 8, 25, 25, 25, 30, 30, 200, 50, 50))),
	ADDI(InstructionType.ADDI, toArray(toArray(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), toArray(50, 40, 40, 35, 35, 50, 50, 50, 50, 50, 200, 70, 70))),
	FORK(InstructionType.FORK, toArray(toArray(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2), toArray(4000, 4000, 2000, 3000, 1600, 3000, 3500, 4000, 65535, 65535, 65535, 65535, 65535))),
	ASR(InstructionType.ASR, toArray(toArray(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), toArray(35, 30, 30, 25, 16, 35, 35, 35, 40, 40, 200, 70, 70))),
	STAT(InstructionType.STAT, toArray(toArray(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2), toArray(30, 30, 10, 40, 30, 40, 30, 30, 65535, 10, 65535, 100, 100))),
	CRASH(InstructionType.CRASH, toArray(toArray(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), toArray(0, 0, 100, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))),
	MOV(InstructionType.MOV, toArray(toArray(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), toArray(30, 30, 30, 20, 10, 30, 30, 30, 30, 30, 200, 50, 40))),
	BS(InstructionType.BS, toArray(toArray(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), toArray(30, 30, 30, 24, 30, 28, 20, 30, 20, 20, 0, 65535, 65535))),
	NOT(InstructionType.NOT, toArray(toArray(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), toArray(20, 15, 15, 12, 0, 20, 20, 20, 20, 20, 200, 50, 50))),
	NOP(InstructionType.NOP, toArray(toArray(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), toArray(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0))),
	BZ(InstructionType.BZ, toArray(toArray(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), toArray(30, 30, 30, 24, 30, 28, 20, 30, 20, 20, 0, 65535, 65535))),
	LC(InstructionType.LC, toArray(toArray(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2), toArray(35, 30, 40, 20, 40, 40, 45, 40, 120, 120, 500, 200, 200))),
	B(InstructionType.B, toArray(toArray(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), toArray(30, 30, 30, 20, 30, 24, 14, 30, 15, 20, 0, 65535, 65535))),
	STB(InstructionType.STB, toArray(toArray(2, 2, 2, 2, 2, 3, 3, 1, 3, 4, 4, 4, 4), toArray(20, 20, 20, 15, 20, 12, 20, 12, 10, 20, 65535, 500, 1000))),
	MODE(InstructionType.MODE, toArray(toArray(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2), toArray(260, 80, 120, 100, 100, 250, 250, 300, 65535, 65535, 65535, 65535, 65535))),
	STR(InstructionType.STR, toArray(toArray(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2), toArray(20, 20, 20, 15, 20, 10, 20, 20, 10, 10, 0, 65535, 160))),
	OR(InstructionType.OR, toArray(toArray(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), toArray(20, 15, 15, 12, 0, 20, 20, 20, 20, 20, 200, 50, 50))),
	CMP(InstructionType.CMP, toArray(toArray(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), toArray(30, 30, 30, 24, 16, 32, 35, 30, 50, 50, 200, 70, 70)));

	public enum State {
		DECODE(0),
		EXECUTE(1);

		private final int value;

		private State(int value) {
			this.value = value;
		}
	}

	private static final Map<InstructionType, Delay> map = new HashMap<>();

	static {
		map.put(CRASH.getInstructionType(), CRASH);
		map.put(NOP.getInstructionType(), NOP);
		map.put(CHECK.getInstructionType(), CHECK);
		map.put(FORK.getInstructionType(), FORK);
		map.put(B.getInstructionType(), B);
		map.put(BZ.getInstructionType(), BZ);
		map.put(BNZ.getInstructionType(), BNZ);
		map.put(BS.getInstructionType(), BS);
		map.put(WRITE.getInstructionType(), WRITE);
		map.put(MODE.getInstructionType(), MODE);
		map.put(AND.getInstructionType(), AND);
		map.put(OR.getInstructionType(), OR);
		map.put(XOR.getInstructionType(), XOR);
		map.put(NOT.getInstructionType(), NOT);
		map.put(ADD.getInstructionType(), ADD);
		map.put(SUB.getInstructionType(), SUB);
		map.put(CMP.getInstructionType(), CMP);
		map.put(NEG.getInstructionType(), NEG);
		map.put(MOV.getInstructionType(), MOV);
		map.put(SWP.getInstructionType(), SWP);
		map.put(ROL.getInstructionType(), ROL);
		map.put(ASR.getInstructionType(), ASR);
		map.put(ADDI.getInstructionType(), ADDI);
		map.put(CMPI.getInstructionType(), CMPI);
		map.put(LC.getInstructionType(), LC);
		map.put(LL.getInstructionType(), LL);
		map.put(STAT.getInstructionType(), STAT);
		map.put(LDR.getInstructionType(), LDR);
		map.put(STR.getInstructionType(), STR);
		map.put(LDB.getInstructionType(), LDB);
		map.put(STB.getInstructionType(), STB);
	}

	private final InstructionType instructionType;
	private final Integer[][] values;

	private Delay(InstructionType instructionType, Integer[][] values) {
		this.instructionType = instructionType;
		this.values = values;
	}

	public static int getDelay(InstructionType instructionType, State state, Mode mode) {
		return map.get(instructionType).getDelay(state, mode);
	}

	@SafeVarargs
	private static <T> T[] toArray(T... values) {
		return Arrays.copyOf(values, values.length);
	}

	public InstructionType getInstructionType() {
		return instructionType;
	}

	public int getDelay(State state, Mode mode) {
		return values[state.value][mode.getValue()];
	}
}
