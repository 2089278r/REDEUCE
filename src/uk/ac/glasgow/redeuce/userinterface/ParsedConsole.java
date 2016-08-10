package uk.ac.glasgow.redeuce.userinterface;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import uk.ac.glasgow.redeuce.processor.Processor;
import uk.ac.glasgow.redeuce.userinterface.console.ConsoleDisplay;

public class ParsedConsole {
	private static Scanner outputScan;
	private static Scanner myInput;
	static PrintStream out;
	static InputStream in;
	private static Parser myParser;
	private static ConsoleDisplay myDisplay;
	
		public static void menu() throws InterruptedException, IOException{
			String command;
			int numberOfOutputs;
		    boolean running = true;
		    Thread parser = new Thread(myParser);
			parser.start();
		    while(running)
		    { 
		    	displayMenu();
		    	command = getCommand();
		    	numberOfOutputs = myParser.getNumberOfOutputs(command);
		        out.println(command);
		        if(!(numberOfOutputs == -1)){
		        	for(int i=0; i<numberOfOutputs; i++){
		        		String output = outputScan.nextLine();
						System.out.println(output);
		        	}
		        }
		        else{
		        	for(int i=0; i<3; i++){
						System.out.println(outputScan.nextLine());
					}
		        	running = false;
		        }
		        myDisplay.update();
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
		    System.out.println("|Press One Shot up...........ONE_SHOT_UP|");
		    System.out.println("|Press One Shot down.......ONE_SHOT_DOWN|");
		    System.out.println("|Clear OS Lamps.................CLEAR_OS|");
		    System.out.println("|Clear ID Lamps.................CLEAR_ID|");
		    System.out.println("|Select a delay line..........DELAY_LINE|");
		    System.out.println("|Change status of NIS check...CHANGE_NIS|");
		    System.out.println("|Ready card puncher..........START_PUNCH|");
		    System.out.println("=========================================");
		}
		
		private static String getCommand(){
		    System.out.println("Enter the command of the function you wish to use: ");
		    String command = myInput.nextLine();
		    return command;
		}
		
		public static void main(String args[]) throws InterruptedException, IOException{
			//Stream to send commands to the parser
			PipedOutputStream temp = new PipedOutputStream();
			InputStream parserIn = new PipedInputStream(temp);
			out = new PrintStream(temp);
			
			
			//Stream to send responses from the parser
			PipedOutputStream parserOut = new PipedOutputStream();
			in = new PipedInputStream(parserOut);
		
			myParser = new Parser(parserIn, parserOut, new Processor());
			myInput = new Scanner(System.in);
			outputScan = new Scanner(in);
			myDisplay = new ConsoleDisplay(myParser.myProc.getMemory());
			menu();
		}
	
}
