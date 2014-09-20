package message;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import user.User;


import database.DB;
  
public class MessageManager {
	
	 protected static Log logger = LogFactory.getLog(MessageManager.class);
	
	 public static boolean  save(User user,Message message ){
		Message messa = MessageManager.getMessagebyoid(message.getOid()+""); 
		Connection conn = DB.getConn(); 
		String sql = "";  
		if(messa == null){    
			 sql = "insert into mdmessage(oid,mdmessage) values ('"+message.getOid()+"','"+message.getMessage()+"')";
		}else{     
			sql = "update mdmessage set mdmessage = '"+messa.getMessage()+":"+message.getMessage()+"\n"+"'  where oid = "+message.getOid() ;
		}
		  
		PreparedStatement pstmt = DB.prepare(conn, sql);
		try {   
			//pstmt.setString(1, Message.getLocateName());    
			//pstmt.setInt(2,Integer.valueOf(Message.getPid()));
			//pstmt.setString(3, Message.getMessage());
	logger.info(pstmt);		
			pstmt.executeUpdate();
		} catch (SQLException e) { 
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
	   return true;
	}	
		public static Message getMessagebyoid(String id ) {
			Message Message = null;  
			Connection conn = DB.getConn(); 
			String sql = "select * from mdmessage where oid = "+ id + "" ;
			Statement stmt = DB.getStatement(conn); 
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {  
				while (rs.next()) { 
					Message = getMessageFromRs(rs);
					
				}   
			} catch (SQLException e) {
				e.printStackTrace();
			} finally { 
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			}
			return Message;
		}
		
		
		
		public static boolean delete(String str ) {
			String ids = "(" + str + ")";
			boolean b = false;
			Connection conn = DB.getConn();
			String sql = "delete from mdMessage where id in " + ids;
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
			String sql = "select * from mdMessage where bname = '"+ name +"'";
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
		
		
		private static Message getMessageFromRs(ResultSet rs){
			Message Message= new Message();
			try {   
				Message.setOid(rs.getInt("oid"));  
				
				Message.setMessage(rs.getString("mdmessage")); 
			} catch (SQLException e) {
				e.printStackTrace();
			}	
			return Message ;
		}	
		
		
		
		
}



