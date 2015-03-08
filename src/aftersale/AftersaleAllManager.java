package aftersale;

import group.Group;

import java.sql.Connection;
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
import user.User;
import user.UserManager;
import utill.DBUtill;
import utill.StringUtill;
import database.DB;

public class AftersaleAllManager {
	protected static Log logger = LogFactory.getLog(AftersaleAllManager.class);
	
	public static List<AftersaleAll> getOrderlist(User user ,int type,int statues ,int num,int page,String sort,String search){
		  String str = "";
		  if(num != -1){
			 str = "  limit " + ((page-1)*num)+","+num ; 
		  }
		  
		  List<AftersaleAll> AfterSales = new ArrayList<AftersaleAll>();
		  
		  String sql = "";    
		  String sqlstr = " 1 = 1 ";
		  //logger.info(f); 
		  //sqlstr = "  1 = 1 and (mdaftersale.submitid in (select id from mduser where mduser.usertype in (select groupid from mdrelategroup where pgroupid = "+user.getUsertype()+")) or  submitid = "+ user.getId() +")";
		    
		  
		 if(Group.aftersalerepare == type){ 
			   if(Order.aftersale == statues){ 
				   if(UserManager.checkPermissions(user, Group.installOrderupload,"q")){
					   sql = "select * from mdaftersale where  statues in (0,2)  and "+sqlstr + search+"  order by "+sort+str;
				   }else if(UserManager.checkPermissions(user, Group.installOrderupload,"w")){
					   sql = "select * from mdaftersale where statues in (2,4)   and submitid = "+ user.getId() +" "+search+"  order by "+sort+str;
				   }       
			   }else if(Order.aftersalecharge == statues){ 
				   if(UserManager.checkPermissions(user, Group.installOrderupload,"q")){
					   sql = "select * from mdaftersale where  statues in (1)  and "+sqlstr + search+"  order by "+sort+str;
				   }else if(UserManager.checkPermissions(user, Group.installOrderupload,"w")){
					   sql = "select * from mdaftersale where statues in (1)   and submitid = "+ user.getId() +" "+search+"  order by "+sort+str;
				   }  
			   }else if(Order.aftersalesearch == statues){  
				   if(UserManager.checkPermissions(user, Group.installOrderupload,"q")){
					   sql = "select * from mdaftersale, mdaftersaleproduct  where (mdaftersale.id = mdaftersaleproduct.asid or mdaftersale.id not in (select asid from mdaftersaleproduct)) and "+sqlstr + search+"  order by "+sort+str;
				   }else if(UserManager.checkPermissions(user, Group.installOrderupload,"w")){
					   sql = "select * from mdaftersale, mdaftersaleproduct  where mdaftersale.id = mdaftersaleproduct.asid  and submitid = "+ user.getId() +" "+search+"  order by "+sort+str;
				   }      
			   }else if(Order.aftersalesecond == statues){    
				       sql = "select * from mdaftersale,mdaftersaleproduct where  mdaftersaleproduct.dealid = "+ user.getId() +"  and  mdaftersaleproduct.asid = mdaftersale.id and ( mdaftersaleproduct.dealsendid = 0   or  mdaftersaleproduct.statues = 2 )  "+search+"  order by "+sort+str;
			   }else if(Order.aftersaledeal == statues){   
			       //sql = "select * from mdaftersale,mdaftersaleproduct where  mdaftersaleproduct.dealid = 0  and  mdaftersaleproduct.asid = mdaftersale.id and mdaftersaleproduct.dealsendid is null    "+search+"  order by "+sort+str;
			       sql = "select * from mdaftersale,mdaftersaleproduct where  ( mdaftersaleproduct.dealid = 0 or mdaftersaleproduct.statues = 1 ) and  mdaftersaleproduct.asid = mdaftersale.id and mdaftersaleproduct.dealsendid = 0   "+search+"  order by "+sort+str;
		      }else if(Order.aftersalephone == statues){   
			       //sql = "select * from mdaftersale,mdaftersaleproduct where  mdaftersaleproduct.dealid = 0  and  mdaftersaleproduct.asid = mdaftersale.id and mdaftersaleproduct.dealsendid is null    "+search+"  order by "+sort+str;
			       sql = "select * from mdaftersale where  mdaftersale.nexttime - curdate() < 5  "+search+"  order by "+sort+str;
		      }else if(Order.aftersaledealupload == statues){     
		    	  sql = "select * from mdaftersale,mdaftersaleproduct where  mdaftersaleproduct.asid = mdaftersale.id and mdaftersaleproduct.result = 1   "+search+"  order by "+sort+str;
		      }else if(Order.aftersaledealcharge== statues){    
		    	  sql = "select * from mdaftersale,mdaftersaleproduct where  mdaftersaleproduct.asid = mdaftersale.id and mdaftersaleproduct.result = 2   "+search+"  order by "+sort+str;
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
						AftersaleAll p = gerAftersaleAllFromRs(rs);
						AfterSales.add(p);
					} 
				} catch (SQLException e) { 
					e.printStackTrace();
				} finally { 
					DB.close(stmt);
					DB.close(rs);
					DB.close(conn);
				 }
				return AfterSales; 
		 }
	
	
	public static List<AftersaleAll> getOrderlistneedmaintain(User user,String statues){
		   
		  
		  List<AftersaleAll> AfterSales = new ArrayList<AftersaleAll>();
		    
		 String sql = "select * from mdaftersale,mdaftersaleproduct where  mdaftersaleproduct.dealsendid = "+user.getId()+"  and  mdaftersaleproduct.statues = "+statues+"  and mdaftersaleproduct.asid = mdaftersale.id  and mdaftersaleproduct.type = "+AfterSaleProduct.maintain;
	
		 logger.info(sql); 
		   Connection conn = DB.getConn();
	       Statement stmt = DB.getStatement(conn);
	      
		   ResultSet rs = DB.getResultSet(stmt, sql); 
				try { 
					while (rs.next()) {
						AftersaleAll p = gerAftersaleAllFromRs(rs);
						AfterSales.add(p);
					} 
				} catch (SQLException e) { 
					e.printStackTrace();
				} finally { 
					DB.close(stmt);
					DB.close(rs);
					DB.close(conn);
				 }
				return AfterSales; 
		 }
	
