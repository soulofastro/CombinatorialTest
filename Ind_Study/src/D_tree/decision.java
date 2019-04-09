package D_tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
//import java.util.LinkedHashSet;

public class decision {

	private ArrayList<Item> unconstrainedChoices;
	private ArrayList<Item> constrainedChoices; 
	private Item itemAssigned;
	private Location location;
	
	/* Given a location, select an item from a list */
	public decision(Location location) {
		setLocation(location);
	}

	/* THIS IS WHERE I ASSIGN AN ITEM TO A LOCATION */
	public void setDecisionSelection(ArrayList<Item> unconChoices) {
		ArrayList<Item> unconClone = new ArrayList<Item>(unconChoices);
		// given a location (which has a name and criteria) and list of item choices
		// create a list of constrained choices from a list of unconstrained choices
		setConstrainedChoices(unconClone);
		
		// old way: then pick the first item in the constrained list and assign it to this location.
		//			this is now done after first pass through.
		// NEW WAY: Pick item with the most occurrences in the constrained list
		Item item;
		try {
			if(location.getMandatoryAssignments().size() > 0) {
				item = location.getMandatoryAssignments().get(0);/*top manAss*/
			}
			else {
				item = getBestFit();
			}
			setItemAssigned(item);
			
			// Remove item selected from unconstrained list
			unconChoices.remove(item);
		}
		catch(Exception e){
			Item noItem = new Item("No Item Assigned to location.");
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
		int oldFrequency = 1;
		
		Set<Entry<Item, Integer>> entrySet = elementCountMap.entrySet();
		
		//StringBuffer NA = new StringBuffer("[NA]");
		for (Entry<Item, Integer> entry: entrySet) {
			if(entry.getValue() > frequency /*&& frequency > oldFrequency*/) {
				oldFrequency = frequency;
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
		// (update hashmap of mandatory assignments to be super large so it gets chosen first?)
		if(this.location.getMandatoryAssignments().size() > 0) {
			ArrayList<Item> manAss = new ArrayList<Item>(this.location.getMandatoryAssignments());
			newList.addAll(manAss);
		}
		
		for (int i=0; i < conChoice.size(); i++ ) {
			for(int j=0; j < conChoice.get(i).getItemProperties().length; j++) {
				for(int k=0; k < this.location.getLocationCriteria().length; k++) {
					/* if this item element contains a ',' then break it up and compare the pieces to the criteria*/
					if(conChoice.get(i).getItemProperties()[j].equals(this.location.getLocationCriteria()[k])) {
						newList.add(conChoice.get(i));
					}
					else if(conChoice.get(i).getItemProperties()[j].equals("[NA]")) {
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
