package httpClient;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import branch.Branch;
import branch.BranchService;

import product.ProductService;

import utill.StringUtill;

public class InventorySN {
	protected static Log logger = LogFactory.getLog(InventorySN.class);
	// 特价 常规
	public static String url = "http://scs.suning.com/sps/StockReport/queryStockReport.action?menuid=100000015&scspageexptime=1428145998207";
	// 样机
	public static String url2 = "http://scs.suning.com/sps/SampleMachineReport/querySampleMachineReport.action";

	public static int getNum(String responseContent) {
		Document doc = MyJsoup.getDocumnetByStr(responseContent);
		String num = "0";
		Elements enpan = doc.getElementsByTag("span");
		if (!enpan.isEmpty()) {
			Iterator<Element> it = enpan.iterator();
			while (it.hasNext()) {
				Element en = it.next();
				String str = en.attr("class");
				if ("to".equals(str)) {
					str = en.text();
					num = str.substring(str.indexOf("共") + 1,
							str.indexOf("共") + 2);
				}
			}
		}
		return Integer.valueOf(num);
	}

	public static String getHtmlString(URI uri, String Goodsnum, int page) {
		String str = "";
		try {
			HttpUriRequest selectPost = RequestBuilder.post().setUri(uri)
					.addParameter("flag", "1").addParameter("gdsId", Goodsnum)
					.addParameter("menuid", "100000015")
					.addParameter("page", page + "")
					.addParameter("scspageexptime", "1428145998207").build();
			CloseableHttpResponse response2 = MyMainClient.getHttpclient()
					.execute(selectPost);

			HttpEntity entity2 = response2.getEntity();
			if (entity2 != null) {
				str = EntityUtils.toString(entity2, "UTF-8");

			}

			response2.close();

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return str;
	}

	public static String getHtmlStringModel(URI uri, String Goodsnum,
			String branchName, int page) {
		String str = "";
		try {
			HttpUriRequest selectPost = RequestBuilder.post().setUri(uri)
 
			.addParameter("productCode", Goodsnum)
					//.addParameter("siteName", branchName)
					.addParameter("categoryName", "")
					.addParameter("warehouseName", "")
					.addParameter("brandName", "")
					.addParameter("flag", "1") 
					.addParameter("page", page + "") 
					.build();  
			CloseableHttpResponse response2 = MyMainClient.getHttpclient()
					.execute(selectPost);

			HttpEntity entity2 = response2.getEntity();
			if (entity2 != null) {
				str = EntityUtils.toString(entity2, "UTF-8");

			}

			response2.close();

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return str;
	}

	public static List<String> getinventoryByName(String tid, String branch,
			String statues,Map<String,String> map,String id ) { 
		List<String> list = null;  
		String Goodsnum = ProductService.getIDmap().get(Integer.valueOf(tid))
				.getEncoded(); 

		Branch b = BranchService.getNameMap().get(branch);
		String branchNum = "";
		if (null != b) {
			branchNum = BranchService.getNameMap().get(branch).getEncoded();
		}
 
		logger.info(Goodsnum + "__" + branchNum + "__" + branch+"__"+statues);

		if ("7".equals(statues) || "8".equals(statues)) { 
			list = getinventory(Goodsnum, branchNum);
		} else if ("9".equals(statues)) {
			list = getinventoryModel(Goodsnum, branch,map,id );
		}
		return list;
	}

	// 获取常规特价机库存方法
	public static List<String> getinventory(String Goodsnum, String branchNum) {

		List<String> list = new ArrayList<String>();
		try {
			logger.info("selectDeliverInform");

			String responseContent = getHtmlString(new URI(url), Goodsnum, 1);
			if (StringUtill.isNull(responseContent)) {
				MyLogin.loginpost(new URI(MyLogin.url));
				responseContent = getHtmlString(new URI(url), Goodsnum, 1);
			}

			int num = getNum(responseContent);
			// logger.info(num);
			for (int i = 1; i <= num; i++) {
				if (i != 1) {
					// logger.info(i);
					responseContent = getHtmlString(new URI(url), Goodsnum, i);
				}
				int row = 0;
				Document doc = MyJsoup.getDocumnetByStr(responseContent);
				Element en = doc.getElementById("gridTable");
				// logger.info(en);
				Elements tr = en.getElementsByTag("tr");
				// logger.info(tr);
				if (!tr.isEmpty()) {
					Iterator<Element> it = tr.iterator();
					while (it.hasNext()) {
						Element e = it.next();
						// logger.info(e);
						row++;
						Elements td = null;
						Elements realtd = new Elements();
						/*
						 * if(i ==1 && row == 1){ td = e.getElementsByTag("th");
						 * // realtd.add(td.get(1)); //
						 * list.add(realtd.toString()); }else { td =
						 * e.getElementsByTag("td"); }
						 */

						// logger.info(td);
						if (row > 1) {
							td = e.getElementsByTag("td");
							if (!td.isEmpty() && td.size() > 0) {
								String s = td.get(3).text();
								/*
								 * Pattern p = Pattern.compile("[0-9]"); Matcher
								 * m = p.matcher(s);
								 */
								String bName = StringUtill.getStringNocn(s);
								if (branchNum.equals(bName)) {

									realtd.add(td.get(7));
									realtd.add(td.get(3));
									realtd.add(td.get(9));
									realtd.add(td.get(10));
									realtd.add(td.get(11));
									list.add(realtd.toString());
									// list.add(td.toString());
									// logger.info(td);
									// String num = td.get(index).text();
									// str += "_"+tr;
								}
							}
						}
					}
				}
				// logger.info(tr.size());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	// 获取样机库存方法
	public static List<String> getinventoryModel(String Goodsnum,
			String branchName,Map<String,String> map,String id ) {
 
		List<String> list = new ArrayList<String>();
		try { 
            //logger.info(Goodsnum+"&&"+branchName);
			String responseContent = getHtmlStringModel(new URI(url2),
					Goodsnum, branchName, 1);
			if (StringUtill.isNull(responseContent)) {
				MyLogin.loginpost(new URI(MyLogin.url));
				responseContent = getHtmlStringModel(new URI(url2), Goodsnum,
						branchName, 1);
			}
			// logger.info(responseContent);
 
			int num = getNum(responseContent);
			// num = 1 ;
			// logger.info(num);
			for (int i = 1; i <= num; i++) {
				if (i != 1) {
					// logger.info(i); 
					responseContent = getHtmlStringModel(new URI(url2),
							Goodsnum, branchName, i);
				}
				int row = 0;
				Document doc = MyJsoup.getDocumnetByStr(responseContent);
				Element en = doc.getElementById("gridTable");
				// logger.info(en);
				Elements tr = en.getElementsByTag("tr");
				// logger.info(tr);
				if (!tr.isEmpty()) {
					Iterator<Element> it = tr.iterator();
					while (it.hasNext()) { 
						Element e = it.next();
						 //logger.info(e);
						row++;  
						Elements td = null;
						Elements realtd = new Elements();
						/*
						 * if(i ==1 && row == 1){ td = e.getElementsByTag("th");
						 * // realtd.add(td.get(1)); //
						 * list.add(realtd.toString()); }else { td =
						 * e.getElementsByTag("td"); }
						 */

						// logger.info(td); 
						if (row > 1) { 
							td = e.getElementsByTag("td");
 
							if (!td.isEmpty() && td.size() > 0) {
    
								String s = td.get(1).text(); 
								String serialnumber =td.get(7).text(); 
								map.put(id, serialnumber);   
								Branch b = BranchService.getNameMap().get(branchName);
								//logger.info(b);   
								String str = b.getNameSN();
								//logger.info(str);    
								//branchName = b.getNameSN();
								//logger.info(s);       
								//logger.info(branchName);  
								if (str.equals(s)) {   
									realtd.add(td.get(4)); 
									realtd.add(td.get(1)); 
									realtd.add(td.get(6));
									realtd.add(td.get(7)); 
									list.add(realtd.toString());
									//logger.info(td);
								}

							}
						}
					}
				}
				// logger.info(tr.size());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

}
