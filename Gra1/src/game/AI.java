package game;

import graphics.sprite.Enemy;

import java.awt.Point;

public abstract class AI {
	
	long curTravelTime = 0;
	Point dest = new Point();
	long totTravelTime = 0;
	int stay = 0;
	boolean spawn = false;
	
	public abstract void runAI(long elapsedTime, Enemy enemy, float[] dists,Region r);
	
	protected abstract void attack(long elapsedTime, Enemy enemy,float[] dists,Region r);
	
	protected abstract void idle(long elapsedTime, Enemy enemy,Region r);
	
	protected abstract void goToSpawn(Enemy enemy, Region r);
		
}

