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

		/* Input your filename */
		
		String STUPAfile = "STUPADataset/u200p200f1d20/STUPA5.txt";

		/* Plan.txt contains all the regions with their coordinates */
		STUPAReader stupaReader = new STUPAReader(STUPAfile, "plan.txt");

		/* TUPA Role Mining */

		HashMap<Interval, ArrayList<Role>> intRoleSetMap = new HashMap<Interval, ArrayList<Role>>();	// contains interval to Roles mapping
		Set<Role> combinedRoleSet = new HashSet<Role>(); // contains all the roles from Temporal RM

		intRoleSetMap = getIntervalRSMap(stupaReader, intRoleSetMap, combinedRoleSet);

		// REB creation for TUPA
		HashMap<Role, ArrayList<Interval>> REB = new HashMap<Role, ArrayList<Interval>>();
		REB = generateREBforTUPA(REB, combinedRoleSet, stupaReader, intRoleSetMap);

		/* SUPA Role Mining */
		HashMap<Integer, ArrayList<Role>> regionRoleSetMap = new HashMap<Integer, ArrayList<Role>>();	// contains region to Roles mapping
		Set<Role> combinedRoleSet2 = new HashSet<Role>(); // contains all the roles from Spatial RM

		regionRoleSetMap = getRegionRSMap(stupaReader, regionRoleSetMap, combinedRoleSet2);

		// REB creation for SUPA
		HashMap<Role, ArrayList<Integer>> REB2 = new HashMap<Role, ArrayList<Integer>>();
		REB2 = generateREBforSUPA(REB2, combinedRoleSet2, stupaReader, regionRoleSetMap);

		// combine Roles from combinedRoleSet and combinedRoleSet2

		ArrayList<RoleFinal> finalSTRoleSet = new ArrayList<RoleFinal>();	// contains combined roleset
		finalSTRoleSet = getSpatioTemporalRoles(finalSTRoleSet, combinedRoleSet, combinedRoleSet2, REB, REB2);


		// print all the spatio-temporal roles
		printFinalRoleSet(finalSTRoleSet);

		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;
		System.out.println(totalTime/Math.pow(10,9)+" seconds");

	}

	public static ArrayList<RoleFinal> checkCompatible (ArrayList<RoleFinal> finalSTRoleSet, int i, int j) {
		// user, time, region 
		if(finalSTRoleSet.get(i).user.equals(finalSTRoleSet.get(j).user) && 
			finalSTRoleSet.get(i).region.equals(finalSTRoleSet.get(j).region) &&
			finalSTRoleSet.get(i).timeInt.equals(finalSTRoleSet.get(j).timeInt)) {
				finalSTRoleSet.get(i).perm.addAll(finalSTRoleSet.get(j).perm);
				finalSTRoleSet.remove(j);	
				// System.out.println("Needs further merging");
		}

		// user, perm, region 
		else if(finalSTRoleSet.get(i).user.equals(finalSTRoleSet.get(j).user) && 
			finalSTRoleSet.get(i).region.equals(finalSTRoleSet.get(j).region) &&
			finalSTRoleSet.get(i).perm.equals(finalSTRoleSet.get(j).perm)) {
				finalSTRoleSet.get(i).timeInt.addAll(finalSTRoleSet.get(j).timeInt);
				finalSTRoleSet.remove(j);
				// System.out.println("Needs further merging");

		}

		// user, perm, time
		else if(finalSTRoleSet.get(i).user.equals(finalSTRoleSet.get(j).user) && 
			finalSTRoleSet.get(i).perm.equals(finalSTRoleSet.get(j).perm) &&
			finalSTRoleSet.get(i).timeInt.equals(finalSTRoleSet.get(j).timeInt)) {
				finalSTRoleSet.get(i).region.addAll(finalSTRoleSet.get(j).region);
				finalSTRoleSet.remove(j);
				// System.out.println("Needs further merging");

		}

		// perm, time, region
		else if(finalSTRoleSet.get(i).perm.equals(finalSTRoleSet.get(j).perm) && 
			finalSTRoleSet.get(i).region.equals(finalSTRoleSet.get(j).region) &&
			finalSTRoleSet.get(i).timeInt.equals(finalSTRoleSet.get(j).timeInt)) {
				finalSTRoleSet.get(i).user.addAll(finalSTRoleSet.get(j).user);
				finalSTRoleSet.remove(j);
				// System.out.println("Needs further merging");

		}

		return finalSTRoleSet;
	}

	public static void printFinalRoleSet(ArrayList<RoleFinal> finalSTRoleSet) {
		for(int i = 0; i < finalSTRoleSet.size(); i++) {
			System.out.print("r" + i + " ");
			System.out.print(finalSTRoleSet.get(i).user + "  ");
			System.out.println(finalSTRoleSet.get(i).perm + "  ");

			System.out.print("int : ");
			for(int j = 0; j < finalSTRoleSet.get(i).timeInt.size(); j++) {
				System.out.print("(" + finalSTRoleSet.get(i).timeInt.get(j).min + "," + finalSTRoleSet.get(i).timeInt.get(j).max + ") ");
			}
			System.out.println();
			System.out.print("region : ");
			for(int j = 0; j < finalSTRoleSet.get(i).region.size(); j++) {
				System.out.print(finalSTRoleSet.get(i).region.get(j) + "  ");
			}
			System.out.println();
			System.out.println("---------------------");

		}

		System.out.println("Total roles : " + finalSTRoleSet.size());
	}

	public static ArrayList<RoleFinal> getSpatioTemporalRoles (ArrayList<RoleFinal> finalSTRoleSet, Set<Role> combinedRoleSet, Set<Role> combinedRoleSet2, HashMap<Role, ArrayList<Interval>> REB, HashMap<Role, ArrayList<Integer>> REB2) {
		Iterator<Role> itr = combinedRoleSet.iterator();
		Iterator<Role> itr2 = combinedRoleSet2.iterator();
		

		while(itr.hasNext()) {
			Role rt = itr.next();
			itr2 = combinedRoleSet2.iterator();	
			while(itr2.hasNext()) {
				Role rs = itr2.next();
				Role temp = new Role();
				temp.user.addAll(rt.user);
				temp.perm.addAll(rt.perm);

				temp.user.retainAll(rs.user);
				temp.perm.retainAll(rs.perm);


				if(temp.user.size() == 0 || temp.perm.size() == 0) {
				}

				else {
					RoleFinal newRole = new RoleFinal();
					newRole.user = temp.user;
					newRole.perm = temp.perm;
					newRole.region.addAll(REB2.get(rs));
					for(int i = 0; i < REB.get(rt).size(); i++) {
						Interval tempInterval = new Interval(REB.get(rt).get(i).min, REB.get(rt).get(i).max);
						newRole.timeInt.add(tempInterval);
					}
					finalSTRoleSet.add(newRole);
				}

			}


		}

		/* Remove all redundant rules */
		Boolean canMerge = false;
		do {
			for(int i = 0; i < finalSTRoleSet.size()-1; i++) {
				for(int j = i+1; j < finalSTRoleSet.size(); j++) {

					int l1 = finalSTRoleSet.size();
					finalSTRoleSet = checkCompatible(finalSTRoleSet, i, j);
					int l2 = finalSTRoleSet.size();

					if(l1 > l2) {
						canMerge = true;
					}

					else {
						canMerge = false;
					}
				}
			}
		} while (canMerge);

		return finalSTRoleSet;
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
