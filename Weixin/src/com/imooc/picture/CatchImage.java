package com.imooc.picture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CatchImage {

	// 地址 
	  //public static final String URL = "http://www.win4000.com/wallpaper_2285_0_0_?.html";//135790 
	  public static final String URL2 = "http://www.win4000.com/zt/meinv_?.html";//1-166
	  
	  //public static final String PAGE_REG = "<a href=\"(.*?)\" title=\"(.*?)\" target=\"_blank\"><img class=\"lazy\" data-original=\"(.*?)\" src=\"(.*?)\" alt=\"(.*?)\" /></a>";
	  public static final String PAGE_REG = "<a href=\"(.*?)\" alt=\"(.*?)\" title=\"(.*?)\" target=\"_blank\">";
	  //public static final String PAGESRC_REG = "http://www.win4000.com/wallpaper_detail_[0-9]{1,}.html";
	  public static final String PAGESRC_REG = "http://www.win4000.com/wallpaper_detail_[0-9]{1,}.html";
	  // 编码 
	  private static final String ECODING = "UTF-8"; 
	  // 获取img标签正则 
	  //public static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>"; 
	  //public static final String IMGURL_REG = "<img.*title=\"(.*?)\".*/>"; 
	  public static final String IMGURL_REG = "<img class=\"pic-large\" src=\"(.*?)\" alt=\"(.*?)\" title=\"(.*?)\"/>"; 
	  //public static final String IMGURL_REG = "<a href=\"(.*?)\" target=\"_blank\">下载壁纸"; 
	  // 获取src路径的正则 
	  public static final String IMGSRC_REG = "http:\"?(.*?)(\"|>|\\s+)"; 
	  
	    
	  public static void main(String[] args) throws Exception { 
	    CatchImage cm = new CatchImage(); 
	    //获得html文本内容 
	    int num = (int) (Math.random()*50);
	    String url = URL2.replace("?", num+"");
	    String HTML = cm.getHTML(url); 
	    List<String> pageUrl = cm.getImageUrl(HTML,PAGE_REG);
	    System.out.println("pageUrl="+pageUrl.size());
	    List<String> pageSrc = new ArrayList<String>();
	    for(String page:pageUrl){
	    	Matcher matcher = Pattern.compile(PAGESRC_REG).matcher(page); 
		    while (matcher.find()) { 
		    	pageSrc.add(matcher.group()); 
		    }
	    }
	    System.out.println("pageSrc="+pageSrc.size());
	    List<String> imgUrl = new ArrayList<String>();
	    for(String src:pageSrc){
	    	String html2 = cm.getHTML(src);
	    	Matcher matcher = Pattern.compile(IMGURL_REG).matcher(html2); 
		    while (matcher.find()) { 
		    	imgUrl.add(matcher.group()); 
		    } 
	    }
	    System.out.println("imgUrl="+imgUrl.size());
	    //获取图片src地址 
	    List<String> imgSrc = cm.getImageSrc(imgUrl,IMGSRC_REG); 
	    //下载图片 
	    cm.Download(imgSrc); 
	  } 
	    
	    
	  /*** 
	   * 获取HTML内容 
	   * 
	   * @param url 
	   * @return 
	   * @throws Exception 
	   */
	  public String getHTML(String url) throws Exception { 
	    URL uri = new URL(url); 
	    URLConnection connection = uri.openConnection(); 
	    InputStream in = connection.getInputStream(); 
	    byte[] buf = new byte[1024]; 
	    int length = 0; 
	    StringBuffer sb = new StringBuffer(); 
	    while ((length = in.read(buf, 0, buf.length)) > 0) { 
	      sb.append(new String(buf, ECODING)); 
	    } 
	    in.close(); 
	    return sb.toString(); 
	  } 
	  
	  /*** 
	   * 获取ImageUrl地址 
	   * 
	   * @param HTML 
	   * @return 
	   */
	  public List<String> getImageUrl(String HTML,String reg) { 
	    Matcher matcher = Pattern.compile(reg).matcher(HTML); 
	    List<String> listImgUrl = new ArrayList<String>(); 
	    while (matcher.find()) { 
	    	listImgUrl.add(matcher.group()); 
	    } 
	    return listImgUrl; 
	  } 
	  
	  /*** 
	   * 获取ImageSrc地址 
	   * 
	   * @param listImageUrl 
	   * @return 
	   */
	  public List<String> getImageSrc(List<String> listImageUrl,String reg) { 
	    List<String> listImgSrc = new ArrayList<String>(); 
	    for (String image : listImageUrl) { 
	      Matcher matcher = Pattern.compile(reg).matcher(image); 
	      while (matcher.find()) {
	    	String src = matcher.group().substring(0, matcher.group().length() - 1);
	    	listImgSrc.add(src);
	      } 
	    } 
	    return listImgSrc; 
	  } 
	  
	  /*** 
	   * 下载图片 
	   * 
	   * @param listImgSrc 
	   */
	  public void Download(List<String> listImgSrc) { 
	    try { 
	      for (String url : listImgSrc) { 
	        String imageName = url.substring(url.lastIndexOf("/") + 1, url.length()); 
	        URL uri = new URL(url); 
	        InputStream in = uri.openStream(); 
	        File file = new File("F:/Image",imageName);
	        FileOutputStream fo = new FileOutputStream(file); 
	        byte[] buf = new byte[1024]; 
	        int length = 0; 
	        System.out.println("开始下载:" + url); 
	        while ((length = in.read(buf, 0, buf.length)) != -1) { 
	          fo.write(buf, 0, length); 
	        } 
	        in.close(); 
	        fo.close(); 
	        System.out.println(imageName + "下载完成"); 
	        System.out.println("下载地址："+file.getAbsolutePath());
	      } 
	    } catch (Exception e) { 
	    	e.printStackTrace();
	      System.out.println("下载失败"); 
	    } 
	  } 
}
