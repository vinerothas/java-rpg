package items;

import java.awt.Color;

public class ArmorNaming {
	
	public String getName(int slot,int type, int quality){
		if(slot==7){
			String q = getQuality(quality);
			String t = getWeaponType(type);
			return q+t;
		}else{
			String q = getQuality(quality);
			String t = getType(type);
			String s = getSlot(slot);
			return q+t+s;
		}
	}
	
	private String getWeaponType(int t){
		if(t==1)return "Dagger";
		else return "ArmorNaming Error";
	}
	
	private String getQuality(int q){
		if(q==2)return "";
		else if(q==3)return "Good ";
		else if(q==4)return "Unusual ";
		else if(q==5)return "Rare ";
		else if(q==6)return "Mighty ";
		else if(q==7)return "Masterpiece ";
		else return "Poor ";
	}
	
	private String getType(int t){
		if(t==1)return "Leather ";
		else return "Wrong ";		
	}
	
	private String getSlot(int s){
		 if(s==1)return "Boots";
		 else if(s==2)return "Chest";
		 else if(s==3)return "Helmet";
		 else if(s==4)return "Leggings";
		 else if(s==5)return "Shoulders";
		 else if(s==6)return "Gloves";
		 else return "Error";
	}
	
	public Color getColor(int q){
		if(q==2)return new Color(250,250,250);
		else if(q==3)return new Color(30,235,230);
		else if(q==4)return new Color(60,160,50);
		else if(q==5)return new Color(210,185,25);
		else if(q==6)return new Color(195,80,220);
		else if(q==7)return new Color(170,0,0);
		else return new Color(120,120,120);
	}
}
