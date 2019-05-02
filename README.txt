README

To run:
javac main.java
java main

This will produce results for the file that has been set in main.java as the STUPAfile.
To view results for other files, change name accordingly, with appropriate path.

Flow of code:
In main.java STUPAReader object is created, which when instantialized sets the following attributes of STUPAreader :
	--> intList (List of unique time intervals)
	--> regionMap (Map with region number corresponding to the coordinates of lower left point of the rectangle and length and width)
	-->STUPA (matrix containing all the STUPA information)
	-->SUPA (SUPA matrix obtained by splitting STUPA)
	-->TUPA (TUPA matrix obtained by splitting STUPA)
	-->UPAmapTUPA (Map with key as Interval and value as the TUPA corresponding to it)
	-->UPAmapSUPA (Map with key as region number and value as the SUPA corresponding to it)

After STUPAreader object is created following steps are followed:
	-->	combinedRoleSet is populated (contains all the roles (Object of type Role) after performing invariant role mining on the TUPA)
	--> REB containing Role with corresponding time intervals it's active for is created
	-->	combinedRoleSet2 is populated (contains all the roles (Object of type Role) after performing invariant role mining on the SUPA)
	--> finalSTRoleSet is populated (contains all spatio-temporal roles (Object of type RoleFinal)which is obtained by merging and pruning all the spatio and temporal roles)

