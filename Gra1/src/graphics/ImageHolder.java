package graphics;

import game.Constants;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;

public class ImageHolder {

	public Image treeImg;
	public Image healthImg;
	public Image[] grassImgs = new Image[Constants.TILE_VARIATIONS_GRASS];
	public Image[][] shoreImgs; //0-upper left corner	1-upper left inner	2-upper side
	//[0][y] corner [1][y] inner [2][y] side
	//[x][0] upper(left) [x][1] (upper)right [x][2] lower(right) [x][3] (lower)left
	public Image[] waterImgs;
	public Image[] spawnImgs;
	public Image borderVerImg;
	public Image borderHorImg;
	public Image tileUIImg;
	public Image[] borderUIImgs;
	public Image windowUIImg;
	public Image[] roadImgs;
	public Image[] flowerImgs;
	public Image[] houseImgs;
	public Image[] itemImgs;
	public Image[] itemIImgs;
	public Image invTileImg;
	public Image[] bpUIImg;
	public Image tavernBG;
	public Image shopBG;
	public Image buttonImg;
	public Image expBarImg;
	public Image moneyUIImg;
	public Image potionImg;
	public Image arrowsUIImg;
	public Image armorUIImg;//TODO remove
	public Image slotUIImg;
	public Image[] coinsImgs;
	public Image cursorImg;
	public Image[][] armorImgs;
	/** 0hands 1helmet 2shoulders 3weapon 4chest 5shield 6sack 7legs 8boots	*/
	public Image[] armorUIImgs;
	public Image[] menuImgs = new Image[3];//menu, button, insert
	public Animation batAnim;
	public Animation slimeAnim;
	public Animation spiderAnim;
	public Animation[] snakeAnim = new Animation[3];
	public Animation[] playerAnim;
	
	public ImageHolder(Window w){
		loadImg(w);
	}
	
	private void loadImg(Window w){
		
		loadPlayerAnim(w);
		
		treeImg = getImage("tree");
		grassImgs[0] = getImage("tiles/grass");
		grassImgs[1] = rotateImage(grassImgs[0],90,w);
		grassImgs[2] = rotateImage(grassImgs[0],180,w);
		grassImgs[3] = rotateImage(grassImgs[0],270,w);
		grassImgs[4] = getImage("tiles/grass2");
		grassImgs[5] = rotateImage(grassImgs[4],90,w);
		grassImgs[6] = rotateImage(grassImgs[4],180,w);
		grassImgs[7] = rotateImage(grassImgs[4],270,w);
		shoreImgs = new Image[3][4];
		shoreImgs[0][0] = getImage("tiles/shore_corner");
		shoreImgs[1][0] = getImage("tiles/shore_inner");
		shoreImgs[2][0] = getImage("tiles/shore_side");
		int theta = 90;
		shoreImgs[0][1] = rotateImage(shoreImgs[0][0],theta,w);
		shoreImgs[1][1] = rotateImage(shoreImgs[1][0],theta,w);
		shoreImgs[2][1] = rotateImage(shoreImgs[2][0],theta,w);
		theta = 180;
		shoreImgs[0][2] = rotateImage(shoreImgs[0][0],theta,w);
		shoreImgs[1][2] = rotateImage(shoreImgs[1][0],theta,w);
		shoreImgs[2][2] = rotateImage(shoreImgs[2][0],theta,w);
		theta = 270;
		shoreImgs[0][3] = rotateImage(shoreImgs[0][0],theta,w);
		shoreImgs[1][3] = rotateImage(shoreImgs[1][0],theta,w);
		shoreImgs[2][3] = rotateImage(shoreImgs[2][0],theta,w);
		waterImgs = new Image[8];
		waterImgs[0] = getImage("tiles/water");
		waterImgs[1] = getImage("tiles/water2");
		waterImgs[2] = getImage("tiles/water3");
		waterImgs[3] = getImage("tiles/water4");
		waterImgs[4] = getImage("tiles/water5");
		waterImgs[7] = getImage("tiles/water2");
		waterImgs[6] = getImage("tiles/water3");
		waterImgs[5] = getImage("tiles/water4");
		borderVerImg = getImage("borderX");
		borderHorImg = getImage("borderY");
		
		Image houseImg = getImage("house");
		Image tavernImg = getImage("tavern");
		Image shopImg = getImage("shop");
		houseImgs = new Image[]{houseImg,tavernImg,shopImg};
					
		Image roadImg = getImage("road");
		Image road2Img = getImage("road2");
		roadImgs = new Image[]{null,roadImg,road2Img};
		
		potionImg = getImage("items/potion");
		
		Image item1Img = getImage("items/item1");
		Image item2Img = getImage("items/item2");
		Image item3Img = getImage("items/item3");
		Image item4Img = getImage("items/item4");
		itemImgs = new Image[]{null,item1Img,item2Img,item3Img,item4Img};
		
		Image itemI1Img = getImage("items/itemI1");
		Image itemI2Img = getImage("items/itemI2");
		Image itemI3Img = getImage("items/itemI3");
		Image itemI4Img = getImage("items/itemI4");
		itemIImgs = new Image[]{null,itemI1Img,itemI2Img,itemI3Img,itemI4Img};
		
		Image flowerImg = getImage("flower");
		Image flower2Img = getImage("flower2");
		flowerImgs = new Image[]{null,flowerImg,flower2Img};
		
		armorImgs = new Image[1][7];
		armorImgs[0][0] = getImage("items/b1");
		armorImgs[0][1] = getImage("items/c1");
		armorImgs[0][2] = getImage("items/h1");
		armorImgs[0][3] = getImage("items/l1");
		armorImgs[0][4] = getImage("items/s1");
		armorImgs[0][5] = getImage("items/g1");
		armorImgs[0][6] = getImage("items/w1");
		
		loadUI();
		loadEnemies();
	}
	
