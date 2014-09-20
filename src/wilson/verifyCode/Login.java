package wilson.verifyCode;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.util.EntityUtils;


public class Login {
	public boolean login(URI uri,String userName,String password) {  
    	// 创建默认的httpClient实例.     	
        try{
        	
        	HttpUriRequest login = RequestBuilder.post()
            .setUri(uri)
            .addParameter("userName", userName)
            .addParameter("password", password)
            .build();
	        System.out.println("executing request " + login.getURI());  
	        CloseableHttpResponse response2 = HttpClientTest.getHttpclient().execute(login);
	        
	        try {  
	        	HttpEntity entity = response2.getEntity();
	        	System.out.println("username :" + userName + "登录");
                System.out.println("Login form get: " + response2.getStatusLine());
                if (entity != null) {  
//                    System.out.println("--------------------------------------");  
//                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));  
//                    System.out.println("--------------------------------------");  
                }  

	            EntityUtils.consume(entity);
	            
	            

	            
	            
	            //cookies
	            
                List<Cookie> cookies = HttpClientTest.getCookieStore().getCookies();
                HttpClientTest.setLoginCookies(cookies);
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
	            		System.out.println(locationURL);
	            		URL url = new URL(locationURL);
	            		URI uri2 = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
	            		this.login(uri2,userName,password);
	            	}
	            	
	            }else{
	            	return false;
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
