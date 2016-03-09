package corewar.stadium.runtime;

import corewar.stadium.memory.InstructionParameters;

import java.util.function.Function;

import static corewar.shared.HexIntConverter.intToHexWithPrefix;

public final class Printers {

	@FunctionalInterface
	public interface Printer extends Function<InstructionParameters, String> {

	}

	private Printers() {
	}

	public static String nop(InstructionParameters p) {
		return "nop";
	}

	public static String crash(InstructionParameters p) {
		return "crash";
	}

	public static String check(InstructionParameters p) {
		return "check";
	}

	public static String fork(InstructionParameters p) {
		return "fork";
	}

	public static String mode(InstructionParameters p) {
		return "mode " + p.getM();
	}

	public static String lc(InstructionParameters p) {
		return "lc r" + p.getRegX() + ", " + intToHexWithPrefix(p.getN(), 2);
	}

	public static String ll(InstructionParameters p) {
		return "ll r" + p.getRegX() + ", " + intToHexWithPrefix(p.getN(), 4);
	}

	public static String swp(InstructionParameters p) {
		return "swp r" + p.getRegX() + ", r" + p.getRegY();
	}

	private static String ldbStb(String instr, InstructionParameters p) {
		return instr + " [r" + p.getRegX() + "], " + intToHexWithPrefix(p.getN(), 2) + ", "
				+ intToHexWithPrefix(p.getM(), 2);
	}

	public static String ldb(InstructionParameters p) {
		return ldbStb("ldb", p);
	}

	public static String stb(InstructionParameters p) {
		return ldbStb("stb", p);
	}

	public static String ldr(InstructionParameters p) {
		return "ldr r" + p.getRegX() + ", [r" + p.getRegY() + "]";
	}

	public static String str(InstructionParameters p) {
		return "str [r" + p.getRegX() + "], r" + p.getRegY();
	}

	public static String stat(InstructionParameters p) {
		return "stat r" + p.getRegX() + ", " + intToHexWithPrefix(p.getN(), 1);
	}

	public static String mov(InstructionParameters p) {
		return "mov r" + p.getRegX() + ", r" + p.getRegY();
	}

	private static String regReg(String instr, InstructionParameters p) {
		return instr + " r" + p.getRegX() + ", r" + p.getRegY();
	}

	public static String add(InstructionParameters p) {
		return regReg("add", p);
	}

	public static String or(InstructionParameters p) {
		return regReg("or", p);
	}

	public static String and(InstructionParameters p) {
		return regReg("and", p);
	}

	public static String xor(InstructionParameters p) {
		return regReg("xor", p);
	}

	public static String sub(InstructionParameters p) {
		return regReg("sub", p);
	}

	public static String neg(InstructionParameters p) {
		return regReg("neg", p);
	}

	public static String not(InstructionParameters p) {
		return regReg("not", p);
	}

	public static String cmp(InstructionParameters p) {
		return regReg("cmp", p);
	}

	public static String asr(InstructionParameters p) {
		return "asr r" + p.getRegX() + ", " + intToHexWithPrefix(p.getN(), 1);
	}

	public static String rol(InstructionParameters p) {
		return "rol r" + p.getRegX() + ", " + intToHexWithPrefix(p.getN(), 1);
	}

	public static String write(InstructionParameters p) {
		return "write r" + p.getRegX();
	}

	private static String branch(String instr, InstructionParameters p) {
		return instr + " r" + p.getRegX();
	}

	public static String b(InstructionParameters p) {
		return branch("b", p);
	}

	public static String bz(InstructionParameters p) {
		return branch("bz", p);
	}

	public static String bs(InstructionParameters p) {
		return branch("bs", p);
	}

	public static String bnz(InstructionParameters p) {
		return branch("bnz", p);
	}

	public static String cmpi(InstructionParameters p) {
		return "cmpi r" + p.getRegX() + ", " + intToHexWithPrefix(p.getN(), 1);
	}

	public static String addi(InstructionParameters p) {
		return "addi r" + p.getRegX() + ", " + intToHexWithPrefix(p.getN(), 1);
	}
}
