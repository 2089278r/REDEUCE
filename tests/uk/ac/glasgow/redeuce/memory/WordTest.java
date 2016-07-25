package uk.ac.glasgow.redeuce.memory;


import org.junit.Test;

public class WordTest {

	@Test
	public void intConstructorTest() {
		int example = 132456;
		Word testWord = new Word(example);
		for (int i=0; i<testWord.getBits().length(); i++){
			if(testWord.getBits().get(i) == false){
			//	System.out.print("0");
			}
			//else System.out.print("1");
		}
	}
	
	@Test
	public void maxValueTest(){
		int example = 2147483647;
		Word testWord = new Word(example);
		for (int i=0; i<testWord.getBits().length(); i++){
			if(testWord.getBits().get(i) == false){
				//System.out.print("0");
			}
			//else System.out.print("1");
		}
	}
	
	@Test
	public void toStringTest(){
		Word testWord = new Word(0);
		String output = testWord.toString();
		System.out.println(output);
	}
}

