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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import orderPrint.OrderPrintln;
import orderPrint.OrderPrintlnManager;
import orderproduct.OrderProductManager;
 
import database.DB;
import user.User;
import user.UserManager;
import utill.DBUtill;
import utill.NumbleUtill;
import utill.TimeUtill;

public class OrderManager {
	 protected static Log logger = LogFactory.getLog(OrderManager.class);
	 public static SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      

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
	
	public static int updateMessage(String phone1,String andate,String locations,String POS,String sailId,String check,String oid,String remark,String saledate,String diqu) {
		int flag = -1 ;  
		List<String> sqls = new ArrayList<String>();
		//insert into  mdgroup( id ,groupname, detail,statues, permissions, products) VALUES (null,?,?,?,?,?)";
		String sql = "update mdorder set phone1= "+phone1+" , andate = "+andate+" , locateDetail = "+locations+", pos = "+POS+", sailId = "+sailId+" ,checked ="+check+" , remark = "+remark+" ,saledate = "+saledate+" ,locates = "+diqu+" where id = " + oid;
        
		String sql1 = "update mdorder set posRemark = 0 where pos != " + POS ;  
		
		String sql2 = "update mdorder set checkedremark = 0 where checked !="+check ;
			
		String sql3 = "update mdorder set sailIdremark = 0 where sailId != "+sailId ;
				
		sqls.add(sql);
		
		sqls.add(sql1);
		sqls.add(sql2);
		sqls.add(sql3);
		
		DBUtill.sava(sqls);
		return flag ;
	}  
	
