import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.InputMismatchException;
import processing.core.*;

public class Squan {
	private ArrayList<Integer> top;
	private ArrayList<Integer> bottom;
	private boolean equator; //eq solved
	private static boolean blackTiles;
	private static boolean whiteTop; 
	private static boolean notation = false;
	private static boolean render3D;
	public static final double pi = Math.PI; //forgive my style please
	public static final float sD = (float)(125/Math.cos(pi/12)); //small diagonal
	public static final float bD = 125 * (float)Math.sqrt(2); //big diagonal
	public static final float cE = 125-(float)(sD*Math.sin(pi/12));//corner edge length
	public static final float eE = (float)(2*sD*Math.sin(pi/12));
	
	
	public Squan() {
		top = new ArrayList<Integer>();
		bottom = new ArrayList<Integer>();
		equator = true;
		whiteTop = true;
		blackTiles = true;
		render3D = true;
		resetSquan();
	}
	public Squan(ArrayList<Integer> top, ArrayList<Integer> bottom, boolean equator, boolean wTop, boolean bTiles, boolean ntion, boolean r3d) {
		
		
		this.top = new ArrayList<Integer>();
		this.bottom = new ArrayList<Integer>();
		for(int i = 0; i < top.size(); i++) {
			this.top.add(top.get(i));
		}
		for(int i = 0; i < bottom.size(); i++) {
			this.bottom.add(bottom.get(i));
		}
		this.equator = equator;
		Squan.whiteTop = wTop;
		Squan.blackTiles = bTiles;
		Squan.notation = ntion;
		Squan.render3D = r3d;
		//resetSquan();
	}
	
	public void switchCaps() {
		whiteTop = !whiteTop;
	}
	public void switchColor() {
		blackTiles = !blackTiles;
	}
	/*
	public void toggleRender() {
		render3D = !render3D;
	}
	*/
	public static void switchNotation() {
		notation = !notation;
	}
	ArrayList<Integer> getTop(){
		return top;
	}
	public ArrayList<Integer> getBottom(){
		return bottom;
	}
	public boolean getEquator() {
		return equator;
	}
	public static boolean getTiles() {
		return blackTiles;
	}
	public static boolean getTopColor() {
		return whiteTop;
	}
	public static boolean getNotation() {
		return notation;
	}
	public static boolean getRender() {
		return render3D;
	}
	
	public void resetSquan() {
		top.clear();
		bottom.clear();
		for(int i = 0; i < 12; i++) {
			top.add(i+1);
			bottom.add(i+13);
		}
		equator = true;
	}
	public void slice() throws InputMismatchException{
		if(!isSliceable()) {
			throw new InputMismatchException("Invalid Square-1 Alg.\n Current position: " + this.toString());
		}
		equator = !equator;
		for(int i = 0; i < 6; i++) {
			int temp = top.get(i);
			top.set(i, bottom.get(5-i));
			bottom.set(5-i, temp);	
		}
	}
	public void move(int topL, int bottomL) {
		//System.out.println (topL + ", " + bottomL);
		ArrayList<Integer> layer = new ArrayList<Integer>();
		for(int i = 0; i < 12; i++) {
			layer.add(top.get(regIndex(i+topL)));
		}
		for(int i = 0; i < 12; i++) {
			top.set(i,layer.get(i));
		}
		layer.clear();
		for(int j = 0; j < 12; j++) {
			layer.add(bottom.get(regIndex(j-bottomL)));
		}
		for(int j = 0; j < 12; j++) {
			bottom.set(j,layer.get(j));
		}
	}
	
	
	private boolean isSliceable() {
		if(top.get(0)==3|top.get(0)==6||top.get(0)==9||top.get(0)==12||top.get(0)==14||top.get(0)==17||top.get(0)==20||top.get(0)==23)
			return false;
		else if(top.get(5)==2||top.get(5)==5||top.get(5)==8||top.get(5)==11||top.get(5)==15||top.get(5)==18||top.get(5)==21||top.get(5)==24)
			return false;
		else if(bottom.get(5)==3||bottom.get(5)==6||bottom.get(5)==9||bottom.get(5)==12||bottom.get(5)==14||bottom.get(5)==17||bottom.get(5)==20||bottom.get(5)==23)
			return false;
		else if(bottom.get(0)==2||bottom.get(0)==5||bottom.get(0)==8||bottom.get(0)==11||bottom.get(0)==15||bottom.get(0)==18||bottom.get(0)==21||bottom.get(0)==24)
			return false;
		return true;
	}
	public int regIndex(int in) {
		int out = in % 12;
		if (out>=0) {
			//System.out.println(out);
			return out;
		}else {
			//System.out.println(out);
			return 12+out;
		}
	}
	public void execAlg(String alg){
		//boolean badAlg = false;
		alg = delim(alg);
		/*
		if(alg.charAt(0)==' ') {
			slice();
			alg = alg.substring(1,alg.length());
		}
		*/
		while(alg.contains("/") && alg.contains(",")) {
			if(alg.indexOf(',') < alg.indexOf('/')) { //move set (e.g. -3,0) next
				move(Integer.parseInt(alg.substring(0, alg.indexOf(','))), Integer.parseInt(alg.substring(alg.indexOf(',')+1,alg.indexOf('/'))));
				alg = alg.substring(alg.indexOf('/'),alg.length());
				//System.out.println(alg);
			}else { //Slice next
				try {
				slice();
				}catch(InputMismatchException e) {
					throw e;
				}
				alg = alg.substring(alg.indexOf('/')+1,alg.length());
				//System.out.println(alg);
			}
		}
		if(alg.contains("/")) {
			try {
				slice();
			}catch(InputMismatchException e) {
				throw e;
			}

		}else if(alg.contains(",")){ // has ","
			move(Integer.parseInt(alg.substring(0, alg.indexOf(','))), Integer.parseInt(alg.substring(alg.indexOf(',')+1,alg.length())));
		}
	}
	
