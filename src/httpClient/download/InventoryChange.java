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

	public static List<SNInventory> compare(String startTime, String endTime) {

		List<SNInventory> list = new ArrayList<SNInventory>();
		if (StringUtill.isNull(startTime)) {
			return list;
		}
		if (StringUtill.isNull(endTime)) {
			endTime = TimeUtill.getdateString();
		}
		Map<String, Map<String, List<GoodsReceipt>>> mapgr = GoodsReceitManager
				.getMapGoodNum(startTime, endTime);

		logger.info(mapgr.size());

		Collection<SNInventory> liststart = get(startTime);

		Map<String, SNInventory> mapstart = changeMap(liststart);

		Collection<SNInventory> listend = get(TimeUtill.dataAdd(endTime, 1));

		logger.info(liststart.size());
		logger.info(listend.size());

		int samecount = 0;

		if (!listend.isEmpty()) {
			Iterator<SNInventory> it = listend.iterator();
			while (it.hasNext()) {
				SNInventory inend = it.next();
				// logger.info(inend.getBranchNum() + "_" + inend.getGoodNum());
				String key = StringUtill.getStringNocn(inend.getBranchName())
						+ "_" + inend.getGoodNum();

				SNInventory instart = mapstart.get(key);

				if (null != mapgr) {
					// logger.info(inend.getGoodNum());
					Map<String, List<GoodsReceipt>> mapg = mapgr.get(inend
							.getGoodNum());
					// logger.info(mapg);
					if (null != mapg) {
						String bnum = StringUtill.getStringNocn(inend
								.getBranchName());

						List<GoodsReceipt> li = mapg.get(bnum);
						// logger.info(li);
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
							logger.info(inend.getGoodNum());
							logger.info(bnum);
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
						if (inend.getInInbranch() != 0) {
							SNInventory in = inend;
							list.add(in);

						} else {
							samecount++;
						}
					} else if (instart.getNum() - inend.getNum() != 0) {
						SNInventory in = inend;
						in.setInreduce(instart.getNum() - inend.getNum());
						// logger.info(in.getInInbranch());
						list.add(in);
					}
				} else {
					if (inend.getNum() == 0) {
						if(inend.getInInbranch() != 0){
							SNInventory in = inend;
							// logger.info(in.getInInbranch());
							in.setInreduce(0 - in.getNum());
							list.add(in);   
						}  
						// logger.info(inend.getGoodpName());
					} else {   
						// logger.info("none");
						SNInventory in = inend; 
						// logger.info(in.getInInbranch());
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
			Set<Map.Entry<String, SNInventory>> set = mapstart.entrySet();
			Iterator<Map.Entry<String, SNInventory>> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry<String, SNInventory> mapent = it.next();
				SNInventory in = mapent.getValue();
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

				// if(in.getNum() != 0){
				in.setInreduce(in.getNum() + count);
				in.setInInbranch(count);
				list.add(in);
				// }

			}
		}
		// logger.info("list" + list.size());
		return list;
	}

	public static Map<String, SNInventory> changeMap(Collection<SNInventory> list) {
		Map<String, SNInventory> map = new HashMap<String, SNInventory>();
		if (!list.isEmpty()) {
			Iterator<SNInventory> it = list.iterator();
			while (it.hasNext()) {
				SNInventory in = it.next();
				String key = StringUtill.getStringNocn(in.getBranchName())
						+ "_" + in.getGoodNum();
				map.put(key, in);
			}
		}

		// logger.info(map.size());
		return map;
	}
	// type branch  
	public static Map<String,Map<String, SNInventory>> changeMapTypeBranch(Collection<SNInventory> list) {
		Map<String,Map<String, SNInventory>> map = new HashMap<String,Map<String, SNInventory>>();
		if (null != list && !list.isEmpty()) {
			Iterator<SNInventory> it = list.iterator();
			while (it.hasNext()) {
				SNInventory in = it.next();
				
				Map<String, SNInventory> mapb = map.get(in.getGoodNum());
				if(null == mapb){ 
					mapb = new HashMap<String, SNInventory>();
					map.put(in.getGoodNum(), mapb);  
				}
				
				mapb.put(StringUtill.getStringNocn(in.getBranchName()), in);
			}
		}

		// logger.info(map.size());
		return map;
	} 
	
	public static Map<String,Map<String, SNInventory>> changeMapTypeBranchNum(Collection<SNInventory> list) {
		Map<String,Map<String, SNInventory>> map = new HashMap<String,Map<String, SNInventory>>();
		if (!list.isEmpty()) {
			Iterator<SNInventory> it = list.iterator();
			while (it.hasNext()) {
				SNInventory in = it.next();
				
				Map<String, SNInventory> mapb = map.get(in.getGoodNum());
				if(null == mapb){ 
					mapb = new HashMap<String, SNInventory>();
					map.put(in.getGoodNum(), mapb);  
				}
				 
				mapb.put(in.getBranchNum(), in);
			}
		}

		// logger.info(map.size());
		return map;
	}

	public static Map<String, SNInventory> changeMap(Collection<SNInventory> list,
			String branch, String type) {
		Map<String, SNInventory> map = new HashMap<String, SNInventory>();
		if (!list.isEmpty()) {
			Iterator<SNInventory> it = list.iterator();
			while (it.hasNext()) {
				SNInventory in = it.next();
				String key = "";
				if (StringUtill.isNull(branch) && StringUtill.isNull(type)) {
					key = in.getGoodNum();
				} else {
					key = StringUtill.getStringNocn(in.getBranchName()) + "_"
							+ in.getGoodNum();
				}

				map.put(key, in);
			}
		}

		// logger.info(map.size());
		return map;
	}
    
	public static Collection<SNInventory> getbyRead(CsvReader reader){
		Map<String, SNInventory> map = new HashMap<String, SNInventory>();
		try {
			reader.readHeaders();
		
        int count = 0 ;
		while (reader.readRecord()) { // 逐行读入除表头的数据
			String[] strs = reader.getValues();
			if (null != strs) {
				SNInventory in = new SNInventory();
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
					}else if(i == 19){ 
						in.setNomention(Integer.valueOf(str));
					}

				}

				String key = StringUtill.getStringNocn(in.getBranchName())
						+ "_" + in.getGoodNum();
               // logger.info(key); 
				SNInventory inmap = map.get(key);
//count ++;
				if (inmap == null) { 
					map.put(key, in);
				} else {
					// logger.info(in.getGoodpName());
					inmap.setATP(in.getATP() + inmap.getATP());
					inmap.setNum(in.getNum() + inmap.getNum());
				}

		 	}
		}   
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//    logger.info(count);
		logger.info(map.size());

		return map.values(); 
	} 
	 
	public static Collection<SNInventory> get(String startTime) {
		// startTime = "2015-05-03";
		// List<Inventory> list = new ArrayList<Inventory>();
		Collection<SNInventory> co = null;
		try {
			String tempPath = PathUtill.getXMLpath();
			tempPath += "data" + File.separator + "DownloadInventory"
					+ File.separator + startTime+File.separator+"SuNing";
			logger.info(tempPath);
			File file = new File(tempPath);
			if (!file.exists()) {
				file.mkdirs();
			}  
 
			File file2 = new File(tempPath + File.separator + "common.csv");
			// file2.createNewFile();
            if(file2.exists()){
            	CsvReader reader = new CsvReader(file2.getAbsolutePath(), ',',
    					Charset.forName("GBK")); // 一般用这编码读就可以了
    			co = getbyRead(reader);  
    			reader.close(); 
            }   
	}catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  
		return co;
	}
	
	public static Collection<SNInventory> get(String startTime,String type) {
		// startTime = "2015-05-03";
		// List<Inventory> list = new ArrayList<Inventory>();
		Collection<SNInventory> co = null; 
		try {
			String tempPath = PathUtill.getXMLpath();
			tempPath += "data" + File.separator + "DownloadInventory"
					+ File.separator + startTime+File.separator+type;
			logger.info(tempPath);
			File file = new File(tempPath);
			if (!file.exists()) {
				file.mkdirs();
			}  
 
			File file2 = new File(tempPath + File.separator + "common.csv");
			// file2.createNewFile();
            if(file2.exists()){
            	CsvReader reader = new CsvReader(file2.getAbsolutePath(), ',',
    					Charset.forName("GBK")); // 一般用这编码读就可以了
    			co = getbyRead(reader);  
    			reader.close(); 
            }
			
			 
	}catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
		return co;
	}
	
	
	// 型号 , 状态
	public static Map<String, List<SNInventory>> getMapBranchType(User user,
			String startTime, int branchid) {
		// startTime = "2015-05-03";　
		// List<Inventory> list = new ArrayList<Inventory>();
		Branch branch = BranchService.getMap().get(branchid);
		Map<String, List<SNInventory>> map = new HashMap<String, List<SNInventory>>();
		if (null != branch &&branch.getBranchtype().getSaletype() == SaleModel.Model.苏宁
				.getValue()) {
			String bnum = branch.getEncoded();
			Map<String, Product> mapp = ProductService.gettypeNUmmap(branch
					.getPid());
			logger.info(bnum + "__" + startTime);

			try {
				String tempPath = PathUtill.getXMLpath();
				tempPath += "data" + File.separator + "DownloadInventory"
						+ File.separator + startTime+File.separator+"SuNing";
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
						SNInventory in = new SNInventory();
						for (int i = 0; i < strs.length; i++) {
							// logger.info(i);
							String str = strs[i];
							if (i == 6) {
								 //logger.info(str);
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
								//logger.info(str); 
								in.setATP(Integer.valueOf(str));
							} else if (i == 18) {
								//logger.info(str);
								in.setNum(Integer.valueOf(str));
							}
						}

						String bnu = StringUtill.getStringNocn(in
								.getBranchName());

						if (bnum.equals(bnu)) {

							String key = in.getGoodNum();

							if (null != mapp.get(key)) {
								String pname = mapp.get(key).getType();

								List<SNInventory> inmap = map.get(pname);

								if (inmap == null) {
									inmap = new ArrayList<SNInventory>();
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
	public static Map<String, List<SNInventory>> getMapBranchTypeNum(User user,
			String startTime, int branchid) {
		// startTime = "2015-05-03";
		// List<Inventory> list = new ArrayList<Inventory>();
		Branch branch = BranchService.getMap().get(branchid);
		Map<String, List<SNInventory>> map = new HashMap<String, List<SNInventory>>();
		if (branch.getBranchtype().getSaletype() == SaleModel.Model.苏宁
				.getValue()) {
			String bnum = branch.getEncoded();
			Map<String, Product> mapp = ProductService.gettypeNUmmap(branch
					.getPid());
			logger.info(bnum + "__" + startTime);

			try {
				String tempPath = PathUtill.getXMLpath();
				tempPath += "data" + File.separator + "DownloadInventory"
						+ File.separator + startTime+File.separator+"SuNing";
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
						SNInventory in = new SNInventory();
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
 
						if (bnum.equals(bnu) || StringUtill.isNull(bnu) && bnum.equals("D011")) {

							String key = in.getGoodNum();

							if (null != mapp.get(key)) {
								// String pname = mapp.get(key).getType();

								List<SNInventory> inmap = map.get(key);

								if (inmap == null) {
									inmap = new ArrayList<SNInventory>();
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

	public static Map<String, List<SNInventory>> getMap(String startTime) {
		// startTime = "2015-05-03";
		// List<Inventory> list = new ArrayList<Inventory>();

		Map<String, List<SNInventory>> map = new HashMap<String, List<SNInventory>>();
		try {
			String tempPath = PathUtill.getXMLpath();
			tempPath += "data" + File.separator + "DownloadInventory"
					+ File.separator + startTime+File.separator+"SuNing";
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
					SNInventory in = new SNInventory();
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

					List<SNInventory> inmap = map.get(key);

					if (inmap == null) {
						inmap = new ArrayList<SNInventory>();
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
