package servlet;

import gift.Gift;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList; 
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import category.CategoryManager;

import order.Order;
import order.OrderManager;
import order.OrderStatues;
import orderPrint.OrderPrintln;
import orderPrint.OrderPrintlnManager;
import orderproduct.OrderProduct;

import user.User;



/**
 * 核心请求处理类
 * 
 * @author liufeng
 * @date 2013-05-18
 */
public class OrderServletbat1 extends HttpServlet {
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
		if("println".equals(method)){
			savePrintln(request,response);
			return ;
		}else{  
		  save(request,response);
		}
	}
	
	public void savePrintln(HttpServletRequest request, HttpServletResponse response){
		String id = request.getParameter("id");
		String mm = request.getParameter("mm");
		System.out.println(id);
		String message = request.getParameter("message");
        if(""==id || null == id){
        	id = "0";
        }
        System.out.println(message);
		OrderPrintln  or = new OrderPrintln();
		or.setOrderid(Integer.valueOf(id));
		or.setMessage(message);
		or.setStatues(OrderPrintln.comit);
		if("tuihuo".equals(mm)){
			or.setType(1);
		}else {
			or.setType(0);
		}
		OrderPrintlnManager.save(or);
	  try {
		response.sendRedirect("serch_list.jsp");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	
    public void save(HttpServletRequest request, HttpServletResponse response) throws IOException{
    	try{
    	User user  = (User)request.getSession().getAttribute("user");
    	Order order= new Order();

        String id = request.getParameter("id");
        if(""==id || null == id){
        	id = "0";
        }
        
        
        //SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Date date1 = new Date();
		//String saletime = df2.format(date1);
        
		String saledate = request.getParameter("saledate");
		String andate = request.getParameter("andate"); //安装日期
		String POS = request.getParameter("POS");
		String sailId = request.getParameter("sailId");
		String check = request.getParameter("check");
		String categoryID = request.getParameter("category");
		  
		List<OrderProduct> listp = new ArrayList<OrderProduct>();
		List<Gift> listg = new ArrayList<Gift>();
		
        String radio = request.getParameter("Statues");
        
       
        
		if("1".equals(radio)){
			OrderProduct oo = new OrderProduct();
			String saleType = request.getParameter("dingmatype");
			String productNum = request.getParameter("dingmaproductNum");
			oo.setCount(Integer.valueOf(productNum));
			oo.setSaleType(saleType);
			oo.setStatues(1);
logger.info(oo.getStatues());		
			listp.add(oo);
		} 
		  
		String[] producs = request.getParameterValues("product");

		for(int i=0;i<producs.length;i++){
			
			OrderProduct o = new OrderProduct();
			String sendType = request.getParameter("ordertype"+producs[i]);
	
			String productNum = request.getParameter("orderproductNum"+producs[i]);
		
			o.setCount(Integer.valueOf(productNum));
			o.setSendType(sendType);
			o.setStatues(0);
			listp.add(o);
		}
	 	 
		String[] gifts = request.getParameterValues("gift");
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
  
		String username = request.getParameter("username");
		//System.out.println(username); 
		String diqu = request.getParameter("diqu");
		//System.out.println(diqu);
		String phone1 = request.getParameter("phone1");
		//System.out.println(phone1);
		String phone2 = request.getParameter("phone2");
		//System.out.println(phone2);
		String locations = request.getParameter("locations");
		//System.out.println(locations);
		String remark = request.getParameter("remark");
		//System.out.println(remark);
		order.setId(Integer.valueOf(id));
		order.setSaleTime(saledate);
        order.setOdate(andate);
        order.setPos(POS);
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
		order.setCategoryID(categoryID);
		OrderManager.save(user, order); 
        response.sendRedirect("welcom.jsp");
    	}catch(Exception e){ 
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
