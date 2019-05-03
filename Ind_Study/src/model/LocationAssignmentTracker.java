package model;

import java.util.ArrayList;

public class LocationAssignmentTracker {

	public static ArrayList<Location> locationAssCount = new ArrayList<Location>();
	
	public static void newTracker(ArrayList<Location> locations) {
		locationAssCount = new ArrayList<Location>(locations);
	}
	
	public static void add(Location location) {
		locationAssCount.add(location);
	}
	
	public static int showCount(Location location) {
		if(!location.getLocationName().equals("null location")||!location.getLocationName().equals("No location assigned")||location!=null){
			Integer index = locationAssCount.indexOf(location);
			Integer count = locationAssCount.get(index).getNumberOfTimesAssigned();
			return count;
		}
		return 0;
	}
	
	public static void increaseCounts(Location location) {
		if(!location.getLocationName().equals("null location")||!location.getLocationName().equals("No location assigned")||location!=null){
			Integer index = locationAssCount.indexOf(location);
			Integer count = locationAssCount.get(index).getNumberOfTimesAssigned();
			count += 1;
			locationAssCount.get(index).setNumberOfTimesAssigned(count);
		}
	}

	public static void decreaseCounts(Location locationAssigned) {
		if(!locationAssigned.getLocationName().equals("null")||!locationAssigned.getLocationName().equals("No location assigned")){
			Integer index = locationAssCount.indexOf(locationAssigned);
			Integer count = locationAssCount.get(index).getNumberOfTimesAssigned();
			count -= 1;
			if(count < 0)
				count = 0;
			locationAssCount.get(index).setNumberOfTimesAssigned(count);
		}
		
	}

	// use this if you want to add an location
	public static boolean checkCount(Location location) {
		if(!location.getLocationName().equals("null")||location.getLocationName().equals("No location assigned")){
			Integer index = locationAssCount.indexOf(location);
			Integer count = locationAssCount.get(index).getNumberOfTimesAssigned();
			if(count < locationAssCount.get(index).getAssignmentLimit()) {
				return true;
			}
		}
		return false;
	}
	
	// use this if you want to remove an location
	public static boolean atLimit(Location location) {
		if(!location.getLocationName().equals("null")||!location.getLocationName().equals("No location assigned")){
//			System.out.println("At limit check for "+location.getLocationName());
			Integer index = locationAssCount.indexOf(location);
			Integer count = locationAssCount.get(index).getNumberOfTimesAssigned();
			if(count >= locationAssCount.get(index).getAssignmentLimit()) {
				return true;
			}
		}
		return false;
	}

	public static void resetCount(Location oldLocation) {
		oldLocation.setNumberOfTimesAssigned(0);	
	}
}
