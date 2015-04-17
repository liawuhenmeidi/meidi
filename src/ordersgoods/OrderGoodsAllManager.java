package ordersgoods;
 
import inventory.InventoryBranchManager;

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

import branch.BranchService;

import database.DB;

import user.User;
import user.UserService;
import utill.DBUtill;
import utill.StringUtill;
import utill.TimeUtill;
 
public class OrderGoodsAllManager {
	protected static Log logger = LogFactory.getLog(OrderGoodsAllManager.class);
   
	public static boolean save(User user, OrderGoodsAll oa) {
		List<String> listsql = new ArrayList<String>();
		List<String> sqlom = OrderMessageManager.save(oa); 
		List<String> listog = OrderGoodsManager.save(user, oa);
		listsql.addAll(sqlom);
		listsql.addAll(listog);

		return DBUtill.sava(listsql);
	}

	public static Map<String, OrderGoodsAll> getmap(User user) {
		Map<String, OrderGoodsAll> map = new HashMap<String, OrderGoodsAll>();
		List<OrderGoodsAll> list = OrderGoodsAllManager.getlist(user);
		for (int i = 0; i < list.size(); i++) {
			OrderGoodsAll oal = list.get(i);
			OrderGoodsAll oa = map.get(oal.getOm().getId() + "");
			if (null == oa) {
				map.put(oal.getOm().getId() + "", oal);
			} else {
				oa.getList().addAll(oal.getList());
			}
		}
		return map;
	}

	public static Map<String, OrderGoodsAll> getmap(User user, int statues) {
		Map<String, OrderGoodsAll> map = new HashMap<String, OrderGoodsAll>();
		List<OrderGoodsAll> list = OrderGoodsAllManager.getlist(user, statues);
		for (int i = 0; i < list.size(); i++) {
			OrderGoodsAll oal = list.get(i);
			OrderGoodsAll oa = map.get(oal.getOm().getId() + "");
			if (null == oa) {
				map.put(oal.getOm().getId() + "", oal);
			} else {
				oa.getList().addAll(oal.getList());
			}
		}
		return map;
	}

	public static Map<String, OrderGoodsAll> getsendmap(User user, int statues) {
		Map<String, OrderGoodsAll> map = new HashMap<String, OrderGoodsAll>();
		List<OrderGoodsAll> list = OrderGoodsAllManager.getsendlist(user,
				statues);
		for (int i = 0; i < list.size(); i++) {
			OrderGoodsAll oal = list.get(i);
			OrderGoodsAll oa = map.get(oal.getOm().getId() + "");
			if (null == oa) {
				map.put(oal.getOm().getId() + "", oal);
			} else {
				oa.getList().addAll(oal.getList());
			}
		}
		return map;
	}

	public static Map<String, Map<String, List<OrderGoods>>> getbillingmap(
			User user, int statues) {
		Map<String, Map<String, List<OrderGoods>>> map = new HashMap<String, Map<String, List<OrderGoods>>>();
		List<OrderGoodsAll> list = OrderGoodsAllManager.getbillinglist(user);
		for (int i = 0; i < list.size(); i++) {
			OrderGoodsAll oal = list.get(i);
			List<OrderGoods> listog = oal.getList();
			if (null != listog) {
				for (int j = 0; j < listog.size(); j++) {
					OrderGoods og = listog.get(j);
					Map<String, List<OrderGoods>> maps = map.get(og.getUuid());
					if (null == maps) {
						maps = new HashMap<String, List<OrderGoods>>();
						map.put(og.getUuid(), maps);
					}
					List<OrderGoods> listogm = maps.get(og.getOid());
					if (null == listogm) {
						listogm = new ArrayList<OrderGoods>();
						maps.put(og.getOid(), listogm);
					}
					listogm.add(og);
				}
			}

		}
		return map;
	}

