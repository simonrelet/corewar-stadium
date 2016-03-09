package corewar.stadium.runtime;

import corewar.shared.Constants;
import corewar.shared.InstructionType;
import corewar.shared.Mode;
import corewar.stadium.Stadium;
import corewar.stadium.StadiumShip;
import corewar.stadium.memory.InstructionParameters;
import corewar.stadium.memory.Register;
import corewar.stadium.runtime.logs.ExecuteLog;
import corewar.stadium.runtime.logs.ReadWriteLog;

import java.util.BitSet;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;

import static corewar.shared.HexIntConverter.intToHexWithPrefix;

public final class Executors {

	@FunctionalInterface
	public interface Executor {

		void accept(StadiumShip ship, InstructionParameters parameter, long currentCycle);
	}

	private Executors() {
	}

	public static void executeNop(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		// Nothing to do
	}

	public static void executeCrash(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		Stadium stadium = ship.getStadium();
		ExecuteLog log = ExecuteLog.create(ship.getId(), stadium.getCycle(), ship.onBlueArrow(),
				ship.onRails(), "crashed");
		stadium.getLogger().log(log);
		ship.crash();
	}

	public static void executeCheck(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		ship.getFlags().setZ(false);
		BitSet checkedZones = ship.getCheckedZones();
		int zone = (int) ((ship.getW0() - InstructionType.CHECK.getSize())
				/ Constants.CHECK_ZONE_SIZE);
		if (zone >= Constants.CHECKPOINTS_COUNT
				|| (zone > 0 && !checkedZones.get(zone - 1))) {
			// Missed a zone
			ship.crash();
		} else if (!checkedZones.get(zone)) {
			checkedZones.set(zone);
			ship.setLastCheckCycle(currentCycle);
			ship.getFlags().setZ(true);
			if (checkedZones.cardinality() == Constants.CHECKPOINTS_COUNT) {
				ship.setFinished();
			}
			Stadium stadium = ship.getStadium();
			ExecuteLog log = ExecuteLog.create(ship.getId(), stadium.getCycle(), ship.onBlueArrow(),
					ship.onRails(), "checked zone " + zone);
			stadium.getLogger().log(log);
		}
	}

	public static void executeFork(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		ship.getFlags().setZ(false);
		ship.fork();
	}

	public static void executeMode(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		Mode mode = Mode.fromValue(parameter.getM());
		if (mode != null) {
			ship.setCurrentMode(mode);
		}
	}

	public static void executeLoad(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		ship.getRegisters()[parameter.getRegX()].set(parameter.getN());
	}

	public static void executeSwp(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		Register[] registers = ship.getRegisters();
		Register rx = registers[parameter.getRegX()];
		Register ry = registers[parameter.getRegY()];

		int value = rx.asInt();
		rx.set(ry.asInt());
		ry.set(value);
	}

	public static void executeLdb(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		int offsetSrc = ship.getRegisters()[parameter.getRegX()].asInt();
		int idx = ship.getCurrentMode().getIdx();
		int offset = parameter.getMeta().getOffset();
		offsetSrc = Mode.offsetWithIdx(offsetSrc + offset, idx);
		long addressSrc = offsetAddressInTrack(ship.getPc(), offsetSrc);

		int offsetDst = (parameter.getN() + offset) % Constants.BUFFER_SIZE;

		byte q = ship.getStadium().getTrack().read(addressSrc);
		ship.getBuffer().set(offsetDst, q);
	}

	public static void executeStb(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		int offsetDst = ship.getRegisters()[parameter.getRegX()].asInt();
		int idx = ship.getCurrentMode().getIdx();
		int offset = parameter.getMeta().getOffset();
		offsetDst = Mode.offsetWithIdx(offsetDst + offset, idx);
		long addressDst = offsetAddressInTrack(ship.getPc(), offsetDst);

		int offsetSrc = (parameter.getN() + offset) % Constants.BUFFER_SIZE;

		byte q = ship.getBuffer().get(offsetSrc);
		Stadium stadium = ship.getStadium();
		stadium.getTrack().write(addressDst, q);

		ReadWriteLog log = ReadWriteLog.createWrite(ship.getId(), currentCycle, addressDst,
				ship.onBlueArrow(), ship.onRails());
		stadium.getLogger().log(log);
	}

	public static void executeLdr(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		int offsetSrc = ship.getRegisters()[parameter.getRegY()].asInt();
		int idx = ship.getCurrentMode().getIdx();
		int offset = parameter.getMeta().getOffset();
		offsetSrc = Mode.offsetWithIdx(offsetSrc + offset, idx);
		long addressSrc = offsetAddressInTrack(ship.getPc(), offsetSrc);

		byte q = ship.getStadium().getTrack().read(addressSrc);
		ship.getRegisters()[parameter.getRegX()].set(offset, q);
	}

