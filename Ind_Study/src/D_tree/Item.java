package D_tree;

public class Item {
	
	private String itemName = "";
	private String[] itemProperties; // placeholder until I can think of something better
									 // This is where I will store "professor preferences"
									 // and/or "class requirements"

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
}
