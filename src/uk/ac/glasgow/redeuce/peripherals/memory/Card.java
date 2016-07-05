package uk.ac.glasgow.redeuce.peripherals.memory;

public class Card {
	CardLine[] lines;
	int positionInCard;
	
	public Card(){
		lines = new CardLine[12];
		positionInCard = 0;
	}
	
	public CardLine getNextLine(){
		return lines[positionInCard++];
	}
	
	public void changeLine(int index, CardLine cardline){
		lines[index] = cardline;
	}

}
