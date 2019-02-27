import java.util.*;
import MBCpackage.*;
import java.io.*;


public class TUPAReader {

	int users, perms, roles;
	HashMap<Interval, Integer[][]> UPAmap;
	Interval[][] TUPA;

	TUPAReader(String TUPAfile) {

		UPAmap = new HashMap<Interval, Integer[][]>();
		String currLine;

		try(BufferedReader reader = new BufferedReader(new FileReader(TUPAfile))) {
			// System.out.println("Hello Uttara");
			currLine = reader.readLine();
			users = Integer.parseInt(currLine);
			currLine = reader.readLine();
			perms = Integer.parseInt(currLine);

			TUPA = new Interval[users][perms];

			currLine = reader.readLine();
			int i = 0;

			while(currLine != null) {
				String[] intRow = currLine.split(",");

				for(int j = 0; j < perms; j++) {

					String intCell = intRow[j];
					intCell.trim();
					intCell = intCell.substring(1,intCell.length()-1);
					String[] cell = intCell.split("-");
					TUPA[i][j] = new Interval(Integer.parseInt(cell[0]) , Integer.parseInt(cell[1]));
				}

				i++;
				currLine = reader.readLine();

			}
				
		}
		catch(IOException e) {
			System.out.println("Error occured when filling TUPA");
			e.printStackTrace();	
		}

		//printing TUPA
		// System.out.println("printing TUPA");
		// for(int i = 0; i < users; i++) {
		// 	for(int j = 0; j < perms; j++) {
		// 		System.out.print(TUPA[i][j].min + "," + TUPA[i][j].max + "  ");
		// 	}

		// 	System.out.println();
		// }


		createUPAlist();

	}

	void createUPAlist() {

		// System.out.println("printing TUPA 2");
		// for(int i = 0; i < users; i++) {
		// 	for(int j = 0; j < perms; j++) {
		// 		System.out.print(TUPA[i][j].min + "," + TUPA[i][j].max + "  ");
		// 	}

		// 	System.out.println();
		// }

		ArrayList<Interval> intList = new ArrayList<Interval>();

		for(int i = 0; i < users; i++) {
			for(int j = 0; j < perms; j++) {
				if(TUPA[i][j].length() != 0 && !intList.contains(TUPA[i][j])) {
					intList.add(TUPA[i][j]);
				}
			}
		}

		// printing interval list
		System.out.println("printing interval list");
		for(int i = 0; i < intList.size(); i++) {
			System.out.print(intList.get(i) + " ");
		}
		System.out.println();

		for(int i = 0; i < intList.size(); i++) {
			// System.out.println("curr interval : " + intList.get(i).min + "," + intList.get(i).max);

			Integer[][] tempUPA = new Integer[users][perms];
			for(int j = 0; j < users; j++) {
				for(int k = 0; k < perms; k++) {
					if(TUPA[j][k].containsInterval(intList.get(i))) {
						tempUPA[j][k] = 1;
					}
					else {
						tempUPA[j][k] = 0;	
					}
				}
			}

			UPAmap.put(intList.get(i) , tempUPA);
		}

		// printing list of UPAs
		System.out.println("printing list of UPAs");
		for(int i = 0; i < intList.size(); i++) {
			System.out.print("Interval: ");
			System.out.println(intList.get(i));

			for(int j = 0; j < users; j++){
				for(int k = 0; k < perms; k++) {
					System.out.print(UPAmap.get(intList.get(i))[j][k] + " ");
				}
				System.out.println();
			}
			System.out.println();
		}

	}
}