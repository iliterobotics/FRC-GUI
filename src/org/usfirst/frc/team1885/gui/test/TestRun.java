package org.usfirst.frc.team1885.gui.test;

import org.usfirst.frc.team1885.gui.widget.voltometer.Voltometer;

public class TestRun implements Runnable{

	private Voltometer volt;
	
	public TestRun(Voltometer volt){
		this.volt = volt;
	}
	
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(750);
				volt.setVoltage((float)(Math.random() * 2 - 1));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
