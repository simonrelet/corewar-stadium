package corewar.shared;

import corewar.shared.OptionParser.Options;
import corewar.stadium.StadiumResult;
import corewar.stadium.StadiumResult.FinishType;
import corewar.stadium.runtime.logs.StadiumLog;

import java.util.LinkedList;
import java.util.Queue;

public abstract class Logger {

	protected final Queue<StadiumLog> logs = new LinkedList<>();

	protected final Options options;

	protected Logger(Options options) {
		this.options = options;
	}

	public static Logger create(Options options) {
		if (options.isPretty()) {
			return new PrettyLogger(options);
		}
		return new JsonLogger(options);
	}

	public static void logError(String msg, Options opt) {
		String res;
		if (opt.isPretty()) {
			res = msg;
		} else {
			res = "{\"errors\":[{\"message\":\"" + msg + "\"}]}";
		}
		System.out.println(res);
	}

	public void logResult(StadiumResult result) {
		StringBuilder stringBuilder = prefix(new StringBuilder());
		handleResult(stringBuilder, result);

		if (!logs.isEmpty()) {
			handleLogs(stringBuilder);
		}

		System.out.println(suffix(stringBuilder).toString());
	}

	protected abstract StringBuilder suffix(StringBuilder stringBuilder);

	protected abstract void handleLogs(StringBuilder stringBuilder);

	protected abstract void handleResult(StringBuilder stringBuilder, StadiumResult result);

	protected abstract StringBuilder prefix(StringBuilder stringBuilder);

	public void log(StadiumLog log) {
		if (log.getType().getVerbosityLevel() <= options.getVerbosity()
				&& log.getCycle() >= options.getFirstCycle()
				&& log.getCycle() <= options.getLastCycle()) {
			logs.add(log);
		}
	}

	public int getVerbosity() {
		return options.getVerbosity();
	}

	private static final class JsonLogger extends Logger {

		protected JsonLogger(Options options) {
			super(options);
		}

		@Override
		protected StringBuilder suffix(StringBuilder stringBuilder) {
			return stringBuilder.append("}");
		}

		@Override
		protected void handleLogs(StringBuilder stringBuilder) {
			stringBuilder.append(",\"logs\":[")
					.append(logs.stream().map(log -> log.toString(options))
							.reduce((s, s2) -> s + "," + s2).get())
					.append("]");
		}

		@Override
		protected void handleResult(StringBuilder stringBuilder, StadiumResult result) {
			stringBuilder.append("\"result\":")
					.append(result.getFinishType() == FinishType.FINISHED ? "finished" : "crashed")
					.append("\",\"cycles\":")
					.append(result.getCycle());
		}

		@Override
		protected StringBuilder prefix(StringBuilder stringBuilder) {
			return stringBuilder.append("{");
		}
	}

	private static final class PrettyLogger extends Logger {

		protected PrettyLogger(Options options) {
			super(options);
		}

		@Override
		protected StringBuilder suffix(StringBuilder stringBuilder) {
			return stringBuilder;
		}

		@Override
		protected void handleLogs(StringBuilder stringBuilder) {
			stringBuilder.append("\n")
					.append(logs.stream().map(log -> log.toString(options))
							.reduce((s, s2) -> s + "\n" + s2).get());
		}

		@Override
		protected void handleResult(StringBuilder stringBuilder, StadiumResult result) {
			if (result.getFinishType() == FinishType.FINISHED) {
				stringBuilder.append("Your ship finished in ")
						.append(result.getCycle())
						.append(" cycles!");
			} else {
				stringBuilder.append("Hah, loser! Your ship ");
				if (result.getFinishType() == FinishType.CRASHED) {
					stringBuilder.append("crashed at cycle ")
							.append(result.getCycle())
							.append("!");
				} else {
					stringBuilder.append("forgot to check!");
				}
			}
		}

		@Override
		protected StringBuilder prefix(StringBuilder stringBuilder) {
			return stringBuilder;
		}
	}
}
