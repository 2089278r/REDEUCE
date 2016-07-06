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
	
	//Was originally (++positionInDeck to make it more like its namesake,
	//but it's probably better if we just have the InitialInput Key or whatever
	//other functionality gets implemented to call this function also as a way to call
	//the first card in a Deck. If this becomes troublesome it can be changed!
	public Card getNextCard() throws OutOfCardsException{
		if (positionInDeck == cards.size()){
			throw new OutOfCardsException("All out of cards!");
		}
		else{
			return cards.get(positionInDeck++);
		}
	}
	
	public void addCard(Card card){
		cards.add(card);
	}
}
