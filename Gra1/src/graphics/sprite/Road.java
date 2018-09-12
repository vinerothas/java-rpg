package graphics.sprite;

import graphics.DrawGame;

import java.util.Random;

public class Road extends Building{
	
	private final int width = 60;
	private final int height = 60;
	private int type;
	
	public Road(int x, int y){
		Random r = new Random();
		type = r.nextInt(2)+1;
		setY(y);
		setX(x);
		setHeight(height);
		setAdvHeight(true);
		setWidth(width);
	}

	public int getType(){
		return type;
	}
	
	public void draw(DrawGame d, int x, int y){
		d.g.drawImage(d.h.roadImgs[type],x, y, null);
	}
}
