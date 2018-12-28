package com.watts.sys.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.watts.sys.repository.BaseJDBCRepository;
import com.watts.sys.utils.CheckUtil;
import com.watts.sys.utils.MessageUtil;
import com.watts.sys.utils.TextMessageUtil;

@RestController
@RequestMapping("/wx")
public class WXController {
	protected static Logger logger = LoggerFactory.getLogger(WXController.class);
	@Autowired
	private BaseJDBCRepository baseJDBCRepository;

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public void login(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("success");
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
				out.write(echostr);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			out.close();
		}

	}

	@RequestMapping(value = "wxdemo", method = RequestMethod.POST)
	public void dopost(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		// 将微信请求xml转为map格式，获取所需的参数
		Map<String, String> map = MessageUtil.xmlToMap(request);
		String ToUserName = map.get("ToUserName");
		String FromUserName = map.get("FromUserName");
		String MsgType = map.get("MsgType");
		String Content = map.get("Content");

		String message = null;
		// 处理文本类型，实现输入1，回复相应的封装的内容
		if ("text".equals(MsgType)) {
			if ("1".equals(Content)) {
				TextMessageUtil textMessage = new TextMessageUtil();
				message = textMessage.initMessage(FromUserName, ToUserName);
			}
		}
		try {
			out = response.getWriter();
			out.write(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.close();
	}
}
