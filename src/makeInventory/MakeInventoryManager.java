package makeInventory;

import httpClient.download.SNInventory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
 
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import product.ProductService;

import database.DB;

import user.User; 
import user.UserService;
 
import utill.DBUtill; 
import utill.StringUtill;
 
public class MakeInventoryManager { 
	protected static Log logger = LogFactory.getLog(MakeInventoryManager.class);
	     
   public static boolean save(User user ,List<MakeInventory> list,boolean flag,String uuid){
	   List<String> listsql = new ArrayList<String>();
	   if(flag){  
		   String sql = "delete from makeinventory where uuid = '"+uuid+ "' and statues = 0 ;";
		   listsql.add(sql);
	   } 
	   for(int i=0;i<list.size();i++){  
		   MakeInventory mi = list.get(i);
		   String sql = "";   
		//   if(mi.getId() == 0 ){    
			     
			   sql = "insert into makeinventory (id,tid,bid,cid,submitid,submittime,statues,typestatues,num,uuid,remark) " +
				   		"values (null,"+mi.getTid()+",'"+mi.getBid()+"','"+mi.getCid()+"','"+mi.getSubmitid()+"','"+mi.getSubmittime()+"','"+mi.getStatues()+"','"+mi.getTypestatues()+"','"+mi.getNum()+"','"+mi.getUuid()+"','"+mi.getRemark()+"') ;" ; 
		 //  }else {
			
			     //sql = "update makeinventory set num="+mi.getNum()+" where id = "+mi.getId(); 
		  // }  
		   listsql.add(sql);
	   } 
	   
	   flag = DBUtill.sava(listsql);
	   return flag ;
   }     
       
  public static List<MakeInventory> get(User user ,int typestatues,String uuid){
	  List<MakeInventory> list = new ArrayList<MakeInventory>(); 
	   Connection conn = DB.getConn(); 
	   String sql = " select * from makeinventory where submitid ="+user.getId()+" and uuid = '"+uuid+"' and typestatues="+typestatues;
		logger.info(sql);    
		Statement stmt = DB.getStatement(conn);       
		ResultSet rs = DB.getResultSet(stmt, sql);    
		try {            
			while (rs.next()) {                      
				//logger.info(rs);  
				MakeInventory og = getMakeInventoryFromRs(rs);
				list.add(og);    
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
  
  public static List<MakeInventory> get(User user ,int typestatues){
	  List<MakeInventory> list = new ArrayList<MakeInventory>(); 
	   
	  Connection conn = DB.getConn(); 
	    
	   String sql = " select * from makeinventory where submitid ="+user.getId()+" and typestatues="+typestatues+" group by uuid";
		 logger.info(sql); 
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {    
			while (rs.next()) {
				MakeInventory og = getMakeInventoryFromRs(rs);
				list.add(og);
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
    
  public static List<String> update(String uuids,String[] types){
	  List<String> listsql = new ArrayList<String>();
	  String sql = " update makeinventory set statues = 1 where uuid in ("+uuids.substring(1,uuids.length())+") and tid in "+StringUtill.getStr(types);
	   listsql.add(sql);
	  return listsql;
  }
  public static List<SNInventory> get(User user ,String uuids){
	  List<SNInventory> list = new ArrayList<SNInventory>(); 
	   
	  Connection conn = DB.getConn();
	      
	   String sql = " select * from makeinventory where uuid in ("+uuids.substring(1,uuids.length())+")";
		 logger.info(sql); 
		Statement stmt = DB.getStatement(conn);  
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {    
			while (rs.next()) { 
				MakeInventory og = getMakeInventoryFromRs(rs);
				SNInventory sn = change(og);
				list.add(sn);
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
   
  public static SNInventory change(MakeInventory mi){
	  SNInventory sn = new SNInventory();
	  sn.setBranchid(mi.getBid()); 
	  sn.setTypeid(mi.getTid()+"");  
	 // logger.info(mi.getTypestatues());  
	  if(mi.getTypestatues() ==MakeInventory.model ){
		  sn.setFlagupm(1);
		  sn.setUpmodel(mi.getNum());
	  }else if(mi.getTypestatues() ==MakeInventory.in ){
		  sn.setUpin(mi.getNum());
		  sn.setFlagupin(1); 
	  }else if(mi.getTypestatues() ==MakeInventory.out ){
		  sn.setFlagupout(1);
		  sn.setUpout(mi.getNum()); 
	  } 
	  sn.setFlag(mi.getStatues());
	  return sn ; 
	  
  }
  // 类型   uuid          
  public static Map<Integer,Map<String,MakeInventory>> getMap(User user ,int bid){
	  Map<Integer,Map<String,MakeInventory>> map = new HashMap<Integer,Map<String,MakeInventory>>(); 
	         
	  Connection conn = DB.getConn();   
	  //List<Integer> lists =  UserService.GetListson(user);
	  String str = user.getProductIDS();
	  //System.out.println(str);             
	        
	   String sql = " select * from makeinventory where  bid="+bid +" and cid in "+str+" group by uuid";
	   logger.info(sql);    
		Statement stmt = DB.getStatement(conn);     
		ResultSet rs = DB.getResultSet(stmt, sql);  
		try {     
			while (rs.next()) {     
				MakeInventory og = getMakeInventoryFromRs(rs); 
				Map<String,MakeInventory> mapt  = map.get(og.getTypestatues());
				if(null == mapt){
					mapt = new HashMap<String,MakeInventory>();
					map.put(og.getTypestatues(), mapt); 
				}		
				MakeInventory list = mapt.get(og.getUuid());
				if(null == list){ 
					// list = new ArrayList<MakeInventory>(); 
					 mapt.put(og.getUuid(), og);
				}  
				//list.add(og);
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
	  
	  
	     
	  return map ;
  }
  
  
   public static MakeInventory getMakeInventoryFromRs(ResultSet rs){
	   MakeInventory mi = null;
		try {    
			mi = new MakeInventory();   
			 mi.setBid(rs.getInt("bid")); 
			 mi.setCid(rs.getInt("cid"));
			 mi.setId(rs.getInt("id")); 
			 mi.setNum(rs.getInt("num")); 
			 mi.setStatues(rs.getInt("statues"));  
			 mi.setSubmitid(rs.getInt("submitid")); 
			 mi.setSubmittime(rs.getString("submittime"));
			 mi.setTid(rs.getInt("tid"));
			 mi.setUuid(rs.getString("uuid"));   
			 mi.setTypestatues(rs.getInt("typestatues")); 
			 mi.setRemark(rs.getString("remark")); 
		} catch (SQLException e) {    
			e.printStackTrace();
		}  
		return mi;  
   }
   
}
