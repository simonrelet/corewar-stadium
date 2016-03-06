package corewar.stadium;

public final class Stadiums {

	private Stadiums() {
	}

	public static StadiumResult run(String inputShipBin) {
		return new StadiumImpl().run(inputShipBin);
	}
}
