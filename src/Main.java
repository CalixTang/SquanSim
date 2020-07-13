import processing.core.*; 
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;
import java.io.BufferedReader; 
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Main {
	
	public static void main (String[] args) {
		
		
	/*      TESTING SPACE
	 * 
	 * 
	 * 
	 * 
	 */
		
		DrawingSurface d = new DrawingSurface("data" + System.getProperty("file.separator") + "data.txt");
		PApplet.runSketch(new String[]{"SquanSim"}, d);
		
		//attempt to run args if possible?
		for(String s : args) {
			d.getSquan().execAlg(s);
		}
		
	}
}