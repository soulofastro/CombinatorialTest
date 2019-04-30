package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ConstraintsCheck {
	
	private static ArrayList<Constraint> constraintsArray;

	public ConstraintsCheck() {
	}

	// TODO delete this method
	public static void generateCheckWithoutAND(ArrayList<Item> items, ArrayList<Location> locations) {
		ArrayList<Constraint> constraintArray = new ArrayList<Constraint>();
		// if number of like criterias minus number of like properties is negative, there are not enough locations to satisfy.
		/**/
		
		for(Item item: items) {
//			System.out.println("ITEM: "+item.getItemName());
			ArrayList<String> itemProperties= new ArrayList<String>(item.getItemProperties());
			int multiplicity = item.getAssignmentLimit();
			for(String propString: itemProperties) {
				String[] separatedProps = propString.split("&");
				for(String props: separatedProps) {
//					System.out.println("Item: "+item.getItemName()+", property: "+props);
					Constraint tempConstraint = new Constraint(props);
					if(!constraintArray.contains(tempConstraint) && props.indexOf('[')>=0) {
						for(int i = 0; i<multiplicity;i++) {
							tempConstraint.getItemsWithConstraint().add(item.getItemName());
							// could change this to an int count later
						}
						constraintArray.add(tempConstraint);
					}
					else if(props.indexOf('[')>=0) {
						int index = constraintArray.indexOf(tempConstraint);
						for(int i = 0; i<multiplicity;i++) {
							constraintArray.get(index).getItemsWithConstraint().add(item.getItemName());
							// could change this to an int count later
						}
					}
				}
			}
		}
		
		for(Location location: locations) {
//			System.out.println("LOCATION: "+location.getLocationName());
			ArrayList<String> locationCriterias = new ArrayList<String>(location.getLocationCriteria());
			int multiplicity = location.getAssignmentLimit();
			for(String critString: locationCriterias) {
				String[] separatedCrits = critString.split("&");
				for(String crits: separatedCrits) {
//					System.out.println("Location: "+location.getLocationName()+", criteria: "+crits);
					Constraint tempConstraint = new Constraint(crits);
					if(!constraintArray.contains(tempConstraint) && crits.indexOf('[')>=0) {
						for(int i = 0; i<multiplicity;i++) {
							tempConstraint.getLocationsWithConstraint().add(location.getLocationName());
							// could change this to primitive int count later
						}
						constraintArray.add(tempConstraint);
					}
					else if(crits.indexOf('[')>=0) {
						int index = constraintArray.indexOf(tempConstraint);
						for(int i = 0; i<multiplicity;i++) {
							constraintArray.get(index).getLocationsWithConstraint().add(location.getLocationName());
							// could change this to int count later
						}
					}
				}
			}
		}
								
		/**/
		// Final array. Each array element has the following object properties:
		//    [Property or criteria] 	[item names with property] [location names with matching criteria] [Criteria minus Property number]
		// -->[Constraint object]       [String Array of names]    [String array of names]				   [single element (Integer)]
		// Then print all elements with a negative number and display items and notify user that all of these items might not be assigned to desired locations.
		// Either reduce the number of elements with this property or increase the number of locations with matching criteria.
		for(Constraint constraint: constraintArray) {
			System.out.println("Constraint: "+constraint.getConstraintName()+", Difference: "+ constraint.getDifference());
		}
		System.out.println();
	}
	
	
	
	public static void generateCheckWithAND(ArrayList<Item> items, ArrayList<Location> locations) { 
		ArrayList<Constraint> constraintArray = new ArrayList<Constraint>();
		
		for(Location location: locations) {
//			System.out.println("LOCATION: "+location.getLocationName());
			ArrayList<String> locationCriterias = new ArrayList<String>(location.getLocationCriteria());
			int multiplicity = location.getAssignmentLimit();
			for(String critString: locationCriterias) {
//				String[] separatedCrits = critString.split("&");
//				for(String crits: separatedCrits) {
//					System.out.println("Location: "+location.getLocationName()+", criteria: "+crits);
				Constraint tempConstraint = new Constraint(critString);
				if(!constraintArray.contains(tempConstraint) && critString.indexOf('[')>=0) {
					for(int i = 0; i<multiplicity;i++) {
						tempConstraint.getLocationsWithConstraint().add(location.getLocationName());
						// could change this to primitive int count later
					}
					constraintArray.add(tempConstraint);
				}
				else if(critString.indexOf('[')>=0) {
					int index = constraintArray.indexOf(tempConstraint);
					for(int i = 0; i<multiplicity;i++) {
						constraintArray.get(index).getLocationsWithConstraint().add(location.getLocationName());
							// could change this to int count later
					}
				}
			}
		}
		
		
		for(Item item: items) {
//			System.out.println("ITEM: "+item.getItemName());
			ArrayList<String> itemProperties= new ArrayList<String>(item.getItemProperties());
			int multiplicity = item.getAssignmentLimit();
			for(String propString: itemProperties) {
//				String[] separatedProps = propString.split("&");
//				for(String props: separatedProps) {
//					System.out.println("Item: "+item.getItemName()+", property: "+props);
				Constraint tempConstraint = new Constraint(propString);
				if(!constraintArray.contains(tempConstraint) && propString.indexOf('[')>=0) {
					for(int i = 0; i<multiplicity;i++) {
						tempConstraint.getItemsWithConstraint().add(item.getItemName());
						// could change this to an int count later
					}
					constraintArray.add(tempConstraint);
				}
				else if(propString.indexOf('[')>=0) {
					int index = constraintArray.indexOf(tempConstraint);
					for(int i = 0; i<multiplicity;i++) {
						constraintArray.get(index).getItemsWithConstraint().add(item.getItemName());
						// could change this to an int count later
					}
				}
			}
		}
		
								
		/**/
		// Final array. Each array element has the following object properties:
		//    [Property or criteria] 	[item names with property] [location names with matching criteria] [Criteria minus Property number]
		// -->[Constraint object]       [String Array of names]    [String array of names]				   [single element (Integer)]
		// Then print all elements with a negative number and display items and notify user that all of these items might not be assigned to desired locations.
		// Either reduce the number of elements with this property or increase the number of locations with matching criteria.

		setConstraintsArray(constraintArray);
		optimizeConstraintsArray();
		//printConstraintsArray();
	}

	/**
	 * This array starts at the end of the constraintsArray and goes backward thru it.
	 * When it visits a constraint and sees a difference of 0, for each item associated with 
	 * that constraint, it goes backwards through the list removing one instance of that item
	 * in all the following constraint-item lists.
	 * 
	 * Remove call in previous method to see what array would look like without optimizing.
	 */
	private static void optimizeConstraintsArray() {
		ArrayList<Constraint> optimizedList = new ArrayList<Constraint>(getConstraintsArray());
		for(int i = optimizedList.size()-1; i >= 0; i--) {
			if(optimizedList.get(i).getDifference() == 0) {
				List<String> itemList = new ArrayList<String>(optimizedList.get(i).getItemsWithConstraint());
				itemList = itemList.stream().distinct().collect(Collectors.toList());
				for(String item: itemList) { // int j would've been used here
					for(int k = i-1; k>=0; k--) {
						optimizedList.get(k).getItemsWithConstraint().remove(item);
					}
				}
			}
		}
		Collections.sort(optimizedList,(constraint1, constraint2) -> constraint1.getDifference() - constraint2.getDifference());		
		setConstraintsArray(optimizedList);
	}

	public static ArrayList<Constraint> getConstraintsArray() {
		return constraintsArray;
	}

	public static void setConstraintsArray(ArrayList<Constraint> constraintsArray) {
		ConstraintsCheck.constraintsArray = new ArrayList<Constraint>(constraintsArray);
	}
	
	public static void printConstraintsArray() {
		for(Constraint constraint: constraintsArray) {
			int diff = constraint.getDifference();
			if(diff < 1) {
				System.out.print("Constraint: "+constraint.getConstraintName()+", Difference: "+ diff);
				if(constraint.getLocationsWithConstraint().size() < 0) {
					System.out.println(",  Items with this constraint have a lower chance of being assigned optimally. ");
				}
				else if(diff == 0) {
					System.out.println(",  Items with this constraint have a higher chance of being assigned optimally. ");
				}
				else if(constraint.getLocationsWithConstraint().size() > 0 && diff < 0) {
					System.out.println(",  Items with this constraint have a moderate chance of being assigned optimally. ");
				}
				else{
					System.out.println();
				}
					System.out.println("	Items with constraint:");
				for(String item: constraint.getItemsWithConstraint()) {
					System.out.println("		Items: "+ item);
				}
			}
		}
		System.out.println();
	}
	
	

}
