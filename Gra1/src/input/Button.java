package input;

import java.awt.Point;

public class Button {
	private int x;
	private int y;
	private int x2;
	private int y2;
	
	public Button(int x,int y,int x2,int y2){
		this.x = x;
		this.x2 = x2;
		this.y = y;
		this.y2 = y2;
	}
	
	public boolean isClicked(int mx, int my){
		if(mx>x&&mx<x2&&my>y&&my<y2){
			return true;
		}
		return false;
	}
	
	public boolean isClicked(Point m){
		if(m.x>x&&m.x<x2&&m.y>y&&m.y<y2){
			return true;
		}
		return false;
	}
	
	public Point getPos(){
		return new Point(x,y);
	}
	
	public Point getSize(){
		return new Point(x2-x,y2-y);
	}
}
