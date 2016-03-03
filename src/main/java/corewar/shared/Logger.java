package corewar.shared;

import corewar.stadium.StadiumResult;

public class Logger {
	private Logger() {
	}

	public static void logError(String msg) {
		System.out.println("{\"errors\":[{\"msg\":\"" + msg + "\"}]}");
	}

	public static void logResult(StadiumResult result) {
		System.out.println("Yeah!");
	}
}
