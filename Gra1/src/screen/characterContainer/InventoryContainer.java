package screen.characterContainer;

import game.Constants;
import graphics.ImageHolder;
import graphics.MathTool;
import graphics.sprite.Player;
import graphics.DrawInventory;

import items.Armor;
import items.ArmorNaming;
import items.Equipable;
import items.InventoryManager;
import items.Item;
import items.MobScrap;
import items.Potion;
import items.Weapon;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;

public class InventoryContainer extends CharacterContainer{
	
	public final static int width = 140;
	public final static int height = 225;
	private Player player;
	public Point hoverPos = null;

	public InventoryContainer(int sizeX, Player player) {
		super((sizeX/2), 0, width, height);
		this.player = player;
	}

	@Override
	public void draw(Graphics2D g, ImageHolder h) {
		int iw = h.invTileImg.getWidth(null);
		int ih = h.invTileImg.getHeight(null);
		Item[][] inventory = player.getInventory();
		int rows = inventory.length;
		int columns = inventory[0].length;
		for(int yi = 0;yi<rows;yi++){
			for(int xi = 0;xi<columns;xi++){
				int xpos = x+(xi*iw)+10;
				int ypos = y+(yi*ih)+10;
				g.drawImage(h.invTileImg, xpos, ypos, null);
				if(inventory[yi][xi]!=null){
					DrawInventory.drawItem(g,h,xpos+2,ypos+2,inventory[yi][xi]);					
				}
			}
		}
		drawMoney(g,h);	
		
		//g.drawRect(x, y, width, height);
	}
	
	private void drawMoney(Graphics2D g, ImageHolder h){
		long money = player.getMoney();
		long z = money/100000;
		long c = money%100;
		long s = (money%100000)/100;
		int a = (z+"").length();
		g.setFont(new Font(Font.MONOSPACED,Font.PLAIN,14));
		int xm = x+25;
		int ym = y+height-15;
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
		if(c>9){
			g.drawString(String.format("%s",c), xm+78, ym+yOffset);
		}else{
			g.drawString(String.format("%s",c), xm+85, ym+yOffset);
		}	
	}
	
	public boolean handleHover(Point m){
		if(m.x>x&&m.x<x2&&m.y>y&&m.y<y2){
			int startX = x+12;
			int startY = y+12;
			int tileX = 20;
			int tileY = 20;
			int border = 4;
			int inventoryWidth = 5;
			int inventoryHeight = 8;
			for(int xi = 0;xi<inventoryWidth;xi++){
				for(int yi = 0;yi<inventoryHeight;yi++){
					int xs=startX+xi*(tileX+border);
					int xs2= xs+tileX;
					int ys = startY+yi*(tileY+border);
					int ys2 = ys+tileY;
					if(m.x>xs&&m.x<xs2&&m.y>ys&&m.y<ys2){
						hoverPos = new Point(xi,yi);
						//System.out.println(hoverPos);
						return true;
					}
				}
			}
			hoverPos = null;

			return true;
		}
		hoverPos = null;
		return false;
	}
		

	public void handleInput(Point m) {
		
		int startX = x+12;
		int startY = y+12;
		int tileX = 20;
		int tileY = 20;
		int border = 4;
		int inventoryWidth = 5;
		int inventoryHeight = 8;
		for(int xi = 0;xi<inventoryWidth;xi++){
			for(int yi = 0;yi<inventoryHeight;yi++){
				int xs=startX+xi*(tileX+border);
				int xs2= xs+tileX;
				int ys = startY+yi*(tileY+border);
				int ys2 = ys+tileY;
				if(m.x>xs&&m.x<xs2&&m.y>ys&&m.y<ys2){
					Item item = player.getInventory()[yi][xi];
					if(item!=null){
						if(item instanceof Armor){
							Armor removed = player.addArmor((Armor) item);
							InventoryManager.removeItem(player.getInventory(), item);
							InventoryManager.addItem(player.getInventory(),removed);
						}else if(item instanceof Weapon){
							Weapon removed = player.addWeapon((Weapon) item);
							InventoryManager.removeItem(player.getInventory(), item);
							InventoryManager.addItem(player.getInventory(),removed);
						}
					}
					return;
				}
			}
		}
		
	}

}
