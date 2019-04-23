package D_tree;

import java.util.ArrayList;
import java.util.Arrays;

public class Constraint {
	
	private String constraintName = "unknown";
	private ArrayList<String> itemsWithProperty;
	private ArrayList<String> locationsWithConstraint;
	private Integer difference = 0;
	
	/* Constructors */
	public Constraint(String name) {
		setConstraintName(name);
		this.itemsWithProperty = new ArrayList<String>();
		this.locationsWithConstraint= new ArrayList<String>();
	}	
	
	/* Setters and getters */
	public String getConstraintName() {
		return constraintName;
	}
	public void setConstraintName(String constraintName) {
		this.constraintName = constraintName;
	}
	public ArrayList<String> getItemsWithConstraint() {
		return itemsWithProperty;
	}
	public void setItemsWithConstraint(ArrayList<String> itemsWithConstraint) {
		this.itemsWithProperty = itemsWithConstraint;
	}
	public ArrayList<String> getLocationsWithConstraint() {
		return locationsWithConstraint;
	}
	public void setLocationsWithConstraint(ArrayList<String> locationsWithConstraint) {
		this.locationsWithConstraint = locationsWithConstraint;
	}
	public Integer getDifference() {
		findDifference();
		return difference;
	}
	public void findDifference() {
		difference = locationsWithConstraint.size()-itemsWithProperty.size();
	}
	
	@Override
	public boolean equals(Object other) {
		if(this == other) {return true;}
		if(other == null) {return false;}
		if(getClass() != other.getClass()) {return false;}
		if(!this.getConstraintName().contentEquals(((Constraint) other).getConstraintName()) && !arrayCompare(this.getConstraintName(),((Constraint) other).getConstraintName())) {
			return false;
		}
		
		return true;
	}
	
	public boolean arrayCompare(String propString, String critString) {
		String[] prop = propString.split("&");
		String[] crit = critString.split("&");
		prop = Arrays.stream(prop).distinct().toArray(String[]::new);
		crit = Arrays.stream(crit).distinct().toArray(String[]::new);
		
		int matches = 0;
		if(prop.length==crit.length) {
			for(int i=0; i<prop.length; i++) {
				for(int j=0; j<crit.length;j++) {
					if(prop[i].equalsIgnoreCase(crit[j])) {
						matches = matches + 1;
					}
				}
			}
		}
		if(matches == prop.length)
			return true;
		else
			return false;
	}
	
	@Override
	public int hashCode() {
		int result = 0;
		result = 7 * constraintName.length();
		return result;
	}

}
