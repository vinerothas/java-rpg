package game;

import java.util.Random;

public class Functions {
 
	public int getLevelExp(int level){
		return (int)(2.8*Math.pow(level, 3)+100*Math.pow(level, 2)-50*level+150);
	}
	
	public int getEnemyPerLevel(int level){
		return (int)(0.8*Math.pow(level,2)+level*15+15);
	}
	
	public int getEnemyExp(int el){
		return getLevelExp(el)/getEnemyPerLevel(el);
	}
	public int getEnemyPower(int l){
		return (int) ((l*l)* 0.025 +l* 3.3 +12);
	}
	
	public int getPlayerHP(int l){
		return (int) ((l*l)* 0.02 +l* 3.5 +97);
	}
	
	public int getPlayerAttack(int l){
		return (int) ((l*l)* 0.015 +l* 2.5 +18);
	}
	
	public int getEnemyHealth(int l) {
		return (int) ((l*l)* 0.03 +l* 4.5 +36);
	}
	
	public int getPlayerArmor(int l){
		return (int) ((l*l)*0.15+l*7.5+30);
	}
	
	public int getEnemyValue(int l){
		return (int) ((l*l)* 0.1 +l* 2.4 - 1);
	}
	
	public double getPercentQuality(int q){
		if(q==Constants.ITEM_QUALITY_NORMAL)return 0.65;
		else if(q==Constants.ITEM_QUALITY_GOOD)return 0.8;
		else if(q==Constants.ITEM_QUALITY_UNUSUAL)return 1.0;
		else if(q==Constants.ITEM_QUALITY_RARE)return 1.2;
		else if(q==Constants.ITEM_QUALITY_MIGHTY)return 1.4;
		else if(q==Constants.ITEM_QUALITY_MASTERPIECE)return 2.0;
		else return  0.5;
	}
	
	private double getPercentSlot(int s){
		if(s==Constants.ITEM_SLOT_CHEST)return 0.34;
		else if(s==Constants.ITEM_SLOT_LEGS||s==Constants.ITEM_SLOT_HEAD)return 0.18;
		else if(s==Constants.ITEM_SLOT_SHOULDERS)return 0.14;
		else if(s==Constants.ITEM_SLOT_WEAPON)return 0.30;
		else return 0.09;
	}
	
	public int getArmor(int l, int q, int s){
		double armor = getPlayerArmor(l);
		double quality = getPercentQuality(q);
		double slot = getPercentSlot(s);
		int a = (int) (quality*armor*slot);
		if(a==0)a=1;
		return a;
	}
	
	public int getArmorValue(int l, int q, int s){
		double quality = 1.8*getPercentQuality(q);
		double slot = 0.7*getPercentSlot(s);
		double enemy = 100*getEnemyValue(l);
		int value = (int) (quality*slot*enemy);
		//System.out.println(quality+" "+slot+" "+enemy+" "+value);
		return value;
	}
	
	/**
	 * @return Returns a random quality of an equipable 
	 * item based on qualities' probability
	 */
	public static int getRandomEquipableQuality(){
		Random rand = new Random();
		int roll = rand.nextInt(100)+1;
		if(roll==100)return Constants.ITEM_QUALITY_MASTERPIECE;//1% 
		else if(roll<100&&roll>96)return Constants.ITEM_QUALITY_MIGHTY;//3%
		else if(roll<97&&roll>89)return Constants.ITEM_QUALITY_RARE;//7%
		else if(roll<90&&roll>75)return Constants.ITEM_QUALITY_UNUSUAL;//14%
		else if(roll<76&&roll>50)return Constants.ITEM_QUALITY_GOOD;//25%
		else if(roll<51&&roll>15)return Constants.ITEM_QUALITY_NORMAL;// 35%
		else return Constants.ITEM_QUALITY_POOR; // 15%
	}

}
