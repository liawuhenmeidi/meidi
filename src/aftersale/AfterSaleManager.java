package aftersale;

import group.Group;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import order.Order;
import order.OrderService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import product.ProductService;

import category.CategoryService;

import database.DB;

import user.User;
import user.UserManager;
import utill.DBUtill;
import utill.TimeUtill;

public class AfterSaleManager {
	protected static Log logger = LogFactory.getLog(AfterSaleManager.class);
	
   public static boolean save(User user ,AfterSale as){
	   boolean flag = false ;
	   List<String> list = new ArrayList<String>();
	   String sql = "";
	   if(as.getId() != 0 ){
		   String sqld = "delete from mdaftersale where id = "+ as.getId();
		   list.add(sqld);
		   sql = "insert into mdaftersale (id,tid,printid,cid,pcount,uname,phone,batchnumber,barcode,location,branch,type,andate,saledate,detail,submittime,submitid) " +
			   		"values ("+as.getId()+","+as.getTid()+",'"+as.getPrintid()+"','"+as.getCid()+"','"+as.getPcount()+"','"+as.getUname()+"','"+as.getPhone()+"','"+as.getBatchNumber()+"','"+as.getBarcode()+"','"+as.getLocation()+"','"+as.getBranch()+"','"+as.getType()+"','"+as.getAndate()+"','"+as.getSaledate()+"','"+as.getDetail()+"','"+as.getSubmitTime()+"',"+as.getSubmitId()+") ;" ;
	   }else {
		   sql = "insert into mdaftersale (id,tid,printid,cid,pcount,uname,phone,batchnumber,barcode,location,branch,type,andate,saledate,detail,submittime,submitid) " +
			   		"values (null,"+as.getTid()+",'"+as.getPrintid()+"','"+as.getCid()+"','"+as.getPcount()+"','"+as.getUname()+"','"+as.getPhone()+"','"+as.getBatchNumber()+"','"+as.getBarcode()+"','"+as.getLocation()+"','"+as.getBranch()+"','"+as.getType()+"','"+as.getAndate()+"','"+as.getSaledate()+"','"+as.getDetail()+"','"+as.getSubmitTime()+"',"+as.getSubmitId()+") ;" ;
	   }
	     
	   list.add(sql);
	   flag = DBUtill.sava(list);
	   
	   return flag ;
	   
   }
   
   public static boolean checkePermission(User user ,String id){
	   boolean flag =  true ;   
	   AfterSale as =  getAfterSaleID(user ,id); 
	   if(UserManager.checkPermissions(user, Group.installOrderupload,"w") && as.getStatues() == 1){
		   flag = false ;
	   } 
	  // logger.info(UserManager.checkPermissions(user, Group.installOrderupload,"w"));
	   //logger.info(as.getStatues() == 1);
	   //logger.info(flag);
	   return flag ;
	   
   }
   
   public static List<String> getsaveSQL(User user ,AfterSale as){
	   List<String> list = new ArrayList<String>();
	   String sql = "";
	   if(as.getId() != 0 ){
		   String sqld = "delete from mdaftersale where id = "+ as.getId();
		   list.add(sqld);
		   sql = "insert into mdaftersale (id,tid,printid,cid,pcount,uname,phone,batchnumber,barcode,location,branch,type,andate,saledate,detail,submittime,submitid,statues,statuestime,nexttime) " +
			   		"values ("+as.getId()+","+as.getTid()+",'"+as.getPrintid()+"','"+as.getCid()+"','"+as.getPcount()+"','"+as.getUname()+"','"+as.getPhone()+"','"+as.getBatchNumber()+"','"+as.getBarcode()+"','"+as.getLocation()+"','"+as.getBranch()+"','"+as.getType()+"','"+as.getAndate()+"','"+as.getSaledate()+"','"+as.getDetail()+"','"+as.getSubmitTime()+"',"+as.getSubmitId()+","+as.getStatues()+","+as.getStatuestime()+","+as.getNexttime()+") ;" ;
	   }else {  
		   sql = "insert into mdaftersale (id,tid,printid,cid,pcount,uname,phone,batchnumber,barcode,location,branch,type,andate,saledate,detail,submittime,submitid,statues,statuestime,nexttime) " +  
			   		"values (null,"+as.getTid()+",'"+as.getPrintid()+"','"+as.getCid()+"','"+as.getPcount()+"','"+as.getUname()+"','"+as.getPhone()+"','"+as.getBatchNumber()+"','"+as.getBarcode()+"','"+as.getLocation()+"','"+as.getBranch()+"','"+as.getType()+"','"+as.getAndate()+"','"+as.getSaledate()+"','"+as.getDetail()+"','"+as.getSubmitTime()+"',"+as.getSubmitId()+","+as.getStatues()+","+as.getStatuestime()+","+as.getNexttime()+") ;" ;
	   }
	   
	   list.add(sql);
	   
	    
	   return list ;
	   
   }
   

