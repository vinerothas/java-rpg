package screen;

import input.KeyAction;
import input.KeyboardConfig;
import items.InventoryManager;
import items.Item;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import window.DeathWindow;
import window.InfoWindow;
import window.Window;
import window.YesNoWindow;

import game.Constants;
import game.Functions;
import game.Region;
import game.SaveLoad;
import game.ScreenManager;
import game.UpdatePlayer;
import graphics.DrawGame;
import graphics.sprite.Building;
import graphics.sprite.Drop;
import graphics.sprite.Enemy;
import graphics.sprite.Enemy3Dir;
import graphics.sprite.EnemyHor;
import graphics.sprite.EnemySimp;
import graphics.sprite.Player;
import graphics.sprite.Spawner;

public class GameScreen extends Screen{
	
	public DrawGame d;
	public Region r;
	public Window window = null;
	private long elapsedTime = 0;
	public boolean hit = false;

	public GameScreen(ScreenManager sm) {
		super(sm,sm.zoom);
		r = sm.r;
		d = new DrawGame(sm.h,sm.w,r,sm.dui,new Point(getDrawWidth(),getDrawHeight()));

	}

	@Override
	public void loop(long elapsedTime) {
		this.elapsedTime = elapsedTime;
		
		if(window!=null){
			updateWindow();
		}else{
			checkInput();			
			updateEnemies(elapsedTime);		
			updateBuildings(elapsedTime);	
			updateItems(elapsedTime);
			//TODO todos in UpdatePlayer
			new UpdatePlayer().update(elapsedTime, this);
		}
	}
	
	private void updateWindow(){
		Point m =sm.dui.mouseClicked;KeyAction[] keyActions = sm.dui.getKeyActions();
		KeyboardConfig kc = sm.kc;
		for(KeyAction a : keyActions){
			if(a==null)break;
			int k = a.getKeyCode();
			if(a.isPressed()){
				if(k==kc.YES){
					Window w = window;
					if(w!=null)w.confirmAction();
				}else if(k==kc.NO){
					Window w = window;
					if(w!=null)w.cancelAction();
				}
			}
		}
		if(m!=null&&window!=null){
			Window w = window;
			w.checkInput(m);
		}
	}
	
	@Override
	protected void checkInput() {
		//TODO finish all functions
		KeyAction[] keyActions = sm.dui.getKeyActions();
		KeyboardConfig kc = sm.kc;
		Player player = r.getPlayer();
		for(KeyAction a : keyActions){
			if(a==null)break;
			int k = a.getKeyCode();
			if(a.isPressed()){
				if(k==kc.RIGHT||k==kc.RIGHT_ALT){
					player.setStanceX(1);
				}else if(k==kc.LEFT||k==kc.LEFT_ALT){
					player.setStanceX(2);
				}else if(k==kc.UP||k==kc.UP_ALT){
					player.setStanceY(1);
				}else if(k==kc.DOWN||k==kc.DOWN_ALT){
					player.setStanceY(2);
				}else if(k==kc.MENU){
					sm.setScreen(new PauseScreen(sm,this));
				}else if(k==16){//XXX shift (remove)
					if(player.getV()==1.5)player.setV((float)0.18);
					else player.setV((float)1.5);
				}else if(k==kc.ATTACK){
					hit = true;
				}else if(k == kc.CHARACTER){
					sm.setScreen(new CharacterScreen(sm,this));
				}
			}else{
				if(k==kc.RIGHT||k==kc.RIGHT_ALT){
					if(player.getStanceX()==1)player.setStanceX(0);		
				}else if(k==kc.LEFT||k==kc.LEFT_ALT){
					if(player.getStanceX()==2)player.setStanceX(0);		
				}else if(k==kc.UP||k==kc.UP_ALT){
					if(player.getStanceY()==1)player.setStanceY(0);		
				}else if(k==kc.DOWN||k==kc.DOWN_ALT){
					if(player.getStanceY()==2)player.setStanceY(0);
				}
			}
		}
		sm.dui.clearInput();
	}

	@Override
	public void draw(Graphics2D g) {
		d.draw(elapsedTime, g);
		
		if(window!=null)window.draw(d);
	}
	
	public void travel(int dir){
		SaveLoad sl = new SaveLoad();
		try {
			int x = r.x;
			int y = r.y;
			//System.out.println(dir);
			if(dir==1)	y-=1;
			else if(dir==2)	x+=1;
			else if(dir==3)	y+=1;
			else if(dir==4)	x-=1;
			sl.makeFile(r);
			r.cleanRegion();
			sl.loadMap(r,d.h,x,y);
			Player player = r.getPlayer();
			int quarter = Constants.REGION_PLAYABLE_QUARTER;
			if(dir==1){
				player.setY(quarter-50);
				player.setX(0);
			}else if(dir==2){
				player.setX(-(quarter-20));
				player.setY(0);
			}else if(dir==3){
				player.setY(-(quarter-50));
				player.setX(0);
			}else if(dir==4){
				player.setX(quarter-20);
				player.setY(0);
			}
			boolean goBack = !player.checkSwimming(r.tiles, (int)player.getX(), (int)player.getY());
			if(goBack){
				int newDir = 0;
				if(dir==1)newDir=3;
				else if(dir==2)newDir=4;
				else if(dir==3)newDir=1;
				else if(dir==4)newDir=2;
				travel(newDir);
				String[] text = new String[]{null,"You didn't find","anything interesting."};
				window = new InfoWindow(this, text);
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
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
						boolean added = InventoryManager.addItem(r.getPlayer().getInventory(),item);
						if(added){
							i.remove();
							r.removeDrop(drop);
						}else{
							drop.setFreeze();
						}
					}
				}				
			}else if(state==4)continue;
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
		if(type==1||type==2)return new EnemySimp(d.h,type);
		else if(type==3)return new EnemyHor(d.h,type);
		else if(type==4)return new Enemy3Dir(d.h,type);
		System.out.println("Error line 217 GameScreen getEnemy(type) - type not supported; Type: "+type);		
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
						if(r.getPlayer().getLife()<1)die();
						enemy.setLastHit(0);
					}					
				}
			}
		}
	}
	
	private void die(){
		window = new DeathWindow(this);
	}
	
	public void respawn(){
		SaveLoad sl = new SaveLoad();
		sl.makeFile(r);
		r.cleanRegion();
		try {
			sl.loadMap(r,d.h,0,0);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Player player = r.getPlayer();
		player.setLife(player.getMhp());
		player.setX(0);
		player.setY(0);
		window = null;
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

	/*
	 Point m = sm.dui.mouseClicked;
		if(m!=null){
			int px = (int)r.getPlayer().getX()-(sm.windowWidth/2)+(r.getPlayer().getWidth()/2);
			int py = (int)r.getPlayer().getY()-(sm.windowHeight/2)+(r.getPlayer().getHeight()/2);
			int sx = m.x+px;
			int sy = m.y+py;
			Point p = Constants.coordToTile(sx,sy);
			if(r.tiles[p.x][p.y]==1)r.tiles[p.x][p.y]=0;
			else r.tiles[p.x][p.y]=1;
		}
	 */

}
