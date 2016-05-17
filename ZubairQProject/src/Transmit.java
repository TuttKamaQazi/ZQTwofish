public class Transmit implements Encrypt {
	private int[] key = { 0x00000000, 0x00000000, 0x00000000, 0x00000000 };
	private int[] plainText = { 0x55555555, 0x55555555, 0x55555555, 0x55555555 };
	private int[] cipherText = { 0x00000000, 0x00000000, 0x00000000, 0x00000000 };
	private static final int[][] MDS = new int[4][256];
	private static final int GF256_FDBK = 0x169;
	private static final int GF256_FDBK_2 = 0x169 / 2;
	private static final int GF256_FDBK_4 = 0x169 / 4;

	private static final byte[][] P = new byte[][] { { // p0
			(byte) 0xA9, (byte) 0x67, (byte) 0xB3, (byte) 0xE8, (byte) 0x04,
					(byte) 0xFD, (byte) 0xA3, (byte) 0x76, (byte) 0x9A,
					(byte) 0x92, (byte) 0x80, (byte) 0x78, (byte) 0xE4,
					(byte) 0xDD, (byte) 0xD1, (byte) 0x38, (byte) 0x0D,
					(byte) 0xC6, (byte) 0x35, (byte) 0x98, (byte) 0x18,
					(byte) 0xF7, (byte) 0xEC, (byte) 0x6C, (byte) 0x43,
					(byte) 0x75, (byte) 0x37, (byte) 0x26, (byte) 0xFA,
					(byte) 0x13, (byte) 0x94, (byte) 0x48, (byte) 0xF2,
					(byte) 0xD0, (byte) 0x8B, (byte) 0x30, (byte) 0x84,
					(byte) 0x54, (byte) 0xDF, (byte) 0x23, (byte) 0x19,
					(byte) 0x5B, (byte) 0x3D, (byte) 0x59, (byte) 0xF3,
					(byte) 0xAE, (byte) 0xA2, (byte) 0x82, (byte) 0x63,
					(byte) 0x01, (byte) 0x83, (byte) 0x2E, (byte) 0xD9,
					(byte) 0x51, (byte) 0x9B, (byte) 0x7C, (byte) 0xA6,
					(byte) 0xEB, (byte) 0xA5, (byte) 0xBE, (byte) 0x16,
					(byte) 0x0C, (byte) 0xE3, (byte) 0x61, (byte) 0xC0,
					(byte) 0x8C, (byte) 0x3A, (byte) 0xF5, (byte) 0x73,
					(byte) 0x2C, (byte) 0x25, (byte) 0x0B, (byte) 0xBB,
					(byte) 0x4E, (byte) 0x89, (byte) 0x6B, (byte) 0x53,
					(byte) 0x6A, (byte) 0xB4, (byte) 0xF1, (byte) 0xE1,
					(byte) 0xE6, (byte) 0xBD, (byte) 0x45, (byte) 0xE2,
					(byte) 0xF4, (byte) 0xB6, (byte) 0x66, (byte) 0xCC,
					(byte) 0x95, (byte) 0x03, (byte) 0x56, (byte) 0xD4,
					(byte) 0x1C, (byte) 0x1E, (byte) 0xD7, (byte) 0xFB,
					(byte) 0xC3, (byte) 0x8E, (byte) 0xB5, (byte) 0xE9,
					(byte) 0xCF, (byte) 0xBF, (byte) 0xBA, (byte) 0xEA,
					(byte) 0x77, (byte) 0x39, (byte) 0xAF, (byte) 0x33,
					(byte) 0xC9, (byte) 0x62, (byte) 0x71, (byte) 0x81,
					(byte) 0x79, (byte) 0x09, (byte) 0xAD, (byte) 0x24,
					(byte) 0xCD, (byte) 0xF9, (byte) 0xD8, (byte) 0xE5,
					(byte) 0xC5, (byte) 0xB9, (byte) 0x4D, (byte) 0x44,
					(byte) 0x08, (byte) 0x86, (byte) 0xE7, (byte) 0xA1,
					(byte) 0x1D, (byte) 0xAA, (byte) 0xED, (byte) 0x06,
					(byte) 0x70, (byte) 0xB2, (byte) 0xD2, (byte) 0x41,
					(byte) 0x7B, (byte) 0xA0, (byte) 0x11, (byte) 0x31,
					(byte) 0xC2, (byte) 0x27, (byte) 0x90, (byte) 0x20,
					(byte) 0xF6, (byte) 0x60, (byte) 0xFF, (byte) 0x96,
					(byte) 0x5C, (byte) 0xB1, (byte) 0xAB, (byte) 0x9E,
					(byte) 0x9C, (byte) 0x52, (byte) 0x1B, (byte) 0x5F,
					(byte) 0x93, (byte) 0x0A, (byte) 0xEF, (byte) 0x91,
					(byte) 0x85, (byte) 0x49, (byte) 0xEE, (byte) 0x2D,
					(byte) 0x4F, (byte) 0x8F, (byte) 0x3B, (byte) 0x47,
					(byte) 0x87, (byte) 0x6D, (byte) 0x46, (byte) 0xD6,
					(byte) 0x3E, (byte) 0x69, (byte) 0x64, (byte) 0x2A,
					(byte) 0xCE, (byte) 0xCB, (byte) 0x2F, (byte) 0xFC,
					(byte) 0x97, (byte) 0x05, (byte) 0x7A, (byte) 0xAC,
					(byte) 0x7F, (byte) 0xD5, (byte) 0x1A, (byte) 0x4B,
					(byte) 0x0E, (byte) 0xA7, (byte) 0x5A, (byte) 0x28,
					(byte) 0x14, (byte) 0x3F, (byte) 0x29, (byte) 0x88,
					(byte) 0x3C, (byte) 0x4C, (byte) 0x02, (byte) 0xB8,
					(byte) 0xDA, (byte) 0xB0, (byte) 0x17, (byte) 0x55,
					(byte) 0x1F, (byte) 0x8A, (byte) 0x7D, (byte) 0x57,
					(byte) 0xC7, (byte) 0x8D, (byte) 0x74, (byte) 0xB7,
					(byte) 0xC4, (byte) 0x9F, (byte) 0x72, (byte) 0x7E,
					(byte) 0x15, (byte) 0x22, (byte) 0x12, (byte) 0x58,
					(byte) 0x07, (byte) 0x99, (byte) 0x34, (byte) 0x6E,
					(byte) 0x50, (byte) 0xDE, (byte) 0x68, (byte) 0x65,
					(byte) 0xBC, (byte) 0xDB, (byte) 0xF8, (byte) 0xC8,
					(byte) 0xA8, (byte) 0x2B, (byte) 0x40, (byte) 0xDC,
					(byte) 0xFE, (byte) 0x32, (byte) 0xA4, (byte) 0xCA,
					(byte) 0x10, (byte) 0x21, (byte) 0xF0, (byte) 0xD3,
					(byte) 0x5D, (byte) 0x0F, (byte) 0x00, (byte) 0x6F,
					(byte) 0x9D, (byte) 0x36, (byte) 0x42, (byte) 0x4A,
					(byte) 0x5E, (byte) 0xC1, (byte) 0xE0 }, { // p1
			(byte) 0x75, (byte) 0xF3, (byte) 0xC6, (byte) 0xF4, (byte) 0xDB,
					(byte) 0x7B, (byte) 0xFB, (byte) 0xC8, (byte) 0x4A,
					(byte) 0xD3, (byte) 0xE6, (byte) 0x6B, (byte) 0x45,
					(byte) 0x7D, (byte) 0xE8, (byte) 0x4B, (byte) 0xD6,
					(byte) 0x32, (byte) 0xD8, (byte) 0xFD, (byte) 0x37,
					(byte) 0x71, (byte) 0xF1, (byte) 0xE1, (byte) 0x30,
					(byte) 0x0F, (byte) 0xF8, (byte) 0x1B, (byte) 0x87,
					(byte) 0xFA, (byte) 0x06, (byte) 0x3F, (byte) 0x5E,
					(byte) 0xBA, (byte) 0xAE, (byte) 0x5B, (byte) 0x8A,
					(byte) 0x00, (byte) 0xBC, (byte) 0x9D, (byte) 0x6D,
					(byte) 0xC1, (byte) 0xB1, (byte) 0x0E, (byte) 0x80,
					(byte) 0x5D, (byte) 0xD2, (byte) 0xD5, (byte) 0xA0,
					(byte) 0x84, (byte) 0x07, (byte) 0x14, (byte) 0xB5,
					(byte) 0x90, (byte) 0x2C, (byte) 0xA3, (byte) 0xB2,
					(byte) 0x73, (byte) 0x4C, (byte) 0x54, (byte) 0x92,
					(byte) 0x74, (byte) 0x36, (byte) 0x51, (byte) 0x38,
					(byte) 0xB0, (byte) 0xBD, (byte) 0x5A, (byte) 0xFC,
					(byte) 0x60, (byte) 0x62, (byte) 0x96, (byte) 0x6C,
					(byte) 0x42, (byte) 0xF7, (byte) 0x10, (byte) 0x7C,
					(byte) 0x28, (byte) 0x27, (byte) 0x8C, (byte) 0x13,
					(byte) 0x95, (byte) 0x9C, (byte) 0xC7, (byte) 0x24,
					(byte) 0x46, (byte) 0x3B, (byte) 0x70, (byte) 0xCA,
					(byte) 0xE3, (byte) 0x85, (byte) 0xCB, (byte) 0x11,
					(byte) 0xD0, (byte) 0x93, (byte) 0xB8, (byte) 0xA6,
					(byte) 0x83, (byte) 0x20, (byte) 0xFF, (byte) 0x9F,
					(byte) 0x77, (byte) 0xC3, (byte) 0xCC, (byte) 0x03,
					(byte) 0x6F, (byte) 0x08, (byte) 0xBF, (byte) 0x40,
					(byte) 0xE7, (byte) 0x2B, (byte) 0xE2, (byte) 0x79,
					(byte) 0x0C, (byte) 0xAA, (byte) 0x82, (byte) 0x41,
					(byte) 0x3A, (byte) 0xEA, (byte) 0xB9, (byte) 0xE4,
					(byte) 0x9A, (byte) 0xA4, (byte) 0x97, (byte) 0x7E,
					(byte) 0xDA, (byte) 0x7A, (byte) 0x17, (byte) 0x66,
					(byte) 0x94, (byte) 0xA1, (byte) 0x1D, (byte) 0x3D,
					(byte) 0xF0, (byte) 0xDE, (byte) 0xB3, (byte) 0x0B,
					(byte) 0x72, (byte) 0xA7, (byte) 0x1C, (byte) 0xEF,
					(byte) 0xD1, (byte) 0x53, (byte) 0x3E, (byte) 0x8F,
					(byte) 0x33, (byte) 0x26, (byte) 0x5F, (byte) 0xEC,
					(byte) 0x76, (byte) 0x2A, (byte) 0x49, (byte) 0x81,
					(byte) 0x88, (byte) 0xEE, (byte) 0x21, (byte) 0xC4,
					(byte) 0x1A, (byte) 0xEB, (byte) 0xD9, (byte) 0xC5,
					(byte) 0x39, (byte) 0x99, (byte) 0xCD, (byte) 0xAD,
					(byte) 0x31, (byte) 0x8B, (byte) 0x01, (byte) 0x18,
					(byte) 0x23, (byte) 0xDD, (byte) 0x1F, (byte) 0x4E,
					(byte) 0x2D, (byte) 0xF9, (byte) 0x48, (byte) 0x4F,
					(byte) 0xF2, (byte) 0x65, (byte) 0x8E, (byte) 0x78,
					(byte) 0x5C, (byte) 0x58, (byte) 0x19, (byte) 0x8D,
					(byte) 0xE5, (byte) 0x98, (byte) 0x57, (byte) 0x67,
					(byte) 0x7F, (byte) 0x05, (byte) 0x64, (byte) 0xAF,
					(byte) 0x63, (byte) 0xB6, (byte) 0xFE, (byte) 0xF5,
					(byte) 0xB7, (byte) 0x3C, (byte) 0xA5, (byte) 0xCE,
					(byte) 0xE9, (byte) 0x68, (byte) 0x44, (byte) 0xE0,
					(byte) 0x4D, (byte) 0x43, (byte) 0x69, (byte) 0x29,
					(byte) 0x2E, (byte) 0xAC, (byte) 0x15, (byte) 0x59,
					(byte) 0xA8, (byte) 0x0A, (byte) 0x9E, (byte) 0x6E,
					(byte) 0x47, (byte) 0xDF, (byte) 0x34, (byte) 0x35,
					(byte) 0x6A, (byte) 0xCF, (byte) 0xDC, (byte) 0x22,
					(byte) 0xC9, (byte) 0xC0, (byte) 0x9B, (byte) 0x89,
					(byte) 0xD4, (byte) 0xED, (byte) 0xAB, (byte) 0x12,
					(byte) 0xA2, (byte) 0x0D, (byte) 0x52, (byte) 0xBB,
					(byte) 0x02, (byte) 0x2F, (byte) 0xA9, (byte) 0xD7,
					(byte) 0x61, (byte) 0x1E, (byte) 0xB4, (byte) 0x50,
					(byte) 0x04, (byte) 0xF6, (byte) 0xC2, (byte) 0x16,
					(byte) 0x25, (byte) 0x86, (byte) 0x56, (byte) 0x55,
					(byte) 0x09, (byte) 0xBE, (byte) 0x91 } };

	@Override
	public int[] Encrypt() {
		// far right and left of block diagram

		int some = plainText[0] ^ key[0];
		int other = plainText[0] ^ key[0];
		int someother = 0;
		int othersome = 0;
		some = FunctionF1(some, other);
		other = FunctionF2(some, other);

		someother = (plainText[2] ^ some) >>> 1;
		othersome = ((plainText[3] ^ key[3]) >>> 1) ^ other;

		cipherText[0] = some;
		cipherText[1] = other;
		cipherText[2] = someother;
		cipherText[3] = othersome;

		return cipherText;
	}

	public static int FunctionF1(int val, int val2) {
		int main1 = allSboxToMDS(val);
		int rotate = Integer.rotateLeft(val2, 8);
		int main2 = allSboxToMDS(rotate);

		int main3 = PHTFunction1(val, val2);
		int main4 = PHTFunction2(val, val2);

		return main3;

	}

	public static int FunctionF2(int val, int val2) {
		int main1 = allSboxToMDS(val);
		int rotate = Integer.rotateLeft(val2, 8);
		int main2 = allSboxToMDS(rotate);

		int main3 = PHTFunction1(val, val2);
		int main4 = PHTFunction2(val, val2);

		return main4;

	}

	public static int PHTFunction1(int val, int val2) {
		// int A = val & 0xFFFF, B = (val >> 16) & 0xFFFF;
		// int A2 = val2 & 0xFFFF, B2 = (val2 >> 16) & 0xFFFF;

		int Atest = MDStoPHT1(val, val2);
		int Btest = MDStoPHT2(val, val2);

		int test = Atest + Btest;

		int Ctest = MDStoPHT1(val2, test);
		int Dtest = MDStoPHT2(val2, test);

		int test2 = Ctest + Dtest;

		int subKey1 = 0x00000000;
		int subKey2 = 0x00000000;

		int Etest = MDStoPHT1(test, subKey1);
		int Ftest = MDStoPHT2(test, subKey1);

		int test3 = Etest + Ftest;

		int Gtest = MDStoPHT1(test2, subKey2);
		int Htest = MDStoPHT2(test2, subKey2);

		int test4 = Gtest + Htest;

		return test3;

	}

	public static int PHTFunction2(int val, int val2) {
		// int A = val & 0xFFFF, B = (val >> 16) & 0xFFFF;
		// int A2 = val2 & 0xFFFF, B2 = (val2 >> 16) & 0xFFFF;

		int Atest = MDStoPHT1(val, val2);
		int Btest = MDStoPHT2(val, val2);

		int test = Atest + Btest;

		int Ctest = MDStoPHT1(val2, test);
		int Dtest = MDStoPHT2(val2, test);

		int test2 = Ctest + Dtest;

		int subKey1 = 0x00000000;
		int subKey2 = 0x00000000;

		int Etest = MDStoPHT1(test, subKey1);
		int Ftest = MDStoPHT2(test, subKey1);

		int test3 = Etest + Ftest;

		int Gtest = MDStoPHT1(test2, subKey2);
		int Htest = MDStoPHT2(test2, subKey2);

		int test4 = Gtest + Htest;

		return test4;

	}

	public static int MDStoPHT1(int val, int val2) {
		int A = (int) (val + val2 % Math.pow(2, 32));

		return A;

	}

	public static int MDStoPHT2(int val, int val2) {
		int B = (int) (val + (2 * val2) % Math.pow(2, 32));
		return B;

	}

	public static int allSboxToMDS(int val) {
		int A, B, C, D;

		A = (val >> 24) & 0xFF;
		B = (val >> 16) & 0xFF;
		C = (val >> 8) & 0xFF;
		D = (val) & 0xFF;

		A = q0(A);
		B = q1(B);
		C = q0(C);
		D = q1(D);

		int endResult = (A << 24) | (B << 16) | (C << 8) | D;
		int S0 = 0x17A019B9;
		int S1 = 0x03A45B78;
		int testResult = endResult ^ S0;

		int A2, B2, C2, D2;

		A2 = q0(testResult);
		B2 = q1(testResult);
		C2 = q0(testResult);
		D2 = q1(testResult);

		int endResult2 = (A2 << 24) | (B2 << 16) | (C2 << 8) | D2;
		int testResult2 = endResult ^ S1;

		int A3, B3, C3, D3;

		A3 = q0(testResult2);
		B3 = q1(testResult2);
		C3 = q0(testResult2);
		D3 = q1(testResult2);

		int main = MDS(A3, B3, C3, D3);
		return main;
	}

	private static final int LFSR1(int x) {
		return (x >> 1) ^ ((x & 0x01) != 0 ? GF256_FDBK_2 : 0);
	}

	private static final int LFSR2(int x) {
		return (x >> 2) ^ ((x & 0x02) != 0 ? GF256_FDBK_2 : 0)
				^ ((x & 0x01) != 0 ? GF256_FDBK_4 : 0);
	}

	private static final int Mx_1(int x) {
		return x;
	}

	private static final int Mx_X(int x) {
		return x ^ LFSR2(x);
	} // 5B

	private static final int Mx_Y(int x) {
		return x ^ LFSR1(x) ^ LFSR2(x);
	} // EF

	public static int MDS(int A, int B, int C, int D) {

		int[] m1 = new int[2];
		int[] mX = new int[2];
		int[] mY = new int[2];
		int i, j;
		for (i = 0; i < 256; i++) {
			j = P[0][i] & 0xFF; // compute all the matrix elements
			m1[0] = j;
			mX[0] = Mx_X(j) & 0xFF;
			mY[0] = Mx_Y(j) & 0xFF;
			j = P[1][i] & 0xFF;
			m1[1] = j;
			mX[1] = Mx_X(j) & 0xFF;
			mY[1] = Mx_Y(j) & 0xFF;
			MDS[0][i] = m1[A] << 0 | // fill matrix w/ above elements
					mX[A] << 8 | mY[A] << 16 | mY[A] << 24;
			MDS[1][i] = mY[B] << 0 | mY[B] << 8 | mX[B] << 16 | m1[B] << 24;
			MDS[2][i] = mX[C] << 0 | mY[C] << 8 | m1[C] << 16 | mY[C] << 24;
			MDS[3][i] = mX[D] << 0 | m1[D] << 8 | mY[D] << 16 | mX[D] << 24;
		}
		int[][] test = MDS;

		int Atest = test[0][A];
		int Btest = test[0][B];
		int Ctest = test[0][C];
		int Dtest = test[0][D];

		int endInt = Atest + Btest + Ctest + Dtest;

		return endInt;
	}

	public static int q0(int val) {
		int A = (val & 0xF0) >> 4;
		int B = val & 0x0F;

		int[] t0 = { 0x8, 0x1, 0x7, 0xA, 0x6, 0xF, 0x3, 0x2, 0x0, 0xB, 0x5,
				0x9, 0xE, 0xC, 0xA, 0x4 };
		int[] t1 = { 0xE, 0xC, 0xB, 0x8, 0x1, 0x2, 0x3, 0x5, 0xF, 0x4, 0xA,
				0x6, 0x7, 0x0, 0x9, 0xD };
		int[] t2 = { 0xB, 0xA, 0x5, 0xE, 0x6, 0xD, 0x9, 0x0, 0xC, 0x8, 0xF,
				0x3, 0x2, 0x4, 0x7, 0x1 };
		int[] t3 = { 0xD, 0x7, 0xF, 0x4, 0x1, 0x2, 0x6, 0xE, 0x9, 0xB, 0x3,
				0x0, 0x8, 0x5, 0xC, 0xA };

		int A2 = A ^ B;
		A2 = t0[A2];
		int B2 = qFunction(A, B);
		B2 = t1[B2];
		int A3 = A2 ^ B2;
		A3 = t2[A3];
		int B3 = qFunction(A2, B2);
		B3 = t3[B3];
		int endResult = (A3 << 4) | B3;

		return endResult;

	}

	public static int q1(int val) {
		int A = (val & 0xF0) >> 4;
		int B = val & 0x0F;

		int[] t0 = { 0x2, 0x8, 0xB, 0xD, 0xF, 0x7, 0x6, 0xE, 0x3, 0x1, 0x9,
				0x4, 0x0, 0xA, 0xC, 0x5 };
		int[] t1 = { 0x1, 0xE, 0x2, 0xB, 0x4, 0xC, 0x3, 0x7, 0x6, 0xD, 0xA,
				0x5, 0xF, 0x9, 0x0, 0x8 };
		int[] t2 = { 0x4, 0xC, 0x7, 0x5, 0x1, 0x6, 0x9, 0xA, 0x0, 0xE, 0xD,
				0x8, 0x2, 0xB, 0x3, 0xF };
		int[] t3 = { 0xB, 0x9, 0x5, 0x1, 0xC, 0x3, 0xD, 0xE, 0x6, 0x4, 0x7,
				0xF, 0x2, 0x0, 0x8, 0xA };

		int A2 = A ^ B;
		A2 = t0[A2];
		int B2 = qFunction(A, B);
		B2 = t1[B2];
		int A3 = A2 ^ B2;
		A3 = t2[A3];
		int B3 = qFunction(A2, B2);
		B3 = t3[B3];
		int endResult = (A3 & 0xE0) | (B3 & 0x1F);

		return endResult;

	}

	public static int qFunction(int A, int B) {
		int Btest = B >> 1;
		int Btest2 = Btest ^ A;
		int Aa = A;
		if (Aa % 2 == 0)
			Aa = 8;
		else
			Aa = 0;
		int B2 = Aa ^ Btest2;
		return B2;

	}

}
