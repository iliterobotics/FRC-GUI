package org.usfirst.frc.team1885.gui.test;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.usfirst.frc.team1885.gui.widget.gauge.Gauge;
import org.usfirst.frc.team1885.gui.widget.gauge.GaugeMark;
import org.usfirst.frc.team1885.gui.widget.voltometer.Voltometer;

public class AppMain extends Application {
	
	Thread runner;
	TestRun testDataRunner;

	public void start(Stage stage) throws Exception {
		VBox hbox = new VBox();
		ScrollPane scroll = new ScrollPane();
		scroll.setContent(hbox);
		Scene scene = new Scene(scroll);
		stage.setScene(scene);
		
		
		stage.show();
		
		scene.setFill(Color.BLACK);
		
		ArrayList<Voltometer> volts = new ArrayList<Voltometer>();
		ArrayList<Gauge> gauges = new ArrayList<Gauge>();
		for(int i = 0; i < 10; i++){
			volts.add(new Voltometer(0));
			ArrayList<GaugeMark> marx = new ArrayList<GaugeMark>();
			int marks = (int)(Math.random() * 10);
			for(int j = 0; j < marks; j++){
				marx.add(new GaugeMark((int)(Math.random() * 8 + 1), Color.rgb((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255) )));
			}
			gauges.add(new Gauge(new SimpleFloatProperty(0), 0, 10, marx));
		}
		
		hbox.getChildren().addAll(volts);
		hbox.getChildren().addAll(gauges);
		
		
		testDataRunner = new TestRun(volts, gauges);
		runner = new Thread(testDataRunner);
		runner.start();
		
		stage.setWidth(800);
		stage.setHeight(600);
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