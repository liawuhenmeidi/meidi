package httpClient;

import httpClient.download.InventoryBadGoodsDownLoad;
import httpClient.download.InventoryChange;
import httpClient.download.InventoryModelDownLoad;
import httpClient.download.SNInventory;
import httpClient.download.SaleDownLoad;
import inventory.InventoryAllManager;
import inventory.InventoryBranchManager;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import makeInventory.MakeInventory;
import makeInventory.MakeInventoryManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import product.Product;
import product.ProductService;

import branch.Branch;
import branch.BranchService;

import user.User;
import utill.StringUtill;
import utill.TimeUtill;
  
public class InventoryMerger {
	
	protected static Log logger = LogFactory
			.getLog(InventoryMerger.class);
   
	// 型号   门店 
    public static Map<String,Map<String,SNInventory>> get(User user ,String branch,String category,String starttime,String endtime,String[] typestatues){
    	 
    	String branchnum = BranchService.getNameMap().get(branch).getEncoded();   
    	// 型号   门店  
    	Map<String,Map<String,SNInventory>> map = new HashMap<String,Map<String,SNInventory>>();
    	   try{      
    	// 苏宁 库存       
    	Collection<SNInventory> coc = InventoryChange.get(TimeUtill.dataAdd(endtime, 1));
        // 苏宁样机 
    	Collection<SNInventory> com =InventoryModelDownLoad.getMap(user, TimeUtill.dataAdd(endtime, 1)).values(); 
        // 苏宁坏机 
        Collection<SNInventory> cob = InventoryBadGoodsDownLoad.getMap(user, TimeUtill.dataAdd(endtime, 1)).values();
          
        List<SNInventory> cos = null ;
        if(!StringUtill.isNull(starttime)){
        	 cos = SaleDownLoad.get(starttime, endtime); 
        }
 
        // 销量  
       // logger.info(123); 
        Collection<SNInventory> coin = InventoryAllManager.get(user,category, branch, endtime).values();
           
        //logger.info(coin);   
        logger.info(" branchnum"+ branchnum);
        
        
        if(null != coc){  
        	      
        	Iterator<SNInventory> it = coc.iterator();
        	while(it.hasNext()){ 
        		SNInventory sn = it.next(); 
        		 String tnum = sn.getGoodNum();
        		 String bnum = StringUtill.getStringNocn(sn.getBranchName()); 
        		 //logger.info(bnum); 
        		 if(bnum.equals(branchnum)){  
        			 Map<String,SNInventory> mapt = map.get(tnum);
             		if(null == mapt){
             			mapt = new HashMap<String,SNInventory>(); 
             			map.put(tnum, mapt);
             		} 
             		      
             		SNInventory snm = mapt.get(bnum);
             		  
             		if(null == snm){
             			mapt.put(bnum, sn);
             		}else {   
             			snm.setNum(snm.getNum()+sn.getNum()); 
             		} 
        		 }
        	}
        	
        }
         
        
       if(null != com){  
  	      
        	Iterator<SNInventory> it = com.iterator();
        	//int count = 0 ;
        	while(it.hasNext()){
        		SNInventory sn = it.next(); 
        		 String tnum = sn.getGoodNum();
        		 String bnum = sn.getBranchNum();  
        		  
        		 if(bnum.equals(branchnum)){
        			// count ++; 
        			// logger.info(bnum);  
        			 Map<String,SNInventory> mapt = map.get(tnum);
             		if(null == mapt){
             			mapt = new HashMap<String,SNInventory>(); 
             			map.put(tnum, mapt);
             		} 
             		      
             		SNInventory snm = mapt.get(bnum);
             		   
             		if(null == snm){
             			//logger.info(sn.getModelnum());
             			mapt.put(bnum, sn); 
             		}else { 
             			//logger.info(snm.getModelnum());
             			//logger.info(sn.getModelnum());
             			snm.setModelnum(snm.getModelnum()+sn.getModelnum()); 
             		} 
        		 } 
        	}
           // logger.info(count);  
        } 
   
        if(null != cob){  
    	      
        	Iterator<SNInventory> it = cob.iterator();
        	
        	while(it.hasNext()){
        		SNInventory sn = it.next();  
        		 String tnum = sn.getGoodNum();
        		 String bnum = sn.getBranchNum();  
        		   
        		 if(bnum.equals(branchnum)){
        			// count ++; 
        			// logger.info(bnum);  
        			 Map<String,SNInventory> mapt = map.get(tnum);
             		if(null == mapt){
             			mapt = new HashMap<String,SNInventory>(); 
             			map.put(tnum, mapt);
             		} 
             		       
             		SNInventory snm = mapt.get(bnum);
             		   
             		if(null == snm){
             			//logger.info(sn.getModelnum());
             			mapt.put(bnum, sn); 
             		}else { 
             			//logger.info(snm.getModelnum());
             			//logger.info(sn.getModelnum());
             			snm.setBadnum(snm.getBadnum()+sn.getBadnum());
             		}  
        		 } 
        	}
            //logger.info(count);  
        }
        
        if(null != cos){  
  	        
        	Iterator<SNInventory> it = cos.iterator();
        	//int count = 0 ; 
        	while(it.hasNext()){ 
        		 SNInventory sn = it.next();        
        		 String tnum = sn.getGoodNum();       
        		 String bnum = sn.getBranchNum();   
        		//logger.info(bnum);       
        		 if(bnum.equals(branchnum)){ 
        			// count ++; 
        			// logger.info(bnum);  
        			 Map<String,SNInventory> mapt = map.get(tnum);
             		if(null == mapt){
             			mapt = new HashMap<String,SNInventory>(); 
             			map.put(tnum, mapt);
             		} 
             		      
             		SNInventory snm = mapt.get(bnum);
             		   
             		if(null == snm){
             			//logger.info(sn.getModelnum());
             			mapt.put(bnum, sn); 
             		}else {  
             			//logger.info(snm.getModelnum());
             			//logger.info(sn.getModelnum());
             			snm.setSaleNum(snm.getSaleNum()+sn.getSaleNum());
             			//snm.setBadnum(snm.getBadnum()+sn.getBadnum());
             		}  
        		 } 
        	}
           // logger.info(count);  
        }
        
          if(null != coin){  
  	         
        	Iterator<SNInventory> it = coin.iterator();
        	//int count = 0 ;   
        	while(it.hasNext()){   
        		 SNInventory sn = it.next();        
        		 String tnum = sn.getProduct().getEncoded();       
        		 String bnum = BranchService.getMap().get(sn.getBranchid()).getEncoded();
        		 //logger.info(bnum+"**"+tnum);           
        		 if(bnum.equals(branchnum)){ 
        			// count ++;  
        			// logger.info(bnum);  
        			 Map<String,SNInventory> mapt = map.get(tnum);
             		if(null == mapt){
             			mapt = new HashMap<String,SNInventory>(); 
             			map.put(tnum, mapt);
             		} 
             		       
             		SNInventory snm = mapt.get(bnum);
             		     
             		if(null == snm){ 
             			//logger.info(sn.getModelnum());
             			mapt.put(bnum, sn);  
             			//Set<Integer> set = sn.getMap().keySet();
             			 
             		}else {      
             			//   logger.info(typestatues); 
             			/*if(null == typestatues){ 
             				snm.setQuerymonth(sn.getQuerymonth());   
             			}else { 
             				for(int i=0;i<typestatues.length;i++){ 
             					logger.info(Integer.valueOf(typestatues[i]));
             					logger.info(snm.getTypeStatues());
             					if(snm.getTypeStatues() == 0){
             						//if(Integer.valueOf(typestatues[i]) ==2 || Integer.valueOf(typestatues[i]) ==1){
             							snm.setQuerymonth(sn.getQuerymonth());   
             						//}  
             					}else {
             						if(snm.getTypeStatues() == Integer.valueOf(typestatues[i])){
                     					snm.setQuerymonth(sn.getQuerymonth());   
                     				} 
             					}
                 				
                 			} 
             			}*/ 
             			
             		     
             			snm.setMap(sn.getMap());
             			//logger.info(snm.getModelnum());
             			//logger.info(sn.getModelnum());
             			snm.setIncommonnum(snm.getIncommonnum()+sn.getIncommonnum());
             			snm.setInmodelnum(snm.getInmodelnum()+sn.getInmodelnum());
             		//	snm.setSaleNum(snm.getSaleNum()+sn.getSaleNum()); 
             		}   
        		 } 
        	}
           // logger.info(count);  
        } 

    	   }catch(Exception e){
    		   logger.info(e);
    	   } 
         
    	return map;
    }
     
