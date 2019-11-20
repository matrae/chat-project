package simpleCoin.model.payload;

import java.nio.charset.StandardCharsets;

public class Message extends PayloadItem {
	String message;
	
	public Message(String message) {
		super();
		this.message = message;
	}

	@Override
	public byte[] toByteArray() {
		return message.getBytes(StandardCharsets.UTF_8);
	}
}
