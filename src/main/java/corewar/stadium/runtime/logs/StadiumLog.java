package corewar.stadium.runtime.logs;

public class StadiumLog {

	private final int shipId;
	private final Type type;
	private final long cycle;
	private final boolean blueArrow;
	private final boolean rails;

	public enum Type {
		READ(3),
		WRITE(3),
		DECODE(2),
		EXECUTE(1);

		private final int verbosityLevel;

		private Type(int verbosityLevel) {
			this.verbosityLevel = verbosityLevel;
		}

		public int getVerbosityLevel() {
			return verbosityLevel;
		}
	}

	protected StadiumLog(int shipId, Type type, long cycle, boolean blueArrow, boolean rails) {
		this.shipId = shipId;
		this.type = type;
		this.cycle = cycle;
		this.blueArrow = blueArrow;
		this.rails = rails;
	}

	public Type getType() {
		return type;
	}

	protected StringBuilder getContent() {
		StringBuilder stringBuilder = new StringBuilder("\"shipId\":")
				.append(shipId)
				.append(",\"type\":\"")
				.append(type.name().toLowerCase())
				.append("\",\"cycle\":")
				.append(cycle);
		if (rails) {
			stringBuilder.append(",\"rails\":").append("true");
		}
		if (blueArrow) {
			stringBuilder.append(",\"blueArrow\":").append("true");
		}
		return stringBuilder;
	}

	@Override
	public String toString() {
		return "{" + getContent().toString() + "}";
	}
}
