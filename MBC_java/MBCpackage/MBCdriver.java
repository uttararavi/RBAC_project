/******************************
MBCdriver-
Runs the MBC algorithm through other modules. Forms the Backbone-code.
makes a copy of the UPA, evaluates the UPA (EvaluateUPA()) to get count the number
of edges per vertex. Purpose of this task is for the vertex-selection of the next iteration.
In each iteration, the UPA is evaluated and then Biclique is found using the Biclique class.
Further, LatticeProccing is done to further reduce the number of roles.
******************************/
package MBCpackage;
import java.util.*;

/*class MBC will implement the minimum biclique cover*/
public class MBCdriver{
	public ArrayList<Role> roleSet = new ArrayList<Role>(); //set of roles covering all the users and premissions
	TreeMap<Integer,Integer> userEdges, permEdges;
	//userEdges = (number of edges from a user-node, user-node number)
	//permEdges = (number of edges from a perm-node, perm-node number)
	int vertexIsPerm; //tells whether the chosen vertex is a permission or not
	int scheme;
	
	/*Preprocess the UPA prior every iteration to find the number of edges per node
	  then choose which node (ie, a user node or a perm node) has minimum no. of uncovered edges*/

	/*Driver function that gets all the roles*/
	public MBCdriver(int[][] fixedUPA, int selectScheme)
	{	
		 //fixedUPA is the UPA that will not be modified throughout
	     //coverUPA is the UPA that will be modified to indicate covered edges
		 scheme  = selectScheme;
		 int row = fixedUPA.length; 
		 int col = fixedUPA[0].length;
		 int[][] coverUPA = new int[row][col];

		 /*copy the fixed UPA to covered UPA to start off*/
		 for(int i=0;i<row;i++)
		 	for(int j=0;j<col;j++)
		 		coverUPA[i][j] = fixedUPA[i][j];

		 //find number of edges per node in the bipartite.
		 EvaluateUPA(coverUPA);
		 Biclique getCover;

		 //while all the edges are not covered
		 while(userEdges.size()>0 || permEdges.size()>0)
		 {
		 	getCover = new Biclique(fixedUPA,coverUPA,scheme,userEdges,permEdges,vertexIsPerm);
		 	Role newRole = getCover.role;
		 	coverUPA = getCover.coverUPA;
		 	roleSet.add(newRole);//add the role to the final roleSet
		 	EvaluateUPA(coverUPA);//re-evaluate the UPA before the next iteration
		 }

		 //reducing the number of roles by removing redundant/repeated roles
		 LatticeProcessing reduce = new LatticeProcessing(roleSet);
		 roleSet = reduce.ROLES;
	};

	void EvaluateUPA(int[][] UPA)
	{
		userEdges = new TreeMap<>();
		permEdges = new TreeMap<>();

		int totUser = UPA.length; // total number of users
		int totPerm = UPA[0].length; // total number or permissions
		int count; // temporary variable that indicates the number of edges for any node

		for(int i=0;i<totUser;i++) //user is a node being tested
		{
			count = 0;
			//find number of edges from the ith user in the bipartite
			for(int j=0;j<totPerm;j++)
				if(UPA[i][j]==1)count++;

			//only one node per edge-count needed. Duplicates can be ignored.
			if(!userEdges.containsKey(count))
				userEdges.put(count,i);
		}

		//using perm as a node, repeat the same.
		for(int j=0;j<totPerm;j++)
		{
			count = 0;
			for(int i=0;i<totUser;i++)
			{
				if(UPA[i][j]==1)count++;
			}
			if(!permEdges.containsKey(count))
				permEdges.put(count,j);
		}

		//any node with 0 uncovered edges is ignored
		if(userEdges.containsKey(0))userEdges.remove(0);
		if(permEdges.containsKey(0))permEdges.remove(0);

		//if no users left
		if(userEdges.size()<=0) vertexIsPerm=1;
		//else if no perms left
		else if(permEdges.size()<=0) vertexIsPerm=0;
		//else compare users and perms
		else
		{
			switch(scheme)// schemes 0 and 1 as defined in MBC.java
			{
				//Arrangement of the TreeMap: smallest value at the top, largest at the bottom.
				//Top = firstKey; Bottom = lastKey
				case 0:{
					if(userEdges.firstKey()<=permEdges.firstKey())
						vertexIsPerm=0;
					else vertexIsPerm=1;
				}break;
				case 1:{
					if(userEdges.lastKey()>=permEdges.lastKey())
						vertexIsPerm=0;
					else vertexIsPerm=1;
				}break;
			}
		}
	}
}