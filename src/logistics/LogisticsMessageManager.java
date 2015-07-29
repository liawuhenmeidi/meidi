package logistics;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;
 
import database.DB;
import user.User;
import utill.DBUtill;
import utill.TimeUtill;

public class LogisticsMessageManager {
	protected static Log logger = LogFactory.getLog(LogisticsMessageManager.class);
	public static   List<String> save(User user ,LogisticsMessage lm){
		   List<String> listsql = new ArrayList<String>();
		    
			String sql = "insert into mdlogistics (id,uid,bid,carid,submittime,statues,prince,sendtime,locates,remark,advanceprice,startlocate,pid) " +
					   		"values (null,"+lm.getUid()+",'"+lm.getBid()+"','"+lm.getCarid()+"','"+lm.getSubmittime()+"','"+lm.getStatues()+"','"+lm.getPrice()+"','"+lm.getSendtime()+"'" +
					   				",'"+lm.getLocates()+"','"+lm.getRemark()+"','"+lm.getAdvancePrice()+"','"+lm.getStartLocate()+"','"+lm.getPid()+"') ;" ; 
			   
			listsql.add(sql);
	  
		  return  listsql; 
		    
	   }       
	    
	 public static int getMaxid(){
		    int id = 1 ; 
		    Connection conn = DB.getConn();
			Statement stmt = DB.getStatement(conn);
			String  sql = "select max(id)+1 as id from mdlogistics" ;
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) {
					id = rs.getInt("id");
					logger.info(id);
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
	public static   List<String> saveID(User user ,LogisticsMessage lm){
		   List<String> listsql = new ArrayList<String>();
		    
			String sql = "insert into mdlogistics (id,uid,bid,carid,submittime,statues,prince,sendtime,locates,remark,advanceprice,startlocate,pid) " +
					   		"values ("+lm.getId()+","+lm.getUid()+",'"+lm.getBid()+"','"+lm.getCarid()+"','"+lm.getSubmittime()+"','"+lm.getStatues()+"','"+lm.getPrice()+"','"+lm.getSendtime()+"'" +
					   				",'"+lm.getLocates()+"','"+lm.getRemark()+"','"+lm.getAdvancePrice()+"','"+lm.getStartLocate()+"','"+lm.getPid()+"') ;" ; 
			   
			listsql.add(sql); 
	   
		  return  listsql; 
		    
	   }  
	
	
	
	public static   boolean delete(User user ,String ids ){
		  String sql ="update mdlogistics set statues = -1 ,operation = 0 where id in ("+ids+")";
		  logger.info(sql);   
		  return  DBUtill.sava(sql);    
		     
	   }   
	    
	
	public static   boolean updaterequest(User user ,String ids ,LogisticsMessage lm){ 
		List<String> sqls = new ArrayList<String>();
		  int id = getMaxid();
		  lm.setId(id); 
		  sqls.addAll(saveID(user, lm)); 
		  String sql ="update mdlogistics set operation = 4 ,upid = "+id+" where id in ("+ids+")";
		  sqls.add(sql);  
		  logger.info(sql);   
		  return  DBUtill.sava(sqls);     
		    
	   }   
	  
	public static   boolean updateAgree(User user ,String ids,String num,String upid ){
		 List<String> sqls = new ArrayList<String>();  
		  String sql ="update mdlogistics set operation = "+num+" where id in ("+ids+")";
		  if(num.equals(0)){
			  String sqld ="delete from mdlogistics where id = "+upid; 
			  sqls.add(sqld);
		  } 
		  sqls.add(sql);  
		  logger.info(sqls);    
		  return  DBUtill.sava(sqls);     
		    
	   } 
	    
	public static   boolean update(User user ,LogisticsMessage lm){
		  List<String> sqls = new ArrayList<String>();  
		// id,uid,bid,carid,submittime,statues,prince,sendtime,locates,remark,advanceprice,startlocate,pid
  
		  String sqlu ="update mdlogistics set statues ="+lm.getStatues()+",locatemessage= '"+lm.getLocateMessage()+"',advancestatues='"+lm.getAdvanceStatues()+"'  where id ="+lm.getUpid();
		  String sqld ="delete from mdlogistics where id = "+lm.getId(); 
		  sqls.add(sqlu);
		  sqls.add(sqld);   
		  logger.info(sqls);    
		  return  DBUtill.sava(sqls);        
	   } 
	
	public static   boolean deleteRequest(User user ,String ids ){
		  String sql ="update mdlogistics set operation = 1  where id in ("+ids+")";
		  return  DBUtill.sava(sql);   
	   }    
	      
	public static   boolean deleteAgree(User user ,String ids,String num){
		  List<String> sqls = new ArrayList<String>();
		  String sql ="update mdlogistics set operation = "+num+"  where id in ("+ids+")";
		  logger.info(sql);   
		  sqls.add(sql);
		  return  DBUtill.sava(sqls);   
		     
	   }  
	
	
	public static   boolean updateLocate(User user ,String locate,String id ){
		  String sql = "update mdlogistics set locateMessage =  CONCAT(locateMessage,',"+TimeUtill.gettimeString()+"::"+locate+"') where id ="+id;
		  return  DBUtill.sava(sql);  
		    
	   }     
	
	public static   boolean updatestatues(User user ,String id ){
		  String sql = "update mdlogistics set statues = 1  where id ="+id;
		  return  DBUtill.sava(sql);  
		    
	   } 
	
	public static   boolean updateAgree(User user ){
		  String sql = "update mdlogistics set statues = 3  where statues = 2 and uid = "+ user.getId();
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
	 
	 
	 public static List<LogisticsMessage>	getlist( String statues){
		  List<LogisticsMessage> list = new ArrayList<LogisticsMessage>();
		  Connection conn = DB.getConn(); 
			String sql = "select * from  mdlogistics where statues in ("+statues+")";
			logger.info(sql);  
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
	 
	 public static List<LogisticsMessage>	getAdvancePrince(User user,String statues){
		  List<LogisticsMessage> list = new ArrayList<LogisticsMessage>(); 
		  Connection conn = DB.getConn(); 
			String sql = "select * from  mdlogistics where  advanceprice != 0 and advancestatues in ("+statues+")";
		   logger.info(sql); 
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
	 
	 public static List<LogisticsMessage>	getlist(int uid,String statues){
		  List<LogisticsMessage> list = new ArrayList<LogisticsMessage>();
		  Connection conn = DB.getConn();
			String sql = "select * from  mdlogistics where statues in ("+statues +") and uid="+uid;
		   logger.info(sql);
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
	 
	 public static List<LogisticsMessage>	getlist(int uid){
		  List<LogisticsMessage> list = new ArrayList<LogisticsMessage>();
		  Connection conn = DB.getConn();
			String sql = "select * from  mdlogistics where  uid="+uid;
		   logger.info(sql);
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
	 
	 public static boolean updateAdvancePrince(String ids,String statues){
		 String sql = "update mdlogistics set advancestatues = "+statues+" where id in "+ids;
		 return  DBUtill.sava(sql);  
		 		   
	 }
	 public static boolean updatecharge(String ids,String statues){
		 String sql = "update mdlogistics set statues = "+statues+" where id in "+ids;
		 return  DBUtill.sava(sql); 
		 		   
	 }
	 
	 public static List<LogisticsMessage>	getlistByIds(String ids){
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
				ca.setAdvancePrice(rs.getInt("advanceprice"));
				ca.setLocates(rs.getString("locates"));
				ca.setStartLocate(rs.getString("startlocate"));
				ca.setRemark(rs.getString("remark"));  
				ca.setAdvanceStatues(rs.getInt("advancestatues"));
				ca.setPid(rs.getInt("pid"));    
				ca.setOperation(rs.getInt("operation")); 
                ca.setUpid(rs.getInt("upid")); 
		   
	                
	             
			} catch (SQLException e) {  
				e.printStackTrace(); 
			}	
			return ca ; 
		}
	  
	  
	 
}
