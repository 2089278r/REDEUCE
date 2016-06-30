/** Just to get a hang of making  numbers backwards binary */

import java.io.*;

public class backbinary{

public static void main(String[] args) throws IOException{
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	System.out.println("Please enter a backwards binary number: ");
	String str = br.readLine();
	int decimalReturn = 0;
	for (int i = 0; i < str.length(); i++){
		int currentNumber = Integer.parseInt(str.substring(i,(i+1)));
		decimalReturn += currentNumber*java.lang.Math.pow(2, i);
	}
	System.out.println(decimalReturn);
}
}
		
		
