package networkTools;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * A very simple program to test outgoing ports, to see which ones are open.
 * Normally, outgoing ports 1024 and higher are available, but some corporate
 * firewalls may block them.
 * 
 * Command line parameters, if present, represent:
 * 
 * - The external address to contact (by default google.ch)
 * - The external port number to contact (by default 80)
 * - The starting port number to test (by default 1024)
 * - The ending port number to test (by default 65535)
 * 
 * To speed the testing, the program runs 10 parallel threads. If a firewall is
 * discarding packets, this can take a while, as each connection attempt will
 * have to time out.
 */
public class FirewallTester {
	private static String externalAddress = "google.ch";
	private static int externalPort = 80;
	private static int startPort = 1024;
	private static int endPort = 65535;
	private static boolean[] openPorts = new boolean[65536];

	public static void main(String[] args) {
		if (args.length >= 1) externalAddress = args[0];
		if (args.length >= 2) externalPort = Integer.parseInt(args[1]);
		if (args.length >= 3) startPort = Integer.parseInt(args[2]);
		if (args.length >= 4) endPort = Integer.parseInt(args[3]);

		startTesters();
		
		printResults();
	}
	
	/**
	 * start 10 threads to share the work
	 */
	private static void startTesters() {
		Thread[] threads = new Thread[10];
		
		int diff = endPort + 1 - startPort;
		for (int i = 0; i < 10; i++) {
			int start = startPort + i * diff / 10;
			int end = startPort + (i+1) * diff / 10 - 1;
			threads[i] = new Tester(start, end);
			threads[i].start();
		}
		
		for (int i = 0; i < 10; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	private static void printResults() {
		boolean currentResult = openPorts[startPort];
		int port = startPort;
		int rangeStart = port;
		while (port < endPort) {
			port++;
			if (openPorts[port] != currentResult) {
				System.out.println("ports are " + (currentResult ? "open" : "closed") + " in the range " + rangeStart + " - " + (port-1));
				rangeStart = port;
				currentResult = openPorts[port];
			}
		}
		System.out.println("ports are " + (currentResult ? "open" : "closed") + " in the range " + rangeStart + " - " + endPort);
	}
	
	private static class Tester extends Thread {
		private int startPort;
		private int endPort;

		public Tester(int startPort, int endPort) {
			this.startPort = startPort;
			this.endPort = endPort;
		}
		
		@Override
		public void run() {
			Socket s = null;
			for (int port = startPort; port <= endPort; port++) {
				openPorts[port] = false;
				try {
					s = new Socket();
					s.bind(new InetSocketAddress(port));
					InetSocketAddress addr = new InetSocketAddress(externalAddress, externalPort); 
					s.connect(addr, 1000); // Timeout of 1 second
					openPorts[port] = true;
				} catch (Exception e1) {
				} finally {
					if (s != null) {
						try { s.close(); } catch (Exception e) {}
					}
				}
				
				// Try not to look like a DDOS attack
				try { sleep(100); } catch (InterruptedException e) { }
			}
		}
		
	}
}
