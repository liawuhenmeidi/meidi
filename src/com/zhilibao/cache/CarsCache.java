package com.zhilibao.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.zhilibao.mapper.CarsOperation;
import com.zhilibao.model.Cars;
   
@Service 
public class CarsCache {
    public static List<Cars> list = null ;
    public static boolean flag = false ;
    @Autowired
    private CarsOperation carsOperation;
    public  Map<Integer,Cars> getmap(){
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
           
    public  void init(){     
    	if(flag || null == list){     
    		list = carsOperation.getlist() ;
    	}   
    }
}
