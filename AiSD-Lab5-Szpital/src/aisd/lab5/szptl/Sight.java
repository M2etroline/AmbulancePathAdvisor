package aisd.lab5.szptl;

public interface Sight {
	public int getID();

	public int getY();

	public int getX();
	
	public void sort();
	
	public int getValue(String key);
	
	public String getNextKey();

	public void setValue(String key, int value);
	
	public int getRoadCount();
}
