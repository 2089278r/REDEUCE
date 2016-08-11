package uk.ac.glasgow.redeuce.peripherals;

/*
 * Represents a card to be read into the machine
 * They are read line-by-line, so appropriate methods are provided
 * so that cards aren't read further than the lines they contain.
 */

public class Card {
	private CardLine[] lines;
	private int positionInCard;
	
	public Card(){
		lines = new CardLine[12];
		positionInCard = 0;
	}
	
	public CardLine getNextLine(){
		if (onLastLine()) return null;
		return lines[positionInCard++];
	}
	
	public void changeLine(int index, CardLine cardline){
		lines[index] = cardline;
	}
	
	public int getSize(){
		return lines.length;
	}
	
	public boolean onLastLine(){
		return (positionInCard >= lines.length);
	}

}
