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



import user.User;
import utill.StringUtill;
import utill.TimeUtill;

import database.DB;
 
public class InventoryBranchMessageManager {  
	protected static Log logger = LogFactory.getLog(InventoryBranchManager.class);
	
	public static List<String> save(Inventory inve ,int branchid ,int type ,String time) { 
        
		   List<InventoryMessage> orders = inve.getInventory() ; 
		   
		   List<String> sqls = new ArrayList<String>();
        
			 for(int i=0;i<orders.size();i++){ 
				InventoryMessage order = orders.get(i); 
				String sql = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, time,type,count,operatortype,inventcount)" + 
                        "  values ( null, '"+branchid+"', '"+inve.getId()+"','"+time+"','"+order.getProductId()+"',"+order.getCount()+","+type+",select count from mdinventorybranch where branchid = " +branchid + " and  type = '"+order.getProductId()+"' )"; 
				logger.info(sql);     
				sqls.add(sql);  
			} 
			 return sqls; 
	   }
	
	
public static List<InventoryBranchMessage> getCategory(String type,String branchid,String time) { 
		
		List<InventoryBranchMessage> categorys = new ArrayList<InventoryBranchMessage>();
		Connection conn = DB.getConn(); 
		String sql = "select * from mdinventorybranchmessage where type = '"+ type +"' and branchid = "+ branchid +time;

	logger.info(sql);	  
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {   
			while (rs.next()) {
				InventoryBranchMessage u = getCategoryFromRs(rs);
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
	
	   
public static List<InventoryBranchMessage> getCategory(String type,String branchid) { 
		
		List<InventoryBranchMessage> categorys = new ArrayList<InventoryBranchMessage>();
		Connection conn = DB.getConn(); 
		String sql = "select * from mdinventorybranchmessage where type = '"+ type +"' and branchid = "+ branchid ;
		
	logger.info(sql);	  
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {   
			while (rs.next()) {
				InventoryBranchMessage u = getCategoryFromRs(rs);
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
	
	private static InventoryBranchMessage getCategoryFromRs(ResultSet rs){
		InventoryBranchMessage c = new InventoryBranchMessage(); 
		try {    
			c.setId(rs.getInt("id"));  
			c.setBranchid(rs.getInt("branchid"));
			c.setCount(rs.getInt("count"));  
			c.setInventoryid(rs.getInt("inventoryid"));
			c.setType(rs.getString("type")); 
			c.setTime(rs.getString("time")); 
			c.setOperatortype(rs.getInt("operatortype"));
			c.setRealcount(rs.getInt("realcount"));
			c.setPapercount(rs.getInt("papercount"));
		} catch (SQLException e) { 
			e.printStackTrace();
		}	
		return c ;
	}
}
