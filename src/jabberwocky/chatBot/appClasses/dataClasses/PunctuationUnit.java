package jabberwocky.chatBot.appClasses.dataClasses;

public class PunctuationUnit extends TrainingUnit {
	private char character;
	
	public PunctuationUnit(char character) {
		super();
		this.character = character;
	}
	
	@Override
	public String toString() {
		return Character.toString(character);
	}
	
	public boolean isEndOfSentence() {
		return (character == '\n' || character == '.' || character == '!' || character == '?');
	}

	/**
	 * Used to identify punctuation characters. Note that EOL counts as a punctuation character.
	 */
	public static boolean isPunctuation(char in) {
		if (in == '\n') return true;
		if (in >= 0x21 && in <= 0x2F) return true;
		if (in >= 0x3A && in <= 0x40) return true;
		if (in >= 0x5B && in <= 0x60) return true;
		if (in >= 0x7B && in <= 0x7E) return true;		
		if (in >= 0x2012 && in <= 0x2015) return true; // en-dash, em-dash, etc.
		return false;
	}
}
