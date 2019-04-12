package D_tree;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;

@SuppressWarnings("rawtypes")
public class Item implements Comparable{
	
	private String itemName = "null";
	private ArrayList<String> itemProperties; // placeholder until I can think of something better
									 // This is where I will store "professor preferences"
									 // and/or "class requirements"
	private Integer numberOfConstraints = 0;
	//variables minus constraints can't be negative
	//items in file/list must be arranged from most to least constrained. (most to least properties)

	/* Constructors */
	public Item() {
	}
	public Item(String name) {
		setItemName(name);
	}
	
	/* setters and getters */
	public Integer getNumberOfConstraints() {
		return numberOfConstraints;
	}
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public ArrayList<String> getItemProperties() {
		return itemProperties;
	}
	public void setItemProperties(String[] itemProps) {
		ArrayList<String> tempProps = new ArrayList<String>();
		/* This is where I turn ORs into array elements. Example: [A]&[B|C] becomes [A]&[B],[A]&[C] */
		for(int i=0; i < itemProps.length;i++) {
			//if(itemProps[i].indexOf("|") >= 0) {
				tempProps.addAll(propertyExpansion(itemProps[i]));
			//}
		}
		
		/* End */
		//String[] newArray = new String[tempProps.size()+itemProps.length];
		//tempProps.toArray(newArray);
		LinkedHashSet<String> set = new LinkedHashSet<String>(tempProps);
		tempProps.clear();
		tempProps.addAll(set);
		set.clear();
		this.itemProperties = tempProps;
		
		String[] line;
		/* go through the item's properties and count the number of constraints */
		if(itemProperties.size() > 1) {
			if(!itemProperties.get(1).contentEquals("[NA]")) { 				
				ArrayList<String> temp = new ArrayList<String>();	
				for (int i=1;i<itemProperties.size();i++) {				
					if(itemProperties.get(i).indexOf("&") >= 0) {
						numberOfConstraints = numberOfConstraints +1;
						//System.out.println("Inside count loop. Constraint count=" +numberOfConstraints);
					}
					//[x]&[y] contains three constraint properties => [x][y], [x], and [y]	
					line = itemProperties.get(i).split("&");
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
	
	@Override
	public int compareTo(Object arg0) {
		
		int compareLength=((Item)arg0).getNumberOfConstraints();
		if(compareLength > 0)
			return this.numberOfConstraints-compareLength;
		else
			return 0;
	}
	
	public static Comparator<Item> PropComparator = new Comparator<Item>(){
		public int compare(Item item1, Item item2) {
				Integer item1Length = item1.getNumberOfConstraints();
				Integer item2Length = item2.getNumberOfConstraints();
				return item2Length.compareTo(item1Length);
		}
	};
}
