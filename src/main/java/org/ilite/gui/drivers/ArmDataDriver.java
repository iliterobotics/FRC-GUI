package org.ilite.gui.drivers;

import org.ilite.gui.widget.armDisplay.ArmDisplay;

import dataclient.DataServerWebClient;
import dataclient.robotdata.arm.ArmStatus;

public class ArmDataDriver extends DataDriver<ArmStatus, ArmDisplay>{

	public ArmDataDriver(DataServerWebClient client, ArmStatus dataObject, ArmDisplay object) {
		super(client, dataObject, object);
	}

	@Override
	public void dataRecieved(ArmStatus dataObject) {
		getControl().setAlpha(dataObject.getAlpha());
		getControl().setBeta(dataObject.getBeta());
		getControl().setDestinationX(dataObject.getDestX());
		getControl().setDestinationY(dataObject.getDestY());
	}

}
