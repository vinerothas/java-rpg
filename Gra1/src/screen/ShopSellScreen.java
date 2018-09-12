package screen;

import input.Button;
import input.KeyAction;
import input.KeyboardConfig;
import items.Item;
import items.InventoryManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;

import game.ScreenManager;
import graphics.DrawInventory;
import graphics.ImageHolder;

public class ShopSellScreen extends Screen{

	private GameScreen gs;
	private Button buyButton;
	private int windowX;
	private int windowY;
	private int inventoryX;
	private int inventoryY;
	private Point hoverPos = null;
	
	public ShopSellScreen(ScreenManager sm, GameScreen gs) {
		super(sm, (float) 2.0);
		this.gs = gs;
		
		windowX = (getDrawWidth()/2)-130;
		windowY = 20;
		inventoryX = windowX+135;
		inventoryY = windowY+50;
		buyButton = new Button(windowX+1,windowY,windowX+42,windowY+18);
	}

	public void loop(long elapsedTime) {

		checkInput();
		
	}

	protected void checkInput() {
		
		Point m = sm.dui.mouseMoved;
		sm.dui.mouseMoved = null;
		if(m!=null){
			boolean found = false;
			int startX = inventoryX+2;
			int startY = inventoryY+2;
			int tileX = 20;
			int tileY = 20;
			int border = 4;
			int inventoryWidth = 5;
			int inventoryHeight = 8;
			for(int xi = 0;xi<inventoryWidth;xi++){
				if(found)break;
				for(int yi = 0;yi<inventoryHeight;yi++){
					int xs=startX+xi*(tileX+border);
					int xs2= xs+tileX;
					int ys = startY+yi*(tileY+border);
					int ys2 = ys+tileY;
					if(m.x>xs&&m.x<xs2&&m.y>ys&&m.y<ys2){
						hoverPos = new Point(xi,yi);
						found = true;
					}
				}
			}
			if(!found)hoverPos = null;
		}
		
		m = sm.dui.mouseClicked;
		sm.dui.mouseClicked = null;
		if(m!=null){
			if(buyButton.isClicked(m)){
				sm.setScreen(new ShopBuyScreen(sm,gs));
			}else{
				boolean found = false;
				int startX = inventoryX+2;
				int startY = inventoryY+2;
				int tileX = 20;
				int tileY = 20;
				int border = 4;
				int inventoryWidth = 5;
				int inventoryHeight = 8;
				for(int xi = 0;xi<inventoryWidth;xi++){
					if(found)break;
					for(int yi = 0;yi<inventoryHeight;yi++){
						int xs=startX+xi*(tileX+border);
						int xs2= xs+tileX;
						int ys = startY+yi*(tileY+border);
						int ys2 = ys+tileY;
						if(m.x>xs&&m.x<xs2&&m.y>ys&&m.y<ys2){
							Item item = gs.r.getPlayer().getInventory()[yi][xi];
							if(item!=null){
								gs.r.getPlayer().addMoney(item.getValue());
								InventoryManager.removeItem(gs.r.getPlayer().getInventory(), item);
							}
							found = true;
						}
					}
				}
				
			}
		}
		
		
		KeyAction[] keyActions = sm.dui.getKeyActions();
		KeyboardConfig kc = sm.kc;
		for(KeyAction a : keyActions){
			if(a==null)break;
			int k = a.getKeyCode();
			if(a.isPressed()){
				if(k == kc.MENU){
					sm.setScreen(gs);
				}
			}
		}
		sm.dui.clearInput();
	}

	
	public void draw(Graphics2D g) {
		g.drawImage(sm.h.shopBG, 0, 0,getDrawWidth(),getDrawHeight(), null);
		
		g.drawImage(gs.sm.h.buttonImg,buyButton.getPos().x,buyButton.getPos().y,null);
		g.setColor(new Color(255,230,0));
		g.setFont(new Font(Font.MONOSPACED,Font.BOLD,12));
		g.drawString("buy",buyButton.getPos().x+10,buyButton.getPos().y+13);	
		
		drawWindow(g,gs.sm.h);
		
		int x = inventoryX;
		int y = inventoryY;
		int iw = sm.h.invTileImg.getWidth(null);
		int ih = sm.h.invTileImg.getHeight(null);
		Item[][] inventory = gs.r.getPlayer().getInventory();
		int rows = inventory.length;
		int columns = inventory[0].length;
		for(int yi = 0;yi<rows;yi++){
			for(int xi = 0;xi<columns;xi++){
				int xpos = x+(xi*iw);
				int ypos = y+(yi*ih);
				g.drawImage(sm.h.invTileImg, xpos, ypos, null);
				if(inventory[yi][xi]!=null){
					DrawInventory.drawItem(g,sm.h,xpos+2,ypos+2,inventory[yi][xi]);					
				}
			}
		}
		
		if(hoverPos!=null){
			Item item = gs.r.getPlayer().getInventory()[hoverPos.y][hoverPos.x];
			if(item!=null){
				DrawInventory.drawValue(g,gs.d.h,item,windowX-20,windowY+150);
			}
		}
	}
	
	private void drawWindow(Graphics2D g, ImageHolder h){
		int ix = windowX-9;
		int iy = windowY+25;
		g.setColor(new Color(0,0,0,200));
		g.fillRect(ix, iy, 280, 240);
		
		for(int i = 0;i<15;i++){
			g.drawImage(h.borderUIImgs[1],(ix-10)+i*20,iy-7,null);	
			g.drawImage(h.borderUIImgs[1],(ix-10)+i*20,iy+240,null);
		}
		for(int i = 0;i<13;i++){
			g.drawImage(h.borderUIImgs[0],ix-5,(iy-10)+(i*20),null);	
			g.drawImage(h.borderUIImgs[0],ix+278,(iy-10)+(i*20),null);
		}
	}
	
	private void drawMoney(Graphics2D g, ImageHolder h){
		long money = gs.r.getPlayer().getMoney();
		long z = money/100000;
		long c = money%100;
		long s = (money%100000)/100;
		int a = (z+"").length();
		g.setFont(new Font(Font.MONOSPACED,Font.PLAIN,14));
		//XXX int xm = windowX+165;
		//XXX int ym = windowY+7;
		int xm = 0;
		int ym = 0;
		int yOffset = 9;
		if(a==5){
			xm+=3;
		}
		if(z>0){
			g.drawImage(h.coinsImgs[2], xm+26, ym, null);
			g.drawString(String.format("%s",z), xm+23-((int)(a*7.5)), ym+yOffset);
		}
		if(s>0){
			g.drawImage(h.coinsImgs[1], xm+65, ym, null);
			g.setColor(new Color(191,210,209));
			if(s<10){
				g.drawString(String.format("%s",s), xm+55, ym+yOffset);
			}else if(s>10&&s<100){
				g.drawString(String.format("%s",s), xm+45, ym+yOffset);
			}else{
				g.drawString(String.format("%s",s), xm+40, ym+yOffset);
			}		
		}
		g.drawImage(h.coinsImgs[0], xm+95, ym, null);
		g.setColor(new Color(196,121,25));
		if(c>10){
			g.drawString(String.format("%s",c), xm+78, ym+yOffset);
		}else{
			g.drawString(String.format("%s",c), xm+85, ym+yOffset);
		}	
	}
	
}
