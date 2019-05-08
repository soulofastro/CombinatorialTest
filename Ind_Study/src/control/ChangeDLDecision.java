package control;

import java.util.ArrayList;

import model.DLDecision;
import model.DecisionAssignmentTracker;
import model.DecisionLocationTreeMaker;
import model.ILDecision;
import model.Location;

public class ChangeDLDecision {

	
	// decisionTree = current DLDecision ArrayList, locationList = list of time slots,
	// new ILDecision = what you want to change? new decision to insert at:, location = place where change is happening, item = ?
	public static ArrayList<DLDecision> changeDecisionSelection(Location timeSlot, ILDecision newILDecision, ArrayList<Location> locationList, ArrayList<DLDecision> DLDtree) {
		// I'm going to make this assuming that each ILDecision will only ever appear once.
		// item -> ILDecision
		// location -> timeSlot
		ArrayList<DLDecision> decList = new ArrayList<DLDecision>(DLDtree);
		boolean meetsCriteria = true;
		Integer locationIndex = null;
//		ILDecision oldILDecision;
		
		Integer[] index = findEarliestAssignment(decList, newILDecision);
		Integer DLDLocationIndex = index[0];
//		Integer DLDAssignmentIndex = index[1];
		
		// First, find location in DLDecision list
		for(int i = 0; i<decList.size();i++) {
			if(decList.get(i).getLocation().getLocationName().equals(timeSlot.getLocationName())) {
				locationIndex = i;
				break;
			}
		}
		
//		BYPASS CRITERIA CHECK COMPLETELY (for now?) this should avoid frustration later on
		if(meetsCriteria) {
			// now I need to see how early this decision is.
			//		if the item is not in the first decisions unconstrained choices list, it's a brand new item
			// 			just add it and update the assignment limit for that timeSlot (increase by one)
			//			(perhaps later we will want to prevent this? for now, if the user wants to undo the limit increase, they have to go back
			//			into the location's criteria/properties)
				if(!decList.get(0).getUnconstrainedChoices().contains(newILDecision)) {
					System.out.println("^^^DLD^^^you are at line 40. Unconstrained choices doesn't contain new item.\n");
					decList.get(locationIndex).getConstrainedChoices().add(newILDecision);
					
					DecisionAssignmentTracker.decisionAssCount.add(newILDecision);
					DecisionAssignmentTracker.increaseCounts(newILDecision);
					
					decList.get(locationIndex).setAssignmentLimit(decList.get(locationIndex).getAssignmentLimit()+1);
					decList.get(locationIndex).getDecisionsAssigned().add(newILDecision);
					
					return decList;
				}
				
			//	else if this decisions current index is less than this decisions earliest index, we are changing when this ILD first appears
				else if(locationIndex < DLDLocationIndex) {
					System.out.println("^^^DLD^^^you are at line 59. You are changing the first instance of an item. \n");
					
					//assign new ILD to this location after increasing timeslot limit
					DecisionAssignmentTracker.resetCount(newILDecision);
					decList.get(locationIndex).setAssignmentLimit(decList.get(locationIndex).getAssignmentLimit()+1);
					decList.get(locationIndex).getDecisionsAssigned().add(newILDecision);
					
					// then I rebuild this list starting after this decision (making sure to decrease the count of every ILD after by one for each appearance)
					ArrayList<ILDecision> newDecList = new ArrayList<ILDecision>(decList.get(locationIndex).getUnconstrainedChoices());
					
					for(int i = locationIndex+1; i<decList.size(); i++) {
						DecisionAssignmentTracker.decreaseCounts(decList.get(i).getDecisionsAssigned());
					}
					
					DecisionAssignmentTracker.increaseCounts(newILDecision);
					
					locationIndex = locationIndex + 1;
					ArrayList<Location> newLocationList = new ArrayList<Location>(locationList.subList(locationIndex, locationList.size()));
					decList.subList(locationIndex, decList.size()).clear();
					
					decList.addAll(DecisionLocationTreeMaker.generateDecisionTree(newLocationList,newDecList));
					return decList;
				}
				else if(locationIndex > DLDLocationIndex){ // we are changing this DLD so that this ILD appears later. 
					// We have to change that earlier DLD with the ILD and make the later ILD a mandatory assignment
					// again, assuming that each ILD appears ONLY ONCE this should be pretty simple
					System.out.println("^^^DLD^^^you are at line 87. Making hard constraint at location.\n");
					ArrayList<ILDecision> manAss = new ArrayList<ILDecision>();
					manAss.add(newILDecision);
					decList.get(locationIndex).getLocation().getMandatoryDecisionAssignments().addAll(0,manAss);
					
					//decrease counts from DLDLocIndex (inclusive) to the end of decList
					for(int i = DLDLocationIndex; i<decList.size(); i++) {
						DecisionAssignmentTracker.decreaseCounts(decList.get(i).getDecisionsAssigned());
					}
					
					decList.get(DLDLocationIndex).getDecisionsAssigned().clear();
					
					// then rebuild the list from that earliest index, assuming that your decision will be assigned to that later location using mandatory assignment rules
					ArrayList<ILDecision> newDecList = new ArrayList<ILDecision>(decList.get(DLDLocationIndex).getUnconstrainedChoices());
					ArrayList<Location> newLocationList = new ArrayList<Location>(locationList.subList(DLDLocationIndex, locationList.size()));
					decList.subList(locationIndex, decList.size()).clear();
					
					decList.addAll(DecisionLocationTreeMaker.generateDecisionTree(newLocationList,newDecList));
					return decList;
					
				}
				else { //locationIndex == DLDLocationIndex, so do nothing
					return decList;
				}
		}
		
		// if it didn't meet criteria, it would return this
		return decList;
	}
	
    private static Integer[] findEarliestAssignment(ArrayList<DLDecision> decisionList, ILDecision newILDecision) {
    	Integer[] Index = new Integer[2];
    	for(int i=0; i<decisionList.size(); i++) {
    		for(int j=0; j<decisionList.get(i).getDecisionsAssigned().size();j++) {
	    		if(decisionList.get(i).getDecisionsAssigned().get(j).equals(newILDecision)) {
	    			Index[0] = i;
	    			Index[1] = j;
	    			break;
	    		}
    		}
    	}
    	
		return Index;
    }
}
