package items;

import game.Constants;

public class EquippedItems {
	
	private Armor head = null;
	private Armor shoulders = null;
	private Armor hands = null;
	private Armor chest = null;
	private Armor legs = null;
	private Armor feet = null;
	private Weapon weapon = null;
	
	public Armor getHead(){
		return head;
	}
	public Armor getShoulders(){
		return shoulders;
	}
	public Armor getHands(){
		return hands;
	}
	public Armor getChest(){
		return chest;
	}
	public Armor getLegs(){
		return legs;
	}
	public Armor getFeet(){
		return feet;
	}
	public Weapon getWeapon(){
		return weapon;
	}
	
	public void loadEquipement(Equipable e){
		if(e instanceof Armor){
			Armor a = (Armor) e;
			if(e.getSlot()==Constants.ITEM_SLOT_HEAD)head = a;
			else if(e.getSlot()==Constants.ITEM_SLOT_SHOULDERS)shoulders = a;
			else if(e.getSlot()==Constants.ITEM_SLOT_HANDS)hands = a;
			else if(e.getSlot()==Constants.ITEM_SLOT_CHEST)chest = a;
			else if(e.getSlot()==Constants.ITEM_SLOT_LEGS)legs = a;
			else if(e.getSlot()==Constants.ITEM_SLOT_FEET)feet = a;
		}else if(e instanceof Weapon){
			Weapon w = (Weapon) e;
			if(e.getSlot()==Constants.ITEM_SLOT_WEAPON)weapon = w;
		}
	}
	
	public boolean addHead(Equipable e){
		if(e instanceof Armor == false)return false;
		if(e.getSlot()!=Constants.ITEM_SLOT_HEAD)return false;
		head = (Armor) e;
		return true;
	}
	
	public boolean addShoulders(Equipable e){
		if(e instanceof Armor == false)return false;
		if(e.getSlot()!=Constants.ITEM_SLOT_SHOULDERS)return false;
		shoulders = (Armor) e;
		return true;
	}
	
	public boolean addHands(Equipable e){
		if(e instanceof Armor == false)return false;
		if(e.getSlot()!=Constants.ITEM_SLOT_HANDS)return false;
		hands = (Armor) e;
		return true;
	}
	
	public boolean addChest(Equipable e){
		if(e instanceof Armor == false)return false;
		if(e.getSlot()!=Constants.ITEM_SLOT_CHEST)return false;
		chest = (Armor) e;
		return true;
	}
	
	public boolean addLegs(Equipable e){
		if(e instanceof Armor == false)return false;
		if(e.getSlot()!=Constants.ITEM_SLOT_LEGS)return false;
		legs = (Armor) e;
		return true;
	}
	
	public boolean addFeet(Equipable e){
		if(e instanceof Armor == false)return false;
		if(e.getSlot()!=Constants.ITEM_SLOT_FEET)return false;
		feet = (Armor) e;
		return true;
	}
	
	public boolean addWeapon(Equipable e){
		if(e instanceof Weapon == false)return false;
		if(e.getSlot()!=Constants.ITEM_SLOT_WEAPON)return false;
		weapon = (Weapon) e;
		return true;
	}
	
	/**
	 * 
	 * @param a Armor to be equipped (needed to find slot)
	 * @return removed armor
	 */
	public Armor removeArmor(Armor a){
		Armor removed = null;
		if(a.getSlot()==Constants.ITEM_SLOT_HEAD){
			removed = head;
			head = null;
		}
		else if(a.getSlot()==Constants.ITEM_SLOT_SHOULDERS){
			removed = shoulders;
			shoulders = null;
		}
		else if(a.getSlot()==Constants.ITEM_SLOT_HANDS){
			removed = hands;
			hands = null;
		}
		else if(a.getSlot()==Constants.ITEM_SLOT_CHEST){
			removed = chest;
			chest = null;
		}
		else if(a.getSlot()==Constants.ITEM_SLOT_LEGS){
			removed = legs;
			legs = null;
		}
		else if(a.getSlot()==Constants.ITEM_SLOT_FEET){
			removed = feet;
			feet = null;
		}
		return removed;
	}
	
	/**
	 * Check if the item of same slot is equipped and return the item
	 */
	public Item findEquipped(Equipable item){
		if(item.getSlot()==Constants.ITEM_SLOT_HEAD){
			if(head!=null)return head;
		}
		else if(item.getSlot()==Constants.ITEM_SLOT_SHOULDERS){
			if(shoulders!=null)return shoulders;
		}
		else if(item.getSlot()==Constants.ITEM_SLOT_HANDS){
			if(hands!=null)return hands;
		}
		else if(item.getSlot()==Constants.ITEM_SLOT_CHEST){
			if(chest!=null)return chest;
		}
		else if(item.getSlot()==Constants.ITEM_SLOT_LEGS){
			if(legs!=null)return legs;
		}
		else if(item.getSlot()==Constants.ITEM_SLOT_FEET){
			if(feet!=null)return feet;
		}else if(item.getSlot()==Constants.ITEM_SLOT_WEAPON){
			if(weapon!=null)return weapon;
		}
		return null;
	}
	
	public Weapon removeWeapon() {
		Weapon w = weapon;
		weapon = null;
		return w;
	}
}
