package saledealsend;


import group.Group;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import user.User;
import user.UserManager;
import utill.BasicUtill;
import utill.DBUtill;
import utill.StringUtill;
import utill.TimeUtill;

import database.DB;
   
public class SaledealsendManager {
	protected static Log logger = LogFactory.getLog(SaledealsendManager.class);

	 public static boolean  save(User user ,Saledealsend  in, boolean flags,String method){
		 boolean flag = false ;
		 List<String> sqls = new ArrayList<String>();
		if(flags){  
			String sql = "";
			//String sql1 = "update mdorderproduct set chargeDealsendtime = '"+TimeUtill.gettime()+"',chargeDealsendID = "+user.getId()+" where id in (" + in.getOrderids()+")";
			String sql1 = "insert into saledealsend(id,dealsendid,name,givestatues,receivestatues,submittime,orderids) values ("+in.getId()+","+in.getDealsendid()+",'"+in.getName()+"',"+in.getGivestatues()+","+in.getReceivestatues()+",now(),'"+in.getOrderids()+"')";
			if("dealsendcharge".equals(method)){
				sql = "update mdorder set statues4 = 1 , chargeDealsendtime = '"+TimeUtill.gettime()+"' where id in (" + in.getOrderids()+")";
			}else if("sendcharge".equals(method)){
				sql = "update mdorder set statuespaigong = 1 , chargeSendtime = '"+TimeUtill.gettime()+"' where id in (" + in.getOrderids()+")";
			}else if("installcharge".equals(method)){
				sql = "update mdorder set statuesinstall = 1  , chargeInstalltime = '"+TimeUtill.gettime()+"' where id in (" + in.getOrderids()+")";
			}else if("sendinstallcharge".equals(method)){
				sql = "update mdorder set statuesinstall = 2 , chargeInstalltime = '"+TimeUtill.gettime()+"'  where id in (" + in.getOrderids()+")";
			}			 
			//logger.info(sql); 
			sqls.add(sql);  
			sqls.add(sql1); 
			List<SaledealsendMessage> list = in.getList();
			for(int i=0;i<list.size();i++){
				SaledealsendMessage sa = list.get(i);
				String sqlsa = " insert into saledealsendmessage (id,saledealsendID,orderids,dealsendprice,dealsendMessage) values (null,"+sa.getSaledealsendID()+",'"+sa.getOrderids()+"','"+sa.getDealsendprice()+"','"+sa.getDealsendMessage()+"') ";
			    sqls.add(sqlsa);
			}
		}else { 
			String sql = "update saledealsend set givestatues = "+in.getGivestatues()+" ,receivestatues = "+in.getReceivestatues() +" where  id = "+ in.getId() ;
			String sql1 = " delete from saledealsendmessage where saledealsendID = "+ in.getId();
			sqls.add(sql);  
			sqls.add(sql1);  
			List<SaledealsendMessage> list = in.getList();
			for(int i=0;i<list.size();i++){
				SaledealsendMessage sa = list.get(i);
				String sqlsa = " insert into saledealsendmessage (id,saledealsendID,orderids,dealsendprice,dealsendMessage) values (null,"+sa.getSaledealsendID()+",'"+sa.getOrderids()+"','"+sa.getDealsendprice()+"','"+sa.getDealsendMessage()+"') ";
			    sqls.add(sqlsa);
			}
		}
		
		flag = DBUtill.sava(sqls); 
		return flag ;
	 }  
	  
	/* public static boolean  update(Saledealsend  in){
			//String sql = "update Saledealsend(id,uid,uname,phone,locate,andate,message) values ("+in.getId()+","+in.getUid()+","+in.getUname()+","+in.getPhone()+","+in.getLocate()+","+in.getAndate()+",'"+in.getMessage()+"')";
			//String sql = "update Saledealsend set uid = "+in.getUid()+" ,uname ="+in.getUname()+" ,phone ="+in.getPhone()+",locate ="+in.getLocate()+" ,andate ="+in.getAndate()+" ,message = '"+in.getMessage()+"' where id = " +in.getId()  ;
			//logger.info(sql); 
			return DBUtill.sava(sql); 
		 } */
	  
