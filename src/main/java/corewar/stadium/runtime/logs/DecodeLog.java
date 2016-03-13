package corewar.stadium.runtime.logs;

public final class DecodeLog extends StadiumLog {

	private final String instruction;

	private DecodeLog(int shipId, long cycle, boolean blueArrow, boolean rails, String
			instruction) {
		super(shipId, Type.DECODE, cycle, blueArrow, rails);
		this.instruction = instruction;
	}

	public static DecodeLog create(int shipId, long cycle, boolean blueArrow, boolean rails,
			String instruction) {
		return new DecodeLog(shipId, cycle, blueArrow, rails, instruction);
	}

	@Override
	protected StringBuilder getContent(int verbosity) {
		return super.getContent(verbosity)
				.append(",\"instruction\":\"")
				.append(instruction)
				.append("\"");
	}
}
