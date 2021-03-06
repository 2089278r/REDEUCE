package uk.ac.glasgow.redeuce.userinterface.console;

/*
 * Used as a prototype text-based interface for the running of the machine.
 * Outputs memory states as our test-program is being ran, and asks the user for input.
 */

import java.io.IOException;
import java.util.BitSet;
import java.util.Scanner;

import uk.ac.glasgow.redeuce.memory.Word;
import uk.ac.glasgow.redeuce.peripherals.CRDFileReader;
import uk.ac.glasgow.redeuce.peripherals.OutOfCardsException;
import uk.ac.glasgow.redeuce.processor.Processor;

public class Console {
	
	private ConsoleDisplay myDisplay;
	private Scanner myScanner;
	private Processor myProc;
	private BitSet osLamps;
	private BitSet idLamps;
	public enum stopKey {
		UP, LEVEL, DOWN
	}
	//DISCRIM enum as well
	public enum EXT{
		REQUEST_STOP, NORMAL, EXTTREE
	}
	private stopKey status;
	private boolean atStop;
	private boolean nisOn;
	private boolean sourceOn;
	private boolean destOn;
	private BitSet nisSwitch;
	private BitSet destSwitch;
	private BitSet sourceSwitch;
	private EXT position;

	public Console(){
	    myScanner = new Scanner(System.in);
	    myProc = new Processor();
	    myDisplay = new ConsoleDisplay(myProc.getMemory());
	    osLamps = new BitSet(32);
	    idLamps = new BitSet(32);
	    status = stopKey.UP;
	    atStop = false;
	    nisOn = false;
	    nisSwitch = new BitSet(3);
	    sourceOn = false;
	    sourceSwitch = new BitSet(5);
	    destOn = false;
	    destSwitch = new BitSet(5);
	    position = EXT.NORMAL;
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

	private boolean execute(String command) throws InterruptedException, IOException, OutOfCardsException{
	    if(command.equals("RUN"))
	    {
	    	if(!atStop){
	    		if(this.status == stopKey.UP){
	    			while((myProc.getCurrentInstruction().getGo() != 0) && (!stopRequested())){
	    				myProc.step();
	    				Thread.sleep(20);
	    			}
	    		}
	    		else if(this.status == stopKey.LEVEL){
	    			myProc.step();
	    			Thread.sleep(20);
	    		}
	    		else{
	    			myProc.step();
	    			if (myProc.getCurrentInstruction().getGo() == 0){
	    				this.atStop = true;
	    			}
	    			Thread.sleep(20);
	    		}
	    	}
	    }
	    else if(command.equals("RELEASE")){
	    	this.atStop = false;
	    }
	    else if(command.equals("STOPKEY"))
	    {
	    	System.out.println("set to UP, LEVEL, or DOWN?");
	    	String setting = myScanner.nextLine();
	    	if(setting.equals("UP")){
	    		this.status = stopKey.UP;
	    	}
	    	if(setting.equals("LEVEL")){
	    		this.status = stopKey.LEVEL;
	    	}
	    	if(setting.equals("DOWN")){
	    		this.status = stopKey.DOWN;
	    	}
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
	    else if(command.equals("ONE_SHOT_DIAL"))
	    {
	    	if(!atStop){
				System.out.println("Enter number of single shots between 1 and 10: ");
				int shots = myScanner.nextInt();
				assert ((shots <= 10) && (shots > 0));
				for (int i = 0; i < shots; i++) {
					myProc.step();
					myDisplay.update();
					Thread.sleep(20);
				}
	    	}
	    }
	    else if(command.equals("ONE_SHOT"))
	    {
	    	if(!atStop){
	    		System.out.println("Up or Down?");
	    		String shots = myScanner.nextLine();
	    		if (shots.equals("Down")){
	    			myProc.step();
	    			myDisplay.update();
	    			Thread.sleep(20);
	    		}
	    		else if(shots.equals("Up")){
	    			for (int i=0; i<600; i++){
	    				if(!stopRequested()){
	    					myProc.step();
	    					myDisplay.update();
	    					Thread.sleep(10);
	    				}
	    				else return true;
	    			}
	    		}
	    	}
	    	else{
	    		System.out.println("incorrect syntax, nothing happened");
	    	}
	    	
	    }
	    else if(command.equals("OFF"))
	    {
	    	return false;
	    }
	    else if(command.equals("START_PUNCH"))
	    {
	    	myProc.turnOnPunch();
	    }
	    else if(command.equals("FULL_CLEAR")){
	    	myProc.resetMemory();
	    }
	    else if(command.equals("SWITCH_OS")){
	    	System.out.println("which switch are you flicking?");
	    	int toggle = myScanner.nextInt();
	    	if(osLamps.get(toggle)){
	    		osLamps.clear(toggle);
	    	}
	    	else{
	    		this.osLamps.set(toggle);
	    	}
	    	String testOutput = new Word(osLamps).toString();
	    	System.out.println(testOutput);
	    }
	    else if(command.equals("SWITCH_ID")){
	    	System.out.println("which switch are you flicking?");
	    	int toggle = myScanner.nextInt();
	    	if(idLamps.get(toggle)){
	    		idLamps.clear(toggle);
	    	}
	    	else{
	    		this.idLamps.set(toggle);
	    	}
	    	String testOutput = new Word(idLamps).toString();
	    	System.out.println(testOutput);
	    }
	    else if(command.equals("CLEAR_ID")){
	    	idLamps.clear();
	    	String testOutput = new Word(idLamps).toString();
	    	System.out.println(testOutput);
	    }
	    else if(command.equals("CLEAR_OS")){
	    	osLamps.clear();
	    	String testOutput = new Word(osLamps).toString();
	    	System.out.println(testOutput);
	    }
	    else if(command.equals("DELAY_LINE")){
	    	System.out.println("which switch are you flicking?");
	    	int dl = myScanner.nextInt();
	    	myDisplay.changeDelay(dl);
	    }
	    else if(command.equals("RELEASE")){
	    	this.atStop = false;
	    }
	    else if(command.equals("CHANGE_NIS")){
	    	if (this.nisOn) this.nisOn = false;
	    	else this.nisOn = true;
	    }
	    else if(command.equals("CHANGE_SOURCE")){
	    	if (this.sourceOn) this.sourceOn = false;
	    	else this.sourceOn = true;
	    }
	    else if(command.equals("CHANGE_DEST")){
	    	if (this.destOn) this.destOn = false;
	    	else this.destOn = true;
	    }
	    else if(command.equals("SWITCH_NIS")){
	    	System.out.println("which switch are you flicking?");
	    	int nis = myScanner.nextInt();
	    	if(nisSwitch.get(nis)){
	    		nisSwitch.clear(nis);
	    	}
	    	else nisSwitch.set(nis);
	    	System.out.println(new Word(nisSwitch).getAsInt());
	    }
	    else if(command.equals("SWITCH_SOURCE")){
	    	System.out.println("which switch are you flicking?");
	    	int source = myScanner.nextInt();
	    	if(sourceSwitch.get(source)){
	    		sourceSwitch.clear(source);
	    	}
	    	else sourceSwitch.set(source);
	    	System.out.println(new Word(sourceSwitch).getAsInt());
	    }
	    else if(command.equals("SWITCH_DEST")){
	    	System.out.println("which switch are you flicking?");
	    	int dest = myScanner.nextInt();
	    	if(destSwitch.get(dest)){
	    		destSwitch.clear(dest);
	    	}
	    	else destSwitch.set(dest);
	    	System.out.println(new Word(destSwitch).getAsInt());
	    }
	    else if(command.equals("EXT"))
	    {
	    	System.out.println("set to REQUEST_STOP, NORMAL, or EXTTREE");
	    	String setting = myScanner.nextLine();
	    	if(setting.equals("REQUEST_STOP")){
	    		this.position = EXT.REQUEST_STOP;
	    	}
	    	if(setting.equals("NORMAL")){
	    		this.position = EXT.NORMAL;
	    	}
	    	if(setting.equals("EXTTREE")){
	    		this.position = EXT.EXTTREE;
	    	}
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
	
	private boolean stopRequested(){
		if(position == EXT.REQUEST_STOP){
			int nisValue = new Word(nisSwitch).getAsInt();
			int sourceValue = new Word(sourceSwitch).getAsInt();
			int destValue = new Word(destSwitch).getAsInt();
			if(!nisOn && !sourceOn && !destOn){
				return false;
			}
			else if(nisOn && !sourceOn && !destOn){
				if (nisValue == (myProc.getCurrentInstruction().getNIS())){
					System.out.println(myProc.getCurrentInstruction().getAsWord().toString());
					return true;
				}
			}
			else if(!nisOn && sourceOn && !destOn){
				if (sourceValue == (myProc.getCurrentInstruction().getSource())){
					System.out.println(myProc.getCurrentInstruction().getAsWord().toString());
					return true;
				}
			}
			else if(!nisOn && !sourceOn && destOn){
				if (destValue == (myProc.getCurrentInstruction().getDest())){
					System.out.println(myProc.getCurrentInstruction().getAsWord().toString());
					return true;
				}
			}
			else if(nisOn && sourceOn && !destOn){
				if ((nisValue == (myProc.getCurrentInstruction().getNIS())) && 
					(sourceValue == (myProc.getCurrentInstruction().getSource()))){
						System.out.println(myProc.getCurrentInstruction().getAsWord().toString());
						return true;
				}
			}
			else if(!nisOn && sourceOn && destOn){
				if ((sourceValue == (myProc.getCurrentInstruction().getSource())) && 
					(destValue == (myProc.getCurrentInstruction().getDest()))){
						System.out.println(myProc.getCurrentInstruction().getAsWord().toString());
						return true;
				}
			}
			else if(nisOn && sourceOn && destOn){
				if ((nisValue == (myProc.getCurrentInstruction().getNIS())) && 
					(sourceValue == (myProc.getCurrentInstruction().getSource())) && 
					(destValue == (myProc.getCurrentInstruction().getDest()))){
						System.out.println("yeah");
						System.out.println(myProc.getCurrentInstruction().getAsWord().toString());
						return true;
				}
			}
			
		}
		return false;
	}

}
