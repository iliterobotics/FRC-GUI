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

	private StackPane background, arcHolder;
	private Region needle;
	private Arc borderArc, backgroundArc;
	private Rotate needleRotate;
	private Timeline timeline;
	private KeyValue kv;
	private Circle knob;

	private static final double radius = 150;
	private static final double baselineAngle = 30;
	
	public GaugeSkin(Gauge control) {
		super(control);
		
		control.getStylesheets().add("/org/usfirst/frc/team1885/gui/css/Gauge.css");
		
		initGraphics();
		setUpListeners();
	}
	
	public void initGraphics(){
		buildBackground();
		
		arcHolder = new StackPane();
		background.getChildren().add(arcHolder);
		
		buildRegions();
		buildNeedle();
		buildBorder();
		
		setUpListeners();
	}
	
	public void buildBackground(){
		background = new StackPane();
		getChildren().add(background);
		background.getStyleClass().setAll("gauge-background");
		background.setMaxWidth(radius);
		
		backgroundArc = new Arc();
		backgroundArc.getStyleClass().setAll("border");
		backgroundArc.setId("background");
		StackPane.setAlignment(backgroundArc, Pos.BOTTOM_LEFT);
		backgroundArc.setType(ArcType.ROUND);
		backgroundArc.setStartAngle(270 - baselineAngle);
		backgroundArc.setLength(-360 + ((int)baselineAngle << 1));
		backgroundArc.setRadiusX(radius);
		backgroundArc.setRadiusY(radius);
		
		background.getChildren().addAll(backgroundArc);
	}
	
	public void buildBorder(){
		borderArc = new Arc();
		borderArc.getStyleClass().setAll("border");
		StackPane.setAlignment(borderArc, Pos.BOTTOM_LEFT);
		borderArc.setType(ArcType.ROUND);
		borderArc.setStartAngle(270 - baselineAngle);
		borderArc.setLength(-360 + ((int)baselineAngle << 1));
		borderArc.setRadiusX(radius);
		borderArc.setRadiusY(radius);
		background.getChildren().add(borderArc);
	}
	
	public void buildNeedle(){
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
	}
	
	public void buildRegions(){
		Set<GaugeMark> marks = getSkinnable().getKeyMarks();
		
		arcHolder.getChildren().clear();
		
		for(GaugeMark mark : marks){
			//setting up the arc
			Arc arc = new Arc();
			arc.setType(ArcType.ROUND);
			arc.setStartAngle(270 - baselineAngle);
			arc.setLength((mark.getValue().doubleValue() / getSkinnable().getMax().doubleValue()) * (-360 + (baselineAngle * 2)));
			arc.setFill(mark.getColor() != null?mark.getColor():Color.web("#4A4C4F"));
			
			StackPane.setAlignment(arc, Pos.BOTTOM_LEFT);
			
			arc.setRadiusX(radius);
			arc.setRadiusY(radius);
			
			double totalAngle = Math.min(90.0, baselineAngle - arc.getLength());
			double radians = Math.toRadians(totalAngle);
			double cosine = Math.sin(radians);
			
			arc.setTranslateX(radius - radius*cosine);
			
			//setting up the numerical text marker
			Text marker = new Text(mark.getValue().toString() + getSkinnable().getUnit());
			StackPane.setAlignment(marker, Pos.BOTTOM_CENTER);
			
			double finalMarkerAngle = 270 - baselineAngle + arc.getLength();
			marker.setTranslateX( (radius - 20) * Math.cos(Math.toRadians(finalMarkerAngle)));
			double initialTY = -radius * Math.cos(Math.toRadians(Math.min(baselineAngle, 90))) - (radius - 20) * Math.sin(Math.toRadians(finalMarkerAngle));
			marker.setTranslateY(initialTY + (mark.getValue().equals(getSkinnable().getMin()) || mark.getValue().equals(getSkinnable().getMax())?-10:10));
			
			marker.getStyleClass().setAll("marker");
			arc.getStyleClass().setAll("gauge-region");
			
			arcHolder.getChildren().addAll(arc, marker);
		}	
		
		Text fMarker = new Text(getSkinnable().getMin() + getSkinnable().getUnit());
		Text lMarker = new Text(getSkinnable().getMax() + getSkinnable().getUnit());
		
		double angle = 270 - baselineAngle;
		double translateY = -radius * Math.cos(Math.toRadians(Math.min(baselineAngle, 90))) - (radius + 20) * Math.sin(Math.toRadians(angle));
		double translateX = (radius + 20) * Math.cos(Math.toRadians(angle));
		
		fMarker.setTranslateX(translateX);
		fMarker.setTranslateY(translateY);
		
		lMarker.setTranslateX(-translateX);
		lMarker.setTranslateY(translateY);
		
		StackPane.setAlignment(fMarker, Pos.BOTTOM_CENTER);
		StackPane.setAlignment(lMarker, Pos.BOTTOM_CENTER);
		
		fMarker.getStyleClass().setAll("end-marker");
		fMarker.setId("min");
		lMarker.getStyleClass().setAll("end-marker");
		lMarker.setId("max");
		
		arcHolder.getChildren().addAll(fMarker, lMarker);
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
	}
	
	public void setUpListeners(){
		getSkinnable().getDataProperty().addListener(valueChanged -> setNeedle(getSkinnable().getValue().doubleValue()));
		getSkinnable().getUnitProperty().addListener(unitChanged -> buildRegions());
	}
	

}
