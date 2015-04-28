package httpClient;

import goodsreceipt.GoodsReceitManager;
import goodsreceipt.OrderReceitManager;

import java.io.IOException;
import java.net.URI;
import java.util.Iterator;

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

import utill.StringUtill;
import utill.TimeUtill;
  
public class inventoryOrder {
	protected static Log logger = LogFactory.getLog(inventoryOrder.class); 
	public static String url = "http://scs.suning.com/sps/rtOrderReport/queryReport.action";  
	public static String urlModel =  "http://scs.suning.com/sps/rtOrderReport/queryModelReport.action";
	
	public static String getHtmlString(URI uri, String starttime,
			String endtime, int page, MyMainClient mc) {
		String str = ""; 

		try { 
			HttpUriRequest selectPost = RequestBuilder.post().setUri(uri)
					.addParameter("formName", "reportQueryConditon")
					.addParameter("puteEnddate1", starttime)
					.addParameter("puteEnddate2", endtime)
					.addParameter("flage", "1")  
					// .addParameter("menuid", "100000111")
					.addParameter("page", page + "") 
					// .addParameter("scspageexptime", "1427005676454")
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

	public static String getHtmlStringOut(URI uri, String starttime,
			String endtime, int page, MyMainClient mc) {
		String str = "";
		// logger.info(starttime);
		// logger.info(endtime);
		try {
			HttpUriRequest selectPost = RequestBuilder.post().setUri(uri)
					.addParameter("flage", "1")
					.addParameter("formName", "reportQueryConditon")
					.addParameter("receiveCode", "")
					.addParameter("puteEnddate1", starttime)
					.addParameter("puteEnddate2", endtime)
					.addParameter("page", page + "")
					// .addParameter("buyerProductcode", "101123403")
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
 
	public static String getHtmlStringOutModel(URI uri, String starttime,
			String endtime, int page, MyMainClient mc) {
		String str = "";
		// logger.info(starttime);
		// logger.info(endtime);
		try { 
			HttpUriRequest selectPost = RequestBuilder.post().setUri(uri)
					.addParameter("flage", "2")
					.addParameter("formName", "reportQueryConditon")
					.addParameter("receiveCode", "") 
					.addParameter("puteEnddate1", starttime)
					.addParameter("puteEnddate2", endtime)
					.addParameter("page", page + "")
					// .addParameter("buyerProductcode", "101123403")
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
	
	public static String getHtmlStringReturn(URI uri, String starttime,
			String endtime, int page, MyMainClient mc) {
		String str = "";
		// starttime = TimeUtill.getdateString();
		// endtime = TimeUtill.getdateString();

		starttime = "2015-04-01";
		endtime = TimeUtill.getdateString();
		// logger.info(uri);
		// logger.info(starttime);
		// logger.info(endtime);

		try {
			HttpUriRequest selectPost = RequestBuilder.post().setUri(uri)
					.addParameter("flage", "1")
					.addParameter("formName", "reportQueryConditon")
					.addParameter("orderCode", "")
					.addParameter("puteEnddate1", starttime)
					.addParameter("puteEnddate2", endtime)

					// .addParameter("page", page+"")
					.addParameter("buyerProductcode", "101123403").build();

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
					int start = str.indexOf("共") + 1;
					str = str.substring(start, str.length());

					int end = str.indexOf("页");

					num = str.substring(0, end);

					// logger.info(end);
					// logger.info(str.substring(0,
					// end));
					// logger.info(str);

					// logger.info(num);
				}
			}
		}
		return Integer.valueOf(num);
	}

	public static boolean getinventoryIN(URI uri, String starttime,
			String endtime, MyMainClient mc) {
		try {
			logger.info("getinventoryIN");
			String responseContent = getHtmlString(uri, starttime, endtime, 1,
					mc);
			if (StringUtill.isNull(responseContent)) {
				MyLogin.loginpost(new URI(MyLogin.url));
				responseContent = getHtmlString(uri, starttime, endtime, 1, mc);
			}
			int num = getNum(responseContent);
			// logger.info(num);
			for (int i = 1; i <= num; i++) {
				responseContent = getHtmlString(uri, starttime, endtime, i, mc);
				Document doc = MyJsoup.getDocumnetByStr(responseContent);
				Element en = doc.getElementById("gridTable");
				// logger.info(en);
				Elements tr = en.getElementsByTag("tr");
				GoodsReceitManager.saveIN(tr, starttime, endtime);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public static boolean getinventoryOut(String starttime, String endtime,
			MyMainClient mc) {
		try {
			logger.info("getinventoryOrder");
			URI uri = new URI(inventoryOrder.url);
			String responseContent = getHtmlStringOut(uri, starttime, endtime,
					1, mc);
			if (StringUtill.isNull(responseContent)) {
				MyLogin.loginpost(new URI(MyLogin.url));
				responseContent = getHtmlStringOut(uri, starttime, endtime, 1,
						mc);
			}  
		//	logger.info(responseContent);
			int num = getNum(responseContent);
			// logger.info(num);
			for (int i = 1; i <= num; i++) {
				responseContent = getHtmlStringOut(uri, starttime, endtime, i,
						mc);
				Document doc = MyJsoup.getDocumnetByStr(responseContent);
				Element en = doc.getElementById("gridTable");
				// logger.info(en);
				Elements tr = en.getElementsByTag("tr");
				// logger.info(tr);
				OrderReceitManager.saveOut(tr, starttime, endtime);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public static boolean getinventoryOutModel(String starttime, String endtime,
			MyMainClient mc) { 
		try { 
			logger.info("getinventoryOrder");
			URI uri = new URI(inventoryOrder.urlModel);
			String responseContent = getHtmlStringOutModel(uri, starttime, endtime,
					1, mc);
			if (StringUtill.isNull(responseContent)) {
				MyLogin.loginpost(new URI(MyLogin.url));
				responseContent =getHtmlStringOutModel(uri, starttime, endtime, 1,
						mc); 
			}       
			//logger.info(responseContent);
			int num = getNum(responseContent);
			// logger.info(num); 
			for (int i = 1; i <= num; i++) {
				responseContent =getHtmlStringOutModel(uri, starttime, endtime, i,
						mc);
				Document doc = MyJsoup.getDocumnetByStr(responseContent);
				Element en = doc.getElementById("gridTable");
				// logger.info(en);
				Elements tr = en.getElementsByTag("tr");
				// logger.info(tr); 
				OrderReceitManager.saveOutModel(tr, starttime, endtime);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
}
