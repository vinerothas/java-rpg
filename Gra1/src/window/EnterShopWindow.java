package window;

import graphics.DrawGame;
import screen.GameScreen;
import screen.ShopBuyScreen;
import screen.TavernScreen;

public class EnterShopWindow extends YesNoWindow{

	public EnterShopWindow(GameScreen gs) {
		super(gs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(DrawGame d) {
		drawYNWindow(d);
		d.g.drawString("Do you want to", x+20, y+50);
		d.g.drawString("enter the shop?", x+20, y+70);
	}

	@Override
	public void confirmAction() {
		gs.sm.setScreen(new ShopBuyScreen(gs.sm,gs));
		gs.window = null;
	}

	@Override
	public void cancelAction() {
		gs.window = null;
		
	}

}
