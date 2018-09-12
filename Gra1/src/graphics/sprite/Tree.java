package graphics.sprite;

import graphics.DrawGame;

public class Tree extends Decor {

	private final int height = 86;
	private final int width = 35;
	
	public Tree(int x, int y) {
		setX(x);
		setY(y);
		setHeight(height);
		setWidth(width);
	}
	
	public void draw(DrawGame d, int x, int y){
		d.g.drawImage(d.h.treeImg,x, y, null);
	}
	
}
