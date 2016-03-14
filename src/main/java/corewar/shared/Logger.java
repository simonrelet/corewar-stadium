package corewar.shared;

import corewar.stadium.StadiumResult;
import corewar.stadium.StadiumResult.FinishType;
import corewar.stadium.runtime.logs.StadiumLog;

import java.util.Deque;
import java.util.LinkedList;

public final class Logger {

	private final Deque<StadiumLog> logs = new LinkedList<>();

	private int verbosity;
	private long firstCycle;
	private long lastCycle;

	private Logger() {
	}

	public static Logger create() {
		return new Logger();
	}

	public static void logError(String msg) {
		System.out.println("{\"errors\":[{\"message\":\"" + msg + "\"}]}");
	}

	public void logResult(StadiumResult result) {
		StringBuilder stringBuilder = new StringBuilder("{\"res\":\"")
				.append(result.getFinishType() == FinishType.FINISHED ? "finished" : "crashed")
				.append("\",\"cycles\":")
				.append(result.getCycle());

		if (!logs.isEmpty()) {
			stringBuilder.append(",\"logs\":[");

			stringBuilder.append(logs.poll().toString(verbosity));
			while (!logs.isEmpty()) {
				stringBuilder.append(",")
						.append(logs.poll().toString(verbosity));
			}

			stringBuilder.append("]");
		}

		stringBuilder.append("}");
		System.out.println(stringBuilder.toString());
	}

	public void log(StadiumLog log) {
		if (log.getType().getVerbosityLevel() <= verbosity
				&& log.getCycle() >= firstCycle && log.getCycle() <= lastCycle) {
			logs.add(log);
		}
	}

	public void setVerbosity(int verbosity) {
		this.verbosity = verbosity;
	}

	public int getVerbosity() {
		return verbosity;
	}

	public void setFirstCycle(long firstCycle) {
		this.firstCycle = firstCycle;
	}

	public void setLastCycle(long lastCycle) {
		this.lastCycle = lastCycle;
	}
}
