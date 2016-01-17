package org.usfirst.frc.team1885.gui.test;

import org.usfirst.frc.team1885.gui.widget.gauge.Gauge;
import org.usfirst.frc.team1885.gui.widget.voltometer.Voltometer;

public class TestRun implements Runnable{

	private Voltometer volt;
	private Gauge gauge;
	private boolean running;
	
	public TestRun(Voltometer volt, Gauge gauge){
		this.volt = volt;
		this.gauge = gauge;
		running = true;
	}
	
	@Override
	public void run() {
		while(running){
			try {
				Thread.sleep(400);
				volt.setVoltage((float)(Math.random() * 2 - 1));
				gauge.setValue((volt.getVoltage() + 1) / 2 * gauge.getMax().doubleValue());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop(){
		running = false;
	}

}
