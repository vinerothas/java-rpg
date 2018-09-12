package screen.characterContainer;

import graphics.ImageHolder;

import java.awt.Graphics2D;
import java.awt.Point;

public abstract class CharacterContainer {
	
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	protected int x2;
	protected int y2;
	
	public CharacterContainer(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		x2= x+width;
		y2= y+height;
	}
	
	public abstract void draw(Graphics2D g, ImageHolder h);
	
	public abstract void handleInput(Point m);
	
	public boolean checkInput(Point m){
		if(m.x>x&&m.x<x2&&m.y>y&&m.y<y2){
			handleInput(m);
			return true;
		}
		return false;
	}
	
}
