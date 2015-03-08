package aftersale;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import database.DB;
 
import user.User;

public class AfterSaleProductManager {
	protected static Log logger = LogFactory.getLog(AfterSaleProductManager.class);
	 
	public static List<String> getsaveSQL(User user ,AfterSaleProduct as){
		   List<String> list = new ArrayList<String>();
		   String sql= "insert into mdaftersaleproduct (id,asid,type,cause,cid,tid,prince,dealid,dealtime,result,statues,detail,nexttime,thistime) " + 
				   		"values ("+as.getId()+","+as.getAsid()+","+as.getType()+",'"+as.getCause()+"','"+as.getCid()+"','"+as.getTid()+"','"+as.getPrince()+"','"+as.getDealid()+"',"+as.getDealtime()+",'"+as.getResult()+"','"+as.getStatues()+"','"+as.getDetail()+"',"+as.getNexttime()+","+as.getThistime()+") ;" ;
		    
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
		logger.info(str); 
		String sql = "update mdaftersaleproduct set dealsendid = " + uid +" where asid  in "+ str ;
		
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

		String sql = "update mdaftersaleproduct set dealid = " + uid +" where asid  in "+ str ;
		
		logger.info(sql); 
		
		listsql.add(sql);
		return listsql; 
	}
	
	public static List<String> getupdatemaintain(List<AfterSaleProduct> list,String statues){
		List<String> listsql = new ArrayList<String>();
		String str = ""; 
		 
		for(int i=0;i<list.size();i++){
			AfterSaleProduct asp = list.get(i);
			str += asp.getId()+","; 
		}
		  
		str = "("+str.substring(0,str.length()-1)+")"; 
		String sql = "";
		if(1 == Integer.valueOf(statues)){ 
			sql = "update mdaftersaleproduct set result = " + statues +" where asid  in "+ str ;
		}else {   
			sql = "update mdaftersaleproduct set statues = " + statues +" where asid  in "+ str ;
		}
		  
		logger.info(sql); 
		
		listsql.add(sql);
		return listsql; 
	}
	
	public static List<AfterSaleProduct> getmaintain(int id,String statues){
		List<AfterSaleProduct> list = new ArrayList<AfterSaleProduct>();
		   
		String sql = "select *  from mdaftersaleproduct where statues = "+statues+" and asid  in ("+id+") and type = "+AfterSaleProduct.maintain ;
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
	
	public static AfterSaleProduct gerAfterSaleProductFromRs(ResultSet rs){
		   AfterSaleProduct p = null;
			try { 
				p = new AfterSaleProduct();
				p.setId(rs.getInt("id"));
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

			} catch (SQLException e) {
				p = null;
				logger.info(e);
				return p ;
				
			} 
			return p ;  
	   }
	
}
