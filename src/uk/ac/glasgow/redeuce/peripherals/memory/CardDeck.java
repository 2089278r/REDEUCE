package uk.ac.glasgow.redeuce.peripherals.memory;

import java.util.ArrayList;
import java.util.List;

public abstract class CardDeck {
	List<Card> cards = new ArrayList<Card>();
	int positionInDeck;
	
	public CardDeck(){
		this.cards = new ArrayList<Card>();
		this.positionInDeck = 0;
	}
	
	public Card getNextCard(){
		return cards.get(++positionInDeck);
	}
	
	public void addCard(Card card){
		cards.add(card);
	}
}
