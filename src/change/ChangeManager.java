package change;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utill.DBUtill;

import database.DB;

public class ChangeManager {
	
	public static void save(BranchTypeChange bt){
		List<String> sqls = new ArrayList<String>();
		Map<String, List<String>>  map = bt.getMaplist();
		Set<Map.Entry<String, List<String>>> setmap = map.entrySet();
		
		Iterator<Map.Entry<String, List<String>>> itmap = setmap.iterator();
		while(itmap.hasNext()){
			Map.Entry<String, List<String>> mape = itmap.next();
			String name = mape.getKey();
			List<String> list = mape.getValue();
			for(int i=0;i<list.size();i++){
				String real = list.get(i);
				String sql = " insert into mdchange (id,changes,bechange) values (null,'"+real+"','"+name+"')";
			    sqls.add(sql);
			}
		}
		DBUtill.sava(sqls); 
		BranchTypeChange.map = getmap();
	}
	public static Map<String,String>  getmap(){
		Map<String,String> map = new HashMap<String,String>();
		List<Change> list = getLocate();
		for(int i=0;i<list.size();i++){
			Change c = list.get(i);
			map.put(c.getChange(), c.getBechange());
		}
		return map;
	} 
	 
	public static Map<String,List<String>>  getmapList(){
		Map<String,List<String>> map = new HashMap<String,List<String>>();
		List<Change> list = getLocate();
		for(int i=0;i<list.size();i++){
			Change c = list.get(i);
			List<String> lists = map.get(c.getBechange());
			if(null == lists){
				lists = new ArrayList<String>();
				map.put(c.getBechange(), lists); 
			} 
			lists.add(c.getChange());
		}
		return map;
	}
	
	public static List<Change> getLocate() {
		List<Change> list = new ArrayList<Change>();
		
		Connection conn = DB.getConn();
		String sql = "select * from mdchange " ;
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {  // cname,uname,phone,locate
			while (rs.next()) { 
				Change g = new Change();
				g.setBechange(rs.getString("bechange"));
				g.setChange(rs.getString("changes"));
				g.setId(rs.getInt("id"));
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
}
