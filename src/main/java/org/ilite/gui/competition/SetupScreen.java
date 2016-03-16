package org.ilite.gui.competition;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import org.json.JSONArray;
import org.json.JSONObject;

public class SetupScreen{

	private static final double DEFAULT_WIDTH = 600;
	private static final double DEFAULT_HEIGHT = 400;
	
	private HBox root;
	private VBox right;
	private ComboBox<SessionType> sessionTypeDropdown;
	private Region middleLine;
	
	private Scene scene;
	
	private ObjectProperty<SessionType> sessionTypeProperty;
	private Spinner<Integer> matchNumberSpinner;
	private ComboBox<String> eventSpinner;

	private Map<String, Set<Integer>> eventMap;
	
	public SetupScreen() {
		root = new HBox();
		right = new VBox();
		buildComponents();
		root.getChildren().add(buildSessionDropdown());
		root.getChildren().add(right);
	}
	
	private void buildComponents(){
		matchNumberSpinner = new Spinner<Integer>();
		
		eventSpinner = new ComboBox<String>();
		eventSpinner.getItems().addAll(getEventMap().keySet());
		eventSpinner.setOnAction(updateMatchNums -> {
			ListProperty<Integer> matchNumbers = new SimpleListProperty<Integer>();
			matchNumbers.addAll(getEventMap().get(eventSpinner.getValue()));
			matchNumberSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<Integer>(matchNumbers));
		});
	}
	
	public Map<String, Set<Integer>> getEventMap(){
		if(eventMap == null){
			try{
				JSONObject events = new JSONObject(new File("/data/events.json"));
				JSONArray eventArray = events.getJSONArray("events");
				eventMap = new HashMap<String, Set<Integer>>();
				for(int i = 0; i < eventArray.length(); i++){
					JSONObject event = eventArray.getJSONObject(i);
					JSONArray matches = event.getJSONArray("matches");
					Set<Integer> matchesSet = new HashSet<Integer>();
					for(int n = 0; n < matches.length(); n++){
						matchesSet.add(matches.getInt(n));
					}
					eventMap.put(event.getString("name"), matchesSet);
				}
			}catch(Exception e){
				System.err.println("ERROR READING JSON:/data/events.json");
				e.printStackTrace();
			}
		}
		return eventMap;
	}
	
	private ComboBox<SessionType> buildSessionDropdown(){
		sessionTypeDropdown = new ComboBox<SessionType>();
		sessionTypeDropdown.getItems().setAll(SessionType.values());
		sessionTypeDropdown.setOnAction(changeRight -> setRight(sessionTypeDropdown.getValue()));
		
		return sessionTypeDropdown;
	}
	
	private void setRight(SessionType type){
		switch(type){
		case MATCH:
			right.getChildren().setAll(matchNumberSpinner);
		break;
		case TEST:
			
		break;
		case PIT_TEST:
		
		break;
		}
	}
	
	public Scene getScene(){
		return scene;
	}

}
