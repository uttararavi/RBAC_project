/*************************************
Biclique-
This class is called by the MBCdriver to get the biclique. One Biclique is one role.
The vertex is chosen based on 2 criteria-
1) vertexIsPerm: already decided by EvaluateUPA and passed here
2) scheme used: The scheme is passed as 0 or 1 and decision made accordingly
Biclique cover chosen using the fixed UPA, and the covered edges are indicated and 
deleted in the coveredUPA
*************************************/


package MBCpackage;
import java.util.*;

public class Biclique{
	int[][] coverUPA;
	Role role = new Role();

	public Biclique(int[][] fixedUPA, int[][] UPA, int scheme,TreeMap<Integer,Integer> userEdges,TreeMap<Integer,Integer> permEdges, int vertexIsPerm)
	{
		coverUPA = UPA;
		int totUser = fixedUPA.length;
		int totPerm = fixedUPA[0].length;
		int noOfEdges, vertex; //vertex identifies the node under processing

		/*following will be 2 cases- if the vertex is a user or it is a perm*/
		if(vertexIsPerm==0)
		{
			switch(scheme)
			{
				case 0: noOfEdges = userEdges.firstKey();break;
				default: noOfEdges = userEdges.lastKey();break;
			}
			vertex = userEdges.get(noOfEdges);

			role.user.add(vertex); //add the user to role
			for(int j=0;j<totPerm;j++)// add all its perms to the role
			{
				if(fixedUPA[vertex][j]==1)
					role.perm.add(j);
			}

			//find any user whose perm-set is the improper superset of the perm-set of the vertex. Let's call this definition as SIMILAR
			for(int i=0;i<totUser;i++)
			{
				if(i==vertex)continue;

				int trigger=0; //if trigger is ON, then the user undercheck is not similar to the vertex
				for(int j=0;j<totPerm;j++)
				{
					if(fixedUPA[vertex][j]==1 && fixedUPA[vertex][j]!=fixedUPA[i][j])
					{trigger=1;break;}
				}
				if(trigger==0)
				{
					role.user.add(i);
					for(int j=0;j<totPerm;j++)
						if(fixedUPA[vertex][j]==1)
							coverUPA[i][j]=0; //remove all covered edges
				}
				else continue;
			}
			for(int j=0;j<totPerm;j++)
				coverUPA[vertex][j]=0; //remove all covered edges
		}
		else //repeat above process when the vertex is a permission
		{
			switch(scheme)
			{
				case 0: noOfEdges = permEdges.firstKey();break;
				default: noOfEdges = permEdges.lastKey();break;
			}
			vertex = permEdges.get(noOfEdges);

			role.perm.add(vertex);
			for(int i=0;i<totUser;i++)
			{
				if(fixedUPA[i][vertex]==1)
					role.user.add(i);
			}
			for(int j=0;j<totPerm;j++)
			{
				if(j==vertex)continue;

				int trigger=0;
				for(int i=0;i<totUser;i++)
				{
					if(fixedUPA[i][vertex]==1 && fixedUPA[i][vertex]!=fixedUPA[i][j])
					{trigger=1;break;}
				}
				if(trigger==0)
				{
					role.perm.add(j);
					for(int i=0;i<totUser;i++)
						if(fixedUPA[i][vertex]==1)
							coverUPA[i][j]=0;
				}
				else continue;
			}
			for(int i=0;i<totUser;i++)
				coverUPA[i][vertex]=0;
		}
	}
}