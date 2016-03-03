package corewar.stadium;

import corewar.shared.Constants;
import corewar.shared.Delay;
import corewar.shared.Mode;
import corewar.stadium.memory.Buffer;
import corewar.stadium.memory.FetchQueue;
import corewar.stadium.memory.Flag;
import corewar.stadium.memory.InstructionParameter;
import corewar.stadium.memory.Register;
import corewar.stadium.memory.Track;
import corewar.stadium.runtime.InstructionInfo;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.BitSet;

public class StadiumShipFork {

	private final StadiumShip mainShip;
	private final int forkId;

	private final Register[] registers;
	private final Buffer buffer;
	private final Flag flags = new Flag();
	private final FetchQueue fetchQueue = new FetchQueue();
	private final BitSet checkedZones = new BitSet(Constants.CHECKPOINTS_PER_LAP * Constants.LAP_NUMBER);

	private long currentCycle;
	private boolean finished;
	private boolean crashed;
	private long lastCheckCycle;
	private boolean rails;
	private StadiumShip.State state = StadiumShip.State.FETCH;
	private long pc;
	private long w0;
	private long waitTime;
	private Mode currentMode = Mode.DEFAULT_MODE;

	@Nullable
	private StadiumShipFork fork;
	@Nullable
	private InstructionInfo currentInstructionInfo;
	@Nullable
	private InstructionParameter currentInstructionParameter;

	public StadiumShipFork(StadiumShip mainShip) {
		this.mainShip = mainShip;
		this.forkId = mainShip.getNextForkId();
		this.registers = new Register[Constants.NB_REGISTERS];
		this.buffer = new Buffer();

		for (int i = 0; i < registers.length; i++) {
			registers[i] = new Register();
		}
		checkedZones.set(0, true);
	}

	private StadiumShipFork(StadiumShip mainShip, StadiumShipFork origin) {
		this.mainShip = mainShip;
		this.forkId = mainShip.getNextForkId();
		this.registers = Arrays.copyOf(origin.registers, origin.registers.length);
		this.buffer = origin.buffer.duplicate();

		this.lastCheckCycle = origin.lastCheckCycle;
		this.pc = origin.pc;
		this.w0 = origin.w0;
		this.currentMode = origin.currentMode;

		flags.setZ(true);
		flags.setS(origin.flags.getS());
		checkedZones.or(origin.checkedZones);
	}

	public StadiumShip getMainShip() {
		return mainShip;
	}

	@Nullable
	public StadiumShipFork getFork() {
		return fork;
	}

	public void setFork(@Nullable StadiumShipFork fork) {
		this.fork = fork;
	}

	public String getName() {
		return mainShip.getName() + (forkId == 0 ? "" : " #" + forkId);
	}

