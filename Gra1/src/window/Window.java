package window;

import graphics.DrawGame;

import java.awt.Point;

import screen.GameScreen;

public abstract class Window {

	protected int x;
	protected int y;
	protected GameScreen gs;
	protected final static int width = 202;
	protected final static int height = 126;
	
	public Window(GameScreen gs){
		this.gs = gs;
		this.x = gs.screenMid.x-(width/2);
		this.y = gs.screenMid.y-(height/2);
	}
	
	public abstract void checkInput(Point m);
	
	public abstract void draw(DrawGame d);
	
	/**	Action done when pressing ENTER (default)*/
	public abstract void confirmAction();
	/**	Action done when pressing BACKSPACE (default)*/
	public abstract void cancelAction();
	
	protected void drawWindow(DrawGame d){
		d.getDialogFont();
		d.g.drawImage(d.h.windowUIImg, x, y, null);
	}
	
}
