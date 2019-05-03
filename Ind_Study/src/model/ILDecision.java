package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
//import java.util.LinkedHashSet;

public class ILDecision {

	private ArrayList<Item> unconstrainedChoices;
	private ArrayList<Item> constrainedChoices; 
	private ArrayList<Item> partialFitChoices = new ArrayList<Item>();
	private ArrayList<String> ItemLocationMatches = new ArrayList<String>();
	private ArrayList<String> ItemLocationPartialMatches = new ArrayList<String>();
	private Item itemAssigned;
	private Location location;
	private String decisionName;
	private Integer assignmentLimit = 1; // The limit on how many times this decision can be assigned
	private Integer numberOfTimesAssigned = 0; // The number of times this decision has been assigned to a location
	
	/* Given a location, select an item from a list */
	public ILDecision(Location location) {
		setLocation(location);
	}
	
	public ILDecision(String string) {
		setDecisionName(string);
	}

	public ArrayList<String> getMatches(){	
			ArrayList<String> temp = new ArrayList<String>(itemAssigned.getItemProperties());
			temp.addAll(location.getLocationCriteria());
			
			HashMap<String,Integer> elementCountMap = new HashMap<String,Integer>();
			
			for(String i: temp) {
				if(elementCountMap.containsKey(i))
					elementCountMap.put(i, elementCountMap.get(i)+1);
				else
					elementCountMap.put(i, 1);
			}
			int frequency = 1;
			
			Set<Entry<String, Integer>> entrySet = elementCountMap.entrySet();
			
			for (Entry<String, Integer> entry: entrySet) {
				if(entry.getValue() > frequency) {
					ItemLocationMatches.add(entry.getKey());
				}
			}
			ArrayList<String> matches = new ArrayList<String>(ItemLocationMatches);
			return matches;
	}
	public ArrayList<String> getPartialMatches(){	
		ArrayList<String> temp = new ArrayList<String>();
		for(String itemProp: itemAssigned.getItemProperties()) {
			String[] tempIP = itemProp.split("&");
			for(int i=0;i<tempIP.length;i++) {
				temp.add(tempIP[i]);
			}
		}
		for(String locCrit: location.getLocationCriteria()) {
			String[] tempLC = locCrit.split("&");
			for(int i=0;i<tempLC.length;i++) {
				temp.add(tempLC[i]);
			}
		}
		
		
		HashMap<String,Integer> elementCountMap = new HashMap<String,Integer>();
		
		for(String i: temp) {
			if(elementCountMap.containsKey(i))
				elementCountMap.put(i, elementCountMap.get(i)+1);
			else
				elementCountMap.put(i, 1);
		}
		int frequency = 1;
		
		Set<Entry<String, Integer>> entrySet = elementCountMap.entrySet();
		
		for (Entry<String, Integer> entry: entrySet) {
			if(entry.getValue() > frequency) {
				ItemLocationPartialMatches.add(entry.getKey());
			}
		}
		ArrayList<String> matches = new ArrayList<String>(ItemLocationPartialMatches);
		return matches;
}
	

