package graphics.sprite;

import java.awt.Image;

import graphics.Animation;
import graphics.DrawGame;
import graphics.ImageHolder;

public class EnemyHor extends Enemy{

	private Animation anim = null;
	private boolean left = false;
	
	public EnemyHor(ImageHolder h, int type){
		super.type = type;
		if(type ==3){
			setV((float) 0.06);
			setHeight(14);
			setWidth(28);
			anim = h.spiderAnim.cloneAnim();
		}
	}
	
	public void setXY(float x, float y){
		setX(x);
		setY(y);
	}
	
	public boolean facingLeft(){
		return left;
	}
	
	public void setX(float x){
		float oldx = super.getX();
		super.setX(x);
		if(x>oldx)left=false;
		else if(x<oldx)left=true;
	}
	
	public Animation getAnim() {
		return anim;
	}
	
	public void draw(DrawGame d, int x, int y){
		Image img = getAnim().getImage(d.elapsedTime);
		if(facingLeft()==true){
			img = d.mirrorImg(img);
		}
		draw(img, d, x, y);
		//d.g.drawImage(img,x, y, null);
		//drawBar(d,x,y);
	}
	
}
