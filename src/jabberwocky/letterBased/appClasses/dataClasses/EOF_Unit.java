package jabberwocky.letterBased.appClasses.dataClasses;

/**
 * Marker for the end of a text (EOF = End of File)
 */
public class EOF_Unit extends TrainingUnit {
	public static final EOF_Unit EOF = new EOF_Unit();
	
	private EOF_Unit() {}
	
	@Override
	public String toString() {
		return "<EOF>";
	}
}
