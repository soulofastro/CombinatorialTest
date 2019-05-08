package control;

import java.util.ArrayList;

import model.ILDecision;
import model.Item;
import model.ItemAssignmentTracker;
import model.ItemLocationTreeMaker;
import model.Location;

public class ChangeILDecision {

	/** not sure how getItemAssigned().getItemProperties().addAll(1, getLocation().getLocationCriteria()); in "decision" class will alter when I have to change a decision **/
	// keep an eye out for odd behavior!
	/* THIS IS WHERE I CHANGE A DECISION AT A LOCATION */
	/* change item assigned to this location to newItem */
	/* arguments are ("where you want to put the new item", "the new item you want to insert", "complete location list")*/
	public static ArrayList<ILDecision> changeDecisionSelection(Location location, Item newItem, ArrayList<Location> locationList, ArrayList<ILDecision> decisionTree) {
		ArrayList<ILDecision> decisionNumber = new ArrayList<ILDecision>(decisionTree);
		boolean meetsCriteria = true;
		Integer locationIndex = null;
//		Item oldItem;
		Integer earliestLocIndex = findEarliestAssignment(decisionNumber, newItem);
		
		
		// First, find location in decision list
		for(int i = 0; i<decisionNumber.size();i++) {
			if(decisionNumber.get(i).getLocation().getLocationName().equals(location.getLocationName())) {
				locationIndex = i;
				break;
			}
		}
//		BYPASS CRITERIA CHECK COMPLETELY (for now?) this should avoid frustration later on
		
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
			    	System.out.println("^^^ILD^^^you are at line 67. Unconstrained choices doesn't contain new item.\n");
			    	decisionNumber.get(locationIndex).getConstrainedChoices().add(newItem);
			    	
			    	ItemAssignmentTracker.itemAssCount.add(newItem);
			    	// add new item and increase its assignment count by 1
			    	ItemAssignmentTracker.increaseCounts(newItem);
					
		    	    decisionNumber.get(locationIndex).setItemAssigned(newItem);
			    	// maybe give the user the option to regenerate the list from here? 
			    	// changing this choice does not immediately effect later decisions
		    	    return decisionNumber;
			    }
		//		I must check and see if that item was assigned earlier (if new item is in location's constrained list and count < max, its ok to assign)
		// 		
			    
			    else if (locationIndex < earliestLocIndex) {
			    	System.out.println("^^^ILD^^^you are at line 83. You are changing the first instance of an item. \n");
		//			if it wasn't assigned earlier, then I change this decision
		//				I assign the new item to this location
			    	// decrease count of old item and then assign new Item and increase its count.
			    	ItemAssignmentTracker.decreaseCounts(decisionNumber.get(locationIndex).getItemAssigned());
					
					decisionNumber.get(locationIndex).setItemAssigned(newItem);

		//					then I rebuild this decision list starting after this decision (making sure to remove the new item from the unconstrained list first)
					if(ItemAssignmentTracker.atLimit(newItem)) {
						for(int i = locationIndex; i< decisionNumber.size(); i++) {
							decisionNumber.get(i).getUnconstrainedChoices().remove(newItem);
						}
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
					
					decisionNumber.addAll(ItemLocationTreeMaker.generateDecisionTree(newLocationList,newItemList));
					return decisionNumber;
				}
				else if(locationIndex < earliestLocIndex) {
		//			if it was assigned earlier, then I change that earlier decision and make this later decision/location item assignment a hard constraint
					System.out.println("^^^ILD^^^you are at line 114. Making hard constraint at location.\n");
					ArrayList<Item> manAss = new ArrayList<Item>();
					manAss.add(newItem);
					decisionNumber.get(locationIndex).getLocation().getMandatoryAssignments().addAll(0,manAss);
					
					for(int i = earliestLocIndex; i< decisionNumber.size();i++) {
						ItemAssignmentTracker.decreaseCounts(decisionNumber.get(i).getItemAssigned()); // decrease count of currently assigned item
					}
					
		//				I add the old assignment back to the unconstrained choices list
//					oldItem = decisionNumber.get(earliestLocIndex).getItemAssigned();
					//System.out.println("I am changing "+location.getLocationName()+" to "+newItem.getItemName());
//					ItemAssignmentTracker.resetCount(oldItem);
//					decisionNumber.get(earliestLocIndex).setDecisionSelection(decisionNumber.get(earliestLocIndex).getConstrainedChoices());
					//decisionNumber.get(earliestLocationIndex).setItemAssigned(newItem);
					//System.out.println("It was assigned earlier, so I have to change "+ decisionNumber.get(earliestLocationIndex).getLocation().getLocationName()+" to "+ decisionNumber.get(earliestLocationIndex).getItemAssigned().getItemName());
//					if(!decisionNumber.get(earliestLocIndex).getUnconstrainedChoices().contains(oldItem)) {
//						decisionNumber.get(earliestLocIndex).getUnconstrainedChoices().add(oldItem);
						//AssignmentTracker.decreaseCounts(oldItem);
//					}
					/*else if(AssignmentTracker.checkCount(oldItem)) {
						AssignmentTracker.increaseCounts(oldItem);
					}*/
					/*if(AssignmentTracker.atLimit(newItem)) {
						decisionNumber.get(earliestLocationIndex+1).getUnconstrainedChoices().remove(newItem);
					}*/
		//					then I rebuild my list starting from that earlier location
//					earliestLocationIndex++;
//					for(int i = earliestLocIndex; i<decisionNumber.size();i++) {
//						// I need to decrease each corresponding items assignment count by one in itemAssCount for each appearance in the rest of decisionNumber
//						ItemAssignmentTracker.decreaseCounts(decisionNumber.get(i).getItemAssigned());
////						System.out.println("DECREASING COUNT FOR "+decisionNumber.get(i).getItemAssigned().getItemName()+" to " + AssignmentTracker.showCount(decisionNumber.get(i).getItemAssigned()));
//					}
					
			    	ArrayList<Item> newItemList = new ArrayList<Item>(decisionNumber.get(earliestLocIndex).getUnconstrainedChoices());
					ArrayList<Location> newLocationList = new ArrayList<Location>(locationList.subList(earliestLocIndex, locationList.size()));
					decisionNumber.subList(earliestLocIndex, decisionNumber.size()).clear();			
					decisionNumber.addAll(ItemLocationTreeMaker.generateDecisionTree(newLocationList,newItemList));
					return decisionNumber;
				}
				else {
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
