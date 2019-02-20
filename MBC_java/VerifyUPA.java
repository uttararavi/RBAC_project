import java.util.*;
import MBCpackage.*;

class VerifyUPA{
	boolean truthValue;

	VerifyUPA(int[][] original, int[][] reconstructed)
	{
		int uo = original.length;
		int po = original[0].length;

		int ur = reconstructed.length;
		int pr = reconstructed[0].length;

		if(uo!=ur || po!=pr)
			truthValue = false;
		else
		{
			int flag=1;
			for(int i=0;i<uo;i++)
			{
				for(int j=0;j<po;j++)
				{
					truthValue = (original[i][j] == reconstructed[i][j]);
					if(truthValue == false)
					{
						flag=0;
						break;
					}
				}
				if(flag==0)break;
			}
		} 
	}
}