package window;

import graphics.DrawGame;

import input.Button;

import java.awt.Point;

import screen.GameScreen;

public class InfoWindow extends Window{
	
	private Button okButton;
	private String[] text;
	protected String buttonText = "ok";

	public InfoWindow(GameScreen gs, String[] text) {
		super(gs);
		this.text = text;
		int w = 40;
		int h = 15;
		int yb = y+93;
		int xb = x+80;
		okButton = new Button(xb,yb,xb+w,yb+h);
	}

	@Override
	public void checkInput(Point m) {
		if(okButton.isClicked(m))confirmAction();
	}

	@Override
	public void draw(DrawGame d) {
		drawWindow(d);
		Point b1 = okButton.getPos();
		d.g.drawImage(d.h.buttonImg, b1.x, b1.y, null);
		d.g.drawString(buttonText,b1.x+13,b1.y+13);
		for(int i = 0;i<text.length;i++){
			if(text[i]==null)continue;
			d.g.drawString(text[i], x+12, y+22+(i*15));
		}
	}

	@Override
	public void confirmAction() {
		gs.window=null;		
	}

	@Override
	public void cancelAction() {
		confirmAction();	
	}

}
