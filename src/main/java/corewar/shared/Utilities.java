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
		return extractFromBin(bin, 2);
	}

	public static String extractNameFromBin(String bin) {
		return extractFromBin(bin, 0);
	}

	public static String extractCommentFromBin(String bin) {
		return extractFromBin(bin, 1);
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

	private static String extractFromBin(String bin, int i) {
		return bin.split(Constants.NON_TEXT_PADDING_CHAR)[i];
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
