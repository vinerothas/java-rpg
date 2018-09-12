package graphics;

import game.Constants;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;

public class DrawTiles {

	ImageHolder h;
	int state = 0;
	private Graphics2D g;
	private long waterTime = 0;	
	int tilesW;
	int tilesH;
	private Point screenMid;
	int[][] tiles;
	
	public DrawTiles(DrawGame d){		
		tilesW = (d.screenSize.x/Constants.TILE_SIZE)+1;
		tilesH = (d.screenSize.y/Constants.TILE_SIZE)+2;
		screenMid = new Point(d.screenSize.x/2,d.screenSize.y/2);
		h = d.h;
	}
	
	/**@param px player position x
	 * @param py player position y
	 * @param elapsedTime
	 * @param g */
	public void drawTiles(int px, int py, long elapsedTime, DrawGame d){
		tiles = d.r.tiles;
		int[][] tilesVar = d.r.tilesVar;
		this.g = d.g;
		waterTime+=elapsedTime;
		int frame = 200;
		int frames = 8;
		if(waterTime>=(frame*frames))waterTime=0;
		state = (int) Math.floor(waterTime/frame);
		
		int cx = px-screenMid.x;
		int cy = py-screenMid.y;
		
		//tile offset
		int sx = cx%Constants.TILE_SIZE;
		int sy = cy%Constants.TILE_SIZE;
		//System.out.println(sx+" "+sy);
	
		Point p = Constants.coordToTile(cx, cy);
		int tx = p.x+1;
		int ty = p.y+1;
		
		int limX = tx +tilesW;
		int limY = ty+tilesH;
		
		//used to make deep water darker, placeholder for proper deep water graphics
		g.setColor(new Color(0,0,0,50));

		g.setFont(new Font("Dialog",Font.PLAIN,10));
		
		for(int i = tx-1;i<limX;i++){
			int x = i-tx;
			for(int j = ty-1;j<limY;j++){
				int y = j-ty;
				int dx = (x*20)-sx;
				int dy = (y*20)-sy;
				try{
					if(tiles[i][j]==Constants.TILE_TYPE_GRASS){
						g.drawImage(h.grassImgs[tilesVar[i][j]], dx, dy, null);	
					}else{
						drawWater(dx,dy,i,j);
					}
				}catch(Exception ex){
					g.drawImage(h.grassImgs[0], dx, dy, null);	
				}
				//g.drawRect(dx, dy, dx+20, dy+20);
				//g.drawString(i+"", dx, dy+10);
				//g.drawString(j+"", dx, dy+20);
			}
		}
		
	}
	
