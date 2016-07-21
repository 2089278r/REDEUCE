package userinterface;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import uk.ac.glasgow.redeuce.memory.Memory;

public class Display {
	
	Memory deuceMemory;
	int delayLine;
	
	public Display(Memory deuceMemory){
		this.deuceMemory = deuceMemory;
		this.delayLine = 1;
	}
	
	
    public void update() {
        try {
            File registerDisplay = new File("displayTest.txt");
            File delayLineDisplay = new File("delayDisplay.txt");
            FileOutputStream outputStream = new FileOutputStream(registerDisplay);
            FileOutputStream delayOutput = new FileOutputStream(delayLineDisplay);
            OutputStreamWriter writing = new OutputStreamWriter(outputStream);  
            OutputStreamWriter delayWriting = new OutputStreamWriter(delayOutput);
            Writer writer = new BufferedWriter(writing);
            Writer delayWriter = new BufferedWriter(delayWriting);
            writer.write(deuceMemory.toString());
            writer.close();
            delayWriter.write(deuceMemory.outputDelayLine(this.delayLine));
            delayWriter.close();
            System.out.println(deuceMemory.toString());
            
        } catch (IOException e) {
            System.err.println("Problem writing to the file");
        }
    }

}
