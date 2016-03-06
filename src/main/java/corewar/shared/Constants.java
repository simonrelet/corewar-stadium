package corewar.shared;

public final class Constants {

	// Others
	public static final String HEX_PREFIX = "0x";

	public static final int NAME_MAX_CHARACTER_COUNT = 32;
	public static final int COMMENT_MAX_CHARACTER_COUNT = 128;

	// Stadium
	public static final int TRACK_SIZE = (int) Math.pow(2, 16); // 65 536
	public static final int LAP_NUMBER = 3;
	public static final int CHECKPOINTS_PER_LAP = 8;
	public static final int CHECK_ZONE_SIZE = TRACK_SIZE / CHECKPOINTS_PER_LAP;
	public static final int CHECKPOINTS_COUNT = CHECKPOINTS_PER_LAP * LAP_NUMBER;
	public static final int CHECKPOINT_DELAY = 18420;

	public static final int NB_REGISTERS = 16;
	public static final int REGISTER_SIZE = 4;
	public static final int BUFFER_SIZE = 64;
	public static final int FETCH_QUEUE_SIZE = 8;
	public static final int BLUE_ARROW_SPACING = 64;

	private Constants() {
	}
}
