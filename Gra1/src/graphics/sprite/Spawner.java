package graphics.sprite;

import graphics.DrawGame;

public class Spawner extends Building{
	
	private int type;
	private long spawnTime;
	private long lastSpawn;
	private int level=1;
	
	//serial number of the spawner
	private int spawnID;
	
	//max number of mobs from this spawner at the same time
	private int spawnLim;
	
	//current number of mobs from this spawner
	private int currMobs = 0;
	
	/**
	 * The ID for each spawner must be different
	 * Each type of spawner spawns different mobs
	 * 1 - SLIME; 2 - BAT; 3 - SPIDER
	 */
	public Spawner(int x, int y, int type, int id){
		this.type = type;
		spawnID = id;
		setY(y);
		setX(x);
		if(type==1){			
			setWidth(100);
			setHeight(86);
			setAdvHeight(true);
		}else if(type==2){			
			setWidth(36);
			setHeight(100);
		}else if(type==3){			
			setWidth(70);
			setHeight(32);
		}else if(type==4){			
			setWidth(70);
			setHeight(32);
		}
	}
	
	public int getType() {
		return type;
	}
	
	public long getSpawnTime() {
		return spawnTime;
	}

	public void setSpawnTime(long spawnTime) {
		this.spawnTime = spawnTime;
		lastSpawn = 0;
	}

	public long getLastSpawn() {
		return lastSpawn;
	}

	public void tickLastSpawn(long elapsedTime) {
		lastSpawn = lastSpawn+=elapsedTime;
	}
	public void resetLastSpawn() {
		lastSpawn = 0;
	}

	public int getSpawnID() {
		return spawnID;
	}

	public int getSpawnLim() {
		return spawnLim;
	}

	public void setSpawnLim(int spawnLim) {
		this.spawnLim = spawnLim;
	}

	public int getCurrMobs() {
		return currMobs;
	}

	public void setCurrMobs(int currMobs) {
		this.currMobs = currMobs;
	}
	public void setLevel(int l){
		if(l >0){
			level = l;
		}else{
			level =1;
		}
	}
	public int getLevel(){
		return level;
	}
	
	public void draw(DrawGame d, int x, int y){
		d.g.drawImage(d.h.spawnImgs[type],x, y, null);	
	}
}
