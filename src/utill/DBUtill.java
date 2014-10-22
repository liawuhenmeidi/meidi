package utill;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.sql.PreparedStatement;
 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 
import database.DB;

public class DBUtill {
  protected static Log logger = LogFactory.getLog(DBUtill.class);
   
  public static boolean sava(List<String> sqls){ 
	  boolean flag = false ; 
	    Connection conn = DB.getConn();    
	    Statement sm = null;  
      try {     
          // 事务开始   
          logger.info("事物处理开始") ;
          conn.setAutoCommit(false);   // 设置连接不自动提交，即用该连接进行的操作都不更新到数据库  
          sm = conn.createStatement(); // 创建Statement对象  
           Object[] strsqls = sqls.toArray();
           
          //依次执行传入的SQL语句      
          for (int i = 0; i < strsqls.length; i++) {
        	  String sql = (String)strsqls[i];
        	  if(!StringUtill.isNull(sql)){   
        		  logger.info(sql);
        		  sm.execute(sql);// 执行添加事物的语句  
        	  } 
             
          }  
          logger.info("提交事务处理！");  
             
          conn.commit();   // 提交给数据库处理   
             
          logger.info("事务处理结束！");  
          // 事务结束   
           flag = true ;   
      //捕获执行SQL语句组中的异常      
      } catch (SQLException e) {  
          try {   
              logger.info("事务执行失败，进行回滚！\n",e);  
              conn.rollback(); // 若前面某条语句出现异常时，进行回滚，取消前面执行的所有操作  
          } catch (SQLException e1) {  
              logger.info(e);
          }  
      } finally {   
          try { 
				sm.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}  
      }  
      
      return flag ;
  }
  
  public static boolean savaPreparedStatement(Connection conn,List<PreparedStatement> sqls){ 
	   boolean flag = false ; 
      try {     
          // 事务开始   
          logger.info("事物处理开始") ; 
          conn.setAutoCommit(false);   // 设置连接不自动提交，即用该连接进行的操作都不更新到数据库  
         
          //依次执行传入的SQL语句      
          for (int i = 0; i < sqls.size(); i++) {
        	 
        	 sqls.get(i).executeUpdate();
             
          }  
          logger.info("提交事务处理！");  
             
          conn.commit();   // 提交给数据库处理   
             
          logger.info("事务处理结束！");  
          // 事务结束   

      //捕获执行SQL语句组中的异常      
      } catch (SQLException e) {  
          try {   
              logger.info("事务执行失败，进行回滚！\n",e);  
              conn.rollback(); // 若前面某条语句出现异常时，进行回滚，取消前面执行的所有操作  
          } catch (SQLException e1) {  
              logger.info(e);
          }  
      } finally {   
          try { 
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}  
      }  
      
      return flag ;
  }
  
  
  public static boolean PreparedStatement(Connection conn, PreparedStatement sql1, PreparedStatement sql2, PreparedStatement sql3){ 
	   boolean flag = false ; 
     try {     
         // 事务开始   
         logger.info("事物处理开始") ;
         conn.setAutoCommit(false);   // 设置连接不自动提交，即用该连接进行的操作都不更新到数据库  
        
         //依次执行传入的SQL语句      
      logger.info(sql1);
         sql1.executeUpdate();
         
         ResultSet rsKey = sql1.getGeneratedKeys();
		 rsKey.next();
		 int key = rsKey.getInt(1);
		  logger.info(sql2);  
		 sql2.setInt(6, key); 
		  logger.info(sql2);  
		 sql2.executeUpdate();
		  logger.info(sql3); 
		 sql3.executeUpdate();
         
         logger.info("提交事务处理！");  
            
         conn.commit();   // 提交给数据库处理   
            
         logger.info("事务处理结束！");  
         // 事务结束   

     //捕获执行SQL语句组中的异常      
     } catch (SQLException e) {  
         try {   
             logger.info("事务执行失败，进行回滚！\n",e);  
             conn.rollback(); // 若前面某条语句出现异常时，进行回滚，取消前面执行的所有操作  
         } catch (SQLException e1) {  
             logger.info(e);
         }  
     } finally {   
         try { 
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}  
     }  
     
     return flag ;
 }
  
}
