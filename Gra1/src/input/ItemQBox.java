package input;

public class ItemQBox {
	
	private int x;
	private int y;
	private int ox;
	private int oy;
	private int cx;
	private int cy;
	private int ax;
	private int ay;
	private int dux;
	private int uy;
	private int dy;
	private int pos;
	private int quantity=1;
	private boolean sell = false;
	
	public ItemQBox(int x, int y,int pos){
		this.x = x;
		this.y = y;
		ox=x+38;
		oy=y+93;
		ax=x+83;
		ay=oy;
		cx=x+129;
		cy=oy;
		dux = x+108;
		uy = y+46;
		dy = uy+12;
		this.pos = pos;
	}

	public int getOx() {
		return ox;
	}
	public int getOy() {
		return oy;
	}

	public int getCx() {
		return cx;
	}
	public int getCy() {
		return cy;
	}
	public int getAx() {
		return ax;
	}
	public int getAy() {
		return ay;
	}
	public int getDy() {
		return dy;
	}
	public int getUy() {
		return uy;
	}
	public int getDux() {
		return dux;
	}

	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getPos(){
		return pos;
	}

	public int getQuantity() {		
		return quantity;
	}

	public void setQuantity(int q) {
		quantity = q;
	}
	public void upQuantity(){
		if(quantity<99){
			quantity++;
		}
	}
	public void downQuantity(){
		if(quantity>0){
			quantity--;
		}
	}
	public boolean getSell() {
		return sell;
	}

	public void setSell(boolean b) {
		sell = b;
	}
	
	public boolean oClicked(int mx, int my){
		if(mx>ox&& mx<ox+33&& my>oy&&my<oy+14){
			return true;
		}
		return false;
	}
	public boolean aClicked(int mx, int my){
		if(mx>ax&& mx<ax+33&& my>ay&&my<ay+14){
			return true;
		}
		return false;
	}
	public boolean cClicked(int mx, int my){
		if(mx>cx&& mx<cx+33&& my>cy&&my<cy+14){
			return true;
		}
		return false;
	}
	public boolean uClicked(int mx, int my){
		if(mx>dux&& mx<dux+12&& my>uy&&my<uy+8){
			return true;
		}
		return false;
	}
	public boolean dClicked(int mx, int my){
		if(mx>dux&& mx<dux+12&& my>dy&&my<dy+8){
			return true;
		}
		return false;
	}

}
