package simpleCoin.utility;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

public class Integer256Test {
	private static long[] longTestValues = { 0, 1, 127, 128, 255, 256, 1000, 2000, 5000, 10000, 20000, 50000, 100000,
			200000, 500000, 1000000, 2000000, 5000000, 10000000, 20000000, 50000000, 100000000, 200000000, 500000000,
			1000000000, 2000000000, 5000000000l, 10000000000l, 20000000000l, 50000000000l, 100000000000l, 200000000000l,
			500000000000l, 1000000000000l, 2000000000000l, 5000000000000l, 10000000000000l, 20000000000000l,
			50000000000000l, 100000000000000l, 200000000000000l, 500000000000000l, 1000000000000000l, 2000000000000000l,
			5000000000000000l, 10000000000000000l, 20000000000000000l, 50000000000000000l, 100000000000000000l,
			200000000000000000l, 500000000000000000l, 1000000000000000000l, 2000000000000000000l,
			5000000000000000000l };

	@Test
	public void testLongValues() {
		for (long testVal : longTestValues) {
			Integer256 num = new Integer256(testVal);
			byte[] compact = num.compactValue();
			Integer256 num2 = new Integer256(compact);
			BigInteger bi = num.bigInteger();
			Integer256 num3 = new Integer256(bi);

			System.out.print(
					testVal + " = " + num.longValue() + " = " + num2.longValue() + " = " + num3.longValue() + " = ");
			System.out.print("0x");
			for (byte b : compact) {
				System.out.print(String.format("%02X", b));
			}
			System.out.println();

			assertEquals(num.longValue(), testVal);
			assertEquals(num3.longValue(), testVal);

			// Max compact error allowed is (1 / mantissa)
			long val = 0;
			for (int i = 1; i <= 3; i++) {
				val = val * 256 + (compact[i] & 0xFF);
			}
			long maxError = (val == 0) ? 0 : testVal / val;
			assertTrue(Math.abs(num2.longValue() - testVal) <= maxError);
		}
	}

}
