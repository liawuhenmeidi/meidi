package uploadtotalgroup;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import utill.DBUtill;


import database.DB;

public class UploadTotalGroupManager {
	protected static Log logger = LogFactory.getLog(UploadTotalGroupManager.class);

	 public static boolean  save(UploadTotalGroup  up){
		List<String> sqls = new ArrayList<String>();
		if(null != up){
			    String sql1 = "delete from mduploadtotalgroup" ;
				String sql = "insert into mduploadtotalgroup(id,name,categoryname) values (null,'"+up.getName()+"','"+up.getCategoryname()+"')";
				sqls.add(sql1); 
				sqls.add(sql);   
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
	  
	 public static UploadTotalGroup getUploadTotalGroup(){
		 UploadTotalGroup in = null; 
			Connection conn = DB.getConn();    
			String sql = "select * from mduploadtotalgroup";  
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {   
				while (rs.next()) {
					in = getUploadTotalGroupFromRs(rs);
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
	 
	 private static UploadTotalGroup getUploadTotalGroupFromRs(ResultSet rs){
		 UploadTotalGroup in = new UploadTotalGroup();
			try {   
				in.setId(rs.getInt("id"));
				
				in.setName(rs.getString("name"));
				in.setCategoryname(rs.getString("categoryname"));
			} catch (SQLException e) {
				e.printStackTrace();
			}	 

			return in ;
		}

}
