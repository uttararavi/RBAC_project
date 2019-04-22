import java.io.*;
import java.util.*;
import MBCpackage.*;

class SUPACell {

	int floorNumber;
	int regionNumber;
	double xCoordinate;
	double yCoordinate;
	double length;
	double width;

	SUPACell(int floorNumber, int regionNumber, ArrayList<Double> locationDetails) {
		this.floorNumber = floorNumber;
		this.regionNumber = regionNumber;
		this.xCoordinate = locationDetails.get(0);
		this.yCoordinate = locationDetails.get(1);
		this.length = locationDetails.get(2);
		this.width = locationDetails.get(3);

	}

	public boolean containsRegion(ArrayList<Double> coordList) {

		if(this.xCoordinate <= coordList.get(0) &&
		   this.yCoordinate <= coordList.get(1) &&
		   this.xCoordinate + this.length <= coordList.get(0) + coordList.get(2) &&
		   this.yCoordinate + this.width <= coordList.get(1) + coordList.get(3)) {
			return true;
		}

		return false;
		
	}

}