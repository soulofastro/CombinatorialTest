package D_tree;

import java.util.ArrayList;

public class Constraint {
	
	private String constraintName = "null";
	private ArrayList<String> itemsWithProperty;
	private ArrayList<String> locationsWithConstraint;
	private Integer difference = 0;
	
	/* Constructors */
	public Constraint(String name) {
		setConstraintName(name);
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

}
