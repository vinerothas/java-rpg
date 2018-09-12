package items;

import graphics.ImageHolder;

import java.awt.Image;


public abstract class Equipable extends Item{

	private int slot;
	private int type;
	private int level;
	private int quality;
	
	public Equipable(int v,int s, int t, int q , int l) {
		super(v);
		slot = s;
		type = t;
		level = l;
		quality = q;
	}

	/**
	 * 1 - boots
	 * 2 - chest
	 * 3 - helmet
	 * 4 - leggings
	 * 5 - shoulders
	 * 6 - gloves
	 * 7 - weapon
	 */
	public int getSlot(){
		return slot;
	}	
	public int getType() {
		return type;
	}
	public int getQuality() {
		return quality;
	}
	public int getLevel() {
		return level;
	}
	
	public abstract String getSaveString();
	
	public Image getImage(ImageHolder h){
		try{
			return h.armorImgs[type-1][slot-1];
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
}
