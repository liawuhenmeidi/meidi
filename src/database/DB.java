package database;
import java.sql.Connection;  
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import java.sql.SQLException;
import java.sql.Statement;
                      
public class DB {                                  
	public static Connection getConn() {            
		Connection conn = null;                     
		try {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
			Class.forName("com.mysql.jdbc.Driver");                           
			//conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/meidi?user=root&password=liaowuhen&characterEncoding=utf-8");
			//conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/meidi?user=root&password=liaowuhen&characterEncoding=utf-8");
		//	conn = DriverManager.getConnection("jdbc:mysql://liaowuhendgnew.gotoftp3.com/liaowuhendgnew?user=liaowuhendgnew&password=n5uQe590Lmg7&characterEncoding=utf-8");
	 //conn = DriverManager.getConnection("jdbc:mysql://voip022.gotoftp3.com/voip022?user=voip022&password=808080&characterEncoding=utf-8");
     conn = DriverManager.getConnection("jdbc:mysql://liaowuhentest.gotoftp5.com/liaowuhentest?user=liaowuhentest&password=liaowuhen&characterEncoding=utf-8");
			//conn = DriverManager.getConnection("jdbc:mysql://liaowuhendg.gotoftp1.com/liaowuhendg?user=liaowuhendg&password=eWp2046Mdaq76&characterEncoding=utf-8");
		 //   conn = DriverManager.getConnection("jdbc:mysql://liaowuhen.gotoftp3.com/liaowuhen?user=liaowuhen&password=liaowuhen&characterEncoding=utf-8");   //   聚美
		} catch (ClassNotFoundException e) {                    
			e.printStackTrace();                           
		} catch (SQLException e) {                  
			e.printStackTrace();       
		}   
		return conn;  
	}   
	
	public static PreparedStatement prepare(Connection conn,  String sql) {
		PreparedStatement pstmt = null; 
		try {
			if(conn != null) {
				pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pstmt;
	}
	
	public static PreparedStatement prepare(Connection conn,  String sql, int autoGenereatedKeys) {
		PreparedStatement pstmt = null; 
		try {
			if(conn != null) {
				pstmt = conn.prepareStatement(sql, autoGenereatedKeys);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pstmt;
	}
	
	public static Statement getStatement(Connection conn) {
		Statement stmt = null; 
		try {
			if(conn != null) {
				stmt = conn.createStatement();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stmt;
	}
	
	public static ResultSet getResultSet(Statement stmt, String sql) {
		ResultSet rs = null;
		try {
			if(stmt != null) {
				rs = stmt.executeQuery(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static int executeUpdate(Statement stmt, String sql) {
		int i = 0 ;
		try {
			if(stmt != null) {   
				i = stmt.executeUpdate(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return i ;
	}
	
	public static void close(Connection conn) {
		try {
			if(conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(Statement stmt) {
		try {
			if(stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void close(ResultSet rs) {
		try {
			if(rs != null) {
				rs.close();
				rs = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
