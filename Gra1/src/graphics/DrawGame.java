package graphics;

import game.Constants;
import game.Region;
import graphics.sprite.*;
import input.Button;
import items.*;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import window.TravelWindow;

public class DrawGame {
	
	public Window w;
	public DataUI dui;
	private DrawTiles dt;
	Region r;
	private Point screenMid;
	Point screenSize;
	public Graphics2D g;
	public long elapsedTime;
	private long drawHit = 0;
	public boolean hit;
	public ImageHolder h;
	//int sunset = 0;
	
	public DrawGame(ImageHolder h, Window w, Region r, DataUI dUI, Point screenSize){
		this.w = w;
		this.r = r;
		this.screenSize = screenSize;
		screenMid = new Point(screenSize.x/2,screenSize.y/2);
		this.dui = dUI;
		this.h = h;
		dt = new DrawTiles(this);
		Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(
				h.cursorImg, new Point(0, 0), "itemHeld");
		w.window.setCursor(cursor);	
	}
	
	public void draw(long elapsedTime, Graphics2D g){
		this.elapsedTime = elapsedTime;
		this.g = g;
		
		// TODO remove
		/*
		if(dui.insideB()){
			new DrawBuilding().drawBuilding(this,g);
			if(dui.getPState()==4){//zeby nie rysowac dwa razy podczas pokera
				return;
			}
		}
		*/
		
		drawGame();
		
		//TODO remove
		/*
		if(dui.getItemHover()<0){
			Item item = r.getPlayer().equips[(-dui.getItemHover())-1];
			if(item!=null)drawValue(350,70,item,item.getValue());
		}
		*/
		
		g.setColor(Color.BLACK);
		g.drawString((int)r.getPlayer().getX()+" "+(int)r.getPlayer().getY()+"   Rx"+r.x+" Ry"+r.y,50,50);
		//g.drawString(dui.mouseClicked.x+" "+dui.mouseClicked.y, 50, 65); null exception
	}
	
	private void drawGame(){
		
		dt.drawTiles((int)r.getPlayer().getX(),(int)r.getPlayer().getY(),elapsedTime,this);
		drawSprites();
		
		if(hit == true){
		drawHit();
		}
		
		//TODO new DrawUI().drawUI(this);
		
		drawBorder();
		
		int xHealth =(screenMid.x)-(h.healthImg.getWidth(null)/2);
		int yHealth = screenSize.y-35;
		double curHP = r.getPlayer().getLife();
		double maxHP = r.getPlayer().getMhp();
		double HP = curHP/maxHP;
		int health = (int) (HP*200);
		if(health>0){
		g.setColor(Color.RED);
		g.fillRect(xHealth+1, yHealth+1, health-1, 4);
		}else{
			dui.setDrawUI(2);
			dui.setTextUI("You died", "Restart?", null);
		}
		g.drawImage(h.healthImg, xHealth, yHealth, null);
		
		//commented out overlays and day/night cycle go here
	}
	
	private void drawBorder(){
		final int borderLength = 100;
		int lengthY = (screenSize.y/borderLength)+1;
		int lengthX = (screenSize.x/borderLength)+1;
		int errorMargin = -50;
		int nearX = (Constants.REGION_PLAYABLE_QUARTER-screenMid.x)+errorMargin;
		int nearY = (Constants.REGION_PLAYABLE_QUARTER-screenMid.y)+errorMargin;
		if(r.getPlayer().getX() > nearX){
			for(int i = 0;i<lengthY;i++){
				g.drawImage(h.borderVerImg, (int) ((Constants.REGION_PLAYABLE_QUARTER+screenMid.x)-r.getPlayer().getX()), i*borderLength, null);
			}
		}else if(r.getPlayer().getX() < -nearX){
			for(int i = 0;i<lengthY;i++){
				g.drawImage(h.borderVerImg, (int) ((-Constants.REGION_PLAYABLE_QUARTER+screenMid.x)-r.getPlayer().getX()), i*borderLength, null);
			}
		}
		if(r.getPlayer().getY() > nearY){
			for(int i = 0;i<lengthX;i++){
				g.drawImage(h.borderHorImg, i*borderLength, (int) ((Constants.REGION_PLAYABLE_QUARTER+screenMid.y)-r.getPlayer().getY()), null);
			}
		}else if(r.getPlayer().getY() < -nearY){
			for(int i = 0;i<lengthX;i++){
				g.drawImage(h.borderHorImg, i*borderLength, (int) ((-Constants.REGION_PLAYABLE_QUARTER+screenMid.y)-r.getPlayer().getY()), null);
			}
		}
	}
	
