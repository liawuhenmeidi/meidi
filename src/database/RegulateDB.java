package database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import utill.DBUtill;

import inventory.InventoryBranchMessage;
import inventory.InventoryBranchMessageManager;

public class RegulateDB {
	protected static Log logger = LogFactory.getLog(RegulateDB.class);

	public static void main(String args[]) {
		regulate();
	} 

	public static void regulate() {

		List<String> listsql = new ArrayList<String>();
		// String sql =
		// "SELECT mdinventorybranchmessage.* from mdinventorybranchmessage,tmp_table where mdinventorybranchmessage.type = tmp_table.type and mdinventorybranchmessage.branchid = tmp_table.branchid order by mdinventorybranchmessage.id";
		String sql = "SELECT mdinventorybranchmessage.* from mdinventorybranchmessage where mdinventorybranchmessage.type in (select id from mdproduct where categoryID = 50)";
		List<InventoryBranchMessage> list = InventoryBranchMessageManager
				.getMap(sql);

		// branch .type
		Map<Integer, Map<String, Map<Integer, List<InventoryBranchMessage>>>> map = new HashMap<Integer, Map<String, Map<Integer, List<InventoryBranchMessage>>>>();

		if (!list.isEmpty()) {
			Iterator<InventoryBranchMessage> it = list.iterator();
			while (it.hasNext()) {
				InventoryBranchMessage in = it.next();
				// logger.info(in.getBranchid());
				Map<String, Map<Integer, List<InventoryBranchMessage>>> mapb = map
						.get(in.getBranchid());

				if (null == mapb) {
					mapb = new HashMap<String, Map<Integer, List<InventoryBranchMessage>>>();
					map.put(in.getBranchid(), mapb);
				}
				// logger.info(in.getTypeid());
				Map<Integer, List<InventoryBranchMessage>> maps = mapb.get(in
						.getTypeid());

				if (null == maps) {
					maps = new HashMap<Integer, List<InventoryBranchMessage>>();
					mapb.put(in.getTypeid(), maps);
				}
logger.info(in.getTypeStatues()); 
				List<InventoryBranchMessage> li = maps.get(in.getTypeStatues());
				if (null == li) {
					li = new ArrayList<InventoryBranchMessage>();
					maps.put(in.getTypeStatues(), li);
				}

				li.add(in);

			}
		}

		logger.info(map.size());
		int count = 0;
		if (!map.isEmpty()) {
			Collection<Map<String, Map<Integer, List<InventoryBranchMessage>>>> co = map
					.values();
			logger.info(co.size());

			if (!co.isEmpty()) {
				Iterator<Map<String, Map<Integer, List<InventoryBranchMessage>>>> it = co
						.iterator();
				while (it.hasNext()) {
					Map<String, Map<Integer, List<InventoryBranchMessage>>> mapt = it
							.next();
					Collection<Map<Integer, List<InventoryBranchMessage>>> mapli = mapt
							.values();
					logger.info(mapli.size());

					if (!mapli.isEmpty()) {
						Iterator<Map<Integer, List<InventoryBranchMessage>>> itins = mapli
								.iterator();
						while (itins.hasNext()) {
							Map<Integer, List<InventoryBranchMessage>> maps = itins
									.next();

							Collection<List<InventoryBranchMessage>> li = maps
									.values();
							Iterator<List<InventoryBranchMessage>> itin = li
									.iterator();

							int branchid = 0;
							String type = "";
							int statues =  0 ;
							while (itin.hasNext()) {
								List<InventoryBranchMessage> liin = itin.next();
								if (!liin.isEmpty()) {
									Iterator<InventoryBranchMessage> itm = liin
											.iterator();
									// logger.info("*******");
									int num = 0;
									int real = 0;
									int paper = 0;
									int oldp = 0;
									int oldr = 0; 
									boolean flag = false;
									while (itm.hasNext()) {
										num++; 
										// logger.info(num);
										InventoryBranchMessage inm = itm.next();
										if (num == 1) {  
											// logger.info(inm.getId()); 
											real = inm.getAllotRealcount();  
											paper =  inm.getAllotPapercount();
											oldp = 0;  
											oldr = 0;
											branchid = inm.getBranchid();
											type = inm.getTypeid();
											statues = inm.getTypeStatues();
										} else {
											oldp = paper;
											oldr = real;
											real += inm.getAllotRealcount();
											paper += inm.getAllotPapercount();

											// String sqls =
											// "update mdinventorybranchmessage set realcount = "+real
											// +" , papercount = "+paper
											// +",oldrealcount = "+oldr
											// +",oldpapercount="+oldp +
											// " where type = "+inm.getTypeid()+" and branchid = "+inm.getBranchid()+" and id = "+inm.getId();
											// logger.info(sqls);
											// || oldp != inm.getOldpapercount()
											// || oldr != inm.getOldrealcount()
											if (real != inm.getRealcount()
													|| paper != inm
															.getPapercount()) {
												 String sqls =
												 "update mdinventorybranchmessage set realcount = "+real
												 +" , papercount = "+paper
												 +",oldrealcount = "+oldr
												 +",oldpapercount="+oldp +
												 " where type = "+inm.getTypeid()+" and branchid = "+inm.getBranchid()+" and id = "+inm.getId();
												 listsql.add(sqls);
												 logger.info(sqls); 
												 flag = true ; 
											} 
										}
									}
     
									// if(flag){
									String sqlin = "update mdinventorybranch  set realcount = "
											+ real
											+ " , papercount = "
											+ paper
											+ " where type = " 
											+ type
											+ " and branchid = " + branchid + " and typestatues = "+statues;
									listsql.add(sqlin);
									//logger.info(sqlin);
									// }

									// logger.info("*******");
								} 
							}
							// logger.info(li);

						}

					}
				}
			} 
		}     
		logger.info(listsql.size());
 
		DBUtill.sava(listsql);
	}

}
