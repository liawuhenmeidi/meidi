package goodsreceipt;
 
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
import java.util.LinkedHashMap;
import java.util.List;  
import java.util.Map;
import java.util.Set;

import ordersgoods.OrderGoodsAllManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.csvreader.CsvReader;

import database.DB;

import user.User;
import utill.DBUtill;
import utill.StringUtill;
import utill.TimeUtill;
 
public class OrderReceitManager {
	protected static Log logger = LogFactory.getLog(OrderReceitManager.class);
 
	public static List<String> saveOut(OrderReceipt gr) {
		List<String> list = new ArrayList<String>();
		//if (0 != gr.getRefusenum()) {
			String sql = " insert into orderreceipt (id,querynum,checknum,pici,activeordertiem,receveid,recevetime,sendid,buyid,ordertype,goodsnum,goodsname,ordernum,recevenum,refusenum,branchid,branchname,uuid,disable,statues,statuesName)"
					+ " values ('"
					+ gr.getId()
					+ "','"
					+ gr.getQueryNum()
					+ "','"
					+ gr.getCheckNum()
					+ "','"
					+ gr.getPici()
					+ "','"
					+ gr.getActiveordertiem()
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
					+ gr.getOrderNum()
					+ "','"
					+ gr.getRecevenum()
					+ "','"
					+ gr.getRefusenum()
					+ "','"
					+ gr.getBranchid()
					+ "','"
					+ gr.getBranchName()
					+ "','" 
					+ gr.getUuid()
					+ "',"
					+ gr.getDisable() + "," + gr.getStatues() + ",'"+gr.getStatuesName()+"');";
			list.add(sql);
		//} 
		return list;
	}  
     
	public static List<String> update(OrderReceipt gr) {
		List<String> list = new ArrayList<String>(); 
		//if (0 != gr.getRefusenum()) { 
			String sql = " update orderreceipt set ordernum ='"
					+ gr.getOrderNum()
					+ "' ,recevenum = '"
					+ gr.getRecevenum()
					+ "',refusenum = '"
					+ gr.getRefusenum()
					+ "',statuesName = '"+gr.getStatuesName()+"' where uuid = '"+gr.getUuid()+"'";
			list.add(sql);  
		//} 
		return list;
	}  
	
	public static List<String> billing(User user ,Map<String,OrderReceipt> map,String[] ids,String buyid) { 
		List<String> list = OrderGoodsAllManager.save(user, map,ids,buyid);    
		return list;
	}
	 
	public static String updateDiable(OrderReceipt gr) { 
		String str = "update orderreceipt set disable = 1 where id = "+gr.getId();
		return str;
	} 
	 
	public static String updatesPrintNum(OrderReceipt gr) { 
		String str = "update orderreceipt set printNum = printNum+"+1+" where id = "+gr.getId();
		return str;
	}
	  
	public static String update(GoodsReceipt gr) {
		String str = "update orderreceipt set recevenum = orderreceipt.recevenum +"
				+ gr.getRecevenum()
				+ ",refusenum = orderreceipt.refusenum -"
				+ gr.getRecevenum() 
				+ " where goodsnum= '"
				+ gr.getGoodsnum()
				+ "' and branchname = '"
				+ gr.getBranchName()
				+ "' and buyid = " + gr.getBuyid();

		return str;
	}

	public static Map<String, OrderReceipt> getMapAll(String starttime,
			String endtime) {
		Map<String, OrderReceipt> map = new HashMap<String, OrderReceipt>();
		String sql = " select * from orderreceipt where  recevetime BETWEEN  '"
				+ starttime + "'  and '" + endtime + "'";
		logger.info(sql);
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn);

		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				OrderReceipt as = getOrderReceitFromRs(rs);
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
  
