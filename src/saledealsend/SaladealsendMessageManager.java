package saledealsend;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database.DB;

public class SaladealsendMessageManager { 
   public static List<SaledealsendMessage> getlistByid(Saledealsend sa){
	   List<SaledealsendMessage> list = null;
	    Connection conn = DB.getConn();    
		String sql = "select * from saledealsendmessage where saledealsendID = " + sa.getId() ;  
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {   
			while (rs.next()) {
				SaledealsendMessage in =  getSaledealsendMessageFromRs(rs);
				if(null == list){
					list = new ArrayList<SaledealsendMessage>();
				} 
				list.add(in);
			}  
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		} 
		return list ;
   }
   
    
   private static SaledealsendMessage getSaledealsendMessageFromRs(ResultSet rs){
	   SaledealsendMessage in = new SaledealsendMessage();
			try {   
				
				in.setId(rs.getInt("id"));    
				in.setDealsendMessage(rs.getString("dealsendMessage"));
				in.setDealsendprice(rs.getInt("dealsendprice"));
				in.setInstallprice(rs.getInt("installprice"));
				in.setOrderids(rs.getString("orderids"));
				in.setSaledealsendID(rs.getInt("saledealsendID"));
				in.setSendInstallprice(rs.getInt("sendInstallprice"));
				in.setSendprice(rs.getInt("sendprice")); 
			} catch (SQLException e) {
				e.printStackTrace();
			}	  
			return in ;
		}
   
   
}
