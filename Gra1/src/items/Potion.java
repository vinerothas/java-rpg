package items;

import game.Constants;

public class Potion extends Item{

	public Potion(int v) {
		super(v);
		value = 10;
		name = "Potion";
	}
	
	public String getSaveString(){
		return Constants.SAVE_ID_ITEM_POTION+" "+getValue();
	}

}
