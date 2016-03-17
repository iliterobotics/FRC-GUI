package org.ilite.gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.ilite.gui.drivers.ArmDataDriver;
import org.ilite.gui.drivers.ShooterDataDriver;
import org.ilite.gui.vision.VisionFeed;
import org.ilite.gui.widget.armDisplay.ArmDisplay;
import org.ilite.gui.widget.shooter.Shooter3D;

import dataclient.DataServerWebClient;
import dataclient.robotdata.arm.ArmStatus;
import dataclient.robotdata.shooter.ShooterStatus;

public class AppMain extends Application{

	public static final int ARM_WIDTH = 300;
	public static final int ARM_HEIGHT = 300;
	
	private VisionFeed feed;
	
	public static final int FEED_WIDTH = 600;
	public static final int FEED_HEIGHT = ARM_HEIGHT + ARM_WIDTH;
	
	private TextField sessionNameField;
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setY(100);
		primaryStage.setResizable(false);
		sessionNameField = new TextField();
		sessionNameField.getStyleClass().setAll("text-field");
		sessionNameField.setPromptText("Session Name");
		
		Scene scene = new Scene(new Group(sessionNameField));
		scene.getStylesheets().add("/login/sessionName.css");
		
		sessionNameField.setOnKeyPressed(new EventHandler<KeyEvent>(){


			@Override
			public void handle(KeyEvent arg0) {
				if(arg0.getCode() == KeyCode.ENTER){
					primaryStage.hide();
					DataServerWebClient client = new DataServerWebClient("http://localhost:5800");

					ArmDisplay armRepresentation = new ArmDisplay(ARM_WIDTH, ARM_HEIGHT);
					ArmDataDriver driver = new ArmDataDriver(client, new ArmStatus(client), armRepresentation);
					driver.launch();
					
					feed = new VisionFeed(FEED_WIDTH, FEED_HEIGHT, sessionNameField.getText(), true);
					
					Shooter3D shooterRepresentation = new Shooter3D(ARM_WIDTH, Color.DARKVIOLET, primaryStage, FEED_WIDTH + 3, ARM_HEIGHT + 25);
					ShooterDataDriver shooterDriver = new ShooterDataDriver(client, new ShooterStatus(client), shooterRepresentation);
					shooterDriver.launch();
					
					BorderPane mainPane = new BorderPane();
					
					mainPane.setRight(new VBox(armRepresentation, shooterRepresentation));
					mainPane.setCenter(feed.getFeed());
					primaryStage.setScene(new Scene(mainPane));
					primaryStage.setWidth(FEED_WIDTH + ARM_WIDTH);
					primaryStage.setHeight(FEED_HEIGHT);
					primaryStage.show();
				}
			}
			
		});
		
		primaryStage.setHeight(100);
		primaryStage.setWidth(800);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args){
		launch();
	}
	
	@Override
	public void stop(){
		feed.saveToMP4("C:/videos/" + feed.getSessionName() + ".mp4");
		System.exit(0);
	}
	

}
