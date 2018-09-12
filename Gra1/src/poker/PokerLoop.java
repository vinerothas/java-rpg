package poker;

import java.util.Random;

import game.Functions;
import game.GameManager;
import graphics.DrawOld;

public class PokerLoop {
	
	protected Card[] deck = new Card[52];
	protected Card[] cCards = new Card[5];
	protected int wager;
	protected int buyin;
	protected PokerAI[] players = new PokerAI[6];
	protected int state = 0;//0-none	1-start	2-startbet	3-flop	4-flopbet	5-turn	6-turnbet	7-river	8-riverbet	9-showdown 10-checkvictory	11-restart(od razu do 0?)
	protected int pool = 0;
	protected int dealer = 0;
	protected int highestBet = 0;
	protected int playerInput = 0;
	protected int joinState = 0; //0-none	1-joining	2-nomoney	3-buyin back
	
	private void init(int level){
		Random r = new Random();
		double rand = r.nextInt(100);
		double factor = (double)1+(rand/(double)100);
		wager = (int)((new Functions().getEnemyValue(level)+5)*factor);
		System.out.println("rand:"+rand+" factor:"+factor+" wager:"+wager);
		rand = r.nextInt(350)-100;
		factor = (rand/(double)100);
		buyin = (int)(wager*(factor+10));
		System.out.println("rand:"+rand+" factor:"+factor+" buyin:"+buyin);
		
		int ai = r.nextInt(4)+2; //od 2 do 5 przeciwnikow
		for(int i = 0;i<ai;i++){
			while(true){
				int p = r.nextInt(5);
				if(players[p]==null){
					players[p]=new PokerAI();
					players[p].id=p;
					players[p].money=buyin;
					break;
				}
			}
		}
		while(true){
			int de = r.nextInt(players.length);
			if(players[de]==null)continue;
			else {
				dealer = de;
				break;
			}
		}
		
	}
	
