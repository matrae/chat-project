package jabberwocky.chatBot.appClasses.training;

import java.util.ArrayList;
import java.util.HashMap;

import jabberwocky.chatBot.ServiceLocator;
import jabberwocky.chatBot.appClasses.App_Model;
import jabberwocky.chatBot.appClasses.TrainedData;
import jabberwocky.chatBot.appClasses.dataClasses.TrainingUnit;
import jabberwocky.chatBot.appClasses.dataClasses.WordUnit;
import jabberwocky.chatBot.commonClasses.Utility;
import jabberwocky.chatBot.appClasses.dataClasses.BOF_Unit;
import jabberwocky.chatBot.appClasses.dataClasses.EOF_Unit;
import jabberwocky.chatBot.appClasses.dataClasses.HashEntry;
import jabberwocky.chatBot.appClasses.dataClasses.Sequence;
import javafx.concurrent.Task;

public class GeneratorTask extends Task<String> {
	private TrainedData trainedData;
	private ServiceLocator serviceLocator;
	private ArrayList<TrainingUnit> sentence;
	private static final int MIN_LENGTH_INTERESTING_WORD = 5;

	public GeneratorTask(App_Model model, ArrayList<TrainingUnit> sentence) {
		super();
		serviceLocator = ServiceLocator.getServiceLocator();
		this.trainedData = model.getFoundationData();
		this.sentence = sentence;
	}

	@Override
	protected String call() throws Exception {
		// TODO: Find least-common word in user entry,
		// but ignore really short words
		int frequency = Integer.MAX_VALUE;
		TrainingUnit key_tu = null;
		for (TrainingUnit tu : sentence) {
			if (tu.toString().length() >= MIN_LENGTH_INTERESTING_WORD) {
				Integer tu_freq = trainedData.wordFrequencies.get(tu);
				if (tu_freq != null && tu_freq < frequency) {
					frequency = tu_freq;
					key_tu = tu;
				} else {
					serviceLocator.getLogger().fine("Word '" + tu + "' not found in training data");
				}
			}
		}
		
		// If we did not recognize any of the words used, pick a random word
		if (key_tu == null) {
			WordUnit[] allWords = trainedData.wordFrequencies.keySet().toArray(new WordUnit[0]);
			key_tu = allWords[(int) (Math.random() * allWords.length)];
		}
		
		// Generate from the chosen word back to BOF
		TrainingUnit t = key_tu;
		Sequence sequence = new Sequence(t); // Used for generation
		ArrayList<TrainingUnit> sentence = new ArrayList<>(); // Full text generated
		sentence.add(t);
		while (!t.equals(BOF_Unit.BOF)) {
			// Determine which trained-data to use: the length of the sequence so far,
			// up to the maximum length selected by the user
			int trainedDataIndex = Math.min(sentence.size(), serviceLocator.getSequenceLength());
			HashMap<String, ArrayList<HashEntry>> useTrainedData = trainedData.reverseSequences.get(trainedDataIndex-1);
			
			// Generate the next unit in the sequence
			t = genOneUnit(useTrainedData, sequence);
			sequence.addUnit(t, serviceLocator.getSequenceLength());
			sentence.add(t);
		}
		
		// Generate forward to EOF
		sentence = Utility.reverse(sentence);
		
		// Rebuild sequence to be the last N words of the sentence so far
		int seqSize = sequence.size();
		sequence = new Sequence();
		for (int i = sentence.size() - seqSize; i < sentence.size(); i++) {
			sequence.addUnit(sentence.get(i), seqSize);
		}
		
		while (!t.equals(EOF_Unit.EOF)) {
			// Determine which trained-data to use: the length of the sequence so far,
			// up to the maximum length selected by the user
			int trainedDataIndex = Math.min(sentence.size(), serviceLocator.getSequenceLength());
			HashMap<String, ArrayList<HashEntry>> useTrainedData = trainedData.forwardSequences.get(trainedDataIndex-1);
			
			// Generate the next unit in the sequence
			t = genOneUnit(useTrainedData, sequence);
			sequence.addUnit(t, serviceLocator.getSequenceLength());
			sentence.add(t);
		}
		
		// Prettify the output
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < sentence.size()-1; i++) {
			sb.append(sentence.get(i));
			if (sentence.get(i+1).getClass() == WordUnit.class) sb.append(" ");
		}
		
		return sb.toString();
	}
	
	private TrainingUnit genOneUnit(HashMap<String, ArrayList<HashEntry>> trainedData, Sequence sequence) {
		ArrayList<HashEntry> hashEntries = trainedData.get(sequence.toString());
		int totalOptions = sum(hashEntries);
		int pick = (int) (Math.random() * totalOptions);
		TrainingUnit t = pickUnit(hashEntries, pick);
		return t;
	}

	/**
	 * Count the total options in the list of HashEntries
	 */
	private int sum(ArrayList<HashEntry> hashEntries) {
		int sum = 0;
		for (HashEntry he : hashEntries)
			sum += he.getQuantity();
		return sum;
	}

	/**
	 * Pick a unit based on the number given
	 */
	private TrainingUnit pickUnit(ArrayList<HashEntry> hashEntries, int pick) {
		int sum = 0;
		TrainingUnit t = null;
		for (int i = 0; i < hashEntries.size(); i++) {
			HashEntry he = hashEntries.get(i);
			sum += he.getQuantity();
			if (sum > pick) {
				t = he.getFollowingUnit();
				break;
			}
		}
		return t;
	}
}
