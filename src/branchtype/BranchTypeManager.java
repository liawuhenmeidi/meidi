package branchtype;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import order.OrderManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import category.Category;
import category.CategoryManager;

import user.User;
import user.UserManager;
import database.DB;
  
public class BranchTypeManager {
	
	 protected static Log logger = LogFactory.getLog(BranchTypeManager.class);
	
	 
	 public static boolean  save(String  c){
		Connection conn = DB.getConn(); 
		String sql = "insert into mdbranchtype(id,bname) values (null, ?)";
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
	 
		public static List<BranchType> getLocate() {
			List<BranchType> users = new ArrayList<BranchType>();
			Connection conn = DB.getConn();
			String sql = "select * from mdbranchtype";
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) { 
					BranchType g = new BranchType();
					g.setId(rs.getInt("id"));
					g.setName(rs.getString("bname"));
					users.add(g);
				}  
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			}
			return users;
		}
 
		
		public static BranchType getLocate(int id) {
			BranchType g = new BranchType();
			Connection conn = DB.getConn();
			String sql = "select * from mdbranchtype where id = " + id ;
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) { 
					
					g.setId(rs.getInt("id"));
					g.setName(rs.getString("bname"));
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
		
		
		public static boolean delete(String str ) {
			String ids = "(" + str + ")";
			boolean b = false;
			Connection conn = DB.getConn();
			String sql = "delete from mdbranchtype where id in " + ids;
logger.info(sql);
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
}
