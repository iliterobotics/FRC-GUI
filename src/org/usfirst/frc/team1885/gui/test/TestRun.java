package org.usfirst.frc.team1885.gui.test;

import org.usfirst.frc.team1885.gui.widget.voltometer.Voltometer;

public class TestRun implements Runnable{

	private Voltometer volt;
	private boolean running;
	
	public TestRun(Voltometer volt){
		this.volt = volt;
		running = true;
	}
	
	@Override
	public void run() {
		while(running){
			try {
				Thread.sleep(750);
				volt.setVoltage((float)(Math.random() * 2 - 1));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop(){
		running = false;
	}

}
