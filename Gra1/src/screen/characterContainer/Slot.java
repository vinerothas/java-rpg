package screen.characterContainer;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

public class Slot {
	
	private int dx;
	private int dy;
	private int x;
	private int y;
	private int x2;
	private int y2;
	private static final int size = 18;
	private static final int offset = 2;
	
	public Slot(int xa, int ya){
		dx = xa;
		dy = ya;
		this.x = xa+offset;
		this.y = ya+offset;
		x2 = x+size;
		y2 = y+size;
	}
	
	public boolean insideSlot(Point m){
		if(m.x>=x&&m.x<=x2&&m.y>=y&&m.y<=y2){
			return true;
		}
		return false;
	}
	
	public void drawSlot(Graphics2D g, Image slot, Image item){
		g.drawImage(slot, dx, dy, null);
		g.drawImage(item, x, y+1, null);
		//g.fillRect(x+offset, y+offset, size, size);
	}
}
