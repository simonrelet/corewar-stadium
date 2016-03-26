package corewar.stadium;

import corewar.shared.Logger;
import corewar.stadium.memory.Track;

public interface Stadium {

	long getCycle();

	Track getTrack();

	void run(String shipBin);

	Logger getLogger();

	int getNextId();
}
