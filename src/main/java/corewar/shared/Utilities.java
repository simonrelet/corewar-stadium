package corewar.shared;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

public final class Utilities {

	private Utilities() {
	}

	public static int hexStringToInt(String string) {
		return Integer.parseInt(string.startsWith("0x") ? string.substring(2) : string, 16);
	}

	public static int intStringToInt(String string) {
		return Integer.parseInt(string);
	}

	public static String extractCodeFromBin(String bin) {
		return bin.substring(Constants.NAME_MAX_CHARACTER_COUNT + Constants.COMMENT_MAX_CHARACTER_COUNT).trim();
	}

	public static String extractNameFromBin(String bin) {
		return bin.substring(0, Constants.NAME_MAX_CHARACTER_COUNT).trim();
	}

	public static String extractCommentFromBin(String bin) {
		return bin.substring(Constants.NAME_MAX_CHARACTER_COUNT, Constants.NAME_MAX_CHARACTER_COUNT + Constants.COMMENT_MAX_CHARACTER_COUNT).trim();
	}

	public static byte charToByte(char aChar) {
		byte ref;
		if (aChar >= '0' && aChar <= '9') {
			ref = (byte) '0';
		} else if (aChar >= 'a' && aChar <= 'f') {
			ref = (byte) 'a' - 10;
		} else {
			throw new IllegalArgumentException("The char '" + aChar + "' is a not valid hex value");
		}

		return (byte) (aChar - ref);
	}

	public static char byteToChar(byte aByte) {
		byte ref;
		if (aByte >= 0 && aByte <= 9) {
			ref = (byte) '0';
		} else if (aByte > 9 && aByte < 16) {
			ref = (byte) 'a' - 10;
		} else {
			throw new IllegalArgumentException("The byte '" + aByte + "' cannot be converted to a char");
		}

		return (char) (aByte + ref);
	}

	public static double clamp(double min, double value, double max) {
		if (value < min) {
			return min;
		}
		if (value > max) {
			return max;
		}
		return value;
	}

	public static <T, R> Stream<R> arraysMap(T[] array, Function<T, R> function) {
		return Arrays.asList(array).stream().map(function);
	}
}
