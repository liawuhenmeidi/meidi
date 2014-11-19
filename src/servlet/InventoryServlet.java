package servlet;

import group.Group;
import inventory.Inventory;
import inventory.InventoryAnalyze;
import inventory.InventoryBranchManager;
import inventory.InventoryManager;
import inventory.InventoryMessage;

import java.io.IOException;
import java.util.ArrayList; 
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import branch.BranchManager;

import product.Product;
import product.ProductService;

import user.User;
import user.UserManager;
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
			}else if("delete".equals(method)){
				deleteBranch(request,response);
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
		String typemethod = request.getParameter("type");
		System.out.println(typemethod);
		String typebranch = request.getParameter("typebranch");
		 
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
			if(StringUtill.isNull(typebranch)){
				inven.setCount(Integer.valueOf(count));  
			}else {
				if("inbranch".equals(typebranch)){
					inven.setRealString(count);
					//inven.setCount(Integer.valueOf(count));  
				}else if("outbranch".equals(typebranch)){
					inven.setPaperString(count);
					//inven.setAnlycount(Integer.valueOf(count));	
				}
			}
			
			inventoryMessage.add(inven); 			
		}
		
		inventory.setId(Integer.valueOf(id)); 
		inventory.setInbranchid(inbranchid);
		inventory.setOutbranchid(outbranchid);
		inventory.setIntime(time);  
		inventory.setUid(uid);  
		inventory.setRemark(remark);
		inventory.setInventory(inventoryMessage);
		if("paper".equals(typemethod)){
			inventory.setIntype(3);
		}else {
			inventory.setIntype(1); 
		}
		
		InventoryManager.save(user, inventory); 
		
		
		try {
			response.sendRedirect("receipts.jsp");
		} catch (IOException e) {
		
			e.printStackTrace();
		}
	}  
     
     private void deleteBranch(HttpServletRequest request, HttpServletResponse response){
 	  
         String id = request.getParameter("id"); 
         
         if(!StringUtill.isNull(id)){ 
        	 InventoryManager.delete(Integer.valueOf(id));
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
 		
 		String starttime = request.getParameter("starttime");
 		
 		String endtime = request.getParameter("endtime");
 		 
 		InventoryManager.deleteBybranchid(Integer.valueOf(inbranch));
 		
 		
 		InventoryAnalyze in = new InventoryAnalyze();
 		
 		in.setStarttime(starttime);
 		
 		in.setEndtime(endtime);
 		
 		String remark = StringUtill.GetJson(in);
 
 		//System.out.println(remark );
 		//int outbranchid = BranchManager.getBranchID(outbranch);
 		//int inbranchid = BranchManager.getBranchID(inbranch); 
 		
 		//String remark = request.getParameter("remark");  
 		
 		String typebranch = request.getParameter("typebranch");
 		
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
 			if("outbranch".equals(typebranch)){
 				inven.setPaperString(count);
 	 		}else {
 	 			inven.setRealString(count);
 	 			//inven.setCount(countInt);   
 	 		} 

 			inventoryMessage.add(inven); 			
 		} 
 		
 		inventory.setId(Integer.valueOf(id));
 		
 		inventory.setInbranchid(Integer.valueOf(inbranch)); 

 		inventory.setIntime(time);   
 		inventory.setUid(uid);  
 		inventory.setRemark(remark);
 		inventory.setInventory(inventoryMessage); 
 		inventory.setIntype(2);  
 		InventoryManager.save(user, inventory); 
 		
 		try {
 			response.sendRedirect("inventoryAnalyze.jsp");
 			return ;
 		} catch (IOException e) {
 		
 			e.printStackTrace();
 		}
 	}  
    
     private void updatesubscribeBranch(HttpServletRequest request, HttpServletResponse response){
    	 User user  = (User)request.getSession().getAttribute("user");
    	 
  		String[] producs = request.getParameterValues("product"); 
  		
  		String add = request.getParameter("add");
  		List<String> sqls = new ArrayList<String>();
  		
  		for(int i=0;i<producs.length;i++){      
  			String type =producs[i];
  			if("inbranch".equals(add)){
	  			String count = request.getParameter(producs[i]);	
	  			String sql = "update inventorymessage set paperString = '" + count + "'   where id = " + type ;
	  		   
	  			String sql1 = "update inventory set instatues = 1 where id in (select inventoryId from inventorymessage where id = "+type+")"; 
	  			 sqls.add(sql);
	  			 sqls.add(sql1);
  			
  			}else if("outbranch".equals(add)){
  				String count = request.getParameter("real"+producs[i]);	
  				String sql1 = "update inventory set outstatues = 1 where id in (select inventoryId from inventorymessage where id = "+type+")"; 
	  			 
  				
	  			String sql = "update inventorymessage set realString = '" + count + "'    where id = " + type ;
	  			sqls.add(sql1);
	  			sqls.add(sql); 			
  			} 
  		}
  		
  	   DBUtill.sava(sqls);
  		
  		try {
  			if(UserManager.checkPermissions(user, Group.dealSend)){
  				String id = request.getParameter("id");
  				response.sendRedirect("print.jsp?id="+id+"&type=2");
  			}else {
  				response.sendRedirect("analyzrecepts.jsp");
  			}
  			
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
