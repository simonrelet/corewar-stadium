package corewar.stadium.memory;

public class InstructionParameterBuilder {
	private final InstructionParameters instr;

	public InstructionParameterBuilder() {
		this.instr = InstructionParameters.create();
	}

	public InstructionParameters get() {
		return instr;
	}

	public InstructionParameterBuilder m(int value) {
		instr.setM(value);
		return this;
	}

	public InstructionParameterBuilder n(int value) {
		instr.setN(value);
		return this;
	}

	public InstructionParameterBuilder x(int value) {
		instr.setRegX(value);
		return this;
	}

	public InstructionParameterBuilder y(int value) {
		instr.setRegY(value);
		return this;
	}
}
