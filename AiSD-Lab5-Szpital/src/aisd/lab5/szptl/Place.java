package aisd.lab5.szptl;

public class Place {
	
	private int id;
	private String name;
	private int x, y;
	
	public Place(int id, String name, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.name = name;
	}
	
	public int getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}
}
