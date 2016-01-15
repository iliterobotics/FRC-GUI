package org.usfirst.frc.team1885.gui.test;

import org.usfirst.frc.team1885.gui.widget.voltometer.Voltometer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class AppMain extends Application {

	public void start(Stage stage) throws Exception {
		HBox hbox = new HBox();
		Scene scene = new Scene(hbox);
		stage.setScene(scene);

//		for(int i = 0; i < 10; i++){
//			hbox.getChildren().add(new ToggleSwitch("LS" + i, "Limit Switch Number " + i));
//		}
		
		hbox.getChildren().add(new Voltometer(0)); 
		
		stage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}