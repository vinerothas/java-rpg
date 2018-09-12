package graphics;

import game.Constants;
import items.Armor;
import items.ArmorNaming;
import items.Equipable;
import items.Item;
import items.MobScrap;
import items.Potion;
import items.Weapon;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class DrawInventory {

	public static void drawItem(Graphics2D g,ImageHolder h,int lx,int ly,Item i){
		if(i instanceof MobScrap){
			MobScrap ms = (MobScrap) i;
			g.drawImage(h.itemIImgs[ms.getType()],lx,ly,null);
		}else if(i instanceof Potion){	
			g.drawImage(h.potionImg,lx,ly,null);
		}else if(i instanceof Equipable){	
			Equipable eq = (Equipable) i;
			g.drawImage(eq.getImage(h),lx,ly,null);
		}
	}
	
	public static void drawValue(Graphics2D g, ImageHolder h, Item item, int x,int y){
		float font13ratio = (float) 1.57;
		int fontSize = 13;
		int border = 5;
		g.setFont(new Font("Monospaced",Font.PLAIN,fontSize));
		
		int v = item.getValue();
		int coinsLength = String.valueOf(v).length();
		int imageSpacing = 12;
		int coins = 0;
		int w2 = 0;
		if(coinsLength>5){
			coins =3;
		}else if(coinsLength==3||coinsLength==4||coinsLength==5){
			coins = 2;
		}else if(coinsLength==1 || coinsLength==2){
			coins = 1;
		}
		w2 = (int) (imageSpacing*coins+(String.valueOf(v).length()*fontSize/font13ratio))+border*2;
		
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
			//System.out.println(name+" "+m+" "+d+" "+d.length());
			if(d.length()>m)m=d.length();
			w = (int) ((fontSize*m/font13ratio)+border);
			if(w2>w)w=w2;
			//if(w<70)w=80;
			//g.setColor(new Color(80,60,10));
			g.setColor(new Color(40,20,5,200));
			//System.out.println(w);
			g.fillRect(x, y+7, w, 64);
			g.setColor(am.getColor(quality));
			g.drawString(name, x+border, y+19);
			if(item instanceof Armor)g.drawString("Armor "+stat, x+border, y+35);
			else g.drawString("Damage "+stat, x+border, y+35);
			g.drawString("Level "+level, x+border, y+51);
			y+=35;
		}else if(item instanceof MobScrap){
			//System.out.println(((MobScrap)item).getName());
			MobScrap ms = (MobScrap) item;
			String name = ms.getName();
			g.setColor(new Color(40,20,5,200));
			w = (int) ((fontSize*name.length()/font13ratio)+border);
			if(w2>w)w=w2;
			g.fillRect(x, y+7, w, 33);

			g.setColor(new ArmorNaming().getColor(Constants.ITEM_QUALITY_POOR));
			g.drawString(name, x+border, y+19);
			y+=4;
		}else{
			g.setColor(new Color(40,20,5,200));			
			g.fillRect(x, y+7, w2, 33);
		}
		y+=18;
		
		int yMargin = 12;
		int gold = v/100000;
		int silver = (v%100000)/100;
		int copper = v%100;
		x+=border;
		if(coins ==3){
			int goldLength = String.valueOf(gold).length();
			g.setColor(new Color(255,230,0));
			g.drawString(String.format("%s",gold), x, y+yMargin);
			int shoveX = (int) (goldLength*fontSize/font13ratio)+1;
			g.drawImage(h.coinsImgs[2], x+shoveX, y+4, null);
			x+= shoveX+imageSpacing;
			coins--;
		}
		if(coins == 2){
			int silverLength = String.valueOf(silver).length();
			g.setColor(new Color(191,210,209));
			g.drawString(String.format("%s",silver), x, y+yMargin);
			int shoveX = (int) (silverLength*fontSize/font13ratio)+1;
			g.drawImage(h.coinsImgs[1], x+shoveX, y+4, null);
			x+= shoveX+imageSpacing;
		}
		g.setColor(new Color(196,121,25));
		g.drawString(String.format("%s",copper), x, y+yMargin);
		int copperLength = String.valueOf(copper).length();
		g.drawImage(h.coinsImgs[0], (int) (x+(copperLength*fontSize/font13ratio))+1, y+4, null);
		
	}
	
	public static void drawComparison(Graphics2D g, ImageHolder h, Item item, int x,int y){
		float font13ratio = (float) 1.57;
		int fontSize = 13;
		int border = 5;
		g.setFont(new Font("Monospaced",Font.PLAIN,fontSize));
		
		int v = item.getValue();
		int coinsLength = String.valueOf(v).length();
		int imageSpacing = 12;
		int coins = 0;
		int w2 = 0;
		if(coinsLength>5){
			coins =3;
		}else if(coinsLength==3||coinsLength==4||coinsLength==5){
			coins = 2;
		}else if(coinsLength==1 || coinsLength==2){
			coins = 1;
		}
		w2 = (int) (imageSpacing*coins+(String.valueOf(v).length()*fontSize/font13ratio))+border*2;
		
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
			//System.out.println(name+" "+m+" "+d+" "+d.length());
			if(d.length()>m)m=d.length();
			w = (int) ((fontSize*m/font13ratio)+border);
			if(w2>w)w=w2;
			//if(w<70)w=80;
			//g.setColor(new Color(80,60,10));
			g.setColor(new Color(40,20,5,200));
			//System.out.println(w);
			g.fillRect(x, y+7, w, 64);
			g.setColor(am.getColor(quality));
			g.drawString(name, x+border, y+19);
			if(item instanceof Armor)g.drawString("Armor "+stat, x+border, y+35);
			else g.drawString("Damage "+stat, x+border, y+35);
			g.drawString("Level "+level, x+border, y+51);
			y+=35;
		}else if(item instanceof MobScrap){
			//System.out.println(((MobScrap)item).getName());
			MobScrap ms = (MobScrap) item;
			String name = ms.getName();
			g.setColor(new Color(40,20,5,200));
			w = (int) ((fontSize*name.length()/font13ratio)+border);
			if(w2>w)w=w2;
			g.fillRect(x, y+7, w, 33);

			g.setColor(new ArmorNaming().getColor(Constants.ITEM_QUALITY_POOR));
			g.drawString(name, x+border, y+19);
			y+=4;
		}else{
			g.setColor(new Color(40,20,5,200));			
			g.fillRect(x, y+7, w2, 33);
		}
		g.setColor(new Color(40,20,5,200));
		g.fillRect(x, y-43, w, 15);
		g.setColor(Color.white);
		g.drawString("Equipped:", x+2, y-31);
		
		y+=18;
		
		int yMargin = 12;
		int gold = v/100000;
		int silver = (v%100000)/100;
		int copper = v%100;
		x+=border;
		if(coins ==3){
			int goldLength = String.valueOf(gold).length();
			g.setColor(new Color(255,230,0));
			g.drawString(String.format("%s",gold), x, y+yMargin);
			int shoveX = (int) (goldLength*fontSize/font13ratio)+1;
			g.drawImage(h.coinsImgs[2], x+shoveX, y+4, null);
			x+= shoveX+imageSpacing;
			coins--;
		}
		if(coins == 2){
			int silverLength = String.valueOf(silver).length();
			g.setColor(new Color(191,210,209));
			g.drawString(String.format("%s",silver), x, y+yMargin);
			int shoveX = (int) (silverLength*fontSize/font13ratio)+1;
			g.drawImage(h.coinsImgs[1], x+shoveX, y+4, null);
			x+= shoveX+imageSpacing;
		}
		g.setColor(new Color(196,121,25));
		g.drawString(String.format("%s",copper), x, y+yMargin);
		int copperLength = String.valueOf(copper).length();
		g.drawImage(h.coinsImgs[0], (int) (x+(copperLength*fontSize/font13ratio))+1, y+4, null);
		
	}
}
