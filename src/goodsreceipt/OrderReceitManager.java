package goodsreceipt;
 
import inventory.InventoryBranchManager;
  
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;  
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import database.DB;

import user.User;
import utill.DBUtill;
import utill.StringUtill;
import utill.TimeUtill;

public class OrderReceitManager {
	protected static Log logger = LogFactory.getLog(OrderReceitManager.class);

	public static List<String> saveOut(OrderReceipt gr) {
		List<String> list = new ArrayList<String>();
		if (0 != gr.getRefusenum()) {
			String sql = " insert into orderreceipt (id,querynum,checknum,pici,activeordertiem,receveid,recevetime,sendid,buyid,ordertype,goodsnum,goodsname,ordernum,recevenum,refusenum,branchid,branchname,uuid,disable,statues)"
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
					+ gr.getDisable() + "," + gr.getStatues() + ");";
			list.add(sql);
		}
		return list;
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

	public static List<OrderReceipt> getList(String starttime, String endtime) {
		List<OrderReceipt> list = new ArrayList<OrderReceipt>();
		String sql = " select * from orderreceipt where refusenum != 0  order by  recevetime";
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
		String sql = " select * from orderreceipt where refusenum = 0  order by  recevetime";
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
 
	public static List<OrderReceipt> getListOver(String buyid) {
		List<OrderReceipt> list = new ArrayList<OrderReceipt>(); 
		String sql = " select * from orderreceipt where refusenum = 0  and buyid = '"
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
		} catch (SQLException e) {
			p = null;
			// logger.info(e);
			return p;

		}
		return p;
	}

}
