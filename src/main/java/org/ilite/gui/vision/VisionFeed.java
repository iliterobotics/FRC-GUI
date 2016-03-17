package org.ilite.gui.vision;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
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

	private static final int FPS = 30;
	private static final String MONGO_URL = "localhost";
	private static final String WEB_SERVER_URL = "http://localhost:5800";
	
	private ArrayList<BufferedImage> frames;
	private CameraFeedDatabase db;
	
	private static final String DEFAULT_BUCKET = "video";
	
	private ICameraConnection cameraConnection;
	
	private StackPane mainPane;
	private ImageView imageView;
	private String session;
	
	/**@deprecated ONLY FOR TESTING*/
	public VisionFeed(){}
	
	public VisionFeed(double d, double height, String videoSession, boolean camera){
		construct(d, height, videoSession, camera);
	}
	
	public void construct(double width, double height, String videoSession, boolean camera){
		imageView = new ImageView(new WritableImage((int)width, (int)height));
		imageView.setFitWidth(width);
		imageView.setFitHeight(height);
		mainPane = new StackPane(imageView);
		session = videoSession;
		frames = new ArrayList<BufferedImage>();
		DataServerWebClient client = new DataServerWebClient(WEB_SERVER_URL);
		db = new CameraFeedDatabase(client, MONGO_URL, DEFAULT_BUCKET, session);
		if(camera){
			pullCameraFeed();
		}
		else{
			pullDatabaseFeed();
		}
	}
	
	private void pullCameraFeed(){
		cameraConnection = CameraConnectionFactory.getCameraConnection(ECameraType.FIELD_CAMERA.getCameraIP());
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
			construct(800, 600, "test1", true);
			primaryStage.setScene(new Scene(mainPane));
			primaryStage.setWidth(800);
			primaryStage.setHeight(600);
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
		displayImage(message.bImage);
	}
	
	public void displayImage(BufferedImage image){
		imageView.setImage(SwingFXUtils.toFXImage(image, (WritableImage)(new WritableImage(image.getWidth(), image.getHeight())) ));	
	}

	@Override
	public void run() {
		int n = 0;
		BufferedImage frame = db.pullFrame(n);
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
	
	public Node getFeed() {
		return mainPane;
	}
	
	public String getSessionName(){
		return session;
	}
	
	public void saveToMP4(String filepath){
		cameraConnection.destroy();
		VisionFeedToMP4.WriteToMP4(db, filepath);
	}
	
}
