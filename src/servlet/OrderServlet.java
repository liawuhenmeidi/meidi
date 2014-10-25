package servlet;

import gift.Gift;
import group.Group;
import group.GroupManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList; 
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import category.Category;
import category.CategoryManager;


import order.Order;
import order.OrderManager;
import orderPrint.OrderPrintln;
import orderPrint.OrderPrintlnManager;
import orderproduct.OrderProduct;
import orderproduct.OrderProductManager;
import product.Product;
import product.ProductService;

import user.User;
import user.UserManager;
import utill.StringUtill;
import utill.TimeUtill;
import utill.TokenGen;


 
/**
 * 核心请求处理类
 * 
 * @author liufeng
 * @date 2013-05-18
 */
public class OrderServlet extends HttpServlet {
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
	
		synchronized(object){
			if("println".equals(method)){
				savePrintln(request,response);
				return ; 
			}else if("huanhuo".equals(method)){ 
				HuanHuo(request,response);
			}else if("savehuanhuo".equals(method)){ 
				saveHuanhuo(request,response);
				// 
			}else {
				save(request,response);
			}
		}
	}
	
	private void savePrintln(HttpServletRequest request, HttpServletResponse response){
		String id = request.getParameter("id");
		String mm = request.getParameter("mm");
		User user  = (User)request.getSession().getAttribute("user");
logger.info(id); 
       
		String message = request.getParameter("message");
        if(""==id || null == id){
        	id = "0";
        }
logger.info(message);  
		OrderPrintln  or = new OrderPrintln();
		or.setOrderid(Integer.valueOf(id));
		or.setMessage(message);
		or.setStatues(OrderPrintln.comit);
		int pgroup = GroupManager.getGroup(user.getUsertype()).getPid();
		or.setpGroupId(pgroup);        
		if("tuihuo".equals(mm)){  
			or.setType(OrderPrintln.returns);   
		}else if("huanhuo".equals(mm)){
			or.setType(OrderPrintln.huanhuo);
		}else {
			or.setType(OrderPrintln.modify);
		}  
			OrderPrintlnManager.save(or);
		
	  try {
		response.sendRedirect("serch_list.jsp");
	} catch (IOException e) {

		e.printStackTrace();
	}
	}  
	
    private void save(HttpServletRequest request, HttpServletResponse response){
    	try{
    		 
    		TokenGen tokenGen=TokenGen.getInstance();
    	    if (!tokenGen.isTokenValid(request)){
    	       logger.info("这是重复提交的单据"); 
    	    }else{ 
	    	      //处理请求，并执行resetToken方法，将session中的token去除
	    	      User user  = (User)request.getSession().getAttribute("user");
	    	      boolean bflag = true ; 
	    	      boolean bbflag = true ; 
	    	      int devedity = -1 ;
	    	      
	    	     if(UserManager.checkPermissions(user, Group.sale) ){
			    	HashMap<Integer,Category> categorymap = CategoryManager.getCategoryMap();
			    	
			    	
					String submitTime= TimeUtill.gettime();
					 
					String pid = TimeUtill.getdatesimple(); 
			
			    	Order order= new Order(); 
			    
			        String id = request.getParameter("orderid");
			       
					String saledate = request.getParameter("saledate");
					String andate = request.getParameter("andate"); //安装日期
					 
					if(StringUtill.isNull(andate)){
						andate = TimeUtill.gettime(); 
					}
					String POS = request.getParameter("POS");
					if(StringUtill.isNull(POS)){
						POS = "空";
					}
					String sailId = request.getParameter("sailId");
					if(StringUtill.isNull(sailId)){
						sailId = "空";
					}
					String check = request.getParameter("check");
					if(StringUtill.isNull(check)){
						check = "空"; 
					}
					String phoneRemark = request.getParameter("phoneRemark"); 
					String posRemark = request.getParameter("posremark"); 
					String chekedRemark = request.getParameter("chekedremark"); 
					String sailidRemark = request.getParameter("sailidremark"); 
					List<OrderProduct> listp = new ArrayList<OrderProduct>();
					
					List<Gift> listg = new ArrayList<Gift>();
					
					if(StringUtill.isNull(id)){
					id = "0";
			        String radio = request.getParameter("Statues");
	        
					if("1".equals(radio)){
						
			            OrderProduct o = new OrderProduct();
						
			            String categoryId = request.getParameter("dingmaordercategory");
			              
						String saleType = request.getParameter("dingmatype");
				        
						Product p = ProductService.gettypemap().get(saleType);
						 
						if(p != null){
							saleType = p.getId()+""; 
						}else {
							request.getSession().setAttribute("message","对不起，您提交的型号被修改，请重新提交");  
							response.sendRedirect("../jieguo.jsp?type=order");
						}
						
						String dingmaproductNum = request.getParameter("dingmaproductNum");
						
						String categoryName = categorymap.get(Integer.valueOf(categoryId)).getName() ;
						o.setCategoryId(Integer.valueOf(categoryId));
						o.setSaleType(saleType);
						o.setCount(Integer.valueOf(dingmaproductNum));
						o.setStatues(1); 
						o.setCategoryName(categoryName); 
						listp.add(o);
					}
			
					String[] producs = request.getParameterValues("product");
					
					    // 送货状态   
					for(int i=0;i<producs.length;i++){		
						OrderProduct o = new OrderProduct();
						String categoryId = request.getParameter("ordercategory"+producs[i]);
						String categoryName = categorymap.get(Integer.valueOf(categoryId)).getName() ;
						//String categoryname = CategoryManager.getCategoryMap()
						String sendType = request.getParameter("ordertype"+producs[i]);
				         
						Product p = ProductService.gettypemap().get(sendType);
						   
						if(p != null){ 
							sendType = p.getId()+"";  
						}else {
							request.getSession().setAttribute("message","对不起，您提交的型号被修改，请重新提交");  
							response.sendRedirect("../jieguo.jsp?type=order");
						}
						
						
						String productNum = request.getParameter("orderproductNum"+producs[i]);
						// productsta
						String salestatues = request.getParameter("productsta"+producs[i]); 
						o.setCategoryId(Integer.valueOf(categoryId));
						o.setCount(Integer.valueOf(productNum));
						o.setSendType(sendType);
						o.setStatues(0); 
						o.setCategoryName(categoryName); 
						o.setSalestatues(Integer.valueOf(salestatues));
						
						//2 只安装门店提货    3 只安装顾客已提
						if(Integer.valueOf(salestatues) != 0){
							if(Integer.valueOf(salestatues) == 2){
								devedity = 9; 
							}else if(Integer.valueOf(salestatues) == 3){
								devedity = 10; 
								
							}else if(Integer.valueOf(salestatues) == 1){
								devedity = 0; 
							}
							bflag = false ; 
						}
						if(Integer.valueOf(salestatues) == 0){
							devedity = 8; 
							bbflag = false ;
						}
						listp.add(o);
						
					}
		 	 
			      String[] gifts = request.getParameterValues("gift");
			
					if(gifts != null){
						logger.info(gifts.length);		
						for(int i=0;i<gifts.length;i++){
							if("0".equals(gifts[i])){
								continue ;
							}
							
							Gift g = new Gift();
							String giftT = request.getParameter("giftT"+gifts[i]);
						
							String count = request.getParameter("giftCount"+gifts[i]);
			
							String giftStatues = request.getParameter("giftsta"+gifts[i]);
							
							g.setCount(Integer.valueOf(count ));
							g.setStatues(Integer.valueOf(giftStatues));
							g.setName(giftT);		
							listg.add(g);
						}
					} 
			}

					String username = request.getParameter("username");
					
					String diqu = request.getParameter("diqu");
				
					String phone1 = request.getParameter("phone1");
					
					String phone2 = request.getParameter("phone2");
					
					String locations = request.getParameter("locations");
					
					String remark = request.getParameter("remark");
					 
					order.setId(Integer.valueOf(id));
					order.setSaleTime(saledate);
			        order.setOdate(andate);
			        order.setPos(POS);
		logger.info(user.getId());
			        order.setSaleID(user.getId()); 
			        order.setBranch(Integer.valueOf(user.getBranch())); 
			        order.setSailId(sailId);
			        order.setCheck(check); 
			     	order.setUsername(username);
			     	order.setPhone1(phone1);
			     	order.setPhone2(phone2);
			     	order.setLocate(diqu);
			        order.setLocateDetail(locations);
			        order.setRemark(remark);
					order.setOrderproduct(listp);
					order.setOrdergift(listg); 
					order.setSubmitTime(submitTime);
					order.setPrintlnid(pid);   
					order.setPhoneRemark(Integer.valueOf(phoneRemark)); 
					order.setPosremark(Integer.valueOf(posRemark));
					order.setReckedremark(Integer.valueOf(chekedRemark));
					order.setSailidrecked(Integer.valueOf(sailidRemark));
					
					Order oldorder = OrderManager.getOrderID(user,order.getId());
					   
					if(oldorder == null){  
			           order.setDeliveryStatues(0); 
			           order.setOderStatus(devedity+"");
					}
					
		            if(bflag && bbflag && StringUtill.isNull(id)){
		            	request.getSession().setAttribute("message","已自提和需配送不能一起提交"); 
		            	response.sendRedirect("../jieguo.jsp?type=order");
		                return ;  
		            }   
		               
					boolean flag = OrderManager.save(user, order);  
					tokenGen.resetToken(request);  
					if(flag){     
						request.getSession().setAttribute("message","您的订单提交成功"); 
					}else { 
						request.getSession().setAttribute("message","对不起，您的订单提交失败，请您重新提交");  
					}   
					response.sendRedirect("../jieguo.jsp?type=order");
		    	} 
	       
	       }
    	}catch(Exception e){  
    		e.printStackTrace();
    		logger.info(e);
    	}
    }
    
    
    private void saveHuanhuo(HttpServletRequest request, HttpServletResponse response){
    	try{
    		 
    		TokenGen tokenGen=TokenGen.getInstance();
    	    if (!tokenGen.isTokenValid(request)){
    	       logger.info("这是重复提交的单据"); 
    	    }else{ 
	    	      //处理请求，并执行resetToken方法，将session中的token去除
	    	      User user  = (User)request.getSession().getAttribute("user");
	    	      
	    	     if(UserManager.checkPermissions(user, Group.sale) ){
			        String oid = request.getParameter("orderid");
			        
			    	Order oldOrder = OrderManager.getOrderID(user, Integer.valueOf(oid));
			    	Map<Integer,List<OrderProduct>> OrPMap = OrderProductManager.getOrderStatuesM(user);
			    	List<OrderProduct> listp = new ArrayList<OrderProduct>();  	
			    	List<OrderProduct> list = OrPMap.get(Integer.valueOf(oid));
				  
					String[] producs = request.getParameterValues("product");
					    // 送货状态   
					for(int i=0;i<producs.length;i++){	
						int opid = Integer.valueOf(producs[i]);
						logger.info(opid);
					    for(int j=0;j<list.size();j++){
					    	OrderProduct or = list.get(j);
					    	if(or.getStatues() == 0 && opid == or.getId()){
					    		int count = Integer.valueOf(request.getParameter("orderproductNum"+opid));
					    		or.setCount(count);
					    		or.setSalestatues(1); 
					    		listp.add(or);
					    	}
					    }						
					}
					
					Order order= new Order(); 
					
					String andate = request.getParameter("andate"); //安装日期
					
					String username = request.getParameter("username");
					
					String diqu = request.getParameter("diqu");
				
					String phone1 = request.getParameter("phone1");
					
					String phone2 = request.getParameter("phone2");
					
					String locations = request.getParameter("locations");
					
					String remark = request.getParameter("remark");
					 
					order.setId(0);
					
					order.setSaleTime(oldOrder.getSaleTime());
			        order.setOdate(andate);

			        order.setPos(oldOrder.getPos());
				     
			        order.setSaleID(oldOrder.getSaleID()); 
			        order.setBranch(oldOrder.getBranch());
			        order.setSailId(oldOrder.getSailId());
			        order.setCheck(oldOrder.getCheck());
			        
			     	order.setUsername(username);
			     	order.setPhone1(phone1);
			     	order.setPhone2(phone2);
			     	order.setLocate(diqu);
			        order.setLocateDetail(locations);

					order.setRemark(remark+".换货单，务必拉回残机。");
				    order.setOderStatus(20+"");
					order.setOrderproduct(listp);
					
					order.setSubmitTime(oldOrder.getSubmitTime());
					order.setPrintlnid(oldOrder.getPrintlnid());  
					order.setDeliveryStatues(0);  
	                
					order.setImagerUrl(oldOrder.getId()+"");
					
					boolean flag = OrderManager.save(user, order);  
					tokenGen.resetToken(request);  
					if(flag){     
						request.getSession().setAttribute("message","您的订单提交成功"); 
					}else { 
						request.getSession().setAttribute("message","对不起，您的订单提交失败，请您重新提交");  
					}   
					response.sendRedirect("../jieguo.jsp?type=order");
		    	} 
	       }
    	}catch(Exception e){  
    		e.printStackTrace();
    		logger.info(e);
    	}
    }
    
    
    
    private void HuanHuo(HttpServletRequest request, HttpServletResponse response){
    	String id = request.getParameter("id");
    	try {
			response.sendRedirect("huanhuo.jsp?id="+id);
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
