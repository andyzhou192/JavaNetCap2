package com.view.util;

import java.awt.Color;
import java.awt.Font;
import java.io.UnsupportedEncodingException;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.common.util.FormatUtil;
import com.common.util.LogUtil;

@SuppressWarnings("serial")
public class ScrollPaneTextArea extends JScrollPane {
	 
	private LineNumberHeaderView lineNumView = new LineNumberHeaderView();
	private JTextArea textArea;

	public ScrollPaneTextArea(){
		this.textArea = new JTextArea();
		textArea.setEditable(true);
		textArea.setLineWrap(true); // 设置自动换行
		textArea.setWrapStyleWord(true);
		textArea.setForeground(Color.BLACK);
		this.setViewportView(textArea);
		this.setRowHeaderView(lineNumView);
	}
	
	public ScrollPaneTextArea(int rows, String content){
		this.textArea = new JTextArea();
		textArea.setEditable(true);
		textArea.setLineWrap(true); // 设置自动换行
		textArea.setWrapStyleWord(true);
		textArea.setForeground(Color.BLACK);
		textArea.setRows(rows);
		textArea.setText(content);
		textArea.setFont(new Font("Text", Font.PLAIN, 13));
		this.setViewportView(textArea);
		this.setRowHeaderView(lineNumView);
	}
	
	public void showWithFormat(String fromatName){
		switch(fromatName){
		case "JSON":
			this.setText(FormatUtil.formatJson(this.getText()));
			break;
		case "HTML":
			String text = this.getText();
			try {
				text = FormatUtil.formatHtml(text);
			} catch (Exception e1) {
				
			}
			this.setText(text);
			break;
		case "TEXT":
			this.setText(FormatUtil.formatText(this.getText()));
			break;
		default:
			break;
		}
	}
	
	public void setText(String text){
		try {
			text = new String(text.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LogUtil.err(ScrollPaneTextArea.class, e);
		}
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
