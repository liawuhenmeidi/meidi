package com.zhilibao.dao;

import java.util.List;
import java.util.Map;
import com.zhilibao.model.Cars;
import com.zhilibao.model.TaxBasicMessage;
 
public interface TaxBasicMessageDao {

	public int save(TaxBasicMessage taxBasicMessage);
	
	public int delete(long id); 
	
	public int update(TaxBasicMessage taxBasicMessage);
	  
	public TaxBasicMessage getTaxBasicMessage(long id);
	 
    public List<TaxBasicMessage> getList(Map<String, Object> params);
	 
	public int getCount(Map<String, Object> params);
	
}
