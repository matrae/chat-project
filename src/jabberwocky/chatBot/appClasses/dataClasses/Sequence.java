package jabberwocky.chatBot.appClasses.dataClasses;

import java.util.ArrayList;

/**
 * A sequence is an ordered list of TrainingUnits, which may be characters, or
 * words, or potentially some other division of the training texts. Since
 * sequences are used as the key-values in the HashMap of trained data, the
 * hashcode method must account for the entire contents of the sequence, i.e.,
 * all TrainingUnits.
 */
public class Sequence extends ArrayList<TrainingUnit> {
	
	public Sequence() {
		super();
	}

	public Sequence(TrainingUnit unit) {
		super();
		this.add(unit);
	}
	
	/**
	 * Add a unit to the end of this sequence. If the total number of units exceeds
	 * the specified limit, remove units from the from of the sequence.
	 * 
	 * @param unit
	 *            The unit to add to the sequence
	 * @param limit
	 *            The total number of units allowed in the sequence
	 */
	public void addUnit(TrainingUnit unit, int limit) {
		this.add(unit);
		while (this.size() > limit)
			this.remove(0);
	}
	
	/**
	 * Reverse this sequence in place
	 */
	public void reverse() {
		for (int i = 0; i < this.size()/2; i++) {
			TrainingUnit tmp = this.get(i);
			this.set(i, this.get(this.size() - i - 1));
			this.set((this.size() - i - 1), tmp);
		}
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Sequence) {
			Sequence s = (Sequence) obj;
			return s.toString().equals(this.toString());
		} else {
			return false;
		}
	}

	/**
	 * The toString method converts all TrainingUnits into Strings. This explicitly
	 * includes all units, including ones that should not actually generate output
	 * (such as the beginning and ending markers).
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (TrainingUnit unit : this)
			sb.append(unit.toString());
		return sb.toString();
	}
}
