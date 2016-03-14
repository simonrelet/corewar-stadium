package corewar.stadium;

import corewar.shared.Logger;
import corewar.shared.OptionParser.Options;
import corewar.stadium.memory.Track;

public interface Stadium {

	long getCycle();

	Track getTrack();

	void run(String shipBin, Options verbosity);

	Logger getLogger();

	int getNextId();
}
