package org.ilite.gui.widget.gauge;

import javafx.scene.paint.Color;

public class GaugeMark implements Comparable<GaugeMark>{
	
	private final Number VALUE;
	private final Color COLOR;
	
	public GaugeMark(Number value, Color c){
		VALUE = value;
		COLOR = c;
	}
	
	public Color getColor(){
		return COLOR;
	}
	
	public Number getValue(){
		return VALUE;
	}
	
	@Override
	public boolean equals(Object other){
		return (other instanceof GaugeMark) && ((GaugeMark)other).getValue().equals(getValue());
	}

	@Override
	public int compareTo(GaugeMark other) {
		float diff = other.getValue().floatValue() - getValue().floatValue();
		return (diff<0?-1:1) + (int)diff;
	}

}
