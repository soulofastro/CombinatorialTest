package control;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.CurrentState;
import model.Item;
import view.ItemRenderer;
import view.MainMenu;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class Controller {
	
	private MainMenu view;
	private CurrentState model;
	
	public Controller(MainMenu v, CurrentState m) {
		model = m;
		view = v;
	}
	
	public void initController() {
//		view.initialize(model);
		
		view.itemList.addListSelectionListener(new ListListener());
		view.locationList.addListSelectionListener(new ListListener());
		view.timeSlotList.addListSelectionListener(new ListListener());
		
		view.itemProp.addListSelectionListener(new ListListener());
		view.locCrit.addListSelectionListener(new ListListener());
		view.tsListCrit.addListSelectionListener(new ListListener());
	}
	
    private class ListListener implements ListSelectionListener {
    	
    	public void valueChanged(ListSelectionEvent e) {
    		JList source = (JList)(e.getSource());
    		
    		if(source == view.itemList) {
    			int itemIndex = view.itemList.getSelectedIndex();
    			ArrayList<String> props = view.itemListModel.get(itemIndex).getItemProperties();
    			view.itemPropsModel = new DefaultListModel<>();
    			
    			for(String prop: props) {
    				view.itemPropsModel.addElement(prop);
    			}
    			view.itemProp = new JList(view.itemPropsModel);
    			view.itemProp.setSelectionBackground(new Color(135, 206, 235));
    			view.itemProp.setMinimumSize(new Dimension(100, 0));
    			view.itemProp.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    			view.itemProp.setVisibleRowCount(20);
    			view.scrollPane_itemProp.setViewportView(view.itemProp);
    			
    		}
    		else if(source == view.locationList) {
    			int itemIndex = view.locationList.getSelectedIndex();
    			ArrayList<String> props = view.locationListModel.get(itemIndex).getLocationCriteria();
    			view.locCritModel = new DefaultListModel<>();
    			
    			for(String prop: props) {
    				view.locCritModel.addElement(prop);
    			}
    			view.locCrit = new JList(view.locCritModel);
    			view.locCrit.setSelectionBackground(new Color(135, 206, 235));
    			view.locCrit.setMinimumSize(new Dimension(100, 0));
    			view.locCrit.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    			view.locCrit.setVisibleRowCount(20);
    			view.scrollPane_locCrit.setViewportView(view.locCrit);
    			
    		}
    		else if(source == view.timeSlotList) {
    			int itemIndex = view.timeSlotList.getSelectedIndex();
    			ArrayList<String> props = view.timeListModel.get(itemIndex).getLocationCriteria();
    			view.tsCritModel = new DefaultListModel<>();
    			
    			for(String prop: props) {
    				view.tsCritModel.addElement(prop);
    			}
    			view.tsListCrit = new JList(view.tsCritModel);
    			view.tsListCrit.setSelectionBackground(new Color(135, 206, 235));
    			view.tsListCrit.setMinimumSize(new Dimension(100, 0));
    			view.tsListCrit.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    			view.tsListCrit.setVisibleRowCount(20);
    			view.scrollPane_tsCrit.setViewportView(view.tsListCrit);
    		}
    	}
    		
    }
    
}
