package window;

import screen.GameScreen;

public class DeathWindow extends InfoWindow{

	public DeathWindow(GameScreen gs) {
		super(gs, new String[]{" ","You have died.","Respawn?"});
	}

	public void confirmAction(){
		gs.respawn();
	}
	
}
