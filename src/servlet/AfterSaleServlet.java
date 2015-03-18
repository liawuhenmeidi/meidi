package servlet;


import group.Group;
import inventory.InventoryBranchManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;

import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import order.Order;
import order.OrderManager;
import orderPrint.OrderPrintln;
import orderPrint.OrderPrintlnManager;
import orderPrint.OrderPrintlnService;
import orderproduct.OrderProductManager;

import user.User;
import user.UserManager;
import utill.DBUtill;
import utill.StringUtill;
import utill.TimeUtill;
 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import aftersale.AfterSaleProduct;
import aftersale.AfterSaleProductManager;
import aftersale.AftersaleAll;
import aftersale.AftersaleAllManager;
 
/** 
 * 核心请求处理类
 * 
 * @author 
 * @date 2013-05-18
 */ 

public class AfterSaleServlet extends HttpServlet {
	private static final long serialVersionUID = 4440739483644821986L;
	 
	protected static Log logger = LogFactory.getLog(AfterSaleServlet.class); 
	/**
	 * 确认请求来自微信服务器
	 */
	 
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		   
		 
		String method = request.getParameter("method");
		
		String uid = request.getParameter("uid"); 
		String afid = request.getParameter("afid");
        logger.info(method); 
		
		User user = (User)request.getSession().getAttribute("user");
		 int statues = -1 ;
	    if("dealsend".equals(method)){
	    	if(dealsend(user,uid,afid)){
	    		statues = 1;
	    	};
	    }else if("deal".equals(method)){
	    	if(deal(user,uid,afid)){
	    		statues = 1;
	    	};
	    }else if("maintain".equals(method)){
	    	String sta = request.getParameter("statues");
	    	String message = request.getParameter("message");
	    	String dealresult = request.getParameter("dealresult");
	    	
	    	String barcode = request.getParameter("barcode");
	    	String batchNumber = request.getParameter("batchNumber");
	    	 
	    	logger.info(barcode+batchNumber);
	    	
	    	if(maintain(user,sta,afid,message,dealresult,barcode,batchNumber)){ 
	    		statues = 1;
	    	};
	    }else if("updatecharge".equals(method)){
	    	String id = request.getParameter("id");
	    	//logger.info(id);
	    	AftersaleAllManager.updatechager(id );
	    	try { 
				response.sendRedirect("admin/afterSale/dingdanAfterSalecharge.jsp");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	    	return ;
	    }else if("cannotupdate".equals(method)){
	    	String id = request.getParameter("id");
	    	//logger.info(id); 
	    	AftersaleAllManager.updatecannotup(id );
	    	try {  
				response.sendRedirect("admin/afterSale/dingdanAfterSale.jsp");
			} catch (IOException e) { 
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	    	return ;
	    }else if("del".equals(method)){
	    	String id = request.getParameter("id");
	    	//logger.info(id); 
	    	AftersaleAllManager.delete(id ); 
	    	try {  
				response.sendRedirect("admin/afterSale/dingdanAfterSalephone.jsp");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	    	return ;
	    }else if("updatestatues".equals(method)){
	    	String id = request.getParameter("id"); 
	    	String sta = request.getParameter("statues");
	    	//logger.info(id); 
	    	AftersaleAllManager.updatestatues(id ,sta);    
	    	try {  
				response.sendRedirect("admin/afterSale/dingdanAfterSalemaintaindealsend.jsp");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	    	return ;  
	    }else if("updatestatuesdeal".equals(method)){
	    	String id = request.getParameter("id");  
	    	int sta = Integer.valueOf(request.getParameter("statues"));
	    	AftersaleAllManager.updatestatuesdeal(id ,sta);    
	    	  
	    }else if("updatestatuesdealsend".equals(method)){
	    	String id = request.getParameter("id");  
	    	int sta = Integer.valueOf(request.getParameter("statues"));
	    	AftersaleAllManager.updatestatuesdealsend(id ,sta);    
	    	
	    }else if("uploadmatian".equals(method)){ 
	    	String id = request.getParameter("id");  
	    	AftersaleAllManager.updatestatuesmatain(id);    
	    	  
	    }else if("chargematian".equals(method)){ 
	    	String id = request.getParameter("id");  
	    	AftersaleAllManager.chargestatuesmatain(id);    
	    }        	
	    
		try {
			response.getWriter().write(""+statues);
			response.getWriter().flush(); 
			response.getWriter().close(); 
		} catch (IOException e) {
			logger.info(e);
		}
    }
	/**
	 * 处理微信服务器发来的消息
	 */
	   
	public synchronized static boolean dealsend(User user,String uid,String afid){
		AftersaleAll af = AftersaleAllManager.getAfterSaleID(user, afid);
		List<AfterSaleProduct> list = af.getAsplist();    
		List<String> sqls = AfterSaleProductManager.getupdateSend(list, uid);  
		boolean count = DBUtill.sava(sqls);  
		return count ;
		 
	}
	
	public synchronized static boolean deal(User user,String uid,String afid){
		AftersaleAll af = AftersaleAllManager.getAfterSaleID(user, afid);
		List<AfterSaleProduct> list = af.getAsplist();    
		List<String> sqls = AfterSaleProductManager.getupdateDeal(list, uid);  
		boolean count = DBUtill.sava(sqls);  
		return count ;
		 
	} 
	 
	public synchronized static boolean maintain(User user,String statues,String afid,String message,String cause,String barcode,String batchNumber){
		AftersaleAll af = AftersaleAllManager.getAfterSaleID(user, afid);
		List<AfterSaleProduct> list = af.getAsplist();        
		List<String> sqls = AfterSaleProductManager.getupdatemaintain(list, statues,message,cause,barcode,batchNumber);  
		boolean count = DBUtill.sava(sqls);   
		return count ;
		 
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			 {
		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		// 调用核心业务类接收消息、处理消息
		doGet(request,response);

		// 响应消息
	}
    
}
