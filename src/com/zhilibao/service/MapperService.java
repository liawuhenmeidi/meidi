package com.zhilibao.service;

import com.zhilibao.mapper.CarsOperation;
import com.zhilibao.mapper.ConfigsOperation;
import com.zhilibao.mapper.LogisticsMessageOperation;
 
public class MapperService {  
   public static CarsOperation getCarsOperation(){ 
	   CarsOperation cd = (CarsOperation)MyApplicationContext.getInstance().getBean("CarsOperation");
	       
	   return cd ;
   }
    
   public static LogisticsMessageOperation getLogisticsMessageOperation(){ 
	   LogisticsMessageOperation cd = (LogisticsMessageOperation)MyApplicationContext.getInstance().getBean("LogisticsMessageOperation");
	   return cd ;
   }
      
   public static ConfigsOperation getConfigsOperation(){ 
	   ConfigsOperation cd = (ConfigsOperation)MyApplicationContext.getInstance().getBean("ConfigsOperation");
	   return cd ;
   }
   
}
