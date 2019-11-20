package jabberwocky.chatBot.appClasses;

import java.util.ArrayList;
import java.util.HashMap;
import jabberwocky.chatBot.appClasses.dataClasses.*;

/**
 * We maintain numerous HashMaps. For all sequence-lengths up to the maximum defined by the user,
 * we have a Map from sequences of that length to the next word, going both forwards and backwards. We also have a
 * simple frequency table that tracks how often each word has appeared in our training data.
 * 
 * Note that multiple instances of this class may be in use simultaneously. For example, we may have some
 * foundational training, to teach our 'bot the language, and then a second set of training data from the
 * current chat.
 */
public class TrainedData {
	public ArrayList<HashMap<String, ArrayList<HashEntry>>> forwardSequences;
	public ArrayList<HashMap<String, ArrayList<HashEntry>>> reverseSequences;
	public HashMap<WordUnit, Integer> wordFrequencies;
	
	public TrainedData(int sequenceLength) {
		forwardSequences = new ArrayList<HashMap<String, ArrayList<HashEntry>>>();
		initMaps(forwardSequences, sequenceLength);
		reverseSequences = new ArrayList<HashMap<String, ArrayList<HashEntry>>>();
		initMaps(reverseSequences, sequenceLength);
		wordFrequencies = new HashMap<WordUnit, Integer>();
	}
	
	private void initMaps(ArrayList<HashMap<String, ArrayList<HashEntry>>> maps, int length) {
		for (int i = 0; i < length; i++) maps.add(new HashMap<String, ArrayList<HashEntry>>());
	}
	
	/**
	 * The number of different words that we have encountered
	 */
	public long getNumWords() {
		return wordFrequencies.size();
	}
	
	/**
	 * To give a rough idea of the complexity of the training data, we count
	 * the total number of links in the longest forwardSequence map
	 */
	public long getNumLinks() {
		HashMap<String, ArrayList<HashEntry>> longestMap = forwardSequences.get(forwardSequences.size()-1);
		int sum = 0;
		for (ArrayList<HashEntry> list : longestMap.values()) {
			for (HashEntry he : list)
				sum += he.getQuantity();
		}
		return sum;
	}
}
