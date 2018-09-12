package graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.net.URL;

import javax.swing.ImageIcon;

import graphics.sprite.Player;
import items.Equipable;

public class UpdatePlayerAnim {

	private String[] types;
	
	public void loadPlayerAnim(Player player, ImageHolder h, Window w){
		
		types = new String[6];
		//0head 1shoulders 2weapon 3chest 4legs 5boots
		int s = 0;
		try{
			if(player.equippedItems.getHead() == null)types[s] = "def";
			else types[s] = String.format("%s",((Equipable) player.equippedItems.getHead()).getType());
		}catch(Exception ex){types[s] = "def";}	
		s++;
		try{
			if(player.equippedItems.getShoulders() == null)types[s] = "def";
			else types[s] = String.format("%s",((Equipable) player.equippedItems.getShoulders()).getType());
		}catch(Exception ex){types[s] = "def";}	
		s++;
		try{
			if(player.equippedItems.getWeapon() == null)types[s] = "def";
			else types[s] = String.format("%s",((Equipable) player.equippedItems.getWeapon()).getType());
		}catch(Exception ex){types[s] = "def";}	
		s++;
		try{
			if(player.equippedItems.getChest() == null)types[s] = "def";
			else types[s] = String.format("%s",((Equipable) player.equippedItems.getChest()).getType());
		}catch(Exception ex){types[s] = "def";}	
		s++;
		try{
			if(player.equippedItems.getLegs() == null)types[s] = "def";
			else types[s] = String.format("%s",((Equipable) player.equippedItems.getLegs()).getType());
		}catch(Exception ex){types[s] = "def";}	
		s++;
		try{
			if(player.equippedItems.getFeet() == null)types[s] = "def";
			else types[s] = String.format("%s",((Equipable) player.equippedItems.getFeet()).getType());
		}catch(Exception ex){types[s] = "def";}	
		
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
		
		h.playerAnim = new Animation[]{playerRAnim, playerLAnim,playerUAnim,playerDAnim};
	}
	
	private Image[] loadPlayerD(Window w){
		Image D1B = getImage("player/D/1/B/"+types[5]);
		Image D1C = getImage("player/D/1/C/"+types[3]);
		Image D1H = getImage("player/D/1/H/"+types[0]);
		Image D1L = getImage("player/D/1/L/"+types[4]);
		Image D1S = getImage("player/D/1/S/"+types[1]);
		Image D1W = getImage("player/D/1/W/"+types[2]);
		Image D2L = getImage("player/D/2/L/"+types[4]);
		Image D2B = getImage("player/D/2/B/"+types[5]);
		Image D3L = getImage("player/D/3/L/"+types[4]);
		Image D3B = getImage("player/D/3/B/"+types[5]);
		
		Image D = playerD(new Image[]{D1B,D1C,D1H,D1L,D1S,D1W},w);       
        Image D2 = playerD(new Image[]{D2B,D1C,D1H,D2L,D1S,D1W},w);
        Image D3 = playerD(new Image[]{D3B,D1C,D1H,D3L,D1S,D1W},w);
        return new Image[]{D,D2,D,D3};
	}
	
	//wszystko minus 1 1 - boots 2 - chest  3 - helmet 4 - leggings  5 - shoulders 6 - gloves 7 - weapon
	private Image playerD(Image[] parts,Window w){
		Image D = w.gc.createCompatibleImage(29,37,Transparency.BITMASK);
		Graphics2D g = (Graphics2D)D.getGraphics();
		g.drawImage(parts[5], 0,12, null);
        g.drawImage(parts[1], 6,9, null);
        g.drawImage(parts[2],9,0,null);
        g.drawImage(parts[3], 9,22, null);
        g.drawImage(parts[4], 5,8, null);     
        g.drawImage(parts[0], 8,32, null);  
        g.dispose();
        return D;
	}
	
	private Image[] loadPlayerL(Window w){
		Image L1B = getImage("player/L/1/B/"+types[5]);
		Image L1C = getImage("player/L/1/C/"+types[3]);
		Image L1H = getImage("player/L/1/H/"+types[0]);
		Image L1L = getImage("player/L/1/L/"+types[4]);
		Image L1S = getImage("player/L/1/S/"+types[1]);
		Image L1W = getImage("player/L/1/W/"+types[2]);
		Image L2L = getImage("player/L/2/L/"+types[4]);
		Image L2B = getImage("player/L/2/B/"+types[5]);
		
		Image L = playerL(new Image[]{L1B,L1C,L1H,L1L,L1S,L1W},w);       
        Image L2 = playerL(new Image[]{L2B,L1C,L1H,L2L,L1S,L1W},w);
        return new Image[]{L,L2};
	}
	
