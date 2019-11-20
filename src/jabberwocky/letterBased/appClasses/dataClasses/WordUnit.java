package jabberwocky.letterBased.appClasses.dataClasses;

public class WordUnit extends TrainingUnit {
	private String word;
	
	public WordUnit(String word) {
		super();
		this.word = word;
	}
	
	@Override
	public String toString() {
		String s = (word.charAt(word.length()-1) == '\n') ? word : word + " ";
		return s;
	}
}
