package main;

import ScotlandYardFrame.ScotlandYardFrame;

public class Main {

	public final static boolean DEBUG = false;
	
	//can be changed to Clever or Random
	public final static MrXType MRXTYPE=MrXType.Random;

	//inner enum
	public enum MrXType {
		Random, Clever
	}
	
	public static void main(String[] args) throws Exception {
		ScotlandYardFrame frame = new ScotlandYardFrame();
		frame.setVisible(true);
	}
	
}