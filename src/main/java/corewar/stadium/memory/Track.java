package corewar.stadium.memory;

import corewar.shared.Utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class Track {

	private final Map<Long, Byte> track = new HashMap<>();
	private final Collection<TrackWrite> writeRequests = new ArrayList<>();
	private final Collection<TrackWrite> writeAtPreviousCycle = new ArrayList<>();

	private Track() {
	}

	public static Track create() {
		return new Track();
	}

	public byte read(long address) {
		return track.getOrDefault(address, (byte) 0);
	}

	public void write(long address, byte q) {
		writeRequests.add(new TrackWrite(address, q));
	}

	public boolean wroteAtPreviousCycle(long address) {
		return writeAtPreviousCycle.stream().anyMatch(trackWrite -> trackWrite.address == address);
	}

	public void placeShip(char[] chars) {
		for (int i = 0; i < chars.length; i++) {
			track.put((long) i, Utilities.charToByte(chars[i]));
		}
	}

	public void flushWrites() {
		writeRequests.forEach(req -> track.put(req.address, req.q));
		writeAtPreviousCycle.clear();
		writeAtPreviousCycle.addAll(writeRequests);
		writeRequests.clear();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Track)) {
			return false;
		}

		Track that = (Track) o;
		if (!track.equals(that.track)) {
			return false;
		}
		if (!writeAtPreviousCycle.equals(that.writeAtPreviousCycle)) {
			return false;
		}
		return writeRequests.equals(that.writeRequests);
	}

	@Override
	public int hashCode() {
		int result = track.hashCode();
		result = 31 * result + writeRequests.hashCode();
		return 31 * result + writeAtPreviousCycle.hashCode();
	}

	@Override
	public String toString() {
		return "Track{"
				+ "track=" + track
				+ ", writeRequests=" + writeRequests
				+ ", writeAtPreviousCycle=" + writeAtPreviousCycle
				+ '}';
	}

	private static final class TrackWrite {

		private final long address;
		private final byte q;

		private TrackWrite(long address, byte q) {
			this.address = address;
			this.q = q;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof TrackWrite)) {
				return false;
			}

			TrackWrite that = (TrackWrite) o;
			return address == that.address && q == that.q;
		}

		@Override
		public int hashCode() {
			int result = (int) (address ^ (address >>> 32));
			return 31 * result + q;
		}

		@Override
		public String toString() {
			return "TrackWrite{"
					+ "address=" + address
					+ ", q=" + q
					+ '}';
		}
	}
}
