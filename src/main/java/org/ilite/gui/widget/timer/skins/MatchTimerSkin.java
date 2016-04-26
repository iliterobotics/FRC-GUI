package org.ilite.gui.widget.timer.skins;

import javafx.scene.control.SkinBase;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import org.ilite.gui.widget.timer.MatchTimer;
import org.ilite.gui.widget.timer.MatchTimer.MatchState;

public class MatchTimerSkin extends SkinBase<MatchTimer>{
	
	private static final int SIZE = 200;
	
	private Circle background;
	private Arc timer;
	private Circle timerBack;
	private StackPane timeDisplay;
	private Text currentTime;

	public MatchTimerSkin(MatchTimer control) {
		super(control);
		setUpListeners();
		initGraphics();
	}
	
	private void initGraphics(){
		background = new Circle(SIZE/2);
		background.getStyleClass().setAll("timer-background");
		
		timer = new Arc();
		timer.setRadiusX(SIZE/2);
		timer.setRadiusY(SIZE/2);
	}
	
	public void updateTime(float time){
		
	}
	
	public void updateState(MatchState state){
		Color timerColor = Color.GREEN; 
		switch(state){
		}
	}
	
	private void setUpListeners(){
		getSkinnable().getMatchStateProperty().addListener(observable -> updateState(getSkinnable().getMatchState()));
		getSkinnable().getTimeProperty().addListener(observable -> updateTime(getSkinnable().getTime()));
	}
	

}
