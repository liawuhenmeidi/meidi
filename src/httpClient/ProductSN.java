package httpClient;

import goodsreceipt.GoodsReceipt;
import goodsreceipt.GoodsReceitManager;
import goodsreceipt.OrderReceitManager;

import inventory.InventoryBranchManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import product.Product;
import product.ProductManager;
import product.ProductService;

import com.csvreader.CsvReader;

import utill.DBUtill;
import utill.PathUtill;
import utill.StringUtill;
import utill.TimeUtill;

public class ProductSN {
	protected static Log logger = LogFactory.getLog(ProductSN.class);
	public static String url = "http://scs.suning.com/sps/productManagerController/queryProduct.action?menuid=100000215&scspageexptime=1432207734342";
	 
	public static String deatailurl = "http://scs.suning.com/sps/productManagerController/showProductetail.action?";
  
	public static String getHtmlString(int i, MyMainClient mc) {
		String str = "";
   
		try {  
			URI uri = new URI(url);  
			HttpUriRequest selectPost = RequestBuilder.post().setUri(uri)
					.addParameter("menuid", "100000215")
					.addParameter("scspageexptime", "1432210238523")
					.addParameter("createStartDate",TimeUtill.getdateString())
					.addParameter("createEndDate", TimeUtill.getdateString())
					.addParameter("page", i+"") 
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
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}

		return str;
	} 
  
