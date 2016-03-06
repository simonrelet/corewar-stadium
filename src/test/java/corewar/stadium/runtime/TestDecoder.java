package corewar.stadium.runtime;

import corewar.stadium.memory.FetchQueue;
import corewar.stadium.memory.InstructionParameters;
import corewar.stadium.runtime.Decoders.Decoder;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(Theories.class)
public class TestDecoder {

	@DataPoint
	public static final TP REG_REG_1_2 = new TP(fq(0x0, 0x1, 0x2), p(1, 2, 0, 0, 0),
			Decoders::decodeRegReg);
	@DataPoint
	public static final TP REG_REG_f_f = new TP(fq(0x0, 0xf, 0xf), p(15, 15, 0, 0, 0),
			Decoders::decodeRegReg);
	@DataPoint
	public static final TP REG_REG_0_0 = new TP(fq(0x0, 0x0, 0x0), p(0, 0, 0, 0, 0),
			Decoders::decodeRegReg);

	@DataPoint
	public static final TP LDR_STR_1_2 = new TP(fq(0x0, 0x1, 0x2), p(1, 2, 0, 0, 3),
			Decoders::decodeLdrStr);
	@DataPoint
	public static final TP LDR_STR_f_f = new TP(fq(0x0, 0xf, 0xf), p(15, 15, 0, 0, 3),
			Decoders::decodeLdrStr);
	@DataPoint
	public static final TP LDR_STR_0_0 = new TP(fq(0x0, 0x0, 0x0), p(0, 0, 0, 0, 3),
			Decoders::decodeLdrStr);

	@DataPoint
	public static final TP REG_N_1_5 = new TP(fq(0x0, 0x1, 0x5), p(1, -1, 5, 0, 0),
			Decoders::decodeRegN);
	@DataPoint
	public static final TP REG_N_2_f = new TP(fq(0x0, 0x2, 0xf), p(2, -1, 15, 0, 0),
			Decoders::decodeRegN);
	@DataPoint
	public static final TP REG_N_f_7 = new TP(fq(0x0, 0xf, 0x7), p(15, -1, 7, 0, 0),
			Decoders::decodeRegN);

	@DataPoint
	public static final TP F_REG_N_1_5 = new TP(fq(0x0, 0x0, 0x1, 0x5), p(1, -1, 5, 0, 0),
			Decoders::decodeFRegN);
	@DataPoint
	public static final TP F_REG_N_2_f = new TP(fq(0x0, 0x0, 0x2, 0xf), p(2, -1, -1, 0, 0),
			Decoders::decodeFRegN);
	@DataPoint
	public static final TP F_REG_N_f_7 = new TP(fq(0x0, 0x0, 0xf, 0x7), p(15, -1, 7, 0, 0),
			Decoders::decodeFRegN);

	@DataPoint
	public static final TP STAT_1_5 = new TP(fq(0x0, 0x0, 0x1, 0x5), p(1, -1, 5, 0, 0),
			Decoders::decodeStat);
	@DataPoint
	public static final TP STAT_2_f = new TP(fq(0x0, 0x0, 0x2, 0xf), p(2, -1, 15, 0, 0),
			Decoders::decodeStat);
	@DataPoint
	public static final TP STAT_f_7 = new TP(fq(0x0, 0x0, 0xf, 0x7), p(15, -1, 7, 0, 0),
			Decoders::decodeStat);

	@DataPoint
	public static final TP LDB_STB_1_5_2 = new TP(fq(0x0, 0x0, 0x1, 0x5, 0x0, 0x2, 0x0),
			p(1, -1, 5, 2, 1), Decoders::decodeLdbStb);
	@DataPoint
	public static final TP LDB_STB_6_255_0 = new TP(fq(0x0, 0x0, 0x6, 0xf, 0xf, 0x0, 0x0),
			p(6, -1, 255, 0, -1), Decoders::decodeLdbStb);
	@DataPoint
	public static final TP LDB_STB_4_0_255 = new TP(fq(0x0, 0x0, 0x4, 0x0, 0x0, 0xf, 0xf),
			p(4, -1, 0, 255, 254), Decoders::decodeLdbStb);