	public static Map<String, OrderGoodsAll> getmap(User user, int statues,
			String[] ids, String[] statue) {
		Map<String, OrderGoodsAll> map = new HashMap<String, OrderGoodsAll>();
		List<OrderGoodsAll> list = OrderGoodsAllManager.getlist(user, statues,
				ids, statue);
		for (int i = 0; i < list.size(); i++) {
			OrderGoodsAll oal = list.get(i);
			OrderGoodsAll oa = map.get(oal.getOm().getId() + "");
			if (null == oa) {
				map.put(oal.getOm().getId() + "", oal);
			} else {
				oa.getList().addAll(oal.getList());
			}
		}
		return map;
	}

	public static OrderGoodsAll getOrderGoodsAllByid(User user, String id) {
		List<Integer> listids = UserService.GetListson(user);
		// logger.info(listids);
		Connection conn = DB.getConn();
		OrderGoodsAll og = null;
		String sql = " select * from mdordergoods,mdordermessage  where mdordermessage.submitid in ("
				+ listids.toString().substring(1,
						listids.toString().length() - 1)
				+ " ) and mdordergoods.mid = mdordermessage.id and mdordermessage.id = "
				+ id;

		logger.info(sql);
		Statement stmt = DB.getStatement(conn);

		ResultSet rs = DB.getResultSet(stmt, sql);
		// logger.info(rs);
		try {
			while (rs.next()) {

				OrderGoodsAll ogs = getOrderGoodsAllFromRs(rs);
				if (null == og) {
					og = ogs;
				} else {
					og.getList().addAll(ogs.getList());
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		return og;
	}

	public static OrderGoodsAll getOrderGoodsAllBySendid(User user, String id,
			String statues) {
		List<Integer> listids = UserService.GetListson(user);
		// logger.info(listids);
		Connection conn = DB.getConn();
		OrderGoodsAll og = null;
		String sql = " select * from mdordergoods,mdordermessage  where mdordermessage.id = "
				+ id
				+ " and mdordergoods.mid = mdordermessage.id and mdordergoods.billingstatues = "
				+ statues
				+ " and mdordermessage.submitid in ("
				+ listids.toString().substring(1,
						listids.toString().length() - 1) + " ) ";

		logger.info(sql);
		Statement stmt = DB.getStatement(conn);

		ResultSet rs = DB.getResultSet(stmt, sql);
		// logger.info(rs);
		try {
			while (rs.next()) {

				OrderGoodsAll ogs = getOrderGoodsAllFromRs(rs);
				if (null == og) {
					og = ogs;
				} else {
					og.getList().addAll(ogs.getList());
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		return og;
	}

	public static OrderGoodsAll getOrderGoodsAllByid(User user, String id,
			int opstatues, String type) {
		List<Integer> listids = UserService.GetListson(user);
		// logger.info(listids);
		Connection conn = DB.getConn();
		OrderGoodsAll og = null;
		String sql = " select * from mdordergoods,mdordermessage  where mdordermessage.id = "
				+ id
				+ " and  mdordergoods.mid = mdordermessage.id and  mdordergoods.opstatues = "
				+ opstatues
				+ " and mdordermessage.opstatues = "
				+ type
				+ " and mdordermessage.submitid in ("
				+ listids.toString().substring(1,
						listids.toString().length() - 1) + " ) ";

		logger.info(sql);
		Statement stmt = DB.getStatement(conn);

		ResultSet rs = DB.getResultSet(stmt, sql);
		// logger.info(rs);
		try {
			while (rs.next()) {

				OrderGoodsAll ogs = getOrderGoodsAllFromRs(rs);
				if (null == og) {
					og = ogs;
				} else {
					og.getList().addAll(ogs.getList());
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(stmt);
			DB.close(conn);
		}
		return og;
	}

	public static List<OrderGoodsAll> getlist(User user) {
		List<OrderGoodsAll> list = new ArrayList<OrderGoodsAll>();

		List<Integer> listids = UserService.GetListson(user);
		// logger.info(listids);
		Connection conn = DB.getConn();

		String sql = " select * from mdordergoods,mdordermessage  where mdordermessage.submitid in ("
				+ listids.toString().substring(1,
						listids.toString().length() - 1)
				+ " ) and mdordergoods.mid = mdordermessage.id";

		logger.info(sql);
		Statement stmt = DB.getStatement(conn);

		ResultSet rs = DB.getResultSet(stmt, sql);
		// logger.info(rs);
		try {
			while (rs.next()) {

				OrderGoodsAll og = getOrderGoodsAllFromRs(rs);
				list.add(og);
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

	public static List<OrderGoodsAll> getsendlist(User user, int opstatues) {
		List<OrderGoodsAll> list = new ArrayList<OrderGoodsAll>();

		List<Integer> listids = UserService.GetListson(user);
		// logger.info(listids);
		Connection conn = DB.getConn();

		String sql = " select * from mdordergoods,mdordermessage  where mdordergoods.mid = mdordermessage.id and mdordergoods.billingstatues = "
				+ opstatues
				+ " and mdordermessage.opstatues = 1 and mdordermessage.submitid in ("
				+ listids.toString().substring(1,
						listids.toString().length() - 1) + " )";

		logger.info(sql);
		Statement stmt = DB.getStatement(conn);

		ResultSet rs = DB.getResultSet(stmt, sql);
		// logger.info(rs);
		try {
			while (rs.next()) {

				OrderGoodsAll og = getOrderGoodsAllFromRs(rs);
				list.add(og);
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

	public static List<OrderGoodsAll> getlist(User user, int opstatues) {
		List<OrderGoodsAll> list = new ArrayList<OrderGoodsAll>();

		List<Integer> listids = UserService.GetListson(user);
		// logger.info(listids);
		Connection conn = DB.getConn();

		String sql = " select * from mdordergoods,mdordermessage  where mdordergoods.mid = mdordermessage.id and  mdordermessage.opstatues = "
				+ opstatues
				+ " and mdordergoods.opstatues = 0  and mdordermessage.submitid in ("
				+ listids.toString().substring(1,
						listids.toString().length() - 1) + " ) ";

		logger.info(sql);
		Statement stmt = DB.getStatement(conn);

		ResultSet rs = DB.getResultSet(stmt, sql);
		// logger.info(rs);
		try {
			while (rs.next()) {

				OrderGoodsAll og = getOrderGoodsAllFromRs(rs);
				list.add(og);
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

	public static List<OrderGoodsAll> getbillinglist(User user) {
		List<OrderGoodsAll> list = new ArrayList<OrderGoodsAll>();

		List<Integer> listids = UserService.GetListson(user);
		// logger.info(listids);
		Connection conn = DB.getConn();
 
		String sql = " select * from mdordergoods,mdordermessage  where  mdordergoods.mid = mdordermessage.id  and mdordergoods.uuid != 'null' and  mdordergoods.opstatues in (1,2) and mdordermessage.submitid in ("
				+ listids.toString().substring(1,
						listids.toString().length() - 1) + " )";
 
		logger.info(sql);
		Statement stmt = DB.getStatement(conn); 

		ResultSet rs = DB.getResultSet(stmt, sql);
		// logger.info(rs);
		try {
			while (rs.next()) {

				OrderGoodsAll og = getOrderGoodsAllFromRs(rs);
				list.add(og);
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

	public static List<OrderGoodsAll> getlist(User user, int opstatues,
			String[] ids, String[] statues) {
		List<OrderGoodsAll> list = new ArrayList<OrderGoodsAll>();

		List<Integer> listids = UserService.GetListson(user);
		// logger.info(listids);
		Connection conn = DB.getConn();

		String sql = " select * from mdordergoods,mdordermessage  where mdordermessage.submitid in ("
				+ listids.toString().substring(1,
						listids.toString().length() - 1)
				+ " ) and mdordergoods.mid = mdordermessage.id and mdordermessage.opstatues = "
				+ opstatues
				+ " and mdordermessage.id in "
				+ StringUtill.getStr(ids)
				+ " and mdordergoods.statues in"
				+ StringUtill.getStr(statues);
		logger.info(sql);
		Statement stmt = DB.getStatement(conn);

		ResultSet rs = DB.getResultSet(stmt, sql);
		// logger.info(rs);
		try {
			while (rs.next()) {

				OrderGoodsAll og = getOrderGoodsAllFromRs(rs);
				list.add(og);
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

	public static List<OrderGoodsAll> getlist(User user, int opstatues,
			String[] ids, String[] statues, String branchtype) {
		List<OrderGoodsAll> list = new ArrayList<OrderGoodsAll>();

		List<Integer> listids = UserService.GetListson(user);
		// logger.info(branchtype);
		List<Integer> listbids = BranchService.getListids(Integer
				.valueOf(branchtype));
		// logger.info(listbids);
		// logger.info(StringUtill.getStr(ids));
		Connection conn = DB.getConn();

		String sql = " select * from mdordergoods,mdordermessage  where mdordermessage.submitid in ("
				+ listids.toString().substring(1,
						listids.toString().length() - 1)
				+ " ) and mdordergoods.mid = mdordermessage.id and mdordermessage.branchid in ("
				+ listbids.toString().substring(1,
						listbids.toString().length() - 1)
				+ ") and mdordergoods.opstatues = 0  and mdordermessage.id in "
				+ StringUtill.getStr(ids)
				+ " and mdordergoods.statues in"
				+ StringUtill.getStr(statues);
		logger.info(sql);
		Statement stmt = DB.getStatement(conn);

		ResultSet rs = DB.getResultSet(stmt, sql);
		// logger.info(rs);
		try {
			while (rs.next()) {

				OrderGoodsAll og = getOrderGoodsAllFromRs(rs);
				list.add(og);
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

	public static Map<Integer, Map<Integer, OrderGoodsAll>> getsendMap(
			User user, int billingstatues, String[] ids) {
		Map<Integer, Map<Integer, OrderGoodsAll>> map = getsendMap(user,
				billingstatues, StringUtill.getStr(ids));
		return map;

	}

	public static Map<Integer, Map<Integer, OrderGoodsAll>> getsendMap(
			User user, int billingstatues, String ids) {
		Map<Integer, Map<Integer, OrderGoodsAll>> map = new HashMap<Integer, Map<Integer, OrderGoodsAll>>();
		List<OrderGoodsAll> list = getsendlist(user, billingstatues, ids);
		if (null != list) {
			for (int i = 0; i < list.size(); i++) {
				OrderGoodsAll og = list.get(i);
				Map<Integer, OrderGoodsAll> mapb = map.get(og.getOm()
						.getBranchid());
				if (null == mapb) {
					mapb = new HashMap<Integer, OrderGoodsAll>();
					map.put(og.getOm().getBranchid(), mapb);
				}

				OrderGoodsAll ogmap = mapb.get(og.getOm().getId());
				if (null == ogmap) {
					mapb.put(og.getOm().getId(), og);
				} else {
					ogmap.getList().addAll(og.getList());
				}
			}
		}
		return map;

	}

	public static List<OrderGoodsAll> getsendlist(User user,
			int billingstatues, String[] ids) {
		List<OrderGoodsAll> list = getsendlist(user, billingstatues,
				StringUtill.getStr(ids));

		return list;
	}

	public static List<OrderGoodsAll> getsendlist(User user,
			int billingstatues, String ids) {
		List<OrderGoodsAll> list = new ArrayList<OrderGoodsAll>();

		List<Integer> listids = UserService.GetListson(user);
		// logger.info(StringUtill.getStr(ids));
		Connection conn = DB.getConn();

		String sql = " select * from mdordergoods,mdordermessage  where mdordermessage.id in "
				+ ids
				+ " and mdordergoods.mid = mdordermessage.id  and mdordergoods.billingstatues = "
				+ billingstatues
				+ "  and mdordermessage.submitid in ("
				+ listids.toString().substring(1,
						listids.toString().length() - 1)
				+ " )  order by mdordermessage.branchid";
		logger.info(sql);
		Statement stmt = DB.getStatement(conn);

		ResultSet rs = DB.getResultSet(stmt, sql);
		// logger.info(rs);
		try {
			while (rs.next()) {

				OrderGoodsAll og = getOrderGoodsAllFromRs(rs);
				list.add(og);
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

	public static List<String> updateSendcount(User user, OrderGoodsAll oa) {

		List<String> listsql = new ArrayList<String>();
		List<OrderGoods> list = oa.getList();
		if (null != list) {
			for (int i = 0; i < list.size(); i++) {
				OrderGoods og = list.get(i);
				int operatortype = 14;
				int realsendnum = og.getRealnum();
				if (og.getStatues() == 6 || og.getStatues() == 7
						|| og.getStatues() == 8 || og.getStatues() == 9) {
					realsendnum = -realsendnum;
					operatortype = 16;
				}

				String sqlIB = "";
				String sqlIBM = "";

				if (null == InventoryBranchManager.getInventoryID(user, oa
						.getOm().getBranchid(), og.getTid() + "")) {
					sqlIB = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)"
							+ "  values ( null,"
							+ og.getCid()
							+ ", '"
							+ og.getTid()
							+ "', '"
							+ 0
							+ "', '"
							+ realsendnum
							+ "'," + oa.getOm().getBranchid() + ")";

					sqlIBM = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)"
							+ "  values ( null, '"
							+ oa.getOm().getBranchid()
							+ "', '"
							+ oa.getOm().getId()
							+ "','"
							+ oa.getOm().getId()
							+ "','"
							+ TimeUtill.gettime()
							+ "','"
							+ og.getTid()
							+ "',"
							+ 0
							+ ",'"
							+ realsendnum
							+ "',"
							+ operatortype
							+ ","
							+ 0
							+ ","
							+ realsendnum
							+ ","
							+ 1 + "," + oa.getOm().getBranchid() + ",-1,0,0)";
				} else {

					sqlIB = "update mdinventorybranch set  papercount =  ((mdinventorybranch.papercount)*1 + "
							+ realsendnum
							+ ")*1  where  branchid = "
							+ oa.getOm().getBranchid()
							+ " and  type = '"
							+ og.getTid() + "'";

					sqlIBM = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)"
							+ "  values ( null, '"
							+ oa.getOm().getBranchid()
							+ "', '"
							+ oa.getOm().getId()
							+ "','"
							+ oa.getOm().getId()
							+ "','"
							+ TimeUtill.gettime()
							+ "','"
							+ og.getTid()
							+ "','"
							+ 0
							+ "','"
							+ realsendnum
							+ "',"
							+ operatortype
							+ ",(select realcount from mdinventorybranch where branchid = "
							+ oa.getOm().getBranchid()
							+ " and  type = '"
							+ og.getTid()
							+ "')*1,(select papercount from mdinventorybranch where branchid = "
							+ oa.getOm().getBranchid()
							+ " and  type = '"
							+ og.getTid()
							+ "')*1,"
							+ 1
							+ ","
							+ oa.getOm().getBranchid()
							+ ",-1,(select realcount from mdinventorybranch where branchid = "
							+ oa.getOm().getBranchid()
							+ " and  type = '"
							+ og.getTid()
							+ "')*1"
							// + -Integer.valueOf(realsendnum)
							+ ",(select papercount from mdinventorybranch where branchid = "
							+ oa.getOm().getBranchid()
							+ " and  type = '"
							+ og.getTid()
							+ "')*1"
							+ -Integer.valueOf(realsendnum) + ")";

				}
				listsql.add(sqlIB);
				listsql.add(sqlIBM);

			}
		}

		return listsql;

	}

	public static List<String> updateSendcount(User user,
			Map<Integer, Map<Integer, OrderGoodsAll>> map) {
		Set<String> setinit = new HashSet<String>(); 
		List<String> listsql = new ArrayList<String>();
		  		 
		if (null != map) {  
			Set<Map.Entry<Integer, Map<Integer, OrderGoodsAll>>> set = map
					.entrySet();
			Iterator<Map.Entry<Integer, Map<Integer, OrderGoodsAll>>> it = set
					.iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, Map<Integer, OrderGoodsAll>> en = it.next();
				Map<Integer, OrderGoodsAll> mapson = en.getValue();
				Set<Map.Entry<Integer, OrderGoodsAll>> setson = mapson
						.entrySet();
				Iterator<Map.Entry<Integer, OrderGoodsAll>> itson = setson
						.iterator();
				while (itson.hasNext()) {
					Map.Entry<Integer, OrderGoodsAll> enson = itson.next();
					OrderGoodsAll oa = enson.getValue();
					List<OrderGoods> list = oa.getList();
					if (null != list) {  
						for (int i = 0; i < list.size(); i++) {
							OrderGoods og = list.get(i);
							String uuid = og.getTid()+"_"+oa.getOm().getBranchid();
							    
							boolean flag = setinit.contains(uuid);
							if(!flag){       
								//logger.info(uuid);
								setinit.add(uuid);  
							}    
							int operatortype = 14;
							int realsendnum = og.getRealnum();
							if (og.getStatues() == 6 || og.getStatues() == 7
									|| og.getStatues() == 8
									|| og.getStatues() == 9) {
								realsendnum = -realsendnum;
								operatortype = 16;
							} 
 
							String sqlIB = "";
							String sqlIBM = "";
                           if(og.getStatues() != 5){
                        	   if (null == InventoryBranchManager.getInventoryID(
   									user, oa.getOm().getBranchid(), og.getTid()
   											+ "") && !flag ) {
   								sqlIB = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)"
   										+ "  values ( null,"
   										+ og.getCid()
   										+ ", '"
   										+ og.getTid()
   										+ "', '"
   										+ 0
   										+ "', '"
   										+ realsendnum
   										+ "',"
   										+ oa.getOm().getBranchid() + ")";

   								sqlIBM = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)"
   										+ "  values ( null, '"
   										+ oa.getOm().getBranchid()
   										+ "', '"
   										+ oa.getOm().getId()
   										+ "','"
   										+ oa.getOm().getId()
   										+ "','"
   										+ TimeUtill.gettime()
   										+ "','"
   										+ og.getTid()
   										+ "',"
   										+ 0
   										+ ",'"
   										+ realsendnum
   										+ "',"
   										+ operatortype
   										+ ","
   										+ 0
   										+ ","
   										+ realsendnum
   										+ ","
   										+ 1
   										+ ","
   										+ oa.getOm().getBranchid()
   										+ ",-1,0,0)";
   							} else {

   								sqlIB = "update mdinventorybranch set  papercount =  ((mdinventorybranch.papercount)*1 + "
   										+ realsendnum
   										+ ")*1  where  branchid = "
   										+ oa.getOm().getBranchid()
   										+ " and  type = '" + og.getTid() + "'";

   								sqlIBM = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)"
   										+ "  values ( null, '"
   										+ oa.getOm().getBranchid()
   										+ "', '"
   										+ oa.getOm().getId()
   										+ "','"
   										+ oa.getOm().getId()
   										+ "','"
   										+ TimeUtill.gettime()
   										+ "','"
   										+ og.getTid()
   										+ "','"
   										+ 0
   										+ "','"
   										+ realsendnum
   										+ "',"
   										+ operatortype
   										+ ",(select realcount from mdinventorybranch where branchid = "
   										+ oa.getOm().getBranchid()
   										+ " and  type = '"
   										+ og.getTid()
   										+ "')*1,(select papercount from mdinventorybranch where branchid = "
   										+ oa.getOm().getBranchid()
   										+ " and  type = '"
   										+ og.getTid()
   										+ "')*1,"
   										+ 1
   										+ ","
   										+ oa.getOm().getBranchid()
   										+ ",-1,(select realcount from mdinventorybranch where branchid = "
   										+ oa.getOm().getBranchid()
   										+ " and  type = '"
   										+ og.getTid()
   										+ "')*1"
   										// + -Integer.valueOf(realsendnum)
   										+ ",(select papercount from mdinventorybranch where branchid = "
   										+ oa.getOm().getBranchid()
   										+ " and  type = '"
   										+ og.getTid() 
   										+ "')*1" 
   										+ Integer.valueOf(realsendnum) + ")";

   							}
   							listsql.add(sqlIB);
   							listsql.add(sqlIBM);
                           }
							

						}
					} 
				}
			}
		}

		return listsql;

	}

	/*
	 * public static List<String> updateSendcount(User user, Map<Integer,
	 * Map<Integer, OrderGoodsAll>> map) { List<String> listsql = new
	 * ArrayList<String>(); if(null != map){ Set<Map.Entry<Integer, Map<Integer,
	 * OrderGoodsAll>>> set = map.entrySet(); Iterator<Map.Entry<Integer,
	 * Map<Integer, OrderGoodsAll>>> it = set.iterator(); while(it.hasNext()){
	 * Map.Entry<Integer, Map<Integer, OrderGoodsAll>> en = it.next();
	 * Map<Integer, OrderGoodsAll> mapson = en.getValue();
	 * Set<Map.Entry<Integer, OrderGoodsAll>> setson = mapson.entrySet();
	 * Iterator<Map.Entry<Integer, OrderGoodsAll>> itson = setson.iterator();
	 * while(itson.hasNext()){ Map.Entry<Integer, OrderGoodsAll> enson =
	 * itson.next(); OrderGoodsAll oa = enson.getValue(); List<OrderGoods> list
	 * = oa.getList(); if (null != list) { for (int i = 0; i < list.size(); i++)
	 * { OrderGoods og = list.get(i); int operatortype = 14; int realsendnum =
	 * og.getRealnum(); if (og.getStatues() == 6 || og.getStatues() == 7 ||
	 * og.getStatues() == 8 || og.getStatues() == 9) { realsendnum =
	 * -realsendnum; operatortype = 16; }
	 * 
	 * String sqlIB = ""; String sqlIBM = "";
	 * 
	 * if (null == InventoryBranchManager.getInventoryID(user, oa
	 * .getOm().getBranchid(), og.getTid() + "")) { sqlIB =
	 * "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid)"
	 * + "  values ( null," + og.getCid() + ", '" + og.getTid() + "', '" + 0 +
	 * "', '" + realsendnum + "'," + oa.getOm().getBranchid() + ")";
	 * 
	 * sqlIBM =
	 * "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)"
	 * + "  values ( null, '" + oa.getOm().getBranchid() + "', '" +
	 * oa.getOm().getId() + "','" + oa.getOm().getId() + "','" +
	 * TimeUtill.gettime() + "','" + og.getTid() + "'," + 0 + ",'" + realsendnum
	 * + "'," + operatortype + "," + 0 + "," + realsendnum + "," + 1 + "," +
	 * oa.getOm().getBranchid() + ",-1,0,0)"; } else {
	 * 
	 * sqlIB =
	 * "update mdinventorybranch set  papercount =  ((mdinventorybranch.papercount)*1 + "
	 * + realsendnum + ")*1  where  branchid = " + oa.getOm().getBranchid() +
	 * " and  type = '" + og.getTid() + "'";
	 * 
	 * sqlIBM =
	 * "insert into  mdinventorybranchmessage (id,branchid,inventoryid, inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount)"
	 * + "  values ( null, '" + oa.getOm().getBranchid() + "', '" +
	 * oa.getOm().getId() + "','" + oa.getOm().getId() + "','" +
	 * TimeUtill.gettime() + "','" + og.getTid() + "','" + 0 + "','" +
	 * realsendnum + "'," + operatortype +
	 * ",(select realcount from mdinventorybranch where branchid = " +
	 * oa.getOm().getBranchid() + " and  type = '" + og.getTid() +
	 * "')*1,(select papercount from mdinventorybranch where branchid = " +
	 * oa.getOm().getBranchid() + " and  type = '" + og.getTid() + "')*1," + 1 +
	 * "," + oa.getOm().getBranchid() +
	 * ",-1,(select realcount from mdinventorybranch where branchid = " +
	 * oa.getOm().getBranchid() + " and  type = '" + og.getTid() + "')*1" // +
	 * -Integer.valueOf(realsendnum) +
	 * ",(select papercount from mdinventorybranch where branchid = " +
	 * oa.getOm().getBranchid() + " and  type = '" + og.getTid() + "')*1" +
	 * -Integer.valueOf(realsendnum) + ")";
	 * 
	 * } listsql.add(sqlIB); listsql.add(sqlIBM);
	 * 
	 * } } } } }
	 * 
	 * 
	 * 
	 * return listsql;
	 * 
	 * }
	 */

	public static List<OrderGoodsAll> getlist(User user, int opstatues,
			String oid,String ids) {
		List<OrderGoodsAll> list = new ArrayList<OrderGoodsAll>();
 
		List<Integer> listids = UserService.GetListson(user);
		// logger.info(listids);
		Connection conn = DB.getConn();
 
		String sql = " select * from mdordergoods,mdordermessage  where mdordergoods.uuid = '"
				+ oid  
				+ "' and mdordergoods.mid = mdordermessage.id  and mdordermessage.id in "+ ids+"  and mdordergoods.opstatues in (1,2) and mdordermessage.submitid in ("
				+ listids.toString().substring(1,
						listids.toString().length() - 1) + " ) ";
		logger.info(sql);
		Statement stmt = DB.getStatement(conn);

		ResultSet rs = DB.getResultSet(stmt, sql);
		// logger.info(rs);
		try {
			while (rs.next()) {

				OrderGoodsAll og = getOrderGoodsAllFromRs(rs);
				list.add(og);
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

	public static List<OrderGoodsAll> getlist(User user, int opstatues,
			String oid) {
		List<OrderGoodsAll> list = new ArrayList<OrderGoodsAll>();
 
		List<Integer> listids = UserService.GetListson(user);
		// logger.info(listids);
		Connection conn = DB.getConn();
 
		String sql = " select * from mdordergoods,mdordermessage  where mdordergoods.uuid = '"
				+ oid  
				+ "' and mdordergoods.mid = mdordermessage.id    and mdordergoods.opstatues in (1,2) and mdordermessage.submitid in ("
				+ listids.toString().substring(1,
						listids.toString().length() - 1) + " ) ";
		logger.info(sql);
		Statement stmt = DB.getStatement(conn);

		ResultSet rs = DB.getResultSet(stmt, sql);
		// logger.info(rs);
		try {
			while (rs.next()) {

				OrderGoodsAll og = getOrderGoodsAllFromRs(rs);
				list.add(og);
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
	
	public static OrderGoodsAll getOrderGoodsAllFromRs(ResultSet rs) {
		OrderGoodsAll oa = new OrderGoodsAll();
		OrderMessage om = OrderMessageManager.getOrderMessageFromRs(rs);
		OrderGoods og = OrderGoodsManager.getOrderGoodsFromRs(rs);
		oa.setOm(om);
		if (null != og) {
			List<OrderGoods> list = oa.getList();
			if (null == list) {
				list = new ArrayList<OrderGoods>();
			}
			list.add(og);
			oa.setList(list);
		}
		return oa;
	}

}
