package corewar.shared;

public enum InstructionType {

	CRASH(1),
	NOP(1),
	CHECK(2),
	FORK(2),
	B(3),
	BZ(3),
	BNZ(3),
	BS(3),
	WRITE(3),
	MODE(3),
	AND(3),
	OR(3),
	XOR(3),
	NOT(3),
	ADD(3),
	SUB(3),
	CMP(3),
	NEG(3),
	MOV(3),
	SWP(4),
	ROL(3),
	ASR(3),
	ADDI(4),
	CMPI(4),
	LC(5),
	LL(7),
	STAT(4),
	LDR(3),
	STR(3),
	LDB(7),
	STB(7);

	private final int size;

	InstructionType(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}
}
