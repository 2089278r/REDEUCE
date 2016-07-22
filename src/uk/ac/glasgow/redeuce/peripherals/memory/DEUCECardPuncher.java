package uk.ac.glasgow.redeuce.peripherals.memory;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DEUCECardPuncher {
	String filename = "cardsPunched.txt";
	File punchedDeck;
	boolean isOn;
	
	public DEUCECardPuncher(){
		this.punchedDeck = new File(filename);
		this.isOn = false;
	}
	public void punch(String output) {
		if(isOn){
	       try {
	           FileWriter writer = new FileWriter(filename, true);
	           writer.append(output + "\n");
	           writer.close();
	        } catch (IOException e) {
	            System.err.println("Problem writing to the file");
	        }
		}
	 }
	public void turnOn() throws IOException{
		this.isOn = true;
		FileWriter writer = new FileWriter(filename);
		writer.write("");
		writer.close();
	}
}