	/* THIS IS WHERE I ASSIGN AN ITEM TO A LOCATION */
	public void setDecisionSelection(ArrayList<Item> unconChoices) {
		ArrayList<Item> unconClone = new ArrayList<Item>(unconChoices);
		// given a location (which has a name and criteria) and list of item choices
		// create a list of constrained choices from a list of unconstrained choices
		setConstrainedChoices(unconClone);
		
		// NEW WAY: Pick item with the most occurrences in the constrained list
		Item item;
		try {
			if(location.getMandatoryAssignments().size() > 0) {
//				System.out.println("I FOUND A MANDATORY ASSIGNMENT AT LINE 60. ITEM: "+location.getMandatoryAssignments().get(0).getItemName());
				item = location.getMandatoryAssignments().get(0);/*top manAss*/
				ItemAssignmentTracker.decreaseCounts(item); 
				//decrease item count here because I increased it artificially earlier
				//this counteracts the next count increase below
			}
			else {
				item = getBestFit();
			}
			setItemAssigned(item);
//			System.out.println("(LINE 70) I JUST ASSIGNED "+this.getItemAssigned().getItemName() +" TO LOCATION "+ this.getLocation().getLocationName());
			
			//this updates how many times an item is assigned
			//System.out.println("ITEM: "+ item.getItemName()+ ", INDEX Of ASSIGNED ITEM: " + CombinatorialTest.itemAssCount.indexOf(item));
			ItemAssignmentTracker.increaseCounts(item);
			
			// Remove item selected from unconstrained list
			if(ItemAssignmentTracker.atLimit(item)) {
				unconChoices.remove(item);
			}
		}
		catch(Exception e){
			Item noItem = new Item("No item assigned");
			setItemAssigned(noItem);
			//System.out.println("Assignment error.");
		}
		
	}
	public void setDecisionSelection(ArrayList<Item> unconChoices, Item oldItem) {
		ArrayList<Item> unconClone = new ArrayList<Item>(unconChoices);
		// given a location (which has a name and criteria) and list of item choices
		// create a list of constrained choices from a list of unconstrained choices
		setConstrainedChoices(unconClone);
		
		// NEW WAY: Pick item with the most occurrences in the constrained list
		Item item;
		try {
			if(location.getMandatoryAssignments().size() > 0) {
				System.out.println("I FOUND A MANDATORY ASSIGNMENT AT LINE 98");
				item = location.getMandatoryAssignments().get(0);/*top manAss*/
				ItemAssignmentTracker.decreaseCounts(item); 
				//decrease item count here because I increased it artificially earlier
				//this counteracts the next count increase below
			}
			else {
				item = getBestFit(oldItem);
			}
			setItemAssigned(item);
			System.out.println("(LINE 108) I JUST ASSIGNED "+this.getItemAssigned().getItemName() +" TO LOCATION "+ this.getLocation().getLocationName());
			
			//this updates how many times an item is assigned
			//System.out.println("ITEM: "+ item.getItemName()+ ", INDEX Of ASSIGNED ITEM: " + CombinatorialTest.itemAssCount.indexOf(item));
			ItemAssignmentTracker.increaseCounts(item);
			
			// Remove item selected from unconstrained list
			if(ItemAssignmentTracker.atLimit(item)) {
				unconChoices.remove(item);
			}
		}
		catch(Exception e){
			Item noItem = new Item("No item assigned");
			setItemAssigned(noItem);
			//System.out.println("Assignment error.");
		}
		
	}
	
	private Item getBestFit() {
		// I need to look at the constrained list and determine which item has appeared the most, then return that item
		HashMap<Item,Integer> elementCountMap = new HashMap<Item,Integer>();
		
		for(Item i: constrainedChoices) {
			if(elementCountMap.containsKey(i))
				elementCountMap.put(i, elementCountMap.get(i)+1);
			else
				elementCountMap.put(i, 1);
		}
		Item element = new Item();
		
		Set<Entry<Item, Integer>> entrySet = elementCountMap.entrySet();
		double frequency = constrainedChoices.size()/entrySet.size();  // if all elements in set are equally likely, don't pick any of them yet
		
		for (Entry<Item, Integer> entry: entrySet) {
			if(entry.getValue() > frequency) {
				element = entry.getKey();
				frequency = entry.getValue();
				// thought: if I see another entry with the same value, set my element back to null
			}
			else if(entry.getValue() == frequency) {
				element = new Item();
			}
		}
		/******
        if(frequency > 1)
        {
        	System.out.println("========================");
            System.out.println("Input Array : "); printConstrainedChoices();
            System.out.println("The most frequent element : "+element.getItemName());
            System.out.println("Its frequency : "+frequency); 
            System.out.println("========================");
        }
        else
        {
        	System.out.println("========================");
            System.out.println("Input Array : "); printConstrainedChoices();
            System.out.println("No frequent element. All elements are unique."); 
            System.out.println("=========================");
        }
		******/
		//System.out.println("ELEMENT: "+ element.getItemName());
		return element;
	}
	
