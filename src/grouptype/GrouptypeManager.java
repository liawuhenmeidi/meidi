package grouptype;


import group.Group;
import group.GroupManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import branchtype.BranchType;

import user.User;
import user.UserManager;
import database.DB;

public class GrouptypeManager {
	protected static Log logger = LogFactory.getLog(GrouptypeManager.class);
	 
	public static List<Grouptype> getGroup(User user) {
		 
		List<Grouptype> users = new ArrayList<Grouptype>();
		Connection conn = DB.getConn();
		String sql = "";  
		if(UserManager.checkPermissions(user, Group.Manger)){
			sql = "select * from mdgrouptype " ;  
		}
		    
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				Grouptype g = GrouptypeManager.getGroupFromRs(rs);
				users.add(g);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		return users;
	}
	
	public static List<Grouptype> getGrouptype() {
		 
		List<Grouptype> users = new ArrayList<Grouptype>();
		Connection conn = DB.getConn();
		String sql = "select * from mdgrouptype " ;  
		
		    
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				Grouptype g = GrouptypeManager.getGroupFromRs(rs);
				users.add(g);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		return users;
	}
	
	public static boolean  save(String  c){
		Connection conn = DB.getConn();
		String sql = "insert into mdgrouptype(id,gname,statues) values (null, ?,?)";
		PreparedStatement pstmt = DB.prepare(conn, sql);
		try { 
			pstmt.setString(1, c);
			pstmt.setInt(2, 0);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
	   return true;
	}
	public static Grouptype getGrouptype(int id) {
		Grouptype g = new Grouptype();
		Connection conn = DB.getConn();
		String sql = "select * from mdgrouptype where id = " + id ;
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {  
			while (rs.next()) { 
			 g = GrouptypeManager.getGroupFromRs(rs);
			}  
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { 
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		} 
		return g;
	}
	
	
	public static int getgrouptype(int id ) {
		int type = 0;   
		Connection conn = DB.getConn();
		String sql = "select * from mdgrouptype where type = "+ id ;
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {  
			while (rs.next()) {  
				Grouptype branch = getGroupFromRs(rs);
				type = branch.getId(); 
			}  
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		return type;
	}
	
	
	public static int delete(User user,String ptype ) {
		
		 
		List<Group> list = GroupManager.getGroup(user,Integer.valueOf(ptype));

		List<Group> listg = GroupManager.getGroupdown(user, Integer.valueOf(ptype),1);
		 
		  
		 
		if(list != null && list.size() > 0 || listg != null && listg.size() > 0){   
			return -1 ;
		}
		
		int count = 0 ;
		Connection conn = DB.getConn(); 
		String sql = "delete from mdgrouptype where id = " + ptype;
logger.info(sql);  
		Statement stmt = DB.getStatement(conn);

		try {
			count = DB.executeUpdate(stmt, sql);
		} finally {
			DB.close(stmt);
			DB.close(conn);
		}
		return count;
	}
	
	
	
	
	
	public static boolean  update(String  c ,String bid ){
		Connection conn = DB.getConn(); 
		String sql = "update mdgrouptype set gname = ? where id = " + bid ;
		PreparedStatement pstmt = DB.prepare(conn, sql);
		try { 
			pstmt.setString(1, c);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
	   return true;
	}
	
	private static Grouptype getGroupFromRs(ResultSet rs){
		Grouptype g = new Grouptype();
		try {     
			g.setId(rs.getInt("id")); 
			g.setName(rs.getString("gname"));
			g.setStatues(rs.getInt("statues"));
			g.setType(rs.getInt("type"));
		} catch (SQLException e) {
			e.printStackTrace();
		}	 
		return g ;
	}
	
	
}
