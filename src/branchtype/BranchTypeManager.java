package branchtype;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import utill.DBUtill;
import utill.StringUtill;

import branch.BranchManager;

import database.DB;
  
public class BranchTypeManager {
	  
	 protected static Log logger = LogFactory.getLog(BranchTypeManager.class);
	
	 public static boolean  update(String  c ,String bid ,String typestatues,String exportmodel){
			String sql = "";   
			if(StringUtill.isNull(bid)){    
				sql = "insert into mdbranchtype(id,bname,typestatues,exportmodel) values (null, '"+c+"',"+typestatues+","+exportmodel+")";
			}else {    
				sql = "update mdbranchtype set bname = '"+c+"', typestatues = '"+typestatues+"',saletype = "+exportmodel+" where id = " +bid;
			}    
			BranchTypeService.flag = true ;       
			DBUtill.sava(sql);
		   return true; 
		}
	  
	  
	 public static boolean  update(int  statues ,String bid ){
			Connection conn = DB.getConn(); 
			String sql = "update mdbranchtype set statues = ? where id = " + bid ;
			PreparedStatement pstmt = DB.prepare(conn, sql);
			try { 
				pstmt.setInt(1, statues);  
				pstmt.executeUpdate();
				BranchTypeService.flag = true ; 
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(pstmt);
				DB.close(conn);
			}
		   return true;
		}
	 

		public static List<BranchType> getLocate() {
			List<BranchType> users = new ArrayList<BranchType>();
			Connection conn = DB.getConn(); 
			String sql = "select * from mdbranchtype order by  id";
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) { 
					BranchType g = getBranchFromRs(rs);
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
       
		public static List<BranchType> getLocatelist(int typestatues) {
			List<BranchType> users = new ArrayList<BranchType>() ;
			Connection conn = DB.getConn();  
			String sql = "select * from mdbranchtype where typestatues = "+typestatues+" order by  id";
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {   
				while (rs.next()) { 
					BranchType g = getBranchFromRs(rs);
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
		
		public static BranchType getLocate(int id) {
			BranchType g = new BranchType();
			Connection conn = DB.getConn();
			String sql = "select * from mdbranchtype where id = " + id ;
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) { 
					g = getBranchFromRs(rs);
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
		
		
		public static boolean delete(String str ) {
			String ids = "(" + str + ")";
			boolean b = false;
			int count = BranchManager.getcount(ids);
			if(count > 0){
				return false ;
			}
			 
			Connection conn = DB.getConn();
			String sql = "delete from mdbranchtype where id in " + ids;
logger.info(sql);
			Statement stmt = DB.getStatement(conn);
			try {
				DB.executeUpdate(stmt, sql);
				BranchTypeService.flag = true ; 
				b = true;
			} finally {
				DB.close(stmt);
				DB.close(conn);
			}  
			return b;
		}

		private static BranchType getBranchFromRs(ResultSet rs){
			BranchType branch= new BranchType();
			try {      
				branch.setId(rs.getInt("id"));    
				branch.setName(rs.getString("bname"));
				branch.setStatues(rs.getInt("statues")); 
				branch.setIsSystem(rs.getInt("isSystem"));
				branch.setTypestatues(rs.getInt("typestatues")); 
				branch.setExportmodel(rs.getInt("exportmodel")); 
				branch.setSaletype(rs.getInt("saletype")); 
			} catch (SQLException e) {  
				e.printStackTrace();
			}	 
			return branch ;
		}	
		
		public static String getNameById(int id){
			return getLocate(id).getName();
		}
		
}
