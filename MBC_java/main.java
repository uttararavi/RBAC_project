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



		// for(int i = 0; i < tupaReader.intList.size(); i++) {

		// 	// put this in a loop for all Loops
		// 	Integer[][] passUPA = tupaReader.UPAmap.get(tupaReader.intList.get(i));
		// 	MBC miner = new MBC(passUPA);//Perform role mining using the MBC class
		// 	System.out.println("DisplayRoles");
		// 	miner.DisplayRoles(); //In case one wants to display the roles
		// 	System.out.println("###");
		// 	ReadDataset verifyWith = new ReadDataset(miner.roleSet,tupaReader.users,tupaReader.perms);

		// 	// }
		// 	/*ReadDataset class has constructor overloading, wherein it can also construct the UPA
		// 	using the roles generated*/
		// 	VerifyUPA verify = new VerifyUPA(passUPA,verifyWith.UPA); //pass the original and reconstructed UPA
		// 	//VerifyUPA class compares element by element to ensure that the two UPAs are exactly equal

		// 	// System.out.println("\n*** FileName : "+commonString+" ***");
		// 	System.out.println("Number of users = "+tupaReader.users);
		// 	System.out.println("Numebr of permissions = "+tupaReader.perms);
		// 	System.out.println("Given number of roles = "+tupaReader.roles);
		// 	System.out.println("Number of mined roles = "+miner.roleSet.size());
		// 	System.out.println("UPAs match?: "+verify.truthValue);

		// 	//Print UPA testing code
		// 	/*
		// 	row = verifyWith.UPA.length;
		// 	col = verifyWith.UPA[0].length;
		// 	for(int i=0;i<row;i++)
		// 	{
		// 		for(int j=0;j<col;j++)
		// 			System.out.print(verifyWith.UPA[i][j]+" ");
		// 		System.out.println();
		// 	}//*/

		// 	long endTime   = System.nanoTime();
		// 	long totalTime = endTime - startTime;
		// 	System.out.println(totalTime/Math.pow(10,9)+" seconds");

		// }
	}
}

// 415196bc62d712d814929bc2f2d21c628df07938
// 8f98a190948eb6a2da6a98ac749d1692c289063e