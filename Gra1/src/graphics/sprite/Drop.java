package graphics.sprite;

import graphics.DrawGame;
import items.Item;
import items.MobScrap;

import java.util.Random;

public class Drop extends Sprite{
	
	private Item item;
	private long dropTime=1;
	private float lastChange=0;
	private int sign = 0;
	private final int width = 10;
	private final int height = 10;
	private boolean freeze = false;
	
	public Drop(Item i){
		item = i;
		setWidth(width);
		setHeight(height);
	}
	
	/**
	 * Constructor for load game only
	 */
	public Drop(Item i, long dropTime){
		item = i;
		this.dropTime = dropTime;
		setWidth(width);
		setHeight(height);
	}
	
	public Item getItem(){
		return item;
	}
	
	public long getDropTime(){
		return dropTime;
	}
	
	public void setFreeze(){
		freeze = true;
		dropTime = 1;
	}
	
	public int drop(long elapsedTime){
		dropTime += elapsedTime;
		if(freeze){
			if(dropTime>1500)freeze=false;
			else return 4;
		}
		if(dropTime>490){
			if(dropTime>60000){
				return 3;
			}
			return 2;
		}
		if(sign == 0){
			Random r = new Random();
			int a = r.nextInt(2);
			if(a == 0){
				sign = -1;
			}else if(a ==1){
				sign = 1;
			}
		}
		float x = (float)(dropTime)/75;
		float y = (float) ((0.5*Math.pow(x, 3))-(1.6*Math.pow(x, 2))-(7.3*x));
		this.setY(this.getY()+(y-lastChange));
		double eT = (double) elapsedTime;
		this.setX((float) (this.getX()-((sign*eT)/12)));
		lastChange = y;
		return 1;
	}
	
	public void draw(DrawGame d, int x, int y){
		if(item instanceof MobScrap){
			MobScrap ms = (MobScrap)item;
			d.g.drawImage(d.h.itemImgs[ms.getType()],x, y, null);
		}
	}
	
}
