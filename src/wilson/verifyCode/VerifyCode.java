package wilson.verifyCode;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.util.EntityUtils;

public class VerifyCode {
	public static final int TRUE = 0;
	public static final int FALSE = 1;
	public static final int RETRY = 2;
	public static final int RETRYLONG = 3;
	public static final int OTHER = 3;
	 
	

	public int VerifyCode(URI uri,String code,String omsOrderItemNo,MainClient mc){
        try {
        	HttpUriRequest verifyCodePost = RequestBuilder.post()
            .setUri(uri)
            .addParameter("omsOrderItemNo", omsOrderItemNo)
            .addParameter("iunik", code)
            .build();
            
        	String verifyCodeResult = "null";
        	
            //cookies
//            System.out.println("cookies before verifyCode post:");
//            List<Cookie> cookies = HttpClientTest.getCookieStore().getCookies();
//            if (cookies.isEmpty()) {
//                System.out.println("None");
//            } else {
//                for (int i = 0; i < cookies.size(); i++) {
//                    System.out.println("- " + cookies.get(i).toString());
//                }
//            }
            
            CloseableHttpResponse response2 = mc.getHttpclient().execute(verifyCodePost);
            
            try {

                HttpEntity entity2 = response2.getEntity();
                
                //System.out.println("selcet form  get: " + response2.getStatusLine());
                if (entity2 != null) {  
                	verifyCodeResult = EntityUtils.toString(entity2, "UTF-8");
//                    System.out.println("--------------------------------------");  
//                    System.out.println("Response conte	nt: " + " code " + code + ":" + verifyCodeResult);  
//                    System.out.println("--------------------------------------");  
                }  
                EntityUtils.consume(entity2);
                
                
            } finally {
                response2.close();
            }
            
            System.out.println(code + ":" + verifyCodeResult);
            if(verifyCodeResult.equals("success")){
            	return TRUE;
            }else if(verifyCodeResult.contains("校验码输入错误")){
            	return FALSE;
            }else if(verifyCodeResult.contains("nginx") && verifyCodeResult.contains("302")){
            	return RETRY;
            }else{
            	return RETRYLONG;
            }
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
        return 2;
	}
}
