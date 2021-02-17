package aisd.lab5.szptl;

import java.util.ArrayList;

public class Patient {
	private int id, x, y;
	public ArrayList<Integer> history = new ArrayList<Integer>();

	public Patient(int id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}

	public int getId() {
		return id;
	}

	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}

	public void addToHistory(int x) {
		history.add(x);
	}

	public boolean checkHistory(int x) {
		return history.contains(x);
	}
}