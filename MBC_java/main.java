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

		// combine Roles from combinedRoleSet and combinedRoleSet2

		Iterator<Role> itr = combinedRoleSet.iterator();
		Iterator<Role> itr2 = combinedRoleSet2.iterator();

		ArrayList<RoleFinal> ultRS = new ArrayList<RoleFinal>();

		// Define a final role which has interval list and region number list as well

		
		while(itr.hasNext()) {
			Role rt = itr.next();
			itr2 = combinedRoleSet2.iterator();	
			while(itr2.hasNext()) {
				Role rs = itr2.next();
				// System.out.println(rt.user + " --> " + rt.perm);
				// System.out.println(rs.user + " --> " + rs.perm);
				Role temp = new Role();
				temp.user.addAll(rt.user);
				temp.perm.addAll(rt.perm);

				temp.user.retainAll(rs.user);
				temp.perm.retainAll(rs.perm);

				// System.out.println(temp.user + " --> " + temp.perm);

				if(temp.user.size() == 0 || temp.perm.size() == 0) {
					// System.out.println("No ");
				}

				else {
					// System.out.println("Yes ");
					RoleFinal newRole = new RoleFinal();
					newRole.user = temp.user;
					newRole.perm = temp.perm;
					newRole.region.addAll(REB2.get(rs));
					for(int i = 0; i < REB.get(rt).size(); i++) {
						Interval tempInterval = new Interval(REB.get(rt).get(i).min, REB.get(rt).get(i).max);
						newRole.timeInt.add(tempInterval);
					}
					ultRS.add(newRole);
				}

				// System.out.println("---------------------------");
			}

		}
		
		Boolean canMerge = false;
		do {
			for(int i = 0; i < ultRS.size()-1; i++) {
				for(int j = i+1; j < ultRS.size(); j++) {

					int l1 = ultRS.size();
					ultRS = checkCompatible(ultRS, i, j);
					int l2 = ultRS.size();

					if(l1 > l2) {
						canMerge = true;
					}

					else {
						canMerge = false;
					}
				}
			}
		} while (canMerge);

		// print all the new "RoleFinals"

		for(int i = 0; i < ultRS.size(); i++) {
			System.out.print("r" + i + " ");
			System.out.print(ultRS.get(i).user + "  ");
			System.out.println(ultRS.get(i).perm + "  ");

			System.out.print("int : ");
			for(int j = 0; j < ultRS.get(i).timeInt.size(); j++) {
				System.out.print("(" + ultRS.get(i).timeInt.get(j).min + "," + ultRS.get(i).timeInt.get(j).max + ") ");
			}
			System.out.println();
			System.out.print("region : ");
			for(int j = 0; j < ultRS.get(i).region.size(); j++) {
				System.out.print(ultRS.get(i).region.get(j) + "  ");
			}
			System.out.println();
			System.out.println("---------------------");

		}

		System.out.println("Total roles : " + ultRS.size());

		// for(int i = 0; i < ultRS.size()-1; i++) {
		// 	for(int j = i+1; j < ultRS.size(); j++) {
		// 		ultRS = checkCompatible(ultRS, i, j);
		// 	}
		// }

		long endTime = System.nanoTime();

		long totalTime = endTime - startTime;
		System.out.println(totalTime/Math.pow(10,9)+" seconds");

	}

	public static ArrayList<RoleFinal> checkCompatible (ArrayList<RoleFinal> ultRS, int i, int j) {
		// user, time, region 
		if(ultRS.get(i).user.equals(ultRS.get(j).user) && 
			ultRS.get(i).region.equals(ultRS.get(j).region) &&
			ultRS.get(i).timeInt.equals(ultRS.get(j).timeInt)) {
				ultRS.get(i).perm.addAll(ultRS.get(j).perm);
				ultRS.remove(j);	
				// System.out.println("Needs further merging");
		}

		// user, perm, region 
		else if(ultRS.get(i).user.equals(ultRS.get(j).user) && 
			ultRS.get(i).region.equals(ultRS.get(j).region) &&
			ultRS.get(i).perm.equals(ultRS.get(j).perm)) {
				ultRS.get(i).timeInt.addAll(ultRS.get(j).timeInt);
				ultRS.remove(j);
				// System.out.println("Needs further merging");

		}

		// user, perm, time
		else if(ultRS.get(i).user.equals(ultRS.get(j).user) && 
			ultRS.get(i).perm.equals(ultRS.get(j).perm) &&
			ultRS.get(i).timeInt.equals(ultRS.get(j).timeInt)) {
				ultRS.get(i).region.addAll(ultRS.get(j).region);
				ultRS.remove(j);
				// System.out.println("Needs further merging");

		}

		// perm, time, region
		else if(ultRS.get(i).perm.equals(ultRS.get(j).perm) && 
			ultRS.get(i).region.equals(ultRS.get(j).region) &&
			ultRS.get(i).timeInt.equals(ultRS.get(j).timeInt)) {
				ultRS.get(i).user.addAll(ultRS.get(j).user);
				ultRS.remove(j);
				// System.out.println("Needs further merging");

		}

		return ultRS;
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

		// /*** Printing the REB ***/
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

		// /*** Printing the REB2 ***/
		// itr = combinedRoleSet2.iterator();
		// int count = 0;
		// while(itr.hasNext()) {

		// 	System.out.print("r" + count + ": ");
		// 	Role r = itr.next();
		// 	System.out.print(r.user + "   " + r.perm + "-->    ");

		// 	for(int j = 0; j < REB2.get(r).size(); j++) {
		// 		System.out.print(REB2.get(r).get(j) + "  ");
		// 	}
		// 	System.out.println();
		// 	count++;
		// }

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