	 public static Map<String,Saledealsend> getmap(){
		    HashMap<String,Saledealsend> map = new HashMap<String,Saledealsend>(); 
			Connection conn = DB.getConn();    
			String sql = "select * from saledealsend";  
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {   
				while (rs.next()) {
					Saledealsend in = getSaledealsendFromRs(rs);
					map.put(in.getDealsendid()+"", in);  
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
	 
	 public static List<Saledealsend> getList(User user ,String startTime, String endTime ,String dealsendid,int type){
		  
		 List<Saledealsend> list = new ArrayList<Saledealsend>();  
		 String search =  TimeUtill.getsearchtime(startTime,endTime);
		 if(BasicUtill.dealsend == type){
			 if(!StringUtill.isNull(dealsendid)){
				 search += " and dealsendid = "+ dealsendid;
			 }  
		 }else {
			 if(!StringUtill.isNull(dealsendid)){
				 search += " and dealsendid = "+ dealsendid;
			 }else {
				 search += " and dealsendid in ( select id from mduser where charge = " +user.getId()+ " ) ";   
			 }
		 }
		 
		 
			Connection conn = DB.getConn();    
			String sql = "select * from saledealsend where 1= 1 "+ search; 
			logger.info(sql);
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {   
				while (rs.next()) {
					Saledealsend in = getSaledealsendFromRs(rs);
					list.add(in);  
				} 
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			} 
			return list;

	 }
	 
	 public static Saledealsend getSaledealsend(String id){
		 Saledealsend in = null; 
			Connection conn = DB.getConn();    
			String sql = "select * from saledealsend where id = "+id;  
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {   
				while (rs.next()) {
					in = getSaledealsendFromRs(rs);
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
	 
	 public static int getmaxid(){
		    int count = 0 ; 
		    Connection conn = DB.getConn();
			Statement stmt = DB.getStatement(conn);
			String  sql = "select max(id) as id from saledealsend" ;
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
	 
	 public static List<Saledealsend> getSaledealsendUnquery(User user,int type){
		 List<Saledealsend> list = new ArrayList<Saledealsend>();
			Connection conn = DB.getConn(); 
			String sql = "";
			if(BasicUtill.dealsend == type){
				if(UserManager.checkPermissions(user, Group.dealSend)){
					sql = "select * from saledealsend where givestatues = 0  ";   
				}else { 
					sql = "select * from saledealsend where givestatues = 0  and dealsendid = "+user.getId();   
				}
			}else if(UserManager.checkPermissions(user, Group.sencondDealsend)){ 
				sql = "select * from saledealsend where givestatues = 0  and dealsendid in ( select id from mduser where charge = " +user.getId()+ " ) ";   
			}else if(UserManager.checkPermissions(user, Group.send)){ 
				sql = "select * from saledealsend where givestatues = 0  and dealsendid = "+user.getId();   
			} 
			
			
			Statement stmt = DB.getStatement(conn);
			ResultSet rs = DB.getResultSet(stmt, sql);
			try {   
				while (rs.next()) { 
					Saledealsend in = getSaledealsendFromRs(rs);
					list.add(in);  
				} 
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DB.close(rs);
				DB.close(stmt);
				DB.close(conn);
			} 
			return list;

	 }
	 
	 private static Saledealsend getSaledealsendFromRs(ResultSet rs){
		 Saledealsend in = new Saledealsend();
			try {   
				
				in.setId(rs.getInt("id"));    
				in.setDealsendid(rs.getInt("dealsendid"));
				in.setGivestatues(rs.getInt("givestatues"));
				in.setName(rs.getString("name"));
				in.setReceivestatues(rs.getInt("receivestatues"));
				in.setSubmittime(rs.getString("submittime"));
				in.setMessage(rs.getString("message"));
				in.setOrderids(rs.getString("orderids")); 
			} catch (SQLException e) {
				e.printStackTrace();
			}	  
			return in ;
		}
}
