package com.view.preference;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import com.view.preference.pane.JcaptureSettingView;
import com.view.preference.pane.ScriptSettingView;
import com.view.preference.pane.WorkspaceSettingView;
import com.view.util.BaseFrame;
import com.view.util.BaseTree;
import com.view.util.FrameWindowAdapter;

@SuppressWarnings("serial")
public class PreferenceFrame extends BaseFrame {

	private BaseTree tree = null;
	private CardLayout card = new CardLayout();
	private JPanel panel = new JPanel(card);
	
	public PreferenceFrame(BaseFrame parent) {
		this.setTitle("Preferences");
		this.setBackground(Color.LIGHT_GRAY);
		this.setBounds(100, 100, 900, 600);
		
		this.panel.add("Workspace", new WorkspaceSettingView(this));
		this.panel.add("Capture", new JcaptureSettingView(this));
		this.panel.add("Java", new ScriptSettingView(this));
		this.card.show(panel, "Workspace");
		this.tree = getNavigateTree();
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.tree, new JScrollPane(panel));
		this.getContentPane().add(splitPane);
		
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new FrameWindowAdapter(parent, this));
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private BaseTree getNavigateTree() {
		BaseTree tree = new BaseTree("Preferences");
		tree.insertNodes("Preferences", "General", "Jcapture", "Script");
		tree.insertNodes("General", "Workspace");
		tree.insertNodes("Jcapture", "Capture");
		tree.insertNodes("Script", "Java");
		tree.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new TreeSelectionListener(){
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) ((JTree)e.getSource()).getLastSelectedPathComponent();
				if (node == null) {
					return;
				}
				String nodeName = node.getUserObject().toString();
				card.show(panel, nodeName);
			}
		});
		tree.setScrollsOnExpand(true);
		// 展开所有子节点
		tree.expandAllTree();
		// 默认选择Workspace节点
		tree.setSelectionPath("Workspace");
		return tree;
	}

}
