package logistics;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database.DB;

import makeInventory.MakeInventory;
import user.User;
import utill.DBUtill;
import utill.TimeUtill;

public class LogisticsMessageManager {
	public static   List<String> save(User user ,LogisticsMessage lm){
		   List<String> listsql = new ArrayList<String>();
		    
			String sql = "insert into mdlogistics (id,uid,bid,carid,submittime,statues,prince,sendtime) " +
					   		"values (null,"+lm.getUid()+",'"+lm.getBid()+"','"+lm.getCarid()+"','"+lm.getSubmittime()+"','"+lm.getStatues()+"','"+lm.getPrice()+"','"+lm.getSendtime()+"') ;" ; 
			 
			   listsql.add(sql);
	 
		  return  listsql;
		    
	   }    
	  
	public static   boolean updateLocate(User user ,String locate,String id ){
		  String sql = "update mdlogistics set locateMessage =  CONCAT(locateMessage,',"+TimeUtill.gettimeString()+"::"+locate+"') where id ="+id;
		  return  DBUtill.sava(sql);  
		    
	   }     
	
	public static   boolean updatestatues(User user ,String id ){
		  String sql = "update mdlogistics set statues = 1  where id ="+id;
		  return  DBUtill.sava(sql);  
		    
	   }
	public static   boolean  saveDB(User user ,LogisticsMessage lm){
		List<String> sqls= save(user,lm);
		 return DBUtill.sava(sqls); 
		    
	   }      
	  
	 public static List<LogisticsMessage>	getlist(){
		  List<LogisticsMessage> list = new ArrayList<LogisticsMessage>();
		  Connection conn = DB.getConn(); 
			String sql = "select * from  mdlogistics";
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) {
					LogisticsMessage ca = getLogisticsMessageFromRs(rs);
					list.add(ca);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			} 
			return list;
	  } 
	     
	 public static List<LogisticsMessage>	getlist(int statues){
		  List<LogisticsMessage> list = new ArrayList<LogisticsMessage>();
		  Connection conn = DB.getConn(); 
			String sql = "select * from  mdlogistics where statues = "+statues;
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) {
					LogisticsMessage ca = getLogisticsMessageFromRs(rs);
					list.add(ca);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			} 
			return list;
	  } 
	 
	 public static List<LogisticsMessage>	getlist(int uid,int statues){
		  List<LogisticsMessage> list = new ArrayList<LogisticsMessage>();
		  Connection conn = DB.getConn(); 
			String sql = "select * from  mdlogistics where statues = "+statues +" and uid="+uid;
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) {
					LogisticsMessage ca = getLogisticsMessageFromRs(rs);
					list.add(ca);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			} 
			return list;
	  }
	 
	 public static LogisticsMessage	getByid(int id){ 
		 LogisticsMessage ca = null; 
		  Connection conn = DB.getConn();       
			String sql = "select * from  mdlogistics where id = "+id;
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) { 
					ca = getLogisticsMessageFromRs(rs);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			}  
			return ca;
	  } 
	 
	 
	 public static List<LogisticsMessage>	getlist(User user ,int statues){
		  List<LogisticsMessage> list = new ArrayList<LogisticsMessage>();
		  Connection conn = DB.getConn();    
			String sql = "select * from  mdlogistics where uid = "+ user.getId()+" and statues = "+statues;
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) {
					LogisticsMessage ca = getLogisticsMessageFromRs(rs);
					list.add(ca);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			} 
			return list;
	  } 
	 
	 public static boolean updatecharge(String ids){
		 String sql = "update mdlogistics set statues = 2 where id in "+ids;
		 return  DBUtill.sava(sql);
		 		  
	 }
	 public static List<LogisticsMessage>	getlist(String ids){
		  List<LogisticsMessage> list = new ArrayList<LogisticsMessage>();
		  Connection conn = DB.getConn();    
			String sql = "select * from  mdlogistics where id in "+ ids;
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try { 
				while (rs.next()) {
					LogisticsMessage ca = getLogisticsMessageFromRs(rs);
					list.add(ca);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			} 
			return list;
	  } 
	 
	 private static LogisticsMessage getLogisticsMessageFromRs(ResultSet rs){
		 LogisticsMessage ca = new LogisticsMessage();
			try { 
				ca.setId(rs.getInt("id"));
				ca.setUid(rs.getInt("uid")); 
				ca.setBid(rs.getInt("bid"));
				ca.setCarid(rs.getInt("carid")); 
				ca.setPrice(rs.getInt("prince"));
				ca.setStatues(rs.getInt("statues"));
				ca.setSubmittime(rs.getString("submittime"));
				ca.setSendtime(rs.getString("sendtime"));
				ca.setLocateMessage(rs.getString("locateMessage"));
			} catch (SQLException e) { 
				e.printStackTrace();
			}	
			return ca ; 
		}
	  
	  
	 
}
