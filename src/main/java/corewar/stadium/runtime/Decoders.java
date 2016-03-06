package corewar.stadium.runtime;

import corewar.stadium.memory.FetchQueue;
import corewar.stadium.memory.InstructionParameters;

import java.util.function.Function;

public final class Decoders {

	@FunctionalInterface
	public interface Decoder extends Function<FetchQueue, InstructionParameters> {

	}

	private Decoders() {
	}

	public static InstructionParameters decodeNothingToDo(FetchQueue fetchQueue) {
		return InstructionParameters.create();
	}

	public static InstructionParameters decodeRegReg(FetchQueue fetchQueue) {
		InstructionParameters res = InstructionParameters.create();
		res.setRegX(fetchQueue.get(1));
		res.setRegY(fetchQueue.get(2));
		return res;
	}

	public static InstructionParameters decodeLdrStr(FetchQueue fetchQueue) {
		InstructionParameters res = InstructionParameters.create();
		res.setRegX(fetchQueue.get(1));
		res.setRegY(fetchQueue.get(2));
		res.getMeta().setCount(3);
		return res;
	}

	public static InstructionParameters decodeRegN(FetchQueue fetchQueue) {
		InstructionParameters res = InstructionParameters.create();
		res.setRegX(fetchQueue.get(1));
		res.setN(fetchQueue.extractUnsignedInt(2, 1));
		return res;
	}

	public static InstructionParameters decodeFRegN(FetchQueue fetchQueue) {
		InstructionParameters res = InstructionParameters.create();
		res.setRegX(fetchQueue.get(2));
		res.setN(fetchQueue.extractInt(3, 1));
		return res;
	}

	public static InstructionParameters decodeStat(FetchQueue fetchQueue) {
		InstructionParameters res = InstructionParameters.create();
		res.setRegX(fetchQueue.get(2));
		res.setN(fetchQueue.extractUnsignedInt(3, 1));
		return res;
	}

	public static InstructionParameters decodeLdbStb(FetchQueue fetchQueue) {
		InstructionParameters res = InstructionParameters.create();
		res.setRegX(fetchQueue.get(2));
		res.setN(fetchQueue.extractUnsignedInt(3, 2));
		res.setM(fetchQueue.extractUnsignedInt(5, 2));
		res.getMeta().setCount(res.getM() - 1);
		return res;
	}

	public static InstructionParameters decodeLc(FetchQueue fetchQueue) {
		InstructionParameters res = InstructionParameters.create();
		res.setRegX(fetchQueue.get(2));
		res.setN(fetchQueue.extractInt(3, 2));
		return res;
	}

	public static InstructionParameters decodeLl(FetchQueue fetchQueue) {
		InstructionParameters res = InstructionParameters.create();
		res.setRegX(fetchQueue.get(2));
		res.setN(fetchQueue.extractInt(3, 4));
		return res;
	}

	public static InstructionParameters decodeSwp(FetchQueue fetchQueue) {
		InstructionParameters res = InstructionParameters.create();
		res.setRegX(fetchQueue.get(2));
		res.setRegY(fetchQueue.get(3));
		return res;
	}

	public static InstructionParameters decodeFReg(FetchQueue fetchQueue) {
		InstructionParameters res = InstructionParameters.create();
		res.setRegX(fetchQueue.get(2));
		return res;
	}

	public static InstructionParameters decodeMode(FetchQueue fetchQueue) {
		InstructionParameters res = InstructionParameters.create();
		res.setM(fetchQueue.extractUnsignedInt(2, 1));
		return res;
	}
}
