package game;

import graphics.sprite.*;

import items.MobScrap;

import java.util.Iterator;
import java.util.Random;

import screen.GameScreen;
import window.EnterShopWindow;
import window.EnterTavernWindow;
import window.TravelWindow;

public class UpdatePlayer {

	private GameScreen gs;
	private Region r;
	private Player player;
	
	public void update(long elapsedTime, GameScreen gs){	
		this.gs = gs;
		this.r = gs.r;
		player = r.getPlayer();
		
		if(gs.hit == true){
			gs.d.hit = true;
			gs.hit=false;
			checkHit();
		}
		
		int x = player.getStanceX();
		int y = player.getStanceY();

		if((x == 0 && y == 0)){
			return;
		}
		
		float moved = player.getV()*elapsedTime;
		int movedX = 0;
		int movedY = 0;
		if(y==0 || x==0){
			if(x ==1)movedX = (Math.round(player.getX()+(moved)));
			else if(x==2)movedX = (Math.round(player.getX()-(moved)));
		
			if(y==1)movedY =(Math.round(player.getY()-(moved)));
			else if(y==2)movedY =(Math.round(player.getY()+(moved)));
		}else{
			moved = (float) (moved/Math.sqrt(2));
			
			if(x==1)movedX = (Math.round(player.getX()+(moved)));
			else if(x==2)movedX = (Math.round(player.getX()-(moved)));
		
			if(y==1)movedY =(Math.round(player.getY()-(moved)));
			else if(y==2)movedY =(Math.round(player.getY()+(moved)));
		}
		
		int dir = checkCol(movedX,movedY);
		if(dir!=0){
			stop(player);
			player.setStanceY(0);
			return;
		}
		
		int quarter = Constants.REGION_PLAYABLE_QUARTER;
		int detection = quarter-100;
		if(Math.abs(player.getX())>detection || Math.abs(player.getY())>detection){
			if(movedX < -(quarter-7) && x==2){
				gs.window = new TravelWindow(gs,4);
				stop(player);
				return;
			}else if(movedX > quarter-10 && x==1){
				gs.window = new TravelWindow(gs,2);
				stop(player);
				return;
			}else if(movedY < -(quarter-24) && y==1){
				gs.window = new TravelWindow(gs,1);
				stop(player);
				return;
			}else if(movedY > (quarter-15) && y==2){
				gs.window = new TravelWindow(gs,3);
				stop(player);
				return;
			}
		}
		

		moved = player.getV()*elapsedTime;
		float movex = player.getX();
		float movey = player.getY();
		if(y!=0 && x!=0)moved = (float) (moved/Math.sqrt(2));		

		if(x ==1)movex=player.getX()+(moved);
		else if(x==2)movex=player.getX()-(moved);
	
		if(y==1)movey=player.getY()-(moved);
		else if(y==2)movey=player.getY()+(moved);
		
		if(player.checkSwimming(r.tiles,(int)movex,(int)movey)){
			player.setX(movex);
			player.setY(movey);
		}
		
	}
	
	private void stop(Player player){
		player.setStanceX(0);
		player.setStanceY(0);
	}
	
	private int checkCol(int px,int py){	
		
		if(px==0){
			px = (int) player.getX();
		}
		if(py==0){
			py = (int) player.getY();
		}
		px +=5;
		py +=player.getHeight();
	
		Iterator<Building> b = r.getBuildings().iterator();
		while(b.hasNext()){
			Building building = b.next();
			if(building instanceof House){
				int x = (int)(building.getX()+(building.getWidth()/2)-px);
				int y = (int)(building.getY()+(building.getHeight()/2)-py);
				if(Math.abs(x)<200 && Math.abs(y)<200){				
					int px2 = px + player.getWidth() -10;
					int bx = (int)building.getX()+13;
					int by = (int)building.getY()+91;
					int bx2 = bx+(248-13);
					int by2 = by+(195-91);
					boolean xin = (px<bx2 && px2>bx);
					boolean yin = (py<by2 && py>by);
					checkEnterable(building,px,py);
					if(px<=bx2 && px2 >=bx2 && yin){
						//player from right
						return 1;
					}else if(px2 >= bx && px2 <= bx2 && yin){
						//player from left
						return 2;
					}else if(py >= by-1 && py <=by2 && xin){
						//player from up
						return 3;
					}else if(py <= by2+1 && py >= by && xin){
						//player from down						
						return 4;
					}
				}
			}
		}
		
		return 0;
	}
	
	//TODO
	
