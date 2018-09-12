package input;

import graphics.DataUI;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardManager implements KeyListener {

	private DataUI dui;
	
	public KeyboardManager(Component comp,DataUI dui){
		comp.addKeyListener(this);
		this.dui = dui;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		dui.addKeyAction(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		dui.addKeyAction(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

}
