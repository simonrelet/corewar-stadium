package corewar.stadium;

public final class StadiumResult {

	public enum FinishType {
		FINISHED,
		MISSED_CHECK,
		CRASHED
	}

	private final FinishType finishType;
	private final long cycle;

	private StadiumResult(FinishType finishType, long cycle) {
		this.finishType = finishType;
		this.cycle = cycle;
	}

	public static StadiumResult create(FinishType finishType, long cycle) {
		return new StadiumResult(finishType, cycle);
	}

	public FinishType getFinishType() {
		return finishType;
	}

	public long getCycle() {
		return cycle;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof StadiumResult)) {
			return false;
		}

		StadiumResult that = (StadiumResult) o;
		return cycle == that.cycle && finishType == that.finishType;
	}

	@Override
	public int hashCode() {
		int result = finishType.hashCode();
		result = 31 * result + (int) (cycle ^ (cycle >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "StadiumShipResult{" +
				"finishType=" + finishType +
				", cycle=" + cycle +
				'}';
	}
}
