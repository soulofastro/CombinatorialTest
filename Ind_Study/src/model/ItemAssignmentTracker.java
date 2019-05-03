package model;

import java.util.ArrayList;

public class ItemAssignmentTracker extends ItemLocationTreeMaker {
	
	public static ArrayList<Item> itemAssCount = new ArrayList<Item>();
	
	public static void newTracker(ArrayList<Item> items) {
		itemAssCount = new ArrayList<Item>(items);
	}
	
	public static void add(Item item) {
		itemAssCount.add(item);
	}
	
	public static int showCount(Item item) {
		if(!item.getItemName().equals("null item")||!item.getItemName().equals("No item assigned")||item!=null){
			Integer index = itemAssCount.indexOf(item);
			Integer count = itemAssCount.get(index).getNumberOfTimesAssigned();
			return count;
		}
		return 0;
	}
	
	public static void increaseCounts(Item item) {
		if(!item.getItemName().equals("null item")||!item.getItemName().equals("No item assigned")||item!=null){
			Integer index = itemAssCount.indexOf(item);
			Integer count = itemAssCount.get(index).getNumberOfTimesAssigned();
			count += 1;
			itemAssCount.get(index).setNumberOfTimesAssigned(count);
		}
	}

	public static void decreaseCounts(Item itemAssigned) {
		if(!itemAssigned.getItemName().equals("null")||!itemAssigned.getItemName().equals("No item assigned")){
			Integer index = itemAssCount.indexOf(itemAssigned);
			Integer count = itemAssCount.get(index).getNumberOfTimesAssigned();
			count -= 1;
			if(count < 0)
				count = 0;
			itemAssCount.get(index).setNumberOfTimesAssigned(count);
		}
		
	}

	// use this if you want to add an item
	public static boolean checkCount(Item item) {
		if(!item.getItemName().equals("null")||item.getItemName().equals("No item assigned")){
			Integer index = itemAssCount.indexOf(item);
			Integer count = itemAssCount.get(index).getNumberOfTimesAssigned();
			if(count < itemAssCount.get(index).getAssignmentLimit()) {
				return true;
			}
		}
		return false;
	}
	
	// use this if you want to remove an item
	public static boolean atLimit(Item item) {
		if(!item.getItemName().equals("null")||!item.getItemName().equals("No item assigned")){
//			System.out.println("At limit check for "+item.getItemName());
			Integer index = itemAssCount.indexOf(item);
			Integer count = itemAssCount.get(index).getNumberOfTimesAssigned();
			if(count >= itemAssCount.get(index).getAssignmentLimit()) {
				return true;
			}
		}
		return false;
	}

	public static void resetCount(Item oldItem) {
		oldItem.setNumberOfTimesAssigned(0);	
	}
}
