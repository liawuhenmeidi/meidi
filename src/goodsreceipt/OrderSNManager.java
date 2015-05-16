package goodsreceipt;
  
import inventory.InventoryBranchManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import java.util.List;  
import java.util.Map;


import ordersgoods.OrderGoodsManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import product.ProductService;

import branch.BranchService;

import database.DB;
import enums.SaleModel;
  
public class OrderSNManager {
	protected static Log logger = LogFactory.getLog(OrderSNManager.class);
 
	public static List<String> save(OrderSN gr) {
		List<String> list = new ArrayList<String>();
		  
			String sql = " insert into mdordersn (id,ordernum,branchname,goodtype,goodpname,goodnum,num,innum,statues,disable,uuid,starttime,endtime)"
					+ " values ("
					+ null 
					+ ",'"
					+ gr.getOrderNum()
					+ "','"
					+ gr.getBranchName()
					+ "','"
					+ gr.getGoodType()
					+ "','"
					+ gr.getGoodpName()
					+ "','"
					+ gr.getGoodNum()
					+ "','"
					+ gr.getNum()
					+ "','"
					+ gr.getInNum()
					+ "','" 
					+ gr.getStatues()
					+ "','" 
					+ gr.getDisable()
					+ "','"+gr.getUuid()+"','"+gr.getStarttime()+"','"+gr.getEndtime()+"');"; 
			list.add(sql);
	
		return list;
	}  
   
	public static List<String> save( List<OrderSN > list,String starttime,String endtime) {
		 
		 List<String> listsql = new ArrayList<String>();
		try{
		 Map<String,OrderSN> map = getMapAll(starttime,endtime);
		  
		 //logger.info(list.size());
		
		   if(!list.isEmpty()){     
			   Iterator<OrderSN > it =  list.iterator();
			   while(it.hasNext()){   
				   OrderSN sn = it.next(); 
				   boolean flag = false ;
				  // logger.info(123);
				  if(null == map){
					  flag = true; 
				  }else {
					  OrderSN sndb = map.get(sn.getUuid()); 
					  if(null == sndb){
						  flag = true ;
					  }  
				  }
				  
				  //logger.info(flag);    
				   
				   if(flag){    
					   List<String> sql = save(sn); 
					 //  logger.info(sql); 
					   listsql.addAll(sql);   
					  // logger.info(sn.getBranchName());
					     
					   int tid = ProductService.gettypeNUmmap().get(sn.getGoodNum()).getId();
					   
					   int bid = BranchService.getNameSNMap().get(sn.getBranchName()).getId();
					     
					  String sqlin = InventoryBranchManager.updateSNMessage(tid, bid, sn.getGoodtypeStatues(), sn.getOrderNum(),sn.getEndtime());
				             
					 // logger.info(sqlin);   
					  String sqlog = OrderGoodsManager.updateOid(tid, bid, sn);    
					// logger.info(sqlog); 
					  listsql.add(sqlog);   
					  listsql.add(sqlin);    
				 //  logger.info(listsql); 
				   } 
			   }
			  // logger.info(listsql); 
		   }
		}catch(Exception e){
			logger.info(e);
		}
	//logger.info(listsql); 
		return listsql;
	}  
   
	
	public static Map<String,OrderSN> getMapAll(String starttime,
			String endtime) { 
		Map<String, OrderSN> map = new HashMap<String, OrderSN>();
		String sql = " select * from mdordersn where  starttime BETWEEN  '"
				+ starttime + "'  and '" + endtime + "'";
		logger.info(sql); 
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn);

		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				OrderSN as = getOrderSNFromRs(rs);
				map.put(as.getUuid(), as); 
			} 
		} catch (SQLException e) {
			logger.info(e); 
		} finally {
			DB.close(stmt);
			DB.close(rs);
			DB.close(conn);
		}

		return map;
	}

	

	


	public static int getMaxid() {
		int id = 1;
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn);
		String sql = "select max(id)+1 as id from mdordersn";
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				id = rs.getInt("id");
				if (id == 0) {
					id++;
				}
				// logger.info(id);
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

	public static OrderSN getOrderSNFromRs(ResultSet rs) {
		OrderSN p = null;
		try { 
			p = new OrderSN();
			p.setBranchName(rs.getString("branchname")) ;
			p.setGoodNum(rs.getString("goodnum"));
			p.setGoodpName(rs.getString("goodpname"));
			p.setGoodType(rs.getString("goodtype"));
			p.setInNum(rs.getInt("innum"));
			p.setNum(rs.getInt("num"));
			p.setStatues(rs.getInt("statues"));
			p.setOrderNum(rs.getString("ordernum")); 
			p.setStarttime(rs.getString("starttime"));
			p.setEndtime(rs.getString("endtime"));
			p.setUuid(rs.getString("uuid")); 
		} catch (SQLException e) {
			p = null;
			// logger.info(e);
			return p;

		}
		return p;
	}

}
