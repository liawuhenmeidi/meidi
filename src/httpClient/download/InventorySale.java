package httpClient.download;

import goodsreceipt.GoodsReceipt;
import goodsreceipt.GoodsReceitManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import utill.StringUtill;
import utill.TimeUtill;

public class InventorySale {
	protected static Log logger = LogFactory.getLog(InventorySale.class);
	
	public static List<SNInventory> compare(String starttime, String endtime) {
		List<SNInventory> list = new ArrayList<SNInventory>();
        
		if(StringUtill.isNull(starttime)){
        	return list;
        } 
        if(StringUtill.isNull(endtime)){
        	endtime = TimeUtill.getdateString();
        }
         
		List<SNInventory> listIn = InventoryChange.compare(starttime,endtime); 
		      
		logger.info(StringUtill.GetJson(listIn));  
		logger.info(listIn.size());
		  
		Map<String,SNInventory> mapsale = SaleDownLoad.getMap(starttime,endtime); 
		//logger.info(mapsale);
		logger.info( mapsale.size());
		  
		if(!listIn.isEmpty()){
			Iterator<SNInventory> it = listIn.iterator();
			while(it.hasNext()){
				SNInventory in = it.next();
				
				/*if(StringUtill.isNull(StringUtill.getStringNocn(in.getBranchName()))){
					logger.info(in.getBranchName()); 
				} */ 
				String bnum = "";
                if("常规机库".equals(in.getBranchName())){
                	 bnum = "1";
                }else if("特价机库".equals(in.getBranchName())){
                	 bnum = "2";
               }else {
            	   bnum = StringUtill.getStringNocn(in.getBranchName());
               } 
                 
                 
				String key = bnum+"_"+in.getGoodNum(); 
				// logger.info(key);  
				SNInventory insale= mapsale.get(key);
				 
				if(null != insale){
					mapsale.remove(key); 
					//logger.info(insale.getSaleNum()+"_"+in.getInreduce());
					if(in.getInreduce() != insale.getSaleNum()){
						in.setSaleNum(insale.getSaleNum());
						list.add(in); 
					}
				}else {
					if(in.getInreduce() != 0 ){
						in.setSaleNum(0);
						list.add(in);  
					}
					
				}
				
				it.remove();
			}
		}
		
		logger.info("list" + list.size());
		 
		logger.info("listin" + listIn.size());
		
		logger.info("mapsale" +mapsale.size());
		 
		 
		if(null != mapsale){  
			Set<Map.Entry<String, SNInventory>> set = mapsale.entrySet();
			 Iterator<Map.Entry<String, SNInventory>> it = set.iterator();
			 while(it.hasNext()){
				 Map.Entry<String, SNInventory> mapent = it.next();
				 SNInventory in = mapent.getValue(); 
				 //logger.info(in.getGoodpName()+in.getBranchName());
				 //in.setSaleNum(in.getInreduce());
				 if(in.getSaleNum() != 0 ){
					 in.setInreduce(0); 
					 list.add(in);
				 }
				 
			 } 
		} 
		logger.info("list" + list.size());
			 
		return list;
	}

	public static Map<String, SNInventory> changeMap(List<SNInventory> list) {
		Map<String, SNInventory> map = new HashMap<String, SNInventory>();
		if (null != list) {
			for (int i = 0; i < list.size(); i++) {
				SNInventory in = list.get(i);
				map.put(in.getBranchNum() + "_" + in.getGoodNum(), in);
			}
		}
		logger.info(map.size());
		return map;
	}
	
}
