package game;

import items.*;

import java.awt.Point;
import java.util.Iterator;
import java.util.Random;

import graphics.DataUI;
import graphics.DrawOld;
import graphics.UpdatePlayerAnim;
import graphics.sprite.*;

public class GameManager {

	protected DrawOld d;
	public boolean hit;
	public DataUI dui;
	protected boolean running = true;
	public Region r;
	public int windowWidth = 620;
	public int windowHeight = 500;
	public Point screenMid = new Point(windowWidth/2,windowHeight/2);
	//public Player player;
	private MainMenu menu;

	public void run(DrawOld d, DataUI dui, Region r, MainMenu menu, boolean load){
		this.d = d;
		this.dui = dui;
		this.r = r;
		this.menu = menu;
		try{
			new UpdatePlayerAnim().loadPlayerAnim(d);
			if(load==false)init();
			gameLoop();
		}finally{
			//wyczyscic region?
			r.cleanRegion();
			menu.backToMenu(d, dui, r);
		}
	}
	
	private void init(){
		InitRegion ir = new InitRegion();
		ir.init(r,0,0);
		
		/*
		new InventoryManager().addItem(new Armor(10,1,1,2,1), r.inventory);
		new InventoryManager().addItem(new Armor(10,2,1,3,1), r.inventory);
		new InventoryManager().addItem(new Armor(10,3,1,4,1), r.inventory);
		new InventoryManager().addItem(new Armor(10,4,1,5,1), r.inventory);
		new InventoryManager().addItem(new Armor(10,5,1,6,1), r.inventory);
		new InventoryManager().addItem(new Armor(10,6,1,7,1), r.inventory);
		
		new InventoryManager().addItem(new Weapon(10,1,1,1), r.inventory);
		new InventoryManager().addItem(new Weapon(10,1,1,3), r.inventory);
		new InventoryManager().addItem(new Weapon(10,1,1,4), r.inventory);
		new InventoryManager().addItem(new Weapon(10,1,1,5), r.inventory);
		new InventoryManager().addItem(new Weapon(10,1,1,6), r.inventory);
		new InventoryManager().addItem(new Weapon(10,1,1,7), r.inventory);
		*/
		new InventoryManager().addItem(new Weapon(10,1,1,2), r.inventory);
		
	}
	
	public void saveGame(){
		SaveLoad s = new SaveLoad();
		s.makeFile(r);
	}
	
	private void gameLoop(){
		long lastTime = System.currentTimeMillis();
		long elapsedTime;
		int tick = 0;
		long skips = 0;
		long second = 0;
		//long[] frames = new long[50000];
		//int index = 0;
		
		while(running){
			elapsedTime = System.currentTimeMillis() - lastTime;
			lastTime += elapsedTime;
			second+=elapsedTime;
			if(elapsedTime>100)elapsedTime = 15;
			if(elapsedTime==0){
				skips++;
				continue;
			}
			if(second >= 1000){
				System.out.println("FPS: "+tick+" Skips: "+skips);
				/*
				frames[index]=tick;
				index++;
				float average = 0;
				for(int i = 0;i<index;i++){
					average+=frames[i];
				}
				average = average/index;
				System.out.println("Average: "+average);
				*/
				second = 0;
				tick = 0;
				skips = 0;
			}
			
			//d.dUI.setInBuilding(2);
			//d.dUI.setPState(4);
			
			
			if(dui.getPState()!=0){
				if(dui.getPState()<5)new PlayerEvents(this).pickAction();
				else if(dui.getPState()==11){
					d.drawMenu(menu);
					tick++;
					continue;
				}else if(dui.getPState()==12){
					saveGame();
					dui.setPState(5);
					break;
				}
			}
			if(dui.activeGame==true){
				update(elapsedTime);
			}
			
			d.draw(elapsedTime);			
			
			tick++;
		}
	}
	
	private void update(long elapsedTime){
		
		UpdatePlayer UP = new UpdatePlayer();
		UP.update(elapsedTime,this);		
		updateEnemies(elapsedTime);		
		updateBuildings(elapsedTime);		
		updateItems(elapsedTime);
		
		int pos = dui.getItemHeld();
		if(pos!=0){
			new Equipping().draggingAction(this,pos);
		}else{
			pos = dui.getItemRightClicked();
			if(pos!=0){
				new Equipping().rightClick(this, pos);
			}
			dui.setItemRightClicked(0);
		}
	}
	
