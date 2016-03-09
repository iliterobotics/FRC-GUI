package org.ilite.gui.widget.armDisplay;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Control;

import org.ilite.gui.widget.armDisplay.skin.ArmDisplaySkin;

public class ArmDisplay extends Control{

	private static final double DEFAULT_BETA = 10;
	private DoubleProperty alphaProperty;
	private DoubleProperty betaProperty;
	private DoubleProperty destinationXProperty;
	private DoubleProperty destinationYProperty;
	
	public ArmDisplay(double width, double height){
		alphaProperty = new SimpleDoubleProperty(0);
		betaProperty = new SimpleDoubleProperty(DEFAULT_BETA);
		destinationXProperty = new SimpleDoubleProperty(0);
		destinationYProperty = new SimpleDoubleProperty(0);
		setSkin(new ArmDisplaySkin(this, width, height));
		setWidth(width);
		setHeight(height);
	}
	
	public void setAlpha(double angle){
		alphaProperty.set(angle);
	}
	
	public void setBeta(double angle){
		betaProperty.set(angle);
	}
	
	public void setDestinationX(double x){
		destinationXProperty.set(x);
	}
	
	public void setDestinationY(double y){
		destinationYProperty.set(y);
	}
	
	public double getDestinationX(){
		return destinationXProperty.get();
	}
	
	public double getDestinationY(){
		return destinationYProperty.get();
	}
	
	public double getAlpha(){
		return alphaProperty.get();
	}
	
	public double getBeta(){
		return betaProperty.get();
	}
	
	public ReadOnlyDoubleProperty getAlphaProperty(){
		return alphaProperty;
	}
	
	public ReadOnlyDoubleProperty getBetaProperty(){
		return betaProperty;
	}
	
	public ReadOnlyDoubleProperty getDestinationXProperty(){
		return destinationXProperty;
	}
	
	public ReadOnlyDoubleProperty getDestinationYProperty(){
		return destinationYProperty;
	}
	
}
