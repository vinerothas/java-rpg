package screen.characterContainer;

import game.Constants;
import graphics.ImageHolder;

import items.Equipable;
import items.EquippedItems;
import items.Item;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class ArmorContainer extends CharacterContainer{
	
	public final static int width = 140;
	public final static int height = 130;
	private Slot headSlot;
	private Slot shouldersSlot;
	private Slot handsSlot;
	private Slot chestSlot;
	private Slot weaponSlot;
	private Slot legsSlot;
	private Slot feetSlot;
	private EquippedItems ei;
	public Item hoverItem = null;

	public ArmorContainer(int sizeX, EquippedItems ei) {
		super((sizeX/2)-width, 0, width, height);
		this.ei = ei;
		int slotSize = 25;
		int slotDist = 30;
		int xs = x+(width/2)-(slotSize/2)-slotDist;
		int ybegin = y+10;
		int ys = ybegin;
		handsSlot = new Slot(xs,ys);
		ys+=slotDist;
		weaponSlot = new Slot(xs,ys);
		
		xs+=slotDist;
		ys=ybegin;
		headSlot = new Slot(xs,ys);
		ys+=slotDist;
		chestSlot = new Slot(xs,ys);
		ys+=slotDist;
		legsSlot = new Slot(xs,ys);
		ys+=slotDist;
		feetSlot = new Slot(xs,ys);
		
		xs+=slotDist;
		ys=ybegin;
		shouldersSlot = new Slot(xs,ys);
		
	}

	@Override
	public void draw(Graphics2D g, ImageHolder h) {
		//g.drawRect(x, y, width, height);
		
		Equipable e = ei.getHands();
		if(e!=null)handsSlot.drawSlot(g, h.slotUIImg, e.getImage(h));
		else handsSlot.drawSlot(g, h.slotUIImg, h.armorUIImgs[Constants.ITEM_SLOT_HANDS]);
		e = ei.getHead();
		if(e!=null)headSlot.drawSlot(g, h.slotUIImg, e.getImage(h));
		else headSlot.drawSlot(g, h.slotUIImg, h.armorUIImgs[Constants.ITEM_SLOT_HEAD]);
		e = ei.getShoulders();
		if(e!=null)shouldersSlot.drawSlot(g, h.slotUIImg, e.getImage(h));
		else shouldersSlot.drawSlot(g, h.slotUIImg, h.armorUIImgs[Constants.ITEM_SLOT_SHOULDERS]);
		e = ei.getChest();
		if(e!=null)chestSlot.drawSlot(g, h.slotUIImg, e.getImage(h));
		else chestSlot.drawSlot(g, h.slotUIImg, h.armorUIImgs[Constants.ITEM_SLOT_CHEST]);
		e = ei.getLegs();
		if(e!=null)legsSlot.drawSlot(g, h.slotUIImg, e.getImage(h));
		else legsSlot.drawSlot(g, h.slotUIImg, h.armorUIImgs[Constants.ITEM_SLOT_LEGS]);
		e = ei.getFeet();
		if(e!=null)feetSlot.drawSlot(g, h.slotUIImg, e.getImage(h));
		else feetSlot.drawSlot(g, h.slotUIImg, h.armorUIImgs[Constants.ITEM_SLOT_FEET]);
		e = ei.getWeapon();
		if(e!=null)weaponSlot.drawSlot(g, h.slotUIImg, e.getImage(h));
		else weaponSlot.drawSlot(g, h.slotUIImg, h.armorUIImgs[Constants.ITEM_SLOT_WEAPON]);
		
	}

	@Override
	public void handleInput(Point m) {
		// TODO Auto-generated method stub
		
	}

	public void handleHover(Point m){
		if(headSlot.insideSlot(m))hoverItem = ei.getHead();
		else if(handsSlot.insideSlot(m))hoverItem = ei.getHands();
		else if(chestSlot.insideSlot(m))hoverItem = ei.getChest();
		else if(legsSlot.insideSlot(m))hoverItem = ei.getLegs();
		else if(feetSlot.insideSlot(m))hoverItem = ei.getFeet();
		else if(weaponSlot.insideSlot(m))hoverItem = ei.getWeapon();
		else if(shouldersSlot.insideSlot(m))hoverItem = ei.getShoulders();
		else  hoverItem = null;
	}
	
}
