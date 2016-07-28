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
	BitSet idLamps;
	BitSet osLamps;
	BitSet isLamps;
	private boolean atStop;
	private boolean nisOn;
	private boolean sourceOn;
	private boolean destOn;
	private boolean externalTreeRaised;
	
	public Parser(InputStream in, OutputStream out, Processor myProc){
		this.in = in;
		this.out = new PrintStream(out);
		this.myProc = myProc;
		this.delayLine = 1;
		this.mcSlipOffset = 0;
		this.idLamps = new BitSet(32);
		this.osLamps = new BitSet(32);
		this.isLamps = new BitSet(13);
		this.status = stopKey.UP;
	}

	public void run(){
		System.out.println("running!");
		Scanner sc = new Scanner(this.in);
		while(sc.hasNext()){
			try {
				processCommand(sc);
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
	
	public void processCommand(Scanner sc) throws IOException, OutOfCardsException, InterruptedException{
		//System.out.println("Process command is called!");
		String token = sc.next();
		switch(token){
		case "STEP":
			if(!atStop){
	    		if(this.status == stopKey.UP){
	    			while(myProc.getCurrentInstruction().getGo() != 0){
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
			out.println("RELEASE " + this.atStop);
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
			myProc.initialInput();
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
		case "ONE_SHOT":
			String direction = sc.next();
			if (direction.equals("Down")){
				myProc.step();
				memOutput();
			}
			else if(direction.equals("Up")){
				for (int i=0; i<600; i++){
					myProc.step();
					memOutput();
				}
			}
			else{
			}

			break;
		case "STOP":
			myProc.resetMemory();
			out.println("STOP");
			memOutput();
			out.close();
			in.close();
			return;
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
			out.print(outputOSLamps());
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
			out.println(outputIDLamps());
			break;
		case "CLEAR_ID":
			idLamps.clear();
			out.println(outputIDLamps());
			break;
		case "CLEAR_OS":
			osLamps.clear();
			out.println(outputOSLamps());
			break;
		case "DELAY_LINE":
			int dl = sc.nextInt();
			assert((dl <= 12) && (dl > 0));
			this.delayLine = dl;
			out.println("DELAY_LINE" + " " + this.delayLine);
		    outputDelayLineDisplay();
			break;
		default:
			assert(false);
		}
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
		outputDelayLineDisplay();
		outputRegisterDisplay();
	}
	
//	private void clearOutput() throws IOException{
//		outputDelayLineDisplay();
//		outputRegisterDisplay();
//	}
	
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
	private String outputOSLamps() throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append(OS_LAMPS);
		sb.append(" ");
		sb.append(asString(this.osLamps));
		return sb.toString();
	}
	private String outputIDLamps() throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append(ID_LAMPS);
		sb.append(" ");
		sb.append(asString(this.idLamps));
		return sb.toString();
	}
	private String outputISLamps() throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append(IS_LAMPS);
		sb.append(" ");
		sb.append(asString(this.isLamps));
		return sb.toString();
	}
}
