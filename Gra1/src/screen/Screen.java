package screen;

import java.awt.Graphics2D;
import java.awt.Point;

import game.ScreenManager;

public abstract class Screen {

	public ScreenManager sm;	
	private int drawWidth;
	private int drawHeight;
	private int sizeX;
	private int sizeY;
	private float zoom;
	public Point screenMid;
	
	public Screen(ScreenManager sm, float zoom) {
		this.sm = sm;
		setZoom(zoom);
	}
	
	public void setZoom(float zoom){
		this.zoom = zoom;
		drawWidth = (int) (sm.windowWidth/zoom);
		drawHeight = (int) (sm.windowHeight/zoom);
		sizeX = (int) (sm.windowWidth*zoom);
		sizeY = (int) (sm.windowHeight*zoom);
		screenMid = new Point(drawWidth/2,drawHeight/2);
	}
	
	public float getZoom(){
		return zoom;
	}
	
	public int getDrawWidth(){return drawWidth;}
	public int getDrawHeight(){return drawHeight;}
	public int getSizeX(){return sizeX;}
	public int getSizeY(){return sizeY;}
	
	/**
	 * Method taking care of drawing, input and computation 
	 * that needs to be done while on current screen
	 */
	public abstract void loop(long elapsedTime);
	
	public abstract void draw(Graphics2D g);
	
	/**
	 * 
	 * @return boolean: abort current screen if true
	 */
	protected abstract void checkInput();
	
}