	public void drawExitButton(){
		g.drawImage(h.buttonImg, 406, 450,60,30, null);
		getDialogFont();
		g.drawString("EXIT", 419, 470);
	}

	private void drawHit(){
		
		final int hitTime = 300;
		
		Player player = r.getPlayer();
				
		if(drawHit+elapsedTime > hitTime){
			drawHit = 0;
			hit = false;
			return;
		}else if(drawHit+elapsedTime > 100 && drawHit+elapsedTime < 250){
			drawHit+=elapsedTime;
			return;
		}
		g.setColor(Color.BLACK);
		drawHit = drawHit+elapsedTime;
		double d = drawHit;
		double a = d/hitTime;	
		int maxRangeX = 21*4;
		int maxRangeY = 21;
		int shoveY = 19;
		int shoveX = 17;
		int px=screenMid.x-15;
		int py=screenMid.y-18;
		if(player.getLastDir()==0){
			maxRangeX = 25*4;
			//g.fillRect(px+10, py-5, 35, 45);
		}else if(player.getLastDir()==1){
			//g.fillRect(px-10, py-5, 35, 45);
			maxRangeX = -21*4;
			shoveX = 10;
		}
		int x2 = (int) (((-(a*a))+a)*maxRangeX);
		int y2 = (int) (Math.sin((a*(Math.PI*2) ))*maxRangeY);
		if(player.getLastDir()==2){
			//g.fillRect(px-5, py-15, 40, 30);
			shoveX=15;
			shoveY=5;
			x2 = (int) (Math.sin((a*(Math.PI*2)))*maxRangeY);
			y2 = (int) (((-(a*a))+a)*maxRangeX);
		}else if(player.getLastDir()==3){
			//g.fillRect(px-5, py+10, 40, 30);
			shoveX=15;
			shoveY=25;
			maxRangeX = -21*4;
			x2 = (int) (Math.sin((a*(Math.PI*2)))*maxRangeY);
			y2 = (int) (((-(a*a))+a)*maxRangeX);
		}		
		g.setColor(Color.YELLOW);
		g.drawLine(px+shoveX+(x2/2), py+shoveY-(y2/2), px+shoveX+x2,py+shoveY-y2);
	}
	
	private void drawSprites(){
		LinkedList<Sprite> list = new LinkedList<Sprite>();
		int px = (int)r.getPlayer().getX()-(screenMid.x)+(r.getPlayer().getWidth()/2);
		int py = (int)r.getPlayer().getY()-(screenMid.y)+(r.getPlayer().getHeight()/2);
		//System.out.println(px+" "+py);
		Iterator<Sprite> i = r.getSprites().iterator();
		int xlim = screenSize.x;
		int ylim = screenSize.y;
		while(i.hasNext()){
			Sprite s = i.next();
			int x = (int) (s.getX()-px);
			int y = (int) (s.getY()-py);
			if(	!(x > xlim
				|| y > ylim
				|| y < -(s.getHeight()+50)
				|| x < -s.getWidth()) )
			{
				list.add(s);
			}
		}
				
		Collections.sort(list, new HeightComparator());
		g.setFont(new Font("Dialog",Font.BOLD,9));
		i = list.iterator();
		while(i.hasNext()){
			Sprite s = i.next();
			int x = (int) (s.getX()-px);
			int y = (int) (s.getY()-py);
			s.draw(this, x, y);
		}
		
	}
	
	/*
	public void drawInv(){
		int ix = 115;
		int iy = 120;		
		for(int i = 0;i<10;i++){
			for(int j = 0;j<10;j++){
				g.drawImage(h.invTileImg,ix+(j*28),iy+(i*24),null);		
			}
		}
		for(int i = 0;i<15;i++){
			g.drawImage(h.borderUIImgs[1],(ix-10)+i*20,iy-7,null);	
			g.drawImage(h.borderUIImgs[1],(ix-10)+i*20,iy+240,null);
		}
		for(int i = 0;i<13;i++){
			g.drawImage(h.borderUIImgs[0],ix-5,(iy-10)+(i*20),null);	
			g.drawImage(h.borderUIImgs[0],ix+278,(iy-10)+(i*20),null);
		}
		//drawCount(ix+5,iy+3,1);
		//g.drawImage(transfImage(itemImgs[1]),ix+5,iy+3,null);
		//drawCount(ix+33,iy+3,2);
		//g.drawImage(transfImage(itemImgs[2]),ix+33,iy+3,null);
		//drawCount(ix+60,iy+3,3);
		//g.drawImage(transfImage(itemImgs[3]),ix+60,iy+3,null);
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Dialog",Font.BOLD,13));
		Iterator<Loot> i = r.inventory.iterator();		
		while(i.hasNext()){			
			Loot l = i.next();
			int posx = l.getPos()%10;
			int posy = l.getPos()/10;
			if(posx==0){
				posx=10;
				posy--;
			}
			int lx = ix+4+((posx-1)*28);
			int ly = iy+2+(posy*24);
			drawCount(lx,ly,l.getQuantity());
			drawItem(lx,ly,l.getItem());
		}
		
		int pos = dui.getItemHover();
		if(pos>0){
			if(dui.isInBuilding()==5||dui.getDrawUI()==1){
				InventoryManager invm = new InventoryManager();
				Loot l = invm.getLootPos(pos, r.inventory);
				if(l!=null){
					getDialogFont();
					Point p = invm.getLootLoc(pos);
					drawValue(p.x,p.y,l.getItem(),l.getValue());
					//g.fillRect(x, y, 20, 10);
				}else{
					dui.setItemHover(0);
				}
			}			
		}
	}*/
	
