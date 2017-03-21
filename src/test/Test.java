package com.test;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Test {
	public static void main(String[] args) throws FileNotFoundException {
//		FileReader fr = new FileReader("requests.txt");
//		System.out.println(fr instanceof Reader);
//		// 这里是多态，父类引用指向子类对象
//		Reader reader = new FileReader("requests.txt");
//		System.out.println(reader instanceof FileReader);
//		System.out.println(reader instanceof Reader);
//		
//		Class c = ArrayList.class;
//		c.isPrimitive(); //判断c是否为基本数据类型
//		System.out.println(c.isPrimitive());
//		c.isAssignableFrom(List.class);  //判断c是否是List类的子类或父类
//		System.out.println(c.isAssignableFrom(List.class));
//		//c.getGenericType(); //得到泛型类型
//		try {
//			Process process = Runtime.getRuntime().exec("cmd mvn -v >> test.txt");
//			System.out.println(process.getOutputStream().toString());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		deleteFolder("D:\\workspace\\JavaNetCap2\\target");
		
	}
	
	public static boolean deleteFolder(String url){  
		java.io.File file=new java.io.File(url);  
	    if(!file.exists()){  
	        return false;  
	    }  
	    if(file.isFile()){  
	        file.delete();  
	        return true;  
	    }else{  
	    	java.io.File[] files=file.listFiles();  
	        for(int i=0;i<files.length;i++){  
	            String root=files[i].getAbsolutePath();//得到子文件或文件夹的绝对路径  
	            //System.out.println(root);  
	            deleteFolder(root);  
	        }  
	        file.delete();  
	        return true;  
	    }  
	}
	      
}
