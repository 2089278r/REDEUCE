package uk.ac.glasgow.redeuce.memory;

/*
 *Class representing the registers in memory which can hold up to 4 words at a time 
 */

public class QuadRegister extends Register{
	
	private static final int SIZE = 4;
	
	public QuadRegister(){
		super(SIZE);
	}

}
