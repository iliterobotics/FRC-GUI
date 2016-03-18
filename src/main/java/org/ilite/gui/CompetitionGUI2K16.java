package org.ilite.gui;

import javafx.application.Application;
import javafx.stage.Stage;

import org.ilite.gui.competition.MainPanel;
import org.ilite.gui.competition.SetupScreen;

public class CompetitionGUI2K16 extends Application{
	
	private static final double DEFAULT_WIDTH = 600;
	private static final double DEFAULT_HEIGHT = 400;

	
	public CompetitionGUI2K16() {
	}
	
	@Override
	public void start(Stage mainStage) throws Exception {
		SetupScreen setup = new SetupScreen();
		setup.onSessionSelected(observable -> {
			MainPanel mainPanel = new MainPanel(setup.getSessionName(), mainStage);
			mainStage.setScene(mainPanel.getScene());
		});
		mainStage.setScene(setup.getScene());
		mainStage.setWidth(DEFAULT_WIDTH);
		mainStage.setHeight(DEFAULT_HEIGHT);
		mainStage.show();
	}
	
	public void stop(){
		System.out.println("ending");
		System.exit(0);
	}
	
	public static void main(String[] args){
		launch();
	}
}
