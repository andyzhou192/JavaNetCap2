package com.netcap.view.util;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import com.codegenerator.util.PropertiesUtil;
import com.common.util.ConstsUtil;

public abstract class AbstractSettingView extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AbstractSettingView(){
		this.setBorder(new LineBorder(new Color(255, 200, 0), 2));
		this.setLayout(ViewModules.getGridBagLayout());
		defineComponents();
		layoutComponents();
		initData();
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
	
	protected void storeProperty(String name, Object value){
		PropertiesUtil.storeProperty(ConstsUtil.PROP_FILE, name, String.valueOf(value), "");
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
