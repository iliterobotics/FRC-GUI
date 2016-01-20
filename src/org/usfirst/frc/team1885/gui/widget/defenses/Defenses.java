package org.usfirst.frc.team1885.gui.widget.defenses;

import java.util.Scanner;

import org.usfirst.frc.team1885.gui.widget.voltometer.Voltometer;
import org.usfirst.frc.team1885.gui.widget.voltometer.skins.DefensesSkin;
import org.usfirst.frc.team1885.gui.widget.voltometer.skins.VoltometerSkin;

import javafx.scene.control.Control;
import javafx.scene.control.Skin; 

public class Defenses extends Control {
	
	private String pos2, pos3, pos4, pos5; 
	
	public Defenses()
	{
		Scanner scan = null; 
		try{
			scan = new Scanner (System.in); 
			System.out.println("What are the defenses? Please use a new line for each:");
			pos2 = scan.nextLine(); 
			pos3 = scan.nextLine(); 
			pos4 = scan.nextLine(); 
			pos5 = scan.nextLine(); 
		}
		catch (Exception e){
			System.out.println("Your exception was:" + e);
		}
		finally{
			if (scan != null)
			{
				scan.close(); 
			}
		}
	}
	public String getPos2(){
		return pos2; 
	}
	public String getPos3(){
		return pos3; 
	}
	public String getPos4(){
		return pos4; 
	}
	public String getPos5(){
		return pos5; 
	}
	
	public void setPos2( String pos2 ){
		this.pos2 = pos2; 
	}
	public void setPos3( String pos3 ){
		this.pos3 = pos3; 
	}
	public void setPos4( String pos4 ){
		this.pos4 = pos4; 
	}
	public void setPos5( String pos5){
		this.pos5 = pos5; 
	}
	
	public Skin<Defenses> createDefaultSkin()
	{
		return new DefensesSkin(this); 
	}
}
