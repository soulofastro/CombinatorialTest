package view;

import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.List;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JMenu;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import model.Item;
import model.Location;
import model.CurrentState;
import model.DLDecision;
import model.ILDecision;

import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class MainMenu {

	private JFrame frame;
	
	public JPanel Professors;
	public JPanel Classes;
	public JPanel itemInfoPanel;
	public JPanel locationInfoPanel;
	public JPanel timeSlots;
	public JPanel timeSlotInfoPanel;
	public JPanel Schedule;
	
	public JList itemList;
	public JList itemProp;
	public JList locationList;
	public JList locCrit;
	public JList timeSlotList;
	public JList tsListCrit;
	
	public DefaultListModel<Item> itemListModel;
	public DefaultListModel<String> itemPropsModel;
	public DefaultListModel<Location> locationListModel;
	public DefaultListModel<String> locCritModel;
	public DefaultListModel<Location> timeListModel;
	public DefaultListModel<String> tsCritModel;
	
	public DefaultTableModel DLDecisions;
	
	public JScrollPane scrollPane_item;
	public JScrollPane scrollPane_itemProp;
	public JScrollPane scrollPane_locList;
	public JScrollPane scrollPane_locCrit;
	public JScrollPane scrollPane_tsList;
	public JScrollPane scrollPane_tsCrit;
	
	private JTable scheduleTable;
	/**
	 * Create the application.
	 */
	public MainMenu(CurrentState m) {
		initialize(m);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize(CurrentState m) {
		frame = new JFrame();
		frame.setMinimumSize(new Dimension(500, 550));
		frame.setBounds(100, 100, 600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane);
		
		Professors = new JPanel();
		tabbedPane.addTab("Professors", null, Professors, null);
		Professors.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JSplitPane splitPane = new JSplitPane();
		Professors.add(splitPane, "2, 2, fill, fill");
		
		scrollPane_item = new JScrollPane();
		scrollPane_item.setMinimumSize(new Dimension(100, 25));
		splitPane.setLeftComponent(scrollPane_item);
		
		/*** ITEM MANIPULATION ***/
		itemListModel = new DefaultListModel<>();
		for(Item item: m.getItems()) {
			itemListModel.addElement(item);
		}
		itemList = new JList(itemListModel);
		itemList.setSelectionBackground(new Color(135, 206, 235));
		itemList.setMinimumSize(new Dimension(100, 0));
		itemList.setCellRenderer(new ItemRenderer());
		itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		itemList.setVisibleRowCount(20);
		scrollPane_item.setViewportView(itemList);
		
		itemInfoPanel = new JPanel();
		splitPane.setRightComponent(itemInfoPanel);
		itemInfoPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		scrollPane_itemProp = new JScrollPane();
		itemInfoPanel.add(scrollPane_itemProp, "2, 2, 7, 1, fill, fill");
		
		itemProp = new JList();
		scrollPane_itemProp.setViewportView(itemProp);
		
		JButton btnAdd = new JButton("Add");
		itemInfoPanel.add(btnAdd, "4, 4");
		
		JButton btnEdit = new JButton("Edit");
		itemInfoPanel.add(btnEdit, "6, 4");
		
		JButton btnRemove = new JButton("Remove");
		itemInfoPanel.add(btnRemove, "8, 4");
		
		Classes = new JPanel();
		tabbedPane.addTab("Classes", null, Classes, null);
		Classes.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JSplitPane splitPane_1 = new JSplitPane();
		Classes.add(splitPane_1, "2, 2, fill, fill");
		
		scrollPane_locList = new JScrollPane();
		scrollPane_locList.setMinimumSize(new Dimension(100, 25));
		splitPane_1.setLeftComponent(scrollPane_locList);
		
		locationListModel = new DefaultListModel<>();
		for(Location loc: m.getLocations()) {
			locationListModel.addElement(loc);
		}
		locationList = new JList(locationListModel);
		locationList.setMinimumSize(new Dimension(50, 0));
		locationList.setCellRenderer(new LocationRenderer());
		locationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_locList.setViewportView(locationList);
		
		locationInfoPanel = new JPanel();
		splitPane_1.setRightComponent(locationInfoPanel);
		locationInfoPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		scrollPane_locCrit = new JScrollPane();
		locationInfoPanel.add(scrollPane_locCrit, "2, 2, 7, 1, fill, fill");
		
		locCrit = new JList();
		scrollPane_locCrit.setViewportView(locCrit);
		
		JButton btnAdd_1 = new JButton("Add");
		locationInfoPanel.add(btnAdd_1, "4, 4");
		
		JButton btnEdit_1 = new JButton("Edit");
		locationInfoPanel.add(btnEdit_1, "6, 4");
		
		JButton btnRemove_1 = new JButton("Remove");
		locationInfoPanel.add(btnRemove_1, "8, 4");
		
		timeSlots = new JPanel();
		tabbedPane.addTab("Time Slots", null, timeSlots, null);
		timeSlots.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JSplitPane splitPane_2 = new JSplitPane();
		timeSlots.add(splitPane_2, "2, 2, fill, fill");
		
		scrollPane_tsList = new JScrollPane();
		scrollPane_tsList.setMinimumSize(new Dimension(100, 25));
		splitPane_2.setLeftComponent(scrollPane_tsList);
		
		timeListModel = new DefaultListModel<>();
		for(Location loc: m.getTimeSlots()) {
			timeListModel.addElement(loc);
		}
		timeSlotList = new JList(timeListModel);
		timeSlotList.setMinimumSize(new Dimension(50, 0));
		timeSlotList.setCellRenderer(new LocationRenderer());
		timeSlotList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_tsList.setViewportView(timeSlotList);
		
		timeSlotInfoPanel = new JPanel();
		splitPane_2.setRightComponent(timeSlotInfoPanel);
		timeSlotInfoPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		scrollPane_tsCrit = new JScrollPane();
		timeSlotInfoPanel.add(scrollPane_tsCrit, "2, 2, 7, 1, fill, fill");
		
		tsListCrit = new JList();
		scrollPane_tsCrit.setViewportView(tsListCrit);
		
		JButton btnAdd_2 = new JButton("Add");
		timeSlotInfoPanel.add(btnAdd_2, "4, 4");
		
		JButton btnEdit_2 = new JButton("Edit");
		timeSlotInfoPanel.add(btnEdit_2, "6, 4");
		
		JButton btnRemove_2 = new JButton("Remove");
		timeSlotInfoPanel.add(btnRemove_2, "8, 4");
		
		Schedule = new JPanel();
		tabbedPane.addTab("Schedule", null, Schedule, null);
		SpringLayout sl_Schedule = new SpringLayout();
		Schedule.setLayout(sl_Schedule);
		
		String col[] = {"Time Slot", "Assignment 1","Assignment 2","Assignment 3","Assignment 4"};
		DLDecisions = new DefaultTableModel(col,0);
		scheduleTable = new JTable(DLDecisions);
		scheduleTable.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		scheduleTable.setDragEnabled(true);
		for(DLDecision dec: m.getDLtree()) {
			ArrayList<String> temp = new ArrayList<String>();
			String name = dec.getDecisionName();
			temp.add(name);
			for(ILDecision ild : dec.getDecisionsAssigned()) {
				temp.add(ild.getDecisionName());
			}
			Object[] data = temp.toArray(new String[0]);
			DLDecisions.addRow(data);
		}
		scheduleTable.setCellSelectionEnabled(true);
		sl_Schedule.putConstraint(SpringLayout.NORTH, scheduleTable, 10, SpringLayout.NORTH, Schedule);
		sl_Schedule.putConstraint(SpringLayout.WEST, scheduleTable, 10, SpringLayout.WEST, Schedule);
		sl_Schedule.putConstraint(SpringLayout.SOUTH, scheduleTable, 460, SpringLayout.NORTH, Schedule);
		sl_Schedule.putConstraint(SpringLayout.EAST, scheduleTable, 410, SpringLayout.WEST, Schedule);
		Schedule.add(scheduleTable);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmLoadData = new JMenuItem("Load Data");
		mnFile.add(mntmLoadData);
		
		JMenuItem mntmSaveData = new JMenuItem("Save Data");
		mnFile.add(mntmSaveData);
		
		frame.pack(); //TODO
		frame.setVisible(true);
	}
}
