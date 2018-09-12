package graphics;

import input.ItemQBox;
import items.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Iterator;

public class DrawBuilding {

	public void drawBuilding(DrawOld d, Graphics2D g){
		
		new DrawUI().drawUI(d);
		
		int b = d.dui.isInBuilding();
		if(b==2){
			if(d.dui.getPState()!=4){
				drawTavern(d,g);			
				d.drawExitButton();
			}else{
				return;
			}
		}else if(b==4 || b==5){
			g.drawImage(d.h.shopBG, 0, 0, null);
			int x = 124;
			int y = 95;
			g.drawImage(d.h.buttonImg,x,y,null);
			g.drawImage(d.h.buttonImg,x+40,y,null);
			d.getDialogFont();
			g.drawString("buy", x+10, y+15);
			g.drawString("sell",x+50,y+15);	
			if(b==5){
				g.drawImage(d.h.buttonImg,x+80,y,110,20,null);
				g.drawString("sell mob loot",x+90,y+15);	
				drawSell(d,g);
			}else if(b==4){
				drawBuy(d,g);
			}
			d.drawExitButton();
		}
		
		int pos = d.dui.getItemHover();
		if(pos!=0 && d.dui.isInBuilding()==4){
			Iterator<Item> it = d.r.shop.iterator();
			int curPos = 1;
			while(it.hasNext()){
				Item item = it.next();
				if(curPos==pos){
					Point p = new InventoryManager().getShopPos(pos);
					d.drawValue(p.x,p.y,item,item.getValue());
					break;
				}
				curPos++;
			}
		}
	}
	
	private void drawTavern(DrawOld d, Graphics2D g) {

		g.drawImage(d.h.tavernBG, 0, 0, null);
		int x = 34;
		int y = 350;
		g.drawImage(d.h.buttonImg, x, y,120,24,null);
		g.drawImage(d.h.buttonImg, x, y+23,120,24,null);
		d.getDialogFont();
		g.drawString("Rest", x+46, y+17);
		g.drawString("Play Poker", x+26, y+40);
	}

	private void drawBuy(DrawOld d, Graphics2D g){
		int ix = 115;
		int iy = 120;
		g.setColor(new Color(235,230,210));
		g.fillRect(ix, iy, 280, 240);
		for(int i = 0;i<5;i++){
			for(int j = 0;j<5;j++){
				g.drawImage(d.h.invTileImg,ix+(j*56),iy+(i*48)+12,null);		
			}
		}
		for(int i = 0;i<15;i++){
			g.drawImage(d.h.borderUIImgs[1],(ix-10)+i*20,iy-7,null);	
			g.drawImage(d.h.borderUIImgs[1],(ix-10)+i*20,iy+240,null);
		}
		for(int i = 0;i<13;i++){
			g.drawImage(d.h.borderUIImgs[0],ix-5,(iy-10)+(i*20),null);	
			g.drawImage(d.h.borderUIImgs[0],ix+278,(iy-10)+(i*20),null);
		}
		
		Iterator<Item> i = d.r.shop.iterator();
		int sx = 119;
		int sy = 134;
		g.setFont(new Font("Dialog",Font.BOLD,13));
		while(i.hasNext()){
			Item item = i.next();
			d.drawItem(sx, sy, item);
			
			int money = item.getValue();
			
			int c = money%100;
			g.setColor(new Color(196,121,25));
			g.drawString(String.format("%02d",c), sx+23, sy+25);
			if(money>100){
				int s = (money%100000)/100;
				g.setColor(new Color(120,140,140));
				g.drawString(String.format("%03d",s), sx+23, sy+14);
				if(money>100000){
					int z = money/100000;
					g.setColor(new Color(230,210,0));
					g.drawString(String.format("%s",z), sx+23, sy+3);
					}
			}				
			sx+=56;
			if(sx> 364){
				sy+=48;
				sx=119;
			}
		}
		
	}
	
	private void drawSell(DrawOld d, Graphics2D g){
		d.drawInv();
		ItemQBox qbox = d.dui.qbox;
		if(qbox != null){
			int qx = 150;
			int qy = 170;
			Loot loot = null;
			Iterator<Loot> i = d.r.inventory.iterator();
			while(i.hasNext()){
				Loot l = i.next();
				if(l.getPos()==(-qbox.getPos())){
					loot = l;
				}
			}					
			if(loot != null){
				if(loot.getQuantity()>2&&qbox.getSell()==false){
				d.drawYesNoWindow(false);
				g.drawImage(d.h.arrowsUIImg,qx+80,qy+46,null);
				d.drawItem(qx+35,qy+45,loot.getItem());
				d.getDialogFont();
				if(qbox.getQuantity()>loot.getQuantity()){
					qbox.setQuantity(loot.getQuantity());
				}
				g.drawString(String.format("%s",qbox.getQuantity()),qx+86,qy+61);
				g.drawString("ok",qx+45,qy+107);
				g.drawImage(d.h.buttonImg,qx+81,qy+93,null);
				g.drawString("all",qx+93,qy+106);
				g.drawString("exit",qx+134,qy+106);
				}else{
					int q = 1;
					if(qbox.getSell()==true){
						q=qbox.getQuantity();
					}
					d.dui.qbox = null;
					InventoryManager invM = new InventoryManager();
					invM.sellItem(loot, q, d.r.inventory, d.r.getPlayer());
				}
			}else{
				d.dui.qbox = null;
			}			
		}
	}
	
}
