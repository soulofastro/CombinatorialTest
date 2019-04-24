package D_tree;

import java.io.BufferedReader;
//import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
/*
 *  An item is equivalent to a Professor, class, or any object that can be placed into something.
 *  A location is equivalent to a class, time slot, or any thing that an object can be placed into.
 *  A decision_list is asking what items can be placed into a certain location.
 *  A decision_choice is assigning an item to a location.
 * 
 */

public class CombinatorialTest 
{ 
	
	//public static ArrayList<decision> decisionNumber = new ArrayList<decision>();	
	public static ArrayList<Item> itemsDuplicate = new ArrayList<Item>();
	
	
	public static void run() throws FileNotFoundException, IOException, Exception { 
		
    	ArrayList<Item> items = new ArrayList<Item>();
    	ArrayList<Location> locations = new ArrayList<Location>();
    	ArrayList<decision> decisionNumber = new ArrayList<decision>();
    	
    	/*
    	// I made this to help me figure out where my txt files are.
    	File file = new File(".");
    	for(String fileNames : file.list()) System.out.println(fileNames);
    	*/
    	
    	items = readItemFile("itemsSimple.txt");
    	//NOTE: items in file/list must be arranged from most to least constrained. (most to least properties)
    	printItemList(items);
    	
    	
    	AssignmentTracker.newTracker(items);
    	AssignmentTracker.add(new Item("null"));
    	AssignmentTracker.add(new Item("No item assigned"));
    	
    	locations = readLocationFile("locationsSimple.txt");
    	printLocationList(locations);

    	
    	/* constraints check */
//    	ConstraintsCheck.generateCheckWithoutAND(items,locations);
    	ConstraintsCheck.generateCheckWithAND(items,locations);
    	
    	/* Look at each location and assign an item to it */
    	/* This generates a decision node that we can later revisit */
    	/* The actual decision choice is made inside the decision class when the node is generated */
    	ArrayList<Item> itemClone = new ArrayList<Item>(items);	
    	decisionNumber = generateDecisionTree(locations, itemClone);
    	
    	/* Display Locations and item assigned to each location */
//    	printDecisionTree(decisionNumber);
    	printPlainDecisionTree(decisionNumber);

    	
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
    	

    	for (Item item : items) {
    		System.out.println("Item Name: "+ item.getItemProperties().get(0)+", Times assigned: "+item.getNumberOfTimesAssigned()+" of "+item.getAssignmentLimit());
    	}
    	System.out.println();
    }

	private static void printLocationList(ArrayList<Location> locations) {
    	for (Location location: locations) {
    		System.out.print("Location name: "+ location.getLocationCriteria().get(0)+",Constraints:->");
    		for (int i=1; i<location.getLocationCriteria().size();i++) {
    			System.out.print(" "+ location.getLocationCriteria().get(i));
    		}
    		System.out.print(" <-|| Number of Constraints: "+location.getNumberOfConstraints());
    		System.out.println();
    	}
    	System.out.println();
		
	}

