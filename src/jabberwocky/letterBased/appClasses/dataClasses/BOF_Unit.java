package jabberwocky.letterBased.appClasses.dataClasses;

/**
 * Marker for the beginning of a text (BOF = Beginning of File)
 */
public class BOF_Unit extends TrainingUnit {
	public static final BOF_Unit BOF = new BOF_Unit();
	
	private BOF_Unit() {}
	
	@Override
	public String toString() {
		return "<BOF>";
	}
}
