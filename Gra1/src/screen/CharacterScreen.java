package screen;

import input.KeyAction;
import input.KeyboardConfig;
import items.Armor;
import items.ArmorNaming;
import items.Item;
import items.MobScrap;
import items.Weapon;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;

import screen.characterContainer.*;
import game.Constants;
import game.ScreenManager;
import graphics.DrawInventory;
import graphics.ImageHolder;
import graphics.sprite.Player;

public class CharacterScreen extends Screen{
	
	GameScreen gs;
	private ArmorContainer ac;
	private InventoryContainer ic;
	private StatsContainer sc;

	public CharacterScreen(ScreenManager sm, GameScreen gs) {
		super(sm, findZoom(sm.windowWidth,sm.windowHeight));
		this.gs = gs;
		
		ac = new ArmorContainer(getDrawWidth(),gs.r.getPlayer().equippedItems);
		ic = new InventoryContainer(getDrawWidth(),gs.r.getPlayer());
		sc = new StatsContainer(screenMid.x,ArmorContainer.height,gs.r.getPlayer());
	}
	
	private static float findZoom(int width, int height){
		if(width>=800&&height>=700)return (float)3.0;
		else if(width>=600&&height>=500)return (float)2.0;
		return (float) 1.0;
	}

	@Override
	public void loop(long elapsedTime) {
		checkInput();
		
	}

	@Override
	public void draw(Graphics2D g) {
		int tileW = gs.d.h.tileUIImg.getWidth(null);
		int tileH = gs.d.h.tileUIImg.getWidth(null);
		int tilesX = (getDrawWidth()/tileW)+1;
		int tilesY = (getDrawHeight()/tileH)+1;
		for(int x = 0;x<tilesX;x++){
			for(int y = 0;y<tilesY;y++){
				g.drawImage(gs.d.h.tileUIImg, tileW*x, tileH*y, null);
			}
		}

		g.setColor(new Color(255,220,0));
		ac.draw(g, gs.d.h);
		ic.draw(g, gs.d.h);
		sc.draw(g, gs.d.h);
		
		if(ic.hoverPos!=null){
			Item item = gs.r.getPlayer().getInventory()[ic.hoverPos.y][ic.hoverPos.x];
			if(item!=null){
				DrawInventory.drawValue(g,gs.d.h,item,25,125);
			}
		}else if(ac.hoverItem!=null){
			DrawInventory.drawValue(g,gs.d.h,ac.hoverItem,25,125);
		}
	}

	@Override
	protected void checkInput() {
		Point m = sm.dui.mouseClicked;
		if(m!=null){
			if(sm.dui.leftClick==false)ic.checkInput(m);
			else ac.checkInput(m);
		}
		sm.dui.mouseClicked=null;
		
		m = sm.dui.mouseMoved;
		if(m!=null){
			ic.handleHover(m);
			ac.handleHover(m);
		}
		
		KeyAction[] keyActions = sm.dui.getKeyActions();
		KeyboardConfig kc = sm.kc;
		for(KeyAction a : keyActions){
			if(a==null)break;
			int k = a.getKeyCode();
			if(a.isPressed()){
				if(k==kc.CHARACTER||k == kc.MENU){
					sm.updatePlayerAnim();
					sm.setScreen(gs);
				}
			}
		}
		sm.dui.clearInput();
		
	}

}
