package corewar.stadium.memory;

public final class InstructionParameters {

	private final InstructionParametersMeta meta = InstructionParametersMeta.create();
	private int regX = -1;
	private int regY = -1;
	private int n;
	private int m;

	private InstructionParameters() {
	}

	public static InstructionParameters create() {
		return new InstructionParameters();
	}

	public int getRegX() {
		return regX;
	}

	public void setRegX(int regX) {
		this.regX = regX;
	}

	public int getRegY() {
		return regY;
	}

	public void setRegY(int regY) {
		this.regY = regY;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
	}

	public InstructionParametersMeta getMeta() {
		return meta;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof InstructionParameters)) {
			return false;
		}

		InstructionParameters that = (InstructionParameters) o;
		if (m != that.m) {
			return false;
		}
		if (n != that.n) {
			return false;
		}
		if (regX != that.regX) {
			return false;
		}
		return regY == that.regY && meta.equals(that.meta);
	}

	@Override
	public int hashCode() {
		int result = meta.hashCode();
		result = 31 * result + regX;
		result = 31 * result + regY;
		result = 31 * result + n;
		result = 31 * result + m;
		return result;
	}

	@Override
	public String toString() {
		return "InstructionParameter{"
				+ "meta=" + meta
				+ ", regX=" + regX
				+ ", regY=" + regY
				+ ", n=" + n
				+ ", m=" + m
				+ '}';
	}

	public static final class InstructionParametersMeta {

		private int count;
		private int offset;

		private InstructionParametersMeta() {
		}

		public static InstructionParametersMeta create() {
			return new InstructionParametersMeta();
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public int getOffset() {
			return offset;
		}

		public void setOffset(int offset) {
			this.offset = offset;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof InstructionParametersMeta)) {
				return false;
			}

			InstructionParametersMeta that = (InstructionParametersMeta) o;
			return count == that.count && offset == that.offset;
		}

		@Override
		public int hashCode() {
			return 31 * count + offset;
		}

		@Override
		public String toString() {
			return "InstructionMeta{"
					+ "count=" + count
					+ ", offset=" + offset
					+ '}';
		}
	}
}