	private Item getBestFit(Item oldItem) {
		// I need to look at the constrained list and determine which item has appeared the most, then return that item
		HashMap<Item,Integer> elementCountMap = new HashMap<Item,Integer>();
		
		constrainedChoices.removeAll(Collections.singleton(oldItem));
		
		for(Item i: constrainedChoices) {
			if(elementCountMap.containsKey(i))
				elementCountMap.put(i, elementCountMap.get(i)+1);
			else
				elementCountMap.put(i, 1);
		}
		Item element = constrainedChoices.get(0);
		
		Set<Entry<Item, Integer>> entrySet = elementCountMap.entrySet();
		double frequency = constrainedChoices.size()/entrySet.size();
		
		for (Entry<Item, Integer> entry: entrySet) {
			if(entry.getValue() > frequency) {
				element = entry.getKey();
				frequency = entry.getValue();
				// thought: if I see another entry with the same value, set my element back to null
			}
			else if(entry.getValue() == frequency) { 
				element = new Item();
			}
		}
		/******
        if(frequency > 1)
        {
        	System.out.println("========================");
            System.out.println("Input Array : "); printConstrainedChoices();
            System.out.println("The most frequent element : "+element.getItemName());
            System.out.println("Its frequency : "+frequency); 
            System.out.println("========================");
        }
        else
        {
        	System.out.println("========================");
            System.out.println("Input Array : "); printConstrainedChoices();
            System.out.println("No frequent element. All elements are unique."); 
            System.out.println("=========================");
        }
		******/
		//System.out.println("ELEMENT: "+ element.getItemName());
		constrainedChoices.add(oldItem);
		return element;
	}

	public String getDecisionSelected() {
		return itemAssigned.getItemName(); 
	}

	public ArrayList<Item> getUnconstrainedChoices() {
		return unconstrainedChoices;
	}

	public void setUnconstrainedChoices(ArrayList<Item> unconstrainedChoices) {
		this.unconstrainedChoices = unconstrainedChoices;
	}

	public Item getItemAssigned() {
		return itemAssigned;
	}

