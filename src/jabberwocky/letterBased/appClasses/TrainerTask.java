package jabberwocky.letterBased.appClasses;

import java.util.ArrayList;
import java.util.HashMap;

import jabberwocky.letterBased.ServiceLocator;
import jabberwocky.letterBased.ServiceLocator.Mode;
import jabberwocky.letterBased.appClasses.dataClasses.BOF_Unit;
import jabberwocky.letterBased.appClasses.dataClasses.CharUnit;
import jabberwocky.letterBased.appClasses.dataClasses.EOF_Unit;
import jabberwocky.letterBased.appClasses.dataClasses.HashEntry;
import jabberwocky.letterBased.appClasses.dataClasses.Sequence;
import jabberwocky.letterBased.appClasses.dataClasses.TrainingUnit;
import jabberwocky.letterBased.appClasses.dataClasses.WordUnit;
import javafx.concurrent.Task;

public class TrainerTask extends Task<Void> {
	private HashMap<String, ArrayList<HashEntry>> trainedData = new HashMap<>();
	private StringBuffer data; // The data to train on
	ServiceLocator serviceLocator;

	public TrainerTask(App_Model model, StringBuffer data) {
		super();
		this.trainedData = model.getTrainedData();
		this.data = data;
		serviceLocator = ServiceLocator.getServiceLocator();
		serviceLocator.getLogger().info("Trainer task created");
	}
	
	@Override
	protected Void call() throws Exception {
		preprocessData(data); // Remove excess whitespace
		Sequence sequence = new Sequence(BOF_Unit.BOF);

		double originalLength = data.length();
		while (data.length() > 0) {
			this.updateProgress((originalLength - data.length()), originalLength);
			TrainingUnit tu = parseTrainingUnit(data);
			trainOneUnit(sequence, tu);
			sequence.addUnit(tu, serviceLocator.getSequenceLength());
		}
		trainOneUnit(sequence, EOF_Unit.EOF);
		return null;
	}
	
	/**
	 * Clean input data in the given StringBuffer by:
	 * - Removing any whitespace at the start or end of the file.
	 * - Removing doubled whitespace characters
	 * - Removing windows-style line-breaks
	 * - Replacing fancy quotes and apostrophes with ASCII characters
	 */
	private void preprocessData(StringBuffer sb) {
		// Remove whitespace at start and end
		while (sb.length() > 0 && sb.charAt(0) <= 0x20)
			sb.deleteCharAt(0);
		while (sb.length() > 0 && sb.charAt(sb.length() - 1) <= 0x20)
			sb.deleteCharAt(sb.length() - 1);

		// Remove doubled whitespace characters
		for (int pos = 0; pos < sb.length() - 1; pos++) {
			while (sb.length() > 1 && sb.charAt(pos) <= 0x20 && sb.charAt(pos) == sb.charAt(pos + 1))
				sb.deleteCharAt(pos + 1);
		}
		
		// Remove Windows-style linebreaks (delete ^M characters)
		for (int pos = sb.length()-1; pos >= 0; pos--) {
			if (sb.charAt(pos) == 0x0013) sb.deleteCharAt(pos);
		}
		
		// Replace fancy quotation marks and apostrophes with ASCII
		for (int pos = 0; pos < sb.length() - 1; pos++) {
			// Apostrophes of various sorts
			if (sb.charAt(pos) == 0x2018) sb.replace(pos, pos+1, "'");
			if (sb.charAt(pos) == 0x2019) sb.replace(pos, pos+1, "'");
			if (sb.charAt(pos) == 0x201A) sb.replace(pos, pos+1, "'");
			if (sb.charAt(pos) == 0x201B) sb.replace(pos, pos+1, "'");
			
			// Quotation marks of various sorts
			if (sb.charAt(pos) == 0x00AB) sb.replace(pos, pos+1, "\"");
			if (sb.charAt(pos) == 0x00BB) sb.replace(pos, pos+1, "\"");
			if (sb.charAt(pos) == 0x201C) sb.replace(pos, pos+1, "\"");
			if (sb.charAt(pos) == 0x201D) sb.replace(pos, pos+1, "\"");
			if (sb.charAt(pos) == 0x201E) sb.replace(pos, pos+1, "\"");
			if (sb.charAt(pos) == 0x201F) sb.replace(pos, pos+1, "\"");
			
			// Hyphens of various sorts
			if (sb.charAt(pos) == 0x2010) sb.replace(pos, pos+1, "-");
			if (sb.charAt(pos) == 0x2011) sb.replace(pos, pos+1, "-");
			
			// Spaces of various sorts
			if (sb.charAt(pos) == 0x00A0) sb.replace(pos, pos+1, " ");
			if (sb.charAt(pos) >= 0x2002 && sb.charAt(pos) <= 0x200B) sb.replace(pos, pos+1, " ");
			if (sb.charAt(pos) == 0x202F) sb.replace(pos, pos+1, " ");
		}		
	}	

