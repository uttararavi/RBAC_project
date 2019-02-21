import java.util.*;
import MBCpackage.*;
import java.io.*;


public class TUPA {
	public static void main(String[] args) {

		String TUPAfile = "nonTemporal_Dataset/TUPA_test.txt";

		ReadTUPAdata TUPAreader = new ReadTUPAdata(TUPAfile);

		// Interval[][] timeMatrix = new Interval[row][col];


		// for(int i = 0; i < row; i++) {
		// 	for(int j = 0; j < col; j++) {
		// 		timeMatrix
		// 	}
		// }
		// Scanner s = new Scanner(System.in);


		int row = 4;
		int col = 3;

		ArrayList<Interval> intList = new ArrayList<Interval>();
		Interval[][] timeMatrix = new Interval[row][col];

		// int row = s.nextInt();
		// int col = s.nextInt();

		timeMatrix[0][0] = new Interval(8,10);
		timeMatrix[0][1] = new Interval(12,14);
		timeMatrix[0][2] = new Interval(0,0);
		timeMatrix[1][0] = new Interval(0,0);
		timeMatrix[1][1] = new Interval(10,14);
		timeMatrix[1][2] = new Interval(15,20);
		timeMatrix[2][0] = new Interval(0,0);
		timeMatrix[2][1] = new Interval(0,0);
		timeMatrix[2][2] = new Interval(12,16);
		timeMatrix[3][0] = new Interval(0,0);
		timeMatrix[3][1] = new Interval(17,23);
		timeMatrix[3][2] = new Interval(17,21);


		for(int i = 0; i < row; i++) {
			for(int j = 0; j < col; j++) {
				if(timeMatrix[i][j].length() != 0 && !intList.contains(timeMatrix[i][j])) {
					intList.add(timeMatrix[i][j]);
				}
			}
		}

		// for(int i = 0; i < intList.size(); i++) {
		// 	System.out.print(intList.get(i) + " ");
		// }

		// System.out.println();

		int[][][] UPAlist = new int[intList.size()][row][col];

		for(int i = 0; i < intList.size(); i++) {
			for(int j = 0; j < row; j++) {
				for(int k = 0; k < col; k++) {
					if(timeMatrix[j][k].containsInterval(intList.get(i))) {
						UPAlist[i][j][k] = 1;
					}
				}
			}
		}


		//printing list of UPAs

		for(int i = 0; i < intList.size(); i++) {
			System.out.println(intList.get(i));
			for(int j = 0; j < row; j++){
				for(int k = 0; k < col; k++) {
					System.out.print(UPAlist[i][j][k] + " ");
				}
				System.out.println();
			}
			System.out.println("####");
		}

	}
}