import java.awt.Toolkit; 
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
//import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;

import processing.core.*;

public class DrawingSurface extends PApplet{
	private Squan sq;
	private String splashText;
	private String cmdPrompt;
	private String cmd;
	private ArrayList<Integer> lastTop;
	private ArrayList<Integer> lastBottom;
	private ArrayList<String> cmdList;
	private int cmdIndex;
	private boolean lastEquator;
	private boolean inversion;
	private boolean stepThrough;
	private boolean render3D;
	private String stepThruAlg;
	private int stepIndex;
	private String file;
	private PFont cmdFont;
	private float rotX;
	private float rotY;
	
	
	public DrawingSurface(String file) {
		sq = new Squan();	
		splashText = "";
		cmdPrompt = "Enter command/alg below:";
		cmd = "";
		inversion = false;
		stepThrough = false;
		stepThruAlg = "";
		stepIndex = 0;
		rotX = -PI/4;
		rotY = PI/4;
		lastTop = new ArrayList<Integer>();
		lastBottom = new ArrayList<Integer>();
		cmdList = new ArrayList<String>();
		cmdIndex = 0;
		this.file = file;
		loadData(file);
		updateSq();
		
	}
	
	private void updateSq() {
		lastTop.clear();
		lastBottom.clear();
		for(int i = 0; i < sq.getTop().size(); i++) {
			lastTop.add(sq.getTop().get(i));
		}
		for(int i = 0; i < sq.getBottom().size(); i++) {
			lastBottom.add(sq.getBottom().get(i));
		}
		lastEquator = sq.getEquator();
		//System.out.println(lastTop.toString() + "\n" + lastBottom.toString());
	}
	
	public Squan getSquan() {
		return sq;
	}
	public void settings() {
		if(Squan.getRender()) {
			size(1000,800,P3D);
			//translate(500, 400, 0);
		}else {
			size(1000,800);
		}
		
	}
	
