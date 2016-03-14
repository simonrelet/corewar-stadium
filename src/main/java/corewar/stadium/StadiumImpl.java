package corewar.stadium;

import corewar.shared.Constants;
import corewar.shared.Logger;
import corewar.shared.OptionParser.Options;
import corewar.shared.Utilities;
import corewar.stadium.StadiumResult.FinishType;
import corewar.stadium.memory.Track;
import corewar.stadium.runtime.logs.ExecuteLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

final class StadiumImpl implements Stadium {

	private final Track track = Track.create();
	private final Collection<StadiumShip> ships = new ArrayList<>();
	private final Logger logger = Logger.create();

	private Optional<StadiumResult> lastFinishedShip = Optional.empty();
	private long cycle;
	private int shipIdGenerator;

	@Override
	public void run(String shipBin, Options options) {
		logger.setVerbosity(options.getVerbosity());
		logger.setFirstCycle(options.getFirstCycle());
		logger.setLastCycle(options.getLastCycle());
		ships.add(StadiumShip.create(this));
		track.placeShip(Utilities.extractCodeFromBin(shipBin).toCharArray());

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

		checkArgument(lastFinishedShip.isPresent(), "There should be at least one finished ship");
		logger.logResult(lastFinishedShip.get());
	}

	@Override
	public long getCycle() {
		return cycle;
	}

	@Override
	public Track getTrack() {
		return track;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public int getNextId() {
		return shipIdGenerator++;
	}

	private void controlCheckPoints() {
		ships.removeIf(this::removeShipIfNotChecked);
	}

	private boolean removeShipIfNotChecked(StadiumShip ship) {
		boolean res = false;
		if (ship.getLastCheckCycle() + Constants.CHECKPOINT_DELAY < cycle) {
			lastFinishedShip = Optional.of(StadiumResult.create(FinishType.MISSED_CHECK, cycle));
			logger.log(ExecuteLog.create(ship.getId(), cycle, false, false,
					"did not check: crashed"));
			res = true;
		}
		return res;
	}

	private void runCycle() {
		ships.forEach(StadiumShip::runCycle);
	}

	private void controlFinish() {
		ships.removeIf(this::removeShipIfFinished);
	}

	private boolean removeShipIfFinished(StadiumShip ship) {
		if (ship.hasCrashed() || ship.hasFinished()) {
			if (!lastFinishedShip.isPresent()
					|| lastFinishedShip.get().getFinishType() != FinishType.FINISHED) {
				FinishType finishType = ship.hasCrashed() ? FinishType.CRASHED : FinishType.FINISHED;
				lastFinishedShip = Optional.of(StadiumResult.create(finishType, cycle));
			}
			return true;
		}
		return false;
	}

	private boolean crashOthersIfOneFinished() {
		if (lastFinishedShip.isPresent()
				&& lastFinishedShip.get().getFinishType() == FinishType.FINISHED) {
			ships.clear();
			return true;
		}
		return false;
	}

	private void addNewForkShips() {
		Collection<StadiumShip> toAdd = new ArrayList<>();
		ships.forEach(s -> {
			Optional<StadiumShip> fork = s.getFork();
			if (fork.isPresent()) {
				toAdd.add(fork.get());
				s.clearFork();
			}
		});
		ships.addAll(toAdd);
	}
}
