package inventory;

import httpClient.download.InventoryChange;
import httpClient.download.InventoryModelDownLoad;

import java.sql.Connection; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import category.Category;

import branch.Branch;
import branch.BranchManager;
import branch.BranchService;



import order.Order;
import order.OrderManager;

import orderproduct.OrderProduct;
import orderproduct.OrderProductManager;
import product.Product;
import product.ProductService;

import user.User;
import user.UserManager;
import utill.DBUtill;
import utill.StringUtill;
import utill.TimeUtill;

import database.DB;

public class InventoryBranchManager {  
	protected static Log logger = LogFactory.getLog(InventoryBranchManager.class);
	     
	public static void initOrderNumSN(){    
		List<String> list = new ArrayList<String>();
		String sql = " update mdinventorybranch set ordernumsn = null,activetime = null where activetime < '"+TimeUtill.getdateString()+"' and ordernumsn != null"; 
		String sql1 = " update mdinventorybranch set activetime = null where activetime < '"+TimeUtill.dataAdd(TimeUtill.getdateString(), -7)+"' and ordernumsn = null"; 
		list.add(sql);     
		list.add(sql1);      
		DBUtill.sava(list);        
	}  
	   
	public static List<InventoryBranch> getCategory(User user ,String branch , String type) { 
		
		List<InventoryBranch> categorys = new ArrayList<InventoryBranch>();
		Connection conn = DB.getConn();
		String sql = ""; 
		String products = user.getProductIDS(); 
		if(!StringUtill.isNull(type) && !StringUtill.isNull(branch)){ 
			//type = ProductService.gettypemap().get(type).getId()+""; 
			sql = "select * from mdinventorybranch where type = '"+type +"' and inventoryid in "+products+" and branchid in (select id from mdbranch where bname = '"+branch+"') and branchid not in (select id from mdbranch where statues = 1 )";  
		}else if(!StringUtill.isNull(type) && StringUtill.isNull(branch)){
			//type = ProductService.gettypemap().get(type).getId()+""; 
			sql = "select * from mdinventorybranch where type = '"+type +"' and inventoryid in "+products+" and branchid not in (select id from mdbranch where statues = 1 )";  
		}else if(StringUtill.isNull(type) && !StringUtill.isNull(branch)){ 
			sql = "select * from mdinventorybranch where  branchid in ("+branch+") and inventoryid in "+products+" and branchid not in (select id from mdbranch where statues = 1 )";  
		}else if(StringUtill.isNull(type) && StringUtill.isNull(branch)){
			sql = "select * from mdinventorybranch where branchid not in (select id from mdbranch where statues = 1 ) and inventoryid in "+products+" ";    
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
		//logger.info(categorys.size());
		return categorys;
	} 
	 

public static List<InventoryBranch> getCategoryid(User user,String branch , String categoryid) {  
		//System.out.println(branch); 
		List<InventoryBranch> categorys = new ArrayList<InventoryBranch>();
		Connection conn = DB.getConn();  
		String products = user.getProductIDS(); 
		String sql = ""; 
		if(!StringUtill.isNull(categoryid) && !StringUtill.isNull(branch)){
			sql = "select * from mdinventorybranch where inventoryid = '"+categoryid +"' and inventoryid in "+products+" and  branchid in ("+branch+")  and branchid not in (select id from mdbranch where statues = 1 ) order by  id desc";  
		}else if(!StringUtill.isNull(categoryid) && StringUtill.isNull(branch)){
			sql = "select * from mdinventorybranch where inventoryid = '"+categoryid +"' and inventoryid in "+products+" and branchid not in (select id from mdbranch where statues = 1 ) order by  id desc";  
		}else if(StringUtill.isNull(categoryid) && !StringUtill.isNull(branch)){
			sql = "select * from mdinventorybranch where  branchid in ("+branch+") and inventoryid in "+products+"  order by  id desc";  
		}else if(StringUtill.isNull(categoryid) && StringUtill.isNull(branch)){
			sql = "select * from mdinventorybranch where 1= 1 and inventoryid in "+products+" and branchid not in (select id from mdbranch where statues = 1 ) order by  id desc";    
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
		return categorys;
	}
  
 
public static List<InventoryBranch> getCategoryid(User user,String branch , String categoryid,String typestatues) {  
	//System.out.println(branch); 
	String typesear = "";
	List<InventoryBranch> categorys = new ArrayList<InventoryBranch>();
	Connection conn = DB.getConn();  
	String products = user.getProductIDS();  
	if(!StringUtill.isNull(typestatues)){ 
		if(typestatues.equals("-1")){
			typesear = "";
		}else if(typestatues.equals("-2")){
			typesear = " and typestatues in  (-2) ";
		} else {  
			typesear = " and (typestatues in  (0,"+typestatues+") or typestatues is null) ";
		}
		
	}   
	String sql = ""; 
	if(!StringUtill.isNull(categoryid) && !StringUtill.isNull(branch)){
		sql = "select * from mdinventorybranch where inventoryid = '"+categoryid +"' and inventoryid in "+products+" and  branchid in ("+branch+")  "+typesear+" order by  id desc";  
	}else if(!StringUtill.isNull(categoryid) && StringUtill.isNull(branch)){
		sql = "select * from mdinventorybranch where inventoryid = '"+categoryid +"' and inventoryid in "+products+" and branchid not in (select id from mdbranch where statues = 1 ) "+typesear+" order by  id desc";  
	}else if(StringUtill.isNull(categoryid) && !StringUtill.isNull(branch)){
		sql = "select * from mdinventorybranch where  branchid in ("+branch+") and inventoryid in "+products+"   "+typesear+" order by  id desc";  
	}else if(StringUtill.isNull(categoryid) && StringUtill.isNull(branch)){
		sql = "select * from mdinventorybranch where 1= 1 and inventoryid in "+products+" and branchid not in (select id from mdbranch where statues = 1 ) "+typesear+"  order by  id desc";    
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
	return categorys;
}
public static List<InventoryBranch> getByBranchids(String branch) {  
	//System.out.println(branch);
	List<InventoryBranch> categorys = new ArrayList<InventoryBranch>();
	Connection conn = DB.getConn(); 
	String sql = ""; 
	sql = "select * from mdinventorybranch where  branchid in ( select branchid from mdordermessage where id in"+branch+") and branchid not in (select id from mdbranch where statues = 1 ) order by  id desc";  
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
	return categorys;
} 

public static List<InventoryBranch> getall(User user ,String sear) {  
	//System.out.println(branch);
	List<InventoryBranch> categorys = new ArrayList<InventoryBranch>();
	Connection conn = DB.getConn(); 
	String sql = ""; 
     String ids = user.getProductIDS();
      
	if(!StringUtill.isNull(sear)){ 
		sear = "and  branchid in"+sear; 
	} 
	sql = "select * from mdinventorybranch where branchid not in (select id from mdbranch where statues = 1 ) and inventoryid in  "+ids + sear+" order by  id desc";  
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
	return categorys;
}


public static List<InventoryBranch> getall(String sear) {  
	//System.out.println(branch);
	List<InventoryBranch> categorys = new ArrayList<InventoryBranch>();
	Connection conn = DB.getConn(); 
	String sql = ""; 
  
	if(!StringUtill.isNull(sear)){ 
		sear = "and  branchid in"+sear;
	} 
	sql = "select * from mdinventorybranch where branchid not in (select id from mdbranch where statues = 1 ) "+sear+" order by  id desc";  
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
               
             logger.info(inventory.getIntype())  ;
             
               if(inventory.getIntype() == 1 ){
	            	   if(getInventoryID(user,inbranchid,order.getProductId()) == null){
	
							insql = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" + 
			                         "  values ( null,"+order.getCategoryId()+", '"+order.getProductId()+"', '"+order.getCount()+"', '"+order.getCount()+"',"+inbranchid+")";
						
							insql1 = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" + 
			                        "  values ( null, '"+inbranchid+"', '"+inventory.getId()+"','"+inventory.getId()+"','"+time+"','"+order.getProductId()+"',"+order.getCount()+",'"+order.getCount()+"',"+1+","+order.getCount()+","+order.getCount()+","+outbranchid+","+inbranchid+",-1,0,0)";    
	
					}else { 
						  
							insql = "update mdinventorybranch set realcount =  ((mdinventorybranch.realcount)*1 + "+ order.getCount() + ")*1 , papercount =  ((mdinventorybranch.papercount)*1 + "+ order.getCount() + ")*1  where  branchid = " +inbranchid + " and  type = '"+order.getProductId()+"'"; 
						
							insql1 = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" +     
				                        "  values ( null, '"+inbranchid+"', '"+inventory.getId()+"','"+inventory.getId()+"','"+time+"','"+order.getProductId()+"','"+order.getCount()+"','"+order.getCount()+"',"+1+",(select realcount from mdinventorybranch where branchid = " +inbranchid + " and  type = '"+order.getProductId()+"')*1,(select papercount from mdinventorybranch where branchid = " +inbranchid + " and  type = '"+order.getProductId()+"')*1,"+outbranchid+","+inbranchid+",-1,(select realcount from mdinventorybranch where branchid = " +inbranchid + " and  type = '"+order.getProductId()+"')*1"+-order.getCount()+",(select papercount from mdinventorybranch where branchid = " +inbranchid + " and  type = '"+order.getProductId()+"')*1"+-order.getCount()+")"; 
						
		     
					}
					
	                if(getInventoryID(user,outbranchid,order.getProductId()) == null){
						
						
						
							outsql = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" + 
			                         "  values ( null,"+order.getCategoryId()+",'"+order.getProductId()+"', '"+-order.getCount()+"', '"+-order.getCount()+"',"+outbranchid+")";
						   
							outsql1 = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString , time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" + 
			                        "  values ( null, '"+outbranchid+"', '"+inventory.getId()+"','"+inventory.getId()+"','"+time+"','"+order.getProductId()+"','"+-order.getCount()+"','"+-order.getCount()+"',"+0+",'"+-order.getCount()+"','"+-order.getCount()+"',"+outbranchid+","+inbranchid+",-1,0,0)";    
						
					}else { 
						   
						 
							outsql = "update mdinventorybranch set realcount =  ((mdinventorybranch.realcount)*1 - "+ order.getCount() + ")*1  , papercount =  ((mdinventorybranch.papercount)*1 - "+ order.getCount() + ")*1  where  branchid = " +outbranchid + " and  type = '"+order.getProductId()+"'"; 
							outsql1 = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString , time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" +       
			                        "  values ( null, '"+outbranchid+"', '"+inventory.getId()+"','"+inventory.getId()+"','"+time+"','"+order.getProductId()+"','"+-order.getCount()+"','"+-order.getCount()+"',"+0+",(select realcount from mdinventorybranch where branchid = " +outbranchid + " and  type = '"+order.getProductId()+"')*1,(select papercount from mdinventorybranch where branchid = " +outbranchid + " and  type = '"+order.getProductId()+"')*1,"+outbranchid+","+inbranchid+",-1,(select realcount from mdinventorybranch where branchid = " +outbranchid + " and  type = '"+order.getProductId()+"')*1+"+ order.getCount() + ",(select papercount from mdinventorybranch where branchid = " +outbranchid + " and  type = '"+order.getProductId()+"')*1+"+ order.getCount() + ")"; 
					  
		     
					}
               }else {
            	   if(getInventoryID(user,inbranchid,order.getProductId()) == null){

						insql = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" + 
		                         "  values ( null,"+order.getCategoryId()+", '"+order.getProductId()+"', 0, '"+order.getCount()+"',"+inbranchid+")";
					
						insql1 = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" + 
		                        "  values ( null, '"+inbranchid+"', '"+inventory.getId()+"','"+inventory.getId()+"','"+time+"','"+order.getProductId()+"',0,'"+order.getCount()+"',"+1+",0,"+order.getCount()+","+outbranchid+","+inbranchid+",-1,0,0)";    

				}else { 
					    
						insql = "update mdinventorybranch set papercount =  ((mdinventorybranch.papercount)*1 + "+ order.getCount() + ")*1  where  branchid = " +inbranchid + " and  type = '"+order.getProductId()+"'"; 
					    
						insql1 = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" +     
			                        "  values ( null, '"+inbranchid+"', '"+inventory.getId()+"','"+inventory.getId()+"','"+time+"','"+order.getProductId()+"',0,'"+order.getCount()+"',"+1+",(select realcount from mdinventorybranch where branchid = " +inbranchid + " and  type = '"+order.getProductId()+"')*1,(select papercount from mdinventorybranch where branchid = " +inbranchid + " and  type = '"+order.getProductId()+"')*1,"+outbranchid+","+inbranchid+",-1,(select realcount from mdinventorybranch where branchid = " +inbranchid + " and  type = '"+order.getProductId()+"')*1,(select papercount from mdinventorybranch where branchid = " +inbranchid + " and  type = '"+order.getProductId()+"')*1"+-order.getCount()+")"; 
				}
				
                if(getInventoryID(user,outbranchid,order.getProductId()) == null){

						outsql = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" + 
		                         "  values ( null,"+order.getCategoryId()+",'"+order.getProductId()+"', 0, '"+-order.getCount()+"',"+outbranchid+")";
					   
						outsql1 = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString , time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" + 
		                        "  values ( null, '"+outbranchid+"', '"+inventory.getId()+"','"+inventory.getId()+"','"+time+"','"+order.getProductId()+"',0,'"+-order.getCount()+"',"+0+",0,'"+-order.getCount()+"',"+outbranchid+","+inbranchid+",-1,0,0)";    
					
				}else {  
					   
						outsql = "update mdinventorybranch set papercount =  ((mdinventorybranch.papercount)*1 - "+ order.getCount() + ")*1  where  branchid = " +outbranchid + " and  type = '"+order.getProductId()+"'"; 
						outsql1 = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString , time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" +       
		                        "  values ( null, '"+outbranchid+"', '"+inventory.getId()+"','"+inventory.getId()+"','"+time+"','"+order.getProductId()+"',0,'"+-order.getCount()+"',"+0+",(select realcount from mdinventorybranch where branchid = " +outbranchid + " and  type = '"+order.getProductId()+"')*1,(select papercount from mdinventorybranch where branchid = " +outbranchid + " and  type = '"+order.getProductId()+"')*1,"+outbranchid+","+inbranchid+",-1,(select realcount from mdinventorybranch where branchid = " +outbranchid + " and  type = '"+order.getProductId()+"')*1,(select papercount from mdinventorybranch where branchid = " +outbranchid + " and  type = '"+order.getProductId()+"')*1+"+ order.getCount() + ")"; 
				  
				}
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
	  
	
	// 更新苏宁信息
		public static String updateSNMessage(Set<Integer> setb ,Set<Integer> sett,String oid ,String time,int statues ){
			String strb = StringUtill.getStr(setb); 
			String strt =StringUtill.getStr(sett);    
			  
			String sql = "update mdinventorybranch set ordernumsn = '"+oid+"' , activetime = '"+time+"' where type in "+strt +" and branchid in "+strb + " and typestatues = "+ statues; ;
			return sql; 
		} 
		
		 
		public static String updateSNMessage(int tid ,int bid,int statues,String oid,String time ){
			 
			String sql = "update mdinventorybranch set ordernumsn = '"+oid+"' , activetime = '"+time+"' where type ="+tid+"  and branchid = "+bid +" and typestatues = "+ statues ;
			return sql;
		}   

		
	public static List<String> chage(User user ,String method , int uid ,Order order ){
		List<String> listsql = new ArrayList<String>(); 
		
		List<OrderProduct> listp = OrderProductManager.getOrderStatues(user, order.getId());
		
		int branchuid = uid;
	    String time = TimeUtill.gettime();
	    
	    for(int i=0;i<listp.size();i++){      
	    	OrderProduct or = listp.get(i); 
	    	if(or.getStatues() == 0){       
		    	if(or.getSalestatues() == 2 || or.getSalestatues() == 3 || or.getSalestatues() == 0 ){     
		    		branchuid = order.getSaleID(); 
		    	}else if(order.getOderStatus().equals(20+"")&& or.getSalestatues() == 1 && "songhuo".equals(method)){
		    		branchuid = order.getDealsendId();
		    		uid = branchuid;  
		    		logger.info(uid);
		    	} 
		    	User u = UserManager.getUesrByID(branchuid+"");
		    	//Branch branch = BranchManager.getLocatebyname(u.getBranch());
		    	Branch branch = BranchService.getMap().get(Integer.valueOf(u.getBranch())); 
		    	String sqlnew = "";  
	    		String sql = "";        
		    		if("peidan".equals(method)){ 
		    			if(or.getSalestatues() == 3 || or.getSalestatues() == 0 ){      
				    	    if(getInventoryID(user,branch.getId(),or.getSendType()) == null){  
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" + 
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"','-"+or.getCount()+"', '-"+or.getCount()+"',"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" +   
			 	                        "  values ( null, '"+branch.getId()+"',"+order.getId()+", '"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"','-"+or.getCount()+"','-"+or.getCount()+"',"+Order.orderDispatching+",'-"+or.getCount()+"', '-"+or.getCount()+"',"+user.getId()+","+branchuid+","+order.getOderStatus()+",0,0)";
			    			 }else {  
			    				sqlnew = "update mdinventorybranch set papercount =  (mdinventorybranch.papercount - "+or.getCount()+")*1 , realcount =  (mdinventorybranch.realcount - "+or.getCount()+")*1  where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'";  
			    			                 
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"', "+order.getId()+",'"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"','-"+or.getCount()+"','-"+or.getCount()+"',"+Order.orderDispatching+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 ,"+user.getId()+","+branchuid+","+order.getOderStatus()+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 + "+or.getCount()+",(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1+ "+or.getCount()+" )";    
			    			 }  
				    	}else if(or.getSalestatues() == 2){     

				    	    if(getInventoryID(user,branch.getId(),or.getSendType()) == null){ 
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" +  
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"',0, '-"+or.getCount()+"',"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" +   
			 	                        "  values ( null, '"+branch.getId()+"', "+order.getId()+",'"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"',0,'-"+or.getCount()+"',"+Order.orderDispatching+",0, '-"+or.getCount()+"',"+user.getId()+","+uid+","+order.getOderStatus()+",0,0)";
			    			 }else {     
			    				sqlnew = "update mdinventorybranch set papercount =  (mdinventorybranch.papercount - "+or.getCount()+")*1  where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'";  
			    			                     
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"', "+order.getId()+",'"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"',0,'-"+or.getCount()+"',"+Order.orderDispatching+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 ,"+user.getId()+","+uid+","+order.getOderStatus()+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 + "+or.getCount()+",(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1+ "+or.getCount()+" )";    
			    			 } 
				    	}else {
			    			 if(getInventoryID(user,branch.getId(),or.getSendType()) == null){ 
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" + 
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"',0, '-"+or.getCount()+"',"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" +   
			 	                        "  values ( null, '"+branch.getId()+"', "+order.getId()+",'"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"',0,'-"+or.getCount()+"',"+Order.orderDispatching+",0, '-"+or.getCount()+"',"+user.getId()+","+uid+","+order.getOderStatus()+",0,0)";
			    			 }else {  
			    				sqlnew = "update mdinventorybranch set papercount =  (mdinventorybranch.papercount - "+or.getCount()+")*1 where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'";  
			    			 
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"', "+order.getId()+",'"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"',0,'-"+or.getCount()+"',"+Order.orderDispatching+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 ,"+user.getId()+","+uid+","+order.getOderStatus()+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1+ "+or.getCount()+" )";    
			    			 } 
				    	}   // Order.ordersong  只安装门店提货
		    		}else if((Order.orderpeisong+"").equals(method) || (Order.ordersong+"").equals(method)){     
		    			     
		    			if(or.getSalestatues() == 3 || or.getSalestatues() == 0 ){     
				    		
				    	}else if(or.getSalestatues() == 2){                       
				    		if(getInventoryID(user,branch.getId(),or.getSendType()) == null){ 
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" +  
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"','-"+or.getCount()+"', 0,"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, inventoryString,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"',"+order.getId()+", '"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"','-"+or.getCount()+"',0,"+Order.porderDispatching+",'-"+or.getCount()+"', 0,"+user.getId()+","+uid+","+order.getOderStatus()+",0,0)";
			    			 }else {  
			    				 sqlnew = "update mdinventorybranch set realcount =  (mdinventorybranch.realcount - "+or.getCount()+")*1 where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'"; 
			    			 
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" +   
			 	                        "  values ( null, '"+branch.getId()+"', "+order.getId()+",'"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"','-"+or.getCount()+"',0,"+Order.porderDispatching+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,"+user.getId()+","+uid+","+order.getOderStatus()+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 + "+or.getCount()+",(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1  )";    
			    			 }   
		    			}else {  
			    			if(getInventoryID(user,branch.getId(),or.getSendType()) == null){ 
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" +  
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"','-"+or.getCount()+"', 0,"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"', "+order.getId()+",'"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"','-"+or.getCount()+"',0,"+Order.porderDispatching+",'-"+or.getCount()+"', 0,"+user.getId()+","+uid+","+order.getOderStatus()+",0,0)";
			    			 }else {  
			    				 sqlnew = "update mdinventorybranch set realcount =  (mdinventorybranch.realcount - "+or.getCount()+")*1 where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'"; 
			    			 
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" +   
			 	                        "  values ( null, '"+branch.getId()+"',"+order.getId()+", '"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"','-"+or.getCount()+"',0,"+Order.porderDispatching+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,"+user.getId()+","+uid+","+order.getOderStatus()+" ,(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 + "+or.getCount()+",(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1)";    
			    			 }      
		    			}   //  安装公司释放
		    		}else if("shifang".equals(method)){
		    			if(or.getSalestatues() == 0){     
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
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"',"+order.getId()+", '"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"',0,"+or.getCount()+","+Order.release+",0, '"+or.getCount()+"',"+user.getId()+","+uid+","+order.getOderStatus()+",0,0)";
			    			 }else {  
			    				 sqlnew = "update mdinventorybranch set papercount =  (mdinventorybranch.papercount + "+or.getCount()+")*1 where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'"; 
			    			 
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" +  
			 	                        "  values ( null, '"+branch.getId()+"',"+order.getId()+", '"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"',0,"+or.getCount()+","+Order.release+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,"+user.getId()+","+uid+","+order.getOderStatus()+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 - "+or.getCount()+" )";    
			    			 }   
		    			}  // 未送货释放
		    		}else if("salereleasesonghuo".equals(method)){ 
		    			if(or.getSalestatues() == 0){       
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
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"',"+order.getId()+", '"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+",0,"+6+",'"+or.getCount()+"', 0,"+user.getId()+","+order.getSendId()+","+order.getOderStatus()+",0,0)"; 
			    			 }else {  
			    				 sqlnew = "update mdinventorybranch set realcount =  (mdinventorybranch.realcount + "+or.getCount()+")*1 where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'"; 
			    			 
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" +  
			 	                        "  values ( null, '"+branch.getId()+"', "+order.getId()+",'"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+",0,"+6+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,"+user.getId()+","+order.getSendId()+","+order.getOderStatus()+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 - "+or.getCount()+",(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1)";    
			    			 }   
		    			} 
				           
		    		}else if("returns".equals(method)){ 
		    			if(or.getSalestatues() == 3 || or.getSalestatues() == 0){     
				    	    if(getInventoryID(user,branch.getId(),or.getSendType()) == null){ 
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" + 
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"','"+or.getCount()+"', '"+or.getCount()+"',"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" +   
			 	                        "  values ( null, '"+branch.getId()+"', "+order.getId()+",'"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+or.getCount()+","+8+",'"+or.getCount()+"', '"+or.getCount()+"',"+user.getId()+","+order.getSaleID()+","+order.getOderStatus()+",0,0)";
			    			 }else {  
			    				sqlnew = "update mdinventorybranch set papercount =  (mdinventorybranch.papercount + "+or.getCount()+")*1 , realcount =  (mdinventorybranch.realcount + "+or.getCount()+")*1  where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'";  
			    			                 
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"', "+order.getId()+",'"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+","+or.getCount()+","+8+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,"+user.getId()+","+order.getSaleID()+","+order.getOderStatus()+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 - "+or.getCount()+",(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1- "+or.getCount()+"  )";    
			    			 }  
				    	}
		    			//else if(or.getSalestatues() == 2){      
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
				    	//}
		    		else{ 
				    		//System.out.println("uid****"+uid);
			    			 if(getInventoryID(user,branch.getId(),or.getSendType()) == null){ 
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" + 
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"',0, '"+or.getCount()+"',"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" +   
			 	                        "  values ( null, '"+branch.getId()+"', "+order.getId()+",'"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"',0,"+or.getCount()+","+8+",0, '"+or.getCount()+"',"+user.getId()+","+order.getDealsendId()+","+order.getOderStatus()+",0,0)";
			    			 }else {   
			    				sqlnew = "update mdinventorybranch set papercount =  (mdinventorybranch.papercount + "+or.getCount()+")*1 where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'";  
			    			 
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"',"+order.getId()+", '"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"',0,"+or.getCount()+","+8+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 ,"+user.getId()+","+order.getDealsendId()+","+order.getOderStatus()+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1- "+or.getCount()+" )";    
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
				    	}//else if(or.getSalestatues() == 2 ){     
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
				    	//}
		    		else{
			    			 if(getInventoryID(user,branch.getId(),or.getSendType()) == null){ 
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" +   
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"',"+or.getCount()+", 0,"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" +   
			 	                        "  values ( null, '"+branch.getId()+"', "+order.getId()+",'"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+",0,"+7+",'"+or.getCount()+"', 0,"+user.getId()+","+uid+","+order.getOderStatus()+",0,0)";
			    			 }else {      
			    				sqlnew = "update mdinventorybranch set realcount =  (mdinventorybranch.realcount + "+or.getCount()+")*1 where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'";  
			    			  
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"',"+order.getId()+", '"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"',"+or.getCount()+",0,"+7+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,"+user.getId()+","+uid+","+order.getOderStatus()+" ,(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 - "+or.getCount()+",(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1)";    
			    			 } 
				    	}  
				           
		    		}else if(("salereleasereturn").equals(method)){      // 退货员释放 
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
				    	}//else if(or.getSalestatues() == 2 ){     
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
				    	//} 
		    			else
				    	{ 
			    			 if(getInventoryID(user,branch.getId(),or.getSendType()) == null){ 
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" +   
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"','-"+or.getCount()+"', 0,"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" +   
			 	                        "  values ( null, '"+branch.getId()+"', "+order.getId()+",'"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"','-"+or.getCount()+"',0,"+9+",'-"+or.getCount()+"', 0,"+user.getId()+","+order.getReturnid()+","+order.getOderStatus()+",0,0)";
			    			 }else {      
			    				sqlnew = "update mdinventorybranch set realcount =  (mdinventorybranch.realcount - "+or.getCount()+")*1 where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'";  
			    			  
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"',"+order.getId()+", '"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"','-"+or.getCount()+"',0,"+9+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,"+user.getId()+","+order.getReturnid()+","+order.getOderStatus()+" ,(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 + "+or.getCount()+",(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1)";    
			    			 } 
				    	}  
				           
		    		}else if(("songhuo").equals(method)){      // 送货员换货确认
		    			if(order.getOderStatus().equals(20+"")){      
			    			 if(getInventoryID(user,branch.getId(),or.getSendType()) == null){ 
			    				 sqlnew = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)" +   
				                         "  values ( null,"+or.getCategoryId()+", '"+or.getSendType()+"','"+or.getCount()+"','"+or.getCount()+"',"+branch.getId()+")"; 
			    				 sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" +   
			 	                        "  values ( null, '"+branch.getId()+"', "+order.getId()+",'"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"','"+or.getCount()+"','"+or.getCount()+"',"+9+",'"+or.getCount()+"', '"+or.getCount()+"',"+user.getId()+","+order.getSendId()+","+order.getOderStatus()+",0,0)";
			    			 }else {      
			    				sqlnew = "update mdinventorybranch set realcount =  (mdinventorybranch.realcount + "+or.getCount()+")*1 , papercount =  (mdinventorybranch.papercount + "+or.getCount()+")*1  where branchid = " + branch.getId() + " and type = '" + or.getSendType()+"'";  
			    			  
			    				sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" + 
			 	                        "  values ( null, '"+branch.getId()+"',"+order.getId()+", '"+order.getPrintlnid()+"','"+time+"','"+or.getSendType()+"','"+or.getCount()+"',"+or.getCount()+","+12+",(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1,"+user.getId()+","+order.getSendId()+","+order.getOderStatus()+" ,(select realcount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 + "+or.getCount()+",(select papercount from mdinventorybranch where branchid = " +branch.getId() + " and  type = '"+or.getSendType()+"')*1 + "+or.getCount()+")";    
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
	 
	public static Map<String,InventoryBranch> getmapType(User user ,String branchid,String type){
		Map<String,InventoryBranch> map = new HashMap<String,InventoryBranch>();
		 List<InventoryBranch>  listInventory = InventoryBranchManager.getCategoryid(user,branchid,type);
		 Iterator<InventoryBranch> it = listInventory.iterator();
		 while(it.hasNext()){ 
			 InventoryBranch in = it.next();
			 map.put(in.getType(), in);
		 } 
		 return map;
	} 
	 
	public static Map<String,InventoryBranch> getmapType(User user,String branchid){
		Map<String,InventoryBranch> map = new HashMap<String,InventoryBranch>(); 
		 List<InventoryBranch>  listInventory = InventoryBranchManager.getCategoryid(user,branchid,""); 
		 Iterator<InventoryBranch> it = listInventory.iterator();
		 while(it.hasNext()){ 
			 InventoryBranch in = it.next(); 
			 if(!StringUtill.isNull(in.getType())){
				 InventoryBranch inmap = map.get(in.getType());
				 if(null == inmap){     
					 inmap = in ;    
					// logger.info(in.getType());
					 map.put(in.getType(), inmap);
				 }else {    
					 inmap.setPapercount(inmap.getPapercount()+in.getPapercount());
					 inmap.setRealcount(inmap.getRealcount()+in.getRealcount());
				 }
			 }
			 
			 
		 }  
		 //logger.info(map);  
		 return map;
	}
	  
	 
	public static Map<Integer,Map<String,InventoryBranch>> getmapType(User user){
		 Map<Integer,Map<String,InventoryBranch>> map = new HashMap<Integer,Map<String,InventoryBranch>>(); 
		 List<InventoryBranch>  listInventory = InventoryBranchManager.getCategoryid(user,"",""); 
		 Iterator<InventoryBranch> it = listInventory.iterator();
		 
		 while(it.hasNext()){ 
			 InventoryBranch in = it.next(); 
			 Map<String,InventoryBranch> mapt  = map.get(in.getBranchid()); 
			 if(null == mapt){
				 mapt = new HashMap<String,InventoryBranch>();
				 map.put(in.getBranchid(), mapt);
			 }
			 
			 InventoryBranch inmap = mapt.get(in.getType());
			 if(null == inmap){
				 mapt.put(in.getType(), in);
			 }else { 
				 inmap.setPapercount(inmap.getPapercount()+in.getPapercount());
			 }
			  
			// logger.info(in.getType()+"**"+in.getPapercount());
			
		 }  
		 //logger.info(map);  
		 return map;
	}
	 
	
	public static Map<Integer,Map<String,InventoryBranch>> getmapType(User user,Category c){
		 Map<Integer,Map<String,InventoryBranch>> map = new HashMap<Integer,Map<String,InventoryBranch>>(); 
		 List<InventoryBranch>  listInventory = InventoryBranchManager.getCategoryid(user,"",c.getId()+""); 
		 Iterator<InventoryBranch> it = listInventory.iterator();
		  
		 while(it.hasNext()){  
			 InventoryBranch in = it.next(); 
			 Map<String,InventoryBranch> mapt  = map.get(in.getBranchid()); 
			 if(null == mapt){
				 mapt = new HashMap<String,InventoryBranch>();
				 map.put(in.getBranchid(), mapt);
			 }
			 
			 InventoryBranch inmap = mapt.get(in.getType());
			 if(null == inmap){
				 mapt.put(in.getType(), in);
			 }else { 
				 inmap.setPapercount(inmap.getPapercount()+in.getPapercount());
			 }
			  
			// logger.info(in.getType()+"**"+in.getPapercount());
			
		 }  
		 //logger.info(map);  
		 return map;
	}
	public static Map<String,InventoryBranch> getmapTypeBranch(String branchid){
		//logger.info(branchid);   
		Map<String,InventoryBranch> map = new HashMap<String,InventoryBranch>();
		 List<InventoryBranch>  listInventory = InventoryBranchManager.getByBranchids(branchid); 
		 Iterator<InventoryBranch> it = listInventory.iterator(); 
		 while(it.hasNext()){  
			 InventoryBranch in = it.next();  
			// logger.info(in.getTypeid()+"_"+in.getBranchid()); 
			 map.put(in.getTypeid()+"_"+in.getBranchid(), in);
		 }  
		 //logger.info(map);  
		 return map;
	}
	  
	
	public static Map<Integer,InventoryBranch> getmapKeyBranchType(String branchtype){
		//logger.info(branchid);  
		String sear = ""; 
		if(!StringUtill.isNull(branchtype)){ 
			sear =   StringUtill.getStr(BranchService.getListids(Integer.valueOf(branchtype)));
		}
		
		 Map<Integer,InventoryBranch> map = new HashMap<Integer,InventoryBranch>();
		 List<InventoryBranch>  listInventory = InventoryBranchManager.getall(sear); 
		 Iterator<InventoryBranch> it = listInventory.iterator(); 
		 while(it.hasNext()){    
			 InventoryBranch in = it.next();  
			// logger.info(in.getTypeid()+"_"+in.getBranchid()); 
			 map.put(in.getBranchid(), in);
		 }  
		 //logger.info(map);  
		 return map;
	}
	 
	public static Map<Integer,List<InventoryBranch>> getmapKeyBranchType(User user ,String branchtype){
		//logger.info(branchid);  
		String sear = "";   
		if(!StringUtill.isNull(branchtype)){ 
			sear =   StringUtill.getStr(BranchService.getListids(Integer.valueOf(branchtype)));
		} 
		 
		Map<Integer,List<InventoryBranch>> map = new HashMap<Integer,List<InventoryBranch>>();
		 List<InventoryBranch>  listInventory = InventoryBranchManager.getall(user,sear); 
		 Iterator<InventoryBranch> it = listInventory.iterator(); 
		 while(it.hasNext()){     
			 InventoryBranch in = it.next(); 
			 List<InventoryBranch> list = map.get(in.getBranchid());
			 if(null == list){
				 list = new ArrayList<InventoryBranch>();
				 map.put(in.getBranchid(),list);
			 } 
			  
			// logger.info(in.getTypeid()+"_"+in.getBranchid()); 
		list.add(in);
		 }  
		 //logger.info(map);  
		 return map;
	}
	
	// type ,branchid ,产品状态
	public static Map<Integer,Map<String,Map<Integer,InventoryBranch>>> getInventoryMap(User user){
		Map<Integer,Map<String,Map<Integer,InventoryBranch>>> map = new HashMap<Integer,Map<String,Map<Integer,InventoryBranch>>>();
		 
		InventoryBranch orders = null;   
		   String sql = "";    
			   sql = "select * from   mdinventorybranch "; 
	logger.info(sql);  
			    Connection conn = DB.getConn();
				Statement stmt = DB.getStatement(conn);
				ResultSet rs = DB.getResultSet(stmt, sql);
				try {   
					while (rs.next()) {  
						orders = getCategoryFromRs(rs); 
						Map<String,Map<Integer,InventoryBranch>> mapt  = map.get(orders.getBranchid());
						if(null == mapt){
							mapt = new HashMap<String,Map<Integer,InventoryBranch>>();
							map.put(orders.getBranchid(), mapt);
						} 
						  
						Map<Integer,InventoryBranch> maptp = mapt.get(orders.getTypeid()); 
						if(null == maptp){
							maptp = new HashMap<Integer,InventoryBranch>();
							mapt.put(orders.getTypeid(), maptp);
						}
						    
						maptp.put(orders.getTypeStatues(), orders);
						 
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
				return map;
		 }
	
	 
	// type ,branchid ,产品状态 
		public static Map<Integer,Map<String,Map<Integer,InventoryBranch>>> getInventoryMap(){ 
			Map<Integer,Map<String,Map<Integer,InventoryBranch>>> map = new HashMap<Integer,Map<String,Map<Integer,InventoryBranch>>>();
			 
			InventoryBranch orders = null;   
			   String sql = "";    
				   sql = "select * from   mdinventorybranch "; 
		logger.info(sql);  
				    Connection conn = DB.getConn();
					Statement stmt = DB.getStatement(conn);
					ResultSet rs = DB.getResultSet(stmt, sql);
					try {   
						while (rs.next()) {  
							orders = getCategoryFromRs(rs); 
							Map<String,Map<Integer,InventoryBranch>> mapt  = map.get(orders.getBranchid());
							if(null == mapt){
								mapt = new HashMap<String,Map<Integer,InventoryBranch>>();
								map.put(orders.getBranchid(), mapt);
							}  
							  //logger.info(orders.getTypeid());
							  //logger.info(orders.getType()); 
							Map<Integer,InventoryBranch> maptp = mapt.get(orders.getTypeid()); 
							if(null == maptp){
								maptp = new HashMap<Integer,InventoryBranch>();
								mapt.put(orders.getTypeid(), maptp);
							}
							    
							maptp.put(orders.getTypeStatues(), orders);
							 
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
					return map;
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
	
	public static Map<String,String> getBranchType(User user ,String branchid){
		  //String branchid
		   Map<String,String> list = new HashMap<String,String>();
		   
		   String sql = "";    
			   sql = "select * from   mdinventorybranch  where branchid in (select branchid from relatebranch where relatebranchid =  " + branchid +")"; 
	logger.info(sql); 
			    Connection conn = DB.getConn();
				Statement stmt = DB.getStatement(conn);
				ResultSet rs = DB.getResultSet(stmt, sql); 
				try {  
					while (rs.next()) {  
						InventoryBranch orders = getCategoryFromRs(rs); 
						list.put(orders.getType(),orders.getType());
					} 
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DB.close(stmt);
					DB.close(rs);
					DB.close(conn);
				 }
				return list;
		 }
	
	public static Map<String,InventoryBranch> getBranchTypeObject(User user ,String branchid){
		  //String branchid
		   Map<String,InventoryBranch> list = new HashMap<String,InventoryBranch>();
		   
		   String sql = "";    
			   sql = "select * from   mdinventorybranch  where branchid in (select branchid from relatebranch where relatebranchid =  " + branchid +")"; 
	logger.info(sql); 
			    Connection conn = DB.getConn();
				Statement stmt = DB.getStatement(conn);
				ResultSet rs = DB.getResultSet(stmt, sql); 
				try {  
					while (rs.next()) {  
						InventoryBranch orders = getCategoryFromRs(rs); 
						list.put(orders.getType(),orders); 
					} 
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DB.close(stmt);
					DB.close(rs);
					DB.close(conn);
				 }
				return list;
		 }

	public static int update(User user ,String branchid,String type){
		     
		   int count = -1 ;  
		   List<String> listsql = new ArrayList<String>(); 
		   String sql = "update mdinventorybranch  set  isquery = 1 , querymonth = '"+TimeUtill.getdateString()+"' where branchid = "+ branchid + " and type = '" + type+"'";  
		   String sql1 = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" + 
                    "  values ( null, "+branchid+",0, 0,'"+TimeUtill.gettime()+"','"+type+"','0',0,"+10+",(select realcount from mdinventorybranch where branchid = " +branchid + " and  type = '"+type+"')*1,(select papercount from mdinventorybranch where branchid = " +branchid + " and  type = '"+type +"')*1,"+user.getId()+",0,0,(select realcount from mdinventorybranch where branchid = " +branchid + " and  type = '"+type+"')*1 ,(select papercount from mdinventorybranch where branchid = " +branchid + " and  type = '"+type+"')*1)";    
 
		   
		   //String sql1 = "insert into mdpandian (id,mdinventorybranchid,mdtime,userid,mdmonth) values (null,(select id from mdinventorybranch  where branchid = "+ branchid + " and type = '" + type+"'),'"+TimeUtill.gettime()+"',"+user.getId()+",'"+TimeUtill.getMonth()+"')";
	       listsql.add(sql);
	       listsql.add(sql1); 
	       DBUtill.sava(listsql);
		   return count ;
	} 
	    
	public static int updateSN(User user ,String branchid,String type,String typestatues){
	      
		   int count = -1 ;   
		   List<String> listsql = new ArrayList<String>(); 
		   String sql = "update mdinventorybranch  set  isquery = 1 , querymonth = '"+TimeUtill.getdateString()+"' where branchid = "+ branchid + " and type = '" + type+"' and typestatues = "+typestatues;  
		   String sql1 = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)" + 
                 "  values ( null, "+branchid+",0, 0,'"+TimeUtill.gettime()+"','"+type+"','0',0,"+10+",(select realcount from mdinventorybranch where branchid = " +branchid + " and  type = '"+type+"' and typestatues = "+typestatues+")*1,(select papercount from mdinventorybranch where branchid = " +branchid + " and  type = '"+type +"' and typestatues = "+typestatues+")*1,"+user.getId()+",0,0,(select realcount from mdinventorybranch where branchid = " +branchid + " and  type = '"+type+"' and typestatues = "+typestatues+")*1 ,(select papercount from mdinventorybranch where branchid = " +branchid + " and  type = '"+type+"' and typestatues = "+typestatues+")*1)";    

		    
		   //String sql1 = "insert into mdpandian (id,mdinventorybranchid,mdtime,userid,mdmonth) values (null,(select id from mdinventorybranch  where branchid = "+ branchid + " and type = '" + type+"'),'"+TimeUtill.gettime()+"',"+user.getId()+",'"+TimeUtill.getMonth()+"')";
	       listsql.add(sql);
	       listsql.add(sql1); 
	       DBUtill.sava(listsql);
		   return count ;
	}
	
	public static int update(User user ,String branchid,String[] types,String time,String[] typestatuess){
		   Map<Integer,Map<String,Map<Integer,InventoryBranch>>> map = InventoryBranchManager.getInventoryMap(); 
		 //  logger.info(map); 
		   int count = -1 ;        
		   List<String> listsql = new ArrayList<String>();  
		        
		   for(int i=0;i<types.length;i++){  
			   String type = types[i];  
			
			   for(int j=0;j<typestatuess.length;j++){
				   String sta = typestatuess[j]; 
				   logger.info(type);    
				   logger.info(branchid);   
				   if(!StringUtill.isNull(sta)){
				   InventoryBranch in = null ;   
				   try{    
					  // logger.info(map.get(Integer.valueOf(branchid)));
					   in = map.get(Integer.valueOf(branchid)).get(type).get(Integer.valueOf(sta)) ;
					  // logger.info(map);
				   }catch(Exception e){ 
					   in = null ;  
				   }  
				    
				   String sql = ""; 
				   if(null != in){  
					   sql = "update mdinventorybranch  set  isquery = 1 , querymonth = '"+time+"' where branchid = "+ branchid + " and type = '" + type+"' and typestatues = "+sta;   
				   }else {
					   logger.info(Integer.valueOf(type));  
					   logger.info(ProductService.getIDmap().get(Integer.valueOf(type)));
					   int cid = ProductService.getIDmap().get(Integer.valueOf(type)).getCategoryID();
					   
					   sql = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid,typestatues)"
								+ "  values ( null,"
								+ cid 
								+ ", '" 
								+ type  
								+ "', '"  
								+ 0  
								+ "', '" 
								+ 0 + "'," + branchid+ ","
										+ sta+ ")";
				   }
	 
				  
				   // (select realcount from mdinventorybranch where branchid = " +branchid + " and  type = '"+type+"' and typstatues = 1 )*1
				   String sql1 = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString, time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount,typestatues)" + 
		                    "  values ( null, "+branchid+",0, 0,'"+time+"','"+type+"','0',0,"+10+",0,0,"+user.getId()+",0,0,0,0,'"+sta +"')";     
		        
				   listsql.add(sql); 
			       listsql.add(sql1);  
				   }
			   }   
		   }
		   //String sql1 = "insert into mdpandian (id,mdinventorybranchid,mdtime,userid,mdmonth) values (null,(select id from mdinventorybranch  where branchid = "+ branchid + " and type = '" + type+"'),'"+TimeUtill.gettime()+"',"+user.getId()+",'"+TimeUtill.getMonth()+"')";
	      //logger.info(listsql); 
	      DBUtill.sava(listsql);
		   return count ;
	}
	 
	
	public static boolean isEmpty(String bid){
		
		boolean flag = true ;
		String sql = "select * from mdinventorybranch where branchid = "+ bid;
		
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql); 
		try {  
			while (rs.next()) {  
				InventoryBranch orders = getCategoryFromRs(rs); 
				if(orders.getRealcount() != 0 || orders.getPapercount() != 0){
					flag =  false ;
				}
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(stmt);
			DB.close(rs);
			DB.close(conn);
		 }
		
		
		return flag ;
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
			
			Product p = ProductService.getIDmap().get(Integer.valueOf(c.getTypeid())) ;
			if(null != p ){ 
				c.setType(ProductService.getIDmap().get(Integer.valueOf(c.getTypeid())).getType());   
			}   
			//logger.info(c.getTypeid());    
			//c.setType(ProductService.getIDmap().get(Integer.valueOf(c.getTypeid())).getType());   
			
			 
		    c.setIsquery(rs.getInt("isquery"));     
		    c.setQuerymonth(rs.getString("querymonth"));  
		    c.setTypeStatues(rs.getInt("typestatues")); 
		    c.setOrderNUmSN(rs.getString("ordernumsn"));
		    c.setActivetime(rs.getString("activetime"));  
		} catch (SQLException e) { 
			e.printStackTrace();
		}	 
		return c ;
	}
}
