package inventory;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import branch.Branch;
import branch.BranchManager;
import branch.BranchService;



import order.Order;
import order.OrderManager;

import orderproduct.OrderProduct;
import orderproduct.OrderProductManager;
import product.ProductService;

import user.User;
import user.UserManager;
import utill.StringUtill;
import utill.TimeUtill;

import database.DB;

public class InventoryBranchManager {  
	protected static Log logger = LogFactory.getLog(InventoryBranchManager.class);
	
	public static List<InventoryBranch> getCategory(String branch , String type) { 
		
		List<InventoryBranch> categorys = new ArrayList<InventoryBranch>();
		Connection conn = DB.getConn();
		String sql = "";
		if(!StringUtill.isNull(type) && !StringUtill.isNull(branch)){ 
			//type = ProductService.gettypemap().get(type).getId()+""; 
			sql = "select * from mdinventorybranch where type = '"+type +"' and branchid in (select id from mdbranch where bname = '"+branch+"') and branchid not in (select id from mdbranch where statues = 1 )";  
		}else if(!StringUtill.isNull(type) && StringUtill.isNull(branch)){
			//type = ProductService.gettypemap().get(type).getId()+""; 
			sql = "select * from mdinventorybranch where type = '"+type +"' and branchid not in (select id from mdbranch where statues = 1 )";  
		}else if(StringUtill.isNull(type) && !StringUtill.isNull(branch)){ 
			sql = "select * from mdinventorybranch where  branchid in ("+branch+") and branchid not in (select id from mdbranch where statues = 1 )";  
		}else if(StringUtill.isNull(type) && StringUtill.isNull(branch)){
			sql = "select * from mdinventorybranch where branchid not in (select id from mdbranch where statues = 1 )";    
		}
	logger.info(sql);	
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {   
			while (rs.next()) {
				InventoryBranch u = getCategoryFromRs(rs);
				categorys.add(u);
			}
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		} 
		logger.info(categorys.size());
		return categorys;
	} 
	 

public static List<InventoryBranch> getCategoryid(String branch , String categoryid) {  
		//System.out.println(branch);
		List<InventoryBranch> categorys = new ArrayList<InventoryBranch>();
		Connection conn = DB.getConn();
		String sql = ""; 
		if(!StringUtill.isNull(categoryid) && !StringUtill.isNull(branch)){
			sql = "select * from mdinventorybranch where inventoryid = '"+categoryid +"' and branchid in ("+branch+")  and branchid not in (select id from mdbranch where statues = 1 ) order by  id desc";  
		}else if(!StringUtill.isNull(categoryid) && StringUtill.isNull(branch)){
			sql = "select * from mdinventorybranch where inventoryid = '"+categoryid +"' and branchid not in (select id from mdbranch where statues = 1 ) order by  id desc";  
		}else if(StringUtill.isNull(categoryid) && !StringUtill.isNull(branch)){
			sql = "select * from mdinventorybranch where  branchid in ("+branch+") and branchid not in (select id from mdbranch where statues = 1 ) order by  id desc";  
		}else if(StringUtill.isNull(categoryid) && StringUtill.isNull(branch)){
			sql = "select * from mdinventorybranch where 1= 1 and branchid not in (select id from mdbranch where statues = 1 ) order by  id desc";    
		}  
	logger.info(sql);	
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {   
			while (rs.next()) {
				InventoryBranch u = getCategoryFromRs(rs);
				categorys.add(u);
			}
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		} 
		logger.info(categorys.size());
		return categorys;
	}
	
