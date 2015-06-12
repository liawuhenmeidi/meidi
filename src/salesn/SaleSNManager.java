package salesn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import database.DB;

import utill.DBUtill;
import utill.TimeUtill;
 
public class SaleSNManager {
	protected static Log logger = LogFactory.getLog(SaleSNManager.class);
    
	 // time  branchnum_goodnum
	    
	public static void save(List<SaleSN> list){
		List<String> listsql = new ArrayList<String>();
		if(!list.isEmpty()){       
			Iterator<SaleSN> it = list.iterator();
			while(it.hasNext()){               
				SaleSN sa= it.next();         
				String sql = "insert into uploadorder (id,name,salesman,shop,posno,saletime,type,num,saleprice,backpoint,filename,uploadtime,checked,checkedtime,checkorderid) VALUES (null,'苏宁网上数据','','"+sa.getBranchname()+"','','"+sa.getSaletime()+"','"+sa.getGoodname()+"','"+sa.getSalenum()+"','0','0','','"+TimeUtill.getdateString()+"',1,'','-1')"; 	
			    logger.info(sql);             
				listsql.add(sql);        
			}     
		}   
		//DBUtill.sava(listsql); 
	}  
	 
	public static Map<String,Map<String,List<SaleSN>>> getMapDB(String starttime,
			String endtime,String name) {      

		Map<String,Map<String,List<SaleSN>>> map = new HashMap<String,Map<String,List<SaleSN>>>();
		String sql = " select * from uploadorder where  saletime BETWEEN  '"
				+ starttime + "'  and '" + endtime + "' and name =  '"+name+"'";        
		logger.info(sql);    
		Connection conn = DB.getConn(); 
		Statement stmt = DB.getStatement(conn);
  
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {   
			while (rs.next()) { 
				SaleSN as = getSaleSNFromRs(rs);
				String key = as.getSaletime();
				Map<String,List<SaleSN>> maps = map.get(key);
				if(null == maps){ 
					maps = new HashMap<String,List<SaleSN>>();
					map.put(key, maps);
				}     
				List<SaleSN> list = maps.get(as.getBranchname()+"_"+as.getGoodname());
				     
				if(null == list){    
					list = new ArrayList<SaleSN>();   
					maps.put(as.getBranchname()+"_"+as.getGoodname(), list);
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
			p.setBranchname(rs.getString("shop"));  
			//p.setSaletime(rs.getString("saletime"));
			//p.setBranchnum(rs.getString("branchnum"));
			p.setGoodname(rs.getString("type"));
			//p.setGoodnum(rs.getString("goodnum"));
			p.setSalenum(rs.getInt("num"));   
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
