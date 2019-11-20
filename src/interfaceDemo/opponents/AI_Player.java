package interfaceDemo.opponents;

import interfaceDemo.commons.Comm;

/**
 * This AI player simply does whatever the player just did, after a brief delay
 */
public class AI_Player implements Comm {
	private Comm player;
	
	public AI_Player(Comm player) {
		this.player = player;
	}

	@Override
	public void select() {
		Runnable r = () -> {
			try { Thread.sleep(500); } catch (InterruptedException e) {}
			player.select();			
		};
		new Thread(r).start();
	}

	@Override
	public void deselect() {
		Runnable r = () -> {
			try { Thread.sleep(500); } catch (InterruptedException e) {}
			player.deselect();			
		};
		new Thread(r).start();
	}
	
}
