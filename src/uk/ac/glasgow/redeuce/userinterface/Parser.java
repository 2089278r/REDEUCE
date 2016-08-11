package uk.ac.glasgow.redeuce.userinterface;

/*
 * Class containing all of the code-words for commands, and the amount of responses that
 * an interface ought to expect after a command has been received. Sends that amount of responses based
 * on the inputs that the parser has been given.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import uk.ac.glasgow.redeuce.memory.Word;
import uk.ac.glasgow.redeuce.peripherals.CRDFileReader;
import uk.ac.glasgow.redeuce.peripherals.OutOfCardsException;
import uk.ac.glasgow.redeuce.processor.Processor;

public class Parser implements Runnable{
	
	//MOVE CONSTANTS TO CASE STATEMENTS and MAP
	
	public static final String DISPLAY_DL = "DISPLAY_DL";
	public static final String DISPLAY_REG = "DISPLAY_REG";
	public static final String OS_LAMPS = "OS_LAMPS";
	public static final String ID_LAMPS = "SWITCH_ID";
	public static final String IS_LAMPS = "IS_LAMPS";
	public static final String OFF = "OFF";
	public static final String RUN = "RUN";
	public static final String RELEASE = "RELEASE";
	public static final String STOPKEY = "STOPKEY";
	public static final String LOADCARDS = "LOAD_CARDS";
	public static final String INITIAL = "INIT_IN";
	public static final String DIAL = "ONE_SHOT_DIAL";
	public static final String ONESHOTUP = "ONE_SHOT_UP";
	public static final String ONESHOTDOWN = "ONE_SHOT_DOWN";
	public static final String START_PUNCH = "START_PUNCH";
	public static final String FULLCLEAR = "FULL_CLEAR";
	public static final String DELAYLINE = "DELAY_LINE";
	public static final String EXT = "EXT";
	public static final String SWITCHNIS = "SWITCH_NIS";
	public static final String SWITCHSOURCE = "SWITCH_SOURCE";
	public static final String SWITCHDEST = "SWITCH_DEST";
	public static final String CHANGESOURCE = "CHANGE_SOURCE";
	public static final String CHANGENIS = "CHANGE_NIS";
	public static final String CHANGEDEST = "CHANGE_DEST";
	
	
	Processor myProc;
	private InputStream in;
	private PrintStream out;
	public enum stopKey {
		UP, LEVEL, DOWN
	}
	int delayLine;
	int mcSlipOffset;
	private stopKey status;
	private BitSet idLamps;
	private BitSet osLamps;
	private boolean atStop;
	private boolean nisOn;
	private boolean sourceOn;
	private boolean destOn;
	public enum ext{
		REQUEST_STOP, NORMAL, EXTTREE
	}
	private ext position;
	private BitSet nisSwitch;
	private BitSet destSwitch;
	private BitSet sourceSwitch;
	private HashMap<String, Integer> numberOfOutputs;
	
	@SuppressWarnings("unchecked") //Hashmap stuff?
	public Parser(InputStream in, OutputStream out, Processor myProc){
		this.in = in;
		this.out = new PrintStream(out);
		this.myProc = myProc;
		this.delayLine = 1;
		this.mcSlipOffset = 0;
		this.idLamps = new BitSet(32);
		this.osLamps = new BitSet(32);
		this.nisSwitch = new BitSet(3);
		this.sourceSwitch = new BitSet(5);
		this.destSwitch = new BitSet(5);
		this.status = stopKey.UP;
		this.numberOfOutputs = constructMap();
		this.position = ext.NORMAL;
		//Construct map here maybe? call constructMap
	}


	@SuppressWarnings("rawtypes")
	private HashMap constructMap(){
		Map<String, Integer> map = new HashMap<>();
		map.put(OS_LAMPS, 1);
		map.put(ID_LAMPS, 1);
		map.put(IS_LAMPS, 1);
		map.put(OFF, -1);
		map.put(RUN, 3);
		map.put(RELEASE, 1);
		map.put(STOPKEY, 1);
		map.put(LOADCARDS, 1);
		map.put(INITIAL, 3);
		map.put(DIAL, 3);
		map.put(ONESHOTDOWN, 2);
		map.put(ONESHOTUP, 1200);
		map.put(START_PUNCH, 1);
		map.put(FULLCLEAR, 2);
		map.put(DELAYLINE, 2);
		map.put(EXT, 1);
		map.put(SWITCHNIS, 1);
		map.put(SWITCHSOURCE, 1);
		map.put(SWITCHDEST, 1);
		map.put(CHANGESOURCE, 1);
		map.put(CHANGENIS, 1);
		map.put(CHANGEDEST, 1);
		
		return (HashMap) map;
	}

	public boolean isValidCommand(String command){
		return numberOfOutputs.containsKey(command);
	}
	
	public int getNumberOfOutputs(String command){
		String[] splited = command.split("\\s+");
		String request = splited[0];
		if(request.equals("ONE_SHOT_DIAL")){
			System.out.println((numberOfOutputs.get(request)) * Integer.parseInt(splited[1]));
			return (numberOfOutputs.get(request)) * Integer.parseInt(splited[1]);
		}
		if(isValidCommand(request)){
			return numberOfOutputs.get(request);
		}
		else return 0;
	}
	
	public void run(){
		Scanner sc = new Scanner(this.in);
		while(sc.hasNext()){
			try {
				processCommand(sc);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void processCommand(Scanner sc) throws IOException, InterruptedException{
		String token = sc.next();
		switch(token){
		case "RUN":
			if(!atStop){
	    		if(this.status == stopKey.UP){
	    			while(myProc.getCurrentInstruction().getGo() != 0 && !stopRequested()){
	    				myProc.step();
	    				//Thread.sleep(20);
	    				out.println("STEPPED ");
	    	    		memOutput();
	    	    		out.println(this.atStop);
	    			}
	    		}
	    		else if(this.status == stopKey.LEVEL){
	    			myProc.step();
	    			Thread.sleep(20);
	    			out.println("STEPPED ");
		    		memOutput();
		    		out.println(this.atStop);
	    		}
	    		else{
	    			myProc.step();
	    			if (myProc.getCurrentInstruction().getGo() == 0){
	    				this.atStop = true;
	    			}
	    			Thread.sleep(20);
	    			out.println("STEPPED ");
		    		memOutput();
		    		out.println(this.atStop);
	    		}
	    	}
			break;
			
		case "RELEASE":
			this.atStop = false;
			out.print("RELEASE " + this.atStop);
			break;
		
			
		case "STOPKEY":
			String setting = sc.next();
			if(setting.equals("UP")){
				this.status = stopKey.UP;
			}
			if(setting.equals("LEVEL")){
				this.status = stopKey.LEVEL;
			}
			if(setting.equals("DOWN")){
				this.status = stopKey.DOWN;
			}
			out.println("STOPKEY " + setting);
			break;
			
			
		case "LOAD_CARDS":
			String deck = sc.next();
			CRDFileReader reader = new CRDFileReader(deck);
			myProc.cardLoad(reader.createNewDeck());	
			out.println("LOAD_CARDS " + deck);
			break;
			
			
		case "INIT_IN":
			try{
				myProc.initialInput();
			}
			catch(OutOfCardsException e){
				System.out.println("Caught?");
			}
			out.println("INITIAL ");
			memOutput();
			break;
			
		case "ONE_SHOT_DIAL":
			int shots = sc.nextInt();
			assert((shots <=10) && (shots > 0));
			for (int i=0; i<shots; i++){
				myProc.step();
				out.println("ONE_SHOT ");
				memOutput();
			}
			break;
			
		case "ONE_SHOT_DOWN":
			myProc.step();
			memOutput();
			break;
				
		case "ONE_SHOT_UP":
			for (int i=0; i<600; i++){
				myProc.step();
				memOutput();
			}
			break;
				
		case "OFF":
			myProc.resetMemory();
			out.println("STOP");
			memOutput();
			out.close();
			in.close();
			break;
			
		case "START_PUNCH":
			myProc.turnOnPunch();
			out.println("PUNCH_START");
			break;
			
		case "FULL_CLEAR":
			myProc.resetMemory();
			memOutput();
			break;
			
		case "SWITCH_OS":
			int toggle = sc.nextInt();
			if(osLamps.get(toggle)){
				osLamps.clear(toggle);
			}
			else{
				this.osLamps.set(toggle);
			}
			outputOSLamps();
			break;
			
		case "SWITCH_ID":
			int idToggle = sc.nextInt();
			int idState = sc.nextInt();
			if(idState == 1){
				idLamps.set(idToggle);
			}
			else{
				idLamps.clear(idToggle);
			}
			outputIDLamps();
			break;
			
		case "CLEAR_ID":
			idLamps.clear();
			outputIDLamps();
			break;
		
		case "CLEAR_OS":
			osLamps.clear();
			outputOSLamps();
			break;
			
		case "DELAY_LINE":
			int dl = sc.nextInt();
			assert((dl <= 12) && (dl > 0));
			this.delayLine = dl;
		    outputDelayLineDisplay();
			break;
		    
		case "CHANGE_NIS":
			if (this.nisOn) this.nisOn = false;
	    	else this.nisOn = true;
			out.println("NIS_CHANGED " + this.nisOn);
			break;
			
		case "CHANGE_SOURCE":
			if (this.sourceOn) this.sourceOn = false;
	    	else this.sourceOn = true;
			out.println("SOURCE_CHANGED " + this.sourceOn);
			break;
			
		case "CHANGE_DEST":
			if (this.destOn) this.destOn = false;
	    	else this.destOn = true;
			out.println("DEST_CHANGED " + this.destOn);
			break;
			
		case "SWITCH_NIS":
			int nis = sc.nextInt();
	    	if(nisSwitch.get(nis)){
	    		nisSwitch.clear(nis);
	    	}
	    	else nisSwitch.set(nis);
	    	outputISLamps();
	    	break;
	    	
		case "SWITCH_SOURCE":
			int source = sc.nextInt();
	    	if(nisSwitch.get(source)){
	    		nisSwitch.clear(source);
	    	}
	    	else nisSwitch.set(source);
	    	outputISLamps();
	    	break;
	    	
		case "SWITCH_DEST":
			int dest = sc.nextInt();
	    	if(nisSwitch.get(dest)){
	    		nisSwitch.clear(dest);
	    	}
	    	else nisSwitch.set(dest);
	    	outputISLamps();
	    	break;
	    	
		case "EXT":
			String toPosition = sc.next();
			if(toPosition.equals("EXTTREE")){
				this.position = ext.EXTTREE;
			}
			if(toPosition.equals("NORMAL")){
				this.position = ext.NORMAL;
			}
			if(toPosition.equals("REQUEST_STOP")){
				this.position = ext.REQUEST_STOP;
			}
			out.println("EXT " + toPosition);
			break;
		    
		default:
			break;
		}
	}
	
	private boolean stopRequested(){
		if(position == ext.REQUEST_STOP){
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
						System.out.println(myProc.getCurrentInstruction().getAsWord().toString());
						return true;
				}
			}
			
		}
		return false;
	}
	
	private String asString(BitSet bits){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<bits.length(); i++){
			if (bits.get(i)){
				sb.append("1");
				}
			else sb.append("0");
		}
		return sb.toString();
	}
	
	private void memOutput() throws IOException{
		outputRegisterDisplay();
		outputDelayLineDisplay();
	}
	private void outputDelayLineDisplay() throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append(DISPLAY_DL);
		sb.append(" ");
		sb.append(this.delayLine);
		sb.append(" ");
		sb.append(this.myProc.getMemory().outputDelayLine(this.delayLine, this.mcSlipOffset));
		out.println(sb.toString());
	}
	private void outputRegisterDisplay() throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append(DISPLAY_REG);
		sb.append(" ");
		sb.append(this.myProc.getMemory().outputRegisters());
		out.println(sb.toString());
	}
	private void outputOSLamps() throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append(OS_LAMPS);
		sb.append(" ");
		sb.append(asString(this.osLamps));
		out.println(sb.toString());
	}
	private void outputIDLamps() throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append(ID_LAMPS);
		sb.append(" ");
		sb.append(asString(this.idLamps));
		out.println(sb.toString());
	}
	private void outputISLamps() throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append(IS_LAMPS);
		sb.append(" ");
		sb.append(asString(this.nisSwitch));
		sb.append(asString(this.sourceSwitch));
		sb.append(asString(this.destSwitch));
		out.println(sb.toString());
	}
}