	public static List<String> save(User user,Inventory inventory) {
		 
	     List<String> sqls = new ArrayList<String>(); 
         int inbranchid = -1; 
         int outbranchid = -1; 

		 String time = TimeUtill.gettime();

	       outbranchid = inventory.getOutbranchid();
       
	   
	       inbranchid = inventory.getInbranchid();
	      
	 
      
		  
		 List<InventoryMessage> orders = inventory.getInventory() ;
		    
			 for(int i=0;i<orders.size();i++){ 
               String insql = "";
               String insql1 = "";
               String outsql = "";
               String outsql1 = "";  
               
               InventoryMessage order = orders.get(i);
               
				if(getInventoryID(user,inbranchid,order.getProductId()) == null){

						insql = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" + 
		                         "  values ( null,"+order.getCategoryId()+", '"+order.getProductId()+"', '"+order.getCount()+"', '"+order.getCount()+"',"+inbranchid+")";
					
						insql1 = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" + 
		                        "  values ( null, '"+inbranchid+"', '"+inventory.getId()+"','"+time+"','"+order.getProductId()+"',"+order.getCount()+","+1+","+order.getCount()+","+order.getCount()+")";    

				}else { 
					 
						insql = "update mdinventorybranch set realcount =  ((mdinventorybranch.realcount)*1 + "+ order.getCount() + ")*1 , papercount =  ((mdinventorybranch.papercount)*1 + "+ order.getCount() + ")*1  where  branchid = " +inbranchid + " and  type = '"+order.getProductId()+"'"; 
					
						insql1 = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" +     
			                        "  values ( null, '"+inbranchid+"', '"+inventory.getId()+"','"+time+"','"+order.getProductId()+"',"+order.getCount()+","+1+",(select realcount from mdinventorybranch where branchid = " +inbranchid + " and  type = '"+order.getProductId()+"')*1,(select papercount from mdinventorybranch where branchid = " +inbranchid + " and  type = '"+order.getProductId()+"')*1)"; 
					
	     
				}
				
                 if(getInventoryID(user,outbranchid,order.getProductId()) == null){
					
					
					
						outsql = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" + 
		                         "  values ( null,"+order.getCategoryId()+",'"+order.getProductId()+"', '-"+order.getCount()+"', '-"+order.getCount()+"',"+outbranchid+")";
					   
						outsql1 = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" + 
		                        "  values ( null, '"+outbranchid+"', '"+inventory.getId()+"','"+time+"','"+order.getProductId()+"',"+order.getCount()+","+0+",'-"+order.getCount()+"','-"+order.getCount()+"')";    
					
				}else { 
					   
					
						outsql = "update mdinventorybranch set realcount =  ((mdinventorybranch.realcount)*1 - "+ order.getCount() + ")*1  , papercount =  ((mdinventorybranch.papercount)*1 - "+ order.getCount() + ")*1  where  branchid = " +outbranchid + " and  type = '"+order.getProductId()+"'"; 
						outsql1 = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" +       
		                        "  values ( null, '"+outbranchid+"', '"+inventory.getId()+"','"+time+"','"+order.getProductId()+"',"+order.getCount()+","+0+",(select realcount from mdinventorybranch where branchid = " +outbranchid + " and  type = '"+order.getProductId()+"')*1,(select papercount from mdinventorybranch where branchid = " +outbranchid + " and  type = '"+order.getProductId()+"')*1)"; 
				  
	     
				}

				sqls.add(insql); 
				sqls.add(insql1);
				sqls.add(outsql); 
				sqls.add(outsql1);   
				logger.info(insql+"%%"+outsql); 
				logger.info(insql1+"%%"+outsql1);  
			} 
			 return sqls; 
	   } 
	 
