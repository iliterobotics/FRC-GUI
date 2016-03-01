package org.ilite.gui.widget.defenses.skins;

import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import org.ilite.gui.widget.defenses.Defenses;

public class DefensesSkin extends SkinBase<Defenses>{

	private HBox background; 
	private Text namePos2, namePos3, namePos4, namePos5; 
	
	
	public DefensesSkin(Defenses control) {
		super(control);
		
		control.getStyleClass().setAll("defenses"); 
		control.getStylesheets().add("/org/ilite/widgets/Defenses.css"); 
		
		initGraphics(); 
		
	}
	public void initGraphics(){
		
		namePos2 = new Text(getSkinnable().getPos2());  
		namePos3 = new Text(getSkinnable().getPos3());
		namePos4 = new Text(getSkinnable().getPos4());
		namePos5 = new Text(getSkinnable().getPos5());
		
		background = new HBox(namePos2, namePos3, namePos4, namePos5); 
		getSkinnable().getStyleClass().addAll("background"); 
		getChildren().add(background); 

	}

}
