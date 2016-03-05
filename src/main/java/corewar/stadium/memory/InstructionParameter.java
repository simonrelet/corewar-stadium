package corewar.stadium.memory;

import corewar.shared.HexIntConverter;

public class InstructionParameter {

	private final InstructionMeta meta = new InstructionMeta();
	private int regX = -1;
	private int regY = -1;
	private int n;
	private int m;

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

	public InstructionMeta getMeta() {
		return meta;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof InstructionParameter)) {
			return false;
		}

		InstructionParameter that = (InstructionParameter) o;
		if (m != that.m) {
			return false;
		}
		if (n != that.n) {
			return false;
		}
		if (regX != that.regX) {
			return false;
		}
		if (regY != that.regY) {
			return false;
		}
		return meta.equals(that.meta);
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
		return "InstructionParameter{" +
				 "meta=" + meta +
				 ", regX=" + regX +
				 ", regY=" + regY +
				 ", n=" + n +
				 ", m=" + m +
				 '}';
	}

	public String toLogString() {
		return "{regX=" + regX +
				 ", regY=" + regY +
				 ", n=" + HexIntConverter.intToHexWithPrefix(n, 4) +
				 ", m=" + HexIntConverter.intToHexWithPrefix(m, 4) +
				 '}';
	}

	public static class InstructionMeta {

		private int count;
		private int offset;
		private int tmpResult;

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

		public int getTmpResult() {
			return tmpResult;
		}

		public void setTmpResult(int tmpResult) {
			this.tmpResult = tmpResult;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof InstructionMeta)) {
				return false;
			}

			InstructionMeta that = (InstructionMeta) o;
			if (count != that.count) {
				return false;
			}
			if (offset != that.offset) {
				return false;
			}
			return tmpResult == that.tmpResult;
		}

		@Override
		public int hashCode() {
			int result = count;
			result = 31 * result + offset;
			result = 31 * result + tmpResult;
			return result;
		}

		@Override
		public String toString() {
			return "InstructionMeta{" +
					 "count=" + count +
					 ", offset=" + offset +
					 ", tmpResult=" + tmpResult +
					 '}';
		}
	}
}
