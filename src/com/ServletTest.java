package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletTest
 */
@WebServlet("/ServletTest")
public class ServletTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletTest() {
        super(); 
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		response.setContentType("application/json; charset=utf-8");
			// TODO Auto-generated method stub
			WeChatUtil we = new WeChatUtil();
			String access_token = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
			String url = request.getParameter("url");
			String corpsecret = request.getParameter("corpsecret");
			System.out.print(url);
			System.out.println();
			System.out.print(corpsecret);
			System.out.println();
	        String accessToken = sendGet(access_token,"corpid="+we.appId+"&corpsecret="+corpsecret);
	        
	        accessToken = "["+accessToken+"]";
	        JSONArray jsonArray = JSONArray.fromObject(accessToken);
	        String AccessToken="";
	        if(jsonArray.size()>0){
	      	  for(int i = 0; i < jsonArray.size(); i++){
	      		  JSONObject job = jsonArray.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
	      		  AccessToken = job.get("access_token").toString();
	      	  }
	      	   System.out.print(AccessToken);
	        }
	        
	        String jsapi_ticket = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket";
	        String jsapiTicket = sendGet(jsapi_ticket,"access_token="+AccessToken);
	        jsapiTicket = "["+jsapiTicket+"]";
	        JSONArray jsonArrayJSAPI = JSONArray.fromObject(jsapiTicket);
	        String JsapiTicket="";
	        if(jsonArrayJSAPI.size()>0){
	        	  for(int i = 0; i < jsonArrayJSAPI.size(); i++){
	        		  JSONObject job = jsonArrayJSAPI.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
	        		  JsapiTicket = job.get("ticket").toString();
	        	  }
	          }
	//		String JsapiTicket="kgt8ON7yVITDhtdwci0qeZivwBqIbBwbdXwB4uJpwVFVK10yE1WezHwAOT7oLLxucqBHZucIMBEbCpOFRKaGVw";
	//		String AccessToken="Idp8-j2-xa5B6Z-9h8XxStafR1aeK8LTomuNE4_0LoPvcN_zfYNt16mmHvigP0Qs3s_PtQXMqlb2y7k8fKilpmcjL5sDyXxxALNuHAmQzTObbAzJfKlBa8GEv8QaU3sXdIIsf8cqDuVxam5Zx0s-0cGmY0MWaUhWdxgk5-Vz_CC7-31qylVmGRLx6TDesGUVbgksn4dKi3CcroI_v5fRHw";
			String test = we.doStep2(JsapiTicket,url);
			// request.getSession().setAttribute("demoTest", "{\"wxconfig\":"+test+",\"access_token\":\""+AccessToken+"\",\"jsapi_ticket\":\""+JsapiTicket+"\"}");
			response.getWriter().print("{\"wxconfig\":"+test+",\"access_token\":\""+AccessToken+"\",\"jsapi_ticket\":\""+JsapiTicket+"\"}");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
	

}
