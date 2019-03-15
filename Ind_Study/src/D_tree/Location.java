package D_tree;

import java.util.ArrayList;

public class Location {
	
	private String locationName = "";
	private String[] locationCriteria;  // string array for now until i figure out a 
										// better way. This is where I will store
										// "class requirements" and/or time slot requirements
	private ArrayList<Item> mandatoryAssignments = new ArrayList<Item>();

	/* constructors */
	public Location() {
	}
	public Location(String locName) {
		setLocationName(locName);
	}

	/* Setters and getters  */
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String[] getLocationCriteria() {
		return locationCriteria;
	}
	public void setLocationCriteria(String[] locationCriteria) {
		this.locationCriteria = locationCriteria;
	}
	public ArrayList<Item> getMandatoryAssignments() {
		return mandatoryAssignments;
	}
	public void setMandatoryAssignments(ArrayList<Item> mandatoryAssignments) {
		this.mandatoryAssignments = mandatoryAssignments;
	}
	public void assignMandatoryItem(Item item) {
		this.mandatoryAssignments.add(item);
	}

}
