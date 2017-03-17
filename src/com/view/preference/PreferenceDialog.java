package com.view.preference;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import com.view.preference.component.JcaptureSettingView;
import com.view.preference.component.ScriptSettingView;
import com.view.preference.component.WorkspaceSettingView;

@SuppressWarnings("serial")
public class PreferenceDialog extends JDialog implements TreeSelectionListener {
	
	private JTree tree = new JTree();
	private CardLayout card = new CardLayout();
	private JPanel panel;
	
	public PreferenceDialog(){
		this.setBackground(Color.LIGHT_GRAY);
		this.setTitle("Preferences");
		this.setBounds(100, 100, 900, 600);
		this.setVisible(true);
		Container container = this.getContentPane();
		panel = new JPanel(card);
		panel.add("WorkspaceSetting", new WorkspaceSettingView());
		panel.add("CaptureSetting", new JcaptureSettingView());
		panel.add("JavaScriptSetting", new ScriptSettingView());
		card.show(panel, "WorkspaceSetting");
		createTree();
		JScrollPane jsp = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		container.add(jsp, BorderLayout.CENTER);
		container.add(tree, BorderLayout.WEST);
		this.pack();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void createTree() {
		tree.setSelectionRow(0);
		tree.setEditable(false); // 不可编辑
		tree.setDragEnabled(false);// 不可拖拉
		tree.setForeground(Color.BLUE);
		tree.setToolTipText("Preferences");
		tree.setBorder(new LineBorder(Color.ORANGE));
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Preferences");
		
		DefaultMutableTreeNode root_general = new DefaultMutableTreeNode("General");
		DefaultMutableTreeNode root_general_workspace = new DefaultMutableTreeNode("Workspace");
		
		DefaultMutableTreeNode root_jcapture = new DefaultMutableTreeNode("Jcapture Setting");
		DefaultMutableTreeNode root_jcapture_Ethernet = new DefaultMutableTreeNode("Capture");
		
		DefaultMutableTreeNode root_script = new DefaultMutableTreeNode("Script Setting");
		DefaultMutableTreeNode root_script_java = new DefaultMutableTreeNode("Java");

		DefaultTreeModel treeModel = new DefaultTreeModel(root);
		treeModel.insertNodeInto(root_general, root, root.getChildCount());
		treeModel.insertNodeInto(root_jcapture, root, root.getChildCount());
		treeModel.insertNodeInto(root_script, root, root.getChildCount());
		treeModel.insertNodeInto(root_general_workspace, root_general, root_general.getChildCount());
		treeModel.insertNodeInto(root_jcapture_Ethernet, root_jcapture, root_jcapture.getChildCount());
		treeModel.insertNodeInto(root_script_java, root_script, root_script.getChildCount());
		
		tree.setModel(treeModel);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(this);
		tree.setScrollsOnExpand(true);
		// 展开根节点下的第一个子节点
		tree.expandRow(1);
		tree.setSelectionRow(2);
	}

	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node == null){
        	return;
        }
        switch (node.getUserObject().toString()){
        case "Workspace":
        	card.show(panel, "WorkspaceSetting");
        	break;
        case "Capture":
        	card.show(panel, "CaptureSetting");
        	break;
        case "Java":
        	card.show(panel, "JavaScriptSetting");
        	break;
        default:
        	break;
        }
	}
	
//	public static void main(String[] args) {
//		PreferenceDialog dialog = new PreferenceDialog();	
//		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//	}
}