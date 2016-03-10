package org.ilite.gui.test;

import java.net.URL;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import org.ilite.gui.drivers.ArmDataDriver;
import org.ilite.gui.widget.armDisplay.ArmDisplay;
import org.ilite.gui.widget.shooter.Shooter3D;

import test.Fbot;
import dataclient.DataServerWebClient;
import dataclient.robotdata.arm.ArmStatus;

public class MockGUI extends Application{
	
	public static final String URL = "http://localhost:8083";
	private Fbot fakebot;
	
	public void start(Stage stage) throws Exception {
		
		Thread fakeRobot = new Thread(fakebot = new Fbot());
		fakeRobot.start();
		
		GridPane mainPane = new GridPane();
		
		DataServerWebClient client = new DataServerWebClient(new URL(URL));
		client.pushSchema(ArmStatus.ARM_STATUS_SCHEMA);
		ArmStatus arm = new ArmStatus(client);
		
		ArmDisplay display = new ArmDisplay(600, 500);
		mainPane.add(display, 0, 0);
		
//		mainPane.add(new Shooter3D(), 1, 0);
		
		ArmDataDriver driver = new ArmDataDriver(client, arm, display);
		driver.launch();
		
		Scene mainScene = new Scene(mainPane);
		stage.setScene(mainScene);
		stage.show();
		
	}
	
	public void stop(){
//		fakebot.close();
	}
	
	public static void main(String[] args){
		launch();
	}
	
}
