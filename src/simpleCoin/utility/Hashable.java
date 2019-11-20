package simpleCoin.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface Hashable {
	public final int BYTES = 32;
	
	public byte[] toByteArray();
	
	default public byte[] getHash() {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			return digest.digest(this.toByteArray());
		} catch (NoSuchAlgorithmException e) {
			// TODO
			return null;
		}
	}
}
