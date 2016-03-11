package corewar.stadium;

public final class Stadiums {

	private Stadiums() {
	}

	public static void run(String inputShipBin, int verbosity) {
		new StadiumImpl().run(inputShipBin, verbosity);
	}
}
