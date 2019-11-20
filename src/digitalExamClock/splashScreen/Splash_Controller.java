package digitalExamClock.splashScreen;

import digitalExamClock.ExamClock;
import digitalExamClock.abstractClasses.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards
 */
public class Splash_Controller extends Controller<Splash_Model, Splash_View> {

    public Splash_Controller(final ExamClock main, Splash_Model model, Splash_View view) {
        super(model, view);
        
        // We could monitor the progress property and pass it on to the progress bar
        // However, JavaFX can also do this for us: We just bind the progressProperty of the
        // progress bar to the progressProperty of the task.
        view.progress.progressProperty().bind(model.initializer.progressProperty());

        // The stateProperty tells us the status of the task. When the state is SUCCEEDED,
        // the task is finished, so we tell the main program to continue.

        // Below are two equivalent implementations - only one of these should be used!
        
        // Using an anonymous class
//        model.initializer.stateProperty().addListener(
//                new ChangeListener<Worker.State>() {
//                    @Override
//                    public void changed(
//                            ObservableValue<? extends Worker.State> observable,
//                            Worker.State oldValue, Worker.State newValue) {
//                        if (newValue == Worker.State.SUCCEEDED)
//                            main.startApp();
//                    }
//                });
        
        // Using a lambda expression
        model.initializer.stateProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue == Worker.State.SUCCEEDED)
                        main.startApp();
                });
    }
}
