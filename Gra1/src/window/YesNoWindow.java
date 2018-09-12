package window;

import java.awt.Graphics2D;
import java.awt.Point;

import screen.GameScreen;

import input.Button;
import graphics.DrawGame;

public abstract class YesNoWindow extends Window{
	
	private Button yesButton;
	private Button noButton;
	protected GameScreen gs;
	
	public YesNoWindow(GameScreen gs) {
		super(gs);
		this.gs = gs;
		int w = 40;
		int h = 15;
		int yb = y+93;
		int xb = x+35;
		int xb2 = x+125;
		yesButton = new Button(xb,yb,xb+w,yb+h);
		noButton = new Button(xb2,yb,xb2+w,yb+h);
	}
	
	/**
	 * Triggers the yes and no actions and removes the window when done
	 * @param m Mouse clicked position
	 * @return remove window if true
	 */
	public void checkInput(Point m){
		if(yesButton.isClicked(m)){
			confirmAction();
		}else if(noButton.isClicked(m)){
			cancelAction();
		}
	}
	
	public abstract void draw(DrawGame d);
	
	protected void drawYNWindow(DrawGame d){
		drawWindow(d);
		Point b1 = yesButton.getPos();
		Point b2 = noButton.getPos();
		d.g.drawImage(d.h.buttonImg, b1.x, b1.y, null);
		d.g.drawImage(d.h.buttonImg, b2.x, b2.y, null);
		d.g.drawString("yes",b1.x+9,b1.y+11);
		d.g.drawString("no",b2.x+13,b2.y+12);
	}
}
