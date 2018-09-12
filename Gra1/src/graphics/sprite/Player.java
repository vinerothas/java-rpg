package graphics.sprite;

import game.Functions;
import graphics.DrawGame;

import items.Armor;
import items.Equipable;
import items.EquippedItems;
import items.Item;
import items.Weapon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Player extends Character {

	private float v = (float) 0.18;
	private int stanceX;
	private int stanceY;
	private final int height = 37;
	private final int width = 28;
	private int hp = 100;
	private int mhp = 100;
	private int lastDir = 0;
	private int exp = 0;
	private int level =1;
	private long money = 0;
	private int armor = 0;
	private int power = 0;
	public EquippedItems equippedItems = new EquippedItems();
	private Item[][] inventory = new Item[8][5];
	
	public Player(){
		setHeight(height);
		setWidth(width);
		setV(v);
		setBaseX(3);
		setBaseX2(18);
		updatePower();
	}
	
	/*
	 * Debugging purposes only
	*/
	/*
	public float getV() {
		//return super.getV();
		//return (float)0.2;
		return (float)1.5;
	}
	*/
	
	public int getStanceX() {
		return stanceX;
	}
	public void setStanceX(int sx) {
		stanceX = sx;
	}
	public int getStanceY() {
		return stanceY;
	}
	public void setStanceY(int sy) {
		stanceY = sy;
	}
	
	public void stop(){
		stanceX = 0;
		stanceY = 0;
	}

	public int getLife() {
		return hp;
	}
	public void setLife(int hp) {
		this.hp = hp;
	}

	/*
	 *  0 - right
	 *  1 - left
	 *  2 - up
	 *  3 - down
	 */
	public int getLastDir() {
		return lastDir;
	}

	public void setLastDir(int dir) {
		lastDir = dir;
	}

	public int getExp() {
		return exp;
	}
	
	/**
	 * exp -1 to reset
	 */
	public void setExp(int exp) {
		if(exp == -1){
			level = 0;
			this.exp = 0;
			return;
		}
		int maxE = new Functions().getLevelExp(level);
		if(exp>= maxE){
			int r = exp-maxE;
			setLevel(level+1);
			this.exp = r;
			setExp(r);
		}else{
			this.exp = exp;
		}
	}
	
	public void setLevel(int level){
		this.level = level;
		Functions f = new Functions();
		mhp = f.getPlayerHP(level);	
		hp = mhp;
		updatePower();
	}
	
	public void updatePower(){
		Functions f = new Functions();
		double unarmed = 0.25;
		int weaponP = 0;
		if(equippedItems.getWeapon()!= null){
			int q = equippedItems.getWeapon().getQuality();
			int l = equippedItems.getWeapon().getLevel();
			double weapon = f.getPercentQuality(q);
			weaponP = (int) (weapon*f.getPlayerAttack(l));
			System.out.println(weapon+" "+weaponP);
		}
		//System.out.println(weaponP);
		int unarmedP = (int) (unarmed*f.getPlayerAttack(level));
		//System.out.println(unarmedP);
		if(unarmedP>weaponP)power = unarmedP;
		else power = weaponP;
	}

	public int getLevel() {
		return level;
	}

	public long getMoney(){
		return money;
	}
	public void addMoney(long m){
		money+=m;
	}

	public int getMhp() {
		return mhp;
	}

	public int getArmor() {
		return armor;
	}
	
	/**@param a
	 * @return removed armor*/
	public Armor addArmor(Armor a){
		Armor removed = equippedItems.removeArmor(a);
		if(removed!=null)armor-=removed.getArmor();
		armor+=a.getArmor();
		equippedItems.loadEquipement(a);
		return removed;
	}
	
	/**@param w
	 * @return weapon armor*/
	public Weapon addWeapon(Weapon w){
		Weapon removed = equippedItems.removeWeapon();		
		equippedItems.addWeapon(w);
		updatePower();
		return removed;
	}
	
	public void setArmor(int a){
		armor = a;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}
	
	public Item[][] getInventory(){
		return inventory;
	}
	
	public void draw(DrawGame d, int x, int y){
		Image img = null;
		if(stanceX == 1){
			img = d.h.playerAnim[0].getImage(d.elapsedTime);
			setLastDir(0);
		}else if(stanceX == 2){
			img=d.h.playerAnim[1].getImage(d.elapsedTime);
			setLastDir(1);
		}else if(stanceY == 1){
			img=d.h.playerAnim[2].getImage(d.elapsedTime);
			setLastDir(2);
		}else if(stanceY == 2){
			img=d.h.playerAnim[3].getImage(d.elapsedTime);
			setLastDir(3);
		}else{
			img= d.h.playerAnim[lastDir].getIdleImage();
		}
		
		if(getSwim()){
			BufferedImage copy = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.BITMASK);
			Graphics2D g = copy.createGraphics();
			g.drawImage(img, 0, 0, null);
			g.setBackground(new Color(0,0,0,0));
			int factor = 10;
			int depth = (getHeight()-factor)/2;
			int lower = (getHeight()+factor)/2;
			g.clearRect(0, lower, getWidth(), getHeight());
			g.dispose();
			d.g.drawImage(copy, x, y+depth, null);
		}else{
			d.g.drawImage(img,x, y,null);
		}
	}
	
}
