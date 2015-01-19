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
import utill.DBUtill;

public class AfterSaleManager {
	protected static Log logger = LogFactory.getLog(AfterSaleManager.class);
	
   public static boolean save(User user ,AfterSale as){
	   boolean flag = false ;
	   List<String> list = new ArrayList<String>();
	   logger.info(as.getId());
	   String sql = "";
	   if(as.getId() != 0 ){
		   String sqld = "delete from mdaftersale where id = "+ as.getId();
		   list.add(sqld);
		   sql = "insert into mdaftersale (id,tid,printid,cid,pcount,uname,phone,batchnumber,barcode,location,branch,type,andate,saledate,detail) " +
			   		"values ("+as.getId()+","+as.getTid()+",'"+as.getPrintid()+"','"+as.getCid()+"','"+as.getPcount()+"','"+as.getUname()+"','"+as.getPhone()+"','"+as.getBatchNumber()+"','"+as.getBarcode()+"','"+as.getLocation()+"','"+as.getBranch()+"','"+as.getType()+"','"+as.getAndate()+"','"+as.getSaledate()+"','"+as.getDetail()+"') ;" ;
	   }else {
		   sql = "insert into mdaftersale (id,tid,printid,cid,pcount,uname,phone,batchnumber,barcode,location,branch,type,andate,saledate,detail) " +
			   		"values (null,"+as.getTid()+",'"+as.getPrintid()+"','"+as.getCid()+"','"+as.getPcount()+"','"+as.getUname()+"','"+as.getPhone()+"','"+as.getBatchNumber()+"','"+as.getBarcode()+"','"+as.getLocation()+"','"+as.getBranch()+"','"+as.getType()+"','"+as.getAndate()+"','"+as.getSaledate()+"','"+as.getDetail()+"') ;" ;
	   }
	   
	   list.add(sql);
	   flag = DBUtill.sava(list);
	   
	   return flag ;
	   
   }
   
   
   public static List<String> getsaveSQL(User user ,AfterSale as){
	   List<String> list = new ArrayList<String>();
	   String sql = "";
	   if(as.getId() != 0 ){
		   String sqld = "delete from mdaftersale where id = "+ as.getId();
		   list.add(sqld);
		   sql = "insert into mdaftersale (id,tid,printid,cid,pcount,uname,phone,batchnumber,barcode,location,branch,type,andate,saledate,detail) " +
			   		"values ("+as.getId()+","+as.getTid()+",'"+as.getPrintid()+"','"+as.getCid()+"','"+as.getPcount()+"','"+as.getUname()+"','"+as.getPhone()+"','"+as.getBatchNumber()+"','"+as.getBarcode()+"','"+as.getLocation()+"','"+as.getBranch()+"','"+as.getType()+"','"+as.getAndate()+"','"+as.getSaledate()+"','"+as.getDetail()+"') ;" ;
	   }else {
		   sql = "insert into mdaftersale (id,tid,printid,cid,pcount,uname,phone,batchnumber,barcode,location,branch,type,andate,saledate,detail) " +
			   		"values (null,"+as.getTid()+",'"+as.getPrintid()+"','"+as.getCid()+"','"+as.getPcount()+"','"+as.getUname()+"','"+as.getPhone()+"','"+as.getBatchNumber()+"','"+as.getBarcode()+"','"+as.getLocation()+"','"+as.getBranch()+"','"+as.getType()+"','"+as.getAndate()+"','"+as.getSaledate()+"','"+as.getDetail()+"') ;" ;
	   }
	   
	   list.add(sql);
	   
	   
	   return list ;
	   
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
        
		 if(Group.aftersalerepare == type){
			   if(Order.aftersale == statues){ 
				   sql = "select * from mdaftersale where statues = 0 "+search+"  order by "+sort+str;
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
						AfterSale p = gerAfterSaleFromRs(rs);
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
   
   public static AfterSale gerAfterSaleFromRs(ResultSet rs){
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
			int cid = rs.getInt("cid");
			p.setCid(cid);
			p.setcName(CategoryService.getmap().get(cid).getName());
			p.setDetail(rs.getString("detail"));
			p.setLocation(rs.getString("location"));
			p.setPhone(rs.getString("phone"));
			p.setSaledate(rs.getString("saledate"));
			int tid = rs.getInt("tid");
			p.setTid(tid);   
			p.settName(ProductService.getIDmap().get(tid).getType());
			p.setUname(rs.getString("uname"));  
			p.setPrintid(rs.getString("printid"));
			p.setDetail(rs.getString("detail"));
			p.setType(rs.getInt("type"));
		} catch (SQLException e) {  
			e.printStackTrace();
		} 
		return p;  
   }
   
}
