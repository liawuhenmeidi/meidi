package com.zhilibao.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 
import org.springframework.stereotype.Service;
import com.zhilibao.dao.CarsDao;
import com.zhilibao.model.Cars;
import com.zhilibao.service.CarsService;
import com.zhilibao.utill.QueryResult;


@Service 
public class CarsServiceImpl implements CarsService {
 
	private static final Log log = LogFactory.getLog(CarsServiceImpl.class);
	 
    
	@Resource   
	private CarsDao carsDao;


	@Override
	public int delete(long id) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int update(Cars t) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Cars get(long id) {
		// TODO Auto-generated method stub
		return null;
	}

      
	@Override  
	public QueryResult<Cars> getList(Map<String, Object> params) {
		int total = carsDao.getCount(params); 
		List<Cars> result = carsDao.getList(params);
		QueryResult<Cars> queryResult = new QueryResult<Cars>();
		queryResult.setRows(result);
		queryResult.setTotal(total);
		return queryResult;
	}


	@Override
	public int save(Cars t) {
		// TODO Auto-generated method stub
		return 0;
	}
	


	

	
}
