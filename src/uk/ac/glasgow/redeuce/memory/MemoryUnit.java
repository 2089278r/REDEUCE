package uk.ac.glasgow.redeuce.memory;

public abstract class MemoryUnit {
	
	int counter = 0; //in this class for testing purposes
	Word[] contents;
	//changes the value of the counter... Should it be here?
	void increment(){
		counter++;
	}
	
	void write(Word word){
		
	}
	
	Word read(){
		return null;
	}
	
}
