package installsale;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Logger;

import order.Order;
import orderproduct.OrderProduct;

import database.DB;

public class InstallSaleMessageManager {
	
	 public static Map<String,List<InstallSaleMessage>> getmap(){
		    HashMap<String,List<InstallSaleMessage>> map = new HashMap<String,List<InstallSaleMessage>>(); 
		    List<InstallSaleMessage> list = null;
			Connection conn = DB.getConn();    
			String sql = "select * from installsaleMessage" ;  
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {   
				while (rs.next()) {
					InstallSaleMessage in = getInstallSaleFromRs(rs);
					list = map.get(in.getInstallsaleID()+"");
					if(null == list){
						list = new ArrayList<InstallSaleMessage>();
						map.put(in.getInstallsaleID()+"", list);
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
			return map;

	 }
	 
	 /*public static int getprice(List<InstallSaleMessage> list , Order o ){
		 int cprice = 0 ;
		 for(int i=0;i<list.size();i++){ 
			 InstallSaleMessage in = list.get(i);
			 List<OrderProduct> listop = o.getOrderproduct();
			 for(int j=0;j<listop.size();j++){ 
				 OrderProduct op = listop.get(j); 
				 if((op.getId()+"").equals(in.getProductID()+"")){
					 cprice += in.getDealsend();
				 }else if((op.getCategoryId()+"").equals(in.getCategoryID())){
					 cprice += in.getDealsend();
				 }
			 }
			 
		 }
		 
		 
		 return cprice ;
	 } */
	 
	 public static int getprice(List<InstallSaleMessage> list , Order o ){
		 int price = 0 ;
		 List<OrderProduct> listop = o.getOrderproduct();
		 //System.out.println(listop.size()); 
		 for(int j=0;j<listop.size();j++){
			     OrderProduct op = listop.get(j);
			     if(op.getStatues() == 0 ){
			    	 int cprice = 0 ;
				     int pprice = 0 ;
				     boolean flag = false ;
				     if(null != list){ 
				    	 for(int i=0;i<list.size();i++){ 
							 InstallSaleMessage in = list.get(i);
							 if(in.getProductID() != 0){
								 if((in.getProductID()+"").equals(op.getSendType())){
									 pprice = in.getDealsend()*op.getCount();
									 flag = true ;
								 } 
							 }else if((op.getCategoryId()+"").equals(in.getCategoryID())){
								 cprice = in.getDealsend()*op.getCount();
							 } 
						}
				    	 
				    	 if(flag){
							 price += pprice ;
						 }else { 
							 price += cprice ;
						 } 
				     }
				     //System.out.println(op.getSendType());
					 
					  //System.out.println(cprice);
					 // System.out.println(pprice);
					 
			     }
			     
				 
		 }
		 return price ;
	 } 
	 private static InstallSaleMessage getInstallSaleFromRs(ResultSet rs){
		 InstallSaleMessage in = new InstallSaleMessage();
			try {   
				in.setCategoryID(rs.getString("categoryID"));
				in.setDealsend(rs.getInt("dealsend"));
				in.setId(rs.getInt("id"));
				in.setInstall(rs.getInt("install"));
				in.setInstallsaleID(rs.getInt("installsaleID"));
				in.setProductID(rs.getInt("productID"));
				in.setSend(rs.getInt("send")); 
				in.setSendInstall(rs.getInt("sendInstall"));
			} catch (SQLException e) {
				e.printStackTrace();
			}	 
			
			return in ;
		}
}
