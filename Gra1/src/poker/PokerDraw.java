package poker;

import graphics.DrawOld;
import graphics.MathTool;
import graphics.Window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.net.URL;

import javax.swing.ImageIcon;

public class PokerDraw {
	
	private Graphics2D g;
	private Window w;
	private Image buttonImg;
	private Image tableImg;
	private Image cardImg;
	private Image backImg;
	private Image dealerImg;
	private Image[] signImgs = new Image[4];//1 - club(acorn)	2 - diamond		3 - heart	4 - spade(sword)
	private Image playerUIImg;
	private Image[] coinsImgs;
	private PokerLoop pl;
	private Point[] betPoints = new Point[6];
	private Point[] moneyPoints = new Point[6];
	
	public PokerDraw(PokerLoop pl, DrawOld d){
		this.pl=pl;
		
		coinsImgs = d.h.coinsImgs;
		buttonImg = d.h.buttonImg;
		dealerImg = getPNG("dealer");
		tableImg = getJPG("table");
		cardImg = getPNG("card");
		backImg = getPNG("back");
		playerUIImg = getPNG("playerUI");
		signImgs[0] = getPNG("club");
		signImgs[1] = getPNG("diamond");
		signImgs[2] = getPNG("heart");
		signImgs[3] = getPNG("spade");
		
		betPoints[0]= new Point(-17,293);
		betPoints[1]= new Point(-17,194);
		betPoints[2]= new Point(142,161);
		betPoints[3]= new Point(300,194);
		betPoints[4]= new Point(300,293);
		betPoints[5]= new Point(142,315);
		
		moneyPoints[0]= new Point(-17,377);
		moneyPoints[1]= new Point(-17,110);
		moneyPoints[2]= new Point(142,77);
		moneyPoints[3]= new Point(300,110);
		moneyPoints[4]= new Point(300,377);
		moneyPoints[5]= new Point(142,396);
	}
	
	public void drawMouse(int x,int y){
		
	}
	
	public void draw(Graphics2D g, Window w){
		this.w=w;
		this.g=g;
				
		g.drawImage(tableImg,0,0,null);
		
		PokerAI player =pl.players[5];
		if(player!=null){
			g.drawImage(playerUIImg, 105, 405, null);
			if(player.turn==false){
				g.setColor(new Color(0,0,0,100));
				g.fillRect(118, 412, 246, 85);
			}
						
			if(player.card1!=null){
				int x = 195;
				int y = 330;
				if(player.card1.visible==2)drawCard(x,y,player.card1);
				if(player.card2.visible==2)drawCard(x+50,y,player.card2);
			}
		}else{
			if(pl.joinState==0||pl.joinState==3){
				int x = 198;
				int y = 348;
				g.drawImage(buttonImg, x, y, 92, 35, null);
				getDialogFont();
				g.drawString("Join for", x+18, y+22);
				drawAmount(140, 385, pl.buyin);
			}else if(pl.joinState==1){
				getDialogFont();
				g.drawString("Waiting for next round", 170, 365);
			}else if(pl.joinState==2){
				getDialogFont();
				g.drawString("Not enough money", 180, 365);
			}
		}
		
		for(int i = 0;i<pl.players.length;i++){
			if(pl.players[i]==null)continue;
			if(i==0){
				drawLL(pl.players[i].card1,pl.players[i].card2);
			}else if(i==1){
				drawUL(pl.players[i].card1,pl.players[i].card2);
			}else if(i==2){
				drawU(pl.players[i].card1,pl.players[i].card2);
			}else if(i==3){
				drawUR(pl.players[i].card1,pl.players[i].card2);
			}else if(i==4){
				drawLR(pl.players[i].card1,pl.players[i].card2);
			}
		}
		
		int x = 120;
		int y = 215;
		for(int j = 0;j<pl.cCards.length;j++){
			if(pl.cCards[j].visible==1){
				g.drawImage(backImg,x+(j*50),y,null);
			}else if(pl.cCards[j].visible==2){
				drawCard(x+(j*50),y,pl.cCards[j]);
			}
		}
		
		drawDealer();
		drawMoney();
	}
	
