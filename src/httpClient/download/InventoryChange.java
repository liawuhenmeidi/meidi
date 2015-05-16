package httpClient.download;

import enums.SaleModel;
import goodsreceipt.GoodsReceipt;
import goodsreceipt.GoodsReceitManager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import product.Product;
import product.ProductService;

import user.User;
import utill.PathUtill;
import utill.StringUtill;
import utill.TimeUtill;

import branch.Branch;
import branch.BranchService;

import com.csvreader.CsvReader;

public class InventoryChange {
	protected static Log logger = LogFactory.getLog(InventoryChange.class);

	public static void main(String args[]) {
		InventoryChange.get("2105-05-03");

	}
 
	public static List<Inventory> compare(String startTime, String endTime) {
		List<Inventory> list = new ArrayList<Inventory>();
		if (StringUtill.isNull(startTime)) {
			return list; 
		}  
		if (StringUtill.isNull(endTime)) {
			endTime = TimeUtill.getdateString();
		}  
		Map<String, Map<String, List<GoodsReceipt>>> mapgr = GoodsReceitManager
				.getMapGoodNum(startTime,endTime);       
    
		logger.info(mapgr.size()); 
		
		Collection<Inventory> liststart = get(startTime);

		Map<String, Inventory> mapstart = changeMap(liststart);
  
		Collection<Inventory> listend = get(TimeUtill.dataAdd(endTime, 1));
   
		logger.info(liststart.size()); 
		logger.info(listend.size()); 
		
		int samecount = 0;

		if (!listend.isEmpty()) {
			Iterator<Inventory> it = listend.iterator();
			while (it.hasNext()) {
				Inventory inend = it.next();
				// logger.info(inend.getBranchNum() + "_" + inend.getGoodNum());
				String key = StringUtill.getStringNocn(inend.getBranchName())
						+ "_" + inend.getGoodNum();

				Inventory instart = mapstart.get(key);

				if (null != mapgr) {
					// logger.info(inend.getGoodNum());
					Map<String, List<GoodsReceipt>> mapg = mapgr.get(inend
							.getGoodNum());
					//logger.info(mapg);  
					if (null != mapg) {
						String bnum = StringUtill.getStringNocn(inend
								.getBranchName());

						List<GoodsReceipt> li = mapg.get(bnum);
						logger.info(li); 
						if (null != li) {
							int count = 0;
							for (int i = 0; i < li.size(); i++) {
								GoodsReceipt gr = li.get(i);
								if (gr.getStatues() == 0) {
									count = count + gr.getRecevenum();
								} else {
									count = count - gr.getRecevenum();
								}
							}
							logger.info(count);  
							// logger.info(inend.getNum() - count);
							inend.setNum(inend.getNum() - count);
							inend.setInInbranch(count); 
							// logger.info(count);
						}
					}
				}
 
				if (null != instart) {

					mapstart.remove(key);
 
					if (instart.getNum() == inend.getNum()) {
						Inventory in = inend;
						list.add(in); 
						samecount++;  
					} else if (instart.getNum() - inend.getNum() != 0) {
						Inventory in = inend; 
						in.setInreduce(instart.getNum() - inend.getNum());
						//logger.info(in.getInInbranch());
						list.add(in); 
					} 
				} else { 
					if (inend.getNum() == 0) {  
						// logger.info(inend.getGoodpName());
					} else {
						// logger.info("none");
						Inventory in = inend;
						//logger.info(in.getInInbranch());
						in.setInreduce(0 - in.getNum());
						list.add(in);
					}

				}

				it.remove();
			}
		}

		logger.info("list" + list.size());
		// logger.info("samecount" + samecount);

		// logger.info("listend" + listend.size());

		// logger.info("mapstart" + mapstart.size());

		if (null != mapstart) {
			Set<Map.Entry<String, Inventory>> set = mapstart.entrySet();
			Iterator<Map.Entry<String, Inventory>> it = set.iterator();
			while (it.hasNext()) { 
				Map.Entry<String, Inventory> mapent = it.next();
				Inventory in = mapent.getValue();
				int count = 0;
				if (null != mapgr) {
					// logger.info(inend.getGoodNum());
					Map<String, List<GoodsReceipt>> mapg = mapgr.get(in
							.getGoodNum());
					if (null != mapg) {
						String bnum = StringUtill.getStringNocn(in
								.getBranchName());

						List<GoodsReceipt> li = mapg.get(bnum);
						if (null != li) {
							
							for (int i = 0; i < li.size(); i++) {
								GoodsReceipt gr = li.get(i);
								if (gr.getStatues() == 0) {
									count = count + gr.getRecevenum();
								} else {
									count = count - gr.getRecevenum();
								}
							}
							// logger.info(inend.getNum() - count);
						 
							// logger.info(count);
						} 
					} 
				}
				 
				//if(in.getNum() != 0){ 
					in.setInreduce(in.getNum()+count);
					in.setInInbranch(count); 
					list.add(in);
				//} 
				
			}
		}
		// logger.info("list" + list.size());
		return list;
	}