	private Image playerL(Image[] parts,Window w){
		Image D = w.gc.createCompatibleImage(29,37,Transparency.BITMASK);
		Graphics2D g = (Graphics2D)D.getGraphics();
        g.drawImage(parts[5], 0,12, null);
        g.drawImage(parts[1], 10,9, null);
        g.drawImage(parts[2], 13,0,null);
        g.drawImage(parts[3], 11,22, null);
        g.drawImage(parts[4], 10,9, null);
        g.drawImage(parts[0], 8,32, null);
        g.dispose();
        return D;
	}
	
	private Image[] loadPlayerR(Window w){
		Image R1B = getImage("player/R/1/B/"+types[5]);
		Image R1C = getImage("player/R/1/C/"+types[3]);
		Image R1H = getImage("player/R/1/H/"+types[0]);
		Image R1L = getImage("player/R/1/L/"+types[4]);
		Image R1S = getImage("player/R/1/S/"+types[1]);
		Image R1W = getImage("player/R/1/W/"+types[2]);
		Image R2L = getImage("player/R/2/L/"+types[4]);
		Image R2B = getImage("player/R/2/B/"+types[5]);
		
		Image R = playerR(new Image[]{R1B,R1C,R1H,R1L,R1S,R1W},w);       
        Image R2 = playerR(new Image[]{R2B,R1C,R1H,R2L,R1S,R1W},w);
        return new Image[]{R,R2};
	}
	
	private Image playerR(Image[] parts,Window w){
		Image D = w.gc.createCompatibleImage(29,37,Transparency.BITMASK);
		Graphics2D g = (Graphics2D)D.getGraphics();
        g.drawImage(parts[1], 1,9, null);
        g.drawImage(parts[2], 4,0,null);
        g.drawImage(parts[3], 3,22, null);
        g.drawImage(parts[4], 0,9, null);
        g.drawImage(parts[5], 2,12, null);
        g.drawImage(parts[0], 4,32, null);
        g.dispose();
        return D;
	}
	
	private Image[] loadPlayerU(Window w){
		Image U1B = getImage("player/U/1/B/"+types[5]);
		Image U1C = getImage("player/U/1/C/"+types[3]);
		Image U1H = getImage("player/U/1/H/"+types[0]);
		Image U1L = getImage("player/U/1/L/"+types[4]);
		Image U1S = getImage("player/U/1/S/"+types[1]);
		Image U1W = getImage("player/U/1/W/"+types[2]);
		Image U2L = getImage("player/U/2/L/"+types[4]);
		Image U2B = getImage("player/U/2/B/"+types[5]);
		Image U2C = getImage("player/U/1/C/"+types[3]);
		Image U3L = getImage("player/U/3/L/"+types[4]);
		Image U3B = getImage("player/U/3/B/"+types[5]);
		
		Image U = playerU(new Image[] {U1B,U1C,U1H,U1L,U1S,U1W},w);       
        Image U2 = playerU(new Image[]{U2B,U2C,U1H,U2L,U1S,U1W},w);
        Image U3 = playerU(new Image[]{U3B,U1C,U1H,U3L,U1S,U1W},w);
        return new Image[]{U,U2,U,U3};
	}
	
	private Image playerU(Image[] parts,Window w){
		Image D = w.gc.createCompatibleImage(29,37,Transparency.BITMASK);
		Graphics2D g = (Graphics2D)D.getGraphics();
        g.drawImage(parts[5], 21,12, null);
        g.drawImage(parts[1], 5,8, null);
        g.drawImage(parts[2], 8,0,null);
        g.drawImage(parts[3], 7,22, null);
        g.drawImage(parts[4], 4,8, null);
        g.drawImage(parts[0], 7,26, null);
        g.dispose();
        return D;
	}
	
	private Image getImage(String path){
		URL is = this.getClass().getResource("img/"+path+".PNG");
		try{
		return new ImageIcon(is).getImage(); 
		}catch(Exception ex){
			is = this.getClass().getResource("img/"+path+".png");
			System.out.println(path);
			//return new ImageIcon(is).getImage(); 
			try{
				return new ImageIcon(is).getImage(); 
				}catch(Exception ex2){
					return null; 
				}
		}
	}
	
}
