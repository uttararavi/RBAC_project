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