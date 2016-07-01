package uk.ac.glasgow.redeuce.memory;

import static org.junit.Assert.*;

import org.junit.Test;

public class DelayLineTest {

	@Test
	public void wordContentTest() {
		DelayLine dl = new DelayLine();
		assertNotNull(dl.contents);
	}
	
	@Test
	public void wordContentLength() {
		DelayLine dl = new DelayLine();
		assertEquals(32, dl.contents.length);
	}
	
	@Test
	public void wordContentValueisZero() {
		DelayLine dl = new DelayLine();
		Word example = new Word();
		for (int i=0; i < 32 ; i++){ 
			assertEquals(example.binaryDigits[i], dl.contents[i].binaryDigits[i]);
		}
	}
	
	@Test
	public void wordWrite() {
		DelayLine dl = new DelayLine();
		int[] examplearray = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,1};
		for (int i=0; i<32; i++){
			dl.increment();
			dl.write(new Word(examplearray));
		}
		for (int i=0; i<32; i++){
			assertEquals(examplearray[i], dl.contents[i].binaryDigits[i]);
		}
	}
	
	@Test
	public void wordRead() {
		DelayLine dl = new DelayLine();
		dl.increment();
		Word wordValue = dl.read();
		assertEquals(wordValue, dl.contents[1]);
	}
	
	@Test
	public void wordWriteThenRead() {
		DelayLine dl = new DelayLine();
		dl.increment();
		dl.write(new Word());
		Word wordValue = dl.read();
		assertEquals(dl.contents[dl.counter], wordValue);
	}
	
	@Test
	public void wordWriteThenReadNext() {
		DelayLine dl = new DelayLine();
		int[] examplearray = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,1};
		Word instruction = new Word(examplearray);
		dl.write(instruction);
		dl.increment();
		Word wordValue = dl.read();
		assertNotEquals(instruction, wordValue);
	}
	
	@Test
	public void writeSucceedsModulo() {
		DelayLine dl = new DelayLine();
		for (int i=0; i<35; i++){
			dl.increment();
		}
		int[] examplearray = new int[] {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,1,1,1,1,1,0,1};
		Word instruction = new Word(examplearray);
		dl.write(instruction);
		assertEquals(dl.read().binaryDigits, examplearray);
	}
	
	@Test
	public void readAlwaysSuccessful() {
		DelayLine dl = new DelayLine();
		for (int i=0; i<100; i++){
			dl.increment();
			Word valueHeld = dl.read();
			assertNotNull(valueHeld);
		}
	}

}
