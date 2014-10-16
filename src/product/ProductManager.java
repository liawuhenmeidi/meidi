package product;
	import java.sql.Connection;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.Statement;
	import java.util.ArrayList;
import java.util.HashMap;
	import java.util.List;

import orderproduct.OrderProduct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import category.Category;
import category.CategoryManager;
	import database.DB;
import user.User;
	
	public class ProductManager {
		 protected static Log logger = LogFactory.getLog(ProductManager.class);
		
		 public static List<Product> getProduct(String id) {
			List<Product> categorys = new ArrayList<Product>();
			Connection conn = DB.getConn();
			String sql = "select * from mdproduct where categoryID = "+ id + " and pstatues = 0";
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) {
					Product p = new Product();
					p.setId(rs.getInt("id"));
					p.setCategoryID(rs.getInt("categoryID"));
					p.setType(rs.getString("ptype"));
					p.setName(rs.getString("name"));
					categorys.add(p);
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
		
		 public static List<String> getProduct(int id) {
				List<String> categorys = new ArrayList<String>();
				Connection conn = DB.getConn();
				String sql = "select * from mdproduct where categoryID = "+ id + " and pstatues = 0";
				Statement stmt = DB.getStatement(conn);
				ResultSet rs = DB.getResultSet(stmt, sql);
				try {
					while (rs.next()) {
						categorys.add(rs.getString("ptype"));
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
		 
		 public static Product getProductbyname(String name) {
			    Product p = new Product();
				Connection conn = DB.getConn();
				String sql = "select * from mdproduct where ptype = '"+ name+"'";
logger.info(sql); 
				Statement stmt = DB.getStatement(conn);
				ResultSet rs = DB.getResultSet(stmt, sql);
				try {
					while (rs.next()) {  
						p.setId(rs.getInt("id"));
						p.setCategoryID(rs.getInt("categoryID"));
						p.setType(rs.getString("ptype"));
						p.setName(rs.getString("name"));
						
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DB.close(rs);
					DB.close(stmt);
					DB.close(conn);
				} 
				return p; 
			}
		 
		public static List<Product> getProduct(String id,int statues) {
			List<Product> categorys = new ArrayList<Product>();
			Connection conn = DB.getConn();
			String sql = "select * from mdproduct where pstatues = "+statues+" and categoryID = "+ id;
logger.info(sql);  
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) {
					Product p = new Product();
					p.setId(rs.getInt("id"));
					p.setCategoryID(rs.getInt("categoryID"));
					p.setType(rs.getString("ptype"));
					p.setName(rs.getString("name"));
					categorys.add(p);
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
		
	
		
		public static HashMap<Integer,ArrayList<Product>> getProduct() {
			HashMap<Integer,ArrayList<Product>> map = new HashMap<Integer,ArrayList<Product>>();
			Connection conn = DB.getConn();
			String sql = "select * from mdproduct where pstatues = 0";
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) {
					int categoryID = rs.getInt("categoryID");
					ArrayList<Product> list = map.get(categoryID);
					if(list == null){
						list = new ArrayList<Product>();
						map.put(categoryID, list);
					}
					Product p = new Product();
					p.setId(rs.getInt("id"));
					p.setCategoryID(categoryID);
					p.setType(rs.getString("ptype"));
					p.setName(rs.getString("name"));
					list.add(p);
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
		
		public static HashMap<String,Product> getProductType() {  
			HashMap<String,Product> map = new HashMap<String,Product>();
			Connection conn = DB.getConn(); 
			String sql = "select * from mdproduct where pstatues = 0";
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) {
					Product p = getOrderStatuesFromRs(rs);
					map.put(p.getType(), p);
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
		
		public static HashMap<Integer,Product> getProductID() {   
			HashMap<Integer,Product> map = new HashMap<Integer,Product>();
			Connection conn = DB.getConn();  
			String sql = "select * from mdproduct";
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) { 
					Product p = getOrderStatuesFromRs(rs);
					map.put(p.getId(), p);
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
		
		public static List<String> getProductlist() { 
			List<String> list = new ArrayList<String>();
			Connection conn = DB.getConn();  
			String sql = "select * from mdproduct where pstatues = 0";
			Statement stmt = DB.getStatement(conn); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) {
					int categoryID = rs.getInt("categoryID");
					Product p = new Product(); 
					p.setId(rs.getInt("id")); 
					p.setCategoryID(categoryID);
					p.setType(rs.getString("ptype"));
					p.setName(rs.getString("name"));
					list.add(p.getType());   
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
		
		
		
		
		public static HashMap<String,ArrayList<String>> getProductName() {
			HashMap<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
			Connection conn = DB.getConn();
			String sql = "select * from mdproduct where pstatues = 0  ";
			 
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try { 
				while (rs.next()) {
					String categoryID = rs.getInt("categoryID")+"";
					ArrayList<String> list = map.get(categoryID+"");
					if(list == null){
						list = new ArrayList<String>();
						map.put(categoryID+"", list);
					}
					String pName = rs.getString("ptype");
					list.add(pName);
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
		public static HashMap<String,ArrayList<String>> getProductName(int statues) {
			HashMap<String,ArrayList<String>> map = new HashMap<String,ArrayList<String>>();
			Connection conn = DB.getConn();
			String sql = "select * from mdproduct where pstatues = 0";
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) {
					String categoryID = rs.getInt("categoryID")+"";
					ArrayList<String> list = map.get(categoryID+"");
					if(list == null){
						list = new ArrayList<String>();
						map.put(categoryID+"", list);
					}
					String pName = rs.getString("ptype");
					list.add(pName);
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
		public static boolean delete(String str ) {
			String ids = "(" + str + ")";
			boolean b = false;
			Connection conn = DB.getConn();
			String sql = "update  mdproduct set pstatues = 1 where  id in " + ids; 
			Statement stmt = DB.getStatement(conn);
			ProductService.flag = true ;
			try {
				DB.executeUpdate(stmt, sql);
				b = true;
			} finally {
				DB.close(stmt);
				DB.close(conn);
			}
			return b;
		}

		
		public static void save(String type,String id ){
			Connection conn = DB.getConn();
			System.out.println("*****"+id);
			String sql = "insert into mdproduct(id, name, ptype,categoryID,pstatues) VALUES (null, null,?,?,0)";
			PreparedStatement pstmt = DB.prepare(conn, sql);
			try {
				pstmt.setString(1, type);
				pstmt.setString(2, id);
				pstmt.executeUpdate();
				ProductService.flag = true ;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(pstmt);
				DB.close(conn);
			}
		 
		}
		
		
		 public static Product getOrderStatuesFromRs(ResultSet rs){
			 Product p = new Product();  
				try { 
					p.setId(rs.getInt("id"));  
					p.setCategoryID(rs.getInt("categoryID"));
					p.setType(rs.getString("ptype"));  
					p.setName(rs.getString("name")); 
					p.setStatues(rs.getInt("pstatues"));
				} catch (SQLException e) { 
					e.printStackTrace();
				}
				return p;
		   }
		  
		 
 
	}


