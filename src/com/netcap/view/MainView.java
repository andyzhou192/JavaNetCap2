package com.netcap.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.common.util.ConstsUtil;
import com.netcap.captor.Netcaptor;
import com.netcap.view.util.ViewModules;
import com.protocol.http.bean.HttpDataBean;

@SuppressWarnings("serial")
public class MainView extends JFrame implements ActionListener {

	private JTable table = null;
	private Vector<Vector<Object>> rows = new Vector<Vector<Object>>();

	private final String[] title = {"ID", "URL", "Method", "ReqHeader", "ReqParams", "StatusCode", "ReasonPhrase", "RspHeader", "RspBody", "Operate"};
	
	private static int id = 1;

	public MainView() {
		super();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 600);
		ConstsUtil.initProperties(ConstsUtil.PROP_FILE);
		initMenu();
		initViewer();
		this.setVisible(true);
	}

	private void initViewer() {
		Vector<Object> heads = ViewModules.createVector(title);
		table = TableView.gettable(rows, heads, "DETAIL", this);
		JScrollPane jsp = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.getContentPane().add(jsp, BorderLayout.CENTER);
		
		JLabel statusLabel = new JLabel("zhouyelin@cmhi.chinamobile.com");
		this.getContentPane().add(statusLabel, BorderLayout.SOUTH);
	}

	private void initMenu() {
		JMenuBar jMenuBar = new JMenuBar();
		this.setJMenuBar(jMenuBar);
		JMenu mainMenu = ViewModules.addMenu(jMenuBar, "Captor");
		JMenu setMenu = ViewModules.addMenu(jMenuBar, "Setting");
//		JMenu geneMenu = ViewModules.addMenu(jMenuBar, "Generator");
		ViewModules.addSimpleMenuItem(mainMenu, "Start", "START", this);
		ViewModules.addSimpleMenuItem(mainMenu, "Stop", "STOP", this);
		ViewModules.addSimpleMenuItem(mainMenu, "Exit", "EXIT", this);
		ViewModules.addSimpleMenuItem(setMenu, "Setting", "SETTING", this);
//		ViewModules.addSimpleMenuItem(geneMenu, "Detail", "DETAIL", this);
	}

	public void updateView(HttpDataBean data){
		Object[] values = {id ++, data.getUrl(), data.getMethod(), data.getRequestHeader(), data.getRequestParam().toString(), data.getStatusCode(), data.getReasonPhrase(), data.getResponseHeader(), data.getResponseBody(), "Detail"};
		Vector<Object> r = ViewModules.createVector(values);
		rows.addElement(r);
		table.addNotify();
	}
	
	public void actionPerformed(ActionEvent event) {
		switch(event.getActionCommand()){
		case "START":
			Netcaptor.startCapture(this);
			break;
		case "STOP":
			Netcaptor.stopCapture();
			break;
		case "EXIT":
			System.exit(0);
			break;
		case "SETTING":
			new PreferenceDialog();
			break;
		case "DETAIL":
			DataDetailView.showDialog(TableView.getRowData(table, table.getSelectedRow()));
			break;
		default:
			break;
		}
	}


}