	public void drawValue(int x, int y,Item item, int v){
		x+=25;
		int w = 0;
		if(item instanceof Armor || item instanceof Weapon){			
			ArmorNaming am = new ArmorNaming();
			String name = "error";
			int quality = 0;
			int level = 0;
			int stat = 0;
			if(item instanceof Armor){
				Armor armor = (Armor) item;
				name = am.getName(armor.getSlot(),armor.getType(),armor.getQuality());
				quality = armor.getQuality();
				level = armor.getLevel();
				stat = armor.getArmor();
			}else if(item instanceof Weapon){
				Weapon weapon = (Weapon) item;
				name = am.getName(7,weapon.getType(),weapon.getQuality());
				quality = weapon.getQuality();
				level = weapon.getLevel();
				stat = weapon.getPower();
			}
			int m = name.length();
			String d = "0";
			if(item instanceof Armor)d = "Armor: "+stat;
			else d = "Damage "+stat;
			if(d.length()>m)m=d.length();
			w = (int) (10*m+6);
			if(w<70)w=80;
			g.setFont(new Font("Monospaced",Font.BOLD,16));
			g.setColor(new Color(80,60,10));
			g.fillRect(x, y-29, w, 65);
			g.setColor(am.getColor(quality));
			g.drawString(name, x+5, y-16);
			if(item instanceof Armor)g.drawString("Armor "+stat, x+5, y);
			else g.drawString("Damage "+stat, x+5, y);
			g.drawString("Level "+level, x+5, y+16);
		}
		y+=18;
		x+=3;
		getDialogFont();
		MathTool mt = new MathTool();
		int d = mt.getDigits(v);
		int coins = 0;
		int w2 = 0;
		if(d>5){
			coins =3;
			w2 = 85+((d-5)*10);
		}else if(d==3||d==4||d==5){
			//d=5;
			coins = 2;
			w2 = 75;
		}else if(d==1 || d==2){
			//d=2;
			coins = 1;
			w2= 34;
		}
		if(w==0){
			g.setColor(new Color(80,60,10));
			g.fillRect(x, y, w2, 18);
		}
		
		int z = v/100000;
		int s = (v%100000)/100;
		int c = v%100;
		if(coins ==3){
			g.setColor(new Color(255,230,0));
			g.drawString(String.format("%s",z), x+4, y+14);
			g.drawImage(h.coinsImgs[2], x+2+((d-5)*10), y+4, null);
			x+= 10+((d-5)*10);
			coins--;
		}
		if(coins == 2){
			g.setColor(new Color(191,210,209));
			g.drawString(String.format("%s",s), x+5, y+14);
			d = mt.getDigits(s);
			g.drawImage(h.coinsImgs[1], x+6+(8*d), y+4, null);
			x+= 13+(d*9);
		}
		g.setColor(new Color(196,121,25));
		g.drawString(String.format("%s",c), x+4, y+14);
		d = mt.getDigits(c);
		g.drawImage(h.coinsImgs[0], x+5+(8*d), y+4, null);
		
	}
	
	public void drawItem(int lx,int ly,Item i){
		if(i instanceof MobScrap){
			MobScrap ms = (MobScrap) i;
			g.drawImage(h.itemIImgs[ms.getType()],lx,ly,null);
		}else if(i instanceof Potion){			
			g.drawImage(h.potionImg,lx,ly,null);
		}else if(i instanceof Equipable){	
			Equipable eq = (Equipable) i;
			g.drawImage(getArmorImg(eq.getType(),eq.getSlot()),lx,ly,null);
		}
	}
	
