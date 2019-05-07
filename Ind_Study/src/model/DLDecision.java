package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class DLDecision {
	
	private ArrayList<ILDecision> unconstrainedChoices;
	private ArrayList<ILDecision> constrainedChoices; 
	private ArrayList<ILDecision> partialFitChoices = new ArrayList<ILDecision>();
	private ArrayList<String> DecisionLocationMatches = new ArrayList<String>();
	private ArrayList<ILDecision> decisionsAssigned;
	private Location location;
	private String decisionName;
	private Integer assignmentLimit = 0; // The limit on how many decisions can be assigned to location
	private Integer numberOfAssignedDecisions = 0; // The number of times this decision has been assigned to a location

	public DLDecision(Location location) {
		setLocation(location);
		setAssignmentLimit(location.getAssignmentLimit());
	}

	public void setDecisionSelection(ArrayList<ILDecision> unconChoices) {
		ArrayList<ILDecision> unconClone = new ArrayList<ILDecision>(unconChoices);
		// given a location (which has a name and criteria) and list of item choices
		// create a list of constrained choices from a list of unconstrained choices
		
		setConstrainedChoices(unconClone);
		
		// NEW WAY: Pick ILDecisions with the most occurrences in the constrained list up to location assignment limit
		// each ILDecision cannot have the same item
		ArrayList<ILDecision> ilDecisions = new ArrayList<ILDecision>();
		try {
			if(location.getMandatoryAssignments().size() > 0) {
				for(int i=0; i<location.getMandatoryAssignments().size(); i++) {
					ilDecisions.add(location.getMandatoryDecisionAssignments().get(i));/*top manAss*/
				//decrease item count here because I increased it artificially earlier
				//this counteracts the next count increase below
				}
				DecisionAssignmentTracker.decreaseCounts(ilDecisions); 
			}
			ilDecisions.addAll(getBestFits());
			setDecisionsAssigned(ilDecisions);
			
			//this updates how many times the decisions are assigned
			DecisionAssignmentTracker.increaseCounts(ilDecisions);
			
			// Remove ILDecisions selected from unconstrained list
			for (ILDecision decision: ilDecisions) {
				if(DecisionAssignmentTracker.atLimit(decision)) {
					unconChoices.remove(decision);
				}
			}
		}
		catch(Exception e){
			ILDecision dec = new ILDecision("No decision assigned");
			ilDecisions.add(dec);
			setDecisionsAssigned(ilDecisions);
		}
		
	}
	
	private ArrayList<ILDecision> getBestFits() {
		HashMap<ILDecision,Integer> elementCountMap = new HashMap<ILDecision,Integer>();
		
		for(ILDecision i: constrainedChoices) {
			if(elementCountMap.containsKey(i))
				elementCountMap.put(i, elementCountMap.get(i)+1);
			else
				elementCountMap.put(i, 1);
		}
		ArrayList<ILDecision> potentialDecisions = new ArrayList<ILDecision>();
		
		// I need to sort this entrySet by entry.getValue() and assign an entry.getKey() up to location assignment limit, making sure no item is added more than once
		Map<ILDecision, Integer> map = sortByValues(elementCountMap);
		Set<Entry<ILDecision, Integer>> entrySet = map.entrySet();
		double frequency = constrainedChoices.size()/entrySet.size();  // if all elements in set are equally likely, don't pick any of them yet
		// make a list of items from each possible ILdecision. If item is in the list, don't add it to potential decisions
		ArrayList<Item> seenItems = new ArrayList<Item>();
		
		for (Entry<ILDecision, Integer> entry: entrySet) { // entrySet should be sorted from Most to Least occurrances
			if(entry.getValue() > frequency && !seenItems.contains(entry.getKey().getItemAssigned())) { // if they're not all tied for frequency, and this item hasn't been added before
				potentialDecisions.add(entry.getKey());
				seenItems.add(entry.getKey().getItemAssigned());
			}
		}
		ArrayList<ILDecision> pDs = new ArrayList<ILDecision>();
		if (potentialDecisions.size() > location.getAssignmentLimit()) {
			pDs = (ArrayList<ILDecision>) potentialDecisions.subList(0, location.getAssignmentLimit());
		}
		else {
			pDs = (ArrayList<ILDecision>) potentialDecisions;
		}
		return pDs;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<ILDecision, Integer> sortByValues(HashMap<ILDecision, Integer> map) {
	       List list = new LinkedList(map.entrySet());
	       // Defined Custom Comparator here
	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
	            }
	       });

	       // Here I am copying the sorted list in HashMap
	       // using LinkedHashMap to preserve the insertion order
	       HashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	}

	public Integer getNumberOfAssignedDecisions() {
		return numberOfAssignedDecisions;
	}

	public void setNumberOfAssignedDecisions(Integer numberOfAssignedDecisions) {
		this.numberOfAssignedDecisions = numberOfAssignedDecisions;
	}

	public Integer getAssignmentLimit() {
		return assignmentLimit;
	}

	public void setAssignmentLimit(Integer assignmentLimit) {
		this.assignmentLimit = assignmentLimit;
	}

	public String getDecisionName() {
		return decisionName;
	}

	public void setDecisionName(String decisionName) {
		this.decisionName = decisionName;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public ArrayList<String> getDecisionLocationMatches() {
		return DecisionLocationMatches;
	}

	public void setDecisionLocationMatches(ArrayList<String> decisionLocationMatches) {
		DecisionLocationMatches = decisionLocationMatches;
	}

	public ArrayList<ILDecision> getConstrainedChoices() {
		return constrainedChoices;
	}

	public void setConstrainedChoices(ArrayList<ILDecision> constrainedChoices) {
		ArrayList<ILDecision> conChoice = new ArrayList<ILDecision>(unconstrainedChoices);
		ArrayList<ILDecision> newList = new ArrayList<ILDecision>();
		
		if(this.location.getMandatoryDecisionAssignments().size() > 0) {
			ArrayList<ILDecision> manAss = new ArrayList<ILDecision>(this.location.getMandatoryDecisionAssignments());
			newList.addAll(manAss);
		}
		
		
		for (int i=0; i < conChoice.size(); i++ ) {
			for(int j=0; j < conChoice.get(i).getItemLocationMatches().size(); j++) {
//				System.out.println("Checking to see if "+conChoice.get(i).getDecisionName()+" matches anything in " +this.location.getLocationName());
				for(int k=0; k < this.location.getLocationCriteria().size(); k++) {
					if(conChoice.get(i).getItemLocationMatches().get(j).equals(this.location.getLocationCriteria().get(k)) && DecisionAssignmentTracker.checkCount(conChoice.get(i))) {
//						System.out.println("ILD-> "+conChoice.get(i).getDecisionName()+", conChoice-> "+conChoice.get(i).getItemLocationMatches().get(j)+"--"+this.location.getLocationCriteria().get(k)+" <-LocCriteria, "+this.location.getLocationName()+" <-Location\n");
						newList.add(conChoice.get(i));
					}
					else if(arrayCompare(conChoice.get(i).getItemLocationMatches().get(j),this.location.getLocationCriteria().get(k)) && DecisionAssignmentTracker.checkCount(conChoice.get(i))){
						newList.add(conChoice.get(i));
					}
					else if(conChoice.get(i).getItemLocationMatches().get(j).equals("[NA]") && DecisionAssignmentTracker.checkCount(conChoice.get(i))) {
						if(!newList.contains(conChoice.get(i)))
							newList.add(conChoice.get(i));
					}
				}
			}
		}
		
		this.constrainedChoices = newList;
	}
	
	private boolean arrayCompare(String propString, String critString) {
		String[] prop = propString.split("&");
		String[] crit = critString.split("&");
		prop = Arrays.stream(prop).distinct().toArray(String[]::new);
		crit = Arrays.stream(crit).distinct().toArray(String[]::new);
		
		int matches = 0;
		if(prop.length==crit.length) {
			for(int i=0; i<prop.length; i++) {
				for(int j=0; j<crit.length;j++) {
					if(prop[i].equalsIgnoreCase(crit[j])) {
						matches = matches + 1;
					}
				}
			}
		}
		if(matches == prop.length)
			return true;
		else
			return false;
	}
	
	public void setConstrainedChoices(ArrayList<ILDecision> descisionsDuplicate, int doesntmatter) {
		ArrayList<ILDecision> conChoice = new ArrayList<ILDecision>(unconstrainedChoices);
		ArrayList<ILDecision> newList = new ArrayList<ILDecision>();
		
		if(this.location.getMandatoryDecisionAssignments().size() > 0) {
			ArrayList<ILDecision> manAss = new ArrayList<ILDecision>(this.location.getMandatoryDecisionAssignments());
			newList.addAll(manAss);
		}
		
		
		for (int i=0; i < conChoice.size(); i++ ) {
			for(int j=0; j < conChoice.get(i).getItemLocationMatches().size(); j++) {
				//System.out.println("Checking to see if "+conChoice.get(i).getItemName()+" matches anything in " +this.location.getLocationName());
				for(int k=0; k < this.location.getLocationCriteria().size(); k++) {
					if(conChoice.get(i).getItemLocationMatches().get(j).equals(this.location.getLocationCriteria().get(k))) {
						//System.out.println("item-> "+conChoice.get(i).getItemName()+", conChoice-> "+conChoice.get(i).getItemLocationMatches().get(j)+"--"+this.location.getLocationCriteria().get(k)+" <-LocCriteria, "+this.location.getLocationName()+" <-Location");
						newList.add(conChoice.get(i));
					}
					else if(arrayCompare(conChoice.get(i).getItemLocationMatches().get(j),this.location.getLocationCriteria().get(k))){
						newList.add(conChoice.get(i));
					}
					else if(conChoice.get(i).getItemLocationMatches().get(j).equals("[NA]")) {
						if(!newList.contains(conChoice.get(i)))
							newList.add(conChoice.get(i));
					}
				}
			}
		}
		this.constrainedChoices = newList;
	}

	public ArrayList<ILDecision> getUnconstrainedChoices() {
		return unconstrainedChoices;
	}

	public void setUnconstrainedChoices(ArrayList<ILDecision> unconstrainedChoices) {
		this.unconstrainedChoices = unconstrainedChoices;
	}

	public ArrayList<ILDecision> getDecisionsAssigned() {
		return decisionsAssigned;
	}

	public void setDecisionsAssigned(ArrayList<ILDecision> decisionsAssigned) {
		this.decisionsAssigned = decisionsAssigned;
	}
	
	public void printConstrainedChoices() {
		System.out.println("Location "+ getLocation().getLocationName() + " constrained choice list: ("+constrainedChoices.size()+")");
    	for (int j=0; j < constrainedChoices.size(); j++) {
    		System.out.println("Constrained Decision Name: "+ constrainedChoices.get(j).getDecisionName());
    	}
		
	}

	public void printUnconstrainedChoices() {
		System.out.println("Location "+ getLocation().getLocationName() + " unconstrained choice list: ("+unconstrainedChoices.size()+")");
    	for (int j=0; j < unconstrainedChoices.size(); j++) {
    		System.out.println("Unconstrained Decision Name: "+ unconstrainedChoices.get(j).getDecisionName());
    	}
		
	}

	public void printMandoAssignments() {
		System.out.println("Location "+ getLocation().getLocationName() + " mandatory assignment list: ("+getLocation().getMandatoryDecisionAssignments().size()+")");
    	for (int j=0; j < getLocation().getMandatoryDecisionAssignments().size(); j++) {
    		System.out.println("Mandatory assignment Decision: "+ getLocation().getMandatoryDecisionAssignments().get(j).getDecisionName());
    	}
		
	}
	
	@Override 
	public boolean equals(Object other){
		if(this == other) {return true;}
		if(other == null) {return false;}
		if(getClass() != other.getClass()) {return false;}
		if(!this.getDecisionName().contentEquals(((DLDecision) other).getDecisionName())) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int result =0;
		result = 5 * decisionName.length();
		return result;
	}

	public void getMediumConfidenceFit(ArrayList<ILDecision> lastNodeUnconstrainedList, ArrayList<String> locationCriteria) {
		// item -> ILDecision
//		System.out.println("GETTING MEDIUM CONFIDENCE FIT ");
		
		for(ILDecision decision: lastNodeUnconstrainedList) {
			
			ArrayList<String> decisionProperties= new ArrayList<String>(decision.getItemLocationPartialMatches());
			for(String propString: decisionProperties) {
				String[] separatedProps = propString.split("&");
				for(String critString: locationCriteria) {
					String[] separatedCrits = critString.split("&");
					for(String props: separatedProps) {
						for(String crits: separatedCrits) {
//							System.out.println("!!!!!!!!!!!!!!!!!decision: "+ decision.getDecisionName() +", "+props+"=="+crits+" ?");
							if(props.contentEquals(crits)) {
								this.constrainedChoices.add(decision);
							}
						}
					}
				}
			}
		}
		
		ArrayList<ILDecision> temp = new ArrayList<ILDecision>();
		try {
			temp.addAll(getBestFits());
			
			setDecisionsAssigned(temp);
			
			//this updates how many times the decisions are assigned
			DecisionAssignmentTracker.increaseCounts(temp);
			
			// Remove ILDecisions selected from unconstrained list
			for (ILDecision decision: temp) {
				if(DecisionAssignmentTracker.atLimit(decision)) {
					this.unconstrainedChoices.remove(decision);
				}
			}
		}
		catch(Exception e){
			ILDecision dec = new ILDecision("No decision assigned");
			temp.add(dec);
			setDecisionsAssigned(temp);
			// avoids divide by zero error
		}
		
		// newItem -> temp
		if(this.constrainedChoices.size()>1 && !temp.contains(new ILDecision("null")) && !temp.contains(new ILDecision("No decision assigned"))) {
//			System.out.println("line376 setItem to newItem "+ newItem.getItemName()+"\n");
			for (ILDecision dec: temp) {
				if(DecisionAssignmentTracker.atLimit(dec)) {
					temp.remove(dec);
				}
			}
			if(temp.size()> this.getAssignmentLimit()) {
				setDecisionsAssigned((ArrayList<ILDecision>) temp.subList(0, this.getAssignmentLimit()));
			}
			else {
				setDecisionsAssigned(temp);
			}
			DecisionAssignmentTracker.increaseCounts(temp);
		}
		
		else if(this.constrainedChoices.size()>0) {
			for(ILDecision dec: constrainedChoices)
				if(DecisionAssignmentTracker.checkCount(dec)) {
					temp.add(dec);
				}
			ILDecision dec1 = new ILDecision("No decision assigned");
			ILDecision dec2 = new ILDecision("null");
			temp.remove(dec1);
			temp.remove(dec2);
			if(temp.size()> this.getAssignmentLimit()) {
				setDecisionsAssigned((ArrayList<ILDecision>) temp.subList(0, this.getAssignmentLimit()));
			}
			else {
				setDecisionsAssigned(temp);
			}
			DecisionAssignmentTracker.increaseCounts(temp);
			//unconstrainedChoices.remove(constrainedChoices.get(0));
		}
		else {
			ILDecision dec = new ILDecision("No decision assigned");
			temp.add(dec);
			setDecisionsAssigned(temp);
		}
		this.setPartialFitChoices(new ArrayList<ILDecision>(getConstrainedChoices()));
		this.constrainedChoices.clear();
	}
	
	public void getLowConfidenceFit(ArrayList<ILDecision> unassigned) {
		// assign decisions in unconstrained list up to limit
		ArrayList<ILDecision> temp = new ArrayList<ILDecision>();
		ArrayList<Item> seen = new ArrayList<Item>();
		
		for(ILDecision dec: unassigned) {
			if(DecisionAssignmentTracker.checkCount(dec) && !seen.contains(dec.getItemAssigned())) { //if limit not reached and item not seen before, add to temp list
				temp.add(dec);
				seen.add(dec.getItemAssigned());
			}
		}
		ILDecision dec1 = new ILDecision("No decision assigned");
		ILDecision dec2 = new ILDecision("null");
		temp.remove(dec1);
		temp.remove(dec2); // remove empty decisions from temp
		if(temp.size() > this.getAssignmentLimit()) {
			ArrayList<ILDecision> cut = new ArrayList<ILDecision>(temp.subList(0, this.getAssignmentLimit()));
			setDecisionsAssigned(cut);
		}
		else {
			setDecisionsAssigned(temp);
		}
		DecisionAssignmentTracker.increaseCounts(temp);
	}

	public ArrayList<ILDecision> getPartialFitChoices() {
		return partialFitChoices;
	}

	public void setPartialFitChoices(ArrayList<ILDecision> partialFitChoices) {
		this.partialFitChoices = partialFitChoices;
	}

	public void printPartialMatches() {
		System.out.println("Location "+ getLocation().getLocationName() + " partial choices list: ("+partialFitChoices.size()+")");
    	for (int j=0; j < partialFitChoices.size(); j++) {
    		System.out.println("Partial fit Decision Name: "+ partialFitChoices.get(j).getDecisionName());
    	}
	}
		
}
