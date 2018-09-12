package screen;

import input.Button;
import input.KeyAction;
import items.Armor;
import items.InventoryManager;
import items.Weapon;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Formatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import game.Constants;
import game.InitRegion;
import game.ScreenManager;

public class NewGameScreen extends Screen{

	private Button newGameButton;
	private Button backButton;
	private boolean nameTaken = false;
	private boolean drawInsertBar = true;
	private long insertBarTime = 0;
	private final long insertBarTick = 500;
	private String inputString = "new_game";
	private char[] supportedChars;
	private final int inputLimit = 18;
	
	public NewGameScreen(ScreenManager sm) {
		super(sm, Constants.MENU_ZOOM);

		int w =  getDrawWidth() / 2;
		int h = getDrawHeight() / 2;
		int bw = 200; //button width
		int bh = 60; //button height
		//arbitrary variables
		int x1 = w-100;
		int w1 = x1+bw;
		int h4 = h-25;
		int h5 = h+70;
		newGameButton = new Button(x1,h4,w1,h4+bh); //210,225,410,285
		backButton = new Button(x1,h5,w1,h5+bh); //210,320,410,380
		
		supportedChars = new char[]{'q','w','e','r','t','y','u','i','o','p','a','s','d','f','g','h','j','k','l','z','x','c','v','b','n','m',
				'_','-','Q','W','E','R','T','Y','U','I','O','P','A','S','D','F','G','H','J','K','L','Z','X','C','V','B','N','M',
				'1','2','3','4','5','6','7','8','9','0'};
	}

	@Override
	public void loop(long elapsedTime) {
		insertBarTime+=elapsedTime;
		if(insertBarTime>insertBarTick){
			insertBarTime = 0;
			drawInsertBar = !drawInsertBar;
		}
		//System.out.println(6357272*357538);
		
		checkInput();
	}
	
	@Override
	protected void checkInput() {
		KeyAction[] keyActions = sm.dui.getKeyActions();
		sm.dui.clearKeyActions();
		for(KeyAction a : keyActions){
			if(a==null)break;
			if(a.isReleased())continue;
			if (a.isCharacter()){
				char c = a.getCharCode();
				if(checkSupported(c)){
					addToInput(c+"");
				}else{
					int k = a.getKeyCode();
					//System.out.println(k);
					final int backspace = 8;
					if(k == backspace){
						substractInput();
					}
				}
			}
		}
		
		Point m = sm.dui.mouseClicked;
		if(m==null)return;
		if(newGameButton.isClicked(m)){
			if(!inputString.isEmpty()&&inputString!=""){
				try {
					initiateGameFiles();
					//XXX remove initiating random name
					if(nameTaken){
						Random r = new Random();
						System.out.println("Warning, name taken, initiating random name"); 
						int rand = 0;
						while(nameTaken){
							rand = r.nextInt(9000000)+100000;
							inputString = "temp"+rand;
							initiateGameFiles();
						}
						System.out.println("initialized "+inputString);
					}
					if(nameTaken==false){
						InitRegion ir = new InitRegion();
						ir.init(sm.r,0,0);
						sm.r.gameName = inputString;
						sm.r.getPlayer().addWeapon(new Weapon(10,1,1,2));
						
						//InventoryManager.addItem(sm.r.getPlayer().getInventory(),new Weapon(12,1,1,Constants.ITEM_QUALITY_GOOD));
						sm.updatePlayerAnim();
						sm.setScreen(new GameScreen(sm));
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}else if(backButton.isClicked(m)){
			sm.setScreen(new MainMenuScreen(sm));
		}
		sm.dui.clearInput();
	}
	
	private void initiateGameFiles() throws IOException{
		String jarPath = getJarPath();
		File folder = new File(jarPath+"\\save\\");
		if(folder.exists()==false){
			folder.mkdir();			
		}
		File saves = new File(jarPath+"\\save\\"+"saves.txt");
		LinkedList<String> names = new LinkedList<String>();
		if(saves.exists()==false){
			saves.createNewFile();
		}else{
			BufferedReader reader = new BufferedReader(new FileReader(saves));
			while(true){
				String line = reader.readLine();
				if(line==null){
					reader.close();
					break;
				}
				names.add(line);
			}
			reader.close();
		}
		if(names.isEmpty()==false){
			Iterator<String> i = names.iterator();
			while(i.hasNext()){
				String s = i.next();
				//System.out.println(s);
				//System.out.println(name);
				if(s.contentEquals(inputString)){
					nameTaken=true;
					return;
				}
			}
		}
		nameTaken = false;
		File gameFolder = new File(jarPath+"\\save\\"+inputString);
		gameFolder.mkdir();
		Formatter x = new Formatter(jarPath+"\\save\\"+"saves.txt");
		x.format(inputString);
		Iterator<String> i = names.iterator();
		while(i.hasNext()){
			x.format("\n");
			x.format(i.next());
		}
		x.close();	
	}
	
	private void addToInput(String s){
		if(inputString.length()<inputLimit)inputString = inputString+s;
	}
	
	private void substractInput(){
		if(!inputString.isEmpty())inputString=inputString.substring(0, inputString.length()-1);
	}
	
	private boolean checkSupported(char c){
		for(Character sc: supportedChars){
			if(sc==c)return true;
		}
		return false;
	}

	public void draw(Graphics2D g) {
		sm.initiateMenuGraphics(g);
		
		Point b1 = newGameButton.getPos();
		Point b2 = backButton.getPos();
		g.drawImage(sm.h.menuImgs[1],b1.x,b1.y,null);//210 225
		g.drawImage(sm.h.menuImgs[1],b2.x,b2.y,null);//210 320
		g.drawString("Start Game", b1.x+14, b1.y+40); //224 265
		g.drawString("Back", b2.x+61, b2.y+40); //271 360
					
		int insertX = (getDrawWidth()/2)-120;
		int insertY = (getDrawHeight()/2)-150;
		g.drawImage(sm.h.menuImgs[2],insertX,insertY,240,60,null);//190, 100
		g.setColor(Color.white);
		if(nameTaken){
			g.drawString("Name Taken", insertX+27, insertY+95);//217 195
		}
		g.setFont(new Font("Monospaced",Font.BOLD,20));
		g.drawString(inputString, insertX+14, insertY+35);
		int s = inputString.length();
		if(drawInsertBar&&s<inputLimit){
			g.setColor(new Color(255,255,255));
			double l = s*12;
			g.fillRect(insertX+16+(int)l,insertY+15,2,30);
		}
		
	}
	
	private String getJarPath(){
		String jarPath = "";
		String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		try {
			jarPath = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		jarPath = jarPath.replace('/', '\\');
		jarPath = jarPath.substring(0, jarPath.lastIndexOf("\\")+1);
		return jarPath;
	}
	
}