	private void checkEnterable(Building b, int px, int py){
		if(b instanceof House == false){
			return;
		}
		House h = (House) b;
		int t = h.getType();
		if(t == Constants.HOUSE_TYPE_NORMAL_LEFT || t == Constants.HOUSE_TYPE_NORMAL_RIGHT){
			return;
		}
		int px2 = px + player.getWidth() -10;
		int hx = (int)h.getX();
		int hy = (int)h.getY()+195;
		if(t==Constants.HOUSE_TYPE_TAVERN_LEFT || t==Constants.HOUSE_TYPE_SHOP_LEFT){
			if(px2>hx+88&&px<hx+109&&py<hy+10&&py>hy){
				if(t==Constants.HOUSE_TYPE_TAVERN_LEFT){
					gs.window = new EnterTavernWindow(gs);
				}else if(t==Constants.HOUSE_TYPE_SHOP_LEFT){
					gs.window = new EnterShopWindow(gs);
				}
				player.setY((int) (player.getY()+8));
				player.stop();
			}		
		}else if(t==Constants.HOUSE_TYPE_TAVERN_RIGHT||t==Constants.HOUSE_TYPE_SHOP_RIGHT){
			if(px2>hx+148&&px<hx+186&&py<hy+10&&py>hy){
				if(t==Constants.HOUSE_TYPE_TAVERN_RIGHT){
					gs.window = new EnterTavernWindow(gs);
				}else if(t==Constants.HOUSE_TYPE_SHOP_RIGHT){
					gs.window = new EnterShopWindow(gs);
				}
				player.setY((int) (player.getY()+8));
				player.stop();
			}	
		}
		
	}
	
	
	private void checkHit(){
		int ld = player.getLastDir();
		int x1=0;
		int y1=0;
		int x2=0;
		int y2=0;
		if(ld ==0){
			x1 = (int) (player.getX()+10);
			y1 = (int) (player.getY()-5);
			x2 = x1+35;
			y2 = y1+50;
		}else if(ld==1){
			x1 = (int) (player.getX()-10);
			y1 = (int) (player.getY()-5);
			x2 = x1+35;
			y2 = y1+50;
		}else if(ld==2){
			x1 = (int) (player.getX()-5);
			y1 = (int) (player.getY()-15);
			x2 = x1+45;
			y2 = y1+30;
		}else if(ld==3){
			x1 = (int) (player.getX()-5);
			y1 = (int) (player.getY()+10);
			x2 = x1+45;
			y2 = y1+30;
		}
		
		Iterator<Enemy> i = r.getEnemies().iterator();
		while(i.hasNext()){
			Enemy enemy = i.next();
			int xe = (int)enemy.getX();	
			int xe2 = (int)enemy.getX()+enemy.getWidth();
			if((xe > x1 || xe2 > x1) && (xe <x2 || xe2<x2)){
				int ye = (int)enemy.getY();
				int ye2 = (int)enemy.getY()+enemy.getHeight();
				if((ye > y1 || ye2 > y1) && (ye < y2 || ye2 < y2)){	
					if(hitEnemy(enemy)==true){
						i.remove();
						r.removeEnemy(enemy);
					}
				}
			}
		}
		
	}
	
	private boolean hitEnemy(Enemy enemy){
		enemy.setCurHp(enemy.getCurHp()-player.getPower());
		if(enemy.getCurHp()<=0){
			if(enemy.getSpawnID()!=0){
				Spawner spawner = gs.getSpawner(enemy.getSpawnID());
				if(spawner != null){
					spawner.setCurrMobs(spawner.getCurrMobs()-1);
				}
			}
			Functions f = new Functions();
			Random rand = new Random();
			int value = (int) (f.getEnemyValue(enemy.getLevel())+rand.nextInt(enemy.getLevel())-Math.ceil(enemy.getLevel()/2));
			Drop item = new Drop(new MobScrap(enemy.getType(),value));
			item.setX(enemy.getX());
			item.setY(enemy.getY());
			int exp = (int) (f.getEnemyExp(enemy.getLevel()));
			player.setExp(player.getExp()+exp);
			//System.out.println(exp);
			r.addDrop(item);
			return true;			
		}else{
			float px = r.getPlayer().getX();
			float py = r.getPlayer().getY();
			final float push = 40;
			float ex = enemy.getX();
			float ey = enemy.getY();
			float ox = ex-px;
			float oy = ey-py;
			float ep = (float) Math.sqrt((ox*ox)+(oy*oy));
			float pushx  =(push*ox)/ep;
			float pushy = (push*oy)/ep;
			enemy.setX(enemy.getX()+pushx);
			enemy.setY(enemy.getY()+pushy);
			return false;
		}
	}
	
}
