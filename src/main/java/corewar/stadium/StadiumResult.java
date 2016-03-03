package corewar.stadium;

import java.util.List;

public class StadiumResult {

	private final List<StadiumShipResult> result;

	public StadiumResult(List<StadiumShipResult> result) {
		this.result = result;
	}

	public List<StadiumShipResult> getResult() {
		return result;
	}
}
