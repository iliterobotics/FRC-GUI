package org.ilite.gui.drivers;

import javafx.scene.Node;
import dataclient.DataServerWebClient;
import dataclient.robotdata.RobotDataObject;

public abstract class DataDriver<D extends RobotDataObject, C extends Node> {
	
	private D dataObject;	
	private C control;
	private DataServerWebClient client;
	
	public DataDriver(DataServerWebClient client, D dataObject, C object){
		this.dataObject = dataObject;
		this.control = object;
		this.client = client;
	}
	
	public void launch(){
		client.watch(dataObject, observable -> dataRecieved(this.dataObject));
	}
	
	public D getDataObject(){
		return dataObject;
	}
	
	public C getControl(){
		return control;
	}
	
	public abstract void dataRecieved(D dataObject);

}
