package product;

import group.GroupService;

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

import database.DB;
import user.User;
import utill.DBUtill;

public class ProductManager {
	protected static Log logger = LogFactory.getLog(ProductManager.class);

	public static List<Product> getProduct(String id) {
		List<Product> categorys = new ArrayList<Product>();
		Connection conn = DB.getConn();
		String sql = "select * from mdproduct where categoryID = " + id
				+ " and pstatues = 0";
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

	public static List<String> save(List<Product> list) {
		List<String> listsql = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			Product p = list.get(i);
			//long start = System.currentTimeMillis();
			String sql = ProductManager.save(p);
			ProductService.flag = false; 
			//logger.info(System.currentTimeMillis() - start); 
			  
			listsql.add(sql);
		}
		ProductService.flag = true;
		return listsql;
	} 
 
	public static String save(Product p) {
		
		//long start = System.currentTimeMillis();
		
		 
		List<String> lists = ProductService.getlist(Integer.valueOf(p
				.getCategoryID()));
		 
		//long start1 = System.currentTimeMillis();
		//logger.info(start1 - start); 
		String sql = "";      
		if (null != lists) {  
			    
			if (lists.contains(p.getType())) {
				//logger.info(System.currentTimeMillis() - start1); 
				sql = "update mdproduct set pstatues = 0 ,saleType = '" + p.getSaleType() + "',encoded = '" + p.getEncoded()
						+ "'  where ptype = '"
						+ p.getType() + "' and categoryID = "+p.getCategoryID();
			} else {    
				sql = "insert into mdproduct(id, name, ptype,categoryID,pstatues,size,stockprice,mataintime,matainids,encoded,saleType) VALUES (null, null,'"
						+ p.getType() 
						+ "','"
						+ p.getCategoryID() 
						+ "',0,'"
						+ p.getSize()
						+ "',"
						+ p.getStockprice()
						+ ","
						+ p.getMataintime()
						+ ",'"
						+ p.getMatainids()
						+ "','"
						+ p.getEncoded() + "','" + p.getSaleType() + "')";
			}
		} else {
			sql = "insert into mdproduct(id, name, ptype,categoryID,pstatues,size,stockprice,mataintime,matainids,encoded,saleType) VALUES (null, null,'"
					+ p.getType()
					+ "','"
					+ p.getCategoryID()
					+ "',0,'"
					+ p.getSize()
					+ "',"
					+ p.getStockprice()
					+ ","
					+ p.getMataintime()
					+ ",'"
					+ p.getMatainids()
					+ "','"
					+ p.getEncoded() + "','" + p.getSaleType() + "')";
		}
		ProductService.flag = true;
		return sql;
	}

