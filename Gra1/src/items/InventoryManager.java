package items;

import graphics.sprite.Player;

import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;

public class InventoryManager {
	
	/**
	 * 
	 * @param inventory Inventory where the item needs to be added
	 * @param item Item to be added
	 * @return true if added, false if inventory full
	 */
	public static boolean addItem(Item[][] inventory, Item item){
		if(item instanceof MobScrap)return addMobScrap(inventory,(MobScrap) item);
		else return addItemAbsolute(inventory, item);
	}
	
	private static boolean addMobScrap(Item[][] inventory, MobScrap ms){
		int rows = inventory.length;
		int columns = inventory[0].length;
		for(int y = 0;y<rows;y++){
			for(int x = 0;x<columns;x++){
				Item item = inventory[y][x];
				if(item==null)continue;
				if(item instanceof MobScrap == false)continue;
				MobScrap ms2 = (MobScrap) item;
				if(ms2.getType()!=ms.getType())continue;
				ms2.increaseValue(ms);
				return true;		
			}
		}
		return addItemAbsolute(inventory,ms);
	}
	
	/**
	 * Either adds item to a free slot or not at all
	 */
	private static boolean addItemAbsolute(Item[][] inventory, Item item){
		int rows = inventory.length;
		int columns = inventory[0].length;
		for(int y = 0;y<rows;y++){
			for(int x = 0;x<columns;x++){
				if(inventory[y][x]==null){
					inventory[y][x]=item;
					return true;
				}
			}
		}
		return false;
	}
	
	public static void removeItem(Item[][] inventory, Item item){
		int rows = inventory.length;
		int columns = inventory[0].length;
		for(int y = 0;y<rows;y++){
			for(int x = 0;x<columns;x++){
				if(inventory[y][x]==item){
					inventory[y][x]=null;
					return;
				}
			}
		}
	}
	
	/*
	public int getNewPos(LinkedList<Loot> inv){
		int pos =0;
		Iterator<Loot> i = inv.iterator();
		while(i.hasNext()){
			i.next();
			pos++;
		}
		pos++;
		return pos;
	}
	
	public Point getShopPos(int pos){
		int x = 119;
		int y = 134;
		int posx = ((pos-1)%5);
		int posy = (pos-1)/5;
		int dx = 56;
		int dy = 48;
		return new Point(x+(posx*dx)+10,y+(posy*dy)+10);
	}
	
	public Loot getLootPos(int pos, LinkedList<Loot> inv){
		Iterator<Loot> i = inv.iterator();
		while(i.hasNext()){
			Loot l = i.next();
			if(l.getPos()==pos){
				return l;
			}
		}
		return null;
	}
	
	public Point getLootLoc(int pos){
		int firstX = 119;
		int firstY = 122;
		int wh = 19;
		int xdis = 28;
		int ydis = 24;
		int posx = pos%10;
		posx--;
		if(posx==-1){
			posx=10;
		}
		int posy = pos/10;
		int x = firstX+(posx*xdis);
		int xmid = x+(wh/2);
		int y = firstY+(posy*ydis);
		int ymid = y+(wh/2);
		return new Point(xmid,ymid);
	}
	
	/**
	 * Checks if there's already an item of this type
	 *  in inventory, if so, ups it by one
	 *
	public boolean checkDuplicate(LinkedList<Loot> inv, Item item){
		if(item instanceof MobScrap){
			int type = ((MobScrap) item).getType();
			Iterator<Loot> i = inv.iterator();			
			while(i.hasNext()){
				Loot l = i.next();
				Item item2 = l.getItem();
				if(item2 instanceof MobScrap){
					if(((MobScrap) item2).getType() == type){
						l.setQuantity(l.getQuantity()+1, l.getValue()+item.getValue());
						return true;
					}
				}else{
					continue;
				}
			}
		}
		if(item instanceof Potion){
			Iterator<Loot> i = inv.iterator();			
			while(i.hasNext()){
				Loot l = i.next();
				if(l.getItem() instanceof Potion){
					l.setQuantity(l.getQuantity()+1, l.getValue()+item.getValue());
					return true;
				}
			}
		}
		return false;
	}
	
	public void removeItem(int pos, LinkedList<Loot> inv){
		Iterator<Loot> i = inv.iterator();
		while(i.hasNext()){
			Loot loot = i.next();
			if(loot.getPos()==pos){
				inv.remove(loot);	
				resetPos(pos,inv);
				return;
			}
		}
	}
	
	public void addItem(Item item, LinkedList<Loot> inventory){
		if(checkDuplicate(inventory, item)==false){
			Loot loot = new Loot(item);
			loot.setPos(getNewPos(inventory));
			inventory.add(loot);
		}
	}
	
	public void sellItem(Loot loot, int quantity,LinkedList<Loot> inventory, Player player){
		if(quantity==loot.getQuantity()){
			player.addMoney(loot.getValue());
			int pos = loot.getPos();
			inventory.remove(loot);	
			resetPos(pos,inventory);
		}else{
			int value = (int) Math.ceil((double)(loot.getValue()*quantity)/(double)(loot.getQuantity()));
			player.addMoney(value);
			loot.setQuantity(loot.getQuantity()-quantity,loot.getValue()+-value);
		}
	}
	
	private void resetPos(int pos, LinkedList<Loot> inv){
		Iterator<Loot> i = inv.iterator();
		while(i.hasNext()){
			Loot l = i.next();
			if(l.getPos()>pos){
				l.setPos(l.getPos()-1);
			}
		}
	}
	*/
}
