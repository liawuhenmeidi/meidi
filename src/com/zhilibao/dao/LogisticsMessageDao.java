package com.zhilibao.dao;

import java.util.List;
import java.util.Map;
import com.zhilibao.model.Cars;
import com.zhilibao.model.LogisticsMessage;
 
public interface LogisticsMessageDao {
  
	public int save(LogisticsMessage logisticsMessage);
	
	public int delete(long id);
	
	public int update(Cars cars);
	 
	public Cars getLogisticsMessage(long id);
	  
}