	private void loadUI(){
		menuImgs[0] = getImageJpg("UI/menu");
		menuImgs[1] = getImage("UI/menubutton");
		menuImgs[2] = getImage("UI/menuinsert");
		
		Image invButtImg = getImage("UI/backpack");
		Image invButtImg2 = getImage("UI/backpack2");
		bpUIImg = new Image[]{invButtImg,invButtImg2};		
		
		Image borderUIImg = getImage("UI/borderUI");
		Image borderUIImg2 = getImage("UI/border2UI");
		borderUIImgs = new Image[]{borderUIImg,borderUIImg2};
		
		invTileImg = getImage("UI/inventoryTile");
		tileUIImg = getImage("UI/tileUI");
		windowUIImg = getImage("UI/windowUI");
		healthImg = getImage("UI/health");
		buttonImg = getImage("UI/button");
		expBarImg = getImage("UI/expBar");
		//moneyUIImg = getImage("UI/moneyUI");
		arrowsUIImg = getImage("UI/arrows");
		cursorImg = getImage("UI/cursor");
		
		armorUIImg = getImage("UI/armorui/armor");
		armorUIImgs = new Image[9];
		armorUIImgs[Constants.ITEM_SLOT_HEAD] = getImage("UI/armorui/helmet");
		armorUIImgs[Constants.ITEM_SLOT_SHOULDERS] = getImage("UI/armorui/shoulders");
		armorUIImgs[Constants.ITEM_SLOT_HANDS] = getImage("UI/armorui/hands");
		armorUIImgs[0] = getImage("UI/armorui/shield");
		armorUIImgs[Constants.ITEM_SLOT_CHEST] = getImage("UI/armorui/chest");
		armorUIImgs[Constants.ITEM_SLOT_LEGS] = getImage("UI/armorui/legs");
		armorUIImgs[Constants.ITEM_SLOT_FEET] = getImage("UI/armorui/boots");
		armorUIImgs[8] = getImage("UI/armorui/sack");
		armorUIImgs[Constants.ITEM_SLOT_WEAPON] = getImage("UI/armorui/weapon");
		slotUIImg = getImage("UI/armorui/slot");
		
		Image c = getImage("UI/copper");
		Image s = getImage("UI/silver");
		Image g = getImage("UI/gold");
		coinsImgs = new Image[]{c,s,g};
		
		tavernBG = getImage("UI/tavernbg");
		shopBG = getImage("UI/shopbg");
	}
	
