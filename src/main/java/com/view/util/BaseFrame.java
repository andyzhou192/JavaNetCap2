package com.view.util;

import java.awt.BorderLayout;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class BaseFrame extends JFrame {

	public StatusProgressPanel progress = new StatusProgressPanel();
	
	public BaseFrame(){
		this.getContentPane().add(progress, BorderLayout.SOUTH);
	}
	
}