	public static List<OrderReceipt> getList(String typenum,String branchName) {
		List<OrderReceipt> list = new ArrayList<OrderReceipt>(); 
		String sql = " select * from orderreceipt where goodsnum = '"+typenum+"' and branchname = '"+branchName+"' order by  recevetime";
		logger.info(sql); 
		Connection conn = DB.getConn();   
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				OrderReceipt as = getOrderReceitFromRs(rs);
				// logger.info(as.getOrderNum());
				// logger.info(as.getRecevenum());
				// logger.info(as.getRefusenum());
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
  
	public static List<OrderReceipt> getList() { 
		List<OrderReceipt> list = new ArrayList<OrderReceipt>();
		String sql = " select * from orderreceipt where refusenum != 0 and disable != 2  order by  recevetime";
		logger.info(sql); 
		Connection conn = DB.getConn();   
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				OrderReceipt as = getOrderReceitFromRs(rs);
				// logger.info(as.getOrderNum());
				// logger.info(as.getRecevenum());
				// logger.info(as.getRefusenum());
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
	
	public static List<OrderReceipt> getListAll() {
		List<OrderReceipt> list = new ArrayList<OrderReceipt>();
		String sql = " select * from orderreceipt  where  disable != 2 order by  recevetime";
		logger.info(sql);  
		Connection conn = DB.getConn();   
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				OrderReceipt as = getOrderReceitFromRs(rs);
				// logger.info(as.getOrderNum());
				// logger.info(as.getRecevenum());
				// logger.info(as.getRefusenum());
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
	 
	
	public static List<OrderReceipt> getListToDeal() {
		List<OrderReceipt> list = new ArrayList<OrderReceipt>();  
		String sql = " select * from orderreceipt  where disable = 0  order by  recevetime";
		logger.info(sql);
		Connection conn = DB.getConn();   
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				OrderReceipt as = getOrderReceitFromRs(rs);
				// logger.info(as.getOrderNum());
				// logger.info(as.getRecevenum());
				// logger.info(as.getRefusenum());
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
	
	public static List<OrderReceipt> getListOver() {
		List<OrderReceipt> list = new ArrayList<OrderReceipt>(); 
		String sql = " select * from orderreceipt where refusenum = 0  and disable = 0 order by  recevetime";
		logger.info(sql);
		Connection conn = DB.getConn();   
		Statement stmt = DB.getStatement(conn); 
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				OrderReceipt as = getOrderReceitFromRs(rs);
				// logger.info(as.getOrderNum());
				// logger.info(as.getRecevenum());
				// logger.info(as.getRefusenum());
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
	
	public static List<OrderReceipt> getList(String buyid) {
		List<OrderReceipt> list = new ArrayList<OrderReceipt>();
		String sql = " select * from orderreceipt where refusenum != 0  and buyid = '"
				+ buyid + "' order by branchid";
		logger.info(sql); 
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try { 
			while (rs.next()) {
				OrderReceipt as = getOrderReceitFromRs(rs);
				// logger.info(as.getOrderNum());
				// logger.info(as.getRecevenum());
				// logger.info(as.getRefusenum());
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
  
	
	public static Map<String,OrderReceipt> getMapOver(String buyid) {
		Map<String,OrderReceipt> map = new LinkedHashMap<String,OrderReceipt>();
		List<OrderReceipt> list = getListOver(buyid); 
		if(null != list){ 
			for(int i=0;i<list.size();i++){ 
				OrderReceipt or = list.get(i);
				map.put(or.getId()+"", or);  
			}
		}
		
		return map;
	} 
	 
	public static Map<String,OrderReceipt> getMap(String buyid,String branch) { 
		Map<String,OrderReceipt> map = new LinkedHashMap<String,OrderReceipt>();
		List<OrderReceipt> list = getList(buyid); 
		if(null != list){    
			for(int i=0;i<list.size();i++){  
				OrderReceipt or = list.get(i);
				if(StringUtill.isNull(branch) || !StringUtill.isNull(branch) && branch.equals(or.getBranchName())){
					map.put(or.getId()+"", or);  
				}
				
			}
		}
		
		return map;
	}
	 
	public static Map<String,OrderReceipt> getMapTypeBranch(String typenum,String branchName) { 
		Map<String,OrderReceipt> map = new LinkedHashMap<String,OrderReceipt>();
		List<OrderReceipt> list = getList(typenum,branchName);    
		if(null != list){      
			for(int i=0;i<list.size();i++){ 
				OrderReceipt or = list.get(i);
				// logger.info(or.getReceveTime()); 
					map.put(or.getId()+"", or);  
				
				 
			}
		}
		 
		return map;
	}  
	  
	public static void delete(String[] ids) {  
		String sql = "update orderreceipt set disable = 2 where id in "+StringUtill.getStr(ids);
		DBUtill.sava(sql); 
	}
	
	public static List<OrderReceipt> getListOver(String buyid) { 
		List<OrderReceipt> list = new ArrayList<OrderReceipt>(); 
		String sql = " select * from orderreceipt where refusenum = 0  and disable = 0 and buyid = '"
				+ buyid + "'"; 
		logger.info(sql);
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn);
		ResultSet rs = DB.getResultSet(stmt, sql);
		try {
			while (rs.next()) {
				OrderReceipt as = getOrderReceitFromRs(rs);
				// logger.info(as.getOrderNum());
				// logger.info(as.getRecevenum());
				// logger.info(as.getRefusenum());
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

	
	/*
	 * public static Map<String, List<OrderReceipt>> getMapAll(String buyid) {
	 * List<OrderReceipt> list = getList(buyid); Map<String,List<OrderReceipt>>
	 * map = new HashMap<String,List<OrderReceipt>>();
	 * 
	 * if(null != list){ for(int i=0;i<list.size();i++){ OrderReceipt or =
	 * list.get(i); List<OrderReceipt> li = map.get(or.getBid()); if(null ==
	 * li){ li = new ArrayList<OrderReceipt>(); map.put(or.getBuyid(), li); }
	 * li.add(or); } } return map;
	 * 
	 * }
	 */
	public static Map<String, OrderReceiptAll> getMapAll() {
		List<OrderReceipt> list = getList(TimeUtill.getdateString(),
				TimeUtill.getdateString());
		Map<String, OrderReceiptAll> map = new LinkedHashMap<String, OrderReceiptAll>();
 
		if (null != list) { 
			for (int i = 0; i < list.size(); i++) {
				OrderReceipt or = list.get(i);
				OrderReceiptAll li = map.get(or.getBuyid());
				if (null == li) { 
					li = new OrderReceiptAll();
					Set<Integer> set = new HashSet<Integer>();
					set.add(or.getPrintNum());
					li.setPrintstatues(set);  
					li.setBuyid(or.getBuyid()); 
					li.setCheckNum(or.getCheckNum());
					li.setActiveordertiem(or.getActiveordertiem());
					li.setReceveTime(or.getReceveTime());
					map.put(or.getBuyid(), li); 
				}else { 
					li.getPrintstatues().add(or.getPrintNum());
				}
			}
		}
		return map;

	}

	public static Map<String, OrderReceiptAll> getMapAllOver() {
		List<OrderReceipt> list = getListOver(); 
		Map<String, OrderReceiptAll> map = new LinkedHashMap<String, OrderReceiptAll>();
 
		if (null != list) { 
			for (int i = 0; i < list.size(); i++) {
				OrderReceipt or = list.get(i);
				OrderReceiptAll li = map.get(or.getBuyid());
				if (null == li) {
					li = new OrderReceiptAll();
					li.setBuyid(or.getBuyid());
					li.setCheckNum(or.getCheckNum());
					li.setActiveordertiem(or.getActiveordertiem());
					li.setReceveTime(or.getReceveTime());
					map.put(or.getBuyid(), li);
				}
			}
		}
		return map;

	}
	
	/*
	 * public static Map<String, OrderReceipt> getMap() { return
	 * getMap(TimeUtill.getdateString(), TimeUtill.getdateString());
	 * 
	 * }
	 */

public static boolean saveOut(CsvReader reader,String starttime,String endtime) {
		
		int id = getMaxid();
   
		Map<String, OrderReceipt> map = new HashMap<String, OrderReceipt>();
		
		boolean flag = true; 
		try {   
			reader.readHeaders();
		while (reader.readRecord()) { // 逐行读入除表头的数据
			String[] strs = reader.getValues();
			if (null != strs) {  
				OrderReceipt gr = new OrderReceipt();
				String rows = "";
				String receveid = "";
					   
					for (int i = 0; i < strs.length; i++) {
						String str = strs[i]; 
						// logger.info(str);
						if (i == 1) {
							gr.setQueryNum(str);
						} else if (i == 3) {
							receveid = str;
							gr.setBuyid(str);
						} else if (i == 4) {
							gr.setCheckNum(str);
						} else if (i == 5) {
							rows = str;
							OrderReceipt grmap = map.get(receveid + "_" + rows);
							if (null == grmap) {
								gr.setId(id);
								gr.setStatues(1);
								id++;
								map.put(receveid + "_" + rows, gr);
							}
							gr.setUuid(receveid + "_" + rows);
						} else if (i == 6) {
							gr.setGoodsnum(str);
						} else if (i == 7) {
							gr.setGoodsName(str);
						} else if (i == 9) {
							gr.setBranchid(str);
							// gr.setOrdertype(str);
						} else if (i == 10) {
							gr.setBranchName(str);
						} else if (i == 11) {
							double realnum = Double.valueOf(str);
							int re = (int) realnum;
							gr.setOrderNum(Integer.valueOf(re));
						} else if (i == 12) {
							double realnum = Double.valueOf(str);
							int re = (int) realnum;
							gr.setRecevenum(re);
						} else if (i == 13) {
							double realnum = Double.valueOf(str);
							int re = (int) realnum;
							gr.setRefusenum(re);
						} else if (i == 14) {
							gr.setReceveTime(str);
						} else if (i == 15) {
							gr.setOrdertype(str);
						} else if (i == 16) {
							gr.setActiveordertiem(str);
						} else if (i == 17) {
							gr.setStatuesName(str);
						} else if (i == 20) {
							gr.setPici(str);
						}
					}
				} 
			}  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}  
		 
		flag = saveOut(map, starttime, endtime);
		// GoodsReceitManager.map = map;
		// logger.info(StringUtill.GetJson(map));
		return flag;
	}
 
public static boolean saveOutModel(CsvReader reader,String starttime,String endtime) {
	
	int id = getMaxid();

	Map<String, OrderReceipt> map = new HashMap<String, OrderReceipt>();
	
	boolean flag = true; 
	try {   
		reader.readHeaders();
	while (reader.readRecord()) { // 逐行读入除表头的数据
		String[] strs = reader.getValues();
		if (null != strs) {  
			OrderReceipt gr = new OrderReceipt();
			String rows = "";
			String receveid = "";
				   
				for (int i = 0; i < strs.length; i++) {
					String str = strs[i]; 
					// logger.info(str);
					if (i == 1) {
						gr.setQueryNum(str);
					} else if (i == 3) {
						receveid = str;
						gr.setBuyid(str);
					} else if (i == 4) {
						gr.setCheckNum(str);
					} else if (i == 5) {
						rows = str;
						OrderReceipt grmap = map.get(receveid + "_" + rows);
						if (null == grmap) {
							gr.setId(id);
							gr.setStatues(1);
							id++;
							map.put(receveid + "_" + rows, gr);
						}
						gr.setUuid(receveid + "_" + rows);
					} else if (i == 6) {
						gr.setGoodsnum(str);
					} else if (i == 7) {
						gr.setGoodsName(str);
					} else if (i == 9) {
						gr.setBranchid(str);
						// gr.setOrdertype(str);
					} else if (i == 10) {
						gr.setBranchName(str);
					} else if (i == 11) {
						double realnum = Double.valueOf(str);
						int re = (int) realnum;
						gr.setOrderNum(Integer.valueOf(re));
					} else if (i == 12) {
						double realnum = Double.valueOf(str);
						int re = (int) realnum;
						gr.setRecevenum(re);
					} else if (i == 13) {
						double realnum = Double.valueOf(str);
						int re = (int) realnum;
						gr.setRefusenum(re);
					} else if (i == 14) {
						gr.setReceveTime(str);
					} else if (i == 15) {
						gr.setOrdertype(str);
					} else if (i == 16) {
						gr.setActiveordertiem(str);
					} else if (i == 17) {
						gr.setStatuesName(str);
					} else if (i == 20) {
						gr.setPici(str);
					}
				}
			} 
		}  
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace(); 
	}  
	 
	flag = saveOut(map, starttime, endtime);
	// GoodsReceitManager.map = map;
	// logger.info(StringUtill.GetJson(map));
	return flag;
}

	public static boolean saveOut(Elements en, String starttime, String endtime) {
		int id = getMaxid();

		Map<String, OrderReceipt> map = new HashMap<String, OrderReceipt>();
		
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
					OrderReceipt gr = new OrderReceipt();
					String rows = "";
					String receveid = "";
					while (tdit.hasNext()) {
						i++;
						Element tde = tdit.next();
						String str = tde.text();
						// logger.info(str);
						if (i == 1) {
							gr.setQueryNum(str);
						} else if (i == 3) {
							receveid = str;
							gr.setBuyid(str);
						} else if (i == 4) {
							gr.setCheckNum(str);
						} else if (i == 5) {
							rows = str;
							OrderReceipt grmap = map.get(receveid + "_" + rows);
							if (null == grmap) {
								gr.setId(id);
								gr.setStatues(1);
								id++;
								map.put(receveid + "_" + rows, gr);
							}
							gr.setUuid(receveid + "_" + rows);
						} else if (i == 6) {
							gr.setGoodsnum(str);
						} else if (i == 7) {
							gr.setGoodsName(str);
						} else if (i == 9) {
							gr.setBranchid(str);
							// gr.setOrdertype(str);
						} else if (i == 10) {
							gr.setBranchName(str);
						} else if (i == 11) {
							double realnum = Double.valueOf(str);
							int re = (int) realnum;
							gr.setOrderNum(Integer.valueOf(re));
						} else if (i == 12) {
							double realnum = Double.valueOf(str);
							int re = (int) realnum;
							gr.setRecevenum(re);
						} else if (i == 13) {
							double realnum = Double.valueOf(str);
							int re = (int) realnum;
							gr.setRefusenum(re);
						} else if (i == 14) {
							gr.setReceveTime(str);
						} else if (i == 15) {
							gr.setOrdertype(str);
						} else if (i == 16) {
							gr.setActiveordertiem(str);
						} else if (i == 17) {
							gr.setStatuesName(str);
						} else if (i == 20) {
							gr.setPici(str);
						}
					}
				} 
			}
		}
		// logger.info(map);
		flag = saveOut(map, starttime, endtime);
		// GoodsReceitManager.map = map;
		// logger.info(StringUtill.GetJson(map));
		return flag;
	}

	public static boolean saveOutModel(Elements en, String starttime,
			String endtime) {
		int id = getMaxid();

		Map<String, OrderReceipt> map = new HashMap<String, OrderReceipt>();
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
					OrderReceipt gr = new OrderReceipt();
					String rows = "";
					String receveid = "";
					while (tdit.hasNext()) {
						i++;
						Element tde = tdit.next();
						String str = tde.text();
						// logger.info(str);
						if (i == 1) {
							receveid = str;
							gr.setBuyid(str);
							// gr.setQueryNum(str);
						} else if (i == 2) {
							gr.setCheckNum(str);
						} else if (i == 3) {
							rows = str;
							OrderReceipt grmap = map.get(receveid + "_" + rows);
							if (null == grmap) {
								gr.setId(id);
								gr.setStatues(1);
								id++;
								map.put(receveid + "_" + rows, gr);
							}
							gr.setUuid(receveid + "_" + rows);
						} else if (i == 4) {
							gr.setGoodsnum(str);
						} else if (i == 5) {
							gr.setGoodsName(str);
						} else if (i == 7) {
							gr.setBranchid(str);
							// gr.setOrdertype(str);
						} else if (i == 8) {
							gr.setBranchName(str);
						} else if (i == 9) {
							double realnum = Double.valueOf(str);
							int re = (int) realnum;
							gr.setOrderNum(Integer.valueOf(re));
						} else if (i == 10) {
							double realnum = Double.valueOf(str);
							int re = (int) realnum;
							gr.setRecevenum(re);
						} else if (i == 11) {
							double realnum = Double.valueOf(str);
							int re = (int) realnum;
							gr.setRefusenum(re);
						} else if (i == 12) {
							gr.setReceveTime(str);
						} else if (i == 13) { 
							gr.setOrdertype(str);
						} else if (i == 14) {
							gr.setActiveordertiem(str);
						}else if (i == 15) {
							gr.setStatuesName(str);
						}
					}
				}
			}
		}
		// logger.info(map);
		flag = saveOut(map, starttime, endtime);
		// GoodsReceitManager.map = map;
		// logger.info(StringUtill.GetJson(map));
		return flag;
	}

	public static boolean saveOut(Map<String, OrderReceipt> map,
			String starttime, String endtime) {
		boolean flag = false;
		// logger.info(map.size()); 
		List<String> list = new ArrayList<String>();
		Map<String, OrderReceipt> mapdb = getMapAll(starttime, endtime);
		Set<Map.Entry<String, OrderReceipt>> en = map.entrySet();
		if (!en.isEmpty()) {
			Iterator<Map.Entry<String, OrderReceipt>> it = en.iterator();
			while (it.hasNext()) {
				Map.Entry<String, OrderReceipt> ent = it.next();
				OrderReceipt gr = ent.getValue();  
				// logger.info(StringUtill.GetJson(gr)); 
				// logger.info(gr.getUuid()); 
				OrderReceipt db = mapdb.get(gr.getUuid());
				// logger.info(db);      
				if (null == db) {       
					List<String> sql = saveOut(gr);
					// logger.info(sql.size());
					list.addAll(sql);
				}else {  
					List<String> sql = update(gr);
					// logger.info(sql.size());
					list.addAll(sql);
				}
			}
		}
		// logger.info(list);
		flag = DBUtill.sava(list);
		return flag;
	}

	public static int getMaxid() {
		int id = 1;
		Connection conn = DB.getConn();
		Statement stmt = DB.getStatement(conn);
		String sql = "select max(id)+1 as id from orderreceipt";
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

	public static OrderReceipt getOrderReceitFromRs(ResultSet rs) {
		OrderReceipt p = null;
		try {
			p = new OrderReceipt();
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
			p.setQueryNum(rs.getString("querynum"));
			p.setPici(rs.getString("pici"));
			p.setOrderNum(rs.getInt("ordernum"));
			p.setActiveordertiem(rs.getString("activeordertiem"));
			p.setCheckNum(rs.getString("checknum"));
			p.setRefusenum(rs.getInt("refusenum"));  
			p.setPrintNum(rs.getInt("printnum")); 
			p.setStatuesName(rs.getString("statuesname"));
		} catch (SQLException e) {
			p = null;
			// logger.info(e);
			return p;

		}
		return p;
	}

}
