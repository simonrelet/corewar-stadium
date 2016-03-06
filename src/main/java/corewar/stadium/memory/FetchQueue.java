package corewar.stadium.memory;

import corewar.shared.Constants;
import corewar.shared.Utilities;

import java.util.Arrays;

/**
 * If memory:
 * [@]: q
 * ------
 * [0]: a
 * [1]: b
 * [2]: c
 * [3]: d
 *
 * Then fetch queue:
 * [i]: q
 * ------
 * [0]: a
 * [1]: b
 * [2]: c
 * [3]: d
 */
public final class FetchQueue extends Buffer {

	private int currentSize;

	private FetchQueue() {
		super(Constants.FETCH_QUEUE_SIZE);
	}

	public static FetchQueue createFetchQueue() {
		return new FetchQueue();
	}

	@Override
	public byte get(int index) {
		checkIndex(index, currentSize);
		return super.get(index);
	}

	public void append(byte q) {
		set(currentSize++, q);
	}

	public int size() {
		return currentSize;
	}

	public void clear() {
		// Setting to 0 is not necessary but we do it for debug readability.
		for (int i = 0; i < currentSize; i++) {
			set(i, (byte) 0x00);
		}
		currentSize = 0;
	}

	public int extractInt(int start, int count) {
		int res = extractUnsignedInt(start, count);
		int shift = 32 - 4 * count;
		return (res << shift) >> shift;
	}

	/**
	 * If fetch queue:
	 * [i]: q
	 * ------
	 * [0]: 1
	 * [1]: 2
	 * [2]: 3
	 * [2]: 4
	 * [4]: 5
	 *
	 * extractUnsignedInt(1, 4) == 0x5432
	 *
	 * @param start The start index.
	 * @param count The nibble count to extract.
	 * @return The corresponding value.
	 */
	public int extractUnsignedInt(int start, int count) {
		int res = 0;
		for (int i = start + count - 1; i >= start; i--) {
			res = (res << 4) | get(i);
		}
		return res;
	}

	@Override
	public byte[] asBytes() {
		return Arrays.copyOf(buffer, currentSize);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof FetchQueue)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}

		FetchQueue that = (FetchQueue) o;
		return currentSize == that.currentSize;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + currentSize;
		return result;
	}

	@Override
	protected String getDisplayable() {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < currentSize; i++) {
			str.append(Utilities.byteToChar(buffer[i]));
		}
		return str.toString();
	}
}