	public static String getHtmlStringDetail(String snProductCode,MyMainClient mc) {
		String str = ""; 
		try {
			URI uri = new URI(deatailurl);   
			HttpUriRequest selectPost = RequestBuilder.post().setUri(uri)
					.addParameter("snProductCode", snProductCode)
					.build();
 
			CloseableHttpResponse response2 = MyMainClient.getHttpclient()
					.execute(selectPost);
 
			int statusCode = response2.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
					|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
				try {
					MyLogin.loginpost(new URI(MyLogin.url));
 
					response2 = MyMainClient.getHttpclient()
							.execute(selectPost);
				} catch (URISyntaxException e) {
					logger.info(e);
				}
			}
 
			HttpEntity entity2 = response2.getEntity();

			if (entity2 != null) {

				str = EntityUtils.toString(entity2, "UTF-8");

			} 

			response2.close();

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
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
					 
				}
			}
		}
		return Integer.valueOf(num);
	}
    
	 
	public static void save(MyMainClient mc){ 
		      
		List<Product> list = getinventory(mc);  
		//Collection list = ProductService.gettypeNUmmapC(555).values();
		//logger.info(list.size()); 
		Map<String, Product> map = ProductService.gettypeNUmmapC(50);
		//logger.info(map.size()); 
		List<String> li = new ArrayList<String>();  
		if(!list.isEmpty()){    
			Iterator<Product> it =list.iterator(); 
			while(it.hasNext()){      
				Product p = it.next();  
				Product DBp = null ;
				/*String sqlnew = "insert into mdproduct(id, name, ptype,categoryID,pstatues,size,stockprice,mataintime,matainids,encoded,saleType) VALUES (null, null,'"
						+ p.getType() 
						+ "','"   
						+ 555  
						+ "',0,'"
						+ p.getSize() 
						+ "'," 
						+ p.getStockprice()
						+ "," 
						+ p.getMataintime()
						+ ",'"
						+ p.getMatainids()
						+ "','" 
						+ p.getEncoded() + "','" + 1 + "')";
						
						      
				li.add(sqlnew);		*/
						  
						    
				Product DBpp = map.get(p.getEncoded());
				if(null != DBpp){
					map.remove(p.getEncoded()); 
					if(DBpp.getCategoryID() == 50){
						DBp = DBpp ;
					} 
				} 
				   
				String sql = "";
				if(null == DBp){ 
					sql = "insert into mdproduct(id, name, ptype,categoryID,pstatues,size,stockprice,mataintime,matainids,encoded,saleType) VALUES (null, null,'"
							+ p.getType() 
							+ "','"   
							+ 50   
							+ "',0,'"
							+ p.getSize() 
							+ "'," 
							+ p.getStockprice()
							+ "," 
							+ p.getMataintime()
							+ ",'"
							+ p.getMatainids()
							+ "','" 
							+ p.getEncoded() + "','" + 1 + "')";
					//logger.info(sql);   
				}else {
					if(DBp.getStatues()  == 0 ){
						if(!DBp.getType().equals(p.getType()) || !DBp.getEncoded().equals(p.getEncoded()) ){
							sql = "update mdproduct set ptype = '"+p.getType()+"' where encoded = '"+p.getEncoded()+"' and pstatues = 0 ;";
						}
						
					}else {
						if(!DBp.getType().equals(p.getType()) || !DBp.getEncoded().equals(p.getEncoded()) ){
							sql = "update mdproduct set ptype = '"+p.getType()+"',pstatues = 0 where encoded = '"+p.getEncoded()+"' and pstatues = 1 ;";
						}
						
						
					}
				} 
				  
				if(!StringUtill.isNull(sql)){
					li.add(sql);  
					logger.info(sql); 
				}
				  
			}    
		}     
		    
		logger.info(map.size()); 
		     
		Collection<String> c = map.keySet();
		 String str = c.toString();  
		 str = str.substring(1, str.length()-1); 
		//String sqldel = "DELETE  from mdproduct where encoded in ("+str+");";
		//logger.info(sqldel); 
		         
		DBUtill.sava(li);   
		  
		  
		
		logger.info(li.size());
	}
	public static List<Product> getinventory(MyMainClient mc) {
		List<Product> list = new ArrayList<Product>();
		try { 
			logger.info("getinventoryIN"); 
			 
			String responseContent = getHtmlString(1,
					mc);   
			if (StringUtill.isNull(responseContent)) {
				MyLogin.loginpost(new URI(MyLogin.url));
				responseContent = getHtmlString(1, mc);
			}   
     
			int num = getNum(responseContent); 
			//num = 2 ;  
			logger.info(num); 
			for (int i = 1; i <= num; i++) {
				responseContent = getHtmlString(i, mc);
				Document doc = MyJsoup.getDocumnetByStr(responseContent);
				Element en = doc.getElementById("gridTable");
				// logger.info(en);
				Elements tr = en.getElementsByTag("tr");
               // logger.info(tr);  
				if (!tr.isEmpty()) { 
					Iterator<Element> it = tr.iterator();
					while (it.hasNext()) {
						 
						// tr   一条记录  
						Element e = it.next(); 
						Product gr = new Product();  
						Elements td = e.getElementsByTag("td");
						//logger.info(td); 
						if (!td.isEmpty()) {
		  
							Iterator<Element> tdit = td.iterator();
						
							int j = 0 ;     
							while (tdit.hasNext()) { 
								j++;   
								Element tde = tdit.next();
                                //logger.info(tde); 
								String str = tde.text(); 
								if (j == 2) {    
										gr.setEncoded(str);
										String response = getHtmlStringDetail(str,
												mc);   
										Document docc = MyJsoup.getDocumnetByStr(response);
										Elements ents = docc.getElementsByTag("table");
										//logger.info(ents.size());
										if(!ents.isEmpty()){
											Iterator<Element> itt = ents.iterator();
											while(itt.hasNext()){
												Element ent = itt.next();
												Elements entrs = ent.getElementsByTag("tr");
												Element enttr =  entrs.get(4);
												Elements enttds = enttr.getElementsByTag("td");
												//logger.info(enttds);  
												Element enttd =  enttds.get(7);
												gr.setType(enttd.text());
												//logger.info(enttd);
											}
											
											 
										}
								}
							} 
							//logger.info(gr); 
							list.add(gr);
						} 
					} 
				} 
			} 
         logger.info(list.size()); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	  return list ;
	}

	
	

}
