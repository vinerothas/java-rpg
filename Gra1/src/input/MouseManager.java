package input;

import graphics.DataUI;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseManager implements MouseListener, MouseMotionListener{
 
	private DataUI dui;
	
	public MouseManager(Component comp, DataUI dUI){
		this.dui = dUI;
		comp.addMouseListener(this);
		comp.addMouseMotionListener(this);
	}

	public void mouseDragged(MouseEvent e) {
		int x = (int) (e.getX()/dui.zoom);
		int y = (int) (e.getY()/dui.zoom);
		dui.mouseDragged = new Point(x,y);
		e.consume();
	}

	public void mouseMoved(MouseEvent e) {
		int x = (int) (e.getX()/dui.zoom);
		int y = (int) (e.getY()/dui.zoom);
		dui.mouseMoved = new Point(x,y);
		e.consume();
	}

	public void mousePressed(MouseEvent e) {
		int x = (int) (e.getX()/dui.zoom);
		int y = (int) (e.getY()/dui.zoom);
		dui.mouseClicked = new Point(x,y);
		if(e.getButton()==3)dui.leftClick=false;
		else dui.leftClick = true;
		e.consume();
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	
}
