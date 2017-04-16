package com.view.mainframe.menu;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.view.listener.ActionListenerForMenu;
import com.view.mainframe.MainFrame;
import com.view.util.ViewModules;

@SuppressWarnings("serial")
public class FrameMenuBar extends JMenuBar {
	
	private JMenuItem startItem, stopItem;

	public FrameMenuBar(MainFrame frame) {
		JMenu fileMenu = ViewModules.addMenu(this, "File");
		JMenu editMenu = ViewModules.addMenu(this, "Edit");
		JMenu mainMenu = ViewModules.addMenu(this, "Captor");
		JMenu scriptMenu = ViewModules.addMenu(this, "Script");
		JMenu setMenu = ViewModules.addMenu(this, "Window");
		
		ActionListenerForMenu listener = new ActionListenerForMenu(frame);
		ViewModules.addSimpleMenuItem(fileMenu, "Import", "IMPORT", listener);
		ViewModules.addSimpleMenuItem(fileMenu, "Export", "EXPORT", listener);
		ViewModules.addSimpleMenuItem(fileMenu, "Exit", "EXIT", listener);
		
		ViewModules.addSimpleMenuItem(editMenu, "Delete", "DELETE", listener);
		
		this.startItem = ViewModules.addSimpleMenuItem(mainMenu, "Start", "START", listener);
		this.stopItem = ViewModules.addSimpleMenuItem(mainMenu, "Stop", "STOP", listener);
		this.stopItem.setEnabled(false);
		
		ViewModules.addSimpleMenuItem(scriptMenu, "Open Script", "OPENSCRIPT", listener);
		ViewModules.addSimpleMenuItem(scriptMenu, "Batch Gene Script", "BATCHGENESCRIPT", listener);
		ViewModules.addSimpleMenuItem(scriptMenu, "Create Script", "CREATESCRIPT", listener);
		
		ViewModules.addSimpleMenuItem(setMenu, "Preference", "PREFERENCE", listener);
	}


	public JMenuItem getStartItem() {
		return startItem;
	}

	public void setStartItem(JMenuItem startItem) {
		this.startItem = startItem;
	}

	public JMenuItem getStopItem() {
		return stopItem;
	}

	public void setStopItem(JMenuItem stopItem) {
		this.stopItem = stopItem;
	}
}
