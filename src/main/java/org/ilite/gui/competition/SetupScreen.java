package org.ilite.gui.competition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import org.json.JSONArray;
import org.json.JSONObject;

public class SetupScreen{
	
	private HBox root;
	private VBox right;
	private ComboBox<SessionType> sessionTypeDropdown;
	private Rectangle middleLine;
	
	private Scene scene;
	
	private Spinner<Integer> matchNumberSpinner;
	private ComboBox<String> eventDropdown;

	private Map<String, Set<Integer>> eventMap;
	
	private Button done;
	
	public SetupScreen() {
		root = new HBox();
		root.getStyleClass().add("root-hbox");
		right = new VBox();
		right.getStyleClass().add("right-vbox");
		scene = new Scene(root);
		scene.getStylesheets().add("org/ilite/autoconfiguration/dropdown.css");
		scene.getStylesheets().add("login/loginscreen.css");
		buildComponents();
		root.getChildren().add(buildSessionDropdown());
		root.getChildren().add(middleLine);
		root.getChildren().add(right);
		root.getChildren().add(done);
	}
	
	private void buildComponents(){
		matchNumberSpinner = new Spinner<Integer>();
		matchNumberSpinner.getStyleClass().setAll("spinner");
		
		eventDropdown = new ComboBox<String>();
		eventDropdown.getItems().addAll(getEventMap().keySet());
		eventDropdown.setOnAction(updateMatchNums -> {
			List<Integer> matchNumbers = new ArrayList<Integer>();
			matchNumbers.addAll(getEventMap().get(eventDropdown.getValue()));
			matchNumberSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<Integer>(FXCollections.observableArrayList(matchNumbers)));
		});
		
		middleLine = new Rectangle(5, 250);
		middleLine.getStyleClass().setAll("middle-line");
		
		done = new Button("START");
		done.getStyleClass().add("start");
		done.setOnAction(observable -> {});
	}
	
	public Map<String, Set<Integer>> getEventMap(){
		if(eventMap == null){
			eventMap = new HashMap<String, Set<Integer>>();
			try{
				File jsonFile = new File("data/events.json");
				if(!jsonFile.exists()){
					System.err.println(jsonFile.getAbsolutePath() + " does not exist");
					return eventMap;
				}
				StringBuilder builder = new StringBuilder();
				BufferedReader reader = new BufferedReader(new FileReader(jsonFile));
				String line = reader.readLine();
				while(line != null){
					builder.append(line);
					line = reader.readLine();
				}
				reader.close();
				JSONObject events = new JSONObject(builder.toString());
				JSONArray eventArray = events.getJSONArray("events");
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
				return eventMap;
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
		right.getChildren().clear();
		switch(type){
		case MATCH:
			right.getChildren().setAll(eventDropdown, matchNumberSpinner);
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
