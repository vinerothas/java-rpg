package items;

import game.Constants;

public class MobScrap extends Item{

	private int type;
	
	public MobScrap(int type, int v) {
		super(v);
		this.type = type;
		if(type == 1){
			name = "Yellow slime";
		}else if(type == 2){
			name = "Bat wings";
		}else if(type == 3){
			name = "Spider silk";
		}else if(type == 4){
			name = "Snake head";
		}
	}

	public int getType() {
		return type;
	}
	
	public void increaseValue(MobScrap ms){
		setValue(this.getValue()+ms.getValue());
	}
	
	public String getSaveString(){
		return Constants.SAVE_ID_ITEM_MOBSCRAP+" "+getType()+" "+getValue();
	}
	
	public String getName(){
		return name;
	}
	
}
