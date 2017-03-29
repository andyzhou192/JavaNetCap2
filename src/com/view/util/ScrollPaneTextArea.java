package com.view.util;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class ScrollPaneTextArea extends JScrollPane {
	 
	private LineNumberHeaderView lineNumView = new LineNumberHeaderView();
	private JTextArea textArea;

	public ScrollPaneTextArea(){
		this.textArea = new JTextArea();
		textArea.setEditable(true);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setForeground(Color.BLACK);
		this.setViewportView(textArea);
		this.setRowHeaderView(lineNumView);
	}
	
	public ScrollPaneTextArea(int rows, String content){
		this.textArea = new JTextArea();
		textArea.setEditable(true);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setForeground(Color.BLACK);
		textArea.setRows(rows);
		textArea.setText(content);
		textArea.setFont(new Font(Font.SERIF, Font.PLAIN, 13));
		this.setViewportView(textArea);
		this.setRowHeaderView(lineNumView);
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
