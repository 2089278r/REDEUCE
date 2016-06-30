package uk.ac.glasgow.redeuce.memory;

import java.util.Arrays;

public class QuadRegister extends Register{
	public QuadRegister(){
		numberOfWords = 4;
		contents = new Word[numberOfWords];
		Arrays.fill(contents, new Word());
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
