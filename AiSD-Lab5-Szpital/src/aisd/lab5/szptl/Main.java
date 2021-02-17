package aisd.lab5.szptl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static boolean readPatients(String path, List<Patient> pt) {
		int id = 0;
		int x = 0;
		int y = 0;
		int sect_number = 0;
		int temp_length = 0;
		int contin = 0;
		int comm_number = 0;
		try {
			File myObj = new File(path);
			Scanner sc = new Scanner(myObj);
			while (sc.hasNextLine()) {
				char[] line = sc.nextLine().toCharArray();
				char[] temp = new char[line.length];
				if (line.length == 0) {
					continue;
				}
				if (line[0] == '#') {
					comm_number++;
					continue;
				}
				if (comm_number == 1) {
					sect_number = 0;
					for (int i = 0; i < line.length; i++) {
						if (line[i] != '|' && i + 1 != line.length) {
							temp[temp_length++] = line[i];
						} else {
							if (i + 1 == line.length) {
								temp[temp_length] = line[i];
							}
							sect_number++;
							String nstr = new String(temp);
							Arrays.fill(temp, ' ');
							nstr = nstr.trim();
							try {
								if (sect_number == 1) {
									id = Integer.parseInt(nstr);
								} else if (sect_number == 2) {
									x = Integer.parseInt(nstr);
								} else if (i + 1 == line.length) {
									y = Integer.parseInt(nstr);
									int mil = 1000000;
									if (x < -mil || x > mil || y < -mil || y > mil || id != contin++) {
										System.err.println("Blad w linii: " + new String(line));
										sc.close();
										return false;
									}
									Patient new_patient = new Patient(id, x, y);
									pt.add(new_patient);
								}
							} catch (NumberFormatException e) {
								System.err.println("Blad w linii: " + new String(line));
								sc.close();
								return false;
							}
							temp_length = 0;
						}
					}
					if (sect_number != 3) {
						System.err.println("Blad w linii: " + new String(line));
						sc.close();
						return false;
					}
				}
			}
			sc.close();
			if (comm_number != 1) {
				System.err.println("Bledna ilosc sekcji. Powinna byc 1, a jest " + comm_number);
				return false;
			}
		} catch (IOException e) {
			System.err.println("Nie udalo sie znalezc pliku " + path);
			return false;
		}
		return true;
	}

	public static void writePatients(PrintWriter writer, List<Patient> pt, int key) {
		for (int i = 0; i < pt.size(); i++) {
			writer.write(i + " " + pt.get(i).getX() + " " + pt.get(i).getY() + " |");
			for (int j = 0; j < pt.get(i).history.size(); j++) {
				if (pt.get(i).history.get(j) > key - 1) {
					writer.write(" " + String.valueOf("K" + (pt.get(i).history.get(j) - key)));
				} else {
					writer.write(" " + String.valueOf("S" + pt.get(i).history.get(j)));
				}
			}
			if(i+1<pt.size()) {
				writer.println();
			}
		}
	}

	public static boolean managePatients(String path, FindHospital fh) {
		List<Patient> patient = new ArrayList<Patient>();
		PrintWriter writer = null;

		if (!readPatients(path, patient)) {
			return false;
		}

		for (int i = 0; i < patient.size(); i++) {
			if (fh.isInside(patient.get(i))) {
				fh.getPlace(patient.get(i));
			}
		}

		try {
			writer = new PrintWriter("Result.txt");
			writePatients(writer, patient, fh.getCode());
			writer.close();
		} catch (IOException e) {
			System.err.println("Nie udalo sie zapisac rezultatu do pliku wyjciowego.");
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		if (args.length == 2) {
			Map map = new Map(args[0]);
			if (map.read() == true) {
				if(map.makeEdge() == true) {
					map.findCrosses();
				if (!map.generateTXTFile()) {
					System.err.println("Nie mozna stworzyc pliku przejsciowego. ");
				}
				FindHospital fh = new FindHospital(map);
				if (managePatients(args[1], fh) != true) {
					System.err.println("Program zostal zatrzymany. ");
				}
				} else {
					System.err.println("Zbyt malo punktow by stworzyc panstwo");
				}
				
			} else {
				System.err.println("Nie udalo sie wczytac pliku " + args[0]);
			}
		}
	}
}
