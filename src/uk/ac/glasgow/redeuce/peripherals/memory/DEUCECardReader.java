package uk.ac.glasgow.redeuce.peripherals.memory;

import uk.ac.glasgow.redeuce.memory.Word;

public class DEUCECardReader {
	FixedCardDeck deck;
	Card[] cardsBeingRead;  //Something to simulate the different states the reader can be in?
	boolean isEmpty;
	boolean readyToRead;    //Methods from Console, and when instructions are being executed can manipulate this
	
	public DEUCECardReader(){
		this.cardsBeingRead = new Card[3];
		this.isEmpty = true;
		this.readyToRead = false;
	}
	
	public void loadDeck(FixedCardDeck deck){
		this.deck = deck;
		this.isEmpty = false;
	}
	
	//The actual reading bit, I assume?
	//Should this read 3 cards at a time(unless an interrupt from Console..?)
	//and return an Array which is then sent to a Delay Line?
	//This function I suppose should be called by Console?
	public Word[] interpretCards(){
		//while (!isEmpty() && !isWaiting())
		    //getNextCard(x 3?)
		return null;
	}
}
