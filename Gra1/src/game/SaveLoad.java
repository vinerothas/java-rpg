package game;

import generate.GenRiver;
import generate.GenShore;
import graphics.ImageHolder;
import graphics.sprite.*;

import items.*;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Random;

public class SaveLoad {	
	
	public void loadMap(Region r, ImageHolder h,int rx, int ry) throws IOException{
		String filename = String.format("%sx%sy", rx,ry);
		loadPlayer(r);
		filename = getSavePath()+"//"+r.gameName+"//"+filename+".txt";
		File file = new File(filename);
		//System.out.println("file exists"+filename+" "+file.exists());
		if(file.exists() == false){
			new InitRegion().init(r,rx,ry);
			return;
		}
		BufferedReader reader = new BufferedReader(new FileReader(filename));
			
		
		while(true){
			String line = reader.readLine();
			if(line==null){
				reader.close();
				break;
			}
			int[] data = new int[10];
			int temp = 0;
			int index = 0;
			int sign = 1;
			for(int i = 0;i<line.length();i++){			
				if(line.charAt(i) == '-'){
					sign = -1;
				}else if(line.charAt(i) != ' '){					
					String o = String.valueOf(line.charAt(i));
					int a = Integer.parseInt(o);
					if(temp != 0){
						temp = temp*10;
					}
					temp+= a;
				}else{					
					data[index] = temp*sign;
					index++;
					temp=0;
					sign = 1;
				}
				if(i == line.length()-1){
					data[index] = temp*sign;
				}
			}
			
			if(data[0]==Constants.SAVE_ID_CHUNK){
				r.x = data[1];
				r.y = data[2];
				r.setLevel(data[3]);
				r.envSeed = data[5];
				if(data[4]==Constants.ENV_TYPE_RIVER)r.tiles = GenRiver.generateRiver(data[5]);
				else if(data[4]==Constants.ENV_TYPE_SHORE)r.tiles = GenShore.generateShore(data[5]);
				r.envType = data[4];
				
				//TODO save seed
				Random rand = new Random();
				for(int i = 0;i<r.tilesVar.length;i++){
					for(int j = 0;j<r.tilesVar[0].length;j++){
						r.tilesVar[i][j]=rand.nextInt(Constants.TILE_VARIATIONS_GRASS);
					}
				}
			}else if(data[0]==Constants.SAVE_ID_TREE){
				Tree tree = new Tree(data[1],data[2]);
				r.addDecor(tree);
			}else if(data[0]==Constants.SAVE_ID_FLOWER){
				Flower flower = new Flower(data[1],data[2],data[3]);
				r.addDecor(flower);
			}else if(data[0]==Constants.SAVE_ID_SPAWNER){
				Spawner spawner = new Spawner(data[6],data[7],data[1],data[2]);
				spawner.setSpawnTime(data[3]);
				spawner.setSpawnLim(data[4]);
				spawner.setCurrMobs(data[5]);
				r.addBuilding(spawner);
			}else if(data[0]==Constants.SAVE_ID_ENEMY){
				Enemy enemy = getEnemy(data[3],h);				
				enemy.setCurHp(data[1]);
				enemy.setSpawnID(data[2]);
				enemy.setX(data[5]);
				enemy.setY(data[6]);
				enemy.setLevel(data[4]);
				r.addEnemy(enemy);
			}else if(data[0]==Constants.SAVE_ID_HOUSE){
				House house = new House(data[2],data[3],data[1]);	
				r.addBuilding(house);
			}else if(data[0]==Constants.SAVE_ID_ROAD){
				Road road = new Road(data[1],data[2]);	
				r.addBuilding(road);
			}else if(data[0] == Constants.SAVE_ID_SHOP){
				Item i = loadItem(data,1);
				r.shop.add(i);
			}else if(data[0]== Constants.SAVE_ID_DROP){
				Item i = loadItem(data,4);
				Drop d = new Drop(i,data[3]);
				d.setX(data[1]);
				d.setY(data[2]);
				r.addDrop(d);
			}
			
		}
		
	}
	
