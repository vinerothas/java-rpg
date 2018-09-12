package game;

import graphics.DataUI;
import graphics.ImageHolder;
import graphics.UpdatePlayerAnim;
import graphics.Window;
import input.KeyboardConfig;
import input.KeyboardManager;
import input.MouseManager;
import items.InventoryManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import screen.CharacterScreen;
import screen.Screen;
import screen.GameScreen;
import screen.PauseScreen;
import screen.MainMenuScreen;;

public class ScreenManager {
	
	//4:3 640 480 ; 800 600 ; 400 300
	//16:9 800 450 ; 1200 675 ; 1040 585 ; 1280 720
	//if (zoom==1.0) y<=1000
	public int windowWidth = 800;
	public int windowHeight = 600;
	public float zoom = (float) 2.0;
	
	public DataUI dui;
	public Region r;
	public Window w;
	public ImageHolder h;
	private Screen screen;
	public KeyboardConfig kc;
	
	public static void main(String args[]){
		new ScreenManager().run();
	}
	
	private void run(){
		dui = new DataUI();
		dui.setPState(5);
		dui.zoom = zoom;
		r = new Region();
		kc = new KeyboardConfig();
		w = new Window();
		w.initJFrame(windowWidth,windowHeight);	
		
		if(w.fullscreen){
			windowWidth = w.canvas.getWidth(); 
			windowHeight = w.canvas.getHeight();
		}
		
		w.window.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				if(screen instanceof GameScreen
					||screen instanceof PauseScreen
					||screen instanceof CharacterScreen)
					saveGame();
				System.exit(0);
			}
		} );
		h = new ImageHolder(w);
		new KeyboardManager(w.window, dui);
		new MouseManager(w.canvas, dui);
		
		setScreen(new MainMenuScreen(this));		
		mainLoop();

		
		System.exit(0);
	}
	
	private void mainLoop(){
		long lastTime = System.currentTimeMillis();
		long elapsedTime;
		long second = 0;
		int frames = 0;
		
		while(true){
			elapsedTime = System.currentTimeMillis() - lastTime;
			lastTime += elapsedTime;
			second += elapsedTime;
			//if(elapsedTime==0)continue;
			frames++;
			if(second>=1000){
				//System.out.println("FPS: "+frames); XXX
				frames = 0;
				second = 0;
			}
			
			screen.loop(elapsedTime);
			
			draw();
		}
	}
	
	private void draw(){
		Graphics2D g = (Graphics2D) w.buffer.getGraphics();
		
		screen.draw(g);
		
		w.graphics = w.bs.getDrawGraphics();
		w.graphics.drawImage(w.buffer,0,0,screen.getSizeX(),screen.getSizeY(),null);       
        w.bs.show();       
        if (w.graphics != null) w.graphics.dispose();
        if (g != null) g.dispose();
	}
	
	public void setScreen(Screen screen){
		this.screen = screen;
		dui.zoom = screen.getZoom();
	}
	
	public void updatePlayerAnim(){
		new UpdatePlayerAnim().loadPlayerAnim(r.getPlayer(), h,w);
	}
	
	public void initiateMenuGraphics(Graphics2D g){
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g.setColor(new Color(0,0,0));
		g.setFont(new Font("Dialog",Font.BOLD,32));
		g.drawImage(h.menuImgs[0], 0, 0, screen.getDrawWidth(),screen.getDrawHeight(), null);
	}
	
	public void saveGame(){
		SaveLoad s = new SaveLoad();
		s.makeFile(r);
	}
	
	public void quit(){
		System.exit(0);
	}
}
