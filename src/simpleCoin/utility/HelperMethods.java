package simpleCoin.utility;

public class HelperMethods {

	/**
	 * Convert a byte array to a hex string. Minor modification of using lower-case
	 * letters in the hex string
	 * 
	 * https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
	 */
	private final static char[] hexArray = "0123456789abcdef".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
	
	public static byte[] longTobyteArray(long value) {
		byte[] number = new byte[Long.BYTES];
		for (int i = Long.BYTES - 1; i >= 0; i--) {
			number[i] = (byte) (value & 0xFF);
			value >>= 8;
		}
		return number;
	}
	
	public static byte[] intTobyteArray(long value) {
		byte[] number = new byte[Integer.BYTES];
		for (int i = Integer.BYTES - 1; i >= 0; i--) {
			number[i] = (byte) (value & 0xFF);
			value >>= 8;
		}
		return number;
	}
	
}
