package jabberwocky.letterBased.appClasses;

import java.util.Locale;
import java.util.logging.Logger;

import jabberwocky.letterBased.ServiceLocator;
import jabberwocky.letterBased.abstractClasses.View;
import jabberwocky.letterBased.commonClasses.Translator;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
	MenuItem menuFileTrain;
	MenuItem menuFileClear;
	Menu menuHelp;
	Slider sliderNumLetters;
	Button btnGenerate;
	TextArea txtGeneratedText;
	ScrollPane txtScroll;
	Label lblStatus;
	ProgressBar progress;
	RadioButton rdoChar;
	RadioButton rdoWord; // Words combined with following punctuation
	RadioButton rdoWord2; // Words separated from following punctuation

	ServiceLocator sl = ServiceLocator.getServiceLocator();

	public App_View(Stage stage, App_Model model) {
		super(stage, model);
		stage.setTitle(sl.getTranslator().getString("program.name"));

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
			language.setOnAction(event -> {
				sl.getConfiguration().setLocalOption("Language", locale.getLanguage());
				sl.setTranslator(new Translator(locale.getLanguage()));
				updateTexts();
			});
		}

		menuFileTrain = new MenuItem(t.getString("program.menu.file.train"));
		menuFile.getItems().add(menuFileTrain);
		menuFileClear = new MenuItem(t.getString("program.menu.file.clear"));
		menuFile.getItems().add(menuFileClear);

		menuHelp = new Menu(t.getString("program.menu.help"));
		menuBar.getMenus().addAll(menuFile, menuHelp);

		ToggleGroup mode = new ToggleGroup();
		rdoChar = new RadioButton(t.getString("radio.char"));
		rdoChar.setToggleGroup(mode);
		rdoChar.setSelected(true);
		rdoWord = new RadioButton(t.getString("radio.word"));
		rdoWord.setToggleGroup(mode);
		rdoWord2 = new RadioButton(t.getString("radio.word2"));
		rdoWord2.setToggleGroup(mode);
		
		sliderNumLetters = new Slider(1, 7, 3);
		sliderNumLetters.setShowTickLabels(true);
		sliderNumLetters.setMajorTickUnit(1);
		sliderNumLetters.setMinorTickCount(0);
		sliderNumLetters.setBlockIncrement(1);
		sliderNumLetters.setSnapToTicks(true);
		
		Region spacer = new Region();
		
		txtGeneratedText = new TextArea();
		txtGeneratedText.setWrapText(true);
		txtGeneratedText.setId("generatedText");
		
		btnGenerate = new Button();
		
		HBox topHBox = new HBox(10, rdoChar, rdoWord, rdoWord2, sliderNumLetters, spacer, btnGenerate);
		HBox.setHgrow(spacer, Priority.ALWAYS);
		VBox topVBox = new VBox(menuBar, topHBox);
		
		lblStatus = new Label();
		Region spacer2 = new Region();
		progress = new ProgressBar();
		progress.setVisible(false);
		HBox bottomBox = new HBox(lblStatus, spacer2, progress);
		HBox.setHgrow(spacer2, Priority.ALWAYS);

		BorderPane root = new BorderPane();
		root.setTop(topVBox);
		root.setCenter(txtGeneratedText);
		root.setBottom(bottomBox);
		
		updateTexts();
		
		Scene scene = new Scene(root, 800, 800);
		scene.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
		return scene;
	}

	protected void updateTexts() {
		Translator t = ServiceLocator.getServiceLocator().getTranslator();

		// The menu entries
		menuFile.setText(t.getString("program.menu.file"));
		menuFileLanguage.setText(t.getString("program.menu.file.language"));
		menuHelp.setText(t.getString("program.menu.help"));

		// Other controls
		rdoChar.setText(t.getString("radio.char"));
		rdoWord.setText(t.getString("radio.word"));
		rdoWord2.setText(t.getString("radio.word2"));
		btnGenerate.setText(t.getString("button.generate"));
		
		// Update status bar
		updateStatus();
	}
	
	void updateStatus() {
		Translator t = ServiceLocator.getServiceLocator().getTranslator();
		int numEntries = model.getNumEntries();
		String status = t.getString("status.entries") + " " + numEntries;
		if (numEntries > 0) status += "   /   "
				+ t.getString("status.links") + " " + model.getNumLinks();
		lblStatus.setText(status);
		// Can only be used after training
		btnGenerate.setDisable(numEntries == 0); 
		
		// Cannot be changed after training
		sliderNumLetters.setDisable(numEntries > 0); 
		rdoChar.setDisable(numEntries > 0);
		rdoWord.setDisable(numEntries > 0);
		rdoWord2.setDisable(numEntries > 0);
	}
}