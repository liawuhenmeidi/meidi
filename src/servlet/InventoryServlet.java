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
		//System.out.println("method"+method);  
		synchronized(object){
			if("add".equals(method)){ 
				saveBranch(request,response);
				return ;  
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
		  
		
		Connection conn = DB.getConn();   
		   
	    Statement sm = null;  
       try {   
           // 事务开始  
          logger.info("事物处理开始") ;
           conn.setAutoCommit(false);   // 设置连接不自动提交，即用该连接进行的操作都不更新到数据库  
            sm = conn.createStatement(); // 创建Statement对象  
            Object[] strsqls = sqls.toArray();
      logger.info(strsqls.toString());
           //依次执行传入的SQL语句      
           for (int i = 0; i < strsqls.length; i++) {  
               sm.execute((String)strsqls[i]);// 执行添加事物的语句  
           }  
           logger.info("提交事务处理！");  
              
           conn.commit();   // 提交给数据库处理  
              
           logger.info("事务处理结束！");  
           // 事务结束      
       //捕获执行SQL语句组中的异常      
       } catch (SQLException e) {  
           try {   
               logger.info("事务执行失败，进行回滚！\n",e);  
               conn.rollback(); // 若前面某条语句出现异常时，进行回滚，取消前面执行的所有操作  
           } catch (SQLException e1) {  
               logger.info(e);
           }  
       } finally {   
           try {
				sm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}  
       }  
		 
		

		try {
			response.sendRedirect("receipts.jsp");
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
		 
		InventoryManager.save(user, inventory); 
		
		
		try {
			response.sendRedirect("receipts.jsp");
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
