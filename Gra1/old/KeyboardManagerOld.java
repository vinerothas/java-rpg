package input;

import game.Constants;
import game.GameManager;
import graphics.sprite.Player;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardManagerOld implements KeyListener {
	
	private GameManager gm;
	private char[] supportedChars;
	private static final int KEY_MENU = 27;//escape
	private static final int KEY_UP = 87; //W
	private static final int KEY_UP_ALT= 38; //up arrow
	private static final int KEY_DOWN = 83; //S
	private static final int KEY_DOWN_ALT = 40;//down arrow
	private static final int KEY_LEFT = 65; //A
	private static final int KEY_LEFT_ALT = 37;//left arrow
	private static final int KEY_RIGHT = 68; //D
	private static final int KEY_RIGHT_ALT = 39; //right arrow
	private static final int KEY_ATTACK = 32; //space
	private static final int KEY_YES = 10; //enter
	private static final int KEY_NO = 8; //backspace
	private static final int KEY_CHARACTER = 67; //C
	private static final int KEY_INVENTORY = 73; //I

	public KeyboardManagerOld(Component comp,GameManager gm){
		comp.addKeyListener(this);
		this.gm = gm;	
		supportedChars = new char[]{'q','w','e','r','t','y','u','i','o','p','a','s','d','f','g','h','j','k','l','z','x','c','v','b','n','m',
				'_','-','Q','W','E','R','T','Y','U','I','O','P','A','S','D','F','G','H','J','K','L','Z','X','C','V','B','N','M',
				'1','2','3','4','5','6','7','8','9','0'};
	}
	
	public void keyPressed(KeyEvent e) {
		//RIGHT LEFT UP DOWN SPACE
		int k = e.getKeyCode();
		//System.out.println(k);
		int state = gm.dui.getPState();
		if(state>3){
			if(state==Constants.PSTATE_PLAY_POKER){
				checkPoker(e);
			}else if(state==Constants.PSTATE_NEW_GAME_MENU||state==Constants.PSTATE_NEW_GAME_NAME_TAKEN){
				char c = e.getKeyChar();
				if(checkSupported(c)==true)gm.dui.addToNgInput(String.valueOf(c));
				else if(e.getKeyCode()==8)gm.dui.substractNgInput();
				
			}else if(state==Constants.PSTATE_PAUSE_MENU_INGAME){
				if(k==KEY_MENU)gm.dui.setPState(Constants.PSTATE_ALIVE);
			}
			e.consume();
			return;
		}
		if(state!=Constants.PSTATE_ALIVE){
			e.consume();
			return;
		}
		Player player = gm.r.getPlayer();
		if(k==KEY_RIGHT||k==KEY_RIGHT_ALT){
			player.setStanceX(1);
		}else if(k==KEY_LEFT||k==KEY_LEFT_ALT){
			player.setStanceX(2);
		}else if(k==KEY_UP||k==KEY_UP_ALT){
			player.setStanceY(1);
		}else if(k==KEY_DOWN||k==KEY_DOWN_ALT){
			player.setStanceY(2);
		}else if(k==KEY_ATTACK){
			gm.hit = true;
		}else if(k==KEY_MENU){
			gm.dui.setPState(Constants.PSTATE_PAUSE_MENU_INGAME);
		}else if(gm.dui.isTextUI()==true){
			if(k==KEY_YES){
				gm.dui.dialogAction(true);
				gm.dui.clearTextUI();
			}else if(k==KEY_NO){
				gm.dui.dialogAction(false);
				gm.dui.clearTextUI();
			}
		}
		e.consume();
	}
	
	public void keyReleased(KeyEvent e) {
		int state = gm.dui.getPState();
		//RIGHT LEFT UP DOWN
		int k = e.getKeyCode();
		 
		Player player = null;
		try{
			player = gm.r.getPlayer();
			if(player==null){
				e.consume();
				return;
			}				
		}catch (NullPointerException ex){
			e.consume();
			return;
		}
		
		if(k==KEY_RIGHT||k==KEY_RIGHT_ALT){
			if(player.getStanceX()==1)player.setStanceX(0);		
		}else if(k==KEY_LEFT||k==KEY_LEFT_ALT){
			if(player.getStanceX()==2)player.setStanceX(0);		
		}else if(k==KEY_UP||k==KEY_UP_ALT){
			if(player.getStanceY()==1)player.setStanceY(0);		
		}else if(k==KEY_DOWN||k==KEY_DOWN_ALT){
			if(player.getStanceY()==2)player.setStanceY(0);
		}
		
		e.consume();
	}
	
	private void checkPoker(KeyEvent e){
		if(e.getKeyCode()>=48&&e.getKeyCode()<=57){
			gm.dui.setPokerInput(e.getKeyCode()-48);
		}else if(e.getKeyCode()==8){
			gm.dui.setPokerInput(-1);
		}
	}
	
	private boolean checkSupported(char c){
		for(Character sc: supportedChars){
			if(sc==c)return true;
		}
		return false;
	}
	
	public void keyTyped(KeyEvent e) {
		//do nothing	
		e.consume();
	}
	
}
