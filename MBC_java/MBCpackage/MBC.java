/************************
MBC-
This class runs the MBC code. Several schemes can be used for role mining.
Two used are "select vertex with max uncovered edges" and "select vertex with min uncovered edges"
The results of the two schemes are compared and the one with lesser number of roles is chose
and stored in roleSet
************************/

package MBCpackage;
import java.util.*;

public class MBC{
	public ArrayList<Role> roleSet; //final optimal roleSet

	public MBC(int[][] UPA) //core-functionality: to test the two schemes
	{
		MBCdriver scheme0 = new MBCdriver(UPA,0); //scheme = select vertex with min uncovered edges
		MBCdriver scheme1 = new MBCdriver(UPA,1); //scheme = select vertex with max uncovered edges

		int size0 = scheme0.roleSet.size();
		int size1 = scheme1.roleSet.size();

		if(size0<=size1)
			roleSet = scheme0.roleSet;
		else roleSet = scheme1.roleSet;
	}
	public void DisplayRoles()//core-functionality: Display the roles in roleSet
	{
		for(int i=0;i<roleSet.size();i++)
		{
			Role r = roleSet.get(i);
			System.out.println("r"+i+" : "+r.user+"  "+r.perm);
		}
		System.out.println("Number of Roles = "+roleSet.size());
	}
}