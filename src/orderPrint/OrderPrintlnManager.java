package orderPrint;


import gift.Gift;
import gift.GiftManager;
import gift.GiftService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import order.Order;
import order.OrderManager;
import orderproduct.OrderProduct;
import orderproduct.OrderProductManager;
import orderproduct.OrderProductService;

import user.User;
import utill.TimeUtill;
import database.DB;
 
public class OrderPrintlnManager { 
	 protected static Log logger = LogFactory.getLog(OrderPrintlnManager.class);
	   public static boolean updateOrderStatues(User user,int id ,int oid, int uid , int statues){
			   Connection conn = DB.getConn();
				String sql = "update mdorderupdateprint set statues = ?" +
						" where id = " + id;  
				PreparedStatement pstmt = DB.prepare(conn, sql);
				try { 
					pstmt.setInt(1, statues);
logger.info(pstmt);  
					pstmt.executeUpdate();
					
					OrderPrintln o = OrderPrintlnManager.getOrderStatues(id); 
					  
					if(OrderPrintln.comited == statues && o.getType() == OrderPrintln.salerelease){
						OrderPrintlnManager.delete(id);   
						OrderManager.updateShifang(user,oid,uid,OrderPrintln.salerelease); 
						return true ; 
					}if(OrderPrintln.comited == statues && o.getType() == OrderPrintln.salereleasereturn){
						OrderPrintlnManager.delete(id);   
						OrderManager.updateShifang(user,oid,uid,OrderPrintln.salereleasereturn); 
						return true ; 
					}else if(OrderPrintln.comited == statues && o.getType() == OrderPrintln.salereleasesonghuo){
						OrderPrintlnManager.delete(id);
						OrderManager.updateShifang(user,oid,uid,OrderPrintln.salereleasesonghuo); 
						return true ;
					}else if(OrderPrintln.comited == statues && o.getType() == OrderPrintln.salereleaseanzhuang){
						OrderPrintlnManager.delete(id);
						OrderManager.updateShifang(user,oid,uid,OrderPrintln.salereleaseanzhuang); 
						return true ; 
					}else if(OrderPrintln.comited == statues && o.getType() == OrderPrintln.release){
						OrderPrintlnManager.delete(id);
						OrderManager.updateShifang(user,oid,uid,OrderPrintln.release); 
						return true ;
					} else  if(OrderPrintln.comited == statues && o.getType() == OrderPrintln.returns){
						//OrderPrintlnManager.delete(id);
						OrderPrintlnManager.delete(oid, statues);
						OrderManager.updateShifang(user,oid,uid,OrderPrintln.returns); 
					} else  if(OrderPrintln.comited == statues && o.getType() == OrderPrintln.releasedispatch){
						//OrderPrintlnManager.delete(id);         
						OrderManager.updateShifang(user,oid,uid,OrderPrintln.releasedispatch); 
					} else  if(OrderPrintln.comited == statues && o.getType() == OrderPrintln.modify){
						OrderPrintlnManager.delete(oid, statues);       
						//OrderManager.updateShifang(oid,uid,OrderPrintln.releasedispatch); 
					}else  if(OrderPrintln.comited == statues && o.getType() == OrderPrintln.huanhuo){

						Order oldOrder = OrderManager.getOrderID(user, oid);
						  
						Map<Integer,List<Gift>> gMap = GiftService.getmap();
						Map<Integer,List<OrderProduct>> OrPMap = OrderProductService.getStaticOrderStatuesM();
					    List<OrderProduct> listp = OrPMap.get(oid);
					    for(int i=0;i<listp.size();i++){
					    	OrderProduct or = listp.get(i);
					    	if(or.getStatues() == 0){
					    		or.setSalestatues(1); 
					    	}
					    }
					    
					    List<Gift> listg = gMap.get(Integer.valueOf(oid)); 
						Order order = new Order();
						order.setId(0);
						order.setSaleTime(oldOrder.getSaleTime());
				        order.setOdate(oldOrder.getOdate());
				        order.setPos(oldOrder.getPos());
				     
				        order.setSaleID(oldOrder.getSaleID()); 
				        order.setBranch(oldOrder.getBranch());
				        order.setSailId(oldOrder.getSailId());
				        order.setCheck(oldOrder.getCheck());
				     	order.setUsername(oldOrder.getUsername());
				     	order.setPhone1(oldOrder.getPhone1());
				     	order.setLocate(oldOrder.getLocate());
				        order.setLocateDetail(oldOrder.getLocateDetail()); 
				        order.setRemark(oldOrder.getRemark()+"换货单，务必拉回残机。");
				        order.setOderStatus(20+"");
						order.setOrderproduct(listp);
						order.setOrdergift(listg);  
						order.setSubmitTime(oldOrder.getSubmitTime());
						order.setPrintlnid(TimeUtill.getdatesimple());  
						order.setDeliveryStatues(0);  
						order.setPhoneRemark(oldOrder.getPhoneRemark()); 
						order.setPosremark(oldOrder.getPosremark());
						order.setReckedremark(oldOrder.getReckedremark());
						order.setSailidrecked(oldOrder.getSailidrecked());
						OrderManager.save(user, order);  

					}  
					return true ;
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DB.close(pstmt);
					DB.close(conn);
				} 
		   return false;
	   }

