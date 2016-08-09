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
	private static Scanner outputScan;
	private static Scanner myInput;
	static PrintStream out;
	static InputStream in;
	private static Parser myParser;
	private static ConsoleDisplay myDisplay;
		
//		private static boolean execute(String command) throws InterruptedException, IOException, OutOfCardsException{
//			while(!command.equals("OFF")){
//				int expectedResponse = myParser.processCommand(command);
				//System.out.println(expectedResponse);
//				if(command.equals("ONE_SHOT")){
//					System.out.println("Please enter which cards you'll enter: ");
//					String direction = myInput.nextLine();
//					out.println("ONE_SHOT " + direction);
//					expectedResponse = 1200;
//				}
//				else if(command.equals("LOAD_CARDS")){
//					System.out.println("Please enter which cards you'll enter: ");
//					String deck = myInput.nextLine();
//					out.println("LOAD_CARDS " + deck);
//					expectedResponse = 1;
//				}
//				else if(command.equals("ONE_SHOT_DIAL")){
//					System.out.println("Please enter number on dial: ");
//					int dial = myInput.nextInt();
//					out.println("ONE_SHOT_DIAL " + dial);
//					expectedResponse = 3*dial;
//				}
//				else if(command.equals("STOPKEY")){
//					System.out.println("UP, LEVEL, or DOWN?");
//					String setting = myInput.nextLine();
//					out.println("STOPKEY " + setting);
//					expectedResponse = 1;
//				}
//				else if(command.equals("START_PUNCH")){
//					out.println(command);
//					expectedResponse = 1;
//				}
//				else if(command.equals("INIT_IN")){
//					out.println(command);
//					expectedResponse = 3;
//				}
//				else if(command.equals("RUN")){
//					out.println(command);
//					expectedResponse = 3;
//				}
//				else {
//					out.println(command);
//					expectedResponse = 2;
//				}
//				
//				for(int i=0; i<expectedResponse; i++){
//					System.out.println(outputScan.nextLine());
//				}
//				
//				myDisplay.update();
//				return true;
//			}
//		    return false;
//		}
	
		public static void menu() throws InterruptedException, IOException{
		   // String command;
		    boolean running = true;
		    Thread parser = new Thread(myParser);
			parser.start();
		    while(running)
		    {
		        displayMenu();
		        System.out.println("Enter the command of the function you wish to use: ");
		        int numberOfOutputs = myParser.processCommand(myInput);
		        System.out.println(numberOfOutputs);
		        if(numberOfOutputs == -1){
		        	for(int i=0; i<3; i++){
						System.out.println(outputScan.nextLine());
					}
		        	running = false;
		        }
		        else{
		        	  for(int i=0; i<numberOfOutputs; i++){
		        		  while (!outputScan.hasNextLine()) {}
		        		String output = outputScan.nextLine();
		        		//System.out.println("yup!");
						System.out.println(output);
					}
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
		    System.out.println("|Press One Shot up or down......ONE_SHOT|");
		    System.out.println("|Clear OS Lamps.................CLEAR_OS|");
		    System.out.println("|Clear ID Lamps.................CLEAR_ID|");
		    System.out.println("|Select a delay line..........DELAY_LINE|");
		    System.out.println("|Change status of NIS check...CHANGE_NIS|");
		    System.out.println("|Ready card puncher..........START_PUNCH|");
		    System.out.println("=========================================");
		}
		
//		private static String getCommand(){
//		    System.out.println("Enter the command of the function you wish to use: ");
//		    String command = myInput.nextLine();
//		    return command;
//		}
		
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
