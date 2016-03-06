package corewar.stadium.runtime;

import corewar.shared.Constants;
import corewar.shared.InstructionType;
import corewar.shared.Mode;
import corewar.stadium.Stadium;
import corewar.stadium.StadiumResult;
import corewar.stadium.StadiumShip;
import corewar.stadium.StadiumShipBuilder;
import corewar.stadium.memory.InstructionParameters;
import corewar.stadium.memory.InstructionParameterBuilder;
import corewar.stadium.memory.Track;
import org.junit.Assert;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class TestExecutor {

	@DataPoint
	public static final TP NOP = new TP(ship().get(), p().get(), 0, ship().get(),
			Executors::executeNop);

	@DataPoint
	public static final TP CRASH = new TP(ship().get(), p().get(), 0, ship().crashed().get(),
			Executors::executeCrash);

	@DataPoint
	public static final TP CHECK_FIRST_ZONE = new TP(
			ship().pcw0(InstructionType.CHECK.getSize()).get(),
			p().get(), 0,
			ship().pcw0(InstructionType.CHECK.getSize()).get(),
			Executors::executeCheck);
	@DataPoint
	public static final TP CHECK_SECOND_ZONE = new TP(
			ship()
					.pcw0(Constants.CHECK_ZONE_SIZE + InstructionType.CHECK.getSize())
					.get(),
			p().get(), 573,
			ship()
					.pcw0(Constants.CHECK_ZONE_SIZE + InstructionType.CHECK.getSize())
					.lastChecked(573)
					.checked(1)
					.z(true)
					.get(),
			Executors::executeCheck);
	@DataPoint
	public static final TP CHECK_LAST_ZONE = new TP(
			ship()
					.pcw0((Constants.LAP_NUMBER * Constants.CHECKPOINTS_PER_LAP - 1)
							* Constants.CHECK_ZONE_SIZE + InstructionType.CHECK.getSize())
					.checked(1, 22)
					.get(),
			p().get(), 54_548,
			ship()
					.pcw0((Constants.LAP_NUMBER * Constants.CHECKPOINTS_PER_LAP - 1)
							* Constants.CHECK_ZONE_SIZE + InstructionType.CHECK.getSize())
					.lastChecked(54_548)
					.checked(1, 23)
					.z(true)
					.finished()
					.get(),
			Executors::executeCheck);
	@DataPoint
	public static final TP CHECK_MISSED_ZONE = new TP(
			ship()
					.pcw0(3 * Constants.CHECK_ZONE_SIZE + InstructionType.CHECK.getSize())
					.checked(1)
					.get(),
			p().get(), 4367,
			ship()
					.pcw0(3 * Constants.CHECK_ZONE_SIZE + InstructionType.CHECK.getSize())
					.lastChecked(0)
					.checked(1)
					.crashed()
					.get(),
			Executors::executeCheck);

	@DataPoint
	public static final TP FORK = new TP(
			ship().get(),
			p().get(), 0,
			ship().fork().get(),
			Executors::executeFork
	);

	@DataPoint
	public static final TP MODE = new TP(
			ship().get(),
			p().m(4).get(), 0,
			ship().mode(4).get(),
			Executors::executeMode
	);
	@DataPoint
	public static final TP MODE_NULL = new TP(
			ship().get(),
			p().m(Mode.values().length).get(), 0,
			ship().get(),
			Executors::executeMode
	);

	@DataPoint
	public static final TP LOAD = new TP(
			ship().get(),
			p().x(3).n(0x7384).get(), 0,
			ship().r(3, 0x7384).get(),
			Executors::executeLoad
	);

	@DataPoint
	public static final TP SWP = new TP(
			ship().r(2, 0xf00f).r(3, 0x00ff).get(),
			p().x(3).y(2).get(), 0,
			ship().r(2, 0x00ff).r(3, 0xf00f).get(),
			Executors::executeSwp
	);

	@DataPoint
	public static final TP LDB = new TP(
			ship().trackFlush(0, 0xf).get(),
			p().x(0).get(), 0,
			ship().trackFlush(0, 0xf).buff(0, 0xf).get(),
			Executors::executeLdb
	);
	@DataPoint
	public static final TP LDB_WITH_PC_OFFSET = new TP(
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.pcw0(Mode.DEFAULT_MODE.getIdx() - 1)
					.r(1, Mode.DEFAULT_MODE.getIdx())
					.trackFlush(Constants.TRACK_SIZE - 1, 0xf)
					.get(),
			p().x(1).n(Constants.BUFFER_SIZE).get(), 0,
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.pcw0(Mode.DEFAULT_MODE.getIdx() - 1)
					.r(1, Mode.DEFAULT_MODE.getIdx())
					.trackFlush(Constants.TRACK_SIZE - 1, 0xf)
					.buff(0, 0xf)
					.get(),
			Executors::executeLdb
	);

	@DataPoint
	public static final TP STB = new TP(
			ship().buff(0, 0xf).get(),
			p().x(0).get(), 0,
			ship().track(0, 0xf).buff(0, 0xf).get(),
			Executors::executeStb
	);
	@DataPoint
	public static final TP STB_WITH_PC_OFFSET = new TP(
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.pcw0(Mode.DEFAULT_MODE.getIdx() - 1)
					.r(1, Mode.DEFAULT_MODE.getIdx())
					.buff(0, 0xf)
					.get(),
			p().x(1).n(Constants.BUFFER_SIZE).get(), 0,
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.pcw0(Mode.DEFAULT_MODE.getIdx() - 1)
					.r(1, Mode.DEFAULT_MODE.getIdx())
					.buff(0, 0xf)
					.track(Constants.TRACK_SIZE - 1, 0xf)
					.get(),
			Executors::executeStb
	);

	@DataPoint
	public static final TP LDR = new TP(
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.pcw0(Mode.DEFAULT_MODE.getIdx() - 1)
					.r(1, Mode.DEFAULT_MODE.getIdx())
					.trackFlush(Constants.TRACK_SIZE - 1, 0xf)
					.get(),
			p().y(1).x(2).get(), 0,
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.pcw0(Mode.DEFAULT_MODE.getIdx() - 1)
					.r(1, Mode.DEFAULT_MODE.getIdx())
					.r(2, 0x000f)
					.trackFlush(Constants.TRACK_SIZE - 1, 0xf)
					.get(),
			Executors::executeLdr
	);

	@DataPoint
	public static final TP STR = new TP(
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.pcw0(Mode.DEFAULT_MODE.getIdx() - 1)
					.r(1, Mode.DEFAULT_MODE.getIdx())
					.r(2, 0x000f)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.pcw0(Mode.DEFAULT_MODE.getIdx() - 1)
					.r(1, Mode.DEFAULT_MODE.getIdx())
					.r(2, 0x000f)
					.track(Constants.TRACK_SIZE - 1, 0xf)
					.get(),
			Executors::executeStr
	);

//	@DataPoint
//	public static final TP STAT = new TP();

	@DataPoint
	public static final TP MOV = new TP(
			ship()
					.r(2, 0x1234)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x1234)
					.r(2, 0x1234)
					.get(),
			Executors::executeMov
	);

	@DataPoint
	public static final TP ADD = new TP(
			ship()
					.r(1, 0x0001)
					.r(2, 0x0002)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x0003)
					.r(2, 0x0002)
					.get(),
			Executors::executeAdd
	);
	@DataPoint
	public static final TP ADD_OVERFLOW = new TP(
			ship()
					.r(1, 0x7fff)
					.r(2, 0x0001)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x8000)
					.r(2, 0x0001)
					.s(true)
					.get(),
			Executors::executeAdd
	);
	@DataPoint
	public static final TP ADD_UPDATE_Z = new TP(
			ship()
					.r(1, 0x0001)
					.r(2, 0xffff)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x0000)
					.r(2, 0xffff)
					.z(true)
					.get(),
			Executors::executeAdd
	);
	@DataPoint
	public static final TP ADD_UPDATE_S = new TP(
			ship()
					.r(1, 0x0000)
					.r(2, 0xffff)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0xffff)
					.r(2, 0xffff)
					.s(true)
					.get(),
			Executors::executeAdd
	);

	@DataPoint
	public static final TP SUB = new TP(
			ship()
					.r(1, 0x0007)
					.r(2, 0x0002)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x0005)
					.r(2, 0x0002)
					.get(),
			Executors::executeSub
	);
	@DataPoint
	public static final TP SUB_OVERFLOW = new TP(
			ship()
					.r(1, 0x8000)
					.r(2, 0x0001)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x7fff)
					.r(2, 0x0001)
					.get(),
			Executors::executeSub
	);
	@DataPoint
	public static final TP SUB_UPDATE_Z = new TP(
			ship()
					.r(1, 0x0001)
					.r(2, 0x0001)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x0000)
					.r(2, 0x0001)
					.z(true)
					.get(),
			Executors::executeSub
	);
	@DataPoint
	public static final TP SUB_UPDATE_S = new TP(
			ship()
					.r(1, 0x0000)
					.r(2, 0x0001)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0xffff)
					.r(2, 0x0001)
					.s(true)
					.get(),
			Executors::executeSub
	);

	@DataPoint
	public static final TP OR = new TP(
			ship()
					.r(1, 0x0f0f)
					.r(2, 0x00f0)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x0fff)
					.r(2, 0x00f0)
					.get(),
			Executors::executeOr
	);
	@DataPoint
	public static final TP OR_UPDATE_Z = new TP(
			ship()
					.r(1, 0x0000)
					.r(2, 0x0000)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x0000)
					.r(2, 0x0000)
					.z(true)
					.get(),
			Executors::executeOr
	);
	@DataPoint
	public static final TP OR_UPDATE_S = new TP(
			ship()
					.r(1, 0x0000)
					.r(2, 0xffff)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0xffff)
					.r(2, 0xffff)
					.s(true)
					.get(),
			Executors::executeOr
	);

	@DataPoint
	public static final TP AND = new TP(
			ship()
					.r(1, 0x0f0f)
					.r(2, 0x00ff)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x000f)
					.r(2, 0x00ff)
					.get(),
			Executors::executeAnd
	);
	@DataPoint
	public static final TP AND_UPDATE_Z = new TP(
			ship()
					.r(1, 0x0000)
					.r(2, 0xffff)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x0000)
					.r(2, 0xffff)
					.z(true)
					.get(),
			Executors::executeAnd
	);
	@DataPoint
	public static final TP AND_UPDATE_S = new TP(
			ship()
					.r(1, 0x8000)
					.r(2, 0xffff)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x8000)
					.r(2, 0xffff)
					.s(true)
					.get(),
			Executors::executeAnd
	);

	@DataPoint
	public static final TP XOR = new TP(
			ship()
					.r(1, 0x0f0f)
					.r(2, 0x00ff)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x0ff0)
					.r(2, 0x00ff)
					.get(),
			Executors::executeXor
	);
	@DataPoint
	public static final TP XOR_UPDATE_Z = new TP(
			ship()
					.r(1, 0xffff)
					.r(2, 0xffff)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x0000)
					.r(2, 0xffff)
					.z(true)
					.get(),
			Executors::executeXor
	);
	@DataPoint
	public static final TP XOR_UPDATE_S = new TP(
			ship()
					.r(1, 0x0000)
					.r(2, 0xffff)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0xffff)
					.r(2, 0xffff)
					.s(true)
					.get(),
			Executors::executeXor
	);

	@DataPoint
	public static final TP NEG = new TP(
			ship()
					.r(2, 0xffff)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x0001)
					.r(2, 0xffff)
					.get(),
			Executors::executeNeg
	);
	@DataPoint
	public static final TP NEG_UPDATE_Z = new TP(
			ship()
					.r(2, 0x0000)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x0000)
					.r(2, 0x0000)
					.z(true)
					.get(),
			Executors::executeNeg
	);
	@DataPoint
	public static final TP NEG_UPDATE_S = new TP(
			ship()
					.r(2, 0x0001)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0xffff)
					.r(2, 0x0001)
					.s(true)
					.get(),
			Executors::executeNeg
	);

	@DataPoint
	public static final TP NOT = new TP(
			ship()
					.r(2, 0xfff0)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x000f)
					.r(2, 0xfff0)
					.get(),
			Executors::executeNot
	);
	@DataPoint
	public static final TP NOT_UPDATE_Z = new TP(
			ship()
					.r(2, 0xffff)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x0000)
					.r(2, 0xffff)
					.z(true)
					.get(),
			Executors::executeNot
	);
	@DataPoint
	public static final TP NOT_UPDATE_S = new TP(
			ship()
					.r(2, 0x0000)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0xffff)
					.r(2, 0x0000)
					.s(true)
					.get(),
			Executors::executeNot
	);

	@DataPoint
	public static final TP CMP = new TP(
			ship()
					.r(1, 0x0002)
					.r(2, 0x0001)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x0002)
					.r(2, 0x0001)
					.get(),
			Executors::executeCmp
	);
	@DataPoint
	public static final TP CMP_UPDATE_Z = new TP(
			ship()
					.r(1, 0x0001)
					.r(2, 0x0001)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x0001)
					.r(2, 0x0001)
					.z(true)
					.get(),
			Executors::executeCmp
	);
	@DataPoint
	public static final TP CMP_UPDATE_S = new TP(
			ship()
					.r(1, 0x0001)
					.r(2, 0x0002)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x0001)
					.r(2, 0x0002)
					.s(true)
					.get(),
			Executors::executeCmp
	);
	@DataPoint
	public static final TP CMP_OVERFLOW_SUP = new TP(
			ship()
					.r(1, 0x8000)
					.r(2, 0x0001)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x8000)
					.r(2, 0x0001)
					.get(),
			Executors::executeCmp
	);
	@DataPoint
	public static final TP CMP_OVERFLOW_INF = new TP(
			ship()
					.r(1, 0x7fff)
					.r(2, 0xffff)
					.get(),
			p().x(1).y(2).get(), 0,
			ship()
					.r(1, 0x7fff)
					.r(2, 0xffff)
					.s(true)
					.get(),
			Executors::executeCmp
	);

	@DataPoint
	public static final TP ASR = new TP(
			ship()
					.r(2, 0x00f0)
					.get(),
			p().x(2).n(1).get(), 0,
			ship()
					.r(2, 0x0078)
					.get(),
			Executors::executeAsr
	);
	@DataPoint
	public static final TP ASR_UPDATE_Z = new TP(
			ship()
					.r(2, 0x0008)
					.get(),
			p().x(2).n(4).get(), 0,
			ship()
					.r(2, 0x0000)
					.z(true)
					.get(),
			Executors::executeAsr
	);
	@DataPoint
	public static final TP ASR_UPDATE_S = new TP(
			ship()
					.r(2, 0xf000)
					.get(),
			p().x(2).n(4).get(), 0,
			ship()
					.r(2, 0xff00)
					.s(true)
					.get(),
			Executors::executeAsr
	);

	@DataPoint
	public static final TP ROL = new TP(
			ship()
					.r(2, 0x00f0)
					.get(),
			p().x(2).n(2).get(), 0,
			ship()
					.r(2, 0x03c0)
					.get(),
			Executors::executeRol
	);
	@DataPoint
	public static final TP ROL_UPDATE_Z = new TP(
			ship()
					.r(2, 0x0000)
					.get(),
			p().x(2).n(4).get(), 0,
			ship()
					.r(2, 0x0000)
					.z(true)
					.get(),
			Executors::executeRol
	);
	@DataPoint
	public static final TP ROL_UPDATE_S = new TP(
			ship()
					.r(2, 0x4000)
					.get(),
			p().x(2).n(1).get(), 0,
			ship()
					.r(2, 0x8000)
					.s(true)
					.get(),
			Executors::executeRol
	);

