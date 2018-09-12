package generate;

import game.Constants;
import game.Region;
import graphics.sprite.House;
import graphics.sprite.Road;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class GenVillage {
	
	private Region r;
	private Random rand;
	private int tavern = 0;
	private int lastY;
	private int lastX;
	private int mainSign;
	private LinkedList<House> houses;
	
	public GenVillage(Region region){
		this.r = region;
		houses = new LinkedList<House>();
		rand = new Random();
		generateRoads();
		if(tavern == 0){
			if(mainSign==-1){
				lastY-=65;
			}else{
				lastY+=45;
			}
			placeHouse(lastX-70,lastY,2);
		}
		placeShop();
	}

	private void generateRoads(){
		
		int[] pos = rollVillage();
		//
		//int vh = pos[0];
		int vh = 0;
		//
		//
		int xy = pos[1];
		int x = -Constants.REGION_TOTAL_QUARTER;
		int y = 0;
		//main road
		while(x<Constants.REGION_TOTAL_QUARTER){
			//village
			if(vh == 0){
				if(x+40 > xy && x-40 < xy){
					System.out.println("line 53 GenVillage x pos: "+x);							
					int x2 = x;
					int y2 = y;
					//up(-1) or down(1) from the road
					mainSign = rand.nextInt(2);
					if(mainSign == 0){
						mainSign = -1;
					}
					int y3 = 0;
					int b = 400;
					for(int i = 0;i<30;i++){
						y2+=(35*mainSign);
						y3+=(35*mainSign);
						//side road in village
						if(Math.abs(y3)+36 > b && Math.abs(y3)-36 < b){
							b+=280;
							sideRoad(x2,y2,y3,1);
							sideRoad(x2,y2,y3,-1);
						}
						Road road = new Road(x2,y2);									
						//y2+= r.nextInt(35)-40;
						x2+= rand.nextInt(20)-10;
						r.addBuilding(road);
						if(i==29){
							lastY =y2;
							lastX = x2;
						}
					}
					vh=-1;
				}
			}
			Road road = new Road(x,y);			
			x+=40;
			x+= rand.nextInt(25)-20;
			y+= rand.nextInt(40)-20;
			r.addBuilding(road);		
		}
		
		x = 0;
		y = -Constants.REGION_TOTAL_QUARTER;
		while(y<Constants.REGION_TOTAL_QUARTER){
			if(vh == 1){
				if(y+40 > xy && y-40 < xy){
					System.out.println("line 96 GenVillage y pos: "+y);					
					int x2 = x;
					int y2 = y;
					//right or left from the road
					int sign = rand.nextInt(2);
					if(sign == 0){
						sign = -1;
					}
					for(int i = 0;i<31;i++){
						x2+=(35*sign);
						Road road = new Road(x2,y2);									
						y2+= rand.nextInt(40)-20;
						//x2+= r.nextInt(40)-20;
						r.addBuilding(road);
					}
					vh=-1;
				}
			}
			Road road = new Road(x,y);			
			y+=50;
			y+= rand.nextInt(35)-40;
			x+= rand.nextInt(40)-20;
			r.addBuilding(road);
		}
		
	}
	
	private void sideRoad(int x2, int y2, int y3, int sign){
		
		int a = rand.nextInt(7);
		if(a == 0 || a ==1){
			int s = 70;
			int type =0;
			if(sign ==-1){
				s= -240;
				type=1;
			}		
			placeHouse(x2+s,y2,type);
		}else if(a == 5){
			sideSideRoad(x2,y2,sign,500);
		}else if(a ==2 || a == 3 || a==4){
			sideSideRoad(x2,y2,sign,250);
		}
		
	}
	
	/*
	 * Sign decides if the road goes left(-1) or right(1)
	 */	
	private void sideSideRoad(int x2, int y2, int sign,int li){
		int xe = x2+(30*sign);
		int ye = y2;
		int lim = xe+(li*sign);
		
		// shows if a house has been placed
		int h = 0;
		
		boolean l = xe<lim;
		if(sign == -1){
			l = xe>lim;
		}
		//the place of first house
		int b = (50*sign);
		int xe2 = 0;
		while(l){
			int type = 0;
			if(sign==-1){
				type =1;
			}
			if(Math.abs(xe2)+36 > b && Math.abs(xe2)-36 < b){
				b+=(280*sign);
				int xe3 = xe;
				//left side house further away from road
				if(sign == -1){
					xe3-=80;
				}else{
					xe3-=15;
				}
				int ye3 = ye;
				if(ye>0){
					ye3+=0;
				}else{
					ye3+=30;
				}
				h++;
				placeHouse(xe3,ye3,type);
			}
			Road road = new Road(xe,ye);
			xe+=(35*sign);
			xe2+=(35*sign);
			ye+= rand.nextInt(25)-12;
			r.addBuilding(road);
			l = xe<lim;
			if(sign == -1){
				l = xe>lim;
			}
			int g = rand.nextInt(2);
			if(l == false && (h == 0 || g==1)){
				int xe4 = xe;
				if(sign == 1){
					xe4+=5;
				}else{
					xe4-=180;
				}
				int ye4 = ye;
				if(ye>0){
					ye4-=150;
				}else{
					ye4-=150;
				}
				placeHouse(xe4,ye4,type);
			}
		}
	}
	
	private void placeShop(){
		int h = 0;
		
		Iterator<House> i = houses.iterator();
		while(i.hasNext()){
			House house = i.next();
			if(house.getType()==0 || house.getType() == 1){
				h++;
			}
		}
		
		
		int s = rand.nextInt(h);
		h = 0;
		i = houses.iterator();
		while(i.hasNext()){
			House house = i.next();
			if(house.getType()==0 || house.getType()==1){
				if(h==s){
					house.setType(house.getType()+4);
					return;
				}else{
					h++;
				}
			}
		}
	}
	
	private void placeHouse(int x, int y, int type){
		if(tavern == 0 && (type==1||type==0)){
			int t = rand.nextInt(4);
			if(t==0){
				tavern++;
				type+=2;
				House house = new House(x,y,type);
				r.addBuilding(house);
				houses.add(house);
			}else{
				House house = new House(x,y,type);
				r.addBuilding(house);
				houses.add(house);
			}
		}else{
			House house = new House(x,y,type);
			r.addBuilding(house);
			houses.add(house);
		}
	}
	
	private int[] rollVillage(){
		
		//rolls horizontal(0) or vertical(1) road to put village on
		int vh = rand.nextInt(2);
		
		//rolls y or x position of village between 1000 and 3000
		int xy = rand.nextInt(2000)+1000;
		
		//rolls if the village will be north/west(0) or south/east(1)
		int sign = rand.nextInt(2);
		if(sign == 0){
			xy = xy*(-1);
		}
		return new int[]{vh,xy};
	}
	
}
