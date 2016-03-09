package org.ilite.gui.widget.shooter;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.ilite.gui.test.RenderShooter;
import org.ilite.gui.widget.shooter.skins.Shooter3DSkin;

public class Shooter3D extends Control{
	
	private DoubleProperty tiltProperty;
	private DoubleProperty twistProperty;
	
	private Color backgroundColor;
	private RenderShooter shooterRender;
	private Stage primaryStage;
	
	private double xoff, yoff;
	
	public Shooter3D(int size, Color backgroundColor, Stage primaryStage, double xOffset, double yOffset){
		tiltProperty = new SimpleDoubleProperty();
		twistProperty = new SimpleDoubleProperty();
		setWidth(size);
		setHeight(size);
		this.backgroundColor = backgroundColor;
		this.primaryStage = primaryStage;
		xoff = xOffset;
		yoff = yOffset;

		primaryStage.xProperty().addListener(observable -> updatePosition());
		primaryStage.yProperty().addListener(observable -> updatePosition());
		
		Stage shooterStage = new Stage();
		shooterStage.initOwner(primaryStage);
		shooterStage.setAlwaysOnTop(true);
		primaryStage.focusedProperty().addListener(observable -> {
			shooterStage.setAlwaysOnTop(primaryStage.isFocused());
		});
		shooterRender = new RenderShooter(shooterStage, size);
		
		updatePosition();
	}
	
	private void updatePosition(){
		shooterRender.getStage().setX(primaryStage.getX() + getLayoutX() + xoff);
		shooterRender.getStage().setY(primaryStage.getY() + getLayoutY() + yoff);
	}
	
	public Color getBackgroundColor(){
		return backgroundColor;
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
