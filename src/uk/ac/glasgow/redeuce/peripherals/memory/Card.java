package uk.ac.glasgow.redeuce.peripherals.memory;

public class Card {
	private CardLine[] lines;
	private int positionInCard;
	
	public Card(){
		lines = new CardLine[12];
		positionInCard = 0;
	}
	
	public CardLine getNextLine(){
		return lines[++positionInCard];
	}
	
	public void changeLine(int index, CardLine cardline){
		lines[index] = cardline;
	}
	
	public int getSize(){
		return lines.length;
	}

}