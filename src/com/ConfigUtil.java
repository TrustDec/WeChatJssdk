package com;

public class ConfigUtil {
	public static final String APPID = "wx02930jss72h2bsi82";
	public static final String APP_SECRET = "974ed9fe-7dbd-4464-a5e3-56dda741f2e3";
	public static final String GRANT_TYPE = "client_credential";
	public static final String JSAPT_TICKET_TYPE = "jsapi";
	public static final String ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+APPID+"&corpsecret="+APP_SECRET; // 获取access_token
	public static final String JSAPI_TICKENT_URL = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=ACCESS_TOKEN"; // 获取jsapi_ticket
	public static final String CURRENT_URL = ""; // 调用接口页面
	public static String ACCESS_TOKEN = ""; // 全局缓存access_token
	public static String JSAPT_TICKET = ""; // 全局缓存jsapi_ticket
	public static String NONCESTR = ""; // 全局缓存noncestr
	public static String TIMESTAMP = ""; // 全局缓存timestamp
	public static String SIGNATURE = ""; // 全局缓存signature
}
