***MAXIMUM BICLIQUE COVER***
This directory contains-
1) main.java
2) ReadDataset.java
3) VerifyUPA.java
4) 0_README_for_datasets.txt : list of all the datasets
5) directory: nonTemporal_Datasets
	1) A README for the names of all the datasets that can be invoked by the main method
	2) The datasets in pairs as UA-file and PA-file
6) directory: MBCpackage
	1) MBC.java
	2) MBCdriver.java
	3) Biclique.java
	4) Role.java
	5) LatticeProcessing.java
	6) README.txt

***USING THIS DIRECTORY***
To run the main method, perform the following commands in the terminal
$javac main.java
$java main
$Enter the name of the Dataset:

The name of the dataset is to be chosen from the text file 0_README_for_datasets.txt

***MODIFICATIONS FOR TESTING***
The block comment for a hand-crafted UPA is available. Uncomment for use.
Comment out the entire file-extracted-dataset portion of code.
Change the following statements appropriately.

***USAGE OF CLASSES***
1) ReadDataset:
	ReadDataset readerName = new ReadDataset(String UAfileNAme, String PAfileName);
	readerName.UPA // to access the UPA

	ReadDataset readerNAme = new ReadDataset(ArrayList<Role> set_of_roles,int number_of _users, int number_of_perms)
	readerName.UPA //to access the UPA.

2) VerifyUPA:
	VerifyUPA name = new VerifyUPA(int[][] UPA1, int[][] UPA2)
	name.truthValue //to know if the UPAs match. 0=>not matching; 1=>matching

3) MBC:
	MBC minerName = new MBC(int[][] UPA)
	minerName.roleSet //to access the roleSet