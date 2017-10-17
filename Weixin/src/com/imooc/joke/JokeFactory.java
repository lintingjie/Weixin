package com.imooc.joke;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.imooc.util.EncodeUtil;


public class JokeFactory {  
	
	private static String URL = "http://xiaohua.zol.com.cn/lengxiaohua/?.html";
	
    /** 
     * 连接到服务器，并请求给定的文档 
     * 
     * @param urlString 
     * ：文档地址 
     * @return：String 
     */  
    public String getDocumentAt(String urlString) {  
    	StringBuilder sb = new StringBuilder();
        try {  
            URL url = new URL(urlString);// 生成url对象  
            URLConnection urlConnection = url.openConnection();// 打开url连接  
            BufferedReader br = new BufferedReader(new InputStreamReader(  
                    urlConnection.getInputStream(),"gbk"));  
            String line = null;  
            while ((line = br.readLine()) != null) {  
                sb.append(line + "\n");  
            }  
        } catch (MalformedURLException e) {  
            System.out.println("不能连接到URL：" + urlString);  
            e.printStackTrace();  
        } catch (IOException e) {  
            System.out.println("连接到URL抛出异常信息：" + urlString);  
            e.printStackTrace();  
        }  
        return sb.toString();  
    }  
    /** 
     * @param args 
     */  
      
    public static List<String> result(String url,String match ){  
          
        List<String> al=new ArrayList<String>();  
  
        Pattern pattern = Pattern.compile(match);  
        Matcher matcher = pattern.matcher(url);  
        String buffer =new String() ;  
          
  
        while(matcher.find()){              
            buffer=matcher.group(1);    
            String fuck=buffer.replaceAll("<p>","").replace("</p>", "").replace("&ldquo;", "").replace("&rdquo;", "")
            		.replace("&hellip;", "").replace("&nbsp;", "");  
            al.add(fuck);  
        }  
        return al;  
          
          
          
    }  
  
    public static String getJoke(){
    	JokeFactory client = new JokeFactory();  
        int num = (int)(Math.random()*125);
        System.out.println(num);
        String url = client.getDocumentAt(URL.replace("?", num+""));  
        List<String> jokeList=result(url, "<div class=\"summary-text\">([\\w\\W]*?)</div>");
        return jokeList.get((int)(Math.random()*jokeList.size()));
    }
    
    
    public static void main(String[] args) throws Exception {  
          
        System.out.println(getJoke());
          
}  
  
}