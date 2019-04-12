package D_tree;

import java.util.ArrayList;

public class ConstraintsCheck {
	
	ArrayList<Constraint> constraintArray = new ArrayList<Constraint>();

	public ConstraintsCheck() {
		// TODO Auto-generated constructor stub
	}

	public static void generateCheck(ArrayList<Item> items, ArrayList<Location> locations) {
		// TODO Auto-generated method stub
		// if number of like criterias minus number of like properties is negative, there are not enough locations to satisfy.
		/*
		for(Item item: items) {
			System.out.println("ITEM: "+item.getItemName());
			ArrayList<String> itemProperties= new ArrayList<String>(item.getItemProperties());
			for(String propString: itemProperties) {
				String[] separatedProps = propString.split("&");
				for(Location location: locations) {
					System.out.println("LOCATION: "+location.getLocationName());
					ArrayList<String> locationCriterias = new ArrayList<String>(location.getLocationCriteria());
					for(String critString: locationCriterias) {
						String[] separatedCrits = critString.split("&");
						for(String props: separatedProps) {
							for(String crits: separatedCrits) {
								System.out.print(props+"=="+crits+" ?");
								if(props.contentEquals(crits)) {
									System.out.print(" ----> MATCH");
								}
								System.out.println();
							}
						}
					}
				}
			}
		}
		*/
		// Final array. Each array element has the following array:
		//    [Property or criteria] 	[item names with property] [location name with matching criteria] [Criteria minus Property number]
		// -->[single element(String)]  [String Array of names]    [String array of names]				  [single element (Integer)]
		// Then print all elements with a negative number and display items and notify user that all of these items might not be assigned to desired locations.
		// Either reduce the number of elements with this property or increase the number of locations with matching criteria.
	}

}
