package com.view.script.editor;

import java.awt.CardLayout;
import java.awt.Color;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
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
		return tree;
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
			this.panel.add(dataBean.getCaseId(), new DataEditPane(this, dataBean));
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
		return DataSaveHandler.updateExcelSingleRowData(resourceFile, 0, dataBean, 0);
	}

	/**
	 * 
	 * @param caseId
	 * @return
	 */
	public boolean delCaseData(String caseId){
		boolean isSucc = DataSaveHandler.deleteExcelSingleRowData(resourceFile, 0, caseId, 0);
		dataMapOneSheet.remove(caseId);
		updateNavigateTree();
		return isSucc;
	}
	
}
