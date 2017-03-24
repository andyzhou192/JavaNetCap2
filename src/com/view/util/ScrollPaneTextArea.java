package com.view.util;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class ScrollPaneTextArea extends JScrollPane {
	
	private JTextArea textArea;

	public ScrollPaneTextArea(){
		this.textArea = new JTextArea();
		textArea.setEditable(true);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setForeground(Color.BLACK);
		this.setViewportView(textArea);
	}
	
	public ScrollPaneTextArea(int rows){
		this.textArea = new JTextArea();
		textArea.setEditable(true);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setForeground(Color.BLACK);
		textArea.setRows(rows);
		this.setViewportView(textArea);
	}
	
	public void setText(String text){
		this.textArea.setText(text);
	}
	
	public String getText(){
		return this.textArea.getText();
	}
	
	public JTextArea getTextArea() {
		return textArea;
	}
	
	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}
}