	public static boolean delete(int id) {
		boolean b = false;
		Connection conn = DB.getConn(); 
		String sql = "delete from mdorderupdateprint where id = " + id;
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
    
	public static String deleteByoid(int id) {
 
		String sql = "delete from mdorderupdateprint where orderid = " + id;
	    return sql ;
	}
	
	public static boolean delete(OrderPrintln order) { 
		List<String> sqls = new ArrayList<String>();
		
		boolean b = false;
		Connection conn = DB.getConn(); 
		String sql = ""; 
		if(OrderPrintln.release == order.getType()){ 
			sql = "delete from mdorderupdateprint where orderid = " +order.getOrderid() + " and mdtype = " + order.getType();
			String sql2 =" update mdorderupdateprint set statues = 2  where orderid = " +order.getOrderid() + " and mdtype = "+OrderPrintln.releasemodfy; 
			sqls.add(sql);
			sqls.add(sql2);
			logger.info(sql2);
			//sql = "delete from mdorderupdateprint where orderid = " +order.getOrderid() + " and mdtype in ( " + order.getType()+","+OrderPrintln.releasemodfy+")";
		}else { 
		  sql = "delete from mdorderupdateprint where orderid = " +order.getOrderid() + " and mdtype = " + order.getType();
		} 
		sqls.add(sql);

		Statement stmt = DB.getStatement(conn);
		try { 
			for(int i=0;i<sqls.size();i++){
				DB.executeUpdate(stmt, sqls.get(i));
			}
			
			b = true;
		} finally {
			DB.close(stmt);
			DB.close(conn);
		}
		return b;
	}
	
	public static boolean delete(int oid ,int statues ) { 
		boolean b = false;
		Connection conn = DB.getConn();   
		String sql = "delete from mdorderupdateprint where orderid = " +oid + " and mdtype = " + statues;
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
	
	   public static void save( OrderPrintln order) {
		         delete(order);
				Connection conn = DB.getConn();
				String sql = "insert into  mdorderupdateprint (id, message ,statues , orderid,mdtype ,pGroupId)" +
	                         "  values ( null, ?, ?, ?,?,?)";
				PreparedStatement pstmt = DB.prepare(conn, sql);
			try {
				pstmt.setString(1, order.getMessage());
				pstmt.setInt(2, order.getStatues());
				pstmt.setInt(3, order.getOrderid());
				pstmt.setInt(4, order.getType());
				pstmt.setInt(5, order.getpGroupId());
		logger.info(pstmt);		 
				pstmt.executeUpdate();
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
	   
	   public static  Map<Integer,OrderPrintln>  getOrderStatues(User user){
		    Map<Integer,OrderPrintln> map = new HashMap<Integer,OrderPrintln>();
		    Connection conn = DB.getConn();
			Statement stmt = DB.getStatement(conn);
			String sql = "select * from  mdorderupdateprint ";
			System.out.println(sql);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) { 
					OrderPrintln Order = getOrderStatuesFromRs(rs);
					map.put(Order.getOrderid(), Order);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(stmt);
				DB.close(rs);
				DB.close(conn);
			 }
			System.out.println(map.toString());
			return map;
	 }   
	   
	   
	   public static int getstatues(Map<Integer,Map<Integer,OrderPrintln>> opmap,int type ,int orderid){
		   int statues = -1 ;  
		   if(opmap.get(type) != null){
				OrderPrintln op = opmap.get(type).get(orderid);
				 if(op != null){  
					 statues = op.getStatues() ;
					 } 
				 }  
		   
		   return statues ;
	   }
	    
	   public static OrderPrintln getOrderPrintln(Map<Integer,Map<Integer,OrderPrintln>> opmap,int type ,int orderid){
		   OrderPrintln op = null ; 
		   if(opmap.get(type) != null){
				op = opmap.get(type).get(orderid);
				 
				 }  
		   
		   return op ;
	   }
	   
	   public static  Map<Integer,Map<Integer,OrderPrintln>>  getOrderStatuesMap(User user){
		   Map<Integer,Map<Integer,OrderPrintln>> maps = new HashMap<Integer,Map<Integer,OrderPrintln>>();
		    Connection conn = DB.getConn();
			Statement stmt = DB.getStatement(conn); 
			String 	sql = "select * from  mdorderupdateprint " ;
//logger.info(sql);  
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) { 
					OrderPrintln order = getOrderStatuesFromRs(rs);
					Map<Integer,OrderPrintln> map = maps.get(order.getType());
					if(map == null){
						map = new HashMap<Integer,OrderPrintln>();
						maps.put(order.getType(), map);
					}
					map.put(order.getOrderid(), order);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(stmt);
				DB.close(rs);
				DB.close(conn);
			 }  
			return maps; 
	 }  
	   
	   public static  Map<Integer,OrderPrintln>  getOrderStatues(User user,int type){
		    Map<Integer,OrderPrintln> map = new HashMap<Integer,OrderPrintln>();
		    Connection conn = DB.getConn();
			Statement stmt = DB.getStatement(conn);
			String 	sql = "select * from  mdorderupdateprint where  mdtype =" + type ;
logger.info(sql); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) { 
					OrderPrintln Order = getOrderStatuesFromRs(rs);
					map.put(Order.getOrderid(), Order);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(stmt);
				DB.close(rs);
				DB.close(conn);
			 }
			return map;
	 }  
	   
	   public static OrderPrintln getOrderStatues(User user ,int id,int type){
		   
		        OrderPrintln Order = null;
			    Connection conn = DB.getConn();
				Statement stmt = DB.getStatement(conn);
				String sql = "select * from  mdorderupdateprint where orderid = "  + id+ " and mdtype = "+ type;
logger.info(sql);  
				ResultSet rs = DB.getResultSet(stmt, sql);
				try {
					while (rs.next()) { 
				  Order = getOrderStatuesFromRs(rs);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DB.close(stmt);
					DB.close(rs);
					DB.close(conn);
				 }
				return Order;
		 }
	   
	   public static List<OrderPrintln> getOrderPrintlnbyOrderid(int id){
		   List<OrderPrintln> list = new ArrayList<OrderPrintln>(); 
		    Connection conn = DB.getConn(); 
			Statement stmt = DB.getStatement(conn);
			String sql = "select * from  mdorderupdateprint where orderid = "  + id;
logger.info(sql);  
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) { 
					OrderPrintln  Order = getOrderStatuesFromRs(rs);
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
	   
	   public static OrderPrintln getOrderStatues(int id){
		   
	        OrderPrintln Order = null;
		    Connection conn = DB.getConn();
			Statement stmt = DB.getStatement(conn);
			String sql = "select * from  mdorderupdateprint where  id = "  + id;
logger.info(sql);    
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) { 
			  Order = getOrderStatuesFromRs(rs);
				} 
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(stmt);
				DB.close(rs);
				DB.close(conn);
			 }
			return Order;
	 }
	   
	   public static OrderPrintln getOrderStatuesFromRs(ResultSet rs){
		   OrderPrintln p = null;
			try {
				p = new OrderPrintln();
				p.setId(rs.getInt("id"));
				p.setMessage(rs.getString("message"));
				p.setStatues(rs.getInt("statues"));
				p.setOrderid(rs.getInt("orderid"));
				p.setType(rs.getInt("mdtype")); 
				p.setpGroupId(rs.getInt("pGroupId")); 
			} catch (SQLException e) { 
				e.printStackTrace();
			}
			return p;
	   }
	  
}
