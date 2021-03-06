package uk.ac.glasgow.redeuce.peripherals;

/*
 * Class which represents the card reader of the machine.
 * Given a deck, it reads through each card line-by-line,
 * taking in 3 cards at a time, or a "triad".
 */

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
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			this.isEmpty = true;
			this.triad = null;
		}
	}
	
	public CardLine readLine(){
		return this.currentLine;
	}
	
	public void readyNextLine() throws OutOfCardsException{
		if (currentCard.onLastLine()){
			try{
				this.currentCard = this.triad.getNext();
			} catch (OutOfCardsException e){
				this.isEmpty = true;
				return;
			}
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
