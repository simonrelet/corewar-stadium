package corewar.stadium.runtime.logs;

public class StadiumLog {

	private final int shipId;
	private final Type type;
	private final long cycle;
	private final boolean blueArrow;
	private final boolean rails;

	public enum Type {
		READ,
		WRITE,
		DECODE,
		EXECUTE
	}

	protected StadiumLog(int shipId, Type type, long cycle, boolean blueArrow, boolean rails) {
		this.shipId = shipId;
		this.type = type;
		this.cycle = cycle;
		this.blueArrow = blueArrow;
		this.rails = rails;
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