	public void makeFile(Region r){
		try {
			String filename = String.format("%sx%sy",r.x,r.y); 
			File file = new File(getSavePath()+"//"+r.gameName+"//"+filename+".txt");
			//System.out.println(file.getAbsolutePath());
			if(file.exists()==false)file.createNewFile();
			Formatter x = new Formatter(getSavePath()+"//"+r.gameName+"//"+filename+".txt");
			x.format("%s %s %s %s %s %s\n",Constants.SAVE_ID_CHUNK, r.x,r.y,r.getLevel(),r.envType,r.envSeed);
			Iterator<Sprite> i = r.getSprites().iterator();
			while(i.hasNext()){
				Sprite sprite = i.next();	
				if(sprite instanceof Decor){
					if(sprite instanceof Tree){
						x.format("%s ",Constants.SAVE_ID_TREE);
					}else if(sprite instanceof Flower){
						Flower flower = (Flower) sprite;
						x.format("%s %s ", Constants.SAVE_ID_FLOWER,flower.getType());
					}
				}else if(sprite instanceof Building){
					if(sprite instanceof Spawner){
						Spawner spawner = (Spawner) sprite;
						x.format("%s %s %s %s %s %s ",Constants.SAVE_ID_SPAWNER,spawner.getType(),spawner.getSpawnID(),
								spawner.getSpawnTime(),spawner.getSpawnLim(),spawner.getCurrMobs());
					}else if(sprite instanceof House){
						House house = (House) sprite;
						x.format("%s %s ", Constants.SAVE_ID_HOUSE, house.getType());
					}else if(sprite instanceof Road){
						x.format("%s ", Constants.SAVE_ID_ROAD);
					}
				}else if(sprite instanceof Enemy){	
					Enemy enemy = (Enemy) sprite;					
					x.format("%s %s %s %s %s ",Constants.SAVE_ID_ENEMY,enemy.getCurHp(),enemy.getSpawnID(), enemy.getType(), enemy.getLevel());
				}else if(sprite instanceof Drop){
					Drop drop = (Drop) sprite;
					x.format("%s %s %s %s %s", Constants.SAVE_ID_DROP, (int)drop.getX(), (int)drop.getY(), drop.getDropTime(), drop.getItem().getSaveString());
				}else if(sprite instanceof Player)continue;
				x.format("%s %s\n", (int)sprite.getX(),(int)sprite.getY());
			}
			Iterator<Item> k = r.shop.iterator();
			while(k.hasNext()){
				Item item = k.next();
				x.format("%s ", Constants.SAVE_ID_SHOP);
				x.format("%s ", item.getSaveString());
				x.format("\n");
			}
			x.close();
			
			Formatter p = new Formatter(getSavePath()+"//"+r.gameName+"//"+"player.txt");
			File file2 = new File(getSavePath()+"//"+r.gameName+"//"+"player.txt");
			if(file2.exists()==false)file2.createNewFile();
			Player player = r.getPlayer();
			/* 1life 2exp 3level 4money 5x 6y 7armor */
			p.format("%s %s %s\n",Constants.SAVE_ID_PLAYER_META, r.x,r.y);
			p.format("%s %s %s %s %s %s %s %s\n",Constants.SAVE_ID_PLAYER_STATS,player.getLife(), player.getExp(), player.getLevel(),
					player.getMoney(),(int)player.getX(),(int)player.getY(),player.getArmor());
			
			Item[][] inventory = player.getInventory();
			int rows = inventory.length;
			int columns = inventory[0].length;
			for(int yi = 0;yi<rows;yi++){
				for(int xi = 0;xi<columns;xi++){
					if(inventory[yi][xi]==null)continue;
					p.format("%s %s ", Constants.SAVE_ID_PLAYER_INVENTORY, inventory[yi][xi].getSaveString());
					p.format("\n");
				}
			}
				
			EquippedItems ei = player.equippedItems;
			if(ei.getChest()!=null)p.format("%s %s ", Constants.SAVE_ID_PLAYER_EQUIPS, ei.getChest().getSaveString());
			p.format("\n");
			if(ei.getHands()!=null)p.format("%s %s ", Constants.SAVE_ID_PLAYER_EQUIPS, ei.getHands().getSaveString());
			p.format("\n");
			if(ei.getHead()!=null)p.format("%s %s ", Constants.SAVE_ID_PLAYER_EQUIPS, ei.getHead().getSaveString());
			p.format("\n");
			if(ei.getLegs()!=null)p.format("%s %s ", Constants.SAVE_ID_PLAYER_EQUIPS, ei.getLegs().getSaveString());
			p.format("\n");
			if(ei.getFeet()!=null)p.format("%s %s ", Constants.SAVE_ID_PLAYER_EQUIPS, ei.getFeet().getSaveString());
			p.format("\n");
			if(ei.getShoulders()!=null)p.format("%s %s ", Constants.SAVE_ID_PLAYER_EQUIPS, ei.getShoulders().getSaveString());
			p.format("\n");
			if(ei.getWeapon()!=null)p.format("%s %s ", Constants.SAVE_ID_PLAYER_EQUIPS, ei.getWeapon().getSaveString());
			p.format("\n");
			
			p.close();
			
		} catch (Exception e) {e.printStackTrace();}
		
	}
	
