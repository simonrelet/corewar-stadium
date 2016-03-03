package corewar.stadium.memory;

import corewar.shared.Constants;
import corewar.shared.Utilities;

import java.util.Arrays;

public class Buffer {

	private static final int NIBBLE_MASK = 0x0f;

	protected final byte[] buffer;

	public Buffer() {
		this(Constants.BUFFER_SIZE);
	}

	protected Buffer(int size) {
		this.buffer = new byte[size];
	}

	public byte get(int index) {
		checkIndex(index, buffer.length);
		return buffer[index];
	}

	public void set(int index, byte q) {
		checkIndex(index, buffer.length);
		buffer[index] = (byte) (NIBBLE_MASK & q);
	}

	protected static void checkIndex(int index, int size) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("index=" + index + ", size=" + size);
		}
	}

	public byte[] asBytes() {
		return Arrays.copyOf(buffer, buffer.length);
	}

	public Buffer duplicate() {
		Buffer other = new Buffer(buffer.length);
		System.arraycopy(buffer, 0, other.buffer, 0, buffer.length);
		return other;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Buffer)) {
			return false;
		}

		Buffer that = (Buffer) o;
		return Arrays.equals(buffer, that.buffer);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(buffer);
	}

	@Override
	public String toString() {
		return getDisplayable();
	}

	protected String getDisplayable() {
		StringBuilder str = new StringBuilder();
		for (int i = buffer.length - 1; i >= 0; i--) {
			str.append(Utilities.byteToChar(buffer[i]));
		}
		return str.toString();
	}
}
