/******************************
\MAIN METHOD TO TEST THE MBC PACKAGE-
Read the Dataset, parse it using ReadDataset class to obtain UA and PA,
Perform MBC on the UPA from the ReadDataset object, regenerate the UPA using the roles,
Compare the original UPA with the regerated UPA.
******************************/

import java.util.*;
import MBCpackage.*;
import java.io.*;

class main{
	public static void main(String args[]) throws IOException
	{
		long startTime = System.nanoTime();
		/****
		BLOCK 1 : Uncomment two following blocks - 
		One out of Test input 1 and Test input 2
		COMMON block 
		****/
		/*Test input 1
		int[][] UPA = {{1,1,1,0,0},{1,1,1,1,0},{0,0,1,1,1},{0,1,1,0,0},{0,1,0,1,0},{0,1,0,0,1}};
		//*/
		
		/*Test input 2
		int[][] UPA = {{1,0,1,0,0},{1,0,0,0,1},{0,0,0,0,0},{1,0,1,0,0}};
		//*/
		
		/*COMMON : Uncomment this for both test input 1 and test input 2
		int row = UPA.length;
		int col = UPA[0].length;
		//*/
		/*__________________________END OF BLOCK 1_______________________________*/

		/****
		BLOCK2 : Uses real datasets. Uncomment the entire block for useage
		****/
		// System.out.print("Enter the name of the Dataset: "); /*Get the name of the dataset.
		// Names of the datasets are stored in 0_README_for_datasets*/
		// BufferedReader screenReader =  new BufferedReader(new InputStreamReader(System.in)); 
		// String commonString = screenReader.readLine();
		// String UAfile = "nonTemporal_Dataset/UA_"+commonString+".txt";
		// String PAfile = "nonTemporal_Dataset/PA_"+commonString+".txt";
		// //We have obtained the filename of the UA and PA for the selected dataset.
		// ReadDataset reader = new ReadDataset(UAfile,PAfile);
		// /*ReadDataset is a class that reads the UA and PA and computes the UPA.
		// The UPA is stored as a class member variable. Hence call "reader.UPA" to access the UPA.*/

		// //print UPA
		// System.out.println("UPA");

		// for(int i = 0; i < reader.users; i++) {
		// 	for(int j = 0; j < reader.perms; j++) {
		// 		System.out.print(reader.UPA[i][j]);
		// 	} 
		// 	System.out.println();
		// }

		/* Does the TUPA splitting */
		// String TUPAfile = "nonTemporal_Dataset/TUPA_test.txt";

		// TUPAReader tupaReader = new TUPAReader(TUPAfile);

		/* Does the STUPA splitting */

		/* Make a list of regions by reading plan.txt */



		String STUPAfile = "nonTemporal_Dataset/STUPA_test.txt";

		STUPAReader stupaReader = new STUPAReader(STUPAfile, "plan.txt");

		HashMap<Interval, ArrayList<Role>> intRoleSetMap = new HashMap<Interval, ArrayList<Role>>();
		/*********/
		Set<Role> combinedRoleSet = new HashSet<Role>();

		intRoleSetMap = getIntervalRSMap(stupaReader, intRoleSetMap, combinedRoleSet);

		// // Print intRoleSetMap
		// for(int i = 0; i < intRoleSetMap.size(); i++) {

		// 	System.out.println("Interval: " + stupaReader.intList.get(i));

		// 	for(int j = 0; j < intRoleSetMap.get(stupaReader.intList.get(i)).size(); j++) {
		// 		Role r = intRoleSetMap.get(stupaReader.intList.get(i)).get(j);
		// 		System.out.println("r" + j +" : " + r.user + "    " + r.perm);
		// 	}
		// }

		// REB creation for TUPA
		HashMap<Role, ArrayList<Interval>> REB = new HashMap<Role, ArrayList<Interval>>();
		REB = generateREBforTUPA(REB, combinedRoleSet, stupaReader, intRoleSetMap);


		

	}

	public static HashMap<Interval, ArrayList<Role>> getIntervalRSMap (STUPAReader stupaReader, HashMap<Interval, ArrayList<Role>> intRoleSetMap, Set<Role> combinedRoleSet) {

		for(int i = 0; i < stupaReader.UPAmapTUPA.size(); i++) {
			int[][] firstUPAfromTUPA = new int[stupaReader.users][stupaReader.perms];

			for(int j = 0; j < stupaReader.users; j++) {
				for(int k = 0; k < stupaReader.perms; k++) {
					firstUPAfromTUPA[j][k] = stupaReader.UPAmapTUPA.get(stupaReader.intList.get(i))[j][k].intValue();
				}
			}

			// printing first UPA from TUPA 
			// for(int j = 0; j < stupaReader.users; j++) {
			// 	for(int k = 0; k < stupaReader.perms; k++) {
			// 		System.out.print(firstUPAfromTUPA[j][k] + " ");
			// 	}
			// 	System.out.println();
			// }

			MBC miner = new MBC(firstUPAfromTUPA);
			// combinedRoleSet.addAll(miner.roleSet);
			for (Role r: miner.roleSet) {
				combinedRoleSet.add(r);
			}
			
			// add to the map 
			intRoleSetMap.put(stupaReader.intList.get(i) ,miner.roleSet);
			// miner.DisplayRoles();

		}

		return intRoleSetMap;
	}

	public static HashMap<Role, ArrayList<Interval>> generateREBforTUPA(HashMap<Role, ArrayList<Interval>> REB, Set<Role> combinedRoleSet, STUPAReader stupaReader, HashMap<Interval, ArrayList<Role>> intRoleSetMap) {
		Iterator<Role> itr = combinedRoleSet.iterator();

		while(itr.hasNext()) {
			ArrayList<Interval> empty = new ArrayList<Interval>();
			REB.put(itr.next() , empty);
		}

		// go through the interval RS map, and add the interval for every role it has

		for(int i = 0; i < stupaReader.intList.size(); i++) {
			for(int j = 0; j < intRoleSetMap.get(stupaReader.intList.get(i)).size(); j++) {
				Role r = intRoleSetMap.get(stupaReader.intList.get(i)).get(j);

				REB.get(r).add(stupaReader.intList.get(i));
			}
		}

		/*** Printing the REB ***/
		// itr = combinedRoleSet.iterator();
		// int count = 0;
		// while(itr.hasNext()) {

		// 	System.out.print("r" + count + ": ");
		// 	Role r = itr.next();
		// 	System.out.print(r.user + "   " + r.perm + "-->");

		// 	for(int j = 0; j < REB.get(r).size(); j++) {
		// 		System.out.print("(" + REB.get(r).get(j).min + "," + REB.get(r).get(j).max + ") ; ");
		// 	}
		// 	System.out.println();
		// 	count++;
		// }

		return REB;
	}
	
}
