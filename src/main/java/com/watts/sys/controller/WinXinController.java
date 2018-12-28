package com.watts.sys.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;  
/**
 * 微信接口
 * 
 * @author 刘宇 E-mail: liuyu_k26@163.com
 * @version 创建时间：2016年11月9日 下午4:44:41
 */
@Controller
@RequestMapping("/sys/weixin/weixin")
public class WinXinController {

	private Log logger = LogFactory.getLog(WinXinController.class);
	private String Token = "liuyu";
	private String echostr;

	/**
	 * 接入连接生效验证
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	@RequestMapping(value = "/weixinconnect", method = { RequestMethod.GET })
	private void weixinconnect(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("RemoteAddr: " + request.getRemoteAddr());
		logger.info("QueryString: " + request.getQueryString());
		if (!accessing(request, response)) {
			logger.info("服务器接入失败.......");
			return;
		}
		
		String echostr = getEchostr();
		if (echostr != null && !"".equals(echostr)) {
			logger.info("服务器接入生效..........");
			response.getWriter().print(echostr);// 完成相互认证
		}
	}
	@RequestMapping(value = "/weixinconnect", method = { RequestMethod.POST })
	private void weixinmessage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		InputStream is = request.getInputStream();
		// 取HTTP请求流长度
		int size = request.getContentLength();
		// 用于缓存每次读取的数据
		byte[] buffer = new byte[size];
		// 用于存放结果的数组
		byte[] xmldataByte = new byte[size];
		int count = 0;
		int rbyte = 0;
		// 循环读取
		while (count < size) {
			// 每次实际读取长度存于rbyte中
			rbyte = is.read(buffer);
			for (int i = 0; i < rbyte; i++) {
				xmldataByte[count + i] = buffer[i];
			}
			count += rbyte;
		}
		is.close();
		String strXml = new String(xmldataByte, "UTF-8");
		logger.info("strXml: " + strXml);
//		String result = new WechatProcess().processWechatMag(strXml);
		String result = "";
		logger.info("result: " + result);
		try {
			OutputStream os = response.getOutputStream();
			os.write(result.getBytes("UTF-8"));
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
//		try {
//			manageMessage(requestStr, request, response);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}

	/*
	 * <p>业务转发组件</p>
	 * 
	 */
	private void manageMessage(String requestStr, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String responseStr;

		try {
			XMLSerializer xmlSerializer = new XMLSerializer();
			JSONObject jsonObject = (JSONObject) xmlSerializer.read(requestStr);
			String event = jsonObject.getString("Event");
			String msgtype = jsonObject.getString("MsgType");
			if ("CLICK".equals(event) && "event".equals(msgtype)) { // 菜单click事件
				String eventkey = jsonObject.getString("EventKey");
				if ("hytd_001".equals(eventkey)) { // hytd_001 这是好友团队按钮的标志值
					jsonObject.put("Content", "欢迎使用好友团队菜单click按钮.");
				}

			}
			responseStr = creatRevertText(jsonObject);// 创建XML
			logger.info("responseStr:" + responseStr);
			OutputStream os = response.getOutputStream();
			os.write(responseStr.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String creatRevertText(JSONObject jsonObject) {
		StringBuffer revert = new StringBuffer();
		revert.append("<xml>");
		revert.append("<ToUserName><![CDATA[" + jsonObject.get("ToUserName") + "]]></ToUserName>");
		revert.append("<FromUserName><![CDATA[" + jsonObject.get("FromUserName") + "]]></FromUserName>");
		revert.append("<CreateTime>" + jsonObject.get("CreateTime") + "</CreateTime>");
		revert.append("<MsgType><![CDATA[text]]></MsgType>");
		revert.append("<Content><![CDATA[" + jsonObject.get("Content") + "]]></Content>");
		revert.append("<FuncFlag>0</FuncFlag>");
		revert.append("</xml>");
		return revert.toString();
	}

	/**
	 * 用来接收微信公众平台的验证
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	private boolean accessing(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		if (isEmpty(signature)) {
			return false;
		}
		if (isEmpty(timestamp)) {
			return false;
		}
		if (isEmpty(nonce)) {
			return false;
		}
		if (isEmpty(echostr)) {
			return false;
		}
		String[] ArrTmp = { Token, timestamp, nonce };
		Arrays.sort(ArrTmp);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ArrTmp.length; i++) {
			sb.append(ArrTmp[i]);
		}
		String pwd = Encrypt(sb.toString());

		logger.info("signature:" + signature + "timestamp:" + timestamp + "nonce:" + nonce + "pwd:" + pwd + "echostr:"
				+ echostr);

		if (trim(pwd).equals(trim(signature))) {
			this.echostr = echostr;
			return true;
		} else {
			return false;
		}
	}

	private boolean isEmpty(String str) {
		return null == str || "".equals(str) ? true : false;
	}

	private String trim(String str) {
		return null != str ? str.trim() : str;
	}

	private String Encrypt(String strSrc) {
		MessageDigest md = null;
		String strDes = null;

		byte[] bt = strSrc.getBytes();
		try {
			md = MessageDigest.getInstance("SHA-1");
			md.update(bt);
			strDes = bytes2Hex(md.digest()); // to HexString
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Invalid algorithm.");
			return null;
		}
		return strDes;
	}

	public String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

	public String getEchostr() {
		return echostr;
	}
}