  public static Map<String,Map<String,SNInventory>> getMap(User user ,String branch,String category,String starttime,String endtime,String[] typestatues,String str){
    	 
    	String branchnum = BranchService.getNameMap().get(branch).getEncoded();
    	int bid = BranchService.getNameMap().get(branch).getId(); 
    	// 型号   门店   
    	Map<String,Map<String,SNInventory>> map = new HashMap<String,Map<String,SNInventory>>();
    	   try{      
    	// 苏宁 库存        
    	Collection<SNInventory> coc = InventoryChange.get(TimeUtill.dataAdd(endtime, 1));
        // 苏宁样机 
    	Collection<SNInventory> com =InventoryModelDownLoad.getMap(user, TimeUtill.dataAdd(endtime, 1)).values(); 
        // 苏宁坏机 
        Collection<SNInventory> cob = InventoryBadGoodsDownLoad.getMap(user, TimeUtill.dataAdd(endtime, 1)).values();
          
        List<SNInventory> cos = null ;
        if(!StringUtill.isNull(starttime)){
        	 cos = SaleDownLoad.get(starttime, endtime); 
        }

        // 销量  
       // logger.info(123); 
        Collection<SNInventory> coin = InventoryAllManager.get(user,category, branch, endtime).values();
        
        List<SNInventory> listup = MakeInventoryManager.get(user, str);
         
       // logger.info(listup.size()); 
       // logger.info(mapmi); 
        //logger.info(coin);   
        logger.info(" branchnum"+ branchnum);
        
        
        if(null != coc){  
        	      
        	Iterator<SNInventory> it = coc.iterator();
        	while(it.hasNext()){ 
        		SNInventory sn = it.next(); 
        		 String tnum = sn.getGoodNum();
        		 String bnum = StringUtill.getStringNocn(sn.getBranchName()); 
        		 //logger.info(bnum); 
        		 if(bnum.equals(branchnum)){  
        			 Map<String,SNInventory> mapt = map.get(tnum);
             		if(null == mapt){
             			mapt = new HashMap<String,SNInventory>(); 
             			map.put(tnum, mapt);
             		} 
             		      
             		SNInventory snm = mapt.get(bnum);
             		  
             		if(null == snm){
             			mapt.put(bnum, sn);
             		}else {   
             			snm.setNum(snm.getNum()+sn.getNum()); 
             		} 
        		 }
        	}
        	
        }
         
        
       if(null != com){  
  	      
        	Iterator<SNInventory> it = com.iterator();
        	//int count = 0 ;
        	while(it.hasNext()){
        		SNInventory sn = it.next(); 
        		 String tnum = sn.getGoodNum();
        		 String bnum = sn.getBranchNum();  
        		  
        		 if(bnum.equals(branchnum)){
        			// count ++; 
        			// logger.info(bnum);  
        			 Map<String,SNInventory> mapt = map.get(tnum);
             		if(null == mapt){
             			mapt = new HashMap<String,SNInventory>(); 
             			map.put(tnum, mapt);
             		} 
             		      
             		SNInventory snm = mapt.get(bnum);
             		   
             		if(null == snm){
             			//logger.info(sn.getModelnum());
             			mapt.put(bnum, sn); 
             		}else { 
             			//logger.info(snm.getModelnum());
             			//logger.info(sn.getModelnum());
             			snm.setModelnum(snm.getModelnum()+sn.getModelnum()); 
             		} 
        		 } 
        	}
           // logger.info(count);  
        } 
   
        if(null != cob){  
    	      
        	Iterator<SNInventory> it = cob.iterator();
        	
        	while(it.hasNext()){
        		SNInventory sn = it.next();  
        		 String tnum = sn.getGoodNum();
        		 String bnum = sn.getBranchNum();  
        		   
        		 if(bnum.equals(branchnum)){
        			// count ++; 
        			// logger.info(bnum);  
        			 Map<String,SNInventory> mapt = map.get(tnum);
             		if(null == mapt){
             			mapt = new HashMap<String,SNInventory>(); 
             			map.put(tnum, mapt);
             		} 
             		       
             		SNInventory snm = mapt.get(bnum);
             		   
             		if(null == snm){
             			//logger.info(sn.getModelnum());
             			mapt.put(bnum, sn); 
             		}else { 
             			//logger.info(snm.getModelnum());
             			//logger.info(sn.getModelnum());
             			snm.setBadnum(snm.getBadnum()+sn.getBadnum());
             		}   
        		 } 
        	}
            //logger.info(count);  
        }
        
        if(null != cos){  
  	        
        	Iterator<SNInventory> it = cos.iterator();
        	//int count = 0 ; 
        	while(it.hasNext()){ 
        		 SNInventory sn = it.next();        
        		 String tnum = sn.getGoodNum();       
        		 String bnum = sn.getBranchNum();   
        		//logger.info(bnum);       
        		 if(bnum.equals(branchnum)){ 
        			// count ++; 
        			// logger.info(bnum);  
        			 Map<String,SNInventory> mapt = map.get(tnum);
             		if(null == mapt){
             			mapt = new HashMap<String,SNInventory>(); 
             			map.put(tnum, mapt);
             		} 
             		      
             		SNInventory snm = mapt.get(bnum);
             		   
             		if(null == snm){
             			//logger.info(sn.getModelnum());
             			mapt.put(bnum, sn); 
             		}else {  
             			//logger.info(snm.getModelnum());
             			//logger.info(sn.getModelnum());
             			snm.setSaleNum(snm.getSaleNum()+sn.getSaleNum());
             			//snm.setBadnum(snm.getBadnum()+sn.getBadnum());
             		}  
        		 } 
        	}
           // logger.info(count);  
        }
        
          if(null != coin){  
  	           
        	Iterator<SNInventory> it = coin.iterator();
        	//int count = 0 ;   
        	while(it.hasNext()){   
        		 SNInventory sn = it.next();        
        		 String tnum = sn.getProduct().getEncoded();       
        		 String bnum = BranchService.getMap().get(sn.getBranchid()).getEncoded();
        		// logger.info("tnum+**"+tnum+"&&&&&&bnum "+bnum);         
        		 if(bnum.equals(branchnum)){ 
        			// count ++;   
        			// logger.info(bnum);  
        			 Map<String,SNInventory> mapt = map.get(tnum);
             		if(null == mapt){ 
             			mapt = new HashMap<String,SNInventory>(); 
             			map.put(tnum, mapt);
             		} 
             		       
             		SNInventory snm = mapt.get(bnum);
             		     
             		if(null == snm){ 
             			//logger.info(sn.getModelnum());
             			mapt.put(bnum, sn);  
             			//Set<Integer> set = sn.getMap().keySet();
             			 
             		}else {      
             			//   logger.info(typestatues); 
             			/*if(null == typestatues){ 
             				snm.setQuerymonth(sn.getQuerymonth());   
             			}else { 
             				for(int i=0;i<typestatues.length;i++){ 
             					logger.info(Integer.valueOf(typestatues[i]));
             					logger.info(snm.getTypeStatues());
             					if(snm.getTypeStatues() == 0){
             						//if(Integer.valueOf(typestatues[i]) ==2 || Integer.valueOf(typestatues[i]) ==1){
             							snm.setQuerymonth(sn.getQuerymonth());   
             						//}  
             					}else {
             						if(snm.getTypeStatues() == Integer.valueOf(typestatues[i])){
                     					snm.setQuerymonth(sn.getQuerymonth());   
                     				} 
             					}
                 				
                 			} 
             			}*/ 
             			
             		     
             			snm.setMap(sn.getMap());
             			//logger.info(snm.getModelnum());
             			//logger.info(sn.getModelnum());
             			snm.setIncommonnum(snm.getIncommonnum()+sn.getIncommonnum());
             			snm.setInmodelnum(snm.getInmodelnum()+sn.getInmodelnum());
             		//	snm.setSaleNum(snm.getSaleNum()+sn.getSaleNum()); 
             		}   
        		 } 
        	}
           // logger.info(count);  
        } 
          
          //logger.info(listup);
          if(null != listup){
        	  Iterator<SNInventory> it =  listup.iterator();
        	  while(it.hasNext()){
        		  SNInventory sn =  it.next();
        		  Branch b = BranchService.getMap().get(sn.getBranchid());
        		  Product p = ProductService.getIDmap().get(Integer.valueOf(sn.getTypeid()));
        		//logger.info("tnum+**"+p.getEncoded()+"&&&&&&bnum"+b.getEncoded());         
        		  Map<String,SNInventory> mapt = map.get(p.getEncoded()); 
           		if(null == mapt){
           			mapt = new HashMap<String,SNInventory>(); 
           			map.put(p.getEncoded(), mapt);
           		} 
           		         
           		SNInventory snm = mapt.get(b.getEncoded());
           		if(null == snm){     
           			logger.info("add");
           			//logger.info(StringUtill.GetJson(sn)); 
           			mapt.put(b.getEncoded(), sn);
           		}else {  
           			logger.info("update");  
           			logger.info(sn.getFlagupm()); 
           		// if(sn.getFlagupm() == 1){
           			 // snm.setIsupm(false);
           			 snm.setFlagupm(sn.getFlagupm());
           			 snm.setUpmodel(sn.getUpmodel());
           			 logger.info("updatem"); 
           			/*int flag = 3;   
           			//logger.info(p.getType()+"upmodel::"+sn.getUpmodel()+"::Modelnum::"+snm.getModelnum()+"::Outmodelnum"+snm.getOutmodelnum()); 
           			   
           			if(sn.getUpmodel() == (snm.getModelnum()+snm.getInmodelnum())){  
           				 flag = 2 ;  
           				// logger.info(p.getType());
           			 }     
           			 snm.setFlagupm(flag);*/
           		// }else if(sn.getFlagupin() == 1){
           			 logger.info("upin");
           			 snm.setFlagupin(sn.getFlagupin());
           			 snm.setUpin(sn.getUpin()); 
           			/* snm.setIsin(false);
           			int flag = 3 ; 
           			 
           			logger.info("upin::"+sn.getUpin()+":::num"+snm.getNum());
           			 if(sn.getUpin() == snm.getNum()){
           				flag = 2 ;
         				   
           			 } 
           			snm.setFlagupin(flag);*/
           		// }else if(sn.getFlagupout() == 1){
           			 snm.setFlagupout(sn.getFlagupout()); 
           		    snm.setUpout(sn.getUpout()); 
           		   /* snm.setIsout(false);  
           			int flag = 3;
           			 if(sn.getUpout() == snm.getIncommonnum()){
           				flag =2 ;
           			 }  
           			snm.setFlagupout(flag);  */
           		// }
           		  
           		 snm.setFlag(sn.getFlag());
        	  }
        	  }
        	  
          }
          
          
    	   }catch(Exception e){
    		   logger.info(e);
    	   } 
         
    	return map;
    }
  
