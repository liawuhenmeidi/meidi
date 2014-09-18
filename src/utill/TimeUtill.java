package utill;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import order.OrderManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 
public class TimeUtill {
	 protected static Log logger = LogFactory.getLog(TimeUtill.class);
	
	
	public static void main(String args[]){
		//isWee_hours();
	}
	
	public static String gettime(){ 
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = new Date();   
		String submitTime= df2.format(date1);
		return submitTime;
	}
	
	public static String getdateString(){ 
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();   
		String submitTime= df2.format(date1);
		return submitTime;
	}
	
	public static String getdatesimple(){ 
		SimpleDateFormat df2 = new SimpleDateFormat("yyMMdd");
        Date date1 = new Date();   
		String submitTime= df2.format(date1);
		return submitTime;
	}
	
    public static boolean isWee_hours(int day){
    	boolean flag = false ;
    	Date date = new Date();  
        if(day != date.getDate()){
        	flag = true ;  
        }  
    	//System.out.println(date.getDate()); 
    	return flag ; 
    } 
    
    public static int getdate(){
    	Date date = new Date();  
    	return date.getDate() ;   
    } 
      
    public static boolean getLongtime(String time){
    	long days = 100; 
    	boolean flag = false ;
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();   
		String submitTime= df.format(date1); 
    	//logger.info(time);
    	//logger.info(submitTime);
    	try{ 
    	Date d1 = df.parse(time); 
    	Date d2 = df.parse(submitTime);
    	long diff =  d1.getTime() -d2.getTime() ;
    	
    	days = diff / (1000 * 60 * 60 * 24);
    	logger.info(days);
    	if(days <= 3){    
    		flag = true;
    	}   
    	//logger.info(days+""+flag);
    	}catch (Exception e)
    	{
    	  logger.info(e);
    	}
    	
    	return flag;
    }
    
    
} 
