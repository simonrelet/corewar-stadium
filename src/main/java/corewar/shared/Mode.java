package corewar.shared;

import javax.annotation.Nullable;

public enum Mode {

	FEISAR(0, 500),
	GOTEKI45(1, 256),
	AGSYSTEMS(2, 288),
	AURICOM(3, 320),
	ASSEGAI(4, 352),
	PIRANHA(5, 384),
	QIREX(6, 416),
	ICARAS(7, 448),
	ROCKET(8, 500),
	MISSILE(9, 416),
	MINE(10, 32),
	PLASMA(11, 65535),
	MINIPLASMA(12, 16384);

	public static final Mode DEFAULT_MODE = FEISAR;

	private final int value;
	private final int idx;

	Mode(int value, int idx) {
		this.value = value;
		this.idx = idx;
	}

	@Nullable
	public static Mode fromValue(int value) {
		return value < 0 || value >= Mode.values().length ? null : Mode.values()[value];
	}

	public static int offsetWithIdx(int offset, int idx) {
		int tmp = (offset + idx) % (2 * idx);
		if(tmp < 0) {
			tmp = 2 * idx + tmp;
		}
		return tmp - idx;
	}

	public int getValue() {
		return value;
	}

	public int getIdx() {
		return idx;
	}
}
