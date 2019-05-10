package view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import model.Location;

@SuppressWarnings("serial")
public class LocationRenderer extends JLabel implements ListCellRenderer<Location> {

	@Override
	public Component getListCellRendererComponent(JList<? extends Location> list, Location location, int index, boolean isSelected, boolean cellHasFocus) {
		
		JLabel itemName = new JLabel(location.getLocationName());
		Color customBackground = new Color(135, 206, 235);
		
        if (isSelected) { 
//            setBackground(Color.BLUE); 
//            setForeground(Color.BLUE); 
        	itemName.setOpaque(true);
            itemName.setBackground(customBackground);
            itemName.setForeground(Color.BLACK);
        } 
        else { 
        	itemName.setOpaque(false);
        	itemName.setBackground(list.getBackground());
        	itemName.setForeground(list.getForeground());
//            setBackground(list.getBackground()); 
//            setForeground(list.getForeground()); 
        } 
        
		return itemName;
	}

}