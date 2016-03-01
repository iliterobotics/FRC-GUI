package org.ilite.gui.vision;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.ilite.vision.camera.CameraConnectionFactory;
import org.ilite.vision.camera.ICameraConnection;
import org.ilite.vision.constants.ECameraType;

import com.fauge.robotics.towertracker.ITowerListener;
import com.fauge.robotics.towertracker.TowerMessage;
import com.fauge.robotics.towertracker.TowerTracker1885;

import dataclient.DataServerWebClient;
import dataclient.robotdata.vision.CameraFeedDatabase;

public class VisionFeed extends Application implements ITowerListener, Runnable{

	private static final int FPS = 10;
	private static final int DEFAULT_WIDTH = 800, DEFAULT_HEIGHT = 600;
	private static final String MONGO_URL = "localhost";
	private static final String WEB_SERVER_URL = "http://localhost:8083";
	
	private ArrayList<BufferedImage> frames;
	private CameraFeedDatabase db;
	
	private static final String DEFAULT_BUCKET = "video";
	
	private StackPane mainPane;
	private ImageView imageView;
	private String session;
	
//TODO re-implement constructor once this class is not main
//	public ReadVideo(String videoSession, boolean camera){
//		
//	}
	
	public void construct(String videoSession, boolean camera){
		imageView = new ImageView(new WritableImage(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		imageView.setFitWidth(DEFAULT_WIDTH);
		imageView.setFitHeight(DEFAULT_HEIGHT);
		mainPane = new StackPane(imageView);
		session = videoSession;
		frames = new ArrayList<BufferedImage>();
		DataServerWebClient client = new DataServerWebClient(WEB_SERVER_URL);
		System.out.println("Using address:" + MONGO_URL);
		db = new CameraFeedDatabase(client, MONGO_URL, DEFAULT_BUCKET, session);
		if(camera){
			pullCameraFeed();
		}
		else{
			pullDatabaseFeed();
		}
	}
	
	private void pullCameraFeed(){
		ICameraConnection cameraConnection = CameraConnectionFactory.getCameraConnection(ECameraType.LOCAL_CAMERA.getCameraIP());
		TowerTracker1885 aTracker = new TowerTracker1885(cameraConnection);
		aTracker.addTowerListener(this);
		aTracker.addTowerListener(db);
		cameraConnection.start();
	}
	
	private void pullDatabaseFeed(){
		Thread readFromDatabase = new Thread(this);
		readFromDatabase.start();
	}
	
	public void start(Stage primaryStage){
		try{
			construct("video01", true);
			primaryStage.setScene(new Scene(mainPane));
			primaryStage.show();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String []  args){
		launch();
	}

	@Override
	public void fire(TowerMessage message) {
		System.out.println("Feed got frame");
		displayImage(message.bImage);
	}
	
	public void displayImage(BufferedImage image){
		imageView.setImage(SwingFXUtils.toFXImage(image, (WritableImage)(imageView.getImage()) ));	
	}

	@Override
	public void run() {
		System.out.println("STARTING DATABASE READ");
		int n = 0;
		BufferedImage frame = db.pullFrame(n);
		System.out.println(frame);
		while(frame != null){
			frames.add(frame);
			displayImage(frame);
			n++;
			try {
				Thread.sleep(1000 / FPS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			frame = db.pullFrame(n);
		}
	}
	
}
