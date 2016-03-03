package corewar.stadium.runtime;

import corewar.shared.Constants;
import corewar.shared.InstructionType;
import corewar.shared.Mode;
import corewar.stadium.StadiumShipFork;
import corewar.stadium.memory.InstructionParameter;
import corewar.stadium.memory.Register;

import java.util.BitSet;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;

public final class Executors {

	@FunctionalInterface
	public interface Executor {

		void accept(StadiumShipFork ship, InstructionParameter parameter, long currentCycle);
	}

	private Executors() {
	}

	public static void executeNop(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		// Nothing to do
	}

	public static void executeCrash(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		ship.crash();
	}

	public static void executeCheck(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		ship.getFlags().setZ(false);
		BitSet checkedZones = ship.getCheckedZones();
		int zone = (int) ((ship.getW0() - InstructionType.CHECK.getSize()) / Constants.CHECK_ZONE_SIZE);
		if (zone >= Constants.CHECKPOINTS_COUNT || (zone > 0 && !checkedZones.get(zone - 1))) {
			// Missed a zone
			ship.crash();
		} else if (!checkedZones.get(zone)) {
			checkedZones.set(zone);
			ship.setLastCheckCycle(currentCycle);
			ship.getFlags().setZ(true);
			if (checkedZones.cardinality() == Constants.CHECKPOINTS_COUNT) {
				ship.setFinished();
			}
		}
	}

	public static void executeFork(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		ship.getFlags().setZ(false);
		ship.fork();
	}

	public static void executeMode(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		Mode mode = Mode.fromValue(parameter.getM());
		if (mode != null) {
			ship.setCurrentMode(mode);
		}
	}

	public static void executeLoad(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		ship.getRegisters()[parameter.getRegX()].set(parameter.getN());
	}

	public static void executeSwp(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		Register[] registers = ship.getRegisters();
		Register rx = registers[parameter.getRegX()];
		Register ry = registers[parameter.getRegY()];

		int value = rx.asInt();
		rx.set(ry.asInt());
		ry.set(value);
	}

	public static void executeLdb(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		int offsetSrc = ship.getRegisters()[parameter.getRegX()].asInt();
		int idx = ship.getCurrentMode().getIdx();
		int offset = parameter.getMeta().getOffset();
		offsetSrc = Mode.offsetWithIdx(offsetSrc + offset, idx);
		long addressSrc = offsetAddressInTrack(ship.getPc(), offsetSrc);

		int offsetDst = (parameter.getN() + offset) % Constants.BUFFER_SIZE;

		ship.getBuffer().set(offsetDst, ship.getTrack().read(addressSrc));
	}

	public static void executeStb(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		int offsetDst = ship.getRegisters()[parameter.getRegX()].asInt();
		int idx = ship.getCurrentMode().getIdx();
		int offset = parameter.getMeta().getOffset();
		offsetDst = Mode.offsetWithIdx(offsetDst + offset, idx);
		long addressDst = offsetAddressInTrack(ship.getPc(), offsetDst);

		int offsetSrc = (parameter.getN() + offset) % Constants.BUFFER_SIZE;

		ship.getTrack().write(addressDst, ship.getBuffer().get(offsetSrc));
	}

	public static void executeLdr(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		int offsetSrc = ship.getRegisters()[parameter.getRegY()].asInt();
		int idx = ship.getCurrentMode().getIdx();
		int offset = parameter.getMeta().getOffset();
		offsetSrc = Mode.offsetWithIdx(offsetSrc + offset, idx);
		long addressSrc = offsetAddressInTrack(ship.getPc(), offsetSrc);

		ship.getRegisters()[parameter.getRegX()].set(offset, ship.getTrack().read(addressSrc));
	}

	public static void executeStr(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		int offsetDst = ship.getRegisters()[parameter.getRegX()].asInt();
		int idx = ship.getCurrentMode().getIdx();
		int offset = parameter.getMeta().getOffset();
		offsetDst = Mode.offsetWithIdx(offsetDst + offset, idx);
		long addressDst = offsetAddressInTrack(ship.getPc(), offsetDst);

		ship.getTrack().write(addressDst, ship.getRegisters()[parameter.getRegY()].get(offset));
	}

	public static void executeStat(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		// TODO
	}

