package servlet;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import timer.MyTimerTask;

/**
 * 核心请求处理类
 * 
 * @author
 * @date 2013-05-18
 */

public class InitServlet extends HttpServlet {
	/**
	 *  
	 */
	private static final long serialVersionUID = 1L;
	protected static Log logger = LogFactory.getLog(InitServlet.class);
	public static Timer timer = new Timer();

	/**
	 * 确认请求来自微信服务器
	 */
	public void init() {

		try {
			/*
			 * //logger.info("init"); Calendar calendar =
			 * Calendar.getInstance(); int year = calendar.get(Calendar.YEAR);
			 * int month = calendar.get(Calendar.MONTH); int day =
			 * calendar.get(Calendar.DAY_OF_MONTH);//每天 //定制每天的21:09:00执行，
			 * calendar.set(year, month, day, 10, 36, 00); Date date =
			 * calendar.getTime(); //logger.info(date);
			 * //System.out.println(date);
			 */  
			 
			TimerTask task = new MyTimerTask();
			int period = 1000 * 60*60;   
			// 每天的date时刻执行task，每隔60秒重复执行 
			timer.schedule(task,1000 * 10,period); 
			// 每天的date时刻执行task, 仅执行一次 
			// timer.schedule(task, date); 
		} catch (Exception e) {
			logger.info(e);
		}
	}

	public void destroy() {
		timer.cancel();
		logger.info("destroy");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) {

	}

	/**
	 * 处理微信服务器发来的消息
	 */

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 调用核心业务类接收消息、处理消息
		doGet(request, response);

		// 响应消息
	}

}
