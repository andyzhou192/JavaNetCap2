package com.view.menu;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.view.MainFrame;
import com.view.listener.ActionListenerForMenu;
import com.view.util.ViewModules;

@SuppressWarnings("serial")
public class FrameMenuBar extends JMenuBar {
	
	private JMenuItem startItem, stopItem;

	public FrameMenuBar(MainFrame frame) {
		JMenu fileMenu = ViewModules.addMenu(this, "File");
		JMenu editMenu = ViewModules.addMenu(this, "Edit");
		JMenu mainMenu = ViewModules.addMenu(this, "Captor");
		JMenu setMenu = ViewModules.addMenu(this, "Setting");
		
		ActionListenerForMenu listener = new ActionListenerForMenu(frame);
		ViewModules.addSimpleMenuItem(fileMenu, "Open", "OPEN", listener);
		ViewModules.addSimpleMenuItem(fileMenu, "Save", "SAVE", listener);
		ViewModules.addSimpleMenuItem(fileMenu, "SaveAs", "SAVEAS", listener);
		ViewModules.addSimpleMenuItem(fileMenu, "Exit", "EXIT", listener);
		ViewModules.addSimpleMenuItem(editMenu, "Delete", "DELETE", listener);
		this.startItem = ViewModules.addSimpleMenuItem(mainMenu, "Start", "START", listener);
		this.stopItem = ViewModules.addSimpleMenuItem(mainMenu, "Stop", "STOP", listener);
		this.stopItem.setEnabled(false);
		ViewModules.addSimpleMenuItem(setMenu, "Setting", "SETTING", listener);
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
