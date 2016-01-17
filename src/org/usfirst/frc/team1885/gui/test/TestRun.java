package org.usfirst.frc.team1885.gui.test;

import java.util.Collection;

import org.usfirst.frc.team1885.gui.widget.gauge.Gauge;
import org.usfirst.frc.team1885.gui.widget.voltometer.Voltometer;

public class TestRun implements Runnable{

	private Collection<Voltometer> volts;
	private Collection<Gauge> gauges;
	private boolean running;
	
	public TestRun(Collection<Voltometer> volts, Collection<Gauge> gauges){
		this.volts = volts;
		this.gauges = gauges;
		running = true;
	}
	
	@Override
	public void run() {
		while(running){
			try {
				Thread.sleep(400);
				for(Voltometer volt : volts){
					volt.setVoltage((float)(Math.random() * 2 - 1));
				}
				for(Gauge gauge : gauges){
					gauge.setValue(Math.random()* gauge.getMax().doubleValue());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop(){
		running = false;
	}

}
