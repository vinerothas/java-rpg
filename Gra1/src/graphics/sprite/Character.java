package graphics.sprite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

import game.Constants;
import graphics.DrawGame;

public abstract class Character extends Sprite{
	
	private float v;
	private boolean floating = false;
	private int baseX = 0;
	private int baseX2= 0;
	private boolean swim = false;
	
	public void draw(Image img, DrawGame d, int x, int y){
		//System.out.println(getSwim());
		if(getSwim()){
			BufferedImage copy = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.BITMASK);
			Graphics2D g = copy.createGraphics();
			g.drawImage(img, 0, 0, null);
			g.setBackground(new Color(0,0,0,0));
			//int factor = 10;
			float factor2 = (float)0.6;		
			//int depth = (getHeight()-factor)/2;
			//int lower = (getHeight()+factor)/2;
			int lower = (int) (getHeight()*factor2);
			int depth = getHeight()-lower;
			//System.out.println(lower);
			g.clearRect(0, lower, getWidth(), getHeight());
			g.dispose();
			d.g.drawImage(copy, x, y+depth, null);
			if(this instanceof Enemy){
				((Enemy)this).drawBar(d,x,y);
			}
		}else{
			d.g.drawImage(img,x, y,null);
			if(this instanceof Enemy){
				((Enemy)this).drawBar(d,x,y);
			}
		}
	}

	public float getV() {
		return v;
	}

	public void setV(float v) {
		this.v = v;
	}
	
	/**
	 * Above water or not
	 */
	public boolean getFloating() {
		return floating;
	}

	public void setFloating(boolean b) {
		floating = b;
	}

	/** Farthest to left x-coord the character is standing on	 */
	public int getBaseX() {
		return baseX;
	}

	public void setBaseX(int baseX) {
		this.baseX = baseX;
	}
	
	/** Farthest to right x-coord the character is standing on	 */
	public int getBaseX2() {
		return baseX2;
	}

	public void setBaseX2(int b) {
		baseX2 = b;
	}

	public boolean getSwim() {
		return swim;
	}

	public void setSwim(boolean swim) {
		if(getFloating()==false)this.swim = swim;
	}
	
	/**
	 * Sets swim, returns false if deep water
	 */
	public boolean checkSwimming(int[][] water, int x, int y){
		Point p = Constants.coordToTile(x+getBaseX(),y+getHeight()-4);
		//System.out.println((int)getX()+getBaseX()+" "+((int)getX()+getBaseX2())+" "+((int)getY()+getHeight()-4));
		//System.out.println("P: "+water[p.x][p.y]);
		if(water[p.x][p.y]!=0){
			if(water[p.x][p.y]==2)return false;
			Point p2 = Constants.coordToTile(x+getBaseX2(),y+getHeight());
			//System.out.println("P2: "+water[p2.x][p2.y]);
			if(water[p2.x][p2.y]!=0){
				if(water[p2.x][p2.y]==2)return false;
				setSwim(true);
			}else setSwim(false);
		}else setSwim(false);
		return true;
	}
	
}
