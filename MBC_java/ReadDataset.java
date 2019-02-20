/************************************
READDATASET CLASS- constructs the UPA from the given information.
constructor1 : takes the names of the files having UA and PA and gets the UPA
constructor2 : takes the roles and number of users and perms and gets the UPA
crossProsuct : called by constructor1 to generate the UPA out of the read UA and PA
************************************/


import java.io.*;
import java.util.*;
import MBCpackage.*;

/*FORMAT OF THE FILE BEING READ-
1st Line of UA = number of users; 2nd line of UA = number of roles
1st line of PA = number of roles; 2nd line of PA = number of permissions
*/

class ReadDataset{
	int users,roles,perms;
	int[][] UA,PA,UPA;

	ReadDataset(String UAfile, String PAfile)//core-functionality = reading the UA and PA 
	{
		String currLine1, currLine2;
		try(BufferedReader reader1 = new BufferedReader(new FileReader(UAfile));
			BufferedReader reader2 = new BufferedReader(new FileReader(PAfile)))
		{
			//Reading the 1st line of both UA and PA
			currLine1 = reader1.readLine();
			users = Integer.parseInt(currLine1);
			currLine2 = reader2.readLine();
			roles = Integer.parseInt(currLine2);

			//reading the 2nd line of the UA and PA
			currLine1 = reader1.readLine();
			currLine2 = reader2.readLine();
			perms = Integer.parseInt(currLine2);

			//reading 1st and 2nd lines of both UA and PA allows us to declare all UA,PA and UPA
			UA = new int[users][roles];
			PA = new int[roles][perms];
			UPA = new int[users][perms];

			//Start reading lines in a loop from here onwards
			currLine1 = reader1.readLine();
			currLine2 = reader2.readLine();
			int row = 0; //Will track the row number in the variables UA and PA

			/*Looping scheme:
			Process the lines to fill UA and PA
			Read the next lines in both files*/
			while(currLine1!=null || currLine2!=null)
			{
				if(currLine1 !=null)
				{
					String[] list = currLine1.split("\\s+"); //split obtained row by spaces
					for(int col=0;col<list.length;col++) //loop to fill the corresponding row of UA
						UA[row][col] = Integer.parseInt(list[col]);
					currLine1 = reader1.readLine(); //read next line after processing the current line
				}
				if(currLine2!=null)
				{
					String[] list = currLine2.split("\\s+"); //split obtained string by spaces
					for(int col=0;col<list.length;col++) //loop to fill the corresponding row of PA
						PA[row][col] = Integer.parseInt(list[col]);
					currLine2 = reader2.readLine(); //read next line after processing the current line
				}
				row = row+1; //will work on the next row of the UA and PA
			}
		}catch(IOException e)
		{
			System.out.println("Error occured when filling UA or PA");
			e.printStackTrace();
		}
		//After filling the UA and PA, get the UPA by cross product
		crossProduct();
	}

	ReadDataset(ArrayList<Role> roleSet, int u, int p)//core-functionality = read the roles and fill the UPA
	{
		roles = roleSet.size();
		users = u;
		perms = p;
		UA = new int[users][roles];
		PA = new int[roles][perms];
		UPA = new int[users][perms];
		for(Role role: roleSet)//for each role
		{
			Iterator<Integer> i = role.user.iterator();
			while(i.hasNext()) //for each user in the role
			{
				int row = i.next();
				Iterator<Integer> j = role.perm.iterator();
				while(j.hasNext()) //for each permission of the role, given the user
					UPA[row][j.next()] = 1;//fill 1 at the corresponding position
			}
		}
	}

	//Performs boolean matrix multiplication by replacing * -> && and + -> ||
	//in normal matrix multiplication.
	void crossProduct()
	{
		for(int row=0;row<users;row++)
			for(int col=0;col<perms;col++)
			{
				boolean value = false;
				for(int k=0;k<roles;k++)
					value = value||((UA[row][k]==1?true:false) && (PA[k][col]==1?true:false));
				UPA[row][col] = (value==false)? 0 : 1;
			}
	}

}