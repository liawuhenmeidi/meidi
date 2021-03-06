package aftersale;

import group.Group;
import group.GroupService;

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
import utill.TimeUtill;
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
		  //logger.info(sort); 
		  //logger.info(f); 
		  //sqlstr = "  1 = 1 and (mdaftersale.submitid in (select id from mduser where mduser.usertype in (select groupid from mdrelategroup where pgroupid = "+user.getUsertype()+")) or  submitid = "+ user.getId() +")";
		      
		  String prodectsp = GroupService.getidMap().get(user.getUsertype()).getProducts(); 
		  String pp = prodectsp.replace("_", ",");  
		  String sqlstr = " mdaftersale.cid in ( "+pp+" ) "; 
		   
		 if(Group.aftersalerepare == type){ 
			   if(Order.aftersale == statues){ 
				   if(UserManager.checkPermissions(user, Group.installOrderupload,"q")){
					   sql = "select * from mdaftersale,mdaftersaleproduct where  mdaftersaleproduct.asid = mdaftersale.id and mdaftersaleproduct.result = 1   and "+sqlstr+search+"  order by "+sort+str;
					   //sql = "select * from mdaftersale where  statues in (0,2)  and "+sqlstr + search+"  order by "+sort+str;
				   }else if(UserManager.checkPermissions(user, Group.installOrderupload,"w")){ 
					   sql = "select * from mdaftersale,mdaftersaleproduct where  mdaftersaleproduct.asid = mdaftersale.id and mdaftersaleproduct.result = 1  and mdaftersaleproduct.dealid = "+ user.getId() +" and "+sqlstr+search+"  order by "+sort+str;
					   //sql = "select * from mdaftersale where statues in (0,2,4)   and submitid = "+ user.getId() +" "+search+"  order by "+sort+str;
				   }         
			   }else if(Order.aftersalecharge == statues){ 
				   if(UserManager.checkPermissions(user, Group.installOrderupload,"q")){
					   sql = "select * from mdaftersale where  statues in (1)  and "+sqlstr + search+"  order by "+sort+str;
				   }else if(UserManager.checkPermissions(user, Group.installOrderupload,"w")){ 
					   sql = "select * from mdaftersale where statues in (1)  and mdaftersaleproduct.dealid = "+ user.getId() +" and "+sqlstr+search+"  order by "+sort+str;
				   }   
			   }else if(Order.aftersalesearch == statues){  
				   if(UserManager.checkPermissions(user, Group.installOrderupload,"q")){
					   sql = "select * from mdaftersale, mdaftersaleproduct  where (mdaftersale.id = mdaftersaleproduct.asid ) and "+sqlstr + search+"  order by "+sort+str;
				   }else if(UserManager.checkPermissions(user, Group.installOrderupload,"w")){
					   sql = "select * from mdaftersale, mdaftersaleproduct  where (mdaftersale.id = mdaftersaleproduct.asid  ) and mdaftersaleproduct.dealid = "+ user.getId() +" and "+sqlstr+search+"  order by "+sort+str;
				   }         
			   }else if(Order.aftersalesecond == statues){     
				       sql = "select * from mdaftersale,mdaftersaleproduct where  mdaftersaleproduct.dealid = "+ user.getId() +"  and  mdaftersaleproduct.asid = mdaftersale.id and mdaftersaleproduct.type != 3 and ( mdaftersaleproduct.dealsendid = 0  and mdaftersaleproduct.statues != 1  or  mdaftersaleproduct.statues = 2 )  and "+sqlstr+search+"  order by "+sort+str;
			   }else if(Order.aftersaledeal == statues){        
			       //sql = "select * from mdaftersale,mdaftersaleproduct where  mdaftersaleproduct.dealid = 0  and  mdaftersaleproduct.asid = mdaftersale.id and mdaftersaleproduct.dealsendid is null    "+search+"  order by "+sort+str;  
			       sql = "select * from mdaftersale,mdaftersaleproduct where   (mdaftersaleproduct.dealid = 0 or mdaftersaleproduct.statues = 1 ) and mdaftersaleproduct.type != 3 and  mdaftersaleproduct.asid = mdaftersale.id and mdaftersaleproduct.dealsendid = 0   and "+sqlstr+search+"  order by "+sort+str;
		      }else if(Order.aftersalephone == statues){         
			       //sql = "select * from mdaftersale,mdaftersaleproduct where  mdaftersaleproduct.dealid = 0  and  mdaftersaleproduct.asid = mdaftersale.id and mdaftersaleproduct.dealsendid is null    "+search+"  order by "+sort+str;
			       //sql = "select * from mdaftersale where  mdaftersale.nexttime - curdate() < 5  "+search+"  order by "+sort+str;
		    	  sql = "select * from mdaftersale where   nexttime is not null  and statues != 2 and "+sqlstr+search+"   order by nexttime "+str;   
		      }else if(Order.aftersaledealupload == statues){    
		    	  if(UserManager.checkPermissions(user, Group.sencondDealsend)){  
		    		  sql = "select * from mdaftersale,mdaftersaleproduct where  mdaftersaleproduct.asid = mdaftersale.id and mdaftersaleproduct.result = 0  and mdaftersaleproduct.dealid =  "+user.getId()+" and "+sqlstr+search+"  order by "+sort+str;
		    	  }else {
		    		  sql = "select * from mdaftersale,mdaftersaleproduct where  mdaftersaleproduct.asid = mdaftersale.id and mdaftersaleproduct.result = 0  and "+sqlstr+search+"  order by "+sort+str;
		    	  } 
		    	  
		      }else if(Order.aftersaledealcharge== statues){ 
		    	  if(UserManager.checkPermissions(user, Group.installOrderupload,"q")){ 
			    	  sql = "select * from mdaftersale,mdaftersaleproduct where  mdaftersaleproduct.asid = mdaftersale.id and mdaftersaleproduct.result = 2   and "+sqlstr+search+"  order by "+sort+str;
				   }else if(UserManager.checkPermissions(user, Group.installOrderupload,"w")){
				    	  sql = "select * from mdaftersale,mdaftersaleproduct where  mdaftersaleproduct.asid = mdaftersale.id and mdaftersaleproduct.result = 2 and  mdaftersaleproduct.dealid = "+user.getId()+"   and "+sqlstr+search+"  order by "+sort+str;
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
						AftersaleAll p = new AftersaleAll();
						p = gerAftersaleAllFromRs(p,rs); 
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
		     
		 String sql = "select * from mdaftersale,mdaftersaleproduct where  mdaftersaleproduct.dealsendid = "+user.getId()+"  and  mdaftersaleproduct.result = "+statues+"  and mdaftersaleproduct.asid = mdaftersale.id and  mdaftersaleproduct.statues != 2 "; 
	
		  logger.info(sql);  
		   Connection conn = DB.getConn();
	       Statement stmt = DB.getStatement(conn);
	       
		   ResultSet rs = DB.getResultSet(stmt, sql); 
				try { 
					while (rs.next()) {
						AftersaleAll p = new AftersaleAll();
						p = gerAftersaleAllFromRs(p,rs);
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
						AftersaleAll p = new AftersaleAll();
						p = gerAftersaleAllFromRs(p,rs);
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
		    		//logger.info(asm);
		    		if(null == asm){
		    			asm = new AftersaleAll() ; 
		    			asm.setAs(as.getAs());  
		    			asm.setAsplist(as.getAsplist()); 
		    			map.put(as.getAs().getId(), asm); 
		    		}else {   
		    			List<AfterSaleProduct> listap = asm.getAsplist();
		    			if(null != as.getAsplist()){ 
		    				listap.addAll(as.getAsplist());
			    		}
		    		}	
		    	}
		    }
	        return map ;
		 }
	 // 合并
	public static List<AftersaleAll> getAftersaleList(List<AftersaleAll> list){
		List<AftersaleAll> map = new ArrayList<AftersaleAll>();
	    
	    if(null != list){
	    	for(int i=0;i<list.size();i++){
	    		AftersaleAll as = list.get(i);
	    		boolean flag = true ;
	    		for(int j=0;j<map.size();j++){
	    			AftersaleAll asj = map.get(j);
	    			if(as.getAs().getId() == asj.getAs().getId()){
	    				asj.setAsplist(as.getAsplist());
	    				flag = false ;
	    			}
	    		}
	    		
	    		if(flag){
	    			map.add(as); 
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
							af = gerAftersaleAllFromRs(af,rs);
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
		//String sql = "update mdaftersaleproduct  set statues = 3  where asid = "+ id + " and result = 1 ; ";
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
		String sql  = "update mdaftersaleproduct  set result = 2  where id in  "+ id + " and result = 1 ; ";
		  
		DBUtill.sava(sql);    
	}   
	 
	public static void chargestatuesmatain(String id){ 
		//String sql  = "update mdaftersaleproduct  set result =3  where id in  ("+ id + ") and result = 2; ";
		String sql  = "update mdaftersaleproduct  set result =3  where asid in  ("+ id + ") and result = 2; ";
		DBUtill.sava(sql);    
	}  
	
	public static void delete(String id ){
		//List<String> sql = new ArrayList<String>();
		
		//String sql1 = "delete from mdaftersale  where id = "+ id;
		//String sql2 = "delete from mdaftersaleproduct where asid = "+ id;
		//sql.add(sql1);
		//sql.add(sql2);  
		//String sql = " update mdaftersale set statues = 2 where id ="+id ; 
		String sql = " update mdaftersale set nexttime = null  where id ="+id ; 
		DBUtill.sava(sql);  
	}  
	
	public static String getlasttime(AftersaleAll asf,int tid){
		String lasttime = asf.getAs().getAndate();
		List<AfterSaleProduct> list = asf.getAsplist();
		//logger.info(tid);  
		for(int i=0;i<list.size();i++){
			AfterSaleProduct asp = list.get(i);
			// logger.info(asp.getDealtime()); 
			//logger.info(asp.getResult());
			//logger.info(asp.getCid());
			if(asp.getTid() == tid && asp.getResult() != 0 ){
				String time = asp.getDealtime();
				//logger.info(time); 
                if(TimeUtill.compare(time, lasttime)){
        			lasttime = time ;
        		} 
			}
		}
		return lasttime ;
	}
	public static AftersaleAll gerAftersaleAllFromRs(AftersaleAll afs ,ResultSet rs){
		  try { 
		   
		    AfterSale p = AfterSaleManager.getAfterSaleFromRs(rs);
		    AfterSaleProduct pp = AfterSaleProductManager.gerAfterSaleProductFromRs(rs);
 //logger.info(StringUtill.GetJson(pp));   
		    afs.setAs(p);   
            if(null != pp){
            	List<AfterSaleProduct> list = afs.getAsplist();
            	if(null == list){
            		list = new ArrayList<AfterSaleProduct>();
            	}
            	list.add(pp); 
    			afs.setAsplist(list);
            }
			
			} catch (Exception e) {   
				e.printStackTrace();
			} 
			return afs ;  
	   }
	
	
}