	private void loadEnemies(){
		
		Image slimeSpawnImg =  getImage("slimeSpawn");
		Image batSpawnImg = getImage("batSpawn");
		Image spiderSpawnImg = getImage("spiderSpawn");
		spawnImgs = new Image[]{null,slimeSpawnImg,batSpawnImg,spiderSpawnImg,spiderSpawnImg};
		
		Image spiderImg = getImage("spider");
		Image spiderImg2 = getImage("spider2");
		Image[] spiderImgs = new Image[]{spiderImg,spiderImg2};
		int[] spiderTimes = new int[]{0,150,300};
		spiderAnim = new Animation(spiderImgs,spiderTimes);
		
		Image[] slimeImgs = new Image[6];
		slimeImgs[0] = getImage("slime/slime");
		for(int i= 1;i<6;i++){
			int a = i+1;
			slimeImgs[i] = getImage("slime/slime"+a);
		}

		int[] slimeTimes = new int[]{0,150,300,450,600,750,900};
		slimeAnim = new Animation(slimeImgs, slimeTimes);
				
		Image batImg = getImage("bat");		
		Image batImg2 = getImage("bat2");		
		Image batImg3 = getImage("bat3");
		Image[] batImgs = new Image[]{batImg,batImg2, batImg3, batImg2};
		int[] batTimes = new int[]{0,100,200,300,400};
		batAnim = new Animation(batImgs, batTimes);
		
		Image snakeImg = getImage("snake/U/1");
		Image snakeImg2 = getImage("snake/U/2");
		Image snakeImg3 = getImage("snake/U/3");
		Image snakeImg4 = getImage("snake/U/4");
		Image snakeImg5 = getImage("snake/U/5");
		Image snakeImg6 = getImage("snake/U/6");
		Image[] snakeImgs = new Image[]{snakeImg,snakeImg2, snakeImg3, snakeImg4, snakeImg5, snakeImg6};
		int t = 130;
		int[] snakeTimes = new int[]{0,t,t*2,t*3,t*4,t*5,t*6};
		snakeAnim[0] = new Animation(snakeImgs, snakeTimes);
		
		snakeImg = getImage("snake/D/1");
		snakeImg2 = getImage("snake/D/2");
		snakeImg3 = getImage("snake/D/3");
		snakeImg4 = getImage("snake/D/4");
		snakeImg5 = getImage("snake/D/5");
		snakeImg6 = getImage("snake/D/6");
		snakeImgs = new Image[]{snakeImg,snakeImg2, snakeImg3, snakeImg4, snakeImg5, snakeImg6};
		t = 100;
		snakeTimes = new int[]{0,t,t*2,t*3,t*4,t*5,t*6};
		snakeAnim[1] = new Animation(snakeImgs, snakeTimes);
		
		snakeImg = getImage("snake/L/1");
		snakeImg2 = getImage("snake/L/2");
		snakeImg3 = getImage("snake/L/3");
		snakeImg4 = getImage("snake/L/4");
		snakeImg5 = getImage("snake/L/5");
		snakeImgs = new Image[]{snakeImg,snakeImg2, snakeImg3, snakeImg4, snakeImg5};
		t = 125;
		snakeTimes = new int[]{0,t,t*2,t*3,t*4,t*5};
		snakeAnim[2] = new Animation(snakeImgs, snakeTimes);
	}
	
