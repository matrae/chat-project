package jabberwocky.letterBased.splashScreen;

import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * A Preloader is an independent GUI within an application that can be displayed
 * quickly. If a program is being started across the network, the classes for
 * the preloader can be packaged separately, so that they download first. When
 * the main program is ready, the Preloader is hidden, and the main program
 * takes its place on the stage.
 * 
 * In principle, a Preloader would be a great way to display a splash screen for
 * any program. Unfortunately, there is no simple way to start and debug a an
 * application and preloader in an IDE, and have them work exactly the same way
 * that they will when deployed. For this reason, the Application Template
 * *does* *not* use this Preloader. It is shown here only for completeness. For
 * further information, refer to the Java Tutorials:
 * https://docs.oracle.com/javafx/2/deployment/preloaders.htm
 * 
 * A Preloader can receive various kinds of updates from the main application.
 * This example includes all four, but only use the ones you need.
 * 
 * @author Brad Richards
 */
public class SplashScreen_Preloader extends Preloader {
    private Stage stage;
    private ProgressBar progress = new ProgressBar();
    private Label lblStatus = new Label();

    @Override
    public void start(Stage splashStage) throws Exception {
        this.stage = splashStage;
        stage.initStyle(StageStyle.TRANSPARENT); // is also undecorated
        BorderPane root = new BorderPane();
        root.setId("splash");
        Scene scene = new Scene(root, 300, 300);
        scene.getStylesheets().addAll(
                this.getClass().getResource("splash.css").toExternalForm());
        stage.setScene(scene);

        root.setCenter(lblStatus);
        root.setBottom(progress);
        stage.show();
    }

    /**
     * Simple progress-bar notifications; values from 0 to 1
     */
    @Override
    public void handleProgressNotification(ProgressNotification pn) {
        progress.setProgress(pn.getProgress());
    }

    /**
     * Provide more detail progress reports
     * @param info Details can be passed in the parameter; the parameter
     *            implements the PreloaderNotification interface. The
     *            application must create a class containing the information it
     *            needs
     */
    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        if (info instanceof InfoToSplashScreen) {
            InfoToSplashScreen myInfo = (InfoToSplashScreen) info;
            lblStatus.setText(myInfo.getDetails());
        }
    }

    /**
     * Respond to standard state changes of the main program. Typically, a
     * splash screen will hide itself when the main application starts
     * @param evt The event that is being reported; see possibilities in the
     *            StateChangeNotification documentation
     */
    @Override
    public void handleStateChangeNotification(StateChangeNotification evt) {
        if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
            stage.hide();
        }
    }

    /**
     * If your program may report an error during initialization
     * @param info Details can be passed in the ErrorNotification object
     * @return true if the error has been displayed; false if Java needs to
     *         display its own error dialog
     */
    @Override
    public boolean handleErrorNotification(Preloader.ErrorNotification info) {
        lblStatus.setText(info.getDetails());
        return true;
    }

    /**
     * This class implements the PreloaderNotification interface, which is used
     * to pass application-notifications to the splash screen
     */
    public static class InfoToSplashScreen implements PreloaderNotification {
        private String details;

        public InfoToSplashScreen(String details) {
            this.details = details;
        }

        public String getDetails() {
            return details;
        }
    }
}
