package com.zhilibao.service;

import com.zhilibao.base.BaseService;
import com.zhilibao.model.Cars;
import com.zhilibao.utill.QueryResult;

 
public interface CarsService extends BaseService<Cars> {
    
	public int save(Cars t); 
} 
