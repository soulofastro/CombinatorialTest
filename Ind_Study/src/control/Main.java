package control;

/*
 *  NOTE: Only use spaces when absolutely necessary. The line parser treats everything between ',' as separate objects.
 *  	  Treat all objects as case sensitive when naming and comparing.
 * 
 *  Item text file format: 
 *  0:Item Name,assignment limit,[CONDITIONS]
 *  1:Item Name,assignment limit,[CONDITIONS]
 *  2:Item Name,assignment limit,[CONDITIONS]
 *  etc.
 *  
 *  Location text file format:
 *  0:Location Name,[CONDITIONS]
 *  1:Location Name,[CONDITIONS]
 *  2:Location Name,[CONDITIONS]
 *  etc.
 */

import java.io.FileNotFoundException;
import java.io.IOException;

import model.CurrentState;
import model.DLDecision;
import model.DecisionLocationTreeMaker;
import model.ItemLocationTreeMaker;

public class Main {
	
	public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
		CurrentState m = new CurrentState("Isimple.txt","Lsimple.txt","Tsimple.txt"); // for simple testing
		/* other constructor for current state is CurrentState(itemFileName, locationFileName, timeSlotsFileName) */
		
		printCurrentState(m);
		
		runILDTestCases(m);
		runDLDTestCases(m);
	}

	private static void printCurrentState(CurrentState m) {
//		ItemLocationTreeMaker.printItemList(m.getItems());
//		ItemLocationTreeMaker.printLocationList(m.getLocations());
//		ItemLocationTreeMaker.printDecisionTree(m.getILtree());
		ItemLocationTreeMaker.printPlainDecisionTree(m.getILtree());
//		ItemLocationTreeMaker.printItemAssStatus(m.getItems());
		System.out.println();
//		DecisionLocationTreeMaker.printILDecisionList(m.getILtree());
//		DecisionLocationTreeMaker.printLocationList(m.getTimeSlots());
//		DecisionLocationTreeMaker.printDecisionTree(m.getDLtree());
		DecisionLocationTreeMaker.printPlainDecisionTree(m.getDLtree());
//		DecisionLocationTreeMaker.printILDAssStatus(m.getILtree());
		
//    	for (DLDecision DLD : m.getDLtree()) {
//    		System.out.println("DLD Name: "+ DLD.getDecisionName() +", Decisions assigned: "+DLD.getNumberOfAssignedDecisions()+" of "+DLD.getAssignmentLimit());
//    	}
//    	System.out.println();
	}

	private static void runDLDTestCases(CurrentState m) {
		/** DLTree Testing **/
		/* Change DLDecision and display updated list */
		
	}

	private static void runILDTestCases(CurrentState m) {
		/** ILTREE TESTING **/
    	/* Change an ILDecision and display updated list */
    	/* arguments are changeDecisionSelection("where you want to put the new item", "the new item you want to insert", "complete location list")*/
//      decisionTree = ChangeILDecision.changeDecisionSelection(finalPass.getLocation(), singlet, locations, decisionTree);
		/* TEST CASE 1 - add brand new item. If item isn't in first unconstrained choice list then it's new */
//    	Item alpha_2 = new Item("Alpha_2");
//    	String[] AlphaProp = {"Alpha_2", "NA"};
//    	alpha_2.setItemProperties(AlphaProp);		
//		m.setILtree(ChangeILDecision.changeDecisionSelection(m.getLocations().get(1), alpha_2, m.getLocations(), m.getILtree()));
    	
    	/* TEST CASE 2 - change first appearance of item. currentItemIndex <= earliestItemIndex   */
//    	System.out.println("TEST 2: Change first decision. Update rest of list");
//    	Item alpha = m.getILtree().get(6).getItemAssigned();
//    	System.out.println("Changing item at "+locations.get(1).getLocationName()+" to "+alpha.getItemName());
//		m.setILtree(ChangeILDecision.changeDecisionSelection(m.getLocations().get(1), alpha, m.getLocations(), m.getILtree()));
		
//    	/* TEST CASE 3a - change later appearance of item. earliestItemIndex < currentItemIndex */
//    	Item charlie = m.getILtree().get(0).getItemAssigned();
//    	System.out.println("Changing item at "+locations.get(5).getLocationName()+" to "+charlie.getItemName());
//		m.setILtree(ChangeILDecision.changeDecisionSelection(m.getLocations().get(5), charlie, m.getLocations(), m.getILtree()));
//
//    	/* TEST CASE 3b - change later appearance of item. earliestItemIndex < currentItemIndex */
//    	Item beta = m.getILtree().get(1).getItemAssigned();
//    	System.out.println("Changing item at "+locations.get(6).getLocationName()+" to "+beta.getItemName());
//		m.setILtree(ChangeILDecision.changeDecisionSelection(m.getLocations().get(6), beta, m.getLocations(), m.getILtree()));
    	
    	/* TEST CASE 4 - add new item to location that does not meet criteria. -CHECK REMOVED- */
    	// don't care about this anymore
//    	Item charlie = m.getILtree().get(0).getItemAssigned();
//		m.setILtree(ChangeILDecision.changeDecisionSelection(m.getLocations().get(2), charlie, m.getLocations(), m.getILtree()));
    	
    	/* Display Locations and item assigned to each location after testing change */
//    	System.out.println("\nList after test case change:\n");
//    	ItemLocationTreeMaker.printDecisionTree(m.getILtree());
		
	}

}
