package com.cn.controller.weChat.util;

import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YancyPeng on 2018/10/16.
 * xml和map互相转化类
 */
public class XmlUtil {

    /**
     *
     * @param strXML 解密过后的xml字符串
     * @return 该字符串对应的map
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        try {
            Map<String, String> data = new HashMap<>();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
            }
            return data;
        } catch (Exception ex) {
            throw ex;
        }

    }

    /**
     *
     * @param param 封装好的map对象
     * @return 对应的xml字符串形式
     */
    public static String mapToXml(Map<String, Object> param) {

        StringBuffer strbuff = new StringBuffer("<xml>");

        if (param != null && !param.isEmpty()) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                strbuff.append("<").append(entry.getKey()).append(">");
                strbuff.append("<![CDATA[");
                if (!StringUtils.isEmpty(entry.getValue())) {
                    strbuff.append(entry.getValue());
                }
                strbuff.append("]]>");
                strbuff.append("</").append(entry.getKey()).append(">");
            }
        }
        return strbuff.append("</xml>").toString();
    }

}
