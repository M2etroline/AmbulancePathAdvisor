package aisd.lab5.szptl;

public class Road {
	Point A;
	Point B;
	int indxA, indxB;
	private char typeA, typeB;
	private int value;

	public Road(Point A, Point B, int value, int indxA, int indxB, char typeA, char typeB) {
		this.A = A;
		this.B = B;
		this.indxA=indxA;
		this.indxB=indxB;
		this.value = value;
		this.typeA = typeA;
		this.typeB = typeB;
	}
	
	public int getValue() {
		return value;
	}
	
	public char getTypeA() {
		return typeA;
	}
	
	public char getTypeB() {
		return typeB;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public void setTypeA(char type) {
		this.typeA = type;
	}
	
	public void setTypeB(char type) {
		this.typeB = type;
	}
}
