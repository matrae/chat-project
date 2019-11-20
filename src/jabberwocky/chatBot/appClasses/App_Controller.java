package jabberwocky.chatBot.appClasses;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import jabberwocky.chatBot.ServiceLocator;
import jabberwocky.chatBot.abstractClasses.Controller;
import jabberwocky.chatBot.appClasses.dataClasses.TrainingUnit;
import jabberwocky.chatBot.appClasses.training.TrainerTask;
import jabberwocky.chatBot.appClasses.training.GeneratorTask;
import jabberwocky.chatBot.commonClasses.Utility;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.stage.FileChooser;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards
 */
public class App_Controller extends Controller<App_Model, App_View> {
	ServiceLocator serviceLocator;
	ChangeListener<Worker.State> trainingChangeListener; // Reusable listener for training
	private TrainerTask trainerTask = null;
	ChangeListener<Worker.State> generatorChangeListener; // Reusable listener for generating
	private GeneratorTask generatorTask = null;

	public App_Controller(App_Model model, App_View view) {
		super(model, view);

		// register ourselves to listen for menu items
		view.menuFileTrain.setOnAction((e) -> train());
		view.menuFileClear.setOnAction((e) -> clear());
		
		// register ourselves to listen for chat messages
		view.txtChat.setOnAction((e) -> chatMessage());

		// control wrapping of generated text
		// view.btnGenerate.widthProperty()

		serviceLocator = ServiceLocator.getServiceLocator();
		serviceLocator.getLogger().info("Application controller initialized");
		
		trainingChangeListener = new ChangeListener<Worker.State>() {
          @Override
          public void changed(
                  ObservableValue<? extends Worker.State> observable,
                  Worker.State oldValue, Worker.State newValue) {
              if (newValue == Worker.State.SUCCEEDED)
                  trainingFinished();
          }
      };
      generatorChangeListener = new ChangeListener<Worker.State>() {
	          @Override
	          public void changed(
	                  ObservableValue<? extends Worker.State> observable,
	                  Worker.State oldValue, Worker.State newValue) {
	              if (newValue == Worker.State.SUCCEEDED)
	            	  generationFinished();
	          }
	      };
	}

	public void clear() {
		model.clearTrainingData();
		view.updateStatus();
	}

	public void train() {
		if (trainerTask == null) {
			FileChooser fileChooser = new FileChooser();
			List<File> files = fileChooser.showOpenMultipleDialog(view.getStage());
			if (files != null && files.size() > 0) trainOnFiles(files);
		}
	}
	
	private void trainOnFiles(List<File> files) {
		serviceLocator.setSequenceLength((int) view.sliderMaxSequenceLength.getValue());

			trainerTask = new TrainerTask(model, files);
			Thread ttt = new Thread(trainerTask);

			// Bind progress-bar to model's progress property
			view.progress.progressProperty().bind(trainerTask.progressProperty());
			view.progress.setVisible(true);

			// Set up binding for when training is finished
			trainerTask.stateProperty().addListener(trainingChangeListener);

			ttt.start();
	}
	
	public void trainingFinished() {
		// Remove event handlers and bindings
		view.progress.setVisible(false);
		view.progress.progressProperty().unbind();
		trainerTask.stateProperty().removeListener(trainingChangeListener);
		trainerTask = null;
		
		view.updateStatus();
	}
	
	public void chatMessage() {
		String msg = view.txtChat.getText();
		appendChatMessage("Person", msg);
		view.txtChat.setText("");

		// Parse the message
		StringBuffer data = new StringBuffer(msg);
		ArrayList<TrainingUnit> sentence = Utility.parseSentence(data);
		
		// TODO: Train from chat message

		// TODO: Generate reply
		generateReply(sentence);
	}
	
	private void generateReply(ArrayList<TrainingUnit> sentence) {
		generatorTask = new GeneratorTask(model, sentence);
		Thread ttt = new Thread(generatorTask);

		// Bind progress-bar to model's progress property
		view.progress.progressProperty().bind(generatorTask.progressProperty());
		view.progress.setVisible(true);

		// Set up binding for when training is finished
		generatorTask.stateProperty().addListener(generatorChangeListener);

		ttt.start();
	}
	
	public void generationFinished() {
		// Get value and add to view
		String reply = generatorTask.getValue();
		appendChatMessage("Bot", reply);
		
		// Remove event handlers and bindings
		view.progress.setVisible(false);
		view.progress.progressProperty().unbind();
		generatorTask.stateProperty().removeListener(generatorChangeListener);
		generatorTask = null;
		
		view.updateStatus();
	}
	
	private void appendChatMessage(String who, String message) {
		String oldContent = view.txtChatHistory.getText();
		String newContent = oldContent + "\n\n" + who + ": " + message;
		view.txtChatHistory.setText(newContent);
		view.txtScroll.setVvalue(view.txtScroll.getVmax());
	}
}
