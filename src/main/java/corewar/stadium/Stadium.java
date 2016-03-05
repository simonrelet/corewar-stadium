package corewar.stadium;

import corewar.shared.Constants;
import corewar.shared.Logger;
import corewar.shared.Utilities;
import corewar.stadium.StadiumShipResult.FinishType;
import corewar.stadium.memory.Track;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Stadium {

	private final Track track = new Track();
	private final List<StadiumShipFork> ships = new ArrayList<>();
	private final List<StadiumShipResult> finishedShips = new ArrayList<>();
	private long cycle;

	private Stadium() {
	}

	public static StadiumResult run(String inputShipBin) {
		return new Stadium().runInternal(inputShipBin);
	}

	public StadiumResult runInternal(String shipBin) {
		Logger.setStadium(this);
		StadiumShip ship = new StadiumShip(Utilities.extractNameFromBin(shipBin),
			Utilities.extractCommentFromBin(shipBin),
			Utilities.extractCodeFromBin(shipBin),
			track);
		ships.add(new StadiumShipFork(ship));
		track.placeShip(ships.get(0).getMainShip().getStrBin().toCharArray());

		try {
			while (!ships.isEmpty()) {
				cycle++;
				controlCheckPoints();
				runCycle();
				controlFinish();
				if (!crashOthersIfOneFinished()) {
					track.flushWrites();
					addNewForkShips();
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return new StadiumResult(finishedShips);
	}

	public long getCycle() {
		return cycle;
	}

	private void controlCheckPoints() {
		ships.removeIf(this::removeShipIfNotChecked);
	}

	private boolean removeShipIfNotChecked(StadiumShipFork ship) {
		boolean res = false;
		if (ship.getLastCheckCycle() + Constants.CHECKPOINT_DELAY < cycle) {
			finishedShips.add(new StadiumShipResult(ship, FinishType.MISSED_CHECK, (int) cycle));
			res = true;
		}
		return res;
	}

	private void runCycle() {
		ships.forEach(s -> s.runCycle(cycle));
	}

	private void controlFinish() {
		ships.removeIf(this::removeShipIfFinished);
	}

	private boolean removeShipIfFinished(StadiumShipFork ship) {
		boolean res = false;
		if (ship.hasCrashed() || ship.hasFinished()) {
			finishedShips.add(new StadiumShipResult(ship, ship.hasCrashed() ? FinishType.CRASHED : FinishType.FINISHED, (int) cycle));
			res = true;
		}
		return res;
	}

	private boolean crashOthersIfOneFinished() {
		if (!finishedShips.isEmpty() && finishedShips.stream().anyMatch(s -> s.getFinishType() == FinishType.FINISHED)) {
			ships.forEach(ship -> finishedShips.add(new StadiumShipResult(ship, FinishType.CRASHED, (int) cycle)));
			ships.clear();
			return true;
		}
		return false;
	}

	private void addNewForkShips() {
		Collection<StadiumShipFork> toAdd = new ArrayList<>();
		ships.forEach(s -> {
			StadiumShipFork fork = s.getFork();
			if (fork != null) {
				toAdd.add(fork);
				s.setFork(null);
			}
		});
		ships.addAll(toAdd);
	}
}
