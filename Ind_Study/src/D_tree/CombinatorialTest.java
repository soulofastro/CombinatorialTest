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
	
	public static void main(String args[]) throws FileNotFoundException, IOException, Exception 
    { 
		LinkedList<decision> decisionNumber = new LinkedList<decision>();
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
    	for (int i=0; i<locations.size(); i++) {    		
    		decision decision_choice = new decision(locations.get(i));
    		
    		ArrayList<Item> itemCloneClone = new ArrayList<Item>(itemClone);
    		decision_choice.setUnconstrainedChoices(itemCloneClone);
    		
    		decision_choice.setDecisionSelection(itemClone);
    		  		
    		decisionNumber.add(decision_choice);
    		
    	}
    	
    	/* Display Locations and item assigned to each location */
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
    	
    	/* Change a decision and display updated list */
    	/* NOT YET IMPLEMENTED */
    	Item alpha = new Item("Alpha");
    	String[] AlphaProp = {"Alpha", "NA"};
    	alpha.setItemProperites(AlphaProp);
    	changeDecisionSelection(locations.get(1), alpha);
       
    }
	
	/* THIS IS WHERE I CHANGE A DECISION AT A LOCATION */
	/* change item assigned to this location to newItem */
	public static void changeDecisionSelection(Location location, Item newItem) {
		// First, find location in decision list
		// Second, I check to make sure the new item's preferences fits the location's criteria.
		//		if so, continue
		// 		if not tell user they can't assign that item to that location.
		
		// Third, I need to see how "early" this decision is
		//		if I want to assign a new item to a later decision/location
		//		I must check and see if that item was assigned earlier
		//			if it was, then I change that earlier decision and make this later decision/location item assignment a hard constraint
		//				I assign the new item to this location 
		//					then I rebuild my list starting from that earlier location, making sure to remove the new item from the unconstrained list first
		//			if it wasn't, then I change this decision
		//				I assign the new item to this location
		//					then I rebuild this decision linked list starting after this decision (making sure to remove the new item from the unconstrained list first, which is should automatically)
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
            item.setItemProperites(line);
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
