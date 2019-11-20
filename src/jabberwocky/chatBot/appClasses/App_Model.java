package jabberwocky.chatBot.appClasses;

import jabberwocky.chatBot.ServiceLocator;
import jabberwocky.chatBot.abstractClasses.Model;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards
 */
public class App_Model extends Model {
	ServiceLocator serviceLocator;
	private TrainedData foundationData;

	public App_Model() {
		serviceLocator = ServiceLocator.getServiceLocator();
		serviceLocator.getLogger().info("Application model initialized");
	}

	/**
	 * This method clears the training data
	 */
	public void clearTrainingData() {
		foundationData = null;
		serviceLocator.setSequenceLength(0);
	}
	
	public TrainedData getFoundationData() {
		return foundationData;
	}
	
	public void setFoundationData(TrainedData trainedData) {
		this.foundationData = trainedData;
	}
}
