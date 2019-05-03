package model;

import java.util.ArrayList;

public class DecisionAssignmentTracker {

	public static ArrayList<ILDecision> decisionAssCount = new ArrayList<ILDecision>();
	
	public static void newTracker(ArrayList<ILDecision> decisions) {
		decisionAssCount = new ArrayList<ILDecision>(decisions);
	}
	
	public static void add(ILDecision decision) {
		decisionAssCount.add(decision);
	}
	
	public static int showCount(ILDecision decision) {
		if(!decision.getDecisionName().equals("null decision")||!decision.getDecisionName().equals("No decision assigned")||decision!=null){
			Integer index = decisionAssCount.indexOf(decision);
			Integer count = decisionAssCount.get(index).getNumberOfTimesAssigned();
			return count;
		}
		return 0;
	}
	
	public static void increaseCounts(ILDecision decision) {
		if(!decision.getDecisionName().equals("null decision")||!decision.getDecisionName().equals("No decision assigned")||decision!=null){
			Integer index = decisionAssCount.indexOf(decision);
			Integer count = decisionAssCount.get(index).getNumberOfTimesAssigned();
			count += 1;
			decisionAssCount.get(index).setNumberOfTimesAssigned(count);
		}
	}

	public static void decreaseCounts(ILDecision decisionAssigned) {
		if(!decisionAssigned.getDecisionName().equals("null")||!decisionAssigned.getDecisionName().equals("No decision assigned")){
			Integer index = decisionAssCount.indexOf(decisionAssigned);
			Integer count = decisionAssCount.get(index).getNumberOfTimesAssigned();
			count -= 1;
			if(count < 0)
				count = 0;
			decisionAssCount.get(index).setNumberOfTimesAssigned(count);
		}
		
	}

	// use this if you want to add an decision
	public static boolean checkCount(ILDecision decision) {
		if(!decision.getDecisionName().equals("null")||decision.getDecisionName().equals("No decision assigned")){
			Integer index = decisionAssCount.indexOf(decision);
			Integer count = decisionAssCount.get(index).getNumberOfTimesAssigned();
			if(count < decisionAssCount.get(index).getAssignmentLimit()) {
				return true;
			}
		}
		return false;
	}
	
	// use this if you want to remove an decision
	public static boolean atLimit(ILDecision decision) {
		if(!decision.getDecisionName().equals("null")||!decision.getDecisionName().equals("No decision assigned")){
//			System.out.println("At limit check for "+decision.getdecisionName());
			Integer index = decisionAssCount.indexOf(decision);
			Integer count = decisionAssCount.get(index).getNumberOfTimesAssigned();
			if(count >= decisionAssCount.get(index).getAssignmentLimit()) {
				return true;
			}
		}
		return false;
	}

	public static void resetCount(ILDecision olddecision) {
		olddecision.setNumberOfTimesAssigned(0);	
	}

	public static void decreaseCounts(ArrayList<ILDecision> ilDecisions) {
		for(ILDecision decisionAssigned: ilDecisions) {
			if(!decisionAssigned.getDecisionName().equals("null")||!decisionAssigned.getDecisionName().equals("No decision assigned")){
				Integer index = decisionAssCount.indexOf(decisionAssigned);
				Integer count = decisionAssCount.get(index).getNumberOfTimesAssigned();
				count -= 1;
				if(count < 0)
					count = 0;
				decisionAssCount.get(index).setNumberOfTimesAssigned(count);
			}
		}
	}
	public static void increaseCounts(ArrayList<ILDecision> ilDecisions) {
		for(ILDecision decision: ilDecisions) {
			if(!decision.getDecisionName().equals("null decision")||!decision.getDecisionName().equals("No decision assigned")||decision!=null){
				Integer index = decisionAssCount.indexOf(decision);
				Integer count = decisionAssCount.get(index).getNumberOfTimesAssigned();
				count += 1;
				decisionAssCount.get(index).setNumberOfTimesAssigned(count);
			}
		}
	}
}

