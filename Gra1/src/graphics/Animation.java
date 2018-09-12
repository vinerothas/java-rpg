package graphics;

import java.awt.Image;

public class Animation {
	
	private int[] animTime;
	private long lastUpdate = 0;
	private long totalTime;
	private Image[] images;

	/**
	 * Creates an animation where animTime[i+1] minus animTime[i]
	 *  is the time of the frame images[i].
	 *  images[0] is idle stance(if needed).
	 */
	public Animation(Image[] images, int[] animTime){
		this.images = images;
		this.animTime = animTime;
		for(int i = 0;i < animTime.length;i++){
			totalTime=animTime[animTime.length-1];
		}
	}
	
	public Image getImage(long elapsedTime){
		lastUpdate+=elapsedTime;
		if(lastUpdate>=totalTime){
			lastUpdate = 0;
			return images[0];
		}
		for(int i = 0; i < animTime.length; i++){
			if(lastUpdate>=animTime[i] && lastUpdate<animTime[i+1]){
				return images[i];
			}
		}
		
		return null;
		
	}
	
	public Image getIdleImage(){
		return images[0];
	}
	
	public Animation cloneAnim(){
		return new Animation(images,animTime);
	}
}
