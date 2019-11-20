package simpleCoin.model;

import java.util.ArrayList;

import javafx.beans.property.SimpleIntegerProperty;
import simpleCoin.model.payload.PayloadItem;
import simpleCoin.utility.MonitorableArrayList;

public class SimpleCoinModel {
	final BlockChain blockchain = new BlockChain();
	final MonitorableArrayList<PayloadItem> payloadPool = new MonitorableArrayList<>();

	public SimpleCoinModel() {
		blockchain.addGenesisBlock();
	}
	
	public BlockChain getBlockChain() {
		return blockchain;
	}
	
	public SimpleIntegerProperty getPayloadPoolSizeProperty() {
		return payloadPool.getSizeProperty();
	}
	
	public MonitorableArrayList<PayloadItem> getPayloadPool() {
		return payloadPool;
	}
	
	public int getPayloadPoolSize() {
		return payloadPool.size();
	}
	
	public void addPayloadItem(PayloadItem item) {
		payloadPool.add(item);
	}
}
