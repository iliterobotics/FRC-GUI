package org.ilite.gui.test;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Application;
import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.ilite.gui.autonomousconfig.ConfigureAutonomous;
import org.ilite.gui.widget.armDisplay.ArmDisplay;
import org.ilite.gui.widget.gauge.Gauge;
import org.ilite.gui.widget.gauge.GaugeMark;
import org.ilite.gui.widget.voltometer.Voltometer;

import dataclient.robotdata.autonomous.AutonomousConfig;

public class AppMain extends Application {
	
	Thread runner;
	TestRun testDataRunner;

	public void start(Stage stage) throws Exception {
//		VBox vbox = new VBox();
//		ScrollPane scroll = new ScrollPane();
//		scroll.setContent(vbox);
//		Scene scene = new Scene(scroll);
//		stage.setScene(scene);
//		
//		
//		stage.show();
//		
//		scene.setFill(Color.BLACK);
//		
//		ArrayList<Voltometer> volts = new ArrayList<Voltometer>();
//		ArrayList<Gauge> gauges = new ArrayList<Gauge>();
//		GaugeMark[] marxs = {new GaugeMark(6, Color.GREEN), new GaugeMark(8, Color.YELLOW), new GaugeMark(10, Color.RED)};
//		gauges.add(new Gauge(new SimpleFloatProperty(0), 0, 10, Arrays.asList(marxs)));
//		
//		for(int i = 0; i < 10; i++){
//			volts.add(new Voltometer(0));
//			ArrayList<GaugeMark> marx = new ArrayList<GaugeMark>();
//			int marks = (int)(Math.random() * 10);
//			for(int j = 0; j < marks; j++){
//				marx.add(new GaugeMark((int)(Math.random() * 8 + 1), Color.rgb((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255) )));
//			}
//			gauges.add(new Gauge(new SimpleFloatProperty(0), 0, 10, marx));
//		}
//		
//		vbox.getChildren().addAll(volts);
//		vbox.getChildren().addAll(gauges);
//		vbox.getChildren().add(new ArmDisplay());
//		
//		
//		testDataRunner = new TestRun(volts, gauges);
//		runner = new Thread(testDataRunner);
//		runner.start();
//		
		try{
		stage.setScene(new ConfigureAutonomous().getScene());
		
		stage.setWidth(800);
		stage.setHeight(600);
		
		stage.show();
		}
		catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	public void stop(){
		try {
			testDataRunner.stop();
			super.stop();
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}