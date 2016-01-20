package org.usfirst.frc.team1885.gui.widget.gauge;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import org.usfirst.frc.team1885.gui.widget.gauge.skins.GaugeSkin;

public class Gauge extends Control{
	
	private Property<Number> dataProperty;
	private Set<GaugeMark> keyMarks;
	private StringProperty unitProperty;
	
	private Number min, max;
	
	public Gauge(Property<Number> dataProperty, Number min, Number max, Collection<GaugeMark> marks){
		this.dataProperty = dataProperty;
		this.keyMarks = new TreeSet<GaugeMark>();
		keyMarks.addAll(marks);
		this.min = min;
		this.max = max;
		
		unitProperty = new SimpleStringProperty("");
		
		setOnMouseClicked(observable -> setUnit("v"));
	}
	
	public void setUnit(String unit){
		unitProperty.set(" " + unit);
	}
	
	public String getUnit(){
		return unitProperty.get();
	}
	
	public Set<GaugeMark> getKeyMarks(){
		return keyMarks;
	}
	
	public Number getMin(){
		return min;
	}
	
	public Number getMax(){
		return max;
	}
	
	public Number getValue(){
		return dataProperty.getValue();
	}
	
	public void setValue(Number num){
		dataProperty.setValue(num);
	}
	
	public Property<Number> getDataProperty(){
		return dataProperty;
	}
	
	public StringProperty getUnitProperty(){
		return unitProperty;
	}
	
	public Skin<Gauge> createDefaultSkin(){
		return new GaugeSkin(this);
	}
}
