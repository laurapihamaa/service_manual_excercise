package com.etteplan.servicemanual.maintenancetask;

import java.util.Comparator;

public class CustomComparator implements Comparator<MaintenanceTask>{
	
	//in this case we are sorting severities only based on alphabetical order as it fits the use case
	//in other use case we could assign an integer for each enum and compare them instead (for example)
	
	/*
	 * Compares two maintenance task based first on severity and then on registration time
	 * 
	 * 
	 * @param t1 Maintenance task 1
	 * @param t2 Maintenance task 2
	 * 
	 * @return int based on the sorting order
	 * 
	 */

	@Override
	public int compare(MaintenanceTask t1, MaintenanceTask t2) {
		
		int severityCompared = t1.getSeverity().compareTo(t2.getSeverity());
		
		if(severityCompared != 0) {
			return severityCompared;
		}

		return t1.getRegistrationTime().compareTo(t2.getRegistrationTime());
	}
	

}
