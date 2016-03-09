package corewar.stadium.runtime.logs;

public final class ReadWriteLog extends StadiumLog {

	private final long address;

	private ReadWriteLog(int shipId, Type type, long cycle, boolean blueArrow, boolean rails,
			long address) {
		super(shipId, type, cycle, blueArrow, rails);
		this.address = address;
	}

	public static ReadWriteLog createRead(int shipId, long cycle, long address, boolean blueArrow,
			boolean rails) {
		return new ReadWriteLog(shipId, Type.READ, cycle, blueArrow, rails, address);
	}

	public static ReadWriteLog createWrite(int shipId, long cycle, long address, boolean blueArrow,
			boolean rails) {
		return new ReadWriteLog(shipId, Type.WRITE, cycle, blueArrow, rails, address);
	}

	@Override
	protected StringBuilder getContent() {
		return super.getContent()
				.append(",\"address\":")
				.append(address);
	}
}
