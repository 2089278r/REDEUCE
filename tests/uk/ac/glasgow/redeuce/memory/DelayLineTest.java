package uk.ac.glasgow.redeuce.memory;

import static org.junit.Assert.*;

import java.util.BitSet;

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
		for (int i=0; i < 32 ; i++){ 
			assertEquals(dl.contents[i].getElements(0, 31), 0);
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
		BitSet examplearray = new BitSet();
		examplearray.set(1);
		examplearray.set(2);
		examplearray.set(3);
		examplearray.set(5);
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
		BitSet examplearray = new BitSet();
		examplearray.set(1);
		Word instruction = new Word(examplearray);
		dl.write(instruction);
		for (int i=0; i<32; i++){
			dl.increment();
		}
		assertEquals(dl.read(), instruction);
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
