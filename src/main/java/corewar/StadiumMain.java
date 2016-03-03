package corewar;

import corewar.shared.Logger;
import corewar.stadium.Stadium;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StadiumMain {

	public static void main(String[] args) {
		if (args.length < 1) {
			Logger.logError("Stadium program argument error");
		} else {
			String file = args[0];
			try {
				String content = new String(Files.readAllBytes(Paths.get(file)));
				Logger.logResult(Stadium.run(content));
			} catch (IOException e) {
				Logger.logError("Cannot read file + '" + file + "'");
			}
		}
	}
}
