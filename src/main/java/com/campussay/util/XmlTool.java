package com.campussay.util;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;


import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;


import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class XmlTool {
	
	/**
	 * @author liupengyan
	 * @Title: mapToXml 
	 * @Description: 把map转化为xml字符串
	 * @param map
	 * @return
	 * @return: String
	 */
	public static String mapToXml(Map<String,Object> map) {
		StringBuffer str=new StringBuffer("<xml>");
		//循环这个map
		for (Map.Entry<String, Object> entry : map.entrySet()) {  
			str.append("<"+entry.getKey()+">"+entry.getValue()+"</"+entry.getKey()+">");
		}  
		str.append("</xml>");
		return str.toString();
	}
	
	 public static Map<String,Object> xmlElements(String xmlDoc)throws Exception {
		 //创建一个新的字符串
		 StringReader read = new StringReader(xmlDoc);
	        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
	        InputSource source = new InputSource(read);
	        //创建一个新的SAXBuilder
	        SAXBuilder sb = new SAXBuilder();
	        Map<String,Object> map=new HashMap<String, Object>();
	            //通过输入源构造一个Document
	            Document doc = sb.build(source);
	            //取的根元素
	            Element root = doc.getRootElement();
	            //System.out.println(root.getName());//输出根元素的名称（测试）
	            //得到根元素所有子元素的集合
	            @SuppressWarnings("rawtypes")
	            List jiedian = root.getChildren();
	            
	            //获得XML中的命名空间（XML中未定义可不写）
	            Element et = null;
	            for(int i=0;i<jiedian.size();i++){
	                et = (Element) jiedian.get(i);//循环依次得到子元素
	               // System.out.println(et.getName()+":"+et.getValue());
	                map.put(et.getName(), et.getValue());
	            }
	           
	            return map;

	 }
}