	public static Map<String, Inventory> changeMap(Collection<Inventory> list) {
		Map<String, Inventory> map = new HashMap<String, Inventory>();
		if (!list.isEmpty()) {
			Iterator<Inventory> it = list.iterator();
			while (it.hasNext()) {
				Inventory in = it.next();
				String key = StringUtill.getStringNocn(in.getBranchName())
						+ "_" + in.getGoodNum();
				map.put(key, in);
			}
		}

		// logger.info(map.size());
		return map;
	} 
  
	public static Map<String, Inventory> changeMap(Collection<Inventory> list,String branch,String type) {
		Map<String, Inventory> map = new HashMap<String, Inventory>();
		if (!list.isEmpty()) { 
			Iterator<Inventory> it = list.iterator();
			while (it.hasNext()) { 
				Inventory in = it.next();
				String key = "";
				if(StringUtill.isNull(branch) && StringUtill.isNull(type)){
					key = in.getGoodNum();
				}else {
					key = StringUtill.getStringNocn(in.getBranchName())
							+ "_" + in.getGoodNum();
				}
				
				map.put(key, in);
			}
		}

		// logger.info(map.size());
		return map;
	}
	
	public static Collection<Inventory> get(String startTime) {
		// startTime = "2015-05-03";
		// List<Inventory> list = new ArrayList<Inventory>();
		Map<String, Inventory> map = new HashMap<String, Inventory>();
		try {
			String tempPath = PathUtill.getXMLpath();
			tempPath += "data" + File.separator + "DownloadInventory"
					+ File.separator + startTime;
			logger.info(tempPath);
			File file = new File(tempPath);
			if (!file.exists()) {
				file.mkdirs();
			}

			File file2 = new File(tempPath + File.separator + "common.csv");
			// file2.createNewFile();

			CsvReader reader = new CsvReader(file2.getAbsolutePath(), ',',
					Charset.forName("GBK")); // 一般用这编码读就可以了

			reader.readHeaders();

			while (reader.readRecord()) { // 逐行读入除表头的数据
				String[] strs = reader.getValues();
				if (null != strs) {
					Inventory in = new Inventory();
					for (int i = 0; i < strs.length; i++) {
						// logger.info(i);
						String str = strs[i];
						if (i == 6) {
							// logger.info(str);
							in.setBranchName(str);
						} else if (i == 7) {
							in.setBranchNum(str);
						} else if (i == 8) {
							in.setGoodType(str);
						} else if (i == 13) {
							in.setGoodGroupName(str);
						} else if (i == 14) {
							in.setGoodGroupNum(str);
						} else if (i == 15) {
							in.setGoodpName(str);
						} else if (i == 16) {
							in.setGoodNum(str);
						} else if (i == 17) {
							in.setATP(Integer.valueOf(str));
						} else if (i == 18) {
							in.setNum(Integer.valueOf(str));
						}

					}

					String key = StringUtill.getStringNocn(in.getBranchName())
							+ "_" + in.getGoodNum();

					Inventory inmap = map.get(key);

					if (inmap == null) {
						map.put(key, in);
					} else {
						// logger.info(in.getGoodpName());
						inmap.setATP(in.getATP() + inmap.getATP());
						inmap.setNum(in.getNum() + inmap.getNum());
					}

				}
			}

			logger.info(map.size());
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 跳过表头 如果需要表头的话，不要写这句。
		return map.values();
	}

	// 型号 , 状态
	public static Map<String, List<Inventory>> getMapBranchType(User user,
			String startTime, int branchid) {
		// startTime = "2015-05-03";
		// List<Inventory> list = new ArrayList<Inventory>();
		Branch branch = BranchService.getMap().get(branchid);
		Map<String, List<Inventory>> map = new HashMap<String, List<Inventory>>();
		if (branch.getBranchtype().getSaletype() == SaleModel.Model.苏宁
				.getValue()) {
			String bnum = branch.getEncoded();
			Map<String, Product> mapp = ProductService.gettypeNUmmap(branch
					.getPid());
			logger.info(bnum + "__" + startTime);
 
			try {
				String tempPath = PathUtill.getXMLpath();
				tempPath += "data" + File.separator + "DownloadInventory"
						+ File.separator + startTime;
				logger.info(tempPath);
				File file = new File(tempPath);
				if (!file.exists()) {
					file.mkdirs();
				}

				File file2 = new File(tempPath + File.separator + "common.csv");
				// file2.createNewFile();

				CsvReader reader = new CsvReader(file2.getAbsolutePath(), ',',
						Charset.forName("GBK")); // 一般用这编码读就可以了

				reader.readHeaders();

				while (reader.readRecord()) { // 逐行读入除表头的数据
					String[] strs = reader.getValues();
					if (null != strs) {
						Inventory in = new Inventory();
						for (int i = 0; i < strs.length; i++) {
							// logger.info(i);
							String str = strs[i];
							if (i == 6) {
								// logger.info(str);
								in.setBranchName(str);
							} else if (i == 7) {
								in.setBranchNum(str);
							} else if (i == 8) {
								in.setGoodType(str);
							} else if (i == 13) {
								in.setGoodGroupName(str);
							} else if (i == 14) {
								in.setGoodGroupNum(str);
							} else if (i == 15) {
								in.setGoodpName(str);
							} else if (i == 16) {
								in.setGoodNum(str);
							} else if (i == 17) {
								in.setATP(Integer.valueOf(str));
							} else if (i == 18) {
								in.setNum(Integer.valueOf(str));
							}
						}

						String bnu = StringUtill.getStringNocn(in
								.getBranchName());

						if (bnum.equals(bnu)) {

							String key = in.getGoodNum();

							if (null != mapp.get(key)) {
								String pname = mapp.get(key).getType();

								List<Inventory> inmap = map.get(pname);

								if (inmap == null) {
									inmap = new ArrayList<Inventory>();
									map.put(pname, inmap);
								}

								inmap.add(in);
							}

						}

					}
				}

				logger.info(map.size());
				reader.close();
			} catch (IOException e) {
				logger.info(e);
			} // 跳过表头 如果需要表头的话，不要写这句。
		}
		return map;
	}

	// 型号 , 状态
		public static Map<String, List<Inventory>> getMapBranchTypeNum(User user,
				String startTime, int branchid) {
			// startTime = "2015-05-03";
			// List<Inventory> list = new ArrayList<Inventory>();
			Branch branch = BranchService.getMap().get(branchid);
			Map<String, List<Inventory>> map = new HashMap<String, List<Inventory>>();
			if (branch.getBranchtype().getSaletype() == SaleModel.Model.苏宁
					.getValue()) {
				String bnum = branch.getEncoded();
				Map<String, Product> mapp = ProductService.gettypeNUmmap(branch
						.getPid());
				logger.info(bnum + "__" + startTime);
	 
				try {
					String tempPath = PathUtill.getXMLpath();
					tempPath += "data" + File.separator + "DownloadInventory"
							+ File.separator + startTime;
					logger.info(tempPath);
					File file = new File(tempPath);
					if (!file.exists()) {
						file.mkdirs();
					}

					File file2 = new File(tempPath + File.separator + "common.csv");
					// file2.createNewFile();

					CsvReader reader = new CsvReader(file2.getAbsolutePath(), ',',
							Charset.forName("GBK")); // 一般用这编码读就可以了

					reader.readHeaders();

					while (reader.readRecord()) { // 逐行读入除表头的数据
						String[] strs = reader.getValues();
						if (null != strs) {
							Inventory in = new Inventory();
							for (int i = 0; i < strs.length; i++) {
								// logger.info(i);
								String str = strs[i];
								if (i == 6) {
									// logger.info(str);
									in.setBranchName(str);
								} else if (i == 7) {
									in.setBranchNum(str);
								} else if (i == 8) {
									in.setGoodType(str);
								} else if (i == 13) {
									in.setGoodGroupName(str);
								} else if (i == 14) {
									in.setGoodGroupNum(str);
								} else if (i == 15) {
									in.setGoodpName(str);
								} else if (i == 16) {
									in.setGoodNum(str);
								} else if (i == 17) {
									in.setATP(Integer.valueOf(str));
								} else if (i == 18) {
									in.setNum(Integer.valueOf(str));
								}
							}

							String bnu = StringUtill.getStringNocn(in
									.getBranchName());

							if (bnum.equals(bnu)) {

								String key = in.getGoodNum();

								if (null != mapp.get(key)) {
									//String pname = mapp.get(key).getType();

									List<Inventory> inmap = map.get(key);

									if (inmap == null) {
										inmap = new ArrayList<Inventory>();
										map.put(key, inmap);
									}

									inmap.add(in);
								}

							}

						}
					}

					logger.info(map.size());
					reader.close();
				} catch (IOException e) {
					logger.info(e);
				} // 跳过表头 如果需要表头的话，不要写这句。
			}
			return map;
		}

		
	public static Map<String, List<Inventory>> getMap(String startTime) {
		// startTime = "2015-05-03";
		// List<Inventory> list = new ArrayList<Inventory>();

		Map<String, List<Inventory>> map = new HashMap<String, List<Inventory>>();
		try {
			String tempPath = PathUtill.getXMLpath();
			tempPath += "data" + File.separator + "DownloadInventory"
					+ File.separator + startTime;
			logger.info(tempPath);
			File file = new File(tempPath);
			if (!file.exists()) {
				file.mkdirs();
			}

			File file2 = new File(tempPath + File.separator + "common.csv");
			// file2.createNewFile();

			CsvReader reader = new CsvReader(file2.getAbsolutePath(), ',',
					Charset.forName("GBK")); // 一般用这编码读就可以了

			reader.readHeaders();

			while (reader.readRecord()) { // 逐行读入除表头的数据
				String[] strs = reader.getValues();
				if (null != strs) {
					Inventory in = new Inventory();
					for (int i = 0; i < strs.length; i++) {
						// logger.info(i);
						String str = strs[i];
						if (i == 6) {
							// logger.info(str);
							in.setBranchName(str);
						} else if (i == 7) {
							in.setBranchNum(str);
						} else if (i == 8) {
							in.setGoodType(str);
						} else if (i == 13) {
							in.setGoodGroupName(str);
						} else if (i == 14) {
							in.setGoodGroupNum(str);
						} else if (i == 15) {
							in.setGoodpName(str);
						} else if (i == 16) {
							in.setGoodNum(str);
						} else if (i == 17) {
							in.setATP(Integer.valueOf(str));
						} else if (i == 18) {
							in.setNum(Integer.valueOf(str));
						}
					}

					String key = StringUtill.getStringNocn(in.getBranchName())
							+ "_" + in.getGoodNum();

					List<Inventory> inmap = map.get(key);

					if (inmap == null) {
						inmap = new ArrayList<Inventory>();
						map.put(key, inmap);
					}

					inmap.add(in);
				}
			}

			logger.info(map.size());
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 跳过表头 如果需要表头的话，不要写这句。
		return map;
	}

}
