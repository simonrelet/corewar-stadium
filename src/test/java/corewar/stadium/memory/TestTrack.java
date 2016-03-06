package corewar.stadium.memory;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestTrack {

	private Track track;

	@Before
	public void setUp() throws Exception {
		track = Track.create();
	}

	@Test
	public void testReadEmpty() {
		assertEquals(0, track.read(0));
		assertEquals(0, track.read(42));
		assertEquals(0, track.read(1000));
	}

	@Test
	public void testWriteIsBuffered() {
		track.write(0, (byte) 0x04);
		track.write(1, (byte) 0x05);
		track.write(2, (byte) 0x06);

		assertEquals(0, track.read(0));
		assertEquals(0, track.read(1));
		assertEquals(0, track.read(2));

		assertFalse(track.wroteAtPreviousCycle(0));
		assertFalse(track.wroteAtPreviousCycle(1));
		assertFalse(track.wroteAtPreviousCycle(2));
	}

	@Test
	public void testFlushWrites() {
		track.write(0, (byte) 0x04);
		track.write(1, (byte) 0x05);
		track.write(2, (byte) 0x06);
		track.flushWrites();

		assertEquals(0x04, track.read(0));
		assertEquals(0x05, track.read(1));
		assertEquals(0x06, track.read(2));

		assertTrue(track.wroteAtPreviousCycle(0));
		assertTrue(track.wroteAtPreviousCycle(1));
		assertTrue(track.wroteAtPreviousCycle(2));
	}

	@Test
	public void testFlush() {
		track.write(0, (byte) 0x04);
		track.write(1, (byte) 0x05);
		track.write(2, (byte) 0x06);
		track.flushWrites();
		track.flushWrites();

		assertFalse(track.wroteAtPreviousCycle(0));
		assertFalse(track.wroteAtPreviousCycle(1));
		assertFalse(track.wroteAtPreviousCycle(2));
	}

	@Test
	public void testPlaceShip() {
		String ship = "0123456789";
		track.placeShip(ship.toCharArray());
		for (int i = 0; i < 10; i++) {
			assertEquals(i, track.read(i));
		}
		assertEquals(0x00, track.read(10));
	}
}
