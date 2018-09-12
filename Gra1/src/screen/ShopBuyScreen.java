package screen;

import input.Button;
import input.KeyAction;
import input.KeyboardConfig;
import items.Armor;
import items.ArmorNaming;
import items.Equipable;
import items.Item;
import items.MobScrap;
import items.Potion;
import items.Weapon;
import items.InventoryManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;

import game.Constants;
import game.ScreenManager;
import graphics.DrawInventory;
import graphics.ImageHolder;

public class ShopBuyScreen extends Screen{

	private GameScreen gs;
	private int windowX;//124
	private int windowY;//95
	private int itemsX;
	private int itemsY;
	private int itemSpacingX =56;
	private int itemSpacingY=48;
	private int itemRows = 5;
	private int itemColumns = 5;
	private int itemHover = -1;
	private Button sellButton;
	
	public ShopBuyScreen(ScreenManager sm, GameScreen gs) {
		super(sm, (float) 2.0);
		this.gs = gs;
		
		windowX = (getDrawWidth()/2)-130;
		windowY = 20;
		
		itemsX = windowX-4;
		itemsY = windowY+50;
		
		sellButton = new Button(windowX+1,windowY,windowX+42,windowY+18);
	}

	public void loop(long elapsedTime) {

		checkInput();
		
	}

