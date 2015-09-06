package com.zhilibao.dao.impl;


import java.util.List; 
import java.util.Map;

import org.springframework.stereotype.Repository;
import com.zhilibao.base.LivesBaseDao;
import com.zhilibao.dao.TaxBasicMessageDao;
import com.zhilibao.model.Cars;
import com.zhilibao.model.TaxBasicMessage;
        
@Repository 
public class TaxBasicMessageDaoImpl extends LivesBaseDao<TaxBasicMessage> implements TaxBasicMessageDao {

	@Override
	public int save(TaxBasicMessage taxBasicMessage) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(long id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(TaxBasicMessage taxBasicMessage) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TaxBasicMessage getTaxBasicMessage(long id) {
		// TODO Auto-generated method stub
		return null;
	} 

	@Override  
	public List<TaxBasicMessage> getList(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return this.getList("getTaxBasicMessageList",params); 
		
		  
	} 

	@Override
	public int getCount(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return 0;
	}
 
	 
	 
   
}
