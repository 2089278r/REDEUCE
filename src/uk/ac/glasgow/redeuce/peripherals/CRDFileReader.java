package uk.ac.glasgow.redeuce.peripherals;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/*
 * A class which creates an "actual" deck of cards from a file
 * which must be written in the correct format.
 * They are read in as strings and then converted into Bits 
 * to make them easier to turn into Words in Memory
 */

public class CRDFileReader {
	private String filename;
	
	public CRDFileReader(String filename){
		this.filename = filename;
	}
	

public FixedCardDeck createNewDeck() throws IOException{
	File file = new File (filename);
	Scanner reader = new Scanner(file);
	String currentLine;
	FixedCardDeck newDeck = new FixedCardDeck();
	while(reader.hasNextLine()){
		currentLine = reader.nextLine().replaceAll("\\s", "");
		
		Card newCard = new Card();
		int i = 0;
		while (i < newCard.getSize()){
			newCard.changeLine(i, stringToBits(currentLine));
			if(reader.hasNextLine()){
				currentLine = reader.nextLine().replaceAll("\\s", "");
			}
			i++;
		}
		newDeck.addCard(newCard);
	}
	reader.close();
	return newDeck; 
	}
	
public CardLine stringToBits(String fileLine){
	char[] fileElements = fileLine.toCharArray();
	CardLine newCardLine = new CardLine();
	for (int j=0; j < fileElements.length; j++){
		newCardLine.changeBit(j, fileElements[j] == '1');
	}
	return newCardLine;
	}
}
