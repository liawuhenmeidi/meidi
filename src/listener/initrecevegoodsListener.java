package listener;

import goodsreceipt.GoodsReceitManager;
import httpClient.MyMainClient;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
   
public class initrecevegoodsListener implements ServletContextListener {
	protected static Log logger = LogFactory.getLog(GoodsReceitManager.class);
	private static Timer timer = new Timer();
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		timer.cancel();   
		  // context销毁时，销毁初始化数据 
	} 
     
	@Override 
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		  try { 
			  Calendar calendar = Calendar.getInstance();
		        int year = calendar.get(Calendar.YEAR);
		        int month = calendar.get(Calendar.MONTH);
		        int day = calendar.get(Calendar.DAY_OF_MONTH);//每天
		        //定制每天的21:09:00执行，
		        calendar.set(year, month, day, 10, 36, 00);
		        Date date = calendar.getTime(); 
		        
		        //System.out.println(date);
		               
		        int period = 1000*60*60;         
		        //每天的date时刻执行task，每隔60秒重复执行
		        timer.schedule(task, date, period);
		        //每天的date时刻执行task, 仅执行一次
		        //timer.schedule(task, date);
		  } catch (Exception e) {
		   logger.info(e);
		  }
		
	} 
	
	TimerTask task = new TimerTask() {
        @Override
        public void run() {
        	logger.info(1); 
        	MyMainClient.getinstance().run();
        }
    };
 
}
