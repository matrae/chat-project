package simpleCoin.utility;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * This class implements 256-bit integers. The internal representation is a
 * byte-array. This is not meant as an efficient mathematical class, but rather
 * as a convenient way to work with SHA-256 results in digital currencies. These
 * integers directly support conversion to-and-from the compact nBits
 * representation used in Bitcoin.
 * 
 * This implementation does not work correctly for negative numbers, since
 * negative numbers are not required for the given use cases.
 * 
 * Math and comparisons are not implemented - use the BigInteger conversions for
 * this purpose.
 */
public class Integer256 extends Number {
	private static final int BYTES = 32;
	private byte[] number = new byte[BYTES];

	/**
	 * Creates an Integer256 from a long, using bitwise operations
	 */
	public Integer256(long x) {
		for (int i = Long.BYTES - 1; i >= 0; i--) {
			number[BYTES - Long.BYTES + i] = (byte) (x & 0xFF);
			x >>= 8;
		}
	}

	/**
	 * Creates an Integer256 from a byte array. This array must either be the full
	 * required length (32 bytes), or else the nbits representation (the exponential
	 * notation used by Bitcoin), which is 4 bytes.
	 */
	public Integer256(byte[] compact) {
		if (compact.length == 4) {
			int byteLength = compact[0];
			int bytesToCopy = Math.min(3, byteLength);
			for (int i = 1; i <= bytesToCopy; i++) {
				int index = BYTES - byteLength + i - 1;
				number[index] = compact[i];
			}
		} else if (compact.length == BYTES) {
			number = Arrays.copyOf(compact, BYTES);
		} else {
			throw new NumberFormatException();
		}
	}

	/**
	 * Creates an Integer256 from a BigInteger. This
	 */
	public Integer256(BigInteger bi) {
		byte[] byteArray = bi.toByteArray();
		int bytesToCopy = Math.min(byteArray.length, BYTES);
		for (int i = 1; i <= bytesToCopy; i++) {
			number[BYTES - i] = byteArray[byteArray.length - i];
		}
	}

	/**
	 * Convert to the compact representation (the exponential notation used by
	 * Bitcoin). Note that this causes a loss of precision.
	 * 
	 * We locate the three most significant bytes to copy as the mantissa. If the
	 * left-most byte is negative (as a signed-byte value), this would create a
	 * negative number in the compact representation. In this case, we must shift
	 * the mantissa one byte to the right, and increment the exponent.
	 * 
	 * If the most significant byte is one of the last two, we leave the value so
	 * that we copy the last three bytes, and the exponent will be set to 3.
	 */
	public byte[] compactValue() {
		byte[] compact = new byte[4];
		int mostSignificantByte = BYTES - 3;
		for (int i = 0; i < mostSignificantByte; i++) {
			if (number[i] != 0) mostSignificantByte = i;
		}

		compact[0] = (byte) (BYTES - mostSignificantByte); // exponent
		if (number[mostSignificantByte] < 0) {
			compact[0]++;
			compact[1] = 0;
			for (int i = 2; i <= 3; i++) {
				compact[i] = number[mostSignificantByte + i - 2];
			}
		} else { // normal case
			for (int i = 1; i <= 3; i++) {
				compact[i] = number[mostSignificantByte + i - 1];
			}
		}
		return compact;
	}

	public BigInteger bigInteger() {
		return new BigInteger(number);
	}
	
	public byte[] getByteArray() {
		return number;
	}
	
	@Override
	public String toString() {
		return HelperMethods.bytesToHex(number);
	}

	@Override
	public int intValue() {
		int result = 0;
		for (int i = 0; i < Integer.BYTES; i++) {
			result <<= 8;
			result |= (number[BYTES - Integer.BYTES + i] & 0xFF);
		}
		return result;
	}

	@Override
	public long longValue() {
		long result = 0;
		for (int i = 0; i < Long.BYTES; i++) {
			result <<= 8;
			result |= (number[BYTES - Long.BYTES + i] & 0xFF);
		}
		return result;
	}

	@Override
	public float floatValue() {
		return (float) doubleValue();
	}

	@Override
	public double doubleValue() {
		double value = 0;
		for (byte b : number) {
			int intValueOfByte = 0 | (b & 0xFF);
			value = value * 256 + intValueOfByte;
		}
		return value;
	}
}