 public static Map<String,SNInventory> getBranch(User user ,String category,String branchtype,String type,String starttime,String endtime){
    String pnum = "";
    if(!StringUtill.isNull(type)){
    	pnum = ProductService.gettypemap(user, Integer.valueOf(branchtype)).get(type).getEncoded();
    }
 
    	// 型号   门店   
    	Map<String,SNInventory> map = new HashMap<String,SNInventory>();
    	   try{      
    	// 苏宁 库存       
    	Collection<SNInventory> coc = InventoryChange.get(TimeUtill.dataAdd(endtime, 1));
        // 苏宁样机 
    	Collection<SNInventory> com =InventoryModelDownLoad.getMap(user, TimeUtill.dataAdd(endtime, 1)).values(); 
        // 苏宁坏机 
        Collection<SNInventory> cob = InventoryBadGoodsDownLoad.getMap(user, TimeUtill.dataAdd(endtime, 1)).values();
         
        List<SNInventory> cos = null ;
        if(!StringUtill.isNull(starttime)){
        	 cos = SaleDownLoad.get(starttime, endtime); 
        }
         
        // 销量  
       // logger.info(123); 
        Collection<SNInventory> coin = InventoryAllManager.get(user,category, "", endtime).values();
         
        //logger.info(coin);  
        
        
        if(null != coc){  
        	      
        	Iterator<SNInventory> it = coc.iterator();
        	while(it.hasNext()){  
        		SNInventory sn = it.next(); 
        		 String tnum = sn.getGoodNum();
        		 if(StringUtill.isNull(pnum) || tnum.equals(pnum)){
        			 String bnum = StringUtill.getStringNocn(sn.getBranchName()); 

              		SNInventory snm = map.get(bnum);
              		  
              		if(null == snm){
              			map.put(bnum, sn);
              		}else {    
              			snm.setNum(snm.getNum()+sn.getNum()); 
              		} 
        		 }
        		 
        	}
        	
        }
         
        
       if(null != com){  
  	      
        	Iterator<SNInventory> it = com.iterator();
        	//int count = 0 ;
        	while(it.hasNext()){
        		SNInventory sn = it.next(); 
        		 String tnum = sn.getGoodNum();
        		 String bnum = sn.getBranchNum();  
        		  
        		 if(StringUtill.isNull(pnum) || tnum.equals(pnum)){
             		SNInventory snm = map.get(bnum);
             		   
             		if(null == snm){
             			//logger.info(sn.getModelnum());
             			map.put(bnum, sn); 
             		}else { 
             			//logger.info(snm.getModelnum());
             			//logger.info(sn.getModelnum());
             			snm.setModelnum(snm.getModelnum()+sn.getModelnum()); 
        		 } 
        		 }
        	}
           // logger.info(count);  
        } 
   
        if(null != cob){  
    	      
        	Iterator<SNInventory> it = cob.iterator();
        	
        	while(it.hasNext()){
        		SNInventory sn = it.next();  
        		 String tnum = sn.getGoodNum();
        		 String bnum = sn.getBranchNum();  
        		   
        		
        			// count ++; 
        			// logger.info(bnum);  
        			
        		 if(StringUtill.isNull(pnum) || tnum.equals(pnum)){      
             		SNInventory snm = map.get(bnum);
             		   
             		if(null == snm){
             			//logger.info(sn.getModelnum());
             			map.put(bnum, sn); 
             		}else { 
             			//logger.info(snm.getModelnum());
             			//logger.info(sn.getModelnum());
             			snm.setBadnum(snm.getBadnum()+sn.getBadnum()); 
        		 } 
        	}
        	}
            //logger.info(count);  
        }
        
        if(null != cos){  
  	        
        	Iterator<SNInventory> it = cos.iterator();
        	//int count = 0 ; 
        	while(it.hasNext()){ 
        		 SNInventory sn = it.next();        
        		 String tnum = sn.getGoodNum();       
        		 String bnum = sn.getBranchNum();   
        		//logger.info(bnum);       
        		 if(StringUtill.isNull(pnum) || tnum.equals(pnum)){
        			
             		SNInventory snm = map.get(bnum);
             		   
             		if(null == snm){
             			//logger.info(sn.getModelnum());
             			map.put(bnum, sn); 
             		}else {  
             			//logger.info(snm.getModelnum());
             			//logger.info(sn.getModelnum());
             			snm.setSaleNum(snm.getSaleNum()+sn.getSaleNum());
             			//snm.setBadnum(snm.getBadnum()+sn.getBadnum());
             		}  
        		 }
        	}
           // logger.info(count);  
        }
        
          if(null != coin){  
  	         
        	Iterator<SNInventory> it = coin.iterator();
        	//int count = 0 ;   
        	while(it.hasNext()){   
        		 SNInventory sn = it.next();        
        		 String tnum = sn.getProduct().getEncoded();       
        		 String bnum = BranchService.getMap().get(sn.getBranchid()).getEncoded();
        		 //logger.info(bnum+"**"+tnum);           
        		
        		 if(StringUtill.isNull(pnum) || tnum.equals(pnum)){  
             		SNInventory snm = map.get(bnum);
             		    
             		if(null == snm){
             			//logger.info(sn.getModelnum());
             			map.put(bnum, sn); 
             		}else { 
             			//logger.info(snm.getModelnum());
             			//logger.info(sn.getModelnum()); 
             			snm.setIncommonnum(snm.getIncommonnum()+sn.getIncommonnum());
             			snm.setInmodelnum(snm.getInmodelnum()+sn.getInmodelnum());
             		//	snm.setSaleNum(snm.getSaleNum()+sn.getSaleNum()); 
             		}   
        		 }
        	}
           // logger.info(count);  
        } 

    	   }catch(Exception e){
    		   logger.info(e);
    	   } 
         
    	return map;
    }
 
