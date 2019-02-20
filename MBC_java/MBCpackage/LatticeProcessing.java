/***********************************************
LatticeProcessing-
This class is for post-processing the roles to reduce further.
Rule is described in the paper.
The final set of roles are stored in ROLES
***********************************************/
package MBCpackage;
import java.util.*;

public class LatticeProcessing{
	public ArrayList<Role> ROLES = new ArrayList<Role>(); //Will store the reduced roleSet

	/*
	The function will find sort the roles w.r.t number of permissions in a role.
	It will the check if the current permission set is the super set of any of the pervious roles' perm set.
	If yes, the current role will be made into a smaller role by removing the common permissions and adding
	the user to the subset role.
	The same is repeated by sorting wrt size of the user set.
	*/
	public LatticeProcessing(ArrayList<Role> roleSet)
	{
		Collections.sort(roleSet, new SortByPermSize()); //Sort the roles w.r.t permissions set size
		for(int i=0;i<roleSet.size();i++)
		{
			Role parentRole = roleSet.get(i); //Pick the current role as parent role
			for(int j=i-1;j>=0;j--)
			{
				Role childRole = roleSet.get(j); 
				//check if any of the previous roles (with lesser perms) are a proper subset
				//of the parent role's perm-set
				if(parentRole.perm.containsAll(childRole.perm)) // if YES then
				{
					parentRole.perm.removeAll(childRole.perm); //remove perms from the parent
					childRole.user.addAll(parentRole.user); // add the corresponding users to the child role
				}
			}
			if(parentRole.perm.size()==0) //If the parent is exactly the union of its children, then parent is redundant
				roleSet.remove(parentRole);
		}
		//As done with the perm set in terms of the parent-child concept, 
		//perform the same with user set of the roles
		Collections.sort(roleSet, new SortByUserSize());
		for(int i=0;i<roleSet.size();i++)
		{
			Role parentRole = roleSet.get(i);
			for(int j=i-1;j>=0;j--)
			{
				Role childRole = roleSet.get(j);
				if(parentRole.user.containsAll(childRole.user))
				{
					parentRole.user.removeAll(childRole.user);
					childRole.perm.addAll(parentRole.perm);
				}
			}
			if(parentRole.user.size()==0)
				roleSet.remove(parentRole);
		}
		ROLES = roleSet;
	}
}