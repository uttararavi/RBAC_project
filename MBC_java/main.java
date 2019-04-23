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

		/* Does the STUPA splitting */

		/* Make a list of regions by reading plan.txt */

		String STUPAfile = "nonTemporal_Dataset/STUPA_test.txt";

		STUPAReader stupaReader = new STUPAReader(STUPAfile, "plan.txt");

		// ####### TUPA Role Mining ######## //
		HashMap<Interval, ArrayList<Role>> intRoleSetMap = new HashMap<Interval, ArrayList<Role>>();
		Set<Role> combinedRoleSet = new HashSet<Role>();

		intRoleSetMap = getIntervalRSMap(stupaReader, intRoleSetMap, combinedRoleSet);

		// REB creation for TUPA
		HashMap<Role, ArrayList<Interval>> REB = new HashMap<Role, ArrayList<Interval>>();
		REB = generateREBforTUPA(REB, combinedRoleSet, stupaReader, intRoleSetMap);

		// ####### SUPA Role Mining ######## //
		HashMap<Integer, ArrayList<Role>> regionRoleSetMap = new HashMap<Integer, ArrayList<Role>>();
		Set<Role> combinedRoleSet2 = new HashSet<Role>();

		regionRoleSetMap = getRegionRSMap(stupaReader, regionRoleSetMap, combinedRoleSet2);

		// REB creation for SUPA
		HashMap<Role, ArrayList<Integer>> REB2 = new HashMap<Role, ArrayList<Integer>>();
		REB2 = generateREBforSUPA(REB2, combinedRoleSet2, stupaReader, regionRoleSetMap);

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
			miner.DisplayRoles();

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

	public static HashMap<Role, ArrayList<Integer>> generateREBforSUPA(HashMap<Role, ArrayList<Integer>> REB2, Set<Role> combinedRoleSet2, STUPAReader stupaReader, HashMap<Integer, ArrayList<Role>> regionRoleSetMap) {
		Iterator<Role> itr = combinedRoleSet2.iterator();

		while(itr.hasNext()) {
			ArrayList<Integer> empty = new ArrayList<Integer>();
			REB2.put(itr.next() , empty);
		}

		// go through the region RS map, and add the region for every role it has

		for(int i = 1; i <= regionRoleSetMap.size(); i++) {
			for(int j = 0; j < regionRoleSetMap.get(i).size(); j++) {
				Role r = regionRoleSetMap.get(i).get(j);

				REB2.get(r).add(i);
			}
		}

		/*** Printing the REB2 ***/
		itr = combinedRoleSet2.iterator();
		int count = 0;
		while(itr.hasNext()) {

			System.out.print("r" + count + ": ");
			Role r = itr.next();
			System.out.print(r.user + "   " + r.perm + "-->    ");

			for(int j = 0; j < REB2.get(r).size(); j++) {
				System.out.print(REB2.get(r).get(j) + "  ");
			}
			System.out.println();
			count++;
		}

		return REB2;
	}

	public static HashMap<Integer, ArrayList<Role>> getRegionRSMap(STUPAReader stupaReader, HashMap<Integer, ArrayList<Role>> regionRoleSetMap, Set<Role> combinedRoleSet2) {
		for(int i = 1; i <= stupaReader.UPAmapSUPA.size(); i++) {

			int[][] firstUPAfromSUPA = new int[stupaReader.users][stupaReader.perms];

			for(int j = 0; j < stupaReader.users; j++) {
				for(int k = 0; k < stupaReader.perms; k++) {
					firstUPAfromSUPA[j][k] = stupaReader.UPAmapSUPA.get(i)[j][k].intValue();
				}
			}

			// printing first UPA from TUPA 
			// System.out.println("i : " + i);
			// for(int j = 0; j < stupaReader.users; j++) {
			// 	for(int k = 0; k < stupaReader.perms; k++) {
			// 		System.out.print(firstUPAfromSUPA[j][k] + " ");
			// 	}
			// 	System.out.println();
			// }

			// System.out.println("##############");
			// System.out.println();

			MBC miner = new MBC(firstUPAfromSUPA);
			combinedRoleSet2.addAll(miner.roleSet);

			// miner.DisplayRoles();
			
			for (Role r: miner.roleSet) {
				combinedRoleSet2.add(r);
			}


			
			// add to the map
			regionRoleSetMap.put(i, miner.roleSet);

		}

			return regionRoleSetMap;
	}
	
}
