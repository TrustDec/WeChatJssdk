package com;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取微信开发必要参数工具类
 * @author trust
 *
 */

public class WeChatUtil {
	/**
	 *获取access_token
	 */
	public static void getAccessToken() {
		Map<String, String> params = new HashMap<String,String>();
		params.put("grant_type",ConfigUtil.GRANT_TYPE);
		params.put("appid",ConfigUtil.APPID);
		params.put("secret",ConfigUtil.APP_SECRET);
		String result = HttpRequestUtil.getRequest(ConfigUtil.ACCESS_TOKEN_URL,params);
		AccessToken access = GsonUtil.fromJson(result,AccessToken.class);
		System.out.println(result);
		ConfigUtil.ACCESS_TOKEN = access.getAccess_token();// access_token 缓存
	}
	/**
	 *获取jsapi_ticket
	 */
	public static void getJsapiTicket() {
		Map<String, String> params = new HashMap<String,String>();
		params.put("access_token",ConfigUtil.ACCESS_TOKEN);
		params.put("type",ConfigUtil.JSAPT_TICKET_TYPE);
		String result = HttpRequestUtil.getRequest(ConfigUtil.JSAPI_TICKENT_URL,params);
		JsapiTicket ticket = GsonUtil.fromJson(result,JsapiTicket.class);
		System.out.println(ticket);
		ConfigUtil.JSAPT_TICKET = ticket.getTicket();// jsapi_ticket 缓存
	}
	public String init(){
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("timestamp",ConfigUtil.TIMESTAMP);
		dataMap.put("noncestr", ConfigUtil.NONCESTR);
		dataMap.put("signature", ConfigUtil.SIGNATURE);
		dataMap.put("appID", ConfigUtil.APPID);
		json = dataMap;
		return "success";
	}
}
