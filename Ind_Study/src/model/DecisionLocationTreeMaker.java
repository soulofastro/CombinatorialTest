package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

public class DecisionLocationTreeMaker {

	public static ArrayList<ILDecision> descisionsDuplicate = new ArrayList<ILDecision>();
	
	public static ArrayList<DLDecision> createDLtree(ArrayList<ILDecision> ILtree, ArrayList<Location> timeSlots) {
		
		ArrayList<DLDecision> DLtree = new ArrayList<DLDecision>();
		ArrayList<ILDecision> ILtreeClone = new ArrayList<ILDecision>(ILtree);
		
		DecisionAssignmentTracker.newTracker(ILtree);
		DecisionAssignmentTracker.add(new ILDecision("null"));
		DecisionAssignmentTracker.add(new ILDecision("No decision assigned"));
		
		printILDecisionList(ILtree);
		
		printLocationList(timeSlots);
		
		DLtree = generateDecisionTree(timeSlots, ILtreeClone);
		
//		printDecisionTree(DLtree);
		printPlainDecisionTree(DLtree);
		
		/*
		 *  change decision testing goes here
		 * 
		 * 
		 */
		
		System.out.println();
		for (ILDecision ILD : ILtree) {
    		System.out.println("ILD Name: "+ ILD.getDecisionName()+", Times assigned: "+ILD.getNumberOfTimesAssigned()+" of "+ILD.getAssignmentLimit());
    	}
    	System.out.println();
		
		return DLtree;
	}
	
