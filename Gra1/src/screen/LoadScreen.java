package screen;

import input.Button;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Formatter;
import java.util.LinkedList;

import game.Constants;
import game.SaveLoad;
import game.ScreenManager;
import graphics.UpdatePlayerAnim;

public class LoadScreen extends Screen{
	
	private Button backButton;
	private Button loadButton;
	private Button deleteButton;
	private String[] games = new String[10];
	private Point listPos;
	private int saveChosen = 0;
	private boolean askDelete = false;

	public LoadScreen(ScreenManager sm) {
		super(sm,Constants.MENU_ZOOM);
		
		getSavedGames();

		int w =  getDrawWidth() / 2; //310
		int h = getDrawHeight() / 2; //250
		listPos = new Point(w,h-166);
		int bw = 200; //button width
		int bh = 60; //button height
		//arbitrary variables
		int x1 = w-250;
		int w1 = x1+bw;
		int h1 = h-130;
		int h2 = h-50;
		int h3 = h+30;
		backButton = new Button(x1,h3,w1,h3+bh);
		loadButton = new Button(x1,h1,w1,h1+bh);
		deleteButton = new Button(x1,h2,w1,h2+bh);
	}

	@Override
	public void loop(long elapsedTime) {
		checkInput();
		
	}

	@Override
	protected void checkInput() {
		Point m = sm.dui.mouseClicked;
		if(m==null)return;
		
		int containerx = listPos.x;
		int containery= listPos.y;
		int x1 = containerx+6;
		int x2 = x1+224;
		for(int i = 1;i<11;i++){
			int by=containery+2+(i*25);
			Button b = new Button(x1,by,x2,by+22);//196, by,420,by+22
			if(b.isClicked(m)){
				if(games[i-1]==null)return;
				saveChosen = i;
				askDelete = false;
				return;
			}
		}
		
		if(backButton.isClicked(m)){
			sm.setScreen(new MainMenuScreen(sm));
		}else if(loadButton.isClicked(m)){
			//TODO load
			if(saveChosen>0){
				loadGame();
				sm.setScreen(new GameScreen(sm));
			}
		}else if(deleteButton.isClicked(m)){
			//TODO check if working on actual saves
			if(!askDelete&&saveChosen>0){
				askDelete = true;
			}else if(saveChosen>0){
				deletePickedSave();
				askDelete = false;
				saveChosen = 0;
				getSavedGames();
			}
		}else{
			askDelete = false;
			saveChosen = 0;
		}
		sm.dui.clearInput();
	}

	@Override
	public void draw(Graphics2D g) {
		sm.initiateMenuGraphics(g);
		
		Point b1 = backButton.getPos();
		Point b2 = loadButton.getPos();
		Point b3 = deleteButton.getPos();
		g.drawImage(sm.h.menuImgs[1],b1.x,b1.y,null);//5 100,400
		g.drawImage(sm.h.menuImgs[1],b2.x,b2.y,null);//6 320,400
		g.drawImage(sm.h.menuImgs[1],b3.x,b3.y,null);
		g.drawString("Back", b1.x+61, b1.y+40); //161 440
		g.drawString("Load", b2.x+63, b2.y+40); //333 440
		if(!askDelete)g.drawString("Delete", b3.x+53, b3.y+40);
		else g.drawString("Confirm", b3.x+42, b3.y+40);
		
		int x = listPos.x;
		int y = listPos.y;
		g.drawImage(sm.h.menuImgs[2],x,y,240,300,null);//190,80
		//g.drawImage(h.menuImgs[2],70,80,480,300,null); dla podwojnego
		
		g.setFont(new Font("Monospaced",Font.BOLD,20));
		g.setColor(new Color(255,255,255));
		for(int i = 0;i<games.length;i++){
			if(games[i]==null)break;
			g.drawString(games[i], x+10, y+42+(i*25));
			if(saveChosen==i+1)g.drawRect(x+7, y+26+(i*25), 225, 24);
		}
	}
	
	private void getSavedGames(){
		
		try{
			games = new String[10];
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
	
	private void loadGame(){
		SaveLoad sl = new SaveLoad();
		try{
			String gameName = games[saveChosen-1];
			Point rPoint = sl.getLastRegion(gameName);
			sm.r.gameName=gameName;
			sl.loadMap(sm.r, sm.h, rPoint.x, rPoint.y);
			sm.updatePlayerAnim();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/** Doesn't support folders inside the main save folder of the savegame */
	private void deletePickedSave(){
		//delete files
		String gameName = games[saveChosen-1];
		File saveFolder = new File(getJarPath()+"\\save\\"+gameName);
		File[] files = saveFolder.listFiles();
		if(files!=null){
			for(File f : files)f.delete();
		}
		saveFolder.delete();
		
		//delete saves.txt entry
		try{
			BufferedReader reader = new BufferedReader(new FileReader(getJarPath()+"save\\saves.txt"));
			LinkedList<String> lines = new LinkedList<String>();
			while(true){
				String line = reader.readLine();
				if(line==null){
					reader.close();
					break;
				}
				lines.add(line);
			}
			reader.close();	
			
			Formatter x = new Formatter(getJarPath()+"\\save\\saves.txt");
			for(String s : lines){
				if(s.equals(gameName))continue;
				x.format(s+"\n");
			}
			x.close();
		}catch(Exception ex){
			ex.printStackTrace();
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
