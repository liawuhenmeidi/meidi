package ordersgoods;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import branch.BranchService;

import user.User;
import user.UserService;
import utill.StringUtill;
import utill.TimeUtill;

import database.DB;
   
public class OrderMessageManager {  
	protected static Log logger = LogFactory.getLog(OrderMessageManager.class);
     
	public static List<String> save(OrderMessage om){  
		List<String> list  = new ArrayList<String>();  
		String sqld = "delete from mdordermessage  where id = "+om.getId(); 
		  
		String sql = " insert into mdordermessage(id,oid,submitid,submittime,opstatues,branchid,remark) values ("+om.getId()+",'"+om.getOid()+"',"+om.getSubmitid()+",'"+om.getSubmittime()+"',"+om.getOpstatues()+","+om.getBranchid()+",'"+om.getRemark()+"')";
		   
		list.add(sqld); 
		list.add(sql);   
		return list ;
		 
	} 
	  
	public static String billing(String name,String ids,String statues,String branchtype){
		List<Integer> listbids = BranchService.getListids(Integer.valueOf(branchtype));
   
		String sql = " update mdordergoods,mdordermessage set mdordergoods.uuid = '"+name+"' ,mdordergoods.uuidtime = '"+TimeUtill.getdateString()+"', mdordergoods.opstatues = 1  where mdordergoods.mid = mdordermessage.id  and mdordermessage.branchid in ("+listbids.toString().substring(1,   
						listbids.toString().length() - 1)+")  and mdordermessage.id in "+ ids+" and mdordergoods.statues in"+statues; 
	    return sql; 
	     
	}   
	 
	public static String billingprint(String name){
		
		String sql = " update mdordergoods set opstatues = 2 where uuid = '"+name+"'";
	    return sql;  
	     
	} 
	
     public static String sendprint(String ids){
		String sql = " update mdordergoods,mdordermessage set mdordergoods.billingstatues = 1 ,mdordergoods.billingtime = '"+TimeUtill.getdateString()+"'  where mdordergoods.mid = mdordermessage.id and mdordermessage.id in "+ids;
	    return sql;   
	} 
     
	public static OrderMessage getbyid(User user,String id){
	    	OrderMessage og = null;
		 // logger.info(listids);
		   Connection conn = DB.getConn();
		   
		   String sql = " select * from mdordermessage where id=" +id;
			Statement stmt = DB.getStatement(conn); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) {
					og = getOrderMessageFromRs(rs);
				} 
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			}
		   return og ;
	   }
	
	public static int getMaxid(){
	    int id = 1 ;
	    Connection conn = DB.getConn();  
		Statement stmt = DB.getStatement(conn);
		String  sql = "select max(id)+1 as id from mdordermessage" ;
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				id = rs.getInt("id");
				if(id == 0){  
					id ++;
				}   
				//logger.info(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(stmt);
			DB.close(rs);
			DB.close(conn);
		 }
		return id;

  }
   public static OrderMessage  getOrderMessageFromRs(ResultSet rs){
	   OrderMessage  p = null;
		try {   
			p = new OrderMessage(); 
			p.setId(rs.getInt("mdordermessage.id")); 
			p.setOid(rs.getString("mdordermessage.oid"));
			p.setSubmitid(rs.getInt("mdordermessage.submitid"));
			p.setSubmittime(rs.getString("mdordermessage.submittime"));
			p.setRemark(rs.getString("mdordermessage.remark"));
			p.setBranchid(rs.getInt("branchid")); 
			p.setRemark(rs.getString("remark"));  
		} catch (SQLException e) {   
			e.printStackTrace();
		} 
		return p;  
   }
   
}
