package generate;

import game.Constants;

import java.awt.Point;
import java.util.Random;

public class GenRiver {
	
	static int max;
	static int min;
	static int[][] water;
	static Random r;

	static public int[][] generateRiver(int seed){
		water = new int[Constants.TILE_NUMBER][Constants.TILE_NUMBER];
		max = Constants.REGION_TOTAL_QUARTER;
		min = -max;
		
		/*
		for(int i = 0;i<426;i++){
			for(int j = 0;j<426;j++){
				water[i][j] = 0;
			}
		}*/
		
		r = new Random(seed);
		int dir = r.nextInt(4);//river begins in: 0-upper_right 1-up 2-upper_left	3-left

		if(dir==0)across(false);
		else if(dir==1)straight(true);//vertical - true
		else if(dir==2)across(true);
		else if(dir==3)straight(false);//false
		return water;
	}
	
	private static void across(boolean left){
		boolean vertical = r.nextBoolean();
		int xinc = 0;
		int yinc = 0;
		int gap = 2000;
		int vq = Constants.REGION_TOTAL_QUARTER;
		Point origin = new Point();
		if(left){
			xinc=-1;
			yinc=1;
			if(r.nextBoolean()){
				origin.x = r.nextInt(vq-gap)+gap;
				origin.y = min;
			}else{
				origin.y = r.nextInt(vq-gap)-vq;
				origin.x = max;
			}
		}else{
			xinc = 1;
			yinc = 1;
			if(r.nextBoolean()){
				origin.x = r.nextInt(vq-gap)-vq;
				origin.y = min;
			}else{
				origin.y = r.nextInt(vq-gap)-vq;
				origin.x = min;
			}
		}
		Point o = coordToTile(origin.x,origin.y);
		
		int spacingMin = 6;//6
		int spacingMax = 10;//10
		double spacing = r.nextInt(spacingMax-spacingMin)+spacingMin; //must be double
		int amplitudeMax = 4;//4
		int amplitudeMin = 2;//2
		double amplitude = r.nextInt(amplitudeMax-amplitudeMin)+amplitudeMin;
		double saFactor = 3.0;
		double lastAmplitude = amplitude;
		//System.out.println(spacing+" "+amplitude);
		int lastChange = -10;
		int lastChange2 = -10;
		double amplitude2 = r.nextInt(amplitudeMax-amplitudeMin)+amplitudeMin;
		double lastAmplitude2 = amplitude2;
		double spacing2 = r.nextInt(spacingMax-spacingMin)+spacingMin;
		
		int lastPos = 0;	
		if(vertical)lastPos= o.y;
		else lastPos = o.x;
		double x = o.x;
		double y = o.y;
		for(int i = 0;i<Constants.TILE_NUMBER;i++){
			double sign = amplitude*Math.sin(i/spacing);
			double sign2 = amplitude2*Math.cos(i/spacing2);
			if(r.nextInt(5)==0){
				if(vertical)setSquare((int)(o.x+(i*xinc)+sign+(sign2/3)),(int)(o.y+(i*yinc)));
				else setSquare((int)(o.x+(i*xinc)),(int)(o.y+(i*yinc)+sign+(sign2/3)));
				if(vertical){
					if(r.nextBoolean())x+=0.25;
					else x-=0.25;
				}else{
					if(r.nextBoolean())y+=0.25;
					else y-=0.25;
				}
				o.x = (int)x;
				o.y = (int)y;
			}
			if((int)(sign)==0&&i-lastChange>15){
				lastChange = i;
				if(r.nextBoolean())spacing+=0.1;
				else spacing -=0.1;
				if(spacing>spacingMax)spacing=spacingMax;
				else if (spacing<spacingMin)spacing = spacingMin;
				//spacing = r.nextInt(8)+5;
				
				if(r.nextBoolean())lastAmplitude+=0.3;
				else lastAmplitude -=0.3;
				if(lastAmplitude>amplitudeMax)amplitude=amplitudeMax;
				else if (lastAmplitude<amplitudeMin)amplitude=amplitudeMin;
				amplitude = lastAmplitude*(spacing/saFactor);
				//amplitude = (r.nextInt(3)+3)*(spacing/4);
				//System.out.println(amplitude+" "+spacing);
				double signTemp = amplitude*Math.sin(i/spacing);
				if(vertical)setBigSquare((int)(o.x+(i*xinc)+signTemp+(sign2/3)),(int)(o.y+(i*yinc)));
				else setBigSquare((int)(o.x+(i*xinc)),(int)(o.y+(i*yinc)+signTemp+(sign2/3)));
				if(vertical)setBigSquare((int)(o.x+((i+1)*xinc)+signTemp+(sign2/3)),(int)(o.y+((i+1)*yinc)));
				else setBigSquare((int)(o.x+((i+1)*xinc)),(int)(o.y+((i+1)*yinc)+signTemp+(sign2/3)));
			}
			if((int)(sign2)==0&&i-lastChange2>15){
				lastChange2 = i;
				if(r.nextBoolean())spacing2+=0.1;
				else spacing2 -=0.1;
				if(spacing2>spacingMax)spacing2=spacingMax;
				else if (spacing2<spacingMin)spacing2 = spacingMin;
				//spacing = r.nextInt(8)+5;
				
				if(r.nextBoolean())lastAmplitude2+=0.3;
				else lastAmplitude2 -=0.3;
				if(lastAmplitude2>amplitudeMax)amplitude2=amplitudeMax;
				else if (lastAmplitude2<amplitudeMin)amplitude2=amplitudeMin;
				amplitude2 = lastAmplitude2*(spacing2/saFactor);
				//amplitude = (r.nextInt(3)+3)*(spacing/4);
				//System.out.println(amplitude2+" "+spacing2);
			}
			
			//System.out.println(sign+" "+i+" ");
			
			if(vertical){
				int pos = (int)(o.x+(i*xinc)+sign+(sign2/3));
				setBigSquare(pos,(int)(o.y+(i*yinc)));
				if(pos-lastPos>2)setBigSquare(pos-2,(int)(o.y+(i*yinc)));
				else if(pos-lastPos<-2)setBigSquare(pos+2,(int)(o.y+(i*yinc)));
				if(pos-lastPos>4)setBigSquare(pos-4,(int)(o.y+(i*yinc)));
				else if(pos-lastPos<-4)setBigSquare(pos+4,(int)(o.y+(i*yinc)));
				lastPos = pos;
			}
			else {
				int pos = (int)(o.y+(i*yinc)+sign+(sign2/3));
				setBigSquare((int)(o.x+(i*xinc)),pos);
				if(pos-lastPos>2)setBigSquare((int)(o.x+(i*xinc)),pos-2);
				else if(pos-lastPos<-2)setBigSquare((int)(o.x+(i*xinc)),pos+2);
				if(pos-lastPos>4)setBigSquare((int)(o.x+(i*xinc)),pos-4);
				else if(pos-lastPos<-4)setBigSquare((int)(o.x+(i*xinc)),pos+4);
				lastPos = pos;
			}
			if(vertical)setBigSquare((int)(o.x+((i+1)*xinc)+sign+(sign2/3)),(int)(o.y+((i+1)*yinc)));
			else setBigSquare((int)(o.x+((i+1)*xinc)),(int)(o.y+((i+1)*yinc)+sign+(sign2/3)));
			
			/*
			if(vertical)System.out.println((int)(o.x+(i*xinc)+sign+(sign2/3)));
			else System.out.println((int)(o.y+(i*yinc)+sign+(sign2/3)));
			*/
		}
		//System.out.println(1+" "+xinc+" "+yinc+" "+origin.x+" "+origin.y);
		
		/*
		for(int i = 0;i<426;i++){
			double sign = 4*Math.sin(i*0.3)+3;
			double sign2 = 4*Math.cos(i*0.3)-3;
			//System.out.println(sign+" "+i+" ");
			setBigSquare((int)(o.x+(i*xinc)+(yinc*sign)),(int)(o.y+(i*yinc)+(xinc*sign2)));
		}
		System.out.println(0+" "+xinc+" "+yinc+" "+origin.x+" "+origin.y);
		*/
	}
	
