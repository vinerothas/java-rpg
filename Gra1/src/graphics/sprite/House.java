package graphics.sprite;

import graphics.DrawGame;

import java.awt.Image;

public class House extends Building{
	private final int height = 180;
	private final int width = 258;
	private int type;

	public House(int x, int y, int type){
		setY(y);
		setX(x);
		this.type = type;
		setHeight(height);
		setWidth(width);
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type){
		this.type = type;
	}
	
	public void draw(DrawGame d, int x, int y){
		Image img = null;
		if(type==0||type==1){
			img = d.h.houseImgs[0];
		}else if(type==2||type==3){
			img = d.h.houseImgs[1];
		}else if(type==4||type==5){
			img = d.h.houseImgs[2];
		}
		if(type==1 || type==3 || type == 5){
			img = d.mirrorImg(img);
		}
		d.g.drawImage(img,x, y, null);
	}
	
}