	/**
	 * Rotate in degrees
	 */
	public Image rotateImage(Image image, int theta, Window w) {
		double rad = ((double)(theta)*2*Math.PI)/(double)360;
		int hi = image.getHeight(null);
		int wi = image.getWidth(null);
		AffineTransform xform = new AffineTransform();
		xform.translate(0.5*hi, 0.5*wi);
		xform.rotate(rad);
		xform.translate(-0.5*wi, -0.5*hi);

        // create a transparent (not translucent) image
        Image newImage = w.gc.createCompatibleImage(
            image.getWidth(null),
            image.getHeight(null),
            Transparency.BITMASK);

        // draw the transformed image
        Graphics2D g = (Graphics2D)newImage.getGraphics();
        g.drawImage(image, xform, null);
        g.dispose();

        return newImage;
    }
	
	private void loadPlayerAnim(Window w){
		
		Image[] playerRImgs = loadPlayerR(w);
		int[] playerRTimes = new int[]{0,100,250};
		Animation playerRAnim = new Animation(playerRImgs, playerRTimes);
		
		Image[] playerLImgs = loadPlayerL(w);
		int[] playerLTimes = new int[]{0,100,250};
		Animation playerLAnim = new Animation(playerLImgs, playerLTimes);
		
		Image[] playerUImgs = loadPlayerU(w);
		int[] playerUTimes = new int[]{0,100,250,350,500};
		Animation playerUAnim = new Animation(playerUImgs, playerUTimes);
		
		Image[] playerDImgs = loadPlayerD(w);
		int[] playerDTimes = new int[]{0,100,250,350,500};
		Animation playerDAnim = new Animation(playerDImgs, playerDTimes);
		
		playerAnim = new Animation[]{playerRAnim, playerLAnim,playerUAnim,playerDAnim};
	}
	
	private Image[] loadPlayerD(Window w){
		Image D1B = getImage("player/D/1/B/def");
		Image D1C = getImage("player/D/1/C/def");
		Image D1H = getImage("player/D/1/H/def");
		Image D1L = getImage("player/D/1/L/def");
		Image D1S = getImage("player/D/1/S/def");
		Image D1W = getImage("player/D/1/W/def");
		Image D2L = getImage("player/D/2/L/def");
		Image D2B = getImage("player/D/2/B/def");
		Image D3L = getImage("player/D/3/L/def");
		Image D3B = getImage("player/D/3/B/def");
		
		Image D = playerD(new Image[]{D1B,D1C,D1H,D1L,D1S,D1W},w);       
        Image D2 = playerD(new Image[]{D2B,D1C,D1H,D2L,D1S,D1W},w);
        Image D3 = playerD(new Image[]{D3B,D1C,D1H,D3L,D1S,D1W},w);
        return new Image[]{D,D2,D,D3};
	}
	
	private Image playerD(Image[] parts,Window w){
		Image D = w.gc.createCompatibleImage(29,37,Transparency.BITMASK);
		Graphics2D g = (Graphics2D)D.getGraphics();
		g.drawImage(parts[5], 0,12, null);
        g.drawImage(parts[0], 8,32, null);
        g.drawImage(parts[1], 6,9, null);
        g.drawImage(parts[2],9,0,null);
        g.drawImage(parts[3], 9,22, null);
        g.drawImage(parts[4], 5,8, null);       
        g.dispose();
        return D;
	}
	
	private Image[] loadPlayerL(Window w){
		Image L1B = getImage("player/L/1/B/def");
		Image L1C = getImage("player/L/1/C/def");
		Image L1H = getImage("player/L/1/H/def");
		Image L1L = getImage("player/L/1/L/def");
		Image L1S = getImage("player/L/1/S/def");
		Image L1W = getImage("player/L/1/W/def");
		Image L2L = getImage("player/L/2/L/def");
		Image L2B = getImage("player/L/2/B/def");
		
		Image L = playerL(new Image[]{L1B,L1C,L1H,L1L,L1S,L1W},w);       
        Image L2 = playerL(new Image[]{L2B,L1C,L1H,L2L,L1S,L1W},w);
        return new Image[]{L,L2};
	}
	
