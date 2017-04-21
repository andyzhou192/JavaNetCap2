package com.view.mainframe.menu;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

import com.common.Constants;
import com.view.mainframe.MainFrame;
import com.view.util.ViewModules;

@SuppressWarnings("serial")
public class FrameMenuBar extends JMenuBar {
	
	private MainFrame frame;
	private JMenuItem startItem, stopItem;

	public FrameMenuBar(MainFrame frame) {
		this.frame = frame;
		JMenu fileMenu = ViewModules.addMenu(this, "File");
		JMenu editMenu = ViewModules.addMenu(this, "Edit");
		JMenu mainMenu = ViewModules.addMenu(this, "Captor");
		JMenu scriptMenu = ViewModules.addMenu(this, "Script");
		JMenu setMenu = ViewModules.addMenu(this, "Window");
		
		ViewModules.addMenuItem(fileMenu, new ActionForMenu(frame, "Import", "Import", Constants.DATA_IMPORT_ICON));
		ViewModules.addMenuItem(fileMenu, new ActionForMenu(frame, "Export", "Export", Constants.DATA_EXPORT_ICON));
		ViewModules.addMenuItem(fileMenu, new ActionForMenu(frame, "Exit", "Exit", Constants.EXIT_ICON));
		
		ViewModules.addMenuItem(editMenu, new ActionForMenu(frame, "Delete", "Delete", Constants.DELETE_ICON));
		
		this.startItem = ViewModules.addMenuItem(mainMenu, new ActionForMenu(frame, "Start", "Start", Constants.START_NORMAL_ICON));
		this.stopItem = ViewModules.addMenuItem(mainMenu, new ActionForMenu(frame, "Stop", "Stop", Constants.STOP_NORMAL_ICON));
		this.stopItem.setEnabled(false);
		
		ViewModules.addMenuItem(scriptMenu, new ActionForMenu(frame, "OpenScript", "OpenScript", Constants.OPEN_SCRIPT_ICON));
		ViewModules.addMenuItem(scriptMenu, new ActionForMenu(frame, "BatchGeneScript", "BatchGeneScript", Constants.GENE_SCRIPT_ICON));
		ViewModules.addMenuItem(scriptMenu, new ActionForMenu(frame, "CreateScript", "CreateScript", Constants.ADD_SCRIPT_ICON));
		
		ViewModules.addMenuItem(setMenu, new ActionForMenu(frame, "Preference", "Preference", Constants.SETTINGS_ICON));
	}

	/**
	 * 创建工具栏
	 * @return
	 */
	public JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setAutoscrolls(true);
		toolBar.setBackground(new Color(216,218,254));
		//toolBar.setMargin(new Insets(0, 10, 0, 10));
		
		JButton importBtn = ViewModules.addToolButton(toolBar, new ActionForMenu(frame, null, "Import", Constants.DATA_IMPORT_ICON));
		importBtn.setToolTipText("Import Data From File");
		importBtn.setBackground(new Color(216,218,254));
		toolBar.add(importBtn);
		
		JButton exportBtn = ViewModules.addToolButton(toolBar, new ActionForMenu(frame, null, "Export", Constants.DATA_EXPORT_ICON));
		exportBtn.setToolTipText("Import Data To File");
		exportBtn.setBackground(new Color(216,218,254));
		toolBar.add(exportBtn);
		
		JButton startBtn = ViewModules.addToolButton(toolBar, new ActionForMenu(frame, null, "Start", Constants.START_NORMAL_ICON));
		startBtn.setToolTipText("Start Capture");
		startBtn.setBackground(new Color(216,218,254));
		toolBar.add(startBtn);
		
		JButton stopBtn = ViewModules.addToolButton(toolBar, new ActionForMenu(frame, null, "Stop", Constants.STOP_NORMAL_ICON));
		stopBtn.setToolTipText("Stop Capture");
		stopBtn.setBackground(new Color(216,218,254));
		toolBar.add(stopBtn);
		
		JButton delDataBtn = ViewModules.addToolButton(toolBar, new ActionForMenu(frame, null, "Delete", Constants.DELETE_ICON));
		delDataBtn.setToolTipText("Delete Data From Data Table");
		delDataBtn.setBackground(new Color(216,218,254));
		toolBar.add(delDataBtn);
		
		JButton openScriptBtn = ViewModules.addToolButton(toolBar, new ActionForMenu(frame, null, "OpenScript", Constants.OPEN_SCRIPT_ICON));
		openScriptBtn.setToolTipText("Open Script");
		openScriptBtn.setBackground(new Color(216,218,254));
		toolBar.add(openScriptBtn);
		
		JButton batchGeneScriptBtn = ViewModules.addToolButton(toolBar, new ActionForMenu(frame, null, "BatchGeneScript", Constants.GENE_SCRIPT_ICON));
		batchGeneScriptBtn.setToolTipText("Batch Generate Script");
		batchGeneScriptBtn.setBackground(new Color(216,218,254));
		toolBar.add(batchGeneScriptBtn);
		
		JButton createScriptBtn = ViewModules.addToolButton(toolBar, new ActionForMenu(frame, null, "CreateScript", Constants.ADD_SCRIPT_ICON));
		createScriptBtn.setToolTipText("Create new Script");
		createScriptBtn.setBackground(new Color(216,218,254));
		toolBar.add(createScriptBtn);
		
		JButton settingBtn = ViewModules.addToolButton(toolBar, new ActionForMenu(frame, null, "Preference", Constants.SETTINGS_ICON));
		settingBtn.setToolTipText("Preference Settings");
		settingBtn.setBackground(new Color(216,218,254));
		toolBar.add(settingBtn);
		return toolBar;
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
