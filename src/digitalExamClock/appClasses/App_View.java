package digitalExamClock.appClasses;

import java.util.Locale;
import java.util.logging.Logger;

import digitalExamClock.ServiceLocator;
import digitalExamClock.abstractClasses.View;
import digitalExamClock.commonClasses.Translator;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * @author Brad Richards
 */
public class App_View extends View<App_Model> {
    Menu menuFile;
    Menu menuFileLanguage;
    Menu menuHelp;
    
    Label lblStartTime;
    TextField txtStartTime;
    Label lblEndTime;
    TextField txtEndTime;
    Label lblMainTime;
    
    Scene scene;
    
	public App_View(Stage stage, App_Model model) {
        super(stage, model);
        ServiceLocator sl = ServiceLocator.getServiceLocator();
        sl.getLogger().info("Application view initialized");    
    }

	@Override
	protected Scene create_GUI() {
	    ServiceLocator sl = ServiceLocator.getServiceLocator();  
	    Logger logger = sl.getLogger();
	    Translator t = sl.getTranslator();
	    
	    MenuBar menuBar = new MenuBar();
	    menuFile = new Menu(t.getString("program.menu.file"));
	    menuFileLanguage = new Menu(t.getString("program.menu.file.language"));
	    menuFile.getItems().add(menuFileLanguage);
	    
       for (Locale locale : sl.getLocales()) {
           MenuItem language = new MenuItem(locale.getLanguage());
           menuFileLanguage.getItems().add(language);
           language.setOnAction( event -> {
				sl.getConfiguration().setLocalOption("Language", locale.getLanguage());
                sl.setTranslator(new Translator(locale.getLanguage()));
                updateTexts();
            });
        }
	    
        menuHelp = new Menu(t.getString("program.menu.help"));
	    menuBar.getMenus().addAll(menuFile, menuHelp);
		
		BorderPane root = new BorderPane();
		root.setTop(menuBar);

		VBox centerBox = new VBox(10);
		centerBox.setId("root");
		root.setCenter(centerBox);
		
		lblStartTime = new Label();
		txtStartTime = new TextField("00:00");
		Region spacer = new Region();
		lblEndTime = new Label();
		txtEndTime = new TextField("00:00");
		
		HBox topBox = new HBox(10, lblStartTime, txtStartTime, spacer, lblEndTime, txtEndTime);
		HBox.setHgrow(spacer, Priority.ALWAYS);
		topBox.getStyleClass().add("centered");
		
		Region r1 = new Region();
		Region r2 = new Region();
		lblMainTime = new Label();
		lblMainTime.setId("clock");
		
		centerBox.getChildren().addAll(topBox, r1, lblMainTime, r2);
		VBox.setVgrow(r1, Priority.ALWAYS);
		VBox.setVgrow(r2, Priority.ALWAYS);
		
		updateTexts();
		
        scene = new Scene(root, 400, 300);
        scene.getStylesheets().add(
                getClass().getResource("app.css").toExternalForm());
        return scene;
	}
	
	   protected void updateTexts() {
	       Translator t = ServiceLocator.getServiceLocator().getTranslator();
	        
	        // The menu entries
	       menuFile.setText(t.getString("program.menu.file"));
	       menuFileLanguage.setText(t.getString("program.menu.file.language"));
           menuHelp.setText(t.getString("program.menu.help"));
	        
	        // Other controls
           lblStartTime.setText(t.getString("start.time"));
           lblEndTime.setText(t.getString("end.time"));
           
           stage.setTitle(t.getString("program.name"));
	    }
}