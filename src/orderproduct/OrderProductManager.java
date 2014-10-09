package orderproduct;


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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import servlet.OrderServlet;
import user.User;
import utill.TimeUtill;
import database.DB;
  
public class OrderProductManager {
	
	public static Map<Integer,List<OrderProduct>> OrPMap = OrderProductManager.getOrderStatuesM();
	
	public static Map<Integer,List<OrderProduct>> getStaticOrderStatuesM(){
		if(OrPMap == null){
			OrPMap = OrderProductManager.getOrderStatuesM();
		}
		return OrPMap;
	}
	
	public static List<OrderProduct> getStaticOrderStatuesMByid(int id){
		if(!OrderProductManager.getStaticOrderStatuesM().containsKey(id)){
			OrderProductManager.resetOrPMap();
		}
		List<OrderProduct> lists = OrderProductManager.getStaticOrderStatuesM().get(id);
		return lists;
	}
	
	public static void resetOrPMap(){
		 OrPMap = OrderProductManager.getOrderStatuesM();
	}
	
	protected static Log logger = LogFactory.getLog(OrderProductManager.class);
	 
	   public static int updateOrderStatues(User user, String categoryid ,String type ,String count ,String oid){
		   int statues = -1 ;
		   if(user.getUsertype() == 2  || user.getUsertype() == 1){
			   Connection conn = DB.getConn();  
				String sql = "update mdorderproduct set saletype = '"+ type + "' , categoryID = "+categoryid+"  , count = "+count + " where orderid = " + oid + " and statues = 1 ";
				logger.info(sql);     
				PreparedStatement pstmt = DB.prepare(conn, sql);
				try { 
					statues = pstmt.executeUpdate();
					OrderProductManager.resetOrPMap(); 
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DB.close(pstmt);
					DB.close(conn);
				}
			} 
		   return statues;
	   }
	public static String delete(int id) {
		String sql = "delete from mdorderproduct where orderid = " + id;
		OrderProductManager.resetOrPMap(); 
        return sql ;
	}
	  
	   public static List<String> save(int id, Order orderr) { 
               
		   List<OrderProduct> orders = orderr.getOrderproduct();
		   List<String> sqls = new ArrayList<String>();
             
			 for(int i=0;i<orders.size();i++){ 
			   
				OrderProduct order = orders.get(i); 
				String sql = "insert into  mdorderproduct (id, categoryID ,sendtype,saletype, count,orderid ,statues ,categoryname,salestatues,subtime)" +  
	                         "  values ( null, "+order.getCategoryId()+", '"+order.getSendType()+"', '"+order.getSaleType()+"',"+order.getCount()+","+id+","+order.getStatues()+",'"+order.getCategoryName()+"',"+order.getSalestatues()+",'"+TimeUtill.gettime()+"')";
				logger.info(sql); 
				sqls.add(sql); 
				OrderProductManager.resetOrPMap();
			} 
			 return sqls; 
	   }
	    
	    
	   public static int getMaxid(){
		   int id = 0 ;
		   Connection conn = DB.getConn();
			Statement stmt = DB.getStatement(conn);
			String  sql = "select max(id)+1 as id from mdorderstatues" ;
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) {
					id = rs.getInt("id");
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
	   
	     
	   public static List<OrderProduct> getOrderStatues(User user ,int id){
		   
		   List<OrderProduct> Orders = new ArrayList<OrderProduct>();
			    Connection conn = DB.getConn();
				Statement stmt = DB.getStatement(conn);
				String sql = "select * from  mdorderproduct where orderid = "  + id;
				logger.info(sql);
				ResultSet rs = DB.getResultSet(stmt, sql);
				try {  
					while (rs.next()) {
						OrderProduct Order = getOrderStatuesFromRs(rs);
						Orders.add(Order);
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
	    
	   
	   //author wilsonlee
	   public static Map<Integer,List<OrderProduct>> getOrderStatuesM(){
		   return getOrderStatuesM(new User());
	   }
	   
	   //author wilsonlee
	   public static List<OrderProduct> getOrderStatuesMByOrderID(int orderID){
		   List<OrderProduct> list = new ArrayList<OrderProduct>();
		    Connection conn = DB.getConn();
			Statement stmt = DB.getStatement(conn);
			String sql = "select * from  mdorderproduct where orderid = " + String.valueOf(orderID);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try { 
				while (rs.next()) {
					OrderProduct Order = getOrderStatuesFromRs(rs);
					list.add(Order);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(stmt);
				DB.close(rs);
				DB.close(conn);
			 }
			return list;
	   }
	   
        public static Map<Integer,List<OrderProduct>> getOrderStatuesM(User user){
		   
        	    Map<Integer,List<OrderProduct>> Orders = new HashMap<Integer,List<OrderProduct>>();
			    Connection conn = DB.getConn();
				Statement stmt = DB.getStatement(conn);
				String sql = "select * from  mdorderproduct ";
				ResultSet rs = DB.getResultSet(stmt, sql);
				try { 
					while (rs.next()) {
						OrderProduct Order = getOrderStatuesFromRs(rs);
						List<OrderProduct> list = Orders.get(Order.getOrderid());
			 			if(list == null){ 
							list = new ArrayList<OrderProduct>();
							Orders.put(Order.getOrderid(),list);
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
	   public static OrderProduct getOrderStatuesFromRs(ResultSet rs){
		   OrderProduct p = null;
			try {
				p = new OrderProduct();
				p.setCategoryId(rs.getInt("categoryID"));
				p.setCount(rs.getInt("count"));
				p.setSaleType(rs.getString("saletype"));
				p.setSendType(rs.getString("sendtype"));
				p.setOrderid(Integer.valueOf(rs.getString("orderid")));
				p.setStatues(rs.getInt("statues"));  
				p.setCategoryName(rs.getString("categoryname"));
				p.setSalestatues(rs.getInt("salestatues")); 
				p.setSubtime(rs.getString("subtime")); 
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return p;
	   }
	  
}
