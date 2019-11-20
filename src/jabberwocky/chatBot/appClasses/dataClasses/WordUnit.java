package jabberwocky.chatBot.appClasses.dataClasses;

public class WordUnit extends TrainingUnit {
	private String word;
	
	public WordUnit(String word) {
		super();
		this.word = word;
	}
	
	@Override
	public String toString() {
		return word;
	}
}