	public void render(){
		w.graphics = w.bs.getDrawGraphics();
		w.graphics.drawImage(w.buffer, 0, 0, null);        
        w.bs.show();       
        if (w.graphics != null) w.graphics.dispose();
        if (g != null) g.dispose();
	}
	
	private void drawMoney(){
		drawPool(coinsImgs,pl.pool);
				
		if(pl.players[5]!=null){
			if(pl.players[5].turn){
				if(pl.playerInput>0)drawAmount(130,465,pl.playerInput);
			}
		}			
			
		for(int i = 0;i<pl.players.length;i++){
			if(pl.players[i]==null)continue;
			if(pl.players[i].bet!=0)drawAmount(betPoints[i].x,betPoints[i].y,pl.players[i].bet);
			int ys = -37;
			if(i==0||i==4)ys=(-ys);
			else if(i==5){
				if(pl.players[i].bet!=0)ys=-15;
				else ys=0;
			}
			if(pl.players[i].state==2){
				getDialogFont();				
				g.drawString("Folded", betPoints[i].x+75, betPoints[i].y+9+ys);
			}else if(pl.players[i].state==1){
				getDialogFont();
				g.drawString("Checked", betPoints[i].x+70, betPoints[i].y+9+ys);
			}
			drawAmount(moneyPoints[i].x,moneyPoints[i].y,pl.players[i].money);
		}
	}
	
	public void drawContinue(){
		int x=190;
		int y=231;
		g.drawImage(buttonImg, x, y, 108, 35, null);
		getDialogFont();
		g.drawString("Continue", x+20, y+22);
	}
	
	private void drawPool(Image[] coinsImgs, int amount){
		if(amount==0)return;
		int z = amount/100000;
		int c = amount%100;
		int s = (amount%100000)/100;
		int za = new MathTool().getDigits(z);
		double a = new MathTool().getDigits(amount);
		getDialogFont();
		int x = 135;
		int y = 285;		
		double shove = a*7.5;
		x+=shove;
		
		if(z>0){
			g.drawImage(coinsImgs[2], x+27, y, null);
			g.drawString(String.format("%s",z), x+23-(int)(za*7.3), y+10);
		}
		if(s>0){
			g.drawImage(coinsImgs[1], x+64, y, null);
			g.setColor(new Color(191,210,209));
			if(s<10){
				g.drawString(String.format("%s",s), x+55, y+10);
			}else if(s>10&&s<100){
				g.drawString(String.format("%s",s), x+45, y+10);
			}else{
				g.drawString(String.format("%s",s), x+40, y+10);
			}		
		}
		g.drawImage(coinsImgs[0], x+95, y, null);
		g.setColor(new Color(196,121,25));
		if(c>=10){
			g.drawString(String.format("%s",c), x+78, y+10);
		}else{
			g.drawString(String.format("%s",c), x+85, y+10);
		}	
	}
	
	public void drawAmount(int x,int y,int amount){
		int z = amount/100000;
		int c = amount%100;
		int s = (amount%100000)/100;
		int za = new MathTool().getDigits(z);
		double a = new MathTool().getDigits(amount);
		getDialogFont();		
		double shove = a*7.5;
		x+=shove;
		
		if(z>0){
			g.drawImage(coinsImgs[2], x+27, y, null);
			g.drawString(String.format("%s",z), x+23-(int)(za*7.3), y+10);
		}
		if(s>0){
			g.drawImage(coinsImgs[1], x+64, y, null);
			g.setColor(new Color(191,210,209));
			if(s<10){
				g.drawString(String.format("%s",s), x+55, y+10);
			}else if(s>10&&s<100){
				g.drawString(String.format("%s",s), x+45, y+10);
			}else{
				g.drawString(String.format("%s",s), x+40, y+10);
			}		
		}
		g.drawImage(coinsImgs[0], x+95, y, null);
		g.setColor(new Color(196,121,25));
		if(c>=10){
			g.drawString(String.format("%s",c), x+78, y+10);
		}else{
			if(s>0)g.drawString(String.format("%02d",c), x+77, y+10);
			else g.drawString(String.format("%s",c), x+85, y+10);
		}	
	}
	
