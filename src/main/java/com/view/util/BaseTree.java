package com.view.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

@SuppressWarnings("serial")
public class BaseTree extends JTree {

	private DefaultTreeModel treeModel = null;
	private DefaultMutableTreeNode root = null;

	public BaseTree(String rootNodeName) {
		this.setSelectionRow(0);
		this.setEditable(false); // 不可编辑
		this.setDragEnabled(false);// 不可拖拉
		this.setForeground(Color.BLUE);
		this.setBorder(new LineBorder(Color.ORANGE));

		this.root = new DefaultMutableTreeNode(rootNodeName);
		this.treeModel = new DefaultTreeModel(root);
		this.setModel(treeModel);
	}
	
	/**
	 * 
	 * @param mode @see TreeSelectionModel
	 */
	public void setSelectionMode(int mode){
		this.getSelectionModel().setSelectionMode(mode);
	}
	
	/**
	 * 
	 * @param nodeName
	 */
	public void setSelectionPath(String nodeName){
		this.setSelectionPath(new TreePath(this.searchSingleNode(nodeName)));
	}
	
	/**
	 * 
	 * @param newNodeName
	 * @param parentNodeName
	 */
	public void insertNodes(String parentNodeName, String...newNodeNames){
		if(null == newNodeNames) return;
		for(String newNodeName : newNodeNames){
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newNodeName);
			DefaultMutableTreeNode parentNode = root;
			if(null != parentNodeName){
				parentNode = this.searchSingleNode(parentNodeName);
			}
			treeModel.insertNodeInto(newNode, parentNode, parentNode.getChildCount());
		}
	}
	
	/**
	 * 
	 * @param parentNodeName
	 * @param newNodeNames
	 */
	public void insertNodes(String parentNodeName, Collection<String> newNodeNames){
		if(null == newNodeNames) return;
		for(String newNodeName : newNodeNames){
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newNodeName);
			DefaultMutableTreeNode parentNode = root;
			if(null != parentNodeName){
				parentNode = this.searchSingleNode(parentNodeName);
			}
			treeModel.insertNodeInto(newNode, parentNode, parentNode.getChildCount());
		}
	}
	
	/**
	 * 
	 * @param newNodeName
	 * @param parentNodeName
	 * @param parentIndex 父节点有多个时，所需父节点在这个父节点数组中的索引
	 */
	public void insertNodes(String parentNodeName, int parentIndex, String...newNodeNames){
		if(null == newNodeNames) return;
		for(String newNodeName : newNodeNames){
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(newNodeName);
			DefaultMutableTreeNode parentNode = this.searchNodes(parentNodeName).get(parentIndex);
			treeModel.insertNodeInto(newNode, parentNode, parentNode.getChildCount());
		}
	}
	
	/**
	 * 根据节点名称搜索节点
	 * @param nodeStr
	 * @return
	 */
	public DefaultMutableTreeNode searchSingleNode(String nodeStr) {
		DefaultMutableTreeNode node = null;
		Enumeration<?> e = root.breadthFirstEnumeration();
		while (e.hasMoreElements()) {
			node = (DefaultMutableTreeNode) e.nextElement();
			if (nodeStr.equals(node.getUserObject().toString())) {
				return node;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param nodeStr
	 * @return
	 */
	public List<DefaultMutableTreeNode> searchNodes(String nodeStr) {
		List<DefaultMutableTreeNode> nodeList = new ArrayList<DefaultMutableTreeNode>();
		DefaultMutableTreeNode node = null;
		Enumeration<?> e = root.breadthFirstEnumeration();
		while (e.hasMoreElements()) {
			node = (DefaultMutableTreeNode) e.nextElement();
			if (nodeStr.equals(node.getUserObject().toString())) {
				nodeList.add(node);
			}
		}
		return nodeList;
	}

	/**
	 * 删除指定节点
	 * @param selNode
	 */
	public void removeNode(DefaultMutableTreeNode selNode) {
		if (selNode == null) {
			return;
		}
		MutableTreeNode parent = (MutableTreeNode) (selNode.getParent());
		if (parent == null) {
			return;
		}
		MutableTreeNode toBeSelNode = getSibling(selNode);
		if (toBeSelNode == null) {
			toBeSelNode = parent;
		}
		TreeNode[] nodes = treeModel.getPathToRoot(toBeSelNode);
		TreePath path = new TreePath(nodes);
		this.scrollPathToVisible(path);
		this.setSelectionPath(path);
		treeModel.removeNodeFromParent(selNode);
	}

	/**
	 * 获取指定节点的兄弟节点
	 * @param selNode
	 * @return
	 */
	private MutableTreeNode getSibling(DefaultMutableTreeNode selNode) {
		MutableTreeNode sibling = (MutableTreeNode) selNode
				.getPreviousSibling();
		if (sibling == null) {
			sibling = (MutableTreeNode) selNode.getNextSibling();
		}
		return sibling;
	}

	/**
	 * 
	 */
	public void expandAllTree(){
		this.expandTree(new TreePath(root));
	}
	
	/**
	 * 展开所有节点
	 * @param nodeNames 各节点名称路径,以"/"分隔，如：/a/b/c
	 */
	public void expandTree(String nodePath){
		if(nodePath.startsWith("/"))
			nodePath = nodePath.substring(1);
		TreePath parent = new TreePath(nodePath.split("/"));
		this.expandTree(parent);
	}
	
	/**
	 *  展开所有节点
	 * @param parent
	 */
	public void expandTree(TreePath parent) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandTree(path);
			}
		}
		this.expandPath(parent);
	}
	
	/**
	 * 
	 */
	public void collapseAllTree(){
		this.collapseTree(new TreePath(root));
	}

	/**
	 * 收起所有节点
	 * @param nodeNames 各节点名称路径,以"/"分隔，如：/a/b/c
	 */
	public void collapseTree(String nodePath){
		if(nodePath.startsWith("/"))
			nodePath = nodePath.substring(1);
		TreePath parent = new TreePath(nodePath.split("/"));
		this.collapseTree(parent);
	}
	
	/**
	 *  收起所有节点
	 * @param parent
	 */
	public void collapseTree(TreePath parent) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				collapseTree(path);
			}
		}
		this.collapsePath(parent);
	}
}
