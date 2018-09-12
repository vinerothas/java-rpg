package graphics;

import game.Constants;
import input.ItemQBox;
import input.KeyAction;
import items.Item;

import java.awt.Point;
import java.awt.event.KeyEvent;

public class DataUI {
	
	private boolean hoverBp = false;
	private int drawUI = 0;
	private String[] textUI = new String[3];
	private boolean isText = false;
	private int inBuilding = 0;
	public boolean activeGame = true;
	private int pState = 0;
	private int buyItem = 0;
	private int travelDir = 0;
	public ItemQBox qbox = null;
	private int itemHover = 0;
	private int itemHeldPos = 0;
	public Item itemHeld = null;
	private int itemTaken = 0;
	private int itemRightClicked = 0;
	private int pokerInput = -2;
	//public ButtonHolder buttons = new ButtonHolder();
	public Point mouseClicked = null;
	public boolean leftClick = true;
	public Point mouseMoved = null;
	public Point mouseDragged = null;
	private final int keyActionsLength = 20;
	private KeyAction[] keyActions = new KeyAction[keyActionsLength];
	private int keyActionsIndex = 0;
	public float zoom = (float) 1.0;

	public boolean isHoverBp() {
		return hoverBp;
	}
	public void setHoverBp(boolean hoverBp) {
		this.hoverBp = hoverBp;
	}
	public int getDrawUI() {
		return drawUI;
	}
	/* 	0 - draw none
	 *	1 - draw Inventory
	 * 	2 - death screen	*/
	public void setDrawUI(int drawUI) {		
		if(this.drawUI==2 && drawUI==1){
			return;
		}
		this.drawUI = drawUI;
	}
	public String[] getTextUI() {
		return textUI;
	}
	/* Max 22 signs per line */
	public void setTextUI(String text, String text2, String text3) {
		textUI[0] = text;
		textUI[1] = text2;
		textUI[2] = text3;
		if(getDrawUI()==1){
			setDrawUI(0);
		}
		activeGame = false;
		isText = true;
	}
	public void clearTextUI(){
		textUI[0] = null;
		textUI[1] = null;
		textUI[2] = null;
		activeGame=true;
		isText = false;
	}
	public boolean isTextUI(){
		return isText;
	}
	public int isInBuilding() {
		return inBuilding;
	}
	/* 0 - outside
	 * 1 - in front of the door
	 * 2 - inside
	 * 1-2 - tavern 3-5 - shop buy(4)/sell(5) */	
	public void setInBuilding(int i) {
		if(i==2 || i==4 || i == 5){
			activeGame = false;
		}else{
			activeGame = true;
		}
		inBuilding = i;
	}
	public boolean insideB(){
		if(inBuilding==2||inBuilding==4||inBuilding==5){
			return true;
		}
		return false;
	}
	public boolean gettingB(){
		if(inBuilding==1||inBuilding==3){
			return true;
		}
		return false;
	}	
	/** States generally related to mouse presses, defined in static class Constants*/
	public int getPState() {
		return pState;
	}
	public void setPState(int s) {
		pState = s;
	}
	/** -2 none
	 * -1 backspace
	 * 0-9 numbers
	 * 10 - raise
	 * 11 - check / call
	 * 12 - fold
	 * 13 - player wants to join
	 * 14 - continue(next round) */
	public void setPokerInput(int k){
		pokerInput = k;
	}
	
	/**
	 * Resets pokerInput
	 */
	public int getPokerInput(){
		int n = pokerInput;
		pokerInput=-2;
		return n;
	}

	public int getBuyItem() {
		return buyItem;
	}

	/*
	 * negative pos means sell
	 */
	public void setBuyItem(int pos) {
		buyItem = pos;
		if(pos !=0){
			pState = 3;
		}else{
			pState = 0;
		}
	}

	public int getTravelDir() {
		return travelDir;
	}
	// 1 - north, 2 - east, 3 - south, 4 - west
	public void setTravelDir(int dir) {
		travelDir = dir;
	}

	public int getItemHover() {
		return itemHover;
	}

	/**
	 * 0 for none
	 * positive for inventory
	 * negative for equips
	 */
	public void setItemHover(int pos) {
		itemHover = pos;
	}

	/*
	 * negative - equipped
	 * positives - inventory
	 */
	public int getItemHeld() {
		return itemHeldPos;
	}

	public void setItemHeld(int pos) {
		itemHeldPos = pos;
	}
	
	public int getItemRightClicked() {
		return itemRightClicked;
	}

	public void setItemRightClicked(int i) {
		itemRightClicked = i;
	}

	/*
	 * 0 - none
	 * positives - inventory
	 * negatives - equipped
	 */
	public int getItemTaken() {
		return itemTaken;
	}
	
	public void setItemTaken(int pos) {
		itemTaken = pos;
	}
	
	public void dialogAction(boolean YN){
		if(getDrawUI()==2){
			if(YN==true){
				setPState(1);
			}else{
				setPState(12);
			}
			return;
		}
		
		if(getTravelDir()!=0){
			if(YN==true){
				setPState(3);
			}else{
				setTravelDir(0);
			}
		}
		
		if(YN==true){
			if(isInBuilding()==1){
				setInBuilding(2);	
				return;
			}
			if(isInBuilding()==3){
				setInBuilding(4);
				return;
			}
		}
		setInBuilding(0);
	}
	
	public void addKeyAction(KeyEvent e){
		if(keyActions.length==keyActionsIndex){
			System.out.println("keyEventesLimit reached, DataUI addKeyEvent line 266");
			return;
		}
		boolean pressed = true;
		if(e.getID()==KeyEvent.KEY_RELEASED)pressed = false;
		else if(e.getID()!=KeyEvent.KEY_PRESSED)return;//don't add if neither pressed nor released

		KeyAction a;
		if(e.getKeyChar()!=KeyEvent.CHAR_UNDEFINED){
			a = new KeyAction(e.getKeyChar(),e.getKeyCode(),pressed);
		}else{
			a = new KeyAction(e.getKeyCode(),pressed);
		}
		keyActions[keyActionsIndex]= a;
		keyActionsIndex++;
	}
	
	public KeyAction[] getKeyActions(){
		return keyActions;
	}
	
	public void clearKeyActions(){
		//System.out.println("clear");
		keyActions = new KeyAction[keyActionsLength];
		keyActionsIndex = 0;
	}
	
	public void clearInput(){
		clearKeyActions();
		mouseClicked = null;
	}
	
}
