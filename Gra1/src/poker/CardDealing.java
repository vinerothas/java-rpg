package poker;

public class CardDealing {
	
	private long elapsedTime = 0;
	private final long turnTime = 300; //400
	
	/**
	 * Return true when finished
	 */
	public boolean deal(long et, PokerLoop pl){
		
		elapsedTime+=et;
		if(elapsedTime<turnTime)return false;
		elapsedTime = 0;
		
		PokerAI player = pl.players[5];
		if(player!=null)
		if(player.card1.visible==0){
			player.card1.visible=2;
			return false;
		}
		
		for(int i = 0;i<pl.players.length;i++){
			if(pl.players[i]==null)continue;
			if(pl.players[i].card1.visible==0){
				pl.players[i].card1.visible=1;
				return false;
			}
		}
		
		if(player!=null)
		if(player.card2.visible==0){
			player.card2.visible=2;
			return false;
		}
		
		for(int j = 0;j<pl.players.length;j++){
			if(pl.players[j]==null)continue;
			if(pl.players[j].card2.visible==0){
				pl.players[j].card2.visible=1;
				return false;
			}
		}
		
		for(int k = 0;k<3;k++){
			if(pl.cCards[k].visible==0){
				pl.cCards[k].visible=1;
				return false;
			}
		}
		
		return true;
	}
	
	public boolean flop(long et, PokerLoop pl){
		
		elapsedTime+=et;
		if(elapsedTime<turnTime)return false;
		elapsedTime = 0;
		
		for(int k = 0;k<3;k++){
			if(pl.cCards[k].visible==1){
				pl.cCards[k].visible=2;
				return false;
			}
		}
		
		return true;
	}
	
	public boolean turn(long et, PokerLoop pl){
		
		elapsedTime+=et;
		if(elapsedTime<turnTime)return false;
		elapsedTime = 0;
		
		if(pl.cCards[3].visible==0){
			pl.cCards[3].visible=1;
			return false;
		}else if(pl.cCards[3].visible==1){
			pl.cCards[3].visible=2;
			return false;
		}
			
		return true;
	}
	
	public boolean river(long et, PokerLoop pl){
		
		elapsedTime+=et;
		if(elapsedTime<turnTime)return false;
		elapsedTime = 0;
		
		if(pl.cCards[4].visible==0){
			pl.cCards[4].visible=1;
			return false;
		}else if(pl.cCards[4].visible==1){
			pl.cCards[4].visible=2;
			return false;
		}
			
		return true;
	}
	
	public boolean showdown(long et, PokerLoop pl){
		
		elapsedTime+=et;
		if(elapsedTime<turnTime)return false;
		elapsedTime = 0;
		
		for(int i = 0;i<pl.players.length-1;i++){
			if(pl.players[i]==null)continue;
			if(pl.players[i].state==2)continue;
			if(pl.players[i].card1.visible==1){
				pl.players[i].card1.visible=2;
				pl.players[i].card2.visible=2;
				return false;
			}
		}
		
		return true;
	}
	
}
