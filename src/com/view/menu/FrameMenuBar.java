package com.view.menu;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.view.util.ViewModules;

@SuppressWarnings("serial")
public class FrameMenuBar extends JMenuBar {
	
	private JMenuItem startItem, stopItem;

	public FrameMenuBar(ActionListener listener) {
		JMenu fileMenu = ViewModules.addMenu(this, "File");
		JMenu mainMenu = ViewModules.addMenu(this, "Captor");
		JMenu setMenu = ViewModules.addMenu(this, "Setting");
		
		ViewModules.addSimpleMenuItem(fileMenu, "Open", "OPEN", listener);
		ViewModules.addSimpleMenuItem(fileMenu, "Save", "SAVE", listener);
		ViewModules.addSimpleMenuItem(fileMenu, "SaveAs", "SAVEAS", listener);
		ViewModules.addSimpleMenuItem(fileMenu, "Delete", "DELETE", listener);
		ViewModules.addSimpleMenuItem(fileMenu, "Exit", "EXIT", listener);
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