   public static  String getupdateIsSubmitsql(String ids,String statues){ 
	   
	    String sql = " update mdaftersale set statues = "+statues+" , statuestime = '"+TimeUtill.getdateString()+"' where id in ("+ ids+")";
		return sql; 
}
   
   public static  AfterSale getAfterSaleID(User user ,String id){
	    AfterSale af = new AfterSale();
	   
	    String sql = " select * from  mdaftersale  where id in ("+ id+")";
	    
	    Connection conn = DB.getConn();
	       Statement stmt = DB.getStatement(conn);
	      
		   ResultSet rs = DB.getResultSet(stmt, sql); 
				try {  
					while (rs.next()) {
						af = getAfterSaleFromRs(rs);
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

    
   public static List<AfterSale> getOrderlist(User user ,int type,int statues ,int num,int page,String sort,String search){
	      
		 // boolean flagSearch = UserManager.checkPermissions(user, type,"r");
		  
		  String str = "";
		  if(num != -1){
			 str = "  limit " + ((page-1)*num)+","+num ; 
		  }
		  
		  List<AfterSale> AfterSales = new ArrayList<AfterSale>();
		  
		  String sql = "";    
		  
		  //logger.info(f); 
		  String sqlstr = " 1 = 1 and (mdaftersale.submitid in (select id from mduser where mduser.usertype in (select groupid from mdrelategroup where pgroupid = "+user.getUsertype()+")) or  submitid = "+ user.getId() +")";
		  
		  
		 if(Group.aftersalerepare == type){ 
			   if(Order.aftersale == statues){
				   if(UserManager.checkPermissions(user, Group.installOrderupload,"q")){
					   sql = "select * from mdaftersale where  statues in (0,2)  and "+sqlstr + search+"  order by "+sort+str;
				   }else if(UserManager.checkPermissions(user, Group.installOrderupload,"w")){
					   sql = "select * from mdaftersale where statues in (0,2)  and submitid = "+ user.getId() +" "+search+"  order by "+sort+str;
				   }
  
			   }else if(Order.aftersalesearch == statues){
				   if(UserManager.checkPermissions(user, Group.installOrderupload,"q")){
					   sql = "select * from mdaftersale where   "+sqlstr + search+"  order by "+sort+str;
				   }else if(UserManager.checkPermissions(user, Group.installOrderupload,"w")){
					   sql = "select * from mdaftersale where 1 =1  and submitid = "+ user.getId() +" "+search+"  order by "+sort+str;
				   }  
			   }else if(Order.aftersalesecond == statues){  
				       sql = "select * from mdaftersale,mdaftersaleproduct where  mdaftersaleproduct.dealid = "+ user.getId() +"  and  mdaftersaleproduct.asid = mdaftersale.id "+search+"  order by "+sort+str;
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
						AfterSale p = getAfterSaleFromRs(rs);
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
   
   public static int getMaxid(){
	    int id = 1 ;
	    Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn);
		String  sql = "select max(id)+1 as id from mdaftersale" ;
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
   
   public static AfterSale getAfterSaleFromRs(ResultSet rs){
	   AfterSale p = null;
		try { 
			p = new AfterSale();
			p.setId(rs.getInt("id")); 
			p.setAndate(rs.getString("andate"));
			p.setBarcode(rs.getString("barcode"));
			p.setBatchNumber(rs.getString("batchnumber"));
			int branch = rs.getInt("branch");
			p.setBranch(branch);
			p.setBranchName(OrderService.getBranchName(branch));
			int cid = rs.getInt("mdaftersale.cid");
			p.setCid(cid);
			p.setcName(CategoryService.getmap().get(cid).getName());
			p.setDetail(rs.getString("detail"));
			p.setLocation(rs.getString("location"));
			p.setPhone(rs.getString("phone"));
			p.setSaledate(rs.getString("saledate"));
			int tid = rs.getInt("mdaftersale.tid");
			p.setTid(tid);    
			p.settName(ProductService.getIDmap().get(tid).getType());
			p.setUname(rs.getString("uname"));  
			p.setPrintid(rs.getString("printid")); 
			p.setDetail(rs.getString("detail"));
			p.setType(rs.getInt("mdaftersale.type")); 
			p.setPcount(rs.getInt("pcount")); 
			int submitId = rs.getInt("submitId"); 
			p.setSubmitId(submitId);   
			p.setStatuestime(rs.getString("statuestime"));
		    p.setStatues(rs.getInt("statues"));   
		} catch (SQLException e) {   
			e.printStackTrace();
		} 
		return p;  
   }
   
}
