package uk.ac.glasgow.redeuce.peripherals.memory;

public class Card {
	public CardLine[] lines;
	private int positionInCard;
	
	public Card(){
		lines = new CardLine[12];
		positionInCard = 0;
	}
	
	public CardLine getNextLine(){
		if (positionInCard>=lines.length) return null;
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