	private void drawDealer(){
		int d = pl.dealer;
		
		int x1 = 295;
		int y1 = 125;
		int x2 = 135;
		int y2 = 155;
		if(d==5)g.drawImage(dealerImg,x1,y1+210,null);
		else if(d==0)g.drawImage(dealerImg,x2,y2+150,null);
		else if(d==1)g.drawImage(dealerImg,x2,y2,null);		
		else if(d==2)g.drawImage(dealerImg,x1,y1,null);
		else if(d==3)g.drawImage(dealerImg,x2+317,y2,null);
		else if(d==4)g.drawImage(dealerImg,x2+317,y2+150,null);
	}
	
	private void drawUL(Card c1, Card c2){
		int y= 126;
		int x=37;
		drawC1C2(x, y, c1, c2);;
	}
	
	private void drawUR(Card c1, Card c2){
		int y= 126;
		int x=352;
		drawC1C2(x, y, c1, c2);
	}
	
	private void drawLL(Card c1, Card c2){
		int y= 305;
		int x=37;
		drawC1C2(x, y, c1, c2);
	}
	
	private void drawLR(Card c1, Card c2){
		int x=352;
		int y= 305;
		drawC1C2(x, y, c1, c2);
	}
	
	private void drawU(Card c1, Card c2){
		int x = 195;
		int y= 95;
		drawC1C2(x, y, c1, c2);
	}
	
	private void drawC1C2(int x, int y, Card c1, Card c2){
		if(c1.visible==1){
			g.drawImage(backImg, x, y, null);
		}else if(c1.visible==2)drawCard(x,y,c1);	
		
		if(c2.visible==1){
			g.drawImage(backImg, x+50, y, null);
		}else if(c2.visible==2)drawCard(x+50,y,c2);
	}
	
	private void drawCard(int x, int y, Card c){
		int n = c.number;
		g.drawImage(cardImg, x, y, null);	
		g.drawImage(signImgs[c.sign-1],x+2,y+30,null);
		g.setFont(new Font("Dialog",Font.BOLD,25));
		if(c.sign==1||c.sign==4){
			g.setColor(new Color(218,204,30));
		}else{
			g.setColor(Color.RED);
		}
		String s = new String();
		if(n==14){
			s = "A";
			x-=2;
		}else if(n==11){
			s = "J";
			x-=1;
		}else if(n==12){
			s = "Q";
			x-=3;
		}else if(n==13){
			s = "K";
			x-=2;
		}else{
			if(n==10){
				x-=8;
			}
			s = String.valueOf(n);
		}
		g.drawString(s, x+16, y+25);
	}
	
	public void getDialogFont(){
		g.setColor(new Color(255,230,0));
		g.setFont(new Font("Dialog",Font.BOLD,15));
	}
	
	private Image getPNG(String path){
		URL is = this.getClass().getResource("img/"+path+".PNG");
		try{
		return new ImageIcon(is).getImage(); 
		}catch(Exception ex){
			is = this.getClass().getResource("img/"+path+".png");
			return new ImageIcon(is).getImage(); 
		}
	}
	
	private Image getJPG(String path){
		URL is = this.getClass().getResource("img/"+path+".JPG");
		try{
		return new ImageIcon(is).getImage(); 
		}catch(Exception ex){
			is = this.getClass().getResource("img/"+path+".jpg");
			return new ImageIcon(is).getImage(); 
		}
	}
	
}