	public void setup() {
		cmdFont = createFont("data" + System.getProperty("file.separator") + "lucidaconsole.ttf",15);
		prepareExitHandler();
	}
	private void prepareExitHandler (){
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		public void run () {

			System.out.println("SHUTDOWN HOOK");
			writeData(file);
		   // application exit code here

		}
		}));
	}	
	public void draw() {
		background(101);
		pushStyle();
		pushMatrix();
		if(Squan.getRender()) {
			translate(500,350);
			rotateX(rotX);
			rotateY(rotY);
		}
		sq.draw(this);
		popMatrix();
		popStyle();
		fill(0);
		stroke(0);
		rect(0,580,498,220,3);
		textAlign(CENTER);
		textSize(20);
		text(cmdPrompt,250,570);
		textSize(50);
		text("Squan Simulator",500,50);
		textSize(20);
		text(splashText,500,120);
		textAlign(LEFT);
		fill(255);
		textSize(15);
		
		
		pushStyle();
		textFont(cmdFont);
		text(">",5,600);
		
		String cmdText = cmd.toString();
		if(stepThruAlg.length()>0) {
			System.out.println(stepIndex + " " +cmdText.charAt(stepIndex));
			if(cmdText.charAt(stepIndex)=='/') {//highlight a /
				text(cmdText.substring(0, stepIndex), 20, 590, 470, 400);
				fill(0, 255, 0);
				text("/", 20 + (stepIndex) % 52 * 9,
						590 + (stepIndex) / 52 * 15,
						470 - (stepIndex) % 52 * 9,
						400 - (stepIndex) / 52 * 15);
				fill(255);
				if(cmdText.indexOf(",",stepIndex)>=0) { // currently / ....
					text(cmdText.substring(stepIndex + 1), 20 + (stepIndex+1) % 52 * 9,
							590 + (stepIndex+1) / 52 * 15,
							470 - (stepIndex+1) % 52 * 9,
							400 - (stepIndex+1) / 52 * 15);
					//stepIndex++;				
				}
			}else { //highlight a [u],[d] set
				if(cmdText.indexOf("/",stepIndex)>=0) { // currently [u].[d] / ...
					text(cmdText.substring(0,stepIndex),20,590,470,400);
					fill(0,255,0);
					text(cmdText.substring(stepIndex,cmdText.indexOf("/",stepIndex)),20 + stepIndex % 52 * 9,
							590 + stepIndex / 52 * 15,
							470 - stepIndex % 52 * 9,
							400 - stepIndex / 52 * 15);
					fill(255);
					text(cmd.substring(cmdText.indexOf("/",stepIndex)), 20 + cmdText.indexOf("/",stepIndex) % 52 * 9,
							590 + cmdText.indexOf("/",stepIndex) / 52 * 15,
							470 - cmdText.indexOf("/",stepIndex) % 52 * 9,
							400 - cmdText.indexOf("/",stepIndex) / 52 * 15);
					
					//stepIndex += cmdText.substring(0,cmdText.indexOf("/",stepIndex)).length();
				}else { // [u],[d] left
					text(cmdText.substring(0, stepIndex), 20, 590, 470, 400);
					fill(0, 255, 0);
					text(cmdText.substring(stepIndex), 20 + stepIndex % 52 * 9,
							590 + stepIndex / 52 * 15,
							470 - stepIndex % 52 * 9,
							400 - stepIndex / 52 * 15);
				}
			}
				
		}else {		
			text(cmdText,20,590,470,400);
		}
		fill(240, 234, 206); //cream
		stroke(0);
		strokeWeight(3);
		rect(900,700,100,100,7); //render
		rect(800,700,100,100,7); //caps
		rect(700,700,100,100,7); //color
		rect(600,700,100,100,7); //notation
		stroke(0);
		fill(0);
		line(625,775,675,725);
		line(725,775,775,725);
		line(825,775,875,725);
		line(925,775,975,725);
		stroke(80);
		rect(705,705,40,40,7);
		fill(255,255,0);
		rect(755,755,40,40,7);
		
		fill(255);
		rect(805,705,40,40,7);
		fill(0);
		rect(855,755,40,40,7);
		noFill();
		stroke(0);
		arc(850,750,60,60,PI/2,PI);
		arc(850,750,60,60,-PI/2,0);
		stroke(0);
		line(850,720,860,710);
		line(850,720,860,730);
		line(850,780,840,770);
		line(850,780,840,790);
		fill(0);
		textAlign(CENTER,CENTER);
		textSize(20);
		text("Kh",625,725);
		text("WCA",675,775);
		text("2D",925,725);
		text("3D",975,775);
		popStyle();
	}
	
	
	
	public void keyReleased() {	
		/*
		System.out.print("cmd: ");
		for(int i = 0; i < cmd.length(); i++) {
			System.out.print("" + cmd.charAt(i));
		}
		*/
		if (key==10) {//enter
			splashText = "";
			if(inversion) {
				/*try {
					sq.execAlg(cmd);
					draw();
				}catch(InputMismatchException e) {
					splashText = "Invalid Squan Alg. Squan reset to last valid state.";
					sq = new Squan(lastTop,lastBottom,lastEquator,Squan.getTopColor(),Squan.getTiles());
					draw();
					return;
				}*/
				cmdPrompt = "Enter command/alg below:";
				inversion = false;
				String inverted = Squan.invert(cmd);
				//System.out.println(inverted);
				try {
					sq.execAlg(inverted);
					splashText = "Inverted alg: " + inverted;
					draw();
				}catch(InputMismatchException e) {
					splashText = "Invalid Squan Alg. Squan reset to last valid state.";
					sq = new Squan(lastTop,lastBottom,lastEquator,Squan.getTopColor(),Squan.getTiles(),Squan.getNotation(),Squan.getRender());
					draw();
					//cmd = "";
				}catch(NumberFormatException e) {
					splashText = "Invalid Squan Alg. Check Notation.";
					sq = new Squan(lastTop,lastBottom,lastEquator,Squan.getTopColor(),Squan.getTiles(),Squan.getNotation(),Squan.getRender());
					draw();
					//cmd = "";
				}
				cmdPrompt = "Enter command/alg below:";
				return;
			}else if(stepThrough) {
				//TODO: add scrub through code for algs
				try {//sanity check to make sure that the alg is valid.
					Squan tempSq = new Squan(lastTop,lastBottom,lastEquator,Squan.getTopColor(),Squan.getTiles(),Squan.getNotation(),Squan.getRender());
					tempSq.execAlg(cmd);
					cmdPrompt = "Press any key to move a step forward.";
					stepThruAlg = Squan.delim(cmd.toString());
					cmd = Squan.delim(cmd);
				}catch(InputMismatchException | NumberFormatException e) {
					splashText = "Invalid alg.";
					cmdPrompt = "Enter command/alg below:";
					cmd = "";
				}
				stepThrough = false;
				return;
			}else if(stepThruAlg.length()>0){
				//just to make sure nothing bad happens when stepThrough
			}
			else {
				System.out.println(cmd);
				cmd = cmd.replace("?","");
				cmd = cmd.replace(" ", "");
				cmd = cmd.replace("\n", "");
				
				if(cmd.equalsIgnoreCase("reset")) {
					sq.resetSquan();
					updateSq();
				}else if(cmd.equalsIgnoreCase("help")){
					System.out.println(
							"\n"
							+ "List of Commands:\n"
							+ "To cmd an alg, just cmd the alg as a comman\n"
							+ "help: returns you to this text.\n"
							+ "reset: returns the Squan to its solved state.\n"
							+ "quit/exit: closes the program.\n"
							+ "toString: prints out the Squan in its array form.\n"
							+ "invert/inverse: inverses the next alg you cmd.\n"
							+ "switchCaps/flip: switches the equator caps.\n"
							+ "switchColor/toggleColor/toggleScheme: switches tile color from black to yellow or yellow to black.\n"
							+ "toggleNotation: toggles WCA and Karnaukh notation.\n"
							+ "toggleRender: switch between 2D and 3D rendering."
							);
				}else if(cmd.equalsIgnoreCase("quit") || cmd.equalsIgnoreCase("exit")) {
					exit();
				}else if(cmd.equalsIgnoreCase("tostring")) {
					splashText = sq.toString();
				}else if(cmd.equals("")) {
					
				}else if(cmd.equalsIgnoreCase("invert") || cmd.equalsIgnoreCase("inverse")){
					inversion = true;
					cmdPrompt = "Enter an alg to invert.";
					
				}else if(cmd.equalsIgnoreCase("switchCaps") || cmd.equalsIgnoreCase("flip") || cmd.equalsIgnoreCase("togglecaps")) {
					sq.switchCaps();
					splashText = "Switched equator caps.";
				}else if(cmd.equalsIgnoreCase("switchColor") || cmd.equalsIgnoreCase("toggleColor") || cmd.equalsIgnoreCase("toggleScheme")) {
					sq.switchColor();
					splashText = "Toglged color scheme.";
				}else if(cmd.equalsIgnoreCase("toggleNotation")) {
					Squan.switchNotation();
					if(Squan.getNotation()) {
						splashText = "Turned on Karnaukh notation.";
					}else {
						splashText = "Turned off Karnuakh notation.";
					}
				}else if(cmd.equalsIgnoreCase("save")) {
					writeData(file);
					splashText = "Saved Squan state and current settings.";
				}else if(cmd.equalsIgnoreCase("checkParity")) {
					try {
						if(sq.checkParity())
							splashText = "There is parity in this state.";
						else
							splashText = "There is no parity in this state.";
					}catch(InputMismatchException e) {
						splashText = "The puzzle is in an invalid state to check parity for";
					}
				}else if(cmd.equalsIgnoreCase("stepByStep") || cmd.equalsIgnoreCase("scrubAlg") || cmd.equalsIgnoreCase("stepThrough")) {
					stepThrough = true;
					cmdPrompt = "Enter an alg to step through.";
					
				}else if (cmd.equalsIgnoreCase("toggleRender")) {
					render3D = !render3D;
					cmdPrompt="Close and re-open to change render settings.";
					//System.out.println(Squan.getRender());
				}else{
					try {
						//System.out.print("cmd is" + cmd);

						sq.execAlg(cmd);
						splashText = cmd;
						//draw();
					}catch(InputMismatchException e) {
						splashText = "Invalid Squan Alg. Squan reset to last valid state.";
						//System.out.println(sq.toString());
						sq = new Squan(lastTop,lastBottom,lastEquator,Squan.getTopColor(),Squan.getTiles(),Squan.getNotation(),Squan.getRender());
						//System.out.println(sq.toString());
						//draw();
						//cmd = "";
					}catch(NumberFormatException e) {
						splashText ="Invalid Command.";
						//cmd = "";
					}
					
				}
				cmdList.add(cmd);
				cmdIndex++;
				cmd = "";
				updateSq();
			}
		}
	}
	
	
	
	public void keyPressed() {
		//System.out.println(keyCode);
		//System.out.println(cmd.length());
		if(stepThruAlg.length()>0) {
			//System.out.println("alg is " + stepThruAlg);
			if(stepThruAlg.contains("/") && stepThruAlg.contains(",")) {
				if(stepThruAlg.indexOf("/") < stepThruAlg.indexOf(",")) {
					sq.execAlg("/");
					stepIndex++;
					stepThruAlg = stepThruAlg.substring(stepThruAlg.indexOf("/")+1);
				}else {
					sq.execAlg(stepThruAlg.substring(0,stepThruAlg.indexOf("/")));
					stepIndex += stepThruAlg.substring(0,stepThruAlg.indexOf("/")).length();
					stepThruAlg = stepThruAlg.substring(stepThruAlg.indexOf("/"));
					
				}
				updateSq();
			}else { //1 move or slice left!
				sq.execAlg(stepThruAlg);
				stepThruAlg = "";
				cmdPrompt = "Enter command/alg below:";
				cmd = "";
				stepIndex = 0;
				//splashText = "";
				updateSq();
			}
		}else {
			if(key==8) {
				if(cmd.length()>0) {//backspace
					cmd = cmd.substring(0,cmd.length()-1);
				}
			}else if(keyCode==16 || keyCode==8 || keyCode==9 || keyCode==17){
			}else if (key==22){ //ctrl v
				//System.out.println(22);
				Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();

				// Get data stored in the clipboard that is in the form of a string (text)
				try {
					cmd += (String) c.getData(DataFlavor.stringFlavor);
					splashText = "Pasted text into command line.";
				} catch (UnsupportedFlavorException | IOException e) {
					// TODO Auto-generated catch block
					splashText = "Something went wrong pasting.";
				}
			}else if (key==19){ //ctrl s
				writeData(file);
				splashText = "Saved squan data and program settings.";
			}else if(key==3){ //ctrl c
				
			}else if(keyCode==DOWN){
				if(cmdIndex<cmdList.size()-1 && !cmdList.isEmpty()){
					cmdIndex++;
					cmd = cmdList.get(cmdIndex);
				}
			}else if(keyCode==UP) {
				if(cmdIndex>0 && !cmdList.isEmpty()){
					cmdIndex--;
					cmd = cmdList.get(cmdIndex);
				}
			}else{
				cmd+=key;
			}
		}
	}
	
	/**Attempts to read data from the file and set up the custom settings encoded within.
	 * 
	 * @param file The path to the file in String format.
	 */
	public void loadData(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String[] topL = reader.readLine().replace("[", "").replace("]","").replace(" ", "").split(",");
			String[] bottomL = reader.readLine().replace("[", "").replace("]","").replace(" ", "").split(",");
			String[] states = reader.readLine().split(" ");
			ArrayList<Integer> topLayer = new ArrayList<Integer>();
			ArrayList<Integer> bottomLayer = new ArrayList<Integer>();
			boolean[] sqStates = new boolean[5];
			for(int i = 0; i < topL.length; i++) {
				topLayer.add(Integer.parseInt(topL[i]));
			}
			for(int i = 0; i < bottomL.length; i++) {
				bottomLayer.add(Integer.parseInt(bottomL[i]));
			}
			for(int i = 0; i < states.length; i++) {
				if(states[i].equalsIgnoreCase("false")) {
					sqStates[i] = false;
				}else {
					sqStates[i] = true;
				}
				//System.out.print(states[i]);
			}
			sq = new Squan(topLayer,bottomLayer,sqStates[0],sqStates[1],sqStates[2],sqStates[3],sqStates[4]);
			render3D = sqStates[4];
			reader.close();
		} catch (FileNotFoundException e) {
			splashText = "data.txt not found.";
		} catch(IOException e) {
			splashText = "Problem loading data from file";
		} catch(IndexOutOfBoundsException e) {
			splashText = "Bad data found in data.txt.";
		}
	}
	
	/**Writes the states of the Squan into a .txt file.
	 * 
	 * @param file The path to the file in String format.
	 */
	public void writeData(String file) {
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			writer.println(lastTop.toString());
			writer.println(lastBottom.toString());
			writer.println(lastEquator + " " + Squan.getTopColor()+" "+Squan.getTiles()+" "+Squan.getNotation()+" "+render3D);
			writer.close();
			System.out.println("Wrote to file.");
			System.out.println(lastEquator + " " + Squan.getTopColor()+" "+Squan.getTiles()+" "+Squan.getNotation()+" "+render3D);
		}catch(IOException e) {
			splashText = "Problem writing data to file.";
			System.out.println("Didn't write to file.");
		}
			
	}
	
	/*
	 
            
    */
	public void mousePressed() {
		if(mouseX>=900 && mouseX <=1000 && mouseY>=700 && mouseY<=800) {
			render3D = !render3D;
			cmdPrompt="Close and re-open to change render settings.";
		}else if(mouseX>=800 && mouseX <900 && mouseY>=700 && mouseY<=800) {
			sq.switchCaps();
			splashText = "Switched equator caps.";
		}else if(mouseX>=700 && mouseX <800 && mouseY>=700 && mouseY<=800) {
			sq.switchColor();
			splashText = "Toggled color scheme.";
		}else if(mouseX>=600 && mouseX <700 && mouseY>=700 && mouseY<=800) {
			Squan.switchNotation();
			if(Squan.getNotation()) {
				splashText = "Turned on Karnaukh notation.";
			}else {
				splashText = "Turned off Karnuakh notation.";
			}
		}
	}
	public void mouseDragged() {
		float dX = mouseX - pmouseX;
		float dY = mouseY - pmouseY;
		rotX = (rotX-dY/500)%(2*PI);
		rotY = (rotY+dX/500)%(2*PI);
	}
}
