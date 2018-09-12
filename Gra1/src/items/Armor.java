package items;

import game.Constants;
import game.Functions;

public class Armor extends Equipable{

	private int armor;
	
	/**
	 * Value, Slot, Type, Quality, Level
	 */
	public Armor(int value, int s, int t, int q , int l) {
		super(value,s,t,q,l);
		Functions f = new Functions();
		armor = f.getArmor(l, q, s);
	}
	
	public int getArmor() {
		return armor;
	}

	public String getSaveString() {
		return Constants.SAVE_ID_ITEM_ARMOR+" "+getValue()+" "+getSlot()+" "+getType()+" "+getQuality()+" "+getLevel();
	}
}
