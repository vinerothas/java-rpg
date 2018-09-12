package graphics.sprite;

import java.awt.Image;

import graphics.Animation;
import graphics.DrawGame;
import graphics.ImageHolder;

public class Enemy3Dir extends Enemy{

	private Animation[] anims = new Animation[3];
	private int dir = 0; //0-up	1-down	2-left	3-right
	private long lastDirSwitch = 0;
	
	public Enemy3Dir(ImageHolder h, int type){
		super.type = type;
		if(type ==4){
			setV((float)0.09);
			setHeight(29);
			setWidth(17);
			for(int i = 0;i<3;i++){
				anims[i] = h.snakeAnim[i].cloneAnim();
			}
		}
	}
	
	public void setXY(float x, float y){
		float oldX = getX();
		float oldY = getY();
		if(x==oldX&&y==oldY)return;
		float xd = x-oldX;
		float yd = y-oldY;
		//if(System.currentTimeMillis()-lastDirSwitch>200){
			lastDirSwitch = System.currentTimeMillis();
			if(Math.abs(xd)>Math.abs(yd)){
				if(x>oldX)dir = 3;
				else dir=2;
			}else{
				if(y>oldY)dir=1;
				else dir = 0;
			}
		//}
		setX(x);
		setY(y);
	}
	
	public int getDir(){
		return dir;
	}
	
	public Animation getAnim(){
		if(dir==3)return anims[2];
		else return anims[dir];
	}
	
	public void draw(DrawGame d, int x, int y){
		Image img = getAnim().getImage(d.elapsedTime);
		if(getDir()==3){
			img = d.mirrorImg(img);
		}
		draw(img, d, x, y);
		//d.g.drawImage(img,x, y, null);
		//drawBar(d,x,y);
	}
	
}
