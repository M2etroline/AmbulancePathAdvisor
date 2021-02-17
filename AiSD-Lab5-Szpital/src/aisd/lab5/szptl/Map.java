package aisd.lab5.szptl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Map {

	private String path;
	private List<Hospital> hospital = new ArrayList<Hospital>();
	private List<Place> place = new ArrayList<Place>();
	private List<Cross> cross = new ArrayList<Cross>();
	private List<Road> road = new ArrayList<Road>();
	private List<Road> road_org = new ArrayList<Road>();
	private Point min_y = new Point(0, 1000000);
	private LinkedList<Point> edge = new LinkedList<Point>();
	private LinkedList<Point> all = new LinkedList<Point>();

	public Map(String path) {
		this.path = path;
	}

	public List<Hospital> getHospital() {
		return hospital;
	}

	public List<Cross> getCross() {
		return cross;
	}

	public void findCrosses() {
		int cr_indx = 0;
		List<Road> in_use = new ArrayList<Road>();
		sortRoadByX();
		int cur_x;
		for (int i = 0; i < road.size(); i++) {
			cur_x = road.get(i).A.x;
			in_use.add(road.get(i));
			for (int j = 0; j < in_use.size(); j++) {
				if (in_use.get(j) == road.get(i)) {
					continue;
				}
				if (doIntersectNotCool(road.get(i).A, road.get(i).B, in_use.get(j).A, in_use.get(j).B)) {
					Point temp = findIntersectionPoint(road.get(i).A, road.get(i).B, in_use.get(j).A, in_use.get(j).B);
					if (!((temp.x == in_use.get(j).A.x && temp.y == in_use.get(j).A.y)
							|| (temp.x == in_use.get(j).B.x && temp.y == in_use.get(j).B.y))) {
						int repeated_cr_indx = cr_indx;
						for (int k = 0; k < cross.size(); k++) {
							if (cross.get(k).getX() == temp.x && cross.get(k).getY() == temp.y) {
								repeated_cr_indx = k;
								break;
							}
						}
						adjustRoads(road.get(i), temp, repeated_cr_indx);
						adjustRoads(in_use.get(j), temp, repeated_cr_indx);
						if (road.get(i).getTypeA() == road.get(i).getTypeB()
								&& road.get(i).indxA == road.get(i).indxB) {
							road.remove(i);
							i--;
						}
						if (repeated_cr_indx == cr_indx) {
							cross.add(new Cross(cr_indx++, temp.x, temp.y));
						}
					}
				}
				if (in_use.get(j).B.x < cur_x) {
					in_use.remove(j);
					j--;
				}
			}
		}
		syncronizeRoadWithHospitalAndCross();
	}

	public static double findLength(Point A, Point B) {
		double length = Math.sqrt(Math.pow(A.x - B.x, 2) + Math.pow(A.y - B.y, 2));
		return length;
	}

	public boolean isInside(Patient n) {
		int num = 0;
		Point pat = new Point(n.getX(), n.getY());
		Point temp1 = new Point(-1000001, pat.y + 1);
		for (int i = 1; i < edge.size(); i++) {
			if (edge.get(i).x == n.getX() && edge.get(i).y == n.getY()) {
				return true;
			}
			if (doIntersect(temp1, pat, edge.get(i - 1), edge.get(i))) {
				num++;
			}
		}
		if (num % 2 == 1) {
			return true;
		}
		return false;
	}

	public boolean makeEdge() {
		if (all.size() < 3) {
			return false;
		}
		all.removeIf(n -> (n.y == min_y.y && n.x == min_y.x));
		all.forEach(n -> {
			double a = Math.atan2(n.y - min_y.y, n.x - min_y.x);
			n.angle = a;
		});
		sortAll();
		edge.add(min_y);
		edge.add(all.getFirst());
		int j = 0;
		for (int i = 1; i < all.size(); i++) {
			while (orientation(edge.get(j), edge.get(j + 1), all.get(i)) >= 0) {
				if (j > 0) {
					edge.remove(j + 1);
					j--;
				} else {
					break;
				}
			}
			edge.add(all.get(i));
			j++;
		}
		edge.add(edge.getFirst());
		return true;
	}

	public boolean generateTXTFile() {
		try {
			PrintWriter writer = new PrintWriter("Map.txt");
			for (int i = 0; i < edge.size(); i++) {
				writer.print(edge.get(i).x + " " + edge.get(i).y);
				if (i + 1 < edge.size()) {
					writer.print(" | ");
				}
			}
			writer.println();
			hospital.forEach((n) -> {
				writer.println("S" + n.getID() + " | " + n.getName() + " | " + n.getX() + " | " + n.getY());
			});
			cross.forEach((n) -> {
				writer.println("K" + n.getID() + " | " + n.getX() + " | " + n.getY());
			});
			place.forEach((n) -> {
				writer.println("O" + n.getID() + " | " + n.getName() + " | " + n.getX() + " | " + n.getY());
			});
			for (int i = 0; i < road_org.size() - 1; i++) {
				writer.println("D" + i + " | " + road_org.get(i).indxA + " | " + road_org.get(i).indxB);
			}
			int last_indx = road_org.size() - 1;
			writer.print(
					"D" + last_indx + " | " + road_org.get(last_indx).indxA + " | " + road_org.get(last_indx).indxB);
			writer.close();
		} catch (FileNotFoundException e) {
			return false;
		}
		return true;
	}

	private void syncronizeRoadWithHospitalAndCross() {
		road.forEach(n -> {
			if (n.getTypeA() == 'S') {
				hospital.get(n.indxA).setValue(String.valueOf(n.getTypeB()) + n.indxB, n.getValue());
			}
			if (n.getTypeB() == 'S') {
				hospital.get(n.indxB).setValue(String.valueOf(n.getTypeA()) + n.indxA, n.getValue());
			}
			if (n.getTypeA() == 'C') {
				cross.get(n.indxA).setValue(String.valueOf(n.getTypeB()) + n.indxB, n.getValue());
			}
			if (n.getTypeB() == 'C') {
				cross.get(n.indxB).setValue(String.valueOf(n.getTypeA()) + n.indxA, n.getValue());
			}
		});
	}

	private void adjustRoads(Road r, Point temp, int cr_indx) {
		int o_value;
		o_value = r.getValue();
		int o_length = (int) Math.round(findLength(r.A, r.B));
		double n_length = (int) Math.round(findLength(r.A, temp));
		int n_value = (int) Math.round(o_value * n_length / o_length);
		Point new_end = new Point(r.B.x, r.B.y);
		int left_value = o_value - n_value;
		putNewRoadInRightPlace(new Road(temp, new_end, left_value, cr_indx, r.indxB, 'C', r.getTypeB()));
		r.B = temp;
		r.indxB = cr_indx;
		r.setTypeB('C');
		r.setValue(n_value);
	}

	private void putNewRoadInRightPlace(Road r) {
		boolean completion = false;
		for (int i = 0; i < road.size(); i++) {
			if (r.A.x < road.get(i).A.x) {
				road.add(i, r);
				completion = true;
				break;
			}
		}
		if (!completion) {
			road.add(r);
		}
	}

	private static Point findIntersectionPoint(Point A, Point B, Point C, Point D) {
		double a1 = B.y - A.y;
		double b1 = A.x - B.x;
		double c1 = a1 * (A.x) + b1 * (A.y);
		double a2 = D.y - C.y;
		double b2 = C.x - D.x;
		double c2 = a2 * (C.x) + b2 * (C.y);
		double determinant = a1 * b2 - a2 * b1;
		int x = (int) Math.round((b2 * c1 - b1 * c2) / determinant);
		int y = (int) Math.round((a1 * c2 - a2 * c1) / determinant);
		return new Point(x, y);
	}

	private static boolean onSegment(Point p, Point q, Point r) {
		if (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) && q.y <= Math.max(p.y, r.y)
				&& q.y >= Math.min(p.y, r.y))
			return true;
		return false;
	}

	private static int orientation(Point p, Point q, Point r) {
		double val = (double) ((q.y - p.y)) * (double) ((r.x - q.x)) - (double) ((q.x - p.x)) * (double) ((r.y - q.y));
		if (val == 0) {
			return 0;
		}
		return (val > 0) ? 1 : -1;
	}

	private static boolean doIntersect(Point p1, Point q1, Point p2, Point q2) {
		int o1 = orientation(p1, q1, p2);
		int o2 = orientation(p1, q1, q2);
		int o3 = orientation(p2, q2, p1);
		int o4 = orientation(p2, q2, q1);
		if (o1 != o2 && o3 != o4) {
			return true;
		}
		if (o1 == 0 && onSegment(p1, p2, q1)) {
			return true;
		}
		if (o2 == 0 && onSegment(p1, q2, q1)) {
			return true;
		}
		if (o3 == 0 && onSegment(p2, p1, q2)) {
			return true;
		}
		if (o4 == 0 && onSegment(p2, q1, q2)) {
			return true;
		}
		return false;
	}

	private static boolean doIntersectNotCool(Point p1, Point q1, Point p2, Point q2) {
		int o1 = orientation(p1, q1, p2);
		int o2 = orientation(p1, q1, q2);
		int o3 = orientation(p2, q2, p1);
		int o4 = orientation(p2, q2, q1);
		if (o1 != o2 && o3 != o4) {
			return true;
		}
		return false;
	}

	private void sortRoadByX() {
		Collections.sort(road, new Comparator<Road>() {
			@Override
			public int compare(Road r1, Road r2) {
				return r1.A.x - r2.A.x;
			}
		});
	}

	private void sortAll() {
		Collections.sort(all, new Comparator<Point>() {
			@Override
			public int compare(Point s1, Point s2) {
				return s1.angle - s2.angle > 0 ? 1 : -1;
			}
		});
	}

	public boolean read() {
		int comm_number = 0;
		int sect_number = 0;
		int temp_length = 0;
		int id = 0;
		String name = new String();
		int x = 0;
		int y = 0;
		int all_bed = 0;
		int idv;
		int idh1 = 0;
		int idh2 = 0;
		int dist = 0;
		int contin = 0;
		try {
			FileInputStream fis = new FileInputStream(path);
			Scanner sc = new Scanner(fis);
			while (sc.hasNextLine()) {
				char[] line = sc.nextLine().toCharArray();
				temp_length = 0;
				char[] temp = new char[line.length];
				if (line.length == 0) {
					continue;
				}
				if (line[0] == '#') {
					contin = 0;
					comm_number++;
					continue;
				}
				if (comm_number == 1) {
					for (int i = 0; i < line.length; i++) {
						if (line[i] != '|' && i + 1 != line.length) {
							temp[temp_length++] = line[i];
						} else {
							temp_length = 0;
							sect_number++;
							String nstr = new String(temp);
							Arrays.fill(temp, ' ');
							nstr = nstr.trim();
							try {
								if (sect_number == 1) {
									id = Integer.parseInt(nstr);
									if (contin++ != id) {
										System.err.println("Blad w linii: " + new String(line));
										sc.close();
										return false;
									}
								} else if (sect_number == 2) {
									name = nstr;
									name = name.trim();
								} else if (sect_number == 3) {
									x = Integer.parseInt(nstr);
								} else if (sect_number == 4) {
									y = Integer.parseInt(nstr);
								} else if (sect_number == 5) {
									all_bed = Integer.parseInt(nstr);
								} else if (i + 1 == line.length) {
									String nnstr = (nstr + line[i]).trim();
									int rest = Integer.parseInt(nnstr);
									int mil = 1000000;
									if (all_bed > mil || rest > mil || x < -mil || x > mil || y < -mil || y > mil
											|| name.length() > 100 || id >= 1000) {
										System.err.println("Blad w linii1: " + new String(line));
										sc.close();
										return false;
									}
									if (min_y.y > y) {
										min_y.y = y;
										min_y.x = x;
									}
									all.add(new Point(x, y));
									if (rest > all_bed) {
										sc.close();
										System.err.println("Blad w linii: " + new String(line));
										return false;
									}
									hospital.add(new Hospital(id, name, x, y, all_bed, rest));
								}
							} catch (NumberFormatException e) {
								System.err.println("Blad w linii: " + new String(line));
								sc.close();
								return false;
							}
						}
					}
					if (sect_number != 6) {
						System.err.println("Blad w linii: " + new String(line));
						sc.close();
						return false;
					}
					sect_number = 0;
				} else if (comm_number == 2) {
					for (int i = 0; i < line.length; i++) {
						if (line[i] != '|' && i + 1 != line.length) {
							temp[temp_length++] = line[i];
						} else {
							temp_length = 0;
							sect_number++;
							String nstr = new String(temp);
							Arrays.fill(temp, ' ');
							nstr = nstr.trim();
							try {
								if (sect_number == 1) {
									id = Integer.parseInt(nstr);
									if (contin++ != id) {
										System.err.println("Blad w linii: " + new String(line));
										sc.close();
										return false;
									}
								} else if (sect_number == 2) {
									name = nstr;
									name = name.trim();
								} else if (sect_number == 3) {
									x = Integer.parseInt(nstr);
								} else if (i + 1 == line.length) {
									String nnstr = (nstr + line[i]).trim();
									y = Integer.parseInt(nnstr);
									int mil = 1000000;
									if (x < -mil || x > mil || y < -mil || y > mil || name.length() > 100
											|| id >= 1000) {
										System.err.println("Blad w linii: " + new String(line));
										sc.close();
										return false;
									}
									if (min_y.y > y) {
										min_y.y = y;
										min_y.x = x;
									}
									all.add(new Point(x, y));
									place.add(new Place(id, name, x, y));
								}
							} catch (NumberFormatException e) {
								System.err.println("Blad w linii: " + new String(line));
								sc.close();
								return false;
							}
						}
					}
					if (sect_number != 4) {
						System.err.println("Blad w linii: " + new String(line));
						sc.close();
						return false;
					}
					sect_number = 0;
				} else if (comm_number == 3) {
					for (int i = 0; i < line.length; i++) {
						if (line[i] != '|' && i + 1 != line.length) {
							temp[temp_length++] = line[i];
						} else {
							temp_length = 0;
							sect_number++;
							String nstr = new String(temp);
							Arrays.fill(temp, ' ');
							nstr = nstr.trim();
							try {
								if (sect_number == 1) {
									idv = Integer.parseInt(nstr);
									if (contin++ != idv) {
										System.err.println("Blad w linii: " + new String(line));
										sc.close();
										return false;
									}
								} else if (sect_number == 2) {
									idh1 = Integer.parseInt(nstr);
								} else if (sect_number == 3) {
									idh2 = Integer.parseInt(nstr);
								} else if (i + 1 == line.length) {
									String nnstr = (nstr + line[i]).trim();
									dist = Integer.parseInt(nnstr);
									if (idh1 >= hospital.size() || idh2 >= hospital.size() || idh1 == idh2
											|| id >= 1000000) {
										System.err.println("Blad w linii: " + new String(line));
										sc.close();
										return false;
									}
									if (hospital.get(idh1).getX() > hospital.get(idh2).getX()) {
										int t = idh1;
										idh1 = idh2;
										idh2 = t;
									}
									Point A = new Point(hospital.get(idh1).getX(), hospital.get(idh1).getY());
									Point B = new Point(hospital.get(idh2).getX(), hospital.get(idh2).getY());
									road.add(new Road(A, B, dist, idh1, idh2, 'S', 'S'));
									road_org.add(new Road(A, B, dist, idh1, idh2, 'S', 'S'));
								}
							} catch (NumberFormatException e) {
								System.err.println("Blad w linii: " + new String(line));
								sc.close();
								return false;
							}
						}
					}
					if (sect_number != 4) {
						System.err.println("Blad w linii: " + new String(line));
						sc.close();
						return false;
					}
					sect_number = 0;
				}
			}
			sc.close();
			if (comm_number != 3) {
				System.err.println("Bledna ilosc sekcji. Powinno byc 3, a jest " + comm_number);
				return false;
			}
			if (road.size() == 0) {
				System.err.println("Powinna byc co najmniej jedna droga");
				return false;
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
