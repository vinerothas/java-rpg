package screen;

import input.Button;
import input.KeyAction;
import input.KeyboardConfig;

import java.awt.Graphics2D;
import java.awt.Point;

import game.Constants;
import game.ScreenManager;

public class PauseScreen extends Screen{
	
	private Button resumeButton;
	private Button menuButton;
	private Button quitButton;
	private GameScreen gs;

	public PauseScreen(ScreenManager sm, GameScreen gs) {
		super(sm, Constants.MENU_ZOOM);
		this.gs = gs;

		int w =  getDrawWidth() / 2;
		int h = getDrawHeight() / 2;
		int bw = 200; //button width
		int bh = 60; //button height
		//arbitrary variables
		int x1 = w-100;
		int w1 = x1+bw;
		int h1 = h-130;
		int h2 = h-50;
		int h3 = h+30;
		resumeButton = new Button(x1,h1,w1,h1+bh); //210,120,410,180
		menuButton = new Button(x1,h2,w1,h2+bh); //210,200,410,260
		quitButton = new Button(x1,h3,w1,h3+bh); //210,280,410,340
	}

	@Override
	public void loop(long elapsedTime) {
		checkInput();
		
	}

	@Override
	public void draw(Graphics2D g) {
		sm.initiateMenuGraphics(g);

		Point b1 = resumeButton.getPos();
		Point b2 = menuButton.getPos();
		Point b3 = quitButton.getPos();
		g.drawImage(sm.h.menuImgs[1],b1.x,b1.y,null);
		g.drawImage(sm.h.menuImgs[1],b2.x,b2.y,null);
		g.drawImage(sm.h.menuImgs[1],b3.x,b3.y,null); 
		
		g.drawString("Resume", b1.x+40, b1.y+40);
		g.drawString("Main Menu", b2.x+18, b2.y+40);
		g.drawString("Save & Quit", b3.x+10, b3.y+40);
		
	}

	@Override
	protected void checkInput() {

		Point m = sm.dui.mouseClicked;
		if(m!=null){
			if(resumeButton.isClicked(m)){
				sm.setScreen(gs);
				sm.dui.clearInput();
				return;
			}else if(menuButton.isClicked(m)){
				sm.saveGame();
				sm.setScreen(new MainMenuScreen(sm));
				sm.dui.clearInput();
				return;
			}else if(quitButton.isClicked(m)){
				sm.saveGame();
				sm.quit();
				return;
			}
		}
		KeyAction[] keyActions = sm.dui.getKeyActions();
		KeyboardConfig kc = sm.kc;
		for(KeyAction a : keyActions){
			if(a==null)break;
			int k = a.getKeyCode();
			if(a.isPressed()){
				if(k == kc.MENU){
					sm.setScreen(gs);
				}
			}
		}
		sm.dui.clearInput();
	}

}
