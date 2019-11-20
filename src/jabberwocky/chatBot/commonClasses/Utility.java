package jabberwocky.chatBot.commonClasses;

import java.util.ArrayList;

import jabberwocky.chatBot.appClasses.dataClasses.BOF_Unit;
import jabberwocky.chatBot.appClasses.dataClasses.EOF_Unit;
import jabberwocky.chatBot.appClasses.dataClasses.PunctuationUnit;
import jabberwocky.chatBot.appClasses.dataClasses.TrainingUnit;
import jabberwocky.chatBot.appClasses.dataClasses.WordUnit;

public class Utility {
	/**
	 * Create a copy of an ArrayList with the elements in reverse order
	 */
	public static <T> ArrayList<T> reverse(ArrayList<T> list) {
		ArrayList<T> reversedList = new ArrayList<>();
		for (int i = list.size()-1; i >= 0; i--) {
			reversedList.add(list.get(i));
		}
		return reversedList;
	}
	
	public static ArrayList<TrainingUnit> parseSentence(StringBuffer data) {
		ArrayList<TrainingUnit> sentence = new ArrayList<>();
		sentence.add(BOF_Unit.BOF);
		boolean endOfSentence = false;
		while (!endOfSentence && data.length() > 0) {
			TrainingUnit tu = parseTrainingUnit(data);
			
			if (tu.getClass() == PunctuationUnit.class) {
				PunctuationUnit pu = (PunctuationUnit) tu;
				endOfSentence = pu.isEndOfSentence();
			}
			sentence.add(tu);
		}
		sentence.add(EOF_Unit.EOF);		
		return sentence;
	}

	/**
	 * Parse one unit from the beginning of the StringBuffer, removing the parsed
	 * data.
	 */
	private static TrainingUnit parseTrainingUnit(StringBuffer sb) {
		TrainingUnit unit;
		
		// skip spaces (note: doubled spaces have already been deleted)
		if (sb.charAt(0) == ' ') sb.delete(0,  1);
		
		if (PunctuationUnit.isPunctuation(sb.charAt(0))) {
			// If next character is punctuation, then it is the next training unit
			char c = sb.charAt(0);
			unit = new PunctuationUnit(c);
			sb.delete(0,  1);
		} else {
			// Parse word to the next punctuation, space or end-of-line character.
			boolean found = false;
			int pos = 0;
			int end = -1;
			while (!found && pos < sb.length()) {
				if (sb.charAt(pos) == ' ' || PunctuationUnit.isPunctuation(sb.charAt(pos))) {
					end = pos;
					found = true;
				}
				pos++;
			}
			if (end >= 0) { // Found word
				unit = new WordUnit(sb.substring(0, end));
				sb.delete(0, end);
			} else { // This is the last word in the StringBuffer
				unit = new WordUnit(sb.toString());
				sb.delete(0, sb.length());
			}
		}
		return unit;
	}	
}
