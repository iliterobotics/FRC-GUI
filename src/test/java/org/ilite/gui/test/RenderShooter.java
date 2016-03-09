package org.ilite.gui.test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

public class RenderShooter extends Application {

	private static final String MESH_FILENAME = "C:/Users/Michael/Desktop/shooterhead.obj";
	private static final String BALL_FILENAME = "C:/Users/Michael/Desktop/ball.obj";

	private static final double MODEL_SCALE_FACTOR = 1;
	private static final double MODEL_X_OFFSET = 0; // standard
	private static final double MODEL_Y_OFFSET = -100; // standard

	private static final Color lightColor = Color.web("#CCCCCC");
	private static final Color modelColor = Color.web("#333333");
	private static final Color ballColor = Color.RED;

	private final int VIEWPORT_SIZE;
	
	private Stage stage;
	private Group root;
	private PointLight pointLight;

	private DoubleProperty xRotateProperty;
	private DoubleProperty yRotateProperty;
	private DoubleProperty zRotateProperty;
	
	private MeshView[] shooterMeshes;
	private MeshView[] ballMeshes;
	
	private boolean isBallVisable;

	private double lastX, lastY;

	public RenderShooter() {
		VIEWPORT_SIZE = 400;
	}

	public RenderShooter(Stage givenStage, int size) {
		VIEWPORT_SIZE = size;
		start(givenStage);
	}

	static MeshView[] loadMeshViews(String filename) {
		File file = new File(filename);
		ObjModelImporter importer = new ObjModelImporter();
		importer.read(file);
		return importer.getImport();
	}

