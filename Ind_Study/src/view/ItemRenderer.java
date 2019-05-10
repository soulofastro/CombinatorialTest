package view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import model.Item;

@SuppressWarnings("serial")
public class ItemRenderer extends JLabel implements ListCellRenderer<Item> {
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Item> list, Item item, int index, boolean isSelected,
			boolean cellHasFocus) {
		
		JLabel itemName = new JLabel(item.getItemName());
		Color customBackground = new Color(135, 206, 235);
		
        if (isSelected) { 
        	itemName.setOpaque(true);
            itemName.setBackground(customBackground);
            itemName.setForeground(Color.BLACK);
        } 
        else { 
        	itemName.setOpaque(false);
        	itemName.setBackground(list.getBackground());
        	itemName.setForeground(list.getForeground());
        } 
        
		return itemName;
	}

}