	private Image playerL(Image[] parts,Window w){
		Image D = w.gc.createCompatibleImage(29,37,Transparency.BITMASK);
		Graphics2D g = (Graphics2D)D.getGraphics();
        g.drawImage(parts[5], 0,12, null);
        g.drawImage(parts[0], 8,32, null);
        g.drawImage(parts[1], 10,9, null);
        g.drawImage(parts[2], 13,0,null);
        g.drawImage(parts[3], 11,22, null);
        g.drawImage(parts[4], 10,9, null);
        g.dispose();
        return D;
	}
	
	private Image[] loadPlayerR(Window w){
		Image R1B = getImage("player/R/1/B/def");
		Image R1C = getImage("player/R/1/C/def");
		Image R1H = getImage("player/R/1/H/def");
		Image R1L = getImage("player/R/1/L/def");
		Image R1S = getImage("player/R/1/S/def");
		Image R1W = getImage("player/R/1/W/def");
		Image R2L = getImage("player/R/2/L/def");
		Image R2B = getImage("player/R/2/B/def");
		
		Image R = playerR(new Image[]{R1B,R1C,R1H,R1L,R1S,R1W},w);       
        Image R2 = playerR(new Image[]{R2B,R1C,R1H,R2L,R1S,R1W},w);
        return new Image[]{R,R2};
	}
	
	private Image playerR(Image[] parts,Window w){
		Image D = w.gc.createCompatibleImage(29,37,Transparency.BITMASK);
		Graphics2D g = (Graphics2D)D.getGraphics();
        g.drawImage(parts[0], 4,32, null);
        g.drawImage(parts[1], 1,9, null);
        g.drawImage(parts[2], 4,0,null);
        g.drawImage(parts[3], 3,22, null);
        g.drawImage(parts[4], 0,9, null);
        g.drawImage(parts[5], 2,12, null);
        g.dispose();
        return D;
	}
	
	private Image[] loadPlayerU(Window w){
		Image U1B = getImage("player/U/1/B/def");
		Image U1C = getImage("player/U/1/C/def");
		Image U1H = getImage("player/U/1/H/def");
		Image U1L = getImage("player/U/1/L/def");
		Image U1S = getImage("player/U/1/S/def");
		Image U1W = getImage("player/U/1/W/def");
		Image U2L = getImage("player/U/2/L/def");
		Image U2B = getImage("player/U/2/B/def");
		Image U2C = getImage("player/U/2/C/def");
		Image U3L = getImage("player/U/3/L/def");
		Image U3B = getImage("player/U/3/B/def");
		
		Image U = playerU(new Image[] {U1B,U1C,U1H,U1L,U1S,U1W},w);       
        Image U2 = playerU(new Image[]{U2B,U2C,U1H,U2L,U1S,U1W},w);
        Image U3 = playerU(new Image[]{U3B,U1C,U1H,U3L,U1S,U1W},w);
        return new Image[]{U,U2,U,U3};
	}
	
	private Image playerU(Image[] parts,Window w){
		Image D = w.gc.createCompatibleImage(29,37,Transparency.BITMASK);
		Graphics2D g = (Graphics2D)D.getGraphics();
        g.drawImage(parts[5], 21,12, null);
        g.drawImage(parts[0], 7,26, null);
        g.drawImage(parts[1], 5,8, null);
        g.drawImage(parts[2], 8,0,null);
        g.drawImage(parts[3], 7,22, null);
        g.drawImage(parts[4], 4,8, null);
        g.dispose();
        return D;
	}
	
	private Image getImage(String path){
		URL is = this.getClass().getResource("img/"+path+".PNG");
		try{
		return new ImageIcon(is).getImage(); 
		}catch(Exception ex){
			is = this.getClass().getResource("img/"+path+".png");
			return new ImageIcon(is).getImage(); 
		}
	}
	
	private Image getImageJpg(String path){
		URL is = this.getClass().getResource("img/"+path+".JPG");
		try{
		return new ImageIcon(is).getImage(); 
		}catch(Exception ex){
			is = this.getClass().getResource("img/"+path+".jpg");
			return new ImageIcon(is).getImage(); 
		}
	}
	
}
