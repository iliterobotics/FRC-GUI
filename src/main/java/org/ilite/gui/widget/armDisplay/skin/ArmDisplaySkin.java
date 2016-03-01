package org.ilite.gui.widget.armDisplay.skin;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.SkinBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.util.Duration;

import org.ilite.gui.widget.armDisplay.ArmDisplay;

public class ArmDisplaySkin extends SkinBase<ArmDisplay> {

	private static final int SCALAR = 7;

	private static final double LENGTH_A = 17.5;
	private static final double LENGTH_B = 18;
	
	private static final double ARM_WIDTH = 15;
	private static final double ARM_OUTLINE_WIDTH = 3;
	
	private static final double TOTAL_GRAPH_WIDTH = 80;
	private static final double TOTAL_GRAPH_HEIGHT = 60;


	private static final int PIVOT_POINT_X = 2;
	private static final int PIVOT_POINT_Y = 3;
	private static final int END_POINT_X = 4;
	private static final int END_POINT_Y = 5;

	private static DoubleProperty pivotPointXProperty;
	private static DoubleProperty pivotPointYProperty;
	private static DoubleProperty endPointXProperty;
	private static DoubleProperty endPointYProperty;
	
	private static DoubleProperty alphaRepresentationProperty;
	private static DoubleProperty betaRepresentationProperty;
	private static DoubleProperty destinationXRepresentationProperty;
	private static DoubleProperty destinationYRepresentationProperty;

	private StackPane background;
	private ImageView backgroundImage;
	private Circle endpoint;
	private Circle toPoint;
	private Polyline arm;
	private Polyline armOutline;
	
	private Timeline polylineAnimation;
	private Timeline destinationAnimation;

	private double oldPivotPointX, oldPivotPointY, oldEndPointX, oldEndPointY;

	public ArmDisplaySkin(ArmDisplay control) {
		super(control);
		control.getStylesheets().add("/org/ilite/widgets/ArmDisplay.css");
		
		getSkinnable().setMinSize(TOTAL_GRAPH_WIDTH * SCALAR, TOTAL_GRAPH_HEIGHT * SCALAR);

		alphaRepresentationProperty = new SimpleDoubleProperty(getSkinnable().getAlpha());
		betaRepresentationProperty = new SimpleDoubleProperty(getSkinnable().getBeta());
		
		destinationXRepresentationProperty = new SimpleDoubleProperty(-1);
		destinationYRepresentationProperty = new SimpleDoubleProperty(-1);

		oldPivotPointX = getPivotPointX();
		oldPivotPointY = getPivotPointY();
		oldEndPointX = getEndPointX();
		oldEndPointY = getEndPointY();

		pivotPointXProperty = new SimpleDoubleProperty(oldPivotPointX);
		pivotPointYProperty = new SimpleDoubleProperty(oldPivotPointY);
		endPointXProperty = new SimpleDoubleProperty(oldEndPointX);
		endPointYProperty = new SimpleDoubleProperty(oldEndPointY);
		
		
		initGraphics();
		updateArm();
		setUpListeners();
		adjustArmOffset();
		updateToPointLocation();
	}

	private void initGraphics() {
		background = new StackPane();
		background.getStyleClass().setAll("background");
		background.setMinSize(TOTAL_GRAPH_WIDTH * SCALAR, TOTAL_GRAPH_HEIGHT * SCALAR);
		background.setMaxSize(TOTAL_GRAPH_WIDTH * SCALAR, TOTAL_GRAPH_HEIGHT * SCALAR);
		getSkinnable().setMinSize(TOTAL_GRAPH_WIDTH * SCALAR, TOTAL_GRAPH_HEIGHT * SCALAR);
		
		backgroundImage = new ImageView(new Image("/arm/background_grid.png"));
		backgroundImage.setFitHeight(TOTAL_GRAPH_HEIGHT * SCALAR);
		backgroundImage.setFitWidth(TOTAL_GRAPH_WIDTH * SCALAR);
		
		arm = new Polyline(0, 0, pivotPointXProperty.get(), pivotPointYProperty.get(), endPointXProperty.get(), endPointYProperty.get());
		arm.getStyleClass().setAll("arm");
		StackPane.setAlignment(arm, Pos.BOTTOM_LEFT);
		
		armOutline = new Polyline(0, 0, pivotPointXProperty.get(), pivotPointYProperty.get(), endPointXProperty.get(), endPointYProperty.get());
		armOutline.getStyleClass().setAll("arm");
		armOutline.setId("outline");
		StackPane.setAlignment(armOutline, Pos.BOTTOM_LEFT);
		
		endpoint = new Circle(ARM_WIDTH / 2);
		endpoint.getStyleClass().setAll("endpoint");
		StackPane.setAlignment(endpoint, Pos.BOTTOM_LEFT);
		
		toPoint = new Circle(ARM_WIDTH / 2);
		toPoint.getStyleClass().setAll("to-point");
		StackPane.setAlignment(toPoint, Pos.BOTTOM_LEFT);
		
		background.getChildren().addAll(backgroundImage, armOutline, arm, endpoint, toPoint);
		
		getChildren().setAll(background);
	}

