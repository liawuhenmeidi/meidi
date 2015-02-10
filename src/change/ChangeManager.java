package change;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

import utill.DBUtill;
import utill.StringUtill;

import database.DB;

public class ChangeManager {
	protected static Log logger = LogFactory.getLog(ChangeManager.class);
	public static void save(BranchTypeChange bt) {
		List<String> sqls = new ArrayList<String>();
		/*
		 * String sqlall = "delete from mdchange "; sqls.add(sqlall);
		 */ 
		Map<String, Set<String>> map = bt.getMaplist();
		Set<Map.Entry<String, Set<String>>> setmap = map.entrySet();
 
		Iterator<Map.Entry<String, Set<String>>> itmap = setmap.iterator();
		while (itmap.hasNext()) { 
			Map.Entry<String, Set<String>> mape = itmap.next();
			String name = mape.getKey();
			Set<String> list = mape.getValue();        
			if (list.size() > 1) {
				Iterator<String> it = list.iterator();
				while(it.hasNext()){
					String real = it.next(); 
					if (StringUtill.isNull(name)) {
						String sql = " insert into  mduploadchange (id,filename,name,statues) values (null,'"
								+ bt.getName() + "','" + real + "',0);";
						sqls.add(sql); 
					} else { 
						String sql = " insert into  mdchange (id,changes,bechange) values (null,'"
								+ real + "','" + name + "')";
						sqls.add(sql);
					}
				}
				

			} else {

				String sql = " insert into mduploadchange (id,filename,name,statues) values (null,'"
						+ bt.getName() + "','" + name + "',1);";
				sqls.add(sql);
			}

		}
		DBUtill.sava(sqls);
		BranchTypeChange.getinstance().init();
	}
  
	 
	public static void save(Object json) {
		List<String> sqls = new ArrayList<String>();
		
		JSONObject jsons = JSONObject.fromObject(json);  
		 
		Iterator<String> it = jsons.keys();  
       while(it.hasNext()){  
        	String key  = it.next();
        	String value = jsons.getString(key);
        	String sql = " insert ignore into  mdchange (id,changes,bechange) values (null,'"
					+ value + "','" + key + "')";
			sqls.add(sql); 
        }     
        logger.info(sqls);
		
		DBUtill.sava(sqls); 
		BranchTypeChange.getinstance().init();
	}
	
	public static void save(String[] bt) {
		List<String> sqls = new ArrayList<String>();
		String idss = "";
		if (null != bt) {
			Map<Integer, UploadChange> map = UploadChangeManager.getmap();
			for (int i = 0; i < bt.length; i++) {
				String id = bt[i];
				String ids[] = id.split("_");
				idss += ids[1] + ","; 
				UploadChange left = map.get(Integer.valueOf(ids[0]));
				UploadChange right = map.get(Integer.valueOf(ids[1]));
 
				String sql = " insert ignore into mdchange (id,changes,bechange) values (null,'"
						+ left.getName() + "','" + right.getName() + "')";
				sqls.add(sql);

			}

		}

		if (!StringUtill.isNull(idss)) {
			idss = "(" + idss.substring(0, idss.length() - 1) + ")";
			String sqld = " update  mduploadchange set statues = 2 where id in " + idss;
			sqls.add(sqld);
		}

		DBUtill.sava(sqls);
		BranchTypeChange.getinstance().init();

	} 
    
	public static void save(String[] lefts,String[] rights) {
		List<String> sqls = new ArrayList<String>();
		String idss = "";
		Map<Integer, UploadChange> map = UploadChangeManager.getmap();
		idss += lefts[0] + ","+rights[0]+","; 
		UploadChange left = map.get(Integer.valueOf(lefts[0]));
		UploadChange right = map.get(Integer.valueOf(rights[0]));
		 
		String sql = " insert into mdchange (id,changes,bechange) values (null,'"
					+ left.getName() + "','" + right.getName() + "')";
			sqls.add(sql);

		if (!StringUtill.isNull(idss)) {
			idss = "(" + idss.substring(0, idss.length() - 1) + ")";
			String sqld = " update  mduploadchange set statues = 2 where id in " + idss;
			sqls.add(sqld);
		} 
 
		DBUtill.sava(sqls);
		BranchTypeChange.getinstance().init();

	} 
	
	public static Map<String, String> getmap() {
		Map<String, String> map = new HashMap<String, String>();
		List<Change> list = getLocate();
		for (int i = 0; i < list.size(); i++) {
			Change c = list.get(i); 
			map.put(c.getBechange(), c.getChange());
		}
		return map;
	}
  
	public static Map<Integer, Change> getmapO() {
		Map<Integer, Change> map = new HashMap<Integer, Change>();
		List<Change> list = getLocate();
		for (int i = 0; i < list.size(); i++) { 
			Change c = list.get(i); 
			map.put(c.getId(), c);
		}
		return map;
	}
	
	public static Map<String, List<Change>> getmapListC(String statues) {
		Map<String, List<Change>> map = new HashMap<String, List<Change>>();
		List<Change> list = getLocate(statues); 
		for (int i = 0; i < list.size(); i++) { 
			Change c = list.get(i);
			List<Change> lists = map.get(c.getChange());
			if (null == lists) {
				lists = new ArrayList<Change>();
				map.put(c.getChange(), lists);
			} 
			lists.add(c); 
		} 
		return map;
	}
     
	public static Map<String, List<String>> getmapList() {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<Change> list = getLocate();
		for (int i = 0; i < list.size(); i++) { 
			Change c = list.get(i);
			List<String> lists = map.get(c.getChange());
			if (null == lists) {
				lists = new ArrayList<String>();
				map.put(c.getChange(), lists);
			} 
			lists.add(c.getBechange()); 
		}
		return map; 
	}
	 
	public static void delete (String[] list){
		List<String> sqls = new ArrayList<String>();
		String str = ""; 
		for(int i=0;i<list.length;i++){
			str += list[i]+","; 
		}  
		str = "(" + str.substring(0,str.length()-1)+")";
		String sql   = "delete from mdchange where id in "+ str;
		//String sql1 = "update mdchange set statues = 0 where id in " +str ;
		// sqls.add(sql1);
		sqls.add(sql);
		DBUtill.sava(sqls);  
		BranchTypeChange.getinstance().init();
	}
	public static List<Change> getLocate() {
		List<Change> list = new ArrayList<Change>();

		Connection conn = DB.getConn();
		String sql = "select * from mdchange "; 
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);  
		try { // cname,uname,phone,locate
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
	 
	public static List<Change> getLocate(String statues) {
		List<Change> list = new ArrayList<Change>();

		Connection conn = DB.getConn();
		String sql = "select * from mdchange "; 
		if(!StringUtill.isNull(statues)){
			if(0 == Integer.valueOf(statues)){
				sql = "select * from mdchange where changes in (select name from mduploadchange where statues = 0 )";
			}else if(1 == Integer.valueOf(statues)){ 
				sql = "select * from mdchange where changes in (select name from mduploadchange where statues = 1 )";
			}
		}
		
		
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);  
		try { // cname,uname,phone,locate
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
