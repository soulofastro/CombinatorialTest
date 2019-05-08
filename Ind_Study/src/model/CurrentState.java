package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class CurrentState {
	
	/* This class should:
	 * 1.read the item file and store it into an arrayList<Item>
	 * 2.read the location file and store it into an arrayList<Location>
	 * 3.record the result of ItemLocationTreeMaker in an arraylist<Decision>
	 * 4.record the result of DecisionLocationTreeMaker in an arrayList<Decision>
	 * then:
	 * 5. Push ILtree into a file
	 * 6. Push DLtree into a file
	 */
	
	private ArrayList<Item> items = new ArrayList<Item>();
	private ArrayList<Location> locations = new ArrayList<Location>();
	private ArrayList<Location> timeSlots = new ArrayList<Location>();
	private ArrayList<ILDecision> itemLocationTree = new ArrayList<ILDecision>();
	private ArrayList<DLDecision> decisionLocationTree = new ArrayList<DLDecision>();
	
	public CurrentState() throws FileNotFoundException, IOException, Exception {
		/* these MUST be initialized in this order */
		setItems("itemsSimple.txt");
		setLocations("locationsSimple.txt");
		setTimeSlots("TimeSlotsSimple.txt");
		setILtree();
		setDLtree();
	}
	
	public CurrentState(String itemFileName, String locationFileName, String timeFileName) throws FileNotFoundException, IOException, Exception {
		/* these MUST be initialized in this order */
		setItems(itemFileName);
		setLocations(locationFileName);
		setTimeSlots(timeFileName);
		setILtree();
		setDLtree();
	}
	
	public ArrayList<Item> getItems(){
		return items;
	}
	
	public void setItems(String filename) throws FileNotFoundException, IOException, Exception {
		this.items = readItemFile(filename);
	}
	
	public ArrayList<Location> getLocations(){
		return locations;
	}
	
	public void setLocations(String filename) throws FileNotFoundException, IOException, Exception {
		this.locations = readLocationFile(filename);
	}
	
	public ArrayList<Location> getTimeSlots(){
		return timeSlots;
	}
	
	public void setTimeSlots(String filename) throws FileNotFoundException, IOException, Exception {
		this.timeSlots = readLocationFile(filename);
	}
	
	public ArrayList<ILDecision> getILtree(){
		//TODO
		Collections.sort(itemLocationTree, ILDecision.PropComparator);
		return itemLocationTree;
	}
	
	public void setILtree() throws FileNotFoundException, IOException, Exception {
		this.itemLocationTree = ItemLocationTreeMaker.createILtree(items,locations);
	}
	
	public ArrayList<DLDecision> getDLtree(){
		return decisionLocationTree;
	}
	
	public void setDLtree() {
		this.decisionLocationTree = DecisionLocationTreeMaker.createDLtree(itemLocationTree, timeSlots);
	}
	
	
	/*
     * The follow two methods read items and locations from a file and return their respective array lists.
     * These were created with simple lists in mind and only assign "name" attributes to their objects.
     */
    public static ArrayList<Item> readItemFile(String filename) throws Exception, FileNotFoundException, IOException {
		FileReader fr = new FileReader(filename);
        BufferedReader textReader = new BufferedReader(fr);
        ArrayList<Item> itemsFromFile = new ArrayList<Item>();
        ArrayList<Item> itemsWithNA = new ArrayList<Item>();
        
        int counter = 0;
        while (textReader.ready()) {
        	String[] line = textReader.readLine().split(","); // for each line read, add 1 to counter.
            Item item = new Item(line[0]);
            item.setAssignmentLimit(Integer.parseInt(line[1]));
            item.setItemProperties(line);
            itemsFromFile.add(item);
            counter++;
        }
        textReader.close();
        if(counter <= 0)
                throw new Exception("Error. "+filename+" is empty."); // if no lines read, throw empty file exception
        
        /* Sort items from most to least constrained according to length of their properties (aka requirements) */
        StringBuffer NA = new StringBuffer("[NA]");
        if(!itemsFromFile.isEmpty()) {
	        for(Item item: itemsFromFile) {
	        	if(!item.getItemProperties().isEmpty()/*.get(1) != null*/){
		        	if(item.getItemProperties().get(1).contentEquals(NA)) {
		        		itemsWithNA.add(item);
		        	}
	        	}
	        }
        itemsFromFile.removeAll(itemsWithNA);
        Collections.sort(itemsFromFile, Item.PropComparator);
        itemsFromFile.addAll(itemsWithNA);
        }
        return itemsFromFile;
    }
    
    public static ArrayList<Location> readLocationFile(String filename) throws Exception, FileNotFoundException, IOException {
		FileReader fr = new FileReader(filename);
        BufferedReader textReader = new BufferedReader(fr);
        ArrayList<Location> locationsFromFile = new ArrayList<Location>();
        ArrayList<Location> locationsWithNA = new ArrayList<Location>();
        
        int counter = 0;
        while (textReader.ready()) {
        	String[] line = textReader.readLine().split(","); // for each line read, add 1 to counter.
            Location location = new Location(line[0]); // this is a simple location list, with no constraints
            location.setAssignmentLimit(Integer.parseInt(line[1]));
            location.setLocationCriteria(line);
            locationsFromFile.add(location);
            counter++;
        }
        textReader.close();
        if(counter <= 0)
                throw new Exception("Error. "+filename+" is empty."); // if no lines read, throw empty file exception
        
        /* Sort locations from most to least constrained according to length of their criteria (aka requirements) */
        StringBuffer NA = new StringBuffer("[NA]");
        if(!locationsFromFile.isEmpty()) {
	        for(Location location: locationsFromFile) {
	        	if(!location.getLocationCriteria().isEmpty()/*.get(1) != null*/){
		        	if(location.getLocationCriteria().get(1).contentEquals(NA)) {
		        		locationsWithNA.add(location);
		        	}
	        	}
	        }
	    locationsFromFile.removeAll(locationsWithNA);
        Collections.sort(locationsFromFile, Location.critComparator);
        locationsFromFile.addAll(locationsWithNA);
        }
        return locationsFromFile;
    }
}
