package com;    
import java.io.BufferedReader;    
import java.io.InputStream;    
import java.io.InputStreamReader;    
import java.io.OutputStream;    
import java.net.ConnectException;    
import java.net.URL;    
    
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;    
import javax.net.ssl.SSLContext;    
import javax.net.ssl.SSLSocketFactory;    
import javax.net.ssl.TrustManager;    
    







import net.sf.json.JSONException;
import net.sf.json.JSONObject;    
    







import org.slf4j.Logger;    
import org.slf4j.LoggerFactory;    
    
/**  
 * 公众平台通用接口工具类  
 *  
 */    
public class WeixinUtil {    
    private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);    
    
    /**  
     * 发起https请求并获取结果  
     *   
     * @param requestUrl 请求地址  
     * @param requestMethod 请求方式（GET、POST）  
     * @param outputStr 提交的数据  
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)  
     */    
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {    
        JSONObject jsonObject = null;    
        StringBuffer buffer = new StringBuffer();    
        try {    
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化    
            TrustManager[] tm = { new MyX509TrustManager() };    
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");    
            sslContext.init(null, tm, new java.security.SecureRandom());    
            // 从上述SSLContext对象中得到SSLSocketFactory对象    
            SSLSocketFactory ssf = sslContext.getSocketFactory();    
    
            URL url = new URL(requestUrl);    
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();    
            httpUrlConn.setSSLSocketFactory(ssf);    
    
            httpUrlConn.setDoOutput(true);    
            httpUrlConn.setDoInput(true);    
            httpUrlConn.setUseCaches(false);    
            // 设置请求方式（GET/POST）    
            httpUrlConn.setRequestMethod(requestMethod);    
    
            if ("GET".equalsIgnoreCase(requestMethod))    
                httpUrlConn.connect();    
    
            // 当有数据需要提交时    
            if (null != outputStr) {    
                OutputStream outputStream = httpUrlConn.getOutputStream();    
                // 注意编码格式，防止中文乱码    
                outputStream.write(outputStr.getBytes("UTF-8"));    
                outputStream.close();    
            }    
    
            // 将返回的输入流转换成字符串    
            InputStream inputStream = httpUrlConn.getInputStream();    
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");    
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);    
    
            String str = null;    
            while ((str = bufferedReader.readLine()) != null) {    
                buffer.append(str);    
            }    
            bufferedReader.close();    
            inputStreamReader.close();    
            // 释放资源    
            inputStream.close();    
            inputStream = null;    
            httpUrlConn.disconnect();    
            jsonObject = JSONObject.fromObject(buffer.toString());    
        } catch (ConnectException ce) {    
            log.error("Weixin server connection timed out.");    
        } catch (Exception e) {    
            log.error("https request error:{}", e);    
        }    
        return jsonObject;    
    }    
 // 获取access_token的接口地址（GET） 限200（次/天）    
    public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";    
        
    /**  
     * 获取access_token  
     *   
     * @param appid 凭证  
     * @param appsecret 密钥  
     * @return  
     */    
    public static AccessToken getAccessToken(String appid, String appsecret) {    
        AccessToken accessToken = null;    
        
        String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);    
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);  //调用通用的https请求方法  
        // 如果请求成功    
        if (null != jsonObject) {    
            try {    
                accessToken = new AccessToken();    
                accessToken.setToken(jsonObject.getString("access_token"));    
                accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
                } catch (JSONException e) {    
                accessToken = null;    
                // 获取token失败    
                log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));    
            }    
        }    
        return accessToken;    
    }   
    //根据前面获取到的 access_token 访问凭证来获取 jsapi_ticket 临时票据
    public final static String ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";   
    
    public static JsApiTicket getJsApiTicket(String accessToken) {   
            JsApiTicket jsApiTicket = null;    
            //String appsecret = null;
			//String appid = null;
			//获取token   
            //AccessToken acess_token= WeixinUtil.getAccessToken(appid, appsecret);             
            String requestUrl = ticket_url.replace("ACCESS_TOKEN", accessToken);  
            JSONObject jsonObject = WeixinUtil.httpRequest(requestUrl, "GET", null);    
            // 如果请求成功    
            if (null != jsonObject) {    
                try {    
                    jsApiTicket = new JsApiTicket();    
                    jsApiTicket.setTicket(jsonObject.getString("ticket"));    
                    jsApiTicket.setExpiresIn(jsonObject.getInt("expires_in"));    
                } catch (JSONException e) {    
                    accessToken = null;    
                    // 获取jsApiTicket失败    
                    log.error("获取jsApiTicket失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));    
                }    
            }    
            return jsApiTicket;    
        } 
  //创建一个静态map缓存  
//    private static Map<String,Ticket> weixinCache = new HashMap<String, Ticket>();  
//    private static JsApiTicket getJsApiTicket(String accessToken){   
//            String ticket_str = null;  
//                    JsApiTicket jsApiTicket = weixinCache.get("ticket");  
//            if(jsApiTicket != null){  
//                Date expiresIn = jsApiTicket.getExpiresIn();  
//                Calendar ever = Calendar.getInstance();  
//                ever.setTime(expiresIn);  
//                Calendar current = Calendar.getInstance();  
//                if(ever.after(current)){  
//                    ticket_str = jsApiTicket.getTicket();  
//                }else{  
//                    String request_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi".replace("ACCESS_TOKEN", accessToken);  
//                    JSONObject jsonObject = WeixinUtils.httpRequest(request_url, "GET", null);  
//                    ticket_str = jsonObect.getString("ticket");  
//                    weixinCache.remove("ticket");  
//                    JsApiTicket ticket2 = new Ticket();  
//                    ticket2.setExpirseIn(jsonObect.getString("expirseIn"));  
//                    ticket2.setTicket(ticket_str);  
//                    weixinCache.put("ticket", ticket2);  
//                }  
//            }else{  
//                //同上面的else里的code  
//            }  
//              
//            return jsApiTicket;  
//        }  
}      