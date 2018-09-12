package poker;

public class PokerAI {
	
	protected int bet = 0;
	protected int id = 0;
	protected Card card1;
	protected Card card2;
	protected boolean turn = false;
	protected int money = 100;
	protected int state = 0; //0 - ready, 1 - check, 2 - fold MAKE ALL-IN STATE
	
	/**
	 * amount is the total bet
	 */
	public void makeBet(int amount){
		if(amount-bet>money){
			bet+=money;
			money=0;
			return;
		}
		money-=(amount-bet);
		bet=amount;
	}
	
	public void increaseBet(int amount){
		if(amount>money){
			bet+=money;
			money=0;
			return;
		}
		money-=amount;
		bet+=amount;
	}
	
}