	public void loop(DrawOld d, GameManager gm){
		long lastTime = System.currentTimeMillis();
		long elapsedTime;
		long showdownTime=0;
		long delayed = 0;
		final long delay = 700;
		int tick = 0;
		long second = 0;
		PokerDraw pd = new PokerDraw(this,d);
		CardDealing cd = new CardDealing();
		Betting b = new Betting();
		AI ai = new AI();
		
		init(gm.r.getLevel());
		
		while(true){
			elapsedTime = System.currentTimeMillis() - lastTime;
			lastTime += elapsedTime;
			second+=elapsedTime;
			if(elapsedTime==0){
				continue;
			}
			if(elapsedTime>100)elapsedTime = 15;
			if(second >= 1000){
				//System.out.println("Poker FPS: "+tick);
				second = 0;
				tick = 0;
			}
			
			if(d.dui.getPState()==0){
				if(players[5]!=null)gm.r.getPlayer().addMoney(players[5].money);
				break;
			}
			
			if(players[players.length-1]==null){
				boolean cont=false;
				if(joinState==0||joinState==3){
					if(d.dui.getPokerInput()==13){
						if(gm.r.getPlayer().getMoney()>=buyin)joinState=1;
						else joinState=2;				
					}
				}
				if(joinState==3){
					cont=true;
				}
				if(cont)continue;
			}
			
			
			if(state>2&&state<10){
				int fold = 0;
				int p = 0;
				for(int i = 0;i<players.length;i++){
					if(players[i]==null)continue;
					p++;
					if(players[i].state==2)fold++;
				}
				if(fold==p-1)state=10;
			}
			
			if(state==0){	//init cards
				
				int p = 0;
				for(int i = 0;i<players.length;i++){
					if(players[i]==null)continue;
					p++;
				}
				randomKickJoin(p);
				
				//check if player joins
				if(players[players.length-1]==null&&joinState==1){
					players[players.length-1]=new PokerAI();
					players[players.length-1].money = buyin;
					gm.r.getPlayer().addMoney(-buyin);
					players[players.length-1].id=players.length-1;
					joinState=0;
				}
				dealer = getSmallBlind().id;
				startGame();
				state=1;
			}else if(state==1){	//deal cards
				boolean dealt = cd.deal(elapsedTime, this);
				if(dealt)state = 2;
			}else if(state==2){	//pre flop bet forced
				boolean bet = b.firstBet(elapsedTime, this);
				if(bet){
					int turn = b.nextPlayer(getBigBlind().id, players);
					players[turn].turn=true;
					state = 3;
				}
			}else if(state==3){//pre flop bet manual
				boolean bet = true;
				if(delayed==0)bet = betLoop(ai,elapsedTime,gm);
				if(bet){
					delayed+=elapsedTime;
					if(delayed>delay){
						ai.resetStates(this);
						delayed=0;
						highestBet =0;
						state++;
					}
				}
			}else if(state==4){	//flop
				boolean flop = cd.flop(elapsedTime, this);
				if(flop){
					getSmallBlind().turn = true;
					state = 5;
				}
			}else if(state==5){	//flop bet				
				boolean bet = true;
				if(delayed==0)bet = betLoop(ai,elapsedTime,gm);
				if(bet){
					delayed+=elapsedTime;
					if(delayed>delay){
						delayed=0;
						ai.resetStates(this);
						highestBet =0;
						state++;
					}
				}
			}else if(state==6){ //turn
				boolean turn = cd.turn(elapsedTime, this);
				if(turn){
					getSmallBlind().turn = true;
					state = 7;
				}
			}else if(state==7){ //turn bet
				boolean bet = true;
				if(delayed==0)bet = betLoop(ai,elapsedTime,gm);
				if(bet){
					delayed+=elapsedTime;
					if(delayed>delay){
						delayed=0;
						ai.resetStates(this);
						highestBet =0;
						state++;
					}
				}
			}else if(state==8){ //river
				boolean river = cd.river(elapsedTime, this);
				if(river){
					getSmallBlind().turn = true;
					state = 9;
				}
			}else if(state==9){ //river bet
				boolean bet = true;
				if(delayed==0)bet = betLoop(ai,elapsedTime,gm);
				if(bet){
					delayed+=elapsedTime;
					if(delayed>delay){
						delayed=0;
						ai.resetStates(this);
						highestBet =0;
						state++;
					}
				}
			}else if(state==10){	//showdown
				boolean showdown = cd.showdown(elapsedTime, this);
				if(showdown)state=11;
			}else if(state==11){
				if(showdownTime==0){
					showdown();
				}
				showdownTime+=elapsedTime;
				if(showdownTime>1000){
					//kick players with too little money
					for(int i = 0;i<players.length;i++){
						if(players[i]==null)continue;
						if(players[i].money<=2*wager){
							players[i]=null;
							if(i==5)joinState=3;
						}
					}
				}
				if(players[5]!=null&&showdownTime>1000){
					if(d.dui.getPokerInput()==14){
						showdownTime=0;
						resetPlayers();
						state=0;
					}
				}else if(showdownTime>2000){
					showdownTime=0;
					resetPlayers();
					state=0;
				}
			}
			
			drawing(elapsedTime,d,pd,state);
			
			tick++;
		}
	}
	
	private void randomKickJoin(int p){
		Random r = new Random();
		if(p==2&&players[5]!=null){
			getNewPlayer();
		}else if(p==1&&players[5]==null){
			getNewPlayer();
		}else{
			int rand = r.nextInt(8);
			if((rand==1||rand==0)&&p<5)getNewPlayer();
			else if(rand==2){
				if(p>3&&players[5]!=null){
					boolean done = false;
					while(done==false){
						int kick = r.nextInt(players.length-1);
						for(int i = 0;i<players.length-1;i++){
							if(players[i]==null)continue;
							if(i==kick){
								players[i]=null;
								done = true;
								break;
							}
						}
					}
				}else if(p>2){
					boolean done = false;
					while(done==false){
						int kick = r.nextInt(players.length-1);
						for(int i = 0;i<players.length-1;i++){
							if(players[i]==null)continue;
							if(i==kick){
								players[i]=null;
								done = true;
								break;
							}
						}
					}
				}
			}
		}
		
	}
	private void getNewPlayer(){
		Random r = new Random();
		for(int i = 0;i<10;i++){
			int s = r.nextInt(5);
			if(players[s]!=null)continue;
			players[s]=new PokerAI();
			players[s].id=s;
			players[s].money=buyin;
			return;
		}
		getNewPlayer();
	}
	
	private void resetPlayers(){
		for(int i = 0;i<players.length;i++){
			if(players[i]==null)continue;
			players[i].state=0;
		}
	}
	
