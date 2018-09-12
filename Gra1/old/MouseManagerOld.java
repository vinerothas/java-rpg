package input;

import game.Constants;
import graphics.DataUI;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseManagerOld implements MouseListener, MouseMotionListener{

	private DataUI dui;
	
	public MouseManagerOld(Component comp, DataUI dUI){
		this.dui = dUI;
		comp.addMouseListener(this);
		comp.addMouseMotionListener(this);
	}
	
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		ButtonHolder buttons = dui.buttons;
		if(e.getX()> 500){
			checkEquipsHover(x,y);
			if(buttons.backpack.isClicked(x, y)){
				dui.setHoverBp(true);
			}else{
				dui.setHoverBp(false);
			}
			return;
		}
		if(dui.getDrawUI()==1||dui.isInBuilding()==5||dui.isInBuilding()==4){
		checkItemHover(x,y);
		}

	}
	
	private void checkItemHover(int mx, int my){
		int pos = 0;
		if(dui.isInBuilding()==4){
			pos = findShopPos(mx,my);
		}else{
			pos = findInvPos(mx,my);
		}
		if(pos==0){
			dui.setItemHover(0);
			return;
		}
		dui.setItemHover(pos);
	}
	
	private void checkEquipsHover(int mx, int my){
		//first box
		int x = 517;
		int y = 31;
		//box size (square)
		int b = 17;
		//grid distance
		int gx = 33;
		int gy = 35;
		for(int i = 0;i<3;i++){
			int bx = x+(gx*i);
			for(int j = 0;j<3;j++){
				int by = y+(gy*j);
				if(mx>=bx&&mx<=bx+b&&my>=by&&my<=by+b){
					dui.setItemHover(-(i+1+(3*j)));
					return;
				}
			}
		}
		dui.setItemHover(0);
	}
	
	private int findShopPos(int mx,int my){
		int x = 119;
		int y = 134;
		int w = 19;
		int dx = 56;
		int dy = 48;
		for(int iy = 0;iy<5;iy++){
			for(int ix = 0;ix<5;ix++){
				int posx = x+(ix*dx);
				int posy = y+(iy*dy);
				if(mx>posx&&mx<posx+w&&my>posy&&my<posy+w){
					return (iy*5)+ix+1;
				}
			}
		}
		return 0;
	}
	
	private int findInvPos(int mx, int my){
		int firstX = 119;
		int firstY = 122;
		int wh = 19;
		int xdis = 28;
		int ydis = 24;
		for(int ypos = 0;ypos<=9;ypos++){
			for(int xpos = 0;xpos<=9;xpos++){
				int x = firstX+xdis*xpos;
				int x2 = x+wh;
				int y = firstY+ypos*ydis;
				int y2 = y+wh;
				if(mx>x&&mx<x2&&my>y&&my<y2){
					return (xpos+1)+(ypos*10);
				}
			}
		}
		return 0;
	}

	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		ButtonHolder buttons = dui.buttons;
		dui.m.x = x;
		dui.m.y = y;
		if(e.getButton()==3){
			if(dui.getDrawUI()==1){
				int pos = findInvPos(x,y);
				if(pos!=0){
					dui.setItemRightClicked(pos);
				}
			}
		}
		if(e.getButton()!=1)return;
		System.out.println(x+" "+y);
		int state = dui.getPState();
		//naprawic wybieranie gry w load game menu
		if(state>4){
			/*	^^^
				set upper limit-----------------------------------------
			*/
			if(state==Constants.PSTATE_MAIN_MENU){
				if(buttons.menuArray[0].isClicked(x, y)){
					dui.setPState(Constants.PSTATE_NEW_GAME_MENU);
				}else if(buttons.menuArray[1].isClicked(x, y)){
					dui.setPState(Constants.PSTATE_LOAD_GAME_MENU);
				}else if(buttons.menuArray[2].isClicked(x, y)){
					dui.setPState(Constants.PSTATE_QUIT_GAME);
				}
			}else if(state==Constants.PSTATE_NEW_GAME_MENU||state==Constants.PSTATE_NEW_GAME_NAME_TAKEN){
				if(buttons.menuArray[3].isClicked(x, y)){
					if(dui.getNgInput().isEmpty()==false)dui.setPState(Constants.PSTATE_START_NEW_GAME);
				}else if(buttons.menuArray[4].isClicked(x, y)){
					dui.setPState(Constants.PSTATE_MAIN_MENU);
				}
			}else if(state==Constants.PSTATE_LOAD_GAME_MENU){
				//190,80
				int containerx = Constants.WINDOW_WIDTH/2-120;
				int containery= Constants.WINDOW_HEIGHT/2-170;
				int x1 = containerx+6;
				int x2 = x1+224;
				for(int i = 1;i<11;i++){
					int by=containery+2+(i*25);
					Button b = new Button(x1,by,x2,by+22);//196, by,420,by+22
					if(b.isClicked(x, y)){
						dui.setSavePicked(i);
						return;
					}
				}
				if(buttons.menuArray[5].isClicked(x, y)){
					dui.setPState(Constants.PSTATE_MAIN_MENU);
				}else if(buttons.menuArray[6].isClicked(x, y)){
					if(dui.getSavePicked()!=0)dui.setPState(Constants.PSTATE_LOAD_GAME);
				}
			}else if(state==Constants.PSTATE_PAUSE_MENU_INGAME){
				if(buttons.menuArray[7].isClicked(x, y)){
					dui.setPState(Constants.PSTATE_ALIVE);
				}else if(buttons.menuArray[8].isClicked(x, y)){
					dui.setPState(Constants.PSTATE_SAVE_AND_QUIT);
				}
			}
			return;
		}
		ItemQBox qbox = dui.qbox;
		if(qbox != null){
			if(qbox.aClicked(x, y)){
				dui.qbox.setQuantity(99);
			}else if(qbox.cClicked(x,y)){
				dui.qbox = null;
			}else if(qbox.oClicked(x, y)){
				dui.qbox.setSell(true);
			}else if(qbox.dClicked(x, y)){
				dui.qbox.downQuantity();
			}else if(qbox.uClicked(x, y)){
				dui.qbox.upQuantity();
			}
			return;
		}
		if(dui.isTextUI()==true){			
			if(buttons.yes.isClicked(x,y)){
				dui.clearTextUI();
				dui.dialogAction(true);
			}else if(buttons.no.isClicked(x, y)){
				dui.clearTextUI();
				dui.dialogAction(false);
			}
			return;
		}
		if(dui.insideB()){
			if(dui.getPState()==Constants.PSTATE_PLAY_POKER){
				for(int i = 0;i<buttons.pokerArray.length;i++){
					if(buttons.pokerArray[i].isClicked(x, y))dui.setPokerInput(i+10);
				}
				if(buttons.exitBuilding.isClicked(x, y)){
					dui.setPState(Constants.PSTATE_ALIVE);
				}
				return;
			}
			int b = dui.isInBuilding();
			if(buttons.exitBuilding.isClicked(x, y)){
				dui.setInBuilding(Constants.PSTATE_ALIVE);
			}else if(buttons.sleep.isClicked(x, y) && b==2){
				dui.setPState(Constants.PSTATE_SLEEP_ACTION_PENDING);
			}else if(buttons.playPoker.isClicked(x, y) && b==2){
				dui.setPState(Constants.PSTATE_PLAY_POKER);
			}else if(b==4 || b==5){
				shopAction(x,y,b);
				return;
			}
		}
		if(dui.getDrawUI()==1){
			int pos = findInvPos(x,y);
			if(pos!=0){
				dui.setItemHeld(pos);
			}
		}
		if(e.getX()< 500){			
			return;
		}		
		if(buttons.backpack.isClicked(x,y)){
			int d = dui.getDrawUI();
			if(d != 1){
				dui.setDrawUI(1);
			}else{
				dui.setDrawUI(0);
			}
		}
		int pos = getEquipPos(x,y);
		if(pos!=0){
			if(dui.itemHeld==null){
				dui.setItemHeld(-pos);
			}
		}
	}
	
	
	
	public void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if(dui.itemHeld!=null){
			int pos = getEquipPos(x,y);
			if(pos!=0){			
				dui.setItemHeld(pos);
			}else{
				dui.setItemHeld(-1);
			}
		}
	}
	
	private int getEquipPos(int x, int y){
		int pos = 0;
		int ix = 516;
		int iy = 30;
		for(int i = 0;i<3;i++){
			for(int j=0;j<3;j++){
				if(x>ix&&x<ix+20&&y>iy&&y<iy+20){
					pos = (i*3)+j+1;
				}
				ix+=33;
			}
			ix = 516;
			iy+= 36;
		}
		if(pos==7)pos=0;
		return pos;
	}
	
	private void shopAction(int x, int y, int b){		
		ButtonHolder buttons = dui.buttons;
		//buy
		if(b==4){
			if(buttons.shopSell.isClicked(x, y)){
				dui.setInBuilding(5);
				return;
			}
			//ZMIENIC
			for(int iy = 0;iy<5;iy++){
				int sy = 134+(48*iy);
				for(int ix = 0;ix<5;ix++){
					int sx = 119+(56*ix);
					if(x>sx&&x<sx+20&&y>sy&&y<sy+20){
						int pos = (iy*5)+ix+1;
						dui.setBuyItem(pos);
						return;
					}
				}
			}
			return;
		//sell
		}else if(b==5){
			if(buttons.shopBuy.isClicked(x, y)){
				dui.setInBuilding(4);
				dui.qbox=null;
				return;
			}else if(buttons.shopSellMobLoot.isClicked(x, y)){
				dui.setPState(3);
			}
			//ZMIENIC
			for(int iy = 0;iy<10;iy++){
				int sy = 122+(24*iy);
				for(int ix = 0;ix<10;ix++){
					int sx = 119+(28*ix);
					if(x>sx&&x<sx+20&&y>sy&&y<sy+20){
						int pos = -((iy*10)+ix+1);
						dui.qbox = new ItemQBox(150,170,pos);
						//dUI.setBuyItem(-pos);
						return;
					}
				}
			}
		}
	}
	
	
	
	public void mouseDragged(MouseEvent arg0) {		
	}
	public void mouseClicked(MouseEvent arg0) {		
	}
	public void mouseEntered(MouseEvent arg0) {
	}
	public void mouseExited(MouseEvent arg0) {		
	}

}
