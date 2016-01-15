package org.usfirst.frc.team1885.gui.widget.voltometer.skins;

import org.usfirst.frc.team1885.gui.widget.voltometer.Voltometer;

import javafx.scene.control.SkinBase;
import javafx.scene.text.Text;

public class VoltometerSkin extends SkinBase<Voltometer>{

	private Text volt; 
	
	public VoltometerSkin(Voltometer control) {
		super(control);
		volt = new Text(control.getVoltage() + ""); 
		getChildren().add(volt); 
		control.getVoltageProperty();
	}
	
}
