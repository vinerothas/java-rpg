package items;

public class Loot {
	
	private Item item;
	private int pos;
	private int quantity;
	private int value = 0;
	
	public Loot(Item item){
		this.item = item;
		value = item.getValue();
		quantity = 1;
	}
	
	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int q, int v) {
		quantity = q;
		value=v;
	}
	
	public Item getItem(){
		return item;
	}

	public int getValue() {
		return value;
	}
	
}
