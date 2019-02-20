***MBCpackage***
Contents of the directory-
1)MBC.java
2)MBCdriver.java
3)Biclique.java
4)Role.java
5)LatticeProcessing.java

***DEPENDENCIES***
		MBC <- MBCdriver <- Biclique 
		 |        |           |
 ----------->Role<-------<-----
 |
LatticeProcessing

All other classes make use of the Role class.

***USAGE***
1) MBC:
	MBC minerName = new MBC(int[][] UPA);
	minerName.roleSet //to access the final set of roles

2) MBCdriver:
	MBCdriver driver = new MBCdriver(int[][] UPA, int scheme)
	//scheme can take only 0 and 1. Both schemes are tried by the MBC class when calling the MBCdriver.
	driver.roleSet //to access the roles

3) Biclique:
	Biclique name = new Biclique(int[][] originalUPA, int[][] coveredUPA, TreeMap<Integer,Integer>userEdges, TreeMap<Integer,Integer> permEdges, int vertexIsPerm)
	name.role //to access the role constructed by this class.

	//originalUPA- the UPA originally to be mined
	//coveredUPA- the UPA that changes dynamically as roles are produced.
	//userEdges- a hashtable that keeps count of number of uncovered edges per user-vertex
	//permEdges- a hashtable that keeps count of number of uncovered edges per perm-vertex
	//vertexIsPerm- 0=>vertex is not perm (i.e., vertex is user); 1=> vertex is a perm

4) LatticeProcessing-
	LatticeProcessing reducer = new LatticeProcessing(ArrayList<Role> roleSet)
	reduces.ROLES //to access the roles produced by this class. 