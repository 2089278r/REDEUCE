package uk.ac.glasgow.redeuce.userinterface;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.BitSet;
import java.util.Scanner;

import uk.ac.glasgow.redeuce.memory.Memory;
import uk.ac.glasgow.redeuce.memory.Word;
import uk.ac.glasgow.redeuce.peripherals.CRDFileReader;
import uk.ac.glasgow.redeuce.peripherals.OutOfCardsException;
import uk.ac.glasgow.redeuce.processor.Processor;
import uk.ac.glasgow.redeuce.userinterface.console.Console.stopKey;

public class Parser implements Runnable{
	
	public static final String DISPLAY_DL = "DISPLAY_DL";
	public static final String DISPLAY_REG = "DISPLAY_REG";
	public static final String OS_LAMPS = "OS_LAMPS";
	public static final String ID_LAMPS = "ID_LAMPS";
	public static final String IS_LAMPS = "IS_LAMPS";
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
	private boolean externalTreeRaised;
	private BitSet nisSwitch;
	private BitSet destSwitch;
	private BitSet sourceSwitch;
	
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
	}

	public void run(){
		Scanner sc = new Scanner(this.in);
		while(sc.hasNext()){
			try {
				processCommand(" ", sc);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			} catch (OutOfCardsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public int processCommand(String command, Scanner sc) throws IOException, OutOfCardsException, InterruptedException{
		String token = command;
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
			return 3;
			
		case "RELEASE":
			this.atStop = false;
			out.print("RELEASE " + this.atStop);
			return 1;
			
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
			return 1;
			
		case "LOAD_CARDS":
			String deck = sc.next();
			CRDFileReader reader = new CRDFileReader(deck);
			myProc.cardLoad(reader.createNewDeck());	
			out.println("LOAD_CARDS " + deck);
			return 1;
			
		case "INIT_IN":
			myProc.initialInput();
			System.out.println("how did I do this before.....");
			out.println("INITIAL ");
			memOutput();
			return 2;
			
		case "ONE_SHOT_DIAL":
			int shots = sc.nextInt();
			assert((shots <=10) && (shots > 0));
			for (int i=0; i<shots; i++){
				myProc.step();
				out.println("ONE_SHOT ");
				memOutput();
			}
			return 3*shots;
		case "ONE_SHOT":
			String direction = sc.next();
			if (direction.equals("Down")){
				myProc.step();
				memOutput();
				return 2;
			}
			else if(direction.equals("Up")){
				for (int i=0; i<600; i++){
					myProc.step();
					memOutput();
				}
				return 1200;
			}
			else{
			}
			break;
		case "OFF":
			myProc.resetMemory();
			out.println("STOP");
			memOutput();
			out.close();
			in.close();
			return 3;
		case "START_PUNCH":
			myProc.turnOnPunch();
			out.println("PUNCH_START");
			return 1;
		case "FULL_CLEAR":
			myProc.resetMemory();
			memOutput();
			return 3;
		case "SWITCH_OS":
			int toggle = sc.nextInt();
			if(osLamps.get(toggle)){
				osLamps.clear(toggle);
			}
			else{
				this.osLamps.set(toggle);
			}
			outputOSLamps();
			return 1;
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
			return 1;
		case "CLEAR_ID":
			idLamps.clear();
			outputIDLamps();
			return 1;
		case "CLEAR_OS":
			osLamps.clear();
			outputOSLamps();
			return 1;
		case "DELAY_LINE":
			int dl = sc.nextInt();
			assert((dl <= 12) && (dl > 0));
			this.delayLine = dl;
		    outputDelayLineDisplay();
			return 2;
		case "CHANGE_NIS":
			if (this.nisOn) this.nisOn = false;
	    	else this.nisOn = true;
			out.println("NIS_CHANGED " + this.nisOn);
			return 1;
		case "CHANGE_SOURCE":
			if (this.sourceOn) this.sourceOn = false;
	    	else this.sourceOn = true;
			out.println("SOURCE_CHANGED " + this.sourceOn);
			return 1;
		case "CHANGE_DEST":
			if (this.destOn) this.destOn = false;
	    	else this.destOn = true;
			out.println("DEST_CHANGED " + this.destOn);
			return 1;
		case "SWITCH_NIS":
			int nis = sc.nextInt();
	    	if(nisSwitch.get(nis)){
	    		nisSwitch.clear(nis);
	    	}
	    	else nisSwitch.set(nis);
	    	outputISLamps();
	    	return 1;
		case "SWITCH_SOURCE":
			int source = sc.nextInt();
	    	if(nisSwitch.get(source)){
	    		nisSwitch.clear(source);
	    	}
	    	else nisSwitch.set(source);
	    	outputISLamps();
	    	return 1;
		case "SWITCH_DEST":
			int dest = sc.nextInt();
	    	if(nisSwitch.get(dest)){
	    		nisSwitch.clear(dest);
	    	}
	    	else nisSwitch.set(dest);
	    	outputISLamps();
	    	return 1;
		case "EXT_TREE":
		    if (externalTreeRaised){
		    	this.externalTreeRaised = false;
		    }
		    else this.externalTreeRaised = true;
		    return 1;
		default:
			return 0;
		}
		//out.println();
		return 0;
	}
	
	private boolean stopRequested(){
		if(externalTreeRaised){
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
