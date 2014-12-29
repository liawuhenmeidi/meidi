package branch;

import group.GroupService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import utill.DBUtill;
import utill.StringUtill;

import database.DB;
  
public class BranchManager {
	
	 protected static Log logger = LogFactory.getLog(BranchManager.class);
	
	 public static boolean  save(Branch branch ){
        List<String> listsql = new ArrayList<String>();
		String sql = "";  
		if(branch.getId() == 0){   
			sql = "insert into mdbranch(id,bname,pid,bmessage,relatebranch) values (null, '"+branch.getLocateName()+"','"+branch.getPid()+"','"+branch.getMessage()+"','"+branch.getBranchids()+"')";
		}else{     
			sql = "update mdbranch set bname = '"+branch.getLocateName()+"' , bmessage = '"+branch.getMessage()+"' , relatebranch = '"+branch.getBranchids()+"' where id = "+ branch.getId();
		}
		
		String sqlall = "delete from relatebranch where branchid = " + branch.getId();
		
		listsql.add(sqlall);
		
		if(!StringUtill.isNull(branch.getBranchids())){
			String[] strlist = branch.getBranchids().split("_");
			for(int i=0;i<strlist.length;i++){
				String id = strlist[i];
				String sql1 = "insert into relatebranch (id,branchid,relatebranchid) values (null,"+branch.getId()+",'"+id+"') ";
			    listsql.add(sql1);
			}
		}
		
	    listsql.add(sql);
	    
	    DBUtill.sava(listsql);
		BranchService.flag = true ;
		
	   return true;
	}
	 
