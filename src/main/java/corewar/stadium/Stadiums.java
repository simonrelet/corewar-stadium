package corewar.stadium;

public final class Stadiums {

	private Stadiums() {
	}

	public static void run(String inputShipBin, boolean verbose) {
		new StadiumImpl().run(inputShipBin, verbose);
	}
}