	private void updateItems(long elapsedTime){
		Iterator<Drop> i = r.getDrops().iterator();
		while(i.hasNext()){
			Drop drop = i.next();
						
			int state = drop.drop(elapsedTime);
			if(state==3){
				i.remove();
				r.removeDrop(drop);
			}else if(state ==2){				
				float xdist = drop.getX() - (r.getPlayer().getX()+8);
				float ydist = drop.getY() - (r.getPlayer().getY()+20);
				float dist = (float) Math.sqrt(xdist*xdist+ydist*ydist);
				if(dist<50){
					if(Math.abs(xdist) > 5){
						drop.setX(drop.getX()-(20/xdist));
					}
					if(Math.abs(ydist) > 5){
						drop.setY(drop.getY()-(20/ydist));
					}else if(Math.abs(xdist) < 5){
						Item item = drop.getItem();
						new InventoryManager().addItem(item,r.inventory);					
						i.remove();
						r.removeDrop(drop);
					}
				}				
			}
		}
	}
	
	private void updateBuildings(long elapsedTime){
		Iterator<Building> i = r.getBuildings().iterator();
		while(i.hasNext()){
			Building building = i.next();
			if(building instanceof Spawner == false)continue;
			
			Spawner spawner = (Spawner) building;
			spawner.tickLastSpawn(elapsedTime);
			if(spawner.getLastSpawn()< spawner.getSpawnTime())continue;
			int px = (int) r.getPlayer().getX();
			int py = (int) r.getPlayer().getY();
			int bx = (int)building.getX();
			int by = (int)building.getY();
			int x = Math.abs(px-bx);
			int y = Math.abs(py-by);
			int d = (int) Math.sqrt(x*x+y*y);
			if(d<500){
				spawner.resetLastSpawn();
				continue;
			}
			
			if(spawner.getCurrMobs() != spawner.getSpawnLim()){
				spawner.setCurrMobs(spawner.getCurrMobs()+1);
				Enemy enemy = getEnemy(spawner.getType());
				enemy.setX(building.getX()+(building.getWidth()/2));
				enemy.setY(building.getY()+building.getHeight()+3);
				enemy.setSpawnID(spawner.getSpawnID());
				enemy.setLevel(spawner.getLevel()+new Random().nextInt(3)-1);
				r.addEnemy(enemy);
			}
			spawner.resetLastSpawn();
			
		}
	}
	
	private Enemy getEnemy(int type){
		if(type==1||type==2)return new EnemySimp(d,type);
		else if(type==3)return new EnemyHor(d,type);
		else if(type==4)return new Enemy3Dir(d,type);
		System.out.println("Error line 203 GameManager getEnemy(type) - type not supported; Type: "+type);		
		return null;	
	}
	
	private void updateEnemies(long elapsedTime){
		Iterator<Enemy> i = r.getEnemies().iterator();
		while(i.hasNext()){
			Enemy enemy = (Enemy) i.next();
			float xdist = Math.abs(enemy.getX() - (r.getPlayer().getX()+3));
			float ydist = Math.abs(enemy.getY() - (r.getPlayer().getY()+5));
			float dist = (float) Math.sqrt(xdist*xdist+ydist*ydist);
			float[] dists = new float[]{dist,xdist,ydist};
			if(dist<600){
				enemy.setLastHit(enemy.getLastHit()+elapsedTime);
				enemy.ai.runAI(elapsedTime, enemy,dists,r);
				if(dist<15){
					final long hitDelay = 2000;
					if(enemy.getLastHit()>hitDelay){
						Functions f = new Functions();
						double defended = ((double)r.getPlayer().getArmor()/((double)f.getPlayerArmor(enemy.getLevel())*2));
						int attacked = (int) Math.round(enemy.getPower()-(defended*enemy.getPower()));
						if(attacked<1)attacked=1;
						r.getPlayer().setLife(r.getPlayer().getLife()-attacked);
						enemy.setLastHit(0);
					}					
				}
			}
		}
	}
	
	public Spawner getSpawner(int id){
		Iterator<Building> i = r.getBuildings().iterator();
		while(i.hasNext()){
			Building building = i.next();
			if(building instanceof Spawner){
				if(((Spawner) building).getSpawnID() == id){
					return (Spawner) building;
				}
			}
		}
		return null;
	}

	
}
