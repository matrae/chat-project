package jabberwocky.letterBased.appClasses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import jabberwocky.letterBased.ServiceLocator;
import jabberwocky.letterBased.ServiceLocator.Mode;
import jabberwocky.letterBased.abstractClasses.Controller;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;

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

	public App_Controller(App_Model model, App_View view) {
		super(model, view);

		// register ourselves to listen for menu items
		view.menuFileTrain.setOnAction((e) -> train());
		view.menuFileClear.setOnAction((e) -> clear());

		// register ourselves to listen for button clicks
		view.btnGenerate.setOnAction((e) -> buttonClick());

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
	}

	public void clear() {
		model.clearTrainingData();
		view.updateStatus();
	}

	public void train() {
		if (trainerTask == null) {
			FileChooser fileChooser = new FileChooser();
			File f = fileChooser.showOpenDialog(view.getStage());
			if (f != null) {
				try (BufferedReader in = new BufferedReader(new FileReader(f))) {
					StringBuffer sb = new StringBuffer();
					String line = in.readLine();
					while (line != null) {
						sb.append(line);
						sb.append("\n");
						line = in.readLine();
					}
					serviceLocator.setSequenceLength((int) view.sliderNumLetters.getValue());
					serviceLocator.setMode(getModeFromView());

					trainerTask = new TrainerTask(model, sb);
					Thread ttt = new Thread(trainerTask);

					// Bind progress-bar to model's progress property
					view.progress.progressProperty().bind(trainerTask.progressProperty());
					 view.progress.setVisible(true);

					// Set up binding for when training is finished
					trainerTask.stateProperty().addListener(trainingChangeListener);

					ttt.start();
				} catch (Exception e) {
					serviceLocator.getLogger().severe(e.toString());
				}
			}
		}
	}
	
	public void trainingFinished() {
		// Remove event handlers and bindings
		view.progress.setVisible(false);
		view.progress.progressProperty().unbind();
		trainerTask.stateProperty().removeListener(trainingChangeListener);
		trainerTask = null;
		
		view.updateStatus();
	}

	private Mode getModeFromView() {
		if (view.rdoChar.isSelected()) return Mode.CharacterMode;
		if (view.rdoWord.isSelected()) return Mode.WordMode1;
		if (view.rdoWord2.isSelected()) return Mode.WordMode2;
		return null; // Should never happen
	}

	public void buttonClick() {
		String out = model.generateText();
		view.txtGeneratedText.setText(out);
	}
}
