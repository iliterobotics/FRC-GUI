package org.ilite.gui.drivers;

import org.ilite.gui.widget.shooter.Shooter3D;

import dataclient.DataServerWebClient;
import dataclient.robotdata.shooter.ShooterStatus;

public class ShooterDataDriver extends DataDriver<ShooterStatus, Shooter3D>{

	public ShooterDataDriver(DataServerWebClient client, ShooterStatus dataObject, Shooter3D object) {
		super(client, dataObject, object);
	}

	@Override
	public void dataRecieved(ShooterStatus dataObject) {
		getControl().setTilt(dataObject.getTilt());
		getControl().setTwist(dataObject.getTwist());
	}

}
