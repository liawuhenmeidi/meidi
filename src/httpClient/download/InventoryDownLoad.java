package httpClient.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServlet;

import httpClient.MyLogin;
import httpClient.MyMainClient;
import httpClient.inventoryOrder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;

import utill.PathUtill;
import utill.TimeUtill;

public class InventoryDownLoad extends HttpServlet implements DownLoad {
	/**
	 *    
	 */
	private static final long serialVersionUID = 1L;
	protected static Log logger = LogFactory.getLog(InventoryDownLoad.class);
	private static String url = "http://scs.suning.com/sps/StockReport/downLoadStockReport.action";

	public static void main(String args[]) {
		InventoryDownLoad id = new InventoryDownLoad();
		// id.get();
		String nowpath = System.getProperty("user.dir");
		logger.info(nowpath);
		logger.info(new File("").getAbsolutePath());
	}
       
	
	public static void save() {
		URI uri;  
		try {
			uri = new URI(url);
			HttpUriRequest request = RequestBuilder.post().setUri(uri)
					.addParameter("brandNm", "")
					// .addParameter("formName", "reportQueryConditon")
					// .addParameter("receiveCode", "")

					// .addParameter("buyerProductcode", "101123403")
					.build();   
  
			CloseableHttpResponse response2 = MyMainClient.getHttpclient()
					.execute(request);
			
			int statusCode = response2.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
					|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
					MyLogin.loginpost(new URI(MyLogin.url));
				
				response2 = MyMainClient.getHttpclient().execute(request);

			}
			
			HttpEntity entity = response2.getEntity();

			//EntityUtils.consume(entity);
			/*
			 * if (entity != null) { String str = EntityUtils.toString(entity,
			 * "UTF-8"); if(StringUtill.isNull(str)){ MyLogin.loginpost(new
			 * URI(MyLogin.url)); response2 = MyMainClient.getHttpclient()
			 * .execute(request); entity = response2.getEntity(); } }
			 */
			InputStream in = entity.getContent(); 

			String tempPath = PathUtill.getXMLpath();
			tempPath += "data" + File.separator + "DownloadInventory"
					+ File.separator + TimeUtill.getdateString()+File.separator+"SuNing";
  
			logger.info(tempPath);  
 
			File file = new File(tempPath);
			if (!file.exists()) {
				file.mkdirs();
			}

			File file2 = new File(tempPath + File.separator + "common.csv");
 
			
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
				response2.close();
			} finally {
				// 关闭低层流。
				in.close();
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
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
