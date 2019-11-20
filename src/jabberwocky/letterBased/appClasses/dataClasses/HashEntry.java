package jabberwocky.letterBased.appClasses.dataClasses;

/**
 * A HashEntry combines an integer - the relative frequency - with a following
 * TrainingUnit
 */
public class HashEntry {
	private int quantity;
	private TrainingUnit followingUnit;

	public HashEntry(int quantity, TrainingUnit followingUnit) {
		this.quantity = quantity;
		this.followingUnit = followingUnit;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void incrementQuantity() {
		quantity++;
	}
	
	public TrainingUnit getFollowingUnit() {
		return followingUnit;
	}
}
