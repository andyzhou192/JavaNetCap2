package com.view.script.generator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import com.view.util.FrameWindowAdapter;
import com.view.util.StatusProgressPanel;

@SuppressWarnings("serial")
public class GeneratorFrame extends JFrame {

	public StatusProgressPanel progress;
	
	public GeneratorFrame(JFrame parent, Map<String, Object> map){
		this.setTitle("Data Detail");
		this.setBackground(Color.LIGHT_GRAY);
		this.setBounds(100, 100, 1000, 600);
		JScrollPane scrollPane = new JScrollPane(new GeneratorPanel(this, map));
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		progress = new StatusProgressPanel();
		this.getContentPane().add(progress, BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new FrameWindowAdapter(parent, this));
	}
	
}
