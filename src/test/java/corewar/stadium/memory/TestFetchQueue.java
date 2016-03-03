package corewar.stadium.memory;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestFetchQueue {

	private FetchQueue fetchQueue;

	@Before
	public void setUp() {
		fetchQueue = new FetchQueue();
	}

	@Test
	public void testEmpty() {
		assertEquals(0, fetchQueue.size());
	}

	@Test
	public void testAppend() {
		for (int i = 0; i < 4; i++) {
			fetchQueue.append((byte) i);
		}

		assertEquals(4, fetchQueue.size());

		for (int i = 0; i < 4; i++) {
			assertEquals(i, fetchQueue.get(i));
		}
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetFails() {
		fetchQueue.append((byte) 0x0f);
		fetchQueue.get(1);
	}

	@Test
	public void testClear() {
		for (int i = 0; i < 4; i++) {
			fetchQueue.append((byte) i);
		}
		fetchQueue.clear();
		assertEquals(0, fetchQueue.size());
	}

	@Test
	public void testAsBytesLimitedSize() {
		for (int i = 0; i < 4; i++) {
			fetchQueue.append((byte) i);
		}

		byte[] bytes = fetchQueue.asBytes();
		assertEquals(4, bytes.length);
		for (int i = 0; i < 4; i++) {
			assertEquals(i, bytes[i]);
		}
	}

	@Test
	public void testExtractIntNibble() {
		fetchQueue.append((byte) 0x00);
		fetchQueue.append((byte) 0x00);
		fetchQueue.append((byte) 0x0f);

		assertEquals(-1, fetchQueue.extractInt(2, 1));
	}

	@Test
	public void testExtractIntShort() {
		fetchQueue.append((byte) 0x00);
		fetchQueue.append((byte) 0x00);
		fetchQueue.append((byte) 0x0f);
		fetchQueue.append((byte) 0x07);
		fetchQueue.append((byte) 0x00);
		fetchQueue.append((byte) 0x00);

		assertEquals(0x7f, fetchQueue.extractInt(2, 2));
	}

	@Test
	public void testExtractInt() {
		fetchQueue.append((byte) 0x00);
		fetchQueue.append((byte) 0x00);
		fetchQueue.append((byte) 0x00);
		fetchQueue.append((byte) 0x01);
		fetchQueue.append((byte) 0x02);
		fetchQueue.append((byte) 0x03);
		fetchQueue.append((byte) 0x04);

		assertEquals(0x4321, fetchQueue.extractInt(3, 4));
	}

	@Test
	public void testExtractUnsignedIntNibble() {
		fetchQueue.append((byte) 0x00);
		fetchQueue.append((byte) 0x00);
		fetchQueue.append((byte) 0x0f);

		assertEquals(0x0f, fetchQueue.extractUnsignedInt(2, 1));
	}

	@Test
	public void testExtractUnsignedIntShort() {
		fetchQueue.append((byte) 0x00);
		fetchQueue.append((byte) 0x00);
		fetchQueue.append((byte) 0x01);
		fetchQueue.append((byte) 0x08);
		fetchQueue.append((byte) 0x00);
		fetchQueue.append((byte) 0x00);

		assertEquals(0x81, fetchQueue.extractUnsignedInt(2, 2));
	}

	@Test
	public void testExtractUnsignedInt() {
		fetchQueue.append((byte) 0x00);
		fetchQueue.append((byte) 0x00);
		fetchQueue.append((byte) 0x00);
		fetchQueue.append((byte) 0x05);
		fetchQueue.append((byte) 0x06);
		fetchQueue.append((byte) 0x07);
		fetchQueue.append((byte) 0x08);

		assertEquals(0x8765, fetchQueue.extractUnsignedInt(3, 4));
	}
}
