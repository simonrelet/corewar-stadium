package corewar.stadium.memory;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestRegister {

	private Register register;

	@Before
	public void setUp() {
		register = new Register();
	}

	@Test
	public void testAsIntEmpty() {
		assertEquals(0, register.asInt());
		assertEquals(0, register.asUnsignedInt());
	}

	@Test
	public void testAsIntFilled() {
		for (int i = 0; i < 4; i++) {
			register.set(i, (byte) 0x0f);
		}

		assertEquals(-1, register.asInt());
		assertEquals(0x0000ffff, register.asUnsignedInt());
	}

	@Test
	public void testAsIntMin() {
		register.set(3, (byte) 0x08);
		assertEquals(0xffff8000, register.asInt());
		assertEquals(0x00008000, register.asUnsignedInt());
	}

	@Test
	public void testAsIntMax() {
		for (int i = 0; i < 3; i++) {
			register.set(i, (byte) 0x0f);
		}
		register.set(3, (byte) 0x07);
		assertEquals(0x00007fff, register.asInt());
		assertEquals(0x00007fff, register.asUnsignedInt());
	}
}
