package screen;

import java.awt.*;

import input.Button;
import game.Constants;
import game.ScreenManager;

public class MainMenuScreen extends Screen{
	
	private Button newGameButton;
	private Button loadGameButton;
	private Button quitButton;

	public MainMenuScreen(ScreenManager sm) {
		super(sm, Constants.MENU_ZOOM);
		sm.r.cleanRegion();
		int w =  getDrawWidth() / 2; //310
		int h = getDrawHeight() / 2; //250
		int bw = 200; //button width
		int bh = 60; //button height
		//arbitrary variables
		int x1 = w-100;
		int w1 = x1+bw;
		int h1 = h-130;
		int h2 = h-50;
		int h3 = h+30;
		newGameButton = new Button(x1,h1,w1,h1+bh); //210,120,410,180
		loadGameButton = new Button(x1,h2,w1,h2+bh); //210,200,410,260
		quitButton = new Button(x1,h3,w1,h3+bh); //210,280,410,340
	}

	@Override
	public void loop(long elapsedTime) {
		checkInput();
		
	}

	@Override
	public void draw(Graphics2D g) {
		sm.initiateMenuGraphics(g);
		
		Point b1 = newGameButton.getPos();
		Point b2 = loadGameButton.getPos();
		Point b3 = quitButton.getPos();
		g.drawImage(sm.h.menuImgs[1],b1.x,b1.y,null);
		g.drawImage(sm.h.menuImgs[1],b2.x,b2.y,null);
		g.drawImage(sm.h.menuImgs[1],b3.x,b3.y,null);
		
		g.drawString("New Game", b1.x+19, b1.y+40);
		g.drawString("Load Game", b2.x+12, b2.y+40);
		g.drawString("Quit Game", b3.x+18, b3.y+40);
	}

	@Override
	protected void checkInput() {
		Point m = sm.dui.mouseClicked;
		if(m==null)return;
		if(newGameButton.isClicked(m)){
			sm.setScreen(new NewGameScreen(sm));
		}else if(loadGameButton.isClicked(m)){
			sm.setScreen(new LoadScreen(sm));
		}else if(quitButton.isClicked(m)){
			sm.quit();
		}
		sm.dui.clearInput();
	}

}