	public static List<String> chage(User user ,String method , int uid ,String oid ){
		List<String> listsql = new ArrayList<String>(); 
		List<OrderProduct> listp = OrderProductManager.getOrderStatues(user, Integer.valueOf(oid)); 
		logger.info("uid****"+uid);
	    String time = TimeUtill.gettime();
	    for(int i=0;i<listp.size();i++){      
	    	OrderProduct or = listp.get(i);
	    	if(or.getStatues() == 0){     
		    	if(or.getSalestatues() == 2 || or.getSalestatues() == 3 || or.getSalestatues() == 0 ){     
		    		Order order = OrderManager.getOrderID(user, Integer.valueOf(oid));
		    		uid = order.getSaleID(); 
		    		logger.info("uid****"+uid);
		    	} 
		    	User u = UserManager.getUesrByID(uid+"");
		    	//Branch branch = BranchManager.getLocatebyname(u.getBranch());
		    	Branch branch = BranchService.getMap().get(Integer.valueOf(u.getBranch())); 
		    	String sqlnew = "";  
	    		String sql = "";        
		    		if("peidan".equals(method)){
		    			if(or.getSalestatues() == 3 || or.getSalestatues() == 0 ){     
				    		Order order = OrderManager.getOrderID(user, Integer.valueOf(oid));
				    		uid = order.getSaleID(); 
				    		logger.info("uid****"+uid);
				    	    if(getInventoryID(user,branch.getId(),or.getSendType()) == null){ 
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" + 
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"','-"+or.getCount()+"', '-"+or.getCount()+"',"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" +   
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+Order.orderDispatching+",'-"+or.getCount()+"', '-"+or.getCount()+"')";
			    			 }else {  
			    				sqlnew = "update mdinventorybranch set papercount =  (mdinventorybranch.papercount - "+or.getCount()+")*1 , realcount =  (mdinventorybranch.realcount - "+or.getCount()+")*1  where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'";  
			    			                 
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+Order.orderDispatching+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 )";    
			    			 } 
				    	}else if(or.getSalestatues() == 2){     

				    	    if(getInventoryID(user,branch.getId(),or.getSendType()) == null){ 
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" +  
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"',0, '-"+or.getCount()+"',"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" +   
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+Order.orderDispatching+",0, '-"+or.getCount()+"')";
			    			 }else {     
			    				sqlnew = "update mdinventorybranch set papercount =  (mdinventorybranch.papercount - "+or.getCount()+")*1  where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'";  
			    			                     
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+Order.orderDispatching+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 )";    
			    			 } 
				    	}else {
			    			 if(getInventoryID(user,branch.getId(),or.getSendType()) == null){ 
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" + 
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"',0, '-"+or.getCount()+"',"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" +   
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+Order.orderDispatching+",0, '-"+or.getCount()+"')";
			    			 }else {  
			    				sqlnew = "update mdinventorybranch set papercount =  (mdinventorybranch.papercount - "+or.getCount()+")*1 where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'";  
			    			 
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+Order.orderDispatching+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 )";    
			    			 } 
				    	} 
		    		}else if((Order.orderpeisong+"").equals(method) || (Order.ordersong+"").equals(method)){     
		    			logger.info(Order.orderpeisong);       
		    			if(or.getSalestatues() == 3 || or.getSalestatues() == 0 ){     
				    		
				    	}else if(or.getSalestatues() == 2){                       
				    		if(getInventoryID(user,branch.getId(),or.getSendType()) == null){ 
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" +  
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"','-"+or.getCount()+"', 0,"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+Order.porderDispatching+",'-"+or.getCount()+"', 0)";
			    			 }else {  
			    				 sqlnew = "update mdinventorybranch set realcount =  (mdinventorybranch.realcount - "+or.getCount()+")*1 where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'"; 
			    			 
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" +   
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+Order.porderDispatching+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 )";    
			    			 }   
		    			}else {  
			    			if(getInventoryID(user,branch.getId(),or.getSendType()) == null){ 
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" +  
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"','-"+or.getCount()+"', 0,"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+Order.porderDispatching+",'-"+or.getCount()+"', 0)";
			    			 }else {  
			    				 sqlnew = "update mdinventorybranch set realcount =  (mdinventorybranch.realcount - "+or.getCount()+")*1 where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'"; 
			    			 
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" +   
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+Order.porderDispatching+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 )";    
			    			 }      
		    			} 
		    		}else if("shifang".equals(method)){
		    			if(or.getSalestatues() == 2){     
		    				/*if(getInventoryID(user,branch.getId(),or.getSendType()) == null){ 
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" + 
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"',0, '"+or.getCount()+"',"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+Order.release+",0, '-"+or.getCount()+"')";
			    			 }else {  
			    				 sqlnew = "update mdinventorybranch set papercount =  (mdinventorybranch.papercount + "+or.getCount()+")*1 where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'"; 
			    			 
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" +  
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+Order.release+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 )";    
			    			 }   */
		    			}else {  
			    			if(getInventoryID(user,branch.getId(),or.getSendType()) == null){ 
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" + 
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"',0, '"+or.getCount()+"',"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+Order.release+",0, '-"+or.getCount()+"')";
			    			 }else {  
			    				 sqlnew = "update mdinventorybranch set papercount =  (mdinventorybranch.papercount + "+or.getCount()+")*1 where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'"; 
			    			 
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" +  
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+Order.release+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 )";    
			    			 }   
		    			}
		    		}else if("salereleasesonghuo".equals(method)){ 
		    			if(or.getSalestatues() == 2){      
		    				/*if(getInventoryID(user,branch.getId(),or.getSendType()) == null){  
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" + 
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"','"+or.getCount()+"', 0,"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+6+",'"+or.getCount()+"', 0)"; 
			    			 }else {  
			    				 sqlnew = "update mdinventorybranch set realcount =  (mdinventorybranch.realcount + "+or.getCount()+")*1 where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'"; 
			    			 
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" +  
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+6+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 )";    
			    			 }   */
		    			}else { 
			    			if(getInventoryID(user,branch.getId(),or.getSendType()) == null){  
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" + 
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"','"+or.getCount()+"', 0,"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+6+",'"+or.getCount()+"', 0)"; 
			    			 }else {  
			    				 sqlnew = "update mdinventorybranch set realcount =  (mdinventorybranch.realcount + "+or.getCount()+")*1 where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'"; 
			    			 
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" +  
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+6+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 )";    
			    			 }   
		    			}
				           
		    		}else if("returns".equals(method)){
		    			logger.info("uid****"+uid); 
		    			if(or.getSalestatues() == 3 || or.getSalestatues() == 0 || or.getSalestatues() == 2){     
				    		Order order = OrderManager.getOrderID(user, Integer.valueOf(oid));
				    		uid = order.getSaleID(); 
				    	    if(getInventoryID(user,branch.getId(),or.getSendType()) == null){ 
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" + 
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"','"+or.getCount()+"', '"+or.getCount()+"',"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" +   
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+8+",'"+or.getCount()+"', '"+or.getCount()+"')";
			    			 }else {  
			    				sqlnew = "update mdinventorybranch set papercount =  (mdinventorybranch.papercount + "+or.getCount()+")*1 , realcount =  (mdinventorybranch.realcount + "+or.getCount()+")*1  where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'";  
			    			                 
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+8+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 )";    
			    			 }  
				    	}else if(or.getSalestatues() == 2){     
				    		/*Order order = OrderManager.getOrderID(user, Integer.valueOf(oid));
				    		uid = order.getSaleID(); 
				    	    
				    		if(getInventoryID(user,branch.getId(),or.getSendType()) == null){ 
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" + 
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"',0, '"+or.getCount()+"',"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" +   
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+8+",0, '"+or.getCount()+"')";
			    			 }else {   
			    				sqlnew = "update mdinventorybranch set papercount =  (mdinventorybranch.papercount + "+or.getCount()+")*1 where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'";  
			    			 
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+8+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 )";    
			    			 }*/   
				    	}else{ 
				    		//System.out.println("uid****"+uid);
			    			 if(getInventoryID(user,branch.getId(),or.getSendType()) == null){ 
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" + 
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"',0, '"+or.getCount()+"',"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" +   
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+8+",0, '"+or.getCount()+"')";
			    			 }else {   
			    				sqlnew = "update mdinventorybranch set papercount =  (mdinventorybranch.papercount + "+or.getCount()+")*1 where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'";  
			    			 
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+8+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 )";    
			    			 }    
				    	}    
				            
		    		}else if((Order.orderreturn+"").equals(method)){      // 送货员拉回货物   
		    			if(or.getSalestatues() == 3 || or.getSalestatues() == 0 ){     
				    		/*Order order = OrderManager.getOrderID(user, Integer.valueOf(oid));
				    		uid = order.getSaleID(); 
				    	    System.out.println("uid****"+uid);
				    	    if(getInventoryID(user,branch.getId(),or.getSendType()) == null){ 
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" + 
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"','"+or.getCount()+"', '"+or.getCount()+"',"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" +   
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+7+",'"+or.getCount()+"', '"+or.getCount()+"')";
			    			 }else {   
			    				sqlnew = "update mdinventorybranch set papercount =  (mdinventorybranch.papercount + "+or.getCount()+")*1 , realcount =  (mdinventorybranch.realcount + "+or.getCount()+")*1  where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'";  
			    			                 
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+7+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 )";    
			    			 } */
				    	}else if(or.getSalestatues() == 2 ){     
				    		/*Order order = OrderManager.getOrderID(user, Integer.valueOf(oid));
				    		uid = order.getSaleID();  
				    	    System.out.println("uid****"+uid);
				    	    if(getInventoryID(user,branch.getId(),or.getSendType()) == null){ 
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" +   
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"',"+or.getCount()+", 0,"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" +   
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+7+",'"+or.getCount()+"', 0)";
			    			 }else {    
			    				sqlnew = "update mdinventorybranch set realcount =  (mdinventorybranch.realcount + "+or.getCount()+")*1 where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'";  
			    			  
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+7+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 )";    
			    			 }*/
				    	}else{
				    		Order order = OrderManager.getOrderID(user, Integer.valueOf(oid));
				    		uid = order.getDealsendId();  
				    		branch = BranchService.getMap().get(Integer.valueOf(u.getBranch())); 
			    			 if(getInventoryID(user,branch.getId(),or.getSendType()) == null){ 
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" +   
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"',"+or.getCount()+", 0,"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" +   
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+7+",'"+or.getCount()+"', 0)";
			    			 }else {    
			    				sqlnew = "update mdinventorybranch set realcount =  (mdinventorybranch.realcount + "+or.getCount()+")*1 where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'";  
			    			  
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,realcount,papercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"', '"+oid+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+7+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 )";    
			    			 }
				    	} 
				          
		    		}     
		    		 logger.info(sql); 
		    		 logger.info(sqlnew);
			          listsql.add(sqlnew);
			          listsql.add(sql); 
		    	} 
	    }
		return listsql;
	} 
	
	public static InventoryBranch getMaxOrder(){
	    int id = 1 ;
	    InventoryBranch order = null;
	    Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn);
		//  select top 1 * from table order by id desc 
		// select * from table where id in (select max(id) from table)
		String  sql = "select * from mdinventorybranch where id in (select max(id) from inventory)" ;
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {  
			while (rs.next()) { 
				order = getCategoryFromRs(rs); 
				logger.info(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(stmt);
			DB.close(rs); 
			DB.close(conn);
		 }
		return order; 

  }
	
	public static InventoryBranch getInventoryID(User user ,int branchid,String type){
		   
		InventoryBranch orders = null;   
		   String sql = "";    
			   sql = "select * from   mdinventorybranch  where branchid = "+ branchid + " and type = '" + type+"'"; 
	logger.info(sql); 
			    Connection conn = DB.getConn();
				Statement stmt = DB.getStatement(conn);
				ResultSet rs = DB.getResultSet(stmt, sql);
				try {  
					while (rs.next()) {  
						orders = getCategoryFromRs(rs); 
						//List<InventoryMessage> listm =  InventoryMessageManager.getInventoryID(user,branchid); 
					   // orders.setInventory(listm); 
					} 
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DB.close(stmt);
					DB.close(rs);
					DB.close(conn);
				 }
				return orders;
		 }
	
	private static InventoryBranch getCategoryFromRs(ResultSet rs){
		InventoryBranch c = new InventoryBranch(); 
		try {  
			c.setId(rs.getInt("id")); 
			c.setBranchid(rs.getInt("branchid"));
			c.setRealcount(rs.getInt("realcount")); 
			c.setPapercount(rs.getInt("papercount")); 
			c.setInventoryid(rs.getInt("inventoryid"));
			c.setTypeid(rs.getString("type"));    
			c.setType(ProductService.getIDmap().get(Integer.valueOf(c.getTypeid())).getType());    
		} catch (SQLException e) { 
			e.printStackTrace();
		}	
		return c ;
	}
}
