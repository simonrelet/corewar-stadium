package corewar.stadium.runtime.logs;

import corewar.shared.OptionParser.Options;

public class StadiumLog {

	private final int shipId;
	private final Type type;
	private final long cycle;
	private final boolean blueArrow;
	private final boolean rails;

	public enum Type {
		READ(3),
		WRITE(3),
		DECODE(1),
		EXECUTE(1);

		private final int verbosityLevel;

		Type(int verbosityLevel) {
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

	public long getCycle() {
		return cycle;
	}

	protected StringBuilder getJsonContent(int verbosity) {
		StringBuilder stringBuilder = new StringBuilder("\"shipId\":")
				.append(shipId)
				.append(",\"type\":\"")
				.append(type.name().toLowerCase())
				.append("\",\"cycle\":")
				.append(cycle);
		if (verbosity >= 3) {
			if (rails) {
				stringBuilder.append(",\"rails\":").append("true");
			}
			if (blueArrow) {
				stringBuilder.append(",\"blueArrow\":").append("true");
			}
		}
		return stringBuilder;
	}

	protected StringBuilder getPrettyContent(int verbosity) {
		StringBuilder stringBuilder = new StringBuilder("Cycle ")
				.append(cycle)
				.append(" for ship ")
				.append(shipId)
				.append(": ")
				.append(type.name().toLowerCase());
		if (verbosity >= 3) {
			if (rails || blueArrow) {
				stringBuilder.append("\n    Boost(s): ");
			}
			if (rails) {
				stringBuilder.append("rails");
			}
			if (blueArrow) {
				if (rails) {
					stringBuilder.append(", ");
				}
				stringBuilder.append("blue arrow");
			}
		}

		return stringBuilder;
	}

	public String toString(Options options) {
		if (options.isPretty()) {
			return getPrettyContent(options.getVerbosity()).toString();
		}
		return "{" + getJsonContent(options.getVerbosity()) + "}";
	}
}
