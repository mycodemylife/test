package com.open.web.utils;

import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;


/**
 * Object转换类
 * 
 * <p>将List转换为符合josn的字符串</p>
 * 
 * @author  高连川,孙旭 
 *
 */
public class ConvetObject {

	private static final String Domain = null;

	/**
	 * 将collection转换为符合josn的字符串
	 * @param collection 要转换的集合
	 * @return 转换后字符串
	 */
	public static String convetListToStringByXStream(Collection collection){ 
		
		XStream xstream = new XStream(new JsonHierarchicalStreamDriver() {
			
		    public HierarchicalStreamWriter createWriter(Writer writer) {
		        return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
		    }
		    
		});
		
		xstream.setMode(XStream.NO_REFERENCES);
		if(collection!=null){
			
			Iterator iterator = collection.iterator();
			while (iterator.hasNext()) {
				Class clzz = iterator.next().getClass();
				String fullName = clzz.getName();
				String[] temp = fullName.split("\\.");
				xstream.alias(temp[temp.length-1], clzz);
				
			}
		}

		return xstream.toXML(collection);

	}
	
	
	/**
	 * 将List转换为符合josn的字符串(在json串中会有类型的标记)
	 * @author 宁培峰
	 * @return josn字符串
	 */
	public static String convetListToString(List<?> list){
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		String json = xstream.toXML(list);
		return json;
	}
	
	/**
	 * 将josn转换为List(在json串中会有类型的标记)
	 * @author 宁培峰
	 * @return List
	 */
	public static List<?> convetStringToList(String jsonString){
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		List<?> list = (List<?>)xstream.fromXML(jsonString);
		return list;
	}
	
	/**
	 * 将map转换为符合josn的字符串(在json串中会有类型的标记)
	 * @author 宁培峰
	 * @return josn字符串
	 */
	public static String convetMapToString(Map<String, String> map){
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		String json = xstream.toXML(map);
		return json;
	}
	
	/**
	 * 将josn转换为map(在json串中会有类型的标记)
	 * @author 宁培峰
	 * @return map
	 */
	public static Map<String, String> convetStringToMap(String jsonString){
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		Map<String, String> maplist = (Map<String, String>)xstream.fromXML(jsonString);
		return maplist;
	}
	
	public static void main(String[] args) {
		
//		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		XStream xstream = new XStream();
//		IcpRespone icpRespone = new IcpRespone();
//		icpRespone.setState("查询成功！");
	/*	ReturnPO returnPO = new ReturnPO();
		returnPO.setMsg("结果");
		returnPO.setMsg_code(0);
		logQuery logQuery = new logQuery();
		logQuery.setBaseUnitName("测试");
		logQuery.setId(1002L);
		returnPO.setLogQuerylogQuery(logQuery);
		xstream.alias("return", ReturnPO.class);
		xstream.aliasAttribute("msg", "生态日");
		String xml = xstream.toXML(returnPO);
		System.out.println(xml);*/
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
//	    List<Domain> details = new ArrayList<Domain>();
//		Domain domain = new Domain();
////		
//		domain.setDomain("miaohui.com.cn");
//		domain.setPhylicnum("网站备案号");
//		domain.setState(1l);
//		details.add(domain);
//		
//		Domain domain1 = new Domain();
//		domain1.setDomain("miaohui.cn");
//		domain1.setPhylicnum("网站备案号11");
//		domain1.setState(1222l);
//		details.add(domain1);
//		
//		xstream.alias("Domain", Domain.class);
//		String xml = xstream.toXML(details);
//		
//		System.out.println(xml);
 		
//		xstream.alias("Domain", Domain.class);
//		List<Domain> details111 = (List<Domain>) xstream.fromXML(xml);
// 		System.out.println(details111);
 		
 		
		
 		
	}
	
}
