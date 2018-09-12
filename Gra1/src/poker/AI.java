package poker;

import java.util.Random;

public class AI {
	
	private long elapsedTime = 0;
	private final long turnTime = 700;
	
	public boolean loopAI(long eT, PokerLoop pl){
		elapsedTime+=eT;
		if(elapsedTime<turnTime)return false;
		elapsedTime = 0;
		
		boolean finished = junctionAI(pl);
		return finished;
	}
	
	private boolean junctionAI(PokerLoop pl){
		PokerAI ai = findTurnAI(pl);
		if(ai.state==2){
			ai.turn=false;
			PokerAI nextAI = findNextTurn(pl,ai);
			if(nextAI.state ==1&&nextAI.bet==pl.highestBet){
				return true;
			}else{
				nextAI.turn=true;
			}
		}
		if(ai.state==1){
			if(ai.bet==pl.highestBet){
				ai.turn = false;
				return true;
			}else{
				ai.state=0;
			}
		}
		if(ai.state==0){
			runAI(ai,pl);
			PokerAI nextAI = findNextTurn(pl,ai);
			if(nextAI.state ==1&&nextAI.bet==pl.highestBet){
				return true;
			}else{
				nextAI.turn=true;
			}
		}	
		
		return false;
	}
	
	public void runAI(PokerAI ai, PokerLoop pl){
		Random r = new Random();
		
		int rand = r.nextInt(8);
		if(ai.bet==pl.highestBet){
			rand+=1;
		}
		if(rand==0){	//fold
			System.out.println("AI"+ai.id+" folded");
			ai.state= 2;
		}else if(rand==1){	//rise
			System.out.println("AI"+ai.id+" rised");
			ai.state=1;
			if(ai.bet==pl.highestBet){
				ai.makeBet(ai.bet*2);
				pl.highestBet=ai.bet;
			}else{
				ai.makeBet(pl.highestBet*2);
				pl.highestBet=ai.bet;
			}
		}else{	//	check/equal bet
			ai.state=1;
			if(ai.bet==pl.highestBet){
				System.out.println("AI"+ai.id+" checked");
			}else{
				System.out.println("AI"+ai.id+" called");
				ai.makeBet(pl.highestBet);
			}
		}
		
		ai.turn=false;
	}
	
	protected void resetStates(PokerLoop pl){
		for(int i = 0;i<pl.players.length;i++){
			if(pl.players[i]==null)continue;
			if(pl.players[i].state!=2&&pl.players[i].money!=0)pl.players[i].state=0;
			
		}
	}
	
	private PokerAI findTurnAI(PokerLoop pl){
		for(int i = 0;i<pl.players.length;i++){
			if(pl.players[i]==null)continue;
			if(pl.players[i].turn==true)return pl.players[i];
		}
		return null;
	}

	private PokerAI findNextTurn(PokerLoop pl, PokerAI ai){
		int next = pl.nextPlayer(ai.id, pl.players);
		if(pl.players[next].state!=2){
			return pl.players[next];
		}else{
			return findNextTurn(pl,pl.players[next]);
		}
		
	}
}
