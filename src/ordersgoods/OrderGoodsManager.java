package ordersgoods;


import goodsreceipt.OrderSN;
import httpClient.InventorySN;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import database.DB;
import user.User;
import user.UserService;
import utill.DBUtill;
import utill.StringUtill;

  
public class OrderGoodsManager { 
	protected static Log logger = LogFactory.getLog(OrderGoodsManager.class);
	  
   public static boolean save(User user ,OrderGoods og){
	   boolean flag = false ;
	   List<String> list = new ArrayList<String>();
	   String sql = ""; 
	   if(og.getId() != 0 ){ 
		   String sqld = "delete from mdordergoods where uuid = "+ og.getId();
		   list.add(sqld);
		  
		   sql = "insert into mdordergoods (id,oid,submitid,submittime,tid,statues,ordernum,realnum,opstatues,uuid) " +
			   		"values ("+og.getId()+","+og.getOid()+",'"+og.getSubmitid()+"','"+og.getSubmittime()+"','"+og.getTid()+"','"+og.getStatues()+"','"+og.getOrdernum()+"','"+og.getRealnum()+"','"+og.getOpstatues()+"','"+og.getUuid()+"') ;" ; 
	   }else {
		   sql = "insert into mdordergoods (id,oid,submitid,submittime,tid,statues,ordernum,realnum,opstatues,uuid) " +
			   		"values (null,"+og.getOid()+",'"+og.getSubmitid()+"','"+og.getSubmittime()+"','"+og.getTid()+"','"+og.getStatues()+"','"+og.getOrdernum()+"','"+og.getRealnum()+"','"+og.getOpstatues()+"','"+og.getUuid()+"') ;" ; 
	   }
	       
	   list.add(sql);
	   flag = DBUtill.sava(list);
	   return flag ;
   }    
           
