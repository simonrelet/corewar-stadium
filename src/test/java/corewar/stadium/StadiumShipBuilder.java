package corewar.stadium;

import corewar.shared.Mode;

import static com.google.common.base.Preconditions.checkNotNull;

public final class StadiumShipBuilder {

	private final StadiumShip ship;

	private StadiumShipBuilder(Stadium stadium) {
		this.ship = StadiumShip.create(stadium);
	}

	public static StadiumShipBuilder create(Stadium stadium) {
		return new StadiumShipBuilder(stadium);
	}

	public StadiumShip get() {
		return ship;
	}

	public StadiumShipBuilder crashed() {
		ship.crash();
		return this;
	}

	public StadiumShipBuilder finished() {
		ship.setFinished();
		return this;
	}

	public StadiumShipBuilder pcw0(int value) {
		ship.incrementPcAndW0(value);
		return this;
	}

	public StadiumShipBuilder lastChecked(int value) {
		ship.setLastCheckCycle(value);
		return this;
	}

	public StadiumShipBuilder checked(int zone) {
		ship.getCheckedZones().set(zone);
		return this;
	}

	public StadiumShipBuilder checked(int from, int to) {
		ship.getCheckedZones().set(from, to + 1);
		return this;
	}

	public StadiumShipBuilder z(boolean value) {
		ship.getFlags().setZ(value);
		return this;
	}

	public StadiumShipBuilder s(boolean value) {
		ship.getFlags().setS(value);
		return this;
	}

	public StadiumShipBuilder fork() {
		ship.getFlags().setZ(false);
		ship.fork();
		return this;
	}

	public StadiumShipBuilder mode(int value) {
		Mode mode = Mode.fromValue(value);
		checkNotNull(mode, "The mode should exists");
		ship.setCurrentMode(mode);
		return this;
	}

	public StadiumShipBuilder r(int reg, int value) {
		ship.getRegisters()[reg].set(value);
		return this;
	}

	public StadiumShipBuilder trackFlush(int address, int value) {
		track(address, value);
		ship.getStadium().getTrack().flushWrites();
		return this;
	}

	public StadiumShipBuilder track(int address, int value) {
		ship.getStadium().getTrack().write(address, (byte) value);
		return this;
	}

	public StadiumShipBuilder buff(int offset, int value) {
		ship.getBuffer().set(offset, (byte) value);
		return this;
	}
}
