package uk.ac.glasgow.redeuce.peripherals.memory;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CRDFileReader {
	String filename = "";
	
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
