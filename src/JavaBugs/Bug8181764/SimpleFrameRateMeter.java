package JavaBugs.Bug8181764;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Program to display the JavaFX framerate. This program comes from
 * https://stackoverflow.com/questions/28287398/what-is-the-preferred-way-of-
 * getting-the-frame-rate-of-a-javafx-application
 * 
 * Normally, the framerate should be around 60fps. On some Ubuntu machines,
 * however, the framerate appears to be unlimited; this program displays more
 * than 1000fps, which destroys certain animation effects. This occurs under
 * Java 1.8.0_131 and Ubuntu 16.04, but unfortunately, not on all machines.
 * 
 * On the affected machines, this behavior can be corrected by setting the
 * option "quantum.multithreaded=false" on the command line:
 * 
 * export _JAVA_OPTIONS="-Dquantum.multithreaded=false"
 * 
 * before executing the program. Oddly, setting this option in the java command
 * itself
 * 
 * java -Dquantum.multithreaded=false SimpleFrameRateMeter
 * 
 * does *not* work.
 * 
 * 
 * This bug originally discussed on StackOverflow
 * https://stackoverflow.com/questions/44327853/crazy-javafx-frame-rate-any-ideas
 */
public class SimpleFrameRateMeter extends Application {
	private final long[] frameTimes = new long[100];
	private int frameTimeIndex = 0;
	private boolean arrayFilled = false;

	@Override
	public void start(Stage primaryStage) {

		Label label = new Label();
		AnimationTimer frameRateMeter = new AnimationTimer() {

			@Override
			public void handle(long now) {
				long oldFrameTime = frameTimes[frameTimeIndex];
				frameTimes[frameTimeIndex] = now;
				frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
				if (frameTimeIndex == 0) {
					arrayFilled = true;
				}
				if (arrayFilled) {
					long elapsedNanos = now - oldFrameTime;
					long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
					double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;
					label.setText(String.format("Current frame rate: %.3f", frameRate));
				}
			}
		};

		frameRateMeter.start();

		primaryStage.setScene(new Scene(new StackPane(label), 250, 150));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
