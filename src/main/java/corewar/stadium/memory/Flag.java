package corewar.stadium.memory;

public class Flag {
	private boolean z;
	private boolean s;

	public boolean getZ() {
		return z;
	}

	public void setZ(boolean value) {
		this.z = value;
	}

	public boolean getS() {
		return s;
	}

	public void setS(boolean value) {
		this.s = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Flag)) {
			return false;
		}

		Flag that = (Flag) o;
		return s == that.s && z == that.z;
	}

	@Override
	public int hashCode() {
		int result = z ? 1 : 0;
		result = 31 * result + (s ? 1 : 0);
		return result;
	}

	@Override
	public String toString() {
		return "{" + (z ? "Z" : "") + (s ? "S" : "") + "}";
	}
}