	public static List<AftersaleAll> getOrderlistneedfalut(User user,String statues){
		  
		   
		  List<AftersaleAll> AfterSales = new ArrayList<AftersaleAll>();
		  
		 String sql = "select * from mdaftersale,mdaftersaleproduct where  mdaftersaleproduct.dealsendid = "+user.getId()+"  and  mdaftersaleproduct.statues = "+statues+" and  mdaftersaleproduct.asid = mdaftersale.id  and mdaftersaleproduct.type = "+AfterSaleProduct.fault;
	 
		 logger.info(sql);  
		   Connection conn = DB.getConn();
	       Statement stmt = DB.getStatement(conn);
	      
		   ResultSet rs = DB.getResultSet(stmt, sql); 
				try { 
					while (rs.next()) {
						AftersaleAll p = gerAftersaleAllFromRs(rs);
						AfterSales.add(p);
					} 
				} catch (SQLException e) { 
					e.printStackTrace();
				} finally { 
					DB.close(stmt);
					DB.close(rs);
					DB.close(conn);
				 }
				return AfterSales; 
		 }
	
	public static Map<Integer,AftersaleAll> getAftersaleAllMap(List<AftersaleAll> list){
		    Map<Integer,AftersaleAll> map = new HashMap<Integer,AftersaleAll>();
		    if(null != list){
		    	for(int i=0;i<list.size();i++){
		    		AftersaleAll as = list.get(i);
		    		AftersaleAll asm = map.get(as.getAs().getId());
		    		if(null == asm){
		    			asm = as ; 
		    			map.put(as.getAs().getId(), as);
		    		}else {
		    			List<AfterSaleProduct> listap = as.getAsplist();
		    			if(null != as.getAsp()){
			    			listap.add(as.getAsp());
			    		}
		    		}
		    		
		    		
		    		
		    		
		    	}
		    }
	        return map ;
		 }
	 
	 
	 public static  AftersaleAll  getAfterSaleID(User user ,String id){
		 AftersaleAll af = new AftersaleAll();
		   
		    String sql = "select * from mdaftersale,mdaftersaleproduct where mdaftersale.id = "+id+"   and  mdaftersaleproduct.asid = mdaftersale.id ";
		    
		       Connection conn = DB.getConn();
		       Statement stmt = DB.getStatement(conn);
logger.info(sql); 		      
			   ResultSet rs = DB.getResultSet(stmt, sql); 
					try {  
						while (rs.next()) {
							af = gerAftersaleAllFromRs(rs);
						} 
					} catch (SQLException e) { 
						e.printStackTrace();
					} finally {
						DB.close(stmt);
						DB.close(rs);
						DB.close(conn);
					 }
					return af; 
			 }

	 
	
