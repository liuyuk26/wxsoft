package com.watts.wx.process;

import java.util.Date;
/**
 * 封装最终的xml格式结果
 * @author pamchen-1
 *
 */
public class FormatXmlProcess {
	/**
	 * 封装文字类的返回消息
	 * @param to
	 * @param from
	 * @param content
	 * @return
	 */
	public String formatXmlAnswer(String to, String from, String content) {
		StringBuffer sb = new StringBuffer();
		Date date = new Date();
		sb.append("<xml><ToUserName><![CDATA[");
		sb.append(to);
		sb.append("]]></ToUserName><FromUserName><![CDATA[");
		sb.append(from);
		sb.append("]]></FromUserName><CreateTime>");
		sb.append(date.getTime());
		sb.append("</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[");
		sb.append(content);
		sb.append("]]></Content><FuncFlag>0</FuncFlag></xml>");
		return sb.toString();
	}
	
	/**
	 * 封装文字类的返回消息
	 * @param to
	 * @param from
	 * @param content
	 * @return
	 */
	public String imageAndText(String to, String from, String content) {
		StringBuffer sb = new StringBuffer();
		Date date = new Date();
		
		
		sb.append("<xml>");
		sb.append("<ToUserName><![CDATA["+to+"]]></ToUserName>");
		sb.append("<FromUserName><![CDATA["+from+"]]></FromUserName>");
		sb.append("<CreateTime>12345678</CreateTime>");
		sb.append("<MsgType><![CDATA[news]]></MsgType>");
		sb.append("<ArticleCount>2</ArticleCount>");
		sb.append("<Articles>");
		sb.append("<item>");
		sb.append("<Title><![CDATA[title1]]></Title> ");
		sb.append("<Description><![CDATA[description1]]></Description>");
		sb.append("<PicUrl><![CDATA[picurl]]></PicUrl>");
		sb.append("<Url><![CDATA[url]]></Url>");
		sb.append("</item>");
		sb.append("<item>");
		sb.append("<Title><![CDATA[title]]></Title>");
		sb.append("<Description><![CDATA[description]]></Description>");
		sb.append("<PicUrl><![CDATA[picurl]]></PicUrl>");
		sb.append("<Url><![CDATA[url]]></Url>");
		sb.append("</item>");
		sb.append("</Articles>");
		sb.append("</xml> ");
		
		return sb.toString();
	}
	
}
