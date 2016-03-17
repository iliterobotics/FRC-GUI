package org.ilite.gui.competition;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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
	
	private static final Integer[] PORTS_TO_CHECK = {80, 8083, 5800, 5801, 5802, 5803, 5804, 5805, 5806, 5807, 5808, 5809, 5810};
	
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
	
	private Scene scene;
	private final String session;
	
	public MainPanel(String session){
		root = new GridPane();
		this.session = session;
		scene = new Scene(root);
		scene.getStylesheets().add("competition/mainPanel.css");
		client = new DataServerWebClient("localhost");
		buildComponents();
	}
	
	private void buildComponents(){
		
		vision = new VisionFeed(600, 400, session, true);
		visionDriver = new VisionConnectionChecker(vision);
		
		arm = new ArmDisplay(300, 300);
		armStatus = new ArmStatus(client);
		armDriver = new ArmDataDriver(client, armStatus, arm);
		
		ports = new VBox();
		portTester = new ConnectionTest("localhost", PORTS_TO_CHECK);
		
		
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
			portText.setId(portTester.isPortOpen(port)?"open":"closed");
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