	public Track getTrack() {
		return mainShip.getTrack();
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

	public FetchQueue getFetchQueue() {
		return fetchQueue;
	}

	public StadiumShip.State getState() {
		return state;
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
		setFork(new StadiumShipFork(mainShip, this));
	}

	public void runCycle(long cycle) {
		this.currentCycle = cycle;
		switch (state) {
		case FETCH:
			runFetch();
			break;
		case DECODE:
			runDecode();
			break;
		case EXECUTE:
		default:
			runExecute();
			break;
		}
	}

	private void runFetch() {
		fetchQueue.append(getTrack().read(pc));
		incrementPcAndW0();

		currentInstructionInfo = InstructionInfo.getInstructionFromFetchQueue(fetchQueue);
		if (currentInstructionInfo != null) {
			state = StadiumShip.State.DECODE;
			handleDelay(false, Delay.State.DECODE, this::runDecode);
		}
	}

	private void runDecode() {
		waitTime--;
		if (waitTime == 0) {
			assert currentInstructionInfo != null;
			currentInstructionParameter = currentInstructionInfo.decode(fetchQueue);
			state = StadiumShip.State.EXECUTE;
			handleDelay(true, Delay.State.EXECUTE, this::runExecute);
		}
	}

	private void handleDelay(boolean blueArrow, Delay.State state, Runnable r) {
		int delay = getDelay(blueArrow, state);
		if (delay == 0) {
			waitTime = 1;
			r.run();
		} else {
			waitTime = delay;
		}
	}

	private int getDelay(boolean blueArrow, Delay.State state) {
		int boost = 1;
		if (blueArrow && onBlueArrow()) {
			boost = 2;
		}
		if (onRails()) {
			boost *= 2;
		}
		assert currentInstructionInfo != null;
		return Delay.getDelay(currentInstructionInfo.getInstructionType(), state, currentMode) / boost;
	}

	private void runExecute() {
		waitTime--;
		if (waitTime == 0) {
			assert currentInstructionInfo != null;
			assert currentInstructionParameter != null;
			currentInstructionInfo.execute(this, currentInstructionParameter, currentCycle);
			InstructionParameter.InstructionMeta meta = currentInstructionParameter.getMeta();
			if (meta.getCount() == 0) {
				state = StadiumShip.State.FETCH;
				fetchQueue.clear();
				currentInstructionInfo = null;
				currentInstructionParameter = null;
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
		boolean res = rails || (fetchQueue.size() == 0 && getTrack().wroteAtPreviousCycle(pc));
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
		if (!(o instanceof StadiumShipFork)) {
			return false;
		}

		StadiumShipFork that = (StadiumShipFork) o;
		if (crashed != that.crashed) {
			return false;
		}
		if (currentCycle != that.currentCycle) {
			return false;
		}
		if (finished != that.finished) {
			return false;
		}
		if (forkId != that.forkId) {
			return false;
		}
		if (lastCheckCycle != that.lastCheckCycle) {
			return false;
		}
		if (pc != that.pc) {
			return false;
		}
		if (rails != that.rails) {
			return false;
		}
		if (w0 != that.w0) {
			return false;
		}
		if (waitTime != that.waitTime) {
			return false;
		}
		if (!buffer.equals(that.buffer)) {
			return false;
		}
		if (!checkedZones.equals(that.checkedZones)) {
			return false;
		}
		if (currentInstructionInfo != that.currentInstructionInfo) {
			return false;
		}
		if (currentInstructionParameter == null ? that.currentInstructionParameter != null : !currentInstructionParameter.equals(that.currentInstructionParameter)) {
			return false;
		}
		if (currentMode != that.currentMode) {
			return false;
		}
		if (!fetchQueue.equals(that.fetchQueue)) {
			return false;
		}
		if (!flags.equals(that.flags)) {
			return false;
		}
		if (!mainShip.equals(that.mainShip)) {
			return false;
		}
		if (fork == null ? that.fork != null : !fork.equals(that.fork)) {
			return false;
		}
		if (!Arrays.equals(registers, that.registers)) {
			return false;
		}
		return state == that.state;
	}

	@Override
	public int hashCode() {
		int result = mainShip.hashCode();
		result = 31 * result + forkId;
		result = 31 * result + Arrays.hashCode(registers);
		result = 31 * result + buffer.hashCode();
		result = 31 * result + flags.hashCode();
		result = 31 * result + fetchQueue.hashCode();
		result = 31 * result + checkedZones.hashCode();
		result = 31 * result + (int) (currentCycle ^ (currentCycle >>> 32));
		result = 31 * result + (finished ? 1 : 0);
		result = 31 * result + (crashed ? 1 : 0);
		result = 31 * result + (int) (lastCheckCycle ^ (lastCheckCycle >>> 32));
		result = 31 * result + (rails ? 1 : 0);
		result = 31 * result + state.hashCode();
		result = 31 * result + (int) (pc ^ (pc >>> 32));
		result = 31 * result + (int) (w0 ^ (w0 >>> 32));
		result = 31 * result + (int) (waitTime ^ (waitTime >>> 32));
		result = 31 * result + currentMode.hashCode();
		result = 31 * result + (currentInstructionInfo == null ? 0 : currentInstructionInfo.hashCode());
		result = 31 * result + (currentInstructionParameter == null ? 0 : currentInstructionParameter.hashCode());
		result = 31 * result + (fork == null ? 0 : fork.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "StadiumShipFork{" +
				 "mainShip=" + mainShip +
				 ", forkId=" + forkId +
				 ", registers=" + Arrays.toString(registers) +
				 ", buffer=" + buffer +
				 ", flags=" + flags +
				 ", fetchQueue=" + fetchQueue +
				 ", checkedZones=" + checkedZones +
				 ", currentCycle=" + currentCycle +
				 ", finished=" + finished +
				 ", crashed=" + crashed +
				 ", lastCheckCycle=" + lastCheckCycle +
				 ", rails=" + rails +
				 ", state=" + state +
				 ", pc=" + pc +
				 ", w0=" + w0 +
				 ", waitTime=" + waitTime +
				 ", currentMode=" + currentMode +
				 ", currentInstructionInfo=" + currentInstructionInfo +
				 ", currentInstructionParameter=" + currentInstructionParameter +
				 ", fork=" + fork +
				 '}';
	}
}
