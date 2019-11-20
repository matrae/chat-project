package JavaBugs.Bug8222624;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * http://bugs.java.com/bugdatabase/view_bug.do?bug_id=JDK-8222624
 * 
 * Original bug report from Andreas Ambühl, OOP2
 */
public class CssProblem extends Application {
    private int value; // our business data

    Label label1 = new Label("A simple label - normal");
    Label label2 = new Label("A simple label - italic");
    Label label3 = new Label("A simple label - bold");

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        GridPane root = new GridPane();
        label2.setId("italic");
        label3.setId("bold");
        root.add((label1), 0, 0);
        root.add((label2), 0, 1);
        root.add((label3), 0, 2);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("CssProblem.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Bold italic problem");
        stage.show();
    }
}