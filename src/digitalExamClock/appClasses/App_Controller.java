package digitalExamClock.appClasses;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import digitalExamClock.ServiceLocator;
import digitalExamClock.abstractClasses.Controller;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards
 */
public class App_Controller extends Controller<App_Model, App_View> implements Runnable {
	ServiceLocator serviceLocator;

	private final SimpleDoubleProperty fontSizeSmall = new SimpleDoubleProperty();
	private final SimpleDoubleProperty fontSizeLarge = new SimpleDoubleProperty();
	private final SimpleDoubleProperty textFieldWidth = new SimpleDoubleProperty();

	public App_Controller(App_Model model, App_View view) {
		super(model, view);

		serviceLocator = ServiceLocator.getServiceLocator();
		serviceLocator.getLogger().info("Application controller initialized");
		
		// Bind font-sizes of GUI elements to window size
	      fontSizeSmall.bind(view.scene.widthProperty().divide(20));
	      textFieldWidth.bind(view.scene.widthProperty().divide(4.5));
	      fontSizeLarge.bind(view.scene.widthProperty().divide(5));

	      view.lblStartTime.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSizeSmall.asString(), ";"));
	      view.lblEndTime.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSizeSmall.asString(), ";"));
	      
	      view.txtStartTime.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSizeSmall.asString(), ";",
	    		  "-fx-pref-width: ", textFieldWidth.asString(), ";"));
	      view.txtEndTime.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSizeSmall.asString(), ";",
	    		  "-fx-pref-width: ", textFieldWidth.asString(), ";"));

	      view.lblMainTime.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSizeLarge.asString(), ";"));
	      
	      
		// Start thread; update time once per second
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	@Override
	public void run() {
		while (true) {
			LocalTime time = LocalTime.now();
			DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");

			Platform.runLater(() -> view.lblMainTime.setText(time.format(format)));

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
}
