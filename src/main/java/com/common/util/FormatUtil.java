package com.common.util;

import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.HTMLWriter;
import org.dom4j.io.OutputFormat;

/**
 * 格式化输入工具类
 * 
 * @author zhouyelin
 * @date 2017-3-12
 */
public final class FormatUtil {
	
	public static String formatText(String text) {
		if (null == text || "".equals(text))
			return "";
		 String dest = "";
		 Pattern p = Pattern.compile("\\t|\r|\n"); //"\\s*|\t|\r|\n"
		 Matcher m = p.matcher(text);
		 dest = m.replaceAll("");
		  return dest;
	}

	/**
	 * 格式化
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static String formatJson(String jsonStr) {
		if (null == jsonStr || "".equals(jsonStr))
			return "";
		StringBuilder sb = new StringBuilder();
		char last = '\0';
		char current = '\0';
		int indent = 0;
		for (int i = 0; i < jsonStr.length(); i++) {
			last = current;
			current = jsonStr.charAt(i);
			switch (current) {
			case '{':
			case '[':
				sb.append(current);
				sb.append('\n');
				indent++;
				addIndentBlank(sb, indent);
				break;
			case '}':
			case ']':
				sb.append('\n');
				indent--;
				addIndentBlank(sb, indent);
				sb.append(current);
				break;
			case ',':
				sb.append(current);
				if (last != '\\') {
					sb.append('\n');
					addIndentBlank(sb, indent);
				}
				break;
			default:
				sb.append(current);
			}
		}

		return sb.toString();
	}

	/**
	 * 添加space
	 * 
	 * @param sb
	 * @param indent
	 */
	private static void addIndentBlank(StringBuilder sb, int indent) {
		for (int i = 0; i < indent; i++) {
			sb.append('\t');
		}
	}
	
	/** 
	 * html 必须是格式良好的 ,依赖org.dom4j
	 * @param str 
	 * @return 
	 * @throws Exception 
	 */  
	public static String formatHtml(String str) throws Exception {  
	    Document document = null;  
	    document = DocumentHelper.parseText(str);  
	  
	    OutputFormat format = OutputFormat.createPrettyPrint();  
	    format.setEncoding("utf-8");  
	    StringWriter writer = new StringWriter();  
	  
	    HTMLWriter htmlWriter = new HTMLWriter(writer, format);  
	  
	    htmlWriter.write(document);  
	    htmlWriter.close();  
	    return writer.toString();  
	}  
	
//	public static void main(String[] args) throws Exception {
////		String jsonStr = "{\"content\":\"this is the msg content.\",\"tousers\":\"user1|user2\",\"msgtype\":\"texturl\",\"appkey\":\"test\",\"domain\":\"test\","
////				+ "\"system\":{\"wechat\":{\"safe\":\"1\"}},\"texturl\":{\"urltype\":\"0\",\"user1\":{\"spStatus\":\"user01\",\"workid\":\"work01\"},\"user2\":{\"spStatus\":\"user02\",\"workid\":\"work02\"}}}";
////		System.out.println(FormatUtil.formatJson(jsonStr));
//		
//		String htmlStr = "<!DOCTYPE html><html><head><meta name=\"test\"/><title>ceshi</title></head><body link=\"#0000cc\"><div id=\"div\" class=\"nihao\">naskjf</div></body></html>";
//		System.out.println(FormatUtil.formatHtml(htmlStr));
//	}
}
