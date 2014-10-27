package servlet;

import inventory.Inventory;
import inventory.InventoryBranchManager;
import inventory.InventoryManager;
import inventory.InventoryMessage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList; 
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import branch.BranchManager;

import database.DB;

import product.Product;
import product.ProductManager;
import product.ProductService;

import user.User;
import utill.DBUtill;
import utill.StringUtill;
import utill.TimeUtill;


 
/**
 * 核心请求处理类
 * 
 * @author liufeng
 * @date 2013-05-18
 */  
public class InventoryServlet extends HttpServlet {
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
		System.out.println("method"+method);  
		synchronized(object){
			if("add".equals(method)){ 
				saveBranch(request,response);
				return ;  
			}else if("addsubscribe".equals(method)){
				savesubscribeBranch(request,response);
			}else if("updatesubscribe".equals(method)){
				updatesubscribeBranch(request,response);
			}else if("outbranch".equals(method) || "inbranch".equals(method)){ 
				saveInventory(request,response);
				return ; 
			}{ 
			  //save(request,response);
			}
		}
	} 
	 
	private void saveInventory(HttpServletRequest request, HttpServletResponse response){
		List<String> sqls = new ArrayList<String>(); 
		User user  = (User)request.getSession().getAttribute("user");
		String method = request.getParameter("method");
		String id = request.getParameter("id");
		String type = request.getParameter("type");
System.out.println(type+"type"); 
		String sql = ""; 
		boolean flag =  InventoryManager.check(method,id); 
		if(method.equals("outbranch")){
			sql = "update inventory set outstatues = 1 where id = "+ id ;
		}else if(method.equals("inbranch")){ 
			sql = "update inventory set instatues = 1 where id = "+ id ;
		}
		   
		sqls.add(sql); 
		
		if(flag){ 
			Inventory inventory = InventoryManager.getInventoryID(user, Integer.valueOf(id));  
			 
			List<String> sqlp = InventoryBranchManager.save(user, inventory);
			
			 
			sqls.addAll(sqlp);
		}
		  
		DBUtill.sava(sqls);
		
		try {
			
			if("phone".equals(type)){
				response.sendRedirect("../../user/inventory/receipts.jsp");
			}else {
				response.sendRedirect("receipts.jsp");
			}
			
		} catch (IOException e) {
		
			e.printStackTrace();
		}
	}  
	
     private void saveBranch(HttpServletRequest request, HttpServletResponse response){
		
		User user  = (User)request.getSession().getAttribute("user");
		 
		Inventory inventory = new Inventory();
		String time = TimeUtill.gettime();
		int uid = user.getId();    
        String id = request.getParameter("id"); 
        if(StringUtill.isNull(id)){
        	id = -1+"" ;  
        }
        
		String outbranch = request.getParameter("outbranch");
		String inbranch = request.getParameter("inbranch");
		 
		int outbranchid = BranchManager.getBranchID(outbranch);
		int inbranchid = BranchManager.getBranchID(inbranch); 
		String remark = request.getParameter("remark");   
		  
		List<InventoryMessage> inventoryMessage = new ArrayList<InventoryMessage>();
		  
		String[] producs = request.getParameterValues("product"); 
		
		
		for(int i=0;i<producs.length;i++){      
			InventoryMessage inven = new InventoryMessage();  
			String type = request.getParameter("orderproductType"+producs[i]);
			Product p = ProductService.gettypemap().get(type);
			int categoryId = p.getCategoryID();
			type = p.getId()+""; 
			String count = request.getParameter("orderproductNum"+producs[i]);
			inven.setProductId(type);   
			inven.setCategoryId(categoryId);  
			inven.setCount(Integer.valueOf(count));   
			inventoryMessage.add(inven); 			
		}
		
		inventory.setId(Integer.valueOf(id)); 
		inventory.setInbranchid(inbranchid);
		inventory.setOutbranchid(outbranchid);
		inventory.setIntime(time);  
		inventory.setUid(uid);  
		inventory.setRemark(remark);
		inventory.setInventory(inventoryMessage); 
		inventory.setIntype(1); 
		InventoryManager.save(user, inventory); 
		
		
		try {
			response.sendRedirect("receipts.jsp");
		} catch (IOException e) {
		
			e.printStackTrace();
		}
	}  

     private void savesubscribeBranch(HttpServletRequest request, HttpServletResponse response){
 		
 		User user  = (User)request.getSession().getAttribute("user");
 		 
 		Inventory inventory = new Inventory();
 		String time = TimeUtill.gettime();
 		int uid = user.getId();
 		
         String id = request.getParameter("id"); 
         if(StringUtill.isNull(id)){
         	id = -1+"" ;  
         }
         
 		//String outbranch = request.getParameter("outbranch");
 		String inbranch = request.getParameter("inbranch");
 		 
 		//int outbranchid = BranchManager.getBranchID(outbranch);
 		//int inbranchid = BranchManager.getBranchID(inbranch); 
 		
 		//String remark = request.getParameter("remark");   
 		  
 		List<InventoryMessage> inventoryMessage = new ArrayList<InventoryMessage>();
 		  
 		String[] producs = request.getParameterValues("product"); 
 		
 		
 		for(int i=0;i<producs.length;i++){      
 			InventoryMessage inven = new InventoryMessage();  
 			String type =producs[i];
 			Product p = ProductService.getIDmap().get(Integer.valueOf(type));
 			int categoryId = p.getCategoryID();
 			type = p.getId()+""; 
 			String count = request.getParameter(producs[i]);
 			inven.setProductId(type);   
 			inven.setCategoryId(categoryId);  
 			inven.setCount(Integer.valueOf(count));   
 			inventoryMessage.add(inven); 			
 		}
 		
 		inventory.setId(Integer.valueOf(id)); 
 		inventory.setInbranchid(Integer.valueOf(inbranch)); 
 		inventory.setOutbranchid(Integer.valueOf(user.getBranch()));
 		inventory.setIntime(time);   
 		inventory.setUid(uid);  
 		//inventory.setRemark(remark);
 		inventory.setInventory(inventoryMessage); 
 		inventory.setIntype(2);  
 		InventoryManager.save(user, inventory); 
 		
 		try {
 			response.sendRedirect("analyzrecepts.jsp");
 			return ;
 		} catch (IOException e) {
 		
 			e.printStackTrace();
 		}
 	}  
    
     private void updatesubscribeBranch(HttpServletRequest request, HttpServletResponse response){
  		
  		String[] producs = request.getParameterValues("product"); 
  		
  		List<String> sqls = new ArrayList<String>();
  		
  		for(int i=0;i<producs.length;i++){      
  			String type =producs[i];
  			String count = request.getParameter(producs[i]);
  					
  			String sql = "update inventorymessage set anlycount = " + count + "   where id = " + type ;
  		   
  			String sql1 = "update inventory set instatues = 1 where id in (select inventoryId from inventorymessage where id = "+type+")";
  			 sqls.add(sql);
  			 sqls.add(sql1);
  		 
  		}
  		
  	   DBUtill.sava(sqls);
  		
  		try {
  			response.sendRedirect("analyzrecepts.jsp");
  			return ;
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
