package inventory;

import group.Group;

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

import category.Category;
import category.CategoryManager;

import order.OrderManager;
import orderPrint.OrderPrintlnManager;
import orderproduct.OrderProductManager;

import user.User;
import user.UserManager;
import utill.DBUtill;

import database.DB;

public class InventoryManager {
	protected static Log logger = LogFactory.getLog(InventoryManager.class);
	
	public static List<Inventory> getCategory(User user,String statues) { 
		List<Inventory> categorys = new ArrayList<Inventory>();
		Connection conn = DB.getConn();
		int branchid = Integer.valueOf(user.getBranch());  
		String sql = "";
		if(UserManager.checkPermissions(user, Group.dealSend)){
			if("unconfirmed".equals(statues)){ 
				sql = "select * from inventory where (instatues = 0 or outstatues = 0) and intype != 2  order by id desc";
			}else if("confirmed".equals(statues)){
				sql = "select * from inventory where instatues = 1 and outstatues = 1 and intype != 2  order by id desc";
			}
		}else {       
			if("unconfirmed".equals(statues)){ 
				sql = "select * from inventory where (instatues = 0 or outstatues = 0) and (inbranchid = " + branchid +" or outbranchid = "+ branchid+") and intype != 2  order by id desc";
			}else if("confirmed".equals(statues)){
				sql = "select * from inventory where instatues = 1 and outstatues = 1 and (inbranchid = " + branchid +" or outbranchid = "+ branchid+")  and intype != 2  order by id desc";
			}
			
		} 
		
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				Inventory u = InventoryManager.getCategoryFromRs(rs);
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
	 
	public static boolean updatePrintln(String id ){
		String sql = "update inventory set outstatues = 1 where id = "+ id ;
		return DBUtill.sava(sql);
	}
	
	public static List<Inventory> getCategoryAnalyze(User user,String statues) { 
		List<Inventory> categorys = new ArrayList<Inventory>();
		Connection conn = DB.getConn();
		int branchid = Integer.valueOf(user.getBranch());  
		String sql = "";
		if(UserManager.checkPermissions(user, Group.dealSend)){
			if("unconfirmed".equals(statues)){ 
				sql = "select * from inventory where intype = 2  order by id desc";
			}else if("confirmed".equals(statues)){
				sql = "select * from inventory where outstatues = 0 and intype = 2  order by id desc";
			}
		}else {       
			if("unconfirmed".equals(statues)){ 
				sql = "select * from inventory where inbranchid = " + branchid +"  and intype = 2  order by id desc";
			}else if("confirmed".equals(statues)){
				sql = "select * from inventory where instatues = 0 and  inbranchid = " + branchid +"   and intype = 2  order by id desc";
			}
			
		} 
		
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				Inventory u = InventoryManager.getCategoryFromRs(rs);
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
	
	public static boolean check(String method,String id ) {    
		boolean flag = false ;  
		Connection conn = DB.getConn();    
		String sql = "";   
		if("outbranch".equals(method)){  
			sql = "select * from inventory where  instatues = 1 and outstatues = 0 and  id = " + id;  
		}else if("inbranch".equals(method)){   
			sql = "select * from inventory where  outstatues = 1 and instatues = 0 and id = "+ id; 
		}
		 
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) { 
				flag = true ;
			} 
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			DB.close(rs);
			DB.close(stmt); 
			DB.close(conn);
		}       
		 
		return flag;
	}
	

