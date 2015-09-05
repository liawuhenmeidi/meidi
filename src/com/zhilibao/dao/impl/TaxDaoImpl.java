package com.zhilibao.dao.impl;


import java.util.List; 
 
import org.springframework.stereotype.Repository;

import com.zhilibao.base.LivesBaseDao;
import com.zhilibao.dao.CarsDao;
import com.zhilibao.dao.TaxDao;
import com.zhilibao.model.Cars;
    
@Repository
public class TaxDaoImpl extends LivesBaseDao<Cars> implements TaxDao {

	@Override
	public int save(Cars cars) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(long id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Cars cars) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Cars getCars(long id) {
		// TODO Auto-generated method stub
		return null;
	}
  
	

 
	 
}
