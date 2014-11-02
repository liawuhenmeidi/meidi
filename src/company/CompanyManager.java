package company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import database.DB;
  
public class CompanyManager {
	
	 protected static Log logger = LogFactory.getLog(CompanyManager.class);
	
	 public static boolean  save(Company  c){
		delete();
		Connection conn = DB.getConn(); 
		String sql = "insert into mdcompany(cname,uname,phone,locate,locatedetail) values (?,?,?,?,?)";
		PreparedStatement pstmt = DB.prepare(conn, sql);
		try {       
			pstmt.setString(1, c.getName());
			pstmt.setString(2, c.getUsername());
			pstmt.setString(3, c.getPhone());
			pstmt.setString(4, c.getLocate()); 
			pstmt.setString(5, c.getLocation()); 
			logger.info(pstmt);  
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { 
			DB.close(pstmt);
			DB.close(conn);
		}
	   return true;
	}
	  
	 public static boolean delete() {
			boolean b = false; 
			Connection conn = DB.getConn();
			String sql = "delete from mdcompany ";
			Statement stmt = DB.getStatement(conn);
			try {
				DB.executeUpdate(stmt, sql);
				b = true;
			} finally {
				DB.close(stmt);
				DB.close(conn);
			}
			return b;
		}
	 
	 public static boolean  update(String  c ,String bid ){
			Connection conn = DB.getConn(); 
			String sql = "update mdbranchtype set bname = ? where id = " + bid ;
			PreparedStatement pstmt = DB.prepare(conn, sql);
			try { 
				pstmt.setString(1, c);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(pstmt);
				DB.close(conn);
			}
		   return true;
		}
	 
		public static Company getLocate() {
			Company g = new Company();
			Connection conn = DB.getConn();
			String sql = "select * from mdcompany " ;
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  // cname,uname,phone,locate
				while (rs.next()) { 
					g.setLocate(rs.getString("locate"));
					g.setName(rs.getString("cname"));
					g.setPhone(rs.getString("phone"));  
					g.setUsername(rs.getString("uname"));
					g.setLocation(rs.getString("locatedetail"));
					g.setUsercount(rs.getInt("usetcunt"));
				}   
			} catch (SQLException e) { 
				e.printStackTrace();
			} finally { 
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			} 
			return g;
		}
}
