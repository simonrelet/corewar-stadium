package corewar.shared;

import corewar.stadium.StadiumResult;
import corewar.stadium.StadiumResult.FinishType;

public final class Logger {

	private Logger() {
	}

	public static void logError(String msg) {
		System.out.println("{\"errors\":[{\"message\":\"" + msg + "\"}]}");
	}

	public static void logResult(StadiumResult result) {
		System.out.println("{\"res\":\"" +
								 (result.getFinishType() == FinishType.FINISHED ? "finished" : "crashed") +
								 "\",\"cycles\":" +
								 result.getCycle() +
								 "}");
	}
}
