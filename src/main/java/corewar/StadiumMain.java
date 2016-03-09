package corewar;

import corewar.shared.Logger;
import corewar.stadium.Stadiums;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class StadiumMain {

	private StadiumMain() {
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			Logger.logError("Stadium program argument error");
		} else {
			boolean verbose = false;
			if(args.length > 1) {
				if ("-v".equals(args[0])) {
					verbose = true;
				}
			}
			String file = args[args.length - 1];
			try {
				String content = new String(Files.readAllBytes(Paths.get(file)));
				Stadiums.run(content, verbose);
			} catch (IOException e) {
				Logger.logError("Cannot read file + '" + file + "'");
			}
		}
	}
}
