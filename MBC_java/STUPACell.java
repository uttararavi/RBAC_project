import java.io.*;
import java.util.*;
import MBCpackage.*;

class STUPACell {
	Interval interval;
	int floorNumber;
	int regionNumber;

	// constructor
	STUPACell(int floorNumber, int regionNumber, int start, int end) {
		this.interval = new Interval(start, end);
		this.floorNumber = floorNumber;
		this.regionNumber = regionNumber;
	}

	STUPACell() {
		interval = new Interval(0,0);
		floorNumber = 0;
		regionNumber = 0;
	}
}