 public static Map<String,SNInventory> getType(User user ,String category,String branchtype,String type,String starttime,String endtime){
	    
	    String pnum = "";
	    if(!StringUtill.isNull(type)){
	    	pnum = ProductService.gettypemap(user, Integer.valueOf(branchtype)).get(type).getEncoded();
	    }
	  
	    	// 型号   门店   
	    	Map<String,SNInventory> map = new HashMap<String,SNInventory>();
	    	   try{      
	    	// 苏宁 库存       
	    	Collection<SNInventory> coc = InventoryChange.get(TimeUtill.dataAdd(endtime, 1));
	        // 苏宁样机 
	    	Collection<SNInventory> com =InventoryModelDownLoad.getMap(user, TimeUtill.dataAdd(endtime, 1)).values(); 
	        // 苏宁坏机 
	        Collection<SNInventory> cob = InventoryBadGoodsDownLoad.getMap(user, TimeUtill.dataAdd(endtime, 1)).values();
	         
	        List<SNInventory> cos = null ;
	        if(!StringUtill.isNull(starttime)){
	        	 cos = SaleDownLoad.get(starttime, endtime); 
	        }
	         
	        // 销量  
	       // logger.info(123); 
	        Collection<SNInventory> coin = InventoryAllManager.get(user,category, "", endtime).values();
	         
	        //logger.info(coin);  
	        
	        
	        if(null != coc){  
	        	      
	        	Iterator<SNInventory> it = coc.iterator();
	        	while(it.hasNext()){  
	        		SNInventory sn = it.next(); 
	        		 String tnum = sn.getGoodNum();
	        		 if(StringUtill.isNull(pnum) || tnum.equals(pnum)){
	        			 String bnum = StringUtill.getStringNocn(sn.getBranchName()); 

	              		SNInventory snm = map.get(tnum);
	              		  
	              		if(null == snm){
	              			map.put(tnum, sn);
	              		}else {    
	              			snm.setNum(snm.getNum()+sn.getNum()); 
	              		} 
	        		 }
	        		 
	        	}
	        	
	        }
	         
	        
	       if(null != com){  
	  	      
	        	Iterator<SNInventory> it = com.iterator();
	        	//int count = 0 ;
	        	while(it.hasNext()){
	        		SNInventory sn = it.next(); 
	        		 String tnum = sn.getGoodNum();
	        		 String bnum = sn.getBranchNum();  
	        		  
	        		 if(StringUtill.isNull(pnum) || tnum.equals(pnum)){
	             		SNInventory snm = map.get(tnum);
	             		   
	             		if(null == snm){
	             			//logger.info(sn.getModelnum());
	             			map.put(tnum, sn); 
	             		}else { 
	             			//logger.info(snm.getModelnum());
	             			//logger.info(sn.getModelnum());
	             			snm.setModelnum(snm.getModelnum()+sn.getModelnum()); 
	        		 } 
	        		 }
	        	}
	           // logger.info(count);  
	        } 
	   
	        if(null != cob){  
	    	      
	        	Iterator<SNInventory> it = cob.iterator();
	        	
	        	while(it.hasNext()){
	        		SNInventory sn = it.next();  
	        		 String tnum = sn.getGoodNum();
	        		 String bnum = sn.getBranchNum();  
	        		   
	        		
	        			// count ++; 
	        			// logger.info(bnum);  
	        			
	        		 if(StringUtill.isNull(pnum) || tnum.equals(pnum)){      
	             		SNInventory snm = map.get(tnum);
	             		   
	             		if(null == snm){
	             			//logger.info(sn.getModelnum());
	             			map.put(tnum, sn); 
	             		}else { 
	             			//logger.info(snm.getModelnum());
	             			//logger.info(sn.getModelnum());
	             			snm.setBadnum(snm.getBadnum()+sn.getBadnum()); 
	        		 } 
	        	}
	        	}
	            //logger.info(count);  
	        }
	        
	        if(null != cos){  
	  	        
	        	Iterator<SNInventory> it = cos.iterator();
	        	//int count = 0 ; 
	        	while(it.hasNext()){ 
	        		 SNInventory sn = it.next();        
	        		 String tnum = sn.getGoodNum();       
	        		 String bnum = sn.getBranchNum();   
	        		//logger.info(bnum);       
	        		 if(StringUtill.isNull(pnum) || tnum.equals(pnum)){
	        			
	             		SNInventory snm = map.get(tnum);
	             		   
	             		if(null == snm){
	             			//logger.info(sn.getModelnum());
	             			map.put(tnum, sn); 
	             		}else {  
	             			//logger.info(snm.getModelnum());
	             			//logger.info(sn.getModelnum());
	             			snm.setSaleNum(snm.getSaleNum()+sn.getSaleNum());
	             			//snm.setBadnum(snm.getBadnum()+sn.getBadnum());
	             		}  
	        		 }
	        	}
	           // logger.info(count);  
	        }
	        
	          if(null != coin){  
	  	         
	        	Iterator<SNInventory> it = coin.iterator();
	        	//int count = 0 ;   
	        	while(it.hasNext()){   
	        		 SNInventory sn = it.next();        
	        		 String tnum = sn.getProduct().getEncoded();       
	        		 String bnum = BranchService.getMap().get(sn.getBranchid()).getEncoded();
	        		 //logger.info(bnum+"**"+tnum);           
	        		
	        		 if(StringUtill.isNull(pnum) || tnum.equals(pnum)){  
	             		SNInventory snm = map.get(tnum);
	             		    
	             		if(null == snm){
	             			//logger.info(sn.getModelnum());
	             			map.put(tnum, sn); 
	             		}else { 
	             			//logger.info(snm.getModelnum());
	             			//logger.info(sn.getModelnum()); 
	             			snm.setIncommonnum(snm.getIncommonnum()+sn.getIncommonnum());
	             			snm.setInmodelnum(snm.getInmodelnum()+sn.getInmodelnum());
	             		//	snm.setSaleNum(snm.getSaleNum()+sn.getSaleNum()); 
	             		}   
	        		 }
	        	} 
	           // logger.info(count);  
	        } 

	    	   }catch(Exception e){
	    		   logger.info(e);
	    	   } 
	         
	    	return map;
	    }
 
    public static List<SNInventory> getlist(User user ,String branch,String catogory,String starttime,String endtime){
    	
    	 
    	return null ;
    }
}
