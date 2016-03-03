package corewar.stadium.runtime;

import corewar.stadium.memory.FetchQueue;
import corewar.stadium.memory.InstructionParameter;

import java.util.function.Function;

public final class Decoders {

	@FunctionalInterface
	public interface Decoder extends Function<FetchQueue, InstructionParameter> {

	}

	private Decoders() {
	}

	public static InstructionParameter decodeNothingToDo(FetchQueue fetchQueue) {
		return new InstructionParameter();
	}

	public static InstructionParameter decodeRegReg(FetchQueue fetchQueue) {
		InstructionParameter res = new InstructionParameter();
		res.setRegX(fetchQueue.get(1));
		res.setRegY(fetchQueue.get(2));
		return res;
	}

	public static InstructionParameter decodeLdrStr(FetchQueue fetchQueue) {
		InstructionParameter res = new InstructionParameter();
		res.setRegX(fetchQueue.get(1));
		res.setRegY(fetchQueue.get(2));
		res.getMeta().setCount(3);
		return res;
	}

	public static InstructionParameter decodeRegN(FetchQueue fetchQueue) {
		InstructionParameter res = new InstructionParameter();
		res.setRegX(fetchQueue.get(1));
		res.setN(fetchQueue.extractUnsignedInt(2, 1));
		return res;
	}

	public static InstructionParameter decodeFRegN(FetchQueue fetchQueue) {
		InstructionParameter res = new InstructionParameter();
		res.setRegX(fetchQueue.get(2));
		res.setN(fetchQueue.extractInt(3, 1));
		return res;
	}

	public static InstructionParameter decodeStat(FetchQueue fetchQueue) {
		InstructionParameter res = new InstructionParameter();
		res.setRegX(fetchQueue.get(2));
		res.setN(fetchQueue.extractUnsignedInt(3, 1));
		return res;
	}

	public static InstructionParameter decodeLdbStb(FetchQueue fetchQueue) {
		InstructionParameter res = new InstructionParameter();
		res.setRegX(fetchQueue.get(2));
		res.setN(fetchQueue.extractUnsignedInt(3, 2));
		res.setM(fetchQueue.extractUnsignedInt(5, 2));
		res.getMeta().setCount(res.getM() - 1);
		return res;
	}

	public static InstructionParameter decodeLc(FetchQueue fetchQueue) {
		InstructionParameter res = new InstructionParameter();
		res.setRegX(fetchQueue.get(2));
		res.setN(fetchQueue.extractInt(3, 2));
		return res;
	}

	public static InstructionParameter decodeLl(FetchQueue fetchQueue) {
		InstructionParameter res = new InstructionParameter();
		res.setRegX(fetchQueue.get(2));
		res.setN(fetchQueue.extractInt(3, 4));
		return res;
	}

	public static InstructionParameter decodeSwp(FetchQueue fetchQueue) {
		InstructionParameter res = new InstructionParameter();
		res.setRegX(fetchQueue.get(2));
		res.setRegY(fetchQueue.get(3));
		return res;
	}

	public static InstructionParameter decodeFReg(FetchQueue fetchQueue) {
		InstructionParameter res = new InstructionParameter();
		res.setRegX(fetchQueue.get(2));
		return res;
	}

	public static InstructionParameter decodeMode(FetchQueue fetchQueue) {
		InstructionParameter res = new InstructionParameter();
		res.setM(fetchQueue.extractUnsignedInt(2, 1));
		return res;
	}
}
