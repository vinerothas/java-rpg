package game;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Formatter;
import java.util.Iterator;
import java.util.LinkedList;

import graphics.DataUI;
import graphics.DrawOld;
import graphics.Window;
import input.KeyboardManagerOld;
import input.MouseManagerOld;

public class MainMenu {

	protected DrawOld d;
	public DataUI dui;
	public Region r;
	GameManager gm;
	public boolean insertState = true;
	public String[] games = new String[10];
	
	/*
	public static void main(String args[]){
		new MainMenu().run();
	}
	*/
	
	private void run(){
		dui = new DataUI();
		dui.setPState(5);
		r = new Region();
		gm = new GameManager();
		Window w = new Window();
		w.initJFrame(500,620);	
		gm.dui=dui;
		d = new DrawOld(w, r, dui,gm);
		new MouseManagerOld(w.canvas, dui);
		new KeyboardManagerOld(w.canvas, gm);
		
		getSavedGames();
		
		menuLoop();
		System.exit(0);
	}
	
	public void backToMenu(DrawOld d, DataUI dui, Region r){
		this.d = d;
		this.dui = dui;
		this.r = r;
		getSavedGames();
		//menuLoop(); menu loop powinienc byc automatycznie wznawiany poprzez konczenie petli w game manager
	}
	
	private void menuLoop(){
		long lastTime = System.currentTimeMillis();
		long elapsedTime;
		long insertTimer = 0;
		
		while(true){
			elapsedTime = System.currentTimeMillis() - lastTime;
			lastTime += elapsedTime;
			if(elapsedTime==0)continue;
			insertTimer+=elapsedTime;
			if(insertTimer>800){
				insertTimer=0;
				insertState=!insertState;
			}
			int state = dui.getPState();
			if(state==8){
				break;
			}else if(state==9){
				try{
					startGame();
				}catch (IOException ex) {
					dui.setPState(5);
					ex.printStackTrace();
					System.out.println("exception line 93 main menu");
				}
			}else if(state==13){
				int save = dui.getSavePicked();
				if(games[save-1]!=null){
					String gameName = games[save-1];
					loadGame();
					newGame(gameName,true);
				}else {
					dui.setSavePicked(0);
					dui.setPState(7);
				}
			}
			d.drawMenu(this);
		}
	}
	
	private void startGame() throws IOException{
		String jarPath = getJarPath();
		String name = dui.getNgInput();
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
				if(s.contentEquals(name)){
					dui.setPState(10);
					return;
				}
			}
		}
		
		File gameFolder = new File(jarPath+"\\save\\"+name);
		gameFolder.mkdir();
		Formatter x = new Formatter(jarPath+"\\save\\"+"saves.txt");
		x.format(name);
		Iterator<String> i = names.iterator();
		while(i.hasNext()){
			x.format("\n");
			x.format(i.next());
		}
		x.close();	
		dui.setPState(0);
		newGame(name,false);
	}
	
	private void loadGame(){
		SaveLoad sl = new SaveLoad();
		try{
			String gameName = games[dui.getSavePicked()-1];
			Point rPoint = sl.getLastRegion(gameName);
			r.gameName=gameName;
			sl.loadMap(r, d.h, rPoint.x, rPoint.y);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		dui.setPState(0);
	}
	
	private void newGame(String gamename, boolean load){
		DrawOld dtemp = d;
		DataUI duitemp = dui;
		Region rtemp = r;
		rtemp.gameName = gamename;
		d = null;
		dui = null;
		r = null;
		gm.run(dtemp, duitemp, rtemp, this,load);
	}
	
	private void getSavedGames(){
		
		try{
			File saves = new File(getJarPath()+"\\save\\"+"saves.txt");
			BufferedReader reader = new BufferedReader(new FileReader(saves));
			int index = 0;
			while(true){
				String line = reader.readLine();
				if(line==null){
					reader.close();
					break;
				}
				games[index]=line;
				index++;
			}
			reader.close();
			}catch (Exception ex){
				
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
