package graphics;

import game.Constants;
import game.Functions;
import items.Equipable;

import java.awt.Color;

public class DrawUI {
	
	private DrawOld d;
	
	public void drawUI(DrawOld d){
		
		this.d = d;
		
		if(Math.abs(d.r.getPlayer().getX())>3500||Math.abs(d.r.getPlayer().getY())>3500){
			if(d.dui.isInBuilding()==0)drawBorder();
		}
		
		for(int y = 7;y<Constants.WINDOW_REGION_VIEW_HEIGHT/20;y++){
			for(int x = 0;x<6;x++){
				d.g.drawImage(d.h.tileUIImg, (x*20)+Constants.WINDOW_REGION_VIEW_WIDTH, y*20, null);
			}	
			d.g.drawImage(d.h.borderUIImgs[0],Constants.WINDOW_REGION_VIEW_WIDTH-5,y*20,null);
		}
		
		drawArmor();
		/*
		int x = Constants.WINDOW_REGION_VIEW_WIDTH+5;
		d.getDialogFont();
		d.g.setColor(new Color(230,0,0));
		int y = 152;
		d.g.drawString("Health: "+d.r.getPlayer().getLife(), x, y);
		y+=16;
		d.g.drawString("Max Health: "+d.r.getPlayer().getMhp(), x, y);
		d.g.setColor(new Color(130,200,0));
		y+=18;
		d.g.drawString("Armor: "+d.r.getPlayer().getArmor(), x, y);
		double defended = 100*((double)d.r.getPlayer().getArmor()/((double)new Functions().getPlayerArmor(d.r.getPlayer().getLevel())*2));
		y+=16;
		d.g.drawString("Defended: "+(int)defended+"%", x, y);
		d.g.setColor(Color.yellow);
		y+=18;
		d.g.drawString("Attack: "+d.r.getPlayer().getPower(), x, y);	
		*/
		
		if(d.dui.isTextUI() == true){
			drawDialog();
		}
		
		drawBackpack();
		drawMoney();
		drawExp();		
	}
	
	private void drawDialog(){
		d.getDialogFont();
		d.drawYesNoWindow(true);
		String[] text = d.dui.getTextUI();
		for(int i = 0; i<3;i++){
			if(text[i] != null){
				d.g.drawString(text[i], 165, 215+(15*i));
			}
		}
	}
	
	private void drawArmor(){
		d.g.drawImage(d.h.armorUIImg, Constants.WINDOW_REGION_VIEW_WIDTH-5, 0, null);
		int ix = Constants.WINDOW_REGION_VIEW_WIDTH+16;
		int iy = 30;
		for(int i = 0;i<3;i++){
			for(int j=0;j<3;j++){
				int pos = (i*3)+j;
				if(d.r.getPlayer().equips[(pos)]== null){
					d.g.drawImage(d.h.armorUIImgs[(i*3)+j], ix, iy, null);
				}else{
					Equipable eq = (Equipable) d.r.getPlayer().equips[pos];
					d.g.drawImage(d.getArmorImg(eq.getType(),eq.getSlot()),ix,iy,null);
				}
				ix+=33;
			}
			ix = Constants.WINDOW_REGION_VIEW_WIDTH+16;
			iy+= 36;
		}
		//g.drawImage(h.armorImgs[0][3], 549, 30, null);
	}
	
	private void drawBackpack(){
		//look at MouseManager before changing
		int bx = 575;
		int by = 410;
		boolean hover = d.dui.isHoverBp();
		if(hover == false){
			d.g.drawImage(d.h.bpUIImg[0],bx,by,null);
		}else{
			d.g.drawImage(d.h.bpUIImg[1],bx,by,null);
		}		
		if(d.dui.getDrawUI() == 1){
			d.drawInv();
		}		
	}
	
	private void drawMoney(){
		long money = d.r.getPlayer().getMoney();
		long z = money/100000;
		long c = money%100;
		long s = (money%100000)/100;
		int a = new MathTool().getDigits(z);
		d.getDialogFont();
		int x = 513;
		int y = 389;
		if(a==5){
			x+=3;
		}
		if(z>0){
			d.g.drawImage(d.h.coinsImgs[2], x+27, y, null);
			d.g.drawString(String.format("%s",z), x+23-(a*7), y+10);
		}
		if(s>0){
			d.g.drawImage(d.h.coinsImgs[1], x+64, y, null);
			d.g.setColor(new Color(191,210,209));
			if(s<10){
				d.g.drawString(String.format("%s",s), x+55, y+10);
			}else if(s>10&&s<100){
				d.g.drawString(String.format("%s",s), x+45, y+10);
			}else{
				d.g.drawString(String.format("%s",s), x+40, y+10);
			}		
		}
		d.g.drawImage(d.h.coinsImgs[0], x+95, y, null);
		d.g.setColor(new Color(196,121,25));
		if(c>10){
			d.g.drawString(String.format("%s",c), x+78, y+10);
		}else{
			d.g.drawString(String.format("%s",c), x+85, y+10);
		}		
		
	}
	
	private void drawExp(){
		int ex = 502;
		int ey = 454;
		int level = d.r.getPlayer().getLevel();
		int exp = (int) Math.ceil(((100*d.r.getPlayer().getExp())/new Functions().getLevelExp(level)));
		String e = String.format("%s"+"%%", exp);
		d.g.setColor(Color.GREEN);
		int w = (exp*78)/100;
		d.g.fillRect(ex+35, ey+13, w, 12);
		d.g.drawImage(d.h.expBarImg, ex, ey, null);		
		d.getDialogFont();
		d.g.drawString(e, ex+5, ey+24);
		String l = String.format("Level %s",d.r.getPlayer().getLevel());
		d.g.drawString(l,ex+36,ey+12);
	}
	
	/*
	 
	
	private void drawBorder(){
		if(d.r.getPlayer().getX() > 3500){
			for(int i = 0;i<5;i++){
				d.g.drawImage(d.h.borderVerImg, (int) (4250-d.r.getPlayer().getX()), i*100, null);
			}
		}else if(d.r.getPlayer().getX() < -3500){
			for(int i = 0;i<5;i++){
				d.g.drawImage(d.h.borderVerImg, (int) (-3750-d.r.getPlayer().getX()), i*100, null);
			}
		}
		if(d.r.getPlayer().getY() > 3500){
			for(int i = 0;i<5;i++){
				d.g.drawImage(d.h.borderHorImg, i*100, (int) (4250-d.r.getPlayer().getY()), null);
			}
		}else if(d.r.getPlayer().getY() < -3500){
			for(int i = 0;i<5;i++){
				d.g.drawImage(d.h.borderHorImg, i*100, (int) (-3750-d.r.getPlayer().getY()), null);
			}
		}
	}
	 */
	
}
