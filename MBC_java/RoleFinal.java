import java.util.*;
import MBCpackage.*;
import java.io.*;

public class RoleFinal {
	public SortedSet<Integer> perm = new TreeSet<>(); // The set of permissions
	public SortedSet<Integer> user = new TreeSet<>(); // The set of users
	public ArrayList<Integer> region = new ArrayList<Integer>(); // set of regions
	public ArrayList<Interval> timeInt = new ArrayList<Interval>();
}