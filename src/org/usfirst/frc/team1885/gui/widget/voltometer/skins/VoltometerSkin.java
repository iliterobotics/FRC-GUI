package org.usfirst.frc.team1885.gui.widget.voltometer.skins;

import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import org.usfirst.frc.team1885.gui.widget.voltometer.Voltometer;

public class VoltometerSkin extends SkinBase<Voltometer>{

	private static final int REG_HEIGHT = 100, REG_WIDTH = 400; 
	private Text mid, min, max, value; 
	
	private VBox background; 
	private Region arm; 
	private TranslateTransition animation;
	
	private StackPane track, textBackground;
	
	
	public VoltometerSkin(Voltometer control) {
		super(control);
		
		control.getStyleClass().setAll("voltometer"); 
		control.getStylesheets().add("/org/usfirst/frc/team1885/gui/css/Voltometer.css");
		
		initGraphics(); 
		setUpListeners(); 
		updateValue(control.getVoltage());
	}
	public void initGraphics(){
		
	
		background = new VBox(); 
		getSkinnable().getStyleClass().setAll("background"); 
		getChildren().add(background);
		
		mid = new Text("0"); 
		min = new Text("-1");
		max = new Text("1");
		value = new Text("0"); 
		
		
		textBackground = new StackPane(min, mid, max); 
		
		textBackground.setMaxWidth(REG_WIDTH);
		
		background.getChildren().add(textBackground); 
		textBackground.getStyleClass().setAll("textBackground");
		StackPane.setAlignment(min, Pos.BOTTOM_LEFT);
		StackPane.setAlignment(mid, Pos.BOTTOM_CENTER);
		StackPane.setAlignment(max, Pos.BOTTOM_RIGHT);
		
		
		
		
		track = new StackPane(); 
		track.getStyleClass().setAll("track"); 
		background.getChildren().add(track); 
		track.setMinSize(REG_WIDTH, REG_HEIGHT);
		track.setMaxSize(REG_WIDTH, REG_HEIGHT);
		track.getChildren().add(value); 
		
		arm = new Region(); 
		arm.getStyleClass().setAll("arm"); 
		track.getChildren().add(arm); 
		
		
	}
	public void setUpListeners()
	{ 
		getSkinnable().getVoltageProperty().addListener(changeListener -> updateValue(getSkinnable().getVoltage()));	
	}
	
	public void updateValue(float voltage){
		if(animation != null){
			animation.stop();
			animation = null;
		}
		animation = new TranslateTransition (Duration.millis(1000), arm);
		value.setText(String.format("%+1.2f", voltage) + "v"); 
		animation.setToX(voltage*(REG_WIDTH>>1));			
		animation.play();
		animation.setOnFinished(finished -> animation = null);
	}
		
	
}
