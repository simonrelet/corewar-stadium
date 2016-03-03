package corewar.shared;

public final class Constants {

	// Platform dependencies
	public static final String SHORTCUT = System.getProperty("os.name").startsWith("Mac") ? "cmd" : "ctrl";
	public static final String REGEX_END_OF_LINE = "\\r?\\n";
	public static final String MONOSPACE_FONT = "Consolas";

	// Others
	public static final String HEX_PREFIX = "0x";

	// Pit
	public static final String DEFAULT_SHIP_NAME = "No name";
	public static final char COMMENT_CHAR = '#';
	public static final String NON_TEXT_PADDING_CHAR = "\0";
	public static final int NAME_MAX_CHARACTER_COUNT = 64;
	public static final int COMMENT_MAX_CHARACTER_COUNT = 256;

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

	public static final String ZORK = "# This is an example ship but is way too slow...\n\n"
												 + "    .name \"Zork\"\n"
												 + "    .comment \"This is the first ship ever.\"\n"
												 + "\n"
												 + "    ll r0, 0x2ecf\n"
												 + "    ll r1, 0x13e0\n"
												 + "    ll r2, to - from1\n"
												 + "    ll r3, to - from2 + 4\n"
												 + "    str [r2], r0\n"
												 + "from1:\n"
												 + "    str [r3], r1\n"
												 + "from2:\n"
												 + "to:\n";

	private Constants() {
	}
}