	 public static String save(){
		 String sql = "insert into  mdgroup( id ,groupname, detail,statues, permissions, products,pid,ptype) VALUES (null,?,?,?,?,?,?,?)";
		 GroupService.flag = true ;
		 return sql ;
	 }
	 
	 
		public static List<Branch> getLocate(String id ) {
			List<Branch> users = new ArrayList<Branch>();
			Connection conn = DB.getConn();
			String sql = "select * from mdbranch where pid = "+ id +" and disable = 0 ";
			Statement stmt = DB.getStatement(conn); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) { 
					Branch g = getBranchFromRs(rs);
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
          
		public static int getcount(String id ) {
			int count = 0;   
			Connection conn = DB.getConn();
			String sql = "select * from mdbranch where pid in "+ id ;
			Statement stmt = DB.getStatement(conn); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) { 
					count++; 
				}  
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			} 
			return count;
		}
		
	
		
		
		public static List<Branch> getLocate(int statues ) {
			List<Branch> users = new ArrayList<Branch>();
			Connection conn = DB.getConn(); 
			String sql = "select * from mdbranch where pid in (select id from mdbranchtype where statues = "+statues+")" ;
			Statement stmt = DB.getStatement(conn); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) { 
					Branch g = getBranchFromRs(rs);
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
		
		public static List<String> getLocateAll( ) {
			List<String> users = new ArrayList<String>();
			Connection conn = DB.getConn();   
			String sql = "select * from mdbranch where disable = 0 " ;
			Statement stmt = DB.getStatement(conn);  
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {    
				while (rs.next()) {  
					Branch g = getBranchFromRs(rs);
					users.add(g.getLocateName()); 
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
		
		public static List<String> getLocateAllIsable( ) {
			List<String> users = new ArrayList<String>();
			Connection conn = DB.getConn();   
			String sql = "select * from mdbranch where disable = 0 " ;
			Statement stmt = DB.getStatement(conn); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {    
				while (rs.next()) {  
					Branch g = getBranchFromRs(rs);
					users.add(g.getLocateName()); 
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
		
		public static List<Branch> getLocate( ) {
			List<Branch> users = new ArrayList<Branch>();
			Connection conn = DB.getConn();   
			String sql = "select * from mdbranch  " ;
			Statement stmt = DB.getStatement(conn); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) {  
					Branch g = getBranchFromRs(rs);
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
		
		public static Branch getLocatebyname(String name ) {
			Branch branch = new Branch();
			Connection conn = DB.getConn(); 
			String sql = "select * from mdbranch where bname = '"+ name + "'" ;
			Statement stmt = DB.getStatement(conn); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) { 
					branch = getBranchFromRs(rs);
					
				}   
			} catch (SQLException e) {
				e.printStackTrace();
			} finally { 
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			}
			return branch;
		}
		  
		public static int getBranchID(String name ) {
		    int id = 0 ; 
			Connection conn = DB.getConn(); 
			String sql = "select * from mdbranch where bname = '"+ name + "'" ;
			Statement stmt = DB.getStatement(conn); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) { 
					Branch branch = getBranchFromRs(rs);
					id = branch.getId();
				}   
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {  
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			}
			return id; 
		}
		
		public static int update(String bid,String statues ) {
		    int id = 0 ; 
			Connection conn = DB.getConn();  
			String sql = " update mdbranch set statues = " + statues + " where id = " + bid  ;
			PreparedStatement pstmt = DB.prepare(conn, sql);
			try {  
				pstmt.executeUpdate();
				BranchService.flag = true ;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {  		
				DB.close(pstmt);
				DB.close(conn);
			}
			return id; 
		} 
		
		public static Branch getLocatebyid(String id ) {
			Branch branch = new Branch();  
			Connection conn = DB.getConn(); 
			String sql = "select * from mdbranch where id = "+ id + "" ;
			Statement stmt = DB.getStatement(conn); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) { 
					branch = getBranchFromRs(rs);
					
				}   
			} catch (SQLException e) {
				e.printStackTrace();
			} finally { 
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			}
			return branch;
		}
		
		public static Map<String,List<String>> getLocateMap() {
			Map<String,List<String>> map = new HashMap<String,List<String>>();
			Connection conn = DB.getConn();
			String sql = "select * from mdbranch ";
			Statement stmt = DB.getStatement(conn); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) { 
					int id = rs.getInt("pid");
					List<String> list = map.get(id+""); 
					if(list == null){ 
						list = new ArrayList<String>();
						map.put(id+"", list);
					}     
					list.add(rs.getString("bname"));
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
		
		public static Map<Integer,Branch> getIdMap() {
			Map<Integer,Branch> map = new HashMap<Integer,Branch>();
			Connection conn = DB.getConn(); 
			String sql = "select * from mdbranch ";
logger.info(sql);			
			Statement stmt = DB.getStatement(conn); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) { 
					Branch branch = getBranchFromRs(rs); 
					map.put(branch.getId(),branch);   
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
		
		public static Map<String,Branch> getNameMap() {
			Map<String,Branch> map = new HashMap<String,Branch>();
			Connection conn = DB.getConn(); 
			String sql = "select * from mdbranch ";
			Statement stmt = DB.getStatement(conn); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) { 
					Branch branch = getBranchFromRs(rs); 
					map.put(branch.getLocateName(),branch);   
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
		
		public static Map<String,List<Branch>> getLocateMapBranch() {
			Map<String,List<Branch>> map = new HashMap<String,List<Branch>>();
			Connection conn = DB.getConn();
			String sql = "select * from mdbranch where disable = 0  ";
			Statement stmt = DB.getStatement(conn); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) { 
					int id = rs.getInt("pid");
					List<Branch> list = map.get(id+""); 
					if(list == null){ 
						list = new ArrayList<Branch>();
						map.put(id+"", list);
					}     
					list.add(getBranchFromRs(rs));  
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
		
		public static boolean delete(String str ) {
			
			String ids = "(" + str + ")"; 
			boolean b = false;  
			Connection conn = DB.getConn();  
			String sql = "update mdbranch set disable = 1 where id in " + ids;
logger.info(sql); 
			Statement stmt = DB.getStatement(conn);
			try {
				DB.executeUpdate(stmt, sql);
				BranchService.flag = true ;
				b = true;
			} finally {
				DB.close(stmt);
				DB.close(conn);
			}  
			return b;
		}
		 
		public static boolean isname(String name){
			boolean flag = false ;
			Connection conn = DB.getConn();
			String sql = "select * from mdbranch where bname = '"+ name +"'";
			logger.info(sql);
			Statement stmt = DB.getStatement(conn); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {    
				while (rs.next()) {  
					flag = true ;
				}  
			} catch (SQLException e) {
				e.printStackTrace();
			} finally { 
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			}
			return flag; 
		}
		
		
		private static Branch getBranchFromRs(ResultSet rs){
			Branch branch= new Branch();
			try {   
				branch.setId(rs.getInt("id"));  
				branch.setLocateName(rs.getString("bname"));
				branch.setPid(rs.getInt("pid")); 
				branch.setMessage(rs.getString("bmessage")); 
				branch.setStatues(rs.getInt("statues"));
				branch.setBranchids(rs.getString("relatebranch"));
				branch.setDisable(rs.getInt("disable"));
			} catch (SQLException e) {
				e.printStackTrace();
			}	
			return branch ;
		}	
		
		public  static String getNameById(int id){
			return BranchService.getMap().get(id).getLocateName();
		}
}



