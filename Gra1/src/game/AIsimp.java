package game;

import graphics.sprite.Building;
import graphics.sprite.Enemy;
import graphics.sprite.Spawner;

import java.util.Iterator;
import java.util.Random;

public class AIsimp extends AI{
	
	public void runAI(long elapsedTime, Enemy enemy, float[] dists, Region r){		
		final int detectionDist = 150;
		final int attackRange = 15;
		spawn = false;
		
		if(dists[0] < attackRange){
			return;
		}		
		if(dists[0] < detectionDist){
			attack(elapsedTime,enemy,dists, r);
		}else{
			idle(elapsedTime, enemy,r);
		}
	}
	
	protected void attack(long elapsedTime, Enemy enemy,float[] dists,Region r){
		float vx = enemy.getV()*(dists[1]/dists[0]);
		float vy = enemy.getV()*(dists[2]/dists[0]);
		if(enemy.getY()>r.getPlayer().getY()){
			vy = -vy;
		}
		if(enemy.getX()>r.getPlayer().getX()){
			vx = -vx;
		}
		float x = ((vx*elapsedTime)+enemy.getX());
		float y = ((vy*elapsedTime)+enemy.getY());
		
		boolean deep = enemy.checkSwimming(r.tiles,(int)x,(int)y);
		if(deep)enemy.setXY(x, y);
		else goToSpawn(enemy, r);
	}
	
	protected void idle(long elapsedTime, Enemy enemy,Region r){	
				
		Random rand = new Random();		
		curTravelTime+=elapsedTime;
		if(curTravelTime > totTravelTime){	//roll staying	
			curTravelTime = 0;
			// 1 in 8 chance to stop for 1.5 seconds
			stay = rand.nextInt(8);
			if(stay == 1){
				totTravelTime = 1500;
			}else{
				
			}
		}
		if(stay == 1){//stay
			return;
		}
		if(curTravelTime == 0){	//pick a new direction
			final int awayDist = 150;
			dest.x = (int) (rand.nextInt(awayDist)-(awayDist*0.5));
			dest.y = (int) (rand.nextInt(awayDist)-(awayDist*0.5));
			float v = (float) (enemy.getV()*0.4);
			totTravelTime = (long) Math.sqrt((dest.x*dest.x+dest.y*dest.y)/(v*v));
		}
		//idle speed is lower than normal speed	
		float vx = (float)dest.x/totTravelTime;
		float vy = (float)dest.y/totTravelTime;
		float x = ((vx*elapsedTime)+enemy.getX());
		float y= ((vy*elapsedTime)+enemy.getY());
		
		boolean walkable = enemy.checkSwimming(r.tiles,(int)x,(int)y);
		if(walkable)enemy.setXY(x, y);
		else if(spawn==false)goToSpawn(enemy, r);
	}
	
	protected void goToSpawn(Enemy enemy, Region r){
		spawn = true;
		Spawner s = null;
		Iterator<Building> it = r.getBuildings().iterator();
		while(it.hasNext()){
			Building b = it.next();
			if(b instanceof Spawner == false)continue;
			int id = ((Spawner) b).getSpawnID();
			if(id==enemy.getSpawnID())s = (Spawner) b;
		}
		int sx = (int) (s.getX()-enemy.getX());
		int sy = (int) (s.getY()-enemy.getY());
		
		curTravelTime = 0;
		Random rand = new Random();	
		
		final int awayDist = 150;
		//multiply by 0.4 so the enemy doesn't go all the way to the spawn
		dest.x = (int) (0.4*(sx+(rand.nextInt(awayDist)-(awayDist*0.5))));
		dest.y = (int) (0.4*(sy+(rand.nextInt(awayDist)-(awayDist*0.5))));
		float v = (float) (enemy.getV()*0.4);
		totTravelTime = (long) Math.sqrt((dest.x*dest.x+dest.y*dest.y)/(v*v));
		
		idle(1,enemy,r);
	}
	
}

