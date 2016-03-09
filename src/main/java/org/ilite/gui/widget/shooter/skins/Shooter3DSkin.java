package org.ilite.gui.widget.shooter.skins;

import javafx.scene.control.SkinBase;
import javafx.scene.shape.Rectangle;

import org.ilite.gui.widget.shooter.Shooter3D;

public class Shooter3DSkin extends SkinBase<Shooter3D>{

	private Rectangle background;
	
	public Shooter3DSkin(Shooter3D control) {
		super(control);
		initGraphics();
	}
	
	private void initGraphics(){
		background = new Rectangle(getSkinnable().getWidth(), getSkinnable().getHeight());
		background.setFill(getSkinnable().getBackgroundColor());
		getChildren().setAll(background);
	}

}
