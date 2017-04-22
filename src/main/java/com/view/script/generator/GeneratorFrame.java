package com.view.script.generator;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import com.generator.bean.DataForJavaBean;
import com.view.script.generator.pane.GeneratorPanel;
import com.view.util.BaseFrame;
import com.view.util.FrameWindowAdapter;

@SuppressWarnings("serial")
public class GeneratorFrame extends BaseFrame {

	public GeneratorFrame(JFrame parent, DataForJavaBean dataBean){
		this.setTitle("Data Detail");
		this.setBackground(Color.LIGHT_GRAY);
		this.setBounds(100, 100, 1000, 600);
		JPanel pane = new GeneratorPanel(this, dataBean);
		this.getContentPane().add(pane, BorderLayout.CENTER);
		this.getContentPane().add(progress, BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new FrameWindowAdapter(parent, this));
	}
	
}
