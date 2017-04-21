package com.view.script.editor;

import java.awt.CardLayout;
import java.awt.Color;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.generator.bean.DataForJavaBean;
import com.handler.DataSaveHandler;
import com.view.util.BaseFrame;
import com.view.util.BaseTree;
import com.view.util.FrameWindowAdapter;

@SuppressWarnings("serial")
public class ScriptEditFrame extends BaseFrame {

	private BaseTree tree = null;
	private CardLayout card = new CardLayout();
	private JPanel panel;
	private String sourceFile = null;
	private String resourceFile = null;
	private Map<String, DataForJavaBean> dataMapOneSheet = new TreeMap<String, DataForJavaBean>(); // TreeMap有排序
	private ScriptEditFrame frame;
	
	public ScriptEditFrame(BaseFrame parent) {
		this.setTitle("Script");
		this.setBackground(Color.LIGHT_GRAY);
		this.setBounds(100, 100, 900, 600);
		
		this.panel = new JPanel(card);
		this.panel.add("Script", new ScriptBaseInfoPane(this));
		this.card.show(this.panel, "Script");
		this.tree = getNavigateTree();
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tree, new JScrollPane(this.panel));
		this.getContentPane().add(splitPane);
		
		this.frame = this;
		//this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(BaseFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new FrameWindowAdapter(parent, this));
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private BaseTree getNavigateTree() {
		BaseTree tree = new BaseTree("Script");
		tree.insertNodes("Script", "JavaCode", "Cases");
		tree.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new TreeSelectionListener(){
			public void valueChanged(TreeSelectionEvent e) {
				//DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
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
		// 默认选择JavaCode节点
		tree.setSelectionPath("Script");
		tree.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				mouseRightButtonClick(evt);
			}
		});
		return tree;
	}
	
	// 鼠标右键点击事件
	private void mouseRightButtonClick(java.awt.event.MouseEvent evt) {
		// 判断是否为鼠标的BUTTON3按钮，BUTTON3为鼠标右键
		if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
			// 通过点击位置找到点击为表格中的行
			TreePath path = tree.getPathForLocation(evt.getX(), evt.getY()); // 关键是这个方法的使用;
			if(path.getLastPathComponent().toString().equals("Cases")){
				// 弹出菜单
				createPopupMenu(false).show(tree, evt.getX(), evt.getY());
			} else if (path.getParentPath().getLastPathComponent().toString().equals("Cases")) {
				// 弹出菜单
				createPopupMenu(true).show(tree, evt.getX(), evt.getY());
			} else {
				return;
			}
			tree.setSelectionPath(path);
			
		}
	}

	private JPopupMenu createPopupMenu(boolean hasDeleteItem) {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem addMenItem = new JMenuItem("AddCase");
		addMenItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// 该操作需要做的事
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						progress.startProgress("Data is Adding...");
						String caseId = "caseId-" + (tree.searchSingleNode("Cases").getChildCount() + 1);
						tree.insertNodes("Cases", caseId);
						panel.add(caseId, new DataEditPane(frame, caseId, null));
						tree.setSelectionPath(caseId);
						progress.stopProgress("Data has Added!");
					}
				});
			}
		});
		popupMenu.add(addMenItem);

		if(hasDeleteItem){
			JMenuItem delMenItem = new JMenuItem("DeleteCase");
			delMenItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					// 该操作需要做的事
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							String nodeName = ((DefaultMutableTreeNode)tree.getLastSelectedPathComponent()).getUserObject().toString();
							String msg = "Are you sure want to delete this case (" + nodeName + ") ?";
							int result = JOptionPane.showConfirmDialog(frame, msg, "ConfirmDialog", JOptionPane.YES_NO_OPTION);
							if(result == JOptionPane.YES_OPTION) {
								progress.startProgress("Data is Saving...");
								delCaseData(nodeName);
								progress.stopProgress("Data has Deleted!");
							}
						}
					});
				}
			});
			popupMenu.add(delMenItem);
		}
		return popupMenu;
	}
	
	/**
	 * 
	 * @param dataMap
	 */
	public void initSourceFile(Map<String, String> dataMap){
		sourceFile = dataMap.get("source_file");
		resourceFile = dataMap.get("resource_file");
		this.panel.add("JavaCode", new CodeEditPane(this, sourceFile));
		List<DataForJavaBean> dataListOneSheet = DataSaveHandler.readExcel(resourceFile, 0);
		for(DataForJavaBean dataBean : dataListOneSheet){
			dataMapOneSheet.put(dataBean.getCaseId(), dataBean);
			this.panel.add(dataBean.getCaseId(), new DataEditPane(this, dataBean.getCaseId(), dataBean));
		}
		tree.insertNodes("Cases", dataMapOneSheet.keySet());
	}
	
	/**
	 * 
	 * @param caseIds
	 */
	@SuppressWarnings("unchecked")
	public void updateNavigateTree(){
		tree.searchSingleNode("Cases").removeAllChildren();
		tree.insertNodes("Cases", dataMapOneSheet.keySet());
		Enumeration<DefaultMutableTreeNode> enums = tree.searchSingleNode("Cases").children();
		if(enums.hasMoreElements())
			tree.setSelectionPath(enums.nextElement().getUserObject().toString());
		tree.expandAllTree();
	}
	
	/**
	 * 
	 * @param dataBean
	 * @return
	 */
	public boolean updateCaseData(DataForJavaBean dataBean){
		if (null != dataMapOneSheet.get(dataBean.getCaseId())) {
			return DataSaveHandler.updateExcelSingleRowData(resourceFile, 0, dataBean, 0);
		} else {
			dataMapOneSheet.put(dataBean.getCaseId(), dataBean);
			return DataSaveHandler.appendToExcel(resourceFile, 0, dataBean);
		}
	}

	/**
	 * 
	 * @param caseId
	 * @return
	 */
	public boolean delCaseData(String caseId){
		boolean isSucc = true;
		if(null != dataMapOneSheet.get(caseId)){
			isSucc = DataSaveHandler.deleteExcelSingleRowData(resourceFile, 0, caseId, 0);
			dataMapOneSheet.remove(caseId);
		}
		updateNavigateTree();
		return isSucc;
	}
	
}
