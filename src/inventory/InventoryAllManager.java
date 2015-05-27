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
import java.util.Set;

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
  
	public static Map<Integer, Map<String, Map<Integer, InventoryBranch>>> getInventoryMap(
			User user, String category, String branch, String time,Map<String, httpClient.download.Inventory> mapc,Map<String, httpClient.download.Inventory> mapm,Map<String, httpClient.download.Inventory> mapbad) { 
		 // 门店 。型号  状态
		Map<Integer, Map<String, Map<Integer, InventoryBranch>>> map = new HashMap<Integer, Map<String, Map<Integer, InventoryBranch>>>();
        
		// 实货库存           
		
		// InventoryBranchMessageManager.getMap(sql);
		// logger.info(mapm.size());
		// logger.info("mapm"+StringUtill.GetJson(mapm));
  
		// 系统库存
		Map<String, Map<Integer, InventoryBranch>> mapin = new HashMap<String, Map<Integer, InventoryBranch>>();
         
		String str = "";
		if (!StringUtill.isNull(branch)) {  
			Branch b = BranchManager.getLocatebyname(branch);
			str = " and branchid = " + b.getId(); 
		} 
  
		String sql = ""; 
		sql = "select * from   mdinventorybranchmessage where type in (select id from mdproduct where categoryID =  '"
				+ category + "') " + str + " and time < '"+TimeUtill.dataAdd(time, 1)+"' order by id ";
		      
		logger.info(sql);  
		List<InventoryBranchMessage> list = InventoryBranchMessageManager
				.getMap(sql);
		 
		logger.info(list.size());
		
		if (!list.isEmpty()) { 
			Iterator<InventoryBranchMessage> it = list.iterator();
			while (it.hasNext()) {
				InventoryBranchMessage inm = it.next();
				String key = inm.getBranchid() + "_" + inm.getTypeid();
				// logger.info(key);
				Map<Integer, InventoryBranch> mapbt = mapin.get(key);
				/*
				 * logger.info(inm .getBranchid() + "_" + inm.getTypeid());
				 */
				if (null == mapbt) {
					mapbt = new HashMap<Integer, InventoryBranch>();
					mapin.put(key, mapbt);
				}

				InventoryBranch in = mapbt.get(inm.getTypeStatues());
				if (null == in) {
					in = new InventoryBranch(); 
					in.setBranchid(inm.getBranchid());
					in.setTypeid(inm.getTypeid()); 
					in.setTypeStatues(inm.getTypeStatues());
					in.setRealcount(inm.getAllotRealcount());
					in.setPapercount(inm.getAllotPapercount());
					if (inm.getOperatortype() == 10) {
						in.setQuerymonth(inm.getTime());
					}
					mapbt.put(inm.getTypeStatues(), in);
				} else {

					/*
					 * if(inm.getTypeid().equals("3376")){
					 * logger.info(in.getPapercount());
					 * logger.info(inm.getAllotPapercount());
					 * logger.info(in.getRealcount()); logger.info(key); }
					 */ 
					in.setPapercount(in.getPapercount()
							+ inm.getAllotPapercount());
					in.setRealcount(in.getRealcount() + inm.getAllotRealcount());
					if (inm.getOperatortype() == 10) {
						in.setQuerymonth(inm.getTime());
					}
				}
			}
		}

		// logger.info("mapin.size()"+mapin);
		// logger.info("mapin"+mapin.values().size());

		int count = 0;

		if (!mapin.isEmpty()) {

			Set<Map.Entry<String, Map<Integer, InventoryBranch>>> set = mapin
					.entrySet();
			Iterator<Map.Entry<String, Map<Integer, InventoryBranch>>> it = set
					.iterator();

			while (it.hasNext()) {
				Map.Entry<String, Map<Integer, InventoryBranch>> mapent = it
						.next();
				String keys = mapent.getKey();
				String[] strs = keys.split("_");
				String branchid = strs[0];
				String type = strs[1];

				String pnum = ProductService.getIDmap()
						.get(Integer.valueOf(type)).getEncoded();
				String bnum = BranchService.getMap()
						.get(Integer.valueOf(branchid)).getEncoded();
				 
				String key = bnum + "_" + pnum;
				// logger.info("key"+key);
				int cnum = 0;  
				int mnum = 0; 
				int badnum ;
				try {  
					cnum = mapc.get(key).getNum();
					count++;
					mapc.remove(key);
				} catch (Exception e) {
					cnum = 0;
				}
				try {
					mnum = mapm.get(key).getNum();
					// logger.info(key+"***"+mnum);
					//count++;  
					mapm.remove(key);
				} catch (Exception e) {
					// logger.info("none"+key);
					mnum = 0;
				}  
 
				try { 
					badnum = mapbad.get(key).getNum();
					// logger.info(key+"***"+mnum);
					//count++;  
					mapbad.remove(key);
				} catch (Exception e) {
					// logger.info("none"+key);
					badnum = 0;
				}  

				
				Map<Integer, InventoryBranch> mapinv = mapent.getValue();

				Collection<InventoryBranch> coinv = mapinv.values();
				if (!coinv.isEmpty()) {
					Iterator<InventoryBranch> itc = coinv.iterator();
					while (itc.hasNext()) {
						InventoryBranch orders = itc.next();
						// count ++;
						// logger.info(mnum);
						orders.setSnNum(cnum);

						orders.setSnModelnum(mnum);
						
						orders.setSnBad(badnum); 
						// logger.info(branch);
						Map<String, Map<Integer, InventoryBranch>> mapt = map
								.get(orders.getBranchid());
						if (null == mapt) {
							// logger.info("branch"+branch);
							mapt = new HashMap<String, Map<Integer, InventoryBranch>>();
							map.put(orders.getBranchid(), mapt);
						}

						Map<Integer, InventoryBranch> maptp = mapt.get(orders
								.getTypeid());
						if (null == maptp) {
							maptp = new HashMap<Integer, InventoryBranch>();
							mapt.put(orders.getTypeid(), maptp);
						}

						InventoryBranch or = maptp.get(orders.getTypeStatues());

						if (null == or) {
							/*if (orders.getSnModelnum() == 1) {
								count++;
							}*/ 
							maptp.put(orders.getTypeStatues(), orders);
						} else {
							/*if (orders.getSnModelnum() == 1) {
								// logger.info(or.getSnModelnum());
								count++;
							}*/ 
							or.setRealcount(or.getRealcount()
									+ orders.getRealcount());
							or.setPapercount(or.getPapercount()
									+ orders.getPapercount());
							or.setSnNum(or.getSnNum() + orders.getSnNum());
							or.setSnModelnum(or.getSnModelnum()
									+ orders.getSnModelnum());
							or.setSnBad(or.getSnBad()+orders.getSnBad());
						}
 
						// List<InventoryMessage> listm =
						// InventoryMessageManager.getInventoryID(user,branchid);
						// orders.setInventory(listm);
					} 
				}

			} 
		}
		logger.info("count" + count);
		// logger.info(map.values().size());
		logger.info(map.size()); 
		logger.info("mapc.size()"+mapc.size());
		logger.info("mapm.size()"+mapm.size()); 
             
		/*int countb = 0;  
		Set<Map.Entry<String, httpClient.download.Inventory>> setm = mapm
				.entrySet();
		Iterator<Map.Entry<String, httpClient.download.Inventory>> itm = setm
				.iterator();
		while (itm.hasNext()) { 
			Map.Entry<String, httpClient.download.Inventory> mapentc = itm
					.next();
			
			String key = mapentc.getKey(); 
			//countb++;
			httpClient.download.Inventory in = mapentc.getValue();
			int tid = -1;
			int bid = -1;
			String tname = "";
			String bname = "";
			
			try { 
				tid = ProductService.gettypeNUmmap().get(in.getGoodNum())
						.getId();
			} catch (Exception e) {
				tname = in.getGoodpName();
				//logger.info(in.getGoodNum());
			}
 
			// logger.info(in.getBranchName());
			 
			try {
				bname = BranchService.getNumMap().get(in.getBranchNum())
						.getLocateName();
				bid = BranchService.getNumMap().get(in.getBranchNum()).getId();
			} catch (Exception e) {
				logger.info(in.getBranchNum());
				bname = "";
			}

			int cnum = 0;
			try { 
				cnum = mapc.get(key).getNum();
				mapc.remove(key);
			} catch (Exception e) {
				cnum = 0;
			}
			
			
			if (StringUtill.isNull(branch) || branch.equals(bname)) {
				// logger.info(bname);
				// logger.info(in.getBranchNum());
				//countb++;
				Map<String, Map<Integer, InventoryBranch>> mapt = map
						.get(branch);
				if (null == mapt) {
					// logger.info("branch"+branch);
					mapt = new HashMap<String, Map<Integer, InventoryBranch>>();
					map.put(branch, mapt);
				}
				
				String tkey = tid + "";
				if (tid == -1) { 
					tkey = tname;
				}
				Map<Integer, InventoryBranch> maptp = mapt.get(tkey);
				if (null == maptp) {
					maptp = new HashMap<Integer, InventoryBranch>();
					mapt.put(tkey, maptp);
				}

				InventoryBranch or = maptp.get(3);

				if (null == or) {
					countb++;
					//logger.info(in.getNum());
					InventoryBranch orders = new InventoryBranch();
					if (tid == -1) {
						orders.setGoodname(tname);
						orders.setGoodnum(in.getGoodNum());
					}
					orders.setTypeid(tid + "");
					orders.setBranchid(bid);
					orders.setTypeStatues(3);
					orders.setSnModelnum(in.getNum());
					orders.setSnNum(cnum);
					maptp.put(3, orders);  
				} else {   
					//logger.info(or.getSnModelnum() + in.getNum());
					countb++;
					or.setSnModelnum(or.getSnModelnum() + in.getNum());
                    or.setSnNum(or.getSnNum()+cnum); 
				}
			}
		}  */
	//	logger.info("countb"+countb);
		/*logger.info(map.size());
		logger.info(mapc.size());
		logger.info(mapm.size());*/
 
	/*	Set<Map.Entry<String, httpClient.download.Inventory>> setc = mapc
				.entrySet();
		Iterator<Map.Entry<String, httpClient.download.Inventory>> itc = setc
				.iterator();
		while (itc.hasNext()) {
			Map.Entry<String, httpClient.download.Inventory> mapentc = itc
					.next();
			String key = mapentc.getKey();
			httpClient.download.Inventory in = mapentc.getValue();
			//itc.remove();
			int bid = -1;
			int tid = -1;
			String tname = "";
			try {  
				tid = ProductService.gettypeNUmmap().get(in.getGoodNum())
						.getId();
			} catch (Exception e) {
				tname = in.getGoodpName();
			//	logger.info(in.getGoodNum());
			}

			String bname = "";
			try {
				bname = BranchService.getNumMap()
						.get(StringUtill.getStringNocn(in.getBranchName()))
						.getLocateName();
				bid = BranchService.getNumMap().get(in.getBranchNum()).getId();
			} catch (Exception e) {
				bname = "";
			}

			if (StringUtill.isNull(branch) || branch.equals(bname)) {
				// logger.info(tid);
				countb++;

				Map<String, Map<Integer, InventoryBranch>> mapt = map
						.get(branch);
				if (null == mapt) {
					// logger.info("branch"+branch);
					mapt = new HashMap<String, Map<Integer, InventoryBranch>>();
					map.put(branch, mapt);
				}  

				String tkey = tid + "";
				if (tid == -1) { 
					tkey = tname;
				} 
				
				Map<Integer, InventoryBranch> maptp = mapt.get(tkey);
				if (null == maptp) {   
					//logger.info("branch"+branch);
					maptp = new HashMap<Integer, InventoryBranch>();
					mapt.put(tkey, maptp); 
				}  
				 
				InventoryBranch or = maptp.get(0);
               // logger.info(or); 
				if (null == or) {
					InventoryBranch orders = new InventoryBranch();
					
					if (tid == -1) {
						orders.setGoodname(tname);
						orders.setGoodnum(in.getGoodNum());
					} 
				  
					orders.setSnNum(in.getNum()); 
					orders.setTypeid(tid + "");
					orders.setBranchid(bid); 
					orders.setTypeStatues(0);  

					maptp.put(0, orders);
				} else {  
					 or.setSnNum(or.getSnNum() +in.getNum());
					
				} 
			}
		}  

		logger.info(map.size()); 
		logger.info(mapc.size());
		logger.info(mapm.size());
*/
		// logger.info(map.size());
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
