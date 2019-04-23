import java.io.*;
import java.util.*;
import MBCpackage.*;

class STUPAReader {
	int users,perms;
	STUPACell[][] STUPA;
	Interval[][] TUPA;
	HashMap<Integer, ArrayList<Double>> regionMap;
	ArrayList<Interval> intList;
	SUPACell[][] SUPA;
	HashMap<Interval, Integer[][]> UPAmapTUPA;
	HashMap<Integer, Integer[][]> UPAmapSUPA;

	STUPAReader(String STUPAfile, String planFile) {
		
		regionMap = new HashMap<Integer, ArrayList<Double>>();
		
		generateRegionMap(planFile);
		generateSTUPA(STUPAfile);
		generateTUPAandSUPA();
		
		// printTUPA();
		// printSUPA();

		generateUPAlistFromTUPA();

		// printUPAfromTUPAlist();
		generateUPAlistFromSUPA();

		// printUPAfromSUPAlist();

	}

	void generateTUPAandSUPA() {
		// Initialize SUPA and TUPA
		TUPA = new Interval[users][perms];
		SUPA = new SUPACell[users][perms];

		// go through STUPA and generate TUPA and SUPA
		for(int i = 0; i < users; i++) {
			for(int j = 0; j < perms; j++) {
				TUPA[i][j] = new Interval(STUPA[i][j].interval.min, STUPA[i][j].interval.max);
				SUPA[i][j] = new SUPACell(STUPA[i][j].floorNumber, STUPA[i][j].regionNumber, regionMap.get(STUPA[i][j].regionNumber));
			}
		}
	}