	private Group buildScene() {
		

		Point3D origin = new Point3D(VIEWPORT_SIZE / 2 + MODEL_X_OFFSET, 
		    								 VIEWPORT_SIZE / 2 + MODEL_Y_OFFSET, 
		    								 VIEWPORT_SIZE * 2);
		  
		shooterMeshes = loadMeshViews(MESH_FILENAME);
		ballMeshes = loadMeshViews(BALL_FILENAME);
		for (MeshView meshView : shooterMeshes) {
			meshView.setTranslateX(origin.getX());
		    meshView.setTranslateY(origin.getY());
		    meshView.setTranslateZ(origin.getZ());
		    meshView.setScaleX(MODEL_SCALE_FACTOR);
		    meshView.setScaleY(MODEL_SCALE_FACTOR);
		    meshView.setScaleZ(MODEL_SCALE_FACTOR);
	
		    PhongMaterial sample = new PhongMaterial(modelColor);
		    meshView.setMaterial(sample);
	    }
		for(MeshView meshView : ballMeshes){
			meshView.setTranslateX(origin.getX());
		    meshView.setTranslateY(origin.getY());
		    meshView.setTranslateZ(origin.getZ());
		    meshView.setScaleX(MODEL_SCALE_FACTOR);
		    meshView.setScaleY(MODEL_SCALE_FACTOR);
		    meshView.setScaleZ(MODEL_SCALE_FACTOR);
		    
		    PhongMaterial sample = new PhongMaterial(ballColor);
		    meshView.setMaterial(sample);
		}
	    
	    xRotateProperty = new SimpleDoubleProperty(0);
	    yRotateProperty = new SimpleDoubleProperty(0);
	    zRotateProperty = new SimpleDoubleProperty(0);
	    
	    InvalidationListener changeRotation = observable -> {
	    	List<Rotate> rotations = Arrays.asList(new Rotate[] {
	    			new Rotate(xRotateProperty.get(), Rotate.X_AXIS),
					new Rotate(yRotateProperty.get(), Rotate.Y_AXIS),
					new Rotate(zRotateProperty.get(), Rotate.Z_AXIS)});
    		
	    	for(MeshView meshView: shooterMeshes){
	    		meshView.getTransforms().setAll(rotations);
	    	}
	    	for(MeshView meshView: ballMeshes){
	    		meshView.getTransforms().setAll(rotations);
	    	}
	    };
	      
	    xRotateProperty.addListener(changeRotation);
	    yRotateProperty.addListener(changeRotation);
	    zRotateProperty.addListener(changeRotation);
	    
	    

	    pointLight = new PointLight(lightColor);
	    pointLight.setTranslateX(VIEWPORT_SIZE*3/4);
	    pointLight.setTranslateY(VIEWPORT_SIZE/2);
	    pointLight.setTranslateZ(VIEWPORT_SIZE/2);
	    PointLight pointLight2 = new PointLight(lightColor);
	    pointLight2.setTranslateX(VIEWPORT_SIZE*1/4);
	    pointLight2.setTranslateY(VIEWPORT_SIZE*3/4);
	    pointLight2.setTranslateZ(VIEWPORT_SIZE*3/4);
	    PointLight pointLight3 = new PointLight(lightColor);
	    pointLight3.setTranslateX(VIEWPORT_SIZE*5/8);
	    pointLight3.setTranslateY(VIEWPORT_SIZE/2);
	    pointLight3.setTranslateZ(0);

	    Color ambientColor = Color.rgb(80, 80, 80, 0);
	    AmbientLight ambient = new AmbientLight(ambientColor);

	    root = new Group(shooterMeshes);
	    root.getChildren().addAll(ballMeshes);
	    root.getChildren().add(pointLight);
	    root.getChildren().add(pointLight2);
	    root.getChildren().add(pointLight3);
	    root.getChildren().add(ambient);
	    
	    root.setOnMouseDragged(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				if(arg0.isMiddleButtonDown()){
					yRotateProperty.set(arg0.getScreenX() - lastX);
					xRotateProperty.set(arg0.getScreenY() - lastY);
				}
			}
	    });
	    root.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				if(arg0.getButton() == MouseButton.SECONDARY){
					showBall(!isBallVisable);
				}
			}
	    });

	    showBall(false);
	    
	    return root;
	  }

	private PerspectiveCamera addCamera(Scene scene) {
		PerspectiveCamera perspectiveCamera = new PerspectiveCamera();
		perspectiveCamera.setFieldOfView(30);
		System.out.println("Near Clip: " + perspectiveCamera.getNearClip());
		System.out.println("Far Clip:  " + perspectiveCamera.getFarClip());
		System.out.println("FOV:       " + perspectiveCamera.getFieldOfView());

		scene.setCamera(perspectiveCamera);
		return perspectiveCamera;
	}

	@Override
	public void start(Stage primaryStage) {
		Group group = buildScene();
		double scale = 1;
		group.setScaleX(scale);
		group.setScaleY(scale);
		group.setScaleZ(scale);

		Scene scene = new Scene(group, VIEWPORT_SIZE, VIEWPORT_SIZE, true);
		scene.setFill(Color.TRANSPARENT);
		addCamera(scene);
		primaryStage.setScene(scene);
		primaryStage.setTitle("ShooterView");
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setWidth(VIEWPORT_SIZE);
		primaryStage.setHeight(VIEWPORT_SIZE);
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				lastX = arg0.getScreenX();
				lastY = arg0.getScreenY();
			}
		});
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (!arg0.isMiddleButtonDown()) {
					primaryStage.setX(primaryStage.getX() + arg0.getScreenX() - lastX);
					primaryStage.setY(primaryStage.getY() + arg0.getScreenY() - lastY);
					lastX = arg0.getScreenX();
					lastY = arg0.getScreenY();
				}
			}
		});
		primaryStage.show();
		stage = primaryStage;
	}
	
	public void showBall(boolean visable){
		isBallVisable = visable;
		for(MeshView meshView : ballMeshes){
			meshView.setVisible(visable);
		}
	}

	public Stage getStage() {
		return stage;
	}

	public void setXRotation(double a) {
		xRotateProperty.set(a);
	}

	public void setYRotation(double a) {
		yRotateProperty.set(a);
	}

	public void setZRotation(double a) {
		zRotateProperty.set(a);
	}

	public static void main(String[] args) {
		launch();
	}
}
