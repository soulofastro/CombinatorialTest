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

import control.ChangeILDecision;

public class ItemLocationTreeMaker 
{ 
	
	//public static ArrayList<decision> decisionNumber = new ArrayList<decision>();	
	public static ArrayList<Item> itemsDuplicate = new ArrayList<Item>();
	
	
	public static ArrayList<ILDecision> createILtree(ArrayList<Item> itemList, ArrayList<Location> locationList) throws FileNotFoundException, IOException, Exception { 
		
    	ArrayList<Item> items = new ArrayList<Item>(itemList);
    	ArrayList<Location> locations = new ArrayList<Location>(locationList);
    	ArrayList<ILDecision> decisionNumber = new ArrayList<ILDecision>();
    	
    	ItemAssignmentTracker.newTracker(items);
    	ItemAssignmentTracker.add(new Item("null"));
    	ItemAssignmentTracker.add(new Item("No item assigned"));
    	
    	/* constraints check */
//    	ConstraintsCheck.generateCheckWithoutAND(items,locations);
//    	ConstraintsCheck.generateCheckWithAND(items,locations);
    	
    	/* Look at each location and assign an item to it */
    	/* This generates a decision node that we can later revisit */
    	/* The actual decision choice is made inside the decision class when the node is generated */
    	ArrayList<Item> itemClone = new ArrayList<Item>(items);	
    	decisionNumber = generateDecisionTree(locations, itemClone);
    	
    	return decisionNumber;
    }
	
	public static void printItemAssStatus(ArrayList<Item> items) {
		System.out.println();
    	for (Item item : items) {
    		System.out.println("Item Name: "+ item.getItemProperties().get(0)+", Times assigned: "+item.getNumberOfTimesAssigned()+" of "+item.getAssignmentLimit());
    	}
    	System.out.println("*************************************************************************************");
	}
	

	public static void printLocationList(ArrayList<Location> locations) {
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

	public static void printItemList(ArrayList<Item> items) {
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
	public static void printDecisionTree(ArrayList<ILDecision> decisionNumber) {
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
	public static void printPlainDecisionTree(ArrayList<ILDecision> decisionNumber) {
		ArrayList<ILDecision> dTree = new ArrayList<ILDecision>(decisionNumber);
		
//		Collections.sort(dTree,(decision1, decision2) -> Integer.parseInt(decision1.getLocation().getLocationName()) - Integer.parseInt(decision2.getLocation().getLocationName()));		
    	try {
			for(int i=0; i<dTree.size(); i++) {	
				Integer y = i+1;
				System.out.println("ILDecision " + y.toString() +", Name: "+ dTree.get(i).getDecisionName()+ ", Location-> "+ dTree.get(i).getLocation().getLocationName() + ", item-> "+ dTree.get(i).getDecisionSelected());
			}
    	}
    	catch(Exception e) {
    		// Location runs into null assignment
    		System.out.println("Assignment error. Too many or incompatible constraints likely.\nMake sure items with NA preference are last in txt file.");
    	}
		
	}

	/*This is where I generate the decision tree */
	@SuppressWarnings("unused")
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
    	// probably not necessary, dead code for now 
    	if(false) {
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
	    				decisionTree = ChangeILDecision.changeDecisionSelection(finalPass.getLocation(), singlet, locations, decisionTree);
	    			}    			
	    			else {
	    				for(Item iter: newList) { System.out.print(iter.getItemName()+", ");}
	    				System.out.println("could not be assigned to Location "+finalPass.getLocation().getLocationName()+" because they were assigned at another location.\n");
	    			}
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

} 