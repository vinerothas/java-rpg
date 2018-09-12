package poker;

public class Card {
	
	protected int number; //2-2 ... 10-10 J-11 Q-12 K-13 A-14
	protected int sign; //1 - club(acorn)	2 - diamond		3 - heart	4 - spade(sword)
	protected int visible = 0;//0 - invisible	1-flipped	2-visible
	
	public Card(int n, int s){
		number = n;
		sign = s;
	}
}
