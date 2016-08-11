package uk.ac.glasgow.redeuce.peripherals;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * A class representing the card Puncher of the machine. Currently just used
 * to write into a fixed file. Only created when the START_PUNCH command is called
 * from the processor
 */

public class DEUCECardPuncher {
	private String filename = "cardsPunched.txt";
	private File punchedDeck;
	boolean isOn;
	
	public DEUCECardPuncher(){
		this.setPunchedDeck(new File(filename));
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
	public File getPunchedDeck() {
		return punchedDeck;
	}
	public void setPunchedDeck(File filename) {
		this.punchedDeck = filename;
	}
}
