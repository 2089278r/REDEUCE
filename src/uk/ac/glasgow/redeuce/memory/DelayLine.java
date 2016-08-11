package uk.ac.glasgow.redeuce.memory;

/* Represents the mercury delay lines in the DEUCE, 
 * which each contain 32 32-bit words, each initialised words of just 0
 */

public class DelayLine extends MemoryUnit{
	
	private static final int size = 32;
		
	public DelayLine(){
		super(size);
	}
}
