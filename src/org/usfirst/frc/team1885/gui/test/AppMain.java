package org.usfirst.frc.team1885.gui.test;

import org.usfirst.frc.team1885.gui.widget.voltometer.Voltometer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AppMain extends Application {

	public void start(Stage stage) throws Exception {
		HBox hbox = new HBox();
		Scene scene = new Scene(hbox);
		stage.setScene(scene);

		//for(int i = 0; i < 10; i++){
	//		hbox.getChildren().add(new ToggleSwitch("LS" + i, "Limit Switch Number " + i));
		//}
		
		Voltometer vm = new Voltometer(0);
		
		hbox.getChildren().add(vm); 
		
		stage.show();
		
		scene.setFill(Color.BLACK);
		
		Thread runner = new Thread(new TestRun(vm));
		runner.start();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}