	public static void updatechager(String id ){ 
		String sql = "update mdaftersale set statues = 3 where id = "+ id;
		DBUtill.sava(sql);   
	}   
	 
	public static void updatecannotup(String id ){
		String sql = "update mdaftersale set statues = 4 where id = "+ id;
		DBUtill.sava(sql); 
	}  
	 
	public static void updatestatues(String id,String statues ){
		String sql = "update mdaftersaleproduct  set statues = "+statues +" where asid = "+ id + " and result = 0 ; ";
		DBUtill.sava(sql);   
	}  
	 
	public static void updatestatuesdeal(String id,int statues ){
		String sql = "";
		if(statues == 0){
			sql = "update mdaftersaleproduct  set statues = 0 , dealid = 0 where asid = "+ id + " and result = 0 and statues = 1  ; ";
		}else if(statues == 1){
			 sql = "update mdaftersaleproduct  set statues = 0  where asid = "+ id + " and result = 0 ; ";
		} 
		 
		DBUtill.sava(sql);    
	}  
	
	public static void updatestatuesdealsend(String id,int statues ){
		String sql = ""; 
		if(statues == 0){ 
			sql = "update mdaftersaleproduct  set statues = 0 , dealsendid = 0 where asid = "+ id + " and result = 0 and statues = 2  ; ";
		}else if(statues == 1){
			 sql = "update mdaftersaleproduct  set statues = 0  where asid = "+ id + " and result = 0 ; ";
		} 
		  
		DBUtill.sava(sql);    
	}  
	
	public static void updatestatuesmatain(String id){ 
		String sql  = "update mdaftersaleproduct  set result = 2  where asid = "+ id + " and result = 1 ; ";
		  
		DBUtill.sava(sql);    
	}  
	
	public static void chargestatuesmatain(String id){ 
		String sql  = "update mdaftersaleproduct  set result =3  where asid = "+ id + " and result = 2; ";
		  
		DBUtill.sava(sql);    
	}  
	
	public static void delete(String id ){
		String sql = "delete from mdaftersale  where id = "+ id;
		DBUtill.sava(sql); 
	}  
	
	public static AftersaleAll gerAftersaleAllFromRs(ResultSet rs){
		 AftersaleAll afs = new AftersaleAll();
		  try { 
		  
		    AfterSale p = AfterSaleManager.getAfterSaleFromRs(rs);
		    AfterSaleProduct pp = AfterSaleProductManager.gerAfterSaleProductFromRs(rs);
 //logger.info(StringUtill.GetJson(pp));   
		    afs.setAs(p); 
		    afs.setAsp(pp);
            List<AfterSaleProduct> list = null;
            if(null != pp){
            	list = new ArrayList<AfterSaleProduct>();
            	list.add(pp); 
    			afs.setAsplist(list);
            }
			
			} catch (Exception e) {   
				e.printStackTrace();
			} 
			return afs ;  
	   }
	
	
}
