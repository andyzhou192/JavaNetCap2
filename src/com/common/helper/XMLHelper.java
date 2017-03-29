package com.common.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.common.util.LogUtil;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;
import javax.xml.xpath.*;

public class XMLHelper {
	private Class<?> cl = XMLHelper.class;
	
	private DocumentBuilderFactory factory;
	private Element root;
	private Document xmldoc;
	private File sourceFile;

	public XMLHelper(){
		this.factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
	}
	
	public XMLHelper(File sourceFile){
		this.sourceFile = sourceFile;
		this.factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		initDocument(sourceFile);
	}
	
	public void initDocument(File file){
		try {
			if(file.exists()){
				DocumentBuilder db = factory.newDocumentBuilder();
				xmldoc = db.parse(file);
				root = xmldoc.getDocumentElement();
			} else {
				LogUtil.err(cl, "file is not exist:" + sourceFile);
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param tagName
	 * @param attributes
	 * @param textContent
	 * @return
	 */
	public Element createElement(String tagName, Map<String, String> attributes, String textContent){
		Element ele = xmldoc.createElement(tagName);
		if(null != attributes){
			for(String name : attributes.keySet()){
				ele.setAttribute(name, attributes.get(name));
			}
		}
		if(null != textContent){
			ele.setTextContent(textContent);
		}
		return ele;
	}
	
	public Element createElementWithoutAttribute(String tagName, String textContent){
		return createElement(tagName, null, textContent);
	}
	
	/**
	 * 
	 * @param express The XPath expression(eg. "/books/book[price<10]", "/books/book[@id='B02']")
	 */
	public Node appendChild(String parentExpress, String tagName, Map<String, String> attributes, String content){
		Node praentNode = findSingleNode(parentExpress, root);
		return praentNode.appendChild(createElement(tagName, attributes, content));
	}
	
	public Node appendChild(String parentExpress, Node child){
		Node praentNode = findSingleNode(parentExpress, root);
		return praentNode.appendChild(child);
	}
	
	/**
	 * 
	 * @param express The XPath expression(eg. "/books/book[price<10]", "/books/book[@id='B02']")
	 */
	public Node appendChild(Node parentNode, String tagName, Map<String, String> attributes, String content){
		return parentNode.appendChild(createElement(tagName, attributes, content));
	}
	
	/**
	 * 
	 * @param parent
	 * @param childern
	 * @return
	 */
	public Node appendChildern(Node parent, Node...childern){
		if(null != childern && null != parent){
			for(Node child : childern)
				parent.appendChild(child);
		}
		return parent;
	}
	
	/**
	 * 
	 * @param childern
	 * @return
	 */
	public Node appendChildernToRoot(Node...childern){
		return appendChildern(root, childern);
	}
	
	/**
	 * 
	 * @param parent
	 * @param childern
	 * @return
	 */
	public Element removeChildern(Element parent, Element...childern){
		if(null != childern && null != parent){
			for(Element child : childern)
				parent.removeChild(child);
		}
		return parent;
	}
	
	/**
	 * 
	 * @param parentElement
	 * @param names
	 * @return
	 */
	public Element removeAttributes(Element parentElement, String...names){
		if(null != names && null != parentElement){
			for(String name : names)
				parentElement.removeAttribute(name);
		}
		return parentElement;
	}
	
	/**
	 * 
	 * @param parentNode
	 * @param childNodes
	 * @return
	 */
	public Node removeNodes(Node parentNode, Node...childNodes){
		if(null != childNodes && null != parentNode){
			for(Node childNode : childNodes)
				parentNode.removeChild(childNode);
		}
		return parentNode;
	}
	
	/**
	 * 
	 * @param express The XPath expression(eg. "/books/book[price<10]", "/books/book[@id='B02']")
	 * @param content
	 */
	public void updateTextContent(String express, String content){
		findSingleNode(express, root).setTextContent(content);
	}
	
	/**
	 * 将node的XML字符串输出到控制台
	 * @param node
	 */
	public void output(Node node) {
		TransformerFactory transFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transFactory.newTransformer();
			transformer.setOutputProperty("encoding", "utf-8");
			transformer.setOutputProperty("indent", "yes");
			DOMSource source = new DOMSource();
			source.setNode(node);
			StreamResult result = new StreamResult();
			result.setOutputStream(System.out);

			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查找节点，并返回第一个符合条件节点
	 * @param express The XPath expression(eg. "/books/book[price<10]", "/books/book[@id='B02']")
	 * @param source The starting context (a node, for example).
	 * @return
	 */
	public Node findSingleNode(String express, Object source) {
		if(null == source) source = root;
		Node result = null;
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		try {
			result = (Node) xpath.evaluate(express, source, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return result;
		}

		return result;
	}

	/**
	 * 查找节点，返回符合条件的节点集。
	 * @param express The XPath expression(eg. "/books/book[price<10]")
	 * @param source The starting context (a node, for example).
	 * @return
	 */
	public NodeList findNodes(String express, Object source) {
		if(null == source) source = root;
		NodeList result = null;
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		try {
			result = (NodeList) xpath.evaluate(express, source, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return result;
		}

		return result;
	}
	
	/**
	 * 
	 * @param express The XPath expression(eg. "/books/book[price<10]")
	 * @return
	 */
	public String getTextContent(String express){
		Node node = findSingleNode(express, root);
		if(null == node)
			return null;
		return node.getTextContent();
	}

	/**
	 * 将Document输出到文件
	 * @param fileName
	 * @param doc
	 */
	public void saveAsXml(String fileName) {
		TransformerFactory transFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transFactory.newTransformer();
			transformer.setOutputProperty("indent", "yes");
			DOMSource source = new DOMSource();
			source.setNode(xmldoc);
			StreamResult result = new StreamResult();
			result.setOutputStream(new FileOutputStream(fileName));

			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将Document输出到文件
	 * @param fileName
	 * @param doc
	 */
	public void saveXml() {
		saveAsXml(this.sourceFile.getAbsolutePath());
	}
	

	public Element getRoot() {
		return root;
	}

	public void setRoot(Element root) {
		this.root = root;
	}

	public Document getXmldoc() {
		return xmldoc;
	}

	public void setXmldoc(Document xmldoc) {
		this.xmldoc = xmldoc;
	}
	
//	public static void main(String[] args) {
//		XMLWriter xmlWriter = new XMLWriter("");
//		// --- 新建一本书开始 ----
//		Element bookElem = xmlWriter.createElement("book", null, null);
//		Element nameElem = xmlWriter.createElement("name", null, "新书");
//		Element priceElem = xmlWriter.createElement("price", null, "20");
//		Element memoElem = xmlWriter.createElement("memo", null, "新书的更好看。");
//		xmlWriter.appendChildern(bookElem, nameElem, priceElem, memoElem);
//		xmlWriter.appendChildernToRoot(bookElem);
//		xmlWriter.output(xmlWriter.getXmldoc());
//		// --- 新建一本书完成 ----
//		
//		// --- 下面对《哈里波特》做一些修改。 ----
//		// --- 查询找《哈里波特》----
//		Element theBook = (Element) xmlWriter.findSingleNode("/books/book[name='哈里波特']", xmlWriter.getRoot());
//		System.out.println("--- 查询找《哈里波特》 ----");
//		xmlWriter.output(theBook);
//		// --- 此时修改这本书的价格 -----
//		theBook.getElementsByTagName("price").item(0).setTextContent("15");// getElementsByTagName
//																			// 返回的是NodeList，所以要跟上item(0)。另外，getElementsByTagName("price")相当于xpath
//																			// 的".//price"。
//		System.out.println("--- 此时修改这本书的价格 ----");
//		xmlWriter.output(theBook);
//		// --- 另外还想加一个属性id，值为B01 ----
//		theBook.setAttribute("id", "B01");
//		System.out.println("--- 另外还想加一个属性id，值为B01 ----");
//		xmlWriter.output(theBook);
//		// --- 对《哈里波特》修改完成。 ----
//		// --- 要用id属性删除《三国演义》这本书 ----
//		theBook = (Element) xmlWriter.findSingleNode("/books/book[@id='B02']", xmlWriter.getRoot());
//		System.out.println("--- 要用id属性删除《三国演义》这本书 ----");
//		xmlWriter.output(theBook);
//		theBook.getParentNode().removeChild(theBook);
//		System.out.println("--- 删除后的ＸＭＬ ----");
//		xmlWriter.output(xmlWriter.getXmldoc());
//		// --- 再将所有价格低于10的书删除 ----
//		NodeList someBooks = xmlWriter.findNodes("/books/book[price<10]", xmlWriter.getRoot());
//		System.out.println("--- 再将所有价格低于10的书删除 ---");
//		System.out.println("--- 符合条件的书有　" + someBooks.getLength() + "本。 ---");
//		for (int i = 0; i < someBooks.getLength(); i++) {
//			someBooks.item(i).getParentNode().removeChild(someBooks.item(i));
//		}
//		xmlWriter.output(xmlWriter.getXmldoc());
//		xmlWriter.saveXml("Test1_Edited.xml");
//	}
}
