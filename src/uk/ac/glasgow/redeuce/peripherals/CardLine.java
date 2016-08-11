package uk.ac.glasgow.redeuce.peripherals;

import java.util.BitSet;

/*
 * Represents each line of a card, used mostly to convert them to and from Words
 */

public class CardLine {
	private BitSet bits;
	
	public CardLine(){
		this.bits = new BitSet(35);
	}
	
	public void changeBit(int index, boolean b){
		bits.set(index, b);
	}
	
	public BitSet getBits(){
		return this.bits;
	}
}
