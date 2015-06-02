package httpClient.download;

import inventory.InventoryBranch;
import inventory.InventoryBranchManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ordersgoods.OrderGoods;
import ordersgoods.OrderGoodsManager;

import comparator.Inventoryomparator;

import product.ProductService;

import enums.SaleModel;

import branch.BranchService;

import user.User;
import utill.StringUtill;
import utill.TimeUtill;

public class IntelligentInventory {

	public static List<OrderGoods> getmessage(User user, String starttime, String endtime,
			String branch) {
		List<OrderGoods> listr = new ArrayList<OrderGoods>(); 
		Map<String, List<Inventory>> map = new LinkedHashMap<String, List<Inventory>>();

		Collection<Inventory> listend = InventoryChange.get(TimeUtill.dataAdd(
				endtime, 1));
		// System.out.println("listend"+listend.size());
		// Map<String, Inventory> mapstart = InventoryChange.changeMap(listend
		// ); 
		// 样机  
		Map<String, Inventory> mapModel = InventoryModelDownLoad.getMap(user,
				TimeUtill.dataAdd(endtime, 1));

		// 销量
		Map<String, Inventory> mapsale = SaleDownLoad
				.getMap(starttime, endtime);

		// 未入库数量
		Map<Integer, Map<String, InventoryBranch>> mapout = InventoryBranchManager
				.getmapType(user);
        
		// 
		Map<String,OrderGoods> mapog = OrderGoodsManager.getmapphone(user);
		// System.out.println("mapout"+mapout);
		// Collection<Inventory> sales = SaleDownLoad.get(starttime, endtime);

		if (!listend.isEmpty()) {
			Iterator<Inventory> it = listend.iterator();
			while (it.hasNext()) {
				Inventory inve = it.next();
				String bnum = "";
				if ("常规机库".equals(inve.getBranchName())) {
					bnum = "1";
				} else if ("特价机库".equals(inve.getBranchName())) {
					bnum = "2";
				} else {
					bnum = StringUtill.getStringNocn(inve.getBranchName());
				}

				String key = bnum + "_" + inve.getGoodNum();

				int bid = 0;
				String tname = "";

				try {
					bid = BranchService.getNumMap(SaleModel.SuNing).get(bnum)
							.getId();
				} catch (Exception e) {
					// e.printStackTrace();
					bid = 0;
				}
				try {
					tname = ProductService.gettypeNUmmap()
							.get(inve.getGoodNum()).getType();
				} catch (Exception e) {
					tname = "";
					// e.printStackTrace();
				}
				// logger.info(key);
 
				Inventory sale = mapsale.get(key);
				Inventory model = mapModel.get(key);
				InventoryBranch inout = null;
				OrderGoods og = mapog.get(key);
				try {
					inout = mapout.get(bid).get(tname);
				} catch (Exception e) {
					// e.printStackTrace();
					inout = null;
				}

				// System.out.println("sale"+sale);
				if (null != sale) {
					// System.out.println("sale"+sale);
					inve.setSaleNum(sale.getSaleNum());
					mapsale.remove(key);
				}

				if (null != model) {
					inve.setModelnum(model.getNum());
					mapModel.remove(key);
				}

				if (null != inout) {
					inve.setOutnum(inout.getPapercount());
				}

				if(null != og){
					if(og.getStatues() == 1 || og.getStatues() == 2)
					inve.setOutnum(inve.getOutnum()+og.getOrdernum()); 
				} 
				// System.out.println("key"+key);

				String keyin = "";

				if (!StringUtill.isNull(branch)) {
					String branchnum = BranchService.getNameMap().get(branch)
							.getEncoded();
					keyin = branchnum;
					if (branchnum.equals(bnum)) {
						List<Inventory> li = map.get(keyin);
						if (null == li) {
							li = new ArrayList<Inventory>();
							map.put(keyin, li);
						}

						li.add(inve);
					}

				} else {
					keyin = bnum;

					List<Inventory> li = map.get(keyin);
					if (null == li) {
						li = new ArrayList<Inventory>();
						map.put(keyin, li);
					}

					li.add(inve);

				}

			}
		}
      
		if (!mapsale.isEmpty()) {
			Set<Map.Entry<String, Inventory>> set = mapsale.entrySet();
			Iterator<Map.Entry<String, Inventory>> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry<String, Inventory> mapent = it.next();
				Inventory inve = mapent.getValue();
				String keyin = "";
				String bnum = "";
				if ("常规机库".equals(inve.getBranchName())) {
					bnum = "1";
				} else if ("特价机库".equals(inve.getBranchName())) {
					bnum = "2";
				} else {
					bnum = StringUtill.getStringNocn(inve.getBranchName());
				}

				String key = bnum + "_" + inve.getGoodNum();

				int bid = 0;
				String tname = "";

				try {
					bid = BranchService.getNumMap(SaleModel.SuNing).get(bnum)
							.getId();
				} catch (Exception e) {
					// e.printStackTrace();
					bid = 0;
				}
				try {
					tname = ProductService.gettypeNUmmap()
							.get(inve.getGoodNum()).getType();
				} catch (Exception e) {
					tname = "";
					// e.printStackTrace();
				}
				// logger.info(key);
				Inventory model = mapModel.get(key);
				InventoryBranch inout = null;
				OrderGoods og = mapog.get(key);
				try {
					inout = mapout.get(bid).get(tname);
				} catch (Exception e) {
					// e.printStackTrace();
					inout = null;
				}

				// System.out.println("sale"+sale);

				if (null != inout) {
					inve.setOutnum(inout.getPapercount());
				}

				if(null != og){
					if(og.getStatues() == 1 || og.getStatues() == 2)
					inve.setOutnum(inve.getOutnum()+og.getOrdernum()); 
				} 
				  
				if (!StringUtill.isNull(branch)) {
					String branchnum = BranchService.getNameMap().get(branch)
							.getEncoded();
					keyin = branchnum;
					if (branchnum.equals(bnum)) {
						List<Inventory> li = map.get(keyin);
						if (null == li) {
							li = new ArrayList<Inventory>();
							map.put(keyin, li);
						}

						li.add(inve);
					}
				} else {
					keyin = bnum;

					List<Inventory> li = map.get(keyin);
					if (null == li) {
						li = new ArrayList<Inventory>();
						map.put(keyin, li);
					}
					li.add(inve);

				}
			}
		}
 
