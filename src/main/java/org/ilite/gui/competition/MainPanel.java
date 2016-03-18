package org.ilite.gui.competition;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;


import org.ilite.gui.autonomousconfig.ConfigureAutonomous;
import org.ilite.gui.competition.VisionConnectionChecker.VisionConnectionStatus;
import org.ilite.gui.drivers.ArmDataDriver;
import org.ilite.gui.drivers.ShooterDataDriver;
import org.ilite.gui.vision.VisionFeed;
import org.ilite.gui.widget.armDisplay.ArmDisplay;
import org.ilite.gui.widget.shooter.Shooter3D;


import dataclient.ConnectionTest;
import dataclient.DataServerWebClient;
import dataclient.robotdata.arm.ArmStatus;
import dataclient.robotdata.shooter.ShooterStatus;

public class MainPanel {
	
	private static final Integer[] PORTS_TO_CHECK = {80, 443, 5800, 5801, 5802, 5803, 5804, 5805, 5806, 5807, 5808, 5809, 5810};
	private static final int HEIGHT = 400;
	private static final int DRIVER_STATION_HEIGHT = 250;
	
	private GridPane root;
	
	private DataServerWebClient client;
	
	private VBox ports;
	private ConnectionTest portTester;
	
	private VisionFeed vision;
	private VisionConnectionChecker visionDriver;
	
	private ArmDisplay arm;
	private ArmStatus armStatus;
	private ArmDataDriver armDriver;
	
	private Shooter3D shooter;
	private ShooterStatus shooterStatus;
	private ShooterDataDriver shooterDriver;
	
	private ConfigureAutonomous autonomousConfig;
	
	private HBox rightTopWrapper;
	private VBox portWrapper, rightWrapper;
	private Text portLabel;
	
	private ToggleButton startCamera;
	
	private Scene scene;
	private final String session;
	
	public MainPanel(String session, Stage mainStage){
		root = new GridPane();
		root.getStyleClass().setAll("root");
		this.session = session;
		scene = new Scene(root);
		scene.getStylesheets().add("competition/mainPanel.css");
		scene.getStylesheets().add("org/ilite/autoconfiguration/dropdown.css");
		client = new DataServerWebClient("localhost");
		buildComponents();

		root.addRow(0, vision.getFeed(), rightWrapper);
		mainStage.setWidth(Screen.getPrimary().getBounds().getWidth());
		mainStage.setHeight(Screen.getPrimary().getBounds().getHeight() - DRIVER_STATION_HEIGHT);
		mainStage.setX(0);
		mainStage.setY(0);
	}
	
	private void buildComponents(){
		
		vision = new VisionFeed(600, HEIGHT, session, true);
		visionDriver = new VisionConnectionChecker(vision);
		Thread visionDriverThread = new Thread(visionDriver);
		visionDriverThread.start();
		
		startCamera = new ToggleButton("Camera Off");
		startCamera.getStyleClass().setAll("camera-button");
		startCamera.setId("not-found");
		visionDriver.getStatusProperty().addListener(observable -> {
			String id = "";
			switch(visionDriver.getStatus()){
				case ENABLED:
					id = "on";
					break;
				case DISABLED:
					id = "found";
					break;
				case NOT_FOUND:
					id = "not-found";
					break;
			}
			startCamera.setId(id);
		});
		startCamera.setOnAction(observable -> {
			if(startCamera.isSelected()){
				startCamera.setText("Camera On");
				vision.openCameraFeed();
			}
			else{
				startCamera.setText("Camera Off");
				vision.closeCameraFeed();
			}
		});
		
		arm = new ArmDisplay(300, 300);
		arm.getStyleClass().add("indent");
		armStatus = new ArmStatus(client);
		armDriver = new ArmDataDriver(client, armStatus, arm);
		
		autonomousConfig = new ConfigureAutonomous();
		autonomousConfig.getAutonomousConfig().getStyleClass().add("indent");
		
		portWrapper = new VBox();
		ports = new VBox();
		ports.getStyleClass().setAll("port-box");
		portTester = new ConnectionTest("localhost", PORTS_TO_CHECK);
		portLabel = new Text("Ports");
		portLabel.getStyleClass().setAll("port-label");
		portWrapper.getChildren().addAll(portLabel, ports);
		refreshPorts();
		
		rightTopWrapper = new HBox(startCamera, arm, portWrapper);
		rightWrapper = new VBox(rightTopWrapper, autonomousConfig.getAutonomousConfig());
		
		//TODO implement shooter status
		//shooter = new Shooter3D();
		//shooterStatus = new ShooterStatus(client);
		//shooterDriver = new ShooterDataDriver(client, shooterStatus, shooter);
	}
	
	public void refreshPorts(){
		portTester.refresh();
		ports.getChildren().clear();
		for(Integer port : PORTS_TO_CHECK){
			Text portText = new Text(port.toString());
			portText.getStyleClass().setAll("port");
			portText.setId(portTester.isPortOpen(port)?"open":"closed");
			ports.getChildren().add(portText);
		}
	}
	
	public void checkForVision(){
		Thread visionCheckThread = new Thread(visionDriver);
		visionCheckThread.start();		
	}
	
	public void disableVisionChecking(){
		visionDriver.stop();
	}
	
	public Scene getScene(){
		return scene;
	}

}