	public static void executeMov(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		Register[] registers = ship.getRegisters();
		registers[parameter.getRegX()].set(registers[parameter.getRegY()].asInt());
	}

	public static void executeAdd(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		executeRegRegOp(ship, parameter, currentCycle, (x, y) -> x + y);
	}

	public static void executeSub(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		executeRegRegOp(ship, parameter, currentCycle, (x, y) -> x - y);
	}

	public static void executeOr(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		executeRegRegOp(ship, parameter, currentCycle, (x, y) -> x | y);
	}

	public static void executeAnd(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		executeRegRegOp(ship, parameter, currentCycle, (x, y) -> x & y);
	}

	public static void executeXor(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		executeRegRegOp(ship, parameter, currentCycle, (x, y) -> x ^ y);
	}

	public static void executeNeg(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		executeRegRegOp(ship, parameter, currentCycle, (x, y) -> -y);
	}

	public static void executeNot(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		executeRegRegOp(ship, parameter, currentCycle, (x, y) -> ~y);
	}

	public static void executeCmp(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		executeRegRegOp(ship, parameter, currentCycle, (x, y) -> x - y, false);
	}

	public static void executeAsr(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		Register rx = ship.getRegisters()[parameter.getRegX()];
		int res = Register.signInt(rx.asInt() >> parameter.getN());
		updateFlags(ship, res);
		rx.set(res);
	}

	public static void executeRol(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		Register rx = ship.getRegisters()[parameter.getRegX()];
		int value = rx.asUnsignedInt();
		int n = parameter.getN();
		int res = Register.signInt((value >>> (Constants.REGISTER_SIZE * 4 - n)) | (value << n));
		updateFlags(ship, res);
		rx.set(res);
	}

	public static void executeWrite(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		// TODO
	}

	public static void executeB(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		executeBranch(ship, parameter, currentCycle, () -> true);
	}

	public static void executeBz(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		executeBranch(ship, parameter, currentCycle, () -> ship.getFlags().getZ());
	}

	public static void executeBs(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		executeBranch(ship, parameter, currentCycle, () -> ship.getFlags().getS());
	}

	public static void executeBnz(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		executeBranch(ship, parameter, currentCycle, () -> !ship.getFlags().getZ());
	}

	public static void executeCmpi(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		int x = ship.getRegisters()[parameter.getRegX()].asInt();
		updateFlags(ship, Register.signInt(x - parameter.getN()));
	}

	public static void executeAddi(StadiumShipFork ship, InstructionParameter parameter, long currentCycle) {
		Register rx = ship.getRegisters()[parameter.getRegX()];
		int res = Register.signInt(rx.asInt() + parameter.getN());
		updateFlags(ship, res);
		rx.set(res);
	}

	private static long offsetAddressInTrack(long address, int offset) {
		long unsafeAddress = address + offset;
		if(unsafeAddress < 0) {
			return Constants.TRACK_SIZE + unsafeAddress;
		}
		return unsafeAddress % Constants.TRACK_SIZE;
	}

	private static void executeRegRegOp(StadiumShipFork ship, InstructionParameter parameter, long currentCycle, BiFunction<Integer, Integer, Integer> op) {
		executeRegRegOp(ship, parameter, currentCycle, op, true);
	}

	private static void executeRegRegOp(StadiumShipFork ship, InstructionParameter parameter, long currentCycle, BiFunction<Integer, Integer, Integer> op, boolean storeResult) {
		Register[] registers = ship.getRegisters();
		int x = registers[parameter.getRegX()].asInt();
		int y = registers[parameter.getRegY()].asInt();
		int res = Register.signInt(op.apply(x, y));
		updateFlags(ship, res);
		if (storeResult) {
			registers[parameter.getRegX()].set(res);
		}
	}

	private static void updateFlags(StadiumShipFork ship, int res) {
		ship.getFlags().setZ(res == 0);
		ship.getFlags().setS(res < 0);
	}

	private static void executeBranch(StadiumShipFork ship, InstructionParameter parameter, long currentCycle, BooleanSupplier test) {
		if (test.getAsBoolean()) {
			int offset = ship.getRegisters()[parameter.getRegX()].asInt();
			int idx = ship.getCurrentMode().getIdx();
			offset = Mode.offsetWithIdx(offset, idx);
			ship.incrementPcAndW0(offset);
		}
	}
}
