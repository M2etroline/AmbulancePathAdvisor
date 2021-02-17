package aisd.lab5.szptl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FindHospital {

	private Map map;

	public FindHospital(Map map) {
		this.map = map;
	}

	public boolean verifyOrAllTrue(List<Boolean> list) {
		for (Boolean i : list) {
			if (!i) {
				return true;
			}
		}
		return false;
	}

	public int getCode() {
		return map.getHospital().size();
	}

	public int getIndexOfMinimumValue(List<Integer> list, List<Boolean> bool) {
		int minIndex = bool.lastIndexOf(false);
		for (int i = 0; i < list.size(); i++) {
			if ((!bool.get(i)) && (list.get(i) < list.get(minIndex))) {
				minIndex = i;
			}
		}
		return minIndex;
	}

	public boolean getPlace(Patient pt) {
		int x = pt.getX();
		int y = pt.getY();

		List<Point> hospitalPoints = new ArrayList<Point>();
		List<Double> distance = new ArrayList<Double>();
		List<Hospital> hospital = new ArrayList<Hospital>();
		List<Cross> cross = new ArrayList<Cross>();
		List<Integer> value = new ArrayList<Integer>();
		List<Sight> all = new ArrayList<Sight>();
		List<Boolean> shortestWay = new ArrayList<Boolean>();
		List<Boolean> shortestWayOrg = new ArrayList<Boolean>();
		List<Integer> roadTo = new ArrayList<Integer>();
		List<Integer> history = new ArrayList<Integer>();

		hospital = map.getHospital();
		cross = map.getCross();
		Point patient = new Point(x, y);

		for (int i = 0; i < hospital.size(); i++) {
			hospitalPoints.add(new Point(hospital.get(i).getX(), hospital.get(i).getY()));
			distance.add(Map.findLength(hospitalPoints.get(i), patient));
			all.add(hospital.get(i));
			value.add(Integer.MAX_VALUE);
			shortestWay.add(false);
			shortestWayOrg.add(false);
			if (hospital.get(i).getRoadCount() == 0) {
				shortestWayOrg.set(i, true);
			}
			roadTo.add(0);
		}

		for (int i = 0; i < cross.size(); i++) {
			all.add(cross.get(i));
			value.add(Integer.MAX_VALUE);
			shortestWay.add(false);
			shortestWayOrg.add(false);
			roadTo.add(0);
		}

		int iAmHere = distance.indexOf(Collections.min(distance));
		if (hospital.get(iAmHere).getBedLeft() > 0 || hospital.get(iAmHere).getRoadCount() == 0) {
			pt.addToHistory(iAmHere);
			hospital.get(iAmHere).reserveBed();
			return true;
		} else {
			int start = iAmHere;
			value.set(iAmHere, 0);
			shortestWay.set(iAmHere, true);
			String key;
			int index = 0;
			roadTo.set(iAmHere, 0);
			int sum;
			while (verifyOrAllTrue(shortestWay)) {
				while ((key = all.get(iAmHere).getNextKey()) != null) {
					try {
						String temp = key.substring(1);
						index = Integer.parseInt(temp);
						if (key.toCharArray()[0] == 'C') {
							index += hospital.size();
						}
					} catch (NumberFormatException e) {
						System.err.println("Bledny format danych");
						return false;
					}
					if ((!shortestWay.get(index))
							&& ((sum = all.get(iAmHere).getValue(key) + value.get(iAmHere)) < value.get(index))) {
						value.set(index, sum);
						roadTo.set(index, iAmHere);
					}
				}
				iAmHere = getIndexOfMinimumValue(value, shortestWay);
				if(value.get(iAmHere)==Integer.MAX_VALUE) {
					return true;
				}
				shortestWay.set(iAmHere, true);
				if (iAmHere < hospital.size() && (!pt.checkHistory(iAmHere))) {
					int goback = iAmHere;
					while ((goback = roadTo.get(goback)) != start) {
						history.add(goback);
					}

					if ((pt.history.size() == 0) || (pt.history.lastIndexOf(start) != pt.history.size() - 1)) {
						history.add(start);
					}
					for (int i = history.size() - 1; i >= 0; i--) {
						pt.addToHistory(history.get(i));
					}
					pt.addToHistory(iAmHere);
					history.clear();
					if (hospital.get(iAmHere).getBedLeft() > 0) {
						hospital.get(iAmHere).reserveBed();
						return true;
					} else {
						start = iAmHere;
						Collections.fill(shortestWay, false);
						shortestWay.clear();
						shortestWay.addAll(shortestWayOrg);
						Collections.fill(value, Integer.MAX_VALUE);
						Collections.fill(roadTo, 0);
						value.set(iAmHere, 0);
						roadTo.set(iAmHere, 0);
						shortestWay.set(iAmHere, true);
						sum = 0;
					}
				}
			}
		}
		return true;
	}

	public boolean isInside(Patient pt) {
		return map.isInside(pt);
	}

}