	void generateSTUPA(String STUPAfile) {
		String currLine;
		/* Generate STUPA from the file*/
		try(BufferedReader reader = new BufferedReader(new FileReader(STUPAfile))) {

			currLine = reader.readLine();
			users = Integer.parseInt(currLine);
			currLine = reader.readLine();
			perms = Integer.parseInt(currLine);

			//Skip next few lines and go to the matrix part
			for(int i = 0; i < 6; i++) {
				currLine = reader.readLine();
			}
			
			STUPA = new STUPACell[users][perms];

			for(int i = 0; i < users; i++) {
				int j = 0;
				while(currLine.contains(",") || currLine.contains("0")) {
					String tempCell = currLine;
					if(!tempCell.equals("0")) {
						// System.out.println("Inside if");

						tempCell = tempCell.substring(1, tempCell.length()-1);
						String[] parts = tempCell.split(",");

						parts[0] = parts[0].substring(1);
						parts[1] = parts[1].substring(0,parts[1].length()-1);
						parts[3] = parts[3].substring(0,parts[3].length()-1);

						// System.out.println("fn: " + Integer.parseInt(parts[0]) + ", rn: " + Integer.parseInt(parts[1]));
						STUPACell temp = new STUPACell(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
						STUPA[i][j] = temp;
						// System.out.println( "( " + STUPA[i][j].floorNumber + ", " + STUPA[i][j].regionNumber + " )");
					}

					else {
						// System.out.println("Inside else");
						STUPA[i][j] = new STUPACell();
					}

					currLine = reader.readLine();
					j++;
				}

				currLine = reader.readLine();
			}


		}
		catch(IOException e) {
			System.out.println("Error occured when filling UA or PA");
			e.printStackTrace();
		}
	}

	void printSTUPA() {
		System.out.println("Printing STUPA");
		for(int i = 0; i < users; i++) {
			for(int j = 0; j < perms; j++) {
				System.out.print(STUPA[i][j].floorNumber + ", " + STUPA[i][j].regionNumber + ", (" + STUPA[i][j].interval.min + ", " + STUPA[i][j].interval.max + ")");
			}
		}
	}

	void printTUPA() {
		System.out.println("Printing TUPA");

		for(int i = 0; i < users; i++) {
			for(int j = 0; j < perms; j++) {
				System.out.print("(" + TUPA[i][j].min + "," + TUPA[i][j].max + ")");
			}
			System.out.println();
		}
	}

	void printSUPA() {
		System.out.println("Printing SUPA");

		for(int i = 0; i < users; i++) {
			for(int j = 0; j < perms; j++) {
				System.out.print(SUPA[i][j].regionNumber + ",(" + SUPA[i][j].xCoordinate + "," + SUPA[i][j].yCoordinate + ")  ");
			}
			System.out.println();
		}
		
		// System.out.println("count : " + count);
	}

	void generateRegionMap(String planFile) {
		String currLine;

		ArrayList<Double> emptyList = new ArrayList<Double>();
		emptyList.add(0.0);
		emptyList.add(0.0);
		emptyList.add(0.0);
		emptyList.add(0.0);

		regionMap.put(0, emptyList);

		/* Generate region list corresponding to coordinates */
		try(BufferedReader reader = new BufferedReader(new FileReader(planFile))) {
			int i = 1;
			currLine = reader.readLine();
			while(i <= 4) {
				System.out.println("currLine : " + currLine);
				String tempCell = currLine;
				tempCell = tempCell.replace("(", "");
				tempCell = tempCell.replace(")", "");
				String[] parts = tempCell.split(",");

				ArrayList<Double> locationDetails = new ArrayList<Double>();
				locationDetails.add(Double.parseDouble(parts[1]));
				locationDetails.add(Double.parseDouble(parts[2]));
				locationDetails.add(Double.parseDouble(parts[3]));
				locationDetails.add(Double.parseDouble(parts[4]));

				System.out.println("locationDetails : " + locationDetails);
				regionMap.put(i, locationDetails);
				i++;
				currLine = reader.readLine();

			}

			//printing the regionMap
			System.out.println("Printing the regionMap");
			for(i = 1; i <= 4; i++) {
				System.out.println(i + " : " + regionMap.get(i));
			}
		}
		catch(IOException e) {
			System.out.println("Error occured when opening plan.txt");
			e.printStackTrace();
		}	
	}

	void generateUPAlistFromSUPA() {
		UPAmapSUPA = new HashMap<Integer, Integer[][]>();

		// i here is the region number
		for(int i = 1; i < regionMap.size(); i++) {
			int count1 = 0;
			Integer[][] tempUPA = new Integer[users][perms];
			for(int j = 0; j < users; j++) {
				for(int k = 0; k < perms; k++) {
					count1++;
					if(SUPA[j][k].regionNumber == 0) {
						// System.out.println("no");
						tempUPA[j][k] = 0;
					}
					else if(SUPA[j][k].containsRegion(regionMap.get(i))) {
						// System.out.println("yes");
						tempUPA[j][k] = 1;
					}
					else {
						// System.out.println("maybe");
						tempUPA[j][k] = 0;	
					}
				}
			}
			UPAmapSUPA.put(i , tempUPA);
		}


	}

	void printIntList() {
		// printing interval list
		System.out.println("printing interval list");
		for(int i = 0; i < intList.size(); i++) {
			System.out.print(intList.get(i) + " ");
		}
		System.out.println();
	}

	void generateUPAlistFromTUPA() {
		intList = new ArrayList<Interval>();
		UPAmapTUPA = new HashMap<Interval, Integer[][]>();

		for(int i = 0; i < users; i++) {
			for(int j = 0; j < perms; j++) {
				if(TUPA[i][j].length() != 0 && !intList.contains(TUPA[i][j])) {
					intList.add(TUPA[i][j]);
				}
			}
		}

		// printIntList();

		for(int i = 0; i < intList.size(); i++) {
			Integer[][] tempUPA = new Integer[users][perms];
			for(int j = 0; j < users; j++) {
				for(int k = 0; k < perms; k++) {
					if(TUPA[j][k].min == 0 && TUPA[j][k].max == 0) {
						// System.out.println("Is zero");
					}
					if(TUPA[j][k].containsInterval(intList.get(i))) {
						// System.out.println("###");
						tempUPA[j][k] = 1;
					}
					else {
						// System.out.println("!!!");
						tempUPA[j][k] = 0;	
					}
				}
			}

			UPAmapTUPA.put(intList.get(i) , tempUPA);
		}
	}

	void printUPAfromTUPAlist() {
		// printing list of UPAs
		System.out.println("printing list of UPAs");
		for(int i = 0; i < intList.size(); i++) {
			System.out.print("Interval: ");
			System.out.println(intList.get(i));

			for(int j = 0; j < users; j++){
				for(int k = 0; k < perms; k++) {
					System.out.print(UPAmapTUPA.get(intList.get(i))[j][k] + " ");
				}
				System.out.println();
			}
			System.out.println();
		}
	}

	void printUPAfromSUPAlist() {

		for(int i = 1; i < regionMap.size(); i++) {
			System.out.println("Region: ");
			System.out.println(regionMap.get(i));

			for(int j = 0; j < users; j++){
				for(int k = 0; k < perms; k++) {
					System.out.print(UPAmapSUPA.get(i)[j][k] + " ");
				}
				System.out.println();
			}

			System.out.println();

		}
	}
}