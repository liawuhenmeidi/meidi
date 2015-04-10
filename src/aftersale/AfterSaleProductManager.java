package aftersale;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import product.ProductService;

import database.DB;
 
import user.User;
import utill.StringUtill;
import utill.TimeUtill;

public class AfterSaleProductManager {
	protected static Log logger = LogFactory.getLog(AfterSaleProductManager.class);
	  
	public static List<String> getsaveSQL(User user ,AfterSaleProduct as){
		   if(!StringUtill.isNull(as.getThistime())){
			   as.setThistime("'"+as.getThistime()+"'"); 
		   }
		   
		   if(!StringUtill.isNull(as.getNexttime())){
			   as.setNexttime("'"+as.getNexttime()+"'"); 
		   }
		    
		   List<String> list = new ArrayList<String>();
		   String sql= "insert into mdaftersaleproduct (id,asid,type,cause,cid,tid,prince,dealid,dealsendid,dealtime,result,statues,detail,nexttime,thistime) " + 
				   		"values ("+as.getId()+","+as.getAsid()+","+as.getType()+",'"+as.getCause()+"','"+as.getCid()+"','"+as.getTid()+"','"+as.getPrince()+"','"+as.getDealid()+"','"+as.getDealsendid()+"',"+as.getDealtime()+",'"+as.getResult()+"','"+as.getStatues()+"','"+as.getDetail()+"',"+as.getNexttime()+","+as.getThistime()+") ;" ;
		     
		   list.add(sql); 
 
		   return list ;
		    
	   }
	
	
	public static List<String> getupdateSend(List<AfterSaleProduct> list,String uid){
		List<String> listsql = new ArrayList<String>();
		String str = ""; 
		
		for(int i=0;i<list.size();i++){
			AfterSaleProduct asp = list.get(i);
			str += asp.getId()+","; 
		}
		   
		str = "("+str.substring(0,str.length()-1)+")";
		// logger.info(str); 
		String sql = "update mdaftersaleproduct set dealsendid = " + uid +" where id  in "+ str ;
		
		logger.info(sql);
		
		listsql.add(sql);
		return listsql; 
	}
	
	public static List<String> getupdateDeal(List<AfterSaleProduct> list,String uid){
		List<String> listsql = new ArrayList<String>();
		String str = ""; 
		
		for(int i=0;i<list.size();i++){
			AfterSaleProduct asp = list.get(i);
			str += asp.getId()+","; 
		}
		  
		str = "("+str.substring(0,str.length()-1)+")"; 
 
		String sql = "update mdaftersaleproduct set dealid = " + uid +" where id  in "+ str ;
		
		logger.info(sql); 
		
		listsql.add(sql);
		return listsql; 
	}
	
	public static List<String> getupdatemaintain(User user ,List<AfterSaleProduct> list,String statues,String message,String cause,String barcode,String batchNumber){
		List<String> listsql = new ArrayList<String>();
		String str = ""; 
		AfterSaleProduct as = null;
		//logger.info(list); 
		for(int i=0;i<list.size();i++){
			as = list.get(i); 
			str += as.getId()+",";  
		} 
		    
		str = "("+str.substring(0,str.length()-1)+")"; 
		String sql = ""; 
		logger.info(str);
		//logger.info(message);
		if(1 == Integer.valueOf(statues)){
			if(!StringUtill.isNull(message)){ 
				String[] strs =  message.split(",");
				for(int i=0;i<strs.length;i++){
					String oids = strs[i];
					//logger.info(oids); 
					String[] oidss = oids.split("_");
					    String cid = oidss[0];
						String oname =oidss[1];    
					//	logger.info(oname); 
						int tid = ProductService.gettypemap(user).get(oname).getId();
						String sql2 = "delete from mdaftersaleproduct  where id = "+as.getId();
						  
						String sql1= "insert into mdaftersaleproduct (id,asid,type,cause,cid,tid,prince,dealid,dealsendid,dealtime,result,statues,detail,nexttime,thistime,dealresult) " +  
						   		"values (null,"+as.getAsid()+","+as.getType()+",'"+as.getCause()+"','"+cid+"','"+tid+"','"+as.getPrince()+"','"+as.getDealid()+"','"+as.getDealsendid()+"','"+TimeUtill.getdateString()+"','"+statues+"','"+as.getStatues()+"','"+as.getDetail()+"',"+as.getNexttime()+",'"+as.getThistime()+"','"+cause+"') ;" ;
						 
						listsql.add(sql2);      
						listsql.add(sql1);
				}     
			}else {  
				sql = "update mdaftersaleproduct set result = " + statues +" , dealresult = '"+cause+"' ,thistime = '"+TimeUtill.getdateString()+"',dealtime = '"+TimeUtill.getdateString()+"' where id  in "+ str +" and result = 0 ";
				listsql.add(sql); 
			}
			  
			//sql = "update mdaftersaleproduct set result = " + statues +" where asid  in "+ str +" and result = 0 " ;
		}else {    
			//sql = "update mdaftersaleproduct set dealsendid= 0 , cause = '"+cause+"' where id  in "+ str +" and result = 0 ";
			sql = "update mdaftersaleproduct set statues = 2  , dealresult = '"+cause+"' where id  in "+ str +" and result = 0 ";
			listsql.add(sql);
		} 
		   
		String sqls = "update mdaftersale set barcode = '"+barcode+"' ,batchnumber = '"+batchNumber+"' where id="+as.getAsid();
		listsql.add(sqls);
		
		logger.info(sql); 
		
		 
		return listsql; 
	}
	
