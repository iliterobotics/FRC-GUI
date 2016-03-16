package org.ilite.gui.competition;

public enum SessionType {
	MATCH("match"),
	TEST("test"),
	PIT_TEST("pit_test");
	
	final String name;
	SessionType(String name){
		this.name = name;
	}
}
