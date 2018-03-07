package ScotlandYardFrame;

import java.awt.Color;

public enum Turn {
	A(0, Color.CYAN), B(1, Color.BLUE), C(2, Color.ORANGE), D(3, Color.MAGENTA), E(4, Color.PINK), MrX(99, Color.BLACK);

	public final int NUM;
	public final Color COLOR;

	Turn(int i, Color c) {
		NUM = i;
		COLOR = c;
	}

	public Turn next() {
		if (this == A)
			return B;
		else if (this == B)
			return C;
		else if (this == C)
			return D;
		else if (this == D)
			return E;
		else if (this == E)
			return MrX;
		else
			return A;
	}
	
	public static Turn identify(int i){
		if(i==0)
			return A;
		else if(i==1)
			return B;
		else if(i==2)
			return C;
		else if(i==3)
			return D;
		else if(i==4)
			return E;
		else
			return MrX;
	}
	
}
