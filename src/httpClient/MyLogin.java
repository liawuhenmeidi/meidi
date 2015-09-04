package httpClient;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utill.StringUtill;
   
public class MyLogin { 
	protected static Log logger = LogFactory.getLog( MyLogin.class); 
	
	public  static String url = "https://passport.suning.com/ids/login";
	//public  static String url = "http://scs.suning.com/sps/portal/showPortalPage.action";
	
	public static boolean loginpost(URI uri) {  
    	// 创建默认的httpClient实例.     	   
        try{    
        	String uuid = "";
        	if(StringUtill.isNull(MyMainClient.getUuid())){
        		uuid = UUID.randomUUID().toString();
        	}else {
        		logger.info("uuid is not null"); 
        		uuid = MyMainClient.getUuid();
        	} 
      logger.info(uuid);
      logger.info(MyMainClient.getVerifyCode());
      logger.info(MyMainClient.getCacheUsername());
      logger.info(MyMainClient.getCachePassword()); 
        	HttpUriRequest login = RequestBuilder.post()
            .setUri(uri)     
            .addParameter("service", "https://scs.suning.com/sps/auth?targetUrl=http%3A%2F%2Fscs.suning.com%2Fsps%2Fmember%2Flogon.do")
            .addParameter("uuid", uuid) 
            .addParameter("loginTheme", "scs")   
            .addParameter("username", MyMainClient.getCacheUsername()) 
            .addParameter("password", MyMainClient.getCachePassword())
            .addParameter("verifyCode",MyMainClient.getVerifyCode())
            .build();      
        	
        	
      
	//logger.info("executing request " + login.getURI());  
	        CloseableHttpResponse response2 = MyMainClient.getHttpclient().execute(login);
	       /* Header[] header = response2.getAllHeaders();
	        if(null != header){
	        	for(int i=0;i<header.length;i++){
	        		logger.info(header[i]); 
	        	}
	        }*/
	        
	          
	        int statusCode = response2.getStatusLine().getStatusCode();
	         
            logger.info(statusCode);
            
	        try {  
	        	if(statusCode != HttpStatus.SC_MOVED_PERMANENTLY && statusCode!= HttpStatus.SC_MOVED_TEMPORARILY){
	        	HttpEntity entity = response2.getEntity();
                if (entity != null) {  
                    String str = EntityUtils.toString(entity, "UTF-8");
                    logger.info(str); 
                    Document doc = MyJsoup.getDocumnetByStr(str); 
    				Element en = doc.getElementById("vcodeimg1");
    				logger.info(en);
    				String src = en.attr("src"); 
    				logger.info(src); 
    				if(!StringUtill.isNull(src)){
    					String[] me = src.split("=");
    					String uid = me[1]; 
        				logger.info(uid);  
        				MyMainClient.setUuid(uid);
    				}
    				
    				MyMainClient.map.put("src", src);
    				
    			
    				 EntityUtils.consume(entity);
                 }  
	        	}
	           
                /*List<Cookie> cookies = MyMainClient.getCookieStore().getCookies();
                if (cookies.isEmpty()) {
                	logger.info("None"); 
                } else {  
                    for (int i = 0; i < cookies.size(); i++) {
                    	logger.info(cookies.get(i)); 
                    } 
                }*/ 

              

	            if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || 
	            		statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
	            logger.info(statusCode);     
	            	if(response2.getHeaders("location").length > 0){
	            		/*Header[] h = response2.getHeaders("location") ;
	            		for(int i=0;i<h.length;i++){
	            			logger.info(h[i]);  
	            		}*/   
	            		String locationURL = response2.getHeaders("location")[0].toString();
	            		//logger.info(locationURL); 
	            		locationURL = locationURL.substring(10);  
	            		// logger.info(locationURL);    
	            		//URL url = new URL(locationURL); 
	            		URI uri3 = new URI(locationURL);
	            		response2.close();
	            		//URI uri2 = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
	            		loginget(uri3);
	            	}
	            	
	            }else{ 
	            	//return false;
	            }
	        } catch (Exception e) {  
	        	e.printStackTrace();
	        } finally {   
	        	response2.close(); 
	        }  
	        return true;
        } catch (UnsupportedEncodingException e) {
			logger.info(e);
		} catch (ClientProtocolException e) {
			logger.info(e); 
		} catch (IOException e) {
			logger.info(e);
		} finally {
            
        } 
		return false;
    } 
	
	public static boolean loginget(URI uri) {  
    	// 创建默认的httpClient实例.     	
        try{  
        	//logger.info(1);
        	String uuid = UUID.randomUUID().toString();
        	HttpUriRequest login = RequestBuilder.post()
            .setUri(uri)
            .addParameter("service", "https://scs.suning.com/sps/auth?targetUrl=http%3A%2F%2Fscs.suning.com%2Fsps%2Fmember%2Flogon.do")
            .addParameter("uuid", uuid) 
            .addParameter("loginTheme", "scs") 
            .addParameter("username", MyMainClient.getCacheUsername()) 
            .addParameter("password", MyMainClient.getCachePassword())
           .addParameter("verifyCode",MyMainClient.getVerifyCode())
            .build();
        	//logger.info(1);
	      //  System.out.println("executing request " + login.getURI());  
	        CloseableHttpResponse response2 = MyMainClient.getHttpclient().execute(login);
	      //  logger.info(response2); 
	        try {  
	        	HttpEntity entity = response2.getEntity();
  
	            EntityUtils.consume(entity); 
                int statusCode = response2.getStatusLine().getStatusCode();
                logger.info(statusCode);
	            if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || 
	            		statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
	            	 
	            	if(response2.getHeaders("location").length > 0){
	            		String locationURL = response2.getHeaders("location")[0].toString();
	            		locationURL = locationURL.substring(10);
	            		//logger.info(locationURL); 
	            //		System.out.println(locationURL); 
	            		response2.close(); 
	            		URL url = new URL(locationURL);
	            		
	            		URI uri2 = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
	            	//	logger.info(uri2 );
		            	logger.info(uri2);  
	            		loginget(uri2); 
	            	}   
	            }else if(statusCode == HttpStatus.SC_NOT_FOUND){
	            	Thread.sleep(10*1000); 
	            	logger.info(uri); 
	            	loginpost(new URI(MyLogin.url)); 
	            }
	        } catch (Exception e) {  
	        	logger.info(e); 
	        } finally {  
	        	response2.close();
	        }  
	        return true; 
        } catch (UnsupportedEncodingException e) {
			logger.info(e);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			logger.info(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info(e);
		} finally {
            
        }
		return false;
    }
	
}
