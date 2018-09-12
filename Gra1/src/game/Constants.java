package game;

import java.awt.Point;

public class Constants {
	
	public static final int DIR_UP = 1;
	public static final int DIR_RIGHT = 2;
	public static final int DIR_DOWN = 3;
	public static final int DIR_LEFT = 4;
	
	public static final int REGION_PLAYABLE_SIZE = 10000;
	public static final int REGION_PLAYABLE_QUARTER = REGION_PLAYABLE_SIZE/2;
	public static final int REGION_BORDER_SIZE = 500;
	public static final int REGION_TOTAL_SIZE = REGION_PLAYABLE_SIZE+(REGION_BORDER_SIZE*2);
	public static final int REGION_TOTAL_QUARTER = REGION_TOTAL_SIZE/2;
	
	public static final float MENU_ZOOM = (float) 1.0;
	
	public static final int TILE_SIZE = 20;
	public static final int TILE_NUMBER = (REGION_TOTAL_SIZE/TILE_SIZE)+1;
	// 0->20 == tile0 
	public static final int TILE_MIDDLE = (REGION_TOTAL_QUARTER/TILE_SIZE);
	
	public static final int	TILE_TYPE_GRASS = 0;
	public static final int TILE_TYPE_WATER = 1;
	public static final int TILE_TYPE_DEEP = 2;
	
	public static final int TILE_VARIATIONS_GRASS = 8;
	
	public static final int ENEMY_TYPE_SLIME = 1;
	public static final int ENEMY_TYPE_BAT = 2;
	public static final int ENEMY_TYPE_SPIDER = 3;
	public static final int ENEMY_TYPE_SNAKE = 4;
	
	public static final int ENV_TYPE_FOREST = 0;
	public static final int ENV_TYPE_RIVER = 1;
	public static final int ENV_TYPE_SHORE = 2;
	
	public static final int HOUSE_TYPE_NORMAL_LEFT = 0;
	public static final int HOUSE_TYPE_NORMAL_RIGHT = 1;
	public static final int HOUSE_TYPE_TAVERN_LEFT = 2;
	public static final int HOUSE_TYPE_TAVERN_RIGHT = 3;
	public static final int HOUSE_TYPE_SHOP_LEFT = 4;
	public static final int HOUSE_TYPE_SHOP_RIGHT = 5;
	
	public static final int SAVE_ID_CHUNK = 0;
	public static final int SAVE_ID_PLAYER = 1;
	public static final int SAVE_ID_TREE = 2;
	public static final int SAVE_ID_SPAWNER = 3;
	public static final int SAVE_ID_HOUSE = 4;
	public static final int SAVE_ID_ENEMY = 5;
	public static final int SAVE_ID_DROP = 6;
	public static final int SAVE_ID_FLOWER = 7;
	public static final int SAVE_ID_SHOP = 8;
	public static final int SAVE_ID_ROAD = 9;
	public static final int SAVE_ID_ITEM_MOBSCRAP = 1;
	public static final int SAVE_ID_ITEM_POTION = 2;
	public static final int SAVE_ID_ITEM_ARMOR = 3;
	public static final int SAVE_ID_ITEM_WEAPON = 4;
	public static final int SAVE_ID_PLAYER_META = 0;
	public static final int SAVE_ID_PLAYER_STATS = 1;
	public static final int SAVE_ID_PLAYER_INVENTORY = 2;
	public static final int SAVE_ID_PLAYER_EQUIPS = 3;
	
	public static final int ITEM_QUALITY_POOR = 1;
	public static final int ITEM_QUALITY_NORMAL = 2;
	public static final int ITEM_QUALITY_GOOD = 3;
	public static final int ITEM_QUALITY_UNUSUAL = 4;
	public static final int ITEM_QUALITY_RARE = 5;
	public static final int ITEM_QUALITY_MIGHTY = 6;
	public static final int ITEM_QUALITY_MASTERPIECE = 7;
	public static final String ITEM_QUALITY_POOR_DESC = "Poor";
	public static final String ITEM_QUALITY_NORMAL_DESC = "";
	public static final String ITEM_QUALITY_GOOD_DESC = "Good";
	public static final String ITEM_QUALITY_UNUSUAL_DESC = "Unusual";
	public static final String ITEM_QUALITY_RARE_DESC = "Rare";
	public static final String ITEM_QUALITY_MIGHTY_DESC = "Mighty";
	public static final String ITEM_QUALITY_MASTERPIECE_DESC = "Masterpiece";
	
	public static final int ITEM_SLOT_FEET = 1;
	public static final int ITEM_SLOT_CHEST = 2;
	public static final int ITEM_SLOT_HEAD = 3;
	public static final int ITEM_SLOT_LEGS = 4;
	public static final int ITEM_SLOT_SHOULDERS = 5;
	public static final int ITEM_SLOT_HANDS = 6;
	public static final int ITEM_SLOT_WEAPON = 7;
	
	public static final int PSTATE_ALIVE = 0;
	public static final int PSTATE_RESURRECT = 1;
	public static final int PSTATE_EXIT_BUILDING = 2;
	public static final int PSTATE_SLEEP_ACTION_PENDING = 3;
	public static final int PSTATE_PLAY_POKER = 4;
	public static final int PSTATE_MAIN_MENU = 5;
	public static final int PSTATE_NEW_GAME_MENU = 6;
	public static final int PSTATE_LOAD_GAME_MENU = 7;
	public static final int PSTATE_QUIT_GAME = 8;
	public static final int PSTATE_START_NEW_GAME=9;
	public static final int PSTATE_NEW_GAME_NAME_TAKEN =10;
	public static final int PSTATE_PAUSE_MENU_INGAME = 11;
	public static final int PSTATE_SAVE_AND_QUIT = 12;
	public static final int PSTATE_LOAD_GAME = 13;
	
	public static Point coordToTile(int x, int y){
		//System.out.println("x "+ x+" y "+y);
		int xrest = x%20;
		int yrest = y%20;
		x-=xrest;
		y-=yrest;
		double xd = (double)x/(double)20;
		double yd = (double)y/(double)20;
		//System.out.println("xd "+xd+" yd "+yd);
		//200 is the position of tile 0
		//as in coords 0->20
		return new Point((int)xd+TILE_MIDDLE,(int)yd+TILE_MIDDLE);
	}
	

	
	/*
	public static final int WINDOW_WIDTH = 620;
	public static final int WINDOW_HEIGHT = 500;
	public static final int WINDOW_UI_WIDTH = 0;
	public static final int WINDOW_REGION_VIEW_WIDTH = WINDOW_WIDTH-WINDOW_UI_WIDTH;
	public static final int WINDOW_REGION_VIEW_HEIGHT = WINDOW_HEIGHT;
		
	//public static final int TILE_NUMBER_WIDE = (WINDOW_REGION_VIEW_WIDTH/TILE_SIZE)+1;
	//public static final int TILE_NUMBER_HIGH = (WINDOW_REGION_VIEW_HEIGHT/TILE_SIZE)+1;
	*/
	
}
