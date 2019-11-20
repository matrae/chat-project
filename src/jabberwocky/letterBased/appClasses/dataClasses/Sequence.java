package jabberwocky.letterBased.appClasses.dataClasses;

import java.util.ArrayList;

/**
 * A sequence is an ordered list of TrainingUnits, which may be characters, or
 * words, or potentially some other division of the training texts. Since
 * sequences are used as the key-values in the HashMap of trained data, the
 * hashcode method must account for the entire contents of the sequence, i.e.,
 * all TrainingUnits.
 */
public class Sequence {
	ArrayList<TrainingUnit> units = new ArrayList<>();

	public Sequence(TrainingUnit unit) {
		units.add(unit);
	}
	
	/**
	 * Add a unit to the end of this sequence. If the total number of units exceeds
	 * the specified limit, remove units from the beginning of the sequence.
	 * 
	 * @param unit
	 *            The unit to add to the sequence
	 * @param limit
	 *            The total number of units allowed in the sequence
	 */
	public void addUnit(TrainingUnit unit, int limit) {
		units.add(unit);
		while (units.size() > limit)
			units.remove(0);
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
		for (TrainingUnit unit : units)
			sb.append(unit.toString());
		return sb.toString();
	}
}
