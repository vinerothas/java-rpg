package graphics.sprite;

import graphics.DrawGame;

public class Flower extends Decor{
	
	private int type;
	
	public Flower(int type, int x, int y){
		setY(y);
		setX(x);
		this.type = type;
		if(type==1){
			setWidth(6);
			setHeight(12);
		}else if(type==2){
			setWidth(5);
			setHeight(11);
		}
	}
	
	public int getType(){
		return type;
	}
	
	public void draw(DrawGame d, int x, int y){
		d.g.drawImage(d.h.flowerImgs[type],x, y, null);
	}
}
