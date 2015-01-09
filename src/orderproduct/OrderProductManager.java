package orderproduct;


import group.Group;

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

import product.ProductService;


import servlet.OrderServlet;
import user.User;
import user.UserManager;
import utill.NumbleUtill;
import utill.StringUtill;
import utill.TimeUtill;
import database.DB;
  
public class OrderProductManager {

	protected static Log logger = LogFactory.getLog(OrderProductManager.class);

	   public static int updateOrderStatues(User user, String categoryid ,String type ,String count ,String oid){
		   int statues = -1 ; 
		   if(UserManager.checkPermissions(user, Group.dealSend)){
			   Connection conn = DB.getConn();  
			   if(!NumbleUtill.isNumeric(type)){
				   type = ProductService.gettypemap().get(type).getId()+"";
			   }
				String sql = "update mdorderproduct set saletype = '"+ type + "' , categoryID = "+categoryid+"  , count = "+count + " where orderid = " + oid + " and statues = 1 ";
				logger.info(sql);     
				PreparedStatement pstmt = DB.prepare(conn, sql);
				try { 
					statues = pstmt.executeUpdate();
					OrderProductService.flag = true ;
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
		OrderProductService.flag = true ;
        return sql ;
	}
	 
	public static String delete(int id,int statues) {
		String sql = "delete from mdorderproduct where orderid = " + id + " and statues = 1";
		OrderProductService.flag = true ;
        return sql ;
	}
	
	   public static List<String> save(int id, Order orderr) { 
               
		   List<OrderProduct> orders = orderr.getOrderproduct();
		   List<String> sqls = new ArrayList<String>();
             
			 for(int i=0;i<orders.size();i++){ 
			   
				OrderProduct order = orders.get(i); 
				String sql = "insert into  mdorderproduct (id, categoryID ,sendtype,saletype, count,orderid ,statues ,categoryname,salestatues,subtime,price)" +  
	                         "  values ( null, "+order.getCategoryId()+", '"+order.getSendType()+"', '"+order.getSaleType()+"',"+order.getCount()+","+id+","+order.getStatues()+",'"+order.getCategoryName()+"',"+order.getSalestatues()+",'"+TimeUtill.gettime()+"',"+order.getPrice()+")";
		logger.info(sql); 
				sqls.add(sql); 
				OrderProductService.flag = true ;
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
	   
	     
	   
	   public static List<OrderProduct> getlist(User user ,int type,int statues ,int num,int page,String sort,String search){
		   String str = "";
			  if(num != -1){
				 str = "  limit " + ((page-1)*num)+","+num ; 
			  }
			  
		   List<OrderProduct> products = new ArrayList<OrderProduct>();
		    
		   String sql = "select * from mdorderproduct where orderid in (select id from  mdorder where  mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))   and printSatues = 1 and (sendId != 0 or installid != 0 ) and deliveryStatues not in (0,3,8,9,10)  "+search+") and statues = 0  and chargeDealsendtime is null order by "+sort+str; ;
		   logger.info(sql); 
		   Connection conn = DB.getConn();
			Statement stmt = DB.getStatement(conn);
		   ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) {
					OrderProduct Order = getOrderStatuesFromRs(rs);
					products.add(Order);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(stmt);
				DB.close(rs);
				DB.close(conn);
			 }
			return products;
	   }
	   
	   public static int getlistcount(User user ,int type,int statues ,int num,int page,String sort,String search){
			  
		  int count = 0 ;
		     
		   String sql = "select count(*) from mdorderproduct where orderid in (select id from  mdorder where  mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))   and printSatues = 1 and (sendId != 0 or installid != 0 ) and deliveryStatues not in (0,3,8,9,10)  "+search+") and statues = 0  and chargeDealsendtime is null  order by "+sort;
		   logger.info(sql); 
		   Connection conn = DB.getConn();
			Statement stmt = DB.getStatement(conn);
		   ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) {
					count = rs.getInt(1); 
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally { 
				DB.close(stmt);
				DB.close(rs);
				DB.close(conn);
			 }
			return count;
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
	    
        public static Map<Integer,List<OrderProduct>> getOrderStatuesM(){
		   
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
				String saletype = rs.getString("saletype");
				String sendtype = rs.getString("sendtype");
				p.setId(rs.getInt("id"));
				p.setCategoryId(rs.getInt("categoryID"));
				p.setCount(rs.getInt("count"));
				p.setSaleType(saletype);
				p.setSendType(sendtype);
				if(!StringUtill.isNull(saletype)){
					p.setTypeName(ProductService.getIDmap().get(Integer.valueOf(saletype)).getType());
				}else if(!StringUtill.isNull(sendtype)){    
					p.setTypeName(ProductService.getIDmap().get(Integer.valueOf(sendtype)).getType());
				}
				p.setOrderid(Integer.valueOf(rs.getString("orderid")));
				p.setStatues(rs.getInt("statues"));  
				p.setCategoryName(rs.getString("categoryname"));
				p.setSalestatues(rs.getInt("salestatues")); 
				p.setSubtime(rs.getString("subtime")); 
				p.setPrice(rs.getDouble("price")); 
				p.setBarcode(rs.getString("Barcode"));
				p.setBatchNumber(rs.getString("batchNumber"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return p;
	   }
	  
}
