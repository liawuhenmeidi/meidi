package order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import user.User;
import database.DB;

public class OrderStatuesManager {
	   public boolean updateOrderStatues(User user, OrderStatues order){
		   if(user.getUsertype() == 2  || user.getUsertype() == 1){
			   Connection conn = DB.getConn();
				String sql = "update mdorderStatues set shipmentStatues = ?, deliveryStatues = ?" +
						", sendId = ?, installTime = ?";
				PreparedStatement pstmt = DB.prepare(conn, sql);
				try {
					pstmt.setInt(1, order.getShipmentStatues());
					pstmt.setInt(2, order.getDeliveryStatues());
					pstmt.setInt(3, order.getSendId());
					pstmt.setString(4, order.getInstallTime());
					pstmt.executeUpdate();
					return true ;
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DB.close(pstmt);
					DB.close(conn);
				}
			}
		   return false;
	   }
	  
	   public static void save(int id,OrderStatues order) {
		   
		    
			Connection conn = DB.getConn();
			String sql = "insert into  mdorderStatues (id, shipmentStatues ,deliveryStatues , sendId , installTime )" +
                         "  values ( ?, ?, ?, ?,?)";
			PreparedStatement pstmt = DB.prepare(conn, sql);
			try {
				pstmt.setInt(1, order.getId());
				pstmt.setInt(2, order.getShipmentStatues());
				pstmt.setInt(3, order.getDeliveryStatues());
				pstmt.setInt(4, order.getSendId());
				pstmt.setString(5,order.getInstallTime());
				pstmt.executeUpdate();
				OrderStatues.i ++ ;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(pstmt);
				DB.close(conn);
			}
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
	   public OrderStatues getOrderStatues(User user ,int id){
		   OrderStatues Orders = new OrderStatues();
		   if(user.getUsertype() == 1){
			    Connection conn = DB.getConn();
				Statement stmt = DB.getStatement(conn);
				String sql = "select * from  desc where id = "  + id;
				ResultSet rs = DB.getResultSet(stmt, sql);
				try {
					while (rs.next()) {
						Orders = getOrderStatuesFromRs(rs);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DB.close(stmt);
					DB.close(rs);
					DB.close(conn);
				 }
		      }
				return Orders;
		 }
	   
	   public OrderStatues getOrderStatuesFromRs(ResultSet rs){
		   OrderStatues p = null;
			try {
				p = new OrderStatues();
				p.setId(rs.getInt("id"));
			    p.setSendId(rs.getInt("sendId"));
			    p.setDeliveryStatues(rs.getInt("deliveryStatues"));
			    p.setInstallTime(rs.getString("installTime"));
			    p.setShipmentStatues(rs.getInt("shipmentStatues"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return p;
	   }
	   
}
