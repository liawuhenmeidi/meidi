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

public class SelectDeliverInform {
	private String salestatus = "40";
	private String hidreturnnoticedetailid = "4292925";
	private String searchResult = "";
	
	public boolean selectDeliverInform(URI uri,String saleOrderNo){
        try {
        	HttpUriRequest selectPost = RequestBuilder.post()
            .setUri(uri)
            .addParameter("isinit", "N")
            .addParameter("deliveryinformid", "")
            .addParameter("type", "10")
            .addParameter("saleorderno", saleOrderNo)
            .addParameter("customername", "")
            .addParameter("prodcode", "")
            .addParameter("status", "")
            .addParameter("ordercreatedatebegin", "")
            .addParameter("ordercreatedateend", "")
            .addParameter("distchannel", "")
            .build();
            
            //cookies
//            System.out.println("cookies before select Post:");
//            List<Cookie> cookies = HttpClientTest.getCookieStore().getCookies();
//            if (cookies.isEmpty()) {
//                System.out.println("None");
//            } else {
//                for (int i = 0; i < cookies.size(); i++) {
//                    System.out.println("- " + cookies.get(i).toString());
//                }
//            }
            
            CloseableHttpResponse response2 = HttpClientTest.getHttpclient().execute(selectPost);
            
            try {

                HttpEntity entity2 = response2.getEntity();
                System.out.println("��ѯ ���۵��� = " + saleOrderNo);
                System.out.println("selcet form  get: " + response2.getStatusLine());
               // System.out.println(response2.getHeaders("location")[0].toString());
                if (entity2 != null) {  
                    System.out.println("--------------------------------------");  
                    String responseContent = EntityUtils.toString(entity2, "UTF-8");                        
                    
                    //��ȡ��Ϣ����..
                    responseContent = responseContent.substring(responseContent.indexOf("tbody"), responseContent.indexOf("/tbody"));
                    int location = responseContent.indexOf("hidreturnnoticedetailid\" value=\"") + "hidreturnnoticedetailid\" value=\"".length();
                    responseContent = responseContent.substring(location);
                   
                    //ȡ��Hidreturnnoticedetailid
                    this.setHidreturnnoticedetailid(responseContent.substring(0,responseContent.indexOf("\""))); 
                    
                    
                    //ȡ����,����û�õ�����,�˳�
                    try{
                    	int t = Integer.parseInt(this.getHidreturnnoticedetailid());
                    }catch(Exception e){
                    	return false;
                    }
                    
                    //�����������
                    searchResult = "";
                    for(int i = 1 ; i <= 10 ; i ++){
                    	location = responseContent.indexOf("<td class=\"detail-content-td\">") + "<td class=\"detail-content-td\">".length();      
                        responseContent = responseContent.substring(location);
                        searchResult += responseContent.substring(0,responseContent.indexOf("</td>")) + ",";
                    }
                    
                    //��ʮһ��,����״̬(salestatus����)
                    location = responseContent.indexOf("<td class=\"detail-content-td\">") + "<td class=\"detail-content-td\">".length();
                    responseContent = responseContent.substring(location);
                    searchResult += responseContent.substring(0,responseContent.indexOf("<input")) + ",";
                    
                    //��ʮ����,������ҵ
                    location = responseContent.indexOf("<td class=\"detail-content-td\">") + "<td class=\"detail-content-td\">".length();
                    responseContent = responseContent.substring(location);
                    searchResult += responseContent.substring(0,responseContent.indexOf("<input")) + ",";
                    
                    //��ʮ����,�������
                    location = responseContent.indexOf("<td class=\"detail-content-td\">") + "<td class=\"detail-content-td\">".length();      
                    responseContent = responseContent.substring(location);
                    searchResult += responseContent.substring(0,responseContent.indexOf("</td>")) + ",";
                    
                    //��ʮ����,�˿��ջ�ʱ��
                    location = responseContent.indexOf("<td class=\"detail-content-td\" id=\"receiptdate\">") + "<td class=\"detail-content-td\" id=\"receiptdate\">".length();      
                    responseContent = responseContent.substring(location);
                    searchResult += responseContent.substring(0,responseContent.indexOf("</td>")) + ",";
                    
                    //��ʮ����,��������
                    location = responseContent.indexOf("<td class=\"detail-content-td\">") + "<td class=\"detail-content-td\">".length();      
                    responseContent = responseContent.substring(location);
                    searchResult += responseContent.substring(0,responseContent.indexOf("</td>"));

                    System.out.println("--------------------------------------"); 
                    System.out.println("��ѯ��� :" + searchResult);
                    System.out.println("Hidreturnnoticedetailid = " + this.getHidreturnnoticedetailid());
                }  
                EntityUtils.consume(entity2);
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

	public String getSalestatus() {
		return salestatus;
	}

	public void setSalestatus(String salestatus) {
		this.salestatus = salestatus;
	}

	public String getHidreturnnoticedetailid() {
		return hidreturnnoticedetailid;
	}

	public void setHidreturnnoticedetailid(String hidreturnnoticedetailid) {
		this.hidreturnnoticedetailid = hidreturnnoticedetailid;
	}

	public String getSearchResult() {
		return searchResult;
	}

	public void setSearchResult(String searchResult) {
		this.searchResult = searchResult;
	}
}
