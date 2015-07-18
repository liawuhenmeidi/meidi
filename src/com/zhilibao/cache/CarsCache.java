package com.zhilibao.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhilibao.dao.CarDao;
import com.zhilibao.model.Cars;
import com.zhilibao.service.MapperService;
import com.zhilibao.service.MyApplicationContext;
 
public class CarsCache {
    public static List<Cars> list = null ;
    public static boolean flag = false ;
           
    public static Map<Integer,Cars> getmap(){
    	init(); 
    	Map<Integer,Cars> map = new HashMap<Integer,Cars>();
    	if(null != list){ 
    		for(int i=0;i<list.size();i++){
    			Cars ca  = list.get(i);
    			map.put(ca.getId(), ca);
    		}
    	}  
    	return map;
    }   
           
    public  static void init(){     
    	if(flag || null == list){     
    		list =MapperService.getCarsOperation().getlist() ;
    	}   
    }
}
