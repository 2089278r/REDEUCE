package uk.ac.glasgow.redeuce.peripherals.memory;

import java.util.BitSet;

import uk.ac.glasgow.redeuce.memory.Memory;
import uk.ac.glasgow.redeuce.memory.Word;

public class DEUCECardReader {
	FixedCardDeck deck;
	Triad triad;  //Something to simulate the different states the reader can be in?
	Card currentCard;
	CardLine currentLine;
	boolean isEmpty;
	boolean readyToRead;    //Methods from Console, and when instructions are being executed can manipulate this
	boolean isReading;
	
	public DEUCECardReader(){
		this.isEmpty = true;
		this.readyToRead = false;
	}
	
	public void loadDeck(FixedCardDeck deck){
		this.deck = deck;
		this.isEmpty = false;
	}
	
	public void takeInCards() throws OutOfCardsException {
		try {
			this.triad = new Triad(deck.getNextCard(), deck.getNextCard(), deck.getNextCard());
			this.isEmpty=false;
		} catch (OutOfCardsException e) {
			e.printStackTrace();
			this.isEmpty = true;
		}
	}
	
	public CardLine readLine(){
		return this.currentLine;
	}
	
	public void readyNextLine() throws OutOfCardsException{
		if (currentCard.onLastLine()){
			this.currentCard = this.triad.getNext();
		}
		this.currentLine = currentCard.getNextLine();
	}
	
	public Triad getTriad(){
		return this.triad;
	}
	
	public boolean isEmpty(){
		return this.isEmpty();
	}
}