	public static void executeStr(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		int offsetDst = ship.getRegisters()[parameter.getRegX()].asInt();
		int idx = ship.getCurrentMode().getIdx();
		int offset = parameter.getMeta().getOffset();
		offsetDst = Mode.offsetWithIdx(offsetDst + offset, idx);
		long addressDst = offsetAddressInTrack(ship.getPc(), offsetDst);

		byte q = ship.getRegisters()[parameter.getRegY()].get(offset);
		Stadium stadium = ship.getStadium();
		stadium.getTrack().write(addressDst, q);

		ReadWriteLog log = ReadWriteLog.createWrite(ship.getId(), currentCycle, addressDst,
				ship.onBlueArrow(), ship.onRails());
		stadium.getLogger().log(log);
	}

	public static void executeStat(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		// TODO
	}

	public static void executeMov(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		Register[] registers = ship.getRegisters();
		int value = registers[parameter.getRegY()].asInt();
		registers[parameter.getRegX()].set(value);
	}

	public static void executeAdd(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		executeRegRegOp(ship, parameter, currentCycle, (x, y) -> x + y);
	}

	public static void executeSub(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		executeRegRegOp(ship, parameter, currentCycle, (x, y) -> x - y);
	}

	public static void executeOr(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		executeRegRegOp(ship, parameter, currentCycle, (x, y) -> x | y);
	}

	public static void executeAnd(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		executeRegRegOp(ship, parameter, currentCycle, (x, y) -> x & y);
	}

	public static void executeXor(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		executeRegRegOp(ship, parameter, currentCycle, (x, y) -> x ^ y);
	}

	public static void executeNeg(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		executeRegRegOp(ship, parameter, currentCycle, (x, y) -> -y);
	}

	public static void executeNot(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		executeRegRegOp(ship, parameter, currentCycle, (x, y) -> ~y);
	}

	public static void executeCmp(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		executeRegRegOp(ship, parameter, currentCycle, (x, y) -> x - y, false);
	}

	public static void executeAsr(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		Register rx = ship.getRegisters()[parameter.getRegX()];
		int res = Register.signInt(rx.asInt() >> parameter.getN());
		updateFlags(ship, res);
		rx.set(res);
	}

	public static void executeRol(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		Register rx = ship.getRegisters()[parameter.getRegX()];
		int value = rx.asUnsignedInt();
		int n = parameter.getN();
		int res = Register.signInt((value >>> (Constants.REGISTER_SIZE * 4 - n)) | (value << n));
		updateFlags(ship, res);
		rx.set(res);
	}

	public static void executeWrite(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		String value = intToHexWithPrefix(ship.getRegisters()[parameter.getRegX()].asInt(), 4);
		Stadium stadium = ship.getStadium();
		ExecuteLog log = ExecuteLog.create(ship.getId(), stadium.getCycle(), ship.onBlueArrow(),
				ship.onRails(), value);
		stadium.getLogger().log(log);
	}

	public static void executeB(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		executeBranch(ship, parameter, currentCycle, () -> true);
	}

	public static void executeBz(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		executeBranch(ship, parameter, currentCycle, () -> ship.getFlags().getZ());
	}

	public static void executeBs(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		executeBranch(ship, parameter, currentCycle, () -> ship.getFlags().getS());
	}

	public static void executeBnz(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		executeBranch(ship, parameter, currentCycle, () -> !ship.getFlags().getZ());
	}

	public static void executeCmpi(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		int x = ship.getRegisters()[parameter.getRegX()].asInt();
		updateFlags(ship, Register.signInt(x - parameter.getN()));
	}

	public static void executeAddi(StadiumShip ship, InstructionParameters parameter,
			long currentCycle) {
		Register rx = ship.getRegisters()[parameter.getRegX()];
		int res = Register.signInt(rx.asInt() + parameter.getN());
		updateFlags(ship, res);
		rx.set(res);
	}

	private static long offsetAddressInTrack(long address, int offset) {
		long unsafeAddress = address + offset;
		if (unsafeAddress < 0) {
			return Constants.TRACK_SIZE + unsafeAddress;
		}
		return unsafeAddress % Constants.TRACK_SIZE;
	}

	private static void executeRegRegOp(StadiumShip ship, InstructionParameters parameter,
			long currentCycle, BiFunction<Integer, Integer, Integer> op) {
		executeRegRegOp(ship, parameter, currentCycle, op, true);
	}

	private static void executeRegRegOp(StadiumShip ship, InstructionParameters parameter,
			long currentCycle, BiFunction<Integer, Integer, Integer> op, boolean storeResult) {
		Register[] registers = ship.getRegisters();
		int x = registers[parameter.getRegX()].asInt();
		int y = registers[parameter.getRegY()].asInt();
		int res = Register.signInt(op.apply(x, y));
		updateFlags(ship, res);
		if (storeResult) {
			registers[parameter.getRegX()].set(res);
		}
	}

	private static void updateFlags(StadiumShip ship, int res) {
		ship.getFlags().setZ(res == 0);
		ship.getFlags().setS(res < 0);
	}

	private static void executeBranch(StadiumShip ship, InstructionParameters parameter,
			long currentCycle, BooleanSupplier test) {
		if (test.getAsBoolean()) {
			int offset = ship.getRegisters()[parameter.getRegX()].asInt();
			int idx = ship.getCurrentMode().getIdx();
			offset = Mode.offsetWithIdx(offset, idx);
			ship.incrementPcAndW0(offset);
		}
	}
}
