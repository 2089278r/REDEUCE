package uk.ac.glasgow.redeuce.memory;

public class SingleRegister extends Register {
	private static final int size = 1;
	
	public SingleRegister(){
		super(size);
	}
	
	//void write(Word word){
	//	contents[(counter % size)] = word;
	//}
	//Word read(){
	//	return contents[(counter % size)];
	//}
}
