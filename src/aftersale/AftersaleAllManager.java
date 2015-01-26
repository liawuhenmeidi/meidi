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
		  
		  //logger.info(f); 
		  String sqlstr = " 1 = 1 and (mdaftersale.submitid in (select id from mduser where mduser.usertype in (select groupid from mdrelategroup where pgroupid = "+user.getUsertype()+")) or  submitid = "+ user.getId() +")";
		  
		  
		 if(Group.aftersalerepare == type){ 
			   if(Order.aftersale == statues){
				   if(UserManager.checkPermissions(user, Group.installOrderupload,"q")){
					   sql = "select * from mdaftersale where  statues = 0  and "+sqlstr + search+"  order by "+sort+str;
				   }else if(UserManager.checkPermissions(user, Group.installOrderupload,"w")){
					   sql = "select * from mdaftersale where statues = 0  and submitid = "+ user.getId() +" "+search+"  order by "+sort+str;
				   } 
			   }else if(Order.aftersalesearch == statues){
				   if(UserManager.checkPermissions(user, Group.installOrderupload,"q")){
					   sql = "select * from mdaftersale where   "+sqlstr + search+"  order by "+sort+str;
				   }else if(UserManager.checkPermissions(user, Group.installOrderupload,"w")){
					   sql = "select * from mdaftersale where 1 =1  and submitid = "+ user.getId() +" "+search+"  order by "+sort+str;
				   }    
			   }else if(Order.aftersalesecond == statues){  
				       sql = "select * from mdaftersale,mdaftersaleproduct where  mdaftersaleproduct.dealid = "+ user.getId() +"  and  mdaftersaleproduct.asid = mdaftersale.id and mdaftersaleproduct.dealsendid is null  "+search+"  order by "+sort+str;
			   }else if(Order.aftersaledeal == statues){   
			       sql = "select * from mdaftersale,mdaftersaleproduct where  mdaftersaleproduct.dealid = 0  and  mdaftersaleproduct.asid = mdaftersale.id and mdaftersaleproduct.dealsendid is null  "+search+"  order by "+sort+str;
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
	
	
	public static List<AftersaleAll> getOrderlistneedmaintain(User user){
		  
		  
		  List<AftersaleAll> AfterSales = new ArrayList<AftersaleAll>();
		  
		 String sql = "select * from mdaftersale,mdaftersaleproduct where  mdaftersaleproduct.dealsendid = "+user.getId()+"  and  mdaftersaleproduct.asid = mdaftersale.id  and mdaftersaleproduct.type = "+AfterSaleProduct.maintain;
	
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
	
	public static List<AftersaleAll> getOrderlistneedfalut(User user){
		  
		  
		  List<AftersaleAll> AfterSales = new ArrayList<AftersaleAll>();
		  
		 String sql = "select * from mdaftersale,mdaftersaleproduct where  mdaftersaleproduct.dealsendid = "+user.getId()+"  and  mdaftersaleproduct.asid = mdaftersale.id  and mdaftersaleproduct.type = "+AfterSaleProduct.fault;
	 
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
		    		}
		    		
		    		List<AfterSaleProduct> listap = as.getAsplist();
		    		if(null == listap){
		    			if(null != as.getAsp()){
		    				listap = new ArrayList<AfterSaleProduct>();
			    			as.setAsplist(listap);
		    			}
		    			
		    		}
		    		
		    		if(null != as.getAsp()){
		    			listap.add(as.getAsp());
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

	 
	
	
	public static AftersaleAll gerAftersaleAllFromRs(ResultSet rs){
		 AftersaleAll afs = new AftersaleAll();
		  try { 
		  
		    AfterSale p = AfterSaleManager.getAfterSaleFromRs(rs);
		    AfterSaleProduct pp = AfterSaleProductManager.gerAfterSaleProductFromRs(rs);
 
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
