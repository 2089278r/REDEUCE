package uk.ac.glasgow.redeuce.peripherals.memory;

import java.util.BitSet;

import uk.ac.glasgow.redeuce.memory.Memory;
import uk.ac.glasgow.redeuce.memory.Word;

public class DEUCECardReader {
	FixedCardDeck deck;
	Card[] triad;  //Something to simulate the different states the reader can be in?
	Memory deuceMemory; //Testing purposes!!!
	boolean isEmpty;
	boolean readyToRead;    //Methods from Console, and when instructions are being executed can manipulate this
	boolean isReading;
	
	public DEUCECardReader(){
		this.triad = new Card[3];
		this.isEmpty = true;
		this.readyToRead = false;
		this.deuceMemory = new Memory();
	}
	
	public void loadDeck(FixedCardDeck deck){
		this.deck = deck;
		this.isEmpty = false;
	}
	
	
	
	public void takeInCards() throws OutOfCardsException{
		for (int i=0; i<triad.length; i++){
			triad[i] = deck.getNextCard();
		}
		while (!((deuceMemory.getMicroCycle())==0)){
			deuceMemory.increment();
		}
		this.isEmpty=false;
	}
	
	public int getDelayLine(Card card){
		CardLine cardline;
		int delayLine = 0;
		cardline = card.getNextLine();
		cardline = card.getNextLine();
		BitSet destination = cardline.bits.get(1, 4);    // Gets NIS from the 2nd card, where the DL should be specified
		if (destination.isEmpty()){
			delayLine = 8;
		}
		else{
			for (int i=0; i<destination.length(); i++){
				if (destination.get(i) == true){
					delayLine += (java.lang.Math.pow(2, i));
				}
			}
		}
		return delayLine;
		
	}
	
	
	//As it is, this function prints out the right things we want written into memory
	//though to avoid hard-coding the places in the DLs where things will be written,
	//I'll need to get more used to how integers and the like get "passed around"
	//when methods are called within other methods...
	public void readFirstCard(){
		Card card = triad[0];
		int delayLine = getDelayLine(card);
		CardLine currentLine = card.getNextLine();
		currentLine = card.getNextLine();
		currentLine = card.getNextLine();
		while (currentLine != null){
			deuceMemory.setWord(delayLine, currentLine.bits);
			currentLine = card.getNextLine();
		}
	}
	//The actual reading bit, I assume?
	//Should this read 3 cards at a time(unless an interrupt from Console..?)
	//public void readTriad() {
//		if(isEmpty){
//			System.out.println("Something went wrong...");
//		}
//		else{
//			int delayLineMicroCycle = 0;	
//			}
//		}
//	}
}
