package uk.ac.glasgow.redeuce.userinterface;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.BitSet;
import java.util.Scanner;

import uk.ac.glasgow.redeuce.memory.Memory;
import uk.ac.glasgow.redeuce.memory.Word;
import uk.ac.glasgow.redeuce.peripherals.CRDFileReader;
import uk.ac.glasgow.redeuce.peripherals.OutOfCardsException;
import uk.ac.glasgow.redeuce.processor.Processor;
import uk.ac.glasgow.redeuce.userinterface.console.Console.stopKey;

public class Parser {
	
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

	public Parser(InputStream in, PrintStream out, Processor myProc){
		this.in = in;
		this.out = out;
		this.myProc = myProc;
		this.delayLine = 1;
		this.mcSlipOffset = 0;
		this.idLamps = new BitSet(32);
		this.osLamps = new BitSet(32);
		this.isLamps = new BitSet(13);
	}

	public void run() throws IOException, OutOfCardsException{
		Scanner sc = new Scanner(this.in);
		while(sc.hasNext()){
			String token = sc.next();
			switch(token){
			case "STEP":
				//			    	if(this.status == stopKey.UP){
				//			    		while(myProc.getCurrentInstruction().getGo() != 0){
				//			    			myProc.step();
				//			    			Thread.sleep(20);
				//			    		}
				//			    	}
				//			    	else if(this.status == stopKey.LEVEL){
				//			    		myProc.step();
				//			    		Thread.sleep(20);
				//			    	}
				//			    	else{
				//			    		
				//			    	}
				//			    
			case "RELEASE":
				//Presses release key
				//Meaning that machine is no longer stopped!

			case "STOPKEY":
				String setting = sc.nextLine();
				if(setting.equals("UP")){
					this.status = stopKey.UP;
				}
				if(setting.equals("LEVEL")){
					this.status = stopKey.LEVEL;
				}
				if(setting.equals("DOWN")){
					this.status = stopKey.DOWN;
				}
				break;
			case "LOAD_CARDS":
				String deck = sc.nextLine();
				CRDFileReader reader = new CRDFileReader(deck);
				myProc.cardLoad(reader.createNewDeck());	
				break;
			case "INIT_IN":
				myProc.initialInput();
				break;
			case "ONE_SHOT_DIAL":
				int shots = sc.nextInt();
				assert((shots <=10) && (shots > 0));
				for (int i=0; i<shots; i++){
					myProc.step();
					outputDisplay();
				}
				break;
			case "ONE_SHOT":
				String direction = sc.nextLine();
				if (direction.equals("Down")){
					myProc.step();
					out.print(myProc.getMemory().outputRegisters());
				}
				else if(direction.equals("Up")){
					for (int i=0; i<600; i++){
						myProc.step();
						outputDisplay();
					}
				}
				else{
				}

				break;
			case "STOP":
				return;
			case "START_PUNCH":
				myProc.turnOnPunch();
				break;
			case "FULL_CLEAR":
				myProc.resetMemory();
				break;
			case "SWITCH_OS":
				
				int toggle = sc.nextInt();
				if(osLamps.get(toggle)){
					osLamps.clear(toggle);
				}
				else{
					this.osLamps.set(toggle);
				}
				//String testOutput = new Word(osLamps).toString();
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
				//String testOutput = new Word(idLamps).toString();
				outputIDLamps();
				break;
				
			case "CLEAR_ID":
				idLamps.clear();
				//String testOutput = new Word(idLamps).toString();
				break;
			case "CLEAR_OS":
				osLamps.clear();
				//String testOutput = new Word(osLamps).toString();
				break;
			case "DELAY_LINE":
				int dl = sc.nextInt();
				assert((dl <= 12) && (dl > 0));
				this.delayLine = dl;
				break;
			default:
				outputDisplay();
				assert(false);
			}
		}
	}
	
	private void outputDisplay(){
		outputDelayLineDisplay();
		outputRegisterDisplay();
	}
	private void outputDelayLineDisplay(){
		out.print(DISPLAY_DL);
	}
	private void outputRegisterDisplay(){
		out.print(DISPLAY_DL);
	}
	private void outputOSLamps(){
		out.print(OS_LAMPS);
	}
	private void outputIDLamps(){
		out.print(ID_LAMPS);
	}
	private void outputISLamps(){
		out.print(IS_LAMPS);
	}
}
