package com.view.script.editor;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.common.util.FileUtil;
import com.generator.bean.DataForJavaBean;
import com.handler.DataSaveHandler;
import com.view.script.generator.component.GeneratorPanel;
import com.view.util.BaseFrame;
import com.view.util.FrameWindowAdapter;
import com.view.util.ViewDataHandler;

@SuppressWarnings("serial")
public class ScriptEditFrame extends BaseFrame implements TreeSelectionListener {

//	public ScriptEditFrame(BaseFrame parent) {
//		String file = ViewDataHandler.openFile(this);
//		setRootPane(new ScriptEditPane(this, file));
//		this.pack();
//		this.setVisible(true);
//		this.setDefaultCloseOperation(BaseFrame.DO_NOTHING_ON_CLOSE);
//		this.addWindowListener(new FrameWindowAdapter(parent, this));
//	}

	private JTree tree = new JTree();
	private CardLayout card = new CardLayout();
	private JPanel panel;
	private Map<String, DataForJavaBean> dataMapOneSheet = new HashMap<String, DataForJavaBean>();
	
	public ScriptEditFrame(BaseFrame parent) {
		String file = ViewDataHandler.openFile(this);
		Container container = this.getContentPane();
		this.panel = new JPanel(card);
		this.panel.add("JavaCode", new ScriptEditPane(this, file));
		
		String dataFile = file.replace(".java", ".xlsx").replace("java", "resources");
		if(!FileUtil.fileIsExists(dataFile))
			dataFile = dataFile.replace(".xlsx", ".xls");
		List<DataForJavaBean> dataListOneSheet = DataSaveHandler.readExcel(dataFile, 0);
		for(DataForJavaBean dataBean : dataListOneSheet){
			dataMapOneSheet.put(dataBean.getCaseId(), dataBean);
			this.panel.add(dataBean.getCaseId(), new GeneratorPanel(this, dataBean));
		}
		card.show(this.panel, "JavaCode");
		createTree();
		JScrollPane jsp = new JScrollPane(this.panel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		container.add(jsp, BorderLayout.CENTER);
		container.add(tree, BorderLayout.WEST);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(BaseFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new FrameWindowAdapter(parent, this));
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void createTree() {
		tree.setSelectionRow(0);
		tree.setEditable(false); // 不可编辑
		tree.setDragEnabled(false);// 不可拖拉
		tree.setForeground(Color.BLUE);
		tree.setBorder(new LineBorder(Color.ORANGE));

		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Script");
		DefaultTreeModel treeModel = new DefaultTreeModel(root);
		DefaultMutableTreeNode root_code = new DefaultMutableTreeNode("Code");
		DefaultMutableTreeNode root_cases = new DefaultMutableTreeNode("Cases");
		
		treeModel.insertNodeInto(root_code, root, root.getChildCount());
		treeModel.insertNodeInto(root_cases, root, root.getChildCount());
		
		for(String key : dataMapOneSheet.keySet()){
			DefaultMutableTreeNode root_cases_case = new DefaultMutableTreeNode(key);
			treeModel.insertNodeInto(root_cases_case, root_cases, root_cases.getChildCount());
		}

		tree.setModel(treeModel);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(this);
		tree.setScrollsOnExpand(true);
		// 展开所有子节点
		expandTree(tree, new TreePath(root));
		// 默认选择Workspace节点
		tree.setSelectionPath(new TreePath(root_code));
	}

	// public void ecTreeTest(JTree tree) {
	// TreeNode root = (TreeNode) tree.getModel().getRoot();
	// expandTree(tree, new TreePath(root));
	// }

	// 展开所有节点
	private void expandTree(JTree tree, TreePath parent) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandTree(tree, path);
			}
		}
		tree.expandPath(parent);
	}

	// 收起所有节点
	public void collapseTree(JTree tree, TreePath parent) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				collapseTree(tree, path);
			}
		}
		tree.collapsePath(parent);
	}

	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		if (node == null) {
			return;
		}
		String nodeName = node.getUserObject().toString();
		card.show(panel, nodeName);
	}
}
