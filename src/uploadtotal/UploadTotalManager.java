package uploadtotal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import utill.DBUtill;

import company.CompanyManager;

import database.DB;

public class UploadTotalManager {
		 protected static Log logger = LogFactory.getLog(CompanyManager.class);

		 public static boolean  save(List<UploadTotal>  list){
			List<String> sqls = new ArrayList<String>();
			if(null != list && list.size() > 0){
				for(int i=0;i<list.size();i++){
					UploadTotal up = list.get(i);
					String sql = "insert into mduploadtotal(id,name,type,branchname,count,totalcount,tatalbreakcount) values (null,'"+up.getName()+"','"+up.getType()+"','"+up.getBranchname()+"',"+up.getCount()+",'"+up.getTotalcount()+"','"+up.getTatalbreakcount()+"')";
					sqls.add(sql);  
				}
			}
			return DBUtill.sava(sqls); 
		 }  
		  
		/* public static boolean  update(UploadTotal  in){
			 List<String> sqls = new ArrayList<String>();
				//String sql = "update installSale(id,uid,uname,phone,locate,andate,message) values ("+in.getId()+","+in.getUid()+","+in.getUname()+","+in.getPhone()+","+in.getLocate()+","+in.getAndate()+",'"+in.getMessage()+"')";
				String sql = "update installSale set uid = "+in.getUid()+" ,uname ="+in.getUname()+" ,phone ="+in.getPhone()+",locate ="+in.getLocate()+" ,andate ="+in.getAndate()+" ,message = '"+in.getMessage()+"' where id = " +in.getId()  ;
				//logger.info(sql); 
				String sqld = " delete from installsaleMessage where installsaleID = " + in.getId(); 
				sqls.add(sqld);  
				List<InstallSaleMessage> list = in.getList();
				for(int i=0;i<list.size();i++){
					InstallSaleMessage ins = list.get(i); 
					String sql1 = " insert into installsaleMessage (id,installsaleID,categoryID,productID,dealsend) values (null,"+ins.getInstallsaleID()+",'"+ins.getCategoryID()+"',"+ins.getProductID()+","+ins.getDealsend()+")";
				    sqls.add(sql1);
				} 
				
				sqls.add(sql);  
				
				return DBUtill.sava(sqls); 
			 }*/ 
		  
		 public static int getmaxid(){
			    int count = 0 ; 
			    Connection conn = DB.getConn();
				Statement stmt = DB.getStatement(conn);
			
				String  sql = "select max(id) as id from installSale" ;
				ResultSet rs = DB.getResultSet(stmt, sql); 
				try { 
					while (rs.next()) {
						count = rs.getInt("id");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DB.close(stmt);
					DB.close(rs);
					DB.close(conn);
				 }
				return count; 
		 } 
		 
		 
		  
		 public static UploadTotal getUploadTotal(String id){
			    UploadTotal in = null; 
				Connection conn = DB.getConn();    
				String sql = "select * from installSale where id = "+id;  
				Statement stmt = DB.getStatement(conn);
				ResultSet rs = DB.getResultSet(stmt, sql);
				try {   
					while (rs.next()) {
						in = getUploadTotalFromRs(rs);
					} 
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DB.close(rs);
					DB.close(stmt);
					DB.close(conn);
				} 
				return in;

		 }
		 
		 private static UploadTotal getUploadTotalFromRs(ResultSet rs){
			    UploadTotal in = new UploadTotal();
				try {   
					in.setId(rs.getInt("id"));
					in.setBranchname(rs.getString("branchname"));
					in.setCount(rs.getInt("count"));
					in.setName(rs.getString("name"));
					in.setTatalbreakcount(rs.getDouble("tatalbreakcount"));
					in.setTotalcount(rs.getDouble("totalcount"));
					in.setType(rs.getString("type"));
				} catch (SQLException e) {
					e.printStackTrace();
				}	 

				return in ;
			}

	
}
