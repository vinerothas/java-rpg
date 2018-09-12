package graphics.sprite;

import graphics.DrawGame;

public abstract class Sprite {
	
	private float y;
	private float x;
	private int width;
	private int height;
	private boolean advHeight;
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public void setWidth(int w){
		width = w;
	}
	public void setHeight(int h){
		height = h;;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	
	abstract public void draw(DrawGame d, int x, int y);
	
	public boolean getAdvHeight() {
		return advHeight;
	}
	public void setAdvHeight(boolean b) {
		advHeight = b;
	}
}
