package uk.ac.glasgow.redeuce.memory;

/*
 * Represents Double Stores, each containing 2 32-bit words 
 */

public class DoubleRegister extends Register{

	private static final int SIZE= 2;
	
	public DoubleRegister(){
		super(SIZE);
	}

}
