package generate;

import game.Constants;

import java.awt.Point;
import java.util.Random;

public class GenShore {
	
	static int max;
	static int min;
	static int[][] water;
	final static int maxChange = 20;
	static Point previousPoint;
	static Point previousPoint2;
	static Random r;
	private static int dir;
	private final static int deepWater = 7;//dist from shore to deep water
	
	static public int[][] generateShore(int seed){
		water = new int[Constants.TILE_NUMBER][Constants.TILE_NUMBER];
		max = Constants.REGION_TOTAL_SIZE/2;
		min = -max;
		
		/*
		for(int i = 0;i<425;i++){
			for(int j = 0;j<425;j++){
				water[i][j] = 0;
			}
		}*/
		
		r = new Random(seed);
		dir = r.nextInt(4)+1;//shore_location: 1-up	2-right	3-down	4-left
		//max reach in opposite dir (theoretical, unimplemented)
		//dir1: y: -1500 -3500
		//dir2: x: 1500 3500
		//dir3: y: 1500 3500
		//dir4: x: -1500 -3500
		
		int c = r.nextInt(10);//0-5 full linear length(coast)	6-9 partial shore
		if(c>6)coast();
		else coast();//shore
		
		//max start 2000 (at least 3/4 of region is shore)
		return water;
	}
	
	/**
	 * Water that covers the whole border in dir
	 */
	private static void coast(){
		//origin of the coast (beginning of generation)
		Point o = new Point();
		int xinc = 0;
		int yinc = 0;	
		
		//max reach in opposite direction to dir
		int reach = r.nextInt(2000)+1500;
		//pick starting point for generation and set the constant increment
		if(dir==1||dir==3){
			o.x=min;
			if(dir==1){
				o.y=-reach;
			}else if(dir==3){
				o.y=reach;
			}
			xinc = 20;
		}else if(dir==2||dir==4){
			o.y=min;
			if(dir==2){
				o.x=reach;
			}else if(dir==4){
				o.x=-reach;
			}
			yinc = 20;
		}
		
		//initialize the previousPoints for dent fixing
		previousPoint = coordToTile(o.x,o.y);
		previousPoint2 = coordToTile(o.x,o.y);
		
		int lastChange = 0;//used so the changes don't happen too often
		int lastInc = 0;//used to define lastIncCount, changes after each diff is applied
		int lastIncCount = 0;//used to force diff addition if same increment is persistent
		int lastSign = 0;//used to define lastSignCount, changes after increment changes sing, possible values: -1,0,1
		int lastSignCount=0;//used to force diff addition if same sign of increment is persistent
		final int lastSignLimit = 7;
		
		for(int i = 0;i<Constants.TILE_NUMBER;i++){
			int change = r.nextInt(6);
			//randomize a change in perpendicular increment
			if((change==0&&lastChange>2)||lastIncCount>4||lastSignCount>lastSignLimit){
				int tries=0;
				do{//repeat for lastSign-7
					//System.out.println(lastSignCount);
					int diff = (r.nextInt(5)*5)-10;//from -10 to 10
					//int diff = (r.nextInt(3)*5)-5;//from -5 to 5
					if(dir==1||dir==3){
						lastInc = yinc;
						yinc+=diff;
						//update lastSign
						int newSign = updateLastSign(yinc);
						if(newSign!=lastSign)lastSignCount=0;
						else if(tries==0)lastSignCount++;
						lastSign=newSign;
						//update lastSign end
						if(yinc>maxChange)yinc=maxChange;//makes sure the increment isn't too big
						else if(yinc<-maxChange)yinc=-maxChange;
					}else if(dir==2||dir==4){
						lastInc = xinc;
						xinc+=diff;
						//update lastSign
						int newSign = updateLastSign(xinc);
						if(newSign!=lastSign)lastSignCount=0;
						else if(tries==0)lastSignCount++;
						lastSign=newSign;
						//update lastSign end
						if(xinc>maxChange)xinc=maxChange;
						else if(xinc<-maxChange)xinc=-maxChange;
					}
					lastChange=0;
					tries++;
					//System.out.println(tries);
				}while(lastSignCount>tries+lastSignLimit);
			}
			//change in increment block end
			
			setLine(o.x,o.y);
			
			o.x= o.x+(xinc);
			o.y= o.y+(yinc);
			
			//update change boolean definers
			lastChange++;
			if(dir==1||dir==3){
				if(yinc==lastInc)lastIncCount++;
			}else if(dir==2||dir==4){
				if(xinc==lastInc)lastIncCount++;
			}
		}
	}
	
	private static void setLine(int x, int y){

		Point t = coordToTile(x,y);
		
		//check for dents, for example x in following coords: 2 1 2
		if(dir==1||dir==3){
			if((Math.abs(previousPoint.y-t.y)==1)){
				if(Math.abs(previousPoint.y-previousPoint2.y)==1){
					if((previousPoint.y-t.y)==(previousPoint.y-previousPoint2.y)){
						t.y=previousPoint.y;
					}
				}
			}
		}else if(dir==2||dir==4){
			//System.out.println("a "+Math.abs(previousPoint.x-t.x));
			//System.out.println(previousPoint.x+" "+t.x);
			if((Math.abs(previousPoint.x-t.x)==1)){
				//System.out.println("b "+Math.abs(previousPoint.x-previousPoint2.x));
				if(Math.abs(previousPoint.x-previousPoint2.x)==1){
					//System.out.println("c "+(previousPoint.x-t.x)+" "+(previousPoint.x-previousPoint2.x));
					if((previousPoint.x-t.x)==(previousPoint.x-previousPoint2.x)){
						t.x=previousPoint.x;
					}
				}
			}
		}
		//dent checking block end
		
		//update previousPoints used for dent fixing
		//System.out.println(previousPoint2+" "+previousPoint+" "+t);
		previousPoint2.x=previousPoint.x;
		previousPoint2.y=previousPoint.y;
		previousPoint.x=t.x;
		previousPoint.y=t.y;
		//System.out.println(previousPoint2+" "+previousPoint+" "+t);
		
		//setLine all the way to map border
		int dist = 0;//used for defining deep/shallow water
		int deep = deepWater+r.nextInt(2);//randomize deep distance by 1
		if(dir==1){
			while(t.y>=0){
				addTile(t,dist,deep);
				t.y= t.y-1;
				dist++;
			}
		}else if(dir==3){
			while(t.y<Constants.TILE_NUMBER){
				addTile(t,dist,deep);
				t.y= t.y+1;
				dist++;
			}
		}else if(dir==2){
			while(t.x<Constants.TILE_NUMBER){
				addTile(t,dist,deep);
				t.x= t.x+1;
				dist++;
			}
		}else if(dir==4){
			while(t.x>=0){
				addTile(t,dist,deep);
				t.x= t.x-1;
				dist++;
			}
		}
	}
	
	private static int updateLastSign(int inc){
		if(inc>0){
			return 1;
		}else if(inc<0){
			return -1;
		}else{
			return 0;
		}
	}
	
	/**
	 * Set chosen tile to water, decides for deep/shallow
	 */
	private static void addTile(Point t, int dist, int deep){
		try{
			if(dist<deep)water[t.x][t.y]=1;
			else water[t.x][t.y]=2;
		}catch (Exception ex){}
	}
	
	private static Point coordToTile(int x, int y){
		int xd = (int) Math.floor((float)x/(float)20);
		int yd = (int) Math.floor((float)y/(float)20);
		return new Point((int)xd+Constants.TILE_MIDDLE,(int)yd+Constants.TILE_MIDDLE);
	}
}
