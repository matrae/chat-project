package simpleCoin.model;

import java.util.ArrayList;

import javafx.beans.property.SimpleIntegerProperty;
import simpleCoin.model.payload.Message;
import simpleCoin.utility.MonitorableArrayList;

/**
 * BlockChain represents the blockchain for this currently. This is a singleton.
 */
public class BlockChain {
	public static final long TARGET_PERIOD_MS = 5000; // When mining, a new block should be generated this often (in ms)
	public static final int BLOCKS_PER_ADJUSTMENT = 3; // How many blocks to look at, when adjusting difficulty
	
	public static final byte[] DIFFICULTY_1 = {0x1e, 0x0F, -1, -1}; // temporarily easier, for debugging
//	public static final byte[] DIFFICULTY_1 = {0x1d, 0x00, -1, -1}; // 0x1d00ffff same as Bitcoin
	public static final byte[] DIFFICULTY_GENESIS = {0x20, 0x0F, -1, -1}; // easier difficulty for the genesis block
	
	private MonitorableArrayList<Block> blocks = new MonitorableArrayList<>();
	private long currentDifficulty = 1; // Initial difficulty; will be adjusted upwards during mining
	
	
	public BlockChain() {
	}
	
	public boolean addGenesisBlock() {
		if (blocks.size() > 0)
			return false;
		else {
			Message msg = new Message("Genesis block");
			Block blk = new Block(this);
			blk.getBlockHeader().setTargetValue(DIFFICULTY_GENESIS);
			blk.getPayload().addItem(msg);
			blk.findProofOfWork();
			blocks.add(blk);
			return true;
		}		
	}

	public Block getLastBlock() {
		if (blocks.size() > 0)
			return blocks.get(blocks.size()-1);
		else
			return null;
	}
	
	void addNewBlock(Block block) {
		if (block.isValid()) {
			blocks.add(block);
			block.setInChain();
			
			adjustCurrentDifficulty();
		}
	}
	
	/**
	 * Adjust the current difficulty, based on the time between the last two blocks.
	 * Current algorithm: If the time between the blocks is less than half of the
	 * target period, double the difficulty. If the time is more than twice the target,
	 * halve the difficulty. Difficulty cannot be less than 1.
	 */
	private void adjustCurrentDifficulty() {
		if (blocks.size() >= BLOCKS_PER_ADJUSTMENT+1) {
			long timeLastBlock = blocks.get(blocks.size()-1).getBlockHeader().getTimeStamp();
			long timePrevBlock = blocks.get(blocks.size()-(BLOCKS_PER_ADJUSTMENT+1)).getBlockHeader().getTimeStamp();
			long period = (timeLastBlock - timePrevBlock) / BLOCKS_PER_ADJUSTMENT;
			if (period * 2 < TARGET_PERIOD_MS)
				currentDifficulty *= 2;
			else if (TARGET_PERIOD_MS * 2 < period) {
				currentDifficulty = currentDifficulty / 2;
				if (currentDifficulty < 1) currentDifficulty = 1;
			}
		}
	}

	public long getCurrentDifficulty() {
		return currentDifficulty;
	}
	
	public SimpleIntegerProperty getBlockChainSizeProperty() {
		return blocks.getSizeProperty();
	}
	
	public int getBlockChainSize() {
		return blocks.size();
	}
	
	public Block getBlock(int index) {
		return blocks.get(index);
	}
}
