package corewar.stadium;

public class StadiumShipResult {

	public enum FinishType {
		FINISHED,
		MISSED_CHECK,
		CRASHED
	}

	private final StadiumShipFork ship;
	private final FinishType finishType;
	private final int cycle;

	public StadiumShipResult(StadiumShipFork ship, FinishType finishType, int cycle) {
		this.ship = ship;
		this.finishType = finishType;
		this.cycle = cycle;
	}

	public StadiumShipFork getShip() {
		return ship;
	}

	public FinishType getFinishType() {
		return finishType;
	}

	public int getCycle() {
		return cycle;
	}
}
