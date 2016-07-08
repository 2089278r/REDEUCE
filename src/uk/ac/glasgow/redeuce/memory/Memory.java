package uk.ac.glasgow.redeuce.memory;

public class Memory {
	MemoryUnit[] linesAndStores;
	
	public Memory(){
		//DL0 was actually DL8, so there is a +1 to make implementation easier later on...
		this.linesAndStores = new MemoryUnit[22]; 
		for (int i=1; i<=12; i++){
			linesAndStores[i] = new DelayLine();
		}
		for (int i=13; i<=16; i++){
			linesAndStores[i] = new SingleRegister();
		}
		for (int i=17; i<=19; i++){
			linesAndStores[i] = new DoubleRegister();
		}
		for (int i=20; i<=21; i++){
			linesAndStores[i] = new QuadRegister();
		}
	}
	
	public void increment(){
		for (int i=1; i<=linesAndStores.length; i++){
			linesAndStores[i].increment();
		}
	}
	
	public int getMicroCycle(){
		return ((linesAndStores[1].counter) % 32);
	}
	
	public Word getWord(int i){
		return linesAndStores[i].read();
	}
	
	public void setWord(int i, Word word){
		linesAndStores[i].write(word);
	}
}