	private void loadPlayer(Region r) throws IOException{
		//System.out.println(getSavePath()+"//"+r.gameName+"//"+"player.txt");
		BufferedReader reader = new BufferedReader(new FileReader(getSavePath()+"//"+r.gameName+"//"+"player.txt"));
		while(true){
			String line = reader.readLine();
			if(line==null){
				reader.close();
				break;
			}
			int[] data = new int[10];
			int temp = 0;
			int index = 0;
			int sign = 1;
			for(int i = 0;i<line.length();i++){			
				if(line.charAt(i) == '-'){
					sign = -1;
				}else if(line.charAt(i) != ' '){					
					String o = String.valueOf(line.charAt(i));
					int a = Integer.parseInt(o);
					if(temp != 0){
						temp = temp*10;
					}
					temp+= a;
				}else{					
					data[index] = temp*sign;
					index++;
					temp=0;
					sign = 1;
				}
				if(i == line.length()-1){
					data[index] = temp*sign;
				}
			}
			
			//player
			if(data[0]==1){
				Player player = new Player();
				player.setLevel(data[3]);
				//set level przed set life bo set level resetuje zycie
				player.setLife(data[1]);
				player.setX(data[5]);
				player.setY(data[6]);
				player.setExp(data[2]);
				player.addMoney(data[4]);
				player.setArmor(data[7]);
				r.addPlayer(player);
				player = r.getPlayer();
				
			//inventory
			}else if(data[0]==2){
				Item item = loadItem(data,1);
				InventoryManager.addItem(r.getPlayer().getInventory(), item);
			}
			//equips
			else if(data[0]==3){ 
				Equipable eq = (Equipable)loadItem(data,1);
				if(eq instanceof Weapon)r.getPlayer().addWeapon((Weapon) eq);
				else r.getPlayer().equippedItems.loadEquipement(eq);
			}
			
		}
	}
	
	public Point getLastRegion(String gameName) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(getSavePath()+"//"+gameName+"//"+"player.txt"));
		while(true){
			String line = reader.readLine();
			if(line==null){
				reader.close();
				break;
			}
			int[] data = new int[10];
			int temp = 0;
			int index = 0;
			int sign = 1;
			for(int i = 0;i<line.length();i++){			
				if(line.charAt(i) == '-'){
					sign = -1;
				}else if(line.charAt(i) != ' '){					
					String o = String.valueOf(line.charAt(i));
					int a = Integer.parseInt(o);
					if(temp != 0){
						temp = temp*10;
					}
					temp+= a;
				}else{					
					data[index] = temp*sign;
					index++;
					temp=0;
					sign = 1;
				}
				if(i == line.length()-1){
					data[index] = temp*sign;
				}
			}
			
			if(data[0]==0){
				return new Point(data[1],data[2]);
			}
		}
		return null;
	}
	
	/**
	 * @param data complete data from a line
	 * @param offset position of data[0] for item, 0 for no offset
	 * @return
	 */
	private Item loadItem(int[] data, int offset){
		if(data[0+offset]==Constants.SAVE_ID_ITEM_MOBSCRAP){
			MobScrap ms = new MobScrap(data[1+offset],data[2+offset]);
			return ms;
		}else if(data[0+offset]==Constants.SAVE_ID_ITEM_POTION){
			return new Potion(data[1+offset]);
		}else if(data[0+offset]==Constants.SAVE_ID_ITEM_ARMOR){
			Armor a = new Armor(data[1+offset],data[2+offset],data[3+offset],data[4+offset],data[5+offset]);
			return a;
		}else if(data[0+offset]==Constants.SAVE_ID_ITEM_WEAPON){
			Weapon w = new Weapon(data[1+offset],data[2+offset],data[3+offset],data[4+offset]);
			return w;
		}
		return null;
	}
	
	private Enemy getEnemy(int type, ImageHolder h){
		if(type==1||type==2)return new EnemySimp(h,type);
		else if(type==3)return new EnemyHor(h,type);
		else if(type==4)return new Enemy3Dir(h,type);
		System.out.println("Error line 376 SaveLoad getEnemy(type) - type not supported");		
		return null;	
	}
	
	private String getSavePath(){
		String jarPath = "";
		String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		try {
			jarPath = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		jarPath = jarPath.replace('/', '\\');
		jarPath = jarPath.substring(0, jarPath.lastIndexOf("\\")+1);
		jarPath = jarPath+"save";
		return jarPath;
	}
	
}
