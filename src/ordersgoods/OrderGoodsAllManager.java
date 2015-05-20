package ordersgoods;

import goodsreceipt.OrderReceipt;
import inventory.InventoryBranch;
import inventory.InventoryBranchManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import category.CategoryService;

import branch.BranchService;

import database.DB;

import user.User;
import user.UserService;
import utill.ArrayListUtil;
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

	public static List<String> save(User user, Map<String, OrderReceipt> map,
			String[] ids, String buyid) {
		List<String> listsql = new ArrayList<String>();
		List<String> listor = Arrays.asList(ids);
		// logger.info(StringUtill.GetJson(listor));
		Map<Integer, List<OrderReceipt>> maps = new HashMap<Integer, List<OrderReceipt>>();
		if (null != map) {
			Set<Map.Entry<String, OrderReceipt>> set = map.entrySet();
			Iterator<Map.Entry<String, OrderReceipt>> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry<String, OrderReceipt> mapent = it.next();
				OrderReceipt gr = mapent.getValue();
				if (listor.contains(gr.getId() + "")) {
					// logger.info(gr.getId());
					List<OrderReceipt> list = maps.get(gr.getBid());
					if (null == list) {
						list = new ArrayList<OrderReceipt>();
						maps.put(gr.getBid(), list);
					}
					list.add(gr);
				}

			}
		}

		int maxid = OrderMessageManager.getMaxid();

		if (null != maps) {
			Set<Map.Entry<Integer, List<OrderReceipt>>> set = maps.entrySet();
			Iterator<Map.Entry<Integer, List<OrderReceipt>>> it = set
					.iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, List<OrderReceipt>> mapent = it.next();
				int bid = mapent.getKey();
				List<OrderReceipt> grs = mapent.getValue();

				String sql = " insert into mdordermessage(id,oid,submitid,submittime,opstatues,branchid,remark) values ("
						+ maxid
						+ ",'',"
						+ user.getId()
						+ ",'"
						+ TimeUtill.getdateString()
						+ "',"
						+ 1
						+ ",'"
						+ bid
						+ "','" + buyid + "')";
				listsql.add(sql);

				for (int i = 0; i < grs.size(); i++) {
					OrderReceipt or = grs.get(i);
					String str = "update orderreceipt set disable = 1 where id = "
							+ or.getId();
					listsql.add(str);
					String sqlN = "insert into mdordergoods (id,oid,submitid,submittime,cid,tid,statues,ordernum,realnum,opstatues,uuid,mid,billingstatues,serialnumber) "
							+ "values (null,'','"
							+ user.getId()
							+ "','"
							+ TimeUtill.getdateString()
							+ "','"
							+ or.getCid()
							+ "','"
							+ or.getTid()
							+ "','0','"
							+ or.getRecevenum()
							+ "','"
							+ or.getRecevenum()
							+ "','" + 1 + "',''," + maxid + "," + 1 + ",'') ;";

					listsql.add(sqlN);

				}
				maxid++;
			}
		}

		return listsql;
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
		Map<String, OrderGoodsAll> map = new LinkedHashMap<String, OrderGoodsAll>();
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
		Map<String, OrderGoodsAll> map = new LinkedHashMap<String, OrderGoodsAll>();
		List<OrderGoodsAll> list = OrderGoodsAllManager.getsendlist(user,
				statues);
		for (int i = 0; i < list.size(); i++) {
			OrderGoodsAll oal = list.get(i);
			// logger.info(oal.getOm().getSubmittime());
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
			User user, int statues, String branchtype) {
		Map<String, Map<String, List<OrderGoods>>> map = new LinkedHashMap<String, Map<String, List<OrderGoods>>>();
		List<OrderGoodsAll> list = OrderGoodsAllManager.getbillinglist(user,
				branchtype);
		for (int i = 0; i < list.size(); i++) {
			OrderGoodsAll oal = list.get(i);
			List<OrderGoods> listog = oal.getList();
			if (null != listog) {
				for (int j = 0; j < listog.size(); j++) {
					OrderGoods og = listog.get(j);
					Map<String, List<OrderGoods>> maps = map.get(og
							.getExportuuid());
					if (null == maps) {
						maps = new LinkedHashMap<String, List<OrderGoods>>();
						map.put(og.getExportuuid(), maps);
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

	public static OrderGoodsAll getOrderGoodsAllexamByid(User user, String id) {
		// logger.info(listids);
		Connection conn = DB.getConn();
		OrderGoodsAll og = null;
		String sql = " select * from mdordergoods,mdordermessage  where mdordermessage.id = "
				+ id
				+ " and mdordergoods.mid = mdordermessage.id and mdordergoods.billingstatues = 0";

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
		String opstatuess = "";
		List<OrderGoodsAll> list = new ArrayList<OrderGoodsAll>();

		List<Integer> listids = UserService.GetListson(user);
		if(OrderMessage.all == opstatues){
			opstatuess = "(2,3)";
		}else { 
			opstatuess = "("+opstatues+")";
		}
		// logger.info(listids);
		Connection conn = DB.getConn();

		String sql = " select * from mdordergoods,mdordermessage  where mdordergoods.mid = mdordermessage.id and mdordergoods.billingstatues in "
				+ opstatuess
				+ " and mdordermessage.opstatues = 1 and mdordermessage.submitid in ("
				+ listids.toString().substring(1,
						listids.toString().length() - 1)
				+ " ) order by mdordermessage.submittime desc";

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
						listids.toString().length() - 1)
				+ " ) order by mdordermessage.id desc";

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

	public static List<OrderGoodsAll> getbillinglist(User user,
			String branchtype) {
		List<OrderGoodsAll> list = new ArrayList<OrderGoodsAll>();

		List<Integer> listids = UserService.GetListson(user);
		// logger.info(listids);
		String sqlbranch = "";
		if (!StringUtill.isNull(branchtype)) {
			List<Integer> listbids = BranchService.getListids(Integer
					.valueOf(branchtype));
			sqlbranch = " and mdordermessage.branchid in ("
					+ listbids.toString().substring(1,
							listbids.toString().length() - 1) + ")";
		}
		Connection conn = DB.getConn();

		String sql = " select * from mdordergoods,mdordermessage  where  mdordergoods.mid = mdordermessage.id  "
				+ sqlbranch
				+ " and mdordergoods.uuid != 'null' and  mdordergoods.opstatues in (1,2) and mdordermessage.submitid in ("
				+ listids.toString().substring(1,
						listids.toString().length() - 1)
				+ " ) order by mdordermessage.id desc ";

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
			String[] ids, String[] statues, String exportmodel) {

		List<OrderGoodsAll> list = getlist(user, opstatues,
				StringUtill.getStr(ids), StringUtill.getStr(statues),
				exportmodel);

		return list;
	}

	public static List<OrderGoodsAll> getlist(User user, int opstatues,
			String ids, String statues, String exportmodel) {
		List<OrderGoodsAll> list = new ArrayList<OrderGoodsAll>();

		List<Integer> listbids = CategoryService.getByExportModel(Integer
				.valueOf(exportmodel));
		// logger.info(listbids);
		// logger.info(StringUtill.getStr(ids));
		Connection conn = DB.getConn();

		String sql = " select * from mdordergoods,mdordermessage  where mdordergoods.mid = mdordermessage.id and mdordergoods.cid in ("
				+ listbids.toString().substring(1,
						listbids.toString().length() - 1)
				+ ") and mdordergoods.opstatues = "
				+ opstatues
				+ "  and mdordermessage.id in "
				+ ids
				+ " and mdordergoods.statues in" + statues;
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

		Map<Integer, Map<String, Map<Integer, InventoryBranch>>> mapin = InventoryBranchManager
				.getInventoryMap(user);

		logger.info(mapin);

		Set<String> setinit = new HashSet<String>();
		List<String> listsql = new ArrayList<String>();
		Set<String> setup = new HashSet<String>();
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
							String uuid = og.getTid() + "_"
									+ oa.getOm().getBranchid();

							boolean flag = setinit.contains(uuid);
							if (!flag) {
								// logger.info(uuid);
								setinit.add(uuid);
							}
							int operatortype = 14;
							int realsendnum = og.getRealnum();

							logger.info(realsendnum);

							boolean flags = true;
							if (og.getStatues() == 6 || og.getStatues() == 7
									|| og.getStatues() == 8
									|| og.getStatues() == 9
									|| og.getStatues() == 5
									|| og.getStatues() == 4) {
								// realsendnum = -realsendnum;
								// operatortype = 16;
								flag = false;
							} else if (og.getStatues() == 4) {
								operatortype = 17;
							} else {
								flag = true;
							}

							String sqlIB = "";
							String sqlIBM = "";
							InventoryBranch in = null;
							if (flag) { 
								try {
									in = mapin.get(oa.getOm().getBranchid())
											.get(og.getTid() + "")
											.get(og.getrealStatues());
								} catch (Exception e) {
									in = null;
								}
								// logger.info( og.getTid());
								// logger.info(setup.contains(oa.getOm().getBranchid()+"_"+og.getTid()));
  
								if (null == in 
										&& !setup.contains(oa.getOm()
												.getBranchid()
												+ "_" 
												+ og.getTid()+"_"+og.getrealStatues())) {
  
									setup.add(oa.getOm().getBranchid() + "_"
											+ og.getTid()+"_"+og.getrealStatues());
									sqlIB = "insert into  mdinventorybranch (id,inventoryid,type,realcount,papercount, branchid,typestatues)"
											+ "  values ( null,"
											+ og.getCid()
											+ ", '" 
											+ og.getTid()
											+ "', '"
											+ 0
											+ "', '"
											+ realsendnum
											+ "',"
											+ oa.getOm().getBranchid()
											+ ","
											+ og.getrealStatues() + ")";

									sqlIBM = "insert into  mdinventorybranchmessage (id,branchid,inventoryid,inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount,isoverstatues,typestatues)"
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
											+ ",-1,0,0,1,"
											+ og.getrealStatues()
											+ ")";
								} else {

									sqlIB = "update mdinventorybranch set  papercount =  ((mdinventorybranch.papercount)*1 + "
											+ realsendnum
											+ ")*1  where  branchid = "
											+ oa.getOm().getBranchid()
											+ " and  type = '"
											+ og.getTid()
											+ "' and typestatues = "
											+ og.getrealStatues();

									sqlIBM = "insert into  mdinventorybranchmessage (id,branchid,inventoryid, inventoryString ,time,type,allotRealcount,allotPapercount,operatortype,realcount,papercount,sendUser,receiveuser,devidety,oldrealcount,oldpapercount,isoverstatues,typestatues)"
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
											+ "' and typestatues = "+og.getrealStatues()+")*1,(select papercount from mdinventorybranch where branchid = "
											+ oa.getOm().getBranchid()
											+ " and  type = '"
											+ og.getTid()
											+ "' and typestatues = "+og.getrealStatues()+")*1,"
											+ 1    
											+ ","   
											+ oa.getOm().getBranchid()
											+ ",-1,(select realcount from mdinventorybranch where branchid = "
											+ oa.getOm().getBranchid()
											+ " and  type = '"
											+ og.getTid()  
											+ "' and typestatues = "+og.getrealStatues()+")*1"
											// + -Integer.valueOf(realsendnum) 
											+ ",(select papercount from mdinventorybranch where branchid = "
											+ oa.getOm().getBranchid()
											+ " and  type = '"
											+ og.getTid()  
											+ "' and typestatues = "+og.getrealStatues()+")*1"
											+ Integer.valueOf(realsendnum) 
											+ ",1," + og.getrealStatues() + ")";

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
			String oid, String ids) {
		List<OrderGoodsAll> list = new ArrayList<OrderGoodsAll>();

		List<Integer> listids = UserService.GetListson(user);
		// logger.info(listids);
		Connection conn = DB.getConn();

		String sql = " select * from mdordergoods,mdordermessage  where mdordergoods.exportuuid = '"
				+ oid
				+ "' and mdordergoods.mid = mdordermessage.id  and mdordermessage.id in "
				+ ids
				+ "  and mdordergoods.opstatues in (1,2) and mdordermessage.submitid in ("
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

		String sql = " select * from mdordergoods,mdordermessage  where mdordergoods.exportuuid = '"
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

	public static List<OrderGoodsAll> getlistName(User user, int opstatues,
			String exportName, String name) {
		List<OrderGoodsAll> list = new ArrayList<OrderGoodsAll>();

		List<Integer> listids = UserService.GetListson(user);
		// logger.info(listids);
		Connection conn = DB.getConn();
		String sql = "";
		if (!StringUtill.isNull(exportName)) {
			sql = " select * from mdordergoods,mdordermessage  where mdordergoods.exportuuid = '"
					+ exportName
					+ "' and mdordergoods.mid = mdordermessage.id    and mdordergoods.opstatues in (1,2) and mdordermessage.submitid in ("
					+ listids.toString().substring(1,
							listids.toString().length() - 1) + " ) ";
		} else {
			sql = " select * from mdordergoods,mdordermessage  where mdordergoods.uuid = '"
					+ name
					+ "' and mdordergoods.mid = mdordermessage.id    and mdordergoods.opstatues in (1,2) and mdordermessage.submitid in ("
					+ listids.toString().substring(1,
							listids.toString().length() - 1) + " ) ";
		}

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
 
	public static List<OrderGoodsAll> getlistName(User user,
			String exportName, String oid) {
		List<OrderGoodsAll> list = new ArrayList<OrderGoodsAll>();

		List<Integer> listids = UserService.GetListson(user);
		// logger.info(listids); 
		Connection conn = DB.getConn();
		String sql = "";
		if (!StringUtill.isNull(oid)) {
			sql = " select * from mdordergoods,mdordermessage  where mdordergoods.exportuuid = '"
					+ exportName 
					+ "' and mdordergoods.mid = mdordermessage.id  and mdordergoods.oid = '"+oid+"'  and mdordergoods.opstatues in (1,2) and mdordermessage.submitid in ("
					+ listids.toString().substring(1, 
							listids.toString().length() - 1) + " ) "; 
		}else {
			sql = " select * from mdordergoods,mdordermessage  where mdordergoods.exportuuid = '"
					+ exportName
					+ "' and mdordergoods.mid = mdordermessage.id  and mdordergoods.oid is null  and mdordergoods.opstatues in (1,2) and mdordermessage.submitid in ("
					+ listids.toString().substring(1, 
							listids.toString().length() - 1) + " ) ";
		} 

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
