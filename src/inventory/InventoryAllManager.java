package inventory;

import httpClient.download.InventoryChange;
import httpClient.download.InventoryModelDownLoad;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import category.Category;
import category.CategoryManager;
import database.DB;

import product.Product;
import product.ProductService;

import branch.Branch;
import branch.BranchManager;
import branch.BranchService;

import user.User;
import utill.NumbleUtill;
import utill.StringUtill;
import utill.TimeUtill;

public class InventoryAllManager {
	protected static Log logger = LogFactory
			.getLog(InventoryBranchManager.class);

	// 苏宁订单刷新
	public static List<InventoryBranch> getlist(User user, String branch,
			String category) {
		List<InventoryBranch> list = null;
		if (!StringUtill.isNull(branch)) {
			if (!NumbleUtill.isNumeric(branch)) {
				Branch b = BranchManager.getLocatebyname(branch);
				branch = b.getId() + "";
			}
		} 
		list = InventoryBranchManager.getCategoryid(user, branch, category);

		return list;
	}

	public static Map<String, Map<String, Map<Integer, InventoryBranch>>> getInventoryMap(
			User user, String category, String branch, String time) {
		Map<String, Map<String, Map<Integer, InventoryBranch>>> map = new HashMap<String, Map<String, Map<Integer, InventoryBranch>>>();

		Collection<httpClient.download.Inventory> listend = InventoryChange
				.get(TimeUtill.dataAdd(time, 1));
		// System.out.println("listend"+listend.size());
		Map<String, httpClient.download.Inventory> mapc = InventoryChange
				.changeMap(listend);
		// 样机
		Map<String, httpClient.download.Inventory> mapm = InventoryModelDownLoad
				.getMap(user, TimeUtill.dataAdd(time, 1));
		// InventoryBranchMessageManager.getMap(sql);
		Map<String, Map<Integer, InventoryBranch>> mapin = new HashMap<String, Map<Integer, InventoryBranch>>();
		InventoryBranch orders = null;
		String str = "";
		if (!StringUtill.isNull(branch)) {
			Branch b = BranchManager.getLocatebyname(branch);
			str = " and branchid = " + b.getId();
		}

		String sql = "";
		sql = "select * from   mdinventorybranchmessage where type in (select id from mdproduct where categoryID =  '"
				+ category + "') " + str +" order by id ";
 
		List<InventoryBranchMessage> list = InventoryBranchMessageManager
				.getMap(sql);
 logger.info(list.size());  
		if (!list.isEmpty()) {
			Iterator<InventoryBranchMessage> it = list.iterator();
			while (it.hasNext()) {
				InventoryBranchMessage inm = it.next();

				Map<Integer, InventoryBranch> mapbt = mapin.get(inm
						.getBranchid() + "_" + inm.getTypeid());
/*logger.info(inm 
		.getBranchid() + "_" + inm.getTypeid());*/
				if (null == mapbt) {
					mapbt = new HashMap<Integer, InventoryBranch>();
					mapin.put(inm.getBranchid() + "_" + inm.getTypeid(), mapbt);
				}
 
				InventoryBranch in = mapbt.get(inm.getTypeStatues());
				if (null == in) {
					in = new InventoryBranch();
					in.setBranchid(inm.getBranchid()); 
					in.setTypeid(inm.getTypeid());
					in.setTypeStatues(inm.getTypeStatues());
					in.setRealcount(inm.getAllotRealcount());
					in.setPapercount(inm.getAllotPapercount());
					if(inm.getOperatortype() == 10){
						in.setQuerymonth(inm.getTime()); 
					}
					mapbt.put(inm.getTypeStatues(), in);
				} else {
					in.setPapercount(in.getPapercount() + inm.getPapercount());
					in.setRealcount(in.getRealcount() + inm.getRealcount());
					if(inm.getOperatortype() == 10){
						in.setQuerymonth(inm.getTime());
					}
				}
			} 
		} 
logger.info("mapin.size()"+mapin.size()); 
		Collection<Map<Integer, InventoryBranch>> co = mapin.values();
		if (!co.isEmpty()) { 
			Iterator<Map<Integer, InventoryBranch>> it = co.iterator();
			while (it.hasNext()) {
				Map<Integer, InventoryBranch> mapinv = it.next();

				Collection<InventoryBranch> coinv = mapinv.values();
				if (!coinv.isEmpty()) {
					Iterator<InventoryBranch> itc = coinv.iterator();
					while (itc.hasNext()) {
						orders = itc.next();
						String pnum = ProductService.getIDmap()
								.get(Integer.valueOf(orders.getTypeid()))
								.getEncoded();
						String bnum = BranchService.getMap()
								.get(orders.getBranchid()).getEncoded();
						String key = bnum + "_" + pnum;
						int cnum = 0;
						int mnum = 0;
						try {
							cnum = mapc.get(key).getNum();
						} catch (Exception e) {
							cnum = 0;
						}
						try {
							mnum = mapm.get(key).getNum();
						} catch (Exception e) {
							mnum = 0;
						} 
						//logger.info(mnum);  
						orders.setSnNum(cnum);
						orders.setSnModelnum(mnum);

						Map<String, Map<Integer, InventoryBranch>> mapt = map
								.get(branch);
						if (null == mapt) {
							mapt = new HashMap<String, Map<Integer, InventoryBranch>>();
							map.put(branch, mapt);
						}

						Map<Integer, InventoryBranch> maptp = mapt.get(orders
								.getTypeid());
						if (null == maptp) {
							maptp = new HashMap<Integer, InventoryBranch>();
							mapt.put(orders.getTypeid(), maptp);
						} 

						maptp.put(orders.getTypeStatues(), orders);
 
						// List<InventoryMessage> listm =
						// InventoryMessageManager.getInventoryID(user,branchid);
						// orders.setInventory(listm);
					}
				} 

			} 
		}
logger.info(map.size());
		return map;
	}

