package graphics.sprite;

import java.awt.Color;

import game.AI;
import game.AIsimp;
import game.Functions;
import graphics.DrawGame;

public abstract class Enemy extends Character{
		
	public AI ai = new AIsimp();
	private long lastHit = 2000;
	private int maxHp = 40;
	private int curHp = maxHp;
	private int spawnID = 0;
	protected int type;
	private int level=1;
	private int power = 15;
	
	public void setXY(float x, float y){
		setX(x);
		setY(y);
	}

	public long getLastHit() {
		return lastHit;
	}

	public void setLastHit(long lastHit) {
		this.lastHit = lastHit;
	}

	public int getCurHp() {
		return curHp;
	}
	public void setCurHp(int hp) {
		curHp = hp;
	}
	
	public int getMaxHp() {
		return maxHp;
	}

	public int getSpawnID() {
		return spawnID;
	}

	public void setSpawnID(int id) {
		spawnID = id;
	}

	public int getType() {
		return type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int l){
		if(l >0){
			level = l;
			Functions f = new Functions();
			power = (f.getEnemyPower(level));
			maxHp = f.getEnemyHealth(level);
			curHp = maxHp;
		}else{
			setLevel(1);
		}
	}
	
	public int getPower(){
		return power;
	}
	
	abstract public void draw(DrawGame d, int x, int y);
	
	protected void drawBar(DrawGame d, int x, int y){
		double a = (double)getCurHp()/(double)getMaxHp();
		int w = (int) (a*(getWidth()-3));
		d.g.setColor(Color.RED);
		d.g.fillRect(x, y+getHeight()+2,w, 2);
		d.g.setColor(Color.YELLOW);
		d.g.drawString(String.format("%s",getLevel()), x+getWidth()-2, y+getHeight()+5);
	}
}
