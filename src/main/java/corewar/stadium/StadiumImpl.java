package corewar.stadium;

import corewar.shared.Constants;
import corewar.shared.Utilities;
import corewar.stadium.StadiumResult.FinishType;
import corewar.stadium.memory.Track;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

class StadiumImpl implements Stadium {

	private final Track track = Track.create();
	private final Collection<StadiumShip> ships = new ArrayList<>();
	private Optional<StadiumResult> lastFinishedShip = Optional.empty();

	private long cycle;

	@Override
	public StadiumResult run(String shipBin) {
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
		return lastFinishedShip.get();
	}

	@Override
	public long getCycle() {
		return cycle;
	}

	@Override
	public Track getTrack() {
		return track;
	}

	private void controlCheckPoints() {
		ships.removeIf(this::removeShipIfNotChecked);
	}

	private boolean removeShipIfNotChecked(StadiumShip ship) {
		boolean res = false;
		if (ship.getLastCheckCycle() + Constants.CHECKPOINT_DELAY < cycle) {
			lastFinishedShip = Optional.of(StadiumResult.create(FinishType.MISSED_CHECK,
					cycle));
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
