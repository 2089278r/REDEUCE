package uk.ac.glasgow.redeuce.userinterface;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.Before;

import uk.ac.glasgow.redeuce.memory.Word;
import uk.ac.glasgow.redeuce.peripherals.CRDFileReader;
import uk.ac.glasgow.redeuce.peripherals.OutOfCardsException;
import uk.ac.glasgow.redeuce.processor.Processor;
import uk.ac.glasgow.redeuce.userinterface.console.ConsoleDisplay;
import uk.ac.glasgow.redeuce.userinterface.console.Console.stopKey;

public class ParsedConsole {

	private static Scanner input;
	private PrintStream out;
	private InputStream in;
	private static Parser myParser;
	private static ConsoleDisplay myDisplay;
	
		public ParsedConsole() throws IOException{
	
		//Stream to send commands to the parser
		PipedOutputStream temp = new PipedOutputStream();
		InputStream parserIn = new PipedInputStream(temp);
		this.out = new PrintStream(temp);
		
		
		//Stream to send responses from the parser
		PipedOutputStream parserOut = new PipedOutputStream();
		this.in = new PipedInputStream(parserOut);
	
		myParser = new Parser(parserIn, parserOut, new Processor());
		
		input = new Scanner(this.in);
	
	}
		
		private static boolean execute(String command) throws InterruptedException, IOException, OutOfCardsException{
		    myDisplay.update();
		    return true;
		}
	
		public static void menu() throws InterruptedException, IOException, OutOfCardsException{
		    String command;
		    boolean running = true;

		    while(running)
		    {
		        displayMenu();
		        command = getCommand();
		        running = execute(command);
		    }
		}

		private static void displayMenu(){
		    System.out.println("            REDEUCE program         ");
		    System.out.println("=========================================");
		    System.out.println("|Initial Input...................INIT_IN|");
		    System.out.println("|Step in the program.................RUN|");
		    System.out.println("|Load in a deck of cards......LOAD_CARDS|");
		    System.out.println("|Switch TCA setting..................TCA|");
		    System.out.println("|Switch TCB setting..................TCB|");
		    System.out.println("|Press Release Key...............RELEASE|");
		    System.out.println("|Change ID.....................SWITCH_ID|");
		    System.out.println("|Change OS.....................SWITCH_OS|");
		    System.out.println("|Stop the machine....................OFF|");
		    System.out.println("|Set Stop Key....................STOPKEY|");
		    System.out.println("|Pause when ID..............STOP_REQUEST|");
		    System.out.println("|Clear all of memory..........FULL_CLEAR|");
		    System.out.println("|Take 1-10 steps...........ONE_SHOT_DIAL|");
		    System.out.println("|Press One Shot up or down......ONE_SHOT|");
		    System.out.println("|Clear OS Lamps.................CLEAR_OS|");
		    System.out.println("|Clear ID Lamps.................CLEAR_ID|");
		    System.out.println("|Select a delay line..........DELAY_LINE|");
		    System.out.println("|Change status of NIS check...CHANGE_NIS|");
		    System.out.println("|Ready card puncher..........START_PUNCH|");
		    System.out.println("=========================================");
		}
		
		private static String getCommand(){
		    System.out.println("Enter the command of the function you wish to use: ");
		    String command = input.nextLine();
		    return command;
		}
		
		public static void main(String args[]) throws InterruptedException, IOException, OutOfCardsException{
			System.out.println("HEY");
			Thread parser = new Thread(myParser);
			parser.start();
			menu();
		}
	
}
