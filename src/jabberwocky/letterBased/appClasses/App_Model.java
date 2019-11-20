package jabberwocky.letterBased.appClasses;

import java.util.ArrayList;
import java.util.HashMap;

import jabberwocky.letterBased.ServiceLocator;
import jabberwocky.letterBased.abstractClasses.Model;
import jabberwocky.letterBased.appClasses.dataClasses.*;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards
 */
public class App_Model extends Model {
	ServiceLocator serviceLocator;
	private HashMap<String, ArrayList<HashEntry>> trainedData = new HashMap<>();

	public App_Model() {
		serviceLocator = ServiceLocator.getServiceLocator();
		serviceLocator.getLogger().info("Application model initialized");
	}

	public int getNumEntries() {
		return trainedData.size();
	}

	public int getNumLinks() {
		int sum = 0;
		for (ArrayList<HashEntry> list : trainedData.values()) {
			for (HashEntry he : list)
				sum += he.getQuantity();
		}
		return sum;
	}

	/**
	 * This method clears the training data
	 */
	public void clearTrainingData() {
		trainedData = new HashMap<>();
		serviceLocator.setMode(null);
		serviceLocator.setSequenceLength(0);
	}

	/**
	 * Generate text using the training data.
	 */
	public String generateText() {
		StringBuffer sb = new StringBuffer();
		Sequence sequence = new Sequence(BOF_Unit.BOF);
		TrainingUnit t = BOF_Unit.BOF;
		while (!t.equals(EOF_Unit.EOF)) {
			t = genOneUnit(sequence);
			if (!t.equals(EOF_Unit.EOF)) sb.append(t.toString());
			sequence.addUnit(t, serviceLocator.getSequenceLength());
		}
		postprocessData(sb);
		return sb.toString();
	}

	/**
	 * This method generates a single unit, based on the given sequence
	 */
	private TrainingUnit genOneUnit(Sequence sequence) {
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


	
	/**
	 * Format the output data for better display, by doubling line-breaks.
	 */
	private void postprocessData(StringBuffer sb) {
		for (int pos = 0; pos < sb.length(); pos++) {
			if (sb.charAt(pos) == '\n') {
				sb.insert(pos, '\n');
				pos++;
			}
		}
	}
	
	public HashMap<String, ArrayList<HashEntry>> getTrainedData() {
		return trainedData;
	}
}
