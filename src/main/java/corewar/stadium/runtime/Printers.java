package corewar.stadium.runtime;

import corewar.stadium.StadiumShip;
import corewar.stadium.memory.InstructionParameters;

import static corewar.shared.HexIntConverter.intToHexWithPrefix;

public final class Printers {

	@FunctionalInterface
	public interface Printer {

		String apply(StadiumShip ship, InstructionParameters parameter, int verbosity);
	}

	private Printers() {
	}

	private static String getRegAndContent(StadiumShip ship, int reg, int verbosity,
			boolean address) {
		String res = "";
		if (verbosity >= 2) {
			res = ":" + intToHexWithPrefix(ship.getRegisters()[reg].asInt(), 4);
		}
		res = "r" + reg + res;
		return address ? "[" + res + "]" : res;
	}

	private static String reg(StadiumShip ship, InstructionParameters p, int verbosity,
			String instr) {
		return reg(ship, p, verbosity, instr, false);
	}

	private static String reg(StadiumShip ship, InstructionParameters p, int verbosity,
			String instr, boolean address) {
		return instr + " " + getRegAndContent(ship, p.getRegX(), verbosity, address);
	}

	private static String regN(StadiumShip ship, InstructionParameters p, int verbosity,
			String instr, int nibbles) {
		return regN(ship, p, verbosity, instr, nibbles, false);
	}

	private static String regN(StadiumShip ship, InstructionParameters p, int verbosity,
			String instr, int nibbles, boolean address) {
		return reg(ship, p, verbosity, instr, address) + ", " + intToHexWithPrefix(p.getN(),
				nibbles);
	}

	private static String regReg(StadiumShip ship, InstructionParameters p, int verbosity,
			String instr) {
		return regReg(ship, p, verbosity, instr, false, false);
	}

	private static String regReg(StadiumShip ship, InstructionParameters p, int verbosity,
			String instr, boolean xAddress, boolean yAddress) {
		return instr + " " + getRegAndContent(ship, p.getRegX(), verbosity, xAddress) + ", "
				+ getRegAndContent(ship, p.getRegY(), verbosity, yAddress);
	}

	private static String ldbStb(StadiumShip ship, InstructionParameters p, int verbosity,
			String instr) {
		return regN(ship, p, verbosity, instr, 2, true) + ", " + intToHexWithPrefix(p.getM(), 2);
	}

	public static String nop(StadiumShip ship, InstructionParameters p, int verbosity) {
		return "nop";
	}

	public static String crash(StadiumShip ship, InstructionParameters p, int verbosity) {
		return "crash";
	}

	public static String check(StadiumShip ship, InstructionParameters p, int verbosity) {
		return "check";
	}

	public static String fork(StadiumShip ship, InstructionParameters p, int verbosity) {
		return "fork";
	}

	public static String mode(StadiumShip ship, InstructionParameters p, int verbosity) {
		return "mode " + p.getM();
	}

	public static String lc(StadiumShip ship, InstructionParameters p, int verbosity) {
		return regN(ship, p, verbosity, "lc", 2);
	}

	public static String ll(StadiumShip ship, InstructionParameters p, int verbosity) {
		return regN(ship, p, verbosity, "ll", 4);
	}

	public static String swp(StadiumShip ship, InstructionParameters p, int verbosity) {
		return regReg(ship, p, verbosity, "swp");
	}

	public static String ldb(StadiumShip ship, InstructionParameters p, int verbosity) {
		return ldbStb(ship, p, verbosity, "ldb");
	}

	public static String stb(StadiumShip ship, InstructionParameters p, int verbosity) {
		return ldbStb(ship, p, verbosity, "stb");
	}

	public static String ldr(StadiumShip ship, InstructionParameters p, int verbosity) {
		return regReg(ship, p, verbosity, "ldr", false, true);
	}

	public static String str(StadiumShip ship, InstructionParameters p, int verbosity) {
		return regReg(ship, p, verbosity, "str", true, false);
	}

	public static String stat(StadiumShip ship, InstructionParameters p, int verbosity) {
		return regN(ship, p, verbosity, "stat", 1);
	}

	public static String mov(StadiumShip ship, InstructionParameters p, int verbosity) {
		return regReg(ship, p, verbosity, "mov");
	}

	public static String add(StadiumShip ship, InstructionParameters p, int verbosity) {
		return regReg(ship, p, verbosity, "add");
	}

	public static String or(StadiumShip ship, InstructionParameters p, int verbosity) {
		return regReg(ship, p, verbosity, "or");
	}

	public static String and(StadiumShip ship, InstructionParameters p, int verbosity) {
		return regReg(ship, p, verbosity, "and");
	}

	public static String xor(StadiumShip ship, InstructionParameters p, int verbosity) {
		return regReg(ship, p, verbosity, "xor");
	}

	public static String sub(StadiumShip ship, InstructionParameters p, int verbosity) {
		return regReg(ship, p, verbosity, "sub");
	}

	public static String neg(StadiumShip ship, InstructionParameters p, int verbosity) {
		return regReg(ship, p, verbosity, "neg");
	}

	public static String not(StadiumShip ship, InstructionParameters p, int verbosity) {
		return regReg(ship, p, verbosity, "not");
	}

	public static String cmp(StadiumShip ship, InstructionParameters p, int verbosity) {
		return regReg(ship, p, verbosity, "cmp");
	}

	public static String asr(StadiumShip ship, InstructionParameters p, int verbosity) {
		return regN(ship, p, verbosity, "asr", 1);
	}

	public static String rol(StadiumShip ship, InstructionParameters p, int verbosity) {
		return regN(ship, p, verbosity, "rol", 1);
	}

	public static String write(StadiumShip ship, InstructionParameters p, int verbosity) {
		return reg(ship, p, verbosity, "write");
	}

	public static String b(StadiumShip ship, InstructionParameters p, int verbosity) {
		return reg(ship, p, verbosity, "b");
	}

	public static String bz(StadiumShip ship, InstructionParameters p, int verbosity) {
		return reg(ship, p, verbosity, "bz");
	}

	public static String bs(StadiumShip ship, InstructionParameters p, int verbosity) {
		return reg(ship, p, verbosity, "bs");
	}

	public static String bnz(StadiumShip ship, InstructionParameters p, int verbosity) {
		return reg(ship, p, verbosity, "bnz");
	}

	public static String cmpi(StadiumShip ship, InstructionParameters p, int verbosity) {
		return regN(ship, p, verbosity, "cmpi", 1);
	}

	public static String addi(StadiumShip ship, InstructionParameters p, int verbosity) {
		return regN(ship, p, verbosity, "addi", 1);
	}
}
