package uk.ac.glasgow.redeuce.userinterface;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import uk.ac.glasgow.redeuce.processor.Processor;
import uk.ac.glasgow.redeuce.userinterface.console.Console;

public class ParserTest {

	PrintStream out;
	InputStream in;
	Parser myParser;
	
	@Before public void initialise() throws IOException{
	
		//Stream to send commands to the parser
		PipedOutputStream temp = new PipedOutputStream();
		InputStream parserIn = new PipedInputStream(temp);
		this.out = new PrintStream(temp);
		
		//Stream to send responses from the parser
		PipedOutputStream parserOut = new PipedOutputStream();
		this.in = new PipedInputStream(parserOut);
	
		this.myParser = new Parser(parserIn, parserOut, new Processor());
		
		
	}

	@Test
	public void test() {
		Thread parser = new Thread(this.myParser);
		
		parser.start();
		System.out.println("waits forever?");
		Scanner testScan = new Scanner(this.in);
		out.println("SWITCH_ID 0 1");
		System.out.println(testScan.next());
		testScan.close();
	}

}
