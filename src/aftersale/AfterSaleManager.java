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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import order.Order;
import order.OrderManager;
import order.OrderService;
import orderproduct.OrderProduct;
import orderproduct.OrderProductManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import product.Product;
import product.ProductService;

import category.Category;
import category.CategoryService;

import database.DB;

import user.User;
import user.UserManager;
import utill.DBUtill;
import utill.StringUtill;
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
      
   public static void saveByOrder(User user, Order or,List<String> list){
      
	   	HashMap<Integer,Category> categorymap = CategoryService.getmap();
	   	Map<Integer, Product> pmap = ProductService.getIDmap(); 
       
				List<OrderProduct> listop = or.getOrderproduct();
				//logger.info(listop);  
				if(null != listop){ 
					for(int j=0;j<listop.size();j++){
						OrderProduct op = listop.get(j);
						if(op.getStatues() == 0 ){ 
							AfterSale as = new AfterSale(); 
							
							int maxid = AfterSaleManager.getMaxid();
							if(0 == maxid){
								maxid = 1 ;
							}  
							//logger.info(maxid);  
							as.setId(maxid); 
							as.setOpid(op.getId());  
							as.setPrintid(or.getPrintlnid());  
							as.setAndate(!StringUtill.isNull(or.getInstalltime())?or.getInstalltime():or.getSendtime()); 
							as.setBarcode(op.getBarcode());  
							as.setBatchNumber(op.getBatchNumber());
							as.setBranch(or.getBranch()); 
							as.setBranchName(or.getbranchName(or.getBranch()));
							as.setCid(op.getCategoryId());
							as.setPcount(op.getCount());  
							as.setcName(categorymap.get(op.getCategoryId()).getName());
							as.setLocation(or.getLocate()+or.getLocateDetail());
							as.setPhone(or.getPhone1());  
							as.setSaledate(or.getSaleTime());   
							as.setTid(Integer.valueOf(op.getSendType()));   
							as.settName(pmap.get(Integer.valueOf(op.getSendType())).getType());
							//System.out.println(pmap.get(Integer.valueOf(op.getSendType())).getName());
							as.setUname(or.getUsername());
							as.setSubmitTime(TimeUtill.getdateString());
							as.setSubmitId(or.getDealsendId()); 
							
							List<String> listsql = AfterSaleManager.getsaveSQL(user, as);  
							list.addAll(listsql);
							
							AfterSaleProduct asp = new AfterSaleProduct();
							asp.setAsid(maxid);     
							asp.setType(AfterSaleProduct.install);
							asp.setResult(AfterSaleProduct.success);
							asp.setDealid(or.getDealsendId());  
							//asp.setDealid(or.getDealsendId());
							//asp.setDealsendid(!StringUtill.isNull(or.getSendId()+"")?or.getSendId():or.getInstallid());
							  
							List<String> listsql1 = AfterSaleProductManager.getsaveSQL(user, asp);  
							list.addAll(listsql1);  
							
						} 
					}
				}
   }
   
   public static List<String> saveByOrderList(User user, Order or,String json){
	   List<String> list = new ArrayList<String>();
	   JSONArray jsons = null;
	   logger.info(json);  
	   try{
		   jsons = JSONArray.fromObject(json); 
			HashMap<Integer,Category> categorymap = CategoryService.getmap();
		   	Map<Integer, Product> pmap = ProductService.getIDmap(); 
	              //  or.setOrderproduct(null);   
					List<OrderProduct> listop = or.getOrderproduct();
					//logger.info(listop);  
					if(null != listop){ 
						for(int j=0;j<listop.size();j++){
							OrderProduct op = listop.get(j);
							if(op.getStatues() == 0 ){ 
								AfterSale as = new AfterSale(); 
								
								int maxid = AfterSaleManager.getMaxid();
								if(0 == maxid){
									maxid = 1 ;
								}  
								for(int i=0;i<jsons.size();i++){
							   		JSONObject js = jsons.getJSONObject(i);
							   		int opid = js.getInt("id"); 
							   		if(opid == op.getId()){
							   			String barcode = js.getString("barcode");
								   		String batchnumber = js.getString("batchnumber");
								   		op.setBarcode(barcode);
								   		op.setBatchNumber(batchnumber);
							   		}

							   		
							   	}
								
								//logger.info(maxid);  
								as.setId(maxid); 
								as.setOpid(op.getId());  
								as.setPrintid(or.getPrintlnid());  
								
								as.setAndate(TimeUtill.getdateString());   
								as.setBarcode(op.getBarcode());  
								as.setBatchNumber(op.getBatchNumber());
								as.setBranch(or.getBranch()); 
								as.setBranchName(or.getbranchName(or.getBranch()));
								as.setCid(op.getCategoryId());
								as.setPcount(op.getCount());  
								as.setcName(categorymap.get(op.getCategoryId()).getName());
								as.setLocation(or.getLocate()+or.getLocateDetail());
								as.setPhone(or.getPhone1());  
								as.setSaledate(or.getSaleTime());   
								as.setTid(Integer.valueOf(op.getSendType()));   
								as.settName(pmap.get(Integer.valueOf(op.getSendType())).getType());
								//System.out.println(pmap.get(Integer.valueOf(op.getSendType())).getName());
								as.setUname(or.getUsername());
								as.setSubmitTime(TimeUtill.getdateString());
								as.setSubmitId(or.getDealsendId()); 
								
								List<String> listsql = AfterSaleManager.getsaveSQL(user, as);  
								list.addAll(listsql);
								
								AfterSaleProduct asp = new AfterSaleProduct();
								asp.setAsid(maxid);     
								asp.setType(AfterSaleProduct.install);
								asp.setResult(AfterSaleProduct.success);
								asp.setDealid(or.getDealsendId());  
								//asp.setDealid(or.getDealsendId());
								//asp.setDealsendid(!StringUtill.isNull(or.getSendId()+"")?or.getSendId():or.getInstallid());
								  
								List<String> listsql1 = AfterSaleProductManager.getsaveSQL(user, asp);  
								list.addAll(listsql1);  
								
							}
						}
					}
	   }catch(Exception e){
			HashMap<Integer,Category> categorymap = CategoryService.getmap();
		   	Map<Integer, Product> pmap = ProductService.getIDmap(); 
	              //  or.setOrderproduct(null);   
					List<OrderProduct> listop = or.getOrderproduct();
					//logger.info(listop);  
					if(null != listop){ 
						for(int j=0;j<listop.size();j++){
							OrderProduct op = listop.get(j);
							if(op.getStatues() == 0 ){ 
								AfterSale as = new AfterSale(); 
								
								int maxid = AfterSaleManager.getMaxid();
								if(0 == maxid){
									maxid = 1 ;
								}  
						
								
								//logger.info(maxid);  
								as.setId(maxid); 
								as.setOpid(op.getId());  
								as.setPrintid(or.getPrintlnid());  
								
								as.setAndate(TimeUtill.getdateString());   
								as.setBarcode(op.getBarcode());  
								as.setBatchNumber(op.getBatchNumber());
								as.setBranch(or.getBranch()); 
								as.setBranchName(or.getbranchName(or.getBranch()));
								as.setCid(op.getCategoryId());
								as.setPcount(op.getCount());  
								as.setcName(categorymap.get(op.getCategoryId()).getName());
								as.setLocation(or.getLocate()+or.getLocateDetail());
								as.setPhone(or.getPhone1());  
								as.setSaledate(or.getSaleTime());   
								as.setTid(Integer.valueOf(op.getSendType()));   
								as.settName(pmap.get(Integer.valueOf(op.getSendType())).getType());
								//System.out.println(pmap.get(Integer.valueOf(op.getSendType())).getName());
								as.setUname(or.getUsername());
								as.setSubmitTime(TimeUtill.getdateString());
								as.setSubmitId(or.getDealsendId()); 
								
								List<String> listsql = AfterSaleManager.getsaveSQL(user, as);  
								list.addAll(listsql);
								
								AfterSaleProduct asp = new AfterSaleProduct();
								asp.setAsid(maxid);     
								asp.setType(AfterSaleProduct.install);
								asp.setResult(AfterSaleProduct.success);
								asp.setDealid(or.getDealsendId());  
								//asp.setDealid(or.getDealsendId());
								//asp.setDealsendid(!StringUtill.isNull(or.getSendId()+"")?or.getSendId():or.getInstallid());
								  
								List<String> listsql1 = AfterSaleProductManager.getsaveSQL(user, asp);  
								list.addAll(listsql1);  
								
							}
						}
					} 
	   }
				return list ;
  }
   
   public static List<String> getsaveSQL(User user ,AfterSale as){
	   if(!StringUtill.isNull(as.getAndate())){
		   as.setAndate("'"+as.getAndate()+"'");
	   }else {
		   as.setAndate(null);
	   } 
	   if(!StringUtill.isNull(as.getSaledate())){ 
		   as.setSaledate("'"+as.getSaledate()+"'");
	   }else{ 
		   as.setSaledate(null);
	   } 
	   List<String> list = new ArrayList<String>();
	   String sql = "";
	   if(as.getId() != 0 ){ 
		   String sqld = "delete from mdaftersale where id = "+ as.getId();
		   list.add(sqld);
		   sql = "insert into mdaftersale (id,tid,printid,cid,pcount,uname,phone,batchnumber,barcode,location,branch,type,andate,saledate,detail,submittime,submitid,statues,statuestime,nexttime,tname) " +
			   		"values ("+as.getId()+","+as.getTid()+",'"+as.getPrintid()+"','"+as.getCid()+"','"+as.getPcount()+"','"+as.getUname()+"','"+as.getPhone()+"','"+as.getBatchNumber()+"','"+as.getBarcode()+"','"+as.getLocation()+"','"+as.getBranch()+"','"+as.getType()+"',"+as.getAndate()+","+as.getSaledate()+",'"+as.getDetail()+"','"+as.getSubmitTime()+"',"+as.getSubmitId()+","+as.getStatues()+","+as.getStatuestime()+","+as.getNexttime()+",'"+as.gettName()+"') ;" ;
	   }else {     
		   sql = "insert into mdaftersale (id,tid,printid,cid,pcount,uname,phone,batchnumber,barcode,location,branch,type,andate,saledate,detail,submittime,submitid,statues,statuestime,nexttime,tname) " +   
			   		"values (null,"+as.getTid()+",'"+as.getPrintid()+"','"+as.getCid()+"','"+as.getPcount()+"','"+as.getUname()+"','"+as.getPhone()+"','"+as.getBatchNumber()+"','"+as.getBarcode()+"','"+as.getLocation()+"','"+as.getBranch()+"','"+as.getType()+"',"+as.getAndate()+","+as.getSaledate()+",'"+as.getDetail()+"','"+as.getSubmitTime()+"',"+as.getSubmitId()+","+as.getStatues()+","+as.getStatuestime()+","+as.getNexttime()+",'"+as.gettName()+"') ;" ;
	   } 
	             
	  /* String sql1 = "update mdorderproduct set issubmit = "+as.getStatues() +" where orderid  = "+ as.getOpid();
	    
	   list.add(sql1);  */
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
			//p.settName(ProductService.getIDmap().get(tid).getType());
			p.settName(rs.getString("tname")); 
			p.setUname(rs.getString("uname"));  
			p.setPrintid(rs.getString("printid")); 
			p.setDetail(rs.getString("detail"));
			p.setType(rs.getInt("mdaftersale.type")); 
			p.setPcount(rs.getInt("pcount")); 
			int submitId = rs.getInt("submitId"); 
			p.setSubmitId(submitId);   
			p.setStatuestime(rs.getString("statuestime"));
		    p.setStatues(rs.getInt("statues"));    
		    p.setNexttime(rs.getString("nexttime"));
		} catch (SQLException e) {   
			e.printStackTrace();
		} 
		return p;  
   }
   
}
