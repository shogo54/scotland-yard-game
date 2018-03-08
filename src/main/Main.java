package main;

import ScotlandYardFrame.ScotlandYardFrame;

/**
 * Class just for main method
 * To play the game, run the main method here
 * 
 * @author Shogo Akiyama
 * 
 */
public class Main {

	public final static boolean DEBUG = false;

	// can be changed to Clever or Random
	public final static MrXType MRXTYPE = MrXType.Random;

	// inner enumerator
	public enum MrXType {
		Random, Clever
	}

	public static void main(String[] args) throws Exception {
		ScotlandYardFrame frame = new ScotlandYardFrame();
		frame.setVisible(true);
	}

}