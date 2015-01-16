package aftersale;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import branch.BranchManager;

import user.User;
import utill.DBUtill;
import utill.StringUtill;

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
   
}
