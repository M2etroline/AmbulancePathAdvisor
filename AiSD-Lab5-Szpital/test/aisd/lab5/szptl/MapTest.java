package aisd.lab5.szptl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.Test;

public class MapTest {

	@Test
	public void shouldNotReadWhenThereIsNoFile() {
		// given
		boolean result;
		Map map = new Map(" ");

		// when
		result = map.read();

		// then
		if (result) {
			assert false;
		} else {
			assert true;
		}
	}

	@Test
	public void shouldNotReadWhenThereIsNotRightAmountOfHash() {
		boolean result = true;
		try {
			File myObj = new File("test.txt");
			myObj.createNewFile();
			FileWriter myWriter = new FileWriter("test.txt");
			myWriter.write("# Szpitale (id | nazwa | wsp. x | wsp. y | Liczba ùóýek | Liczba wolnych ùóýek)"
					+ System.getProperty("line.separator"));
			myWriter.write("0 | Szpital Wojewódzki nr 997 | 25 | 0 | 1000 | 0" + System.getProperty("line.separator"));
			myWriter.write("# Obiekty (id | nazwa | wsp. x | wsp. y)" + System.getProperty("line.separator"));
			myWriter.write("0 | Pomnik Fryderyka Chopina | 110 | 55" + System.getProperty("line.separator"));
			myWriter.close();
			Map map = new Map("test");
			result = map.read();
			myObj.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (result) {
			assert false;
		} else {
			assert true;
		}
	}

	@Test
	public void shouldNotReadWhenThereIsBreakInContinuityOfIndexation() {
		boolean result = true;
		try {
			File myObj = new File("test.txt");
			myObj.createNewFile();
			FileWriter myWriter = new FileWriter("test.txt");
			myWriter.write("# Szpitale (id | nazwa | wsp. x | wsp. y | Liczba ùóýek | Liczba wolnych ùóýek)" + System.getProperty("line.separator"));
			myWriter.write("0 | Szpital Wojewódzki nr 997 | 25 | 0 | 1000 | 0" + System.getProperty("line.separator"));
			myWriter.write("2 | TUTAJ JEST BLAD | 27 | 20 | 1000 | 0" + System.getProperty("line.separator"));
			myWriter.write("# Obiekty (id | nazwa | wsp. x | wsp. y)" + System.getProperty("line.separator"));
			myWriter.write("0 | Pomnik Fryderyka Chopina | 110 | 55" + System.getProperty("line.separator"));
			myWriter.write("1 | Pomnik A | 111 | 56" + System.getProperty("line.separator"));
			myWriter.write("# Drogi (id | id_szpitala | id_szpitala | odlegùoúã)" + System.getProperty("line.separator"));
			myWriter.write("0 | 0 | 1 | 700" + System.getProperty("line.separator"));
			myWriter.close();
			Map map = new Map("test.txt");
			result = map.read();
			myObj.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (result) {
			assert false;
		} else {
			assert true;
		}
	}

	@Test
	public void shouldNotReadWhenThereIsNotInitializedIndexInRoad() {
		boolean result = true;
		try {
			File myObj = new File("test.txt");
			myObj.createNewFile();
			FileWriter myWriter = new FileWriter("test.txt");
			myWriter.write("# Szpitale (id | nazwa | wsp. x | wsp. y | Liczba ùóýek | Liczba wolnych ùóýek)" + System.getProperty("line.separator"));
			myWriter.write("0 | Szpital Wojewódzki nr 997 | 25 | 0 | 1000 | 0" + System.getProperty("line.separator"));
			myWriter.write("1 | Szpital Grzeczny | 27 | 20 | 1000 | 0" + System.getProperty("line.separator"));
			myWriter.write("# Obiekty (id | nazwa | wsp. x | wsp. y)" + System.getProperty("line.separator"));
			myWriter.write("0 | Pomnik Fryderyka Chopina | 110 | 55" + System.getProperty("line.separator"));
			myWriter.write("1 | Pomnik A | 111 | 56" + System.getProperty("line.separator"));
			myWriter.write("# Drogi (id | id_szpitala | id_szpitala | odlegùoúã)" + System.getProperty("line.separator"));
			myWriter.write("0 | 0 | 2 | 700" + System.getProperty("line.separator"));
			myWriter.close();
			Map map = new Map("test.txt");
			result = map.read();
			myObj.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (result) {
			assert false;
		} else {
			assert true;
		}
	}

	@Test
	public void shouldNotReadWhenLineFormatIsIncorrect() {
		boolean result = true;
		try {
			File myObj = new File("test.txt");
			myObj.createNewFile();
			FileWriter myWriter = new FileWriter("test.txt");
			myWriter.write("# Szpitale (id | nazwa | wsp. x | wsp. y | Liczba ùóýek | Liczba wolnych ùóýek)" + System.getProperty("line.separator"));
			myWriter.write("0 | Szpital Wojewódzki nr 997 | 25 | 0 | 1000 | 0" + System.getProperty("line.separator"));
			myWriter.write("1 | Szpital Grzeczny | 27 | 20 | 1000 | 0" + System.getProperty("line.separator"));
			myWriter.write("# Obiekty (id | nazwa | wsp. x | wsp. y)" + System.getProperty("line.separator"));
			myWriter.write("0 | TUTAJ JEST BLAD | 110 | 55 | 15" + System.getProperty("line.separator"));
			myWriter.write("1 | Pomnik A | 111 | 56" + System.getProperty("line.separator"));
			myWriter.write("# Drogi (id | id_szpitala | id_szpitala | odlegùoúã)" + System.getProperty("line.separator"));
			myWriter.write("0 | 0 | 2 | 700" + System.getProperty("line.separator"));
			myWriter.close();
			Map map = new Map("test.txt");
			result = map.read();
			myObj.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (result) {
			assert false;
		} else {
			assert true;
		}
	}
	
	@Test
	public void shouldNotReadWhenThereIsOtherSymbolInNumberPlace() {
		boolean result = true;
		try {
			File myObj = new File("test.txt");
			myObj.createNewFile();
			FileWriter myWriter = new FileWriter("test.txt");
			myWriter.write("# Szpitale (id | nazwa | wsp. x | wsp. y | Liczba ùóýek | Liczba wolnych ùóýek)" + System.getProperty("line.separator"));
			myWriter.write("0 | Szpital Wojewódzki nr 997 | 25 | 0 | 1000 | 0" + System.getProperty("line.separator"));
			myWriter.write("1 | Szpital Grzeczny | 27 | 20 | TUTAJ_JEST_BLAD | 0" + System.getProperty("line.separator"));
			myWriter.write("# Obiekty (id | nazwa | wsp. x | wsp. y)" + System.getProperty("line.separator"));
			myWriter.write("0 | Pomnik B | 110 | 55 | 15" + System.getProperty("line.separator"));
			myWriter.write("1 | Pomnik A | 111 | 56" + System.getProperty("line.separator"));
			myWriter.write("# Drogi (id | id_szpitala | id_szpitala | odlegùoúã)" + System.getProperty("line.separator"));
			myWriter.write("0 | 0 | 2 | 700" + System.getProperty("line.separator"));
			myWriter.close();
			Map map = new Map("test.txt");
			result = map.read();
			myObj.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (result) {
			assert false;
		} else {
			assert true;
		}
	}
	
	@Test
	public void shouldNotReadWhenThereIsMoreFreeBedsThanActual() {
		boolean result = true;
		try {
			File myObj = new File("test.txt");
			myObj.createNewFile();
			FileWriter myWriter = new FileWriter("test.txt");
			myWriter.write("# Szpitale (id | nazwa | wsp. x | wsp. y | Liczba ùóýek | Liczba wolnych ùóýek)" + System.getProperty("line.separator"));
			myWriter.write("0 | Szpital Wojewódzki nr 997 | 25 | 0 | 1000 | 0" + System.getProperty("line.separator"));
			myWriter.write("1 | TUTAJ JEST BLAD | 27 | 20 | 5 | 2000" + System.getProperty("line.separator"));
			myWriter.write("# Obiekty (id | nazwa | wsp. x | wsp. y)" + System.getProperty("line.separator"));
			myWriter.write("0 | Pomnik B | 110 | 55 | 15" + System.getProperty("line.separator"));
			myWriter.write("1 | Pomnik A | 111 | 56" + System.getProperty("line.separator"));
			myWriter.write("# Drogi (id | id_szpitala | id_szpitala | odlegùoúã)" + System.getProperty("line.separator"));
			myWriter.write("0 | 0 | 2 | 700" + System.getProperty("line.separator"));
			myWriter.close();
			Map map = new Map("test.txt");
			result = map.read();
			myObj.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (result) {
			assert false;
		} else {
			assert true;
		}
	}
	
	@Test
	public void shouldNotReadWhenThereIsToMuchHospitals() {
		boolean result = true;
		try {
			File myObj = new File("test.txt");
			myObj.createNewFile();
			FileWriter myWriter = new FileWriter("test.txt");
			myWriter.write("# Szpitale (id | nazwa | wsp. x | wsp. y | Liczba ùóýek | Liczba wolnych ùóýek)" + System.getProperty("line.separator"));
			for (int i = 0; i < 1000;i++) {
				myWriter.write(i + " | Szpital A nr " + i + " | " + i + "  | 0 | 1000 | 0" + System.getProperty("line.separator"));
			}
			myWriter.write(1000 + " | TUTAJ JEST BLAD " + 1000 + "  | 0 | 1000 | 0" + System.getProperty("line.separator"));
			myWriter.write("# Obiekty (id | nazwa | wsp. x | wsp. y)" + System.getProperty("line.separator"));
			myWriter.write("0 | Pomnik B | 110 | 55 | 15" + System.getProperty("line.separator"));
			myWriter.write("1 | Pomnik A | 111 | 56" + System.getProperty("line.separator"));
			myWriter.write("# Drogi (id | id_szpitala | id_szpitala | odlegùoúã)" + System.getProperty("line.separator"));
			myWriter.write("0 | 0 | 2 | 700" + System.getProperty("line.separator"));
			myWriter.close();
			Map map = new Map("test.txt");
			result = map.read();
			myObj.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (result) {
			assert false;
		} else {
			assert true;
		}
	}
}
