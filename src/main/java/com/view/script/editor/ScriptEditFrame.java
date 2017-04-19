package com.view.script.editor;

import java.awt.CardLayout;
import java.util.Collection;
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
import com.view.script.generator.component.GeneratorPanel;
import com.view.util.BaseFrame;
import com.view.util.BaseTree;
import com.view.util.FrameWindowAdapter;

@SuppressWarnings("serial")
public class ScriptEditFrame extends BaseFrame {

	private BaseTree tree = null;
	private CardLayout card = new CardLayout();
	private JPanel panel;
	
	public ScriptEditFrame(BaseFrame parent) {
		this.panel = new JPanel(card);
		this.panel.add("Script", new ScriptBaseInfoPane(this));
		
		card.show(this.panel, "Script");
		tree = getTree();
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tree, new JScrollPane(this.panel));
		this.getContentPane().add(splitPane);
		
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(BaseFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new FrameWindowAdapter(parent, this));
	}
	
	public void updateData(Map<String, String> dataMap){
//		String file = ViewDataHandler.openFile(this);
		String sourceFile = dataMap.get("source_file");
		String resourceFile = dataMap.get("resource_file");
		this.panel.add("JavaCode", new CodeEditPane(this, sourceFile));
		
//		String dataFile = file.replace(".java", ".xlsx").replace("java", "resources");
//		if(!FileUtil.fileIsExists(dataFile))
//			dataFile = dataFile.replace(".xlsx", ".xls");
		Map<String, DataForJavaBean> dataMapOneSheet = new TreeMap<String, DataForJavaBean>(); // TreeMap有排序
		List<DataForJavaBean> dataListOneSheet = DataSaveHandler.readExcel(resourceFile, 0);
		for(DataForJavaBean dataBean : dataListOneSheet){
			dataMapOneSheet.put(dataBean.getCaseId(), dataBean);
			this.panel.add(dataBean.getCaseId(), new GeneratorPanel(this, dataBean));
		}
		updateTree(dataMapOneSheet.keySet());
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private BaseTree getTree() {
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
		tree.setSelectionPath("JavaCode");
		return tree;
	}
	
	public void updateTree(Collection<String> caseIds){
		tree.insertNodes("Cases", caseIds);
	}

}
