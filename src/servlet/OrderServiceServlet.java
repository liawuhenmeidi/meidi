package servlet;

import group.Group;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import order.Order;
import order.OrderManager;
import order.OrderService;
import user.User;
import utill.HttpRequestUtill;
import utill.StringUtill;
/**
 * 核心请求处理类
 * 
 * @author liufeng
 * @date 2013-05-18
 */
public class OrderServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 4440739483644821986L;
	 protected static Log logger = LogFactory.getLog(OrderServlet.class);
	/**
	 * 确认请求来自微信服务器
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		Object object = new Object();
		  
		String method = request.getParameter("method");
		
		if("GETLIST".equals(method)){
			GEtList(request,response);
		}
	
		synchronized(object){
			
		}
	}

    private void GEtList(HttpServletRequest request, HttpServletResponse response){
    	User user = (User)request.getSession().getAttribute("user");
    	String type = request.getParameter("type");
    	String statues = request.getParameter("statues");
    	String num = request.getParameter("num");
    	String page = request.getParameter("page");
    	String sort = request.getParameter("sort");
    	String sear = request.getParameter("searched");
    	if(StringUtill.isNull(sear)){
    		sear = "";
    	}else {  
    		sear = HttpRequestUtill.getSearch(request);
    	}
    	
    	List<Order> list = OrderManager.getOrderlist(user,Integer.valueOf(type),Integer.valueOf(statues),Integer.valueOf(num),Integer.valueOf(page),sort,sear);
    	 
    	int count = OrderManager.getOrderlistcount(user,Integer.valueOf(type),Integer.valueOf(statues),Integer.valueOf(num),Integer.valueOf(page),sort,sear);
    	
    	String html = "";
    	 
    	if(Integer.valueOf(type) == Group.dealSend ){
    		if(Integer.valueOf(statues) == Order.over){
    			html = OrderService.getHtmlOver(list);
    		}else if(Integer.valueOf(statues) == Order.serach){
    			html = OrderService.getHtmlSearch(user,list);
    		}else if(Integer.valueOf(statues) == Order.orderDispatching || Integer.valueOf(statues) == Order.returns || Integer.valueOf(statues) == Order.neworder || Integer.valueOf(statues) == Order.release){
    			html = OrderService.getHtmlDispatching(user,list);
    		}else if(Integer.valueOf(statues) == Order.callback){
    			html = OrderService.getHtmlcallback(user,list);
    		}else if(Integer.valueOf(statues) == Order.deliveryStatuesTuihuo){
    			html = OrderService.getHtmldeliveryStatuesTuihuo(user,list);
    		}else if(Integer.valueOf(statues) == Order.come || Integer.valueOf(statues) == Order.go || Integer.valueOf(statues) == Order.charge){
    			html = OrderService.getHtmlcome(user,list); 
    		} 
    	}
    	//
    	Map<String,String> map = new HashMap<String,String>();
    	
    	map.put("count", count+"");
    	
    	map.put("html", html);
    	
    	String strmap = StringUtill.GetJson(map);
    	
    	
    	try {
    		response.getWriter().write(strmap);  
        	response.getWriter().flush(); 
        	response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }
    
    
    
	/**
	 * 处理微信服务器发来的消息
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
 
		// 调用核心业务类接收消息、处理消息
		doGet(request,response);
 
		// 响应消息
	}
    
}
