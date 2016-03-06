package corewar.stadium;

import corewar.stadium.memory.Track;

public interface Stadium {

	long getCycle();

	Track getTrack();

	StadiumResult run(String shipBin);
}
