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
   
 
public class MyLogin { 
	protected static Log logger = LogFactory.getLog( MyLogin.class); 
	public  static String url = "https://passport.suning.com/ids/login";
	//public  static String url = "http://scs.suning.com/sps/portal/showPortalPage.action";
	public static boolean loginpost(URI uri) {  
    	// 创建默认的httpClient实例.     	  
        try{   
        	String uuid = UUID.randomUUID().toString();
        	HttpUriRequest login = RequestBuilder.post()
            .setUri(uri)   
            .addParameter("service", "https://scs.suning.com/sps/auth?targetUrl=http%3A%2F%2Fscs.suning.com%2Fsps%2Fmember%2Flogon.do")
            .addParameter("uuid", uuid) 
            .addParameter("loginTheme", "scs")  
            .addParameter("username", MyMainClient.getCacheUsername()) 
            .addParameter("password", MyMainClient.getCachePassword())
            .build();    
	        logger.info("executing request " + login.getURI());  
	        CloseableHttpResponse response2 = MyMainClient.getHttpclient().execute(login);
	         
	        try { 
	        	
	        	HttpEntity entity = response2.getEntity();
                if (entity != null) {  
                    String str = EntityUtils.toString(entity, "UTF-8");
                   // logger.info(str);
                }  

	            EntityUtils.consume(entity);
                /*List<Cookie> cookies = MyMainClient.getCookieStore().getCookies();
                if (cookies.isEmpty()) {
                	logger.info("None"); 
                } else {  
                    for (int i = 0; i < cookies.size(); i++) {
                    	logger.info(cookies.get(i)); 
                    } 
                }*/

                int statusCode = response2.getStatusLine().getStatusCode();
               
	            if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || 
	            		statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
	            //logger.info(statusCode);     
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
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            
        } 
		return false;
    } 
	
	public static boolean loginget(URI uri) {  
    	// 创建默认的httpClient实例.     	
        try{ 
        	String uuid = UUID.randomUUID().toString();
        	HttpUriRequest login = RequestBuilder.post()
            .setUri(uri)
            .addParameter("service", "https://scs.suning.com/sps/auth?targetUrl=http%3A%2F%2Fscs.suning.com%2Fsps%2Fmember%2Flogon.do")
            .addParameter("uuid", uuid)
            .addParameter("loginTheme", "scs")
            .addParameter("username", MyMainClient.getCacheUsername()) 
            .addParameter("password", MyMainClient.getCachePassword())
            .build();
	      //  System.out.println("executing request " + login.getURI());  
	        CloseableHttpResponse response2 = MyMainClient.getHttpclient().execute(login);
	        
	        try {  
	        	HttpEntity entity = response2.getEntity();
 
	            EntityUtils.consume(entity); 
                int statusCode = response2.getStatusLine().getStatusCode();
               // logger.info(statusCode);
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
	            		loginget(uri2);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
            
        }
		return false;
    }
	
}
