package httpClient.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServlet;

import database.DB;
import enums.SaleModel;
import goodsreceipt.GoodsReceipt;
import goodsreceipt.GoodsReceitManager;
import goodsreceipt.OrderSN;
import httpClient.MyLogin;
import httpClient.MyMainClient;
import httpClient.inventoryOrder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import product.Product;
import product.ProductService;

import branch.Branch;
import branch.BranchService;

import com.csvreader.CsvReader;

import salesn.SaleSN;
import salesn.SaleSNManager;
import utill.PathUtill;
import utill.StringUtill;
import utill.TimeUtill;

public class SaleDownLoad extends HttpServlet implements DownLoad {
	/**     
	 *   
	 */
	private static final long serialVersionUID = 1L;
	protected static Log logger = LogFactory.getLog(SaleDownLoad.class);

	private static String url = "http://scs.suning.com/sps/supplierSaleReport/downloadSaleReport.action";

	public static void main(String args[]) {
		SaleDownLoad id = new SaleDownLoad();
		// id.get();
		String nowpath = System.getProperty("user.dir");
		logger.info(nowpath);
		logger.info(new File("").getAbsolutePath());
	}

	public static Map<String, Inventory> getMap(String starttime, String endtime) {
		// startTime = "2015-05-03";
		Map<String, Inventory> map = new HashMap<String, Inventory>();

		try {

			save(starttime, endtime);

			if (StringUtill.isNull(starttime) || StringUtill.isNull(endtime)) {
				return map;
			}

			String tempPath = PathUtill.getXMLpath();

			tempPath += "data" + File.separator + "DownloadSale";
			logger.info(tempPath);
			File file = new File(tempPath);
			if (!file.exists()) {
				file.mkdirs();
			}

			File file2 = new File(tempPath + File.separator + "sale.csv");

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
						if (i == 2) {
							// logger.info(str);
							in.setBranchNum(str);
						} else if (i == 3) {
							in.setBranchName(str);
						} else if (i == 6) {
							in.setGoodpName(str);

						} else if (i == 7) {
							in.setGoodNum(str);
						} else if (i == 8) {
							in.setSaleNum(Integer.valueOf(str));
						}

					}
					String key = in.getBranchNum() + "_" + in.getGoodNum();
					// logger.info(key);
					Inventory inmap = map.get(key);

					if (null == inmap) {
						map.put(key, in);
					} else {
						inmap.setSaleNum(inmap.getSaleNum() + in.getSaleNum());
					}
				}
			}

			logger.info(map.size());
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info(e);
		} // 跳过表头 如果需要表头的话，不要写这句。
		return map;
	}

	public static Map<String, Inventory> getMap(String starttime,
			String endtime, int branchid) {
		// startTime = "2015-05-03";
		Branch branch = BranchService.getMap().get(branchid);
		String bnum = branch.getEncoded();

		Map<String, Inventory> map = new HashMap<String, Inventory>();
		Map<String, Product> mapp = ProductService.gettypeNUmmap();
		if (StringUtill.isNull(starttime) || StringUtill.isNull(endtime)) {
			return map;
		}
		if (branch.getBranchtype().getSaletype() == SaleModel.Model.苏宁
				.getValue()) {
			try {

				save(starttime, endtime);

				String tempPath = PathUtill.getXMLpath();

				tempPath += "data" + File.separator + "DownloadSale";

				logger.info(tempPath);
				File file = new File(tempPath);
				if (!file.exists()) {
					file.mkdirs();
				}

				File file2 = new File(tempPath + File.separator + "sale.csv");
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
							if (i == 2) {
								// logger.info(str);
								in.setBranchNum(str);
							} else if (i == 3) {
								in.setBranchName(str);
							} else if (i == 6) {
								in.setGoodpName(str);

							} else if (i == 7) {
								in.setGoodNum(str);
							} else if (i == 8) {
								in.setSaleNum(Integer.valueOf(str));
							}
						}

						// logger.info(in.getBranchNum());
						// logger.info(bnum);
						// logger.info(bnum == in.getBranchNum());
						if (bnum.equals(in.getBranchNum())) {
							String key = in.getGoodNum();
							// logger.info(key);
							Product p = mapp.get(key);
							if (null != p) {
								String pname = p.getType();
								Inventory inmap = map.get(pname);
								if (null == inmap) {
									map.put(pname, in);
								} else {
									inmap.setSaleNum(inmap.getSaleNum()
											+ in.getSaleNum());
								}
							}

						}
					}
				}

				logger.info(map.size());
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // 跳过表头 如果需要表头的话，不要写这句。
		}
		return map;
	}

	public static List<Inventory> get(String starttime, String endtime) {
		// startTime = "2015-05-03";

		List<Inventory> list = new ArrayList<Inventory>();
		save(starttime, endtime);
		if (StringUtill.isNull(starttime) || StringUtill.isNull(endtime)) {
			return list;
		}
		try {
			String tempPath = PathUtill.getXMLpath();

			tempPath += "data" + File.separator + "DownloadSale";
			logger.info(tempPath);
			File file = new File(tempPath);
			if (!file.exists()) {
				file.mkdirs();
			}

			File file2 = new File(tempPath + File.separator + "sale.csv");
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
						if (i == 2) {
							// logger.info(str);
							in.setBranchNum(str);
						} else if (i == 3) {
							in.setBranchName(str);
						} else if (i == 6) {
							in.setGoodpName(str);

						} else if (i == 7) {
							in.setGoodNum(str);
						} else if (i == 8) {
							in.setSaleNum(Integer.valueOf(str));
						}

					}
					list.add(in);
				}
			}

			logger.info(list.size());
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 跳过表头 如果需要表头的话，不要写这句。
		return list;
	}

	public static List<SaleSN> getSaleSN(String starttime, String endtime) {
		// startTime = "2015-05-03";

		List<SaleSN> list = new ArrayList<SaleSN>();

		save(starttime, endtime);
		if (StringUtill.isNull(starttime) || StringUtill.isNull(endtime)) {
			return list;
		}
		try {
			String tempPath = PathUtill.getXMLpath();

			tempPath += "data" + File.separator + "DownloadSale";
			logger.info(tempPath);
			File file = new File(tempPath);
			if (!file.exists()) {
				file.mkdirs();
			}

			File file2 = new File(tempPath + File.separator + "sale.csv");
			// file2.createNewFile();

			CsvReader reader = new CsvReader(file2.getAbsolutePath(), ',',
					Charset.forName("GBK")); // 一般用这编码读就可以了

			reader.readHeaders();

			while (reader.readRecord()) { // 逐行读入除表头的数据
				String[] strs = reader.getValues();
				if (null != strs) {

					SaleSN in = new SaleSN();
					for (int i = 0; i < strs.length; i++) {
						// logger.info(i);
						String str = strs[i];
						if (i == 2) {
							// logger.info(str);
							in.setBranchnum(str);
						} else if (i == 3) {
							in.setBranchname(str);
						} else if (i == 6) {
							in.setGoodname(str);

						} else if (i == 7) {
							in.setGoodnum(str);
						} else if (i == 8) {
							in.setSalenum(Integer.valueOf(str));
						}

					}
					list.add(in);
				}
			}

			logger.info(list.size());
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 跳过表头 如果需要表头的话，不要写这句。
		return list;
	}
 
	public static void saveDB(String starttime, String endtime) {
		List<SaleSN> listsql = new ArrayList<SaleSN>();

		List<SaleSN> list = getSaleSN(starttime, endtime);

		Map<String, Map<String, List<SaleSN>>> map = new HashMap<String, Map<String, List<SaleSN>>>();

		Map<String, Map<String, List<SaleSN>>> mapdb = SaleSNManager.getMapDB(
				starttime, endtime);

		if (null == mapdb) {
			listsql =  list;
		} else {  
			if (!list.isEmpty()) { 
				Iterator<SaleSN> it = list.iterator();
				while (it.hasNext()) {
					SaleSN as = it.next();
					Map<String, List<SaleSN>> maps = map.get(as.getSaletime());
					if (null == maps) {
						maps = new HashMap<String, List<SaleSN>>();
						map.put(as.getSaletime(), maps);
					}

					List<SaleSN> li = maps.get(as.getBranchnum() + "_"
							+ as.getGoodnum());

					if (null == li) {
						li = new ArrayList<SaleSN>();
						maps.put(as.getBranchnum() + "_" + as.getGoodnum(), li);
					}

					li.add(as);
				}
			}

			if (!map.isEmpty()) {
				Set<Map.Entry<String, Map<String, List<SaleSN>>>> set = map
						.entrySet();
				Iterator<Map.Entry<String, Map<String, List<SaleSN>>>> it = set
						.iterator();
				while (it.hasNext()) {
					Map.Entry<String, Map<String, List<SaleSN>>> mapen = it
							.next();
					String time = mapen.getKey();
					Map<String, List<SaleSN>> mapbtDB = mapdb.get(time);

					Map<String, List<SaleSN>> mapbt = mapen.getValue();

					if (!mapbt.isEmpty()) {
						Set<Map.Entry<String, List<SaleSN>>> setbt = mapbt
								.entrySet();
						Iterator<Map.Entry<String, List<SaleSN>>> itbt = setbt
								.iterator();
						while (itbt.hasNext()) {
							Map.Entry<String, List<SaleSN>> mapenbt = itbt
									.next();
							String key = mapenbt.getKey();

							List<SaleSN> li = mapenbt.getValue();

							if (null == mapbtDB) {
                                       listsql.addAll(li);
							}else {
								List<SaleSN> lidb = mapbtDB.get(key);
								if(null == lidb){
									 listsql.addAll(li);
								}else {
									int num = li.size() - lidb.size();
									if(num != 0 ){
										 
										 
										Iterator<SaleSN> itlidb = lidb.iterator();
										
										while(itlidb.hasNext()){
											SaleSN sa= itlidb.next();
											Iterator<SaleSN> itli = li.iterator();
											while(itli.hasNext()){
												SaleSN s= itli.next();
												if(s.getSalenum() == sa.getSalenum()){
													itli.remove();
												}
											}
										} 
										listsql.addAll(li);
									}
								}
							}
						}
					}
				}
			}
		}
		
		
		
		
	}

	public static void save(String starttime, String endtime) {
		logger.info(starttime + "_" + endtime);
		URI uri;
		try {
			uri = new URI(url);

			// MyLogin.loginpost(new URI(MyLogin.url));
			HttpUriRequest request = RequestBuilder.post().setUri(uri)
					.addParameter("statisDateBegin", starttime)
					.addParameter("statisDateEnd", endtime)
					.addParameter("mark", "1")
					// .addParameter("buyerProductcode", "101123403")
					.build();

			// logger.info(starttime + "_" + endtime);
			// MyLogin.loginpost(new URI(MyLogin.url));
			// logger.info("login");
			CloseableHttpClient client = MyMainClient.getHttpclient();
			CloseableHttpResponse response2 = null;

			// logger.info(starttime + "_" + endtime);
			try {
				response2 = client.execute(request);
			} catch (Exception e) {
				// logger.info("aaa");
				logger.info(e);
			}

			// logger.info(starttime + "_" + endtime);

			/*
			 * logger.info(response2.getAllHeaders());
			 * response2.getHeaders("Status Code");
			 * logger.info(response2.getHeaders("Status Code")); Header[] heads
			 * = response2.getAllHeaders(); logger.info(heads.length); for(int
			 * i=0;i<heads.length;i++){ Header h = heads[i];
			 * logger.info(h.getName()); // logger.info(h.getValue()); }
			 */

			int statusCode = response2.getStatusLine().getStatusCode();
			logger.info(statusCode);

			if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
					|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {

				MyLogin.loginpost(new URI(MyLogin.url));
				response2 = MyMainClient.getHttpclient().execute(request);

			}
			HttpEntity entity = response2.getEntity();

			// EntityUtils.consume(entity);
			/*
			 * logger.info(statusCode);
			 * 
			 * 
			 * entity.getContentType(); logger.info(entity.getContentType());
			 */
			/*
			 * if (entity != null) { String str = EntityUtils.toString(entity,
			 * "UTF-8"); if(StringUtill.isNull(str)){ MyLogin.loginpost(new
			 * URI(MyLogin.url)); response2 = MyMainClient.getHttpclient()
			 * .execute(request); entity = response2.getEntity(); } }
			 */

			InputStream in = entity.getContent();

			String tempPath = PathUtill.getXMLpath();

			tempPath += "data" + File.separator + "DownloadSale";

			logger.info(tempPath);

			File file = new File(tempPath);
			if (!file.exists()) {
				file.mkdirs();
			}

			File file2 = new File(tempPath + File.separator + "sale.csv");

			file2.createNewFile();

			try {
				FileOutputStream fout = new FileOutputStream(file2);
				int l = -1;
				byte[] tmp = new byte[1024];
				while ((l = in.read(tmp)) != -1) {
					fout.write(tmp, 0, l);
					// 注意这里如果用OutputStream.write(buff)的话，图片会失真，大家可以试试
				}
				fout.flush();
				fout.close();
			} finally {
				// 关闭低层流。
				in.close();
			}
		} catch (URISyntaxException e) {
			logger.info(e);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			logger.info(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info(e);
		}

	}
}
