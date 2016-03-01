package org.ilite.gui.widget.shooter.skins;

import java.io.File;

import javafx.scene.Group;
import javafx.scene.control.SkinBase;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;

import org.ilite.gui.widget.shooter.Shooter3D;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

public class Shooter3DSkin extends SkinBase<Shooter3D>{

	public Shooter3DSkin(Shooter3D control) {
		super(control);
		initGraphics();
	}
	
	private void initGraphics(){
		Group root = new Group();
		
		PhongMaterial color = new PhongMaterial(Color.GREEN);
		
		for(MeshView meshView : loadMeshViews()){
			meshView.setMaterial(color);
		}
		
		
		//Scene viewport = new Scene(root);
		getChildren().add(root);
	}
	
	private static MeshView[] loadMeshViews(){
		File file = new File("C:/Users/Michael/Desktop/teddy.obj");
		ObjModelImporter importer = new ObjModelImporter();
		importer.read(file);
		return importer.getImport();
	}
	

}
