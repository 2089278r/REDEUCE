package uk.ac.glasgow.redeuce.peripherals.memory;

import uk.ac.glasgow.redeuce.memory.Word;

public class DEUCECardReader {
	FixedCardDeck deck;
	Card[] cardsBeingRead;  //Something to simulate the different states the reader can be in?
	boolean isEmpty;
	boolean readyToRead;    //Methods from Console, and when instructions are being executed can manipulate this
	boolean isReading;
	
	public DEUCECardReader(){
		this.cardsBeingRead = new Card[3];
		this.isEmpty = true;
		this.readyToRead = false;
	}
	
	public void loadDeck(FixedCardDeck deck){
		this.deck = deck;
		this.isEmpty = false;
	}
	
	public void takeInCards() throws OutOfCardsException{
		for (int i=0; i<cardsBeingRead.length; i++){
			cardsBeingRead[i] = deck.getNextCard();
		}
	}
	
	//The actual reading bit, I assume?
	//Should this read 3 cards at a time(unless an interrupt from Console..?)
	public Word readCard() {
		
		return null;
	}
}
