package inventory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import category.Category;
import category.CategoryManager;

import product.Product;
import product.ProductService;

import branch.Branch;
import branch.BranchManager;

import user.User;
import utill.NumbleUtill;
import utill.StringUtill;
  
public class InventoryAllManager {
	 // 苏宁订单刷新
	
	
	public static  Collection<InventoryAll> getMap(User user,String branch,String category,String product,String isSN){ 
		System.out.println("isSN"+isSN);  
		List<InventoryBranch> list = null ; 
		Collection<InventoryAll> clist  = null; 
		if(StringUtill.isNull(product)){ 
	    	if(!StringUtill.isNull(branch)){
	    		if(!NumbleUtill.isNumeric(branch)){
	    			Branch b = BranchManager.getLocatebyname(branch);
	    			branch = b.getId()+"";
	    		}  
	    	}
	    	 
	    	list = InventoryBranchManager.getCategoryid(user,branch, category);  
	    }else { 
	    	if(!NumbleUtill.isNumeric(product)){
				Product p = ProductService.gettypemap().get(product);
				product = p.getId()+"";
			} 
	    	
	    	
	    	
	    	if(!StringUtill.isNull(branch)){
	    		//System.out.println(branch);
	    		if(NumbleUtill.isNumeric(branch)){
	    			Branch b = BranchManager.getLocatebyid(branch);
	    			branch = b.getLocateName();
	    		}  
	    		 
	    		list = InventoryBranchManager.getCategory(user,branch, product);  
	    	}else { 
	    		list = InventoryBranchManager.getCategory(user,branch, product);
	    	}
	    }
		 
		if(StringUtill.isNull(category) && StringUtill.isNull(isSN)){ 
		    Map<Integer,InventoryAll> map  = new HashMap<Integer,InventoryAll>();  
			 
		    for(int i=0;i<list.size();i++){ 
		    	InventoryBranch inb = list.get(i);
		    	int categoryid = inb.getInventoryid();
		    	InventoryAll listp= map.get(categoryid);  
		    	if(listp == null){
		    		listp = new InventoryAll();
		    		Category c = CategoryManager.getCategory(categoryid+"");
		    		listp.setCategoryid(c.getId());  
		    		listp.setCateoryName(c.getName());  
		    		listp.setTypeid(inb.getTypeid());
		    		listp.setPapercount(inb.getPapercount());   
		    		listp.setRealcount(inb.getRealcount()); 
		    		listp.setIsquery(inb.isquery());
		    		listp.setOrderNUmSN(inb.getOrderNUmSN());
		    		listp.setActivetime(inb.getActivetime());
		    		listp.setProduct(inb.getProduct());
		    		//System.out.println(inb.getPapercount()+"***"+inb.getRealcount());
		    		map.put(categoryid, listp); 
		    	}else {  
		    		//System.out.println(inb.getPapercount()+"***"+inb.getRealcount());
		    		listp.setPapercount(listp.getPapercount()+inb.getPapercount());
		    		listp.setRealcount(listp.getRealcount()+inb.getRealcount());
		    		listp.setIsquery(listp.getIsquery()&&inb.isquery());
		    	}
		    } 
			  
		    clist = map.values(); 
		}else if(!StringUtill.isNull(category) || !StringUtill.isNull(isSN)){
	        Map<String,InventoryAll> map  = new HashMap<String,InventoryAll>();   
			 
		    for(int i=0;i<list.size();i++){  
		    	InventoryBranch inb = list.get(i);
		    	int categoryid = inb.getInventoryid();
		    	String type = inb.getType();  
		    	//int statues = inb.getTypeStatues(); 
		    	//String key = type+"_"+statues;
		    	String key = type;
		    	if(StringUtill.isNull(isSN)){
		    		key = type;
		    	}  
		    	InventoryAll listp= map.get(key );  
		    	if(listp == null){ 
		    		listp = new InventoryAll();   
		    		Category c = CategoryManager.getCategory(categoryid+"");
		    		listp.setCategoryid(c.getId()); 
		    	    listp.setType(type);  
		    	    listp.setTypeid(inb.getTypeid());
		    		listp.setCateoryName(c.getName());  
		    		listp.setPapercount(inb.getPapercount()); 
		    		listp.setRealcount(inb.getRealcount());
		    		listp.setIsquery(inb.isquery()); 
		    		listp.setOrderNUmSN(inb.getOrderNUmSN());
		    		listp.setActivetime(inb.getActivetime());
		    		listp.setProduct(inb.getProduct()); 
		    		listp.setTypestatues(inb.getTypeStatues()); 
		    		if(!StringUtill.isNull(branch)){
			    		listp.setTime(inb.getQuerymonth());
			    	} 
		    		map.put(key , listp); 
		    	}else {   
		    		listp.setOrderNUmSN(listp.getOrderNUmSN()+"_"+inb.getOrderNUmSN()); 
		    		listp.setTypestatuesName(listp.getTypestatuesName()+"_"+inb.getTypestatuesName()); 
		    		listp.setPapercount(listp.getPapercount()+inb.getPapercount());
		    		listp.setRealcount(listp.getRealcount()+inb.getRealcount());
		    		listp.setIsquery(listp.getIsquery()&&inb.isquery());
		    	} 
		    	 
		    	 
		    	//System.out.println(StringUtill.GetJson(listp));
		    }  
		    clist = map.values();
		}      
		  
		return clist ; 
	}   
	
	public static  Map<String,List<InventoryAll>> getMapSN(User user,String branch,String category,String product,String isSN){ 
		Map<String,List<InventoryAll>> map = new HashMap<String,List<InventoryAll>>();
		 
		Collection<InventoryAll> c = InventoryAllManager.getMap(user,branch,
				category, product,isSN);   
		  
		if(!c.isEmpty()){
			Iterator<InventoryAll> it = c.iterator();
			while(it.hasNext()){
				InventoryAll ia = it.next();
				List<InventoryAll> list = map.get(ia.getOrderNUmSN());
			    if(null == list){
			    	list = new ArrayList<InventoryAll>();
			    	map.put(ia.getOrderNUmSN(), list); 
			    }
			    if(ia.getPapercount() != 0 ){
			    	 list.add(ia); 
			    } 
			   
			}
		}
		
		return map ;
		
	}  
	
	
	
}
