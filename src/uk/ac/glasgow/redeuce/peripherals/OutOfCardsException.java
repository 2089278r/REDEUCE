package uk.ac.glasgow.redeuce.peripherals;

public class OutOfCardsException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OutOfCardsException(){
		
	}
	
	public OutOfCardsException(String message){
		super(message);
	}

}
