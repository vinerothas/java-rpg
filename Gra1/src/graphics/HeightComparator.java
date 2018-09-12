package graphics;

import graphics.sprite.Sprite;
import graphics.sprite.Enemy;

import java.util.Comparator;

public class HeightComparator implements Comparator<Sprite>{

	public int compare(Sprite s1, Sprite s2) {
		int heightA;
		int heightB;
		if(s1.getAdvHeight()){
			if(s1 instanceof Enemy){//bat
				heightA = 100;
			}else{//ground level (road/spawner)
				heightA = 0;
			}
		}else{
			heightA = s1.getHeight();
		}
		if(s2.getAdvHeight()){
			if(s2 instanceof Enemy){//bat
				heightB = 100;
			}else{//ground level (road/spawner)
				heightB = 0;
			}
		}else{
			heightB = s2.getHeight();
		}
		float a = s1.getY()+heightA;
		float b = s2.getY()+heightB;
		if(a>b){
			return 1;
		}else if(a<b){
			return -1;
		}else{
			return 0;
		}
	}

}
