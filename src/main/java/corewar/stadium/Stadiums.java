package corewar.stadium;

import corewar.shared.OptionParser.Options;

public final class Stadiums {

	private Stadiums() {
	}

	public static void run(String inputShipBin, Options options) {
		new StadiumImpl().run(inputShipBin, options);
	}
}
