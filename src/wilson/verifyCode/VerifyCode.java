package wilson.verifyCode;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.util.EntityUtils;

public class VerifyCode {
	int FLASE = 1;
	int TRUE = 0;
	int OTHER = 2;
	public int VerifyCode(URI uri,String code,String omsOrderItemNo){
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
            
            CloseableHttpResponse response2 = MainClient.getHttpclient().execute(verifyCodePost);
            
            try {

                HttpEntity entity2 = response2.getEntity();
                
                //System.out.println("selcet form  get: " + response2.getStatusLine());
                if (entity2 != null) {  
                	verifyCodeResult = EntityUtils.toString(entity2, "UTF-8");
//                    System.out.println("--------------------------------------");  
//                    System.out.println("Response content: " + " code " + code + ":" + verifyCodeResult);  
//                    System.out.println("--------------------------------------");  
                }  
                EntityUtils.consume(entity2);
            } finally {
                response2.close();
            }
            
            
            if(verifyCodeResult.equals("success")){
            	return 0;
            }else if(verifyCodeResult.equals("false")){
            	return 1;
            }else{
            	return 2;
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
