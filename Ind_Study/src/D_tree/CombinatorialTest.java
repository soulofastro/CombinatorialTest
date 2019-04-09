package D_tree;

import java.io.BufferedReader;
//import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
/*
 *  An item is equivalent to a Professor, class, or any object that can be placed into something.
 *  A location is equivalent to a class, time slot, or any thing that an object can be placed into.
 *  A decision_list is asking what items can be placed into a certain location.
 *  A decision_choice is assigning an item to a location.
 * 
 */

public class CombinatorialTest 
{ 
	
	public static ArrayList<decision> decisionNumber = new ArrayList<decision>();	
	
	public static void main(String args[]) throws FileNotFoundException, IOException, Exception { 
		
    	ArrayList<Item> items = new ArrayList<Item>();
    	ArrayList<Location> locations = new ArrayList<Location>();
    	
    	/*
    	// I made this to help me figure out where my txt files are.
    	File file = new File(".");
    	for(String fileNames : file.list()) System.out.println(fileNames);
    	*/
    	
    	items = readItemFile("items.txt");
    	//NOTE: items in file/list must be arranged from most to least constrained. (most to least properties)
    	for (Item item : items) {
    		System.out.print("Item Name: "+ item.getItemProperties()[0]);
    		for (int i=1;i<item.getItemProperties().length;i++) {
    			System.out.print(" "+item.getItemProperties()[i]);
    		}
    		System.out.println();
    	}
    	System.out.println();
    	
    	locations = readLocationFile("locations.txt");
    	for (Location location: locations) {
    		System.out.print("Location name: "+ location.getLocationCriteria()[0]);
    		for (int i=1; i<location.getLocationCriteria().length;i++) {
    			System.out.print(" "+ location.getLocationCriteria()[i]);
    		}
    		System.out.println();
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
//    	Item alpha_2 = new Item("Alpha_2");
//    	String[] AlphaProp = {"Alpha_2", "NA"};
//    	alpha_2.setItemProperties(AlphaProp);
//    	changeDecisionSelection(locations.get(1), alpha_2, locations);
    	
    	/* TEST CASE 2 - change first appearance of item */
//    	System.out.println("TEST 2: Change first decision. Update rest of list");
//    	Item newG = decisionNumber.get(1).getItemAssigned();
//    	changeDecisionSelection(locations.get(2), newG, locations);
    	
    	/* TEST CASE 3 - change earlier appearance of item */
//    	Item echo = decisionNumber.get(4).getItemAssigned();
//    	changeDecisionSelection(locations.get(3), echo, locations);
    	
    	/* TEST CASE 4 - add new item to location that does not meet criteria */
//    	Item charlie = decisionNumber.get(0).getItemAssigned();
//    	changeDecisionSelection(locations.get(2), charlie, locations);
    	
    	/* Display Locations and item assigned to each location after testing change */
//    	System.out.println("\nList after test case change:\n");
//    	printDecisionTree(decisionNumber);
       
    }
	
	/* This Method prints the linked list decision tree */
	private static void printDecisionTree(ArrayList<decision> decisionNumber) {
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
		// This is the first pass through the tree, where I remove mandatory assignments from consideration
		ArrayList<Item> mandatoryAss = new ArrayList<Item>();
		for (int i=0;i<locations.size();i++) {
			if(locations.get(i).getMandatoryAssignments().size() > 0) {
				mandatoryAss.addAll(locations.get(i).getMandatoryAssignments());
			}
		}
		// This is the second pass through the tree, assigning normally
    	for (int i=0; i<locations.size(); i++) {    		
    		decision decision_choice = new decision(locations.get(i)); 		
    		ArrayList<Item> itemClone = new ArrayList<Item>(items);
    		itemClone.removeAll(mandatoryAss);
    		items.removeAll(mandatoryAss);
    		decision_choice.setUnconstrainedChoices(itemClone);		
    		decision_choice.setDecisionSelection(items);		  		
    		decisionNumber.add(decision_choice); 		
    	}
    	// This is the third pass thru the tree. Iterate through the tree, removing assigned items from all constrained choice lists
    	StringBuffer NA = new StringBuffer("null");
    	for(decision choice: decisionNumber) {
    		if(!choice.getDecisionSelected().contentEquals(NA)) {
    			for(decision eachDecision: decisionNumber) {
    				eachDecision.getConstrainedChoices().removeAll(Collections.singleton(choice.getItemAssigned()));
    			}
    		}
    	}
    	
    	// Now revisit each decision node with a "null" assigned decision and select first item from new constrained lists.
    	
    	/*for(decision secondPass: decisionNumber) { // old loop
    		if(secondPass.getDecisionSelected().contentEquals(NA)) {
    			Item item = secondPass.getConstrainedChoices().get(0);
    			secondPass.setItemAssigned(item);
    			// Now remove that item from the constrained choice lists
    			for(decision eachDecision: decisionNumber) {
    				eachDecision.getConstrainedChoices().remove(item);
    				//eachDecision.getUnconstrainedChoices().remove(item);
    			}
    		}
    	}*/
    	for(int i=0;i<decisionNumber.size();i++) { // new loop
       		if(decisionNumber.get(i).getDecisionSelected().contentEquals(NA)) {
       			Item item;
       			if(decisionNumber.get(i).getConstrainedChoices().size() > 0) {
       				item = decisionNumber.get(i).getConstrainedChoices().get(0);
       			}
       			else
       				item = new Item("No item assigned");
    			decisionNumber.get(i).setItemAssigned(item);
    			decisionNumber.get(i).getConstrainedChoices().removeAll(Collections.singleton(item));
    			// Now remove that item from the choice lists that follow
    			for(int j=i+1;j<decisionNumber.size();j++) {
    				decisionNumber.get(j).getConstrainedChoices().removeAll(Collections.singleton(item));
    				decisionNumber.get(j).getUnconstrainedChoices().removeAll(Collections.singleton(item));
    			}
    		}
    	}
    	// Finally, go back and add the decided items back to their constrained lists if it's not already in them
    	for(decision finalPass: decisionNumber) {
    		if(!finalPass.getConstrainedChoices().contains(finalPass.getItemAssigned())) {
    			finalPass.getConstrainedChoices().add(0,finalPass.getItemAssigned());
    		}
    		if(!finalPass.getUnconstrainedChoices().contains(finalPass.getItemAssigned())) {
    			finalPass.getUnconstrainedChoices().add(finalPass.getItemAssigned());
    			if(finalPass.getUnconstrainedChoices().size() > 2)
    				Collections.sort(finalPass.getUnconstrainedChoices(), Item.PropComparator);
    		}
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
/*need to update this part bc of more properties and criteria */
System.out.println("This section at line 166 needs to be updated!!! It only checks to see if it fits at least one criteria");
						if(newItem.getItemProperties()[j].equals(decisionNumber.get(i).getLocation().getLocationCriteria()[k]) || newItem.getItemProperties()[j].equals("[NA]")) {
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
			    if(!decisionNumber.get(0).getUnconstrainedChoices().contains(newItem)) {
			    	System.out.println("^^^^^^you are at line 202. Unconstrained choices doesn't contain new item.");
			    	decisionNumber.get(locationIndex).getConstrainedChoices().add(newItem);
			    	decisionNumber.get(locationIndex).setItemAssigned(newItem);
			    	// maybe give the user the option to regenerate the list from here? 
			    	// changing this choice does not immediately effect later decisions
			    }
		//		I must check and see if that item was assigned earlier (if new item is not in location's constrained list, it was)
			    else if (decisionNumber.get(locationIndex).getConstrainedChoices().contains(newItem)) {
			    	System.out.println("^^^^^^you are at line 210. Constrained choices contains new item. Assigned earlier chech = true");
		//			if it wasn't assigned earlier, then I change this decision
					// insert original item back into unconstrained list of choices (turns out this isn't necessary)
		//				I assign the new item to this location
					decisionNumber.get(locationIndex).setItemAssigned(newItem);

		//					then I rebuild this decision linked list starting after this decision (making sure to remove the new item from the unconstrained list first)
					decisionNumber.get(locationIndex).getUnconstrainedChoices().remove(newItem);
					ArrayList<Item> newItemList = new ArrayList<Item>(decisionNumber.get(locationIndex).getUnconstrainedChoices());
					locationIndex++;
					ArrayList<Location> newLocationList = new ArrayList<Location>(locationList.subList(locationIndex, locationList.size()));
					decisionNumber.subList(locationIndex, decisionNumber.size()).clear();			
					generateDecisionTree(newLocationList,newItemList);
				}
				else {
		//			if it was assigned earlier, then I change that earlier decision and make this later decision/location item assignment a hard constraint
					System.out.println("^^^^^^^you are at line 226. Making hard constraint at location.");
					ArrayList<Item> manAss = new ArrayList<Item>();
					manAss.add(newItem);
					decisionNumber.get(locationIndex).getLocation().setMandatoryAssignments(manAss);
					Integer earliestLocationIndex = findEarliestAssignment(decisionNumber, newItem);
		//				I assign the new item to this location and add the old assignment back to the unconstrained choices list
					oldItem = decisionNumber.get(earliestLocationIndex).getItemAssigned();
					decisionNumber.get(earliestLocationIndex).setDecisionSelection(decisionNumber.get(earliestLocationIndex).getConstrainedChoices());
					//decisionNumber.get(earliestLocationIndex).setItemAssigned(newItem);
					decisionNumber.get(earliestLocationIndex).getUnconstrainedChoices().add(oldItem);
					decisionNumber.get(earliestLocationIndex).getUnconstrainedChoices().remove(newItem);				
		//					then I rebuild my list starting from that earlier location
					ArrayList<Item> newItemList = new ArrayList<Item>(decisionNumber.get(earliestLocationIndex).getUnconstrainedChoices());
					//locationIndex++;
					ArrayList<Location> newLocationList = new ArrayList<Location>(locationList.subList(earliestLocationIndex, locationList.size()));
					decisionNumber.subList(earliestLocationIndex, decisionNumber.size()).clear();			
					generateDecisionTree(newLocationList,newItemList);
				}
		}
			
		
	}
	
	/* This methods finds the location index for earliest assignment of newItem */
    private static Integer findEarliestAssignment(ArrayList<decision> decisionList, Item newItem) {
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
        ArrayList<Item> itemsWithNA = new ArrayList<Item>();
        
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
        
        /* Sort items from most to least constrained according to length of their properties (aka requirements) */
        StringBuffer NA = new StringBuffer("[NA]");
        for(Item item: itemsFromFile) {
        	if(item.getItemProperties()[1].contentEquals(NA)) {
        		itemsWithNA.add(item);
        	}
        }
        itemsFromFile.removeAll(itemsWithNA);
        Collections.sort(itemsFromFile, Item.PropComparator);
        itemsFromFile.addAll(itemsWithNA);
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
        
        /* Sort locations from most to least constrained according to length of their criteria (aka requirements) */
        Collections.sort(locationsFromFile, Location.critComparator);
        return locationsFromFile;
    }
} 
