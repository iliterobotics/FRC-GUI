package org.usfirst.frc.team1885.gui.widget.gauge.skins;

import java.util.Set;

import javafx.geometry.Pos;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

import org.usfirst.frc.team1885.gui.widget.gauge.Gauge;
import org.usfirst.frc.team1885.gui.widget.gauge.GaugeMark;

public class GaugeSkin extends SkinBase<Gauge>{

	private StackPane background;
	private Region needle;

	private static final double radius = 100;
	private static final double baselineAngle = 45;
	
	public GaugeSkin(Gauge control) {
		super(control);
		
		getSkinnable().getStylesheets().add("/org/usfirst/frc/team1885/gui/css/Gauge.css");
		
		initGraphics();
	}
	
	public void initGraphics(){
		background = new StackPane();
		getChildren().add(background);
		background.getStyleClass().setAll("gauge-background");
		
		buildRegions();
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
			
			arc.setRadiusX(100);
			arc.setRadiusY(100);
			
			double totalAngle = Math.min(90.0, baselineAngle - arc.getLength());
			double radians = Math.toRadians(totalAngle);
			double cosine = Math.sin(radians);
			
			arc.setTranslateX(100 - 100.0*cosine);
			
			background.getChildren().add(arc);
		}	
	}
	
	public void setUpNeedle(){
		needle = new Region();
		needle.getStyleClass().setAll("needle");
	}
	

}