	public void setItemAssigned(Item item) {
//		System.out.println("ASSIGNING ITEM "+ item.getItemName());
		this.itemAssigned = item;
		this.setDecisionName(this.location.getLocationName()+"_"+item.getItemName());
		if(!item.equals(new Item("No item assigned"))) {
			if(!item.equals(new Item("null"))){
				getMatches();
			}
		}
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getDecisionName() {
		return decisionName;
	}

	public void setDecisionName(String decisionName) {
		this.decisionName = decisionName;
	}

	public ArrayList<Item> getConstrainedChoices() {
		return constrainedChoices;
	}

	public void setConstrainedChoices(ArrayList<Item> unconstrainedChoices) {
		ArrayList<Item> conChoice = new ArrayList<Item>(unconstrainedChoices);
		ArrayList<Item> newList = new ArrayList<Item>();
		
		// add mandatory choices to list first, then add other choices
		if(this.location.getMandatoryAssignments().size() > 0) {
			ArrayList<Item> manAss = new ArrayList<Item>(this.location.getMandatoryAssignments());
			newList.addAll(manAss);
		}
		
		
		for (int i=0; i < conChoice.size(); i++ ) {
			for(int j=0; j < conChoice.get(i).getItemProperties().size(); j++) {
				//System.out.println("Checking to see if "+conChoice.get(i).getItemName()+" matches anything in " +this.location.getLocationName());
				for(int k=0; k < this.location.getLocationCriteria().size(); k++) {
					if(conChoice.get(i).getItemProperties().get(j).equals(this.location.getLocationCriteria().get(k)) && ItemAssignmentTracker.checkCount(conChoice.get(i))) {
						//System.out.println("item-> "+conChoice.get(i).getItemName()+", conChoice-> "+conChoice.get(i).getItemProperties().get(j)+"--"+this.location.getLocationCriteria().get(k)+" <-LocCriteria, "+this.location.getLocationName()+" <-Location");
						newList.add(conChoice.get(i));
					}
					else if(arrayCompare(conChoice.get(i).getItemProperties().get(j),this.location.getLocationCriteria().get(k)) && ItemAssignmentTracker.checkCount(conChoice.get(i))){
						newList.add(conChoice.get(i));
					}
					else if(conChoice.get(i).getItemProperties().get(j).equals("[NA]") && ItemAssignmentTracker.checkCount(conChoice.get(i))) {
						if(!newList.contains(conChoice.get(i)))
							newList.add(conChoice.get(i));
					}
				}
			}
		}
		// this is where I would build my sublist of allowed choices
		// using location criteria and item properties
		//LinkedHashSet<Item> set = new LinkedHashSet<Item>(newList);
		//newList.clear();
		//newList.addAll(set);
		this.constrainedChoices = newList;
	}
	
	// this overload ignores the count requirement
	public void setConstrainedChoices(ArrayList<Item> itemsDuplicate, int doesntmatter) {
		ArrayList<Item> conChoice = new ArrayList<Item>(itemsDuplicate);
		ArrayList<Item> newList = new ArrayList<Item>();
		
		// add mandatory choices to list first, then add other choices
		if(this.location.getMandatoryAssignments().size() > 0) {
			ArrayList<Item> manAss = new ArrayList<Item>(this.location.getMandatoryAssignments());
			newList.addAll(manAss);
		}
		Item itemNA = new Item("No item assigned");
		Item itemNull = new Item("null");
		conChoice.remove(itemNA);
		conChoice.remove(itemNull);
		
		
		for (int i=0; i < conChoice.size(); i++ ) {
			for(int j=0; j < conChoice.get(i).getItemProperties().size(); j++) {
				//System.out.println("Checking to see if "+conChoice.get(i).getItemName()+" matches anything in " +this.location.getLocationName());
				for(int k=0; k < this.location.getLocationCriteria().size(); k++) {
					if(conChoice.get(i).getItemProperties().get(j).equals(this.location.getLocationCriteria().get(k))) {
						//System.out.println("item-> "+conChoice.get(i).getItemName()+", conChoice-> "+conChoice.get(i).getItemProperties().get(j)+"--"+this.location.getLocationCriteria().get(k)+" <-LocCriteria, "+this.location.getLocationName()+" <-Location");
						newList.add(conChoice.get(i));
					}
					else if(arrayCompare(conChoice.get(i).getItemProperties().get(j),this.location.getLocationCriteria().get(k))){
						newList.add(conChoice.get(i));
					}
					else if(conChoice.get(i).getItemProperties().get(j).equals("[NA]")) {
						if(!newList.contains(conChoice.get(i)))
							newList.add(conChoice.get(i));
					}
				}
			}
		}
		// this is where I would build my sublist of allowed choices
		// using location criteria and item properties
		this.constrainedChoices = newList;
	}
	
	public void setItemLocationMatches(){
		getMatches();
	}
	
	public ArrayList<String> getItemLocationMatches(){
		return ItemLocationMatches;
	}
	
	public Integer getAssignmentLimit() {
		return assignmentLimit;
	}

	public void setAssignmentLimit(Integer assignmentLimit) {
		this.assignmentLimit = assignmentLimit;
	}

	public Integer getNumberOfTimesAssigned() {
		return numberOfTimesAssigned;
	}

	public void setNumberOfTimesAssigned(Integer numberOfTimesAssigned) {
		this.numberOfTimesAssigned = numberOfTimesAssigned;
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

	public void getMediumConfidenceFit(ArrayList<Item> Unassigned, ArrayList<String> locationCriteria) {
		
		for(Item item: Unassigned) {
			//System.out.println("ITEM: "+item.getItemName());
			ArrayList<String> itemProperties= new ArrayList<String>(item.getItemProperties());
			for(String propString: itemProperties) {
				String[] separatedProps = propString.split("&");
				for(String critString: locationCriteria) {
					String[] separatedCrits = critString.split("&");
					for(String props: separatedProps) {
						for(String crits: separatedCrits) {
//							System.out.println("ITEM: "+ item.getItemName()+", "+props+"=="+crits+" ?");
							if(props.contentEquals(crits)) {
								this.constrainedChoices.add(item);
							}
						}
					}
				}
			}
		}
		
		Item newItem = new Item();
		try {
			newItem = getBestFit();
		}
		catch(Exception e){
			newItem = new Item();
			// avoids divide by zero error
		}
		
		if(this.constrainedChoices.size()>1 && !newItem.getItemName().contentEquals("null")) {
//			System.out.println("line376 setItem to newItem "+ newItem.getItemName()+"\n");
			if(ItemAssignmentTracker.checkCount(newItem)) {
				setItemAssigned(newItem);
				ItemAssignmentTracker.increaseCounts(newItem);
			}
			//unconstrainedChoices.remove(newItem);
		}
		else if(this.constrainedChoices.size()>0) {
			if(ItemAssignmentTracker.checkCount(this.constrainedChoices.get(this.constrainedChoices.size()-1))) {
				newItem = this.constrainedChoices.get(this.constrainedChoices.size()-1);
//				System.out.println("line384 setItem to newItem "+ newItem.getItemName()+"\n");
				setItemAssigned(newItem);
				ItemAssignmentTracker.increaseCounts(this.constrainedChoices.get(this.constrainedChoices.size()-1));
			}
			//unconstrainedChoices.remove(constrainedChoices.get(0));
		}
		else {
			Item noItem = new Item("No item assigned");
			setItemAssigned(noItem);
		}
		this.setPartialFitChoices(new ArrayList<Item>(getConstrainedChoices()));
		this.constrainedChoices.clear();
	}
	
	public void getLowConfidenceFit(ArrayList<Item> unassigned) {
		if(unassigned.size()>0 && ItemAssignmentTracker.checkCount(unassigned.get(0))) {
			setItemAssigned(unassigned.get(0));	
		
		//for(Item item: unassigned)
			ItemAssignmentTracker.increaseCounts(unassigned.get(0));
			/*if(!item.getItemName().equals("null")||!item.getItemName().equals("No item assigned")){
				Integer index = AssignmentTracker.itemAssCount.indexOf(item);
				Integer count = AssignmentTracker.itemAssCount.get(index).getNumberOfTimesAssigned();
				count += 1;
				AssignmentTracker.itemAssCount.get(AssignmentTracker.itemAssCount.indexOf(item)).setNumberOfTimesAssigned(count);
			}*/
		}
	}
	
	/* Methods to print constrained and unconstrained lists */
	public void printConstrainedChoices() {
		System.out.println("Location "+ getLocation().getLocationName() + " constrained choice list:");
    	for (int j=0; j < constrainedChoices.size(); j++) {
    		System.out.println("Constrained Item Name: "+ constrainedChoices.get(j).getItemName());
    	}
	}
	
	public void printUnconstrainedChoices() {
		System.out.println("Location "+ getLocation().getLocationName() + " unconstrained choice list:");
    	for (int j=0; j < unconstrainedChoices.size(); j++) {
    		System.out.println("Unconstrained Item Name: "+ unconstrainedChoices.get(j).getItemName());
    	}
	}

	public void printMandoAssignments() {
		System.out.println("Location "+ getLocation().getLocationName() + " mandatory assignment list:");
    	for (int j=0; j < getLocation().getMandatoryAssignments().size(); j++) {
    		System.out.println("Mandatory assignment Item: "+ getLocation().getMandatoryAssignments().get(j).getItemName());
    	}
		
	}
	
	@Override 
	public boolean equals(Object other){
		if(this == other) {return true;}
		if(other == null) {return false;}
		if(getClass() != other.getClass()) {return false;}
		if(!this.getDecisionName().contentEquals(((ILDecision) other).getDecisionName())) {
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

	public Collection<? extends String> getItemLocationPartialMatches() {
		return ItemLocationPartialMatches;
	}

	public ArrayList<Item> getPartialFitChoices() {
		return partialFitChoices;
	}

	public void setPartialFitChoices(ArrayList<Item> partialFitChoices) {
		this.partialFitChoices = partialFitChoices;
	}

	public void printPartialMatches() {
		System.out.println("Location "+ getLocation().getLocationName() + " partial choice list:");
    	for (int j=0; j < partialFitChoices.size(); j++) {
    		System.out.println("partial fit Item Name: "+ partialFitChoices.get(j).getItemName());
    	}
		
	}
}