	private static void straight(boolean vertical){
		int xinc = 0;
		int yinc = 0;
		Point origin = new Point();
		int gap = 500;
		int span = Constants.REGION_PLAYABLE_QUARTER-gap;
		if(vertical){
			yinc=1;
			if(r.nextBoolean())origin.x = r.nextInt(span)+gap;
			else origin.x = r.nextInt(span)-Constants.REGION_PLAYABLE_QUARTER;
			origin.y = min;
		}else{
			xinc = 1;
			if(r.nextBoolean())origin.y = r.nextInt(span)+gap;
			else origin.y = r.nextInt(span)-Constants.REGION_PLAYABLE_QUARTER;
			origin.x = min;
		}	
		
		int spacingMin = 10;
		int spacingMax = 16;
		double spacing = r.nextInt(spacingMax-spacingMin)+spacingMin; //must be double
		int amplitudeMax = 5;
		int amplitudeMin = 3;
		double amplitude = r.nextInt(amplitudeMax-amplitudeMin)+amplitudeMin;
		double saFactor = 3.5;
		double lastAmplitude = amplitude;
		//System.out.println(spacing+" "+amplitude);
		int lastChange = -10;
		int lastChange2 = -10;
		double amplitude2 = r.nextInt(amplitudeMax-amplitudeMin)+amplitudeMin;
		double lastAmplitude2 = amplitude2;
		double spacing2 = r.nextInt(spacingMax-spacingMin)+spacingMin;
		
		Point o = coordToTile(origin.x,origin.y);
		int lastPos = 0;	
		if(vertical)lastPos= o.y;
		else lastPos = o.x;
		double x = o.x;
		double y = o.y;
		for(int i = 0;i<Constants.TILE_NUMBER+100;i++){// +100 ? fixes rivers not reaching the end of map
			double sign = amplitude*Math.sin(i/spacing);
			double sign2 = amplitude2*Math.cos(i/spacing2);
			if(r.nextInt(5)==0){
				if(vertical)setSquare((int)(o.x+(i*xinc)+sign+(sign2/3)),(int)(o.y+(i*yinc)));
				else setSquare((int)(o.x+(i*xinc)),(int)(o.y+(i*yinc)+sign+(sign2/3)));
				if(vertical){
					if(r.nextBoolean())x+=0.25;
					else x-=0.25;
				}else{
					if(r.nextBoolean())y+=0.25;
					else y-=0.25;
				}
				o.x = (int)x;
				o.y = (int)y;
			}
			if((int)(sign)==0&&i-lastChange>15){
				lastChange = i;
				if(r.nextBoolean())spacing+=0.1;
				else spacing -=0.1;
				if(spacing>spacingMax)spacing=spacingMax;
				else if (spacing<spacingMin)spacing = spacingMin;
				//spacing = r.nextInt(8)+5;
				
				if(r.nextBoolean())lastAmplitude+=0.3;
				else lastAmplitude -=0.3;
				if(lastAmplitude>amplitudeMax)amplitude=amplitudeMax;
				else if (lastAmplitude<amplitudeMin)amplitude=amplitudeMin;
				amplitude = lastAmplitude*(spacing/saFactor);
				//amplitude = (r.nextInt(3)+3)*(spacing/4);
				//System.out.println(amplitude+" "+spacing);
				double signTemp = amplitude*Math.sin(i/spacing);
				if(vertical)setBigSquare((int)(o.x+(i*xinc)+signTemp+(sign2/3)),(int)(o.y+(i*yinc)));
				else setBigSquare((int)(o.x+(i*xinc)),(int)(o.y+(i*yinc)+signTemp+(sign2/3)));
				if(vertical)setBigSquare((int)(o.x+((i+1)*xinc)+signTemp+(sign2/3)),(int)(o.y+((i+1)*yinc)));
				else setBigSquare((int)(o.x+((i+1)*xinc)),(int)(o.y+((i+1)*yinc)+signTemp+(sign2/3)));
			}
			if((int)(sign2)==0&&i-lastChange2>15){
				lastChange2 = i;
				if(r.nextBoolean())spacing2+=0.1;
				else spacing2 -=0.1;
				if(spacing2>spacingMax)spacing2=spacingMax;
				else if (spacing2<spacingMin)spacing2 = spacingMin;
				//spacing = r.nextInt(8)+5;
				
				if(r.nextBoolean())lastAmplitude2+=0.3;
				else lastAmplitude2 -=0.3;
				if(lastAmplitude2>amplitudeMax)amplitude2=amplitudeMax;
				else if (lastAmplitude2<amplitudeMin)amplitude2=amplitudeMin;
				amplitude2 = lastAmplitude2*(spacing2/saFactor);
				//amplitude = (r.nextInt(3)+3)*(spacing/4);
				//System.out.println(amplitude2+" "+spacing2);
			}
			
			//System.out.println(sign+" "+i+" ");
			
			if(vertical){
				int pos = (int)(o.x+(i*xinc)+sign+(sign2/3));
				setBigSquare(pos,(int)(o.y+(i*yinc)));
				if(pos-lastPos>2)setBigSquare(pos-2,(int)(o.y+(i*yinc)));
				else if(pos-lastPos<-2)setBigSquare(pos+2,(int)(o.y+(i*yinc)));
				if(pos-lastPos>4)setBigSquare(pos-4,(int)(o.y+(i*yinc)));
				else if(pos-lastPos<-4)setBigSquare(pos+4,(int)(o.y+(i*yinc)));
				lastPos = pos;
			}
			else {
				int pos = (int)(o.y+(i*yinc)+sign+(sign2/3));
				setBigSquare((int)(o.x+(i*xinc)),pos);
				if(pos-lastPos>2)setBigSquare((int)(o.x+(i*xinc)),pos-2);
				else if(pos-lastPos<-2)setBigSquare((int)(o.x+(i*xinc)),pos+2);
				if(pos-lastPos>4)setBigSquare((int)(o.x+(i*xinc)),pos-4);
				else if(pos-lastPos<-4)setBigSquare((int)(o.x+(i*xinc)),pos+4);
				lastPos = pos;
			}
			if(vertical)setBigSquare((int)(o.x+((i+1)*xinc)+sign+(sign2/3)),(int)(o.y+((i+1)*yinc)));
			else setBigSquare((int)(o.x+((i+1)*xinc)),(int)(o.y+((i+1)*yinc)+sign+(sign2/3)));
			
			/*
			if(vertical)System.out.println((int)(o.x+(i*xinc)+sign+(sign2/3)));
			else System.out.println((int)(o.y+(i*yinc)+sign+(sign2/3)));
			*/
		}
		//System.out.println(1+" "+xinc+" "+yinc+" "+origin.x+" "+origin.y);
	}

