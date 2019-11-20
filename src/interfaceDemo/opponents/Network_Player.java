package interfaceDemo.opponents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import interfaceDemo.commons.Comm;

/**
 * Very simple networking class: always runs on port 54321, and always uses the
 * loopback address. Messages are simply strings, derived from the enumeration.
 * 
 * Note: this class has no error handling, and resources are not closed!!
 */
public class Network_Player extends Thread implements Comm {
	private static final String IP_ADDRESS = "127.0.0.1";
	private static final int PORT = 54321;

	private static enum Messages {
		SELECT, DESELECT
	};

	private ObjectInputStream in;
	private ObjectOutputStream out;

	private Comm player;

	public Network_Player(Comm player, boolean isServer) {
		super(isServer ? "Network Server" : "Network Player");
		this.player = player;

		try {
			Socket socket;
			if (isServer) {
				ServerSocket listener = new ServerSocket(PORT, 10, null);
				socket = listener.accept();
			} else {
				socket = new Socket(IP_ADDRESS, PORT);
			}
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			this.setDaemon(true);
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Methods to send messages to opponent
	@Override
	public void select() {
		try {
			out.writeObject(Messages.SELECT);
			out.flush();
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}

	@Override
	public void deselect() {
		try {
			out.writeObject(Messages.DESELECT);
			out.flush();
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Messages msg = (Messages) in.readObject();
				if (msg == Messages.SELECT) {
					player.select();
				} else { // DESELECT
					player.deselect();
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}
}
