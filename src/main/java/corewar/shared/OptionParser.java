package corewar.shared;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

public final class OptionParser {

	private OptionParser() {
	}

	public static Optional<Options> parse(String[] args) {
		int verbosity = 0;
		String file = "";
		try {
			checkArgument(args.length > 0);
			for (int i = 0; i < args.length; i++) {
				String arg = args[i];
				if ("-v".equals(arg)) {
					verbosity = Integer.parseInt(args[i + 1]);
					i++;
				} else {
					file = arg;
				}
			}
		} catch (Throwable t) {
			return Optional.empty();
		}
		return Optional.of(new Options(verbosity, file));
	}

	public static final class Options {

		private final int verbosity;
		private final String file;

		private Options(int verbosity, String file) {
			this.verbosity = verbosity;
			this.file = file;
		}

		public int getVerbosity() {
			return verbosity;
		}

		public String getFile() {
			return file;
		}
	}
}