	public static AfterSaleProduct getByid(String id){
		 AfterSaleProduct as = null; 
		String sql = "select *  from mdaftersaleproduct where  asid  in ("+id+")  and result = 0 " ; 
		Connection conn = DB.getConn();   
	    Statement stmt = DB.getStatement(conn);
 
		ResultSet rs = DB.getResultSet(stmt, sql); 
		    
				try {  
					while (rs.next()) {
						 as = gerAfterSaleProductFromRs(rs); 
					}  
				} catch (SQLException e) { 
					e.printStackTrace();
				} finally {
					DB.close(stmt);
					DB.close(rs);
					DB.close(conn);
				 }

        return as ;
	}
	 
	public static List<AfterSaleProduct> getmaintain(int id,String statues){
		List<AfterSaleProduct> list = new ArrayList<AfterSaleProduct>();
		String sql = "select *  from mdaftersaleproduct where statues = "+statues+" and asid  in ("+id+")  " ;
		logger.info(sql);   
		Connection conn = DB.getConn();  
	    Statement stmt = DB.getStatement(conn);

		ResultSet rs = DB.getResultSet(stmt, sql); 
		    
				try {  
					while (rs.next()) {
						 AfterSaleProduct as = gerAfterSaleProductFromRs(rs);
						 list.add(as);  
					}  
				} catch (SQLException e) { 
					e.printStackTrace();
				} finally {
					DB.close(stmt);
					DB.close(rs);
					DB.close(conn);
				 }

        return list ;
	}
	
	public static List<AfterSaleProduct> getfault(int id,String statues){
		List<AfterSaleProduct> list = new ArrayList<AfterSaleProduct>();
		   
		String sql = "select *  from mdaftersaleproduct where stautes = "+statues+" and asid  in ("+id+") and type = "+AfterSaleProduct.fault ;
		
		logger.info(sql);    
		
		Connection conn = DB.getConn();  
	    Statement stmt = DB.getStatement(conn);
 
		ResultSet rs = DB.getResultSet(stmt, sql); 
		    
				try {  
					while (rs.next()) {
						 AfterSaleProduct as = gerAfterSaleProductFromRs(rs);
						 list.add(as);  
					}  
				} catch (SQLException e) { 
					e.printStackTrace();
				} finally {
					DB.close(stmt);
					DB.close(rs);
					DB.close(conn);
				 }
		
		 
		
        return list ;
	}
	
	public static String delete(int id){ 
		String sql = " delete from mdaftersaleproduct where id = "+ id ;
		return sql ;
	}
	
	public static AfterSaleProduct gerAfterSaleProductFromRs(ResultSet rs){
		   AfterSaleProduct p = null;
			try {  
				p = new AfterSaleProduct();
				p.setId(rs.getInt("mdaftersaleproduct.id"));
				p.setAsid(rs.getInt("asid"));
				p.setCause(rs.getString("cause"));
				int cid = rs.getInt("mdaftersaleproduct.cid");
				p.setCid(cid);    
				int tid = rs.getInt("mdaftersaleproduct.tid"); 
				p.setTid(tid); 
				p.setDealid(rs.getInt("mdaftersaleproduct.dealid")); 
				p.setDealsendid(rs.getInt("mdaftersaleproduct.dealsendid")); 
				p.setDealtime(rs.getString("dealtime"));
				p.setNexttime(rs.getString("mdaftersaleproduct.nexttime")); 
				p.setThistime(rs.getString("thistime")); 
				p.setPrince(rs.getDouble("prince"));  
				p.setResult(rs.getInt("result")) ; 
				p.setStatues(rs.getInt("mdaftersaleproduct.statues"));
				p.setDetail(rs.getString("mdaftersaleproduct.detail"));
				p.setType(rs.getInt("mdaftersaleproduct.type"));  
                p.setDealresult(rs.getString("dealresult")); 
			} catch (SQLException e) {
				p = null;
				//logger.info(e); 
				return p ;
				 
			} 
			return p ;  
	   }
	
}