	private static void setSquare(int x, int y){
		x--;
		y--;
		for(int i = 0;i<3;i++){
			for(int j = 0;j<3;j++){
				try{
					water[x+i][y+j]=1;
					//System.out.println((x+i)+" "+(y+j));
				}catch(Exception ex){}
			}
		}
	}
	
	private static void setBigSquare(int x, int y){
		x--;
		y--;
		for(int i = 0;i<4;i++){
			for(int j = 0;j<4;j++){
				try{
					water[x+i][y+j]=1;
					//System.out.println((x+i)+" "+(y+j));
				}catch(Exception ex){}
			}
		}
	}
	
	/**
	 * Coords have to be dividable by 20
	 */
	private static Point coordToTile(int x, int y){
		double xd = (double)x/(double)20;
		double yd = (double)y/(double)20;
		//213 is the position of tile 0???
		//as in coords 0->20???
		//(close enough) (probs)
		return new Point((int)xd+213,(int)yd+213);
	}
	
	private static void dir1old(){
		int xinc = 0;
		int yinc = 0;
		Point origin = new Point();
		yinc=1;
		if(r.nextBoolean())origin.x = r.nextInt(3750)+500;
		else origin.x = r.nextInt(3250)-3750;
		origin.y = min;
		
		double spacing = 10; //must be double
		int spacingMin = 5;
		int spacingMax = 14;
		double amplitude = 4;
		double lastAmplitude = amplitude;
		int amplitudeMax = 8;
		int amplitudeMin = 4;
		int lastChange = -10;
		
		Point o = coordToTile(origin.x,origin.y);
		double x = o.x;
		double y = o.y;
		for(int i = 0;i<426;i++){
			double sign = amplitude*Math.sin(i/spacing);
			if(r.nextInt(10)==0){
				//setSquare((int)(o.x+(i*xinc)+(sign)),(int)(o.y+(i*yinc)));
				if(r.nextBoolean())x+=0.25;
				else x-=0.25;
				o.x = (int)x;
				o.y = (int)y;
			}
			if((int)(sign)==0&&i-lastChange>15){
				lastChange = i;
				if(r.nextBoolean())spacing+=0.1;
				else spacing -=0.1;
				if(spacing>spacingMax)spacing=spacingMax;
				else if (spacing<spacingMin)spacing = spacingMin;
				//spacing = r.nextInt(8)+5;
				
				if(r.nextBoolean())lastAmplitude+=0.3;
				else lastAmplitude -=0.3;
				if(lastAmplitude>amplitudeMax)amplitude=amplitudeMax;
				else if (lastAmplitude<amplitudeMin)amplitude=amplitudeMin;
				amplitude = lastAmplitude*(spacing/5);
				//amplitude = (r.nextInt(3)+3)*(spacing/4);
				
				//System.out.println(amplitude+" "+spacing);
				//double signTemp = amplitude*Math.sin(i/spacing);
				//setSquare((int)(o.x+(i*xinc)+signTemp),(int)(o.y+(i*yinc)));
			}
			//System.out.println(sign+" "+i+" ");
			setBigSquare((int)(o.x+(i*xinc)+sign),(int)(o.y+(i*yinc)));
		}
		//System.out.println(1+" "+xinc+" "+yinc+" "+origin.x+" "+origin.y);
	}
}