	/**
	 * Parse one unit from the beginning of the StringBuffer, removing the parsed
	 * data.
	 */
	private TrainingUnit parseTrainingUnit(StringBuffer sb) {
		TrainingUnit tu = null;
		if (serviceLocator.getMode() == Mode.CharacterMode) {
			tu = new CharUnit(sb.charAt(0));
			sb.deleteCharAt(0);
		} else if (serviceLocator.getMode() == Mode.WordMode1) {
			String word;
			// parse out one word, including any punctuation or end-of-line character as
			// part of the word
			int posSpace = sb.indexOf(" ");
			int posEOL = sb.indexOf("\n");

			if (posSpace >= 0 && (posEOL < 0 || posSpace < posEOL)) {
				// Parse to next space, discarding the space
				word = sb.substring(0, posSpace);
				sb.delete(0, posSpace + 1);
			} else if (posEOL >= 0 && (posSpace < 0 || posEOL < posSpace)) {
				// Parse to next EOL, including the EOL character
				word = sb.substring(0, posEOL + 1);
				sb.delete(0, posEOL + 1);
			} else { // last word of the file
				word = sb.toString();
				sb.delete(0, sb.length());
			}
			tu = new WordUnit(word);
		} else if (serviceLocator.getMode() == Mode.WordMode2) {
			String word;
			
			if (isPunctuation(sb.charAt(0), true)) {
				// If next character is punctuation, then it is the next training unit
				word = sb.substring(0, 1);
				assert(word.length() > 0);
				sb.delete(0,  1);
			} else {
				// Parse word to the next punctuation, space or end-of-line character.
				boolean found = false;
				int pos = 0;
				int end = -1;
				while (!found && pos < sb.length()) {
					if (isPunctuation(sb.charAt(pos), true)) {
						end = pos;
						found = true;
					}
					pos++;
				}
				if (end >= 0) { // Found word
					word = sb.substring(0, end);
					assert(word.length() > 0);
					sb.delete(0, end);
					// if the next character is a space, discard it
					if (sb.charAt(0) == ' ') sb.delete(0,  1);
				} else { // This is the last word in the StringBuffer
					word = sb.toString();
					assert(word.length() > 0);
					sb.delete(0, sb.length());
				}
			}
			tu = new WordUnit(word);
		}
		return tu;
	}	
	
	/**
	 * Add one unit to the training data
	 */
	private void trainOneUnit(Sequence sequence, TrainingUnit c) {
		ArrayList<HashEntry> hashEntries = getHashEntries(sequence);
		boolean found = false;
		for (HashEntry entry : hashEntries) {
			if (entry.getFollowingUnit().equals(c)) {
				entry.incrementQuantity();
				found = true;
				break;
			}
		}
		if (!found) {
			hashEntries.add(new HashEntry(1, c));
		}
	}
	
	// When parsing in a punctuation-sensitive manner, we need to know if a particular character is, in fact, a punctuation character. We can optionally include spaces and line-breaks in the test.
	private boolean isPunctuation(char in, boolean includeSpaceAndLineBreak) {
		if (includeSpaceAndLineBreak && (in == ' ' || in == '\n')) return true;
		if (in >= 0x21 && in <= 0x2F) return true;
		if (in >= 0x3A && in <= 0x40) return true;
		if (in >= 0x5B && in <= 0x60) return true;
		if (in >= 0x7B && in <= 0x7E) return true;		
		if (in >= 0x2012 && in <= 0x2015) return true; // en-dash, em-dash, etc.
		return false;
	}

	/**
	 * This method returns the list of HashEntries for the character-sequence given.
	 */
	private ArrayList<HashEntry> getHashEntries(Sequence sequence) {
		ArrayList<HashEntry> hashEntries = trainedData.get(sequence.toString());
		if (hashEntries == null) {
			hashEntries = new ArrayList<>();
			trainedData.put(sequence.toString(), hashEntries);
		}
		return hashEntries;
	}
}
