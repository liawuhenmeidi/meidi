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
import utill.StringUtill;

import database.DB;

public class ChangeManager {

	public static void save(BranchTypeChange bt) {
		List<String> sqls = new ArrayList<String>();
		/*
		 * String sqlall = "delete from mdchange "; sqls.add(sqlall);
		 */
		Map<String, List<String>> map = bt.getMaplist();
		Set<Map.Entry<String, List<String>>> setmap = map.entrySet();

		Iterator<Map.Entry<String, List<String>>> itmap = setmap.iterator();
		while (itmap.hasNext()) {
			Map.Entry<String, List<String>> mape = itmap.next();
			String name = mape.getKey();
			List<String> list = mape.getValue();
			if (list.size() > 1) {
 
				for (int i = 0; i < list.size(); i++) {
					String real = list.get(i);
					if (StringUtill.isNull(name)) {
						String sql = " insert into ignore mduploadchange (id,filename,name,statues) values (null,'"
								+ bt.getName() + "','" + real + "',0);";
						sqls.add(sql); 
					} else { 
						String sql = " insert into ignore mdchange (id,changes,bechange) values (null,'"
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
		BranchTypeChange.setMap(getmap());
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
 
				String sql = " insert into mdchange (id,changes,bechange) values (null,'"
						+ left.getName() + "','" + right.getName() + "')";
				sqls.add(sql);

			}

		}

		if (!StringUtill.isNull(idss)) {
			idss = "(" + idss.substring(0, idss.length() - 1) + ")";
			String sql = " delete from  mduploadchange where id in " + idss;
			sqls.add(sql);
		}

		DBUtill.sava(sqls);
		BranchTypeChange.setMap(getmap());

	}

	public static Map<String, String> getmap() {
		Map<String, String> map = new HashMap<String, String>();
		List<Change> list = getLocate();
		for (int i = 0; i < list.size(); i++) {
			Change c = list.get(i);
			map.put(c.getChange(), c.getBechange());
		}
		return map;
	}

	public static Map<String, List<Change>> getmapListC() {
		Map<String, List<Change>> map = new HashMap<String, List<Change>>();
		List<Change> list = getLocate();
		for (int i = 0; i < list.size(); i++) { 
			Change c = list.get(i);
			List<Change> lists = map.get(c.getBechange());
			if (null == lists) {
				lists = new ArrayList<Change>();
				map.put(c.getBechange(), lists);
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
			List<String> lists = map.get(c.getBechange());
			if (null == lists) {
				lists = new ArrayList<String>();
				map.put(c.getBechange(), lists);
			} 
			lists.add(c.getChange()); 
		}
		return map;
	}
	 
	public static void delete (String[] list){
		String str = "";
		for(int i=0;i<list.length;i++){
			str += list[i]+","; 
		} 
		str = "(" + str.substring(0,str.length()-1)+")";
		String sql   = "delete from mdchange where id in "+ str;
		DBUtill.sava(sql); 
		BranchTypeChange.setMap(getmap());
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
}
