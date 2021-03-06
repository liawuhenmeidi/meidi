package httpClient;

import goodsreceipt.GoodsReceitManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.csvreader.CsvReader;

import utill.PathUtill;
import utill.StringUtill;
import utill.TimeUtill;

public class inventoryOut {
	protected static Log logger = LogFactory.getLog(inventoryOut.class);

	public static String urlOut = "http://scs.suning.com/sps/rtOrderReport/querySendReport.action";

	public static String dowmurlOut = "http://scs.suning.com/sps/rtOrderReport/downLoadModelReport.action";

	public static String getHtmlStringOut(URI uri, String starttime,
			String endtime, int page, MyMainClient mc) {
		String str = ""; 
		// logger.info(starttime);  
		// logger.info(endtime); 
		try { 
			HttpUriRequest selectPost = RequestBuilder.post().setUri(uri)
					.addParameter("flage", "3")
					.addParameter("formName", "reportQueryConditon")
					.addParameter("receiveCode", "")
					.addParameter("receivedDate1", starttime)
					.addParameter("receivedDate2", endtime)
					.addParameter("page", page + "")
					// .addParameter("buyerProductcode", "101123403")
					.build();

			CloseableHttpResponse response2 = MyMainClient.getHttpclient()
					.execute(selectPost);

			int statusCode = response2.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
					|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {

				try {
					MyLogin.loginpost(new URI(MyLogin.url));
				} catch (URISyntaxException e) {
					logger.info(e);
				}
				response2 = MyMainClient.getHttpclient().execute(selectPost);

			}

			HttpEntity entity2 = response2.getEntity();
			if (entity2 != null) {

				str = EntityUtils.toString(entity2, "UTF-8");

			}

			response2.close();

		} catch (ClientProtocolException e) {
			logger.info(e);
		} catch (IOException e) {
			logger.info(e);
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

	public static void get(String starttime, String endtime) {
		// startTime = "2015-05-03"; 
		if (StringUtill.isNull(starttime) || StringUtill.isNull(endtime)) {
			return;  
		}
		
		save(starttime, endtime);
		
		  
		try {  
			String tempPath = PathUtill.getXMLpath();
  
			tempPath += "data" + File.separator + "InventoryOut";
			logger.info(tempPath);
   
			File file = new File(tempPath);
			if (!file.exists()) {
				file.mkdirs();
			}
 
			String file2 = tempPath + File.separator + "InventoryOut.csv";
			/*
			 * File file2 = new File(tempPath + File.separator +
			 * "InventoryOut.csv"); 
			 */  
			// file2.createNewFile();
 
			// logger.info(file2.getAbsolutePath());
			// logger.info(file2.); 
			CsvReader reader = new CsvReader(file2, ',', Charset.forName("GBK")); // 一般用这编码读就可以了
 
			GoodsReceitManager.saveOut(reader, starttime, endtime);
            
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info(e);
		} // 跳过表头 如果需要表头的话，不要写这句。

	}

	public static boolean getinventoryOut(String starttime, String endtime,
			MyMainClient mc) {
		try {
			logger.info("getinventoryINReturn");
			URI uri = new URI(inventoryOut.urlOut);
			String responseContent = getHtmlStringOut(uri, starttime, endtime,
					1, mc);

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
				GoodsReceitManager.saveOut(tr, starttime, endtime);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info(e);
		}
		return true;
	}

	public static void save(String starttime, String endtime) {
		URI uri = null;

		try { 
			uri = new URI(dowmurlOut);
		} catch (URISyntaxException e) {
			logger.info(e);
		} 
		HttpUriRequest request = RequestBuilder.post().setUri(uri)
				.addParameter("formName", "reportQueryConditon")
				.addParameter("receivedDate1", starttime)
				.addParameter("receivedDate2", endtime)
				.addParameter("flage", "3")
				// .addParameter("buyerProductcode", "101123403")
				.build();

		CloseableHttpResponse response2 = null;

		try {
			response2 = MyMainClient.getHttpclient().execute(request);
		} catch (ClientProtocolException e) {
			logger.info(e);
		} catch (IOException e) {
			logger.info(e);
		}

		int statusCode = response2.getStatusLine().getStatusCode();

		if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
				|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
			try {
				MyLogin.loginpost(new URI(MyLogin.url));
			} catch (URISyntaxException e) {
				logger.info(e);
			}

			try {
				response2 = MyMainClient.getHttpclient().execute(request);
			} catch (ClientProtocolException e) {
				logger.info(e);
			} catch (IOException e) {
				logger.info(e);
			}

		}
           
		
		MyMainClient.mapreturn.put("Inventoryoutstatue",statusCode+"");  
		
		
		HttpEntity entity = response2.getEntity();

		// EntityUtils.consume(entity);
		/*
		 * if (entity != null) { String str = EntityUtils.toString(entity,
		 * "UTF-8"); if(StringUtill.isNull(str)){ MyLogin.loginpost(new
		 * URI(MyLogin.url)); response2 = MyMainClient.getHttpclient()
		 * .execute(request); entity = response2.getEntity(); } }
		 */

		InputStream in = null;

		try {
			in = entity.getContent();
		} catch (IllegalStateException e) {
			logger.info(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info(e);
		}

		String tempPath = PathUtill.getXMLpath();
		tempPath += "data" + File.separator + "InventoryOut";
		File file = new File(tempPath);
		  
		logger.info(file.exists());  
		if (!file.exists()) { 
			try{logger.info(file.mkdirs());
			}catch(Exception e){
				logger.info(e);
			}  
		}      
		logger.info(tempPath + File.separator + "InventoryOut.csv"); 
		  
		File file2 = new File(tempPath + File.separator + "InventoryOut.csv");
     
		try {        
			if(!file2.exists()){ 
				file2.createNewFile(); 
			}
			 
		} catch (IOException e) {
			logger.info(e);
			logger.info(file.getAbsolutePath() + File.separator + "InventoryOut.csv");
			 
			file = new File(file.getAbsolutePath());
			if (!file.exists()) {
				file.mkdirs();
			}   
		  	
			file2 = new File(file.getAbsolutePath() + File.separator + "InventoryOut.csv");
			try {
				file2.createNewFile();
			} catch (IOException e1) {
			logger.info(e);
			}
		} 

		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(file2);
		} catch (FileNotFoundException e) {
			logger.info(e);
		}

		int l = -1;
		byte[] tmp = new byte[1024];
		try {
			while ((l = in.read(tmp)) != -1) {
				fout.write(tmp, 0, l);
				// 注意这里如果用OutputStream.write(buff)的话，图片会失真，大家可以试试
			}
		} catch (IOException e) {
			logger.info(e);
		}
		try {
			fout.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fout.close();
		} catch (IOException e) {
			logger.info(e);
		}
		try {
			response2.close();
		} catch (IOException e) {
			logger.info(e);
		}
		  
		finally{ 
			try {
				in.close();
			} catch (IOException e) {
				logger.info(e); 
			}
		}

	}

	public static boolean getinventoryOutModel(URI uri, String starttime,
			String endtime, MyMainClient mc) {
		try {
			logger.info("getinventoryINReturnModel");
			String responseContent = getHtmlStringOut(uri, starttime, endtime,
					1, mc);
			if (StringUtill.isNull(responseContent)) {
				MyLogin.loginpost(new URI(MyLogin.url));
				responseContent = getHtmlStringOut(uri, starttime, endtime, 1,
						mc);
			}
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
				GoodsReceitManager.saveOut(tr, starttime, endtime);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
