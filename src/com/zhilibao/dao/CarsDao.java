package com.zhilibao.dao;

import java.util.List;
import java.util.Map;
import com.zhilibao.model.Cars;
 
public interface CarsDao {

	public int save(Cars cars);
	
	public int delete(long id);
	
	public int update(Cars cars);
	 
	public Cars getCars(long id);
	
	public List<Cars> getlist();
}
