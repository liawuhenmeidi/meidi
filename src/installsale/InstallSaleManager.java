package installsale;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import utill.DBUtill;

import company.CompanyManager;

public class InstallSaleManager {
	 protected static Log logger = LogFactory.getLog(CompanyManager.class);

	 public static boolean  save(InstallSale  in){
		String sql = "insert into installSale(id,uid,uname,phone,locate,andate,message) values (null,"+in.getUid()+","+in.getUname()+","+in.getPhone()+","+in.getLocate()+","+in.getAndate()+","+in.getMessage()+")";
		return DBUtill.sava(sql); 
	 }
}
