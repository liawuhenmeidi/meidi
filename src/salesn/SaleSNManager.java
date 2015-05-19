package salesn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import database.DB;

import utill.DBUtill;
 
public class SaleSNManager {
	protected static Log logger = LogFactory.getLog(SaleSNManager.class);
    
	 // time  branchnum_goodnum
	
	public static void save(List<SaleSN> list){
		List<String> listsql = new ArrayList<String>();
		if(!list.isEmpty()){ 
			Iterator<SaleSN> it = list.iterator();
			while(it.hasNext()){ 
				SaleSN sa= it.next();  
				String sql  = " insert into mdsalesn (id,saletime,branchnum,branchname,goodname,goodnum,salenum) values (null,'"+sa.getSaletime()+"','"+sa.getBranchnum()+"','"+sa.getBranchname()+"','"+sa.getGoodname()+"','"+sa.getGoodnum()+"','"+sa.getSalenum()+"') "; 
			    listsql.add(sql);
			}
		}
		
		DBUtill.sava(listsql); 
	}
	public static Map<String,Map<String,List<SaleSN>>> getMapDB(String starttime,
			String endtime) {     
		Map<String,Map<String,List<SaleSN>>> map = new HashMap<String,Map<String,List<SaleSN>>>();
		String sql = " select * from mdsalesn where  saletime BETWEEN  '"
				+ starttime + "'  and '" + endtime + "'"; 
		logger.info(sql);  
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn);

		ResultSet rs = DB.getResultSet(stmt, sql);
		try {  
			while (rs.next()) {
				SaleSN as = getSaleSNFromRs(rs);
				Map<String,List<SaleSN>> maps = map.get(as.getSaletime());
				if(null == maps){
					maps = new HashMap<String,List<SaleSN>>();
					map.put(as.getSaletime(), maps);
				} 
				List<SaleSN> list = maps.get(as.getBranchnum()+"_"+as.getGoodnum());
				 
				if(null == list){  
					list = new ArrayList<SaleSN>();  
					maps.put(as.getBranchnum()+"_"+as.getGoodnum(), list);
				}
				 
				list.add(as);  
			// 	map.put(as.getUuid(), as); 
			} 
		} catch (SQLException e) {
			logger.info(e); 
		} finally {
			DB.close(stmt);
			DB.close(rs);
			DB.close(conn);
		}

		return map;
	}
	
	
	
	


	public static SaleSN getSaleSNFromRs(ResultSet rs) {
		SaleSN p = null;
		try { 
			p = new SaleSN();
			p.setBranchname(rs.getString("branchname"));
			p.setBranchnum(rs.getString("branchnum"));
			p.setGoodname(rs.getString("goodname"));
			p.setGoodnum(rs.getString("goodnum"));
			p.setSalenum(rs.getInt("salenum")); 
			p.setSaletime(rs.getString("saletime"));
			p.setId(rs.getInt("id"));
		} catch (SQLException e) {
			p = null; 
			// logger.info(e); 
			return p;

		}
		return p;
	}

}
