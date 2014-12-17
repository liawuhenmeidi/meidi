package group;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import database.DB;

public class GroupRelateManager {
	 protected static Log logger = LogFactory.getLog(GroupRelateManager.class);

	 public static Map<Integer,List<Integer>> getmap(){ 
		    HashMap<Integer,List<Integer>> map = new HashMap<Integer,List<Integer>>();   
			Connection conn = DB.getConn();      
			String sql = "select * from mdrelategroup ";   
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {    
				while (rs.next()) {  
					GroupRelate in = getGroupRelateFromRs(rs);
					List<Integer> list= map.get(in.getGroupid());
					if(null == list){ 
						list = new ArrayList<Integer>();
						map.put(in.getGroupid(), list);
					}
					list.add(in.getPgroupid());
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
	  
	 public static GroupRelate getGroupRelate(String id){
		    GroupRelate in = null;  
			Connection conn = DB.getConn();    
			String sql = "select * from mdrelategroup where id = "+id;  
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {   
				while (rs.next()) {
					in = getGroupRelateFromRs(rs);
				} 
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			} 
			return in;

	 }
	 
	 private static GroupRelate getGroupRelateFromRs(ResultSet rs){
		 GroupRelate in = new GroupRelate();
			try {    
				in.setId(rs.getInt("id"));
				in.setGroupid(rs.getInt("groupid"));
				in.setPgroupid(rs.getInt("pgroupid"));
				
			} catch (SQLException e) {
				e.printStackTrace();
			}	 

			return in ;
		}
}
