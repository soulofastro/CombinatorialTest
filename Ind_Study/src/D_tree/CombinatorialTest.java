package D_tree;

import java.io.BufferedReader;
//import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
/*
 *  An item is equivalent to a Professor, class, or any object that can be placed into something.
 *  A location is equivalent to a class, time slot, or any thing that an object can be placed into.
 *  A decision_list is asking what items can be placed into a certain location.
 *  A decision_choice is assigning an item to a location.
 * 
 */

public class CombinatorialTest 
{ 
	
	public static LinkedList<decision> decisionNumber = new LinkedList<decision>();	
	
	public static void main(String args[]) throws FileNotFoundException, IOException, Exception { 
		
    	ArrayList<Item> items = new ArrayList<Item>();
    	ArrayList<Location> locations = new ArrayList<Location>();
    	
    	/*
    	// I made this to help me figure out where my txt files are.
    	File file = new File(".");
    	for(String fileNames : file.list()) System.out.println(fileNames);
    	*/
    	
    	items = readItemFile("items.txt");
    	for (Item item : items) {
    		System.out.println("Item Name: "+ item.getItemName());
    	}
    	System.out.println();
    	
    	locations = readLocationFile("locations.txt");
    	for (Location location: locations) {
    		System.out.println("Location name: "+ location.getLocationName());
    	}
    	System.out.println();
    	
    	/* Look at each location and assign an item to it */
    	/* This generates a decision node that we can later revisit */
    	/* The actual decision choice is made inside the decision class when the node is generated */
    	ArrayList<Item> itemClone = new ArrayList<Item>(items);	
    	generateDecisionTree(locations, itemClone);
    	
    	/* Display Locations and item assigned to each location */
    	printDecisionTree(decisionNumber);

    	
    	/* Change a decision and display updated list */
    	/* arguments are changeDecisionSelection("where you want to put the new item", "the new item you want to insert", "complete location list")*/
    	/* TEST CASE 1 - add brand new item */
    	//Item alpha_2 = new Item("Alpha_2");
    	//String[] AlphaProp = {"Alpha_2", "NA"};
    	//alpha_2.setItemProperties(AlphaProp);
    	//changeDecisionSelection(locations.get(1), alpha_2, locations);
    	
    	/* TEST CASE 2 - change first appearance of item */
    	//Item echo = decisionNumber.get(4).getItemAssigned();
    	//changeDecisionSelection(locations.get(1), echo, locations);
    	
    	/* TEST CASE 3 - change earlier appearance of item */
    	//Item delta = decisionNumber.get(1).getItemAssigned();
    	//changeDecisionSelection(locations.get(4), delta, locations);
    	
    	/* Display Locations and item assigned to each location after testing change */
    	//System.out.println("\nList after test case change:\n");
    	//printDecisionTree(decisionNumber);
       
    }
	
	/* This Method prints the linked list decision tree */
	private static void printDecisionTree(LinkedList<decision> decisionNumber) {
    	try {
			for(int i=0; i<decisionNumber.size(); i++) {	
				Integer y = i+1;
				System.out.println("Decision " + y.toString() + ": "+"Location-> "+ decisionNumber.get(i).getLocation().getLocationName() + ", item-> "+ decisionNumber.get(i).getDecisionSelected());
				decisionNumber.get(i).printConstrainedChoices();
				decisionNumber.get(i).printUnconstrainedChoices();
				System.out.println();
			}
    	}
    	catch(Exception e) {
    		// Location runs into null assignment
    		System.out.println("Assignment error. Too many or incompatible constraints likely.\nMake sure items with NA preference are last in txt file.");
    	}
		
	}

	/*This is where I generate the decision tree */
	public static void generateDecisionTree(ArrayList<Location> locations, ArrayList<Item> items) {
    	for (int i=0; i<locations.size(); i++) {    		
    		decision decision_choice = new decision(locations.get(i));
    		
    		ArrayList<Item> itemClone = new ArrayList<Item>(items);
    		decision_choice.setUnconstrainedChoices(itemClone);
    		
    		decision_choice.setDecisionSelection(items);
    		  		
    		decisionNumber.add(decision_choice);
    		
    	}
	}
	
