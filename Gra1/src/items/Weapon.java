package items;

import game.Constants;
import game.Functions;

public class Weapon extends Equipable{

	private int power;
	
	public Weapon(int v,int t, int l, int q){
		super(v, 7, t, q, l);
		Functions f = new Functions();
		double weapon = f.getPercentQuality(q);
		power = (int) (weapon*f.getPlayerAttack(l));
	}
	
	public int getPower() {
		return power;
	}
	
	public String getSaveString() {
		return Constants.SAVE_ID_ITEM_WEAPON+" "+getValue()+" "+getType()+" "+getLevel()+" "+getQuality()+" "+getSlot();
	}
	
}
