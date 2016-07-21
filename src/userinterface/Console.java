package userinterface;

import java.io.IOException;
import java.util.Scanner;

import uk.ac.glasgow.redeuce.peripherals.memory.CRDFileReader;
import uk.ac.glasgow.redeuce.peripherals.memory.OutOfCardsException;
import uk.ac.glasgow.redeuce.processor.Instruction;
import uk.ac.glasgow.redeuce.processor.Processor;

public class Console {
	
	private Display myDisplay;
	private Scanner myScanner;
	private Processor myProc;

	public Console(){
	    myScanner = new Scanner(System.in);
	    myProc = new Processor();
	    myDisplay = new Display(myProc.getMemory());
	}

	public void menu() throws InterruptedException, IOException, OutOfCardsException{
	    String command;
	    boolean running = true;

	    while(running)
	    {
	        displayMenu();
	        command = getCommand();
	        running = execute(command);
	    }
	}

	private void displayMenu(){
	    System.out.println("            REDEUCE program         ");
	    System.out.println("=========================================");
	    System.out.println("|Initial Input...................INIT_IN|");
	    System.out.println("|Step in the program................STEP|");
	    System.out.println("|Load in a deck of cards......LOAD_CARDS|");
	    System.out.println("|Switch TCA setting..................TCA|");
	    System.out.println("|Switch TCB setting..................TCB|");
	    System.out.println("|Change ID.....................SWITCH_ID|");
	    System.out.println("|Change OS.....................SWITCH_OS|");
	    System.out.println("|Request a stop.....................STOP|");
	    System.out.println("|Clear all of memory..........FULL_CLEAR|");
	    System.out.println("|Take 1-10 steps................ONE_SHOT|");
	    System.out.println("|Clear OS Lamps.................CLEAR_OS|");
	    System.out.println("|Clear ID Lamps.................CLEAR_ID|");
	    System.out.println("|Select a delay line..........DELAY_LINE|");
	    System.out.println("|Ready card puncher..........START_PUNCH|");
	    System.out.println("=========================================");
	}

	private boolean execute(String command) throws InterruptedException, IOException, OutOfCardsException{
	    if(command.equals("STEP"))
	    {
	        myProc.step();
	        Thread.sleep(100);
	    }
	    else if(command.equals("LOAD_CARDS"))
	    {
	    	System.out.println("Please give file name or other form of card input:");
	    	String deck = myScanner.nextLine();
	    	CRDFileReader reader = new CRDFileReader(deck);
	    	myProc.cardLoad(reader.createNewDeck());	
	    }
	    else if(command.equals("INIT_IN"))
	    {
	    	myProc.initialInput();
	    }
	    else if(command.equals("ONE_SHOT"))
	    {
	    	System.out.println("Enter number of single shots: ");
	    	int shots = myScanner.nextInt();
	    	for (int i=0; i<shots; i++){
	    		myProc.step();
	    		myDisplay.update();
	    		Thread.sleep(100);
	    	}
	    }
	    else if(command.equals("STOP"))
	    {
	    	return false;
	    }
	    else if(command.equals("START_PUNCH"))
	    {
	    	myProc.turnOnPunch();
	    }
	    else{
	    	System.out.println("Sorry! Either incorrect format or that instruction isn't implemented yet!");
	    }
	    myDisplay.update();
	    return true;
	}

	private String getCommand(){
	    System.out.println("Enter the command of the function you wish to use: ");
	    String command = myScanner.nextLine();
	    return command;
	}

}
