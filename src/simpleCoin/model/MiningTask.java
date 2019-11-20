package simpleCoin.model;

import java.math.BigInteger;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import simpleCoin.model.payload.PayloadItem;
import simpleCoin.utility.Integer256;
import simpleCoin.utility.MonitorableArrayList;

/**
 * This is the core mining method. This Task can be used in two ways:
 * 
 * - If provided with a specific block, it searches for a suitable nonce, such
 * that the hash is less than or equal to the target. If all nonces have been
 * tried, we update the timestamp and start again. The result of the method, in
 * this case, is the original block, with updated timestamp and nonce.
 * 
 * - If provided with a blockchain, this task begins long-term mining. It
 * creates a new block containing payload items, and searches for a hash. When
 * the hash is found, the block is added to the blockchain, and the process
 * repeats. The task will only end when no more payload items are available.
 * 
 * If a mining program wished to mine on multiple threads, it must create
 * multiple (different!) blocks, for example, using different payloads, and
 * start multiple instances of this task.
 */
public class MiningTask extends Task<Void> {
	private final int UPDATE_FREQ = 1000000;
	private BlockChain blockchain;
	MonitorableArrayList<PayloadItem> payloadPool;
	private Block block;
	private final SimpleIntegerProperty hashRate = new SimpleIntegerProperty();

	/**
	 * Begin mining on the given blockchain, creating block after block.
	 */
	public MiningTask(BlockChain blockchain, MonitorableArrayList<PayloadItem> payloadPool) {
		this.blockchain = blockchain;
		this.payloadPool = payloadPool;
	}

	/**
	 * Look for the hash for the given block; take no other action. This is used
	 * when creating the genesis block.
	 */
	public MiningTask(Block block) {
		this.block = block;
	}

	/**
	 * This task must differentiate between the two ways it can be initialized
	 * (refer to the constructors)
	 */
	@Override
	public Void call() {
		if (block != null) {
			findHashForBlock();
		} else {
			// Create our own blocks, until cancelled, or no more payloads
			block = Block.constructNewBlock(blockchain, payloadPool);
			while(block != null && !this.isCancelled()) {
				findHashForBlock();
				
				if (block.isValid()) {
					// Permanently remove payload items from the payload-pool
					for (int i = 0; i < block.getPayload().size(); i++) {
						payloadPool.remove(block.getPayload().getItem(i));
					}

					// Add block to the chain
					blockchain.addNewBlock(block);
				}
				
				// Next block				
				block = Block.constructNewBlock(blockchain, payloadPool);
			}
		}
		return null;
	}
	
	private void findHashForBlock() {
		boolean found = false;
		int hashCounter = 0;
		long hashStart = System.currentTimeMillis();
		while (!found && !this.isCancelled()) {
			BlockHeader header = block.getBlockHeader();
			header.setTimeStamp(System.currentTimeMillis());
			for (int nonce = Integer.MIN_VALUE; nonce <= Integer.MAX_VALUE && !found
					&& !this.isCancelled(); nonce++) {
				header.setNonce(nonce);
				found = block.validProofOfWOrk();

				// Update hash rate
				hashCounter++;
				if (hashCounter % UPDATE_FREQ == 0) {
					long hashEnd = System.currentTimeMillis();
					long rate = UPDATE_FREQ * 1000 / (hashEnd - hashStart);

					hashRate.set((int) rate);
					hashStart = hashEnd;

					// Reset to prevent overflows
					if (hashCounter >= 100000000) hashCounter = 0;
				}
			}
		}
	}

	public int getHashRate() {
		return hashRate.get();
	}

	public SimpleIntegerProperty getHashRateProperty() {
		return hashRate;
	}

}
