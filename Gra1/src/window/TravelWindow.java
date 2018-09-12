package window;

import graphics.DrawGame;

import screen.GameScreen;

public class TravelWindow extends YesNoWindow{
	
	private String direction;
	private int dir;

	public TravelWindow(GameScreen gs, int dir) {
		super(gs);
		if(dir==1)direction="north";
		else if(dir==2)direction="east";
		else if(dir==3)direction = "south";
		else if(dir==4)direction = "west";
		this.dir = dir;
	}

	@Override
	public void draw(DrawGame d) {
		drawYNWindow(d);
		d.g.drawString("Do you want to", x+20, y+50);
		d.g.drawString("travel "+direction+"?", x+20, y+70);
		
	}

	@Override
	public void confirmAction() {
		gs.travel(dir);
		if(gs.window == this)gs.window = null;		
	}

	@Override
	public void cancelAction() {
		if(gs.window == this)gs.window = null;
	}

}
