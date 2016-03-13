package corewar.stadium.runtime.logs;

public final class ExecuteLog extends StadiumLog {

	private final String msg;

	private ExecuteLog(int shipId, long cycle, boolean blueArrow, boolean rails,
			String msg) {
		super(shipId, Type.EXECUTE, cycle, blueArrow, rails);
		this.msg = msg;
	}

	public static ExecuteLog create(int shipId, long cycle, boolean blueArrow, boolean rails,
			String msg) {
		return new ExecuteLog(shipId, cycle, blueArrow, rails, msg);
	}

	@Override
	protected StringBuilder getContent(int verbosity) {
		return super.getContent(verbosity)
				.append(",\"message\":\"")
				.append(msg)
				.append("\"");
	}
}