	private static void printDecisionTree(ArrayList<DLDecision> decisionNumber) {
    	try {
			for(int i=0; i<decisionNumber.size(); i++) {	
				Integer y = i+1;
				System.out.print("Decision " + y.toString() +", Location-> "+ decisionNumber.get(i).getLocation().getLocationName() + ", ILDs-> ");
				for (ILDecision selected: decisionNumber.get(i).getDecisionsAssigned()) {
					System.out.print(selected.getDecisionName()+", ");
				}
				System.out.println();
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
    		e.printStackTrace();
    	}
		
	}
	
	private static void printPlainDecisionTree(ArrayList<DLDecision> decisionNumber) {
		ArrayList<DLDecision> dTree = new ArrayList<DLDecision>(decisionNumber);
		
    	try {
			for(int i=0; i<dTree.size(); i++) {	
				Integer y = i+1;
				System.out.print("Decision " + y.toString() +", Location-> "+ dTree.get(i).getLocation().getLocationName() + ", ILDs-> ");
				for (ILDecision selected: dTree.get(i).getDecisionsAssigned()) {
					System.out.print(selected.getDecisionName()+", ");
				}
			System.out.println();
			}
    	}
    	catch(Exception e) {
    		// Location runs into null assignment
    		System.out.println("Assignment error. Too many or incompatible constraints likely.");
    	}
		
	}
	
	private static void printILDecisionList(ArrayList<ILDecision> decisions) {
    	for (ILDecision decision: decisions) {
    		System.out.print("Decision name: "+ decision.getDecisionName()+", Constraints:->");
    		for (int i=0; i<decision.getItemLocationMatches().size();i++) {
    			System.out.print(" "+ decision.getItemLocationMatches().get(i));
    		}
    		System.out.print(", Partial Constraints:->");
    		for (int i=0; i<decision.getItemLocationPartialMatches().size();i++) {
    			System.out.print(" "+ decision.getItemLocationPartialMatches().get(i));
    		}
    		System.out.print(" <-|| Number of Constraints: "+ decision.getNumberOfConstraints()/*decision.getItemLocationMatches().size()+decision.getItemLocationPartialMatches().size()*/);
    		System.out.println();
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

	private static ArrayList<DLDecision> generateDecisionTree(ArrayList<Location> timeSlots, ArrayList<ILDecision> ILtreeClone) {
		// this is a mirror of method in ILTreeMaker
		// items --> ILtreeClone
		// locations --> timeSlots
		
		// This is the first pass through the tree, where I add mandatory assignments into a list
		ArrayList<DLDecision> decisionTree = new ArrayList<DLDecision>();
		
		ArrayList<ILDecision> mandatoryAss = new ArrayList<ILDecision>();
		for (int i=0;i<timeSlots.size();i++) {
			if(timeSlots.get(i).getMandatoryDecisionAssignments().size() > 0) {
				mandatoryAss.addAll(timeSlots.get(i).getMandatoryDecisionAssignments());
			}
		}
		
		
		// This is the second pass through the tree, assigning normally
		for(ILDecision decision: mandatoryAss) {
			DecisionAssignmentTracker.increaseCounts(decision);
		}
		
    	for (int i=0; i<timeSlots.size(); i++) {    		
    		DLDecision decision_choice = new DLDecision(timeSlots.get(i)); 		
    		ArrayList<ILDecision> decisionClone = new ArrayList<ILDecision>(ILtreeClone);
    		decision_choice.setUnconstrainedChoices(decisionClone);		
    		decision_choice.setDecisionSelection(ILtreeClone);	
    		decisionTree.add(decision_choice); 		
    	}

    	// This is the third pass thru the tree. Iterate through the tree, removing assigned items from all constrained choice lists
    	ILDecision decNull = new ILDecision("null");
    	ILDecision decNA = new ILDecision("No decision assigned");
    	for(DLDecision choice: decisionTree) {
    		for(int i = 0; i<choice.getDecisionsAssigned().size();i++) {
	    		if(!choice.getDecisionsAssigned().get(i).equals(decNA)||!choice.getDecisionsAssigned().get(i).equals(decNull)) { // look at decisions selected for each DLD
	    			for(DLDecision eachDecision: decisionTree) { // remove choice in eachDecision
	    				for(int j=0;j < eachDecision.getDecisionsAssigned().size(); j++) {
		    				if(DecisionAssignmentTracker.atLimit(choice.getDecisionsAssigned().get(j))) {
		    					eachDecision.getConstrainedChoices().removeAll(Collections.singleton(choice.getDecisionsAssigned().get(j)));
		    				}
	    				}
	    			}
	    		}
    		}
    	}
    	
    	/* Now revisit each DLdecision node with a "null" assigned ILdecision and select ILDs from new constrained lists (up to assignment limit).*/
    	/* these constrained lists should be composed only of available items */
    	
//    	for(DLDecision DLD: decisionTree) {
    	for(int k=0; k<decisionTree.size();k++) {
       		if(decisionTree.get(k).getDecisionsAssigned().contains(decNA)||decisionTree.get(k).getDecisionsAssigned().contains(decNull) || decisionTree.get(k).getDecisionsAssigned().size() == 0) {
//       			ILDecision noDec = new ILDecision("No decision assigned");
       			if(decisionTree.get(k).getConstrainedChoices().size() > 0) {
       				ArrayList<ILDecision> assigned = new ArrayList<ILDecision>();
       				ArrayList<Item> seenItems = new ArrayList<Item>();
       				
   					for(int i=0; i<decisionTree.get(k).getConstrainedChoices().size() && i<decisionTree.get(k).getAssignmentLimit();i++) { // assign decisions up to DLDs assignment limit
   						if(!seenItems.contains(decisionTree.get(k).getConstrainedChoices().get(i).getItemAssigned())) {
   							assigned.add(decisionTree.get(k).getConstrainedChoices().get(i));
   							seenItems.add(decisionTree.get(k).getConstrainedChoices().get(i).getItemAssigned()); // make sure an Item isn't assigned twice to same timeslot
   						}
   					} 
   					decisionTree.get(k).setDecisionsAssigned(assigned);
       			}
       			
    			DecisionAssignmentTracker.increaseCounts(decisionTree.get(k).getDecisionsAssigned());
    			
    			// remove these assigned ILdecisions from it's decision lists
//    			decisionTree.get(k).getConstrainedChoices().removeAll(decisionTree.get(k).getDecisionsAssigned());
    			
//    			if(item.getItemName().contentEquals(noItem)) {
//    				DLD.getUnconstrainedChoices().removeAll(Collections.singleton(noDec));
//    				DLD.getConstrainedChoices().removeAll(Collections.singleton(noDec));
//    			}
    			// Now remove the assigned ILdecisions from the choice lists that follow
    			for(int j=k+1;j<decisionTree.size();j++) {
    				for(int z=0; z<decisionTree.get(k).getDecisionsAssigned().size();z++) {
	    				if(DecisionAssignmentTracker.atLimit(decisionTree.get(k).getDecisionsAssigned().get(z))){
	    					decisionTree.get(j).getConstrainedChoices().removeAll(Collections.singleton(decisionTree.get(k).getDecisionsAssigned().get(z)));
	    					decisionTree.get(j).getUnconstrainedChoices().removeAll(Collections.singleton(decisionTree.get(k).getDecisionsAssigned().get(z)));
	    				}
    				}
    			}
    		}
    	}
    	
       	for(DLDecision finalPass: decisionTree) {
    		//System.out.println("xxxx loc: "+finalPass.getLocation().getLocationName());
    		if(finalPass.getDecisionsAssigned().size() <= 0) {
    			//look in original location list to see if anything fits. If only one item can be assigned here, assign it and rebuild the list.
    			descisionsDuplicate = new ArrayList<ILDecision>(DecisionAssignmentTracker.decisionAssCount);
    			//System.out.println("FOunD NO ITEM ASSIGNED. dupe size:"+itemsDuplicate.size());
    			finalPass.setConstrainedChoices(descisionsDuplicate,00); // here it tries to build a list, it should ignore times assigned
    			
    			if(finalPass.getConstrainedChoices().size() > 0) {
    				//System.out.println("CONSTR CHOICES > 0");
    				LinkedHashSet<ILDecision>	set = new LinkedHashSet<ILDecision>(finalPass.getConstrainedChoices());
    				ArrayList<ILDecision> newList = new ArrayList<ILDecision>();
    				newList.addAll(set);
    				
	    			if(set.size()==1) {
	    				ILDecision singlet = finalPass.getConstrainedChoices().get(0);
	    				DecisionAssignmentTracker.decreaseCounts(singlet);
//	    				System.out.println("LAST CHANCE TO ASSIGN "+singlet.getItemName()+" TO "+ finalPass.getLocation().getLocationName());
	    				finalPass.getConstrainedChoices().clear();
	    				//System.out.println("LINE 227 TRYING TO FIX NO ITEM ASSIGNED");
	    				// TODO implement this later
	    				// decisionTree = changeDecisionSelection(finalPass.getLocation(), singlet, locations, decisionTree);
	    			}    			
//	    			else {
//	    				for(Item iter: newList) { System.out.print(iter.getItemName()+", ");}
//	    				System.out.println("could not be assigned to Location "+finalPass.getLocation().getLocationName()+" because they were assigned at another location.\n");
//	    			}
    			}
    		}
    	}
       	
       	
    	decisionTree = generateMediumConfidenceDecisionTree(decisionTree);
    	decisionTree = generateLowConfidenceDecisionTree(decisionTree);
		return decisionTree;
	}

	private static ArrayList<DLDecision> generateMediumConfidenceDecisionTree(ArrayList<DLDecision> decisionTree){
		ArrayList<DLDecision> decisions = new ArrayList<DLDecision>(decisionTree);
		ArrayList<ILDecision> lastNodeAssignments = new ArrayList<ILDecision>(decisions.get(decisions.size()-1).getDecisionsAssigned());
		ArrayList<ILDecision> lastNodeUnconstrainedList = new ArrayList<ILDecision>(decisions.get(decisions.size()-1).getUnconstrainedChoices());
		
		for(ILDecision lastNode: lastNodeAssignments) {
			if(DecisionAssignmentTracker.atLimit(lastNode)) {
				lastNodeUnconstrainedList.removeAll(Collections.singleton(lastNode));
			}
		}
		
		for(int i=0; i<decisions.size(); i++) {
			if(decisions.get(i).getDecisionsAssigned().contains(new ILDecision("No decision assigned"))) {
				ArrayList<String> locationCrit = new ArrayList<String>(decisions.get(i).getLocation().getLocationCriteria());
				
				decisions.get(i).getMediumConfidenceFit(lastNodeUnconstrainedList, locationCrit);
				
				for(int z = 0; i< decisions.get(i).getDecisionsAssigned().size();z++) {
					System.out.println("MED DECISION "+ decisions.get(i).getDecisionName() +" ASSIGNING(258): " + decisions.get(i).getDecisionsAssigned().get(z));
				}
				
				ArrayList<ILDecision> decisionAssignd = new ArrayList<ILDecision>(decisions.get(i).getDecisionsAssigned());
				for(int j=i+1; j<decisions.size();j++) {
					for(ILDecision dec: decisionAssignd) {
						if(DecisionAssignmentTracker.atLimit(dec)){
							decisions.get(j).getUnconstrainedChoices().removeAll(Collections.singleton(dec));
						}
					}
				}
			}
		}
		
		return decisions;
	}
	
	
	private static ArrayList<DLDecision> generateLowConfidenceDecisionTree(ArrayList<DLDecision> decisionTree){
		ArrayList<DLDecision> decisions = new ArrayList<DLDecision>(decisionTree);
		ArrayList<ILDecision> lastNodeAssignments = new ArrayList<ILDecision>(decisions.get(decisions.size()-1).getDecisionsAssigned());
		ArrayList<ILDecision> lastNodeUnconstrainedList = new ArrayList<ILDecision>(decisions.get(decisions.size()-1).getUnconstrainedChoices());
		
		for(ILDecision lastNode: lastNodeAssignments) {
			if(DecisionAssignmentTracker.atLimit(lastNode)) {
				lastNodeUnconstrainedList.removeAll(Collections.singleton(lastNode));
			}
		}
		
		for(int i=0; i<decisions.size(); i++) {
			if(decisions.get(i).getDecisionsAssigned().contains(new ILDecision("No decision assigned"))) {
				decisions.get(i).getLowConfidenceFit(lastNodeUnconstrainedList);
				ArrayList<ILDecision> decisionAssignd = new ArrayList<ILDecision>(decisions.get(i).getDecisionsAssigned());
				for(int j=i+1; j<decisions.size();j++) {
					for(ILDecision dec: decisionAssignd) {
						if(DecisionAssignmentTracker.atLimit(dec)){
							decisions.get(j).getUnconstrainedChoices().removeAll(Collections.singleton(dec));
						}
					}
				}
			}
		}
		
		return decisions;
	}
	
	public static ArrayList<ILDecision> changeDecisionSelection(Location location, Item newItem, ArrayList<Location> locationList, ArrayList<ILDecision> decisionTree) {
		//TODO
		return null;
	}
	
    private static Integer findEarliestAssignment(ArrayList<DLDecision> decisionList, DLDecision newDecision) {
    	Integer Index = null;
    	for(int i=0; i<decisionList.size(); i++) {
    		for(int j=0; j<decisionList.get(i).getDecisionsAssigned().size();j++) {
	    		if(decisionList.get(i).getDecisionsAssigned().get(j).equals(newDecision)) {
	    			Index = i;
	    			break;
	    		}
    		}
    	}
    	
		return Index;
    }
	
}
