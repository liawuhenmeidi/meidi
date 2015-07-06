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
 
public class CarsManager {
	 protected static Log logger = LogFactory.getLog(CarsManager.class);
	public static   List<String> save(User user ,Cars ca){
		CarsService.flag = true ; 
		   List<String> listsql = new ArrayList<String>();
	  
			     
				       
			String sql = "insert into mdcars (id,uid,num) " +
					   		"values (null,"+ca.getUid()+",'"+ca.getNum()+"') ;" ; 
	logger.info(sql);
			listsql.add(sql);
		 
		  return  listsql;
		   
	   }   
	
	public static   void saveDB(User user ,Cars ca){
		CarsService.flag = true ;  
		   List<String> listsql = new ArrayList<String>();
				       
			String sql = "insert into mdcars (id,uid,num) " +
					   		"values (null,"+ca.getUid()+",'"+ca.getNum()+"') ;" ; 
			 
			listsql.add(sql);
	         DBUtill.sava(listsql); 
		   
	   }  
	
	
  public static List<Cars>	getlist(){
	  List<Cars> list = new ArrayList<Cars>();
	  Connection conn = DB.getConn();
		String sql = "select * from  mdcars";
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				Cars ca = getCarsFromRs(rs);
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
	
  private static Cars getCarsFromRs(ResultSet rs){
	  Cars ca = new Cars();
		try {
			ca.setId(rs.getInt("id"));
			ca.setUid(rs.getInt("uid"));
			ca.setNum(rs.getString("num"));
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return ca ; 
	}
  
  
	
}
