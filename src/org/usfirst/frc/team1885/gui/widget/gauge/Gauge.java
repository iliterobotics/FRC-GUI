package org.usfirst.frc.team1885.gui.widget.gauge;

import javafx.beans.property.Property;
import javafx.scene.control.Control;

public class Gauge extends Control{
	
	private Property<Number> dataProperty;
	
	public Gauge(Property<Number> dataProperty){
		this.dataProperty = dataProperty;
	}
	
	public Number getValue(){
		return dataProperty.getValue();
	}
	
	public void setValue(Number num){
		dataProperty.setValue(num);
	}
}
