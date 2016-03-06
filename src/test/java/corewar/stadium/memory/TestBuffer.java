package corewar.stadium.memory;

import corewar.shared.Constants;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestBuffer {

	private Buffer buffer;

	@Before
	public void setUp() {
		buffer = Buffer.createBuffer();
	}

	@Test
	public void testEmpty() {
		for (int i = 0; i < Constants.BUFFER_SIZE; i++) {
			assertEquals(0, buffer.get(i));
		}
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetIndexOutOfBounds() {
		buffer.get(Constants.BUFFER_SIZE);
	}

	@Test
	public void testSet() {
		for (int i = 0; i < Constants.BUFFER_SIZE; i++) {
			buffer.set(i, (byte) 0x04);
		}

		for (int i = 0; i < Constants.BUFFER_SIZE; i++) {
			byte actual = buffer.get(i);
			assertEquals(0x04, actual);
		}
	}

	@Test
	public void testAsBytes() {
		for (int i = 0; i < Constants.BUFFER_SIZE; i++) {
			buffer.set(i, (byte) 0x04);
		}

		byte[] bytes = buffer.asBytes();
		assertEquals(Constants.BUFFER_SIZE, bytes.length);
		for (byte aByte : bytes) {
			assertEquals(0x04, aByte);
		}
	}
}
