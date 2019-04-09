package D_tree;
import java.util.Comparator;

@SuppressWarnings("rawtypes")
public class Item implements Comparable{
	
	private String itemName = "null";
	private String[] itemProperties; // placeholder until I can think of something better
									 // This is where I will store "professor preferences"
									 // and/or "class requirements"
	//variables minus constraints can't be negative
	//items in file/list must be arranged from most to least constrained. (most to least properties)

	/* Constructors */
	public Item() {
	}
	public Item(String name) {
		setItemName(name);
	}
	
	/* setters and getters */
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String[] getItemProperties() {
		return itemProperties;
	}
	public void setItemProperties(String[] itemProperites) {
		this.itemProperties = itemProperites;
	}

	@Override
	public int compareTo(Object arg0) {
		int compareLength=((Item)arg0).getItemProperties().length;
		return this.itemProperties.length-compareLength;
	}
	
	public static Comparator<Item> PropComparator = new Comparator<Item>(){
		public int compare(Item item1, Item item2) {
				Integer item1Length = item1.getItemProperties().length;
				Integer item2Length = item2.getItemProperties().length;
				return item2Length.compareTo(item1Length);
		}
	};
}
