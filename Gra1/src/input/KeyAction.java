package input;

public class KeyAction {
	
	private char c;
	private int keyCode;
	private boolean pressed;
	private boolean isChar;
	
	public KeyAction(char c, int keyCode, boolean pressed){
		this.c = c;
		this.keyCode = keyCode;
		this.pressed = pressed;
		isChar = true;
	}
	
	public KeyAction(int keyCode, boolean pressed){
		isChar = false;
		this.keyCode = keyCode;
		this.pressed = pressed;
	}
	
	public boolean isPressed(){
		return pressed;
	}
	
	public boolean isReleased(){
		return !pressed;
	}
	
	public char getCharCode(){
		if(isChar)return c;
		else return '?';
	}
	
	public boolean isCharacter(){
		return isChar;
	}
	
	public int getKeyCode(){
		return keyCode;
	}
}