	public static Collection<InventoryAll> getMap(User user, String branch,
			String category, String product, String isSN, String typestatues) {
		// System.out.println("isSN"+isSN);
		List<InventoryBranch> list = null;
		Collection<InventoryAll> clist = null;
		if (StringUtill.isNull(product)) {
			if (!StringUtill.isNull(branch)) {
				if (!NumbleUtill.isNumeric(branch)) {
					Branch b = BranchManager.getLocatebyname(branch);
					branch = b.getId() + "";
				}
			}

			list = InventoryBranchManager.getCategoryid(user, branch, category,
					typestatues);
		} else {
			if (!NumbleUtill.isNumeric(product)) {
				Product p = ProductService.gettypemap().get(product);
				product = p.getId() + "";
			}

			if (!StringUtill.isNull(branch)) {
				// System.out.println(branch);
				if (NumbleUtill.isNumeric(branch)) {
					Branch b = BranchManager.getLocatebyid(branch);
					branch = b.getLocateName();
				}

				list = InventoryBranchManager
						.getCategory(user, branch, product);
			} else {
				list = InventoryBranchManager
						.getCategory(user, branch, product);
			}
		}

		if (StringUtill.isNull(category) && StringUtill.isNull(isSN)) {
			Map<Integer, InventoryAll> map = new HashMap<Integer, InventoryAll>();

			for (int i = 0; i < list.size(); i++) {
				InventoryBranch inb = list.get(i);
				int categoryid = inb.getInventoryid();
				InventoryAll listp = map.get(categoryid);
				if (listp == null) {
					listp = new InventoryAll();
					Category c = CategoryManager.getCategory(categoryid + "");
					listp.setCategoryid(c.getId());
					listp.setCateoryName(c.getName());
					listp.setTypeid(inb.getTypeid());
					listp.setPapercount(inb.getPapercount());
					listp.setRealcount(inb.getRealcount());
					listp.setIsquery(inb.isquery());
					listp.setOrderNUmSN(inb.getOrderNUmSN());
					listp.setActivetime(inb.getActivetime());
					listp.setProduct(inb.getProduct());
					listp.setBranchid(inb.getBranchid());
					// System.out.println(inb.getPapercount()+"***"+inb.getRealcount());
					map.put(categoryid, listp);
				} else {
					// System.out.println(inb.getPapercount()+"***"+inb.getRealcount());
					listp.setPapercount(listp.getPapercount()
							+ inb.getPapercount());
					listp.setRealcount(listp.getRealcount()
							+ inb.getRealcount());
					listp.setIsquery(listp.getIsquery() && inb.isquery());
				}
			}

			clist = map.values();
		} else if (!StringUtill.isNull(category) || !StringUtill.isNull(isSN)) {
			Map<String, InventoryAll> map = new HashMap<String, InventoryAll>();

			for (int i = 0; i < list.size(); i++) {
				InventoryBranch inb = list.get(i);
				int categoryid = inb.getInventoryid();
				String type = inb.getType();
				// int statues = inb.getTypeStatues();
				// String key = type+"_"+statues;
				String key = type;
				if (StringUtill.isNull(isSN)) {
					key = type;
				}
				InventoryAll listp = map.get(key);
				if (listp == null) {
					listp = new InventoryAll();
					Category c = CategoryManager.getCategory(categoryid + "");
					listp.setCategoryid(c.getId());
					listp.setType(type);
					listp.setTypeid(inb.getTypeid());
					listp.setCateoryName(c.getName());
					listp.setPapercount(inb.getPapercount());
					listp.setRealcount(inb.getRealcount());
					listp.setIsquery(inb.isquery());
					listp.setOrderNUmSN(inb.getOrderNUmSN());
					listp.setActivetime(inb.getActivetime());
					listp.setProduct(inb.getProduct());
					listp.setTypestatues(inb.getTypeStatues());
					listp.setBranchid(inb.getBranchid());
					if (!StringUtill.isNull(branch)) {
						listp.setTime(inb.getQuerymonth());
					}
					map.put(key, listp);
				} else {
					listp.setOrderNUmSN(listp.getOrderNUmSN() + "_"
							+ inb.getOrderNUmSN());
					listp.setTypestatuesName(listp.getTypestatuesName() + "_"
							+ inb.getTypestatuesName());
					listp.setPapercount(listp.getPapercount()
							+ inb.getPapercount());
					listp.setRealcount(listp.getRealcount()
							+ inb.getRealcount());
					listp.setIsquery(listp.getIsquery() && inb.isquery());
				}

				// System.out.println(StringUtill.GetJson(listp));
			}
			clist = map.values();
		}

		return clist;
	}

	public static Map<String, List<InventoryAll>> getMapSN(User user,
			String branch, String category, String product, String isSN) {
		Map<String, List<InventoryAll>> map = new HashMap<String, List<InventoryAll>>();

		Collection<InventoryAll> c = InventoryAllManager.getMap(user, branch,
				category, product, isSN, "-1");

		if (!c.isEmpty()) {
			Iterator<InventoryAll> it = c.iterator();
			while (it.hasNext()) {
				InventoryAll ia = it.next();
				List<InventoryAll> list = map.get(ia.getOrderNUmSN());
				if (null == list) {
					list = new ArrayList<InventoryAll>();
					map.put(ia.getOrderNUmSN(), list);
				}
				if (ia.getPapercount() != 0) {
					list.add(ia);
				}

			}
		}

		return map;

	}

}
