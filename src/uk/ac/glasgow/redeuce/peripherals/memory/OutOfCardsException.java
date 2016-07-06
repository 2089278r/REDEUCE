package uk.ac.glasgow.redeuce.peripherals.memory;

public class OutOfCardsException extends Exception {
	
	public OutOfCardsException(){
		
	}
	
	public OutOfCardsException(String message){
		super(message);
	}

}