	private void drawCount(int x,int y,int count){	
		//g.fillRect(x+21,y+8,5,12);
		//g.setColor(Color.YELLOW);
		int j = count%10;
		int d = (int) Math.floor(count/10);
		if(d!=0){
			g.drawString(String.format("%s",d), x+21,y+10);
		}
		g.drawString(String.format("%s",j), x+21,y+20);
	}
	
	public Image getArmorImg(int type, int slot){
		type--;
		slot--;
		return h.armorImgs[type][slot];
	}

	public void getDialogFont(){
		g.setColor(new Color(255,230,0));
		g.setFont(new Font(Font.MONOSPACED,Font.BOLD,14));
	}
	
	public Image mirrorImg(Image img){
		return transfImage(img,-1,1);
	}
	
	private Image transfImage(Image image, int x, int y) {

        // set up the transform
        AffineTransform transform = new AffineTransform();
        transform.scale(x, y);
        transform.translate(
            (x-1) * image.getWidth(null) / 2,
            (y-1) * image.getHeight(null) / 2);

        // create a transparent (not translucent) image
        Image newImage = w.gc.createCompatibleImage(
            image.getWidth(null),
            image.getHeight(null),
            Transparency.BITMASK);

        // draw the transformed image
        Graphics2D g = (Graphics2D)newImage.getGraphics();
        g.drawImage(image, transform, null);
        g.dispose();

        return newImage;
    }
	
	public Image scaleImage(Image image, double x, double y) {

        // set up the transform
        AffineTransform transform = new AffineTransform();
        transform.scale(x, y);

        // create a transparent (not translucent) image
        Image newImage = w.gc.createCompatibleImage(
            (int)(image.getWidth(null)*x),
            (int)(image.getHeight(null)*y),
            Transparency.BITMASK);

        // draw the transformed image
        Graphics2D g = (Graphics2D)newImage.getGraphics();
        g.drawImage(image, transform, null);
        g.dispose();

        return newImage;
    }
	
	/**
	 * Rotate in degrees
	 */
	public Image rotateImage(Image image, int theta) {
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
	
	//day/night cycle
			/*
			g.setColor(new Color(0,0,30,120));//120 without overlays
			g.fillRect(0, 0, 495, 500);
			*/
			/*
				g.setColor(new Color(200,50,0,50));
				g.fillRect(0, 0, 495, 500);
			 */
			/*
			sunset++;
			int s = sunset;
			sunset = s/2;
			if(sunset==800){
				sunset=0;
				s=0;
			}
			if(sunset>50&&sunset<=600){
				g.setColor(new Color(0,0,30,(sunset-50)/4));
				g.fillRect(0, 0, 495, 500);
			}else if(sunset>600){
				g.setColor(new Color(0,0,30,(600-50)/4));
				g.fillRect(0, 0, 495, 500);
			}
			if(sunset<300){
				g.setColor(new Color(200,50,0,sunset/6));
				g.fillRect(0, 0, 495, 500);
			}else if(sunset<=500){
				g.setColor(new Color(200,50,0, (50*300)/(sunset) ));
				g.fillRect(0, 0, 495, 500);
			}else if(sunset<=1000){
				g.setColor(new Color(200,50,0, 30*100/(sunset-400) ));
				g.fillRect(0, 0, 495, 500);
			}
			sunset=s;
			*/
			/*
			Circle overlayCircle = new Circle(new Point(256,260),150);
			Image overlay = w.gc.createCompatibleImage(500,500,Transparency.TRANSLUCENT);
			Graphics2D og = (Graphics2D) overlay.getGraphics();
			og.setColor(new Color(20,50,20,70));
			og.fillRect(0, 0, 495, 500);
			og.setBackground(new Color(0,0,0,0));
			overlayCircle.clearCircle(og);
			og.dispose();
			g.drawImage(overlay, 0, 0, null);
			
			overlayCircle = new Circle(new Point(256,260),90);
			overlay = w.gc.createCompatibleImage(500,500,Transparency.TRANSLUCENT);
			og = (Graphics2D) overlay.getGraphics();
			og.setColor(new Color(80,40,40,60));
			og.fillRect(0, 0, 495, 500);
			og.setBackground(new Color(0,0,0,0));
			overlayCircle.clearCircle(og);
			og.dispose();
			g.drawImage(overlay, 0, 0, null);
			
			overlayCircle = new Circle(new Point(256,260),200);
			overlay = w.gc.createCompatibleImage(500,500,Transparency.TRANSLUCENT);
			og = (Graphics2D) overlay.getGraphics();
			og.setColor(new Color(20,50,20,50));
			og.fillRect(0, 0, 495, 500);
			og.setBackground(new Color(0,0,0,0));
			overlayCircle.clearCircle(og);
			og.dispose();
			g.drawImage(overlay, 0, 0, null);
			*/
	
}

