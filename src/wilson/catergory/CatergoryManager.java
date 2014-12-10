package wilson.catergory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import utill.TimeUtill;
import wilson.upload.UploadManager;

import database.DB;

public class CatergoryManager {
	protected static Log logger = LogFactory.getLog(CatergoryManager.class);
	
	//临时的
	public static String getCatergoryMapingByUploadOrderName(String uploadorderName){
		String result = "";
		Connection conn = DB.getConn(); 
		String sql = "select distinct filename from uploadorder where name = '" + uploadorderName + "'";
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		logger.info(sql);
		try {
			while (rs.next()) {
				result = rs.getString("filename");
				if(result.endsWith("xls")){
					continue;
				}
				if(getCatergory(result).size() > 0){
					return result;
				}
			}	
			result = "";
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return result;
	}
	
	
	public static boolean  updateCatergoryMaping(CatergoryMaping cm){
		//判断是否存在
		CatergoryMaping base = getCatergory(cm.getName(),cm.getShop());
		
		//已经有的catergoryMaping，需要更新
		boolean result = false;
		String sql= "update catergorymaping set name=?,shop=?,content=?,modifytime=? where id = " + base.getId();	
		Connection conn = DB.getConn();
		
		PreparedStatement pstmt = DB.prepare(conn, sql);
		try {
			pstmt.setString(1, cm.getName());
			pstmt.setString(2, cm.getShop());
			pstmt.setString(3, cm.getContent());
			pstmt.setString(4, cm.getModifyTime());
			pstmt.executeUpdate();
			result = true;
			logger.info(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return result;
		
		
		
	}
	
	public static boolean addCatergoryMaping(CatergoryMaping cm){

		boolean result = false;
		Connection conn = DB.getConn(); 
		String sql = "insert into catergorymaping values(null,?,?,?,?)";
		PreparedStatement pstmt = DB.prepare(conn, sql);
		
		try {
			pstmt.setString(1, cm.getName());
			pstmt.setString(2, cm.getShop());
			pstmt.setString(3, cm.getContent());
			pstmt.setString(4, cm.getModifyTime());
			pstmt.executeUpdate();
			logger.info(sql);
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}	
		return result;

	}
	
	public static boolean addCatergoryMaping(String categoryName, String filename){

		boolean result = false;
		Connection conn = DB.getConn(); 
		
		List<String> shopNameList = UploadManager.getShopNameListFromFileName(filename);
		if(!shopNameList.contains("")){
			shopNameList.add("");
		}
		
		String sql = "insert into catergorymaping values(null,?,?,?,?)";
		PreparedStatement pstmt = DB.prepare(conn, sql);
		
		try {
			for(int i = 0 ; i < shopNameList.size() ; i ++){
				pstmt.setString(1, categoryName);
				pstmt.setString(2, shopNameList.get(i));
				pstmt.setString(3, "");
				pstmt.setString(4, TimeUtill.gettime());
				pstmt.executeUpdate();
			}	
			logger.info(sql);
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}	
		return result;

	}
	
	public static boolean  delCatergoryMaping(String name,String shop){
		boolean result = false;

		String sql= "delete from catergorymaping where name = '" + name + "' and shop = '" + shop +"'";	
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn); 
		try {
			stmt.executeUpdate(sql);
			result = true;
			logger.info(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(stmt);
			DB.close(conn);
		}
		return result;

	}
	
	public static boolean  delCatergoryMaping(String name){
		boolean result = false;

		String sql= "delete from catergorymaping where name = '" + name + "'";	
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn); 
		try {
			stmt.executeUpdate(sql);
			result = true;
			logger.info(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(stmt);
			DB.close(conn);
		}
		return result;

	}
	
	public static CatergoryMaping getCatergory(String name,String shop){
		CatergoryMaping result  = new CatergoryMaping();

		Connection conn = DB.getConn(); 
		String sql = "select * from catergorymaping where name = '" + name + "' and shop = '" + shop + "'";
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		logger.info(sql);
		try {
			while (rs.next()) {
				result = getCategoryMapingFromRs(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return result;
	}
	
	public static CatergoryMaping getCatergoryByid(String id){
		CatergoryMaping result  = new CatergoryMaping();

		Connection conn = DB.getConn(); 
		String sql = "select * from catergorymaping where id = " + id;
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		logger.info(sql);
		try {
			while (rs.next()) {
				result = getCategoryMapingFromRs(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return result;
	}
	
	public static List<CatergoryMaping> getCatergory(String name){
		List<CatergoryMaping> result  = new ArrayList<CatergoryMaping>();

		Connection conn = DB.getConn(); 
		String sql = "select * from catergorymaping where name = '" + name + "'";
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		logger.info(sql);
		CatergoryMaping temp = new CatergoryMaping();
		try {     
			while (rs.next()) {
				temp = getCategoryMapingFromRs(rs);
				result.add(temp);
				temp = new CatergoryMaping();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}	
		return result;
	}
	
	public static HashMap<String, List<CatergoryMaping>> getCatergoryMap(){
		HashMap<String,List<CatergoryMaping>> result = new HashMap<String,List<CatergoryMaping>>();
		Connection conn = DB.getConn();
		String sql = "select * from catergorymaping";
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		logger.info(sql);
		
		List<CatergoryMaping> tempList = new ArrayList<CatergoryMaping>();
		try {
			while (rs.next()) { 
				CatergoryMaping cm = getCategoryMapingFromRs(rs);
				if(result.containsKey(cm.getName())){
					tempList = new ArrayList<CatergoryMaping>();
					tempList = result.get(cm.getName());
					tempList.add(cm);
					result.put(cm.getName(), tempList);
					
				}else{
					tempList = new ArrayList<CatergoryMaping>();
					tempList.add(cm);
					result.put(cm.getName(), tempList);
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		return result;
	}
	
	public static CatergoryMaping getCategoryMapingFromRs(ResultSet rs ) throws SQLException{
		CatergoryMaping result = new CatergoryMaping();
		result.setId(rs.getInt("id"));
		result.setName(rs.getString("name"));
		result.setShop(rs.getString("shop"));
		result.setContent(rs.getString("content"));
		result.setModifyTime(rs.getString("modifytime"));
		return result;
	}
}
