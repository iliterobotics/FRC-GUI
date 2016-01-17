package org.usfirst.frc.team1885.gui.widget.toggleSwitch.skins;

import javafx.animation.TranslateTransition;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import org.usfirst.frc.team1885.gui.widget.toggleSwitch.ToggleSwitch;

public class ToggleSwitchSkin extends SkinBase<ToggleSwitch>{
	
	private static final int REG_HEIGHT = 135, REG_WIDTH = 68; 

	private Region background;
	
	private Text symbol;
	
	private StackPane track;
	private Region onIcon;
	private Region thumb;
	
	public ToggleSwitchSkin(ToggleSwitch control){
		super(control);
		
		control.getStylesheets().add("/org/usfirst/frc/team1885/gui/css/ToggleSwitch.css");
		control.getStyleClass().setAll("toggle");
		
		initGraphics();
		setUpListeners();
	}
	
	private void initGraphics(){

		getSkinnable().setMinHeight(REG_HEIGHT);
		getSkinnable().setMinWidth(REG_WIDTH);
		getSkinnable().setMaxHeight(REG_HEIGHT);
		getSkinnable().setMaxWidth(REG_WIDTH);
		
		background = new Region();
		background.getStyleClass().setAll("background");
		getChildren().add(background);
		
		symbol = new Text(getSkinnable().getShortName());
		getChildren().add(symbol);
		
		track = new StackPane();
		track.getStyleClass().setAll("track");
		getChildren().add(track);
		
		onIcon = new Region();
		onIcon.getStyleClass().setAll("icon");
		onIcon.setId("on");
		track.getChildren().add(onIcon);
		
		thumb = new Region();
		thumb.getStyleClass().setAll("thumb");
		track.getChildren().add(thumb);
	}
	
	private void setUpListeners(){
		getSkinnable().getActivatedProperty().addListener(changeListener -> {
			TranslateTransition ta = new TranslateTransition(Duration.millis(100), thumb);
			if(getSkinnable().isActivated()){
				ta.setFromY(16);
				ta.setToY(-16);
			}
			else{ 
				ta.setFromY(-16);
				ta.setToY(16);
			}
			ta.play();
		});
	}
	//

}