		int count = 0;
		List<Integer> list = new ArrayList<Integer>();

		if (!map.isEmpty()) {
			Set<Map.Entry<String, List<Inventory>>> set = map.entrySet();
			Iterator<Map.Entry<String, List<Inventory>>> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry<String, List<Inventory>> mapen = it.next();
				List<Inventory> li = mapen.getValue();
				Inventoryomparator c = new Inventoryomparator();
				Collections.sort(li, c);
				if (null != li) {
					for (int i = 0; i < li.size(); i++) {
						Inventory in = li.get(i);

						String bnum = StringUtill.getStringNocn(in
								.getBranchName());
						String bname = "";
						if (StringUtill.isNull(bnum)) {
							bnum = in.getBranchNum();
						}

						if (null != BranchService.getNumMap(SaleModel.SuNing)) {
							if (null != BranchService.getNumMap(
									SaleModel.SuNing).get(bnum)) {
								bname = BranchService
										.getNumMap(SaleModel.SuNing).get(bnum)
										.getLocateName();
							}
						}
						int realcont = in.getSaleNum()
								- (in.getNum() + in.getOutnum());
						if (branch.equals(bname) && realcont > 0) {
							String cl = "class=\"asc\"";
							int pid = 0;
							count++;
 
							try {
								pid = ProductService.gettypemap(user, bname)
										.get(in.getGoodpName()).getId();

								list.add(count);
 
							} catch (Exception e) {
								pid =0;
								cl = "class=\"bsc\"";
							}
							     
							if( 0 != pid){
								OrderGoods og = new OrderGoods();
								og.setTid(pid); 
								og.setStatues(1); 
								og.setRealnum(realcont);
								  
								listr.add(og);
							}
							

						}
					}
				}
			}
		}
      
		return listr;
	}
}
