package graphics;

public class MathTool {
	
	public int getDigits(long a){
		if(a>=0&&a<10){
			return 1;
		}else if(a>=10&&a<100){
			return 2;
		}else if(a>=100&&a<1000){
			return 3;
		}else if(a>=1000&&a<10000){
			return 4;
		}else if(a>=10000&&a<100000){
			return 5;
		}else if(a>=100000&&a<1000000){
			return 6;
		}else if(a>=1000000&&a<10000000){
			return 7;
		}else if(a>=10000000&&a<100000000){
			return 8;
		}else if(a>=100000000&&a<1000000000){
			return 9;
		}else if(a>=1000000000&&a<10000000000L){
			return 10;
		}

		return 1;
	}
}
