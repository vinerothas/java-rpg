package window;

import graphics.DrawGame;
import screen.GameScreen;
import screen.TavernScreen;

public class EnterTavernWindow extends YesNoWindow{

	public EnterTavernWindow(GameScreen gs) {
		super(gs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(DrawGame d) {
		drawYNWindow(d);
		d.g.drawString("Do you want to", x+20, y+50);
		d.g.drawString("enter the tavern?", x+20, y+70);
	}

	@Override
	public void confirmAction() {
		gs.sm.setScreen(new TavernScreen(gs.sm,gs));
		gs.window = null;
	}

	@Override
	public void cancelAction() {
		gs.window = null;
		
	}

}
