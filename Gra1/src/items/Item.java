package items;

public abstract class Item {
	
	protected String name = "null";
	protected int value;
		
	public Item(int v){	
		value = v;		
	}
	
	public int getValue(){
		return value;
	}
	
	protected void setValue(int v){
		value = v;
	}
	
	public abstract String getSaveString();
	
}
