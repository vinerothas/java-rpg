package graphics;

import java.awt.Graphics2D;
import java.awt.Point;

public class Circle {
	
	private int radius;
	private Point middle;
	private int[] yValues;
	private final int PIXEL_WIDTH = 1;
	
	/**
	 * 
	 * @param Position of the circle on screen
	 * @param Circles radius
	 */
	public Circle(Point middle, int radius){
		this.radius=radius;
		this.middle=middle;
		
		//first point is y=radius
		//last point is y=0
		//these values are not saved and are handled separately
		yValues = new int[radius-1];
		
		for(int x = 1;x<radius;x++){
			int y = (int) Math.round(Math.sqrt((radius*radius)-(x*x)));
			yValues[x-1]=y;
		}
	}
	
	/**
	 * Replace the circle area with background
	 * @param Graphics2D of the image where the circle area needs to be cleared
	 */
	public void clearCircle(Graphics2D g){
		//clear x=0 && x=-1
		int posY = middle.y-radius;
		int posX = middle.x;
		int height = radius+radius;
		g.clearRect(posX, posY, PIXEL_WIDTH, height);
		posX = posX-PIXEL_WIDTH;
		g.clearRect(posX, posY, PIXEL_WIDTH, height);
		
		for(int i = 1;i<radius;i++){
			int y = yValues[i-1];
			posY = middle.y-y;
			posX = middle.x+i;
			height = y+y;
			g.clearRect(posX, posY, PIXEL_WIDTH, height);
			
			//clear opposite side
			posX = middle.x-(i+PIXEL_WIDTH);
			g.clearRect(posX, posY, PIXEL_WIDTH, height);
		}
		
		//clear y=0 with half value of the previous height
		height = (int)Math.round(height/(double)2);
		
		//get half the current height which is posY in relation to radius
		posY = posY+(int)(Math.round(height/(double)2));
		
		//opposite side first
		posX = posX-PIXEL_WIDTH;
		g.clearRect(posX, posY, PIXEL_WIDTH, height);
		
		//back to normal side
		posX = posX+PIXEL_WIDTH;
		posX = middle.x+(middle.x-posX);
		g.clearRect(posX, posY, PIXEL_WIDTH, height);
	}
}
