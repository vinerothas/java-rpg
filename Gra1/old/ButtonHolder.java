package input;

import game.Constants;

public class ButtonHolder {
	
	public Button backpack;
	public Button yes;
	public Button no;
	public Button exitBuilding;
	public Button sleep;
	public Button playPoker;
	public Button shopBuy;
	public Button shopSell;
	public Button shopSellMobLoot;
	public Button[] pokerArray = new Button[5];
	
	public Button[] menuArray = new Button[9]; 
	//0-new_game 1-load_game 2-quit 3-ng_start_game 4-ng_back 5-lg_back 6-lg_load 7-resume 8-save&quit
	
	public ButtonHolder(){
		backpack = new Button(575,410,608,453);
		yes = new Button(185,263,225,278);
		no = new Button(275,263,315,278);
		exitBuilding = new Button(405,453,471,481);
		sleep = new Button(44,352,147,370);
		playPoker = new Button(44,375,147,395);
		shopBuy = new Button(126, 97, 158, 113);
		shopSell = new Button(166, 97, 198, 113);
		shopSellMobLoot = new Button(204, 97, 314, 113);
		pokerArray[0] = new Button(127,419,183,446);
		pokerArray[1] = new Button(191,419,294,446);
		pokerArray[2] = new Button(302,419,354,446);
		pokerArray[3] = new Button(205,352,281,380);
		pokerArray[4] = new Button(202,235,285,261);
		int w =  Constants.WINDOW_WIDTH / 2; //310
		int h = Constants.WINDOW_HEIGHT / 2; //250
		int bw = 200; //button width
		int bh = 60; //button height
		//arbitrary variables
		int x1 = w-100;
		int w1 = x1+bw;
		int x2 = w-210;
		int x3 = w+10;
		int h1 = h-130;
		int h2 = h-50;
		int h3 = h+30;
		int h4 = h-25;
		int h5 = h+70;
		int h6 = h+150;
		int h7 = h-100;
		
		//menuArray[0] = new Button(x1,h1,w1,h1+bh); //210,120,410,180
		//menuArray[1] = new Button(x1,h2,w1,h2+bh); //210,200,410,260
		//menuArray[2] = new Button(x1,h3,w1,h3+bh); //210,280,410,340
		//menuArray[3] = new Button(x1,h4,w1,h4+bh); //210,225,410,285
		//menuArray[4] = new Button(x1,h5,w1,h5+bh); //210,320,410,380
		//menuArray[5] = new Button(x2,h6,x2+bw,h6+bh); //100,400,300,460
		//menuArray[6] = new Button(x3,h6,x3+bw,h6+bh); //320,400,520,460
		menuArray[7] = new Button(x1,h7,w1,h7+bh); //210,150,410,210
		menuArray[8] = new Button(x1,h,w1,h+bh); //210,250,410,310
	}
}
