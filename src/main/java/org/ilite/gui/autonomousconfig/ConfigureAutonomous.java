package org.ilite.gui.autonomousconfig;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import dataclient.DataServerWebClient;
import dataclient.robotdata.autonomous.AutonomousConfig;

public class ConfigureAutonomous extends Application{
	
	/**DEFAULT URL OF WEBSERVER*/
	private static final String URL = "http://localhost:8083";
	
	/**DEFAULT WIDTH AND HEIGHT*/
	private static final int DEF_WIDTH = 750, DEF_HEIGHT = 150;
	
	private GridPane mainPane;
	private Button pushConfig;
	private Scene scene;
	private ComboBox<Integer> positionBox;
	private ComboBox<String>  defenseBox;
	private ComboBox<String>  goalBox;
	private Slider delay;
	private Text delayText;
	
	private StringProperty pushButtonStatus;
	
	private DataServerWebClient client;
	private AutonomousConfig config;
	
	/**
	 * Basic constructor that builds a scene for pushing autonomous configurations
	 */
	public ConfigureAutonomous(){
		//sets up server client and autonomous configuration object
		client = new DataServerWebClient(URL);
		config = new AutonomousConfig(client);
		
		mainPane = new GridPane();
		
		//sets up drop-down to configure defense position
		positionBox = new ComboBox<Integer>();
		positionBox.getItems().addAll(new Integer[] {1, 2, 3, 4, 5});
		positionBox.setPromptText("Position");
		
		//sets up drop-down to set defenses
		defenseBox = new ComboBox<String>();
		defenseBox.getItems().addAll(AutonomousConfig.getDefenseNameMap().keySet());
		defenseBox.setPromptText("Defense");
		
		//check to make sure that if low-bar is selected only position can be one, and if anything else is selected position is not 1
		defenseBox.setOnAction(check -> { 
			if(defenseBox.getValue() != null && defenseBox.getValue().equals(AutonomousConfig.getDefenseName(AutonomousConfig.LOW_BAR))){
				if(positionBox.getValue() == null || !positionBox.getValue().equals(1)){
					positionBox.setValue(1);
				}
			}
			else{
				if(positionBox.getValue() != null && positionBox.getValue().equals(1)){
					positionBox.setValue(null);
				}
			}
		});
		//checks to see that if position 1 is selected only low-bar is chosen and if anything but 1 is selected, low-bar is not chosen
		positionBox.setOnAction(check -> {
			if(positionBox.getValue() != null && positionBox.getValue().equals(1) ){
				if(defenseBox.getValue() == null || !defenseBox.getValue().equals(AutonomousConfig.getDefenseName(AutonomousConfig.LOW_BAR))){
					defenseBox.setValue(AutonomousConfig.getDefenseName(AutonomousConfig.LOW_BAR));
				}
			}
			else{
				if(defenseBox.getValue() != null && defenseBox.getValue().equals(AutonomousConfig.getDefenseName(AutonomousConfig.LOW_BAR))){
					defenseBox.setValue(null);
				}
			}
		});
		
		//sets up drop-down for choosing which goal we are going to end up in front of
		goalBox = new ComboBox<String>();
		goalBox.getItems().addAll(AutonomousConfig.getGoalNameMap().keySet());
		goalBox.setPromptText("goal");
		
		//sets up slider for delay and text to display current delay value
		delayText = new Text("Delay:0.0s");
		delay = new Slider(0, 10000, 0);
		delay.setBlockIncrement(100);
		delay.setMajorTickUnit(1000);
		delay.valueProperty().addListener(observable -> {
			delayText.setText("Delay:" + (int)(delay.getValue()/100) / 10.0 + "s");
		});
		
		//sets up button which updates all of the values of the configuration and pushes it
		pushConfig = new Button("Push Configuration");
		pushConfig.getStyleClass().setAll("push-config");
		pushConfig.setOnAction(push -> {
			if(positionBox.getValue() != null){
				config.setPosition(positionBox.getValue());
			}
			else{
				wobble(positionBox);
				return;
			}
			
			if(defenseBox.getValue() != null){
				config.setDefense(AutonomousConfig.getDefenseNameMap().get((defenseBox.getValue())));
			}
			else{
				wobble(defenseBox);
				return;
			}
			
			if(goalBox.getValue() != null){
				config.setGoal(AutonomousConfig.getGoalNameMap().get((goalBox.getValue())));
			}
			else{
				wobble(goalBox);
				return;
			}
			
			config.setDelay((int)delay.getValue());
			try{
				config.push();
				pushButtonStatus.set("success");
			}catch(Exception e){
				pushButtonStatus.set("error");
			}finally{
				resetPushStatus();
			}
		});
		
		//sets up the main grid pane and inserts all of the Nodes into it
		mainPane.getStyleClass().setAll("grid-pane");
		mainPane.addRow(0, positionBox, defenseBox, goalBox, new VBox(delayText, delay));
		mainPane.add(pushConfig, 3, 1);
		
		//sets up a property bound to the css id of the push-button
		pushButtonStatus = new SimpleStringProperty(null);
		pushButtonStatus.addListener(observable -> {
			pushConfig.setId(pushButtonStatus.get());		
		});
		
		//sets up the scene with the default
		scene = new Scene(mainPane, DEF_WIDTH, DEF_HEIGHT);
		scene.getStylesheets().add("/org/ilite/autoconfiguration/dropdown.css");
	}
	
	public void wobble(Node n){
		RotateTransition rt = new RotateTransition(Duration.millis(100), n);
		rt.setToAngle(10);
		rt.setFromAngle(-10);
		rt.setCycleCount(3);
		rt.setOnFinished(finished -> n.setRotate(0));
		rt.play();
	}
	
	public void resetPushStatus(){
		Timeline turnback = new Timeline(new KeyFrame(Duration.millis(500), new KeyValue(pushButtonStatus, "def")));
		turnback.play();
	}
	
	public Scene getScene(){
		return scene;
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.initStyle(StageStyle.UNIFIED);
		stage.setTitle("Autonomous Configuration Utility");
		stage.setResizable(false);
		ConfigureAutonomous config = new ConfigureAutonomous();
		
		stage.setScene(config.getScene());

		stage.show();

	}
	
	public static void main(String[] args){
		try{
			launch();
		}
		catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	

}
