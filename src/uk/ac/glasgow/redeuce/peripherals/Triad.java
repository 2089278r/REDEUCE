package uk.ac.glasgow.redeuce.peripherals;

/*
 * Represents 3 cards which get read in as a group by the cardReader.
 * Also provides a method to get the Delay line that a triad is meant to be read into, 
 * as the first 4 lines of the first card in a Triad ought to be used to ready a Delay 
 * Line to take in the words of the triad.
 */

import java.util.BitSet;

public class Triad {
	private Card[] triad;  //Something to simulate the different states the reader can be in?
	int delayLine;
	private int positionInTriad;
	int size = (3 - positionInTriad);
	
	public Triad(){
		this.triad = new Card[3];
		this.positionInTriad = 0;
	}
	public Triad(Card card1, Card card2, Card card3){
		this.triad = new Card[3];
		this.triad[0] = card1;
		this.triad[1] = card2;
		this.triad[2] = card3;
		this.positionInTriad = 0;
	}
	
	//public void setCard(){
	public int getDelayLine(){
		Card firstCard = getCurrentCard();
		CardLine currentLine;
		this.delayLine = 0;
		currentLine = firstCard.getNextLine();
		currentLine = firstCard.getNextLine();
		BitSet destination = currentLine.getBits().get(1, 4);    // Gets NIS from the 2nd card, where the DL should be specified
		if (destination.isEmpty()){
			this.delayLine = 8;
		}
		else{
			for (int i=0; i<destination.length(); i++){
				if (destination.get(i) == true){
					this.delayLine += (java.lang.Math.pow(2, i));
				}
			}
		}
		currentLine = firstCard.getNextLine();
		currentLine = firstCard.getNextLine();
		return this.delayLine;
	}
	
	public void setCard(int position, Card card){
		if(card == null){
			return;
		}
		this.triad[position] = card;
	}
	public Card getCurrentCard(){
		if (positionInTriad > 2){
			return null;
		}
		else return triad[positionInTriad];	}

	public Card getNext() throws OutOfCardsException{
		if (positionInTriad > 2){
			return null;
		}
		else return triad[positionInTriad++];
	}
	
	public boolean onLastCard(){
		return (size == 1);
	}
	
	public int getPosition(){
		return this.positionInTriad;
	}
}
