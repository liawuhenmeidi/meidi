package goodsreceipt;

import inventory.InventoryBranch;
import inventory.InventoryBranchManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.csvreader.CsvReader;

import database.DB;

import user.User;
import utill.DBUtill;
import utill.TimeUtill;
 
public class GoodsReceitManager {
	protected static Log logger = LogFactory.getLog(GoodsReceitManager.class);
      
	  
	public static List<String> saveIN(Map<Integer, Map<String, Map<Integer, InventoryBranch>>> mapin ,GoodsReceipt gr,Set<String> setup) {
		User user = new User();
		boolean flag = true;
		List<String> list = new ArrayList<String>();

		logger.info(gr.getTid() + "***" + gr.getBidSN());
		if (gr.getTid() == 0 || gr.getBidSN() == 0) {
			gr.setDisable(1); 
			flag = false;  
		} 

		String sql = " insert into goodsreceipt (id,receveid,recevetime,sendid,buyid,ordertype,goodsnum,goodsname,recevenum,refusenum,branchid,branchname,uuid,disable)"
				+ " values ('"
				+ gr.getId()
				+ "','"
				+ gr.getReceveid()
				+ "','"
				+ gr.getReceveTime()
				+ "','"
				+ gr.getSendid()
				+ "','" 
				+ gr.getBuyid()
				+ "','" 
				+ gr.getOrdertype()
				+ "',"
				+ "'" 
				+ gr.getGoodsnum()
				+ "','"
				+ gr.getGoodsName()
				+ "','"
				+ gr.getRecevenum()
				+ "','"
				+ gr.getRefusenum()
				+ "','" 
				+ gr.getBranchid() 
				+ "','"
				+ gr.getBranchName()
				+ "','"
				+ gr.getUuid() + "'," + gr.getDisable() + ");";
		if (flag) {

			String sqlIB = "";
			String sqlIBM = "";
			InventoryBranch in = null; 
			try {
				in = mapin.get(gr.getBidSN())
						.get(gr.getTid() + "")
						.get(gr.getGoodtypeStatues()); 
			} catch (Exception e) {
				in = null;
			}
			  
			if (null == in 
					&& !setup.contains(gr.getBidSN()
							+ "_"
							+ gr.getTid()+"_"+gr.getGoodtypeStatues())) {

				setup.add(gr.getBidSN() + "_"
						+ gr.getTid()+"_"+gr.getGoodtypeStatues());
				sqlIB = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid,typestatues)"
						+ "  values ( null,"
						+ gr.getCid()
						+ ", '"
						+ gr.getTid()
						+ "', '" 
						+ +gr.getRecevenum() 
						+ "', '"
						+ -gr.getRecevenum() + "'," + gr.getBidSN()+ ","
								+ gr.getGoodtypeStatues()+ ")";
                 
			
				sqlIBM = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount,isoverstatues,typestatues)"
						+ "  values ( null, '"
						+ gr.getBidSN()
						+ "', '"
						+ gr.getId()
						+ "','"
						+ gr.getBuyid()+"<p>"+gr.getReceveid()
						+ "','"
						+ gr.getReceveTime()
						+ "','"
						+ gr.getTid()
						+ "',"
						+ +gr.getRecevenum()
						+ ",'"
						+ -gr.getRecevenum()
						+ "',"
						+ 13
						+ ","
						+ +gr.getRecevenum()
						+ ","
						+ -gr.getRecevenum()
						+ ","
						+ 1 + "," + gr.getBidSN() + ",21,0,0,0,"
											+ gr.getGoodtypeStatues()
											+ ")";
			} else { 

				sqlIB = "update mdinventorybranch set papercount =  ((mdinventorybranch.papercount)*1 - "
						+ gr.getRecevenum()
						+ ")*1  , realcount = ((mdinventorybranch.realcount)*1 + "
						+ gr.getRecevenum()
						+ ")*1   where  branchid = "
						+ gr.getBidSN() + " and  type = '" + gr.getTid() + "' and typestatues = "+gr.getGoodtypeStatues();

				sqlIBM = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount,isoverstatues,typestatues)"
						+ "  values ( null, '"
						+ gr.getBidSN()
						+ "', '"
						+ gr.getId()
						+ "','"
						+ gr.getBuyid()+"<p>"+gr.getReceveid()
						+ "','"
						+ gr.getReceveTime()
						+ "','"
						+ gr.getTid()
						+ "','"
						+ +gr.getRecevenum()
						+ "','"
						+ -gr.getRecevenum()
						+ "',"
						+ 13
						+ ",(select realcount from mdinventorybranch where branchid = "
						+ gr.getBidSN()
						+ " and  type = '"
						+ gr.getTid()
						+ "' and typestatues = "+gr.getGoodtypeStatues() +")*1,(select papercount from mdinventorybranch where branchid = "
						+ gr.getBidSN()
						+ " and  type = '"
						+ gr.getTid()
						+ "'  and typestatues = "+gr.getGoodtypeStatues() +")*1,"
						+ 1
						+ ","
						+ gr.getBidSN()
						+ ",21,(select realcount from mdinventorybranch where branchid = "
						+ gr.getTid()
						+ " and  type = '" 
						+ gr.getTid() 
						+ "'  and typestatues = "+gr.getGoodtypeStatues() +")*1"
						+ -Integer.valueOf(gr.getRecevenum())
						+ ",(select papercount from mdinventorybranch where branchid = "
						+ gr.getBidSN() 
						+ " and  type = '"
						+ gr.getTid()  
						+ "'  and typestatues = "+gr.getGoodtypeStatues() +")*1"
						+ +Integer.valueOf(gr.getRecevenum()) + ",0," + gr.getGoodtypeStatues() + ")";

			} 
               
			//String sqlor = OrderReceitManager.update(gr);
			//list.add(sqlor);    
			list.add(sqlIB);
			list.add(sqlIBM); 
		} 

		list.add(sql);
		return list;
	}
  
	public static List<String> saveOut(Map<Integer, Map<String, Map<Integer, InventoryBranch>>> mapin ,GoodsReceipt gr,Set<String> setup) {
		User user = new User();
		boolean flag = true;   
		List<String> list = new ArrayList<String>();
           
		//logger.info(gr.getTid() + "***" + gr.getBidSN());
		if (gr.getTid() == 0 || gr.getBidSN() == 0) {
			 
			gr.setDisable(1); 
			flag = false;  
		} 
		 
        String sqlup = OrderReceitManager.update(gr);
          
		String sql = " insert into goodsreceipt (id,receveid,recevetime,sendid,buyid,ordertype,goodsnum,goodsname,recevenum,refusenum,branchid,branchname,uuid,disable,statues)"
				+ " values ('"
				+ gr.getId()
				+ "','" 
				+ gr.getReceveid()
				+ "','"
				+ gr.getReceveTime()
				+ "','"
				+ gr.getSendid()
				+ "','" 
				+ gr.getBuyid()
				+ "','"
				+ gr.getOrdertype()
				+ "'," 
				+ "'" 
				+ gr.getGoodsnum()
				+ "','" 
				+ gr.getGoodsName()
				+ "','"
				+ gr.getRecevenum()
				+ "','"
				+ gr.getRefusenum()
				+ "','" 
				+ gr.getBranchid() 
				+ "','" 
				+ gr.getBranchName()
				+ "','"  
				+ gr.getUuid() + "'," + gr.getDisable() + ","+gr.getStatues()+");";
		if (flag) { 
  
			String sqlIB = "";
			String sqlIBM = ""; 
			InventoryBranch in = null;
			 
			try { 
				in = mapin.get(gr.getBidSN())
						.get(gr.getTid() + "")
						.get(gr.getGoodtypeStatues()); 
			} catch (Exception e) {
				in = null;
			}  
			 
			if (null == in 
					&& !setup.contains(gr.getBidSN()
							+ "_"
							+ gr.getTid()+"_"+gr.getGoodtypeStatues())) {

				setup.add(gr.getBidSN() + "_"
						+ gr.getTid()+"_"+gr.getGoodtypeStatues());
				sqlIB = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid,typestatues)"
						+ "  values ( null,"
						+ gr.getCid()
						+ ", '" 
						+ gr.getTid()
						+ "', '"
						+ -gr.getRecevenum()
						+ "', '"
						+ gr.getRecevenum() + "'," + gr.getBidSN() + ","
								+ gr.getGoodtypeStatues()+ ")";
 
				sqlIBM = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount,isoverstatues,typestatues)"
						+ "  values ( null, '"
						+ gr.getBidSN()
						+ "', '"
						+ gr.getId()
						+ "','"
						+ gr.getBuyid()+"<p>"+gr.getReceveid()
						+ "','"
						+ gr.getReceveTime()
						+ "','"
						+ gr.getTid()
						+ "',"
						+ -gr.getRecevenum()
						+ ",'"
						+ gr.getRecevenum()
						+ "',"
						+ 15
						+ ","
						+ -gr.getRecevenum()
						+ ","
						+ gr.getRecevenum()
						+ ","
						+ 1 + "," + gr.getBidSN() + ",21,0,0,0,"
											+ gr.getGoodtypeStatues()
											+ ")";
			} else {

				sqlIB = "update mdinventorybranch set papercount =  ((mdinventorybranch.papercount)*1 + "
						+ gr.getRecevenum()
						+ ")*1 , realcount = ((mdinventorybranch.realcount)*1 - "
						+ gr.getRecevenum()
						+ ")*1  where  branchid = "
						+ gr.getBidSN() + " and  type = '" + gr.getTid() + "' and typestatues = "+gr.getGoodtypeStatues();

				sqlIBM = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount,isoverstatues,typestatues)"
						+ "  values ( null, '"
						+ gr.getBidSN()
						+ "', '"
						+ gr.getId()
						+ "','"
						+ gr.getBuyid()+"<p>"+gr.getReceveid()
						+ "','"
						+ gr.getReceveTime()
						+ "','"
						+ gr.getTid()
						+ "','"
						+ -gr.getRecevenum()
						+ "','"
						+ gr.getRecevenum()
						+ "',"  
						+ 15  
						+ ",(select realcount from mdinventorybranch where branchid = "
						+ gr.getBidSN()
						+ " and  type = '"
						+ gr.getTid()
						+ "' and typestatues = "+gr.getGoodtypeStatues() +")*1,(select papercount from mdinventorybranch where branchid = "
						+ gr.getBidSN()
						+ " and  type = '" 
						+ gr.getTid() 
						+ "'  and typestatues = "+gr.getGoodtypeStatues() +")*1,"
						+ 1
						+ ","
						+ gr.getBidSN() 
						+ ",21,(select realcount from mdinventorybranch where branchid = "
						+ gr.getTid()
						+ " and  type = '"
						+ gr.getTid()
						+ "' and typestatues = "+gr.getGoodtypeStatues() +")*1"
						+ Integer.valueOf(gr.getRecevenum()) 
						+ ",(select papercount from mdinventorybranch where branchid = "
						+ gr.getBidSN()
						+ " and  type = '"
						+ gr.getTid()
						+ "' and typestatues = "+gr.getGoodtypeStatues() +")*1"
						+ -Integer.valueOf(gr.getRecevenum()) + ",0," + gr.getGoodtypeStatues() + ")";
 
			}    
			list.add(sqlup); 
			list.add(sqlIB);
			list.add(sqlIBM);  
		} 
 
		list.add(sql);
		return list;
	}
 
	public static List<String> saveDisable(GoodsReceipt gr) {
		User user = new User(); 
		String papermark = "-"; 
		String realmark = "+";
		int type = 13; 
		if (gr.getStatues() == 1) { 
			logger.info(123); 
			papermark = "+";
			realmark = "-";
			type = 15;
		}
		boolean flag = true;
		List<String> list = new ArrayList<String>();

		logger.info(gr.getTid() + "***" + gr.getBidSN());

		if (gr.getTid() == 0 || gr.getBidSN() == 0 || gr.getDisable() == 0) {
			flag = false;
		}

		if (flag) {
			String sql = "update goodsreceipt set disable = 0 where id =  "
					+ gr.getId(); 
             
			String sqlup = OrderReceitManager.update(gr); 
			   
			String sqlIB = "";
			String sqlIBM = "";
			if (null == InventoryBranchManager.getInventoryID(user,
					gr.getBidSN(), gr.getTid() + "")) {
				sqlIB = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid,typestatues)"
						+ "  values ( null,"
						+ gr.getCid()
						+ ", '"
						+ gr.getTid()
						+ "', '"
						+ realmark
						+ gr.getRecevenum()
						+ "', '"
						+ papermark
						+ gr.getRecevenum()
						+ "',"
						+ gr.getBidSN() + ","
								+ gr.getGoodtypeStatues()+ ")";

				sqlIBM = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount,isoverstatues,typestatues)"
						+ "  values ( null, '"
						+ gr.getBidSN()
						+ "', '"
						+ gr.getId()
						+ "','"
						+ gr.getBuyid()+"<p>"+gr.getReceveid()
						+ "','"
						+ gr.getReceveTime()
						+ "','"
						+ gr.getTid()
						+ "',"
						+ papermark
						+ gr.getRecevenum()
						+ ",'"
						+ papermark
						+ gr.getRecevenum()
						+ "',"
						+ type
						+ ","
						+ papermark
						+ gr.getRecevenum()
						+ ","
						+ papermark
						+ gr.getRecevenum()
						+ ","
						+ 1
						+ ","
						+ gr.getBidSN()
						+ ",21,0,0,1,"+ gr.getGoodtypeStatues()
											+ ")";
			} else {

				sqlIB = "update mdinventorybranch set papercount =  ((mdinventorybranch.papercount)*1  "
						+ papermark
						+ gr.getRecevenum()
						+ ")*1 , realcount = ((mdinventorybranch.realcount)*1  "
						+ realmark
						+ gr.getRecevenum()
						+ ")*1   where  branchid = "
						+ gr.getBidSN() 
						+ " and  type = '" + gr.getTid() + "' and typestatues = "+gr.getGoodtypeStatues();
 
				sqlIBM = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount,isoverstatues,typestatues)"
						+ "  values ( null, '"
						+ gr.getBidSN()  
						+ "', '"  
						+ gr.getId() 
						+ "','"
						+ gr.getBuyid()+"<p>"+gr.getReceveid()
						+ "','"  
						+ gr.getReceveTime()
						+ "','"
						+ gr.getTid()
						+ "','"
						+ realmark
						+ gr.getRecevenum()
						+ "','"
						+ papermark
						+ gr.getRecevenum()
						+ "',"
						+ type
						+ ",(select realcount from mdinventorybranch where branchid = "
						+ gr.getBidSN()
						+ " and  type = '"
						+ gr.getTid()
						+ "' and typestatues = "+gr.getGoodtypeStatues() +")*1,(select papercount from mdinventorybranch where branchid = "
						+ gr.getBidSN()
						+ " and  type = '"
						+ gr.getTid()
						+ "' and typestatues = "+gr.getGoodtypeStatues() +")*1,"
						+ 1  
						+ ","
						+ gr.getBidSN() 
						+ ",21,(select realcount from mdinventorybranch where branchid = "
						+ gr.getTid()
						+ " and  type = '"
						+ gr.getTid()  
						+ "' and typestatues = "+gr.getGoodtypeStatues() +")*1"
						+ realmark + Integer.valueOf(gr.getRecevenum())  
						+ ",(select papercount from mdinventorybranch where branchid = "
						+ gr.getBidSN()    
						+ " and  type = '" 
						+ gr.getTid()    
						+ "' and typestatues = "+gr.getGoodtypeStatues() +")*1" 
						+ papermark + Integer.valueOf(gr.getRecevenum()) + ")";
  
			} 
			list.add(sql);   
		    list.add(sqlup);  
			list.add(sqlIB);
			list.add(sqlIBM);
		}
		return list;
	}

	public static GoodsReceipt getByid(String id) {
		Map<String, GoodsReceipt> map = new HashMap<String, GoodsReceipt>();
		String sql = " select * from goodsreceipt where id=   " + id;
 
		logger.info(sql);
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn);
		GoodsReceipt as = new GoodsReceipt();
		ResultSet rs = DB.getResultSet(stmt, sql);
		try { 
			while (rs.next()) { 
				as = getGoodsReceitFromRs(rs);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(stmt);
			DB.close(rs);
			DB.close(conn);
		}

		return as;
	}

	public static List<GoodsReceipt> getList(String starttime,
			String endtime) {
		List<GoodsReceipt> list = new ArrayList<GoodsReceipt>();
		String sql = " select * from goodsreceipt where  recevetime BETWEEN  '"
				+ starttime + "'  and '" + endtime + "'";
		logger.info(sql);
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn);

		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				GoodsReceipt as = getGoodsReceitFromRs(rs);
				list.add(as); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(stmt);
			DB.close(rs);
			DB.close(conn);
		}

		return list;
	}
	
	public static Map<String,Map<String,List<GoodsReceipt>>> getMapGoodNum(String starttime,
			String endtime) {
		Map<String,Map<String,List<GoodsReceipt>>> map = new HashMap<String,Map<String,List<GoodsReceipt>>>();
		List<GoodsReceipt> list = getList(starttime,endtime);
		if(null != list){  
			for(int i=0;i<list.size();i++){
				GoodsReceipt gr = list.get(i);
				Map<String,List<GoodsReceipt>> mapg = map.get(gr.getGoodsnum());
				if(null == mapg){
					mapg = new HashMap<String,List<GoodsReceipt>>();
					map.put(gr.getGoodsnum(), mapg);        
				}
				List<GoodsReceipt> li = mapg.get(gr.getBranchid());
				if(null == li){
					li = new ArrayList<GoodsReceipt>();
					mapg.put(gr.getBranchid(), li);
				}  
				li.add(gr); 
			} 
		}

		return map;
	}
	
	public static Map<String, GoodsReceipt> getMapAll(String starttime,
			String endtime) {
		Map<String, GoodsReceipt> map = new HashMap<String, GoodsReceipt>();
		String sql = " select * from goodsreceipt where  recevetime BETWEEN  '"
				+ starttime + "'  and '" + endtime + "'";
		logger.info(sql);
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn);

		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				GoodsReceipt as = getGoodsReceitFromRs(rs);
				map.put(as.getUuid(), as);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(stmt);
			DB.close(rs);
			DB.close(conn);
		}

		return map;
	}

	public static Map<String, GoodsReceipt> getMap(String starttime,
			String endtime) {
		Map<String, GoodsReceipt> map = new HashMap<String, GoodsReceipt>();
		String sql = " select * from goodsreceipt where disable = 1  ";
		logger.info(sql);
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn);

		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				GoodsReceipt as = getGoodsReceitFromRs(rs);
				// logger.info(as.getStatues()); 
				map.put(as.getUuid(), as);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(stmt);
			DB.close(rs);
			DB.close(conn);
		}

		return map;
	}

	public static Map<String, GoodsReceipt> getMap() {
		return getMap(TimeUtill.getdateString(), TimeUtill.getdateString());

	}

	
	
	public static boolean saveIN(Elements en,String starttime,String endtime) {
		int id = getMaxid();
 
		Map<String, GoodsReceipt> map = new HashMap<String, GoodsReceipt>();
		boolean flag = true;
		if (!en.isEmpty()) { 
			Iterator<Element> it = en.iterator();
			while (it.hasNext()) {
				Element e = it.next(); 
				// logger.info(e);
				Elements td = e.getElementsByTag("td");

				if (!td.isEmpty()) {
 
					Iterator<Element> tdit = td.iterator();
					int i = 0;
					GoodsReceipt gr = null;
					while (tdit.hasNext()) {
						i++;
						Element tde = tdit.next();

						String str = tde.text();
						if (i == 1) {
							Elements input = tde.getElementsByTag("input");
							Iterator<Element> itinput = input.iterator();
							while (itinput.hasNext()) {
								Element ei = itinput.next();
								String uid = ei.attr("value");
								gr = map.get(uid);
								if (null == gr) {
									gr = new GoodsReceipt();
									gr.setId(id);
									id++;
									gr.setUuid(uid);
									map.put(uid, gr);
								}
							}
						} else if (i == 2) {
							// logger.info(str);
							gr.setReceveid(str);
						} else if (i == 4) {
							gr.setReceveTime(str);
						} else if (i == 5) {
							gr.setSendid(str);
						} else if (i == 6) {
							gr.setBuyid(str);
						} else if (i == 8) {
							gr.setOrdertype(str);
						} else if (i == 9) {
							gr.setGoodsnum(str);
						} else if (i == 10) {
							gr.setGoodsName(str);
						} else if (i == 11) {
							gr.setRecevenum(Integer.valueOf(str));
						} else if (i == 12) {
							gr.setRefusenum(Integer.valueOf(str));
						} else if (i == 14) {
							gr.setBranchid(str);
						} else if (i == 15) {
							gr.setBranchName(str);
						}
					}
				} 
			} 
		}
 
		flag = save(map,starttime,endtime); 
		// GoodsReceitManager.map = map;
		// logger.info(StringUtill.GetJson(map));
		return flag;
	}
  
	public static boolean saveIN(CsvReader reader,String starttime,String endtime) {
		
		int id = getMaxid();
 
		Map<String, GoodsReceipt> map = new HashMap<String, GoodsReceipt>();
		boolean flag = true;
		try { 
			reader.readHeaders();
		while (reader.readRecord()) { // 逐行读入除表头的数据
			String[] strs = reader.getValues();
			if (null != strs) {  
					GoodsReceipt gr = null;
					String rows = "";
					String receveid = "";
					 
					for (int i = 0; i < strs.length; i++) {
						String str = strs[i]; 
						if (i == 0) {    
							receveid = str ; 
						} else if (i == 1) {  
							rows = str ;
							gr = map.get(receveid + "_" + rows);
							if (null == gr) {
								gr = new GoodsReceipt();
								gr.setId(id);   
								gr.setStatues(1); 
								id++;
								map.put(receveid + "_" + rows, gr);
							}  
							gr.setReceveid(receveid);
							gr.setUuid(receveid + "_" + rows);
						} else if (i == 2) {
							gr.setReceveTime(str);
						} else if (i == 3) {
							gr.setSendid(str);
						} else if (i == 4) { 
							gr.setBuyid(str); 
						} else if (i == 6) {
							gr.setOrdertype(str);
						} else if (i == 7) {
							 
							gr.setGoodsnum(str); 
						} else if (i == 8) { 
							gr.setGoodsName(str);
						} else if (i == 9) { 
							double realnum = Double.valueOf(str);
							int re = (int) realnum; 
							gr.setRecevenum(re); 
						} else if (i == 10) {
							double realnum = Double.valueOf(str);
							int re = (int) realnum;
							gr.setRefusenum(re);
						} else if (i == 12) { 
							gr.setBranchid(str);
						} else if (i == 13) {
							gr.setBranchName(str);
						}
					}
				} 
			} 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		flag = save(map,starttime,endtime); 
		// GoodsReceitManager.map = map;
		// logger.info(StringUtill.GetJson(map));
		return flag;
	}
 
public static boolean saveOut(CsvReader reader,String starttime,String endtime) {
		
		int id = getMaxid();
  
		Map<String, GoodsReceipt> map = new HashMap<String, GoodsReceipt>();
		boolean flag = true; 
		try { 
			reader.readHeaders();
		while (reader.readRecord()) { // 逐行读入除表头的数据
			String[] strs = reader.getValues();
			if (null != strs) {  
					GoodsReceipt gr = null;
					String rows = "";
					String receveid = "";
					   
					for (int i = 0; i < strs.length; i++) {
						String str = strs[i]; 
						if (i == 0) {     
							receveid = str ; 
						} else if (i == 1) {  
							rows = str ;
							gr = map.get(receveid + "_" + rows);
							if (null == gr) {
								gr = new GoodsReceipt();
								gr.setId(id);   
								gr.setStatues(1); 
								id++;
								map.put(receveid + "_" + rows, gr);
							}  
							gr.setReceveid(receveid);
							gr.setUuid(receveid + "_" + rows);
						} else if (i == 2) {
							gr.setBuyid(str);
						} else if (i == 5) {
							gr.setGoodsnum(str);

						}  else if (i == 6) {
							gr.setGoodsName(str);

						} else if (i == 7) {
							double realnum = Double.valueOf(str);
							int re = (int) realnum;
							gr.setRecevenum(re);
						} else if (i == 8) {
							gr.setOrdertype(str);
						} else if (i == 10) {
							gr.setBranchName(str);
						} else if (i == 11) {
							gr.setReceveTime(str);
						} 
					}
				} 
			}  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		flag = saveOut(map,starttime,endtime); 
		// GoodsReceitManager.map = map;
		// logger.info(StringUtill.GetJson(map));
		return flag;
	}

public static boolean saveOutModel(CsvReader reader,String starttime,String endtime) {
	 
	int id = getMaxid();

	Map<String, GoodsReceipt> map = new HashMap<String, GoodsReceipt>();
	boolean flag = true; 
	try { 
		reader.readHeaders();
	while (reader.readRecord()) { // 逐行读入除表头的数据
		String[] strs = reader.getValues();
		if (null != strs) {  
				GoodsReceipt gr = null;
				String rows = "";
				String receveid = "";
				   
				for (int i = 0; i < strs.length; i++) {
					String str = strs[i]; 
					if (i == 0) {     
						receveid = str ; 
					} else if (i == 1) {  
						rows = str ;
						gr = map.get(receveid + "_" + rows);
						if (null == gr) {
							gr = new GoodsReceipt();
							gr.setId(id);   
							gr.setStatues(1); 
							id++;
							map.put(receveid + "_" + rows, gr);
						}  
						gr.setReceveid(receveid);
						gr.setUuid(receveid + "_" + rows);
					} else if (i == 2) {
						gr.setBuyid(str);
					} else if (i == 5) {
						gr.setGoodsnum(str);

					}  else if (i == 6) {
						gr.setGoodsName(str);

					} else if (i == 7) {
						double realnum = Double.valueOf(str);
						int re = (int) realnum;
						gr.setRecevenum(re);
					} else if (i == 8) {
						gr.setOrdertype(str);
					} else if (i == 10) {
						gr.setBranchName(str);
					} else if (i == 11) {
						gr.setReceveTime(str);
					} 
				}
			} 
		}  
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  
	flag = saveOut(map,starttime,endtime); 
	// GoodsReceitManager.map = map;
	// logger.info(StringUtill.GetJson(map));
	return flag;
}


	public static boolean saveOut(Elements en,String starttime, String endtime) {
		int id = getMaxid();

		Map<String, GoodsReceipt> map = new HashMap<String, GoodsReceipt>();
		boolean flag = true;
		if (!en.isEmpty()) {
			Iterator<Element> it = en.iterator();
			while (it.hasNext()) {
				Element e = it.next();
				// logger.info(e);
				Elements td = e.getElementsByTag("td");

				if (!td.isEmpty()) {

					Iterator<Element> tdit = td.iterator();
					int i = 0;
					GoodsReceipt gr = null;
					String rows = "";
					String receveid = "";
					while (tdit.hasNext()) {
						i++;
						Element tde = tdit.next();
						String str = tde.text();
						// logger.info(str);
						if (i == 1) {
							receveid = str;
						} else if (i == 2) {
							rows = str;
							gr = map.get(receveid + "_" + rows);
							if (null == gr) {
								gr = new GoodsReceipt();
								gr.setId(id);  
								gr.setStatues(1); 
								id++;
								map.put(receveid + "_" + rows, gr);
							}
							gr.setReceveid(receveid);
							gr.setUuid(receveid + "_" + rows);
						} else if (i == 3) {
							gr.setBuyid(str);
						} else if (i == 6) {
							gr.setGoodsnum(str);

						}  else if (i == 7) {
							gr.setGoodsName(str);

						} else if (i == 8) {
							double realnum = Double.valueOf(str);
							int re = (int) realnum;
							gr.setRecevenum(re);
						} else if (i == 9) {
							gr.setOrdertype(str);
						} else if (i == 11) {
							gr.setBranchName(str);
						} else if (i == 12) {
							gr.setReceveTime(str);
						}
					}
				}
			}
		}
		// logger.info(map); 
		flag = saveOut(map,starttime, endtime);   
		// GoodsReceitManager.map = map; 
		// logger.info(StringUtill.GetJson(map));
		return flag;
	}

	
	public static boolean saveOutModel(Elements en,String starttime, String endtime) {
		int id = getMaxid();

		Map<String, GoodsReceipt> map = new HashMap<String, GoodsReceipt>();
		boolean flag = true;
		if (!en.isEmpty()) {
			Iterator<Element> it = en.iterator();
			while (it.hasNext()) {
				Element e = it.next();
				// logger.info(e);
				Elements td = e.getElementsByTag("td");
 
				if (!td.isEmpty()) {

					Iterator<Element> tdit = td.iterator();
					int i = 0;
					GoodsReceipt gr = null;
					String rows = "";
					String receveid = "";
					while (tdit.hasNext()) {
						i++;
						Element tde = tdit.next();
						String str = tde.text();
						// logger.info(str);
						if (i == 1) {
							receveid = str;
						} else if (i == 2) {
							rows = str;
							gr = map.get(receveid + "_" + rows);
							if (null == gr) {
								gr = new GoodsReceipt();
								gr.setId(id);  
								gr.setStatues(1); 
								id++;
								map.put(receveid + "_" + rows, gr);
							}
							gr.setReceveid(receveid);
							gr.setUuid(receveid + "_" + rows);
						} else if (i == 3) {
							gr.setBuyid(str);
						} else if (i == 5) {
							gr.setGoodsnum(str);

						} else if (i == 6) { 
							gr.setGoodsName(str);

						} else if (i == 7) {
							double realnum = Double.valueOf(str);
							int re = (int) realnum;
							gr.setRecevenum(re);
						} else if (i == 8) {
							gr.setOrdertype(str);
						} else if (i == 9) {
							gr.setBranchName(str);
						} else if (i == 10) { 
							gr.setReceveTime(str);
						}
					}
				}
			}
		} 
		// logger.info(map); 
		flag = saveOut(map,starttime, endtime);   
		// GoodsReceitManager.map = map; 
		// logger.info(StringUtill.GetJson(map));
		return flag;
	}

	
	public static boolean save(Map<String, GoodsReceipt> map,String starttime, String endtime) {
		boolean flag = false; 
		Map<Integer, Map<String, Map<Integer, InventoryBranch>>> mapin = InventoryBranchManager
				.getInventoryMap();
		Set<String> setup = new HashSet<String>();
		List<String> list = new ArrayList<String>();
		Map<String, GoodsReceipt> mapdb = getMapAll(starttime,
				endtime);
		Set<Map.Entry<String, GoodsReceipt>> en = map.entrySet();
		if (!en.isEmpty()) {
			Iterator<Map.Entry<String, GoodsReceipt>> it = en.iterator();
			while (it.hasNext()) {
				Map.Entry<String, GoodsReceipt> ent = it.next();
				GoodsReceipt gr = ent.getValue();
				GoodsReceipt db = mapdb.get(gr.getUuid());
				if (null == db) {
					List<String> sql = saveIN(mapin,gr,setup);
					list.addAll(sql);
				}
			}
		}

		flag = DBUtill.sava(list);
		return flag;
	} 
 
	public static boolean saveOut(Map<String, GoodsReceipt> map,String starttime, String endtime) {
		boolean flag = false; 
		 
		Map<Integer, Map<String, Map<Integer, InventoryBranch>>> mapin = InventoryBranchManager
				.getInventoryMap(); 
		Set<String> setup = new HashSet<String>();
		List<String> list = new ArrayList<String>();
		Map<String, GoodsReceipt> mapdb = getMapAll(starttime,
				endtime); 
		Set<Map.Entry<String, GoodsReceipt>> en = map.entrySet();
		if (!en.isEmpty()) {
			Iterator<Map.Entry<String, GoodsReceipt>> it = en.iterator();
			while (it.hasNext()) {
				Map.Entry<String, GoodsReceipt> ent = it.next();
				GoodsReceipt gr = ent.getValue(); 
				GoodsReceipt db = mapdb.get(gr.getUuid());
				if (null == db) {
					List<String> sql = saveOut(mapin,gr,setup);
					list.addAll(sql);
				} 
			}
		}

		flag = DBUtill.sava(list);
		return flag;
	}

	public static int getMaxid() {
		int id = 1;
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn);
		String sql = "select max(id)+1 as id from goodsreceipt";
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

	public static GoodsReceipt getGoodsReceitFromRs(ResultSet rs) {
		GoodsReceipt p = null;
		try {
			p = new GoodsReceipt();
			p.setBranchid(rs.getString("branchid"));
			p.setBranchName(rs.getString("branchname"));
			p.setBuyid(rs.getString("buyid"));
			p.setGoodsName(rs.getString("goodsname"));
			p.setGoodsnum(rs.getString("goodsnum")); 
			p.setId(rs.getInt("id"));
			p.setOrdertype(rs.getString("ordertype"));
			p.setReceveid(rs.getString("receveid"));
			p.setRecevenum(rs.getInt("recevenum"));
			p.setReceveTime(rs.getString("recevetime"));
			p.setSendid(rs.getString("sendid"));
			p.setUuid(rs.getString("uuid"));
			p.setDisable(rs.getInt("disable"));
			p.setStatues(rs.getInt("statues")); 
		} catch (SQLException e) {
			p = null; 
			// logger.info(e); 
			return p;

		}
		return p;
	}

}
