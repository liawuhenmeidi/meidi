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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
	protected static Log logger = LogFactory.getLog(IntelligentInventory.class);
	public static List<OrderGoods> getmessage(User user, String starttime, String endtime,
			String branch) {
		List<OrderGoods> listr = new ArrayList<OrderGoods>(); 
		try{
		  
	/*	Map<String, List<Inventory>> map = new LinkedHashMap<String, List<Inventory>>();*/
// 库存  
		Collection<SNInventory> listend = InventoryChange.get(TimeUtill.dataAdd(
				endtime, 1)); 
		   
		Map<String, SNInventory> mapinven = InventoryChange.changeMap(listend );
		    
		// System.out.println("listend"+listend.size());
		// Map<String, Inventory> mapstart = InventoryChange.changeMap(listend
		// );  
		// 样机  
		/*Map<String, Inventory> mapModel = InventoryModelDownLoad.getMap(user,
				TimeUtill.dataAdd(endtime, 1));
*/
		// 销量
		Map<String, SNInventory> mapsale = SaleDownLoad
				.getMap(starttime, endtime);

		// 未入库数量
		Map<Integer, Map<String, InventoryBranch>> mapout = InventoryBranchManager
				.getmapType(user);
        
		
		//  
		Map<String,OrderGoods> mapog = OrderGoodsManager.getmapphone(user);
		// System.out.println("mapout"+mapout);
		// Collection<Inventory> sales = SaleDownLoad.get(starttime, endtime);
 
		/*if (!listend.isEmpty()) {
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
		}*/ 
		logger.info(mapsale.size()); 
		if (!mapsale.isEmpty()) {
			 
			Set<Map.Entry<String, SNInventory>> set = mapsale.entrySet();
			Iterator<Map.Entry<String, SNInventory>> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry<String, SNInventory> mapent = it.next();
				String key = mapent.getKey(); 
				SNInventory sale = mapent.getValue();
				String bnum = "";
				if(!StringUtill.isNull(branch)){
					bnum = BranchService.getNameMap().get(branch).getEncoded();
				}else {
					bnum = sale.getBranchNum();
				}
				
				  
				if(sale.getBranchNum().equals(bnum)){
                 //logger.info(sale.getGoodpName());
				int bid = 0;   
				String tname = "";
				int pid = 0; 
				try {
					bid = BranchService.getNumMap(SaleModel.SuNing).get(bnum)
							.getId();
				} catch (Exception e) {
					// e.printStackTrace();
					bid = 0;
				}
				
				try {
					tname = ProductService.gettypeNUmmap()
							.get(sale.getGoodNum()).getType();
					pid = ProductService.gettypeNUmmap()
							.get(sale.getGoodNum()).getId();
				} catch (Exception e) {
					tname = ""; 
					// e.printStackTrace();
				}
				// logger.info(key);
				SNInventory inve = mapinven.get(key); 
				//Inventory model = mapModel.get(key);
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
					 
					sale.setOutnum(inout.getPapercount());
				}
				//logger.info("库外"+sale.getOutnum());
				//logger.info(sale.getOutnum());
				if(null != og){
					if(og.getStatues() == 1 || og.getStatues() == 2)
						sale.setOutnum(sale.getOutnum()+og.getOrdernum()); 
				} 
				//logger.info("库外"+sale.getOutnum());
				
				if(null != inve){
					sale.setNum(inve.getNum());  
				} 
				//logger.info("库存"+sale.getNum());
				//logger.info("销量"+sale.getSaleNum());
				 
				int realcont = sale.getSaleNum() 
						- (sale.getNum() + sale.getOutnum());
				//logger.info(realcont); 
				//logger.info("**********"); 
				if (!StringUtill.isNull(branch)) {
					String branchnum = BranchService.getNameMap().get(branch)
							.getEncoded();
					
					if (branchnum.equals(bnum) && realcont > 0 ) {
						if( 0 != pid){
							OrderGoods ogg = new OrderGoods();
							ogg.setTid(pid); 
							ogg.setStatues(1); 
							ogg.setRealnum(realcont);
							  
							listr.add(ogg);
						}
						
						/*List<Inventory> li = map.get(keyin);
						if (null == li) {
							li = new ArrayList<Inventory>();
							map.put(keyin, li);
						}

						li.add(sale);*/
					}
				} else {
				
					if(realcont > 0){
						 
						if( 0 != pid){
							OrderGoods ogg = new OrderGoods();
							ogg.setTid(pid); 
							ogg.setStatues(1); 
							ogg.setRealnum(realcont);
							  
							listr.add(ogg);
						}
						/*List<Inventory> li = map.get(keyin);
						if (null == li) {
							li = new ArrayList<Inventory>();
							map.put(keyin, li);
						}
						li.add(sale);*/
					}
					
				}
			}
		} 
		}  
		/*int count = 0;
		List<Integer> list = new ArrayList<Integer>();*/
 
/*		if (!map.isEmpty()) {
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
		}*/  
		}catch(Exception e){ 
			logger.info(e);
		}
		return listr;
	}
}
