package uk.ac.glasgow.redeuce.peripherals;

import java.util.ArrayList;
import java.util.List;

/*
 * Abstract class representing a collection of cards.
 * Cards are added to decks, and next cards can be retrieved.
 */

public abstract class CardDeck {
	public List<Card> cards = new ArrayList<Card>();
	public int positionInDeck;
	
	public CardDeck(){
		this.cards = new ArrayList<Card>();
		this.positionInDeck = 0;
	}
	
	public Card getNextCard(){
		if (positionInDeck >= cards.size()){
			throw new IndexOutOfBoundsException("All out of cards!");
		}
		else{
			return cards.get(positionInDeck++);
		}
	}
	
	public void addCard(Card card){
		cards.add(card);
	}
}
