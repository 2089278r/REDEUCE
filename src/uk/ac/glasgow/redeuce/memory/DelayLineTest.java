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
	public void wordContentValue() {
		DelayLine dl = new DelayLine();
		for (int i=0; i < 32 ; i++){ 
			assertEquals("00000000000000000000000000000000", dl.contents[i]);
		}
	}
	
	@Test
	public void wordWrite() {
		DelayLine dl = new DelayLine();
		for (int i=0; i<32; i++){
			dl.counter++;
			dl.write("1");
		}
		for (int i=0; i<32; i++){
			assertEquals("1", dl.contents[i]);
		}
	}
	
	@Test
	public void wordRead() {
		DelayLine dl = new DelayLine();
		dl.counter++;
		String wordValue = dl.read();
		assertEquals(wordValue, dl.contents[1]);
	}
	
	@Test
	public void wordWriteThenRead() {
		DelayLine dl = new DelayLine();
		dl.counter++;
		dl.write("1");
		String wordValue = dl.read();
		assertEquals("1", wordValue);
	}
	
	@Test
	public void wordWriteThenReadPrevious() {
		DelayLine dl = new DelayLine();
		dl.counter = 3;
		dl.write("1");
		dl.counter--;
		String wordValue = dl.read();
		assertNotEquals("1", wordValue);
	}
	
	@Test
	public void writeSucceedsModulo() {
		DelayLine dl = new DelayLine();
		dl.counter = 35;
		dl.write("1");
		assertEquals("1", dl.contents[3]);
	}
	
	@Test
	public void readAlwaysSuccessful() {
		DelayLine dl = new DelayLine();
		for (int i=0; i<100; i++){
			dl.counter++;
			String valueHeld = dl.read();
			assertNotNull(valueHeld);
		}
	}

}