	private void drawWater(int dx, int dy, int i, int j){
		g.drawImage(h.waterImgs[state],dx, dy, null);
		if(tiles[i][j]==2)g.fillRect(dx, dy, 20, 20);
		boolean[] land = new boolean[8];
		int index = 0;
		int amount = 0;
		for(int k = 0;k<3;k++){
			for(int l = 0;l<3;l++){
				if(k==1&&l==1)continue;
				try{
					if(tiles[(i-1)+l][(j-1)+k]==0){
						land[index]=true;
						amount++;
					}else land[index]=false;
				}catch(Exception ex){
					land[index]=false;
				}

				index++;
			}
		}
		if(amount==1){
			if(land[0]==true){
				g.drawImage(h.shoreImgs[0][0], dx, dy, null);	
			}else if(land[1]==true){
				g.drawImage(h.shoreImgs[2][0], dx, dy, null);	
			}else if(land[2]==true){
				g.drawImage(h.shoreImgs[0][1], dx, dy, null);	
			}else if(land[3]==true){
				g.drawImage(h.shoreImgs[2][3], dx, dy, null);	
			}else if(land[4]==true){
				g.drawImage(h.shoreImgs[2][1], dx, dy, null);	
			}else if(land[5]==true){
				g.drawImage(h.shoreImgs[0][3], dx, dy, null);	
			}else if(land[6]==true){
				g.drawImage(h.shoreImgs[2][2], dx, dy, null);	
			}else if(land[7]==true){
				g.drawImage(h.shoreImgs[0][2], dx, dy, null);	
			}
		}else if(amount==2){
			if(land[1]==true){
				g.drawImage(h.shoreImgs[2][0], dx, dy, null);	
			}else if(land[3]==true){
				g.drawImage(h.shoreImgs[2][3], dx, dy, null);	
			}else if(land[4]==true){
				g.drawImage(h.shoreImgs[2][1], dx, dy, null);	
			}else if(land[6]==true){
				g.drawImage(h.shoreImgs[2][2], dx, dy, null);	
			}
		}else if(amount==3||amount==4||amount==5){
			if(land[1]==true&&land[3]==true){
				g.drawImage(h.shoreImgs[1][0], dx, dy, null);	
			}else if(land[1]==true&&land[4]==true){
				g.drawImage(h.shoreImgs[1][1], dx, dy, null);	
			}else if(land[4]==true&&land[6]==true){
				g.drawImage(h.shoreImgs[1][2], dx, dy, null);	
			}else if(land[6]==true&&land[3]==true){
				g.drawImage(h.shoreImgs[1][3], dx, dy, null);	
			}else if(land[1]==true){
				g.drawImage(h.shoreImgs[2][0], dx, dy, null);	
			}else if(land[3]==true){
				g.drawImage(h.shoreImgs[2][3], dx, dy, null);	
			}else if(land[4]==true){
				g.drawImage(h.shoreImgs[2][1], dx, dy, null);	
			}else if(land[6]==true){
				g.drawImage(h.shoreImgs[2][2], dx, dy, null);	
			}
		}
	}
	/*
	 * if(amount==1){
			if(land[0]==true){
				g.drawImage(h.shoreImgs[0], (x*20)-sx, (y*20)-sy, null);	
			}else if(land[1]==true){
				g.drawImage(h.shoreImgs[2], (x*20)-sx, (y*20)-sy, null);	
			}else if(land[2]==true){
				g.drawImage(d.mirrorImg(h.shoreImgs[0]), (x*20)-sx, (y*20)-sy, null);	
			}else if(land[3]==true){
				g.drawImage(d.rotateImage(h.shoreImgs[2], 270), (x*20)-sx, (y*20)-sy, null);	
			}else if(land[4]==true){
				g.drawImage(d.rotateImage(h.shoreImgs[2], 90), (x*20)-sx, (y*20)-sy, null);	
			}else if(land[5]==true){
				g.drawImage(d.rotateImage(h.shoreImgs[0], 270), (x*20)-sx, (y*20)-sy, null);	
			}else if(land[6]==true){
				g.drawImage(d.rotateImage(h.shoreImgs[2], 180), (x*20)-sx, (y*20)-sy, null);	
			}else if(land[7]==true){
				g.drawImage(d.rotateImage(h.shoreImgs[0], 180), (x*20)-sx, (y*20)-sy, null);	
			}
		}else if(amount==2){
			if(land[1]==true){
				g.drawImage(h.shoreImgs[2], (x*20)-sx, (y*20)-sy, null);	
			}else if(land[3]==true){
				g.drawImage(d.rotateImage(h.shoreImgs[2], 270), (x*20)-sx, (y*20)-sy, null);	
			}else if(land[4]==true){
				g.drawImage(d.rotateImage(h.shoreImgs[2], 90), (x*20)-sx, (y*20)-sy, null);	
			}else if(land[6]==true){
				g.drawImage(d.rotateImage(h.shoreImgs[2], 180), (x*20)-sx, (y*20)-sy, null);	
			}
		}else if(amount==3||amount==4||amount==5){
			if(land[1]==true&&land[3]==true){
				g.drawImage(h.shoreImgs[1], (x*20)-sx, (y*20)-sy, null);	
			}else if(land[1]==true&&land[4]==true){
				g.drawImage(d.rotateImage(h.shoreImgs[1], 90), (x*20)-sx, (y*20)-sy, null);	
			}else if(land[4]==true&&land[6]==true){
				g.drawImage(d.rotateImage(h.shoreImgs[1], 180), (x*20)-sx, (y*20)-sy, null);	
			}else if(land[6]==true&&land[3]==true){
				g.drawImage(d.rotateImage(h.shoreImgs[1], 270), (x*20)-sx, (y*20)-sy, null);	
			}else if(land[1]==true){
				g.drawImage(h.shoreImgs[2], (x*20)-sx, (y*20)-sy, null);	
			}else if(land[3]==true){
				g.drawImage(d.rotateImage(h.shoreImgs[2], 270), (x*20)-sx, (y*20)-sy, null);	
			}else if(land[4]==true){
				g.drawImage(d.rotateImage(h.shoreImgs[2], 90), (x*20)-sx, (y*20)-sy, null);	
			}else if(land[6]==true){
				g.drawImage(d.rotateImage(h.shoreImgs[2], 180), (x*20)-sx, (y*20)-sy, null);	
			}
		}
	 */
	
	/*
	 * //800 600
		//px-=127;
		//py-=40;
		
		//1040 585
		px-=246;
		py-=20;
		
		//tile offset
		int sx = px-((int)(Math.round((float)px/(float)20))*20);
		int sy = py-((int)(Math.round((float)py/(float)20))*20);
		//System.out.println(sx+" "+sy);
		
		//add REGION_QUARTER_SIZE to make the player position match array indexes
		// this is becuase array indexes are always positive, whilst player pos can be negative
		float ox = px+Constants.REGION_QUARTER_SIZE+Constants.TILE_SIZE;
		float oy = py+Constants.REGION_QUARTER_SIZE+Constants.TILE_SIZE;

		//get array position of top left tile
		int tx = (int) Math.round(ox/(float)Constants.TILE_SIZE);
		int ty = (int) Math.round(oy/(float)Constants.TILE_SIZE);
		
		//Point p = Constants.coordToTile(px, py);
		//tx = p.x;
		//ty = p.y;
		
		int limX = tx +tilesW;
		int limY = ty+tilesH;
	 */
}
