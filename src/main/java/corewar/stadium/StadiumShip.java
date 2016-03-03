package corewar.stadium;

import corewar.stadium.memory.Track;

public class StadiumShip {

	public enum State {
		FETCH,
		DECODE,
		EXECUTE
	}

	private final Track track;
	private final String name;
	private final String comment;
	private final String strBin;

	private int forkIds;

	public StadiumShip(String name, String comment, String strBin, Track track) {
		this.track = track;
		this.name = name;
		this.comment = comment;
		this.strBin = strBin;
	}

	public Track getTrack() {
		return track;
	}

	public String getName() {
		return name;
	}

	public String getComment() {
		return comment;
	}

	public String getStrBin() {
		return strBin;
	}

	public int getNextForkId() {
		return forkIds++;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof StadiumShip)) {
			return false;
		}

		StadiumShip that = (StadiumShip) o;
		if (!comment.equals(that.comment)) {
			return false;
		}
		if (!name.equals(that.name)) {
			return false;
		}
		if (!strBin.equals(that.strBin)) {
			return false;
		}
		if (forkIds != that.forkIds) {
			return false;
		}
		return track.equals(that.track);
	}

	@Override
	public int hashCode() {
		int result = track.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + forkIds;
		result = 31 * result + comment.hashCode();
		result = 31 * result + strBin.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "StadiumShip{" +
				 "track=" + track +
				 ", name='" + name + '\'' +
				 ", comment='" + comment + '\'' +
				 ", strBin='" + strBin + '\'' +
				 '}';
	}
}
