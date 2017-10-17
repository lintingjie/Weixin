package com.imooc.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import com.imooc.joke.JokeFactory;
import com.imooc.po.TextMessage;
import com.imooc.translate.TransApi;
import com.imooc.util.CheckUtil;
import com.imooc.util.MessageUtil;

public class WeixinServlet extends HttpServlet {

	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		
		PrintWriter pw = response.getWriter();
		if(CheckUtil.checkSignature(signature, timestamp, nonce)){
			pw.write(echostr);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		try {
			Map<String,String> map = MessageUtil.xmlToMap(req);
			String toUserName = map.get("ToUserName");
			String fromUserName = map.get("FromUserName");
			String msgType = map.get("MsgType");
			String content = map.get("Content");
			String message = null;
			if(MessageUtil.MESSAGE_TEXT.equals(msgType)){
				if("1".equals(content)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.firstText());
				}else if("2".equals(content)){//发图片
					//message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.secondText());
					message = MessageUtil.initImageMessage(toUserName, fromUserName);
				}else if("?".equals(content)||"？".equals(content)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
				}else if("3".equals(content)){//发笑话
					message = MessageUtil.initText(toUserName, fromUserName, JokeFactory.getJoke());
				}else if("4".equals(content)){//发图文消息
					message = MessageUtil.initNewsMessage(toUserName, fromUserName);
				}else{//翻译
					//message = MessageUtil.initText(toUserName, fromUserName, content);
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.getTransResult(content));
				}
			}else if(MessageUtil.MESSAGE_EVENT.equals(msgType)){
				String eventType = map.get("Event");
				if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
				}else if(MessageUtil.MESSAGE_CLICK.equals(eventType)){
					String eventKey = map.get("EventKey");
					if("11".equals(eventKey)){
						message = MessageUtil.initImageMessage(toUserName, fromUserName);
					}else if("21".equals(eventKey)){
						message = MessageUtil.initText(toUserName, fromUserName, JokeFactory.getJoke());
					}
					
				}
			}
			System.out.println(message);
			out.print(message);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			out.close();
		}
	}

}
