package wilson.verifyCode;
import java.sql.*;
import java.text.SimpleDateFormat;
import database.*;
import java.util.Date;


public class VerifyCodeManager {
	public boolean saveVerifyCode(String saleOrderNo ,int codeInt,String detail){
		boolean flag = false;
		String sql = ""; 
		//不判断是否已经有这个verifyCode了，直接存
		sql = "insert into verifycode (saleorderno, verifycode,detail,recordtime) VALUES (?,?,?,?)";	
		Connection conn = DB.getConn();
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


		PreparedStatement pstmt = DB.prepare(conn, sql);
		try {
			pstmt.setString(1, saleOrderNo);
			pstmt.setString(2, String.valueOf(codeInt));
			pstmt.setString(3, detail);
			pstmt.setString(4,fmt.format(new Date()));

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
}
