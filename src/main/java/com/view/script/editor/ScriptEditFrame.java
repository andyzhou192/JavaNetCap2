package com.view.script.editor;

import javax.swing.JFrame;

import com.view.util.FrameWindowAdapter;
import com.view.util.ViewDataHandler;

@SuppressWarnings("serial")
public class ScriptEditFrame extends JFrame {

	public ScriptEditFrame(JFrame parent) {
		String file = ViewDataHandler.openFile(this);
		setRootPane(new ScriptEditPane(this, file));
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new FrameWindowAdapter(parent, this));
	}

}
