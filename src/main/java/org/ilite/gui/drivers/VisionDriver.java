//package org.ilite.gui.drivers;
//
//import java.awt.image.BufferedImage;
//
//import javafx.embed.swing.SwingFXUtils;
//import javafx.scene.image.ImageView;
//import javafx.scene.image.WritableImage;
//
//import com.mongodb.MongoClientURI;
//
//import dataclient.robotdata.vision.CameraFeedDatabase;
//
//public class VisionDriver implements Runnable{
//	
//	private final int WAIT;
//	private static final MongoClientURI MONGO_URI = new MongoClientURI("mongodb://localhost/ilite");
//	private ImageView imageView;
//	private CameraFeedDatabase db;
//	private boolean listening;
//	
//	private BufferedImage lastFrame;
//	
//	public VisionDriver(ImageView imageView, String dbName, String session, double frameRateCap) {
//		this.imageView = imageView;
//		WAIT = (int)(1000.0/frameRateCap);
//		db = new CameraFeedDatabase(MONGO_URI.toString(), dbName, session);
//		
//	}
//	
//	public void open(){
//		listening = true;
//		Thread listeningThread = new Thread(this);
//		listeningThread.start();
//	}
//	
//	public void run(){
//		while(listening){
//			BufferedImage frame = db.pullMostRecentFrame();
//			if(!frame.equals(lastFrame)){
//				updateImageView(frame);
//			}
//			try {
//				Thread.sleep(WAIT);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//				close();
//			}
//		}
//	}
//	
//	public void updateImageView(BufferedImage image){
//		imageView.setImage(SwingFXUtils.toFXImage(image, new WritableImage(image.getWidth(), image.getHeight())));
//	}
//	
//	public ImageView getFrame(){
//		return imageView;
//	}
//	
//	public void close(){
//		listening = false;
//	}
//}