	private void setUpListeners() {
		pivotPointXProperty.addListener(observable -> arm.getPoints().set(PIVOT_POINT_X, pivotPointXProperty.get()));
		pivotPointYProperty.addListener(observable -> arm.getPoints().set(PIVOT_POINT_Y, pivotPointYProperty.get()));
		endPointXProperty.addListener(observable -> arm.getPoints().set(END_POINT_X, endPointXProperty.get()));
		endPointYProperty.addListener(observable -> arm.getPoints().set(END_POINT_Y, endPointYProperty.get()));
		
		getSkinnable().getDestinationXProperty().addListener(observable -> updateToPointLocation());
		getSkinnable().getDestinationYProperty().addListener(observable -> updateToPointLocation());
		
		alphaRepresentationProperty.addListener(observable -> {
			pivotPointXProperty.set(getPivotPointX());
			pivotPointYProperty.set(getPivotPointY());
		});
		
		betaRepresentationProperty.addListener(observable -> {
			endPointXProperty.set(getEndPointX());
			endPointYProperty.set(getEndPointY());
		});
		
		destinationXRepresentationProperty.addListener(observable -> {
			toPoint.setTranslateX(TOTAL_GRAPH_WIDTH / 2 * SCALAR - ARM_WIDTH / 2 + destinationXRepresentationProperty.get() * SCALAR);
		});
		
		destinationYRepresentationProperty.addListener(observable -> {
			toPoint.setTranslateY(-TOTAL_GRAPH_HEIGHT * (1.0/3) * SCALAR + ARM_WIDTH / 2 - destinationYRepresentationProperty.get() * SCALAR);
		});
		
		
		arm.getPoints().addListener(new InvalidationListener() {
			public void invalidated(Observable observable) {
				armOutline.getPoints().setAll(arm.getPoints());
				adjustArmOffset();
			}
		});

		getSkinnable().getAlphaProperty().addListener(observable -> updateArm());
		getSkinnable().getBetaProperty().addListener(observable -> updateArm());
	}
	
	private void updateToPointLocation() {
		if(destinationAnimation != null){
			destinationAnimation.stop();
		}
		destinationAnimation = new Timeline(new KeyFrame(Duration.millis(100), new KeyValue(destinationXRepresentationProperty, getSkinnable().getDestinationX()),
				                                                               new KeyValue(destinationYRepresentationProperty, getSkinnable().getDestinationY())));
		destinationAnimation.play();
	}

	private void adjustArmOffset() {
		double xDist = 0;
		if (arm.getPoints().get(END_POINT_X) < 0) {
			if (arm.getPoints().get(PIVOT_POINT_X) < 0 && arm.getPoints().get(PIVOT_POINT_X) < arm.getPoints().get(END_POINT_X)) {
				xDist = arm.getPoints().get(PIVOT_POINT_X);
			} else {
				xDist = arm.getPoints().get(END_POINT_X);
			}
		}
		else if(arm.getPoints().get(PIVOT_POINT_X) < 0){
			if (arm.getPoints().get(END_POINT_X) < 0 && arm.getPoints().get(END_POINT_X) < arm.getPoints().get(PIVOT_POINT_X)) {
				xDist = arm.getPoints().get(END_POINT_X);
			} else {
				xDist = arm.getPoints().get(PIVOT_POINT_X);
			}
		}
		
		double yDist = 0;
		if (arm.getPoints().get(END_POINT_Y) > 0) {
			if (arm.getPoints().get(PIVOT_POINT_Y) > 0 && arm.getPoints().get(PIVOT_POINT_Y) > arm.getPoints().get(END_POINT_Y)) {
				yDist = arm.getPoints().get(PIVOT_POINT_Y);
			} else {
				yDist = arm.getPoints().get(END_POINT_Y);
			}
		}
		else if(arm.getPoints().get(PIVOT_POINT_Y) > 0){
			if (arm.getPoints().get(END_POINT_Y) > 0 && arm.getPoints().get(END_POINT_Y) > arm.getPoints().get(PIVOT_POINT_Y)) {
				yDist = arm.getPoints().get(END_POINT_Y);
			} else {
				yDist = arm.getPoints().get(PIVOT_POINT_Y);
			}
		}
		arm.setTranslateX(xDist + TOTAL_GRAPH_WIDTH * SCALAR / 2 - ARM_WIDTH / 2);
		arm.setTranslateY(yDist + -TOTAL_GRAPH_HEIGHT * SCALAR * (1.0/3) + ARM_WIDTH / 2);
		
		armOutline.setTranslateX(arm.getTranslateX() - ARM_OUTLINE_WIDTH/2);
		armOutline.setTranslateY(arm.getTranslateY() + ARM_OUTLINE_WIDTH/2);
		
		endpoint.setTranslateX( TOTAL_GRAPH_WIDTH  / 2 * SCALAR - ARM_WIDTH / 2 + arm.getPoints().get(END_POINT_X) );
		endpoint.setTranslateY( -TOTAL_GRAPH_HEIGHT * (1.0/3) * SCALAR + ARM_WIDTH / 2 + arm.getPoints().get(END_POINT_Y) );
	}
	
	public void updateArm() {
		if(polylineAnimation != null)polylineAnimation.stop();
		polylineAnimation = new Timeline(new KeyFrame(Duration.millis(500), new KeyValue(alphaRepresentationProperty, getSkinnable().getAlpha()), new KeyValue(betaRepresentationProperty, getSkinnable().getBeta())));
		polylineAnimation.play();
	}

	private double getPivotPointX() {
		return SCALAR * LENGTH_A * Math.cos(Math.toRadians(alphaRepresentationProperty.get()));
	}

	private double getPivotPointY() {
		return -SCALAR * LENGTH_A * Math.sin(Math.toRadians(alphaRepresentationProperty.get()));
	}

	private double getEndPointX() {
		return getPivotPointX() + SCALAR * LENGTH_B * Math.cos(Math.toRadians(180 - betaRepresentationProperty.get() + alphaRepresentationProperty.get()));
	}

	private double getEndPointY() {
		return getPivotPointY() - SCALAR * LENGTH_B * Math.sin(Math.toRadians(180 - betaRepresentationProperty.get() + alphaRepresentationProperty.get()));
	}
}
