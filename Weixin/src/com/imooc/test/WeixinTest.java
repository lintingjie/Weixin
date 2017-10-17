package com.imooc.test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import com.imooc.po.AccessToken;
import com.imooc.translate.TransApi;
import com.imooc.util.WeixinUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

public class WeixinTest {
	
	//百度翻译API接口权限
	private static final String APP_ID = "20171006000086503";
    private static final String SECURITY_KEY = "OElbDshVOsy0HhwbwvP7";
	public static void main(String[] args) {
		//String token = WeixinUtil.getAccessToken();
		
//		System.out.println("token:"+token);
//		
//		String path = "C:/Users/lintingjie/Desktop/timg.jpg";
//		try {
//			String mediaId = WeixinUtil.upload(path, token, "image");
//			System.out.println(mediaId);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		String menu = JSONObject.fromObject(WeixinUtil.ininMenu()).toString();
//		int result = WeixinUtil.creatMenu(token, menu);
//		System.out.println(result);
//		JSONObject jsonObject = WeixinUtil.deleteMenu(token);
//		System.out.println(jsonObject);
		
//		TransApi api = new TransApi(APP_ID, SECURITY_KEY);
//        String query = "cryptography";
//        String result = api.getTransResult(query, "auto", "auto");
//        JSONObject jsonObj = JSONObject.fromObject(result);
//        JSONArray jsonArr = JSONArray.fromObject(jsonObj.get("trans_result"));
//        JSONObject obj = JSONObject.fromObject(jsonArr.get(0));
//        System.out.println(obj.get("dst"));
	}

}
