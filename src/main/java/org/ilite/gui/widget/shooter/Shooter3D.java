package org.ilite.gui.widget.shooter;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import org.ilite.gui.widget.shooter.skins.Shooter3DSkin;

public class Shooter3D extends Control{
	
	private DoubleProperty tiltProperty;
	private DoubleProperty twistProperty;
	
	public Shooter3D(){
		tiltProperty = new SimpleDoubleProperty();
		twistProperty = new SimpleDoubleProperty();
	}
	
	public ReadOnlyDoubleProperty getTiltProperty(){
		return tiltProperty;
	}
	
	public ReadOnlyDoubleProperty getTwistProperty(){
		return twistProperty;
	}
	
	public void setTilt(double tilt){
		tiltProperty.set(tilt);
	}
	
	public void setTwist(double twist){
		twistProperty.set(twist);
	}
	
	public Skin<Shooter3D> createDefaultSkin(){
		return new Shooter3DSkin(this);
	}

}
