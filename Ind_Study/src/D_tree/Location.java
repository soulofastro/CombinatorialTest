package D_tree;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("rawtypes")
public class Location implements Comparable{
	
	private String locationName = "";
	private ArrayList<String> locationCriteria;  // string array for now until i figure out a 
										// better way. This is where I will store
										// "class requirements" and/or time slot requirements
	private ArrayList<Item> mandatoryAssignments = new ArrayList<Item>();
	private Integer numberOfConstraints = 0;

	
	/* constructors */
	public Location() {
	}
	public Location(String locName) {
		setLocationName(locName);
	}

	/* Setters and getters  */
	public Integer getNumberOfConstraints() {
		return numberOfConstraints;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public ArrayList<String> getLocationCriteria() {
		return locationCriteria;
	}
	public void setLocationCriteria(String[] locationCrits) {
		ArrayList<String> tempProps = new ArrayList<String>();
		/* This is where I turn ORs into array elements. Example: [A]&[B|C] becomes [A]&[B],[A]&[C] */
		for(int i=0; i < locationCrits.length;i++) {
			//if(itemProps[i].indexOf("|") >= 0) {
				tempProps.addAll(propertyExpansion(locationCrits[i]));
			//}
		}
		
		/* End */
		//String[] newArray = new String[tempProps.size()+itemProps.length];
		//tempProps.toArray(newArray);
		LinkedHashSet<String> set = new LinkedHashSet<String>(tempProps);
		tempProps.clear();
		tempProps.addAll(set);
		set.clear();
		this.locationCriteria = tempProps;
		
		String[] line;
		/* go through the location's criteria and count the number of constraints */
		if(locationCriteria.size() > 1) {
			if(!locationCriteria.get(1).contentEquals("[NA]")) { 				
				ArrayList<String> temp = new ArrayList<String>();	
				for (int i=1;i<locationCriteria.size();i++) {				
					if(locationCriteria.get(i).indexOf("&") >= 0) {
						numberOfConstraints = numberOfConstraints +1;
						//System.out.println("Inside count loop. Constraint count=" +numberOfConstraints);
					}
					//[x]&[y] contains three constraint properties => [x][y], [x], and [y]	
					line = locationCriteria.get(i).split("&");
					for(String string: line) {
						temp.add(string);
					}
				}
				
				LinkedHashSet<String> set2 = new LinkedHashSet<String>(temp);
				temp.clear();
				temp.addAll(set2);
				numberOfConstraints = numberOfConstraints + set2.size();
				set.clear();
			}
		}
	}
	
	/* This is where I turn ORs into array elements. Example: [A]&[B|C] becomes [A]&[B],[A]&[C] */
	ArrayList<String> expanded = new ArrayList<String>();
	
	private Collection<? extends String> propertyExpansion(String string) {
		char[] propLine = string.toCharArray();
		int index = 0;
		boolean finished = false;
		String property ="";
		String lastOpenBrak = string.substring(0, 0);
		while(!finished) {
			//System.out.print("Index: "+index+"   Char: "+propLine[index]);
			if(propLine[index] != '|') {
				property = property + propLine[index];
			}
			else if(propLine[index] == '|') {
				property = property + ']';
				String nextAnd = string.substring(index+1);
				String otherOption = property.substring(0, property.lastIndexOf('[')+1) + string.substring(index+1);
				char[] tempString = nextAnd.toCharArray();
				if(containsChar('&',tempString)) {
					int andSign = nextAnd.indexOf('&');
					//System.out.println("    saw an '&' at line 99 at index: "+andSign+" in "+nextAnd);
					//String previousBuild = property.substring(0, property.lastIndexOf('[')+1);//this should be the string prior to the [ before tbe current | index
					//System.out.println("Previous build: "+previousBuild);
					//System.out.println("Other option: "+otherOption);
					expanded.addAll(propertyExpansion(otherOption));
					index = andSign + index;
					
				}
				else {
					expanded.add(property);
					//System.out.println("didn't see an '&' at line 99, so I added: "+property);
					if(containsChar('|', tempString)) {
						int lastOpenBracket = property.lastIndexOf('[')+1;
						lastOpenBrak = property.substring(0, lastOpenBracket);
						property = lastOpenBrak;
					}
					else { // no more |s or &s left must mean you are at the end. 
						int lastOpenBracket = property.lastIndexOf('[')+1;
						lastOpenBrak = property.substring(0, lastOpenBracket);
						//System.out.println("lastOpenBrak: "+lastOpenBrak);
						StringBuilder sb = new StringBuilder();
						for(int k=index+1;k<propLine.length;k++) {
							sb.append(propLine[k]);
						}
						String restOfString = sb.toString();
						property = lastOpenBrak;
						property = property + restOfString;
						//System.out.println("you made it to line 123. I just added: "+property);
						expanded.add(property);
						finished = true;
					}
				}
			}
			if(index+1 < propLine.length) {
				index++;
			}
			else {
				expanded.add(property);
				finished = true;
			}
			//System.out.println("    property: "+property);
		}
		
		return expanded;
	}
	
	public boolean containsChar(char c, char[] array) {
	    for (char x : array) {
	        if (x == c) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public int numberOfCharAppearances(char c, char[] array) {
		int count = 0;
	    for (char x : array) {
	        if (x == c) {
	            count++;
	        }
	    }
	    return count;
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
	public int compareTo(Object arg0) {
		int compareLength=((Location)arg0).getNumberOfConstraints();
		if(compareLength > 0)
			return this.numberOfConstraints-compareLength;
		else
			return 0;
	}
	
	public static Comparator<Location> critComparator = new Comparator<Location>(){
		public int compare(Location L1, Location L2) {
			Integer L1Length = L1.getNumberOfConstraints();
			Integer L2Length = L2.getNumberOfConstraints();
			return L2Length.compareTo(L1Length);
		}
	};

}
