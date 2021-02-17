package aisd.lab5.szptl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Cross implements Sight {
	private int id;
	private int x, y;
	private LinkedHashMap<String, Integer> all_possible = new LinkedHashMap<String, Integer>();
	private int idx_next_sorted = 0;

	public Cross(int id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}

	public int getID() {
		return id;
	}

	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}

	public void setValue(String id, int length) {
		boolean f = false;
		for (String k : all_possible.keySet()) {
			if (k.contentEquals(id)) {
				all_possible.put(k, length);
				f = true;
			}
		}
		if (!f) {
			all_possible.put(id, length);
		}
	}

	public void removeId(String id) {
		String to_rem = "";
		for (String k : all_possible.keySet()) {
			if (k.contentEquals(id)) {
				to_rem = k;
			}
		}
		all_possible.remove(to_rem);
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
	public int getRoadCount() {
		return all_possible.size();
	}
}