	public static List<Product> getProductList() {
		List<Product> categorys = new ArrayList<Product>();
		Connection conn = DB.getConn();
		String sql = "select * from mdproduct";
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				Product p = getOrderStatuesFromRs(rs);
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

	public static List<Product> getProductList(User user) {

		List<Product> categorys = new ArrayList<Product>();
		Connection conn = DB.getConn();
		String sql = "select * from mdproduct where pstatues = 0 and categoryID in ("
				+ GroupService.getidMap().get(user.getUsertype()).getProducts()
						.replaceAll("_", ",") + ")";
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

	public static List<String> getProduct(int id) {
		List<String> categorys = new ArrayList<String>();
		Connection conn = DB.getConn();
		String sql = "select * from mdproduct where categoryID = " + id
				+ " and pstatues = 0";
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

	public static Product getProductbyname(String id, String name) {
		Product p = null;
		Connection conn = DB.getConn();
		String sql = "select * from mdproduct where ptype = '" + name
				+ "' and categoryID = " + id;
		logger.info(sql);
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				p = new Product();
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

	public static List<Product> getProduct(String id, int statues) {
		List<Product> categorys = new ArrayList<Product>();
		Connection conn = DB.getConn();
		String sql = "select * from mdproduct where pstatues = " + statues
				+ " and categoryID = " + id;
		logger.info(sql);
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				Product p = getOrderStatuesFromRs(rs);
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

	public static HashMap<Integer, ArrayList<Product>> getProduct() {
		HashMap<Integer, ArrayList<Product>> map = new HashMap<Integer, ArrayList<Product>>();
		Connection conn = DB.getConn();
		String sql = "select * from mdproduct where pstatues = 0";
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				int categoryID = rs.getInt("categoryID");
				ArrayList<Product> list = map.get(categoryID);
				if (list == null) {
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

	public static HashMap<String, ArrayList<Product>> getProductstr() {
		HashMap<String, ArrayList<Product>> map = new HashMap<String, ArrayList<Product>>();
		Connection conn = DB.getConn();
		String sql = "select * from mdproduct where pstatues = 0";
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				int categoryID = rs.getInt("categoryID");
				ArrayList<Product> list = map.get(categoryID + "");
				if (list == null) {
					list = new ArrayList<Product>();
					map.put(categoryID + "", list);
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

	public static HashMap<String, Product> getProductType() {
		HashMap<String, Product> map = new HashMap<String, Product>();
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

	public static HashMap<Integer, Product> getProductID() {
		HashMap<Integer, Product> map = new HashMap<Integer, Product>();
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

	public static HashMap<String, ArrayList<String>> getProductName() {
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		Connection conn = DB.getConn();
		String sql = "select * from mdproduct where pstatues = 0  ";

		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				String categoryID = rs.getInt("categoryID") + "";
				ArrayList<String> list = map.get(categoryID + "");
				if (list == null) {
					list = new ArrayList<String>();
					map.put(categoryID + "", list);
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

	public static List<String> getAllProductName() {
		List<String> result = new ArrayList<String>();
		Connection conn = DB.getConn();
		String sql = "select * from mdproduct where pstatues = 0  ";

		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				String pName = rs.getString("ptype");
				result.add(pName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		return result;
	}

	public static HashMap<String, ArrayList<String>> getProductName(int statues) {
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		Connection conn = DB.getConn();
		String sql = "select * from mdproduct where pstatues = 0";
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				String categoryID = rs.getInt("categoryID") + "";
				ArrayList<String> list = map.get(categoryID + "");
				if (list == null) {
					list = new ArrayList<String>();
					map.put(categoryID + "", list);
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

	public static boolean delete(String str) {
		String ids = "(" + str + ")";
		boolean b = false;
		Connection conn = DB.getConn();
		String sql = "update  mdproduct set pstatues = 1 where  id in " + ids;
		Statement stmt = DB.getStatement(conn);
		ProductService.flag = true;
		try {
			DB.executeUpdate(stmt, sql);
			ProductService.flag = true;
			b = true;
		} finally {
			DB.close(stmt);
			DB.close(conn);
		}
		return b;
	}

	public static void save(String type, String id, double size,
			double stockprice, String mataintime, String matainids,
			String encoded, String saleType) {
		String sql = "";
		Product p = getProductbyname(id, type);
		if (null == p) {
			sql = "insert into mdproduct(id, name, ptype,categoryID,pstatues,size,stockprice,mataintime,matainids,encoded,saleType) VALUES (null, null,'"
					+ type
					+ "','"
					+ id
					+ "',0,'"
					+ size
					+ "',"
					+ stockprice
					+ ","
					+ mataintime
					+ ",'"
					+ matainids
					+ "','"
					+ encoded
					+ "'," + saleType + ")";
		} else {
			sql = "update mdproduct set pstatues = 0 where id = " + p.getId();
		}
		// System.out.println("*****"+id);
		DBUtill.sava(sql);
		ProductService.flag = true;
	}

	public static void update(String type, String id, double size,
			double stockprice, String mataintime, String matainids,
			String encoded, String saleType) {
		Connection conn = DB.getConn();
		String sql = "update mdproduct set ptype = ? , size = ? ,stockprice = ? ,mataintime = ? ,matainids = ? ,encoded = ? ,saleType = ?  where id = ?";
		PreparedStatement pstmt = DB.prepare(conn, sql);
		try {
			pstmt.setString(1, type);
			pstmt.setDouble(2, size);
			pstmt.setDouble(3, stockprice);
			pstmt.setString(4, mataintime);
			pstmt.setString(5, matainids);
			pstmt.setString(6, encoded);
			pstmt.setString(7, saleType);
			pstmt.setString(8, id);
			pstmt.executeUpdate();
			ProductService.flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}

	}

	public static Product getOrderStatuesFromRs(ResultSet rs) {
		Product p = new Product();
		try {
			p.setId(rs.getInt("id"));
			p.setCategoryID(rs.getInt("categoryID"));
			p.setType(rs.getString("ptype"));
			p.setName(rs.getString("name"));
			p.setStatues(rs.getInt("pstatues"));
			p.setSize(rs.getDouble("size"));
			p.setStockprice(rs.getDouble("stockprice"));
			p.setMatainids(rs.getString("matainids"));
			p.setMataintime(rs.getInt("mataintime"));
			p.setEncoded(rs.getString("encoded"));
			p.setSaleType(rs.getInt("saleType"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return p;
	}

}
