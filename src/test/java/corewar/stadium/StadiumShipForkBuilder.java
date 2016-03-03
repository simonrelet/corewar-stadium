package corewar.stadium;

import corewar.shared.Mode;

public class StadiumShipForkBuilder {
	private final StadiumShipFork ship;

	public StadiumShipForkBuilder(StadiumShip mainShip) {
		this.ship = new StadiumShipFork(mainShip);
	}

	public StadiumShipFork get() {
		return ship;
	}

	public StadiumShipForkBuilder crashed() {
		ship.crash();
		return this;
	}

	public StadiumShipForkBuilder finished() {
		ship.setFinished();
		return this;
	}

	public StadiumShipForkBuilder pcw0(int value) {
		ship.incrementPcAndW0(value);
		return this;
	}

	public StadiumShipForkBuilder lastChecked(int value) {
		ship.setLastCheckCycle(value);
		return this;
	}

	public StadiumShipForkBuilder checked(int zone) {
		ship.getCheckedZones().set(zone);
		return this;
	}

	public StadiumShipForkBuilder checked(int from, int to) {
		ship.getCheckedZones().set(from, to + 1);
		return this;
	}

	public StadiumShipForkBuilder z(boolean value) {
		ship.getFlags().setZ(value);
		return this;
	}

	public StadiumShipForkBuilder s(boolean value) {
		ship.getFlags().setS(value);
		return this;
	}

	public StadiumShipForkBuilder fork() {
		ship.getFlags().setZ(false);
		ship.fork();
		return this;
	}

	public StadiumShipForkBuilder mode(int value) {
		Mode mode = Mode.fromValue(value);
		assert mode != null;
		ship.setCurrentMode(mode);
		return this;
	}

	public StadiumShipForkBuilder r(int reg, int value) {
		ship.getRegisters()[reg].set(value);
		return this;
	}

	public StadiumShipForkBuilder trackFlush(int address, int value) {
		track(address, value);
		ship.getMainShip().getTrack().flushWrites();
		return this;
	}

	public StadiumShipForkBuilder track(int address, int value) {
		ship.getMainShip().getTrack().write(address, (byte) value);
		return this;
	}

	public StadiumShipForkBuilder buff(int offset, int value) {
		ship.getBuffer().set(offset, (byte) value);
		return this;
	}
}
