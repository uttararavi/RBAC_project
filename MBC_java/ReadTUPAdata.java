import java.io.*;
import java.util.*;
import MBCpackage.*;

class ReadTUPAdata {
	int users,perms;
	Interval[][] TUPA;
	int[][][] listUPA;

	ReadTUPAdata(String TUPAfile) {
		String currLine;

		try(BufferedReader reader = new BufferedReader(new FileReader(TUPAfile))) {

			currLine = reader.readLine();
			users = Integer.parseInt(currLine);
			currLine = reader.readLine();
			perms = Integer.parseInt(currLine);

			TUPA = new int[users][perms];

			currLine = reader.readLine();


		}
		catch(IOException e) {
			System.out.println("Error occured when filling UA or PA");
			e.printStackTrace();
		}
	}
}