	private void showdown(){
		boolean[] winners = new CheckVictory().checkVictory(players, cCards);
		int won = 0;
		for(int i = 0;i<winners.length;i++){
			if(winners[i])won++;
		}
		if(won==1){
			for(int i = 0;i<winners.length;i++){
				if(winners[i]==false)continue;
				players[i].money+=pool;
			}
			pool = 0;
		}else{
			int share = pool/won;
			int leftover = pool-(share*won);
			pool = leftover;
			for(int i = 0;i<winners.length;i++){
				if(winners[i]==false)continue;
				players[i].money+=share;
			}
		}
	}
	
	private boolean betLoop(AI ai, long elapsedTime, GameManager gm){
		boolean playerTurn = false;
		if(players[5]!=null){
			if(players[5].turn==true)playerTurn= true;
		}
		if(playerTurn==false){
			boolean bet = ai.loopAI(elapsedTime, this);
			if(bet){
				for(int i = 0;i<players.length;i++){
					if(players[i]!=null){
						pool+=players[i].bet;
						players[i].bet=0;
					}
				}
				return true;
			}
		}else{
			checkPlayerInput(gm);
			if(players[5].turn==false){
				players[nextPlayer(5,players)].turn=true;
			}
		}
		return false;
	}
	
	private void drawing(long elapsedTime, DrawOld d, PokerDraw pd, int state){
		d.draw(elapsedTime);			
		pd.draw(d.g,d.w);
		if(players[5]!=null&&state==11){
			pd.drawContinue();
		}
		//pd.drawAmount(d.dUI.m.x-50, d.dUI.m.y,123456789);
		pd.drawMouse(d.dui.m.x, d.dui.m.y);
		d.drawExitButton();
		pd.render();
	}
	
	private void checkPlayerInput(GameManager gm){
		PokerAI player = players[5];
		int n = gm.dui.getPokerInput();
		if((player.money==0)||player.state==2){
			if(player.money==0&&player.state==0)player.state=1;
			player.turn=false;
			return;
		}
		if(n!=-2){
			if(n==-1){
				playerInput= playerInput/10;
			}else if(n>=0&&n<=9){
				if(playerInput<2100000000);
				playerInput = (playerInput*10)+n;
			}else if(n==10){
				if(playerInput!=0
						&& playerInput+player.bet>highestBet
						&& playerInput <= player.money){
					player.increaseBet(playerInput);
					highestBet=player.bet;
					playerInput=0;
					player.turn=false;
					player.state=1;
				}
				
			}else if(n==11){
				player.state=1;
				player.makeBet(highestBet);
				player.turn=false;
			}else if(n==12){
				player.state=2;
				player.turn=false;
			}
		}
	}
	
	private void startGame(){
		deck = createMainDeck();
		for(int i = 0;i<players.length;i++){
			if(players[i]==null)continue;
			players[i].card1 = getRandomCard();
			players[i].card2 = getRandomCard();
		}
		for(int j = 0;j<cCards.length;j++){
			cCards[j]= getRandomCard();
		}
	}
	
	private PokerAI getSmallBlind(){
		int n = nextPlayer(dealer, players);
		return players[n];
	}
	
	private PokerAI getBigBlind(){
		int s = getSmallBlind().id;
		int n = nextPlayer(s, players);
		return players[n];
	}
	
	public int nextPlayer(int p, PokerAI[] ais){
		if(p==ais.length-1){
			if(ais[0]!=null)return 0;//current - player, next - AI1
			else{
				return nextPlayer(0,ais);//no AI1, get next AI
			}
		}
		int next = p+1; //get next player in normal rotation
		if(ais[next]!=null)return next;
		else{
			return nextPlayer(next,ais);//no next player in normal rotation, find next one
		}
	}
	
	private Card[] createMainDeck(){
		int index = 0;
		Card[] deck = new Card[52];
		for(int i = 1;i<5;i++){
			for(int j = 2;j<15;j++){
				deck[index]= new Card(j,i);
				index++;
			}
		}
		return deck;
	}
	
	private Card getRandomCard(){
		Random r = new Random();
		while(true){
			int c = r.nextInt(52);
			if(deck[c]!=null){
				Card card = deck[c];
				deck[c] = null;
				return card;
			}
		}
	}
	
}
