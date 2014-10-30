package gift;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import order.Order;
import orderproduct.OrderProduct;
import orderproduct.OrderProductManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import user.User;
import user.UserManager;
import database.DB;
	    
	public class GiftManager {  
		 protected static Log logger = LogFactory.getLog(GiftManager.class);
		 
		 public static Map<Integer,List<Gift>> OrPMap; 
		 
		 
		 public static Map<Integer,List<Gift>> getOrderStatues(User user){
			   
    	    Map<Integer,List<Gift>> Orders = new HashMap<Integer,List<Gift>>();
		    Connection conn = DB.getConn();
			Statement stmt = DB.getStatement(conn);
			String sql = "select * from  mdordergift "; 
			logger.info(sql); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try { 
				while (rs.next()) {
					Gift Order = getGiftFromRs(rs); 
					List<Gift> list = Orders.get(Order.getOrderId());
					if(list == null){
						list = new ArrayList<Gift>();
						Orders.put(Order.getOrderId(),list);
					}  
					list.add(Order);
				} 
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(stmt);
				DB.close(rs);
				DB.close(conn);
			 } 
			return Orders;
	 }
		
		 public static Map<Integer,List<Gift>> getOrderStatuesM(){
					if(OrPMap == null){
						OrPMap = GiftManager.getOrderStatues(new User());
					}
					return OrPMap;
		 }
		 
		 
		 public static Map<Integer,List<Gift>> resetOrPMap(){
			 OrPMap = GiftManager.getOrderStatues(new User()); 
			 return OrPMap;
		}
		 
			public static void save(User user,Gift Gift) {
				boolean flag = false ;
				String[] per = UserManager.getPermission(user);
				for(int i=0;i<per.length;i++){
		           if("0".equals(per[i])){
		        	  flag = true;
		           }
				}
				if(flag){
					Connection conn = DB.getConn();
					String sql = "insert into mdGift values (null, ?,null)";
					PreparedStatement pstmt = DB.prepare(conn, sql);
					try {
						pstmt.setString(1, Gift.getName());
						logger.info(Gift.getName());
						pstmt.executeUpdate();
						resetOrPMap();
					} catch (SQLException e) {   
						e.printStackTrace();
					} finally {
						DB.close(pstmt);
						DB.close(conn);
					}

				}
				
			}
			 
			
			 public static List<String> save(int id, Order orderr) {
				 List<String> sqls = new ArrayList<String>();
				 List<Gift> orders = orderr.getOrdergift();
				 if(null != orders){
					 
					for(int i=0;i<orders.size();i++){
						Gift order = orders.get(i);
						String sql = "insert into  mdordergift(id, orderid ,giftName,count,statues)" +
			                         "  values ( null, "+id+", '"+order.getName()+"', "+order.getCount()+","+order.getStatues()+")";
	                    sqls.add(sql);
						
				   } 
				 }
				 
				 GiftManager.resetOrPMap();
				return sqls ;
		   }
			 
			 
			 
	        public static boolean getName(String c){
	        	boolean flag = false ;
				Connection conn = DB.getConn();
				String sql = "select * from mdGift where Giftname = '"+ c+ "'";
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
	        
			public static boolean  save(Gift c){
				Connection conn = DB.getConn();
				String sql = "insert into mdGift(id,Giftname,pid,time) values (null, ?,null,?)";
				PreparedStatement pstmt = DB.prepare(conn, sql);
				try {
					pstmt.setString(1, c.getName());
					pstmt.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DB.close(pstmt);
					DB.close(conn);
				}
			   return true;
			}
			 
			public static List<Gift> getGift() {
				List<Gift> Gifts = new ArrayList<Gift>();
				Connection conn = DB.getConn();
				String sql = "select * from mdGift";
				Statement stmt = DB.getStatement(conn);
				ResultSet rs = DB.getResultSet(stmt, sql);
				try {
					while (rs.next()) {
						Gift u = GiftManager.getGiftFromRs(rs);
						Gifts.add(u);
					}
				} catch (SQLException e) {
					logger.error(e);
				} finally {
					DB.close(rs);
					DB.close(stmt);
					DB.close(conn);
				}
				logger.info(Gifts.size());
				return Gifts;
			}
			// 根据订单号获取赠品
			public static List<Gift> getGift(User user ,int id) {
				List<Gift> Gifts = new ArrayList<Gift>();
				Connection conn = DB.getConn();
				String sql = "select * from mdordergift where orderid =  " + id ;
				Statement stmt = DB.getStatement(conn);
				ResultSet rs = DB.getResultSet(stmt, sql);
				try {
					while (rs.next()) {
						Gift u = GiftManager.getGiftFromRs(rs);
						Gifts.add(u);
					}
				} catch (SQLException e) {
					logger.error(e);
				} finally {
					DB.close(rs);
					DB.close(stmt);
					DB.close(conn);
				}
				logger.info(Gifts.size());
				return Gifts;
			}
			// 通过id获取
			public static Gift getGift(String id) {
				Gift u = new Gift();
				Connection conn = DB.getConn();
				String sql = "select * from mdGift where id = "+ id;
				Statement stmt = DB.getStatement(conn);
				ResultSet rs = DB.getResultSet(stmt, sql);
				try {
					while (rs.next()) {
						u = GiftManager.getGiftFromRs(rs);
					}
				} catch (SQLException e) {
					System.out.print(e);
				} finally {
					DB.close(rs);
					DB.close(stmt);
					DB.close(conn);
				}
				return u;
			}
			public static HashMap<Integer,Gift> getGiftMap() {
				HashMap<Integer,Gift> Gifts = new HashMap<Integer,Gift>();
				Connection conn = DB.getConn();
				String sql = "select * from mdGift";
				Statement stmt = DB.getStatement(conn);
				ResultSet rs = DB.getResultSet(stmt, sql);
				try {
					while (rs.next()) {
						Gift c = GiftManager.getGiftFromRs(rs);
						Gifts.put(rs.getInt("id"), c);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DB.close(rs);
					DB.close(stmt);
					DB.close(conn);
				}
				return Gifts;
			}
			/**
			 * 
			 * @param users
			 * @param pageNo
			 * @param pageSize
			 * @return 总共有多少条记录
			 */
			public static int getGift(List<Gift> users, int pageNo, int pageSize) {

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
						Gift u = GiftManager.getGiftFromRs(rs);
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
 
			// 删除对应订单号的
			public static String delete(int id  ) {
				String sql = "delete from mdordergift where orderid =" + id;
				return sql;
			}

			public static Gift check(String username, String password)
					 {
				Gift u = null;
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
						u = new Gift();
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
			
			public void update(Gift user) {
				Connection conn = DB.getConn();
				String sql = "update mduser set type = ?, products = ? where id = ?";
				PreparedStatement pstmt = DB.prepare(conn, sql);
				try {
					
					pstmt.setInt(3, user.getId());
					pstmt.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DB.close(pstmt);
					DB.close(conn);
				}
			}
			
			private static Gift getGiftFromRs(ResultSet rs){
				Gift c = new Gift();
				try {
					c.setId(rs.getInt("id"));   
					c.setName(rs.getString("Giftname"));
					c.setCount(rs.getInt("count"));
					
					c.setOrderId(rs.getInt("orderid"));
					c.setStatues(rs.getInt("statues"));
				} catch (SQLException e) {
					e.printStackTrace();
				}	
				return c ;
			}
	}