	public String toString() {
		return "\n" + "Top: " + top.toString() +  "\n" + "Bottom: " + bottom.toString() + "\n" + "Equator flipped: " + !equator + "\n";
	}
		
	private Color gC(int piece) {
		if(whiteTop) {
			if(piece==1 || piece ==2 || piece==12 || piece==13 || piece==14 || piece==24) {
				return new Color(174,20,12);
			}else if((piece>=3 && piece<=5) || (piece>=15 && piece<=17)) {
				return new Color(16,59,164);
			}else if((piece>=6 && piece<=8) || (piece>=18 && piece<=20)) {
				return new Color(255,165,0);
			}else {
				return new Color(1,221,37);
			}
		}else {
			if(piece==1 || piece ==2 || piece==12 || piece==13 || piece==14 || piece==24) {
				return new Color(174,20,12);
			}else if((piece>=3 && piece<=5) || (piece>=15 && piece<=17)) {
				return new Color(1,221,37);
			}else if((piece>=6 && piece<=8) || (piece>=18 && piece<=20)) {
				return new Color(255,165,0);
			}else {
				return new Color(16,59,164);
			}
		}
		
	}
	
	/**Returns whether the Square-one is completely solved or not. Does not like AUF.
	 * 
	 * @return solved or not?
	 */
	public boolean isSolved() {
		if(top.size()==12 && bottom.size()==12) {
			for(int i = 0; i<12; i++) {
				if(top.get(i)!=i-1 || bottom.get(i) != i-13) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static String invert(String scr) {
		String inv = "";
		scr = delim(scr);
		while(scr.contains("/") && scr.contains(",")) {
			if(scr.lastIndexOf("/")>scr.lastIndexOf(",")) {
				inv += "/";
				scr = scr.substring(0,scr.lastIndexOf("/"));
			}else {
				String temp = scr.substring(scr.lastIndexOf("/")+1,scr.length());
				for(int i = 0; i <temp.length();i++) {
					if(Character.isDigit(temp.charAt(i))) {
						temp = temp.substring(0,i) + "-" + temp.substring(i);
						i++;
					}
				}
				inv += temp;
				scr = scr.substring(0,scr.lastIndexOf("/")+1);
			}
		}
		if(scr.contains("/")) {
			inv += "/";
		}else {
			for(int i = 0; i <scr.length();i++) {
				if(Character.isDigit(scr.charAt(i))) {
					scr = scr.substring(0,i) + "-" + scr.substring(i);
					i++;
				}
			}
			inv += scr;
		}
		inv = inv.replace("--", "");
		inv = inv.replace("-0", "0");
		inv = inv.replace("-6", "6");
		return inv;
		
	}
	public static String delim(String alg) {
		//alg = alg.trim();
		if(notation) {
			alg = alg.replace(" ","/");
			alg = alg.replace("E'", "-3,3");
			alg = alg.replace("E", "3,-3");
			alg = alg.replace("e'", "-3,-3");
			alg = alg.replace("e", "3,3");
			alg = alg.replace("u'", "-2,1");
			alg = alg.replace("u", "2,-1");
			alg = alg.replace("d'", "1,-2");
			alg = alg.replace("d", "-1,2");
			alg = alg.replace("U'","-3,0");
			alg = alg.replace("D'","0,-3");
			alg = alg.replace("U","3,0");
			alg = alg.replace("D","0,3");
			alg = alg.replace("M2","/-1,-1/");
			alg = alg.replace("m2","/1,1/");	
			for(int i = 0; i<alg.length()-1;i++) {
				if(Character.isDigit(alg.charAt(i)) && (Character.isDigit(alg.charAt(i+1))||alg.charAt(i+1)=='-')) {
					alg = alg.substring(0,i+1) + "," + alg.substring(i+1);
				}
			}
		}else {
			alg = alg.replace(" ", "");
			
		}
		alg = alg.replace("\n", "");
		alg = alg.replace("(","");
		alg = alg.replace(")","");
		
		//alg = alg.replace(",", "");
		return alg;
	}
	
	/**Checks whether the squan has parity or not
	 * 
	 * @return true if parity, false if not
	 * @precondition the squan must be in cubeshape, or the method will throw InputMismatchException
	 */
	public boolean checkParity() throws InputMismatchException{
		//check for cubeshape, if fails, throw the excpetion
		for(int i = 0; i<3;i++) {
			if(top.get(i)%3==1) {
				for(int j = i+3; j < i+12; j+=3) {
					if(top.get(j%12)%3 != 1) {
						throw new InputMismatchException();
					}
				}
			}
			if(bottom.get(i)%3==1) {
				for(int j = i+3; j < i+12; j+=3) {
					if(bottom.get(j%12)%3 != 1) {
						throw new InputMismatchException();
					}
				}
			}
		}
		
		//somehow trace parity
		boolean parity = false;
		int temp = 0;
		//CSP time 
		ArrayList<Integer> pieces = new ArrayList<Integer>();
		
		for(int i = 0; i < top.size(); i++) {
			pieces.add(top.get(i));			
		}
		for(int i = 0; i < bottom.size(); i++) {
			pieces.add(bottom.get(i));
		}
		for(int i = 0; i < pieces.size(); i++) {
			if(pieces.get(i) == i+1) {
				//parity = !parity;
				continue;
			}else if(i%3==0){
				continue;
			}else {
				parity = !parity;
				pieces.set(pieces.indexOf(i+1),pieces.get(i));
				pieces.set(i,i+1);
			}
		}
		return parity;
	}
	
	
	public void draw(PApplet p) {
		if(!render3D) {//2D
			p.strokeWeight(4);
			p.stroke(0);
			p.fill(174,20,12);
			p.rect(450, 500, 50, 50, 7);
			if(!equator) {
				p.fill(254,58,0);
				p.rect(500, 500, 50, 50, 7);
			}else {
				p.rect(500, 500, 100, 50, 7);
			}
			p.strokeWeight(3);
			//top face: center at 250,300
			for(int i = 0; i < 12; i++) {
				//fill in piece color
				if(whiteTop) {
					if(top.get(i)<=12) {
						p.fill(255);
					}else {
						if(blackTiles) {
							p.fill(0);//black
						}else {
							p.fill(255,255,0);//yellow
						}
					}
				}else {
					if(top.get(i)<=12) {
						if(blackTiles) {
							p.fill(0);//black
						}else {
							p.fill(255,255,0);//yellow
						}
					}else {
						p.fill(255);
					}
				}	
				p.stroke(120);
				if(top.get(i)%3==1) {
					p.triangle(250, 300, (float)(250+Math.sin(i*pi/6+pi/12)*100), (float)(300+Math.cos(i*pi/6+pi/12)*100), (float)(250+Math.sin(i*pi/6-pi/12)*100), (float)(300+Math.cos(i*pi/6-pi/12)*100));
					Color c = gC(top.get(i));
					p.stroke(c.getRed(),c.getGreen(),c.getBlue());
					p.line((float)(250+Math.sin(i*pi/6+pi/12)*100), (float)(300+Math.cos(i*pi/6+pi/12)*100), (float)(250+Math.sin(i*pi/6-pi/12)*100), (float)(300+Math.cos(i*pi/6-pi/12)*100));
					p.stroke(0);
				}else if((top.get(i)%3==0 && top.get(i)<=12) || (top.get(i)%3==2 && top.get(i)>12)){
				}else {
					p.stroke(120);
					p.quad(250, 300, (float) (250 + Math.sin((i - 1) * pi / 6 + pi / 12) * 100),(float) (300 + Math.cos((i - 1) * pi / 6 + pi / 12) * 100),(float) (250 + Math.sin(i * pi / 6 + pi / 12) * 136.60254),(float) (300 + Math.cos(i * pi / 6 + pi / 12) * 136.60254),(float) (250 + Math.sin((i + 1) * pi / 6 + pi / 12) * 100),(float) (300 + Math.cos((i + 1) * pi / 6 + pi / 12) * 100));
					Color c = gC(top.get(i));
					p.stroke(c.getRed(), c.getGreen(), c.getBlue());
					p.line((float) (250 + Math.sin((i - 1) * pi / 6 + pi / 12) * 100),(float) (300 + Math.cos((i - 1) * pi / 6 + pi / 12) * 100),(float) (250 + Math.sin(i * pi / 6 + pi / 12) * 136.60254),(float) (300 + Math.cos(i * pi / 6 + pi / 12) * 136.60254));
					p.stroke(120);
					c = gC(top.get((i+1)%12));
					p.stroke(c.getRed(), c.getGreen(), c.getBlue());
					p.line((float) (250 + Math.sin(i * pi / 6 + pi / 12) * 136.60254),(float) (300 + Math.cos(i * pi / 6 + pi / 12) * 136.60254),(float) (250 + Math.sin((i + 1) * pi / 6 + pi / 12) * 100),(float) (300 + Math.cos((i + 1) * pi / 6 + pi / 12) * 100));
					p.stroke(120);
					i++; //drawing a corner means we go over 2 positions, thus we have to skip one position in the counter
				}
			}
			//bottom face: center at 750, 300
			for(int j = 0; j < 12; j++) {
				if(whiteTop) {
					if(bottom.get(j)<=12) {
						p.fill(255);
					}else {
						if(blackTiles) {
							p.fill(0);//black
						}else {
							p.fill(255,255,0);//yellow
						}
					}
				}else {
					if(bottom.get(j)<=12) {
						if(blackTiles) {
							p.fill(0);//black
						}else {
							p.fill(255,255,0);//yellow
						}
					}else {
						p.fill(255);
					}
				}
				if(bottom.get(j)%3==1) {
					p.triangle(750,300, (float)(750+Math.sin(j*pi/6+pi/12)*100), (float)(300-Math.cos(j*pi/6+pi/12)*100), (float)(750+Math.sin(j*pi/6-pi/12)*100), (float)(300-Math.cos(j*pi/6-pi/12)*100));
					Color c = gC(bottom.get(j));
					p.stroke(c.getRed(),c.getGreen(),c.getBlue());
					p.line((float)(750+Math.sin(j*pi/6+pi/12)*100), (float)(300-Math.cos(j*pi/6+pi/12)*100), (float)(750+Math.sin(j*pi/6-pi/12)*100), (float)(300-Math.cos(j*pi/6-pi/12)*100));
					p.stroke(0);
				}else if((bottom.get(j)%3==2 && bottom.get(j)<=12) || (bottom.get(j)%3==0 && bottom.get(j)>12)){
				}else{
					p.stroke(120);
					p.quad(750, 300, (float) (750 + Math.sin((j - 1) * pi / 6 + pi / 12) * 100),
							(float) (300 -Math.cos((j - 1) * pi / 6 + pi / 12) * 100),
							(float) (750 + Math.sin(j * pi / 6 + pi / 12) * 136.60254),
							(float) (300 -Math.cos(j * pi / 6 + pi / 12) * 136.60254),
							(float) (750 + Math.sin((j + 1) * pi / 6 + pi / 12) * 100),
							(float) (300 -Math.cos((j + 1) * pi / 6 + pi / 12) * 100));
					Color c = gC(bottom.get(j));
					p.stroke(c.getRed(), c.getGreen(), c.getBlue());
					p.line((float) (750 + Math.sin((j - 1) * pi / 6 + pi / 12) * 100),
							(float) (300 -Math.cos((j - 1) * pi / 6 + pi / 12) * 100),
							(float) (750 + Math.sin(j * pi / 6 + pi / 12) * 136.60254),
							(float) (300 -Math.cos(j * pi / 6 + pi / 12) * 136.60254));
					p.stroke(120);
					c = gC(bottom.get((j+1)%12));
					p.stroke(c.getRed(), c.getGreen(), c.getBlue());
					p.line((float) (750 + Math.sin(j * pi / 6 + pi / 12) * 136.60254),
							(float) (300 -Math.cos(j * pi / 6 + pi / 12) * 136.60254),
							(float) (750 + Math.sin((j + 1) * pi / 6 + pi / 12) * 100),
							(float) (300 -Math.cos((j + 1) * pi / 6 + pi / 12) * 100));
					p.stroke(120);
					j++;
				}
			}
		}else {//3D
			p.stroke(80);
			p.strokeWeight(4);
			if(whiteTop) {
					// left cap
				p.fill(1,221,37);
				p.beginShape();
				p.vertex(-125, 45, -125);
				p.vertex(-125, -45, -125);
				p.vertex(-125, -45, 125);
				p.vertex(-125, 45, 125);
				p.vertex(-125, 45, -125);
				p.endShape();
					// right cap	
				if(!equator) {
					p.fill(16,59,164);
					p.beginShape();
					p.vertex(eE/2+(float)((cE+eE)/2*Math.sqrt(3)),-45,-125+(cE+eE)/2);
					p.vertex(eE/2+(float)((cE+eE)/2*Math.sqrt(3)),45,-125+(cE+eE)/2);
					p.vertex(-eE/2+(float)(cE/2*Math.sqrt(3)),45,125+cE/2);
					p.vertex(-eE/2+(float)(cE/2*Math.sqrt(3)),-45,125+cE/2);
					p.vertex(eE/2+(float)((cE+eE)/2*Math.sqrt(3)),-45,-125+(cE+eE)/2);
					p.endShape();
					p.fill(240, 234, 206); //cream
					p.beginShape();
					p.vertex(eE/2+(float)((cE+eE)/2*Math.sqrt(3)),-45,-125+(cE+eE)/2);
					p.vertex(-eE/2+(float)(cE/2*Math.sqrt(3)),-45,125+cE/2);
					p.vertex(-eE/2,-45,125);
					p.vertex(eE/2,-45,-125);
					p.vertex(eE/2+(float)((cE+eE)/2*Math.sqrt(3)),-45,-125+(cE+eE)/2);
					p.endShape();		
					p.beginShape();
					p.vertex(eE/2+(float)((cE+eE)/2*Math.sqrt(3)),45,-125+(cE+eE)/2);
					p.vertex(-eE/2+(float)(cE/2*Math.sqrt(3)),45,125+cE/2);
					p.vertex(-eE/2,45,125);
					p.vertex(eE/2,45,-125);
					p.vertex(eE/2+(float)((cE+eE)/2*Math.sqrt(3)),45,-125+(cE+eE)/2);
					p.endShape();
					p.beginShape();
					p.vertex(-125,-45,-125);
					p.vertex(-125,-45,125);
					p.vertex(-eE/2,-45,125);
					p.vertex(eE/2,-45,-125);
					p.vertex(-125,-45,-125);
					p.endShape();
					p.beginShape();
					p.vertex(-125,45,-125);
					p.vertex(-125,45,125);
					p.vertex(-eE/2,45,125);
					p.vertex(eE/2,45,-125);
					p.vertex(-125,45,-125);
					p.endShape();
				}else {
					p.fill(16,59,164);
					p.beginShape();
					p.vertex(125, 45, -125);
					p.vertex(125, -45, -125);
					p.vertex(125, -45, 125);
					p.vertex(125, 45, 125);
					p.vertex(125, 45, -125);
					p.endShape();
					p.fill(240, 234, 206); //cream
					p.beginShape();
					p.vertex(-125,45,-125);
					p.vertex(125,45,-125);
					p.vertex(125,45,125);
					p.vertex(-125,45,125);
					p.vertex(-125,45,-125);
					p.endShape();
					p.beginShape();
					p.vertex(-125,-45,-125);
					p.vertex(125,-45,-125);
					p.vertex(125,-45,125);
					p.vertex(-125,-45,125);
					p.vertex(-125,-45,-125);
					p.endShape();
				}
				

			}else{
					// left cap
				p.fill(16,59,164);
				p.beginShape();
				p.vertex(-125, 45, -125);
				p.vertex(-125, -45, -125);
				p.vertex(-125, -45, 125);
				p.vertex(-125, 45, 125);
				p.vertex(-125, 45, -125);
				p.endShape();
					// right cap	
				if(!equator) {
					p.fill(1,221,37);
					p.beginShape();
					p.vertex(eE/2+(float)((cE+eE)/2*Math.sqrt(3)),-45,-125+(cE+eE)/2);
					p.vertex(eE/2+(float)((cE+eE)/2*Math.sqrt(3)),45,-125+(cE+eE)/2);
					p.vertex(-eE/2+(float)(cE/2*Math.sqrt(3)),45,125+cE/2);
					p.vertex(-eE/2+(float)(cE/2*Math.sqrt(3)),-45,125+cE/2);
					p.vertex(eE/2+(float)((cE+eE)/2*Math.sqrt(3)),-45,-125+(cE+eE)/2);
					p.endShape();
					p.fill(240, 234, 206); //cream
					p.beginShape();
					p.vertex(eE/2+(float)((cE+eE)/2*Math.sqrt(3)),-45,-125+(cE+eE)/2);
					p.vertex(-eE/2+(float)(cE/2*Math.sqrt(3)),-45,125+cE/2);
					p.vertex(-eE/2,-45-125);
					p.vertex(eE/2,-45,125);
					p.vertex(eE/2+(float)((cE+eE)/2*Math.sqrt(3)),-45,-125+(cE+eE)/2);
					p.endShape();		
					p.beginShape();
					p.vertex(eE/2+(float)((cE+eE)/2*Math.sqrt(3)),45,-125+(cE+eE)/2);
					p.vertex(-eE/2+(float)(cE/2*Math.sqrt(3)),45,125+cE/2);
					p.vertex(-eE/2,45-125);
					p.vertex(eE/2,45,125);
					p.vertex(eE/2+(float)((cE+eE)/2*Math.sqrt(3)),45,-125+(cE+eE)/2);
					p.endShape();
					p.beginShape();
					p.vertex(-125,-45,-125);
					p.vertex(-125,-45,125);
					p.vertex(-eE/2,-45,125);
					p.vertex(eE/2,-45,-125);
					p.vertex(-125,-45,-125);
					p.endShape();
					p.beginShape();
					p.vertex(-125,45,-125);
					p.vertex(-125,45,125);
					p.vertex(-eE/2,45,125);
					p.vertex(eE/2,45,-125);
					p.vertex(-125,45,-125);
					p.endShape();
				}else {
					p.fill(1,221,37);
					p.beginShape();
					p.vertex(125, 45, -125);
					p.vertex(125, -45, -125);
					p.vertex(125, -45, 125);
					p.vertex(125, 45, 125);
					p.vertex(125, 45, -125);
					p.endShape();
					p.fill(240, 234, 206); //cream
					p.beginShape();
					p.vertex(-125,45,-125);
					p.vertex(125,45,-125);
					p.vertex(125,45,125);
					p.vertex(-125,45,125);
					p.vertex(-125,45,-125);
					p.endShape();
					p.beginShape();
					p.vertex(-125,-45,-125);
					p.vertex(125,-45,-125);
					p.vertex(125,-45,125);
					p.vertex(-125,-45,125);
					p.vertex(-125,-45,-125);
					p.endShape();
				}
				
			}
				//thumbGrip
			p.fill(174,20,12);
			p.beginShape();
			p.vertex(-125,45,125);
			p.vertex(-125,-45,125);
			p.vertex(-eE/2,-45,125);
			p.vertex(-eE/2,45,125);
			p.vertex(-125,45,125);
			p.endShape();
				//backGrip
			p.fill(255,165,0);
			p.beginShape();
			p.vertex(-125,45,-125);
			p.vertex(-125,-45,-125);
			p.vertex(eE/2,-45,-125);
			p.vertex(eE/2,45,-125);
			p.vertex(-125,45,-125);
			p.endShape();
			if(!equator) {
					//eqfront
				p.fill(255,165,0);
				p.beginShape();
				p.vertex(-eE/2,45,125);
				p.vertex(-eE/2,-45,125);
				p.vertex(-eE/2+(float)(cE/2*Math.sqrt(3)),-45,125+cE/2);
				p.vertex(-eE/2+(float)(cE/2*Math.sqrt(3)),45,125+cE/2);
				p.vertex(-eE/2,45,125);
				p.endShape();
					//eqback
				p.fill(174,20,12);
				p.beginShape();
				p.vertex(eE/2,45,-125);
				p.vertex(eE/2,-45,-125);
				p.vertex(eE/2+(float)((cE+eE)/2*Math.sqrt(3)),-45,-125+(cE+eE)/2);
				p.vertex(eE/2+(float)((cE+eE)/2*Math.sqrt(3)),45,-125+(cE+eE)/2);
				p.vertex(eE/2,45,-125);
				p.endShape();
			}else {
					//eqfront
				p.fill(174,20,12);
				p.beginShape();
				p.vertex(125,45,125);
				p.vertex(125,-45,125);
				p.vertex(-eE/2,-45,125);
				p.vertex(-eE/2,45,125);
				p.vertex(125,45,125);
				p.endShape();
					//eqback
				p.fill(255,165,0);
				p.beginShape();
				p.vertex(125,45,-125);
				p.vertex(125,-45,-125);
				p.vertex(eE/2,-45,-125);
				p.vertex(eE/2,45,-125);
				p.vertex(125,45,-125);
				p.endShape();
			}
			
			//top tiles
			
			for(int i = 0; i < 12; i++) {
				//fill in piece color
				if(whiteTop) {
					if(top.get(i)<=12) {
						p.fill(255);
					}else {
						if(blackTiles) {
							p.fill(0);//black
						}else {
							p.fill(255,255,0);//yellow
						}
					}
				}else {
					if(top.get(i)<=12) {
						if(blackTiles) {
							p.fill(0);//black
						}else {
							p.fill(255,255,0);//yellow
						}
					}else {
						p.fill(255);
					}
				}	
				if(top.get(i)%3==1) {
					p.beginShape();
					p.vertex(0,-125,0);
					p.vertex((float)(Math.sin(i*pi/6+pi/12)*sD),-125,(float)(Math.cos(i*pi/6+pi/12)*sD));
					p.vertex((float)(Math.sin(i*pi/6-pi/12)*sD),-125,(float)(Math.cos(i*pi/6-pi/12)*sD));
					p.vertex(0,-125,0);
					p.endShape();
					p.fill(240, 234, 206); //cream
					p.beginShape();
					p.vertex(0,-125,0);
					p.vertex((float)(Math.sin(i*pi/6+pi/12)*sD),-45,(float)(Math.cos(i*pi/6+pi/12)*sD));
					p.vertex((float)(Math.sin(i*pi/6-pi/12)*sD),-45,(float)(Math.cos(i*pi/6-pi/12)*sD));
					p.vertex(0,-125,0);
					p.endShape();
					Color c = gC(top.get(i));
					p.fill(c.getRed(),c.getGreen(),c.getBlue());
					p.beginShape();
					p.vertex((float)(Math.sin(i*pi/6+pi/12)*sD),-125,(float)(Math.cos(i*pi/6+pi/12)*sD));
					p.vertex((float)(Math.sin(i*pi/6-pi/12)*sD),-125,(float)(Math.cos(i*pi/6-pi/12)*sD));
					p.vertex((float)(Math.sin(i*pi/6-pi/12)*sD),-45,(float)(Math.cos(i*pi/6-pi/12)*sD));
					p.vertex((float)(Math.sin(i*pi/6+pi/12)*sD),-45,(float)(Math.cos(i*pi/6+pi/12)*sD));
					p.vertex((float)(Math.sin(i*pi/6+pi/12)*sD),-125,(float)(Math.cos(i*pi/6+pi/12)*sD));
					p.endShape();
				}else if((top.get(i)%3==0 && top.get(i)<=12) || (top.get(i)%3==2 && top.get(i)>12)){
				}else {
					p.beginShape();
					p.vertex(0,-125,0);
					p.vertex((float)(Math.sin((i-1)*pi/6+pi/12) * sD),-125,(float)(Math.cos((i-1)*pi/6+pi/12)*sD));
					p.vertex((float)(Math.sin(i*pi/6+pi/12) * bD),-125,(float)(Math.cos(i*pi/6+pi/12)*bD));
					p.vertex((float)(Math.sin((i+1)*pi/6+pi/12)*sD),-125,(float)(Math.cos((i+1)*pi/6+pi/12)*sD));
					p.vertex(0,-125,0);
					p.endShape();
					p.fill(240, 234, 206); //cream
					p.beginShape();
					p.vertex(0,-45,0);
					p.vertex((float)(Math.sin((i-1)*pi/6+pi/12) * sD),-45,(float)(Math.cos((i-1)*pi/6+pi/12)*sD));
					p.vertex((float)(Math.sin(i*pi/6+pi/12) * bD),-45,(float)(Math.cos(i*pi/6+pi/12)*bD));
					p.vertex((float)(Math.sin((i+1)*pi/6+pi/12)*sD),-45,(float)(Math.cos((i+1)*pi/6+pi/12)*sD));
					p.vertex(0,-45,0);
					p.endShape();
					Color c = gC(top.get(i));
					p.fill(c.getRed(),c.getGreen(),c.getBlue());
					p.beginShape();
					p.vertex((float)(Math.sin((i-1)*pi/6+pi/12) * sD),-125,(float)(Math.cos((i-1)*pi/6+pi/12)*sD));
					p.vertex((float)(Math.sin(i*pi/6+pi/12) * bD),-125,(float)(Math.cos(i*pi/6+pi/12)*bD));
					p.vertex((float)(Math.sin(i*pi/6+pi/12) * bD),-45,(float)(Math.cos(i*pi/6+pi/12)*bD));
					p.vertex((float)(Math.sin((i-1)*pi/6+pi/12) * sD),-45,(float)(Math.cos((i-1)*pi/6+pi/12)*sD));
					p.vertex((float)(Math.sin((i-1)*pi/6+pi/12) * sD),-125,(float)(Math.cos((i-1)*pi/6+pi/12)*sD));
					p.endShape();
					c = gC(top.get((i+1)%12));
					p.fill(c.getRed(),c.getGreen(),c.getBlue());
					p.beginShape();
					p.vertex((float)(Math.sin(i*pi/6+pi/12) * bD),-125,(float)(Math.cos(i*pi/6+pi/12)*bD));
					p.vertex((float)(Math.sin((i+1)*pi/6+pi/12)*sD),-125,(float)(Math.cos((i+1)*pi/6+pi/12)*sD));
					p.vertex((float)(Math.sin((i+1)*pi/6+pi/12)*sD),-45,(float)(Math.cos((i+1)*pi/6+pi/12)*sD));
					p.vertex((float)(Math.sin(i*pi/6+pi/12) * bD),-45,(float)(Math.cos(i*pi/6+pi/12)*bD));
					p.vertex((float)(Math.sin(i*pi/6+pi/12) * bD),-125,(float)(Math.cos(i*pi/6+pi/12)*bD));
					p.endShape();
					i++; //drawing a corner means we go over 2 positions, thus we have to skip one position in the counter
					
					
				}
			}
			
			//bottom tiles
			
			for(int i = 0; i < 12; i++) {
				//fill in piece color
				if(whiteTop) {
					if(bottom.get(i)<=12) {
						p.fill(255);
					}else {
						if(blackTiles) {
							p.fill(0);//black
						}else {
							p.fill(255,255,0);//yellow
						}
					}
				}else {
					if(bottom.get(i)<=12) {
						if(blackTiles) {
							p.fill(0);//black
						}else {
							p.fill(255,255,0);//yellow
						}
					}else {
						p.fill(255);
					}
				}	
				if(bottom.get(i)%3==1) {
					p.beginShape();
					p.vertex(0,125,0);
					p.vertex((float)(Math.sin(i*pi/6+pi/12)*sD),125,(float)(Math.cos(i*pi/6+pi/12)*sD));
					p.vertex((float)(Math.sin(i*pi/6-pi/12)*sD),125,(float)(Math.cos(i*pi/6-pi/12)*sD));
					p.vertex(0,125,0);
					p.endShape();
					p.fill(240, 234, 206); //cream
					p.beginShape();
					p.vertex(0,45,0);
					p.vertex((float)(Math.sin(i*pi/6+pi/12)*sD),45,(float)(Math.cos(i*pi/6+pi/12)*sD));
					p.vertex((float)(Math.sin(i*pi/6-pi/12)*sD),45,(float)(Math.cos(i*pi/6-pi/12)*sD));
					p.vertex(0,45,0);
					p.endShape();
					Color c = gC(bottom.get(i));
					p.fill(c.getRed(),c.getGreen(),c.getBlue());
					p.beginShape();
					p.vertex((float)(Math.sin(i*pi/6+pi/12)*sD),125,(float)(Math.cos(i*pi/6+pi/12)*sD));
					p.vertex((float)(Math.sin(i*pi/6-pi/12)*sD),125,(float)(Math.cos(i*pi/6-pi/12)*sD));
					p.vertex((float)(Math.sin(i*pi/6-pi/12)*sD),45,(float)(Math.cos(i*pi/6-pi/12)*sD));
					p.vertex((float)(Math.sin(i*pi/6+pi/12)*sD),45,(float)(Math.cos(i*pi/6+pi/12)*sD));
					p.vertex((float)(Math.sin(i*pi/6+pi/12)*sD),125,(float)(Math.cos(i*pi/6+pi/12)*sD));
					p.endShape();
				}else if((bottom.get(i)%3==2 && bottom.get(i)<=12) || (bottom.get(i)%3==0 && bottom.get(i)>12)){
				}else {
					p.beginShape();
					p.vertex(0,125,0);
					p.vertex((float)(Math.sin((i-1)*pi/6+pi/12) * sD),125,(float)(Math.cos((i-1)*pi/6+pi/12)*sD));
					p.vertex((float)(Math.sin(i*pi/6+pi/12) * bD),125,(float)(Math.cos(i*pi/6+pi/12)*bD));
					p.vertex((float)(Math.sin((i+1)*pi/6+pi/12)*sD),125,(float)(Math.cos((i+1)*pi/6+pi/12)*sD));
					p.vertex(0,125,0);
					p.endShape();
					p.fill(240, 234, 206); //cream
					p.beginShape();
					p.vertex(0,45,0);
					p.vertex((float)(Math.sin((i-1)*pi/6+pi/12) * sD),45,(float)(Math.cos((i-1)*pi/6+pi/12)*sD));
					p.vertex((float)(Math.sin(i*pi/6+pi/12) * bD),45,(float)(Math.cos(i*pi/6+pi/12)*bD));
					p.vertex((float)(Math.sin((i+1)*pi/6+pi/12)*sD),45,(float)(Math.cos((i+1)*pi/6+pi/12)*sD));
					p.vertex(0,45,0);
					p.endShape();
					Color c = gC(bottom.get(i));
					p.fill(c.getRed(),c.getGreen(),c.getBlue());
					p.beginShape();				
					p.vertex((float)(Math.sin((i-1)*pi/6+pi/12) * sD),125,(float)(Math.cos((i-1)*pi/6+pi/12)*sD));
					p.vertex((float)(Math.sin(i*pi/6+pi/12) * bD),125,(float)(Math.cos(i*pi/6+pi/12)*bD));
					p.vertex((float)(Math.sin(i*pi/6+pi/12) * bD),45,(float)(Math.cos(i*pi/6+pi/12)*bD));
					p.vertex((float)(Math.sin((i-1)*pi/6+pi/12) * sD),45,(float)(Math.cos((i-1)*pi/6+pi/12)*sD));
					p.vertex((float)(Math.sin((i-1)*pi/6+pi/12) * sD),125,(float)(Math.cos((i-1)*pi/6+pi/12)*sD));
					p.endShape();
					c = gC(bottom.get((i+1)%12));
					p.fill(c.getRed(),c.getGreen(),c.getBlue());
					p.beginShape();
					p.vertex((float)(Math.sin(i*pi/6+pi/12) * bD),125,(float)(Math.cos(i*pi/6+pi/12)*bD));
					p.vertex((float)(Math.sin((i+1)*pi/6+pi/12)*sD),125,(float)(Math.cos((i+1)*pi/6+pi/12)*sD));
					p.vertex((float)(Math.sin((i+1)*pi/6+pi/12)*sD),45,(float)(Math.cos((i+1)*pi/6+pi/12)*sD));
					p.vertex((float)(Math.sin(i*pi/6+pi/12) * bD),45,(float)(Math.cos(i*pi/6+pi/12)*bD));
					p.vertex((float)(Math.sin(i*pi/6+pi/12) * bD),125,(float)(Math.cos(i*pi/6+pi/12)*bD));
					p.endShape();
					i++; //drawing a corner means we go over 2 positions, thus we have to skip one position in the counter					
					
				}
			}
			
			//DEBUG SPHERE
			p.stroke(0);
			p.sphere(5);
		}
	}
}
