package game;

import graphics.DataUI;
import graphics.sprite.Enemy;
import graphics.sprite.Player;
import items.InventoryManager;
import items.Item;
import items.Loot;
import items.MobScrap;

import java.io.IOException;
import java.util.Iterator;

import poker.PokerLoop;

public class PlayerEvents{

	private DataUI dui;
	private GameManager gm;
	
	public PlayerEvents(GameManager gm){
		dui = gm.dui;
		this.gm = gm;
	}
	
	public void pickAction(){	
		int s = dui.getPState(); 
		int b = dui.isInBuilding();
		int pos = dui.getBuyItem();
		int dir = dui.getTravelDir();
		
		if(s==4){
			new PokerLoop().loop(gm.d,gm);
			return;
		}
		if(dir!=0 && s==3){
			travel();
			return;
		}
		if(s==3 && pos !=0){
			if(b==4 && pos>0){
				buyItem();
			}else if(b==5&&pos<0){
				sellItem();				
			}
			dui.setBuyItem(0);
			dui.setPState(0);
			return;
		}
		if(s == 1){
			resetPlayer();
		}else if(s==2){
			gm.running = false;
		}else if(s==3){
			if(b ==2){
				//zaplacic
				gm.r.getPlayer().setLife(gm.r.getPlayer().getMhp());
				dui.setInBuilding(0);
			}else if(b ==5){	
				while(true){
					if(sellMob()==false)break;
				}
			}
		}
		dui.setPState(0);
	}
	
	private void travel(){
		SaveLoad sl = new SaveLoad();
		try {
			int x = gm.r.x;
			int y = gm.r.y;
			int dir = dui.getTravelDir();
			//System.out.println(dir);
			if(dir==1){
				y-=1;
			}else if(dir==2){
				x+=1;
			}else if(dir==3){
				y+=1;
			}else if(dir==4){
				x-=1;
			}
			sl.makeFile(gm.r);
			gm.r.cleanRegion();
			sl.loadMap(gm.r,gm.d,x,y);
			if(dir==1){
				gm.r.getPlayer().setY(3950);
				gm.r.getPlayer().setX(0);
			}else if(dir==2){
				gm.r.getPlayer().setX(-3980);
				gm.r.getPlayer().setY(0);
			}else if(dir==3){
				gm.r.getPlayer().setY(-3950);
				gm.r.getPlayer().setX(0);
			}else if(dir==4){
				gm.r.getPlayer().setX(3980);
				gm.r.getPlayer().setY(0);
			}
			dui.setTravelDir(0);
			dui.setPState(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	private void resetPlayer() {
		SaveLoad sl = new SaveLoad();
		sl.makeFile(gm.r);
		gm.r.cleanRegion();
		try {
			sl.loadMap(gm.r,gm.d,0,0);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Player player = gm.r.getPlayer();
		player.setLife(player.getMhp());
		player.setX(0);
		player.setY(0);
		dui.clearTextUI();			
		dui.setDrawUI(0);
	}
	
	private boolean sellMob(){
		Iterator<Loot> i = gm.r.inventory.iterator();
		while(i.hasNext()){
			Loot l = i.next();
			if(l.getItem() instanceof MobScrap){
				InventoryManager invM = new InventoryManager();
				invM.sellItem(l, l.getQuantity(), gm.r.inventory, gm.r.getPlayer());
				return true;
			}
		}
		return false;
	}

	private void sellItem(){
		int pos = dui.getBuyItem();
		pos= -pos;
		Iterator<Loot> i = gm.r.inventory.iterator();
		int itemPos = 0;
		Item item = null;
		while(i.hasNext()){
			Loot l = i.next();
			Item it = l.getItem();
			itemPos=l.getPos();
			if(itemPos==pos){
				item = it;
				break;
			}
		}
		if(item==null){
			dui.setBuyItem(0);
			dui.setPState(0);
			return;
		}
	}
	
	private void buyItem(){
		Iterator<Item> i = gm.r.shop.iterator();
		int itemPos = 0;
		int pos = dui.getBuyItem();
		Item item = null;
		while(i.hasNext()){
			Item it = i.next();
			itemPos++;
			if(itemPos==pos){
				item = it;
				break;
			}
		}
		if(item==null){
			dui.setBuyItem(0);
			dui.setPState(0);
			return;
		}else if(item.getValue()>gm.r.getPlayer().getMoney()){
			dui.setBuyItem(0);
			dui.setPState(0);
			return;
		}
		gm.r.getPlayer().addMoney(-item.getValue());
		new InventoryManager().addItem(item,gm.r.inventory);
	}
	
}
