package utill;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import order.OrderManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 
public class TimeUtill {
	 protected static Log logger = LogFactory.getLog(TimeUtill.class);
	
	
	public static void main(String args[]){
		//
		System.out.println(getdateString());
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

	public static Date StringToDate(String dateStr,String formatStr){
		DateFormat sdf=new SimpleDateFormat(formatStr);
		Date date=null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
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
       
    public static int getMonth(){
    	Date date = new Date();  
    	return date.getMonth() ; 
    }
    public static String  dataAdd(String data,int count){    
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String putdate =  ""; 
        try {    
        	Date date = null ;
        	if(StringUtill.isNull(data)){
        		date = new Date();
        	}else { 
        		date = sdf.parse(data);   
        	}
           
            Calendar   calendar   =   new   GregorianCalendar();   
            calendar.setTime(date);   
            calendar.add(calendar.DATE,count);//把日期往后增加一天.整数往后推,负数往前移动   
            date=calendar.getTime();   //这个时间就是日期往后推一天的结果   
            putdate = sdf.format(date); //增加一天后的日期    
        } catch (ParseException e) {  
            e.printStackTrace();   
        } 
        return putdate; 
    }
    
    public static String getsearchtime(String starttime,String endtime){
    	boolean flag = false ;
    	String str = "";
    	logger.info(starttime+endtime);
    	if(starttime != null && starttime != "" && starttime != "null"){
   		   str += " and time  BETWEEN  '" + starttime + "'  and  ";
   	       flag = true ;
   	    } 
   	 
   	 if(endtime != null && endtime != "" && endtime != "null"){
   		 if(flag){
   			str += " ' " + endtime + "'";
   		 }
   	 }else if(flag){
   		 str += "now()";
   	 } 
   	 
   	 return str ;
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
