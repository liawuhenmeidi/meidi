package installsale;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import java.util.Map; 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import utill.DBUtill;

import company.CompanyManager;
import database.DB;

public class InstallSaleManager {
	 protected static Log logger = LogFactory.getLog(CompanyManager.class);

	 public static boolean  save(InstallSale  in){
		String sql = "insert into installSale(id,uid,uname,phone,locate,andate,message) values (null,"+in.getUid()+","+in.getUname()+","+in.getPhone()+","+in.getLocate()+","+in.getAndate()+",'"+in.getMessage()+"')";
		//logger.info(sql);
		return DBUtill.sava(sql); 
	 }  
	  
	 public static boolean  update(InstallSale  in){
			//String sql = "update installSale(id,uid,uname,phone,locate,andate,message) values ("+in.getId()+","+in.getUid()+","+in.getUname()+","+in.getPhone()+","+in.getLocate()+","+in.getAndate()+",'"+in.getMessage()+"')";
			String sql = "update installSale set uid = "+in.getUid()+" ,uname ="+in.getUname()+" ,phone ="+in.getPhone()+",locate ="+in.getLocate()+" ,andate ="+in.getAndate()+" ,message = '"+in.getMessage()+"' where id = " +in.getId()  ;
			//logger.info(sql); 
			return DBUtill.sava(sql); 
		 } 
	  
	 public static Map<String,InstallSale> getmap(){
		    HashMap<String,InstallSale> map = new HashMap<String,InstallSale>(); 
			Connection conn = DB.getConn();    
			String sql = "select * from installSale";  
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {   
				while (rs.next()) {
					InstallSale in = getInstallSaleFromRs(rs);
					map.put(in.getUid()+"", in);  
				} 
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			} 
			return map;

	 }
	 
	 public static InstallSale getmap(String id){
		    InstallSale in = null; 
			Connection conn = DB.getConn();    
			String sql = "select * from installSale where id = "+id;  
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {   
				while (rs.next()) {
					in = getInstallSaleFromRs(rs);
				} 
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			} 
			return in;

	 }
	 
	 private static InstallSale getInstallSaleFromRs(ResultSet rs){
		    InstallSale in = new InstallSale();
			try {   
				in.setUid(rs.getInt("uid"));
				in.setId(rs.getInt("id"));  
				in.setPhone(rs.getInt("phone"));
				in.setUname(rs.getInt("uname"));
				in.setAndate(rs.getInt("andate"));
				in.setLocate(rs.getInt("locate"));
				in.setMessage(rs.getString("message"));
			} catch (SQLException e) {
				e.printStackTrace();
			}	 
			return in ;
		}
	 
}
