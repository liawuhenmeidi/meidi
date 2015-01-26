package aftersale;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 
import user.User;

public class AfterSaleProductManager {
	protected static Log logger = LogFactory.getLog(AfterSaleProductManager.class);
	 
	public static List<String> getsaveSQL(User user ,AfterSaleProduct as){
		   List<String> list = new ArrayList<String>();
		   String sql= "insert into mdaftersaleproduct (id,asid,type,cause,cid,tid,prince,dealid,dealtime,result,statues,detail,nexttime) " + 
				   		"values ("+as.getId()+","+as.getAsid()+","+as.getType()+",'"+as.getCause()+"','"+as.getCid()+"','"+as.getTid()+"','"+as.getPrince()+"','"+as.getDealid()+"',"+as.getDealtime()+",'"+as.getResult()+"','"+as.getStatues()+"','"+as.getDetail()+"',"+as.getNexttime()+") ;" ;
		    
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
		logger.info(str);  
		String sql = "update mdaftersaleproduct set dealid = " + uid +" where asid  in "+ str ;
		
		logger.info(sql); 
		
		listsql.add(sql);
		return listsql; 
	}
	
	public static AfterSaleProduct gerAfterSaleProductFromRs(ResultSet rs){
		   AfterSaleProduct p = null;
			try { 
				p = new AfterSaleProduct();
				p.setId(rs.getInt("id"));
				p.setAsid(rs.getInt("asid"));
				p.setCause(rs.getString("cause"));
				int cid = rs.getInt("cid");
				p.setCid(cid); 
				p.setDealid(rs.getInt("dealid"));
				p.setDealsendid(rs.getInt("dealsendid"));
				p.setDealtime(rs.getString("dealtime"));
				p.setNexttime(rs.getString("nexttime"));
				p.setPrince(rs.getDouble("prince")); 
				p.setResult(rs.getInt("result")) ;
				p.setStatues(rs.getInt("statues"));
				p.setDetail(rs.getString("detail"));
				p.setType(rs.getInt("type"));  

			} catch (SQLException e) {
				p = null;
				logger.info(e);
				return p ;
				
			} 
			return p ;  
	   }
	
}
