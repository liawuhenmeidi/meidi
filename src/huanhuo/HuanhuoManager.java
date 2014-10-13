package huanhuo;

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

import database.DB;
  
public class HuanhuoManager {
	
	 protected static Log logger = LogFactory.getLog(HuanhuoManager.class);
	
	 public static boolean  save(HuanHuo huanhuo ){
		  
		Connection conn = DB.getConn(); 
		String sql = "insert into mdhuanhuo(id,orderid,uid,type,statues) values (null,?,?,?,?)";
		PreparedStatement pstmt = DB.prepare(conn, sql);
		try {   	
			pstmt.setInt(1, huanhuo.getOrderid());
			pstmt.setInt(2, huanhuo.getUid());
			pstmt.setInt(3, huanhuo.getType());
			pstmt.setInt(4, huanhuo.getStatues());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
	   return true;
	}
	

		public static int update(String bid,String statues ) {
		    int id = 0 ; 
			Connection conn = DB.getConn();  
			String sql = " update mdhuanhuo set statues = ? where id = ?";
			PreparedStatement pstmt = DB.prepare(conn, sql);
			try {   
				pstmt.setInt(1, Integer.valueOf(statues));
				pstmt.setInt(1, Integer.valueOf(bid)); 
				id = pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {  		
				DB.close(pstmt);
				DB.close(conn);
			}
			return id; 
		} 
		
		public static HuanHuo getLocatebyid(String ids ) {
			HuanHuo HuanHuo = new HuanHuo();  
			Connection conn = DB.getConn(); 
			String sql = "select * from mdhuanhuo where id in "+ ids ;
			Statement stmt = DB.getStatement(conn); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) { 
					HuanHuo = getHuanHuoFromRs(rs);	
				}   
			} catch (SQLException e) {
				e.printStackTrace();
			} finally { 
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			}
			return HuanHuo;
		}
		
		
		
		public static Map<Integer,HuanHuo> getNameMap() {
			Map<Integer,HuanHuo> map = new HashMap<Integer,HuanHuo>();
			Connection conn = DB.getConn(); 
			String sql = "select * from mdhuanhuo ";
			Statement stmt = DB.getStatement(conn); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) { 
					HuanHuo HuanHuo = getHuanHuoFromRs(rs); 
					map.put(HuanHuo.getId(),HuanHuo);   
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
		
		public static Map<String,List<HuanHuo>> getOidMapHuanHuo() {
			Map<String,List<HuanHuo>> map = new HashMap<String,List<HuanHuo>>();
			Connection conn = DB.getConn();
			String sql = "select * from mdhuanhuo ";
			Statement stmt = DB.getStatement(conn); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) { 
					
					HuanHuo huanhuo = getHuanHuoFromRs(rs);
					List<HuanHuo> list = map.get(huanhuo.getOrderid()+""); 
					if(list == null){ 
						list = new ArrayList<HuanHuo>();
						map.put(huanhuo.getOrderid()+"", list);
					}     
					list.add(huanhuo);  
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
		
		public static boolean delete(String ids ) {
			boolean b = false;   
			Connection conn = DB.getConn();  
			String sql = "delete mdhuanhuo  where id in " + ids;
logger.info(sql); 
			Statement stmt = DB.getStatement(conn);
			try {
				DB.executeUpdate(stmt, sql);
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
			String sql = "select * from mdHuanHuo where bname = '"+ name +"'";
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
		
		
		private static HuanHuo getHuanHuoFromRs(ResultSet rs){
			HuanHuo huanhuo= new HuanHuo();
			try {   
				huanhuo.setId(rs.getInt("id"));  
				huanhuo.setOrderid(rs.getInt("orderid"));
				huanhuo.setStatues(rs.getInt("statues"));
				huanhuo.setType(rs.getInt("type"));
				huanhuo.setUid(rs.getInt("uid"));
			} catch (SQLException e) {
				e.printStackTrace();
			}	
			return huanhuo ;
		}	
}



