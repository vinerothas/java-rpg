package poker;

public class Score {
	protected int hand = 0;
	protected int[]	kickers;
	protected int[]	heights;
	
	public Score(int h,int[]k,int[]he){
		hand = h;
		kickers = k;
		heights = he;
	}
}
