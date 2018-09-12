package screen;

import input.Button;
import input.KeyAction;
import input.KeyboardConfig;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;

import game.Constants;
import game.ScreenManager;

public class TavernScreen extends Screen{

	private GameScreen gameScreen;
	private Button sleepButton;
	
	public TavernScreen(ScreenManager sm, GameScreen gs) {
		super(sm, (float)2.0);
		gameScreen = gs;
		
		sleepButton = new Button(100,getDrawHeight()-100,141,getDrawHeight()-82);
	}

	@Override
	public void loop(long elapsedTime) {

		checkInput();
		
	}

	@Override
	protected void checkInput() {
		Point m = sm.dui.mouseClicked;
		sm.dui.mouseClicked = null;
		if(m!=null){
			if(sleepButton.isClicked(m)){
				sm.r.getPlayer().setLife(sm.r.getPlayer().getMhp());
				sm.setScreen(gameScreen);
				sm.dui.clearInput();
				sm.setScreen(gameScreen);
			}
		}
		
		KeyAction[] keyActions = sm.dui.getKeyActions();
		KeyboardConfig kc = sm.kc;
		for(KeyAction a : keyActions){
			if(a==null)break;
			int k = a.getKeyCode();
			if(a.isPressed()){
				if(k == kc.MENU){
					sm.setScreen(gameScreen);
				}
			}
		}
		sm.dui.clearInput();
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(sm.h.tavernBG, 0, 0,getDrawWidth(),getDrawHeight(), null);
		
		g.drawImage(sm.h.buttonImg,sleepButton.getPos().x,sleepButton.getPos().y,null);
		g.setColor(new Color(255,230,0));
		g.setFont(new Font(Font.MONOSPACED,Font.BOLD,12));
		g.drawString("sleep",sleepButton.getPos().x+4,sleepButton.getPos().y+13);
	}

}
