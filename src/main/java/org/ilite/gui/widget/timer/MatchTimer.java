package org.ilite.gui.widget.timer;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import org.ilite.gui.widget.timer.skins.MatchTimerSkin;

public class MatchTimer extends Control implements Runnable{
	
	public static final float TOTAL_TIME = 120.0f;
	
	private static final float AUTO_TIME = 15;
	private static final float TELEOP_TIME = 135; 
	
	private long lastTime;
	private long currTime;
	
	private FloatProperty timeProperty;
	private ObjectProperty<MatchState> matchStateProperty;
	
	private boolean running;
	private boolean paused;
	private boolean stopped;
	
	private Thread timerThread;
	
	public enum MatchState{
		PRE_MATCH, AUTO, TELEOP, LAST_30, LAST_20,  POST_MATCH;
	}
	
	public MatchTimer(){
		timeProperty = new SimpleFloatProperty(AUTO_TIME + TELEOP_TIME);
		matchStateProperty = new SimpleObjectProperty<MatchState>(MatchState.PRE_MATCH);
		
		running = false;
		paused = false;
	}
	
	public void start(){
		reset();
		running = true;
		lastTime = System.currentTimeMillis();
		
		timerThread = new Thread(this);
		timerThread.setName("Match Timer");
		timerThread.start();
	}
	
	public void resume(){
		lastTime = System.currentTimeMillis();
		paused = false;
	}
	
	public void pause(){
		paused = true;
	}
	
	public void reset(){
		stop();
		timeProperty.set(AUTO_TIME + TELEOP_TIME);
		matchStateProperty.set(MatchState.PRE_MATCH);
		
	}
	
	public FloatProperty getTimeProperty(){
		return timeProperty;
	}
	
	public ObjectProperty<MatchState> getMatchStateProperty(){
		return matchStateProperty;
	}
	
	public void stop(){
		running = false;
		stopped = false;
		while(!stopped);
		matchStateProperty.set(MatchState.POST_MATCH);
	}
	
	public Skin<MatchTimer> createDefaultSkin(){
		return new MatchTimerSkin(this);
	}
	
	public void run(){
		matchStateProperty.set(MatchState.AUTO);
		while(running){
			if(!paused){
				currTime = System.currentTimeMillis();
				float deltaTimeS = (currTime - lastTime)/1000.0f;
				timeProperty.subtract(deltaTimeS);
				lastTime = currTime;
				checkState();
			}
		}
		stopped = true;
		matchStateProperty.set(MatchState.POST_MATCH);
	}
	
	public float getTime(){
		return timeProperty.get();
	}
	
	public MatchState getMatchState(){
		return matchStateProperty.get();
	}
	
	public void checkState(){
		if(getTime() > TELEOP_TIME + AUTO_TIME && getMatchState() != MatchState.PRE_MATCH){
			matchStateProperty.set(MatchState.PRE_MATCH);
		}
		else if(getTime() > TELEOP_TIME && getMatchState() != MatchState.AUTO){
			matchStateProperty.set(MatchState.AUTO);
		}
		else if(getTime() > 30 && getMatchState() != MatchState.TELEOP){
			matchStateProperty.set(MatchState.TELEOP);
		}
		else if(getTime() > 20 && getTime() <= 30 && getMatchState() != MatchState.LAST_30){
			matchStateProperty.set(MatchState.LAST_30);
		}
		else if(getTime() > 0 && getMatchState() != MatchState.LAST_20){
			matchStateProperty.set(MatchState.LAST_20);
		}
		else{
			stop();
		}
	}
}
