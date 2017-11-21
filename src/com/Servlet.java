package com;

//导入必需的 java 库
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Trust
 *
 */
@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public Servlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 设置响应内容类型
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String timestamp = createTimestamp();
        String noncestr = createNoncestr();
        out.println("{\"noncestr\":\""+noncestr+"\",\"timestamp\":\""+timestamp+"\"}");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	private static String createTimestamp() {
		return Long.toString(System.currentTimeMillis() / 1000);
	}
	
	private static String createNoncestr(){
		return UUID.randomUUID().toString();
	}
	private static String jointString(){
		String sb = new String();
		ConfigUtil.NONCESTR = createNoncestr();
		ConfigUtil.TIMESTAMP = createTimestamp();
		sb +="jsapi_ticket=" +ConfigUtil.JSAPT_TICKET+"&";
		sb +="noncestr=" +ConfigUtil.NONCESTR+"&";
		sb +="timestamp=" +ConfigUtil.TIMESTAMP+"&";
		sb +="url=" +ConfigUtil.CURRENT_URL;
		return sb;
	}
	public static void createSign(){
		ConfigUtil.SIGNATURE = SHA1Util.SHA1(jointString());
	}
}
