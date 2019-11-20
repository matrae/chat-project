package simpleCoin.model;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import simpleCoin.utility.Hashable;
import simpleCoin.utility.HelperMethods;
import simpleCoin.utility.Integer256;

public class BlockHeader implements Hashable {
	int versionNumber; // 4 bytes
	byte[] hashPreviousBlock = new byte[32];
	byte[] hashPayload = new byte[32];
	long timeStamp; // 8 bytes, time of start of search
	byte[] targetValue; // 4 bytes in nBits format: maximum acceptable hash value
	int nonce = 0; // 4 bytes, proof of work

	/**
	 * Create a block header for a new block, not yet in the blockchain
	 */
	public BlockHeader(BlockChain blockchain, Block block) {
		versionNumber = Block.VERSION_NUMBER;
		
		// Get hash of previous block; genesis-block has no previous
		Block previousBlock = blockchain.getLastBlock();
		hashPreviousBlock = (previousBlock == null) ? new byte[32] : previousBlock.getHash();

		hashPayload = block.getPayload().getHash();
		timeStamp = System.currentTimeMillis();
		targetValue = calculateTargetValue(blockchain);
	}

	private byte[] calculateTargetValue(BlockChain blockchain) {
		Integer256 difficulty_1 = new Integer256(BlockChain.DIFFICULTY_1);
		
		BigInteger bd_diff1 = difficulty_1.bigInteger();
		BigInteger bd_currentDiff = BigInteger.valueOf(blockchain.getCurrentDifficulty());
		BigInteger bi_threshold = bd_diff1.divide(bd_currentDiff);
		Integer256 i256_threshold = new Integer256(bi_threshold);
		return i256_threshold.compactValue();
	}

	@Override
	public byte[] toByteArray() {
		ByteBuffer buffer = ByteBuffer.allocate(84); // Really ought to calculate the 84...
		
		buffer.put(HelperMethods.intTobyteArray(versionNumber));
		buffer.put(hashPreviousBlock);
		buffer.put(hashPayload);
		buffer.put(HelperMethods.longTobyteArray(timeStamp));
		buffer.put(targetValue);
		buffer.put(HelperMethods.intTobyteArray(nonce));
		
		return buffer.array();
	}

	public int getVersionNumber() {
		return versionNumber;
	}

	public byte[] getHashPreviousBlock() {
		return hashPreviousBlock;
	}

	public byte[] getHashPayload() {
		return hashPayload;
	}

	public long getTimeStamp() {
		return timeStamp;
	}
	
	void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public byte[] getTargetValue() {
		return targetValue;
	}
	
	void setTargetValue(byte[] targetValue) {
		this.targetValue = targetValue;
	}

	public int getNonce() {
		return nonce;
	}
	
	void setNonce(int nonce) {
		this.nonce = nonce;
	}
}