	private static void printItemList(ArrayList<Item> items) {
		for (Item item : items) {
    		System.out.print("Item Name: "+ item.getItemProperties().get(0)+", Constraints:->");
    		for (int i=1;i<item.getItemProperties().size();i++) {
    			System.out.print(" "+item.getItemProperties().get(i));
    		}
    		System.out.print(" <-|| Number of Constraints: "+item.getNumberOfConstraints());
    		System.out.println();
    	}
    	System.out.println();
		
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
	/* This Method prints the linked list decision tree in ascending order*/
	private static void printPlainDecisionTree(ArrayList<decision> decisionNumber) {
		ArrayList<decision> dTree = new ArrayList<decision>(decisionNumber);
		
//		Collections.sort(dTree,(decision1, decision2) -> Integer.parseInt(decision1.getLocation().getLocationName()) - Integer.parseInt(decision2.getLocation().getLocationName()));		
    	try {
			for(int i=0; i<dTree.size(); i++) {	
				Integer y = i+1;
				System.out.println("Decision " + y.toString() + ": "+"Location-> "+ dTree.get(i).getLocation().getLocationName() + ", item-> "+ dTree.get(i).getDecisionSelected());
//				if(!dTree.get(i).getDecisionSelected().contentEquals("No item assigned")) {
//					System.out.print("		Matched properties when this decision was made: ");
//					for(String match: dTree.get(i).getMatches()) {System.out.print(" "+ match);}
//					System.out.println();
//				}	
			System.out.println();
			}
    	}
    	catch(Exception e) {
    		// Location runs into null assignment
    		System.out.println("Assignment error. Too many or incompatible constraints likely.\nMake sure items with NA preference are last in txt file.");
    	}
		
	}

	/*This is where I generate the decision tree */
	public static ArrayList<decision> generateDecisionTree(ArrayList<Location> locations, ArrayList<Item> items) {
		// This is the first pass through the tree, where I add mandatory assignments into a list
		ArrayList<decision> decisionTree = new ArrayList<decision>();
		
		ArrayList<Item> mandatoryAss = new ArrayList<Item>();
		for (int i=0;i<locations.size();i++) {
			if(locations.get(i).getMandatoryAssignments().size() > 0) {
				mandatoryAss.addAll(locations.get(i).getMandatoryAssignments());
			}
		}
		// This is the second pass through the tree, assigning normally
		
		// look at all mandatory assignments, see how often each one must assigned and increment counts
		// then just assign mandos at their specified location without checking to see if they are at assignment limit
		// e.g. if x must be assigned at y, but x has an assignment limit of 2, increment x by 1, so it has one remaining
		//		then it will be assigned somewhere and incremented to 2, and when the software goes to assign it to the mand ass
		// 		location, it will do so and not increment assignment count (since I did it at the beginning).
		for(Item item: mandatoryAss) {
			AssignmentTracker.increaseCounts(item);
		}
		
    	for (int i=0; i<locations.size(); i++) {    		
    		decision decision_choice = new decision(locations.get(i)); 		
    		ArrayList<Item> itemClone = new ArrayList<Item>(items);
    		decision_choice.setUnconstrainedChoices(itemClone);		
    		decision_choice.setDecisionSelection(items);		  		
    		decisionTree.add(decision_choice); 		
    	}
    	// This is the third pass thru the tree. Iterate through the tree, removing assigned items from all constrained choice lists
    	StringBuffer NA = new StringBuffer("null");
    	for(decision choice: decisionTree) {
    		if(!choice.getDecisionSelected().contentEquals(NA)) {
    			for(decision eachDecision: decisionTree) {
    				if(AssignmentTracker.atLimit(choice.getItemAssigned())) {
    					eachDecision.getConstrainedChoices().removeAll(Collections.singleton(choice.getItemAssigned()));
    				}
    			}
    		}
    	}
    	
    	StringBuffer noItem = new StringBuffer("No item assigned");
    	
    	/* Now revisit each decision node with a "null" assigned decision and select first item from new constrained lists.*/
    	/* these constrained lists should be composed only of available items */
    	for(int i=0;i<decisionTree.size();i++) { // new loop
       		if(decisionTree.get(i).getDecisionSelected().contentEquals(NA)) {
       			Item item = new Item("No item assigned");
       			if(decisionTree.get(i).getConstrainedChoices().size() > 0) {
       				item = decisionTree.get(i).getConstrainedChoices().get(0);
       			}
       			decisionTree.get(i).setItemAssigned(item);
    			
    			AssignmentTracker.increaseCounts(item);
    			
    			// remove this assigned item from it's decision lists
    			decisionTree.get(i).getConstrainedChoices().removeAll(Collections.singleton(item));
    			if(item.getItemName().contentEquals(noItem)) {
    				decisionTree.get(i).getUnconstrainedChoices().removeAll(Collections.singleton(item));
    				decisionTree.get(i).getConstrainedChoices().removeAll(Collections.singleton(item));
    			}
    			// Now remove the assigned item from the choice lists that follow
    			for(int j=i+1;j<decisionTree.size();j++) {
    				if(AssignmentTracker.atLimit(item)){
    					decisionTree.get(j).getConstrainedChoices().removeAll(Collections.singleton(item));
    					decisionTree.get(j).getUnconstrainedChoices().removeAll(Collections.singleton(item));
    				}
    			}
    		}
    	}
    	// Next, go back and add the decided items back to their constrained lists if it's not already in them
    	for(decision nextPass: decisionTree) {
    		if(!nextPass.getConstrainedChoices().contains(nextPass.getItemAssigned())) {
    			nextPass.getConstrainedChoices().add(0,nextPass.getItemAssigned());
    		}
    		if(!nextPass.getUnconstrainedChoices().contains(nextPass.getItemAssigned()) && !nextPass.getItemAssigned().getItemName().contentEquals(noItem)) {
    			nextPass.getUnconstrainedChoices().add(nextPass.getItemAssigned());
    			/*if(finalPass.getConstrainedChoices().size() == 1)
    				Collections.sort(finalPass.getUnconstrainedChoices(), Item.PropComparator); */
    		}
    	}
    	// Finally, go back and check for locations with "No item assigned" and check to see if one item in the original list fit
    	
    	for(decision finalPass: decisionTree) {
    		//System.out.println("xxxx loc: "+finalPass.getLocation().getLocationName());
    		if(finalPass.getDecisionSelected().contentEquals(noItem)) {
    			//look in original location list to see if anything fits. If only one item can be assigned here, assign it and rebuild the list.
    			itemsDuplicate = new ArrayList<Item>(AssignmentTracker.itemAssCount);
    			//System.out.println("FOunD NO ITEM ASSIGNED. dupe size:"+itemsDuplicate.size());
    			finalPass.setConstrainedChoices(itemsDuplicate,00); // here it tries to build a list, it should ignore times assigned
    			
    			if(finalPass.getConstrainedChoices().size() > 0) {
    				//System.out.println("CONSTR CHOICES > 0");
    				LinkedHashSet<Item>	set = new LinkedHashSet<Item>(finalPass.getConstrainedChoices());
    				ArrayList<Item> newList = new ArrayList<Item>();
    				newList.addAll(set);
    				
	    			if(set.size()==1) {
	    				Item singlet = finalPass.getConstrainedChoices().get(0);
	    				AssignmentTracker.decreaseCounts(singlet);
	    				finalPass.getConstrainedChoices().clear();
	    				//System.out.println("LINE 227 TRYING TO FIX NO ITEM ASSIGNED");
	    				decisionTree = changeDecisionSelection(finalPass.getLocation(), singlet, locations, decisionTree);
	    			}    			
	    			else {
	    				for(Item iter: newList) { System.out.print(iter.getItemName()+", ");}
	    				System.out.println("could not be assigned to Location "+finalPass.getLocation().getLocationName()+" because they were assigned at another location.\n");
	    			}
    			}
    		}
    	}
    
    	decisionTree = generateMediumConfidenceDecisionTree(decisionTree);
    	decisionTree = generateLowConfidenceDecisionTree(decisionTree);
    	return decisionTree;
	}

	/* Go through decisions and try to assign items that only partially fit to locations */
	private static ArrayList<decision> generateMediumConfidenceDecisionTree(ArrayList<decision> decisionTree) {
		ArrayList<decision> decisionNumber = new ArrayList<decision>(decisionTree);
		//System.out.println("GENERATING MED CONF TREE");
		Item item = decisionNumber.get(decisionNumber.size()-1).getItemAssigned();
		ArrayList<Item> Unassigned = new ArrayList<Item>(decisionNumber.get(decisionNumber.size()-1).getUnconstrainedChoices());
		if(AssignmentTracker.atLimit(item)){
			Unassigned.removeAll(Collections.singleton(item));
		}
		StringBuffer noItem = new StringBuffer("No item assigned");
		
		for(int i=0;i<decisionNumber.size();i++) {
			if(decisionNumber.get(i).getItemAssigned().getItemName().contentEquals(noItem)) {
				ArrayList<String> locationCrit = new ArrayList<String>(decisionNumber.get(i).getLocation().getLocationCriteria());
				decisionNumber.get(i).getMediumConfidenceFit(Unassigned, locationCrit);
				Item medItem = decisionNumber.get(i).getItemAssigned();
				for(int j=i+1; j<decisionNumber.size();j++) {
					if(AssignmentTracker.atLimit(medItem)){
						decisionNumber.get(j).getUnconstrainedChoices().removeAll(Collections.singleton(medItem));
					}
				}
			}
		}		
		return decisionNumber;
	}
	/* go through decisions and assign items that are leftover to open locations without checking criteria */
	private static ArrayList<decision> generateLowConfidenceDecisionTree(ArrayList<decision> decisionTree) {
		ArrayList<decision> decisionNumber = new ArrayList<decision>(decisionTree);
		//System.out.println("GENERATING LOW CONF TREE");
		Item item = decisionNumber.get(decisionNumber.size()-1).getItemAssigned();
		ArrayList<Item> Unassigned = new ArrayList<Item>(decisionNumber.get(decisionNumber.size()-1).getUnconstrainedChoices());
		if(AssignmentTracker.atLimit(item)){
			Unassigned.removeAll(Collections.singleton(item));
		}
		StringBuffer noItem = new StringBuffer("No item assigned");
		
		for(int i=0;i<decisionNumber.size();i++) {
			if(decisionNumber.get(i).getItemAssigned().getItemName().contentEquals(noItem)) {
				decisionNumber.get(i).getLowConfidenceFit(Unassigned);
				Item medItem = decisionNumber.get(i).getItemAssigned();
				if(AssignmentTracker.atLimit(medItem)){
					Unassigned.remove(medItem);
				}
				for(int j=i+1; j<decisionNumber.size();j++) {
					if(AssignmentTracker.atLimit(medItem)){
						decisionNumber.get(j).getUnconstrainedChoices().removeAll(Collections.singleton(medItem));
					}
				}
			}
		}
		return decisionNumber;
	}

	/** not sure how getItemAssigned().getItemProperties().addAll(1, getLocation().getLocationCriteria()); in "decision" class will alter when I have to change a decision **/
	// keep an eye out for odd behavior!
	/* THIS IS WHERE I CHANGE A DECISION AT A LOCATION */
	/* change item assigned to this location to newItem */
	/* arguments are ("where you want to put the new item", "the new item you want to insert", "complete location list")*/
	public static ArrayList<decision> changeDecisionSelection(Location location, Item newItem, ArrayList<Location> locationList, ArrayList<decision> decisionTree) {
		ArrayList<decision> decisionNumber = new ArrayList<decision>(decisionTree);
		boolean meetsCriteria = false;
		Integer locationIndex = null;
		Item oldItem;
		
		// First, find location in decision list
		for(int i=0; i<decisionNumber.size(); i++) {
			if(decisionNumber.get(i).getLocation().equals(location)) {
		// Second, I check to make sure the new item's preferences fits the location's criteria.
		//		if so, continue
        //		if not tell user they can't assign that item to that location.
				for(int j=0; j<newItem.getItemProperties().size(); j++) {
					for(int k=0; k<decisionNumber.get(i).getLocation().getLocationCriteria().size(); k++) {
						if(newItem.getItemProperties().get(j).equals(decisionNumber.get(i).getLocation().getLocationCriteria().get(k)) || newItem.getItemProperties().get(j).equals("[NA]")) {
							meetsCriteria = true;
							locationIndex = i;
						}
					}
				}
				if(!meetsCriteria) {
					System.out.println(newItem.getItemName()+" does not meet location "+decisionNumber.get(i).getLocation().getLocationName() +" criteria. Returning to menu.");
					return decisionNumber;
				}
			}
		}
		
		if(meetsCriteria) {
		// Third, I need to see how "early" this decision is
		//		if I want to assign a new item to a later decision/location
		//			if it's not in the original unconstrained list, it's a brand new item.
		//			then just update the assigned item at the location 
			    if(!decisionNumber.get(0).getUnconstrainedChoices().contains(newItem)) {
			    	//System.out.println("^^^^^^you are at line 342. Unconstrained choices doesn't contain new item.\n");
			    	decisionNumber.get(locationIndex).getConstrainedChoices().add(newItem);
			    	
			    	AssignmentTracker.itemAssCount.add(newItem);
			    	// add new item and increase its assignment count by 1
			    	AssignmentTracker.increaseCounts(newItem);
					/*if(!newItem.getItemName().equals("null")||!newItem.getItemName().equals("No item assigned")){
						Integer index = AssignmentTracker.itemAssCount.indexOf(newItem);
						Integer count = AssignmentTracker.itemAssCount.get(index).getNumberOfTimesAssigned();
						count += 1;
						AssignmentTracker.itemAssCount.get(AssignmentTracker.itemAssCount.indexOf(newItem)).setNumberOfTimesAssigned(count);
					}*/
					
		    	    decisionNumber.get(locationIndex).setItemAssigned(newItem);
			    	// maybe give the user the option to regenerate the list from here? 
			    	// changing this choice does not immediately effect later decisions
		    	    return decisionNumber;
			    }
		//		I must check and see if that item was assigned earlier (if new item is in location's constrained list and count < max, its ok to assign)
		// 		
			    else if (decisionNumber.get(locationIndex).getConstrainedChoices().contains(newItem) && AssignmentTracker.checkCount(newItem)) {
			    	//System.out.println("^^^^^^you are at line 361. Constrained choices contains newItem. It hasn't reached its assignment limit.\n");
		//			if it wasn't assigned earlier, then I change this decision
		//				I assign the new item to this location
			    	// decrease count of old item and then assign new Item and increase its count.
			    	AssignmentTracker.decreaseCounts(decisionNumber.get(locationIndex).getItemAssigned());
					
					decisionNumber.get(locationIndex).setItemAssigned(newItem);
					AssignmentTracker.increaseCounts(newItem);

		//					then I rebuild this decision list starting after this decision (making sure to remove the new item from the unconstrained list first)
					if(AssignmentTracker.atLimit(newItem)) {
						decisionNumber.get(locationIndex).getUnconstrainedChoices().remove(newItem);
					}
					ArrayList<Item> newItemList = new ArrayList<Item>(decisionNumber.get(locationIndex).getUnconstrainedChoices());
					
					for(int i = locationIndex+1; i<decisionNumber.size();i++) {
						// I need to decrease each corresponding items assignment count by one in itemAssCount for each appearance in the rest of decisionNumber
						AssignmentTracker.decreaseCounts(decisionNumber.get(i).getItemAssigned());
					}
					
					locationIndex++;
					ArrayList<Location> newLocationList = new ArrayList<Location>(locationList.subList(locationIndex, locationList.size()));
					decisionNumber.subList(locationIndex, decisionNumber.size()).clear();			
					decisionNumber = generateDecisionTree(newLocationList,newItemList);
					return decisionNumber;
				}
				else {
		//			if it was assigned earlier, then I change that earlier decision and make this later decision/location item assignment a hard constraint
					//System.out.println("^^^^^^^you are at line 395. Making hard constraint at location.\n");
					ArrayList<Item> manAss = new ArrayList<Item>();
					manAss.add(newItem);
					decisionNumber.get(locationIndex).getLocation().getMandatoryAssignments().addAll(manAss);
					
					Integer earliestLocationIndex = findEarliestAssignment(decisionNumber, newItem);
					//Integer earliestLocationIndex = findLastAssignment(decisionNumber, newItem);
					
		//				I assign the new item to this location and add the old assignment back to the unconstrained choices list
					oldItem = decisionNumber.get(earliestLocationIndex).getItemAssigned();
					
					//System.out.println("I am changing "+location.getLocationName()+" to "+newItem.getItemName());
					
					AssignmentTracker.resetCount(oldItem);
					decisionNumber.get(earliestLocationIndex).setDecisionSelection(decisionNumber.get(earliestLocationIndex).getConstrainedChoices(), oldItem);
					//decisionNumber.get(earliestLocationIndex).setItemAssigned(newItem);
					
					//System.out.println("It was assigned earlier, so I have to change "+ decisionNumber.get(earliestLocationIndex).getLocation().getLocationName()+" to "+ decisionNumber.get(earliestLocationIndex).getItemAssigned().getItemName());
					
					if(!decisionNumber.get(earliestLocationIndex).getUnconstrainedChoices().contains(oldItem)) {
						decisionNumber.get(earliestLocationIndex).getUnconstrainedChoices().add(oldItem);
						//AssignmentTracker.decreaseCounts(oldItem);
					}
					/*else if(AssignmentTracker.checkCount(oldItem)) {
						AssignmentTracker.increaseCounts(oldItem);
					}*/
					/*if(AssignmentTracker.atLimit(newItem)) {
						decisionNumber.get(earliestLocationIndex+1).getUnconstrainedChoices().remove(newItem);
					}*/
		//					then I rebuild my list starting from that earlier location
					ArrayList<Item> newItemList = new ArrayList<Item>(decisionNumber.get(earliestLocationIndex).getUnconstrainedChoices());
					earliestLocationIndex++;
			
					
					for(int i = locationIndex; i<decisionNumber.size();i++) {
						// I need to decrease each corresponding items assignment count by one in itemAssCount for each appearance in the rest of decisionNumber
						AssignmentTracker.decreaseCounts(decisionNumber.get(i).getItemAssigned());
					}
					
					ArrayList<Location> newLocationList = new ArrayList<Location>(locationList.subList(earliestLocationIndex, locationList.size()));
					decisionNumber.subList(earliestLocationIndex, decisionNumber.size()).clear();			
					decisionNumber.addAll(generateDecisionTree(newLocationList,newItemList));
					return decisionNumber;
				}
		}
		return decisionNumber;
			
		
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
    private static Integer findLastAssignment(ArrayList<decision> decisionList, Item newItem) {
    	Integer locationIndex = null;
    	for(int i=0; i<decisionList.size(); i++) {
    		if(decisionList.get(i).getItemAssigned().equals(newItem)) {
    			locationIndex = i;
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
        	String[] line = textReader.readLine().split(","); // for each line read, add 1 to counter.
            Item item = new Item(line[0]);
            item.setAssignmentLimit(Integer.parseInt(line[1]));
            item.setItemProperties(line);
            itemsFromFile.add(item);
            counter++;
        }
        textReader.close();
        if(counter <= 0)
                throw new Exception("Error. "+filename+" is empty."); // if no lines read, throw empty file exception
        
        /* Sort items from most to least constrained according to length of their properties (aka requirements) */
        StringBuffer NA = new StringBuffer("[NA]");
        if(!itemsFromFile.isEmpty()) {
	        for(Item item: itemsFromFile) {
	        	if(!item.getItemProperties().isEmpty()/*.get(1) != null*/){
		        	if(item.getItemProperties().get(1).contentEquals(NA)) {
		        		itemsWithNA.add(item);
		        	}
	        	}
	        }
        itemsFromFile.removeAll(itemsWithNA);
        Collections.sort(itemsFromFile, Item.PropComparator);
        itemsFromFile.addAll(itemsWithNA);
        }
        return itemsFromFile;
    }
    
    public static ArrayList<Location> readLocationFile(String filename) throws Exception, FileNotFoundException, IOException {
		FileReader fr = new FileReader(filename);
        BufferedReader textReader = new BufferedReader(fr);
        ArrayList<Location> locationsFromFile = new ArrayList<Location>();
        ArrayList<Location> locationsWithNA = new ArrayList<Location>();
        
        
        int counter = 0;
        while (textReader.ready()) {
        	String[] line = textReader.readLine().split(","); // for each line read, add 1 to counter.
            Location location = new Location(line[0]); // this is a simple location list, with no constraints
            location.setLocationCriteria(line);
            locationsFromFile.add(location);
            counter++;
        }
        textReader.close();
        if(counter <= 0)
                throw new Exception("Error. "+filename+" is empty."); // if no lines read, throw empty file exception
        
        /* Sort locations from most to least constrained according to length of their criteria (aka requirements) */
        /* Sort items from most to least constrained according to length of their properties (aka requirements) */
        StringBuffer NA = new StringBuffer("[NA]");
        if(!locationsFromFile.isEmpty()) {
	        for(Location location: locationsFromFile) {
	        	if(!location.getLocationCriteria().isEmpty()/*.get(1) != null*/){
		        	if(location.getLocationCriteria().get(1).contentEquals(NA)) {
		        		locationsWithNA.add(location);
		        	}
	        	}
	        }
	    locationsFromFile.removeAll(locationsWithNA);
        Collections.sort(locationsFromFile, Location.critComparator);
        locationsFromFile.addAll(locationsWithNA);
        }
        return locationsFromFile;
    }
} 