//	@DataPoint
//	public static final TP WRITE = new TP();

	@DataPoint
	public static final TP B = new TP(
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.pcw0(50)
					.r(1, 0x000a)
					.get(),
			p().x(1).get(), 0,
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.pcw0(60)
					.r(1, 0x000a)
					.get(),
			Executors::executeB
	);
	@DataPoint
	public static final TP B_OFFSET_OVERFLOW_SUP = new TP(
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.pcw0(Mode.DEFAULT_MODE.getIdx())
					.r(1, Mode.DEFAULT_MODE.getIdx())
					.get(),
			p().x(1).get(), 0,
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.pcw0(Mode.DEFAULT_MODE.getIdx())
					.pcw0(-Mode.DEFAULT_MODE.getIdx())
					.r(1, Mode.DEFAULT_MODE.getIdx())
					.get(),
			Executors::executeB
	);
	@DataPoint
	public static final TP B_OFFSET_OVERFLOW_INF = new TP(
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.r(1, -(Mode.DEFAULT_MODE.getIdx() + 1))
					.get(),
			p().x(1).get(), 0,
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.pcw0(Mode.DEFAULT_MODE.getIdx() - 1)
					.r(1, -(Mode.DEFAULT_MODE.getIdx() + 1))
					.get(),
			Executors::executeB
	);
	@DataPoint
	public static final TP B_TRACK_OVERFLOW_SUP = new TP(
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.pcw0(Constants.TRACK_SIZE - 1)
					.r(1, 2)
					.get(),
			p().x(1).get(), 0,
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.pcw0(Constants.TRACK_SIZE - 1)
					.pcw0(2)
					.r(1, 2)
					.get(),
			Executors::executeB
	);
	@DataPoint
	public static final TP B_TRACK_OVERFLOW_INF = new TP(
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.r(1, -2)
					.get(),
			p().x(1).get(), 0,
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.pcw0(-2)
					.r(1, -2)
					.get(),
			Executors::executeB
	);
	@DataPoint
	public static final TP B_OFFSET_OVERFLOW_INF_TRACK_OVERFLOW_SUP = new TP(
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.pcw0(Constants.TRACK_SIZE - 1)
					.r(1, -(Mode.DEFAULT_MODE.getIdx() + 1))
					.get(),
			p().x(1).get(), 0,
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.pcw0(Constants.TRACK_SIZE - 1)
					.pcw0(Mode.DEFAULT_MODE.getIdx() - 1)
					.r(1, -(Mode.DEFAULT_MODE.getIdx() + 1))
					.get(),
			Executors::executeB
	);
	@DataPoint
	public static final TP B_OFFSET_OVERFLOW_SUP_TRACK_OVERFLOW_INF = new TP(
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.r(1, Mode.DEFAULT_MODE.getIdx())
					.get(),
			p().x(1).get(), 0,
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.pcw0(-Mode.DEFAULT_MODE.getIdx())
					.r(1, Mode.DEFAULT_MODE.getIdx())
					.get(),
			Executors::executeB
	);

	@DataPoint
	public static final TP BZ_TRUE = new TP(
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.z(true)
					.r(1, 1)
					.get(),
			p().x(1).get(), 0,
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.z(true)
					.r(1, 1)
					.pcw0(1)
					.get(),
			Executors::executeBz
	);
	@DataPoint
	public static final TP BZ_FALSE = new TP(
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.r(1, 1)
					.get(),
			p().x(1).get(), 0,
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.r(1, 1)
					.get(),
			Executors::executeBz
	);

	@DataPoint
	public static final TP BS_TRUE = new TP(
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.s(true)
					.r(1, 1)
					.get(),
			p().x(1).get(), 0,
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.s(true)
					.r(1, 1)
					.pcw0(1)
					.get(),
			Executors::executeBs
	);
	@DataPoint
	public static final TP BS_FALSE = new TP(
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.r(1, 1)
					.get(),
			p().x(1).get(), 0,
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.r(1, 1)
					.get(),
			Executors::executeBs
	);

	@DataPoint
	public static final TP BNZ_TRUE = new TP(
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.r(1, 1)
					.get(),
			p().x(1).get(), 0,
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.r(1, 1)
					.pcw0(1)
					.get(),
			Executors::executeBnz
	);
	@DataPoint
	public static final TP BNZ_FALSE = new TP(
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.z(true)
					.r(1, 1)
					.get(),
			p().x(1).get(), 0,
			ship()
					.mode(Mode.DEFAULT_MODE.getValue())
					.z(true)
					.r(1, 1)
					.get(),
			Executors::executeBnz
	);

	@DataPoint
	public static final TP CMPI = new TP(
			ship()
					.r(1, 0x0002)
					.get(),
			p().x(1).n(1).get(), 0,
			ship()
					.r(1, 0x0002)
					.get(),
			Executors::executeCmpi
	);
	@DataPoint
	public static final TP CMPI_UPDATE_Z = new TP(
			ship()
					.r(1, 0x0001)
					.get(),
			p().x(1).n(1).get(), 0,
			ship()
					.r(1, 0x0001)
					.z(true)
					.get(),
			Executors::executeCmpi
	);
	@DataPoint
	public static final TP CMPI_UPDATE_S = new TP(
			ship()
					.r(1, 0x0001)
					.get(),
			p().x(1).n(2).get(), 0,
			ship()
					.r(1, 0x0001)
					.s(true)
					.get(),
			Executors::executeCmpi
	);
	@DataPoint
	public static final TP CMPI_OVERFLOW_SUP = new TP(
			ship()
					.r(1, 0x8000)
					.get(),
			p().x(1).n(1).get(), 0,
			ship()
					.r(1, 0x8000)
					.get(),
			Executors::executeCmpi
	);
	@DataPoint
	public static final TP CMPI_OVERFLOW_INF = new TP(
			ship()
					.r(1, 0x7fff)
					.get(),
			p().x(1).n(-1).get(), 0,
			ship()
					.r(1, 0x7fff)
					.s(true)
					.get(),
			Executors::executeCmpi
	);

	@DataPoint
	public static final TP ADDI = new TP(
			ship()
					.r(1, 0x0001)
					.get(),
			p().x(1).n(2).get(), 0,
			ship()
					.r(1, 0x0003)
					.get(),
			Executors::executeAddi
	);
	@DataPoint
	public static final TP ADDI_OVERFLOW = new TP(
			ship()
					.r(1, 0x7fff)
					.get(),
			p().x(1).n(1).get(), 0,
			ship()
					.r(1, 0x8000)
					.s(true)
					.get(),
			Executors::executeAddi
	);
	@DataPoint
	public static final TP ADDI_UPDATE_Z = new TP(
			ship()
					.r(1, 0x0001)
					.get(),
			p().x(1).n(-1).get(), 0,
			ship()
					.r(1, 0x0000)
					.z(true)
					.get(),
			Executors::executeAddi
	);
	@DataPoint
	public static final TP ADDI_UPDATE_S = new TP(
			ship()
					.r(1, 0x0000)
					.get(),
			p().x(1).n(-1).get(), 0,
			ship()
					.r(1, 0xffff)
					.s(true)
					.get(),
			Executors::executeAddi
	);

	private static InstructionParameterBuilder p() {
		return new InstructionParameterBuilder();
	}

	private static StadiumShipBuilder ship() {
		return StadiumShipBuilder.create(stadium());
	}

	private static Stadium stadium() {
		Track track = Track.create();
		return new Stadium() {

			@Override
			public long getCycle() {
				return 0;
			}

			@Override
			public Track getTrack() {
				return track;
			}

			@Override
			public StadiumResult run(String shipBin) {
				throw new IllegalArgumentException("Should not be called");
			}
		};
	}

	@Theory
	public void testExecute(TP p) {
		p.executor.accept(p.ship, p.param, p.cycle);
		Assert.assertEquals(p.expected, p.ship);
	}

	private static final class TP {

		private final StadiumShip ship;
		private final InstructionParameters param;
		private final int cycle;
		private final StadiumShip expected;
		private final Executors.Executor executor;

		private TP(StadiumShip ship, InstructionParameters param, int cycle, StadiumShip expected,
				Executors.Executor executor) {
			this.ship = ship;
			this.param = param;
			this.cycle = cycle;
			this.expected = expected;
			this.executor = executor;
		}
	}
}
