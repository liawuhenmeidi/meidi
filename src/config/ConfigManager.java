package config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import database.DB;

public class ConfigManager {
private static ConfigManager instance;

	public Map<Integer,Config> map = null;
	
	public  static ConfigManager getinstance(){
		if(null == instance){
			instance = new ConfigManager();
			instance.init();
		} 
		return instance;
	}
	
	 
	
	public void init(){
		this.map=getmap(); 
		
	}
	
	public static Map<Integer,Config> getmap() {
		Map<Integer,Config> map = new HashMap<Integer,Config>();
		
		Connection conn = DB.getConn();
		String sql = "select * from mdconfig " ;
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {  // cname,uname,phone,locate
			while (rs.next()) { 
				Config g = getConfigFromRs(rs);
				map.put(g.getName(), g);
			}   
		} catch (SQLException e) { 
			e.printStackTrace();
		} finally { 
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}   
		return map;
	}
	
	public static List<Config> getLocate() {
		List<Config> list = new ArrayList<Config>();
		
		Connection conn = DB.getConn();
		String sql = "select * from mdconfig " ;
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {  // cname,uname,phone,locate
			while (rs.next()) { 
				Config g = getConfigFromRs(rs);
				list.add(g);
			}   
		} catch (SQLException e) { 
			e.printStackTrace();
		} finally { 
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}  
		return list;
	}
	
	 public static Config getConfigFromRs(ResultSet rs){
		 Config p = null;
			try { 
				p = new Config();
				p.setId(rs.getInt("id")); 
				p.setName(rs.getInt("name"));
				p.setStatues(rs.getInt("statues"));
			} catch (SQLException e) {   
				e.printStackTrace();
			} 
			return p;  
	   }
	   
	
	
	
}
