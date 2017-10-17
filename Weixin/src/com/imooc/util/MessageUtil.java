package com.imooc.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.imooc.joke.JokeFactory;
import com.imooc.picture.CatchImage;
import com.imooc.po.Image;
import com.imooc.po.ImageMessage;
import com.imooc.po.News;
import com.imooc.po.NewsMessage;
import com.imooc.po.TextMessage;
import com.imooc.translate.TransApi;
import com.thoughtworks.xstream.XStream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MessageUtil {
	
	public static String MESSAGE_TEXT = "text";
	public static String MESSAGE_NEWS = "news";
	public static String MESSAGE_IMAGE = "image";
	public static String MESSAGE_VOICE = "voice";
	public static String MESSAGE_LINK = "link";
	public static String MESSAGE_LOCATION = "location";
	public static String MESSAGE_EVENT = "event";
	public static String MESSAGE_SUBSCRIBE= "subscribe";
	public static String MESSAGE_UNSUBSCRIBE = "unsubscribe";
	public static String MESSAGE_CLICK = "CLICK";
	public static String MESSAGE_VIEW = "VIEW";
	
	//百度翻译API接口权限
	private static final String APP_ID = "20171006000086503";
    private static final String SECURITY_KEY = "OElbDshVOsy0HhwbwvP7";
	
	/**
	 * xml转为map
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Map<String,String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException{
		Map<String,String> map = new HashMap<String,String>();
		
		SAXReader reader = new SAXReader();
		
		InputStream ins = request.getInputStream();
		
		Document doc = reader.read(ins);
		
		Element root = doc.getRootElement();
		
		List<Element> list = root.elements();
		
		for(Element e:list){
			map.put(e.getName(), e.getText());
		}
		
		ins.close();
		
		return map;
	}
	
	/**
	 * 文本转为xml
	 * @param textMessage
	 * @return
	 */
	public static String TextToXml(TextMessage textMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}
	
	/**
	 * 初始化回复消息
	 * @param toUserName
	 * @param fromUserName
	 * @param content
	 * @return
	 */
	public static String initText(String toUserName,String fromUserName,String content){
		TextMessage text = new TextMessage();
		text.setFromUserName(toUserName);
		text.setToUserName(fromUserName);
		text.setCreateTime(new Date().getTime());
		text.setMsgType(MESSAGE_TEXT);
		text.setContent(content);
		return MessageUtil.TextToXml(text);
	}
	
	public static String menuText(){
		StringBuffer sb = new StringBuffer();
		sb.append("准备好上车了吗\n\n");
		sb.append("1、输入任意内容可进行翻译\n");
		sb.append("2、回复“2”可获得图片\n");
		sb.append("3、回复“3”可获得冷笑话\n\n");
		sb.append("回复“？”调出此菜单。\n");
		return sb.toString();
	}
	
	public static String firstText(){
		StringBuffer sb = new StringBuffer();
		sb.append("本套课程介绍微信公众号开发,主要涉及公众号介绍、编辑模式介绍、开发模式介绍等");
		return sb.toString();
	}
	
	public static String secondText(){
		StringBuffer sb = new StringBuffer();
		sb.append("慕课网是垂直的互联网IT技能免费学习网站。以独家视频教程、在线编程工具、学习计划、问答社区为核心特色。"
				+ "在这里，你可以找到最好的互联网技术牛人，也可以通过免费的在线公开视频课程学习国内领先的互联网IT技术。");
		return sb.toString();
	}
	
	/**
	 * 图文消息转为xml
	 * @param newsMessage
	 * @return
	 */
	public static String newsToXml(NewsMessage newsMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", News.class);
		return xstream.toXML(newsMessage);
	}
	
	/**
	 * 图片消息转为xml
	 * @param newsMessage
	 * @return
	 */
	public static String imageToXml(ImageMessage imageMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", imageMessage.getClass());
		return xstream.toXML(imageMessage);
	}
	
	/**
	 * 图文消息组装
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String initNewsMessage(String toUserName,String fromUserName){
		String message = null;
		List<News> list = new ArrayList<News>();
		NewsMessage newsMessage = new NewsMessage();
		
		News news = new News();
		news.setTitle("慕课网介绍");
		news.setDescription("慕课网是垂直的互联网IT技能免费学习网站。以独家视频教程、在线编程工具、学习计划、问答社区为核心特色。"
				+ "在这里，你可以找到最好的互联网技术牛人，也可以通过免费的在线公开视频课程学习国内领先的互联网IT技术。");
		news.setPicUrl("http://27myce.natappfree.cc/Weixin/image/imooc.jpg");
		news.setUrl("www.imooc.com");
		
		list.add(news);
		
		newsMessage.setArticles(list);
		newsMessage.setToUserName(fromUserName);
		newsMessage.setFromUserName(toUserName);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setArticleCount(list.size());
		newsMessage.setMsgType(MESSAGE_NEWS);
		
		message = newsToXml(newsMessage);
		
		return message;
	}

	/**
	 * 组装图片消息
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 * @throws Exception 
	 */
	public static String initImageMessage(String toUserName,String fromUserName) throws Exception{
		String message = null;
		
		CatchImage cm = new CatchImage(); 
	    //获得html文本内容 
	    int num = (int) (Math.random()*5);
	    System.out.println("我是："+num);
	    String url = CatchImage.URL2.replace("?", num+"");
	    String HTML = cm.getHTML(url); 
	    List<String> pageUrl = cm.getImageUrl(HTML,CatchImage.PAGE_REG);
	    System.out.println("pageUrl="+pageUrl.size());
	    int num2 = (int)(Math.random()*pageUrl.size());
	    List<String> pageSrc = cm.getImageUrl(pageUrl.get(num2),CatchImage.PAGESRC_REG);
	    System.out.println("pageSrc="+pageSrc.size());
	    String html2 = cm.getHTML(pageSrc.get(0));
	    //获取图片标签 
	    List<String> imgUrl = cm.getImageUrl(html2,CatchImage.IMGURL_REG); 
	    System.out.println("imgUrl="+imgUrl.size());
	    //获取图片src地址 
	    List<String> imgSrc = cm.getImageSrc(imgUrl,CatchImage.IMGSRC_REG); 
	    System.out.println("imgSrc="+imgSrc.size());
	    int num3 = (int)(Math.random()*imgSrc.size());
	    String imageName = imgSrc.get(num3).substring(imgSrc.get(num3).lastIndexOf("/") + 1, imgSrc.get(num3).length()); 
	    URL uri = new URL(imgSrc.get(num3)); 
        InputStream in = uri.openStream();
	    
		Image image = new Image();
		//String filePath = "F:/Image/"+imageName;
		String accessToken = WeixinUtil.getAccessToken();
		String mediaId = WeixinUtil.streamUpload(in,imageName, accessToken, MESSAGE_IMAGE);
		image.setMediaId(mediaId);
		
		ImageMessage imageMessage = new ImageMessage();
		imageMessage.setFromUserName(toUserName);
		imageMessage.setToUserName(fromUserName);
		imageMessage.setCreateTime(new Date().getTime());
		imageMessage.setMsgType(MESSAGE_IMAGE);
		imageMessage.setImage(image);
		
		message = imageToXml(imageMessage);
				
		return message;
	}
	
	/**
	 * 翻译
	 * @param query
	 * @return
	 */
	public static String getTransResult(String query){
		TransApi api = new TransApi(APP_ID, SECURITY_KEY);
		String result = api.getTransResult(query, "auto", "auto");
        JSONObject jsonObj = JSONObject.fromObject(result);
        JSONArray jsonArr = JSONArray.fromObject(jsonObj.get("trans_result"));
        JSONObject obj = JSONObject.fromObject(jsonArr.get(0));
        return obj.getString("dst");
	}
	

	
	
}
