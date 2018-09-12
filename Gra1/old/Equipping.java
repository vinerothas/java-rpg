package items;

import game.GameManager;
import game.Region;
import graphics.DrawOld;
import graphics.UpdatePlayerAnim;
import graphics.sprite.Player;
import items.*;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Transparency;
	
	/*
	 * Class that manages equipping items, dragging,
	 * changing the cursor icon and player skin
	 */
public class Equipping {
	
	private int pos = 0;
	private DrawOld d;
	private Region r;
	
	public void draggingAction(GameManager gm,int pos){
		r = gm.r;
		d = gm.d;
		this.pos = pos;
		if(pos>0&&d.dui.itemHeld==null){
			startDragging(d);
		}else if(pos== -1&&d.dui.itemHeld!=null){
			stopDragging(d);
		}else if(pos>0&&d.dui.itemHeld!=null){
			equip(d);
		}else if(pos<0&&d.dui.itemHeld==null){
			sideDragging(d);
		}
	}
	
	public void rightClick(GameManager gm,int pos){
		r = gm.r; 
		d = gm.d;
		Player player = r.getPlayer();
		this.pos = pos;
		if(pos>0){
			InventoryManager invM = new InventoryManager();
			Item i = invM.getLootPos(pos, r.inventory).getItem();
			if(i instanceof Equipable){
				Equipable eq = (Equipable) i;
				int slot = eq.getSlot();
				slot = getItemToEquipSlot(slot);
				if(player.equips[slot]!=null){
					if(eq instanceof Armor){
						player.addArmor(-((Armor) player.equips[slot]).getArmor());
						player.addArmor(((Armor) eq).getArmor());	
					}
					invM.removeItem(pos, r.inventory);
					invM.addItem(player.equips[slot], r.inventory);
					player.equips[slot]= eq;
				}else{
					if(eq instanceof Armor)player.addArmor(((Armor) eq).getArmor());
					player.equips[slot]= eq;
					invM.removeItem(pos, r.inventory);
				}
				if(slot!=0){
					//0 - gloves
					changeArmor(d);
				}
			}
		}

	}
	
	/**
	 * Dragging of equipped items
	 */
	private void sideDragging(DrawOld d){
		try{
			Item i = r.getPlayer().equips[(-d.dui.getItemHeld())-1];
			if(i instanceof Equipable){
				Equipable eq = (Equipable) i;
				d.dui.itemHeld = eq;
				d.dui.setItemTaken(pos);
				Image cursorImg = d.w.gc.createCompatibleImage(32,32,Transparency.BITMASK);
				Image armorImg = d.getArmorImg(eq.getType(),eq.getSlot());
				Graphics2D g = (Graphics2D)cursorImg.getGraphics();
				g.setColor(new Color(219,124,0));
				g.drawImage(armorImg, 0, 0, null);
				g.fillRect(0,0,6,2);
				g.fillRect(0,0,2,6);
				g.dispose();
				Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(
						cursorImg, new Point(0, 0), "itemHeld");
				d.w.window.setCursor(cursor);
			}
		}catch(Exception ex){}
		d.dui.setItemHeld(0);
	}
	
	private void startDragging(DrawOld d){
		try{
			InventoryManager invM = new InventoryManager();
			Item i = invM.getLootPos(pos, r.inventory).getItem();
			if(i instanceof Equipable){
				Equipable eq = (Equipable) i;
				d.dui.itemHeld = eq;
				d.dui.setItemTaken(pos);
				Image cursorImg = d.w.gc.createCompatibleImage(32,32,Transparency.BITMASK);
				Image armorImg = d.getArmorImg(eq.getType(),eq.getSlot());
				Graphics2D g = (Graphics2D)cursorImg.getGraphics();
				g.setColor(new Color(219,124,0));
				g.drawImage(armorImg, 0, 0, null);
				g.fillRect(0,0,6,2);
				g.fillRect(0,0,2,6);
				g.dispose();
				Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(
						cursorImg, new Point(0, 0), "itemHeld");
				d.w.window.setCursor(cursor);
			}
		}catch(Exception ex){}
		d.dui.setItemHeld(0);
	}
	
	private void stopDragging(DrawOld d){
		int pos = d.dui.getItemTaken();
		if(pos<0){
			pos = (-pos)-1;
			new InventoryManager().addItem(r.getPlayer().equips[pos],r.inventory);
			if(d.dui.itemHeld instanceof Armor)r.getPlayer().addArmor(-((Armor) r.getPlayer().equips[pos]).getArmor());
			r.getPlayer().equips[pos] = null;
			if(pos!=0){
				//0 - gloves
				changeArmor(d);
			}
		}
		Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(
				d.h.cursorImg, new Point(0, 0), "itemHeld");
		d.w.window.setCursor(cursor);
		d.dui.itemHeld = null;
		d.dui.setItemHeld(0);
		d.dui.setItemTaken(0);
	}
	
	private void equip(DrawOld d){
		if(d.dui.getItemTaken()<0){
			stopDragging(d);
			return;
		}
		int slot = ((Equipable) d.dui.itemHeld).getSlot();
		Player player = r.getPlayer();
		if(rightSlot(pos,slot)==true){
			if(player.equips[pos-1]==null){
				player.equips[pos-1]=d.dui.itemHeld;
				if(d.dui.itemHeld instanceof Armor)player.addArmor(((Armor) d.dui.itemHeld).getArmor());
				new InventoryManager().removeItem(d.dui.getItemTaken(), r.inventory);				
			}else{
				new InventoryManager().removeItem(d.dui.getItemTaken(), r.inventory);
				new InventoryManager().addItem(player.equips[pos-1], r.inventory);
				if(d.dui.itemHeld instanceof Armor)player.addArmor(-((Armor) player.equips[pos-1]).getArmor());
				player.equips[pos-1]=d.dui.itemHeld;
				if(d.dui.itemHeld instanceof Armor)player.addArmor(((Armor) d.dui.itemHeld).getArmor());
			}
			if(pos!=0){
				//0 - gloves
				changeArmor(d);
			}
			d.dui.setItemTaken(0);
		}
		d.dui.itemHeld=null;
		d.dui.setItemHeld(0);
		Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(
				d.h.cursorImg, new Point(0, 0), "itemHeld");
		d.w.window.setCursor(cursor);
	}
	
	private void changeArmor(DrawOld d){
		r.getPlayer().updatePower();
		new UpdatePlayerAnim().loadPlayerAnim(d);
	}
	
	private boolean rightSlot(int pos, int item){
		pos--;
		if(pos==0&&item==6)return true;
		if(pos==1&&item==3)return true;
		if(pos==2&&item==5)return true;
		if(pos==4&&item==2)return true;
		if(pos==7&&item==4)return true;
		if(pos==8&&item==1)return true;
		if(pos==3&&item==7)return true;
		return false;
	}
	
	private int getItemToEquipSlot(int item){
		if(item==1)return 8;
		if(item==2)return 4;
		if(item==3)return 1;
		if(item==4)return 7;
		if(item==5)return 2;
		if(item==6)return 0;
		if(item==7)return 3;
		return -1;
	}
}
