package servlet;

import group.Group;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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
		  
		String method = request.getParameter("method");
		
		if("GETLIST".equals(method)){
			GEtList(request,response);
		}
	}

    private void GEtList(HttpServletRequest request, HttpServletResponse response){
    	User user = (User)request.getSession().getAttribute("user");
    	
    	String type = request.getParameter("type");
    	String statues = request.getParameter("statues");
    	String num = request.getParameter("num");
    	String page = request.getParameter("page");
    	String sort = request.getParameter("sort");
    	String search = request.getParameter("searched");
    	String sear = "";
    	if(!StringUtill.isNull(search)){ 
    		sear = HttpRequestUtill.getSearch(request);
    	}
    	 
    	//if(!StringUtill.isNull(sear)){  
    		//Cookie cookie=new Cookie("sear",search);
			//cookie.setMaxAge(10*60);  //设置过期之前的最长时间
			//response.addCookie(cookie); 
    	//}
    	
    	List<Order> list = OrderManager.getOrderlist(user,Integer.valueOf(type),Integer.valueOf(statues),Integer.valueOf(num),Integer.valueOf(page),sort,sear);
    	 
    	int count = OrderManager.getOrderlistcount(user,Integer.valueOf(type),Integer.valueOf(statues),Integer.valueOf(num),Integer.valueOf(page),sort,sear);
    	
    	String html = "";
    	 
    	if(Integer.valueOf(type) == Group.dealSend ){
    		if(Integer.valueOf(statues) == Order.over){
    			html = OrderService.getHtmlOver(list);
    		}else if(Integer.valueOf(statues) == Order.serach){
    			long start = System.currentTimeMillis();
    			html = OrderService.getHtmlSearch(user,list);
    			logger.info(System.currentTimeMillis() - start); 
    		}else if(Integer.valueOf(statues) == Order.orderDispatching || Integer.valueOf(statues) == Order.returns || Integer.valueOf(statues) == Order.neworder || Integer.valueOf(statues) == Order.release){
    			html = OrderService.getHtmlDispatching(user,list);
    		}else if(Integer.valueOf(statues) == Order.callback){
    			html = OrderService.getHtmlcallback(user,list);
    		}else if(Integer.valueOf(statues) == Order.deliveryStatuesTuihuo){
    			html = OrderService.getHtmldeliveryStatuesTuihuo(user,list);
    		}else if(Integer.valueOf(statues) == Order.come){
    			html = OrderService.getHtmlcome(user,list); 
    		}else if(Integer.valueOf(statues) == Order.go){ 
    			html = OrderService.getHtmlgo(user,list); 
    		}else if(Integer.valueOf(statues) == Order.charge){ 
    			html = OrderService.getHtmlcharge(user,list); 
    		}    
    	}else if(Integer.valueOf(type) == Group.sencondDealsend){
    		
    		if(Integer.valueOf(statues) == Order.orderDispatching){
    			html = OrderService.getHtmlSecondorderDispatching(user,list);
    		}else if(Integer.valueOf(statues) == Order.orderquery){
    			html = OrderService.getHtmlorderquery(user,list); 
    		}else if(Integer.valueOf(statues) == Order.porderDispatching){
    			html = OrderService.getHtmlporderDispatching(user,list);  
    		}else if(Integer.valueOf(statues) == Order.serach){
    			html = OrderService.getHtmlpSearch(user,list);   
    		}else if(Integer.valueOf(statues) == Order.charge){
    			html = OrderService.getHtmlpcharge(user,list);   
    		}else if(Integer.valueOf(statues) == Order.chargeall){
    			html = OrderService.getHtmlpchargeall(user,list);   
    		}else if(Integer.valueOf(statues) == Order.pcharge){
    			html = OrderService.getHtmlppcharge(user,list);    
    		}else if(Integer.valueOf(statues) == Order.callback){
    			html = OrderService.getHtmlpcallback(user,list);    
    		} 
    	}
    	//Order.serach
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
