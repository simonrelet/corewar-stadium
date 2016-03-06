package corewar.stadium;

import corewar.shared.Constants;
import corewar.shared.Delay;
import corewar.shared.Mode;
import corewar.stadium.memory.Buffer;
import corewar.stadium.memory.FetchQueue;
import corewar.stadium.memory.Flag;
import corewar.stadium.memory.InstructionParameters;
import corewar.stadium.memory.InstructionParameters.InstructionParametersMeta;
import corewar.stadium.memory.Register;
import corewar.stadium.runtime.Instruction;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

public final class StadiumShip {

	public enum State {
		FETCH,
		DECODE,
		EXECUTE
	}

	private final Stadium stadium;
	private final Register[] registers = new Register[Constants.NB_REGISTERS];
	private final Buffer buffer = Buffer.createBuffer();
	private final Flag flags = Flag.create();
	private final FetchQueue fetchQueue = FetchQueue.createFetchQueue();
	private final BitSet checkedZones = new BitSet(Constants.CHECKPOINTS_PER_LAP
			* Constants.LAP_NUMBER);

	private boolean finished;
	private boolean crashed;
	private long lastCheckCycle;
	private boolean rails;
	private State state = State.FETCH;
	private long pc;
	private long w0;
	private long waitTime;
	private Mode currentMode = Mode.DEFAULT_MODE;
	private Optional<StadiumShip> fork = Optional.empty();
	private Optional<Instruction> instruction = Optional.empty();
	private Optional<InstructionParameters> instructionParameters = Optional.empty();

	private StadiumShip(Stadium stadium) {
		this.stadium = stadium;
		for (int i = 0; i < registers.length; i++) {
			registers[i] = Register.createRegister();
		}
		checkedZones.set(0, true);
	}

	private StadiumShip(StadiumShip origin) {
		this(origin.stadium);
		this.lastCheckCycle = origin.lastCheckCycle;
		this.pc = origin.pc;
		this.w0 = origin.w0;
		this.currentMode = origin.currentMode;

		origin.buffer.copyTo(buffer);
		for (int i = 0; i < registers.length; i++) {
			origin.registers[i].copyTo(registers[i]);
		}

		flags.setZ(true);
		flags.setS(origin.flags.getS());
		checkedZones.or(origin.checkedZones);
	}

	public static StadiumShip create(Stadium stadium) {
		return new StadiumShip(stadium);
	}

	public static StadiumShip create(StadiumShip origin) {
		return new StadiumShip(origin);
	}

	public Stadium getStadium() {
		return stadium;
	}

	public Optional<StadiumShip> getFork() {
		return fork;
	}

	private void setFork(StadiumShip fork) {
		this.fork = Optional.of(fork);
	}

	public void clearFork() {
		this.fork = Optional.empty();
	}

	public Register[] getRegisters() {
		return registers;
	}

	public Buffer getBuffer() {
		return buffer;
	}

	public Flag getFlags() {
		return flags;
	}

	public long getPc() {
		return pc;
	}

	public Mode getCurrentMode() {
		return currentMode;
	}

	public void setCurrentMode(Mode currentMode) {
		this.currentMode = currentMode;
	}

	public long getLastCheckCycle() {
		return lastCheckCycle;
	}

	public void setLastCheckCycle(long lastCheckCycle) {
		this.lastCheckCycle = lastCheckCycle;
	}

	public boolean hasFinished() {
		return finished;
	}

	public void setFinished() {
		this.finished = true;
	}

	public boolean hasCrashed() {
		return crashed;
	}

	public void crash() {
		crashed = true;
	}

	public BitSet getCheckedZones() {
		return checkedZones;
	}

	public long getW0() {
		return w0;
	}

	public void fork() {
		setFork(create(this));
	}

	public void runCycle() {
		switch (state) {
		case FETCH:
			runFetch();
			break;
		case DECODE:
			runDecode();
			break;
		case EXECUTE:
			runExecute();
			break;
		}
	}

	private void runFetch() {
		// Update on rails flag
		onRails();
		fetchQueue.append(stadium.getTrack().read(pc));
		incrementPcAndW0();

		instruction = Instruction.getInstructionFromFetchQueue(fetchQueue);
		if (instruction.isPresent()) {
			state = State.DECODE;
			handleDelay(false, Delay.State.DECODE, this::runDecode);
		}
	}

	private void runDecode() {
		waitTime--;
		if (waitTime == 0) {
			checkArgument(instruction.isPresent(), "There should be an instruction");
			instructionParameters = Optional.of(instruction.get().decode(fetchQueue));
			state = State.EXECUTE;
			handleDelay(true, Delay.State.EXECUTE, this::runExecute);
		}
	}

