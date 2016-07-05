package uk.ac.glasgow.redeuce.peripherals.memory;

import java.util.BitSet;

public class CardLine {
	BitSet bits;
	
	public CardLine(){
		bits = new BitSet(35);
	}
	
	public void changeBit(int index, boolean b){
		this.bits.set(index, b);
	}
}
