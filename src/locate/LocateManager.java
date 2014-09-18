package locate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import category.Category;
import category.CategoryManager;

import user.User;
import user.UserManager;
import database.DB;

public class LocateManager {
	public static boolean  save(String  c){
		Connection conn = DB.getConn();
		String sql = "insert into mdlocate(id,lname) values (null, ?)";
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
	
		public static List<Locate> getLocate() {
			List<Locate> users = new ArrayList<Locate>();
			Connection conn = DB.getConn();
			String sql = "select * from mdlocate";
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {
				while (rs.next()) {
					Locate g = new Locate();
					g.setId(rs.getInt("id"));
					g.setLocateName(rs.getString("lname"));
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

		public static boolean delete(String str ) {
			String ids = "(" + str + ")";
			boolean b = false; 
			Connection conn = DB.getConn();
			String sql = "delete from mdlocate where id in " + ids;
			System.out.println(sql);
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
}
