package corewar.stadium.memory;

import corewar.shared.Constants;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Register: (value: 0x4321)
 * [i]: w: q
 * ---------
 * [0]: 0: 1
 * [1]: 1: 2
 * [2]: 2: 3
 * [3]: 3: 4
 */
public final class Register extends Buffer {

	private Register() {
		super(Constants.REGISTER_SIZE);
	}

	public static int signInt(int value) {
		int shift = 32 - 4 * Constants.REGISTER_SIZE;
		return (value << shift) >> shift;
	}

	public static Register createRegister() {
		return new Register();
	}

	public int asInt() {
		return signInt(asUnsignedInt());
	}

	public int asUnsignedInt() {
		int res = 0;
		for (int i = Constants.REGISTER_SIZE - 1; i >= 0; i--) {
			res = (res << 4) | get(i);
		}
		return res;
	}

	public void set(int value) {
		for (int i = 0; i < Constants.REGISTER_SIZE; i++) {
			set(i, (byte) (value >> (4 * i)));
		}
	}

	@Override
	public void copyTo(Buffer dst) {
		checkArgument(dst instanceof Register, "Can only copy to another Register");
		System.arraycopy(buffer, 0, dst.buffer, 0, buffer.length);
	}
}
