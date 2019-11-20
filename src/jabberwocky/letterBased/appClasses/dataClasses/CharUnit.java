package jabberwocky.letterBased.appClasses.dataClasses;

public class CharUnit extends TrainingUnit {
	private char character;
	
	public CharUnit(char character) {
		super();
		this.character = character;
	}
	
	@Override
	public String toString() {
		return Character.toString(character);
	}
}
