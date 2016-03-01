package org.ilite.gui.test;

import java.io.File;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;


import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

public class TestRender extends Application {

	  private static final String MESH_FILENAME =
	    "C:/Users/Michael/Desktop/teddy.obj";

	  private static final double MODEL_SCALE_FACTOR = 20;
	  private static final double MODEL_X_OFFSET = 0; // standard
	  private static final double MODEL_Y_OFFSET = -100; // standard

	  private static final int VIEWPORT_SIZE = 400;

	  private static final Color lightColor = Color.WHITE;
	  private static final Color jewelColor = Color.CYAN;

	  private int rot = 0;
	  private Group root;
	  private PointLight pointLight;

	  static MeshView[] loadMeshViews() {
	    File file = new File(MESH_FILENAME);
	    ObjModelImporter importer = new ObjModelImporter();
	    importer.read(file);
	    return importer.getImport();
	  }

	  private Group buildScene() {
	    MeshView[] meshViews = loadMeshViews();
	    for (int i = 0; i < meshViews.length; i++) {
	      meshViews[i].setTranslateX(VIEWPORT_SIZE / 2 + MODEL_X_OFFSET);
	      meshViews[i].setTranslateY(VIEWPORT_SIZE / 2 + MODEL_Y_OFFSET);
	      meshViews[i].setTranslateZ(VIEWPORT_SIZE * 2);
	      meshViews[i].setScaleX(MODEL_SCALE_FACTOR);
	      meshViews[i].setScaleY(MODEL_SCALE_FACTOR);
	      meshViews[i].setScaleZ(MODEL_SCALE_FACTOR);

	      PhongMaterial sample = new PhongMaterial(jewelColor);
	      sample.setSpecularColor(lightColor);
	      sample.setSpecularPower(16);
	      meshViews[i].setMaterial(sample);
	      
	      MeshView meshView = meshViews[i];
	      
	      DoubleProperty rotateProperty = new SimpleDoubleProperty(0);
	      rotateProperty.addListener(observable -> {
	    	  meshView.getTransforms().setAll(new Rotate(rotateProperty.get(), Rotate.Y_AXIS));
	      });
	      
	      meshViews[i].setOnMouseClicked(observable -> {
	    	  Timeline timeline = new Timeline(new KeyFrame(Duration.millis(3000), new KeyValue(rotateProperty, 360)));
	    	  timeline.setAutoReverse(true);
	    	  timeline.setCycleCount(Timeline.INDEFINITE);
	    	  timeline.play();
	      });

	    //  meshViews[i].getTransforms().setAll(new Rotate(38, Rotate.Z_AXIS), new Rotate(20, Rotate.X_AXIS));
	    }

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

	    root = new Group(meshViews);
	    root.getChildren().add(pointLight);
	    root.getChildren().add(pointLight2);
	    root.getChildren().add(pointLight3);
	    root.getChildren().add(ambient);

	    return root;
	  }

	  private PerspectiveCamera addCamera(Scene scene) {
	    PerspectiveCamera perspectiveCamera = new PerspectiveCamera();
	    perspectiveCamera.setFieldOfView(100);
	    perspectiveCamera.setNearClip(1);
	    System.out.println("Near Clip: " + perspectiveCamera.getNearClip());
	    System.out.println("Far Clip:  " + perspectiveCamera.getFarClip());
	    System.out.println("FOV:       " + perspectiveCamera.getFieldOfView());

	    scene.setCamera(perspectiveCamera);
	    return perspectiveCamera;
	  }

	  @Override
	  public void start(Stage primaryStage) {
	    Group group = buildScene();
	    group.setScaleX(2);
	    group.setScaleY(2);
	    group.setScaleZ(2);
	    group.setTranslateX(50);
	    group.setTranslateY(50);

	    Scene scene = new Scene(group, VIEWPORT_SIZE, VIEWPORT_SIZE, true);
	    scene.setFill(Color.rgb(10, 10, 40));
	    addCamera(scene);
	    primaryStage.setTitle("Jewel Viewer");
	    primaryStage.setScene(scene);
	    primaryStage.show();
	  }

	  public static void main(String[] args) {
	    System.setProperty("prism.dirtyopts", "false");
	    launch(args);
	  }
	}
