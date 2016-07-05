package uk.ac.glasgow.redeuce.peripherals.memory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CRDFileReader {
	String filename = "";
	
	CRDFileReader(String filename){
		this.filename = filename;
	}
	

public FixedCardDeck createNewDeck() throws IOException{
	BufferedReader reader = new BufferedReader(new FileReader(filename));
	String currentLine = reader.readLine();
	FixedCardDeck newDeck = new FixedCardDeck();
	//This seems awfully messy, not sure if we should hardcode the integer 12 or have
	//a different statement which says something more like "until blank line reached"...
	while(currentLine!=null){
		Card newCard = new Card();
		for (int i=0; i<12; i++){
			newCard.changeLine(i, stringToBits(currentLine));
			currentLine=reader.readLine();
		}
		newDeck.addCard(newCard);
		currentLine = reader.readLine();
	}
	reader.close();
	return newDeck; 
	}
	
public CardLine stringToBits(String fileLine){
	char[] fileElements = fileLine.toCharArray();
	CardLine newCardLine = new CardLine();
	for (int i=0; i<35; i++){
		newCardLine.changeBit(i, fileElements[i] == '1');
	}
	System.out.println(newCardLine.toString());
	return newCardLine;
	}
}
