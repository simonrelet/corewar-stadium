package corewar.stadium.runtime.logs;

import corewar.shared.HexIntConverter;

public final class ReadWriteLog extends StadiumLog {

	private final long address;
	private final byte q;

	private ReadWriteLog(int shipId, Type type, long cycle, boolean blueArrow, boolean rails,
			long address, byte q) {
		super(shipId, type, cycle, blueArrow, rails);
		this.address = address;
		this.q = q;
	}

	public static ReadWriteLog createRead(int shipId, long cycle, long address, boolean blueArrow,
			boolean rails, byte q) {
		return new ReadWriteLog(shipId, Type.READ, cycle, blueArrow, rails, address, q);
	}

	public static ReadWriteLog createWrite(int shipId, long cycle, long address, boolean blueArrow,
			boolean rails, byte q) {
		return new ReadWriteLog(shipId, Type.WRITE, cycle, blueArrow, rails, address, q);
	}

	@Override
	protected StringBuilder getContent(int verbosity) {
		StringBuilder stringBuilder = super.getContent(verbosity)
				.append(",\"address\":")
				.append(address);
		if (verbosity >= 4) {
			stringBuilder
					.append(",\"nibble\":\"")
					.append(HexIntConverter.intToHexWithPrefix(q, 1))
					.append("\"");
		}
		return stringBuilder;
	}
}