	@DataPoint
	public static final TP LC_9_0 = new TP(fq(0x0, 0x0, 0x9, 0x0, 0x0), p(9, -1, 0, 0, 0),
			Decoders::decodeLc);
	@DataPoint
	public static final TP LC_a_0x80 = new TP(fq(0x0, 0x0, 0xa, 0x0, 0x8), p(10, -1, -128, 0, 0),
			Decoders::decodeLc);
	@DataPoint
	public static final TP LC_c_0x7f = new TP(fq(0x0, 0x0, 0xc, 0xf, 0x7), p(12, -1, 127, 0, 0),
			Decoders::decodeLc);

	@DataPoint
	public static final TP LL_6_0 = new TP(fq(0x0, 0x0, 0x6, 0x0, 0x0, 0x0, 0x0), p(6, -1, 0, 0, 0),
			Decoders::decodeLl);
	@DataPoint
	public static final TP LL_d_MIN = new TP(fq(0x0, 0x0, 0xd, 0x0, 0x0, 0x0, 0x8),
			p(13, -1, -32_768, 0, 0), Decoders::decodeLl);
	@DataPoint
	public static final TP LL_e_MAX = new TP(fq(0x0, 0x0, 0xe, 0xf, 0xf, 0xf, 0x7),
			p(14, -1, 32_767, 0, 0), Decoders::decodeLl);

	@DataPoint
	public static final TP SWP_9_a = new TP(fq(0x0, 0x0, 0x9, 0xa), p(9, 10, 0, 0, 0),
			Decoders::decodeSwp);
	@DataPoint
	public static final TP SWP_f_f = new TP(fq(0x0, 0x0, 0xf, 0xf), p(15, 15, 0, 0, 0),
			Decoders::decodeSwp);
	@DataPoint
	public static final TP SWP_0_0 = new TP(fq(0x0, 0x0, 0x0, 0x0), p(0, 0, 0, 0, 0),
			Decoders::decodeSwp);

	@DataPoint
	public static final TP F_REG_f = new TP(fq(0x0, 0x0, 0xf), p(15, -1, 0, 0, 0),
			Decoders::decodeFReg);
	@DataPoint
	public static final TP F_REG_4 = new TP(fq(0x0, 0x0, 0x4), p(4, -1, 0, 0, 0),
			Decoders::decodeFReg);
	@DataPoint
	public static final TP F_REG_3 = new TP(fq(0x0, 0x0, 0x3), p(3, -1, 0, 0, 0),
			Decoders::decodeFReg);

	@DataPoint
	public static final TP MODE_0 = new TP(fq(0x0, 0x0, 0x0), p(-1, -1, 0, 0, 0),
			Decoders::decodeMode);
	@DataPoint
	public static final TP MODE_8 = new TP(fq(0x0, 0x0, 0x8), p(-1, -1, 0, 8, 0),
			Decoders::decodeMode);
	@DataPoint
	public static final TP MODE_f = new TP(fq(0x0, 0x0, 0xf), p(-1, -1, 0, 15, 0),
			Decoders::decodeMode);

	@Theory
	public void testDecode(TP p) {
		assertEquals(p.param, p.decoder.apply(p.fq));
	}

	private static FetchQueue fq(int... content) {
		FetchQueue fq = FetchQueue.createFetchQueue();
		for (int q : content) {
			fq.append((byte) q);
		}
		return fq;
	}

	private static InstructionParameters p(int reg1, int reg2, int n, int m, int count) {
		InstructionParameters res = InstructionParameters.create();
		res.setRegX(reg1);
		res.setRegY(reg2);
		res.setN(n);
		res.setM(m);
		res.getMeta().setCount(count);
		return res;
	}

	private static final class TP {

		private final FetchQueue fq;
		private final InstructionParameters param;
		private final Decoder decoder;

		private TP(FetchQueue fq, InstructionParameters param, Decoder decoder) {
			this.fq = fq;
			this.param = param;
			this.decoder = decoder;
		}
	}
}
