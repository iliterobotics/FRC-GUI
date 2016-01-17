package org.usfirst.frc.team1885.gui.widget.toggleSwitch;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.Tooltip;

import org.usfirst.frc.team1885.gui.widget.toggleSwitch.skins.ToggleSwitchSkin;

/**
 * A simple on/off switch that slides vertically
 * @author Michael Kelly 
 * @version 1.0
 */
public class ToggleSwitch extends Control{

	private BooleanProperty activatedProperty;
	
	private final String SHORT_NAME;
	private final String FULL_NAME;
	
	public ToggleSwitch(String shortName, String fullName){
		this(new SimpleBooleanProperty(false), shortName, fullName);
	}
	
	public ToggleSwitch(BooleanProperty onoffProperty, String shortName, String fullName){
		activatedProperty = onoffProperty;
		setOnMouseClicked(observable -> {
			setState(!isActivated());
		});
		SHORT_NAME = shortName;
		FULL_NAME = fullName;
		
		setTooltip(new Tooltip(FULL_NAME));
	}
	
	public void setState(boolean activated){
		activatedProperty.set(activated);
	}
	
	public boolean isActivated(){
		return activatedProperty.get();
	}
	
	public String getShortName(){
		return SHORT_NAME;
	}
	
	public String getFullName(){
		return FULL_NAME;
	}
	
	public BooleanProperty getActivatedProperty(){
		return activatedProperty;
	}

	public void flip(){
		activatedProperty.set(!activatedProperty.get());
	}
	
	public Skin<ToggleSwitch> createDefaultSkin(){
		return new ToggleSwitchSkin(this);
	}
}
