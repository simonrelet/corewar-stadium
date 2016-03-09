package corewar.shared;

import corewar.stadium.StadiumResult;
import corewar.stadium.StadiumResult.FinishType;
import corewar.stadium.runtime.logs.StadiumLog;

import java.util.Deque;
import java.util.LinkedList;

public final class Logger {

	private final Deque<StadiumLog> logs = new LinkedList<>();

	private boolean verbose;

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

			stringBuilder.append(logs.poll().toString());
			while (!logs.isEmpty()) {
				stringBuilder.append(",")
						.append(logs.poll().toString());
			}

			stringBuilder.append("]");
		}

		stringBuilder.append("}");
		System.out.println(stringBuilder.toString());
	}

	public void log(StadiumLog log) {
		if (verbose) {
			logs.add(log);
		}
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
}