	private void handleDelay(boolean blueArrow, Delay.State delayForState, Runnable r) {
		int delay = getDelay(blueArrow, delayForState);
		if (delay == 0) {
			waitTime = 1;
			r.run();
		} else {
			waitTime = delay;
		}
	}

	private int getDelay(boolean blueArrow, Delay.State delayForState) {
		int boost = 1;
		if (blueArrow && onBlueArrow()) {
			boost = 2;
		}
		if (onRails()) {
			boost *= 2;
		}
		checkArgument(instruction.isPresent(), "There should be an instruction");
		return Delay.getDelay(instruction.get().getInstructionType(), delayForState, currentMode)
				/ boost;
	}

	private void runExecute() {
		waitTime--;
		if (waitTime == 0) {
			checkArgument(instruction.isPresent(), "There should be an instruction");
			checkArgument(instructionParameters.isPresent(), "There should be an instruction");
			instruction.get().execute(this, instructionParameters.get(), stadium.getCycle());
			InstructionParametersMeta meta = instructionParameters.get().getMeta();
			if (meta.getCount() == 0) {
				state = State.FETCH;
				fetchQueue.clear();
				instruction = Optional.empty();
				instructionParameters = Optional.empty();
				rails = false;
			} else {
				meta.setCount(meta.getCount() - 1);
				meta.setOffset(meta.getOffset() + 1);
				waitTime = getDelay(true, Delay.State.EXECUTE);
			}
		}
	}

	private void incrementPcAndW0() {
		incrementPcAndW0(1);
	}

	public void incrementPcAndW0(int offset) {
		w0 += Math.abs(offset);
		pc += offset;
		pc = pc < 0 ? Constants.TRACK_SIZE + pc : pc % Constants.TRACK_SIZE;
	}

	private boolean onRails() {
		boolean res = rails
				|| (fetchQueue.size() == 0 && stadium.getTrack().wroteAtPreviousCycle(pc));
		if (res) {
			rails = true;
		}
		return res;
	}

	private boolean onBlueArrow() {
		return (pc - fetchQueue.size()) % Constants.BLUE_ARROW_SPACING == 0;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof StadiumShip)) {
			return false;
		}

		StadiumShip that = (StadiumShip) o;

		if (finished != that.finished) {
			return false;
		}
		if (crashed != that.crashed) {
			return false;
		}
		if (lastCheckCycle != that.lastCheckCycle) {
			return false;
		}
		if (rails != that.rails) {
			return false;
		}
		if (pc != that.pc) {
			return false;
		}
		if (w0 != that.w0) {
			return false;
		}
		if (waitTime != that.waitTime) {
			return false;
		}
		if (!stadium.getTrack().equals(that.stadium.getTrack())) {
			return false;
		}
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		if (!Arrays.equals(registers, that.registers)) {
			return false;
		}
		if (!buffer.equals(that.buffer)) {
			return false;
		}
		if (!flags.equals(that.flags)) {
			return false;
		}
		if (!fetchQueue.equals(that.fetchQueue)) {
			return false;
		}
		if (!checkedZones.equals(that.checkedZones)) {
			return false;
		}
		if (state != that.state) {
			return false;
		}
		if (currentMode != that.currentMode) {
			return false;
		}
		if (!fork.equals(that.fork)) {
			return false;
		}
		if (instruction != that.instruction) {
			return false;
		}
		return instructionParameters.equals(that.instructionParameters);
	}

	@Override
	public int hashCode() {
		int result = stadium.getTrack().hashCode();
		result = 31 * result + Arrays.hashCode(registers);
		result = 31 * result + buffer.hashCode();
		result = 31 * result + flags.hashCode();
		result = 31 * result + fetchQueue.hashCode();
		result = 31 * result + checkedZones.hashCode();
		result = 31 * result + (finished ? 1 : 0);
		result = 31 * result + (crashed ? 1 : 0);
		result = 31 * result + (int) (lastCheckCycle ^ (lastCheckCycle >>> 32));
		result = 31 * result + (rails ? 1 : 0);
		result = 31 * result + state.hashCode();
		result = 31 * result + (int) (pc ^ (pc >>> 32));
		result = 31 * result + (int) (w0 ^ (w0 >>> 32));
		result = 31 * result + (int) (waitTime ^ (waitTime >>> 32));
		result = 31 * result + currentMode.hashCode();
		result = 31 * result + fork.hashCode();
		result = 31 * result + instruction.hashCode();
		return 31 * result + instructionParameters.hashCode();
	}
}
