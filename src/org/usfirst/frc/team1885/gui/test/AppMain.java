package org.usfirst.frc.team1885.gui.test;

import java.util.Arrays;

import javafx.application.Application;
import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.usfirst.frc.team1885.gui.widget.gauge.Gauge;
import org.usfirst.frc.team1885.gui.widget.gauge.GaugeMark;
import org.usfirst.frc.team1885.gui.widget.voltometer.Voltometer;

public class AppMain extends Application {
	
	Thread runner;
	TestRun testDataRunner;

	public void start(Stage stage) throws Exception {
		HBox hbox = new HBox();
		Scene scene = new Scene(hbox);
		stage.setScene(scene);

		//for(int i = 0; i < 10; i++){
		//		hbox.getChildren().add(new ToggleSwitch("LS" + i, "Limit Switch Number " + i));
		//}
		
		Voltometer vm = new Voltometer(0);
		SimpleFloatProperty pressure = new SimpleFloatProperty(1);
		GaugeMark[] marks = {new GaugeMark(4, Color.GREEN), new GaugeMark(9, Color.YELLOW), new GaugeMark(10, Color.RED)};
		Gauge gauge = new Gauge(pressure, 0, 10, Arrays.asList(marks));
		
		
		hbox.getChildren().addAll(vm, gauge); 
		
		stage.show();
		
		scene.setFill(Color.BLACK);
		
		testDataRunner = new TestRun(vm);
		runner = new Thread(testDataRunner);
		runner.start();
	}
	
	public void stop(){
		try {
			testDataRunner.stop();
			super.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}