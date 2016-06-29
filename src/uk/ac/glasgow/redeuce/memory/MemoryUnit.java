package uk.ac.glasgow.redeuce.memory;

public abstract class MemoryUnit {
	
	int counter = 0; //in this class for testing purposes
	String[] contents;
	//changes the value of the counter... Should it be here?
	void increment(){
		counter++;
	}
	
	void write(String word){
		
	}
	
	String read(){
		return null;
	}
	
}
