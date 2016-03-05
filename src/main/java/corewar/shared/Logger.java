package corewar.shared;

import corewar.stadium.Stadium;
import corewar.stadium.StadiumResult;
import corewar.stadium.StadiumShipResult;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

public class Logger {

	private static Stadium stadium;

	private Logger() {
	}

	public static void logError(String msg) {
		System.out.println("{\"errors\":[{\"msg\":\"" + msg + "\"}]}");
	}

	public static void logResult(StadiumResult result) {
		StadiumShipResult res = getRelevant(result.getResult());
		String stringBuilder = "{\"res\":\"" +
									  (res.getFinishType() == StadiumShipResult.FinishType.FINISHED ? "finished" : "crashed") +
									  "\",\"cycles\":\"" +
									  res.getCycle() +
									  "\"}";
		System.out.println(stringBuilder);
	}

	private static StadiumShipResult getRelevant(List<StadiumShipResult> result) {
		Optional<StadiumShipResult> max = result.stream().max((o1, o2) -> o1.getCycle() - o2.getCycle());
		checkArgument(max.isPresent(), "There should be at least one ship");
		return max.get();
	}

	public static void setStadium(Stadium stadium) {
		Logger.stadium = stadium;
	}

	public static void logInfo(String msg) {
//		System.out.println(stadium.getCycle() + " | " + msg);
	}
}
