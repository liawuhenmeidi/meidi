package wilson.verifyCode;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

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


public class Login {
	
	public boolean loginpost(URI uri,String userName,String password,MainClient mc) {  
    	// 创建默认的httpClient实例.     	
        try{
        	String uuid = UUID.randomUUID().toString();
        	HttpUriRequest login = RequestBuilder.post()
            .setUri(uri)
            .addParameter("service", "https://scs.suning.com/sps/auth?targetUrl=http%3A%2F%2Fscs.suning.com%2Fsps%2Fmember%2Flogon.do")
            .addParameter("uuid", uuid)
            .addParameter("loginTheme", "scs")
            .addParameter("username", userName)
            .addParameter("password", password)
            .build();
	       // System.out.println("executing request " + login.getURI());  
	        CloseableHttpResponse response2 = mc.getHttpclient().execute(login);
	        
	        try {  
	        	HttpEntity entity = response2.getEntity();
	        	//System.out.println("username :" + userName + "登录");
              //  System.out.println("Login form get: " + response2.getStatusLine());
                if (entity != null) {  
//                    System.out.println("--------------------------------------");  
//                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));  
//                    System.out.println("--------------------------------------");  
                }  

	            EntityUtils.consume(entity);
	            
	            

	            
	            
	            //cookies
	            
//                List<Cookie> cookies = MainClient.getCookieStore().getCookies();
//                MainClient.setLoginCookies(cookies);
                
                
//                System.out.println("Post logon cookies:");
//                if (cookies.isEmpty()) {
//                    System.out.println("None");
//                } else {
//                    for (int i = 0; i < cookies.size(); i++) {
//                        System.out.println("- " + cookies.get(i).toString());
//                    }
//                }
                
                
                //301,302转向
                int statusCode = response2.getStatusLine().getStatusCode();
	            if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || 
	            		statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
	            	
	            	if(response2.getHeaders("location").length > 0){
	            		String locationURL = response2.getHeaders("location")[0].toString();
	            		locationURL = locationURL.substring(10);
	            	//	System.out.println(locationURL);
	            		//URL url = new URL(locationURL);
	            		URI uri3 = new URI(locationURL);
	            		//URI uri2 = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
	            		this.loginget(uri3,userName,password,mc);
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
	
	public boolean loginget(URI uri,String userName,String password,MainClient mc) {  
    	// 创建默认的httpClient实例.     	
        try{
        	String uuid = UUID.randomUUID().toString();
        	HttpUriRequest login = RequestBuilder.post()
            .setUri(uri)
            .addParameter("service", "https://scs.suning.com/sps/auth?targetUrl=http%3A%2F%2Fscs.suning.com%2Fsps%2Fmember%2Flogon.do")
            .addParameter("uuid", uuid)
            .addParameter("loginTheme", "scs")
            .addParameter("username", userName)
            .addParameter("password", password)
            .build();
	      //  System.out.println("executing request " + login.getURI());  
	        CloseableHttpResponse response2 = mc.getHttpclient().execute(login);
	        
	        try {  
	        	HttpEntity entity = response2.getEntity();

           //     System.out.println("Login form get: " + response2.getStatusLine());
                if (entity != null) {  
//                    System.out.println("--------------------------------------");  
//                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));  
//                    System.out.println("--------------------------------------");  
                }  

	            EntityUtils.consume(entity);
	            
	            

	            
	            
	            //cookies
	            
//                List<Cookie> cookies = MainClient.getCookieStore().getCookies();
//                MainClient.setLoginCookies(cookies);
                
                
//                System.out.println("Post logon cookies:");
//                if (cookies.isEmpty()) {
//                    System.out.println("None");
//                } else {
//                    for (int i = 0; i < cookies.size(); i++) {
//                        System.out.println("- " + cookies.get(i).toString());
//                    }
//                }
                
                
                //301,302转向
                int statusCode = response2.getStatusLine().getStatusCode();
	            if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || 
	            		statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
	            	
	            	if(response2.getHeaders("location").length > 0){
	            		String locationURL = response2.getHeaders("location")[0].toString();
	            		locationURL = locationURL.substring(10);
	            //		System.out.println(locationURL);
	            		URL url = new URL(locationURL);
	            		URI uri2 = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
	            		this.loginget(uri2,userName,password,mc);
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
