package model;

//import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
/*
 *  An item is equivalent to a Professor, class, or any object that can be placed into something.
 *  A location is equivalent to a class, time slot, or any thing that an object can be placed into.
 *  A decision_list is asking what items can be placed into a certain location.
 *  A decision_choice is assigning an item to a location.
 * 
 */

public class ItemLocationTreeMaker 
{ 
	
	//public static ArrayList<decision> decisionNumber = new ArrayList<decision>();	
	public static ArrayList<Item> itemsDuplicate = new ArrayList<Item>();
	
	
	public static ArrayList<ILDecision> createILtree(ArrayList<Item> itemList, ArrayList<Location> locationList) throws FileNotFoundException, IOException, Exception { 
		
    	ArrayList<Item> items = new ArrayList<Item>(itemList);
    	ArrayList<Location> locations = new ArrayList<Location>(locationList);
    	ArrayList<ILDecision> decisionNumber = new ArrayList<ILDecision>();
    	
    	//NOTE: items in file/list must be arranged from most to least constrained. (most to least properties)
    	printItemList(items);
    	
    	
    	ItemAssignmentTracker.newTracker(items);
    	ItemAssignmentTracker.add(new Item("null"));
    	ItemAssignmentTracker.add(new Item("No item assigned"));
    	
    	printLocationList(locations);

    	
    	/* constraints check */
//    	ConstraintsCheck.generateCheckWithoutAND(items,locations);
//    	ConstraintsCheck.generateCheckWithAND(items,locations);
    	
    	/* Look at each location and assign an item to it */
    	/* This generates a decision node that we can later revisit */
    	/* The actual decision choice is made inside the decision class when the node is generated */
    	ArrayList<Item> itemClone = new ArrayList<Item>(items);	
    	decisionNumber = generateDecisionTree(locations, itemClone);
    	
    	/* Display Locations and item assigned to each location */
    	printDecisionTree(decisionNumber);
//    	printPlainDecisionTree(decisionNumber);

    	
    	/* Change a decision and display updated list */
    	/* arguments are changeDecisionSelection("where you want to put the new item", "the new item you want to insert", "complete location list")*/
    	/* TEST CASE 1 - add brand new item */
//    	Item alpha_2 = new Item("Alpha_2");
//    	String[] AlphaProp = {"Alpha_2", "NA"};
//    	alpha_2.setItemProperties(AlphaProp);
//    	decisionNumber = changeDecisionSelection(locations.get(1), alpha_2, locations, decisionNumber);
    	
    	/* TEST CASE 2 - change first appearance of item */
//    	System.out.println("TEST 2: Change first decision. Update rest of list");
//    	Item alpha = decisionNumber.get(6).getItemAssigned();
//    	System.out.println("Changing item at "+locations.get(1).getLocationName()+" to "+alpha.getItemName());
//    	decisionNumber = changeDecisionSelection(locations.get(1), alpha, locations, decisionNumber);
    	
//    	/* TEST CASE 3a - change earlier appearance of item */
//    	Item charlie = decisionNumber.get(0).getItemAssigned();
//    	System.out.println("Changing item at "+locations.get(5).getLocationName()+" to "+charlie.getItemName());
//    	decisionNumber = changeDecisionSelection(locations.get(5), charlie, locations, decisionNumber);
//    	/* TEST CASE 3b - change earlier appearance of item */
//    	Item beta = decisionNumber.get(1).getItemAssigned();
//    	System.out.println("Changing item at "+locations.get(6).getLocationName()+" to "+beta.getItemName());
//    	decisionNumber = changeDecisionSelection(locations.get(6), beta, locations, decisionNumber);
    	
    	/* TEST CASE 4 - add new item to location that does not meet criteria */
    	// don't care about this anymore
//    	Item charlie = decisionNumber.get(0).getItemAssigned();
//    	decisionNumber = changeDecisionSelection(locations.get(2), charlie, locations, decisionNumber);
    	
    	/* Display Locations and item assigned to each location after testing change */
//    	System.out.println("\nList after test case change:\n");
//    	printDecisionTree(decisionNumber);
    	

    	for (Item item : items) {
    		System.out.println("Item Name: "+ item.getItemProperties().get(0)+", Times assigned: "+item.getNumberOfTimesAssigned()+" of "+item.getAssignmentLimit());
    	}
    	System.out.println("*************************************************************************************");
    	
    	return decisionNumber;
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
	private static void printDecisionTree(ArrayList<ILDecision> decisionNumber) {
    	try {
			for(int i=0; i<decisionNumber.size(); i++) {	
				Integer y = i+1;
				System.out.println("Decision " + y.toString() + ": "+"Location-> "+ decisionNumber.get(i).getLocation().getLocationName() + ", item-> "+ decisionNumber.get(i).getDecisionSelected());
				if(decisionNumber.get(i).getConstrainedChoices().size() >0)
					decisionNumber.get(i).printConstrainedChoices();
				if(decisionNumber.get(i).getUnconstrainedChoices().size() >0)
					decisionNumber.get(i).printUnconstrainedChoices();
				if(decisionNumber.get(i).getLocation().getMandatoryDecisionAssignments().size() > 0)
					decisionNumber.get(i).printMandoAssignments();
				if(decisionNumber.get(i).getPartialFitChoices().size() > 0 && !decisionNumber.get(i).getPartialFitChoices().equals(null))
					decisionNumber.get(i).printPartialMatches();
				System.out.println();
			}
    	}
    	catch(Exception e) {
    		// Location runs into null assignment
    		System.out.println("Assignment error. Too many or incompatible constraints likely.\nMake sure items with NA preference are last in txt file.");
    	}
		
	}
	/* This Method prints the linked list decision tree in ascending order*/
	private static void printPlainDecisionTree(ArrayList<ILDecision> decisionNumber) {
		ArrayList<ILDecision> dTree = new ArrayList<ILDecision>(decisionNumber);
		
//		Collections.sort(dTree,(decision1, decision2) -> Integer.parseInt(decision1.getLocation().getLocationName()) - Integer.parseInt(decision2.getLocation().getLocationName()));		
    	try {
			for(int i=0; i<dTree.size(); i++) {	
				Integer y = i+1;
				System.out.println("Decision " + y.toString() +", Name: "+ dTree.get(i).getDecisionName()+ ", Location-> "+ dTree.get(i).getLocation().getLocationName() + ", item-> "+ dTree.get(i).getDecisionSelected());
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
	public static ArrayList<ILDecision> generateDecisionTree(ArrayList<Location> locations, ArrayList<Item> items) {
		// This is the first pass through the tree, where I add mandatory assignments into a list
		ArrayList<ILDecision> decisionTree = new ArrayList<ILDecision>();
		
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
			ItemAssignmentTracker.increaseCounts(item);
		}
		
    	for (int i=0; i<locations.size(); i++) {    		
    		ILDecision decision_choice = new ILDecision(locations.get(i)); 		
    		ArrayList<Item> itemClone = new ArrayList<Item>(items);
    		decision_choice.setUnconstrainedChoices(itemClone);		
    		decision_choice.setDecisionSelection(items);		  		
    		decisionTree.add(decision_choice); 		
    	}
    	// This is the third pass thru the tree. Iterate through the tree, removing assigned items from all constrained choice lists
    	StringBuffer NA = new StringBuffer("null");
    	for(ILDecision choice: decisionTree) {
    		if(!choice.getDecisionSelected().contentEquals(NA)) {
    			for(ILDecision eachDecision: decisionTree) {
    				if(ItemAssignmentTracker.atLimit(choice.getItemAssigned())) {
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
    			
    			ItemAssignmentTracker.increaseCounts(item);
    			
    			// remove this assigned item from it's decision lists
    			decisionTree.get(i).getConstrainedChoices().removeAll(Collections.singleton(item));
    			if(item.getItemName().contentEquals(noItem)) {
    				decisionTree.get(i).getUnconstrainedChoices().removeAll(Collections.singleton(item));
    				decisionTree.get(i).getConstrainedChoices().removeAll(Collections.singleton(item));
    			}
    			// Now remove the assigned item from the choice lists that follow
    			for(int j=i+1;j<decisionTree.size();j++) {
    				if(ItemAssignmentTracker.atLimit(item)){
    					decisionTree.get(j).getConstrainedChoices().removeAll(Collections.singleton(item));
    					decisionTree.get(j).getUnconstrainedChoices().removeAll(Collections.singleton(item));
    				}
    			}
    		}
    	}
    	// Next, go back and add the decided items back to their constrained lists if it's not already in them
    	for(ILDecision nextPass: decisionTree) {
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
    	
    	for(ILDecision finalPass: decisionTree) {
    		//System.out.println("xxxx loc: "+finalPass.getLocation().getLocationName());
    		if(finalPass.getDecisionSelected().contentEquals(noItem)) {
    			//look in original location list to see if anything fits. If only one item can be assigned here, assign it and rebuild the list.
    			itemsDuplicate = new ArrayList<Item>(ItemAssignmentTracker.itemAssCount);
    			//System.out.println("FOunD NO ITEM ASSIGNED. dupe size:"+itemsDuplicate.size());
    			finalPass.setConstrainedChoices(itemsDuplicate,00); // here it tries to build a list, it should ignore times assigned
    			
    			if(finalPass.getConstrainedChoices().size() > 0) {
    				//System.out.println("CONSTR CHOICES > 0");
    				LinkedHashSet<Item>	set = new LinkedHashSet<Item>(finalPass.getConstrainedChoices());
    				ArrayList<Item> newList = new ArrayList<Item>();
    				newList.addAll(set);
    				
	    			if(set.size()==1) {
	    				Item singlet = finalPass.getConstrainedChoices().get(0);
	    				ItemAssignmentTracker.decreaseCounts(singlet);
//	    				System.out.println("LAST CHANCE TO ASSIGN "+singlet.getItemName()+" TO "+ finalPass.getLocation().getLocationName());
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
	private static ArrayList<ILDecision> generateMediumConfidenceDecisionTree(ArrayList<ILDecision> decisionTree) {
		ArrayList<ILDecision> decisionNumber = new ArrayList<ILDecision>(decisionTree);
		//System.out.println("GENERATING MED CONF TREE");
		Item item = decisionNumber.get(decisionNumber.size()-1).getItemAssigned();
		ArrayList<Item> Unassigned = new ArrayList<Item>(decisionNumber.get(decisionNumber.size()-1).getUnconstrainedChoices());
		if(ItemAssignmentTracker.atLimit(item)){
			Unassigned.removeAll(Collections.singleton(item));
		}
		StringBuffer noItem = new StringBuffer("No item assigned");
		
		for(int i=0;i<decisionNumber.size();i++) {
			if(decisionNumber.get(i).getItemAssigned().getItemName().contentEquals(noItem)) {
				ArrayList<String> locationCrit = new ArrayList<String>(decisionNumber.get(i).getLocation().getLocationCriteria());
//				System.out.println("LOCATION: " + decisionNumber.get(i).getLocation().getLocationName());
				decisionNumber.get(i).getMediumConfidenceFit(Unassigned, locationCrit);
				Item medItem = decisionNumber.get(i).getItemAssigned();
				for(int j=i+1; j<decisionNumber.size();j++) {
					if(ItemAssignmentTracker.atLimit(medItem)){
						decisionNumber.get(j).getUnconstrainedChoices().removeAll(Collections.singleton(medItem));
					}
				}
			}
		}		
		return decisionNumber;
	}
	/* go through decisions and assign items that are leftover to open locations without checking criteria */
	private static ArrayList<ILDecision> generateLowConfidenceDecisionTree(ArrayList<ILDecision> decisionTree) {
		ArrayList<ILDecision> decisionNumber = new ArrayList<ILDecision>(decisionTree);
		//System.out.println("GENERATING LOW CONF TREE");
		Item item = decisionNumber.get(decisionNumber.size()-1).getItemAssigned();
		ArrayList<Item> Unassigned = new ArrayList<Item>(decisionNumber.get(decisionNumber.size()-1).getUnconstrainedChoices());
		if(ItemAssignmentTracker.atLimit(item)){
			Unassigned.removeAll(Collections.singleton(item));
		}
		StringBuffer noItem = new StringBuffer("No item assigned");
		
		for(int i=0;i<decisionNumber.size();i++) {
			if(decisionNumber.get(i).getItemAssigned().getItemName().contentEquals(noItem)) {
				decisionNumber.get(i).getLowConfidenceFit(Unassigned);
				Item medItem = decisionNumber.get(i).getItemAssigned();
				if(ItemAssignmentTracker.atLimit(medItem)){
					Unassigned.remove(medItem);
				}
				for(int j=i+1; j<decisionNumber.size();j++) {
					if(ItemAssignmentTracker.atLimit(medItem)){
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
	public static ArrayList<ILDecision> changeDecisionSelection(Location location, Item newItem, ArrayList<Location> locationList, ArrayList<ILDecision> decisionTree) {
		ArrayList<ILDecision> decisionNumber = new ArrayList<ILDecision>(decisionTree);
		boolean meetsCriteria = true;
		Integer locationIndex = null;
		Item oldItem;
		
		// First, find location in decision list
		for(int i = 0; i<decisionNumber.size();i++) {
			if(decisionNumber.get(i).getLocation().getLocationName().equals(location.getLocationName())) {
				locationIndex = i;
				break;
			}
		}
		
//		for(int i=0; i<decisionNumber.size(); i++) {
//			if(decisionNumber.get(i).getLocation().equals(location)) {
//		// Second, I check to make sure the new item's preferences fits the location's criteria.
//		//		if so, continue
//        //		if not tell user they can't assign that item to that location.
//				
//				
//				
//				for(int j=0; j<newItem.getItemProperties().size(); j++) {
//					for(int k=0; k<decisionNumber.get(i).getLocation().getLocationCriteria().size(); k++) { // compare item properties to location criteria
//						// implement array compare so C1 = C1&C1 ?
////						if(newItem.getItemProperties().get(j).equals(decisionNumber.get(i).getLocation().getLocationCriteria().get(k)) || newItem.getItemProperties().get(j).equals("[NA]")) {
//						if(arrayCompare(newItem.getItemProperties().get(j),decisionNumber.get(i).getLocation().getLocationCriteria().get(k)) || newItem.getItemProperties().get(j).equals("[NA]")) {
//							meetsCriteria = true; // if there's at least one match, item meets criteria
//							locationIndex = i;
//						}
//					}
//				}
//				
//				
//				
//				if(!meetsCriteria) {
//					System.out.println(newItem.getItemName()+" does not meet location "+decisionNumber.get(i).getLocation().getLocationName() +" criteria. Returning to menu.");
//					return decisionNumber;
//				}
//			}
//		}
		
		if(meetsCriteria) {
		// Third, I need to see how "early" this decision is
		//		if I want to assign a new item to a later decision/location
		//			if it's not in the original unconstrained list, it's a brand new item.
		//			then just update the assigned item at the location 
			    if(!decisionNumber.get(0).getUnconstrainedChoices().contains(newItem)) {
			    	System.out.println("^^^^^^you are at line 342. Unconstrained choices doesn't contain new item.\n");
			    	decisionNumber.get(locationIndex).getConstrainedChoices().add(newItem);
			    	
			    	ItemAssignmentTracker.itemAssCount.add(newItem);
			    	// add new item and increase its assignment count by 1
			    	ItemAssignmentTracker.increaseCounts(newItem);
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
			    
			    else if (locationIndex <= findEarliestAssignment(decisionNumber, newItem)) {
			    	System.out.println("^^^^^^you are at line 414. You are changing the first instance of an item. \n");
		//			if it wasn't assigned earlier, then I change this decision
		//				I assign the new item to this location
			    	// decrease count of old item and then assign new Item and increase its count.
			    	ItemAssignmentTracker.decreaseCounts(decisionNumber.get(locationIndex).getItemAssigned());
					
					decisionNumber.get(locationIndex).setItemAssigned(newItem);

		//					then I rebuild this decision list starting after this decision (making sure to remove the new item from the unconstrained list first)
					if(ItemAssignmentTracker.atLimit(newItem)) {
						decisionNumber.get(locationIndex).getUnconstrainedChoices().remove(newItem);
					}
					ArrayList<Item> newItemList = new ArrayList<Item>(decisionNumber.get(locationIndex).getUnconstrainedChoices());
					
					for(int i = locationIndex+1; i<decisionNumber.size();i++) {
						// I need to decrease each corresponding items assignment count by one in itemAssCount for each appearance in the rest of decisionNumber
						ItemAssignmentTracker.decreaseCounts(decisionNumber.get(i).getItemAssigned());
					}
					ItemAssignmentTracker.increaseCounts(newItem);
					
					locationIndex = locationIndex + 1;
					ArrayList<Location> newLocationList = new ArrayList<Location>(locationList.subList(locationIndex, locationList.size()));
					decisionNumber.subList(locationIndex, decisionNumber.size()).clear();
					
					System.out.println("CLEAR PARTIAL LIST FROM "+locationIndex+" TO "+decisionNumber.size());
					printPlainDecisionTree(decisionNumber);
					
					decisionNumber.addAll(generateDecisionTree(newLocationList,newItemList));
					return decisionNumber;
				}
				else {
		//			if it was assigned earlier, then I change that earlier decision and make this later decision/location item assignment a hard constraint
					System.out.println("^^^^^^^you are at line 450. Making hard constraint at location.\n");
					ArrayList<Item> manAss = new ArrayList<Item>();
					manAss.add(newItem);
					decisionNumber.get(locationIndex).getLocation().getMandatoryAssignments().addAll(0,manAss);
					
					ItemAssignmentTracker.decreaseCounts(decisionNumber.get(locationIndex).getItemAssigned()); // decrease count of currently assigned item
					Integer earliestLocationIndex = findEarliestAssignment(decisionNumber, newItem);
					//Integer earliestLocationIndex = findLastAssignment(decisionNumber, newItem);
					
		//				I assign the new item to this location and add the old assignment back to the unconstrained choices list
					oldItem = decisionNumber.get(earliestLocationIndex).getItemAssigned();
					
					//System.out.println("I am changing "+location.getLocationName()+" to "+newItem.getItemName());
					
					ItemAssignmentTracker.resetCount(oldItem);
					decisionNumber.get(earliestLocationIndex).setDecisionSelection(decisionNumber.get(earliestLocationIndex).getConstrainedChoices()/*, oldItem*/);
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
//					earliestLocationIndex++;
			
					
					for(int i = earliestLocationIndex; i<decisionNumber.size();i++) {
						// I need to decrease each corresponding items assignment count by one in itemAssCount for each appearance in the rest of decisionNumber
						ItemAssignmentTracker.decreaseCounts(decisionNumber.get(i).getItemAssigned());
//						System.out.println("DECREASING COUNT FOR "+decisionNumber.get(i).getItemAssigned().getItemName()+" to " + AssignmentTracker.showCount(decisionNumber.get(i).getItemAssigned()));
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
    private static Integer findEarliestAssignment(ArrayList<ILDecision> decisionList, Item newItem) {
    	Integer Index = null;
    	for(int i=0; i<decisionList.size(); i++) {
    		if(decisionList.get(i).getItemAssigned().equals(newItem)) {
    			Index = i;
    			break;
    		}
    	}
    	
		return Index;
	}
//    private static Integer findLastAssignment(ArrayList<decision> decisionList, Item newItem) {
//    	Integer locationIndex = null;
//    	for(int i=0; i<decisionList.size(); i++) {
//    		if(decisionList.get(i).getItemAssigned().equals(newItem)) {
//    			locationIndex = i;
//    		}
//    	}
//    	
//		return locationIndex;
//	}
//    
//	private static boolean arrayCompare(String propString, String critString) {
//		String[] prop = propString.split("&");
//		String[] crit = critString.split("&");
//		prop = Arrays.stream(prop).distinct().toArray(String[]::new);
//		crit = Arrays.stream(crit).distinct().toArray(String[]::new);
//		
//		int matches = 0;
//		if(prop.length==crit.length) {
//			for(int i=0; i<prop.length; i++) {
//				for(int j=0; j<crit.length;j++) {
//					if(prop[i].equalsIgnoreCase(crit[j])) {
//						matches = matches + 1;
//					}
//				}
//			}
//		}
//		if(matches == prop.length)
//			return true;
//		else
//			return false;
//	}

} 