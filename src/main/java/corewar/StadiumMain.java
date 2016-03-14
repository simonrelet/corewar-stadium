package corewar;

import corewar.shared.Logger;
import corewar.shared.OptionParser;
import corewar.shared.OptionParser.Options;
import corewar.stadium.Stadiums;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public final class StadiumMain {

	private StadiumMain() {
	}

	public static void main(String[] args) {
		Optional<Options> options = OptionParser.parse(args);
		if (options.isPresent()) {
			Options opt = options.get();
			try {
				String content = new String(Files.readAllBytes(Paths.get(opt.getFile())));
				Stadiums.run(content, opt);
			} catch (IOException e) {
				Logger.logError("Cannot read file + '" + opt.getFile() + "'");
			}
		} else {
			Logger.logError("Stadium program argument error");
		}
	}
}
