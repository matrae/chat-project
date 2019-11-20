package jabberwocky.letterBased.appClasses.dataClasses;

public abstract class TrainingUnit {
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TrainingUnit) {
			TrainingUnit s = (TrainingUnit) obj;
			return s.toString().equals(this.toString());
		} else {
			return false;
		}
	}
}
