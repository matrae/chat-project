package simpleCoin.model.payload;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import simpleCoin.utility.Hashable;

public class Payload implements Hashable {
	private ArrayList<PayloadItem> items = new ArrayList<>();
	private boolean finalized = false;
	
	public boolean addItem(PayloadItem item) {
		if (!finalized) items.add(item);
		return !finalized;
	}
	
	/**
	 * When the payload is finalized, no more items can be added. This is a one-way option - you cannot "unfinalize" the payload.
	 */
	public void finalize() {
		finalized = true;
	}
	
	public boolean isFinalized() {
		return finalized;
	}

	public int size() {
		return items.size();
	}
	
	public PayloadItem getItem(int index) {
		PayloadItem item = null;
		if (index >= 0 && index < items.size()) item = items.get(index);
		return item;
	}
	
	/**
	 * Concatenate the hashes of all PayloadItems
	 */
	@Override
	public byte[] toByteArray() {
		ByteBuffer buffer = ByteBuffer.allocate(Hashable.BYTES * items.size());
		for (PayloadItem item : items) {
			buffer.put(item.getHash());			
		}
		return buffer.array();
	}
}
