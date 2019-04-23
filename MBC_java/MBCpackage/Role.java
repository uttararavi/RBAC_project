/***********************************************
Role-
This is the class that defins a Role.
Definiton of Role: The set of user and permissions.
***********************************************/


package MBCpackage;
import java.util.*;

/*An object of the role class will define a single role*/
public class Role{
	public SortedSet<Integer> perm = new TreeSet<>(); // The set of permissions
	public SortedSet<Integer> user = new TreeSet<>(); // The set of users

	public void display(int roleNo)
	{
		System.out.println("r"+roleNo+" : "+user+"  "+perm+"\t");
	}
	public void display()
	{
		System.out.println("role : "+user+"  "+perm+"\t");
	}

	public boolean equals(Object obj) {

		if(obj instanceof Role) {
			Role temp = (Role) obj;
			if(this.perm.equals(temp.perm) && this.user.equals(temp.user)) {
				return true;
			}
		}

		return false;
	}

	public int hashCode() {
		// For some unknown reason gives correct output while removing duplicates
        // int hash1 = ((Double) min).hashCode();
        // int hash2 = ((Double) max).hashCode();
        return 7;//31*hash1 + hash2;
    }

}

class SortByPermSize implements Comparator<Role>{
	public int compare(Role a, Role b)
	{
		return a.perm.size() - b.perm.size();
	}
}

class SortByUserSize implements Comparator<Role>{
	public int compare(Role a, Role b)
	{
		return a.user.size() - b.user.size();
	}
}