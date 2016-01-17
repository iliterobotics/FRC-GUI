package org.usfirst.frc.team1885.gui.widget.gauge.skins;

import java.util.Set;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import org.usfirst.frc.team1885.gui.widget.gauge.Gauge;
import org.usfirst.frc.team1885.gui.widget.gauge.GaugeMark;

public class GaugeSkin extends SkinBase<Gauge>{

	private StackPane background;
	private Region needle;
	private Arc border;
	private Rotate needleRotate;
	private Timeline timeline;
	private KeyValue kv;
	private Circle knob;

	private static final double radius = 150;
	private static final double baselineAngle = 50;
	
	public GaugeSkin(Gauge control) {
		super(control);
		
		control.getStylesheets().add("/org/usfirst/frc/team1885/gui/css/Gauge.css");
		
		initGraphics();
		setUpListeners();
	}
	
	public void initGraphics(){
		background = new StackPane();
		getChildren().add(background);
		background.getStyleClass().setAll("gauge-background");
		background.setMaxWidth(radius);
		
		buildRegions();
		
		needle = new Region();
		needle.getStyleClass().setAll("needle");
		needle.setMinSize(radius / 10, radius);
		needle.setMaxSize(radius / 10, radius);
		StackPane.setAlignment(needle, Pos.BOTTOM_CENTER);
		double angle = Math.min(90, baselineAngle);
		double radians = Math.toRadians(angle);
		needle.setTranslateY((Math.cos(radians) * -radius) - 4);
		needleRotate = new Rotate(0, radius/20, radius);
		needle.getTransforms().setAll(needleRotate);
		background.getChildren().add(needle);
		
		knob = new Circle(radius / 10);
		knob.getStyleClass().setAll("knob");
		StackPane.setAlignment(knob, Pos.BOTTOM_CENTER);
		knob.setTranslateY(needle.getTranslateY() + (radius/10));
		knob.setTranslateX(-1);
		background.getChildren().addAll(knob);
		
		border = new Arc();
		border.getStyleClass().setAll("border");
		StackPane.setAlignment(border, Pos.BOTTOM_LEFT);
		border.setType(ArcType.ROUND);
		border.setStartAngle(270 - baselineAngle);
		border.setLength(-360 + ((int)baselineAngle << 1));
		border.setRadiusX(radius);
		border.setRadiusY(radius);
		background.getChildren().add(border);
	}
	
	public void buildRegions(){
		Set<GaugeMark> marks = getSkinnable().getKeyMarks();
		for(GaugeMark mark : marks){
			Arc arc = new Arc();
			arc.setType(ArcType.ROUND);
			arc.setStartAngle(270 - baselineAngle);
			arc.setLength((mark.getValue().doubleValue() / getSkinnable().getMax().doubleValue()) * (-360 + (baselineAngle * 2)));
			arc.setFill(mark.getColor() != null?mark.getColor():Color.web("#4A4C4F"));
			
			StackPane.setAlignment(arc, Pos.BOTTOM_LEFT);
			
			Text marker = new Text(mark.getValue().toString());
			StackPane.setAlignment(marker, Pos.BOTTOM_CENTER);
			
			arc.setRadiusX(radius);
			arc.setRadiusY(radius);
			
			double totalAngle = Math.min(90.0, baselineAngle - arc.getLength());
			double radians = Math.toRadians(totalAngle);
			double cosine = Math.sin(radians);
			
			arc.setTranslateX(radius - radius*cosine);
			
			
			double finalMarkerAngle = 270 - baselineAngle + arc.getLength();
			marker.setTranslateX( (radius - 20) * Math.cos(Math.toRadians(finalMarkerAngle)));
			double initialTY = -radius * Math.cos(Math.toRadians(Math.min(baselineAngle, 90))) - (radius - 10) * Math.sin(Math.toRadians(finalMarkerAngle));
			marker.setTranslateY(initialTY + (initialTY > -radius * Math.cos(Math.toRadians(baselineAngle))?-10:10));
			
			marker.getStyleClass().setAll("marker");
			
			background.getChildren().addAll(arc, marker);
		}	
	}
	
	public void setNeedle(double value){
		if(timeline != null){
			timeline.stop();
			timeline = null;
		}
		
		timeline = new Timeline();
		kv = new KeyValue(needleRotate.angleProperty(), 
				(value / getSkinnable().getMax().doubleValue() - 0.5) * (360 - baselineAngle * 2));
		timeline.getKeyFrames().addAll(new KeyFrame(Duration.millis(333), kv));
		timeline.play();
		timeline.setOnFinished(o -> timeline = null);
	
//		RotateTransition rt = new RotateTransition(Duration.millis(333), needle);
//		rt.setToAngle( (value / getSkinnable().getMax().doubleValue()) * ((360 - baselineAngle * 2) - 180));
//		rt.play();
	}
	
	public void setUpListeners(){
		getSkinnable().getDataProperty().addListener(observable -> {
			setNeedle(getSkinnable().getValue().doubleValue());
		});
	}
	

}
