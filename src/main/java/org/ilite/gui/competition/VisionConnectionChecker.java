package org.ilite.gui.competition;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import org.ilite.gui.vision.VisionFeed;

public class VisionConnectionChecker implements Runnable{
	
	private final VisionFeed FEED;
	private ObjectProperty<VisionConnectionStatus> statusProperty;
	private boolean running;
	private boolean streamingEnabled;
	
	public enum VisionConnectionStatus{
		ENABLED, DISABLED, NOT_FOUND;
	}
	
	public VisionConnectionChecker(VisionFeed feed){
		FEED = feed;
		statusProperty = new SimpleObjectProperty<VisionConnectionChecker.VisionConnectionStatus>(VisionConnectionStatus.NOT_FOUND);
	}
	
	public void enableStreaming(boolean enabled){
		streamingEnabled = enabled;
	}
	
	public void run(){
		running = true;
		while(running){
			if(FEED.testCamera()){
				if(streamingEnabled){
					statusProperty.set(VisionConnectionStatus.ENABLED);
				}
				else{
					statusProperty.set(VisionConnectionStatus.DISABLED);
				}
			}
			else{
				statusProperty.set(VisionConnectionStatus.NOT_FOUND);
			}
		}
		switch(statusProperty.get()){
			case ENABLED:
				FEED.openCameraFeed();
				break;
			case DISABLED:
			case NOT_FOUND:
				FEED.closeCameraFeed();
		}
	}
	
	public VisionConnectionStatus getStatus(){
		return statusProperty.get();
	}
	
	public ReadOnlyObjectProperty<VisionConnectionStatus> getStatusProperty(){
		return statusProperty;
	}
	
	public void stop(){
		running = false;
	}
	
}