	/* THIS IS WHERE I CHANGE A DECISION AT A LOCATION */
	/* change item assigned to this location to newItem */
	/* arguments are ("where you want to put the new item", "the new item you want to insert", "complete location list")*/
	public static void changeDecisionSelection(Location location, Item newItem, ArrayList<Location> locationList) {
		boolean meetsCriteria = false;
		Integer locationIndex = null;
		Item oldItem;
		
		// First, find location in decision list
		for(int i=0; i<decisionNumber.size(); i++) {
			if(decisionNumber.get(i).getLocation().equals(location)) {
		// Second, I check to make sure the new item's preferences fits the location's criteria.
		//		if so, continue
        //		if not tell user they can't assign that item to that location.
				for(int j=0; j<newItem.getItemProperties().length; j++) {
					for(int k=0; k<decisionNumber.get(i).getLocation().getLocationCriteria().length; k++) {
						if(newItem.getItemProperties()[j].equals(decisionNumber.get(i).getLocation().getLocationCriteria()[k]) || newItem.getItemProperties()[j].equals("NA")) {
							meetsCriteria = true;
							locationIndex = i;
						}
					}
				}
				if(!meetsCriteria) {
					System.out.println(newItem.getItemName()+" does not meet location "+decisionNumber.get(i).getLocation().getLocationName() +" criteria. Returning to menu.");
					break;
				}
			}
		}
		
		if(meetsCriteria) {
		// Third, I need to see how "early" this decision is
		//		if I want to assign a new item to a later decision/location
		//			if it's not in the original unconstrained list, it's a brand new item.
		//			then just update the assigned item at the location 
			    if(!decisionNumber.getFirst().getUnconstrainedChoices().contains(newItem)) {
			    	System.out.println("you are at line 136");
			    	decisionNumber.get(locationIndex).getConstrainedChoices().add(newItem);
			    	decisionNumber.get(locationIndex).setItemAssigned(newItem);
			    	// maybe give the user to regenerate the list from here? 
			    	// changing this choice does not effect later decisions
			    }
		//		I must check and see if that item was assigned earlier (if new item is not in location's constrained list, it was)
			    else if (decisionNumber.get(locationIndex).getConstrainedChoices().contains(newItem)) {
			    	System.out.println("you are at line 143");
		//			if it wasn't assigned earlier, then I change this decision
					// insert original item back into unconstrained list of choices
		//				I assign the new item to this location
					decisionNumber.get(locationIndex).setItemAssigned(newItem);

		//					then I rebuild this decision linked list starting after this decision (making sure to remove the new item from the unconstrained list first, which it should automatically)
					decisionNumber.get(locationIndex).getUnconstrainedChoices().remove(newItem);
					//decisionNumber.get(locationIndex).getUnconstrainedChoices().remove(oldItem);
					ArrayList<Item> newItemList = new ArrayList<Item>(decisionNumber.get(locationIndex).getUnconstrainedChoices());
					locationIndex++;
					ArrayList<Location> newLocationList = new ArrayList<Location>(locationList.subList(locationIndex, locationList.size()));
					decisionNumber.subList(locationIndex, decisionNumber.size()).clear();			
					generateDecisionTree(newLocationList,newItemList);
				}
				else {
		//			if it was assigned earlier, then I change that earlier decision and make this later decision/location item assignment a hard constraint
					System.out.println("you are at line 163");
					ArrayList<Item> manAss = new ArrayList<Item>();
					manAss.add(newItem);
					decisionNumber.get(locationIndex).getLocation().setMandatoryAssignments(manAss);
					Integer earliestLocationIndex = findEarliestAssignment(decisionNumber, newItem);
		//				I assign the new item to this location (clear this locations constrained list and add new item to first spot).
					oldItem = decisionNumber.get(earliestLocationIndex).getItemAssigned();
					decisionNumber.get(earliestLocationIndex).setItemAssigned(newItem);
					decisionNumber.get(earliestLocationIndex).getUnconstrainedChoices().add(oldItem);
					decisionNumber.get(earliestLocationIndex).getUnconstrainedChoices().remove(newItem);				
		//					then I rebuild my list starting from that earlier location, making sure to remove the new item from the unconstrained list first
					ArrayList<Item> newItemList = new ArrayList<Item>(decisionNumber.get(earliestLocationIndex).getUnconstrainedChoices());
					locationIndex++;
					ArrayList<Location> newLocationList = new ArrayList<Location>(locationList.subList(earliestLocationIndex, locationList.size()));
					decisionNumber.subList(earliestLocationIndex, decisionNumber.size()).clear();			
					generateDecisionTree(newLocationList,newItemList);
				}
		}
			
		
	}
	
	/* This methods finds the location index for earliest assignment of newItem */
    private static Integer findEarliestAssignment(LinkedList<decision> decisionList, Item newItem) {
    	Integer locationIndex = null;
    	for(int i=0; i<decisionList.size(); i++) {
    		if(decisionList.get(i).getItemAssigned().equals(newItem)) {
    			locationIndex = i;
    			break;
    		}
    	}
    	
		return locationIndex;
	}

	/*
     * The follow two methods read items and locations from a file and return their respective array lists.
     * These were created with simple lists in mind and only assign "name" attributes to their objects.
     */
    public static ArrayList<Item> readItemFile(String filename) throws Exception, FileNotFoundException, IOException {
		FileReader fr = new FileReader(filename);
        BufferedReader textReader = new BufferedReader(fr);
        ArrayList<Item> itemsFromFile = new ArrayList<Item>();
        
        
        int counter = 0;
        while (textReader.ready()) {
        	String[] line = textReader.readLine().split(" "); // for each line read, add 1 to counter.
            Item item = new Item(line[0]);
            item.setItemProperties(line);
            itemsFromFile.add(item);
            counter++;
        }
        textReader.close();
        if(counter <= 0)
                throw new Exception("Error. "+filename+" is empty."); // if no lines read, throw empty file exception
        return itemsFromFile;
    }
    
    public static ArrayList<Location> readLocationFile(String filename) throws Exception, FileNotFoundException, IOException {
		FileReader fr = new FileReader(filename);
        BufferedReader textReader = new BufferedReader(fr);
        ArrayList<Location> locationsFromFile = new ArrayList<Location>();
        
        
        int counter = 0;
        while (textReader.ready()) {
        	String[] line = textReader.readLine().split(" "); // for each line read, add 1 to counter.
            Location location = new Location(line[0]); // this is a simple location list, with no constraints
            location.setLocationCriteria(line);
            locationsFromFile.add(location);
            counter++;
        }
        textReader.close();
        if(counter <= 0)
                throw new Exception("Error. "+filename+" is empty."); // if no lines read, throw empty file exception
        return locationsFromFile;
    }
} 
