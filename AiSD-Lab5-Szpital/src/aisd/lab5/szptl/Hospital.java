package aisd.lab5.szptl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Hospital implements Sight {
	private int id;
	private String name;
	private int x, y;
	private int bed_count;
	private int bed_left;
	private LinkedHashMap<String, Integer> all_possible = new LinkedHashMap<String, Integer>();
	private int idx_next_sorted = 0;

	public Hospital(int id, String name, int x, int y, int all, int left) {
		this.id = id;
		this.name = name;
		this.x = x;
		this.y = y;
		bed_count = all;
		bed_left = left;
	}

	public int getID() {
		return id;
	}

	public void reserveBed() {
		bed_left--;
	}

	public String getName() {
		return name;
	}

	public int getBedLeft() {
		return bed_left;
	}

	public int getBedCount() {
		return bed_count;
	}

	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}

	public void sort() {
		List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(all_possible.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(java.util.Map.Entry<String, Integer> o1, java.util.Map.Entry<String, Integer> o2) {
				return o1.getValue() - o2.getValue();
			}
		});
		all_possible.clear();
		for (Map.Entry<String, Integer> entry : entries) {
			all_possible.put(entry.getKey(), entry.getValue());
		}
	}

	public int getValue(String key) {
		for (String k : all_possible.keySet()) {
			if (k.contentEquals(key)) {
				return all_possible.get(k);
			}
		}
		return 0;
	}

	public String getNextKey() {
		int temp = 0;
		for (String key : all_possible.keySet()) {
			if (temp == idx_next_sorted) {
				idx_next_sorted++;
				return key;
			}
			temp++;
		}
		idx_next_sorted = 0;
		return null;
	}

	public void setValue(String key, int value) {
		all_possible.put(key, value);
	}
	
	public int getRoadCount() {
		return all_possible.size();
	}
}
