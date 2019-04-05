package D_tree;
import java.util.Comparator;
import java.util.ArrayList;

@SuppressWarnings("rawtypes")
public class Location implements Comparable{
	
	private String locationName = "";
	private String[] locationCriteria;  // string array for now until i figure out a 
										// better way. This is where I will store
										// "class requirements" and/or time slot requirements
	private ArrayList<Item> mandatoryAssignments = new ArrayList<Item>();

	
	/* constructors */
	public Location() {
	}
	public Location(String locName) {
		setLocationName(locName);
	}

	/* Setters and getters  */
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String[] getLocationCriteria() {
		return locationCriteria;
	}
	public void setLocationCriteria(String[] locationCriteria) {
		this.locationCriteria = locationCriteria;
	}
	public ArrayList<Item> getMandatoryAssignments() {
		return mandatoryAssignments;
	}
	public void setMandatoryAssignments(ArrayList<Item> mandatoryAssignments) {
		this.mandatoryAssignments = mandatoryAssignments;
	}
	public void assignMandatoryItem(Item item) {
		this.mandatoryAssignments.add(item);
	}
	@Override
	public int compareTo(Object o) {
		int compareLength=((Location)o).getLocationCriteria().length;
		return this.locationCriteria.length-compareLength;
	}
	
	public static Comparator<Location> critComparator = new Comparator<Location>(){
		public int compare(Location L1, Location L2) {
			Integer L1Length = L1.getLocationCriteria().length;
			Integer L2Length = L2.getLocationCriteria().length;
			return L2Length.compareTo(L1Length);
		}
	};

}
