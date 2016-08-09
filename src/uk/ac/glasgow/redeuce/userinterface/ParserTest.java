package uk.ac.glasgow.redeuce.userinterface;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
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
		Scanner testScan = new Scanner(this.in);
		Scanner userInput = new Scanner(System.in);
		String userIn = userInput.nextLine();
		while(!userIn.equals("OFF")){
			System.out.println(userIn);
			out.println(userIn);
			//System.out.println("got a command but I don't do anything with it");
			int expectedResponse;
			if(userIn.equals("ONE_SHOT Up")){
				expectedResponse = 1200;
			}
			else if(userIn.equals("INIT_IN")){
				expectedResponse = 3;
			}
			else expectedResponse = 1;
			for(int i=0; i<expectedResponse; i++){
				System.out.println(testScan.nextLine());
			}
			System.out.println("next command?");
			userIn = userInput.nextLine();
		}
		testScan.close();
		userInput.close();
	}
	
	@Test
	public void stopTest() {
		Thread parser = new Thread(this.myParser);
		parser.start();
		Scanner testScan = new Scanner(this.in);
		out.println("OFF");
		System.out.print(testScan.nextLine());
		testScan.close();
	}
	
	@Test
	public void multipleCommandsTest() throws FileNotFoundException {
		Thread parser = new Thread(this.myParser);
		parser.start();
		Scanner testScan = new Scanner(this.in);
		out.println("LOAD_CARDS squaresProgram.txt");
		out.println("INIT_IN");
		out.println("DELAY_LINE 1");
		out.println("STOPKEY LEVEL");
		out.println("RUN");
		out.println("OFF");
		while(testScan.hasNext()){
			System.out.println(testScan.nextLine());
		}
		testScan.close();
	}
	
	@Test
	public void memOutputTest(){
		Thread parser = new Thread(this.myParser);
		parser.start();
		Scanner testScan = new Scanner(this.in);
		out.println("LOAD_CARDS squaresProgram.txt");
		System.out.println(testScan.nextLine());
		out.println("INIT_IN");
		System.out.println(testScan.nextLine());
		System.out.println(testScan.nextLine());
		System.out.println(testScan.nextLine());
		out.println("OFF");
		System.out.println(testScan.nextLine());
		System.out.println(testScan.nextLine());
		System.out.println(testScan.nextLine());
		testScan.close();
	}

}