   public static String updateOid(int tid ,int bid,OrderSN os){ 
	      
	   String sql = "update mdordergoods set oid = '"+os.getOrderNum()+"' , effectiveendtime = '"+os.getEndtime()+"' where tid ="+tid+"  and mid in (select id from mdordermessage where branchid = "+bid  + ") and oid is null and opstatues = 2 and statues = "+os.getGoodtypeStatues()+" and  ordernum <= " +os.getNum(); 
	     
	   return sql ;
	   
   }
    
   
   public static List<String> save(User user ,OrderGoodsAll oa){
	   //boolean flag = false ;   
	   List<OrderGoods> ogs = oa.getList();
	   List<String> list = new ArrayList<String>();
	   String sqld = "";   
	   if(null == ogs || ogs.size() == 0 ||null == ogs.get(0)){   
		   sqld = "delete from mdordergoods where mid = "+oa.getOm().getId();
	   }else {
		   if(2 == ogs.get(0).getBillingstatues()){
			   sqld = "delete from mdordergoods where mid = "+oa.getOm().getId();
		   }else {
			   sqld = "delete from mdordergoods where mid = "+oa.getOm().getId() +" and opstatues = 0 ";
		   }
	   }
	   
	   
	   list.add(sqld);   
	       
	   for(int i=0;i<ogs.size();i++){    
		   OrderGoods og = ogs.get(i);  
		    String value = "";   
		    if(og.getStatues() == 9){ 
		    	Map<String,String> map = new HashMap<String,String>(); 
				//System.out.println(branch+tname+statues);         
				InventorySN.getinventoryByName(og.getTid()+"",og.getBranch(),"9",map,"ID");
				 Set<Map.Entry<String,String>> set = map.entrySet();   
				  Iterator<Map.Entry<String,String>> it = set.iterator();
				  while(it.hasNext()){
					  Map.Entry<String,String> mapent = it.next();
					  value = mapent.getValue(); 
				  } 
		    } 
		       
			// DBUtill.sava(list);
		   String sql = "insert into mdordergoods (id,oid,submitid,submittime,cid,tid,statues,ordernum,realnum,opstatues,uuid,mid,billingstatues,serialnumber) " +
				   		"values ("+og.getId()+","+og.getOid()+",'"+og.getSubmitid()+"','"+og.getSubmittime()+"','"+og.getCid()+"','"+og.getTid()+"','"+og.getStatues()+"','"+og.getOrdernum()+"','"+og.getRealnum()+"','"+og.getOpstatues()+"','"+og.getUuid()+"',"+oa.getOm().getId()+","+og.getBillingstatues()+",'"+value+"') ;" ; 
		   list.add(sql); 
	   }   
   return list ;
	   
   }
   
  
   public static List<OrderGoods> getlist(User user){
	   List<OrderGoods> list = new ArrayList<OrderGoods>();
	  List<Integer> listids = UserService.GetListson(user);
	 // logger.info(listids);
	   Connection conn = DB.getConn();
	   
	   String sql = " select * from mdordergoods where submitid in (" + listids.toString().substring(1,listids.toString().length()-1) +" )";
		 logger.info(sql);
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {  
			while (rs.next()) {
				OrderGoods og = getOrderGoodsFromRs(rs);
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
   
   public static List<OrderGoods> getlist(User user,String uuid){
	   List<OrderGoods> list = new ArrayList<OrderGoods>();
	  List<Integer> listids = UserService.GetListson(user);
	 // logger.info(listids);
	   Connection conn = DB.getConn();
	    
	   String sql = " select * from mdordergoods where submitid in (" + listids.toString().substring(1,listids.toString().length()-1) +" ) and uuid = '"+ uuid+"'";
		 logger.info(sql);
		Statement stmt = DB.getStatement(conn);  
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {  
			while (rs.next()) {
				OrderGoods og = getOrderGoodsFromRs(rs);
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
    
  public static String updaterealsendnum(User user ,int ogid,String realsendnum,String returnrealsendnum){
	    
	  String sql = " update mdordergoods set realsendnum = "+realsendnum + " ,returnrealsendnum = "+returnrealsendnum+" ,billingstatues = 2  where id = " + ogid;
	  return sql ;  
  }   
     
  public static String updateInstoragenum(User user ,int ogid,String realsendnum){
	     
	  String sql = " update mdordergoods set instoragenum  = "+realsendnum + ", billingstatues = 3  where id = " + ogid;
	  return sql ;  
  }  
  
  public static String updateIOS(String name,int type ,String oid,String time){ 
	  if(StringUtill.isNull(time)){
		  time = null;
	  }else {  
		  time = "'"+time+"'"; 
	  }  
	  String sql = " update mdordergoods set oid = "+oid + " , effectiveendtime = "+time+"  where exportuuid = '" + name+"' and statues = "+type;
	  return sql ;  
  }   
   
  public static List<String> Updateserialnumber(Map<String,String> map){
	  List<String> listsql = new ArrayList<String>();
	  Set<Map.Entry<String,String>> set = map.entrySet();
	  Iterator<Map.Entry<String,String>> it = set.iterator();
	  while(it.hasNext()){
		  Map.Entry<String,String> mapent = it.next();
		  String key = mapent.getKey();
		  String value = mapent.getValue(); 
		  String sql = "update mdordergoods set serialnumber = '" +value+"' where id = "+key;
		  listsql.add(sql);
	  }
	  return listsql ; 
  }
  
  
  public static OrderGoods getbyid(int id){
	  OrderGoods og = new OrderGoods();
	 
	   Connection conn = DB.getConn();
	    
	   String sql = " select * from mdordergoods where id="+id;
		 logger.info(sql);
		Statement stmt = DB.getStatement(conn);  
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {  
			while (rs.next()) {
				og = getOrderGoodsFromRs(rs);
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
	   return og ;
  }
  
  
   public static Map<String,OrderGoodsAll> getmap(User user){
	   Map<String,OrderGoodsAll> map = new HashMap<String,OrderGoodsAll>();
	   
	   List<OrderGoods> list = OrderGoodsManager.getlist(user); 
	  //  logger.info(list.size());  
	   for(int i=0;i<list.size();i++){
		   OrderGoods o = list.get(i); 
		  // logger.info(o.getUuid());
		   OrderGoodsAll oa = map.get(o.getUuid());
		 //  logger.info(oa);   
		   if(null == oa){
			   oa = new OrderGoodsAll();
			    
			   List<OrderGoods> li = new ArrayList<OrderGoods>(); 
			   li.add(o);
			   oa.setList(li);  
			   map.put(o.getUuid(), oa); 
		   }else { 
			   oa.getList().add(o);
		   }
	   } 
	    
	   return map;
   }
   
   public static int getMaxid(){
	    int id = 1 ;
	    Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn);
		String  sql = "select max(id)+1 as id from mdordergoods" ;
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				id = rs.getInt("id");
				if(id == 0){  
					id ++;
				}  
				logger.info(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(stmt);
			DB.close(rs);
			DB.close(conn);
		 }
		return id;

  } 
    
   public static OrderGoods getOrderGoodsFromRs(ResultSet rs){
	   OrderGoods p = null;
		try {   
			p = new OrderGoods();   
			p.setId(rs.getInt("mdordergoods.id")); 
			p.setOid(rs.getString("mdordergoods.oid")); 
			p.setOpstatues(rs.getInt("mdordergoods.opstatues"));
			p.setBillingstatues(rs.getInt("mdordergoods.billingstatues")); 
			p.setOrdernum(rs.getInt("mdordergoods.ordernum")); 
			p.setRealnum(rs.getInt("mdordergoods.realnum")); 
			p.setStatues(rs.getInt("mdordergoods.statues"));   
			p.setSubmitid(rs.getInt("mdordergoods.submitid")); 
			p.setSubmittime(rs.getString("mdordergoods.submittime"));   
			p.setUuid(rs.getString("mdordergoods.uuid"));   
			p.setTid(rs.getInt("mdordergoods.tid"));     
			p.setUuidtime(rs.getString("mdordergoods.uuidtime")); 
			p.setRealsendnum(rs.getInt("mdordergoods.realsendnum"));
			p.setEffectiveendtime(rs.getString("effectiveendtime"));
			p.setSerialnumber(rs.getString("serialnumber"));   
			p.setCid(rs.getInt("cid"));             
			p.setReturnrealsendnum(rs.getInt("returnrealsendnum"));
			p.setExportuuid(rs.getString("exportuuid"));
			p.setMid(rs.getInt("mid"));    
			p.setInstoragenum(rs.getInt("instoragenum")); 
			p.setExportmodel(rs.getInt("exportmodel"));  
            p.setExportstatues(rs.getInt("exportstatues")); 
		    //p.setNexttime(rs.getString("nexttime"));
           // System.out.println("p.getStatues()"+p.getStatues()); 
		} catch (SQLException e) {    
			e.printStackTrace();
		}  
		return p;  
   }
   
}
