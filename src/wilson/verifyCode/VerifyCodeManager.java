package wilson.verifyCode;
import java.sql.*;
import java.text.SimpleDateFormat;
import database.*;

import java.util.Date;

//status -> 0 已经消了
//status -> 1 正在消


public class VerifyCodeManager {
	public boolean saveVerifyCode(String saleOrderNo ,int codeInt,String detail,int statues){
		boolean flag = false;
		String sql = ""; 
		//不判断是否已经有这个verifyCode了，直接存
		sql = "insert into verifycode (saleorderno, verifycode,detail,recordtime,statues) VALUES (?,?,?,?,?)";	
		Connection conn = DB.getConn();
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		detail = detail.substring(0,detail.length()>240?240:detail.length());

		PreparedStatement pstmt = DB.prepare(conn, sql);
		try {
			pstmt.setString(1, saleOrderNo);
			pstmt.setString(2, String.valueOf(codeInt));
			pstmt.setString(3, detail);
			pstmt.setString(4,fmt.format(new Date()));
			pstmt.setInt(5, statues);

			int count = pstmt.executeUpdate();
			if(count > 0){
				flag = true ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
    return flag ;  
	}
	
	public boolean updateVerifyCode(String saleOrderNo ,int codeInt,String detail,int statues){
		boolean flag = false;
		String sql = ""; 
		
		//sql = "insert into verifycode (saleorderno, verifycode,detail,recordtime) VALUES (?,?,?,?)";	
		sql = "update verifycode set statues = ? ,verifycode = ? where saleorderno = " + saleOrderNo;
		Connection conn = DB.getConn();

		PreparedStatement pstmt = DB.prepare(conn, sql);
		try {
			pstmt.setInt(1,statues);
			pstmt.setString(2, String.valueOf(codeInt));
			int count = pstmt.executeUpdate();
			if(count > 0){
				flag = true ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
		return flag ;  
	}
	
	public String getDoingVerifyCode(){
		String result = "";
		Connection conn = DB.getConn(); 
		String sql = "select * from verifycode where statues = 1";

		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {     
			while (rs.next()) {
				result += rs.getString("saleorderno") + ",";
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
}
