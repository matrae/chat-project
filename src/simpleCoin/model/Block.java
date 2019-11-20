package simpleCoin.model;

import java.math.BigInteger;

import simpleCoin.model.payload.Payload;
import simpleCoin.model.payload.PayloadItem;
import simpleCoin.utility.Hashable;
import simpleCoin.utility.Integer256;
import simpleCoin.utility.MonitorableArrayList;

public class Block implements Hashable {
	public static final int VERSION_NUMBER = 1;
	private BlockHeader header; // Header information
	private Payload payload; // Contents of the block
	private boolean inChain = false; // Set to indicate whether this block is in the BlockChain

	/**
	 * Constructor to construct a block with no payload
	 */
	public Block(BlockChain blockchain) {
		payload = new Payload();
		header = new BlockHeader(blockchain, this);
	}
	
	/**
	 * Construct a block, given a payload. Used by the factory method below
	 */
	private Block(BlockChain blockchain, Payload payload) {
		this.payload = payload;
		header = new BlockHeader(blockchain, this);
	}

	/**
	 * Factory method to construct a block with a payload. We first construct a payload, and then
	 * create the block. If no payload items are available, we return null.
	 */
	public static Block constructNewBlock(BlockChain blockchain, MonitorableArrayList<PayloadItem> payloadPool) {
		if (payloadPool.size() == 0) {
			return null;
		} else {
			// Fill payload with items from the pool. In our case, we select the first item,
			// and randomly add a couple of further items
			Payload payload = new Payload();
			payload.addItem(payloadPool.get(0));
			for (int i = 1; i < payloadPool.size() && Math.random() < 0.5; i++) {
				payload.addItem(payloadPool.get(i));
			}
	
			return new Block(blockchain, payload);
		}
	}

	public boolean validProofOfWOrk() {
		byte[] hash = this.getHash();

		Integer256 i256Hash = new Integer256(hash);
		BigInteger iHash = i256Hash.bigInteger();
		BigInteger iThreshold = (new Integer256(this.header.targetValue)).bigInteger();
		return (iHash.compareTo(BigInteger.ZERO) >= 0 && iHash.compareTo(iThreshold) <= 0);
	}
	
	
	/**
	 * Validity check - this should be used before adding a block to the blockchain.
	 * 
	 * In our simple, initial implementation without a network of miners, the only
	 * relevant point is ensuring that the block hash is below the given target This is necessary because
	 * the user can interrupt the mining process, thus leaving the block with an incorrect proof-of-work.
	 * 
	 * In a networked implementation, many more checks must be added, because we cannot
	 * trust some other random client on the net.
	 */
	public boolean isValid() {
		// Payload not empty
		boolean result =  (payload.size() > 0);
		
		// Timestamp is reasonable
		long MAX_TIME_DIFF = 2 * 60 * 60 * 1000; // tolerance 2 hours
		long now = System.currentTimeMillis();
		long diff = Math.abs(this.header.timeStamp - now);
		result = result && diff < MAX_TIME_DIFF;
		
		// Check all hashes in the block header
		// TODO
		
		// Proof-of-work is valid
		result = result && this.validProofOfWOrk();

		// TODO
		// If already in the blockchain:
		// - version number >= previous block
		// - Hash of previous block is correct
		// If not in chain:
		// - not already in chain (search for matching hashes)
		// - version number >= version number of last block in chain
		// - Hash of previous block matches last block on chain

		return result;
	}

	public boolean addToPayload(PayloadItem item) {
		boolean finalized = !payload.isFinalized();
		if (finalized) payload.addItem(item);
		return !finalized;
	}

	public Payload getPayload() {
		return payload;
	}

	public BlockHeader getBlockHeader() {
		return header;
	}

	/**
	 * Initiate mining, to find a valid proof-of-work. This version is
	 * self-contained, i.e., it is not possible to monitor the Task from outside the
	 * method. It only returns when the mining is complete.
	 */
	public void findProofOfWork() {
		MiningTask task = new MiningTask(this);

		Thread t = new Thread(task, "Mining thread");
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
		}
	}

	/**
	 * For the block, we just use the header. Why? Because the block is the header
	 * plus the payload, and the payload hash is already included in the header.
	 */
	@Override
	public byte[] toByteArray() {
		return header.toByteArray();
	}

	public boolean isInChain() {
		return inChain;
	}

	/**
	 * Package private; only called by BlockChain upon adding
	 */
	void setInChain() {
		this.inChain = true;
	}

}
