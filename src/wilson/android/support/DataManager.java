package wilson.android.support;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import database.DB;

public class DataManager {
	
	protected static Log logger = LogFactory.getLog(DataManager.class);
	
	/**
	 * 
	 * @param username
	 * @param password
	 * @return 	1 invalid user
	 * 		   	2 wrong password
	 * 			0 success
	 */
	public static String login(String username, String password){
		String result = "";
		Connection conn = DB.getConn();
		String sql = "select * from mduser where username = '" + username + "' and statues = 1;";
		logger.info(sql); 
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			if(!rs.next()) {
				result = "failed,invalid user(1)";
			} else {
				if(!password.equals(rs.getString("userpassword"))) {
					result = "failed,wrong password(2)";
				}else{
					result = "success(0)";
				}
			}
			 
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		logger.info("<android_login> user = " + username + ",message = " + result); 
		return result;
	}
}
