package graphics.sprite;

import graphics.Animation;
import graphics.DrawGame;
import graphics.ImageHolder;

public class EnemySimp extends Enemy{

	private Animation anim = null;
	
	public EnemySimp(ImageHolder h, int type){
		super.type = type;
		if(type == 1){
			setV((float) 0.05);
			setHeight(22);
			setWidth(24);
			anim = h.slimeAnim.cloneAnim();
		}else if(type ==2){
			setV((float) 0.10);
			setHeight(20);
			setWidth(20);
			setAdvHeight(true);
			setFloating(true);
			anim = h.batAnim.cloneAnim();
		}
	}
	
	public Animation getAnim() {
		return anim;
	}
	
	public void draw(DrawGame d, int x, int y){

		draw(getAnim().getImage(d.elapsedTime), d, x, y);
		//d.g.drawImage(getAnim().getImage(d.elapsedTime),x, y, null);
		//drawBar(d,x,y);
	}
	
}
