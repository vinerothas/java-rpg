package poker;

public class Betting {
	
	private long elapsedTime = 0;
	private final int turnTime = 700;
	
	public boolean firstBet(long et, PokerLoop pl){
		
		elapsedTime+=et;
		if(elapsedTime<turnTime)return false;
		
		elapsedTime = 0;
			
		int p = pl.dealer;
		while(true){
			if(pl.highestBet!=0)return true;
			p = nextPlayer(p,pl.players);
			
			int blind = nextPlayer(pl.dealer,pl.players);
			if(p==blind){
				if(pl.players[p].bet!=0)continue;
				else {
					pl.players[p].makeBet(pl.wager/2);
					return false;
				}
			}else if(p==nextPlayer(blind,pl.players)){
				if(pl.players[p].bet!=0)continue;
				else {
					pl.players[p].makeBet(pl.wager);
					pl.highestBet=pl.wager;
					return true;
				}
			}
			
		}	
		
	}
	
	public int nextPlayer(int p, PokerAI[] ais){
		if(p==5){
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
}
