package screen.characterContainer;

import game.Functions;
import graphics.ImageHolder;
import graphics.sprite.Player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;

public class StatsContainer extends CharacterContainer{

	public static final int width = 150;
	public static final int height = 80;
	private Player player;
	
	/**
	 * @param x x-position of InventoryContainer
	 * @param y height of ArmorContainer
	 */
	public StatsContainer(int x, int y, Player player) {
		super(x-width, y, width, height);
		this.player = player;
	}

	@Override
	public void draw(Graphics2D g, ImageHolder h) {
		//g.drawRect(x, y, width, height);

		g.setFont(new Font(Font.MONOSPACED,Font.PLAIN,11));
		g.setColor(new Color(230,0,0));
		int ys = y+10;
		int xs = x+5;
		g.drawString("Health: "+player.getLife(), xs, ys);
		ys+=16;
		g.drawString("Max Health: "+player.getMhp(), xs, ys);
		g.setColor(new Color(130,200,0));
		ys+=16;
		g.drawString("Armor: "+player.getArmor(), xs, ys);
		double defended = 100*((double)player.getArmor()/((double)new Functions().getPlayerArmor(player.getLevel())*2));
		ys+=16;
		g.drawString("Defended: "+(int)defended+"%", xs, ys);
		g.setColor(Color.yellow);
		ys+=16;
		g.drawString("Attack: "+player.getPower(), xs, ys);
		
		int ex = x+20;
		int ey = ys+8;
		int level = player.getLevel();
		int exp = (int) Math.floor(((100*player.getExp())/new Functions().getLevelExp(level)));
		String e = String.format("%s"+"%%", exp);
		g.setColor(new Color(80,255,0));
		int w = (exp*78)/100;
		g.fillRect(ex+35, ey+14, w, 12);
		g.drawImage(h.expBarImg, ex, ey, null);		
		g.setColor(new Color(255,230,0));
		g.setFont(new Font("Monospaced",Font.PLAIN,15));
		g.drawString(e, ex+5, ey+24);
		g.setFont(new Font("Monospaced",Font.PLAIN,12));
		String l = String.format("Level %s",level);
		g.drawString(l,ex+36,ey+12);
		
	}

	@Override
	public void handleInput(Point m) {
		// TODO Auto-generated method stub
		
	}

}