	public static int updateMessage(String phone1,String andate,String locations,String oid,String remark) {
		int flag = -1 ; 
		Connection conn = DB.getConn(); 
		//insert into  mdgroup( id ,groupname, detail,statues, permissions, products) VALUES (null,?,?,?,?,?)";
		String sql = "update mdorder set phone1= ? , andate = ?, remark = ? where id = " + oid;
		PreparedStatement pstmt = DB.prepare(conn, sql);
		try {   
			pstmt.setString(1,phone1);    
			pstmt.setString(2,andate);   
			pstmt.setString(3,remark);  
			
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
	
	
	public static String getDeliveryStatues(Order o){
		int statues = o.getDeliveryStatues();
		String str = "";
		String remark = "";
		if(o.getOderStatus().equals(20+"")){
			remark = "换货单";
		   }
		// 0 表示未送货  1 表示正在送  2 送货成功
		 if(0 == statues){
		   if(o.getOderStatus().equals(20+"")){
			   str = "换货单";
		   }else {
			   str = "需派送";
		   }
          }else if(1 == statues){
        	  str = "已送货"+remark;
		
          }else if(2 == statues){
		      
        	  str = "已安装"+remark;
		
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
	
	public static String getOrderStatues(Order o){
		int statues = Integer.valueOf(o.getOderStatus());
		String str = "";
		// 0 表示未送货  1 表示正在送  2 送货成功
		 if(0 == statues){
		    str = "需配送";
          }else if( 8 == statues){ 
        	  str = "已自提 ";
          }else if(9 == statues){
        	  str = "只安装(门店提货)";
          }else if(10 == statues){
        		str = "只安装(顾客已提) ";
          }else if(20 == statues){
        	   str = "换货单";
          }
		return str ;
		
		
	}
	
	
	public static Map<String,String> getDeliveryStatuesMap(){
		Map<String,String> map = new HashMap<String,String>();
		map.put(0+"", "需配送安装");
		map.put(1+"", "已送货");
		map.put(8+"", "已自提 ");
		map.put(9+"", "只安装(门店提货)");
		map.put(10+"", "只安装(顾客已提) ");
		map.put(-1+"", "调拨单"); 
		map.put(20+"", "换货单"); 
		
		return map ;
		
		
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
					if(o.getType() == OrderPrintln.modify &&  o.getStatues() != 4 && !"tuihuo".equals(method) && !"print4".equals(method) && !"orderCome".equals(method) && !"orderGo".equals(method)&& !"orderCharge".equals(method) && !"orderover".equals(method) && !"statuescallback".equals(method)){
						//logger.info(1); 
						return OrderPrintln.modify;
					}else if(o.getType() == OrderPrintln.returns && o.getStatues() != 4 && !"tuihuo".equals(method) && !"print4".equals(method) && !"orderCome".equals(method) && !"orderGo".equals(method)&& !"orderCharge".equals(method) && !"orderover".equals(method) && !"statuescallback".equals(method)){
						//logger.info(1);   
						return 20 ;     
					}else if(o.getType() == OrderPrintln.unmodify && !"print4".equals(method) && !"orderCome".equals(method) && !"orderGo".equals(method)&& !"orderCharge".equals(method)  && !"orderover".equals(method) && !"statuescallback".equals(method)){
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
				sql = "update mdorder set statues4 = "+statues+"  , chargeDealsendtime = "+TimeUtill.gettime()+" where id in " + ids;
			} else if("songhuo".equals(method)){
				boolean flags = false ;
				if(NumbleUtill.isNumeric(id)){
					Order order = OrderManager.getOrderID(user, Integer.valueOf(id));
				    if((2 == statues || 1 == statues ) && order.getOderStatus().equals(20+"")){
				    	flags = true ; 
				    	String sql1 = " delete from mdorderupdateprint where orderid = "+ order.getImagerUrl();
				    	listsql.add(sql1);
				    }
				}
				
				if(2 == statues){
					sql = "update mdorder set deliveryStatues = "+statues+" , deliverytype = 1 , sendTime = '"+time+"' , installTime = '"+time+"' , installid = mdorder.sendId   where id in " + ids;
				}else if(1 == statues) {  
					sql = "update mdorder set deliveryStatues = "+statues+"  , deliverytype = 2 ,  printSatuesp = 0 , sendTime = '"+time+"'  where id in " + ids;
				}else if( 4 == statues ||  9 == statues || 10 == statues){
					statues = 2 ;    
					sql = "update mdorder set deliveryStatues = "+statues+"  , deliverytype = 2 , installTime = '"+time+"'  where id in " + ids;
				} 
				
				if(flags){
					 List<String> lists = InventoryBranchManager.chage(user, method, statues, id);
				     listsql.addAll(lists); 
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
				sql = "update mdorder set statuespaigong = "+statues+"  , chargeSendtime = "+TimeUtill.gettime()+" where id in " + ids;
			} else if("statuesinstall".equals(method)){  
				sql = "update mdorder set statuesinstall = "+statues+"  , chargeInstalltime = "+TimeUtill.gettime()+" where id in " + ids;
			} else if("statuesinstalled".equals(method)){
				statues = 2 ;
				sql = "update mdorder set statuesinstall = "+statues+" , chargeInstalltime = "+TimeUtill.gettime()+"  where id in " + ids;
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
		Order or = OrderManager.getOrderID(user, id);
		String sql = "";
		if(type == OrderPrintln.release){               
			sql = "update mdorder set dealSendid = 0  , printSatues = 0  where id = " + id;
			  List<String> lists = InventoryBranchManager.chage(user,"shifang", uid, id+"");    
		      listsql.addAll(lists);    
		}else if(type == OrderPrintln.salerelease){        
			sql = "update mdorder set sendId = 0, printSatuesp = 0  where id = " + id;
			  List<String> lists = InventoryBranchManager.chage(user,"salereleasesonghuo", user.getId(), id+"");    
		      listsql.addAll(lists); 
		}else if(type == OrderPrintln.salereleasereturn){        
			sql = "update mdorder set returnid = 0, printSatuesp = 0  where id = " + id;
			  List<String> lists = InventoryBranchManager.chage(user,"salereleasereturn", user.getId(), id+"");    
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
			if(or.getOderStatus().equals(20+"")){
				String sql1 = " delete from mdorderupdateprint where orderid = "+ or.getImagerUrl()+ " and statues = 10 ";
	            listsql.add(sql1);
			}
            
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

	// 第二次配单 
		public static int updatePeisong(User user,int uid,int id,int type) {
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
				sql = "update mdorder set sendId = "+uid+"  , printSatuesp= 1  where id = " + id ;
				  List<String> lists = InventoryBranchManager.chage(user,type+"", uid, id+""); 
			      listsql.addAll(lists);      
			}else if(Order.orderinstall == type){ 
				sql = "update mdorder set installid = "+uid+" , printSatuesp= 1  where id = " + id ;
			} else if(Order.orderreturn == type){  
				sql = "update mdorder set returnid = "+uid+" , returnprintstatues = 1  where id = " + id ;
				List<String> lists = InventoryBranchManager.chage(user, type+"", uid, id+"");
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
			
	   order.setOderStatus(oldorder.getOderStatus());		 
	   List<String> listd =  OrderManager.update(maxid); 
			  
	   sqls.addAll(listd);
			 
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
		 }else if(9<daymark && daymark <100){
			 daymarkk = "0"+daymark; 
		 }
		 
		 
		if(maxid == 0){ 
			maxid = 1 ;
		}    
	
		
		List<String> sqlp = OrderProductManager.save(maxid, order);
	    List<String> sqlg = GiftManager.save(maxid, order);
		 
	     
	     sqls.addAll(sqlp);
	     sqls.addAll(sqlg);
	     
	     String printlnid = "";
	     if(order.getOderStatus().equals(20+"")){ 
	    	 String sql1 = "insert into  mdorderupdateprint (id, message ,statues , orderid,mdtype ,pGroupId)" +
                     "  values ( null, '换货申请', 0,"+order.getImagerUrl()+","+OrderPrintln.huanhuo+","+user.getUsertype()+")";
	    	 
	    	 sqls.add(sql1);
	    	 
	    	 printlnid = "H"+order.getPrintlnid();
	     }else {
	    	 printlnid = order.getPrintlnid()+"-"+daymarkk;
	     }
	    String sql = "insert into  mdorder ( id ,andate , saledate ,pos, username, locates" +
				", locateDetail, saleID , printSatues ,oderStatus,sailId,checked,phone1,phone2,remark,"+
	    		"deliveryStatues,orderbranch,sendId,statues1,statues2,statues3,dealSendid,submittime,printlnid,dayremark,dayID,phoneRemark,sailIdremark,checkedremark,posRemark,imagerUrl) values "+  
				"( "+maxid+", '"+order.getOdate()+"', '"+order.getSaleTime()+"', '"+order.getPos()+"', '"+order.getUsername()+"', '" 
	    		+order.getLocate()+"', '"+order.getLocateDetail()+"',"+order.getSaleID()+", "+order.getPrintSatues()    
	    		+", "+order.getOderStatus()+", '"+order.getSailId()+"', '"+order.getCheck()+"', '"+order.getPhone1()+"','"+order.getPhone2()+"','"+order.getRemark()+"',"+order.getDeliveryStatues()+",'"+order.getBranch()+"',0,0,0,0,"+order.getDealsendId()+",'"+order.getSubmitTime()+"','"+printlnid+"',"+daymark+","+dayID+","+order.getPhoneRemark()+","+order.getSailidrecked()+","+order.getReckedremark()+","+order.getPosremark()+","+order.getImagerUrl()+")";   
	   
	    sqls.add(sql);
	    logger.info(sql);       
	  
	    flag  = DBUtill.sava(sqls);
        
		return flag ; 

	}
    
  
    public static List<Order> getOrderlist(User user ,int type,int statues ,int num,int page,String sort,String search){
	   
	  boolean f = UserManager.checkPermissions(user, Group.Manger);  
	   
	  boolean flag = UserManager.checkPermissions(user, type);
		  
	  String str = "";
	  if(num != -1){
		 str = "  limit " + ((page-1)*num)+","+num ; 
	  }
	  
	  List<Order> Orders = new ArrayList<Order>();
	  
	  String sql = "";    
	  
	  //logger.info(f); 
	  if(f){  
	     if(Group.send == type){
			   if(Order.serach == statues){
				   sql = "select * from  mdorder where  deliveryStatues in (0,9,10)   and printSatuesp = 1  or  installid = "+user.getId() + " and deliveryStatues in (1,10,9)  and printSatuesp = 1    order by id  desc";
			   } 
		   }else if(Group.sale == type){
			   if(Order.serach == statues){
				   sql = "select * from  mdorder where  1 =1 "+ str + " order by id desc ";
			   }
		   }else if(Group.dealSend == type){ 
			  if(Order.orderDispatching == statues){    
				 // sql = "select * from  mdorder  where  (dealSendid = 0   and sendId = 0 and printSatues = 0  and deliveryStatues not in (3,4,5)  and  ( mdorder.id in (select orderid from mdorderproduct where salestatues in (0,1,2,3)))  or id in (select orderid from mdorderupdateprint where statues = 0 and mdtype in (0 ,1,2,4) ))  "+search+"  order by  "+sort+"  limit " + ((page-1)*num)+","+ page*num ;  
				  sql = "select * from  mdorder  where  (dealSendid = 0   and sendId = 0 and printSatues = 0  and deliveryStatues not in (3,4,5,11,12,13) or id in (select orderid from mdorderupdateprint where statues = 0 and mdtype in (0 ,1,2,4,10) ))  "+search+"  order by  "+sort+str ;  
			  }else if(Order.neworder == statues){
				  sql = "select * from  mdorder  where  dealSendid = 0   and sendId = 0 and printSatues = 0  and deliveryStatues != 3  and mdorder.id in (select orderid from mdorderproduct where salestatues in (0,1,2,3))   "+search+"  order by "+sort+" ";   
			  }else if(Order.motify == statues){
				  sql = "select * from  mdorder  where  id in (select orderid from mdorderupdateprint where statues = 0 and mdtype = 0 )   "+search+"  order by "+sort;   
			  }else if(Order.returns == statues){ 
				  sql = "select * from  mdorder  where  id in (select orderid from mdorderupdateprint where statues = 0 and mdtype = 1 )   "+search+"  order by "+sort;   
			  }else if(Order.release == statues){
				  sql = "select * from  mdorder  where  id in (select orderid from mdorderupdateprint where statues = 0 and mdtype = 2  )   "+search+"  order by "+sort;   
			  }else if(Order.orderPrint == statues){   
				  sql = "select * from  mdorder  where  dealSendid != 0   and sendId = 0 and printSatues = 0  and deliveryStatues != 3    "+search+"  order by "+sort+str ;  
			  }else if(Order.serach == statues){ 
				  sql = "select * from  mdorder  where 1 =1 "+search+" order by "+sort ;
			  }else if(Order.charge == statues){ 
				  sql = "select * from  mdorder  where statues1 = 1 and statues2 = 1 and statues3 = 0  "+search+" order by "+sort+str ;
			  }else if(Order.callback == statues){ 
				   sql = "select * from  mdorder where  deliveryStatues in (2)  and wenyuancallback = 0 "+search+" order by "+sort+str;
			   }else if(Order.come == statues){
				  sql = "select * from  mdorder  where  statues1 = 0  "+search+" order by "+sort+str ;
			  }else if(Order.go == statues){ 
				  sql = "select * from  mdorder  where  statues1 = 1 and statues2 = 0  "+search+" order by "+sort+str ;
			  }else if(Order.over == statues){  
				  sql = "select * from  mdorder  where dealSendid != 0   and printSatues = 1  and deliveryStatues not in (0,3)  and statues4 = 0  "+search+"  order by "+sort+str;
			  }else if(Order.dingma == statues){ 
				  sql = "select * from  mdorder  where  id  in (select orderid from mdorderproduct where statues = 1 ) and statuesdingma = 0  "+search+"  order by "+sort+str ;
			  }else if(Order.deliveryStatuesTuihuo == statues){ 
				   sql = "select * from  mdorder where  deliveryStatues in (3,4,5,11,12,13)  "+search+"  order by  "+sort+str;
			   }         
		  }else if(Group.sencondDealsend == type){ 
				   if(Order.orderDispatching == statues){  
					   sql = "select * from  mdorder where  ( dealSendid != 0   and printSatues = 1   and  printSatuesp = 0 and sendId = 0  and  deliveryStatues in (0,9)  and mdorder.id not in (select orderid from mdorderupdateprint where statues = 2 and mdtype = 6 )  or (mdorder.id in (select orderid from mdorderupdateprint where mdtype in (3,6,7) and pGroupId = "+ user.getUsertype()+ " and statues = 0 )) ) "+search+"  order by "+sort+str; 
				   }else if(Order.dispatch == statues){    
					   sql = "select * from  mdorder where   dealSendid != 0   and printSatues = 1   and  printSatuesp = 0 and sendId = 0  and  deliveryStatues in (0,9) and mdorder.id not in (select orderid from mdorderupdateprint where statues = 2 and mdtype = 6 )  "+search+"  order by "+sort+str; 
				   }else if(Order.release == statues){   
					   sql = "select * from  mdorder where  mdorder.id  in (select orderid from mdorderupdateprint where mdtype in (3,4,5) and pGroupId = "+ user.getUsertype()+ " )      "+search+"  order by "+sort+str; 
				   }else if(Order.porderDispatching == statues){ 
					   sql = "select * from  mdorder where  dealSendid != 0   and printSatues = 1    and  installid = 0 and  deliveryStatues in (1,10)  and statuesinstall = 0  and returnid = 0  or (mdorder.id in (select orderid from mdorderupdateprint where mdtype in (3,4,5) and pGroupId = "+ user.getUsertype()+ " and statues = 0 ))  "+search+" order by "+sort+str; 
				   }else if(Order.installonly  == statues){ 
					   sql = "select * from  mdorder where  dealSendid != 0   and printSatues = 1    and  installid = 0 and  deliveryStatues in (1,10)  and statuesinstall = 0  "+search+" order by "+sort+str; 
				   }else if(Order.orderPrint == statues){ 
					   sql = "select * from  mdorder where  sendId != 0  and  deliveryStatues = 0  and printSatuesp = 0  or  returnid != 0 and  returnprintstatues = 0  "+search+" order by "+sort+str;
				   }else if(Order.porderPrint == statues){
					   sql = "select * from  mdorder where  installid != 0 and  deliveryStatues = 1   and printSatuesp = 0  and returnstatues = 0 "+search+"  order by "+sort+str;
				   }else if(Order.serach == statues){   
					   sql = "select * from  mdorder where  dealSendid != 0  and printSatues = 1  "+search+"  order by "+sort+str;
				   }else if(Order.callback == statues){ 
					   sql = "select * from  mdorder where  deliveryStatues in (2)  and statuescallback = 0 "+search+" order by "+sort+str;
				   }else if(Order.charge== statues){    
					   sql = "select * from  mdorder where  deliveryStatues in (2,5) and  statuesinstall = 0   and statuescallback = 1  and deliverytype = 2  and  statuesinstall = 0 "+search+" order by "+sort+str; 
				   }else if(Order.chargeall== statues){   
					   sql = "select * from  mdorder where  deliveryStatues in (2,5) and  statuesinstall = 0   and statuescallback = 1   and deliverytype = 1  and statuesinstall  = 0 "+search+" order by "+sort+str; 
				   }else if(Order.pcharge == statues){
					   sql = "select * from  mdorder where  deliverytype = 2  and  deliveryStatues in (1,2,4,5)  and statuespaigong  = 0  "+search+" order by "+sort+str; 
				   }else if(Order.orderquery == statues){ 
					   sql = "select * from  mdorder where  ( deliveryStatues in (0,9,10) and sendid != 0  or  installid  != 0  and deliveryStatues in (1,10,9)  or returnid != 0  and returnstatues =0 ) and printSatuesp = 1   order by " + sort+str;
					   //returnid = "+user.getId() + " and returnstatues =0  and returnprintstatues = 1 
				   }            
		     } 
		 }else{        
			   if(flag && Group.send == type){  
				   if(Order.serach == statues){ // 待送货
					   sql = "select * from  mdorder where  sendId = "+user.getId() + " and deliveryStatues in (0,9)   and printSatuesp = 1  or  installid = "+user.getId() + " and deliveryStatues in (1,10)  and printSatuesp = 1   order by id  desc";
				   }else if(Order.orderDispatching == statues){   // 待安装
					   sql = "select * from  mdorder where  installid = "+user.getId() + " and deliveryStatues in (1,10)  order by id  desc"; 
				   }else if(Order.over == statues){  // 已送货
					   sql = "select * from  mdorder where  ( sendId = "+user.getId() + " )  order by id  desc";
				   }else if(Order.returns == statues){ // 已安装
					   sql = "select * from  mdorder where  (sendId = "+user.getId() + " and installid = 0  or  installid = "+user.getId() + " )  and deliveryStatues in (2,5)    order by id  desc";
				   }
			   }else if(flag && Group.sale == type){
				   if(Order.serach == statues){
					   sql = "select * from  mdorder where  orderbranch = '"+  user.getBranch() +"' and deliveryStatues in (0,9,10) "+ str + " order by id desc ";
				   }else if(Order.orderDispatching == statues){ 
					   sql = "select * from  mdorder where  orderbranch = '"+  user.getBranch() +"' and deliveryStatues in (1)  "+ str + " order by id desc ";
				   }else if(Order.over == statues){
					   sql = "select * from  mdorder where  orderbranch = '"+  user.getBranch() +"' and deliveryStatues in (2)  "+ str + " order by id desc ";
				   }else if(Order.returns == statues){
					   sql = "select * from  mdorder where  orderbranch = '"+  user.getBranch() +"' and deliveryStatues in (3,4,5,11,12,13)  "+ str + " order by id desc ";
				   }else if(Order.come == statues){
					   sql = "select * from  mdorder where  orderbranch = '"+  user.getBranch() +"' and deliveryStatues in (8)  "+ str + " order by id desc "; 
				   } 
			   }else if(flag && Group.tuihuo == type){
				   if(Order.unquery == statues){  // 
					   sql = "select * from  mdorder where  (returnid = "+user.getId() + " ) and returnstatues = 0    order by id  desc";
				   }else {
					   sql = "select * from  mdorder where  (returnid = "+user.getId() + " ) and returnstatues = 2    order by id  desc";
				   }
			   }else if(flag && Group.dealSend == type){
				   if(Order.orderDispatching == statues){     
					   sql = "select * from mdorder where   ( mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  and  dealSendid = 0   and sendId = 0  and printSatues = 0 and deliveryStatues != 3  and ( mdorder.id in (select orderid from mdorderproduct where salestatues in (0,1,2,3)))  or id in (select orderid from mdorderupdateprint where   pGroupId= '"+user.getUsertype()+"'  and statues = 0  and mdtype in (0 ,1,2,4,10) ))  "+search+" order by  "+sort+str;  
				   }else if(Order.neworder == statues){    
					   sql = "select * from mdorder where   ( mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+")) ) and  dealSendid = 0   and printSatues = 0 and deliveryStatues != 3   and mdorder.id in (select orderid from mdorderproduct where salestatues in (0,1,2,3))  "+search+" order by  "+sort+str ;
				   }else if(Order.motify == statues){     
					   sql = "select * from mdorder where   ( mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+")) )  and   id in (select orderid from mdorderupdateprint where   pGroupId= "+user.getUsertype()+"  and statues = 0  and mdtype = 0 )   "+search+" order by  "+sort+str;
				   }else if(Order.returns == statues){    
					   sql = "select * from mdorder where   ( mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+")) )  and   id in (select orderid from mdorderupdateprint where   pGroupId= "+user.getUsertype()+"  and statues = 0  and mdtype = 1 )   "+search+" order by  "+sort+str ;
				   }else if(Order.release == statues){     
					   sql = "select * from mdorder where   ( mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+")) ) and   id in (select orderid from mdorderupdateprint where   pGroupId= "+user.getUsertype()+"  and statues = 0  and mdtype = 2  )   "+search+" order by  "+sort+str ;
				   }else if(Order.orderPrint == statues){   
					   sql = "select * from mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  and  dealSendid != 0  and printSatues = 0  and sendId = 0  and  deliveryStatues = 0  "+search+"  order by "+sort+str ;  
				   }else if(Order.charge == statues){ 
					   sql = "select * from mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  and statues1 = 1 and statues2 = 1 and statues3 = 0  "+search+"  order by "+sort+str ;  
				   }else if(Order.callback == statues){ 
					   sql = "select * from mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))   and deliveryStatues in (2)   and wenyuancallback = 0  "+search+" order by "+sort+str;  
				   }else if(Order.come == statues){
					   sql = "select * from mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))    and statues1 = 0 "+search+" order by "+sort+str ;  
				   }else if(Order.go == statues){ 
					   sql = "select * from mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))    and statues1 = 1 and statues2 = 0   "+search+" order by "+sort+str ;  
				   }else if(Order.over == statues){  
					   sql = "select * from  mdorder where  mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))   and printSatues = 1 and sendId != 0  and deliveryStatues not in (0,3)  and statues4 = 0  "+search+" order by "+sort+str; 
				   }else if(Order.serach == statues){    
						  sql = "select * from  mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  "+search+"  order by "+sort+str; 
						  //sql = "select * from  mdorder where mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  "+search+"  or (mdorder.id in (select orderid from mdorderupdateprint where mdtype = 3 and pGroupId = "+ user.getUsertype()+ " ))  order by "+sort+"  desc limit " + ((page-1)*num)+","+ page*num; 
				   }else if(Order.dingma == statues){ 
						  sql = "select * from  mdorder  where  mdorder.saleID in  (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  and id  in (select orderid from mdorderproduct where statues = 1 )  and statuesdingma = 0 "+search+"  order by "+sort+str;
				   }else if(Order.deliveryStatuesTuihuo == statues){
						   sql = "select * from  mdorder where  deliveryStatues in (3,4,5,11,12,13) "+search+"  order by "+sort+str;
					}   // dispatch 
			   }else if(Group.sencondDealsend == type){   
				   if(Order.orderDispatching == statues){  
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+"  and  ( printSatues = 1 and printSatuesp = 0  and sendId = 0  and  deliveryStatues in (0,9) and mdorder.id not in (select orderid from mdorderupdateprint where statues = 2 and mdtype = 6 )  or (mdorder.id in (select orderid from mdorderupdateprint where mdtype in (3,6,7,8)  and statues = 0 )) )  "+search+" order by "+sort+str; 
				   }else if(Order.release == statues){     
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+"  and  mdorder.id in (select orderid from mdorderupdateprint where mdtype in (3,4,5) and pGroupId = "+ user.getUsertype()+ " )  "+search+" order by "+sort+  str; 
				   }else if(Order.dispatch == statues){ 
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+"  and printSatues = 1 and printSatuesp = 0  and sendId = 0  and  deliveryStatues in (0,9)  and mdorder.id not in (select orderid from mdorderupdateprint where statues = 2 and mdtype = 6 )  "+search+" order by "+sort+str; 
				   }else if(Order.porderDispatching == statues){ 
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+"  and ( printSatues = 1 and printSatuesp = 0    and installid = 0 and  deliveryStatues in (1,10)  and statuesinstall = 0  and returnid = 0  or (mdorder.id in (select orderid from mdorderupdateprint where mdtype in (3,4,5) and pGroupId = "+ user.getUsertype()+ "  and statues = 0 )) )"  + " order by "+sort+str; 
				   }else if(Order.installonly == statues){ 
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+"  and printSatues = 1 and printSatuesp = 0    and installid = 0 and  deliveryStatues in (1,10)  and statuesinstall = 0  "  + " order by "+sort+str; 
				   }else if(Order.orderPrint == statues){  
					   sql = "select * from  mdorder where  (sendId != 0  and printSatuesp = 0   and  deliveryStatues = 0  or  returnid != 0 and  returnprintstatues = 0 ) and  dealSendid = "+user.getId()+"  "+search+"  order by "+sort+str;
				   }else if(Order.porderPrint == statues){ 
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+"  and  installid != 0 and  deliveryStatues = 1  and printSatuesp = 0  and returnstatues = 0 "+search+"  order by "+sort+str;
				   }else if(Order.serach == statues){   
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+ "  and printSatues = 1 "+search+"  order by "+sort+str;
				   }else if(Order.callback == statues){ 
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+" and  deliveryStatues in (2,5)  and statuescallback = 0 "+search+" order by "+sort+  str; 
				   }else if(Order.charge == statues){ //and deliverytype = 1  
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+" and   deliveryStatues in (2,5)  and statuescallback = 1  and deliverytype = 2  and  statuesinstall = 0  "+search+"  order by "+sort+str; 
				   }else if(Order.chargeall == statues){ // 
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+" and   deliveryStatues in (2,5)  and statuescallback = 1  and deliverytype = 1  and statuesinstall  = 0   "+search+"  order by "+sort+str; 
				   }else if(Order.pcharge == statues){ 
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+" and   deliveryStatues in (1,2,4,5)  and deliverytype = 2  and statuespaigong  = 0  "+search+"  order by "+sort+str ; 
				   }else if(Order.orderquery == statues){  
					   sql = "select * from  mdorder where  dealSendid = "+user.getId()+"  and ( deliveryStatues in (0,9,10)   and sendid != 0  or  installid != 0  and deliveryStatues in (1,10,9)  or returnid != 0  and returnstatues =0  )      "+search+"  order by "+sort+str;    
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
					   sql = "select count(*) from  mdorder where  ( deliveryStatues in (0,9,10)   and sendid != 0   or  installid != 0 and deliveryStatues in (1,10,9) or returnid != 0  and returnstatues =0 ) and printSatuesp = 1  ";
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
				   }else if(Order.huanhuo == statues){    
					   sql = "select count(*) from mdorder where   ( mdorder.saleID in (select id from mduser where mduser.usertype in (select id from mdgroup where pid = "+user.getUsertype()+"))  and   id in (select orderid from mdorderupdateprint where   pGroupId= "+user.getUsertype()+"  and statues = 0  and mdtype = 10 ) )  "+search ; 
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
					   sql = "select count(*) from  mdorder where  dealSendid = "+user.getId()+"  and (deliveryStatues in (0,9,10)   and sendid != 0  or  installid != 0  and deliveryStatues in (1,10,9)  or returnid != 0  and returnstatues =0  )  and printSatuesp = 1    "+search ; 
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
    //未结款的Order
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
    
    //wrote by wilsonlee
    //已经结款的Order
    public static List<Order> getCheckedDBOrders(){
    	//boolean flag = UserManager.checkPermissions(user, Group.dealSend); 
    	//flag = true;
    	List<Order> Orders = new ArrayList<Order>();
   
    	String sql = "select * from  mdorder  where statues1 = 1 and statues2 = 1 and statues3 = 1";                  
    	   
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
    
  //wrote by 
    //根据门店获取Order
    public static List<Order> getCheckedDBOrdersbyBranch(String branchid){
    	//boolean flag = UserManager.checkPermissions(user, Group.dealSend); 
    	//flag = true;
    	List<Order> Orders = new ArrayList<Order>();
   
    	String sql = "select * from  mdorder  where statues1 = 1 and statues2 = 1 and statues3 = 0 and orderbranch in ("+branchid+")";                  
    	   
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
    
  //wrote by 
    //根据门店类别获取Order
    public static List<Order> getCheckedDBOrdersbyBranchType(String branchid){
    	//boolean flag = UserManager.checkPermissions(user, Group.dealSend); 
    	//flag = true;
    	List<Order> Orders = new ArrayList<Order>();
   
    	String sql = "select * from  mdorder  where statues1 = 1 and statues2 = 1 and statues3 = 0 and orderbranch in (select id from mdbranch where pid in ( "+branchid+"))";                  
    	   
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
    
   
   
   public static boolean delete(User user ,int oid) {
	   
	    boolean flag = false;
	     
	    Order order = OrderManager.getOrderID(user, oid);
	    List<String> listsqls = new ArrayList<String>();
 
		String sqlp = OrderProductManager.delete(order.getId());
        String sqlg = GiftManager.delete(order.getId()) ;  
        String sqlop =  OrderPrintlnManager.deleteByoid(order.getId());
         
        String sql = "delete from mdorder where id = " + order.getId();
        listsqls.add(sqlp); 
        listsqls.add(sqlg); 
        listsqls.add(sqlop);
        listsqls.add(sql); 
		 
        if(order.getOderStatus().equals(20+"")){
        	String sql1 = " delete from mdorderupdateprint where orderid = "+ order.getImagerUrl();
            listsqls.add(sql1);
        }
		if (listsqls.size() == 0) {  
            return false;   
        }        
       
        flag = DBUtill.sava(listsqls);
		return flag ;
	}
     
   
   public static List<String> update(int id) {
	
	    List<String> listsqls = new ArrayList<String>();

       String sqlop =  OrderPrintlnManager.deleteByoid(id);
        
       String sql = "delete from mdorder where id = " + id;

       listsqls.add(sqlop);
       listsqls.add(sql); 
		return listsqls ;
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
   
   public static int getShifangStatues(Order or){
	   
	   int opstatues = -1;  
	   
	   if(or.getDeliveryStatues() == 0 || or.getDeliveryStatues() == 9 ){ 
			opstatues = OrderPrintln.salerelease;     
		}else if (or.getDeliveryStatues() == 1 || or.getDeliveryStatues() == 10){
			if(or.getInstallid() != 0){  
				opstatues = OrderPrintln.salereleaseanzhuang;
			}
			if(or.getReturnid() != 0){ 
				opstatues = OrderPrintln.salereleasereturn ;
			}
		}else if(or.getDeliveryStatues() == 2 ){  
			if(or.getReturnid() != 0){ 
				opstatues = OrderPrintln.salereleasereturn ;
			}
		} 

	   return opstatues;
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
			p.setDeliveryStatues(rs.getInt("deliveryStatues"));
			p.setPos(rs.getString("pos"));
			p.setSailId(rs.getString("sailId"));
			p.setCheck(rs.getString("checked"));
			p.setRemark(rs.getString("remark"));
			p.setBranch(rs.getInt("orderbranch")); 
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
		    p.setSubmitTime(rs.getString("submitTime"));
		    p.setReturnstatuse(rs.getInt("returnstatues"));
		    p.setReturntime(rs.getString("returntime"));  
		    p.setReturnprintstatues(rs.getInt("returnprintstatues")); 
		    p.setReturnwenyuan(rs.getInt("returnwenyuan")); 
		    p.setPrintdingma(rs.getInt("printdingma"));  
		    p.setDealSendTime(rs.getString("dealsendTime"));
		    p.setWenyuancallback(rs.getInt("wenyuancallback"));
		    p.setOderStatus(rs.getString("oderStatus"));
		    p.setImagerUrl(rs.getString("imagerUrl")); 
		} catch (SQLException e) {  
			e.printStackTrace();
		} 
		return p;  
   }
   
}
