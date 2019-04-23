package D_tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
//import java.util.LinkedHashSet;

public class decision {

	private ArrayList<Item> unconstrainedChoices;
	private ArrayList<Item> constrainedChoices; 
	private ArrayList<String> ItemLocationMatches = new ArrayList<String>();
	private Item itemAssigned;
	private Location location;
	
	/* Given a location, select an item from a list */
	public decision(Location location) {
		setLocation(location);
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
				item = location.getMandatoryAssignments().get(0);/*top manAss*/
				AssignmentTracker.decreaseCounts(item); 
				//decrease item count here because I increased it artificially earlier
				//this counteracts the next count increase below
			}
			else {
				item = getBestFit();
			}
			setItemAssigned(item);
			
			//this updates how many times an item is assigned
			//System.out.println("ITEM: "+ item.getItemName()+ ", INDEX Of ASSIGNED ITEM: " + CombinatorialTest.itemAssCount.indexOf(item));
			AssignmentTracker.increaseCounts(item);
			
			// Remove item selected from unconstrained list
			if(AssignmentTracker.atLimit(item)) {
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
				item = location.getMandatoryAssignments().get(0);/*top manAss*/
				AssignmentTracker.decreaseCounts(item); 
				//decrease item count here because I increased it artificially earlier
				//this counteracts the next count increase below
			}
			else {
				item = getBestFit(oldItem);
			}
			setItemAssigned(item);
			
			//this updates how many times an item is assigned
			//System.out.println("ITEM: "+ item.getItemName()+ ", INDEX Of ASSIGNED ITEM: " + CombinatorialTest.itemAssCount.indexOf(item));
			AssignmentTracker.increaseCounts(item);
			
			// Remove item selected from unconstrained list
			if(AssignmentTracker.atLimit(item)) {
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
		int frequency = 1;
		
		Set<Entry<Item, Integer>> entrySet = elementCountMap.entrySet();
		
		for (Entry<Item, Integer> entry: entrySet) {
			if(entry.getValue() > frequency) {
				element = entry.getKey();
				frequency = entry.getValue();
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
		int frequency = 1;
		//int oldFrequency = 1;
		
		Set<Entry<Item, Integer>> entrySet = elementCountMap.entrySet();
		
		//StringBuffer NA = new StringBuffer("[NA]");
		for (Entry<Item, Integer> entry: entrySet) {
			if(entry.getValue() > frequency /*&& frequency > oldFrequency*/) {
				//oldFrequency = frequency;
				element = entry.getKey();
				frequency = entry.getValue();
			}
		}
		/*
		if(constrainedChoices.size() == 1) {
			element = constrainedChoices.get(0);
		}*/
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
		this.itemAssigned = item;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
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
					if(conChoice.get(i).getItemProperties().get(j).equals(this.location.getLocationCriteria().get(k)) && AssignmentTracker.checkCount(conChoice.get(i))) {
						//System.out.println("item-> "+conChoice.get(i).getItemName()+", conChoice-> "+conChoice.get(i).getItemProperties().get(j)+"--"+this.location.getLocationCriteria().get(k)+" <-LocCriteria, "+this.location.getLocationName()+" <-Location");
						newList.add(conChoice.get(i));
					}
					else if(arrayCompare(conChoice.get(i).getItemProperties().get(j),this.location.getLocationCriteria().get(k)) && AssignmentTracker.checkCount(conChoice.get(i))){
						newList.add(conChoice.get(i));
					}
					else if(conChoice.get(i).getItemProperties().get(j).equals("[NA]") && AssignmentTracker.checkCount(conChoice.get(i))) {
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
							//System.out.println(props+"=="+crits+" ?");
							if(props.contentEquals(crits)) {
								this.constrainedChoices.add(item);
							}
						}
					}
				}
			}
		}
		if(this.constrainedChoices.size()>1 && getBestFit()!=null) {
			Item newItem = getBestFit();
			//System.out.println("line190 setItem to newItem "+ newItem.getItemName()+"\n");
			if(AssignmentTracker.checkCount(newItem)) {
				setItemAssigned(newItem);
				AssignmentTracker.increaseCounts(newItem);
			}
			//unconstrainedChoices.remove(newItem);
		}
		else if(this.constrainedChoices.size()>0) {
			if(AssignmentTracker.checkCount(this.constrainedChoices.get(0))) {
				setItemAssigned(this.constrainedChoices.get(0));
				AssignmentTracker.increaseCounts(this.constrainedChoices.get(0));
			}
			//unconstrainedChoices.remove(constrainedChoices.get(0));
		}
		else {
			Item noItem = new Item("No item assigned");
			setItemAssigned(noItem);
		}
			
		this.constrainedChoices.clear();
	}
	
	public void getLowConfidenceFit(ArrayList<Item> unassigned) {
		if(unassigned.size()>0 && AssignmentTracker.checkCount(unassigned.get(0))) {
			setItemAssigned(unassigned.get(0));	
		
		//for(Item item: unassigned)
			AssignmentTracker.increaseCounts(unassigned.get(0));
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
    		Integer i = 1;
    		System.out.println("Constrained Item "+ i.toString() +" Name: "+ constrainedChoices.get(j).getItemName());
    		i++;
    	}
	}
	
	public void printUnconstrainedChoices() {
		System.out.println("Location "+ getLocation().getLocationName() + " unconstrained choice list:");
    	for (int j=0; j < unconstrainedChoices.size(); j++) {
    		Integer i = 1;
    		System.out.println("Unconstrained Item "+ i.toString() +" Name: "+ unconstrainedChoices.get(j).getItemName());
    		i++;
    	}
	}
}