	protected void checkInput() {
		
		Point m = sm.dui.mouseMoved;
		sm.dui.mouseMoved = null;
		boolean hover = false;
		if(m!=null){
			for(int xw = 0;xw<itemColumns;xw++){
				if(hover)break;
				for(int yw = 0;yw<itemRows;yw++){									
					int sx = itemsX+(yw*itemSpacingX);
					int sy = itemsY+(xw*itemSpacingY);
					int size = 20;
					
					if(m.x>=sx&&m.x<=sx+size&&m.y>=sy&&m.y<=sy+size){
						itemHover = xw*5+yw;
						hover = true;
						break;
					}
				}
			}
			if(!hover)itemHover =-1;
		}
		
		m = sm.dui.mouseClicked;
		sm.dui.mouseClicked = null;
		Iterator<Item> s = gs.r.shop.iterator();
		if(m!=null){
			if(sellButton.isClicked(m)){
				sm.setScreen(new ShopSellScreen(sm,gs));
			}else if(itemHover!=-1){
				boolean done = false;
				for(int xw = 0;xw<itemColumns;xw++){
					if(done)break;
					for(int yw = 0;yw<itemRows;yw++){	
						if(!s.hasNext()){
							done = true;
							break;
						}
						Item item = s.next();
						int sx = itemsX+(yw*itemSpacingX);
						int sy = itemsY+(xw*itemSpacingY);
						int size = 20;
						
						if(m.x>=sx&&m.x<=sx+size&&m.y>=sy&&m.y<=sy+size){
							if(gs.r.getPlayer().getMoney()>=item.getValue()){
								gs.r.getPlayer().addMoney(-item.getValue());
								InventoryManager.addItem(gs.r.getPlayer().getInventory(),item);
							}
							
							done = true;
							break;
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
		
		g.setColor(new Color(255,230,0));
		//g.drawImage(gs.sm.h.buttonImg,windowX,windowY,null);
		g.drawImage(gs.sm.h.buttonImg,sellButton.getPos().x,sellButton.getPos().y,null);
		//g.fillRect(sellButton.getPos().x, sellButton.getPos().y, sellButton.getSize().x, sellButton.getSize().y);
		g.setFont(new Font(Font.MONOSPACED,Font.BOLD,12));
		//g.drawString("buy", windowX+10, windowY+13);
		g.drawString("sell",sellButton.getPos().x+8,sellButton.getPos().y+14);	
		drawBuy(gs.sm.h,gs.r.shop,g);		
		
		g.setColor(new Color(220,190,0));
		g.setFont(new Font(Font.MONOSPACED,Font.BOLD,28));
		g.drawString("Shop", windowX+92, windowY+43);
		g.setFont(new Font(Font.MONOSPACED,Font.BOLD,12));
		g.drawString("Inventory: ", windowX+85, windowY+15);
		drawMoney(g, gs.d.h);		
		
		Iterator<Item> s = gs.r.shop.iterator();
		if(itemHover!=-1){
			for(int i = 0;i<itemHover+1;i++){
				if(!s.hasNext())break;
				Item item = s.next();
				if(i == itemHover){
					DrawInventory.drawValue(g, gs.d.h, item,25,120);
					if(item instanceof Equipable){
						Item equipped = gs.r.getPlayer().equippedItems.findEquipped((Equipable) item);
						if(equipped!=null)DrawInventory.drawComparison(g, gs.d.h, equipped,25,210);
					}
				}
			}
		}
		
		/*
		for(int xw = 0;xw<itemColumns;xw++){
			for(int yw = 0;yw<itemRows;yw++){
							
				int sx = itemsX+(yw*itemSpacingX);
				int sy = itemsY+(xw*itemSpacingY);
				g.fillRect(sx, sy, 20, 20);
			}
		}*/
	}
	
	
	
	private void drawBuy(ImageHolder h, LinkedList<Item> shop,Graphics2D g){
		int ix = windowX-9;
		int iy = windowY+25;
		g.setColor(new Color(0,0,0,200));
		g.fillRect(ix, iy, 280, 240);
		for(int i = 0;i<5;i++){
			for(int j = 0;j<5;j++){
				g.drawImage(h.invTileImg,itemsX+(j*itemSpacingX)-2,itemsY+(i*itemSpacingY)-2,null);		
			}
		}
		for(int i = 0;i<15;i++){
			g.drawImage(h.borderUIImgs[1],(ix-10)+i*20,iy-7,null);	
			g.drawImage(h.borderUIImgs[1],(ix-10)+i*20,iy+240,null);
		}
		for(int i = 0;i<13;i++){
			g.drawImage(h.borderUIImgs[0],ix-5,(iy-10)+(i*20),null);	
			g.drawImage(h.borderUIImgs[0],ix+278,(iy-10)+(i*20),null);
		}
		
		Iterator<Item> i = shop.iterator();
		//sy = 134;
		//sx = 119;
		g.setFont(new Font("Dialog",Font.PLAIN,13));
		float font13ratio = (float) 1.80;	
		boolean done = false;
		
		for(int xw = 0;xw<itemColumns;xw++){
			if(done)break;
			for(int yw = 0;yw<itemRows;yw++){
				if(!i.hasNext()){
					done = true;
					break;
				}
				
				int sx = itemsX+(yw*itemSpacingX);
				int sy = itemsY+(xw*itemSpacingY);
				Item item = i.next();
				DrawInventory.drawItem(g,h,sx, sy, item);
				int money = item.getValue();
				
				int c = money%100;
				g.setColor(new Color(196,121,25));
				g.drawString(String.format("%d",c), sx+23, sy+27);
				int l = (int) (String.valueOf(c).length()/font13ratio*13);
				g.drawImage(gs.d.h.coinsImgs[0], sx+23+l, sy+18, null);
				if(money>100){
					int s = (money%100000)/100;
					g.setColor(new Color(120,140,140));
					g.drawString(String.format("%d",s), sx+23, sy+16);
					l = (int) (String.valueOf(s).length()/font13ratio*13);
					g.drawImage(gs.d.h.coinsImgs[1], sx+23+l, sy+7, null);
					if(money>100000){
						int z = money/100000;
						g.setColor(new Color(230,210,0));
						g.drawString(String.format("%s",z), sx+23, sy+5);
						l = (int) (String.valueOf(z).length()/font13ratio*13);
						g.drawImage(gs.d.h.coinsImgs[2], sx+23+l, sy-4, null);
					}
				}	
			}
		}
		
	}
	
	private void drawMoney(Graphics2D g, ImageHolder h){
		long money = gs.r.getPlayer().getMoney();
		long z = money/100000;
		long c = money%100;
		long s = (money%100000)/100;
		int a = (z+"").length();
		g.setFont(new Font(Font.MONOSPACED,Font.PLAIN,14));
		int xm = windowX+165;
		int ym = windowY+7;
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
