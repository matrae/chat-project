package jabberwocky.chatBot.appClasses;

import java.util.Locale;

import jabberwocky.chatBot.ServiceLocator;
import jabberwocky.chatBot.abstractClasses.View;
import jabberwocky.chatBot.commonClasses.Translator;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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
	Slider sliderMaxSequenceLength;
	Text txtChatHistory;
	ScrollPane txtScroll;
	Label lblStatus;
	ProgressBar progress;
	Label lblChat;
	TextField txtChat;

	ServiceLocator sl = ServiceLocator.getServiceLocator();

	public App_View(Stage stage, App_Model model) {
		super(stage, model);
		stage.setTitle(sl.getTranslator().getString("program.name"));

		sl.getLogger().info("Application view initialized");
	}

	@Override
	protected Scene create_GUI() {
		ServiceLocator sl = ServiceLocator.getServiceLocator();
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

		sliderMaxSequenceLength = new Slider(1, 7, 3);
		sliderMaxSequenceLength.setShowTickLabels(true);
		sliderMaxSequenceLength.setMajorTickUnit(1);
		sliderMaxSequenceLength.setMinorTickCount(0);
		sliderMaxSequenceLength.setBlockIncrement(1);
		sliderMaxSequenceLength.setSnapToTicks(true);
		
		Region spacer = new Region();
		
		txtChatHistory = new Text();
		txtChatHistory.setId("generatedText");
		txtChatHistory.setWrappingWidth(770);
		txtChatHistory.setTextAlignment(TextAlignment.LEFT);
		txtScroll = new ScrollPane(txtChatHistory);
		txtScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
		txtScroll.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		
		HBox topHBox = new HBox(10, sliderMaxSequenceLength, spacer);
		HBox.setHgrow(spacer, Priority.ALWAYS);
		VBox topVBox = new VBox(menuBar, topHBox);
		
		lblStatus = new Label();
		Region spacer2 = new Region();
		progress = new ProgressBar();
		progress.setVisible(false);
		HBox statusBox = new HBox(lblStatus, spacer2, progress);
		HBox.setHgrow(spacer2, Priority.ALWAYS);
		
		lblChat = new Label();
		txtChat = new TextField();
		HBox.setHgrow(txtChat, Priority.ALWAYS);
		HBox chatBox = new HBox(lblChat, txtChat);
		VBox bottomVBox = new VBox(chatBox, statusBox);

		BorderPane root = new BorderPane();
		root.setTop(topVBox);
		root.setCenter(txtScroll);
		root.setBottom(bottomVBox);
		
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
		lblChat.setText(t.getString("label.chat"));
		txtChat.setPromptText(t.getString("text.chat"));
		
		// Update status bar
		updateStatus();
	}
	
	void updateStatus() {
		Translator t = ServiceLocator.getServiceLocator().getTranslator();
		String status = "";
		long numEntries = 0;
		if (model.getFoundationData() != null) {
			numEntries = model.getFoundationData().getNumWords();
			status = t.getString("status.entries") + " " + numEntries;
		    status += "   /   "	+ t.getString("status.links") + " " + model.getFoundationData().getNumLinks();
		}
		lblStatus.setText(status);
		
		// Can only be used after training
		lblChat.setVisible(numEntries != 0);
		txtChat.setVisible(numEntries != 0);
		
		// Cannot be changed after training
		sliderMaxSequenceLength.setDisable(numEntries > 0);
	}
}