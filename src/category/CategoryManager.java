package category;
import group.Group; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import user.User;
import user.UserManager;
import database.DB;
  
public class CategoryManager { 
	 protected static Log logger = LogFactory.getLog(CategoryManager.class);
	 
		public static void save(User user,Category category) {
			boolean flag = false ;
			String[] per = UserManager.getPermission(user);
			for(int i=0;i<per.length;i++){
	           if("0".equals(per[i])){
	        	  flag = true;
	           }
			}
			if(flag){
				Connection conn = DB.getConn();
				String sql = "insert into mdcategory values (null, ?,null)";
				PreparedStatement pstmt = DB.prepare(conn, sql);
				try {
					pstmt.setString(1, category.getName());
logger.info(category.getName());
					pstmt.executeUpdate();
				} catch (SQLException e) {   
					e.printStackTrace();
				} finally {
					DB.close(pstmt);
					DB.close(conn);
				}

			}
			
		}
		
        public static boolean getName(String c){
        	boolean flag = false ;
			Connection conn = DB.getConn();
			String sql = "select * from mdcategory where categoryname = '"+ c+ "' and cstatues = 0";
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) {
					flag = true ;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			}	
        	return flag;
        }
         
		public static boolean  save(Category c){
			Connection conn = DB.getConn();
			String sql = "insert into mdcategory(id,categoryname,pid,time,cstatues) values (null, ?,null,?,0)";
			PreparedStatement pstmt = DB.prepare(conn, sql);
			try {
				pstmt.setString(1, c.getName()); 
				pstmt.setString(2, c.getTime());
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(pstmt);
				DB.close(conn);
			}
		   return true;
		} 
		
		public  static boolean setStatues(int id , int statues){
			boolean flag = false ;
			String sql = "update mdcategory set cstatues = "+statues + "  where id = " + id;
			Connection conn = DB.getConn();
			Statement stmt = DB.getStatement(conn);
			try {   
				flag = stmt.execute(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return flag ;
			
			
		}
		
		public static List<Category> getCategory() {
			List<Category> categorys = new ArrayList<Category>();
			Connection conn = DB.getConn();
			String sql = "select * from mdcategory ";
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) {
					Category u = CategoryManager.getCategoryFromRs(rs);
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
		// 获取在售的产品
		public static List<Category> getCategory(int statues) {
			List<Category> categorys = new ArrayList<Category>();
			Connection conn = DB.getConn(); 
			String sql = "select * from mdcategory where cstatues = "+ statues;
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) {
					Category u = CategoryManager.getCategoryFromRs(rs);
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
		
	 // 对应用户在售的产品	
		
		public static List<Category> getCategory(User user,int statues) {
			 
			List<Category> categorys = new ArrayList<Category>();
			Connection conn = DB.getConn(); 
			String[] products = UserManager.getProducts(user);	 
		    String str = "(";
			for(int i=0;i<products.length;i++){
				str += products[i] +",";
			}  
			str = str.substring(0,str.length()-1)+")";
			String sql = ""; 
             if(UserManager.checkPermissions(user, Group.Manger)){
            	 sql = "select * from mdcategory" ;
             }else {    
            	 sql = "select * from mdcategory where cstatues = "+ statues+ " and  id  in " + str ;
             } 
			//String sql = " select mdcategory.*,mdgroup.* from mdcategory , mdgroup  where mdcategory.cstatues = "+statues+ " and  mdgroup.id = " + user.getUsertype()+ " and mdgroup.products like concat('%',mdcategory.id,'%')";
			
			Statement stmt = DB.getStatement(conn);
logger.info(sql);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) {
					Category u = CategoryManager.getCategoryFromRs(rs);
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
		
		// 通过id获取
		public static Category getCategory(String id) {
			Category u = new Category();
			Connection conn = DB.getConn();
			String sql = "select * from mdcategory where id = "+ id;
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) {
					u = CategoryManager.getCategoryFromRs(rs);
				}
			} catch (SQLException e) {
logger.info(e);
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			}
			return u;
		}
		public static HashMap<Integer,Category> getCategoryMap() {
			HashMap<Integer,Category> categorys = new HashMap<Integer,Category>();
			Connection conn = DB.getConn();
			String sql = "select * from mdcategory ";
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) {
					Category c = CategoryManager.getCategoryFromRs(rs);
					categorys.put(rs.getInt("id"), c);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			}
			return categorys;
		}
		/**
		 * 
		 * @param users
		 * @param pageNo
		 * @param pageSize
		 * @return 总共有多少条记录
		 */
		public static int getCategory(List<Category> users, int pageNo, int pageSize) {

			int totalRecords = -1;

			Connection conn = DB.getConn();
			String sql = "select * from mduser limit " + (pageNo - 1) * pageSize
					+ "," + pageSize;
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);

			Statement stmtCount = DB.getStatement(conn);
			ResultSet rsCount = DB.getResultSet(stmtCount,
					"select count(*) from user");

			try {
				rsCount.next();
				totalRecords = rsCount.getInt(1);

				while (rs.next()) {
					Category u = CategoryManager.getCategoryFromRs(rs);
					users.add(u);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rsCount);
				DB.close(stmtCount);
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			}
			return totalRecords;
		}

		
		public static boolean delete(String str ) {
			String ids = "(" + str + ")";
			boolean b = false;
			Connection conn = DB.getConn();
			String sql = "delete from mdcategory where id in " + ids;
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

		public static Category check(String username, String password)
				 {
			Category u = null;
			Connection conn = DB.getConn();
			String sql = "select * from mduser where name = '" + username + "'";
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				if(!rs.next()) {
					//throw new UserNotFoundException("用户不存在:" + username);
				} else {
					if(!password.equals(rs.getString("password"))) {
						//throw new PasswordNotCorrectException("密码不正确哦!");
					}
					u = new Category();
					u.setId(rs.getInt("id"));
					u.setName(rs.getString("name"));
					
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			}
			return u;
		}
		
		public static boolean update(Category user) {
			boolean flag = false ;
			Connection conn = DB.getConn();
			String sql = "update mdcategory set categoryname = ?, time = ? where id = ?";
			PreparedStatement pstmt = DB.prepare(conn, sql);
			try {
				pstmt.setString(1, user.getName());
				pstmt.setString(2, user.getTime());
				pstmt.setInt(3, user.getId());
				pstmt.executeUpdate();
				flag = true ;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(pstmt);
				DB.close(conn);
			}
			return flag ;
		}
		
		private static Category getCategoryFromRs(ResultSet rs){
			Category c = new Category();
			try { 
				c.setId(rs.getInt("id"));
				c.setName(rs.getString("categoryname"));
				c.setPid(rs.getInt("pid"));
				c.setTime(rs.getString("time")); 
				c.setStatues(rs.getInt("cstatues"));
				
			} catch (SQLException e) {
				e.printStackTrace();
			}	
			return c ;
		}
}