	public static boolean save(User user,Inventory order) {
	     boolean flag  = false ; 
	     
	    // Map<Integer,Branch> branchmap = BranchManager.getIdMap();
	     List<String> sqls = new ArrayList<String>(); 
         int instatues = 0 ; 
         int outstatues = 0 ; 
		 int maxid;  
		
		 Inventory oldorder = InventoryManager.getInventoryID(user, order.getId());
		  
		 if(oldorder != null){ 
			 maxid = oldorder.getId();    
			 delete(maxid);
		 }else {   
			 oldorder = InventoryManager.getMaxOrder();
			 if(oldorder == null){
				 maxid = 1 ;
			 }else { 
				 maxid = oldorder.getId()+1;     
			 }
			 
		 }
		 

		if(maxid == 0){ 
			maxid = 1 ;
		}    
	
		
		List<String> sqlp = InventoryMessageManager.save(maxid, order);
	    
		 
	    sqls.addAll(sqlp);  
	    
	    /*if(branchmap != null){
			   Branch branch = branchmap.get(order.getOutbranchid());
		       if(branch != null){
		    	   if(user.getBranch().equals(branch.getLocateName())){
		    		   outstatues =1 ;
		    	   } 
		       } 
		       branch = branchmap.get(order.getInbranchid());
		       if(branch != null){
		    	   if(user.getBranch().equals(branch.getLocateName())){
		    		   instatues =1 ;
		    	   }
		    	   
		       } 
		   }*/
    
	    String sql = "insert into  inventory ( id ,uid , intime ,chekid, inbranchid, outbranchid" +
				", remark,outstatues,instatues,intype) values "+  
				"( "+maxid+", '"+order.getUid()+"', '"+order.getIntime()+"', '"+order.getChekid()+"', '"+order.getInbranchid()+"', '" 
	    		+order.getOutbranchid()+"', '"+order.getRemark()+"',"+outstatues+","+instatues+","+order.getIntype()+")";     
	    sqls.add(sql);

	    DBUtill.sava(sqls);
       
		return flag ; 

	}
	
	
	public static Inventory getMaxOrder(){
	    int id = 1 ;
	    Inventory order = null;
	    Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn);
		//  select top 1 * from table order by id desc 
		// select * from table where id in (select max(id) from table)
		String  sql = "select * from inventory where id in (select max(id) from inventory)" ;
		ResultSet rs = DB.getResultSet(stmt, sql);
		try { 
			while (rs.next()) { 
				order = InventoryManager.getCategoryFromRs(rs);
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
	
	public static boolean delete(int id) {
		   
	    boolean flag = false;
	    List<String> listsqls = new ArrayList<String>();
  
		String sqlp = InventoryMessageManager.delete(id);
       
          
        String sql = "delete from inventory where id = " + id ;
        listsqls.add(sqlp); 
        
        listsqls.add(sql); 
		 
		if (listsqls.size() == 0) {  
            return false;   
        }      
        flag = DBUtill.sava(listsqls);
		return flag ;
	}
	
	public static boolean deleteBybranchid(int id) {
		   
	    boolean flag = false;
	    List<String> listsqls = new ArrayList<String>();
  
		String sqlp = InventoryMessageManager.deletenybranchid(id);
       
          
        String sql = "delete from inventory where inbranchid = " + id + "  and intype = 2 ";
        listsqls.add(sqlp); 
        
        listsqls.add(sql); 
		 
		if (listsqls.size() == 0) {  
            return false;   
        }      
        flag = DBUtill.sava(listsqls);
		return flag ;
	}
	
	public static Inventory getInventoryID(User user ,int id){
		   
		Inventory orders = null;   
		   String sql = "";   
			   sql = "select * from  inventory where id = "+ id ;
	logger.info(sql); 
			    Connection conn = DB.getConn();
				Statement stmt = DB.getStatement(conn);
				ResultSet rs = DB.getResultSet(stmt, sql);
				try {  
					while (rs.next()) {  
						orders = getCategoryFromRs(rs); 
						List<InventoryMessage> listm =  InventoryMessageManager.getInventoryID(user,id); 
					    orders.setInventory(listm); 
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
	
	
	
	
	
	
	private static Inventory getCategoryFromRs(ResultSet rs){
		Inventory c = new Inventory(); 
		try {  
			c.setId(rs.getInt("id")); 
			c.setRemark(rs.getString("remark")); 
			c.setChekid(rs.getInt("chekid"));
			c.setUid(rs.getInt("uid"));  
			c.setIntime(rs.getString("intime"));
			c.setInbranchid(rs.getInt("inbranchid"));
			c.setOutbranchid(rs.getInt("outbranchid")); 
			c.setOutstatues(rs.getInt("outstatues")); 
			c.setInstatues(rs.getInt("instatues"));
			c.setIntype(rs.getInt("intype"));
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return c ;
	}
}
