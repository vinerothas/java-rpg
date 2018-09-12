package game;

import graphics.sprite.Building;
import graphics.sprite.Decor;
import graphics.sprite.Enemy;
import graphics.sprite.Drop;
import graphics.sprite.Player;
import graphics.sprite.Sprite;
import items.*;

import java.util.LinkedList;

public class Region {

	private LinkedList<Sprite> sprites;
	private LinkedList<Enemy> enemies;
	private LinkedList<Building> buildings;
	private LinkedList<Decor> decors;
	private LinkedList<Drop> drops;
	public LinkedList<Loot> inventory;
	public LinkedList<Item> shop;
	private Player player;
	private int level = 1;
	public int x = 0;
	public int y = 0;
	public String gameName = "";
	public int[][] tiles = new int[Constants.TILE_NUMBER][Constants.TILE_NUMBER];	//0-grass	1-shallow_water
	public int[][] tilesVar = new int[Constants.TILE_NUMBER][Constants.TILE_NUMBER];
	public int envSeed;
	public int envType; //1-forest	2-river	3-shore	
	
	public Region(){
		cleanRegion();
	}
	
	public void cleanRegion(){
		sprites = new LinkedList<Sprite>();			
		enemies = new LinkedList<Enemy>();
		buildings = new LinkedList<Building>();
		decors = new LinkedList<Decor>();
		drops = new LinkedList<Drop>();
		inventory = new LinkedList<Loot>();
		shop = new LinkedList<Item>();
		tiles = new int[Constants.TILE_NUMBER][Constants.TILE_NUMBER];
		for(int i = 0;i<tiles.length;i++){
			for(int j = 0;j<tiles[0].length;j++){
				tiles[i][j]=0;
				tilesVar[i][j]=0;
			}
		}
		player= null;
	}
	
	public int getLevel(){
		return level;
	}
	public void setLevel(int l){
		if(l >0){
			level = l;
		}else{
			level =1;
		}
	}
	
	public void addDrop(Drop d){
		sprites.add(d);
		drops.add(d);
	}
	public void removeDrop(Drop d){
		sprites.remove(d);
		drops.remove(d);
	}
	public LinkedList<Drop> getDrops(){
		return drops;
	}
	
	public void addDecor(Decor d){
		sprites.add(d);
		decors.add(d);
	}
	public void removeDecor(Decor d){
		sprites.remove(d);
		decors.remove(d);
	}
	public LinkedList<Decor> getDecors(){
		return decors;
	}
	
	public void addBuilding(Building b){
		sprites.add(b);
		buildings.add(b);
	}
	public void removeBuilding(Building b){
		sprites.remove(b);
		buildings.remove(b);
	}
	public LinkedList<Building> getBuildings(){
		return buildings;
	}
	
	public void addEnemy(Enemy e){
		sprites.add(e);
		enemies.add(e);
	}
	public void removeEnemy(Enemy e){
		sprites.remove(e);
		enemies.remove(e);
	}
	public LinkedList<Enemy> getEnemies(){
		return enemies;
	}
	
	public LinkedList<Sprite> getSprites(){
		return sprites;
	}
	
	public void addPlayer(Player p){
		sprites.add(p);
		player = p;
	}
	public Player getPlayer(){
		return player;
	}
}
