package game;

import generate.GenRiver;
import generate.GenShore;
import generate.GenVillage;
import graphics.sprite.*;
import items.*;

import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class InitRegion {
	
	private Region r;
	//private final int vrs = 8500; //visibleSizeRegion
	//private final int qs = 4000; //quadrant size
	//private final int mvd = 4250; //distance from region middle to visibility edge
	
	public void init(Region r, int rx, int ry){		
		
		Random rand = new Random();
		
		final int vrs = Constants.REGION_TOTAL_SIZE; //visibleSizeRegion
		final int mvd = vrs/2; //distance from region middle to visibility edge
		
		this.r = r;
		r.x = rx;
		r.y = ry;
			
		if(r.getPlayer() == null){
		Player player = new Player();
		player.setX(0);
		player.setY(0);
		r.addPlayer(player);
		}
		
		int a = rand.nextInt(20);
		int l = 0;
		if(a==0 || a==1){
			l = r.getPlayer().getLevel()-1;
		}else if(a>=2&&a<=8){
			l = r.getPlayer().getLevel();
		}else if(a>=9&&a<=16){
			l = r.getPlayer().getLevel()+1;
		}else if(a>=17&&a<=19){
			l = r.getPlayer().getLevel()+2;
		}
		
		r.setLevel(l);
		
		for(int i = 0;i < 7000;i++){//5000
			int x = rand.nextInt(vrs)-mvd;
			int y = rand.nextInt(vrs)-mvd;			
			Tree tree = new Tree(x,y);
			r.addDecor(tree);
		}
		for(int i = 0;i < 1000;i++){//500
			int x = rand.nextInt(vrs)-mvd;
			int y = rand.nextInt(vrs)-mvd;			
			Flower flower = new Flower(1,x,y);
			r.addDecor(flower);
		}
		for(int i = 0;i < 1000;i++){//500
			int x = rand.nextInt(vrs)-mvd;
			int y = rand.nextInt(vrs)-mvd;			
			Flower flower = new Flower(2,x,y);
			r.addDecor(flower);
		}
		
		int spawnerRange = (vrs-Constants.REGION_BORDER_SIZE)-1000;
		int spawnerRadius = spawnerRange/2;
		for(int i = 1; i<71;i++){
			int x = rand.nextInt(spawnerRange)-spawnerRadius;
			int y = rand.nextInt(spawnerRange)-spawnerRadius;
			int type = rand.nextInt(4)+1;
			Spawner spawner = new Spawner(x,y,type,i);
			spawner.setSpawnTime(5000);
			spawner.setSpawnLim(5);
			spawner.setLevel(r.getLevel()+rand.nextInt(3)-1);
			r.addBuilding(spawner);
		}
				
		int itemsMax = 13;
		int itemsMin = 7;
		int items = rand.nextInt(itemsMax-itemsMin)+itemsMin+1;
		for(int i = 0;i<items;i++){
			int slot = rand.nextInt(7)+1;
			//int quality = rand.nextInt(7)+1;
			int quality = Functions.getRandomEquipableQuality();
			int level = r.getLevel();
			int value = new Functions().getArmorValue(level, quality, slot);
			int type = 1;
			if(slot!=7)r.shop.add(new Armor(value,slot,type,quality,level));
			else r.shop.add(new Weapon(value,type,level,quality));
		}
		
		//r.shop.add(new Potion(10));
		
		new GenVillage(r);
		
		for(int i = 0;i<r.tilesVar.length;i++){
			for(int j = 0;j<r.tilesVar[0].length;j++){
				r.tilesVar[i][j]=rand.nextInt(Constants.TILE_VARIATIONS_GRASS);
			}
		}
		for(int i = 0;i<r.tiles.length;i++){
			for(int j = 0;j<r.tiles[0].length;j++){
				r.tiles[i][j]=0;
			}
		}
		
		//30% forest; 40% river 30% shore
		int env = rand.nextInt(10);
		//env =7;	//XXX env = 7 => shore	(remove)
		//env = 0;
		if(env<3){//0 1 2
			r.envType = 0;
			r.envSeed = 0;
		}else if(env>2&&env<7){//3 4 5 6
			r.envType = 1;
			genWater();
		}else{//7 8 9
			r.envType = 2;
			genWater();
		}
		
		checkBuilDecCol();
		checkDecorCol();
		checkBuilCol();
		boolean count = false;
		if(count == true){
			entityCount();
		}
		
	}
	
	private void genWater(){
		
		int[][] water = null;
		int seed = 0;
		while(true){
			
			boolean replay = false;

			seed = new Random().nextInt();
			if(r.envType==1)water = GenRiver.generateRiver(seed);
			else if(r.envType==2)water = GenShore.generateShore(seed);
			
			Iterator<Building> it = r.getBuildings().iterator();
			while(it.hasNext()){
				if(replay)break;
				Building b = it.next();
				if(b instanceof House==false)continue;
				int x = (int)b.getX();
				int y = (int)b.getY();
				int w = x+b.getWidth();
				int h = y+b.getHeight();
				x-=10;
				y-=10;
				if(waterCollision(water,x,y,w,h)){
					replay = true;
					break;
				}
			}
			if(replay ==false){
				r.tiles = water;
				r.envSeed = seed;
				break;
			}
		}
		
		int dr = 0;
		Iterator<Decor> id = r.getDecors().iterator();
		while(id.hasNext()){
			Decor d = id.next();
			int x = (int)d.getX();
			int y = (int)d.getY();
			int w = x+d.getWidth();
			int h = y+d.getHeight();
			if(d instanceof Tree){
				y+=60;
				x-=10;
			}
			if(waterCollision(water,x,y,w,h)){
				//System.out.println(x+" "+y+" "+w+" "+h);
				id.remove();				
				r.removeDecor(d);
				dr++;
			}
		}
		//System.out.println("Decors removed: "+dr);
		
		int br = 0;
		Iterator<Building> ib = r.getBuildings().iterator();
		while(ib.hasNext()){
			Building b = ib.next();
			if(b instanceof House)continue;
			int x = (int)b.getX();
			int y = (int)b.getY();
			int w = x+b.getWidth();
			int h = y+b.getHeight();
			x-=10;
			y-=10;
			if(waterCollision(water,x,y,w,h)){
				ib.remove();
				r.removeBuilding(b);
				br++;
			}
		}
		//System.out.println("Buildings removed: "+br);
	}
	
	private boolean waterCollision(int[][] water, int x, int y, int w, int h){
		try{
			//.out.println(x+" "+y+" "+w+" "+h);
			for(int yi=0;y+(yi*20)<h;yi++){
				for(int xi=0;x+(xi*20)<w;xi++){
					Point c = Constants.coordToTile(x+(xi*20),y+(yi*20));
					//System.out.println(x+" "+y+" "+c.x+" "+c.y);
					if(water[c.x][c.y]!=0){
						return true;
					}
				}
			}
		}catch(Exception ex){}
		return false;
	}
		
	private void checkBuilCol(){
		
		@SuppressWarnings("unchecked")
		LinkedList<Building> list = (LinkedList<Building>) r.getBuildings().clone();
		Iterator<Building> i = list.iterator();
		while(i.hasNext()){
			Building b1 = i.next();
			//skipping this code makes roads delete spawners
			//if(b1 instanceof Road){
			//	continue;
			//}
			Iterator<Building> i2 = r.getBuildings().iterator();
			while(i2.hasNext()){
				Building b2 = i2.next();
				if(b1 == b2 || b2 instanceof Road || b2 instanceof House){
					continue;
				}
				if(b1 instanceof Road && b2 instanceof House){
					continue;
				}
				int bx1 = (int) b1.getX();
				int by1 = (int) b1.getY();
				int bx2 = bx1 + b1.getWidth();
				int by2 = by1 + b1.getHeight();
				if(b1 instanceof House){
					by2+=50;
				}
				int b2x1 = (int) b2.getX();
				int b2y1 = (int) b2.getY();
				int b2x2 = b2x1 + b2.getWidth();
				int b2y2 = b2y1 + b2.getHeight();
				if(((bx1>=b2x1&&bx1<=b2x2)||(bx2>=b2x1&&bx2<=b2x2))&&((by1>=b2y1&&by1<=b2y2)||(by2>=b2y1&&by2<=b2y2))){
					i2.remove();
					r.removeBuilding(b2);
				}else if(by1 <= b2y2 && by2 >= b2y2 && bx1 <= b2x2 && bx2>=b2x2){
					i2.remove();
					r.removeBuilding(b2);
				}else if(by2>=b2y1 && by1<=b2y1 && bx2>=b2x1&&bx1<=b2x1){
					i2.remove();
					r.removeBuilding(b2);
				}
			}
		}
	}
	
	private void checkBuilDecCol(){
		
		Iterator<Building> i = r.getBuildings().iterator();
		while(i.hasNext()){
			Building b = i.next();
			int x1 = (int) b.getX();
			int y1 = (int) b.getY();
			int x2 = x1 + b.getWidth();
			int y2 = y1 + b.getHeight();
			if(b instanceof House){
				y2+=50;
			}
			Iterator<Decor> decor = r.getDecors().iterator();
			while(decor.hasNext()){
				Decor d = decor.next();
				int dx1 = (int) d.getX();
				int dy1 = (int) d.getY();
				int dx2 = dx1 + d.getWidth();
				int dy2 = dy1 + d.getHeight();
				if(((dx1 >= x1 && dx1 <= x2)||(dx2 >= x1 && dx2 <= x2))&&((dy1 >= y1 && dy1 <= y2)||(dy2 >= y1 && dy2 <= y2))){
					decor.remove();
					r.removeDecor(d);				
				}else if(((x1 >= dx1 && x1 <= dx2)||(x2 >= dx1 && x2 <= dx2))&&((y1 >= dy1 && y1 <= dy2)||(y2 >= dy1 && y2 <= dy2))){
					decor.remove();
					r.removeDecor(d);			
				}				
			}
		}
	}
	
	private void checkDecorCol(){
		
		LinkedList<Tree> trees = new LinkedList<Tree>();
		Iterator<Decor> i = r.getDecors().iterator();
		while(i.hasNext()){
			Decor d = i.next();
			if(d instanceof Tree){
				trees.add((Tree) d);				
			}
		}
		
		Iterator<Tree> t = trees.iterator();
		while(t.hasNext()){
			Tree d = t.next();
			int x1 = (int) d.getX();
			int y1 = (int) d.getY();
			int x2 = x1 + d.getWidth();
			int y2 = y1 + d.getHeight();
			Iterator<Decor> decor = r.getDecors().iterator();
			while(decor.hasNext()){
				Decor dc = decor.next();
				if(dc instanceof Flower){
					int dx1 = (int) dc.getX();
					int dy1 = (int) dc.getY();
					int dx2 = dx1 + dc.getWidth();
					int dy2 = dy1 + dc.getHeight();
					if(((dx1 >= x1 && dx1 <= x2)||(dx2 >= x1 && dx2 <= x2))&&((dy1 >= y1 && dy1 <= y2)||(dy2 >= y1 && dy2 <= y2))){
						decor.remove();
						r.removeDecor(dc);			
					}
				}
			}			
		}
	}	
	
	private void entityCount(){
		int tree = 0;
		int flower = 0;
		int road = 0;
		int spawner = 0;
		int entities = 0;
		Iterator<Sprite> i = r.getSprites().iterator();
		while(i.hasNext()){
			Sprite s = i.next();
			entities++;
			if(s instanceof Road){
				road++;
			}else if(s instanceof Tree){
				tree++;
			}else if(s instanceof Flower){
				flower++;
			}else if(s instanceof Spawner){
				spawner++;
			}
		}
		System.out.println("Trees: "+tree);
		System.out.println("Flowers: "+flower);
		System.out.println("Road: "+road);
		System.out.println("Spawner: "+spawner);
		System.out.println("Entities: "+entities);
	}
	
}
