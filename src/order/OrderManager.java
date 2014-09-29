package order;

import gift.GiftManager;
import group.Group; 
import group.GroupManager;
 
import inventory.InventoryBranchManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import branch.Branch;
import branch.BranchManager;

import orderPrint.OrderPrintln;
import orderPrint.OrderPrintlnManager;
import orderproduct.OrderProduct;
import orderproduct.OrderProductManager;
 
import database.DB;
import user.User;
import user.UserManager;
import utill.DBUtill;
import utill.TimeUtill;

public class OrderManager {
	 protected static Log logger = LogFactory.getLog(OrderManager.class);
	 public static SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     
	 // 跟新打印方法 
	public static void updatePrint(int statues,int id) {
		Connection conn = DB.getConn();
		//insert into  mdgroup( id ,groupname, detail,statues, permissions, products) VALUES (null,?,?,?,?,?)";
		String sql = "update mdorder set printSatues = ? where id = " + id;
		PreparedStatement pstmt = DB.prepare(conn, sql);
		try {
			pstmt.setInt(1,statues);
			System.out.println(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { 
			DB.close(pstmt);
			DB.close(conn);
		}
	}  
	  
	public static int updatePrint2(int statues,String id,String pid) {
		int flag = -1 ; 
		Connection conn = DB.getConn(); 
		//insert into  mdgroup( id ,groupname, detail,statues, permissions, products) VALUES (null,?,?,?,?,?)";
		String sql = "update mdorder set printSatues= ? , printlnid = "+pid+" where id = " + id;
		PreparedStatement pstmt = DB.prepare(conn, sql);
		try {  
			pstmt.setInt(1,statues);  
			logger.info(pstmt);    
			flag = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { 
			DB.close(pstmt);
			DB.close(conn);
		} 
		return flag ;
	}  
	
	public static int updateMessage(String phone1,String andate,String locations,String POS,String sailId,String check,String oid,String remark,String saledate) {
		int flag = -1 ;  
		Connection conn = DB.getConn(); 
		//insert into  mdgroup( id ,groupname, detail,statues, permissions, products) VALUES (null,?,?,?,?,?)";
		String sql = "update mdorder set phone1= ? , andate = ? , locateDetail = ?, pos = ?, sailId = ? ,checked =? , remark = ? ,saledate = ? where id = " + oid;
		PreparedStatement pstmt = DB.prepare(conn, sql);
		try {   
			pstmt.setString(1,phone1);   
			pstmt.setString(2,andate);   
			pstmt.setString(3,locations);    
			pstmt.setString(4,POS);     
			pstmt.setString(5,sailId);    
			pstmt.setString(6,check);   
			pstmt.setString(7,remark);
			pstmt.setString(8,saledate);
logger.info(pstmt);    
			flag = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { 
			DB.close(pstmt);
			DB.close(conn);
		} 
		return flag ;
	}  
	
	public static int updateMessage(String phone1,String andate,String locations,String oid,String remark) {
		int flag = -1 ; 
		Connection conn = DB.getConn(); 
		//insert into  mdgroup( id ,groupname, detail,statues, permissions, products) VALUES (null,?,?,?,?,?)";
		String sql = "update mdorder set phone1= ? , andate = ? , locateDetail = ? , remark = ? where id = " + oid;
		PreparedStatement pstmt = DB.prepare(conn, sql);
		try {   
			pstmt.setString(1,phone1);    
			pstmt.setString(2,andate);   
			pstmt.setString(3,locations);
			pstmt.setString(4,remark);  
			
			logger.info(pstmt);    
			flag = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { 
			DB.close(pstmt);
			DB.close(conn);
		} 
		return flag ;
	}  
	
	
	public static String getDeliveryStatues(int statues){
		String str = "";
	
		// 0 表示未送货  1 表示正在送  2 送货成功
		 if(0 == statues){
		
			 str = "未发货";
		
          }else if(1 == statues){

		
        	  str = "已送货";
		
          }else if(2 == statues){
		
        	  str = "已安装";
		
          }else if(3 == statues || 4 == statues || 5 == statues || 11 == statues || 13 == statues || 12 == statues){
        	  str = "已退货";
		
          }else if( 8 == statues){ 
		
        	  str = "已自提 ";
		
          }else if(9 == statues){
		
        	  str = "只安装(门店提货)";
		
		
            }else if(10 == statues){
        		
        		str = "只安装(顾客已提) ";
        		 
            }
        		
		
		
		return str ;
		
		
	}
	
	//by wilsonlee
	public static int updateStatues(String method ,int statues,String id) {
		return updateStatues(new User(),method,statues,id);
	}
	 // 确认厂送票已回
		public static int updateStatues(User user ,String method ,int statues,String id) {
			List<String> listsql = new ArrayList<String>(); 
			String[] listt = id.split(",");
			if(listt.length <2){  
				for(int j=0;j<listt.length;j++){
					String idd = listt[j]; 
				
				
				List<OrderPrintln> list = OrderPrintlnManager.getOrderPrintlnbyOrderid(Integer.valueOf(idd));
				for(int i=0;i<list.size();i++){ 
					OrderPrintln o = list.get(i);  
					if(o.getType() == OrderPrintln.modify && o.getStatues() != 4 && !"tuihuo".equals(method) && !"print4".equals(method) && !"orderCome".equals(method) && !"orderGo".equals(method)&& !"orderCharge".equals(method) && !"orderover".equals(method)){
						//logger.info(1); 
						return OrderPrintln.modify;
					}else if(o.getType() == OrderPrintln.returns && o.getStatues() != 4 && !"tuihuo".equals(method) && !"print4".equals(method) && !"orderCome".equals(method) && !"orderGo".equals(method)&& !"orderCharge".equals(method) && !"orderover".equals(method)){
						//logger.info(1);   
						return 20 ;    
					}else if(o.getType() == OrderPrintln.unmodify && !"print4".equals(method) && !"orderCome".equals(method) && !"orderGo".equals(method)&& !"orderCharge".equals(method)  && !"orderover".equals(method)){
						//logger.info(1);   
						return OrderPrintln.unmodify ;  
					} 
				}
				 
				}
			}
			//logger.info(1);
			
			int flag = -1 ;
			Date date1 = new Date();
			String time= df2.format(date1); 
			if(id.startsWith(",")){
				id = id.substring(1); 
			}
			String ids = "(" + id + ")";
			String sql = ""; 
			if("orderCome".equals(method)){
				sql = "update mdorder set statues1 = "+statues+" where id in " + ids;
			}else if("orderDingma".equals(method)){
				sql = "update mdorder set statuesdingma = "+statues+" where id in " + ids;
			}else if("orderGo".equals(method)){ 
				sql = "update mdorder set statues2 = "+statues+" where id in " + ids;
			}else if("orderCharge".equals(method)){
				sql = "update mdorder set statues3 = "+statues+" where id in " + ids;
			} else if("orderover".equals(method)){ 
				sql = "update mdorder set statues4 = "+statues+" where id in " + ids;
			} else if("songhuo".equals(method)){
				
				if(2 == statues){
					sql = "update mdorder set deliveryStatues = "+statues+" , deliverytype = 1 , sendTime = '"+time+"' , installTime = '"+time+"' , installid = mdorder.sendId   where id in " + ids;
				}else if(1 == statues) {  
					sql = "update mdorder set deliveryStatues = "+statues+"  , deliverytype = 2 ,  printSatuesp = 0 , sendTime = '"+time+"'  where id in " + ids;
				}else if( 4 == statues ||  9 == statues || 10 == statues){
					statues = 2 ;    
					sql = "update mdorder set deliveryStatues = "+statues+"  , deliverytype = 2 , installTime = '"+time+"'  where id in " + ids;
				}  
			} else if("peidan".equals(method)){     
			    sql = "update mdorder set dealSendid = "+statues+" , printSatues = 1 , dealsendTime = '"+TimeUtill.gettime()+"'  where id in " + ids;
			    List<String> lists = InventoryBranchManager.chage(user, method, statues, id);
			    listsql.addAll(lists);   
			}else if("tuihuo".equals(method)){     
			    sql = "update mdorder set returnstatues = "+statues+" , returntime = '"+time+"'  where id in " + ids;
			   // List<String> lists = InventoryBranchManager.chage(user, method, statues, id);
			    //listsql.addAll(lists);   

			} else if("print".equals(method)){ //  
				sql = "update mdorder set printSatues = "+statues+" where id in " + ids;
			} else if("printdingma".equals(method)){    
				sql = "update mdorder set printdingma = "+statues+" where id in " + ids;
			}else if("print2".equals(method)){  
				sql = "update mdorder set printSatuesp= "+statues+" where id in " + ids;
			}else if("print3".equals(method)){  
				sql = "update mdorder set returnprintstatues = "+statues+" where id in " + ids;
			}else if("print4".equals(method)){   
				sql = "update mdorder set returnwenyuan = "+statues+" where id in " + ids;
			}   else if("statuescallback".equals(method)){ 
				sql = "update mdorder set statuescallback = "+statues+" where id in " + ids;
			} else if("statuespaigong".equals(method)){ 
				sql = "update mdorder set statuespaigong = "+statues+" where id in " + ids;
			} else if("statuesinstall".equals(method)){  
				sql = "update mdorder set statuesinstall = "+statues+" where id in " + ids;
			} else if("statuesinstalled".equals(method)){
				statues = 2 ;
				sql = "update mdorder set statuesinstall = "+statues+" where id in " + ids;
			}else if("statuescallback".equals(method)){ 
				sql = "update mdorder set statuescallback = "+statues+" where id in " + ids;
			}else if("wenyuancallback".equals(method)){ 
				sql = "update mdorder set wenyuancallback = "+statues+" where id in " + ids;
			}  
			listsql.add(sql); 
			logger.info(listsql.toString()); 
            // statuesinstalled  // statuescallback
			if(DBUtill.sava(listsql)){ 
				flag = 1 ; 
			};
			return flag ;
		}  
			
	public static void updateShifang(User user,int id,int uid,int type) {
		List<String> listsql = new ArrayList<String>();
		String sql = "";
		if(type == OrderPrintln.release){               
			sql = "update mdorder set dealSendid = 0  , printSatues = 0  where id = " + id;
			  List<String> lists = InventoryBranchManager.chage(user,"shifang", uid, id+"");    
		      listsql.addAll(lists);    
		}else if(type == OrderPrintln.salerelease){        
			sql = "update mdorder set sendId = 0, printSatuesp = 0  where id = " + id;
			  List<String> lists = InventoryBranchManager.chage(user,"salereleasesonghuo", user.getId(), id+"");    
		      listsql.addAll(lists); 
		}else if(type == OrderPrintln.salereleasesonghuo){          
			//sql = "update mdorder set deliveryStatues = 3   where id = " + id;
			sql = "update mdorder set sendId = 0, printSatuesp = 0  where id = " + id; 
			  //List<String> lists = InventoryBranchManager.chage(user,"salereleasesonghuo", uid, id+"");    
		      //listsql.addAll(lists); 
		     
		}else if(type == OrderPrintln.salereleaseanzhuang){    
			sql = "update mdorder set installid = 0,printSatuesp = 0  where id = " + id; 
		}else if(type == OrderPrintln.modify){ 
			return ;     
		}else if(type == OrderPrintln.returns){  // releasedispatch    
			sql = "update mdorder set deliveryStatues  = (mdorder.deliveryStatues + 3)  where id = " + id;
			List<String> lists = InventoryBranchManager.chage(user,"returns", uid, id+"");     
		    listsql.addAll(lists); 
			
			
		} else if(type == OrderPrintln.releasedispatch ){  // releasedispatch  
			sql = "update mdorder set returnstatues = 2   where id = " + id; 
		}            
		//insert into  mdgroup( id ,groupname, detail,statues, permissions, products) VALUES (null,?,?,?,?,?)";
         logger.info(sql);	
         listsql.add(sql);  
//insert into  mdgroup( id ,groupname, detail,statues, permissions, products) VALUES (null,?,?,?,?,?)";
     DBUtill.sava(listsql);       

	}  
	
	// 第一次配单 
	public static int updatePeidan(int statues,int id) {
		int count = 0 ;
		Connection conn = DB.getConn();
		//insert into  mdgroup( id ,groupname, detail,statues, permissions, products) VALUES (null,?,?,?,?,?)";
		String sql = "update mdorder set dealSendid = ? where id = " + id ;
		
		PreparedStatement pstmt = DB.prepare(conn, sql);    
		try {  
			pstmt.setInt(1,statues);
			count = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return count ;
	}
	
	
	public static int updateimagerUrl(String pritnlnd, String url) {
		int count = 0 ;
		Connection conn = DB.getConn();
		//insert into  mdgroup( id ,groupname, detail,statues, permissions, products) VALUES (null,?,?,?,?,?)";
		String sql = "update mdorder set imagerUrl = ?  where printlnid = '" + pritnlnd +"'";
		
		PreparedStatement pstmt = DB.prepare(conn, sql);    
		try {  
			pstmt.setString(1,url);           
			count = pstmt.executeUpdate(); 
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return count ;
	}
	
	// 第二次配单 
		public static int updatePeisong(User user,int statues,int id,int type) {
			List<OrderPrintln> list = OrderPrintlnManager.getOrderPrintlnbyOrderid(id);
			List<String> listsql = new ArrayList<String>(); 
			for(int i=0;i<list.size();i++){
				OrderPrintln o = list.get(i); 
				if(o.getType() == OrderPrintln.modify && o.getStatues() != 4 && Order.orderreturn != type){
					return OrderPrintln.modify;
				}else if(o.getType() == OrderPrintln.returns && o.getStatues() != 4 && Order.orderreturn != type){
					return 20;   
				} 
			}		
			
			int count = -1 ;
			  
			String sql = ""; 
			if(Order.orderpeisong == type || Order.ordersong == type){ 
				sql = "update mdorder set sendId = "+statues+"  , printSatuesp= 1  where id = " + id ;
				  List<String> lists = InventoryBranchManager.chage(user,type+"", statues, id+""); 
			      listsql.addAll(lists);     
			}else if(Order.orderinstall == type){ 
				sql = "update mdorder set installid = "+statues+" , printSatuesp= 1  where id = " + id ;
			} else if(Order.orderreturn == type){  
				sql = "update mdorder set returnid = "+statues+" , returnprintstatues = 1  where id = " + id ;
				List<String> lists = InventoryBranchManager.chage(user, type+"", statues, id+"");
			    listsql.addAll(lists);   
			
			
			}   
			listsql.add(sql); 
			//insert into  mdgroup( id ,groupname, detail,statues, permissions, products) VALUES (null,?,?,?,?,?)";
			if(DBUtill.sava(listsql)){ 
				count = 1 ; 
			}; 
			
			return count ;
		}
		
	public static boolean getName(String method,String c,String branch){
    	boolean flag = false ;
		Connection conn = DB.getConn(); 
		String sql = "";
		if("phone1".equals(method)){ 
			sql = "select * from mdorder where "+method+"  = '"+ c+ "'  and orderbranch = '"+branch+"' and  (TIMESTAMPDIFF(DAY,saledate,now()) <= 5)";  
		}else {
			sql = "select * from mdorder where "+method+"  = '"+ c+ "'  and orderbranch = '"+branch+"' ";  
		}
		
logger.info(sql);    
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
	
	// 判断是不是顶码
	public static boolean Check(int oid){
    	boolean flag = false ;
		Connection conn = DB.getConn(); 
		
		
		String	sql = "select * from mdorderproduct where statues = 1 and orderid = "+ oid;  
		
logger.info(sql);    
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
	
	// 抢单    配单   等
	public static int updateSendstat(User user, String oid) {
		int count = 0 ;
		Connection conn = DB.getConn();
		String str = "(";
		String[] slist = oid.split(",");
		for(int i=0;i<slist.length;i++){
			str += slist[i] +",";
		}
		str = str.substring(0,str.length()-1)+")";
	
		String sql = "update mdorder set sendId = " + user.getId() + "  where  id  in " + str ; 
		PreparedStatement pstmt = DB.prepare(conn, sql);
		try {
logger.info(sql);   
			count = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return count ;
	}
	
	// 释放状态
public static void updateSendstad(User user, String oid) {
		 
		Connection conn = DB.getConn();
		String str = "(";
		String[] slist = oid.split(",");
		for(int i=0;i<slist.length;i++){
			str += slist[i] +",";
		}
		str = str.substring(0,str.length()-1)+")";
		System.out.println(str);
		//insert into  mdgroup( id ,groupname, detail,statues, permissions, products) VALUES (null,?,?,?,?,?)";
		String sql = "update mdorder set sendId = 10  where  id  in " + str;
		PreparedStatement pstmt = DB.prepare(conn, sql);
		try {
	System.out.println(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
	}
public static void updateSendstat(int statues,int sid, int oid) {
		
		Connection conn = DB.getConn();
		
		
		//insert into  mdgroup( id ,groupname, detail,statues, permissions, products) VALUES (null,?,?,?,?,?)";
		String sql = "update mdorder set deliveryStatues = ? where id = " + oid;
		PreparedStatement pstmt = DB.prepare(conn, sql);
		try {
			pstmt.setInt(1,statues);
			System.out.println(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
	}
   public boolean updateOrder(User user, Order order){
	   if(user.getUsertype() == 2 && user.getId() == order.getSaleID() ){
		   Connection conn = DB.getConn();
			String sql = "update mdorder set productCategory = ?, productType = ? , locate = ?" +
					", locateDetail = ?, date = ?, time = ?, saleID = ?" +
					", saleTime = ?, printSatues = ?, mail = ?,oderStatus = ?";
			PreparedStatement pstmt = DB.prepare(conn, sql);
			try {
				//pstmt.setString(1, order.getProductCategory());
				pstmt.setString(2, order.getProductType());
				pstmt.setString(3, order.getLocate());
				pstmt.setString(4, order.getLocateDetail());
				pstmt.setString(5,order.getDate());
				pstmt.setString(6, order.getTime());
				pstmt.setInt(7 ,order.getSaleID());
				pstmt.setString(8, order.getSaleTime());
				pstmt.setInt(9, order.getPrintSatues());
				pstmt.setString(10, order.getMail());
				//pstmt.setInt(11, order.getOderStatus());
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
   
   // select top 1 * from table order by id desc
   public static int getMaxid(){
	    int id = 1 ;
	    Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn);
		String  sql = "select max(id)+1 as id from mdorder" ;
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				id = rs.getInt("id");
				logger.info(id);
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
   
   public static Order getMaxOrder(){
	    int id = 1 ;
	    Order order = null;
	    Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn);
		//  select top 1 * from table order by id desc
		// select * from table where id in (select max(id) from table)
		String  sql = "select * from mdorder where id in (select max(id) from mdorder)" ;
		ResultSet rs = DB.getResultSet(stmt, sql);
		try { 
			while (rs.next()) {
				order = OrderManager.gerOrderFromRs(rs);
				logger.info(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(stmt);
			DB.close(rs);
			DB.close(conn);
		 }
		return order; 

  }
   
   
   public static boolean getid(int id){
	   boolean flag = false ;
	   if(0== id ){
		   return flag ;
	   }
	   
	    Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn);
		String  sql = "select id from mdorder where id = " + id ;
		logger.info(sql);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				flag = true ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(stmt);
			DB.close(rs);
			DB.close(conn);
		 }
		return flag;
   }
    
   
  
   public static boolean save(User user,Order order) {
	     boolean flag  = false ; 
	     List<String> sqls = new ArrayList<String>(); 
	      
		 //boolean isflag = OrderManager.getid(order.getId());

		 int maxid; 
		 int daymark; 
		 int dayID ;
		 String daymarkk = "" ;
		 Order oldorder = OrderManager.getOrderID(user,order.getId());
		 
		 if(oldorder != null){ 
			 maxid = oldorder.getId();   
			 daymark =oldorder.getDayremark(); 
			 dayID = oldorder.getDayID(); 
			 OrderPrintln oor = OrderPrintlnManager.getOrderStatues(user, oldorder.getId(),0);
			 if(oldorder.getPrintSatues() == 1){
				 if(oor != null && oor.getStatues() != 2){
					 return false ;
				 } 
			 } 
			
			 OrderManager.update(maxid); 
			 
			 
			 
		 }else {  
			 oldorder = OrderManager.getMaxOrder();
			 if(oldorder == null){
				 maxid = 1 ;
				 daymark = 1;   
				 dayID = TimeUtill.getdate(); 
			 }else {  
				 maxid = oldorder.getId()+1;   
				 daymark =oldorder.getDayremark()+1;
				 if(TimeUtill.isWee_hours(oldorder.getDayID())){
					 daymark = 1 ; 
				 }    
				 dayID = TimeUtill.getdate();   
			 }
			 
		 }
		 
	/*	 if(isflag){   
			 maxid = order.getId(); 
			 daymark = order.getDayremark();
			 OrderManager.delete(maxid);
		 }else {   
			 maxid = OrderManager.getMaxid();
		 }  */
		 if(daymark<10){  
			 daymarkk = "00"+daymark;
		 }else if(9<daymark || daymark <100){
			 daymarkk = "0"+daymark; 
		 }
		 
		 
		if(maxid == 0){ 
			maxid = 1 ;
		}    
	
		
		List<String> sqlp = OrderProductManager.save(maxid, order);
	    List<String> sqlg = GiftManager.save(maxid, order);
		 
	     sqls.addAll(sqlp);
	     sqls.addAll(sqlg);
	     
	    order.setSaleID(user.getId());
	    String sql = "insert into  mdorder ( id ,andate , saledate ,pos, username, locates" +
				", locateDetail, saleID , printSatues ,oderStatus,sailId,checked,phone1,phone2,remark,"+
	    		"deliveryStatues,orderbranch,sendId,statues1,statues2,statues3,dealSendid,submittime,printlnid,dayremark,dayID,phoneRemark,sailIdremark,checkedremark,posRemark) values "+  
				"( "+maxid+", '"+order.getOdate()+"', '"+order.getSaleTime()+"', '"+order.getPos()+"', '"+order.getUsername()+"', '" 
	    		+order.getLocate()+"', '"+order.getLocateDetail()+"',"+order.getSaleID()+", "+order.getPrintSatues()    
	    		+", "+user.getId()+", '"+order.getSailId()+"', '"+order.getCheck()+"', '"+order.getPhone1()+"','"+order.getPhone2()+"','"+order.getRemark()+"',"+order.getDeliveryStatues()+",'"+user.getBranch()+"',0,0,0,0,"+order.getDealsendId()+",'"+order.getSubmitTime()+"','"+order.getPrintlnid()+"-"+daymarkk+"',"+daymark+","+dayID+","+order.getPhoneRemark()+","+order.getSailidrecked()+","+order.getReckedremark()+","+order.getPosremark()+")";   
	    sqls.add(sql);
	          
	    Connection conn = DB.getConn();   
		  
	    Statement sm = null;  
        try {    
            // 事务开始  
            logger.info("事物处理开始") ;
            conn.setAutoCommit(false);   // 设置连接不自动提交，即用该连接进行的操作都不更新到数据库  
            sm = conn.createStatement(); // 创建Statement对象  
             Object[] strsqls = sqls.toArray();
             logger.info(strsqls.toString());
            //依次执行传入的SQL语句      
            for (int i = 0; i < strsqls.length; i++) {  
                sm.execute((String)strsqls[i]);// 执行添加事物的语句  
            }  
            logger.info("提交事务处理！");  
               
            conn.commit();   // 提交给数据库处理   
               
            logger.info("事务处理结束！");  
            // 事务结束   
             flag = true ;   
        //捕获执行SQL语句组中的异常      
        } catch (SQLException e) {  
            try {   
                logger.info("事务执行失败，进行回滚！\n",e);  
                conn.rollback(); // 若前面某条语句出现异常时，进行回滚，取消前面执行的所有操作  
            } catch (SQLException e1) {  
                logger.info(e);
            }  
        } finally {   
            try { 
				sm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}  
        }  
        
		return flag ; 

	}
    
   public static int getCount(String str){
	   
	    Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn);
		
		String sql = "select count(*) as cc from  mdorder ";
		
		ResultSet rs = DB.getResultSet(stmt, sql);

		int count = 0;
		
		try { 
			while (rs.next()) {
				count = rs.getInt("cc");
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
   
   public static List<Order> getOrderlist(User user ,int type){  
	   
	   
	   List<Order> Orders = new ArrayList<Order>();
	   try{
	   //  2  表示送货员  
	   boolean flag = UserManager.checkPermissions(user, type);
	  // boolean f = UserManager.checkPermissions(user, Group.Manger); 
	    
	   String sql = "";  
	   logger.info(type);
	   if(user.getUsertype() == 1    ){  
		   sql = "select * from  mdorder where 1 =1 order by id " ;
	   }else if(flag && Group.send == type){      
		   sql = "select * from  mdorder where  ( sendId = "+user.getId() + " and deliveryStatues in (0,9,10)   and printSatuesp = 1  or  installid = "+user.getId() + " and deliveryStatues in (1,10,9)  and printSatuesp = 1  )  order by id  desc";
	   }else if(flag && Group.tuihuo == type){  
		   sql = "select * from  mdorder where  ( returnid = "+user.getId() + " and returnstatues =0  and returnprintstatues = 1  )  order by id  desc";
	   }else if(flag && Group.sale == type){  
		   sql = "select * from  mdorder where  saleID = '"+ user.getId() +"' and  deliveryStatues= 0 order by id"; 
	   }   
	   else if(flag && Group.dealSend == type){    
		   sql = "select * from  mdorder  where  sendId = "+ user.getId() +"  and printSatues = 1  or (mdorder.id in (select orderid from mdorderupdateprint where mdtype = 3 and pGroupId = "+ user.getUsertype()+ " ) and sendId != 0) order by id" ;
	   }else if(Group.sencondDealsend == type){   
		   sql = "select * from  mdorder where  dealSendid = "+user.getId()+"  and deliveryStatues= 0  and printSatues = 1 and sendId = 0  or (mdorder.id in (select orderid from mdorderupdateprint where mdtype = 3 and pGroupId = "+ user.getUsertype()+ " ))  order by id" ;
	   }      
	   if("".equals(sql)){
		   return Orders;
	   }
	    
logger.info(sql);
		    Connection conn = DB.getConn();
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try { 
				while (rs.next()) {
					Order p = gerOrderFromRs(rs);
					Orders.add(p);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(stmt);
				DB.close(rs);
				DB.close(conn);
			 }
	   }catch(Exception e){
		   logger.info(e);
	   }
    logger.info(Orders.size());
			return Orders;
	 }
   
public static List<Order> getOrderlistl(User user ,int type){  
	   
	   List<Order> Orders = new ArrayList<Order>();
	   try{
	   //  2  表示送货员  
	    List<Group> listg = GroupManager.getInstance().getListGroupFromPemission(type);
	    
		boolean flag = false ;
		for(int i=0;i<listg.size();i++){
			Group g = listg.get(i);
			if(user.getUsertype() == g.getId()){
				flag = true ;
			}
		}  

	   String sql = "";  
	   if(user.getUsertype() == 1  && Group.sale != type  && Group.send != type && Group.dealSend != type && Group.sencondDealsend != type){
		   sql = "select * from  mdorder where 1 =1 " ;
	   }else if(flag && Group.send == type){
		   sql = "select * from  mdorder where sendId = "+user.getId() + " and deliveryStatues = 0  and printSatues = 1";
	   }else if(flag && Group.sale == type){ 
		   sql = "select * from  mdorder where  saleID = '"+ user.getId() +"' and  deliveryStatues= 0 order by id"; 
	   }      
	   else if(flag && Group.dealSend == type){    
		   sql = "select * from  mdorder  where  sendId = "+ user.getId() +"  and printSatues = 1  or (mdorder.id in (select orderid from mdorderupdateprint where mdtype = 3 and pGroupId = "+ user.getUsertype()+ " ) and sendId != 0) " ;
	   }else if(Group.sencondDealsend == type){    
		   sql = "select * from  mdorder where  dealSendid = "+user.getId()+" and  deliveryStatues != 3   or (mdorder.id in (select orderid from mdorderupdateprint where mdtype = 3 and pGroupId = "+ user.getUsertype()+ " )) " ;
	   }      
	   if("".equals(sql)){
		   return Orders;
	   }
	    
logger.info(sql);
		    Connection conn = DB.getConn();
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try { 
				while (rs.next()) {
					Order p = gerOrderFromRs(rs);
					Orders.add(p);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(stmt);
				DB.close(rs);
				DB.close(conn);
			 }
	   }catch(Exception e){
		   logger.info(e);
	   }
    logger.info(Orders.size());
			return Orders;
	 }
 
   public static List<Order> getOrderlist(User user ,int type,String str,String sort){
	   //  2  表示送货员   
	     // boolean f = UserManager.checkPermissions(user, Group.Manger); 
		   
		  boolean flag = UserManager.checkPermissions(user, type);
			    
		  List<Order> Orders = new ArrayList<Order>();
		  String sql = "";     
			  if(user.getUsertype() == 1){
				   sql = "select * from  mdorder where 1 =1 " + str  + " order by id desc ";
			   }else if(flag && Group.send == type){  
				   if(UserManager.checkPermissions(user, Group.serchBranch)){ 
					   sql = "select * from  mdorder where orderbranch in (select branch from mduser where id = "+user.getId()+" ) " + str + " order by id desc " ;
				   }else { 
					   sql = "select * from  mdorder where sendId = "+user.getId() + str + " order by id desc " ;
				   }
				         
			   }else if(flag && Group.sale == type){  
				   sql = "select * from  mdorder where  saleID = '"+ user.getId() +"' "+ str + " order by id desc ";
			   }    
			   else if(flag && Group.dealSend == type){
				   sql = "select * from  mdorder  where  sendId = "+ user.getId() +"  and printSatues = 1  or (mdorder.id in (select orderid from mdorderupdateprint where mdtype = 3 and pGroupId = "+ user.getUsertype()+ " ) and sendId != 0)  "  + " order by id desc ";
				   //sql = "select * from  mdorder where sendId = "+user.getId()  +str+ "   or (mdorder.id in (select orderid from mdorderupdateprint where mdtype = 3) and sendId != 0)"  ;
			   }else if(Group.sencondDealsend == type){
				   if(str == "" || "".equals(str)){   
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+"  and deliveryStatues= 0  and printSatues = 1 and printSatuesp = 0  and sendId = 0  or (mdorder.id in (select orderid from mdorderupdateprint where mdtype = 3 and pGroupId = "+ user.getUsertype()+ " )) "  + " order by id desc "; 
				   }else {      
					   sql = "select * from  mdorder where  deliveryStatues != 3 and  dealSendid = "+user.getId()+" "+ str + "   or (mdorder.id in (select orderid from mdorderupdateprint where mdtype = 3 and pGroupId = "+ user.getUsertype()+ " )) " + " order by id desc " ;
				   }  
			   }   
 
	   if("".equals(sql)){
		   return Orders;
	   }
logger.info(sql);
		    Connection conn = DB.getConn();
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try { 
				while (rs.next()) {
					Order p = gerOrderFromRs(rs);
					Orders.add(p);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(stmt);
				DB.close(rs);
				DB.close(conn);
			 }
logger.info(Orders.size()); 
			return Orders;
	 }
    
   public static List<Order> getOrderlistl(User user ,int type,String str,String sort){
	   //  2  表示送货员  
	   boolean f = UserManager.checkPermissions(user, Group.Manger); 
		  
		  boolean flag = UserManager.checkPermissions(user, type);
			   
		  List<Order> Orders = new ArrayList<Order>();
		  String sql = "";   
		  if(f){      
			  sql = "select * from  mdorder  where dealSendid = "+user.getId()+"  or (mdorder.id in (select orderid from mdorderupdateprint where mdtype = 3  ))  order by "+sort+" desc ";
		  }else {
			  if(user.getUsertype() == 1  && Group.sale != type  && Group.send != type && Group.dealSend != type && Group.sencondDealsend != type){
				   sql = "select * from  mdorder where 1 =1 " + str ; 
			   }else if(flag && Group.send == type){  
				   sql = "select * from  mdorder where sendId = "+user.getId() + str ;
			   }else if(flag && Group.sale == type){ 
				   sql = "select * from  mdorder where  saleID = '"+ user.getId() +"' "+ str + "and  deliveryStatues= 0 order by saledate";
			   }  
			   else if(flag && Group.dealSend == type){  
				   sql = "select * from  mdorder where sendId = "+user.getId()  +str+ "   or (mdorder.id in (select orderid from mdorderupdateprint where mdtype = 3) and sendId != 0)"  ;
			   }else if(Group.sencondDealsend == type){   
				   sql = "select * from  mdorder where  dealSendid = "+user.getId()+"   or (mdorder.id in (select orderid from mdorderupdateprint where mdtype = 3 and pGroupId = "+ user.getUsertype()+ " )) " ;
			   }  
		  } 
		     
	   if("".equals(sql)){
		   return Orders;
	   }   
logger.info(sql);
		    Connection conn = DB.getConn();
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try { 
				while (rs.next()) {
					Order p = gerOrderFromRs(rs);
					Orders.add(p);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(stmt);
				DB.close(rs);
				DB.close(conn);
			 }
logger.info(Orders.size()); 
			return Orders;
	 }
     
  //   
   
   
   
    public static List<Order> getOrderlist(User user ,int type,int statues ,int num,int page,String sort,String search){
	   
	  boolean f = UserManager.checkPermissions(user, Group.Manger);  
	   
	  boolean flag = UserManager.checkPermissions(user, type);
		   
	  List<Order> Orders = new ArrayList<Order>();
	  
	  String sql = "";    
	  
	  logger.info(f); 
	  if(f){  
		  if(Group.dealSend == type){ 
			  if(Order.orderDispatching == statues){    
				 // sql = "select * from  mdorder  where  (dealSendid = 0   and sendId = 0 and printSatues = 0  and deliveryStatues not in (3,4,5)  and  ( mdorder.id in (select orderid from mdorderproduct where salestatues in (0,1,2,3)))  or id in (select orderid from mdorderupdateprint where statues = 0 and mdtype in (0 ,1,2,4) ))  "+search+"  order by  "+sort+"  limit " + ((page-1)*num)+","+ page*num ;  
				  sql = "select * from  mdorder  where  (dealSendid = 0   and sendId = 0 and printSatues = 0  and deliveryStatues not in (3,4,5) or id in (select orderid from mdorderupdateprint where statues = 0 and mdtype in (0 ,1,2,4) ))  "+search+"  order by  "+sort+"  limit " + ((page-1)*num)+","+ page*num ;  
			  }else if(Order.neworder == statues){
				  sql = "select * from  mdorder  where  dealSendid = 0   and sendId = 0 and printSatues = 0  and deliveryStatues != 3  and mdorder.id in (select orderid from mdorderproduct where salestatues in (0,1,2,3))   "+search+"  order by "+sort+" ";   
			  }else if(Order.motify == statues){
				  sql = "select * from  mdorder  where  id in (select orderid from mdorderupdateprint where statues = 0 and mdtype = 0 )   "+search+"  order by "+sort;   
			  }else if(Order.returns == statues){ 
				  sql = "select * from  mdorder  where  id in (select orderid from mdorderupdateprint where statues = 0 and mdtype = 1 )   "+search+"  order by "+sort;   
			  }else if(Order.release == statues){
				  sql = "select * from  mdorder  where  id in (select orderid from mdorderupdateprint where statues = 0 and mdtype = 2  )   "+search+"  order by "+sort;   
			  }else if(Order.orderPrint == statues){   
				  sql = "select * from  mdorder  where  dealSendid != 0   and sendId = 0 and printSatues = 0  and deliveryStatues != 3    "+search+"  order by "+sort+"  limit " + ((page-1)*num)+","+ page*num ;  
			  }else if(Order.serach == statues){ 
				  sql = "select * from  mdorder  where 1 =1 "+search+" order by "+sort+"   limit " + ((page-1)*num)+","+ page*num ;
			  }else if(Order.charge == statues){ 
				  sql = "select * from  mdorder  where statues1 = 1 and statues2 = 1 and statues3 = 0  "+search+" order by "+sort+"   limit " + ((page-1)*num)+","+ page*num ;
			  }else if(Order.callback == statues){ 
				   sql = "select * from  mdorder where  deliveryStatues in (2)  and wenyuancallback = 0 "+search+" order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; ;
			   }else if(Order.come == statues){
				  sql = "select * from  mdorder  where  statues1 = 0  "+search+" order by "+sort+"  limit " + ((page-1)*num)+","+ page*num ;
			  }else if(Order.go == statues){ 
				  sql = "select * from  mdorder  where  statues1 = 1 and statues2 = 0  "+search+" order by "+sort+"  limit " + ((page-1)*num)+","+ page*num ;
			  }else if(Order.over == statues){  
				  sql = "select * from  mdorder  where dealSendid != 0   and printSatues = 1  and deliveryStatues not in (0,3)  and statues4 = 0  "+search+"  order by "+sort+"   limit " + ((page-1)*num)+","+ page*num ;
			  }else if(Order.dingma == statues){ 
				  sql = "select * from  mdorder  where  id  in (select orderid from mdorderproduct where statues = 1 ) and statuesdingma = 0  "+search+"  order by "+sort+"   limit " + ((page-1)*num)+","+ page*num ;
			  }else if(Order.deliveryStatuesTuihuo == statues){ 
				   sql = "select * from  mdorder where  deliveryStatues in (3,4,5,11,12,13)  "+search+"  order by  "+sort+"  limit " + ((page-1)*num)+","+ page*num; ; ;
			   }         
		  }else if(Group.sencondDealsend == type){ 
				   if(Order.orderDispatching == statues){  
					   sql = "select * from  mdorder where  ( dealSendid != 0   and printSatues = 1   and  printSatuesp = 0 and sendId = 0  and  deliveryStatues in (0,9)  and mdorder.id not in (select orderid from mdorderupdateprint where statues = 2 and mdtype = 6 )  or (mdorder.id in (select orderid from mdorderupdateprint where mdtype in (3,6,7) and pGroupId = "+ user.getUsertype()+ " and statues = 0 )) ) "+search+"  order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; 
				   }else if(Order.dispatch == statues){    
					   sql = "select * from  mdorder where   dealSendid != 0   and printSatues = 1   and  printSatuesp = 0 and sendId = 0  and  deliveryStatues in (0,9) and mdorder.id not in (select orderid from mdorderupdateprint where statues = 2 and mdtype = 6 )  "+search+"  order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; 
				   }else if(Order.release == statues){   
					   sql = "select * from  mdorder where  mdorder.id  in (select orderid from mdorderupdateprint where mdtype in (3,4,5) and pGroupId = "+ user.getUsertype()+ " )      "+search+"  order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; 
				   }else if(Order.porderDispatching == statues){ 
					   sql = "select * from  mdorder where  dealSendid != 0   and printSatues = 1    and  installid = 0 and  deliveryStatues in (1,10)  and statuesinstall = 0  and returnid = 0  or (mdorder.id in (select orderid from mdorderupdateprint where mdtype in (3,4,5) and pGroupId = "+ user.getUsertype()+ " and statues = 0 ))  "+search+" order by "+sort+"  limit " + ((page-1)*num)+","+ page*num; 
				   }else if(Order.installonly  == statues){ 
					   sql = "select * from  mdorder where  dealSendid != 0   and printSatues = 1    and  installid = 0 and  deliveryStatues in (1,10)  and statuesinstall = 0  "+search+" order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; 
				   }else if(Order.orderPrint == statues){ 
					   sql = "select * from  mdorder where  sendId != 0  and  deliveryStatues = 0  and printSatuesp = 0  or  returnid != 0 and  returnprintstatues = 0  "+search+" order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; ;
				   }else if(Order.porderPrint == statues){
					   sql = "select * from  mdorder where  installid != 0 and  deliveryStatues = 1   and printSatuesp = 0  and returnstatues = 0 "+search+"  order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; ;
				   }else if(Order.serach == statues){   
					   sql = "select * from  mdorder where  dealSendid != 0  and printSatues = 1  "+search+"  order by "+sort+"   limit " + ((page-1)*num)+","+ page*num;
				   }else if(Order.callback == statues){ 
					   sql = "select * from  mdorder where  deliveryStatues in (2)  and statuescallback = 0 "+search+" order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; ;
				   }else if(Order.charge== statues){    
					   sql = "select * from  mdorder where  deliveryStatues in (2,5) and  statuesinstall = 0   and statuescallback = 1  and deliverytype = 2  and  statuesinstall = 0 "+search+" order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; 
				   }else if(Order.chargeall== statues){   
					   sql = "select * from  mdorder where  deliveryStatues in (2,5) and  statuesinstall = 0   and statuescallback = 1   and deliverytype = 1  and statuesinstall  = 0 "+search+" order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; 
				   }else if(Order.pcharge == statues){
					   sql = "select * from  mdorder where  deliverytype = 2  and  deliveryStatues in (1,2,4,5)  and statuespaigong  = 0  "+search+" order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; 
				   }else if(Order.orderquery == statues){ 
					   sql = "select * from  mdorder where  ( deliveryStatues in (0,9,10) and sendid != 0  or  installid  != 0  and deliveryStatues in (1,10,9) ) and printSatuesp = 1   order by " + sort;
				   }            
		     } 
		 }else{       
			   if(flag && Group.send == type){    
				   sql = "select * from  mdorder where sendId = "+user.getId(); 
			   }else if(flag && Group.sale == type){
				   sql = "select * from  mdorder where  orderbranch = '"+ user.getBranch() +"' and  deliveryStatues= 0 order by saledate";
			   }else if(flag && Group.dealSend == type){
				   if(Order.orderDispatching == statues){     
					   sql = "select * from mdorder where   ( mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  and  dealSendid = 0   and sendId = 0  and printSatues = 0 and deliveryStatues != 3  and ( mdorder.id in (select orderid from mdorderproduct where salestatues in (0,1,2,3)))  or id in (select orderid from mdorderupdateprint where   pGroupId= '"+user.getUsertype()+"'  and statues = 0  and mdtype in (0 ,1,2,4) ))  "+search+" order by  "+sort+"   limit " + ((page-1)*num)+","+ page*num ;  
				   }else if(Order.neworder == statues){    
					   sql = "select * from mdorder where   ( mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+")) ) and  dealSendid = 0   and printSatues = 0 and deliveryStatues != 3   and mdorder.id in (select orderid from mdorderproduct where salestatues in (0,1,2,3))  "+search+" order by  "+sort+"   " ;
				   }else if(Order.motify == statues){     
					   sql = "select * from mdorder where   ( mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+")) )  and   id in (select orderid from mdorderupdateprint where   pGroupId= "+user.getUsertype()+"  and statues = 0  and mdtype = 0 )   "+search+" order by  "+sort+"   " ;
				   }else if(Order.returns == statues){    
					   sql = "select * from mdorder where   ( mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+")) )  and   id in (select orderid from mdorderupdateprint where   pGroupId= "+user.getUsertype()+"  and statues = 0  and mdtype = 1 )   "+search+" order by  "+sort+"   " ;
				   }else if(Order.release == statues){     
					   sql = "select * from mdorder where   ( mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+")) ) and   id in (select orderid from mdorderupdateprint where   pGroupId= "+user.getUsertype()+"  and statues = 0  and mdtype = 2  )   "+search+" order by  "+sort+"  desc " ;
				   }else if(Order.orderPrint == statues){   
					   sql = "select * from mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  and  dealSendid != 0  and printSatues = 0  and sendId = 0  and  deliveryStatues = 0  "+search+"  order by "+sort+"   limit " + ((page-1)*num)+","+ page*num ;  
				   }else if(Order.charge == statues){ 
					   sql = "select * from mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  and statues1 = 1 and statues2 = 1 and statues3 = 0  "+search+"  order by "+sort+"   limit " + ((page-1)*num)+","+ page*num ;  
				   }else if(Order.callback == statues){ 
					   sql = "select * from mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))   and deliveryStatues in (2)   and wenyuancallback = 0  "+search+" order by "+sort+"   limit " + ((page-1)*num)+","+ page*num ;  
				   }else if(Order.come == statues){
					   sql = "select * from mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))    and statues1 = 0 "+search+" order by "+sort+"   limit " + ((page-1)*num)+","+ page*num ;  
				   }else if(Order.go == statues){ 
					   sql = "select * from mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))    and statues1 = 1 and statues2 = 0   "+search+" order by "+sort+"   limit " + ((page-1)*num)+","+ page*num ;  
				   }else if(Order.over == statues){  
					   sql = "select * from  mdorder where  mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))   and printSatues = 1 and sendId != 0  and deliveryStatues not in (0,3)  and statues4 = 0  "+search+" order by "+sort+"   limit " + ((page-1)*num)+","+ page*num ; 
				   }else if(Order.serach == statues){    
						  sql = "select * from  mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  "+search+"  order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; 
						  //sql = "select * from  mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  "+search+"  or (mdorder.id in (select orderid from mdorderupdateprint where mdtype = 3 and pGroupId = "+ user.getUsertype()+ " ))  order by "+sort+"  desc limit " + ((page-1)*num)+","+ page*num; 
				   }else if(Order.dingma == statues){ 
						  sql = "select * from  mdorder  where  mdorder.saleID in  (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  and id  in (select orderid from mdorderproduct where statues = 1 )  and statuesdingma = 0 "+search+"  order by "+sort+"   limit " + ((page-1)*num)+","+ page*num ;
				   }else if(Order.deliveryStatuesTuihuo == statues){
						   sql = "select * from  mdorder where  deliveryStatues in (3,4,5,11,12,13) "+search+"  order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; ;;
					}   // dispatch 
			   }else if(Group.sencondDealsend == type){   
				   if(Order.orderDispatching == statues){  
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+"  and  ( printSatues = 1 and printSatuesp = 0  and sendId = 0  and  deliveryStatues in (0,9) and mdorder.id not in (select orderid from mdorderupdateprint where statues = 2 and mdtype = 6 )  or (mdorder.id in (select orderid from mdorderupdateprint where mdtype in (3,6,7)  and statues = 0 )) )  "+search+" order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; 
				   }else if(Order.release == statues){   
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+"  and  mdorder.id in (select orderid from mdorderupdateprint where mdtype in (3,4,5) and pGroupId = "+ user.getUsertype()+ " )  "+search+" order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; 
				   }else if(Order.dispatch == statues){ 
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+"  and printSatues = 1 and printSatuesp = 0  and sendId = 0  and  deliveryStatues in (0,9)  and mdorder.id not in (select orderid from mdorderupdateprint where statues = 2 and mdtype = 6 )  "+search+" order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; 
				   }else if(Order.porderDispatching == statues){ 
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+"  and ( printSatues = 1 and printSatuesp = 0    and installid = 0 and  deliveryStatues in (1,10)  and statuesinstall = 0  and returnid = 0  or (mdorder.id in (select orderid from mdorderupdateprint where mdtype in (3,4,5) and pGroupId = "+ user.getUsertype()+ "  and statues = 0 )) )"  + " order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; 
				   }else if(Order.installonly == statues){ 
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+"  and printSatues = 1 and printSatuesp = 0    and installid = 0 and  deliveryStatues in (1,10)  and statuesinstall = 0  "  + " order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; 
				   }else if(Order.orderPrint == statues){  
					   sql = "select * from  mdorder where  (sendId != 0  and printSatuesp = 0   and  deliveryStatues = 0  or  returnid != 0 and  returnprintstatues = 0 ) and  dealSendid = "+user.getId()+"  "+search+"  order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; ;
				   }else if(Order.porderPrint == statues){ 
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+"  and  installid != 0 and  deliveryStatues = 1  and printSatuesp = 0  and returnstatues = 0 "+search+"  order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; ;
				   }else if(Order.serach == statues){   
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+ "  and printSatues = 1 "+search+"  order by "+sort+"   limit " + ((page-1)*num)+","+ page*num;
				   }else if(Order.callback == statues){ 
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+" and  deliveryStatues in (2)  and statuescallback = 0 "+search+" order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; ; ; 
				   }else if(Order.charge == statues){ //and deliverytype = 1  
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+" and   deliveryStatues in (2,5)  and statuescallback = 1  and deliverytype = 2  and  statuesinstall = 0  "+search+"  order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; ; ; 
				   }else if(Order.chargeall == statues){ // 
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+" and   deliveryStatues in (2,5)  and statuescallback = 1  and deliverytype = 1  and statuesinstall  = 0   "+search+"  order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; ; ; 
				   }else if(Order.pcharge == statues){ 
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+" and   deliveryStatues in (1,2,4,5)  and deliverytype = 2  and statuespaigong  = 0  "+search+"  order by "+sort+"   limit " + ((page-1)*num)+","+ page*num; ; ; 
				   }else if(Order.orderquery == statues){  
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+" ( and deliveryStatues in (0,9,10)   and sendid != 0  or  installid != 0  and deliveryStatues in (1,10,9) )  and printSatuesp = 1    "+search+"  order by "+sort+"   limit " + ((page-1)*num)+","+ page*num;    
				   }        
			   }                    
	    }      
	    
	  if("".equals(sql)){
		   return null;  
	   } 
logger.info(sql); 
	   Connection conn = DB.getConn();
       Statement stmt = DB.getStatement(conn);
     
	   ResultSet rs = DB.getResultSet(stmt, sql); 
			try { 
				while (rs.next()) {
					Order p = gerOrderFromRs(rs);
					Orders.add(p);
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
    
     
    public static int getOrderlistcount(User user ,int type,int statues ,int num,int page,String sort,String search){
  	  
  	  boolean f = UserManager.checkPermissions(user, Group.Manger);  
  	   
  	  boolean flag = UserManager.checkPermissions(user, type);
  	  int count = 0 ;	   
  	  String sql = "";   
  	       
  	if(f){ 
		  if(Group.dealSend == type){  
			  if(Order.orderDispatching == statues){
				  sql = "select count(*) from  mdorder  where  (dealSendid = 0   and sendId = 0 and printSatues = 0  and deliveryStatues != 3  and ( mdorder.id in (select orderid from mdorderproduct where salestatues in (0,1,2,3)))   or id in (select orderid from mdorderupdateprint where statues = 0 and mdtype in (0 ,1,2,4) ))  "+search  ;  
			  }else if(Order.neworder == statues){
				  sql = "select count(*) from  mdorder  where  dealSendid = 0   and sendId = 0 and printSatues = 0  and deliveryStatues != 3  and mdorder.id in (select orderid from mdorderproduct where salestatues in (0,1,2,3))  "+search;    
			  }else if(Order.motify == statues){
				  sql = "select count(*) from  mdorder  where  id in (select orderid from mdorderupdateprint where statues = 0 and mdtype = 0 )   "+search;   
			  }else if(Order.returns == statues){
				  sql = "select count(*) from  mdorder  where  id in (select orderid from mdorderupdateprint where statues = 0 and mdtype = 1 )   "+search;   
			  }else if(Order.release == statues){
				  sql = "select count(*) from  mdorder  where  id in (select orderid from mdorderupdateprint where statues = 0 and mdtype = 2  )   "+search;   
			  }else if(Order.callback == statues){ 
				   sql = "select count(*) from  mdorder where  deliveryStatues in (2)  and wenyuancallback = 0 "+search ;
			   }else if(Order.orderPrint == statues){ 
				  sql = "select count(*) from  mdorder  where  dealSendid != 0   and sendId = 0 and printSatues = 0  and deliveryStatues != 3    "+search;  
			  }else if(Order.serach == statues){ 
				  sql = "select count(*) from  mdorder  where 1 =1 "+search;
			  }else if(Order.charge == statues){ 
				  sql = "select count(*) from  mdorder  where statues1 = 1 and statues2 = 1 and statues3 = 0  "+search;
			  }else if(Order.come == statues){
				  sql = "select count(*) from  mdorder  where  statues1 = 0  "+search;
			  }else if(Order.go == statues){ 
				  sql = "select count(*) from  mdorder  where  statues1 = 1 and statues2 = 0  "+search ;
			  }else if(Order.over == statues){ 
				  sql = "select count(*) from  mdorder  where dealSendid != 0   and printSatues = 1  and deliveryStatues not in (0,3)  and statues4 = 0  "+search ;
			  }else if(Order.dingma == statues){ 
				  sql = "select count(*) from  mdorder  where id  in (select orderid from mdorderproduct where statues = 1 )  and statuesdingma = 0  "+search;
			  }else if(Order.deliveryStatuesTuihuo == statues){  
				   sql = "select count(*) from  mdorder where  deliveryStatues in (3,4,5,11,12,13)  "+search ;
			   }          
		  }else if(Group.sencondDealsend == type){ 
				   if(Order.orderDispatching == statues){ 
					   sql = "select count(*) from  mdorder where  ( dealSendid != 0   and printSatues = 1   and  printSatuesp = 0 and sendId = 0  and  deliveryStatues in (0,9)  and mdorder.id not in (select orderid from mdorderupdateprint where statues = 2 and mdtype = 6 )   or (mdorder.id in (select orderid from mdorderupdateprint where mdtype in (3,6,7) and pGroupId = "+ user.getUsertype()+ " and statues = 0 )) ) "+search; 
				   }else if(Order.release == statues){   
					   sql = "select count(*) from  mdorder where  mdorder.id  in (select orderid from mdorderupdateprint where mdtype in (3,4,5) and pGroupId = "+ user.getUsertype()+ " )      "+search; 
				   }else if(Order.dispatch == statues){  
					   sql = "select count(*) from  mdorder where   dealSendid != 0   and printSatues = 1   and  printSatuesp = 0 and sendId = 0  and  deliveryStatues in (0,9)   and mdorder.id not in (select orderid from mdorderupdateprint where statues = 2 and mdtype = 6 )  "+search;  
				   }else if(Order.porderDispatching == statues){ 
					   sql = "select count(*) from  mdorder where  dealSendid != 0   and printSatues = 1    and  installid = 0 and  deliveryStatues in (1,10)  and statuesinstall = 0  and returnid = 0 or (mdorder.id in (select orderid from mdorderupdateprint where mdtype in (4,5) and pGroupId = "+ user.getUsertype()+ "  and statues = 0 )) "+search; 
				   }else if(Order.installonly  == statues){ 
					   sql = "select count(*) from  mdorder where  dealSendid != 0   and printSatues = 1    and  installid = 0 and  deliveryStatues in (1,10)  and statuesinstall = 0  "+search; 
				   }else if(Order.orderPrint == statues){   
					   sql = "select count(*) from  mdorder where  sendId != 0  and  deliveryStatues = 0  and printSatuesp = 0  "+search ;
				   }else if(Order.porderPrint == statues){
					   sql = "select count(*) from  mdorder where  installid != 0 and  deliveryStatues = 1  and printSatuesp = 0 and returnstatues = 0 "+search ;
				   }else if(Order.serach == statues){   
					   sql = "select count(*) from  mdorder where  dealSendid != 0  and printSatues = 1  "+search;
				   }else if(Order.callback == statues){ 
					   sql = "select count(*) from  mdorder where  deliveryStatues in (2)  and statuescallback = 0 "+search ; 
				   }else if(Order.charge== statues){ //and deliverytype = 1 
					   sql = "select count(*) from  mdorder where  deliveryStatues in (2,5)  and statuescallback = 1   and deliverytype = 2  and  statuesinstall = 0  "+search; 
				   }else if(Order.chargeall== statues){ //and deliverytype = 1 
					   sql = "select count(*) from  mdorder where  deliveryStatues in (2,5)  and statuescallback = 1   and deliverytype = 1  and  statuesinstall = 0  "+search; 
				   }else if(Order.pcharge== statues){
					   sql = "select count(*) from  mdorder where  deliveryStatues in (1,2,4,5)  and deliverytype = 2   and statuespaigong  = 0  "+search;  
				   }else if(Order.orderquery == statues){ 
					   sql = "select count(*) from  mdorder where  ( deliveryStatues in (0,9,10)   and sendid != 0   or  installid != 0 and deliveryStatues in (1,10,9) ) and printSatuesp = 1  ";
				   }           
		     }  
		 }else{       
			   if(flag && Group.send == type){    
				   sql = "select count(*) from  mdorder where sendId = "+user.getId(); 
			   }else if(flag && Group.sale == type){
				   sql = "select count(*) from  mdorder where  orderbranch = '"+ user.getBranch() +"' and  deliveryStatues= 0 order by saledate";
			   }else if(flag && Group.dealSend == type){ 
				   if(Order.orderDispatching == statues){     
					   sql = "select count(*) from mdorder where   ( mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  and  dealSendid = 0   and printSatues = 0 and deliveryStatues != 3  and sendId = 0   and ( mdorder.id in (select orderid from mdorderproduct where salestatues in (0,1,2,3)))   or id in (select orderid from mdorderupdateprint where   pGroupId= '"+user.getUsertype()+"'  and statues = 0  and mdtype in (0 ,1,2,4) ))  "+search;  
				   }else if(Order.neworder == statues){     
					   sql = "select count(*) from mdorder where    mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+") )  and  dealSendid = 0   and printSatues = 0  and  deliveryStatues != 3   and mdorder.id in (select orderid from mdorderproduct where salestatues in (0,1,2,3))  "+search  ;  
				   }else if(Order.motify == statues){     
					   sql = "select count(*) from mdorder where   ( mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  and   id in (select orderid from mdorderupdateprint where   pGroupId= "+user.getUsertype()+"  and statues = 0  and mdtype = 0  ) )  "+search ; 
				   }else if(Order.returns == statues){    
					   sql = "select count(*) from mdorder where   ( mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  and   id in (select orderid from mdorderupdateprint where   pGroupId= "+user.getUsertype()+"  and statues = 0  and mdtype = 1 ) )  "+search ; 
				   }else if(Order.release == statues){     
					   sql = "select count(*) from mdorder where   ( mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  and   id in (select orderid from mdorderupdateprint where   pGroupId= "+user.getUsertype()+"  and statues = 0  and mdtype = 2  ) )   "+search; 
				   }else if(Order.callback == statues){ 
					   sql = "select count(*) from mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  and deliveryStatues in (2)  and wenyuancallback = 0  "+search;  
				   }else if(Order.orderPrint == statues){ 
					   sql = "select count(*) from mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  and  dealSendid != 0  and printSatues = 0  and sendId = 0  and deliveryStatues != 3  "+search;  
				   }else if(Order.charge == statues){ 
					   sql = "select count(*) from mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  and statues1 = 1 and statues2 = 1 and statues3 = 0  "+search;  
				   }else if(Order.come == statues){  
					   sql = "select count(*) from mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))    and statues1 = 0 "+search;  
				   }else if(Order.go == statues){ 
					   sql = "select count(*) from mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))    and statues1 = 1 and statues2 = 0   "+search;  
				   }else if(Order.over == statues){  
					   sql = "select count(*) from  mdorder where  mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))   and printSatues = 1 and sendId != 0  and deliveryStatues not in (0,3)  and statues4 = 0  "+search; 
				   }else if(Order.serach == statues){    
						  sql = "select count(*) from  mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  "+search;  
						  //sql = "select * from  mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  "+search+"  or (mdorder.id in (select orderid from mdorderupdateprint where mdtype = 3 and pGroupId = "+ user.getUsertype()+ " ))  order by "+sort+"  desc limit " + ((page-1)*num)+","+ page*num; 
				   }else if(Order.dingma == statues){    
						  sql = "select count(*) from  mdorder  where  mdorder.saleID in  (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  and id  in (select orderid from mdorderproduct where statues = 1 )  and statuesdingma = 0 "+search;
				   }else if(Order.deliveryStatuesTuihuo == statues){ 
						   sql = "select count(*) from  mdorder where  deliveryStatues in (3,4,5,11,12,13) "+search;
					}    
			   }else if(Group.sencondDealsend == type){   
				   if(Order.orderDispatching == statues){   
					   sql = "select count(*) from  mdorder where   ( dealSendid = "+user.getId()+"  and printSatues = 1 and printSatuesp = 0  and sendId = 0  and  deliveryStatues in (0,9)  and mdorder.id not in (select orderid from mdorderupdateprint where statues = 2 and mdtype = 6 )   or (mdorder.id in (select orderid from mdorderupdateprint where mdtype in (3,6,7)  and statues = 0 )) )  "+search; 
				   }else if(Order.release == statues){   
					   sql = "select count(*) from  mdorder where  dealSendid = "+user.getId()+"  and  mdorder.id in (select orderid from mdorderupdateprint where mdtype in (3,4,5) and pGroupId = "+ user.getUsertype()+ " )  "+search; 
				   }else if(Order.dispatch == statues){  
					   sql = "select count(*) from  mdorder where  dealSendid = "+user.getId()+"  and printSatues = 1 and printSatuesp = 0  and sendId = 0  and  deliveryStatues in (0,9) and mdorder.id not in (select orderid from mdorderupdateprint where statues = 2 and mdtype = 6 )   "+search; 
				   }else if(Order.porderDispatching == statues){
					   sql = "select count(*) from  mdorder where  dealSendid = "+user.getId()+"  and printSatues = 1 and printSatuesp = 0    and installid = 0 and  deliveryStatues in (1,10)  and statuesinstall = 0  and returnid = 0 or (mdorder.id in (select orderid from mdorderupdateprint where mdtype in (4,5) and pGroupId = "+ user.getUsertype()+ "  and statues = 0 )) "+search;
				   }else if(Order.installonly == statues){ 
					   sql = "select count(*) from  mdorder where  dealSendid = "+user.getId()+"  and printSatues = 1 and printSatuesp = 0    and installid = 0 and  deliveryStatues in (1,10)  and statuesinstall = 0  "; 
				   }else if(Order.orderPrint == statues){   
					   sql = "select count(*) from  mdorder where  dealSendid = "+user.getId()+"    and  sendId != 0  and printSatuesp = 0   "+search;
				   }else if(Order.porderPrint == statues){ 
					   sql = "select count(*) from  mdorder where  dealSendid = "+user.getId()+"  and  installid != 0 and  deliveryStatues = 1  and printSatuesp = 0  and returnstatues = 0  "+search;
				   }else if(Order.serach == statues){   
					   sql = "select count(*) from  mdorder where  dealSendid = "+user.getId()+ "  and printSatues = 1 "+search;
				   }else if(Order.callback == statues){  
					   sql = "select count(*) from  mdorder where  dealSendid = "+user.getId()+" and  deliveryStatues in (2)  and statuescallback = 0 "+search;  
				   }else if(Order.charge == statues){ 
					   sql = "select count(*) from  mdorder where  dealSendid = "+user.getId()+" and  deliveryStatues in (2,5)  and statuescallback = 1   and deliverytype = 2  and  statuesinstall = 0 "+search ; 
				   }else if(Order.chargeall == statues){   
					   sql = "select count(*) from  mdorder where  dealSendid = "+user.getId()+" and  deliveryStatues in (2,5)  and statuescallback = 1   and deliverytype = 1  and  statuesinstall = 0 "+search ; 
				   } else if(Order.pcharge == statues){  
					   sql = "select count(*) from  mdorder where  dealSendid = "+user.getId()+" and  deliveryStatues in (1,2,4,5)  and deliverytype = 2   and statuespaigong  = 0  "+search ; 
				   }else if(Order.orderquery == statues){  
					   sql = "select count(*) from  mdorder where  dealSendid = "+user.getId()+" ( and deliveryStatues in (0,9,10)   and sendid != 0  or  installid != 0  and deliveryStatues in (1,10,9) )  and printSatuesp = 1    "+search ; 
				   }    
			   }                    
	    }       
  	  if("".equals(sql)){ 
  		   count =  0; 
  		   return count ;
  	   }  
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
    
    
    //wrote by wilsonlee
    //已经结款的Order
    public static List<Order> getUnCheckedDBOrders(){
    	  
    	
    	//boolean flag = UserManager.checkPermissions(user, Group.dealSend); 
    	//flag = true;
    	List<Order> Orders = new ArrayList<Order>();
   
    	String sql = "select * from  mdorder  where statues1 = 1 and statues2 = 1 and statues3 = 0";                  
    	   
    	if(true){
    		Connection conn = DB.getConn();
            Statement stmt = DB.getStatement(conn);
            ResultSet rs = DB.getResultSet(stmt, sql);

 			try { 
 				while (rs.next()) {
 					Order p = gerOrderFromRs(rs);
  					Orders.add(p);
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
    
   public static List<Order> getOrderlistPrintln(User user ,int type,int num,int page,String sort){
	    boolean f = UserManager.checkPermissions(user, Group.Manger); 
		  
        boolean flag = UserManager.checkPermissions(user, type);
			   
		List<Order> Orders = new ArrayList<Order>();
		String sql = "";  
		if(f){
			  sql = "select * from  mdorder  where dealSendid != 0  and sendId = 0 and printSatues = 0  and deliveryStatues != 3  order by "+sort+"  desc limit " + ((page-1)*num)+","+ page*num ;
		  }else {
			  if(user.getUsertype() == 1   && Group.send != type){
				   sql = "select * from  mdorder  where sendId != 0  and printSatues = 1  and deliveryStatues != 3  order by id  desc limit " + ((page-1)*num)+","+ page*num ;
			   }else if(flag && Group.send == type){  
				   sql = "select * from  mdorder where sendId = "+user.getId();
			   }else if(flag && Group.sale == type){
				   sql = "select * from  mdorder where  orderbranch = '"+ user.getBranch() +"' and  deliveryStatues= 0 order by saledate";
			   }
			   else if(flag && Group.dealSend == type){ 
				   sql = "select * from mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  and  dealSendid != 0  and printSatues = 0  and sendId = 0  and deliveryStatues != 3  order by "+sort+"  desc limit " + ((page-1)*num)+","+ page*num ;  
			   }
		  }
	   
	   if("".equals(sql)){
		   return null;
	   }
logger.info(sql);
		Connection conn = DB.getConn();
      Statement stmt = DB.getStatement(conn);
	   ResultSet rs = DB.getResultSet(stmt, sql);
			try { 
				while (rs.next()) {
					Order p = gerOrderFromRs(rs);
					Orders.add(p);
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
   
   public static List<Order> getOrderlistCome(User user ,int type,int num,int page,String sort){
	    boolean f = UserManager.checkPermissions(user, Group.Manger); 
		  
       boolean flag = UserManager.checkPermissions(user, type);
			   
		List<Order> Orders = new ArrayList<Order>();
		String sql = "";  
		if(f){  
			  sql = "select * from  mdorder  where  statues1 = 0  order by "+sort+"  desc limit " + ((page-1)*num)+","+ page*num ;
		  }else {
			  if(user.getUsertype() == 1   && Group.send != type){
				   sql = "select * from  mdorder  where sendId != 0  and printSatues = 1  and deliveryStatues != 3  order by id  desc limit " + ((page-1)*num)+","+ page*num ;
			   }else if(flag && Group.send == type){  
				   sql = "select * from  mdorder where sendId = "+user.getId();
			   }else if(flag && Group.sale == type){
				   sql = "select * from  mdorder where  orderbranch = '"+ user.getBranch() +"' and  deliveryStatues= 0 order by saledate";
			   }
			   else if(flag && Group.dealSend == type){ 
				   sql = "select * from mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))    and statues1 = 0 order by "+sort+"  desc limit " + ((page-1)*num)+","+ page*num ;  
			   }
		  }
	   
	   if("".equals(sql)){
		   return null;
	   }
logger.info(sql);
		Connection conn = DB.getConn();
     Statement stmt = DB.getStatement(conn);
	   ResultSet rs = DB.getResultSet(stmt, sql);
			try { 
				while (rs.next()) {
					Order p = gerOrderFromRs(rs);
					Orders.add(p);
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
   
   
   public static List<Order> getOrderlistgo(User user ,int type,int num,int page,String sort){
	    boolean f = UserManager.checkPermissions(user, Group.Manger); 
		  
      boolean flag = UserManager.checkPermissions(user, type);
			   
		List<Order> Orders = new ArrayList<Order>();
		String sql = "";  
		if(f){  
			  sql = "select * from  mdorder  where  deliveryStatues != 3  and statues1 = 1 and statues2 = 0  order by "+sort+"  desc limit " + ((page-1)*num)+","+ page*num ;
		  }else {
			  if(user.getUsertype() == 1   && Group.send != type){
				   sql = "select * from  mdorder  where sendId != 0  and printSatues = 1  and deliveryStatues != 3  order by id  desc limit " + ((page-1)*num)+","+ page*num ;
			   }else if(flag && Group.send == type){  
				   sql = "select * from  mdorder where sendId = "+user.getId();
			   }else if(flag && Group.sale == type){
				   sql = "select * from  mdorder where  orderbranch = '"+ user.getBranch() +"' and  deliveryStatues= 0 order by saledate";
			   }
			   else if(flag && Group.dealSend == type){ 
				   sql = "select * from mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))    and statues1 = 1 and statues2 = 0   order by "+sort+"  desc limit " + ((page-1)*num)+","+ page*num ;  
			   }
		  }
	   
	   if("".equals(sql)){
		   return null;
	   }
logger.info(sql);
		Connection conn = DB.getConn();
    Statement stmt = DB.getStatement(conn);
	   ResultSet rs = DB.getResultSet(stmt, sql);
			try { 
				while (rs.next()) {
					Order p = gerOrderFromRs(rs);
					Orders.add(p);
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
   
   public static List<Order> getOrderlistCharge(User user ,int type,int num,int page,String sort){
	    boolean f = UserManager.checkPermissions(user, Group.Manger); 
		  
     boolean flag = UserManager.checkPermissions(user, type);
			   
		List<Order> Orders = new ArrayList<Order>();
		String sql = "";  
		if(f){    
			  sql = "select * from  mdorder  where statues1 = 1 and statues2 = 1 and statues3 = 0  order by "+sort+"  desc limit " + ((page-1)*num)+","+ page*num ;
		  }else {
			  if(user.getUsertype() == 1   && Group.send != type){
				   sql = "select * from  mdorder  where sendId != 0  and printSatues = 1  and deliveryStatues != 3  order by id  desc limit " + ((page-1)*num)+","+ page*num ;
			   }else if(flag && Group.send == type){  
				   sql = "select * from  mdorder where sendId = "+user.getId();
			   }else if(flag && Group.sale == type){
				   sql = "select * from  mdorder where  orderbranch = '"+ user.getBranch() +"' and  deliveryStatues= 0 order by saledate";
			   } 
			   else if(flag && Group.dealSend == type){  
				  // sql = "select * from mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+")) and deliveryStatues != 3  and and statues1 = 1 and statues2 = 1 and statues3 = 0  order by id  desc limit " + ((page-1)*num)+","+ page*num ;  
				   sql = "select * from mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  and statues1 = 1 and statues2 = 1 and statues3 = 0   order by "+sort+"  desc limit " + ((page-1)*num)+","+ page*num ;  
			   }
		  }
	   
	   if("".equals(sql)){
		   return null;
	   }
logger.info(sql);
		Connection conn = DB.getConn();
   Statement stmt = DB.getStatement(conn);
	   ResultSet rs = DB.getResultSet(stmt, sql);
			try { 
				while (rs.next()) {
					Order p = gerOrderFromRs(rs);
					Orders.add(p);
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
   
   public static List<Order> getOrderlistPrintlnSend(User user ,int type,int num,int page,String sort){
	   boolean f = UserManager.checkPermissions(user, Group.Manger); 
		  
       boolean flag = UserManager.checkPermissions(user, type);
			    
		List<Order> Orders = new ArrayList<Order>();
		String sql = "";   
		if(f){   
			  sql = "select * from  mdorder  where  printSatuesp = 0  and printSatues = 1 and sendId != 0 order by "+sort+"  desc limit " + ((page-1)*num)+","+ page*num ;
		  }else { 
			  if(user.getUsertype() == 1   && Group.send != type){ 
				   sql = "select * from  mdorder  where sendId != 0  and printSatues = 0  and deliveryStatues != 3  order by id  desc limit " + ((page-1)*num)+","+ page*num ;
			   }else if(flag && Group.send == type){  
				   sql = "select * from  mdorder where sendId = "+user.getId();
			   }else if(flag && Group.sale == type){  
				   sql = "select * from  mdorder where  orderbranch = '"+ user.getBranch() +"' and  deliveryStatues= 0 order by saledate";
			   }  
			   else if(flag && Group.dealSend == type){       
				   sql = "select * from mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  order by "+sort+" desc limit " + ((page-1)*num)+","+ page*num ;  
			   } else if(flag && Group.sencondDealsend == type){         
				   sql = "select * from  mdorder where  dealSendid = "+user.getId()+"   and  printSatuesp = 0  and printSatues = 1  and sendId != 0  or (mdorder.id in (select orderid from mdorderupdateprint where mdtype = 3 and pGroupId = "+ user.getUsertype()+ " ) and sendId != 0) " ;
			   }   
   
		  } 
		
	   if("".equals(sql)){
		   return null;
	   }
logger.info(sql);
		Connection conn = DB.getConn();
     Statement stmt = DB.getStatement(conn);
	   ResultSet rs = DB.getResultSet(stmt, sql);
			try { 
				while (rs.next()) {
					Order p = gerOrderFromRs(rs);
					Orders.add(p);
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
  
   public static List<Order> getOrderlistover(User user ,int type,int num,int page,String sort){
	   boolean f = UserManager.checkPermissions(user, Group.Manger);  
       boolean flag = UserManager.checkPermissions(user, type);
			   
		List<Order> Orders = new ArrayList<Order>();
		String sql = "";   
		if(f){   
			  sql = "select * from  mdorder  where dealSendid != 0   and printSatues = 1  and deliveryStatues = 2  and statues4 = 0   order by "+sort+"  desc limit " + ((page-1)*num)+","+ page*num ;
		  }else {
			  if(user.getUsertype() == 1   && Group.send != type){
				   sql = "select * from  mdorder  where sendId != 0  and printSatues = 0  and deliveryStatues = 2  order by id  desc limit " + ((page-1)*num)+","+ page*num ;
			   }else if(flag && Group.send == type){  
				   sql = "select * from  mdorder where sendId = "+user.getId();
			   }else if(flag && Group.sale == type){  
				   sql = "select * from  mdorder where  orderbranch = '"+ user.getBranch() +"' and  deliveryStatues= 0 order by saledate";
			   }  
			   else if(flag && Group.dealSend == type){   
				   sql = "select * from mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  and  dealSendid != 0  and printSatues = 1  and deliveryStatues = 2 and statues4 = 0  order by "+sort+"  desc limit " + ((page-1)*num)+","+ page*num ;  
			   } else if(flag && Group.sencondDealsend == type){     
				   sql = "select * from  mdorder where  dealSendid = "+user.getId()+"   and printSatues = 1 and sendId != 0  or (mdorder.id in (select orderid from mdorderupdateprint where mdtype = 3 and pGroupId = "+ user.getUsertype()+ " ) and sendId != 0) " ; 
			   }   
  
		  } 
		
	          
	  
	   
	   if("".equals(sql)){
		   return null;
	   }
logger.info(sql);
		Connection conn = DB.getConn();
     Statement stmt = DB.getStatement(conn);
	   ResultSet rs = DB.getResultSet(stmt, sql);
			try { 
				while (rs.next()) {
					Order p = gerOrderFromRs(rs);
					Orders.add(p);
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
   
   public static List<Order> getOrderlistTuihuo(User user ,int type,int num,int page){
	    
	    List<Group> listg = GroupManager.getGroupPeimission(type);
	    
		boolean flag = false ;
		for(int i=0;i<listg.size();i++){
			Group g = listg.get(i);
			if(user.getUsertype() == g.getId()){
				flag = true ;
			}
		} 
		   
	   List<Order> Orders = new ArrayList<Order>();
	   String sql = "";         
	   if(user.getUsertype() == 1  && Group.send != type){
		   sql = "select * from  mdorder  where  deliveryStatues = 3  order by id  desc limit " + ((page-1)*num)+","+ page*num ;
	   }else if(flag && Group.send == type){  
		   sql = "select * from  mdorder where sendId = "+user.getId();
	   }else if(flag && Group.sale == type){   
		   sql = "select * from  mdorder where  orderbranch = '"+ user.getBranch() +"' and  deliveryStatues= 0 order by saledate";
	   } 
	   else if(flag && Group.dealSend == type){
		   sql = "select * from  mdorder where sendId = "+ user.getId() +" and  deliveryStatues= 3 " ;
	   }
	     
	   if("".equals(sql)){
		   return null;
	   } 
logger.info(sql); 
		Connection conn = DB.getConn();
    Statement stmt = DB.getStatement(conn);
	   ResultSet rs = DB.getResultSet(stmt, sql);
			try { 
				while (rs.next()) {
					Order p = gerOrderFromRs(rs);
					Orders.add(p);
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
 
   public static Order getOrderID(User user ,int id){
	   
	   Order orders = null; 
	   String sql = "";  
		   sql = "select * from  mdorder where id = "+ id ;
		   
logger.info(sql);
		    Connection conn = DB.getConn();
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try { 
				while (rs.next()) {
					orders = gerOrderFromRs(rs);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(stmt);
				DB.close(rs);
				DB.close(conn);
			 }
			return orders;
	 }
   
   
   public static String getSql(User user ,int type){
	   
	   List<Group> listg = GroupManager.getInstance().getListGroupFromPemission(type);
	    
		boolean flag = false ;
		for(int i=0;i<listg.size();i++){
			Group g = listg.get(i);
			if(user.getUsertype() == g.getId()){
				flag = true ;
			}
		}    
		 
		String sql = "";  
		   if(user.getUsertype() == 1   && Group.send != type && Group.dealSend != type){
			   sql = "select * from  mdorder where 1 =1 "; 
		   }else if(flag && Group.send == type){
			   sql = "select * from  mdorder where sendId = "+user.getId()  ;
		   }else if(flag && Group.sale == type){
			   sql = "select * from  mdorder where  orderbranch = '"+ user.getBranch() +"' " + "and  deliveryStatues= 0 order by saledate";
		   }  
		   else if(flag && Group.dealSend == type){
			   sql = "select * from  mdorder where saleID in  (select id from mdgroup where pid = "+ user.getId()+")  or (mdorder.id in (select orderid from mdorderupdateprint where mdtype = 3) and sendId != 0)" ;
		   }
	   return sql;
   }
    
   
   
   public static boolean delete(int id) {
	   
	    boolean flag = false;
	    List<String> listsqls = new ArrayList<String>();
		
		Connection conn = DB.getConn();
 
		String sqlp = OrderProductManager.delete(id);
        String sqlg = GiftManager.delete(id) ;  
        String sqlop =  OrderPrintlnManager.deleteByoid(id);
         
        String sql = "delete from mdorder where id = " + id;
        listsqls.add(sqlp); 
        listsqls.add(sqlg); 
        listsqls.add(sqlop);
        listsqls.add(sql); 
		 
		if (listsqls.size() == 0) {  
            return false;   
        }      
        Statement sm = null;  
        try {  
            // 事务开始  
           logger.info("事物处理开始") ;
            conn.setAutoCommit(false);   // 设置连接不自动提交，即用该连接进行的操作都不更新到数据库  
            sm = conn.createStatement(); // 创建Statement对象  
            
             Object[] sqls = listsqls.toArray() ;
            //依次执行传入的SQL语句  
            for (int i = 0; i < sqls.length; i++) {  
                sm.execute((String)sqls[i]);// 执行添加事物的语句  
            }   
            logger.info("提交事务处理！");  
               
            conn.commit();   // 提交给数据库处理  
               
            logger.info("事务处理结束！");  
            // 事务结束  
             flag = true ;   
        //捕获执行SQL语句组中的异常      
        } catch (SQLException e) {  
            try {   
                logger.info("事务执行失败，进行回滚！\n");  
                conn.rollback(); // 若前面某条语句出现异常时，进行回滚，取消前面执行的所有操作  
            } catch (SQLException e1) {  
                e1.printStackTrace();  
            }  
        } finally {  
            try {
				sm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}  
        }  
		return flag ;
	}
     
   
   public static boolean update(int id) {
	   
	    boolean flag = false;
	    List<String> listsqls = new ArrayList<String>();
		
		Connection conn = DB.getConn();

		//String sqlp = OrderProductManager.delete(id);
     //  String sqlg = GiftManager.delete(id) ;  
       String sqlop =  OrderPrintlnManager.deleteByoid(id);
        
       String sql = "delete from mdorder where id = " + id;
      // listsqls.add(sqlp); 
     //  listsqls.add(sqlg); 
       listsqls.add(sqlop);
       listsqls.add(sql); 
		 
		if (listsqls.size() == 0) {  
           return false;   
       }      
       Statement sm = null;  
       try {  
           // 事务开始  
          logger.info("事物处理开始") ;
           conn.setAutoCommit(false);   // 设置连接不自动提交，即用该连接进行的操作都不更新到数据库  
           sm = conn.createStatement(); // 创建Statement对象  
           
            Object[] sqls = listsqls.toArray() ;
           //依次执行传入的SQL语句  
           for (int i = 0; i < sqls.length; i++) {  
               sm.execute((String)sqls[i]);// 执行添加事物的语句  
           }   
           logger.info("提交事务处理！");  
              
           conn.commit();   // 提交给数据库处理  
              
           logger.info("事务处理结束！");  
           // 事务结束  
            flag = true ;   
       //捕获执行SQL语句组中的异常      
       } catch (SQLException e) {  
           try {   
               logger.info("事务执行失败，进行回滚！\n");  
               conn.rollback(); // 若前面某条语句出现异常时，进行回滚，取消前面执行的所有操作  
           } catch (SQLException e1) {  
               e1.printStackTrace();  
           }  
       } finally {  
           try {
				sm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}  
       }  
		return flag ;
	}
   
   public static boolean deleteed(int id) {
		boolean b = false;
		Connection conn = DB.getConn();       
		String sql = "update mdorder set deliveryStatues = 3 where id = " + id;
		Statement stmt = DB.getStatement(conn);
		try {
			DB.executeUpdate(stmt, sql);
			b = true;  
			OrderPrintlnManager.delete(id, OrderPrintln.returns); 
		} finally {
			DB.close(stmt);
			DB.close(conn); 
		}
		return b;
	}
    
   public static Order gerOrderFromRs(ResultSet rs){
	   Order p = null;
		try { 
			p = new Order();
			p.setId(rs.getInt("id")); 
			p.setLocate(rs.getString("locates"));
			p.setLocateDetail(rs.getString("locateDetail"));
			p.setSaleTime(rs.getString("saledate"));
			p.setSaleID(rs.getInt("saleID"));
			p.setSendId(rs.getInt("sendID"));
			p.setOdate(rs.getString("andate"));
			p.setPhone1(rs.getString("phone1")); 
			p.setPhone2(rs.getString("phone2")); 
			p.setUsername(rs.getString("username"));
			p.setPrintSatues(rs.getInt("printSatues"));
			p.setSaleID(rs.getInt("saleID"));
			p.setDeliveryStatues(rs.getInt("deliveryStatues"));
			p.setPos(rs.getString("pos"));
			p.setSailId(rs.getString("sailId"));
			p.setCheck(rs.getString("checked"));
			p.setRemark(rs.getString("remark"));
			p.setBranch(rs.getString("orderbranch"));
			p.setCategoryID(rs.getString("categoryID"));
			p.setDealsendId(rs.getInt("dealSendid"));
			p.setStatues1(rs.getInt("statues1"));
			p.setStatues2(rs.getInt("statues2")); 
			p.setStatues3(rs.getInt("statues3"));
			p.setStatues4(rs.getInt("statues4"));    
			p.setPrintSatuesP(rs.getInt("printSatuesp"));   
			p.setStatuesDingma(rs.getInt("statuesdingma"));  
			p.setInstallid(rs.getInt("installid")); 
			p.setPrintlnid(rs.getString("printlnid"));  
			p.setStatuescallback(rs.getInt("statuescallback")); 
		    p.setStatuesPaigong(rs.getInt("statuespaigong")); 
		    p.setDayremark(rs.getInt("dayremark")); 
		    p.setDayID(rs.getInt("dayID"));    
		    p.setPhoneRemark(rs.getInt("phoneRemark")); 
		    p.setInstalltime(rs.getString("installTime"));
		    p.setSendtime(rs.getString("sendTime"));
		    p.setDeliverytype(rs.getInt("deliverytype"));
		    p.setPosremark(rs.getInt("posRemark"));
		    p.setSailidrecked(rs.getInt("sailIdremark"));
		    p.setReckedremark(rs.getInt("checkedremark")); 
		    p.setStatuesinstall(rs.getInt("statuesinstall"));
		    p.setReturnid(rs.getInt("returnid")); 
		    p.setReturnstatuse(rs.getInt("returnstatues"));
		    p.setReturntime(rs.getString("returntime"));  
		    p.setReturnprintstatues(rs.getInt("returnprintstatues")); 
		    p.setReturnwenyuan(rs.getInt("returnwenyuan")); 
		    p.setPrintdingma(rs.getInt("printdingma"));  
		    p.setDealSendTime(rs.getString("dealsendTime"));
		    p.setWenyuancallback(rs.getInt("wenyuancallback"));
		   // p.setImagerUrl(rs.getString("imagerUrl")); 
		} catch (SQLException e) {  
			e.printStackTrace();
		} 
		return p;  
   }
   
}
