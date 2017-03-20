package com.view.preference;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.common.Constants;
import com.common.util.PropertiesUtil;
import com.view.util.ViewModules;

@SuppressWarnings("serial")
public abstract class AbstractPreferencesView extends JPanel implements ActionListener {
	
	public AbstractPreferencesView(int rowNum, int columnNum){
		this.setBorder(new LineBorder(new Color(255, 200, 0), 2));
		this.setLayout(ViewModules.getGridBagLayout(rowNum, columnNum, 5, 5, 1.0, 1.0));
	}
	
	/**
	 * 定义组件
	 */
	public abstract void defineComponents();
	
	/**
	 * 对界面进行布局
	 */
	public abstract void layoutComponents();
	
	/**
	 * 初始化界面数据
	 */
	public abstract void initData();

	/**
	 * 保存配置信息
	 */
	public abstract void saveSettings();
	
	protected boolean storeProperty(String name, Object value){
		return PropertiesUtil.storeProperty(Constants.DEF_SET_PROP_FILE, name, String.valueOf(value), "");
	}
	
	/**
	 * 递归获取指定路径下的所有文件
	 * @param path
	 * @return
	 */
	public String[] traverseFolder(String path) {
		String[] files;
		List<String> fileList = new ArrayList<String>();
        File folder = new File(path);
        if (folder.exists()) {
            File[] fileNames = folder.listFiles();
            if (null != fileNames && fileNames.length != 0) {
                for (File file : fileNames) {
                    if (file.isDirectory()) {
                    	String[] tempFiles = traverseFolder(file.getAbsolutePath());
                    	for(String str : tempFiles)
                    		fileList.add(str);
                    } else {
                    	fileList.add(file.getAbsolutePath());
                    }
                }
            }
        }
        files = (String[]) fileList.toArray(new String[]{""});
		return files;
    }
}
